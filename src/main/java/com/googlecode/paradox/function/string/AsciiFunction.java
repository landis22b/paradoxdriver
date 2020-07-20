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
import com.googlecode.paradox.function.IFunction;

import java.sql.SQLException;
import java.sql.Types;
import java.util.logging.Logger;

/**
 * The SQL ASCII function.
 *
 * @version 1.1
 * @since 1.6.0
 */
public class AsciiFunction implements IFunction {

    /**
     * The function name.
     */
    public static final String NAME = "ASCII";
    private static final Logger LOGGER = Logger.getLogger(AsciiFunction.class.getName());

    @Override
    public int sqlType() {
        return Types.INTEGER;
    }

    @Override
    public int parameterCount() {
        return 1;
    }

    @Override
    public Object execute(final ParadoxConnection connection, final Object[] values, final int[] types)
            throws SQLException {
        Object value = values[0];
        if (value != null && !value.toString().isEmpty()) {
            return (int) value.toString().charAt(0);
        }

        return null;
    }
}