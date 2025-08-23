/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.config;

import com.hybris.cockpitng.config.dashboard.jaxb.Dashboard;
import com.hybris.cockpitng.config.dashboard.jaxb.Grid;
import com.hybris.cockpitng.config.dashboard.jaxb.UnassignedBehavior;

public class DefaultDashboardConfigurationFallbackStrategy implements CockpitConfigurationFallbackStrategy<Dashboard>
{
    public static final String DEFAULT = "default";


    @Override
    public Dashboard loadFallbackConfiguration(final ConfigContext context, final Class<Dashboard> configurationType)
    {
        final Dashboard dashboard = new Dashboard();
        dashboard.setDefaultGridId(DEFAULT);
        final Grid grid = new Grid();
        grid.setId(DEFAULT);
        grid.setUnassigned(UnassignedBehavior.APPEND);
        dashboard.getGrid().add(grid);
        return dashboard;
    }
}
