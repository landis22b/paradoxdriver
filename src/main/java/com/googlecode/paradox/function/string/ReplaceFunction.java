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
package com.googlecode.paradox.function.string;

import com.googlecode.paradox.ParadoxConnection;
import com.googlecode.paradox.function.FunctionType;
import com.googlecode.paradox.function.AbstractFunction;
import com.googlecode.paradox.planner.nodes.FieldNode;
import com.googlecode.paradox.results.Column;
import com.googlecode.paradox.results.ParadoxType;
import com.googlecode.paradox.utils.Constants;

import java.sql.DatabaseMetaData;

/**
 * The SQL REPLACE function.
 *
 * @version 1.2
 * @since 1.6.0
 */
@SuppressWarnings("java:S109")
public class ReplaceFunction extends AbstractFunction {

    /**
     * The function name.
     */
    public static final String NAME = "REPLACE";

    @Override
    public String remarks() {
        return "Replaces all occurrences of a substring within a string, with a new substring.";
    }

    @Override
    public Column[] getColumns() {
        return new Column[]{
                new Column(null, ParadoxType.VARCHAR, 0, Constants.MAX_STRING_SIZE,
                        "The string or replaced.", 0, true, DatabaseMetaData.functionColumnResult),
                new Column("value", ParadoxType.VARCHAR, 0, Constants.MAX_STRING_SIZE,
                        "The original string.", 1, true, DatabaseMetaData.functionColumnIn),
                new Column("old_string", ParadoxType.VARCHAR, 0, Constants.MAX_STRING_SIZE,
                        "The string to be replaced.", 2, false, DatabaseMetaData.functionColumnIn),
                new Column("new_string", ParadoxType.VARCHAR, 0, Constants.MAX_STRING_SIZE,
                        "The new replacement string..", 3, false, DatabaseMetaData.functionColumnIn)
        };
    }

    @Override
    public FunctionType type() {
        return FunctionType.STRING;
    }

    @Override
    public ParadoxType fieldType() {
        return ParadoxType.VARCHAR;
    }

    @Override
    public int parameterCount() {
        return 3;
    }

    @Override
    public Object execute(final ParadoxConnection connection, final Object[] values, final ParadoxType[] types,
                          final FieldNode[] fields) {
        if (values[0] == null || values[1] == null || values[2] == null) {
            return null;
        }

        return values[0].toString().replace(values[1].toString(), values[2].toString());
    }
}
