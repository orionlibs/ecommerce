/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.common.simplelist;

import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.config.simplelist.jaxb.Description;
import com.hybris.cockpitng.config.simplelist.jaxb.Image;
import com.hybris.cockpitng.config.simplelist.jaxb.Name;
import com.hybris.cockpitng.config.simplelist.jaxb.SimpleList;
import com.hybris.cockpitng.core.async.Operation;
import com.hybris.cockpitng.core.async.Progress;
import com.hybris.cockpitng.core.async.Progress.ProgressType;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationNotFoundException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.engine.CockpitWidgetEngine;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.i18n.LocalizedValuesService;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.util.DefaultWidgetController;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

/**
 * Controller of the simple list widget.
 *
 * @deprecated since 6.6 use {@link com.hybris.cockpitng.widgets.collectionbrowser.CollectionBrowserController}
 */
@Deprecated(since = "6.6", forRemoval = true)
public class SimpleListController extends DefaultWidgetController
{
    public static final String SOCKET_IN_PAGEABLELIST = "pageablelist";
    public static final String SOCKET_IN_PAGEABLE = "pageable";
    public static final String SOCKET_OUT_SELECTEDITEM = "selecteditem";
    public static final String SETTING_VIEW_MODE = "viewMode";
    public static final String SETTING_PAGE_SIZE = "pageSize";
    public static final String SETTING_CONFIG_CONTEXT = "configContext";
    public static final String SETTING_ASYNC = "async";
    public static final String SETTING_MAX_ENTRY_LIMIT = "maxEntryLimit";
    protected static final String ENTRY_NAME = "entryName";
    protected static final String ENTRY_IMG_URL = "entryImgURL";
    protected static final String ENTRY_DESCR = "entryDescr";
    protected static final String ENTRY_IMG_FALLBACK = "entryImgFallback";
    protected static final String CONFIG_CTX_SIMPLE_LIST = "simple-list";
    protected static final String COMP_ID_GRIDLIST = "gridList";
    protected static final String VIEW_MODE_LIST = "List";
    protected static final String VIEW_MODE_GRID = "Grid";
    protected static final String MODEL_PAGEABLE = "pageable";
    protected static final String MODEL_PAGEABLE_LIST = "pageableList";
    private static final String DEFAULT_RENDERER_TEMPLATE = "/defaultDataRendererTemplate.zul";
    private static final String LABEL_SHOWMORE = "showmore";
    private static final String LABEL_LOADING = "loading";
    private static final long serialVersionUID = 6369447376098281521L;
    private static final Logger LOG = LoggerFactory.getLogger(SimpleListController.class);
    private transient ObjectValueService objectValueService;
    @WireVariable
    private transient CockpitLocaleService cockpitLocaleService;
    @WireVariable
    private transient LocalizedValuesService localizedValuesService;
    private Component gridContainer;
    private Listbox gridList;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        final Pageable<Object> pageable = getValue(MODEL_PAGEABLE, Pageable.class);
        final List<Object> objectList = getValue(MODEL_PAGEABLE_LIST, List.class);
        if(pageable != null && objectList != null)
        {
            render(pageable, objectList);
        }
    }


    /**
     * Called whenever a socket event is sent to the {@value #SOCKET_IN_PAGEABLE} socket. Renders the entries in the
     * pageable object (if any) by invoking {@link #render(Pageable, List)}.
     *
     * @param pageable
     *           the pageable object to render
     */
    @SocketEvent(socketId = SOCKET_IN_PAGEABLE)
    public void setPageable(final Pageable<?> pageable)
    {
        if(pageable != null)
        {
            if(getWidgetSettings().getBoolean(SETTING_ASYNC))
            {
                executeOperation(new Operation()
                {
                    @Override
                    public boolean isTerminable()
                    {
                        return false;
                    }


                    @Override
                    public ProgressType getProgressType()
                    {
                        return ProgressType.FAKED;
                    }


                    @Override
                    public String getLabel()
                    {
                        return SimpleListController.this.getLabel(LABEL_LOADING);
                    }


                    @Override
                    public Object execute(final Progress progress)
                    {
                        return pageable.getCurrentPage();
                    }
                }, evt -> render(pageable, (List<?>)evt.getData()), getLabel(LABEL_LOADING));
            }
            else
            {
                render(pageable, pageable.getCurrentPage());
            }
        }
    }


    /**
     * Called whenever a socket event is sent to the {@value #SOCKET_IN_PAGEABLELIST} socket. Creates a new
     * {@link PageableList} object using the provided list <p>objectList</p> and the page size defined by the
     * {@value #SETTING_PAGE_SIZE} setting and then passes it on to {@link #setPageable(Pageable)}.
     *
     * @param objectList
     *           the list of objects to be rendered
     */
    @SocketEvent(socketId = SOCKET_IN_PAGEABLELIST)
    public void setList(final List<?> objectList)
    {
        if(CollectionUtils.isNotEmpty(objectList))
        {
            setPageable(new PageableList<>(objectList, getWidgetSettings().getInt(SETTING_PAGE_SIZE)));
        }
    }


    /**
     * Renders the provided entries as either a <i>list</i> or a <i>grid</i> depending on the value of the setting
     * {@value #SETTING_VIEW_MODE} (list is rendered if <p>true</p>, a grid is rendered otherwise).
     *
     * @param pageable
     *           used to resolve the relevant <i>type code</i> (if any) and other paging information
     * @param genericObjectList
     *           the objects to be rendered
     */
    protected void render(final Pageable<?> pageable, final List<?> genericObjectList)
    {
        // update widget model (so that the state can be retained across requests)
        getModel().put(MODEL_PAGEABLE, pageable);
        getModel().put(MODEL_PAGEABLE_LIST, genericObjectList);
        if(VIEW_MODE_LIST.equals(getWidgetSettings().getString(SETTING_VIEW_MODE)) && gridList != null)
        {
            gridList.getChildren().clear();
            if(CollectionUtils.isNotEmpty(pageable.getCurrentPage()))
            {
                final Listitem buttonListItem = createButtonListItem(pageable);
                gridList.appendChild(buttonListItem);
                buttonListItem.setVisible(pageable.hasNextPage());
                renderNewItems(pageable.getTypeCode(), genericObjectList, buttonListItem);
            }
            gridList.invalidate();
        }
        else if(gridContainer != null)
        {
            gridContainer.getChildren().clear();
            final Button showMoreButton = createShowMoreButton(pageable, null);
            gridContainer.appendChild(showMoreButton);
            renderNewItems(pageable.getTypeCode(), genericObjectList, showMoreButton);
            gridContainer.invalidate();
        }
    }


    private void renderNewItems(final String typeCode, final List<?> genericObjectList, final Component showMoreButton)
    {
        showMoreButton.detach();
        final Component aggregateComponent = resolveContainerComponent();
        if(aggregateComponent == null)
        {
            LOG.error("Aggregate component could not be resolved. Not rendering entries.");
        }
        else
        {
            appendNewItems(aggregateComponent, genericObjectList, loadConfiguration(typeCode));
            aggregateComponent.appendChild(showMoreButton);
        }
    }


    private void appendNewItems(final Component aggregateComponent, final List<?> genericObjectList, final SimpleList config)
    {
        if(CollectionUtils.isNotEmpty(genericObjectList))
        {
            final int maxLimit = getWidgetSettings().getInt(SETTING_MAX_ENTRY_LIMIT);
            if(VIEW_MODE_LIST.equals(getWidgetSettings().getString(SETTING_VIEW_MODE)))
            {
                for(int i = 0; i < genericObjectList.size(); ++i)
                {
                    logReachingMax(i, maxLimit);
                    aggregateComponent.appendChild(wrapListItem(createEntry(genericObjectList.get(i), config)));
                }
            }
            else
            {
                for(int i = 0; i < genericObjectList.size(); ++i)
                {
                    logReachingMax(i, maxLimit);
                    aggregateComponent.appendChild(createEntry(genericObjectList.get(i), config));
                }
            }
        }
    }


    protected static void logReachingMax(final int counter, final int limit)
    {
        if(counter > limit)
        {
            LOG.info("Rendering has reached defined max entry limit of '{}'. Skipping further entries.", limit);
        }
    }


    /**
     * Resolves and returns the container component used when rendering entries. Which component is returned depends on
     * the value of the setting {@value #SETTING_VIEW_MODE}.
     *
     * @return the container component
     */
    protected Component resolveContainerComponent()
    {
        return VIEW_MODE_LIST.equals(getWidgetSettings().getString(SETTING_VIEW_MODE)) ? gridList : gridContainer;
    }


    protected Button createShowMoreButton(final Pageable<?> pageable, final Component parent)
    {
        final Button showMoreButton = new Button(getLabel(LABEL_SHOWMORE));
        showMoreButton.setVisible(pageable.hasNextPage());
        showMoreButton.addEventListener(Events.ON_CLICK, new EventListener()
        {
            @Override
            public void onEvent(final Event event)
            {
                if(pageable.hasNextPage())
                {
                    renderNewItems(pageable.getTypeCode(), pageable.nextPage(), parent == null ? event.getTarget() : parent);
                    event.getTarget().setVisible(pageable.hasNextPage());
                }
            }
        });
        return showMoreButton;
    }


    private Listitem createButtonListItem(final Pageable<?> pageable)
    {
        final Listitem buttonListItem = new Listitem();
        final Listcell cell = new Listcell();
        buttonListItem.appendChild(cell);
        final Button showMoreButton = createShowMoreButton(pageable, buttonListItem);
        cell.appendChild(showMoreButton);
        return buttonListItem;
    }


    private static Listitem wrapListItem(final Component component)
    {
        final Listitem buttonListItem = new Listitem();
        final Listcell cell = new Listcell();
        buttonListItem.appendChild(cell);
        cell.appendChild(component);
        return buttonListItem;
    }


    protected Component createEntry(final Object entry, final SimpleList config)
    {
        final String templateLocation = "/" + getWidgetslot().getAttribute(CockpitWidgetEngine.WIDGET_RESOURCE_PATH_PARAM)
                        + DEFAULT_RENDERER_TEMPLATE;
        final Div gridItemContainer = new Div();
        // get arguments to be passed to zul (renderer) template
        final Map<String, Object> args = getRendererProps(entry, config);
        try(final InputStream resource = this.getClass().getResourceAsStream(templateLocation))
        {
            if(resource == null)
            {
                LOG.error("File not found: {}", templateLocation);
            }
            else
            {
                try(final InputStreamReader is = new InputStreamReader(resource, Charset.defaultCharset().toString()))
                {
                    Executions.createComponentsDirectly(is, null, gridItemContainer, args);
                }
            }
        }
        catch(final IOException e)
        {
            LOG.error("File not found exception", e);
        }
        gridItemContainer.addEventListener(Events.ON_CLICK, new EventListener<Event>()
        {
            @Override
            public void onEvent(final Event event)
            {
                sendOutput(SOCKET_OUT_SELECTEDITEM, entry);
            }
        });
        return gridItemContainer;
    }


    /**
     * Loads the UI configuration for component with code {@value #CONFIG_CTX_SIMPLE_LIST} and for the specified type,
     * <p>typeCode</p>.
     *
     * @param typeCode
     *           the type code for which the configuration should be loaded
     * @return the configuration object if found, <p>null</p> otherwise
     */
    protected SimpleList loadConfiguration(final String typeCode)
    {
        SimpleList config = null;
        final DefaultConfigContext configContext = new DefaultConfigContext(CONFIG_CTX_SIMPLE_LIST);
        configContext.setType(typeCode);
        try
        {
            config = getWidgetInstanceManager().loadConfiguration(configContext, SimpleList.class);
            if(config == null)
            {
                LOG.warn("Loaded UI configuration is null. Ignoring.");
            }
        }
        catch(final CockpitConfigurationNotFoundException ccnfe)
        {
            LOG.info("Could not find UI configuration for given context (" + configContext + ").", ccnfe);
        }
        catch(final CockpitConfigurationException cce)
        {
            LOG.error("Could not load cockpit config for the given context '" + configContext + "'.", cce);
        }
        return config;
    }


    /**
     * Returns a map with all the key/value pairs which should be passed to the zul template used for rendering.
     *
     * @param entry
     *           the object to be rendered
     * @param config
     *           the UI configuration to be used when rendering
     * @return map with renderer properties, which are accessible in the zul template (using
     *         <p>${args.&lt;KEY_NAME&gt;}</p> )
     */
    protected Map<String, Object> getRendererProps(final Object entry, final SimpleList config)
    {
        final Map<String, Object> rendererProps = new HashMap<String, Object>();
        if(config == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("No UI config provided. Using toString() and default image.");
            }
            rendererProps.put(ENTRY_NAME, entry.toString());
            rendererProps.put(ENTRY_IMG_FALLBACK, true);
        }
        else
        {
            final Object nameValue = getNameValue(config.getName(), entry);
            final Object descrValue = getDescriptionValue(config.getDescription(), entry);
            final Object imgValue = getImageValue(config.getImage(), entry);
            rendererProps.put(ENTRY_NAME, nameValue == null ? null : nameValue.toString());
            rendererProps.put(ENTRY_DESCR, descrValue == null ? null : descrValue.toString());
            if(imgValue == null)
            {
                rendererProps.put(ENTRY_IMG_FALLBACK, true);
            }
            else
            {
                rendererProps.put(ENTRY_IMG_URL, imgValue);
            }
        }
        return rendererProps;
    }


    protected Object getNameValue(final Name name, final Object entry)
    {
        if(name != null && StringUtils.isNotBlank(name.getField()))
        {
            final Object nameValue = getObjectValueService().getValue(name.getField(), entry);
            return getLocalizedValue(nameValue);
        }
        return null;
    }


    protected Object getDescriptionValue(final Description descr, final Object entry)
    {
        if(descr != null && StringUtils.isNotBlank(descr.getField()))
        {
            final Object descrValue = getObjectValueService().getValue(descr.getField(), entry);
            return getLocalizedValue(descrValue);
        }
        return null;
    }


    protected Object getImageValue(final Image img, final Object entry)
    {
        if(img != null && StringUtils.isNotBlank(img.getField()))
        {
            return getObjectValueService().getValue(img.getField(), entry);
        }
        return null;
    }


    protected Object getLocalizedValue(final Object value)
    {
        if(value instanceof Map)
        {
            return localizedValuesService.getCurrentValue(cockpitLocaleService.getCurrentLocale(), (Map<Locale, Object>)value);
        }
        return value;
    }


    /**
     * Call back method for select events. Resolves the selected object and sends it out on the outgoing socket called
     * {@value #SOCKET_OUT_SELECTEDITEM}.
     *
     * @param event
     *           the select event
     */
    @ViewEvent(componentID = COMP_ID_GRIDLIST, eventName = Events.ON_SELECT)
    public void selectEntry(final SelectEvent<Component, ?> event)
    {
        final Set<?> selectedObjects = event.getSelectedObjects();
        if(CollectionUtils.isNotEmpty(selectedObjects))
        {
            final Object selectedObject = selectedObjects.iterator().next();
            sendOutput(SOCKET_OUT_SELECTEDITEM, selectedObject);
        }
    }


    protected ObjectValueService getObjectValueService()
    {
        return objectValueService;
    }


    public void setObjectValueService(final ObjectValueService objectValueService)
    {
        this.objectValueService = objectValueService;
    }


    public Component getGridContainer()
    {
        return gridContainer;
    }


    public Listbox getGridList()
    {
        return gridList;
    }


    public CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    public LocalizedValuesService getLocalizedValuesService()
    {
        return localizedValuesService;
    }


    public void setLocalizedValuesService(final LocalizedValuesService localizedValuesService)
    {
        this.localizedValuesService = localizedValuesService;
    }
}
