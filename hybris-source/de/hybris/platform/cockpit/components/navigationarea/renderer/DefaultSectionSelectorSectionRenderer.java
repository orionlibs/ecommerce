package de.hybris.platform.cockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.navigationarea.DefaultSectionSelectorSection;
import de.hybris.platform.cockpit.components.navigationarea.SectionSelectorSection;
import de.hybris.platform.cockpit.components.navigationarea.SelectorSection;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.util.UITools;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;

public class DefaultSectionSelectorSectionRenderer extends DefaultSelectorSectionRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultSectionSelectorSectionRenderer.class.getName());
    protected static final String ON_DEMAND_ATTR = "ondemand";
    protected static final String SELECTOR_SECTION_SCLASS = "selectorSection";
    protected static final String SELECTOR_SECTION_ITEM_SCLASS = "selectorSectionItem";
    private SectionPanel panelSection = null;
    private Listbox listBox;


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        this.panelSection = panel;
        if(section instanceof SectionSelectorSection)
        {
            this.lastSection = (SelectorSection)section;
            loadCaptionComponent(captionComponent);
            SectionSelectorSection selectorSection = (SectionSelectorSection)section;
            Div mainDiv = new Div();
            mainDiv.setSclass("selectorSection");
            parent.appendChild((Component)mainDiv);
            if(selectorSection.getItems().isEmpty())
            {
                Label emptyLabel = new Label("Nothing to display.");
                mainDiv.appendChild((Component)emptyLabel);
            }
            else
            {
                this.listBox = createList("selectorSectionItem", selectorSection.getItems(), getListRenderer());
                mainDiv.appendChild((Component)this.listBox);
                this.listBox.setMultiple(selectorSection.isMultiple());
                this.listBox.addEventListener("onSelect", (EventListener)new Object(this, selectorSection));
                this.listBox.addEventListener("onSelectLater", (EventListener)new Object(this, selectorSection));
                TypedObject selectedItem = selectorSection.getRelatedObject();
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
                        this.listBox.setSelectedIndex(selectedIndex);
                    }
                    selectorSection.setSelectedItem(selectedItem);
                }
            }
        }
        else
        {
            LOG.error("Section is not a section selector section.");
        }
    }


    protected Listbox getListboxComponent()
    {
        return this.listBox;
    }


    protected void renderSubsections(HtmlBasedComponent parent, TypedObject value)
    {
        parent.setAttribute("ondemand", Boolean.TRUE);
        UITools.detachChildren((Component)parent);
        getSection().setRelatedObject(value);
        List<SectionSelectorSection> subSections = getSection().getSubSections();
        for(SectionSelectorSection subSection : subSections)
        {
            subSection.setOpen(Boolean.TRUE.booleanValue());
            ((DefaultSectionSelectorSection)subSection).getRenderer().render(this.panelSection, (Component)parent, null, (Section)subSection);
        }
    }


    protected DefaultSectionSelectorSection getSection()
    {
        if(super.getSection() instanceof DefaultSectionSelectorSection)
        {
            return (DefaultSectionSelectorSection)super.getSection();
        }
        return null;
    }


    protected void loadCaptionComponent(Component parent)
    {
    }
}
