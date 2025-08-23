package de.hybris.platform.cockpit.widgets.impl;

import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.WidgetContainer;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;
import de.hybris.platform.cockpit.widgets.models.WidgetModel;
import de.hybris.platform.cockpit.widgets.renderers.WidgetRenderer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.api.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public abstract class AbstractWidget<T extends WidgetModel, U extends WidgetController> extends Div implements Widget<T, U>
{
    protected static final String LAZY_LOAD_EVT_KEY = "onLazyLoad";
    private boolean initialized = false;
    private final transient Div mainComponent;
    private transient HtmlBasedComponent content;
    private transient HtmlBasedComponent caption;
    private String widgetCode;
    private String title;
    private U widgetController;
    private String controllerCtx;
    private WidgetRenderer widgetRenderer;
    private T model;
    private boolean lazyLoading = false;
    private transient HtmlBasedComponent lazyLoadContainer;
    private final transient List<HtmlBasedComponent> oldContentComponents = new ArrayList<>();
    private WidgetContainer container;
    private boolean focusable = true;


    public AbstractWidget()
    {
        this.mainComponent = new Div();
        appendChild((Component)this.mainComponent);
        this.mainComponent.setDynamicProperty("class", "mainWidgetComponent");
        addEventListener("onClick", (EventListener)new Object(this));
    }


    private HtmlBasedComponent createLazyLoadContainer()
    {
        Div lazyContainer = new Div();
        lazyContainer.addEventListener("onLazyLoad", (EventListener)new LazyLoadEventListener(this));
        lazyContainer.setSclass("widgetLazyLoadContainer");
        lazyContainer.appendChild((Component)new Label(Labels.getLabel("general.loading", "Loading")));
        return (HtmlBasedComponent)lazyContainer;
    }


    public void initialize(Map<String, Object> params)
    {
        this.initialized = true;
    }


    public boolean isInitialized()
    {
        return this.initialized;
    }


    public void handleFocus(boolean focused)
    {
        this.mainComponent.setDynamicProperty("class", focused ? "mainWidgetComponent focused" : "mainWidgetComponent");
    }


    public abstract void cleanup();


    public HtmlBasedComponent getContent()
    {
        return this.content;
    }


    protected void setContent(HtmlBasedComponent content)
    {
        if(this.content != content)
        {
            if(this.lazyLoading)
            {
                if(this.lazyLoadContainer != null && !UITools.isFromOtherDesktop((Component)this.lazyLoadContainer))
                {
                    this.lazyLoadContainer.detach();
                }
                this.lazyLoadContainer = createLazyLoadContainer();
                Events.echoEvent("onLazyLoad", (Component)this.lazyLoadContainer, null);
                this.mainComponent.appendChild((Component)this.lazyLoadContainer);
            }
            else if(this.content == null)
            {
                this.mainComponent.appendChild((Component)content);
            }
            else
            {
                HtmlBasedComponent oldContent = this.content;
                this.mainComponent.insertBefore((Component)content, (Component)oldContent);
                this.mainComponent.removeChild((Component)oldContent);
            }
            this.oldContentComponents.add(this.content);
            this.content = content;
        }
    }


    public HtmlBasedComponent getCaption()
    {
        return this.caption;
    }


    protected void setCaption(HtmlBasedComponent caption)
    {
        if(this.caption != caption)
        {
            if(this.caption == null)
            {
                this.mainComponent.appendChild((Component)caption);
            }
            else
            {
                HtmlBasedComponent oldCaption = this.caption;
                this.mainComponent.insertBefore((Component)caption, (Component)oldCaption);
                this.mainComponent.removeChild((Component)oldCaption);
            }
            this.caption = caption;
        }
    }


    public void setWidgetContainer(WidgetContainer container)
    {
        this.container = container;
    }


    public WidgetContainer getWidgetContainer()
    {
        return this.container;
    }


    public void setWidgetModel(T model)
    {
        this.model = model;
    }


    public T getWidgetModel()
    {
        return this.model;
    }


    public void setWidgetCode(String widgetCode)
    {
        this.widgetCode = widgetCode;
    }


    public String getWidgetCode()
    {
        return this.widgetCode;
    }


    public void setWidgetRenderer(WidgetRenderer widgetRenderer)
    {
        this.widgetRenderer = widgetRenderer;
    }


    public WidgetRenderer getWidgetRenderer()
    {
        return this.widgetRenderer;
    }


    public void setFocusable(boolean focusable)
    {
        this.focusable = focusable;
    }


    public boolean isFocusable()
    {
        return this.focusable;
    }


    public void setWidgetController(U widgetController)
    {
        this.widgetController = widgetController;
    }


    public U getWidgetController()
    {
        return this.widgetController;
    }


    public void setControllerCtx(String controllerCtx)
    {
        this.controllerCtx = controllerCtx;
    }


    public String getControllerCtx()
    {
        return this.controllerCtx;
    }


    public boolean isLazyLoadingEnabled()
    {
        return this.lazyLoading;
    }


    public void setLazyLoadingEnabled(boolean lazyLoading)
    {
        this.lazyLoading = lazyLoading;
    }


    public void setWidgetTitle(String title)
    {
        this.title = title;
    }


    public String getWidgetTitle()
    {
        return this.title;
    }
}
