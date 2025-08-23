package de.hybris.platform.productcockpit.components.navigationarea.renderer;

import de.hybris.platform.cockpit.components.navigationarea.renderer.AbstractNavigationAreaSectionRenderer;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Vbox;

public abstract class AbstractCatalogNavigationAreaSectionRenderer extends AbstractNavigationAreaSectionRenderer
{
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


    public abstract void render(SectionPanel paramSectionPanel, Component paramComponent1, Component paramComponent2, Section paramSection);
}
