package de.hybris.platform.cockpit.util;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Div;

public class DefaultLazyLoader extends Div implements LazyLoader
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultLazyLoader.class);
    private final transient List<Component> components = new ArrayList<>();
    private transient Component container;
    private EventListener lazyListener;


    public DefaultLazyLoader(Component container, List<Component> components)
    {
        this.container = container;
        this.components.clear();
        if(CollectionUtils.isNotEmpty(components))
        {
            this.components.addAll(components);
        }
    }


    public void loadComponents()
    {
        addEventListener("onLazyLoad", getLazyListener());
        Events.echoEvent("onLazyLoad", (Component)this, null);
    }


    public void setLazyLoadChildren(List<Component> children)
    {
        this.components.clear();
        if(CollectionUtils.isNotEmpty(children))
        {
            this.components.addAll(children);
        }
    }


    public void setLazyLoadParent(Component parent)
    {
        this.container = parent;
    }


    protected EventListener getLazyListener()
    {
        if(this.lazyListener == null)
        {
            this.lazyListener = (EventListener)new LazyListener(this);
        }
        return this.lazyListener;
    }
}
