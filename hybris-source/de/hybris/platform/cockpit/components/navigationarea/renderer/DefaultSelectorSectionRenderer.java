package de.hybris.platform.cockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.navigationarea.SelectorSection;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public class DefaultSelectorSectionRenderer extends AbstractNavigationAreaSectionRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSelectorSectionRenderer.class);
    protected static final String EMPTY_MESSAGE = "Nothing to display.";
    protected SelectorSection lastSection = null;
    public static final String SELECTOR_SCLASS = "cockpitSelector";
    public static final String SELECTOR_ITEM_SCLASS = "cockpitSelectorItem";
    public static final String SELECTOR_ACTIVE_ITEM_SCLASS = "cockpitSelectorActiveItem";
    private ListitemRenderer listRenderer = null;


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        if(section instanceof SelectorSection)
        {
            this.lastSection = (SelectorSection)section;
            SelectorSection selectorSection = (SelectorSection)section;
            Div mainDiv = new Div();
            mainDiv.setSclass("cockpitSelector");
            parent.appendChild((Component)mainDiv);
            if(selectorSection.getItems().isEmpty())
            {
                Label emptyLabel = new Label("Nothing to display.");
                mainDiv.appendChild((Component)emptyLabel);
            }
            else
            {
                Listbox listBox = createList("cockpitSelectorItem", selectorSection.getItems(), getListRenderer());
                mainDiv.appendChild((Component)listBox);
                listBox.setMultiple(selectorSection.isMultiple());
                listBox.addEventListener("onSelect", (EventListener)new Object(this, selectorSection));
                TypedObject selectedItem = selectorSection.getSelectedItem();
                List<TypedObject> items = selectorSection.getItems();
                if(items != null && !items.isEmpty())
                {
                    if(selectedItem == null && selectorSection.isInitiallySelected())
                    {
                        selectedItem = items.get(0);
                    }
                    int selectedIndex = selectorSection.getItems().indexOf(selectedItem);
                    if(selectedIndex >= 0)
                    {
                        listBox.setSelectedIndex(selectedIndex);
                    }
                    selectorSection.setSelectedItem(selectedItem);
                }
            }
        }
        else
        {
            LOG.error("Section is not a selector section.");
        }
    }


    public void setListRenderer(ListitemRenderer listRenderer)
    {
        this.listRenderer = listRenderer;
    }


    public ListitemRenderer getListRenderer()
    {
        if(this.listRenderer == null)
        {
            this.listRenderer = (ListitemRenderer)new MyListRenderer(this);
        }
        return this.listRenderer;
    }


    protected List<TypedObject> extractSelectedItems(Set<Object> listValues)
    {
        List<TypedObject> selectedItems = new ArrayList<>();
        if(listValues != null && !listValues.isEmpty())
        {
            for(Object object : listValues)
            {
                if(object instanceof Listitem)
                {
                    Listitem listItem = (Listitem)object;
                    Object value = listItem.getValue();
                    if(value instanceof TypedObject)
                    {
                        selectedItems.add((TypedObject)value);
                        continue;
                    }
                    LOG.warn("Can not extract selected items. Reason: Value not a TypedObject.");
                }
            }
        }
        return selectedItems;
    }


    protected SelectorSection getSection()
    {
        return this.lastSection;
    }
}
