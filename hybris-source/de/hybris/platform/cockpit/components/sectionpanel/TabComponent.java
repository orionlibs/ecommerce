package de.hybris.platform.cockpit.components.sectionpanel;

import org.zkoss.zk.ui.Component;

public class TabComponent
{
    private Component container = null;
    private TabComponentRenderer renderer = null;
    private TabComponentModel model = new TabComponentModel(this);


    public Component getContainer()
    {
        return this.container;
    }


    public void setContainer(Component container)
    {
        this.container = container;
    }


    public TabComponentModel getModel()
    {
        return this.model;
    }


    public void setModel(TabComponentModel model)
    {
        this.model = model;
    }


    public TabComponentRenderer getRenderer()
    {
        return this.renderer;
    }


    public void setRenderer(TabComponentRenderer renderer)
    {
        this.renderer = renderer;
    }


    public void render()
    {
        if(this.container == null || this.renderer == null)
        {
            return;
        }
        this.renderer.setContainer(this.container);
        this.renderer.setModel(this.model);
        this.renderer.render();
    }
}
