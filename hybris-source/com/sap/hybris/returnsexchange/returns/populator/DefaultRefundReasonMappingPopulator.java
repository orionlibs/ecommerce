/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.sap.hybris.returnsexchange.returns.populator;

import com.sap.hybris.returnsexchange.model.SapReturnOrderReasonModel;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import java.util.Map;

public class DefaultRefundReasonMappingPopulator implements Populator<SapReturnOrderReasonModel, Map<String, Object>>
{
    @Override
    public void populate(final SapReturnOrderReasonModel source, final Map<String, Object> target) throws ConversionException
    {
        target.put(SapReturnOrderReasonModel.REFUNDREASON, source.getRefundReason().getCode());
    }
}
