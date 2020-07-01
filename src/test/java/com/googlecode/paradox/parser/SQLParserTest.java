/*
 * Copyright (C) 2009 Leonardo Alves da Costa
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any
 * later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details. You should have received a copy of the GNU General Public License along with this
 * program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.googlecode.paradox.parser;

import com.googlecode.paradox.Driver;
import com.googlecode.paradox.ParadoxConnection;
import com.googlecode.paradox.parser.nodes.SQLNode;
import com.googlecode.paradox.parser.nodes.SelectNode;
import com.googlecode.paradox.parser.nodes.StatementNode;
import com.googlecode.paradox.parser.nodes.values.AsteriskNode;
import com.googlecode.paradox.parser.nodes.values.CharacterNode;
import com.googlecode.paradox.parser.nodes.values.NumericNode;
import com.googlecode.paradox.planner.nodes.FieldNode;
import com.googlecode.paradox.planner.nodes.value.NullNode;
import com.googlecode.paradox.planner.nodes.comparable.EqualsNode;
import com.googlecode.paradox.planner.nodes.comparable.IsNotNullNode;
import com.googlecode.paradox.planner.nodes.comparable.IsNullNode;
import com.googlecode.paradox.planner.nodes.comparable.NotEqualsNode;
import com.googlecode.paradox.planner.nodes.join.ANDNode;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * Unit test for {@link SQLParser}.
 *
 * @author Leonardo Alves da Costa
 * @version 1.3
 * @since 1.0
 */
public class SQLParserTest {

    /**
     * The connection string used in this tests.
     */
    public static final String CONNECTION_STRING = "jdbc:paradox:target/test-classes/";

    /**
     * The database connection.
     */
    private static ParadoxConnection conn;

    /**
     * Register the database driver.
     *
     * @throws SQLException in case of failures.
     */
    @BeforeClass
    public static void setUp() throws SQLException {
        new Driver();
        conn = (ParadoxConnection) DriverManager.getConnection(CONNECTION_STRING + "db");
    }

    /**
     * Close the database connection.
     *
     * @throws SQLException in case of failures.
     */
    @AfterClass
    public static void tearDown() throws SQLException {
        conn.close();
    }

    /**
     * Test for is null expressions.
     *
     * @throws SQLException in case of failures.
     */
    @Test
    public void testIsNull() throws SQLException {
        final SQLParser parser = new SQLParser(conn, "SELECT A FROM db.B WHERE A is NULL");
        final List<StatementNode> list = parser.parse();
        final SQLNode tree = list.get(0);

        final SelectNode select = (SelectNode) tree;

        Assert.assertTrue("Invalid condition value.", select.getCondition() instanceof IsNullNode);
        final IsNullNode node = (IsNullNode) select.getCondition();
        Assert.assertEquals("Invalid field name.", "A", node.getField().getName());
        Assert.assertNull("Invalid field value.", node.getLast());
    }

    /**
     * Test for is not null expressions.
     *
     * @throws SQLException in case of failures.
     */
    @Test
    public void testIsNotNull() throws SQLException {
        final SQLParser parser = new SQLParser(conn, "SELECT A FROM db.B WHERE A is not NULL");
        final List<StatementNode> list = parser.parse();
        final SQLNode tree = list.get(0);

        final SelectNode select = (SelectNode) tree;

        Assert.assertTrue("Invalid condition value.", select.getCondition() instanceof IsNotNullNode);
        final IsNotNullNode node = (IsNotNullNode) select.getCondition();
        Assert.assertEquals("Invalid field name.", "A", node.getField().getName());
        Assert.assertNull("Invalid field value.", node.getLast());
    }

    /**
     * Test for not as a value.
     *
     * @throws SQLException in case of failures.
     */
    @Test
    public void testNullAsValue() throws SQLException {
        final SQLParser parser = new SQLParser(conn, "SELECT A FROM db.B WHERE A = NULL");
        final List<StatementNode> list = parser.parse();
        final SQLNode tree = list.get(0);

        final SelectNode select = (SelectNode) tree;

        Assert.assertTrue("Invalid condition value.", select.getCondition() instanceof EqualsNode);
        final EqualsNode node = (EqualsNode) select.getCondition();
        Assert.assertEquals("Invalid field name.", "A", node.getField().getName());
        Assert.assertTrue("Invalid field value.", node.getLast() instanceof NullNode);
    }

    /**
     * Test select with alias in fields.
     *
     * @throws SQLException in case of failures.
     */
    @Test
    public void testSelectWithAlias() throws SQLException {
        final SQLParser parser = new SQLParser(conn, "SELECT t.* FROM table t");
        final List<StatementNode> list = parser.parse();
        final SQLNode tree = list.get(0);

        final SelectNode select = (SelectNode) tree;

        Assert.assertEquals("Invalid node size.", 1, select.getFields().size());
        final SQLNode node = select.getFields().get(0);

        Assert.assertTrue("Invalid node type.", node instanceof AsteriskNode);
        final AsteriskNode asteriskNode = (AsteriskNode) node;

        Assert.assertEquals("Invalid value.", "t", asteriskNode.getTableName());
    }

    /**
     * Test for schema name.
     *
     * @throws SQLException in case of failures.
     */
    @Test
    public void testSchemaName() throws SQLException {
        final SQLParser parser = new SQLParser(conn, "SELECT t.* FROM db.table t");
        final List<StatementNode> list = parser.parse();
        final SQLNode tree = list.get(0);

        final SelectNode select = (SelectNode) tree;

        Assert.assertEquals("Invalid node size.", 1, select.getFields().size());
        final SQLNode node = select.getFields().get(0);

        Assert.assertTrue("Invalid node type.", node instanceof AsteriskNode);
        final AsteriskNode asteriskNode = (AsteriskNode) node;

        Assert.assertEquals("Invalid value.", "t", asteriskNode.getTableName());
    }

    /**
     * Test for column values.
     *
     * @throws Exception in case of failures.
     */
    @Test
    public void testColumnValue() throws Exception {
        final SQLParser parser = new SQLParser(conn, "SELECT 'test', 123 as number FROM client");
        final List<StatementNode> list = parser.parse();
        final SQLNode tree = list.get(0);

        Assert.assertTrue("Invalid node type.", tree instanceof SelectNode);

        final SelectNode select = (SelectNode) tree;

        Assert.assertEquals("Invalid node size.", 2, select.getFields().size());
        Assert.assertTrue("Invalid node type.", select.getFields().get(0) instanceof CharacterNode);
        Assert.assertEquals("Invalid node name.", "test", select.getFields().get(0).getName());
        Assert.assertTrue("Invalid node type.", select.getFields().get(1) instanceof NumericNode);
        Assert.assertEquals("Invalid node name.", "123", select.getFields().get(1).getName());
        Assert.assertEquals("Invalid node alias.", "number", select.getFields().get(1).getAlias());

        Assert.assertEquals("Invalid node size.", 1, select.getTables().size());
        Assert.assertEquals("Invalid node name.", "client", select.getTables().get(0).getName());
    }

    /**
     * Test for join token.
     *
     * @throws Exception in case of failures.
     */
    @Test
    public void testJoin() throws Exception {
        final SQLParser parser = new SQLParser(conn,
                "SELECT * FROM client c inner join test t on test_id = id and a <> b left join table on a = b");
        final List<StatementNode> list = parser.parse();
        final SQLNode tree = list.get(0);

        Assert.assertTrue("Invalid node type.", tree instanceof SelectNode);

        final SelectNode select = (SelectNode) tree;

        Assert.assertEquals("Invalid node size.", 1, select.getFields().size());
        Assert.assertEquals("Invalid node name.", TokenType.ASTERISK.name(), select.getFields().get(0).getName());

        Assert.assertEquals("Invalid node size.", 3, select.getTables().size());
        Assert.assertEquals("Invalid node name.", "client", select.getTables().get(0).getName());
    }

    /**
     * Test for SELECT token.
     *
     * @throws Exception in case of failures.
     */
    @Test
    public void testSelect() throws Exception {
        final SQLParser parser = new SQLParser(conn, "SELECT * FROM client");
        final List<StatementNode> list = parser.parse();
        final SQLNode tree = list.get(0);

        Assert.assertTrue("Invalid node type.", tree instanceof SelectNode);

        final SelectNode select = (SelectNode) tree;

        Assert.assertEquals("Invalid node size.", 1, select.getFields().size());
        Assert.assertEquals("Invalid node name.", TokenType.ASTERISK.name(), select.getFields().get(0).getName());

        Assert.assertEquals("Invalid node size.", 1, select.getTables().size());
        Assert.assertEquals("Invalid node name.", "client", select.getTables().get(0).getName());
    }

    /**
     * Test for tables.
     *
     * @throws Exception in case of failures.
     */
    @Test
    public void testTable() throws Exception {
        final SQLParser parser = new SQLParser(conn, "SELECT * FROM \"client.db\"");
        final List<StatementNode> list = parser.parse();
        final SQLNode tree = list.get(0);

        Assert.assertTrue("Invalid node type.", tree instanceof SelectNode);

        final SelectNode select = (SelectNode) tree;

        Assert.assertEquals("Invalid node size.", 1, select.getFields().size());
        Assert.assertEquals("Invalid node name.", TokenType.ASTERISK.name(), select.getFields().get(0).getName());

        Assert.assertEquals("Invalid node size.", 1, select.getTables().size());
        Assert.assertEquals("Invalid node name.", "client", select.getTables().get(0).getName());
    }

    /**
     * Test a SELECT with two tables.
     *
     * @throws Exception in case of failures.
     */
    @Test
    public void testTwoTable() throws Exception {
        final SQLParser parser = new SQLParser(conn, "select a.CODE as cod, state.NAME name FROM client, state");
        final List<StatementNode> list = parser.parse();
        final SQLNode tree = list.get(0);

        Assert.assertTrue("Invalid node type.", tree instanceof SelectNode);

        final SelectNode select = (SelectNode) tree;

        Assert.assertEquals("Invalid node size.", 2, select.getFields().size());
        Assert.assertEquals("Invalid node name.", "CODE", select.getFields().get(0).getName());
        Assert.assertEquals("Invalid node alias.", "cod", select.getFields().get(0).getAlias());

        Assert.assertEquals("Invalid node name.", "state", ((FieldNode) select.getFields().get(1)).getTableName());
        Assert.assertEquals("Invalid node name.", "NAME", select.getFields().get(1).getName());
        Assert.assertEquals("Invalid node alias.", "name", select.getFields().get(1).getAlias());

        Assert.assertEquals("Invalid node size.", 2, select.getTables().size());
        Assert.assertEquals("Invalid node name.", "client", select.getTables().get(0).getName());
        Assert.assertEquals("Invalid node alias.", "state", select.getTables().get(1).getName());
    }

    /**
     * Test tables with alias.
     *
     * @throws Exception in case of failures.
     */
    @Test
    public void testTwoTableWithAlias() throws Exception {
        final SQLParser parser = new SQLParser(conn, "select *, name FROM client as cli, state STATE");
        final List<StatementNode> list = parser.parse();
        final SQLNode tree = list.get(0);

        Assert.assertTrue("Invalid node type.", tree instanceof SelectNode);

        final SelectNode select = (SelectNode) tree;

        Assert.assertEquals("Invalid node size.", 2, select.getFields().size());
        Assert.assertEquals("Invalid node size.", 2, select.getTables().size());

        Assert.assertEquals("Invalid node name.", TokenType.ASTERISK.name(), select.getFields().get(0).getName());

        Assert.assertEquals("Invalid node name.", "client", select.getTables().get(0).getName());
        Assert.assertEquals("Invalid node alias.", "cli", select.getTables().get(0).getAlias());
        Assert.assertEquals("Invalid node name.", "state", select.getTables().get(1).getName());
        Assert.assertEquals("Invalid node alias.", "STATE", select.getTables().get(1).getAlias());
    }

    /**
     * Test for where token.
     *
     * @throws Exception in case of failures.
     */
    @Test
    public void testWhere() throws Exception {
        final SQLParser parser = new SQLParser(conn, "SELECT * FROM client as test WHERE a = b and c <> t");
        final List<StatementNode> list = parser.parse();
        final SQLNode tree = list.get(0);

        Assert.assertTrue("Invalid node type.", tree instanceof SelectNode);

        final SelectNode select = (SelectNode) tree;

        Assert.assertEquals("Invalid node size.", 1, select.getFields().size());
        Assert.assertEquals("Invalid node name.", TokenType.ASTERISK.name(), select.getFields().get(0).getName());

        Assert.assertEquals("Invalid node size.", 1, select.getTables().size());
        Assert.assertEquals("Invalid node name.", "client", select.getTables().get(0).getName());
        Assert.assertEquals("Invalid node alias.", "test", select.getTables().get(0).getAlias());

        Assert.assertNotNull("Invalid node value.", select.getCondition());
        Assert.assertTrue("Invalid node type.", select.getCondition() instanceof ANDNode);

        ANDNode node = ((ANDNode) select.getCondition());
        Assert.assertEquals("Invalid node size.", 2, node.getChildhood().size());
        Assert.assertTrue("Invalid node type.", node.getChildhood().get(0) instanceof EqualsNode);
        Assert.assertTrue("Invalid node type.", node.getChildhood().get(1) instanceof NotEqualsNode);
        Assert.assertEquals("Invalid node name.", "a",
                ((EqualsNode) node.getChildhood().get(0)).getField().getName());
        Assert.assertEquals("Invalid node name.", "b",
                ((EqualsNode) node.getChildhood().get(0)).getLast().getName());
        Assert.assertEquals("Invalid node name.", "c",
                ((NotEqualsNode) node.getChildhood().get(1)).getField().getName());
        Assert.assertEquals("Invalid node name.", "t",
                ((NotEqualsNode) node.getChildhood().get(1)).getLast().getName());
    }

    /**
     * Test a where with alias.
     *
     * @throws Exception in case of failures.
     */
    @Test
    public void testWhereWithAlias() throws Exception {
        final SQLParser parser = new SQLParser(conn, "SELECT * FROM client as test WHERE test.a = c.b");
        final List<StatementNode> list = parser.parse();
        final SQLNode tree = list.get(0);

        Assert.assertTrue("Invalid node type.", tree instanceof SelectNode);

        final SelectNode select = (SelectNode) tree;

        Assert.assertEquals("Invalid node size.", 1, select.getFields().size());
        Assert.assertEquals("Invalid node name.", TokenType.ASTERISK.name(), select.getFields().get(0).getName());

        Assert.assertEquals("Invalid node size.", 1, select.getTables().size());
        Assert.assertEquals("Invalid node name.", "client", select.getTables().get(0).getName());
        Assert.assertEquals("Invalid node alias.", "test", select.getTables().get(0).getAlias());

        Assert.assertNotNull("Invalid node value.", select.getCondition());
        Assert.assertTrue("Invalid node type.", select.getCondition() instanceof EqualsNode);

        EqualsNode node = ((EqualsNode) select.getCondition());
        Assert.assertEquals("Invalid node table name.", "test", node.getField().getTableName());
        Assert.assertEquals("Invalid node name.", "a", node.getField().getName());
        Assert.assertEquals("Invalid node table name.", "c", node.getLast().getTableName());
        Assert.assertEquals("Invalid node name.", "b", node.getLast().getName());
    }
}
