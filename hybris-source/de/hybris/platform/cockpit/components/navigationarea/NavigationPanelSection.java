package de.hybris.platform.cockpit.components.navigationarea;

import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.util.resource.Labels;

public class NavigationPanelSection implements Section
{
    private String label;
    private String localizedLabel;
    private boolean visible;
    private SectionRenderer renderer;
    private boolean open = false;
    private Map<String, Object> attributes;


    public void setLabel(String label)
    {
        this.label = label;
    }


    public String getLabel()
    {
        return (this.label == null) ? getLocalizedLabel() : this.label;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    public boolean isVisible()
    {
        return this.visible;
    }


    public void setRenderer(SectionRenderer renderer)
    {
        this.renderer = renderer;
    }


    public SectionRenderer getRenderer()
    {
        return this.renderer;
    }


    public boolean isTabbed()
    {
        return false;
    }


    public boolean isInitialOpen()
    {
        return true;
    }


    public void setLocalizedLabel(String localizedLabel)
    {
        this.localizedLabel = localizedLabel;
    }


    public String getLocalizedLabel()
    {
        return Labels.getLabel(this.localizedLabel);
    }


    public boolean isOpen()
    {
        return this.open;
    }


    public void setOpen(boolean open)
    {
        this.open = open;
    }


    public void setAttribute(String key, Object value)
    {
        if(this.attributes == null)
        {
            this.attributes = new HashMap<>();
        }
        this.attributes.put(key, value);
    }


    public Object getAttribute(String key)
    {
        return (this.attributes == null) ? null : this.attributes.get(key);
    }
}
