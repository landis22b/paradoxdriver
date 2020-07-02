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
package com.googlecode.paradox.planner;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Comparator;
import java.util.function.IntPredicate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Compare Paradox values.
 *
 * @version 1.2
 * @since 1.6.0
 */
public class ValuesComparator implements Comparator<Object>, Serializable {

    private static final Logger LOGGER = Logger.getLogger(ValuesComparator.class.getName());

    /**
     * Creates a new instance.
     */
    public ValuesComparator() {
        super();
    }

    public boolean compare(final Object o1, final Object o2, IntPredicate condition) {
        if (o1 == null || o2 == null) {
            return false;
        }

        return condition.test(compare(o1, o2));
    }

    private static Time getTime(final Object value) {
        if (value instanceof Time) {
            return (Time) value;
        }

        return Time.valueOf(value.toString());
    }

    private static Boolean getBoolean(final Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }

        return Boolean.valueOf(value.toString());
    }

    private static Byte getByte(final Object value) {
        if (value instanceof Byte) {
            return (Byte) value;
        }

        return Byte.valueOf(value.toString());
    }

    private static Integer getInteger(final Object value) {
        if (value instanceof Integer) {
            return (Integer) value;
        }

        return Integer.valueOf(value.toString());
    }

    private static Long getLong(final Object value) {
        if (value instanceof Long) {
            return (Long) value;
        }

        return Long.valueOf(value.toString());
    }

    private static Double getDouble(final Object value) {
        if (value instanceof Double) {
            return (Double) value;
        }

        return Double.valueOf(value.toString());
    }

    private static Timestamp getTimestamp(final Object value) {
        if (value instanceof Timestamp) {
            return (Timestamp) value;
        }

        return Timestamp.valueOf(value.toString());
    }

    private static Date getDate(final Object value) {
        if (value instanceof Date) {
            return (Date) value;
        }

        return Date.valueOf(value.toString());
    }

    @Override
    public int compare(final Object o1, final Object o2) {
        // Try to compare with Boolean values.
        if (o1 instanceof Boolean || o2 instanceof Boolean) {
            final Boolean n1 = getBoolean(o1);
            final Boolean n2 = getBoolean(o2);
            return n1.compareTo(n2);
        }

        // FIXME move conversions to a unique class only for this.

        // Try to compare with Byte values.
        if (o1 instanceof Byte || o2 instanceof Byte) {
            try {
                final Byte n1 = getByte(o1);
                final Byte n2 = getByte(o2);
                return n1.compareTo(n2);
            } catch (final NumberFormatException e) {
                LOGGER.log(Level.FINEST, e.getMessage(), e);
            }
        }

        // Try to compare with Integer values.
        if (o1 instanceof Integer || o2 instanceof Integer) {
            try {
                final Integer n1 = getInteger(o1);
                final Integer n2 = getInteger(o2);
                return n1.compareTo(n2);
            } catch (final NumberFormatException e) {
                LOGGER.log(Level.FINEST, e.getMessage(), e);
            }
        }

        // Try to compare with Long values.
        if (o1 instanceof Long || o2 instanceof Long) {
            try {
                final Long n1 = getLong(o1);
                final Long n2 = getLong(o2);
                return n1.compareTo(n2);
            } catch (final NumberFormatException e) {
                LOGGER.log(Level.FINEST, e.getMessage(), e);
            }
        }

        // Try to compare with Double values.
        if (o1 instanceof Double || o2 instanceof Double) {
            try {
                final Double n1 = getDouble(o1);
                final Double n2 = getDouble(o2);
                return n1.compareTo(n2);
            } catch (final NumberFormatException e) {
                LOGGER.log(Level.FINEST, e.getMessage(), e);
            }
        }

        // Try to compare with Time values.
        if (o1 instanceof Time || o2 instanceof Time) {
            final Time n1 = getTime(o1);
            final Time n2 = getTime(o2);
            return n1.compareTo(n2);
        }

        // Try to compare with Timestamp values.
        if (o1 instanceof Timestamp || o2 instanceof Timestamp) {
            final Timestamp n1 = getTimestamp(o1);
            final Timestamp n2 = getTimestamp(o2);
            return n1.compareTo(n2);
        }

        // Try to compare with Date values.
        if (o1 instanceof Date || o2 instanceof Date) {
            final Date n1 = getDate(o1);
            final Date n2 = getDate(o2);
            return n1.compareTo(n2);
        }

        // Try to compare with String values.
        if (o1 instanceof String || o2 instanceof String) {
            final String n1 = String.valueOf(o1);
            final String n2 = String.valueOf(o2);
            return n1.compareTo(n2);
        }

        // Try to compare with String values.
        if (o1 instanceof byte[] || o2 instanceof byte[]) {
            final byte[] n1 = getByteArray(o1);
            final byte[] n2 = getByteArray(o2);
            if (Arrays.equals(n1, n2)) {
                return 0;
            }
            return -1;
        }

        return -1;
    }

    private static byte[] getByteArray(final Object value) {
        if (value instanceof byte[]) {
            return (byte[]) value;
        }

        return value.toString().getBytes(StandardCharsets.UTF_8);
    }
}
