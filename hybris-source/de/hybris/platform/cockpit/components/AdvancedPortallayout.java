package de.hybris.platform.cockpit.components;

import java.util.Map;
import org.zkoss.zkmax.zul.api.Portallayout;
import org.zkoss.zul.Panel;

public interface AdvancedPortallayout extends Portallayout
{
    void setWidths(String paramString);


    void addPanel(Panel paramPanel, int paramInt);


    int getChildCount(int paramInt);


    Map<Panel, int[]> getPositions();
}
