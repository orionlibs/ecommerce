package de.hybris.platform.cockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.AdvancedGroupbox;
import de.hybris.platform.cockpit.components.navigationarea.AbstractDrillableSelectorSection;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

public abstract class AbstractDrillableSelectorSectionListRenderer extends AbstracttSectionSelectorSectionListRenderer
{
    protected AbstractDrillableSelectorSection getSection()
    {
        if(getSectionRenderer() != null)
        {
            return getSectionRenderer().getSection();
        }
        return null;
    }


    public void render(Listitem item, Object data) throws Exception
    {
        Div main = new Div();
        if(data instanceof TypedObject)
        {
            TypedObject value = (TypedObject)data;
            item.setValue(value);
            item.setSclass(getListitemSclass());
            Listcell drillableListcell = new Listcell();
            drillableListcell.setSclass(getListcellSclass());
            AdvancedGroupbox drillableNavigationEntry = new AdvancedGroupbox();
            loadCaptionComponent((HtmlBasedComponent)drillableNavigationEntry.getCaptionContainer(), value);
            drillableNavigationEntry.appendChild((Component)main);
            drillableNavigationEntry.setWidth("100%");
            drillableNavigationEntry.setAttribute("ondemand", Boolean.FALSE);
            boolean selected = value.equals(getSection().getRelatedObject());
            if(selected && getSection().isSubSectionsVisible())
            {
                drillableNavigationEntry.setOpen(Boolean.TRUE.booleanValue());
                drillableNavigationEntry.setAttribute("ondemand", Boolean.TRUE);
                getSectionRenderer().renderSubsections((HtmlBasedComponent)main, value);
            }
            else
            {
                drillableNavigationEntry.setOpen(Boolean.FALSE.booleanValue());
            }
            drillableNavigationEntry.addEventListener("onOpen", (EventListener)new Object(this, drillableNavigationEntry, main, value));
            Div labelComponent = drillableNavigationEntry.getPreLabelComponent();
            labelComponent.setSclass("advancedGroupboxPreLabel");
            Label storeNavigationEntryName = new Label(UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(value));
            labelComponent.appendChild((Component)storeNavigationEntryName);
            drillableListcell.appendChild((Component)drillableNavigationEntry);
            item.appendChild((Component)drillableListcell);
            item.addEventListener("onClick", (EventListener)new Object(this));
        }
    }


    public abstract DefaultDrillableSelectorSectionRenderer getSectionRenderer();
}
