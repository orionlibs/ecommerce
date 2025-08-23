/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.dndconfig.renderer;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionListener;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.actions.impl.DefaultActionRenderer;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.dnd.DragAndDropActionType;
import com.hybris.cockpitng.services.dnd.DragAndDropConfigurationService;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.ComboitemRenderer;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Popup;

public class DragAndDropConfigActionRenderer extends DefaultActionRenderer<Object, Object>
{
    public static final String POPUP_ATTRIBUTE_IDENTIFIER = "DnD_Popup_Attribute_Identifier";
    public static final String SCLASS_MAIN_POPUP = "yw-dnd-config-popup yw-pointer-menupopup yw-pointer-menupopup-top-right";
    public static final String SCLASS_YW_DND_CONFIG_MAIN_INFO = "yw-dnd-config-main-info";
    public static final String SCLASS_YW_DND_CONFIG_ADDITIONAL_INFO = "yw-dnd-config-additional-info";
    public static final String YW_DND_CONFIG_TYPE_COMBO = "yw-dnd-config-type-combo";
    public static final String ADDITIONAL_INFO = "dnd.additional.info";
    public static final String DND_MOVE_BEHAVIOUR = "dnd.move.behaviour";
    public static final String DND_COPY_BEHAVIOUR = "dnd.copy.behaviour";
    private DragAndDropConfigurationService dragAndDropConfigurationService;


    @Override
    protected EventListener<? extends Event> createEventListener(final CockpitAction<Object, Object> action,
                    final ActionContext<Object> context, final ActionListener<Object> listener)
    {
        Validate.notNull("All arguments are mandatory", action, context, listener);
        return (EventListener<Event>)event -> {
            final var clickedComponent = event.getTarget();
            final Component popupTarget = (clickedComponent instanceof Button) ? clickedComponent.getParent() : clickedComponent;
            final Popup popup = findOrCreateActionPopup(popupTarget, context);
            if(!popup.isVisible())
            {
                popup.open(popupTarget, "after_end");
            }
        };
    }


    protected Popup findOrCreateActionPopup(final Component target, final ActionContext<Object> context)
    {
        Popup popup = findPopup(target);
        if(popup == null)
        {
            popup = createPopup(target, context);
        }
        return popup;
    }


    protected Popup createPopup(final Component target, final ActionContext<Object> context)
    {
        final Popup popup = new Popup();
        popup.setAttribute(POPUP_ATTRIBUTE_IDENTIFIER, Boolean.TRUE);
        popup.setSclass(SCLASS_MAIN_POPUP);
        final Label mainLabel = new Label(context.getLabel("dnd.default.behaviour"));
        mainLabel.setSclass(SCLASS_YW_DND_CONFIG_MAIN_INFO);
        popup.appendChild(mainLabel);
        final ListModelList<DragAndDropActionType> model = new ListModelList<>();
        model.add(DragAndDropActionType.REPLACE);
        model.add(DragAndDropActionType.APPEND);
        final Combobox combo = new Combobox();
        combo.setModel(model);
        combo.setReadonly(true);
        final DragAndDropActionType selectedActionType = getDragAndDropConfigurationService().getDefaultActionType();
        if(selectedActionType != null)
        {
            model.setSelection(Collections.singleton(selectedActionType));
        }
        combo.setSclass(YW_DND_CONFIG_TYPE_COMBO);
        combo.setItemRenderer(createItemRenderer(context));
        combo.addEventListener(Events.ON_SELECT, createSelectListener(context));
        final Label infoLabel = new Label(context.getLabel(ADDITIONAL_INFO));
        infoLabel.setSclass(SCLASS_YW_DND_CONFIG_ADDITIONAL_INFO);
        popup.appendChild(combo);
        popup.appendChild(infoLabel);
        target.appendChild(popup);
        return popup;
    }


    protected EventListener<SelectEvent<?, DragAndDropActionType>> createSelectListener(final ActionContext<Object> context)
    {
        return (final SelectEvent<?, DragAndDropActionType> selectEvent) -> {
            final Set<DragAndDropActionType> selectedObjects = selectEvent.getSelectedObjects();
            if(selectedObjects != null && selectedObjects.size() == 1)
            {
                final DragAndDropActionType selectedType = selectedObjects.iterator().next();
                getDragAndDropConfigurationService().setDefaultActionType(selectedType);
            }
        };
    }


    protected ComboitemRenderer<DragAndDropActionType> createItemRenderer(final ActionContext<Object> context)
    {
        return (final Comboitem item, final DragAndDropActionType type, final int index) -> {
            final String label;
            switch(type)
            {
                case REPLACE:
                    label = context.getLabel(DND_MOVE_BEHAVIOUR);
                    break;
                case APPEND:
                    label = context.getLabel(DND_COPY_BEHAVIOUR);
                    break;
                default:
                    throw new IllegalStateException("Unknown DND behaviour: " + type);
            }
            item.setLabel(label);
        };
    }


    protected Popup findPopup(final Component target)
    {
        final List<Component> children = target.getChildren();
        for(final Component child : children)
        {
            if(Boolean.TRUE.equals(child.getAttribute(POPUP_ATTRIBUTE_IDENTIFIER)) && child instanceof Popup)
            {
                return (Popup)child;
            }
        }
        return null;
    }


    public DragAndDropConfigurationService getDragAndDropConfigurationService()
    {
        return dragAndDropConfigurationService;
    }


    @Autowired
    public void setDragAndDropConfigurationService(final DragAndDropConfigurationService dragAndDropConfigurationService)
    {
        this.dragAndDropConfigurationService = dragAndDropConfigurationService;
    }
}
