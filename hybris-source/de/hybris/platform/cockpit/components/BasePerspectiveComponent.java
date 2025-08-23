package de.hybris.platform.cockpit.components;

import de.hybris.platform.cockpit.components.notifier.NotifierZKComponent;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.UITools;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkex.zul.Borderlayout;
import org.zkoss.zkex.zul.Center;
import org.zkoss.zkex.zul.East;
import org.zkoss.zkex.zul.West;
import org.zkoss.zul.Caption;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

public class BasePerspectiveComponent extends Div
{
    private static final Logger LOG = LoggerFactory.getLogger(BasePerspectiveComponent.class);


    public BasePerspectiveComponent()
    {
        UISession session = UISessionUtils.getCurrentSession();
        UICockpitPerspective perspective = session.getCurrentPerspective();
        NotifierZKComponent notifier = new NotifierZKComponent();
        notifier.setParent((Component)this);
        notifier.setSclass("productNotifier");
        notifier.setDynamicProperty("delay", "2000");
        notifier.setDynamicProperty("duration", "8500");
        notifier.afterCompose();
        session.getCurrentPerspective().setNotifier(notifier);
        Borderlayout borderlayout = new Borderlayout();
        borderlayout.setParent((Component)this);
        borderlayout.setWidth("100%");
        borderlayout.setHeight("100%");
        borderlayout.setStyle("background:none");
        West west = new West();
        west.setParent((Component)borderlayout);
        String navWidth = "265px";
        String navWidthParam = UITools.getCockpitParameter("default.navigationarea.width", Executions.getCurrent());
        if(navWidthParam != null)
        {
            navWidth = navWidthParam;
        }
        if(perspective.getNavigationArea() != null && StringUtils.isNotBlank(perspective.getNavigationArea().getWidth()))
        {
            navWidth = perspective.getNavigationArea().getWidth();
        }
        west.setWidth(navWidth);
        west.setBorder("none");
        west.setSclass("navigationarea cockpitarea");
        west.setSplittable(true);
        west.setMinsize(100);
        west.setCollapsible(true);
        west.setTitle(" ");
        west.setVisible((perspective.getNavigationArea() != null));
        Center center = new Center();
        center.setParent((Component)borderlayout);
        center.setBorder("none");
        center.setMargins("0, 0, 0, 0");
        center.setAutoscroll(false);
        center.setFlex(false);
        East east = new East();
        east.setParent((Component)borderlayout);
        String editWidth = "26%";
        String editorWidthParam = UITools.getCockpitParameter("default.editorarea.width", Executions.getCurrent());
        if(editorWidthParam != null)
        {
            editWidth = editorWidthParam;
        }
        if(perspective.getEditorArea() != null && StringUtils.isNotBlank(perspective.getEditorArea().getWidth()))
        {
            editWidth = perspective.getEditorArea().getWidth();
        }
        east.setWidth(editWidth);
        east.setBorder("none");
        east.setMargins("0, 0, 0, 0");
        east.setTitle(" ");
        east.setCollapsible(true);
        east.setSplittable(true);
        east.setSclass("editorarea cockpitarea");
        east.setMinsize(100);
        east.setOpen(false);
        east.setVisible((perspective.getEditorArea() != null));
        east.addEventListener("onPerspectiveChanged", (EventListener)new Object(this, east));
        Window popupwindow = new Window();
        popupwindow.setParent((Component)this);
        popupwindow.setSclass("popupwindow");
        popupwindow.setVisible(false);
        popupwindow.setWidth("590px");
        popupwindow.setHeight("590px");
        popupwindow.setBorder("none");
        popupwindow.setClosable(true);
        popupwindow.setMaximizable(false);
        popupwindow.setSizable(false);
        popupwindow.setShadow(false);
        popupwindow.setAction("onhide: anima.fade(#{self}); onshow: anima.appear(#{self});");
        Caption caption = new Caption();
        caption.setParent((Component)popupwindow);
        popupwindow.addEventListener("onClose", (EventListener)new Object(this, popupwindow));
        popupwindow.addEventListener("onPerspectiveChanged", (EventListener)new Object(this, popupwindow));
        addEventListener("onCreate", (EventListener)new Object(this, perspective, center, west, east, popupwindow));
    }
}
