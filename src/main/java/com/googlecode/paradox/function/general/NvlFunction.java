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
import com.googlecode.paradox.exceptions.ParadoxSyntaxErrorException;
import com.googlecode.paradox.function.IFunction;
import com.googlecode.paradox.planner.FieldValueUtils;
import com.googlecode.paradox.planner.nodes.FieldNode;
import com.googlecode.paradox.results.ParadoxType;

/**
 * The SQL NVL function.
 *
 * @version 1.2
 * @since 1.6.0
 */
public class NvlFunction implements IFunction {

    /**
     * The function name.
     */
    public static final String NAME = "NVL";

    private ParadoxType type = ParadoxType.NULL;

    @Override
    public ParadoxType type() {
        return type;
    }

    @Override
    public int parameterCount() {
        return 2;
    }

    @Override
    public boolean isVariableParameters() {
        return false;
    }

    @Override
    public Object execute(final ParadoxConnection connection, final Object[] values, final ParadoxType[] types,
                          final FieldNode[] fields) throws ParadoxSyntaxErrorException {
        this.type = FieldValueUtils.getSqlType(values, types);

        if (values[0] != null) {
            return values[0];
        }

        return values[1];
    }
}
