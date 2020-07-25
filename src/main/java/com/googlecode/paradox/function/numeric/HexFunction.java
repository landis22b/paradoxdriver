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

import java.math.BigDecimal;
import java.sql.DatabaseMetaData;

/**
 * The SQL HEX functions.
 *
 * @version 1.1
 * @since 1.6.0
 */
public class HexFunction extends AbstractNumericFunction {

    /**
     * The function name.
     */
    public static final String NAME = "HEX";

    @Override
    public String getRemarks() {
        return "Returns a hexadecimal representation of a number, as a string value..";
    }

    @Override
    public Column[] getColumns() {
        return new Column[]{
                new Column(null, ParadoxType.VARCHAR,
                        "The the hexadecimal representation of a number.", 0, false,
                        DatabaseMetaData.functionColumnResult),
                new Column("number", ParadoxType.LONG, "A numeric value.", 1,
                        false, DatabaseMetaData.functionColumnIn)
        };
    }

    @Override
    public ParadoxType getFieldType() {
        return ParadoxType.VARCHAR;
    }

    @Override
    public int getParameterCount() {
        return 1;
    }

    @Override
    public Object execute(final ParadoxConnection connection, final Object[] values, final ParadoxType[] types,
                          final FieldNode[] fields) {
        final BigDecimal value = ValuesConverter.getBigDecimal(values[0]);
        if (value == null) {
            return null;
        }

        return value.toBigInteger().toString(0x10);
    }
}
