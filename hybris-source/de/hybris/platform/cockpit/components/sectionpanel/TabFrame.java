package de.hybris.platform.cockpit.components.sectionpanel;

import org.zkoss.zk.ui.Component;

public class TabFrame
{
    private Component container = null;
    private final TabFrameRenderer renderer = new TabFrameRenderer(this);
    private TabFrameModel model = new TabFrameModel(this);


    public TabFrameModel getModel()
    {
        return this.model;
    }


    public void setModel(TabFrameModel model)
    {
        this.model = model;
    }


    public Component getContainer()
    {
        return this.container;
    }


    public void setContainer(Component container)
    {
        this.container = container;
    }


    public void render()
    {
        if(this.container == null)
        {
            throw new RuntimeException("Container cannot be null");
        }
        this.renderer.setContainer(this.container);
        this.renderer.setModel(this.model);
        this.renderer.render();
    }
}
