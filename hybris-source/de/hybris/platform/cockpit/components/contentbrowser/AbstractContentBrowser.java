package de.hybris.platform.cockpit.components.contentbrowser;

import de.hybris.platform.cockpit.components.listview.ActionColumnConfiguration;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropWrapper;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.util.DesktopRemovalAwareComponent;
import de.hybris.platform.cockpit.util.UITools;
import java.util.Set;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;

public abstract class AbstractContentBrowser extends Div implements DesktopRemovalAwareComponent
{
    public static final String CONTENT_BROWSER_CHANGE = "onContentBrowserChange";
    public static final String DEFAULT_SCLASS = "contentBrowser";
    public static final String FOCUSED_SCLASS = "contentBrowserFocused";
    public static final String DEFAULT_OVERLAY_SCLASS = "contentBrowserOverlay";
    public static final String FOCUSED_OVERLAY_SCLASS = "contentBrowserFocusedOverlay";
    private String sclass = null;
    private BrowserModel model = null;
    private boolean activeWhenUnfocused = false;
    protected transient AbstractBrowserComponent captionComponent = null;
    protected transient AbstractBrowserComponent toolbarComponent = null;
    protected transient AbstractBrowserComponent mainAreaComponent = null;
    protected transient AbstractBrowserComponent contextAreaComponent = null;
    protected transient Div statusBar = null;
    protected boolean initialized = false;
    private HtmlBasedComponent focusComponent = null;
    private HtmlBasedComponent contentBrowserComponent = null;


    public void setFocus(boolean focus)
    {
        if(!UITools.isFromOtherDesktop((Component)this))
        {
            if(this.focusComponent != null && !isActiveWhenUnfocused())
            {
                this.focusComponent.setSclass(focus ? "contentBrowserFocusedOverlay" : "contentBrowserOverlay");
            }
            if(getFirstChild() instanceof BrowserFocusDiv)
            {
                ((BrowserFocusDiv)getFirstChild()).setFocus(focus);
            }
        }
    }


    protected DragAndDropWrapper getDDWrapper()
    {
        return getModel().getArea().getPerspective().getDragAndDropWrapperService().getWrapper();
    }


    public void setSclass(String sclass)
    {
        this.sclass = sclass;
        if(getContentBrowserComponent() != null && !UITools.isFromOtherDesktop((Component)getContentBrowserComponent()))
        {
            getContentBrowserComponent().setSclass(sclass);
        }
    }


    public void setRealSclass(String sclass)
    {
        if(!UITools.isFromOtherDesktop((Component)this))
        {
            super.setSclass(sclass);
        }
    }


    public void setFocusComponent(HtmlBasedComponent focusComponent)
    {
        this.focusComponent = focusComponent;
        if(getModel() != null)
        {
            setFocus(getModel().isFocused());
        }
    }


    protected HtmlBasedComponent getFocusComponent()
    {
        return this.focusComponent;
    }


    public void setContentBrowserComponent(HtmlBasedComponent contentBrowserComponent)
    {
        this.contentBrowserComponent = contentBrowserComponent;
        if(this.contentBrowserComponent != null)
        {
            this.contentBrowserComponent.setSclass(this.sclass);
            Events.postEvent("onContentBrowserChange", (Component)this.contentBrowserComponent, null);
        }
    }


    protected HtmlBasedComponent getContentBrowserComponent()
    {
        return this.contentBrowserComponent;
    }


    public BrowserModel getModel()
    {
        return this.model;
    }


    public void setModel(BrowserModel model)
    {
        this.model = model;
        if(this.model != null)
        {
            setFocus(this.model.isFocused());
            initialize();
        }
    }


    public abstract void updateActiveItems();


    public abstract void updateSelectedItems();


    public abstract void updateItem(TypedObject paramTypedObject, Set<PropertyDescriptor> paramSet, Object paramObject);


    public void updateItem(TypedObject item, Set<PropertyDescriptor> modifiedProperties)
    {
        updateItem(item, modifiedProperties, null);
    }


    public abstract void updateActivation();


    public abstract void updateMainArea();


    public abstract void updateViewMode();


    public abstract void updateContextArea();


    public abstract void updateCaption();


    public abstract void updateToolbar();


    public abstract boolean update();


    protected abstract boolean initialize();


    public abstract void resize();


    public void updateStatusBar()
    {
    }


    public void setActiveWhenUnfocused(boolean activeWhenUnfocused)
    {
        this.activeWhenUnfocused = activeWhenUnfocused;
    }


    public boolean isActiveWhenUnfocused()
    {
        return this.activeWhenUnfocused;
    }


    public boolean isInitialized()
    {
        return this.initialized;
    }


    public AbstractBrowserComponent getCaptionComponent()
    {
        return this.captionComponent;
    }


    public AbstractBrowserComponent getToolbarComponent()
    {
        return this.toolbarComponent;
    }


    public AbstractBrowserComponent getMainAreaComponent()
    {
        return this.mainAreaComponent;
    }


    public AbstractBrowserComponent getContextAreaComponent()
    {
        return this.contextAreaComponent;
    }


    public ActionColumnConfiguration getActionConfiguration()
    {
        return null;
    }


    public void setParent(Component parent)
    {
        super.setParent(parent);
        if(parent == null)
        {
            cleanup();
        }
    }


    public void detach()
    {
        super.detach();
        cleanup();
    }


    public void desktopRemoved(Desktop desktop)
    {
        this.focusComponent = null;
        cleanup();
    }


    protected void cleanup()
    {
        if(this.captionComponent != null)
        {
            this.captionComponent.detach();
        }
        if(this.toolbarComponent != null)
        {
            this.toolbarComponent.detach();
        }
        if(this.mainAreaComponent != null)
        {
            this.mainAreaComponent.detach();
        }
        if(this.contextAreaComponent != null)
        {
            this.contextAreaComponent.detach();
        }
    }
}
