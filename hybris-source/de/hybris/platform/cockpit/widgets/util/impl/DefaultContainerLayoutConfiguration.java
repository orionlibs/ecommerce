package de.hybris.platform.cockpit.widgets.util.impl;

import de.hybris.platform.cockpit.widgets.portal.ContainerLayout;
import de.hybris.platform.cockpit.widgets.util.ContainerLayoutConfiguration;
import java.util.Collections;
import java.util.List;

public class DefaultContainerLayoutConfiguration implements ContainerLayoutConfiguration
{
    private List<ContainerLayout> containerLayouts;


    public void setContainerLayouts(List<ContainerLayout> containerLayouts)
    {
        this.containerLayouts = containerLayouts;
    }


    public List<ContainerLayout> getContainerLayouts()
    {
        return (this.containerLayouts == null) ? Collections.EMPTY_LIST : this.containerLayouts;
    }
}
