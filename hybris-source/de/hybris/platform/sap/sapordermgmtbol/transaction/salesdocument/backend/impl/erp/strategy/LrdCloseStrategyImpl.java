/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.sapordermgmtbol.transaction.salesdocument.backend.impl.erp.strategy;

import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoStructure;
import de.hybris.platform.sap.core.jco.connection.JCoConnection;
import de.hybris.platform.sap.core.jco.exceptions.BackendException;
import de.hybris.platform.sap.sapordermgmtbol.transaction.salesdocument.backend.interf.erp.ConstantsR3Lrd;
import de.hybris.platform.sap.sapordermgmtbol.transaction.salesdocument.backend.interf.erp.strategy.LrdCloseStrategy;

/**
 * Default implementation: Close LO-API session in back end
 *
 */
public class LrdCloseStrategyImpl implements LrdCloseStrategy
{
    @Override
    public void close(final JCoConnection connection) throws BackendException
    {
        final JCoFunction function = connection.getFunction(ConstantsR3Lrd.FM_LO_API_CLOSE);
        connection.execute(function);
        final JCoParameterList exportParameterList = function.getExportParameterList();
        final String stillLoaded = exportParameterList.getString("EF_LOADED");
        final JCoStructure esError = exportParameterList.getStructure("ES_ERROR");
        if(!stillLoaded.isEmpty())
        {
            throw new BackendException("LO-API still loaded");
        }
        if(!esError.getString("ERRKZ").isEmpty())
        {
            throw new BackendException("Exception from close: " + esError.getString("MSGID") + "," + esError.getString("MSGNO"));
        }
    }
}
