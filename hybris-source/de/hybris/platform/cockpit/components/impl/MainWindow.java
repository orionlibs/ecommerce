package de.hybris.platform.cockpit.components.impl;

import de.hybris.platform.cockpit.components.PushComponent;
import de.hybris.platform.cockpit.session.CockpitHotKeyHandler;
import de.hybris.platform.cockpit.session.PushController;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.UISession;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.DefaultHotKeyHandler;
import de.hybris.platform.cockpit.session.impl.PushControllerCreator;
import de.hybris.platform.cockpit.session.impl.PushCreationContainer;
import de.hybris.platform.cockpit.util.UITools;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Window;

public class MainWindow extends Window
{
    private static final Logger LOG = LoggerFactory.getLogger(MainWindow.class);
    private CockpitHotKeyHandler keyHandler;


    public MainWindow()
    {
        setShadow(false);
        Checkbox checkbox = new Checkbox();
        checkbox.setParent((Component)this);
        checkbox.setName("globalKeyHandlerComponent");
        checkbox.setVisible(false);
        CockpitHotKeyHandler hotKeyHandler = getHotKeyHandler();
        Object object = new Object(this, hotKeyHandler);
        if(hotKeyHandler.isBusyListener())
        {
            UITools.addBusyListener((Component)checkbox, "onUser", (EventListener)object, null, null);
        }
        else
        {
            checkbox.addEventListener("onUser", (EventListener)object);
        }
        Div perspectiveContainer = new Div();
        perspectiveContainer.setParent((Component)this);
        perspectiveContainer.setAlign("center");
        perspectiveContainer.setWidth("100%");
        perspectiveContainer.setHeight("100%");
        perspectiveContainer.setStyle("background:none");
        perspectiveContainer.addEventListener("onPerspectiveChanged", (EventListener)new Object(this, perspectiveContainer));
        Map<String, String[]> reqParams = (Map)new HashMap<>();
        initializePushComponents();
        addEventListener("onCreate", (EventListener)new Object(this, reqParams, perspectiveContainer));
        addEventListener("onCreateLater", (EventListener)new Object(this, reqParams));
    }


    protected void createPerspectiveComponent(UICockpitPerspective perspective, Component parent)
    {
        Component perspectiveComponent = Executions.createComponents(perspective.getViewURI(), parent, null);
        perspectiveComponent.setAttribute("perspective", perspective);
    }


    protected CockpitHotKeyHandler getHotKeyHandler()
    {
        if(this.keyHandler == null)
        {
            try
            {
                this.keyHandler = (CockpitHotKeyHandler)SpringUtil.getBean("cockpitHotKeyHandler");
            }
            catch(Exception e)
            {
                LOG.info("Hot key handler could not be loaded. Default one will be used.", e);
            }
            if(this.keyHandler == null)
            {
                this.keyHandler = (CockpitHotKeyHandler)new DefaultHotKeyHandler();
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Default hot key handler created.");
                }
            }
        }
        return this.keyHandler;
    }


    protected void initializePushComponents()
    {
        UISession session = UISessionUtils.getCurrentSession();
        for(PushCreationContainer creationContainer : session.getPushContainers())
        {
            PushController pushController = null;
            try
            {
                pushController = PushControllerCreator.createPushController(creationContainer);
            }
            catch(Exception e)
            {
                LOG.error("Error occurred while initializing push controller.", e);
                continue;
            }
            if(pushController != null)
            {
                appendChild((Component)new PushComponent(pushController));
            }
        }
    }
}
