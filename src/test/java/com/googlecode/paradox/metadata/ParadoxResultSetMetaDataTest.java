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
package com.googlecode.paradox.metadata;

import com.googlecode.paradox.Driver;
import com.googlecode.paradox.ParadoxConnection;
import com.googlecode.paradox.metadata.paradox.ParadoxField;
import com.googlecode.paradox.metadata.paradox.ParadoxTable;
import com.googlecode.paradox.results.Column;
import com.googlecode.paradox.results.ParadoxType;
import com.googlecode.paradox.utils.Constants;
import com.googlecode.paradox.utils.Utils;
import org.junit.*;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;

/**
 * Unit test for {@link ResultSetMetaData} class.
 *
 * @version 1.1
 * @since 1.3
 */
public class ParadoxResultSetMetaDataTest {

    /**
     * The connection string used in this tests.
     */
    private static final String CONNECTION_STRING = "jdbc:paradox:target/test-classes/";

    /**
     * The database connection.
     */
    private ParadoxConnection conn;

    /**
     * Register the database driver.
     *
     * @throws Exception in case of failures.
     */
    @BeforeClass
    public static void setUp() throws Exception {
        Class.forName(Driver.class.getName());
    }

    /**
     * Close the test connection.
     *
     * @throws Exception in case of failures.
     */
    @After
    public void closeConnection() throws Exception {
        if (this.conn != null) {
            this.conn.close();
        }
    }

    /**
     * Connect to the test database.
     *
     * @throws Exception in case of failures.
     */
    @Before
    public void connect() throws Exception {
        this.conn = (ParadoxConnection) DriverManager
                .getConnection(ParadoxResultSetMetaDataTest.CONNECTION_STRING + "db");
    }

    /**
     * Test for column metadata.
     *
     * @throws SQLException in case of errors.
     */
    @Test
    public void testColumn() throws SQLException {
        final Column column = new Column(new ParadoxField(ParadoxType.INTEGER));
        column.getField().setTable(new ParadoxTable(null, "NAME", conn.getConnectionInfo()));
        column.getField().setSize(255);
        column.setName("name");
        column.setIndex(1);
        column.setPrecision(2);
        column.getField().setTable(new ParadoxTable(null, "table", conn.getConnectionInfo()));
        final ResultSetMetaData metaData = new ResultSetMetaData(this.conn.getConnectionInfo(),
                Collections.singletonList(column));
        Assert.assertEquals("Testing for column size.", 1, metaData.getColumnCount());
        Assert.assertEquals("Testing for class name.", ParadoxType.INTEGER.getJavaClass().getName(),
                metaData.getColumnClassName(1));
        Assert.assertEquals("Testing for catalog name.", conn.getCatalog(), metaData.getCatalogName(1));
        Assert.assertEquals("Testing for schema name.", "db", metaData.getSchemaName(1));
        Assert.assertEquals("Testing for column display size.", Constants.MAX_STRING_SIZE,
                metaData.getColumnDisplaySize(1));
        Assert.assertEquals("Testing for column label.", "name", metaData.getColumnLabel(1));
        Assert.assertEquals("Testing for column name.", "name", metaData.getColumnName(1));
        Assert.assertEquals("Testing for column type.", ParadoxType.INTEGER.getSQLType(), metaData.getColumnType(1));
        Assert.assertEquals("Testing for column type name.", ParadoxType.INTEGER.getName(),
                metaData.getColumnTypeName(1));
        Assert.assertEquals("Testing for column precision.", 2, metaData.getPrecision(1));
        Assert.assertEquals("Testing for column scale.", 2, metaData.getScale(1));
        Assert.assertEquals("Testing for table name.", "table", metaData.getTableName(1));
        Assert.assertFalse("Testing for auto increment value.", metaData.isAutoIncrement(1));
        Assert.assertFalse("Testing for case sensitivity.", metaData.isCaseSensitive(1));
        Assert.assertFalse("Testing for currency.", metaData.isCurrency(1));
        Assert.assertFalse("Testing for writable.", metaData.isWritable(1));
        Assert.assertFalse("Testing for definitely writable.", metaData.isDefinitelyWritable(1));
        Assert.assertTrue("Testing for read only.", metaData.isReadOnly(1));
        Assert.assertTrue("Testing for searchable.", metaData.isSearchable(1));
        Assert.assertTrue("Testing for sign.", metaData.isSigned(1));

        Assert.assertEquals("Testing for nullable.", java.sql.ResultSetMetaData.columnNullable, metaData.isNullable(1));
    }

    /**
     * Test for instance.
     */
    @Test
    public void testInstance() {
        final ResultSetMetaData metaData = new ResultSetMetaData(this.conn.getConnectionInfo(),
                Collections.emptyList());
        Assert.assertEquals("Testing for column size.", 0, metaData.getColumnCount());
    }

    /**
     * Test for invalid column with high value.
     *
     * @throws SQLException in case of errors.
     */
    @Test(expected = SQLException.class)
    public void testInvalidColumnHighValue() throws SQLException {
        final ResultSetMetaData metaData = new ResultSetMetaData(this.conn.getConnectionInfo(),
                Collections.emptyList());
        metaData.getColumnName(5);
    }

    /**
     * Test for invalid column with low value.
     *
     * @throws SQLException in case of errors.
     */
    @Test(expected = SQLException.class)
    public void testInvalidColumnLowValue() throws SQLException {
        final ResultSetMetaData metaData = new ResultSetMetaData(this.conn.getConnectionInfo(),
                Collections.emptyList());
        metaData.getColumnName(0);
    }

    /**
     * Test for the {@link Utils#isWrapperFor(java.sql.Wrapper, Class)}.
     */
    @Test
    public void testIsWrapFor() {
        final ResultSetMetaData metaData = new ResultSetMetaData(this.conn.getConnectionInfo(),
                Collections.emptyList());
        Assert.assertTrue("Invalid value.", metaData.isWrapperFor(ResultSetMetaData.class));
    }

    /**
     * Test for unwrap.
     *
     * @throws Exception in case of failures.
     */
    @Test
    public void testUnwrap() throws Exception {
        final ResultSetMetaData metaData = new ResultSetMetaData(this.conn.getConnectionInfo(),
                Collections.emptyList());
        Assert.assertNotNull("Invalid value.", metaData.unwrap(ResultSetMetaData.class));
    }
}
