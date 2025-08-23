/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.runtime.cps.populator.impl;

import de.hybris.platform.sap.productconfig.runtime.cps.masterdata.service.MasterDataContainerResolver;
import de.hybris.platform.sap.productconfig.runtime.cps.model.runtime.CPSItem;
import de.hybris.platform.sap.productconfig.runtime.interf.ContextualPopulator;
import de.hybris.platform.sap.productconfig.runtime.interf.model.InstanceModel;

/**
 * Responsible for attributes that we read from the master data cache
 */
public class InstanceMasterDataPopulator implements ContextualPopulator<CPSItem, InstanceModel, MasterDataContext>
{
    private MasterDataContainerResolver masterDataResolver;


    @Override
    public void populate(final CPSItem source, final InstanceModel target, final MasterDataContext ctxt)
    {
        target.setLanguageDependentName(getMasterDataResolver().getItemNameWithFallback(ctxt, source.getKey(), source.getType()));
    }


    protected MasterDataContainerResolver getMasterDataResolver()
    {
        return masterDataResolver;
    }


    public void setMasterDataResolver(final MasterDataContainerResolver masterDataResolver)
    {
        this.masterDataResolver = masterDataResolver;
    }
}
