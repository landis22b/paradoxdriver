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
import com.googlecode.paradox.metadata.ParadoxField;
import com.googlecode.paradox.metadata.ParadoxTable;
import com.googlecode.paradox.results.ParadoxFieldType;

import java.nio.ByteBuffer;

/**
 * Parses a VARCHAR field.
 *
 * @author Leonardo Costa
 * @version 1.3
 * @since 1.3
 */
public final class VarcharField implements FieldParser {

    /**
     * {@inheritDoc}.
     */
    @Override
    public boolean match(final int type) {
        return type == ParadoxFieldType.VARCHAR.getType();
    }

    /**
     * {@inheritDoc}.
     */
    @Override
    public Object parse(final ParadoxTable table, final ByteBuffer buffer, final ParadoxField field) {
        final ByteBuffer valueString = ByteBuffer.allocate(field.getSize());

        System.arraycopy(buffer.array(), buffer.position(), valueString.array(), 0, field.getSize());
        buffer.position(buffer.position() + field.getSize());

        final byte[] value = valueString.array();
        int length = value.length;

        for (; length > 0; length--) {
            // array value starts with zero, not 1
            if (value[length - 1] != 0) {
                break;
            }
        }
        valueString.flip();
        valueString.limit(length);
        final String str = table.getCharset().decode(valueString).toString();

        if (str.isEmpty()) {
            return null;
        }

        return str;
    }

}
