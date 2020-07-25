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
package com.googlecode.paradox.function.numeric;

import com.googlecode.paradox.ParadoxConnection;
import com.googlecode.paradox.planner.nodes.FieldNode;
import com.googlecode.paradox.results.Column;
import com.googlecode.paradox.results.ParadoxType;
import com.googlecode.paradox.rowset.ValuesConverter;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * The SQL NUMERIC function.
 *
 * @version 1.3
 * @since 1.6.0
 */
public class NumericFunction extends AbstractNumericFunction {

    /**
     * The function name.
     */
    public static final String NAME = "NUMERIC";

    @Override
    public String getRemarks() {
        return "Converts the value to numeric type.";
    }

    @Override
    public Column[] getColumns() {
        return new Column[]{
                new Column(null, ParadoxType.NUMBER, "A number value.", 0, true,
                        DatabaseMetaData.functionColumnResult),
                new Column("value", ParadoxType.VARCHAR,
                        "A value to convert.", 1, true, DatabaseMetaData.functionColumnIn)
        };
    }

    @Override
    public int getParameterCount() {
        return 1;
    }

    @Override
    public Object execute(final ParadoxConnection connection, final Object[] values, final ParadoxType[] types,
                          final FieldNode[] fields) throws SQLException {
        return ValuesConverter.getDouble(values[0]);
    }
}
