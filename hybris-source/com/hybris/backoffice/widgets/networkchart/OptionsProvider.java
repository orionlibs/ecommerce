/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.networkchart;

import com.hybris.cockpitng.components.visjs.network.data.Options;

/**
 * Provides {@link Options} for {@link com.hybris.cockpitng.components.visjs.network.NetworkChart} component
 */
public interface OptionsProvider
{
    Options provide(NetworkChartController controller);
}
