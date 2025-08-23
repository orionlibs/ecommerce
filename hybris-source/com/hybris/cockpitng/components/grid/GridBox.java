/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.components.grid;

import com.google.common.collect.Sets;
import com.hybris.cockpitng.util.ComponentMarkingUtils;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.impl.IdentifiableMarkEventConsumer;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.MouseEvent;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;

public class GridBox extends Div
{
    public static final String DATA_ATTRIBUTE = "DataAttributeQualifier";
    public static final String YW_SELECTED = "yw-selected";
    public static final String SCLASS_YW_GRID_VIEW_TILES = "yw-grid-view";
    public static final String MARK_NAME_SELECT = "select";
    private static final String SELECT_ITEM_LISTENER = "selectItemListener";
    private static final Logger LOG = LoggerFactory.getLogger(GridBox.class);
    private static final String BEAN_COMPONENT_MARKING_UTILS = "componentMarkingUtils";
    private final Label emptyLabel = new Label();
    private final Div wrapper = new Div();
    private transient GridBoxItemRenderer renderer;
    private transient ComponentMarkingUtils componentMarkingUtils;
    private ListModelList model;
    private boolean multiple;
    private boolean selectOnClick = true;
    private int lastSelectedIndex = -1;


    public void refreshView()
    {
        lastSelectedIndex = -1;
        getChildren().clear();
        getWrapper().getChildren().clear();
        wrapper.setSclass(SCLASS_YW_GRID_VIEW_TILES);
        appendChild(wrapper);
        final ListModelList listModelList = getModel();
        if(listModelList.getSize() > 0)
        {
            for(int i = 0; i < listModelList.getSize(); i++)
            {
                final Object element = listModelList.getElementAt(i);
                final HtmlBasedComponent box = createBox(element);
                renderBox(box, element, i);
                markSelectedIfNeeded(box, element);
                wrapper.appendChild(box);
                getComponentMarkingUtils().registerMarkedComponentsListener(box, MARK_NAME_SELECT, Events.ON_CLICK,
                                new IdentifiableMarkEventConsumer(SELECT_ITEM_LISTENER,
                                                click -> handleSelectClick(box, element, listModelList, click)));
                box.addEventListener(Events.ON_CLICK, (MouseEvent click) -> handleBoxClick(box, element, listModelList, click));
            }
        }
        else
        {
            wrapper.appendChild(emptyLabel);
        }
    }


    protected void markSelectedIfNeeded(final HtmlBasedComponent box, final Object element)
    {
        if(getModel().isSelected(element))
        {
            UITools.modifySClass(box, YW_SELECTED, true);
        }
    }


    protected void renderBox(final HtmlBasedComponent box, final Object element, final int i)
    {
        if(renderer == null)
        {
            box.appendChild(new Label(Objects.toString(element, StringUtils.EMPTY)));
        }
        else
        {
            renderer.render(box, element, i);
        }
    }


    protected void handleBoxClick(final HtmlBasedComponent box, final Object data, final ListModelList model,
                    final MouseEvent event)
    {
        if(!isMultiple())
        {
            handleSelectClick(box, data, model, event);
        }
        else
        {
            setFocus(box, data);
        }
    }


    protected void handleSelectClick(final HtmlBasedComponent box, final Object data, final ListModelList model,
                    final Object event)
    {
        if(!(event instanceof MouseEvent))
        {
            return;
        }
        final MouseEvent mouseEvent = (MouseEvent)event;
        Set<Object> selectedItems;
        final Set<Object> unselectedItems;
        if(model.getSelection().contains(data))
        {
            selectedItems = null;
            unselectedItems = Collections.singleton(data);
        }
        else
        {
            selectedItems = Collections.singleton(data);
            unselectedItems = null;
            if(isMultiple() && ((mouseEvent.getKeys() & SelectEvent.SHIFT_KEY) == SelectEvent.SHIFT_KEY))
            {
                final int currentSelectedItem = model.indexOf(data);
                if(getLastSelectedIndex() != -1 && currentSelectedItem != -1 && isAnyElementSelectedOnCurrentPage(model))
                {
                    selectedItems = findSelectionRange(model, currentSelectedItem);
                    selectedItems.add(data);
                }
            }
        }
        handleItemSelection(box, data, model);
        sendSelectEvent(data, selectedItems, unselectedItems, mouseEvent.getKeys());
    }


    protected boolean isAnyElementSelectedOnCurrentPage(final ListModelList model)
    {
        return getTileChildren().stream().anyMatch(box -> model.getSelection().contains(box.getAttribute(DATA_ATTRIBUTE)));
    }


    protected Set<Object> findSelectionRange(final ListModelList model, final int currentSelectedItem)
    {
        if(getLastSelectedIndex() < currentSelectedItem)
        {
            return Sets.newLinkedHashSet(model.subList(getLastSelectedIndex() + 1, currentSelectedItem));
        }
        else
        {
            return Sets.newLinkedHashSet(model.subList(currentSelectedItem, getLastSelectedIndex()));
        }
    }


    protected void sendSelectEvent(final Object data, final Set<Object> selectedItems, final Set<Object> unselectedItems,
                    final int keys)
    {
        Events.sendEvent(
                        new SelectEvent(Events.ON_SELECT, this, selectedItems, null, unselectedItems, null, null, null, null, data, keys));
    }


    protected void handleItemSelection(final HtmlBasedComponent box, final Object data, final ListModelList model)
    {
        if(model.getSelection().contains(data))
        {
            deselectItem(box);
        }
        else
        {
            selectItem(box, data);
        }
    }


    protected void selectItem(final HtmlBasedComponent box, final Object data)
    {
        if(isMultiple())
        {
            model.addToSelection(data);
            UITools.modifySClass(box, YW_SELECTED, true);
        }
        else
        {
            model.setSelection(Collections.singleton(data));
            this.getTileChildren().forEach(child -> UITools.modifySClass((HtmlBasedComponent)child, YW_SELECTED,
                            child.getAttribute(DATA_ATTRIBUTE) == data));
        }
        lastSelectedIndex = model.indexOf(data);
    }


    protected void setFocus(final HtmlBasedComponent box, final Object element)
    {
        setFocus(box);
        sendFocusEvent(element);
    }


    protected void setFocus(final HtmlBasedComponent box)
    {
        box.setTabindex(-1);
        Clients.evalJavaScript("jq('#" + box.getUuid() + "').focus()");
    }


    protected void sendFocusEvent(final Object focusedItem)
    {
        Events.sendEvent(Events.ON_FOCUS, this, focusedItem);
    }


    protected void deselectItem(final HtmlBasedComponent box)
    {
        model.removeFromSelection(box.getAttribute(DATA_ATTRIBUTE));
        UITools.modifySClass(box, YW_SELECTED, false);
    }


    public void selectByIndex(final int index)
    {
        final List<Component> tileChildren = getTileChildren();
        if(index >= 0 && index < tileChildren.size())
        {
            final Component box = tileChildren.get(index);
            sendOnClickEvent(box);
        }
        else
        {
            LOG.warn("Index [{}] is out of scope", index);
        }
    }


    /**
     * Returns a tile component for specified data.
     *
     * @param data
     *           data for which tile is to be found
     * @return a tile that is bound to specified data
     */
    public Component getTile(final Object data)
    {
        final Optional<Component> component = getTileChildren().stream()
                        .filter(box -> Objects.equals(box.getAttribute(DATA_ATTRIBUTE), data)).findFirst();
        return component.orElse(null);
    }


    public void selectByData(final Set<?> selection)
    {
        getTileChildren().stream().filter(box -> isSelectionChangeNeeded(selection, box))
                        .forEach(box -> handleItemSelection((HtmlBasedComponent)box, box.getAttribute(DATA_ATTRIBUTE), getModel()));
    }


    protected boolean isSelectionChangeNeeded(final Set<?> selection, final Component box)
    {
        final Object boxData = box.getAttribute(DATA_ATTRIBUTE);
        return selection.contains(boxData) != model.isSelected(boxData);
    }


    protected void sendOnClickEvent(final Component box)
    {
        Events.sendEvent(new MouseEvent(Events.ON_CLICK, box));
    }


    protected HtmlBasedComponent createBox(final Object data)
    {
        final Div box = new Div();
        box.setAttribute(DATA_ATTRIBUTE, data);
        return box;
    }


    public ListModelList getModel()
    {
        if(model == null)
        {
            model = new ListModelList(new Object[0]);
        }
        return model;
    }


    public void setModel(final ListModelList model)
    {
        this.model = model;
        refreshView();
        registerListDataListener(model);
    }


    public String getEmptyMessage()
    {
        return emptyLabel.getValue();
    }


    public void setEmptyMessage(final String emptyMessage)
    {
        this.emptyLabel.setValue(emptyMessage);
    }


    protected void registerListDataListener(final ListModelList model)
    {
        if(model != null)
        {
            model.addListDataListener(e -> {
                if(indexesInRange(e.getIndex0(), e.getIndex1()))
                {
                    final int index = e.getIndex0();
                    final Object element = model.get(index);
                    final HtmlBasedComponent newBox = createBox(element);
                    renderBox(newBox, element, index);
                    markSelectedIfNeeded(newBox, element);
                    newBox.addEventListener(Events.ON_CLICK, (MouseEvent click) -> handleBoxClick(newBox, element, model, click));
                    getComponentMarkingUtils().registerMarkedComponentsListener(newBox, MARK_NAME_SELECT, Events.ON_CLICK,
                                    new IdentifiableMarkEventConsumer(SELECT_ITEM_LISTENER,
                                                    click -> handleSelectClick(newBox, element, model, click)));
                    final HtmlBasedComponent oldBox = (HtmlBasedComponent)getTileChildren().get(index);
                    getWrapper().insertBefore(newBox, oldBox);
                    getWrapper().removeChild(oldBox);
                }
            });
        }
    }


    private List<Component> getTileChildren()
    {
        return wrapper.getChildren().stream().filter(child -> !child.equals(emptyLabel)).collect(Collectors.toList());
    }


    protected boolean indexesInRange(final int index0, final int index1)
    {
        return index0 > -1 && index0 == index1;
    }


    public void setItemRenderer(final GridBoxItemRenderer renderer)
    {
        this.renderer = renderer;
    }


    public boolean isMultiple()
    {
        return multiple;
    }


    public void setMultiple(final boolean multiple)
    {
        this.multiple = multiple;
    }


    public int getLastSelectedIndex()
    {
        return lastSelectedIndex;
    }


    public Div getWrapper()
    {
        return wrapper;
    }


    public boolean isSelectOnClick()
    {
        return selectOnClick;
    }


    public void setSelectOnClick(final boolean selectOnClick)
    {
        this.selectOnClick = selectOnClick;
    }


    protected ComponentMarkingUtils getComponentMarkingUtils()
    {
        if(componentMarkingUtils == null)
        {
            componentMarkingUtils = (ComponentMarkingUtils)SpringUtil.getBean(BEAN_COMPONENT_MARKING_UTILS);
        }
        return componentMarkingUtils;
    }
}
