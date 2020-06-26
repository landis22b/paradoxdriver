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
package com.googlecode.paradox.data.field;

import com.googlecode.paradox.data.FieldParser;
import com.googlecode.paradox.data.table.value.FieldValue;
import com.googlecode.paradox.metadata.ParadoxField;
import com.googlecode.paradox.metadata.ParadoxTable;
import com.googlecode.paradox.results.ParadoxFieldType;

import java.nio.ByteBuffer;
import java.sql.Types;

/**
 * Parses boolean fields.
 *
 * @author Leonardo Costa
 * @version 1.1
 * @since 1.3
 */
public final class BooleanField implements FieldParser {

    private static final int TRUE_VALUE = -127;
    private static final int FALSE_VALUE = -128;

    private static final FieldValue NULL = new FieldValue(Types.BOOLEAN);
    private static final FieldValue TRUE = new FieldValue(Boolean.TRUE, ParadoxFieldType.BOOLEAN.getSQLType());
    private static final FieldValue FALSE = new FieldValue(Boolean.FALSE, ParadoxFieldType.BOOLEAN.getSQLType());

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean match(final int type) {
        return type == ParadoxFieldType.BOOLEAN.getType();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public FieldValue parse(final ParadoxTable table, final ByteBuffer buffer, final ParadoxField field) {
        final byte v = buffer.get();
        FieldValue ret = NULL;
        if (v == TRUE_VALUE) {
            ret = TRUE;
        } else if (v == FALSE_VALUE) {
            ret = FALSE;
        }

        return ret;
    }

}
