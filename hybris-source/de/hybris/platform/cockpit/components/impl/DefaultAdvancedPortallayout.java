package de.hybris.platform.cockpit.components.impl;

import de.hybris.platform.cockpit.components.AdvancedPortallayout;
import de.hybris.platform.cockpit.util.UITools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zkmax.zul.Portalchildren;
import org.zkoss.zkmax.zul.Portallayout;
import org.zkoss.zul.Panel;

public class DefaultAdvancedPortallayout extends Portallayout implements AdvancedPortallayout
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAdvancedPortallayout.class);
    List<Portalchildren> portalchildren = new ArrayList<>();


    public DefaultAdvancedPortallayout(int columns)
    {
        if(columns > 0)
        {
            String width = "" + Math.round(100.0F / columns) + "%";
            for(int i = 0; i < columns; i++)
            {
                Portalchildren portalcolumn = new Portalchildren();
                portalcolumn.setWidth(width);
                super.insertBefore((Component)portalcolumn, null);
                this.portalchildren.add(portalcolumn);
            }
        }
    }


    public void setWidths(String widths)
    {
        try
        {
            String[] widthArray = widths.split(",");
            UITools.setComponentWidths(widthArray, (HtmlBasedComponent[])this.portalchildren.toArray((Object[])new Portalchildren[0]));
        }
        catch(Exception e)
        {
            LOG.error("Could not set widths, reason:", e);
        }
    }


    public void addPanel(Panel panel, int column)
    {
        Portalchildren pchild = this.portalchildren.get(column);
        pchild.appendChild((Component)panel);
    }


    public boolean insertBefore(Component newChild, Component refChild)
    {
        LOG.error("Use addPanel() to add children to AdvancedPortallayout");
        return super.insertBefore(newChild, refChild);
    }


    public int getChildCount(int column)
    {
        if(column < this.portalchildren.size())
        {
            return ((Portalchildren)this.portalchildren.get(column)).getChildren().size();
        }
        return -1;
    }


    public Map<Panel, int[]> getPositions()
    {
        Map<Panel, int[]> ret = (Map)new HashMap<>();
        try
        {
            for(int i = 0; i < this.portalchildren.size(); i++)
            {
                Portalchildren pchild = this.portalchildren.get(i);
                for(int j = 0; j < pchild.getChildren().size(); j++)
                {
                    Panel panel = pchild.getChildren().get(j);
                    ret.put(panel, new int[] {i, j});
                }
            }
        }
        catch(Exception e)
        {
            LOG.warn("Could not get positions, reason: ", e);
        }
        return ret;
    }
}
