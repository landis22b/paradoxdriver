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
package com.googlecode.paradox.metadata;

/**
 * Index types.
 *
 * @version 1.0
 * @since 1.6.0
 */
public enum IndexType {

    /**
     * Primary key.
     */
    PRIMARY_KEY,

    /**
     * Foreign key.
     */
    FOREIGN_KEY,

    /**
     * Check constraints.
     */
    CHECK,

    /**
     * Unique key.
     */
    UNIQUE,

    /**
     * Column index.
     */
    INDEX;

    /**
     * Gets the type description.
     *
     * @return the type description.
     */
    public String description() {
        return name().replace('_', ' ');
    }
}
