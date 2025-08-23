/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
package de.hybris.platform.sap.productconfig.integration.service.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.sap.productconfig.services.intf.ExternalConfigurationAccess;

/**
 * Default implementation of {@link ExternalConfigurationAccess} that writes and reads to
 * {@link AbstractOrderEntryModel}
 */
public class DefaultExternalConfigurationAccess implements ExternalConfigurationAccess
{
    @Override
    public void setExternalConfiguration(final String externalConfiguration, final AbstractOrderEntryModel orderEntryModel)
    {
        orderEntryModel.setExternalConfiguration(externalConfiguration);
    }


    @Override
    public String getExternalConfiguration(final AbstractOrderEntryModel orderEntryModel)
    {
        return orderEntryModel.getExternalConfiguration();
    }
}
