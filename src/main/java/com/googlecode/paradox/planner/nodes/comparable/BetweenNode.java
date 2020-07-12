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
import com.googlecode.paradox.planner.nodes.FieldNode;
import com.googlecode.paradox.rowset.ValuesComparator;

/**
 * Stores the between node.
 *
 * @version 1.6
 * @since 1.1
 */
public final class BetweenNode extends AbstractComparableNode {

    /**
     * The field node.
     */
    private final FieldNode first;

    /**
     * Create a new instance.
     *
     * @param connection the Paradox connection.
     * @param field      the middle node.
     * @param first      the first node.
     * @param last       the last node.
     */
    public BetweenNode(final ParadoxConnection connection, final FieldNode field, final FieldNode first,
                       final FieldNode last) {
        super(connection, "BETWEEN", field, last);
        this.first = first;
    }

    public FieldNode getFirst() {
        return first;
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public String toString() {
        return this.field + " BETWEEN " + first + " AND " + last;
    }

    @Override
    public boolean evaluate(final Object[] row, final Object[] parameters) {
        final Object value1 = getValue(row, field, parameters);
        final Object value2 = getValue(row, first, parameters);
        final Object value3 = getValue(row, last, parameters);

        return ValuesComparator.compare(value1, value2, i -> i >= 0) &&
                ValuesComparator.compare(value1, value3, i -> i <= 0);
    }
}
