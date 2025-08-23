package de.hybris.platform.cockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.AdvancedGroupbox;
import de.hybris.platform.cockpit.components.navigationarea.AbstractDrillableSelectorSection;
import de.hybris.platform.cockpit.components.navigationarea.DrillableSelectorSection;
import de.hybris.platform.cockpit.components.navigationarea.SelectorSection;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Space;
import org.zkoss.zul.Toolbarbutton;

public class DefaultDrillableSelectorSectionRenderer extends DefaultSectionSelectorSectionRenderer
{
    protected static final String ON_DEMAND_ATTR = "ondemand";
    protected static final String DRILLABLE_SELECTOR_SCLASS = "drillableSelector";
    protected static final String BACK_BUTTTON_LABEL = "<< back | ";
    protected static final String BACK_BUTTON_SCLASS = "drillableSelectorBackButton";
    protected static final int INTINITY_DRILL_LEVELS = -1;


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        if(section instanceof DrillableSelectorSection)
        {
            this.lastSection = (SelectorSection)section;
            DrillableSelectorSection selectorSection = (DrillableSelectorSection)section;
            Div mainDiv = new Div();
            mainDiv.setSclass("drillableSelector");
            parent.appendChild((Component)mainDiv);
            if(selectorSection.currentLevel() != 0)
            {
                AdvancedGroupbox parenGroupBox = new AdvancedGroupbox();
                parenGroupBox.setWidth("100%");
                parenGroupBox.setClosable(false);
                Div labelComponent = parenGroupBox.getPreLabelComponent();
                labelComponent.setSclass("advancedGroupboxPreLabel");
                Hbox labelContainer = new Hbox();
                labelContainer.setWidth("100%");
                labelContainer.setWidths("20%,none,80%");
                Toolbarbutton backButton = new Toolbarbutton("<< back | ", "");
                backButton.setSclass("drillableSelectorBackButton");
                backButton.addEventListener("onClick", (EventListener)new Object(this, selectorSection));
                Label storeNavigationEntryName = new Label(UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(selectorSection.getLastElement()));
                labelContainer.appendChild((Component)backButton);
                Space horizontalSpace = new Space();
                horizontalSpace.setOrient("horizontal");
                horizontalSpace.setWidth("3px");
                labelContainer.appendChild((Component)horizontalSpace);
                labelContainer.appendChild((Component)storeNavigationEntryName);
                labelComponent.appendChild((Component)labelContainer);
                mainDiv.appendChild((Component)parenGroupBox);
            }
            if(getSection().getDrilldownLevel() == -1 ||
                            getSection().currentLevel() <= getSection().getDrilldownLevel())
            {
                Listbox listBox = createList("cockpitSelectorItem", selectorSection.getItems(), getListRenderer());
                if(listBox != null)
                {
                    mainDiv.appendChild((Component)listBox);
                    listBox.setMultiple(selectorSection.isMultiple());
                    listBox.addEventListener("onSelect", (EventListener)new Object(this, selectorSection));
                }
            }
        }
    }


    protected AbstractDrillableSelectorSection getSection()
    {
        if(super.getSection() instanceof DrillableSelectorSection)
        {
            return (AbstractDrillableSelectorSection)super.getSection();
        }
        return null;
    }
}
