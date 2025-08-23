package de.hybris.platform.cockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.navigationarea.AbstractNavigationAreaModel;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.model.query.impl.UICollectionQuery;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Vbox;

public class SpecialCollectionSectionRenderer extends AbstractNavigationAreaSectionRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(SpecialCollectionSectionRenderer.class.getName());
    private String allItemsLabel;
    private List<String> allowedTypes;


    protected Separator createSeparator()
    {
        Separator sep = new Separator();
        sep.setBar(true);
        sep.setOrient("horizontal");
        return sep;
    }


    public String getAllItemsLabel()
    {
        if(StringUtils.isBlank(this.allItemsLabel))
        {
            return "general.allitems";
        }
        return this.allItemsLabel;
    }


    public void setAllItemsLabel(String allItemsLabel)
    {
        this.allItemsLabel = allItemsLabel;
    }


    public List<String> getAllowedTypes()
    {
        if(this.allowedTypes == null)
        {
            this.allowedTypes = Collections.EMPTY_LIST;
        }
        return this.allowedTypes;
    }


    public void setAllowedTypes(List<String> allowedTypes)
    {
        this.allowedTypes = allowedTypes;
    }


    public BaseUICockpitNavigationArea getNavigationArea()
    {
        return (BaseUICockpitNavigationArea)super.getNavigationArea();
    }


    public void render(SectionPanel panel, Component parent, Component captionComponent, Section section)
    {
        Div div = new Div();
        div.setSclass("catalog_section_container");
        parent.appendChild((Component)div);
        Div showAllLabel = new Div();
        showAllLabel.appendChild((Component)new Label(Labels.getLabel(getAllItemsLabel())));
        showAllLabel.setSclass("show-all-label");
        showAllLabel.setStyle("margin-bottom:0px;");
        UITools.addBusyListener((Component)showAllLabel, "onClick", (EventListener)new Object(this), null, null);
        showAllLabel.setParent((Component)div);
        if(panel.getModel() instanceof AbstractNavigationAreaModel)
        {
            AbstractNavigationAreaModel model = (AbstractNavigationAreaModel)panel.getModel();
            List<UICollectionQuery> coll = model.getSpecialCollections();
            SpecialCollectionRenderer collectionRenderer = new SpecialCollectionRenderer(getNavigationArea());
            Listbox collList = createList("navigation_collectionlist", coll, (ListitemRenderer)collectionRenderer);
            Vbox vbox = new Vbox();
            vbox.setSclass("navigation_queries");
            if(collList == null)
            {
                vbox.setAlign("left");
                vbox.appendChild((Component)new Label(Labels.getLabel("querysection.empty")));
            }
            else
            {
                UITools.addBusyListener((Component)collList, "onSelect", (EventListener)new Object(this, collList), null, null);
                Integer selectedIndex = getNavigationArea().getSelectedIndex("special");
                if(selectedIndex != null)
                {
                    collList.setSelectedIndex(selectedIndex.intValue());
                }
                collList.setFixedLayout(true);
                vbox.appendChild((Component)collList);
            }
            parent.appendChild((Component)vbox);
        }
        else
        {
            LOG.error("Could not render section '" + section
                            .getLabel() + "'. Model is not an instance of NavigationAreaModel.");
        }
    }
}
