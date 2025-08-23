/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.collectionpreview;

import com.google.common.collect.Lists;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.core.CockpitComponentDefinitionService;
import com.hybris.cockpitng.core.Widget;
import com.hybris.cockpitng.core.WidgetDefinition;
import com.hybris.cockpitng.core.async.Operation;
import com.hybris.cockpitng.core.async.Progress;
import com.hybris.cockpitng.core.async.Progress.ProgressType;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationNotFoundException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.ui.WidgetInstance;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.search.data.pageable.Pageable;
import com.hybris.cockpitng.search.data.pageable.PageableList;
import com.hybris.cockpitng.services.media.ObjectPreview;
import com.hybris.cockpitng.services.media.ObjectPreviewService;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.type.BackofficeTypeUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Div;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;

/**
 * Controller of the collection preview widget - responsible for rendering a preview for each item from a passed
 * {@link com.hybris.cockpitng.search.data.pageable.Pageable} pageable
 * <p>
 * . In order to setPage a comprehensive item representation controller relies on
 * {@link com.hybris.cockpitng.core.config.impl.jaxb.hybris.Base} configuration where one can specified its name,
 * description and a graphical representation.
 * </p>
 */
public class CollectionPreviewController extends DefaultWidgetController
{
    public static final String SOCKET_IN_PAGEABLE = "pageable";
    public static final String SOCKET_SELECTED_ITEM = "selectedItem";
    public static final String WIDGET_SETTING_CONFIG_CONTEXT = "configContext";
    public static final String WIDGET_SETTING_ASYNC = "async";
    protected static final String ENTRY_NAME = "entryName";
    protected static final String ENTRY_DESCR = "entryDescr";
    protected static final String ENTRY_IMG_URL = "entryImgURL";
    protected static final String ENTRY_IMG_FALLBACK = "entryImgFallback";
    protected static final String YW_COLLECTION_PREVIEW_EMPTY_LIST = "yw-collection-preview-empty-list";
    protected static final String COMP_ID_ITEMLIST = "itemList";
    protected static final String MODEL_PAGEABLE = "pageable";
    protected static final String MODEL_PAGEABLE_LIST = "pageableList";
    protected static final String MODEL_ITEM_SELECTED_INDEX = "itemSelectedIndex";
    private static final String DEFAULT_RENDERER_TEMPLATE = "/itemTemplate.zul";
    private static final Logger LOG = LoggerFactory.getLogger(CollectionPreviewController.class);
    private final transient Object mutex = new Object();
    @WireVariable
    private transient ObjectPreviewService objectPreviewService;
    @WireVariable
    private transient LabelService labelService;
    @WireVariable
    private transient TypeFacade typeFacade;
    @WireVariable
    private transient CockpitComponentDefinitionService componentDefinitionService;
    @WireVariable
    private transient BackofficeTypeUtils backofficeTypeUtils;
    @Wire
    private Listbox itemList;
    private boolean renderingInProgress;


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
     * pageable object (if any)<br>
     * <br>
     * Thread-safety is provided
     *
     * @param pageable
     *           the pageable object to setPage
     */
    @SocketEvent(socketId = SOCKET_IN_PAGEABLE)
    public void setPageable(final Pageable<?> pageable)
    {
        setPageable(pageable, false);
    }


    /**
     * Renders the entries in the pageable object (if any)<br>
     * <br>
     * Thread-safety is provided
     *
     * @param pageable
     *           the pageable object to setPage
     * @param synch
     *           when true renders entries synchronously
     */
    protected void setPageable(final Pageable<?> pageable, final boolean synch)
    {
        if(pageable != null)
        {
            synchronized(mutex)
            {
                getModel().setValue(MODEL_PAGEABLE, pageable);
                final List<?> allElements = convertAllPagesIntoSingleList(pageable);
                getModel().setValue(MODEL_PAGEABLE_LIST, allElements);
                UITools.modifySClass(itemList, YW_COLLECTION_PREVIEW_EMPTY_LIST, CollectionUtils.isEmpty(allElements));
                if(getWidgetSettings().getBoolean(WIDGET_SETTING_ASYNC) && !synch)
                {
                    renderingInProgress = true;
                    executeOperation(createLoadingOperation(), evt -> {
                        render(pageable, (List<?>)evt.getData());
                        selectItem(getValue(MODEL_ITEM_SELECTED_INDEX, Integer.class));
                    }, getLabel("loading"));
                }
                else
                {
                    render(pageable, getValue(MODEL_PAGEABLE_LIST, List.class));
                }
            }
        }
    }


    @InextensibleMethod
    private Operation createLoadingOperation()
    {
        return new LoadingOperation();
    }


    /**
     * Call back method for select events. Resolves the selected object and sends it out on the outgoing socket called
     * {@value #SOCKET_SELECTED_ITEM}.
     *
     * @param event
     *           the select event
     */
    @ViewEvent(componentID = COMP_ID_ITEMLIST, eventName = Events.ON_SELECT)
    public void selectEntry(final SelectEvent<Component, ?> event)
    {
        final Set<?> selectedObjects = event.getSelectedObjects();
        if(CollectionUtils.isNotEmpty(selectedObjects))
        {
            final Object selectedObject = selectedObjects.iterator().next();
            sendOutput(SOCKET_SELECTED_ITEM, selectedObject);
        }
    }


    @SocketEvent(socketId = SOCKET_SELECTED_ITEM)
    public void setSelectedItem(final Object itemToSelect)
    {
        final List<?> genericObjectList = getValue(MODEL_PAGEABLE_LIST, List.class);
        if(CollectionUtils.isNotEmpty(genericObjectList))
        {
            final int itemToSelectIndex = genericObjectList.indexOf(itemToSelect);
            selectItem(Integer.valueOf(itemToSelectIndex));
        }
    }


    /**
     * Renders the provided entries as a <i>list</i> - each item is represented by base configuration where one can
     * configure name, description and an image representation.
     *
     *
     * @param pageable
     *           used to resolve the relevant <i>type code</i> (if any) and other paging information
     * @param genericObjectList
     *           the objects to be rendered
     */
    protected void render(final Pageable<?> pageable, final List<?> genericObjectList)
    {
        if(itemList != null && pageable != null)
        {
            itemList.getChildren().clear();
            final ListModelList selectedElementsListModel = new ListModelList<>();
            selectedElementsListModel.addAll(convertAllPagesIntoSingleList(pageable));
            itemList.setModel(selectedElementsListModel);
            final Base baseConfig = loadConfiguration(pageable.getTypeCode());
            itemList.setItemRenderer((item, data, index) -> {
                final Listcell cell = new Listcell();
                cell.appendChild(createEntry(data, baseConfig));
                item.appendChild(cell);
            });
        }
        renderingInProgress = false;
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, scope = CockpitEvent.SESSION)
    public void handleObjectUpdatedEvent(final CockpitEvent event)
    {
        updatePreviewCollection(event);
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_DELETED_EVENT, scope = CockpitEvent.SESSION)
    public void handleObjectDeletedEvent(final CockpitEvent event)
    {
        updatePreviewCollection(event);
    }


    protected void updatePreviewCollection(final CockpitEvent event)
    {
        synchronized(mutex)
        {
            final Pageable pageable = getValue(MODEL_PAGEABLE, Pageable.class);
            final Object updatedObject = event.getData();
            if(updatedObject != null && pageable != null && CollectionUtils.isNotEmpty(pageable.getAllResults()))
            {
                final Collection<Object> updatedObjectCollection = new ArrayList<>();
                if(updatedObject instanceof Collection)
                {
                    updatedObjectCollection.addAll((Collection<?>)updatedObject);
                }
                else
                {
                    updatedObjectCollection.add(updatedObject);
                }
                for(final Object object : updatedObjectCollection)
                {
                    updatePreviewCollection(pageable, event, object);
                }
            }
        }
    }


    protected void updatePreviewCollection(final Pageable pageable, final CockpitEvent event, final Object updatedObject)
    {
        final String typeCode = typeFacade.getType(updatedObject);
        if(!backofficeTypeUtils.isAssignableFrom(pageable.getTypeCode(), typeCode))
        {
            return;
        }
        boolean modified = false;
        Integer selectedItemIndex = getValue(MODEL_ITEM_SELECTED_INDEX, Integer.class);
        if(selectedItemIndex == null)
        {
            selectedItemIndex = Integer.valueOf(-1);
        }
        final List<Object> allResultsModifiable = new ArrayList<>(pageable.getAllResults());
        final ListIterator<Object> allResultsModifiableIterator = allResultsModifiable.listIterator();
        while(allResultsModifiableIterator.hasNext())
        {
            final Object currentObject = allResultsModifiableIterator.next();
            if(Objects.equals(updatedObject, currentObject))
            {
                if(ObjectCRUDHandler.OBJECTS_DELETED_EVENT.equals(event.getName()))
                {
                    selectedItemIndex = Integer.valueOf(
                                    getIndexToSelectAfterRemoval(selectedItemIndex.intValue(), allResultsModifiableIterator.nextIndex() - 1));
                    allResultsModifiableIterator.remove();
                }
                else
                {
                    allResultsModifiableIterator.set(updatedObject);
                }
                modified = true;
            }
        }
        if(modified)
        {
            setResultList(allResultsModifiable, selectedItemIndex, true);
        }
    }


    /**
     * Returns new index of item to select based on currently selected and removed items or -1 if currently selected item
     * has been removed
     *
     * @param currentIndex
     *           index of currently selected item
     * @param removedIndex
     *           index of removed item
     * @return new index of item to select
     */
    protected int getIndexToSelectAfterRemoval(final int currentIndex, final int removedIndex)
    {
        if(removedIndex < currentIndex)
        {
            return currentIndex - 1;
        }
        else if(removedIndex == currentIndex)
        {
            return -1;
        }
        return currentIndex;
    }


    /**
     * Changes current {@link #MODEL_PAGEABLE} to its copy with passed as an argument collection of available elements
     * and selects an element with provided index
     *
     * @param allResults
     *           collection of available elements to be set on the {@link #MODEL_PAGEABLE}.
     * @param indexToSelect
     *           index of an element to be selected
     */
    protected void setResultList(final List<?> allResults, final Integer indexToSelect)
    {
        setResultList(allResults, indexToSelect, false);
    }


    /**
     * Changes current {@link #MODEL_PAGEABLE} to its copy with passed as an argument collection of available elements
     * and selects an element with provided index
     *
     * @param allResults
     *           collection of available elements to be set on the {@link #MODEL_PAGEABLE}.
     * @param indexToSelect
     *           index of an element to be selected
     * @param synch
     *           when true renders entries synchronously
     */
    protected void setResultList(final List<?> allResults, final Integer indexToSelect, final boolean synch)
    {
        final Pageable<?> updatedPageable = getPageableCopyWithResultList(allResults);
        setPageable(updatedPageable, synch);
        selectItem(indexToSelect);
    }


    protected <T> Pageable<T> getPageableCopyWithResultList(final List<T> allResults)
    {
        final Pageable source = getValue(MODEL_PAGEABLE, Pageable.class);
        if(source != null)
        {
            final Pageable updatedPageable = createPageableInstance(allResults, source.getPageSize(), source.getTypeCode());
            updatedPageable.setPageNumber(source.getPageNumber());
            updatedPageable.setSortData(source.getSortData());
            updatedPageable.refresh();
            return updatedPageable;
        }
        else
        {
            return null;
        }
    }


    /**
     * Creates new instance of {@link com.hybris.cockpitng.search.data.pageable.Pageable}
     *
     * @param allResults
     *           collection of all elements available in the preview. The collection is exposed through
     *           {@link com.hybris.cockpitng.search.data.pageable.Pageable#getAllResults()}
     * @param pageSize
     * @param typeCode
     * @return new instance of {@link com.hybris.cockpitng.search.data.pageable.Pageable} with provided parameters
     */
    protected <T> Pageable<T> createPageableInstance(final List<T> allResults, final int pageSize, final String typeCode)
    {
        return new PageableList<>(allResults, pageSize, typeCode);
    }


    protected String getWidgetResourcePath()
    {
        String resourcePath = StringUtils.EMPTY;
        final Widget widget = getWidget(getWidgetslot());
        if(widget != null)
        {
            final WidgetDefinition widgetDefinition = (WidgetDefinition)componentDefinitionService
                            .getComponentDefinitionForCode(widget.getWidgetDefinitionId());
            if(widgetDefinition != null)
            {
                resourcePath = widgetDefinition.getResourcePath();
            }
        }
        return resourcePath;
    }


    protected Widget getWidget(final Widgetslot widgetslot)
    {
        Widget widget = null;
        if(widgetslot != null)
        {
            final WidgetInstance widgetInstance = widgetslot.getWidgetInstance();
            widget = widgetInstance == null ? null : widgetInstance.getWidget();
        }
        return widget;
    }


    protected Component createEntry(final Object entry, final Base config)
    {
        final String resourcePath = getWidgetResourcePath();
        final Div gridItemContainer = new Div();
        if(StringUtils.isNotBlank(resourcePath))
        {
            final String templateLocation = "/" + resourcePath + DEFAULT_RENDERER_TEMPLATE;
            // get arguments to be passed to zul (renderer) template
            final Map<String, Object> args = getRendererProps(entry, config);
            try(final InputStream resource = this.getClass().getResourceAsStream(templateLocation))
            {
                if(resource == null)
                {
                    LOG.error("File not found: %s", templateLocation);
                }
                else
                {
                    createComponentDirectly(gridItemContainer, args, resource);
                }
            }
            catch(final IOException e)
            {
                LOG.error("File not found exception", e);
            }
            gridItemContainer.addEventListener(Events.ON_CLICK, event -> sendOutput(SOCKET_SELECTED_ITEM, entry));
        }
        return gridItemContainer;
    }


    @InextensibleMethod
    private void createComponentDirectly(final Div gridItemContainer, final Map<String, Object> args, final InputStream resource)
                    throws IOException
    {
        try(final InputStreamReader isr = new InputStreamReader(resource, Charset.defaultCharset().toString()))
        {
            Executions.createComponentsDirectly(isr, null, gridItemContainer, args);
        }
    }


    /**
     * Loads the UI configuration for component with code {@value #WIDGET_SETTING_CONFIG_CONTEXT} and for the specified
     * type, <p>typeCode</p>.
     *
     * @param typeCode
     *           the type code for which the configuration should be loaded
     * @return the configuration object if found, <p>null</p> otherwise
     */
    protected Base loadConfiguration(final String typeCode)
    {
        Base config = null;
        final DefaultConfigContext configContext = new DefaultConfigContext(
                        getWidgetSettings().getString(WIDGET_SETTING_CONFIG_CONTEXT));
        configContext.setType(typeCode);
        try
        {
            config = getWidgetInstanceManager().loadConfiguration(configContext, Base.class);
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
     * @param baseConfig
     *           the UI configuration to be used when rendering
     * @return map with renderer properties, which are accessible in the zul template (using
     *         <p>${args.&lt;KEY_NAME&gt;}</p> )
     */
    protected Map<String, Object> getRendererProps(final Object entry, final Base baseConfig)
    {
        final Map<String, Object> rendererProps = new HashMap<>();
        if(baseConfig == null)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("No UI config provided. Using toString()");
            }
            rendererProps.put(ENTRY_NAME, entry != null ? entry.toString() : StringUtils.EMPTY);
        }
        else
        {
            rendererProps.put(ENTRY_NAME, labelService.getObjectLabel(entry));
            rendererProps.put(ENTRY_DESCR, labelService.getObjectDescription(entry));
            if(baseConfig.getPreview() != null)
            {
                final ObjectPreview objectPreview = objectPreviewService.getPreview(entry, baseConfig);
                if(objectPreview != null)
                {
                    rendererProps.put(ENTRY_IMG_URL, objectPreview.getUrl());
                    rendererProps.put(ENTRY_IMG_FALLBACK, Boolean.valueOf(objectPreview.isFallback()));
                }
            }
        }
        return rendererProps;
    }


    protected void selectItem(final Integer position)
    {
        if(position != null)
        {
            synchronized(mutex)
            {
                final ListModelList model = (ListModelList)itemList.getModel();
                if(!renderingInProgress && model != null && model.getSize() >= (position.intValue() + 1))
                {
                    if(position.intValue() > -1)
                    {
                        model.setSelection(Collections.singletonList(model.getElementAt(position.intValue())));
                    }
                    else
                    {
                        model.clearSelection();
                        sendOutput(SOCKET_SELECTED_ITEM, null);
                    }
                }
                setValue(MODEL_ITEM_SELECTED_INDEX, position);
            }
        }
    }


    protected <T> List<T> convertAllPagesIntoSingleList(final Pageable<T> pageable)
    {
        if(pageable == null)
        {
            return Lists.newLinkedList();
        }
        else
        {
            return Lists.newLinkedList(pageable.getAllResults());
        }
    }


    public LabelService getLabelService()
    {
        return labelService;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    public CockpitComponentDefinitionService getComponentDefinitionService()
    {
        return componentDefinitionService;
    }


    public Listbox getItemList()
    {
        return itemList;
    }


    public ObjectPreviewService getObjectPreviewService()
    {
        return objectPreviewService;
    }


    private class LoadingOperation implements Operation
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
            return CollectionPreviewController.this.getLabel("loading");
        }


        @Override
        public Object execute(final Progress progress)
        {
            return getValue(MODEL_PAGEABLE_LIST, List.class);
        }
    }
}
