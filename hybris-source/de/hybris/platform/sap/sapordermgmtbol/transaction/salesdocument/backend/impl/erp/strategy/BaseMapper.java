/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapordermgmtbol.transaction.salesdocument.backend.impl.erp.strategy;

import com.sap.conn.jco.JCoTable;
import de.hybris.platform.sap.sapordermgmtbol.transaction.salesdocument.backend.interf.erp.ConstantsR3Lrd;

/**
 * Base class for LO-API / BOL mappers
 */
abstract public class BaseMapper
{
    /**
     * Bean initializer.
     * <p>
     * Check if all mandatory values injected, else throw runtime error.
     */
    abstract public void init();


    /**
     * Adds an entry to the table of object instances we need to exchange with LO-API
     *
     * @param objInst
     * @param handle
     * @param headHandle
     * @param objectId
     */
    public void addToObjInst(final JCoTable objInst, final String handle, final String headHandle, final String objectId)
    {
        objInst.appendRow();
        objInst.setValue(ConstantsR3Lrd.FIELD_HANDLE, handle);
        objInst.setValue(ConstantsR3Lrd.FIELD_HANDLE_PARENT, headHandle);
        objInst.setValue(ConstantsR3Lrd.FIELD_OBJECT_ID, objectId);
    }
}
