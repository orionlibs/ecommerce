package de.hybris.platform.cockpit.components.sectionpanel;

import org.zkoss.zk.ui.Component;

public abstract class AbstractTabComponentRenderer implements TabComponent.TabComponentRenderer
{
    private Component container = null;
    private TabComponent.TabComponentModel model = null;


    public Component getContainer()
    {
        return this.container;
    }


    public void setContainer(Component container)
    {
        this.container = container;
    }


    public TabComponent.TabComponentModel getModel()
    {
        return this.model;
    }


    public void setModel(TabComponent.TabComponentModel model)
    {
        this.model = model;
    }
}
