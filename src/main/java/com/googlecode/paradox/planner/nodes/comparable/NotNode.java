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
package com.googlecode.paradox.planner.nodes.comparable;

import com.googlecode.paradox.ParadoxConnection;
import com.googlecode.paradox.parser.ScannerPosition;
import com.googlecode.paradox.parser.nodes.SQLNode;
import com.googlecode.paradox.planner.nodes.FieldNode;
import com.googlecode.paradox.results.Column;
import com.googlecode.paradox.results.ParadoxType;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * Stores the not node.
 *
 * @version 1.10
 * @since 1.1
 */
public final class NotNode extends AbstractComparableNode {

    /**
     * Create a new instance.
     *
     * @param position the current Scanner position.
     */
    public NotNode(final ScannerPosition position) {
        super("NOT", null, null, position);
    }

    @Override
    public Set<FieldNode> getClauseFields() {
        final Set<FieldNode> nodes = super.getClauseFields();
        for (final SQLNode node : children) {
            if (node instanceof AbstractComparableNode) {
                nodes.addAll(node.getClauseFields());
            }
        }

        return nodes;
    }

    @Override
    public boolean evaluate(final ParadoxConnection connection, final Object[] row, final Object[] parameters,
                            final ParadoxType[] parameterTypes, final List<Column> columnsLoaded) throws SQLException {
        if (!children.isEmpty() && children.get(0) instanceof AbstractComparableNode) {
            return !((AbstractComparableNode) children.get(0)).evaluate(connection, row, parameters,
                    parameterTypes, columnsLoaded);
        }

        // Should not never happens.
        return false;
    }

    @Override
    public String toString() {
        if (!children.isEmpty()) {
            return String.format("%s %s", name, children.get(0));
        }

        return name;
    }
}
