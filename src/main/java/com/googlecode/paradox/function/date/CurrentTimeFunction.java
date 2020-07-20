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
package com.googlecode.paradox.function.date;

import com.googlecode.paradox.ParadoxConnection;
import com.googlecode.paradox.exceptions.ParadoxSyntaxErrorException;
import com.googlecode.paradox.function.IFunction;
import com.googlecode.paradox.parser.nodes.SQLNode;
import com.googlecode.paradox.rowset.ValuesConverter;

import java.sql.SQLException;
import java.sql.Time;
import java.sql.Types;
import java.util.List;
import java.util.TimeZone;

/**
 * The SQL CURRENT_TIME function.
 *
 * @version 1.2
 * @since 1.6.0
 */
public class CurrentTimeFunction implements IFunction {

    /**
     * The function name.
     */
    public static final String NAME = "CURRENT_TIME";

    @Override
    public int sqlType() {
        return Types.TIME;
    }

    @Override
    public int parameterCount() {
        return 0;
    }

    @Override
    public boolean isAllowAlias() {
        return true;
    }

    @Override
    public boolean isVariableParameters() {
        return true;
    }

    @Override
    public Object execute(final ParadoxConnection connection, final Object[] values, final int[] types)
            throws SQLException {
        if (types.length == 1) {
            final int value = ValuesConverter.getPositiveInteger(values[0]);
            if (value < 0x00 || value > 0x06) {
                throw new ParadoxSyntaxErrorException(ParadoxSyntaxErrorException.Error.INVALID_PARAMETER_VALUE,
                        values[0]);
            }
        }

        long time = System.currentTimeMillis();
        return new Time(time + connection.getTimeZone().getOffset(time) - TimeZone.getDefault().getOffset(time));
    }

    @Override
    public void validate(final List<SQLNode> parameters) throws ParadoxSyntaxErrorException {
        if (parameters.size() > 1) {
            throw new ParadoxSyntaxErrorException(ParadoxSyntaxErrorException.Error.INVALID_PARAMETER_COUNT, "1");
        }
    }
}