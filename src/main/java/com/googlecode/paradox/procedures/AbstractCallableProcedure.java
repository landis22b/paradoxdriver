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
package com.googlecode.paradox.procedures;

import com.googlecode.paradox.ParadoxConnection;
import com.googlecode.paradox.metadata.ParadoxField;

import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class used to create any callable procedure.
 *
 * @version 1.1
 * @since 1.1
 */
public abstract class AbstractCallableProcedure {

    protected final ParadoxConnection connection;

    /**
     * @param connection the Paradox connection.
     */
    public AbstractCallableProcedure(ParadoxConnection connection) {
        this.connection = connection;
    }

    public abstract boolean isNative();

    /**
     * Get the procedure columns.
     *
     * @return the procedure columns.
     */
    public final List<ParadoxField> getCols() {
        final ArrayList<ParadoxField> ret = new ArrayList<>();

        final ParadoxField field = new ParadoxField(connection, (byte) 0xC);
        field.setName("field");
        ret.add(field);

        return ret;
    }

    /**
     * Gets the procedure name.
     *
     * @return the procedure name.
     */
    public abstract String getName();

    /**
     * Gets the procedure description.
     *
     * @return the procedure description.
     */
    public abstract String getRemarks();

    /**
     * Gets the return type.
     *
     * @return the return type.
     */
    public final int getReturnType() {
        return DatabaseMetaData.procedureReturnsResult;
    }
}
