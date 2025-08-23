/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.networkchart;

import com.hybris.cockpitng.components.visjs.network.data.Options;
import java.util.Objects;

public class NetworkChartOptionsResolver
{
    /**
     * Obtains {@link Options} object from {@link NetworkChartController#getChartOptions()} or via {@link OptionsProvider}
     * bean
     *
     * @return {@link Options} of vis.js
     */
    public Options resolveOptions(final NetworkChartController controller)
    {
        Objects.requireNonNull(controller);
        final Options inlineOptions = controller.getChartOptions();
        final OptionsProvider optionsProvider = controller.getChartOptionsProvider();
        if(optionsProvider != null && areOptionsEmpty(inlineOptions))
        {
            return optionsProvider.provide(controller);
        }
        return inlineOptions;
    }


    protected boolean areOptionsEmpty(final Options inlineSettings)
    {
        return Options.EMPTY.equals(inlineSettings);
    }
}
