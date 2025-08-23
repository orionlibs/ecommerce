package de.hybris.platform.cockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.AdvancedGroupbox;
import de.hybris.platform.cockpit.components.navigationarea.DefaultSectionSelectorSection;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public abstract class AbstracttSectionSelectorSectionListRenderer implements ListitemRenderer
{
    protected static final String ON_DEMAND_ATTR = "ondemand";
    protected static final String COCKPIT_ID_NAVIGATION_AREA_PREFIX = "NavigationArea";


    public abstract DefaultSectionSelectorSectionRenderer getSectionRenderer();


    protected abstract String getListitemSclass();


    protected abstract String getListcellSclass();


    @Deprecated
    protected Component getCaptionComponent()
    {
        return null;
    }


    protected Component loadCaptionComponent(HtmlBasedComponent parent, TypedObject currentTypedObjectValue)
    {
        return null;
    }


    protected DefaultSectionSelectorSection getSection()
    {
        if(getSectionRenderer() != null)
        {
            return getSectionRenderer().getSection();
        }
        return null;
    }


    public void render(Listitem listitem, Object data) throws Exception
    {
        Div main = new Div();
        if(data instanceof TypedObject)
        {
            TypedObject value = (TypedObject)data;
            listitem.setValue(value);
            listitem.setSclass(getListitemSclass());
            Listcell navigationListcell = new Listcell();
            navigationListcell.setSclass(getListcellSclass());
            AdvancedGroupbox navigationEntry = new AdvancedGroupbox();
            loadCaptionComponent((HtmlBasedComponent)navigationEntry.getCaptionContainer(), value);
            navigationEntry.appendChild((Component)main);
            navigationEntry.setWidth("100%");
            navigationEntry.setAttribute("ondemand", Boolean.FALSE);
            boolean selected = value.equals(getSection().getRelatedObject());
            if((selected && (getSection().isSubSectionsVisible() || CollectionUtils.isEmpty(getSection().getSubSections()))) || (
                            getSection().getRelatedObjects().contains(value) && getSection().isMultiselect()))
            {
                navigationEntry.setOpen(Boolean.TRUE.booleanValue());
                navigationEntry.setAttribute("ondemand", Boolean.TRUE);
                getSectionRenderer().renderSubsections((HtmlBasedComponent)main, value);
            }
            else
            {
                navigationEntry.setOpen(Boolean.FALSE.booleanValue());
            }
            navigationEntry.addEventListener("onOpen", (EventListener)new Object(this, navigationEntry, main, value));
            Div labelComponent = navigationEntry.getPreLabelComponent();
            labelComponent.setSclass("advancedGroupboxPreLabel");
            Label navigationEntryName = new Label(UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(value));
            String formattedLabel = StringUtils.deleteWhitespace(navigationEntryName.getValue());
            formattedLabel = StringUtils.remove(formattedLabel, '/');
            String sectionName = getSection().getLabel();
            UITools.applyTestID((Component)navigationEntryName, "NavigationArea_" + sectionName + "_" + formattedLabel + "_button");
            labelComponent.appendChild((Component)navigationEntryName);
            createContextMenu((HtmlBasedComponent)navigationEntry.getCaption(), value);
            navigationListcell.appendChild((Component)navigationEntry);
            listitem.appendChild((Component)navigationListcell);
            listitem.addEventListener("onClick", (EventListener)new Object(this, navigationEntry, listitem));
        }
    }


    public void createContextMenu(HtmlBasedComponent parent, TypedObject value)
    {
    }
}
