/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.controller.collapsiblecontainer;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents Collapsible Container panels state object
 */
public class CollapsibleContainerState
{
    private final Boolean topPanelCollapsed;
    private final Boolean centerPanelCollapsed;
    private final Boolean bottomPanelCollapsed;


    /**
     * Create Collapsible Container state object when Collapsible Container displays 3 panels. Pass null value if not
     * want to change state of given panel.
     *
     * @param topPanelCollapsed
     * @param centerPanelCollapsed
     * @param bottomPanelCollapsed
     */
    @JsonCreator
    public CollapsibleContainerState(@JsonProperty("topPanelCollapsed") final Boolean topPanelCollapsed,
                    @JsonProperty("centerPanelCollapsed") final Boolean centerPanelCollapsed,
                    @JsonProperty("bottomPanelCollapsed") final Boolean bottomPanelCollapsed)
    {
        this.topPanelCollapsed = topPanelCollapsed;
        this.centerPanelCollapsed = centerPanelCollapsed;
        this.bottomPanelCollapsed = bottomPanelCollapsed;
    }


    /**
     * Create Collapsible Container state object when Collapsible Container displays 2 panels. Pass null value if not
     * want to change state of given panel.
     *
     * @param centerPanelCollapsed
     * @param bottomPanelCollapsed
     */
    public CollapsibleContainerState(final Boolean centerPanelCollapsed, final Boolean bottomPanelCollapsed)
    {
        this.topPanelCollapsed = null;
        this.centerPanelCollapsed = centerPanelCollapsed;
        this.bottomPanelCollapsed = bottomPanelCollapsed;
    }


    public Boolean getTopPanelCollapsed()
    {
        return topPanelCollapsed;
    }


    public Boolean getCenterPanelCollapsed()
    {
        return centerPanelCollapsed;
    }


    public Boolean getBottomPanelCollapsed()
    {
        return bottomPanelCollapsed;
    }
}
