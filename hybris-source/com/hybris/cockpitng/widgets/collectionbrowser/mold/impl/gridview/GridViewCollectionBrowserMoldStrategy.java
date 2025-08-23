/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.gridview;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.hybris.cockpitng.components.grid.GridBox;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.impl.DefaultConfigContext;
import com.hybris.cockpitng.core.config.impl.jaxb.gridview.GridView;
import com.hybris.cockpitng.core.context.impl.DefaultCockpitContext;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.core.util.impl.TypedSettingsMap;
import com.hybris.cockpitng.dnd.DragAndDropStrategy;
import com.hybris.cockpitng.dnd.SelectionSupplier;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import com.hybris.cockpitng.widgets.collectionbrowser.CollectionBrowserController;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.SinglePage;
import com.hybris.cockpitng.widgets.collectionbrowser.mold.impl.common.AbstractMoldStrategy;
import com.hybris.cockpitng.widgets.common.NotifyingWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRendererListener;
import com.hybris.cockpitng.widgets.navigation.NavigationItemSelectorContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

public class GridViewCollectionBrowserMoldStrategy extends AbstractMoldStrategy<Component, GridView, Object>
{
    public static final String DEFAULT_GRID_VIEW_CONFIG_CTX = "grid-view";
    public static final String SETTING_GRID_VIEW_CONFIG_CTX = "gridViewConfigCtx";
    public static final String YTEST_ID_GRID_VIEW = "gridView";
    public static final String GRID_VIEW_NAME = "grid-view";
    public static final String TOOLTIP_PROPERTY_KEY = "mold.gridview.tooltip";
    public static final String SCLASS_GRID_VIEW_CONTAINER = "yw-grid-view-container";
    public static final String YW_DRAGGABLE_SCLASS = "yw-draggable";
    private static final String SETTING_ITEM_RENDERER = "gridItemRenderer";
    private static final Logger LOG = LoggerFactory.getLogger(GridViewCollectionBrowserMoldStrategy.class);
    private GridBox grid;


    @Override
    public void render(final Component parent, final SinglePage singlePage)
    {
        Validate.notNull("Parent may not be null", parent);
        Validate.notNull("Page may not be null", singlePage);
        setTypeCode(singlePage.getTypeCode());
        grid = new GridBox();
        final DragAndDropStrategy dragAndDropStrategy = getContext().getDragAndDropStrategy();
        final boolean multiSelectEnabled = getContext().isMultiSelectEnabled();
        grid.setMultiple(multiSelectEnabled);
        grid.addEventListener(Events.ON_SELECT, this::onSelectEvent);
        grid.addEventListener(Events.ON_FOCUS, this::onFocusEvent);
        grid.setEmptyMessage(chooseEmptyMessageToDisplayFor(singlePage));
        final SelectionSupplier<Object> selectionSupplier = () -> new ArrayList<>(getContext().getSelectedItems());
        YTestTools.modifyYTestId(grid, YTEST_ID_GRID_VIEW);
        final GridView config = loadConfiguration(singlePage.getTypeCode());
        grid.setItemRenderer((box, data, position) -> {
            getRenderer().render(box, config, data, getDataType(data), getContext().getWidgetInstanceManager());
            if(dragAndDropStrategy != null)
            {
                final HtmlBasedComponent htmlComponent = (HtmlBasedComponent)box;
                UITools.modifySClass(htmlComponent, YW_DRAGGABLE_SCLASS, true);
                final DefaultCockpitContext context = new DefaultCockpitContext();
                final Map inputContext = getWidgetModel().getValue(CollectionBrowserController.MODEL_SELECTION_CONTEXT, Map.class);
                if(inputContext != null)
                {
                    context.setParameters(inputContext);
                }
                if(getContext().isDragEnabled())
                {
                    dragAndDropStrategy.makeDraggable(htmlComponent, data, context, selectionSupplier);
                }
                if(getContext().isDropEnabled())
                {
                    dragAndDropStrategy.makeDroppable(htmlComponent, data, context);
                }
            }
            setTileFocused((HtmlBasedComponent)box, Objects.equals(data, getContext().getFocusedItem()));
        });
        grid.setSclass(SCLASS_GRID_VIEW_CONTAINER);
        parent.appendChild(grid);
        setPage(singlePage);
    }


    @Override
    protected String getRendererSetting()
    {
        return SETTING_ITEM_RENDERER;
    }


    @Override
    protected void initializeRenderer(final NotifyingWidgetComponentRenderer<Component, GridView, Object> renderer)
    {
        super.initializeRenderer(renderer);
        if(getContext().areHyperlinksSupported())
        {
            renderer.addRendererListener(createLinkRenderedListener());
        }
        else
        {
            renderer.addRendererListener(createTileRenderedListener());
        }
    }


    protected WidgetComponentRendererListener<Component, GridView, Object> createLinkRenderedListener()
    {
        return event -> {
            if(event.isFinal())
            {
                addLink((HtmlBasedComponent)event.getSource(), event.getData());
            }
        };
    }


    protected void addLink(final HtmlBasedComponent component, final Object entry)
    {
        addLink(component, () -> entry);
    }


    protected WidgetComponentRendererListener<Component, GridView, Object> createTileRenderedListener()
    {
        return event -> {
            if(event.isFinal())
            {
                UITools.modifySClass((HtmlBasedComponent)event.getSource(), SCLASS_CELL_HYPERLINK, true);
            }
        };
    }


    /**
     * @deprecated since 6.7, use {@link #onSelectEvent(Event)} instead
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void onClickEvent(final Event event)
    {
        onSelectEvent(event);
    }


    protected void onSelectEvent(final Event event)
    {
        final Set selectedItems = ((SelectEvent)event).getSelectedItems();
        final Set selection;
        if(getContext().isMultiSelectEnabled())
        {
            selection = new LinkedHashSet(getSelectedObjectsFromModel());
            selection.removeAll(((SelectEvent)event).getUnselectedItems());
            selection.addAll(selectedItems);
        }
        else
        {
            selection = new LinkedHashSet(selectedItems);
            getContext().notifyItemClicked(event.getData());
        }
        final boolean isShiftPressed = (((SelectEvent)event).getKeys() & SelectEvent.SHIFT_KEY) == SelectEvent.SHIFT_KEY;
        userSelectionStyle(isShiftPressed);
        getContext().notifyItemsSelected(selection);
    }


    protected void onFocusEvent(final Event event)
    {
        final Object focusedObject = event.getData();
        focusItem(getFocusedObjectFromModel().orElse(null), focusedObject);
        getContext().notifyItemClicked(focusedObject);
    }


    protected void userSelectionStyle(final boolean isShiftPressed)
    {
        if(getContext().isMultiSelectEnabled() && isShiftPressed)
        {
            UITools.addStyle(grid, "user-select", "none");
        }
        else
        {
            UITools.removeStyle(grid, "user-select");
        }
    }


    protected Set getSelectedObjectsFromModel()
    {
        final Collection<?> selected = getContext().getSelectedItems();
        return selected != null ? new LinkedHashSet(selected) : Collections.emptySet();
    }


    protected GridView loadConfiguration(final String typeCode)
    {
        try
        {
            final TypedSettingsMap settings = getContext().getWidgetInstanceManager().getWidgetSettings();
            final String readConfigCtx = settings.getString(SETTING_GRID_VIEW_CONFIG_CTX);
            final String gridViewConfigCtx = StringUtils.defaultIfBlank(readConfigCtx, DEFAULT_GRID_VIEW_CONFIG_CTX);
            final DefaultConfigContext context = new DefaultConfigContext(gridViewConfigCtx, typeCode);
            return getContext().getWidgetInstanceManager().loadConfiguration(context, GridView.class);
        }
        catch(final CockpitConfigurationException cockpitConfigurationException)
        {
            LOG.warn("Could not load GridView configuration", cockpitConfigurationException);
        }
        return new GridView();
    }


    @Override
    public void setPage(final SinglePage singlePage)
    {
        Validate.notNull("Page may not be null", singlePage);
        final List<?> list = getPermissionFacade().canReadType(singlePage.getTypeCode()) ? singlePage.getList()
                        : Lists.newArrayList();
        processViewWith(list, singlePage);
    }


    protected void processViewWith(final List<?> list, final SinglePage singlePage)
    {
        final ListModelList model = new ListModelList(list);
        model.setMultiple(getContext().isMultiSelectEnabled());
        final Set previouslySelectedObjects = getSelectedObjectsFromModel();
        model.setSelection(previouslySelectedObjects);
        setTypeCode(singlePage.getTypeCode());
        grid.setModel(model);
    }


    @Override
    protected int getUiModelSize()
    {
        if(grid == null)
        {
            return 0;
        }
        final ListModel model = grid.getModel();
        if(model == null)
        {
            return 0;
        }
        return model.getSize();
    }


    @Override
    protected Optional<Object> getUiElementAt(final int index)
    {
        if(grid == null)
        {
            return Optional.empty();
        }
        final ListModel model = grid.getModel();
        if(model == null)
        {
            return Optional.empty();
        }
        return Optional.of(model.getElementAt(index));
    }


    @Override
    public void handleObjectDeleteEvent(final CockpitEvent event)
    {
        Validate.notNull("Event may not be null", event);
        final Object deleted = event.getData();
        if(deleted instanceof Collection)
        {
            final Set selected = getSelectedObjectsFromModel();
            if(CollectionUtils.isNotEmpty(selected))
            {
                final Collection toRemove = (Collection)deleted;
                if(CollectionUtils.containsAny(selected, toRemove))
                {
                    selectItems(new LinkedHashSet(CollectionUtils.removeAll(selected, toRemove)));
                }
            }
        }
    }


    @Override
    protected void handleCollectionUpdate(final Collection<Object> collection)
    {
        if(grid != null && grid.getModel() != null)
        {
            final ListModelList model = grid.getModel();
            for(final Object item : collection)
            {
                updateModelData(model, item);
            }
        }
    }


    protected void updateModelData(final ListModelList<Object> model, final Object data)
    {
        for(int i = 0; i < model.getSize(); i++)
        {
            if(Objects.equals(data, model.getElementAt(i)))
            {
                model.set(i, data);
                break;
            }
        }
    }


    @Override
    public void handleObjectCreateEvent(final CockpitEvent event)
    {
        // NOP
    }


    @Override
    public void selectItems(final Set<?> items)
    {
        if(CollectionUtils.isEmpty(items))
        {
            deselectItems();
        }
        else if(grid != null)
        {
            final List innerList = grid.getModel().getInnerList();
            final Set<?> selection = new LinkedHashSet(Sets.intersection(items, new LinkedHashSet(innerList)));
            grid.selectByData(selection);
        }
    }


    @Override
    public void deselectItems()
    {
        if(grid != null)
        {
            grid.selectByData(Collections.emptySet());
        }
    }


    @Override
    public void focusItem(final Object oldFocus, final Object newFocus)
    {
        if(grid != null)
        {
            setTileFocused((HtmlBasedComponent)grid.getTile(oldFocus), false);
            setTileFocused((HtmlBasedComponent)grid.getTile(newFocus), true);
        }
    }


    protected void setTileFocused(final HtmlBasedComponent tile, final boolean focused)
    {
        if(tile != null)
        {
            UITools.modifySClass(tile, SCLASS_CELL_FOCUSED, focused);
        }
    }


    @Override
    public void release()
    {
        reset();
        if(grid != null)
        {
            grid.getParent().removeChild(grid);
            grid = null;
        }
    }


    @Override
    public String getName()
    {
        return GRID_VIEW_NAME;
    }


    @Override
    public String getTooltipText()
    {
        return getContext().getWidgetInstanceManager().getLabel(TOOLTIP_PROPERTY_KEY);
    }


    @Override
    public NavigationItemSelectorContext getNavigationItemSelectorContext()
    {
        if(getFocusedObjectFromModel().isPresent())
        {
            final SinglePage singlePage = getContext().getCurrentPage();
            final int pageSize = singlePage == null ? SinglePage.EMPTY.getPageSize() : singlePage.getListSize();
            final int sizeOfSelectedItems = getSelectedObjectsFromModel().size();
            final int indexOfLastSelectedItem = (sizeOfSelectedItems <= 1 && grid != null) ? grid.getLastSelectedIndex() : -1;
            final int indexOfFocusedItem = getIndexOf(getContext().getFocusedItem());
            return new NavigationItemSelectorContext(pageSize, indexOfLastSelectedItem, indexOfFocusedItem);
        }
        else
        {
            return EMPTY_NAVIGATION_ITEM_SELECTOR_CONTEXT;
        }
    }


    @Override
    public void reset()
    {
        if(grid != null)
        {
            grid.setModel(null);
        }
    }


    public GridBox getGrid()
    {
        return grid;
    }
}
