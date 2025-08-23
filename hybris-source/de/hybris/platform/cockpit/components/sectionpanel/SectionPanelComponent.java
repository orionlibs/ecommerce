package de.hybris.platform.cockpit.components.sectionpanel;

import de.hybris.platform.cockpit.components.StyledDiv;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.North;
import org.zkoss.zul.Div;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;

public class SectionPanelComponent extends Div
{
    public SectionPanelComponent()
    {
        setSclass("sectionPanelComponent");
        Menupopup menupopup = new Menupopup();
        menupopup.setParent((Component)this);
        setContext((Popup)menupopup);
        Menupopup menupopup2 = new Menupopup();
        menupopup2.setParent((Component)this);
        menupopup2.setId("infoContextMenu");
        Menuitem menuitem = new Menuitem(Labels.getLabel("sectionmenu.showallmessages"));
        menuitem.setParent((Component)menupopup2);
        menuitem.addEventListener("onClick", (EventListener)new Object(this));
        Borderlayout borderlayout = new Borderlayout();
        borderlayout.setParent((Component)this);
        borderlayout.setId("mainBorderlayout");
        borderlayout.setSclass("sectionPanelComponentBlayout");
        borderlayout.setStyle("background:transparent");
        borderlayout.setWidth("100%");
        borderlayout.setHeight("100%");
        North north = new North();
        north.setBorder("none");
        north.setParent((Component)borderlayout);
        StyledDiv div = new StyledDiv();
        div.setParent((Component)north);
        div.setId("infoContainer");
        div.setWidth("100%");
        div.setSclass("sectionmessagebox");
        div.setContext((Popup)menupopup2);
        Div div2 = new Div();
        div2.setParent((Component)div);
        div2.setId("messageContainer");
        div2.setWidth("100%");
        Div div3 = new Div();
        div3.setParent((Component)div);
        div3.setId("labelContainer");
        div3.setWidth("100%");
        div3.setSclass("sectionpaneltitle");
        Center center = new Center();
        center.setParent((Component)borderlayout);
        center.setAutoscroll(true);
        center.setBorder("none");
        Div panelContainer = new Div();
        panelContainer.setId("sectionContainerDiv");
        center.appendChild((Component)panelContainer);
    }
}
