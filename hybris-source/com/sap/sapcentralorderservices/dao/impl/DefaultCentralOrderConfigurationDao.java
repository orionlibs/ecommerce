/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.sapcentralorderservices.dao.impl;

import com.sap.sapcentralorderservices.dao.CentralOrderConfigurationDao;
import com.sap.sapcentralorderservices.model.SAPCentralOrderConfigurationModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;

/**
 * DefaultCentralOrderConfigurationDao
 */
public class DefaultCentralOrderConfigurationDao extends AbstractItemDao implements CentralOrderConfigurationDao
{
    @Override
    public SAPCentralOrderConfigurationModel findSapCentralOrderConfiguration()
    {
        final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT { " + ItemModel.PK + "} "
                        + "FROM {" + SAPCentralOrderConfigurationModel._TYPECODE + "} ");
        final SearchResult<SAPCentralOrderConfigurationModel> result = getFlexibleSearchService().search(query);
        final int resultCount = result.getTotalCount();
        if(resultCount == 0)
        {
            throw new UnknownIdentifierException("No Model for SAPCentralOrderConfigurationModel found!");
        }
        else if(resultCount > 1)
        {
            throw new AmbiguousIdentifierException(
                            "Model with name SAPCentralOrderConfigurationModel is not unique, " + resultCount + " Models found!");
        }
        return result.getResult().get(0);
    }
}
