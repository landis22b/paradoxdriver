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
package com.googlecode.paradox.function.general;

import com.googlecode.paradox.ParadoxConnection;
import com.googlecode.paradox.planner.nodes.FieldNode;
import com.googlecode.paradox.results.Column;
import com.googlecode.paradox.results.ParadoxType;
import com.googlecode.paradox.rowset.ValuesConverter;

import java.sql.DatabaseMetaData;

/**
 * The SQL BINARY functions.
 *
 * @version 1.1
 * @since 1.6.0
 */
public class BinaryFunction extends AbstractGeneralFunction {

    /**
     * The function name.
     */
    public static final String NAME = "BINARY";

    @Override
    public String remarks() {
        return "Returns the square root of a number.";
    }

    @Override
    public Column[] getColumns() {
        return new Column[]{
                new Column(null, ParadoxType.BLOB, "The BLOB value.", 0, false, DatabaseMetaData.functionColumnResult),
                new Column("value", ParadoxType.NUMBER, "A value to convert.", 1, true,
                        DatabaseMetaData.functionColumnIn)
        };
    }

    @Override
    public ParadoxType fieldType() {
        return ParadoxType.BLOB;
    }

    @Override
    public int parameterCount() {
        return 1;
    }

    @Override
    public Object execute(final ParadoxConnection connection, final Object[] values, final ParadoxType[] types,
                          final FieldNode[] fields) {
        return ValuesConverter.getByteArray(values[0]);
    }
}
