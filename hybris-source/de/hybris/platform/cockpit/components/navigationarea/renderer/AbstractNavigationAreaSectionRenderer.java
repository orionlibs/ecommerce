package de.hybris.platform.cockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionRenderer;
import de.hybris.platform.cockpit.session.UINavigationArea;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Separator;
import org.zkoss.zul.SimpleListModel;
import org.zkoss.zul.Vbox;

public abstract class AbstractNavigationAreaSectionRenderer implements SectionRenderer
{
    private UINavigationArea navigationArea;


    public static Popup createBrowserItemTooltip(String string)
    {
        Popup ret = null;
        if(string != null)
        {
            int querySeparatorIndex = string.indexOf('|');
            ret = new Popup();
            if(querySeparatorIndex < 0)
            {
                ret.appendChild((Component)new Label(string.trim()));
            }
            else
            {
                String queryString = string.substring(0, querySeparatorIndex).trim();
                String[] catalogItems = (querySeparatorIndex < 0) ? string.split(", ") : string.substring(querySeparatorIndex + 1).split(", ");
                Vbox vbox = new Vbox();
                vbox.appendChild((Component)new Label("\"" + queryString + "\""));
                Separator sep = new Separator();
                sep.setBar(true);
                sep.setParent((Component)vbox);
                for(String catItemStr : catalogItems)
                {
                    vbox.appendChild((Component)new Label(catItemStr));
                }
                ret.appendChild((Component)vbox);
            }
        }
        return ret;
    }


    public UINavigationArea getNavigationArea()
    {
        return this.navigationArea;
    }


    public void setNavigationArea(UINavigationArea navigationArea)
    {
        this.navigationArea = navigationArea;
    }


    public SectionPanelModel getSectionPanelModel()
    {
        return getNavigationArea().getSectionModel();
    }


    protected Listbox createList(String sclass, List items, ListitemRenderer renderer)
    {
        if(items.isEmpty())
        {
            return null;
        }
        Listbox listBox = new Listbox();
        listBox.setItemRenderer(renderer);
        listBox.setSclass(sclass);
        SimpleListModel simpleListModel = new SimpleListModel(items);
        listBox.setModel((ListModel)simpleListModel);
        return listBox;
    }


    public abstract void render(SectionPanel paramSectionPanel, Component paramComponent1, Component paramComponent2, Section paramSection);
}
