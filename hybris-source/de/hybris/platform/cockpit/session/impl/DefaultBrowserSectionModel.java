package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.events.impl.SectionModelEvent;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserSectionRenderer;
import de.hybris.platform.cockpit.session.SectionBrowserModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.zkoss.zk.ui.HtmlBasedComponent;

public class DefaultBrowserSectionModel extends AbstractBrowserSectionModel
{
    protected boolean initialized = false;
    private BrowserSectionRenderer sectionRenderer = null;
    private final List<TypedObject> items = new ArrayList<>();
    private String label = null;
    private HtmlBasedComponent preLabel = null;
    private String icon = null;
    private boolean focused = false;
    private boolean visible = true;
    private final List<Integer> selectedIndexes = new ArrayList<>();


    public DefaultBrowserSectionModel(SectionBrowserModel browserModel)
    {
        this(browserModel, "");
    }


    public DefaultBrowserSectionModel(SectionBrowserModel browserModel, String label)
    {
        this(browserModel, label, null);
    }


    public DefaultBrowserSectionModel(SectionBrowserModel browserModel, String label, Object rootItem)
    {
        super(browserModel, rootItem);
        this.label = label;
    }


    public void initialize()
    {
        this.initialized = true;
    }


    public void update()
    {
        if(this.initialized && isVisible())
        {
            this.modified = true;
            fireEvent(new SectionModelEvent(this, "changed"));
        }
    }


    public BrowserSectionRenderer getBrowserSectionRenderer()
    {
        return this.sectionRenderer;
    }


    public List<TypedObject> getItems()
    {
        return Collections.unmodifiableList(this.items);
    }


    public String getLabel()
    {
        return this.label;
    }


    public String getIcon()
    {
        return this.icon;
    }


    public Integer getSelectedIndex()
    {
        Integer selIndex = null;
        if(!this.selectedIndexes.isEmpty())
        {
            selIndex = this.selectedIndexes.get(0);
        }
        return selIndex;
    }


    public List<Integer> getSelectedIndexes()
    {
        return Collections.unmodifiableList(this.selectedIndexes);
    }


    public boolean isFocused()
    {
        return this.focused;
    }


    public boolean isModified()
    {
        return this.modified;
    }


    public void setModified(boolean modified)
    {
        this.modified = modified;
    }


    public boolean isVisible()
    {
        return this.visible;
    }


    public void setBrowserSectionRenderer(BrowserSectionRenderer sectionRenderer)
    {
        this.sectionRenderer = sectionRenderer;
        if(this.initialized && isVisible())
        {
            this.modified = true;
            fireEvent(new SectionModelEvent(this, "changed"));
        }
    }


    public void setFocused(boolean focused)
    {
        this.focused = focused;
    }


    public void setItems(List<? extends TypedObject> items)
    {
        this.selectedIndexes.clear();
        if((this.items == null && items != null) || (this.items != null && !this.items.equals(items)))
        {
            this.items.clear();
            if(items != null && !items.isEmpty())
            {
                this.items.addAll(items);
            }
            if(this.initialized && isVisible())
            {
                this.modified = true;
                fireEvent(new SectionModelEvent(this, "itemsChanged"));
            }
        }
    }


    public void setLabel(String label)
    {
        this.label = label;
        if(this.initialized && isVisible())
        {
            this.modified = true;
            fireEvent(new SectionModelEvent(this, "changed"));
        }
    }


    public void setIcon(String icon)
    {
        this.icon = icon;
        if(this.initialized && isVisible())
        {
            this.modified = true;
            fireEvent(new SectionModelEvent(this, "changed"));
        }
    }


    public void setSelectedIndex(int index)
    {
        setSelectedIndexes(Collections.singletonList(Integer.valueOf(index)));
    }


    public void setSelectedIndexes(List<Integer> indexes)
    {
        if((this.selectedIndexes == null && indexes != null) || (this.selectedIndexes != null &&
                        !this.selectedIndexes.equals(indexes)))
        {
            this.selectedIndexes.clear();
            if(indexes != null && !indexes.isEmpty())
            {
                this.selectedIndexes.addAll(indexes);
            }
            if(this.initialized && isVisible())
            {
                this.modified = true;
                fireEvent(new SectionModelEvent(this, "selectionChange"));
            }
        }
    }


    public void setVisible(boolean visible)
    {
        if(this.visible != visible)
        {
            this.visible = visible;
            if(this.initialized)
            {
                this.modified = true;
                fireEvent(new SectionModelEvent(this, "changed"));
            }
        }
    }


    protected void fireEvent(SectionModelEvent event)
    {
        if(this.initialized)
        {
            super.fireEvent(event);
        }
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        if(event instanceof ItemChangedEvent)
        {
            switch(null.$SwitchMap$de$hybris$platform$cockpit$events$impl$ItemChangedEvent$ChangeType[((ItemChangedEvent)event).getChangeType().ordinal()])
            {
                case 1:
                case 2:
                    if(getItems().contains(((ItemChangedEvent)event).getItem()))
                    {
                        setModified(Boolean.TRUE.booleanValue());
                    }
                    break;
            }
        }
    }


    public HtmlBasedComponent getPreLabel()
    {
        return this.preLabel;
    }


    public void setPreLabel(HtmlBasedComponent preLabel)
    {
        this.preLabel = preLabel;
    }
}
