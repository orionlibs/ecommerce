/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.actions.create;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionListener;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.actions.impl.DefaultActionRenderer;
import com.hybris.cockpitng.common.model.TypeSelectorTreeModel;
import com.hybris.cockpitng.common.renderer.TypeSelectorTreeItemRenderer;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.util.CockpitSessionService;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.YTestTools;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Tree;
import org.zkoss.zul.TreeModel;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;

public class CreateActionRenderer extends DefaultActionRenderer<Object, Object>
{
    public static final String CREATE_ACTION_ROOT_TYPE = "__create_action_root_type";
    public static final String CREATE_ACTION_USER_CHOSEN_TYPE = "__create_action_user_chosen_type";
    public static final String CREATE_ACTION_EXPANDED_PATHS = "__create_action_expanded_paths";
    public static final String ACTION_CONTAINER_ATTRIBUTE = "__action_container";
    public static final String SCLASS_YA_CREATE_TYPE_SELECTOR_BUTTON = "ya-create-type-selector-button";
    public static final String SCLASS_YA_CREATE_CONTAINER = "ya-create-container";
    public static final String SETTING_CREATED_TYPE_KEY = "createdTypeKey";
    public static final String SETTING_FORBID_SUBTYPES_CREATION = "forbidSubtypesCreation";
    private static final Logger LOG = LoggerFactory.getLogger(CreateActionRenderer.class);
    @Autowired
    protected CockpitSessionService cockpitSessionService;
    @Autowired
    protected TypeFacade typeFacade;
    @Autowired
    protected LabelService labelService;
    @Autowired
    protected PermissionFacade permissionFacade;


    @Override
    public void render(final Component parent, final CockpitAction<Object, Object> action, final ActionContext<Object> context,
                    final boolean updateMode, final ActionListener<Object> listener)
    {
        final DataType currentType = extractCreatedTypeCode(context);
        if(!context.containsParameter(ActionContext.VIEW_MODE_PARAM))
        {
            context.setParameter(ActionContext.VIEW_MODE_PARAM, ActionContext.VIEWMODE_ICONANDTEXT);
        }
        final DataType lastRootType = loadFromParentModel(context, CREATE_ACTION_ROOT_TYPE, DataType.class);
        if(ObjectUtils.notEqual(currentType, lastRootType))
        {
            storeInParentModel(null, context, CREATE_ACTION_USER_CHOSEN_TYPE);
            storeInParentModel(null, context, CREATE_ACTION_EXPANDED_PATHS);
            storeInParentModel(currentType, context, CREATE_ACTION_ROOT_TYPE);
        }
        final HtmlBasedComponent container = getOrCreateContainer(parent);
        super.render(container, action, context, updateMode, listener);
        final Label label = getOrCreateLabel(container.getFirstChild(), context);
        final boolean canPerform = action.canPerform(context);
        if(!isSubtypesCreationForbidden(context))
        {
            final Popup subtypesPopup = getOrCreatePopup(container, label, action, context, listener);
            final Toolbarbutton openTypeChooser = getOrCreateTypeChooserButton(container);
            openTypeChooser.setDisabled(!canPerform);
            bindTypeChooserToSubtypesPopup(context, openTypeChooser, subtypesPopup, currentType);
            parent.appendChild(subtypesPopup);
        }
    }


    private static boolean isSubtypesCreationForbidden(final ActionContext<Object> context)
    {
        return Boolean.parseBoolean(String.valueOf(context.getParameter(SETTING_FORBID_SUBTYPES_CREATION)));
    }


    protected void bindTypeChooserToSubtypesPopup(final ActionContext<Object> context, final Toolbarbutton openTypeChooser,
                    final Popup subtypesPopup, final DataType currentType)
    {
        final Iterator<Component> childrenIterator = subtypesPopup.getChildren().iterator();
        final Tree typeSelector = (Tree)childrenIterator.next();
        final Label noSubtypesLabel = (Label)childrenIterator.next();
        subtypesPopup.appendChild(typeSelector);
        YTestTools.modifyYTestId(openTypeChooser, "create-type-selector-button");
        removeEventListeners(openTypeChooser, Events.ON_CLICK);
        openTypeChooser.addEventListener(Events.ON_CLICK, (DefaultActionRendererEventListener<Event>)event -> {
            final TreeModel previousModel = typeSelector.getModel();
            if(previousModel == null || !((TypeSelectorTreeModel)previousModel).getRootType().equals(currentType))
            {
                final TypeSelectorTreeModel newModel = new TypeSelectorTreeModel(currentType, typeFacade, permissionFacade);
                final int[][] openPaths = loadFromParentModel(context, CREATE_ACTION_EXPANDED_PATHS, int[][].class);
                if(openPaths != null)
                {
                    newModel.addOpenPaths(openPaths);
                }
                final DataType userChosenType = loadFromParentModel(context, CREATE_ACTION_USER_CHOSEN_TYPE, DataType.class);
                if(previousModel == null && userChosenType != null)
                {
                    newModel.setSelection(Collections.singleton(userChosenType));
                }
                typeSelector.setModel(newModel);
            }
            expandFirstNonAbstractSubtype((AbstractTreeModel)typeSelector.getModel(), currentType);
            UITools.modifySClass(openTypeChooser, "ya-create-type-selector-button-opened", true);
            if(CollectionUtils.isEmpty(currentType.getSubtypes()))
            {
                typeSelector.setVisible(false);
                noSubtypesLabel.setValue(context.getLabel("create.action.no.visible.subtypes.type.selected", new Object[]
                                {currentType.getCode()}));
                noSubtypesLabel.setVisible(true);
            }
            else
            {
                typeSelector.setVisible(true);
                noSubtypesLabel.setVisible(false);
            }
            subtypesPopup.open(openTypeChooser, "after_start");
        });
        subtypesPopup.addEventListener(Events.ON_OPEN,
                        event -> UITools.modifySClass(openTypeChooser, "ya-create-type-selector-button-opened", subtypesPopup.isVisible()));
    }


    protected DataType extractCreatedTypeCode(final ActionContext<Object> context)
    {
        final Object data = context.getData();
        String typeCode = "";
        if(data instanceof String)
        {
            typeCode = (String)data;
        }
        else if(data instanceof Map)
        {
            final String createdTypeKey = (String)context.getParameter(SETTING_CREATED_TYPE_KEY);
            if(StringUtils.isNotBlank(createdTypeKey))
            {
                typeCode = (String)((Map)data).get(createdTypeKey);
            }
            if(StringUtils.isBlank(typeCode))
            {
                typeCode = loadFromParentModel(context, createdTypeKey, String.class);
            }
        }
        return getType(typeCode);
    }


    protected void expandFirstNonAbstractSubtype(final AbstractTreeModel treeModel, final DataType currentType)
    {
        if(currentType != null && currentType.isAbstract())
        {
            treeModel.addOpenObject(currentType);
            final Optional<String> anyNonAbstractSubtype = currentType.getSubtypes().stream().filter(subtypeCode -> {
                final DataType dataType = loadType(subtypeCode);
                return dataType != null && !dataType.isAbstract();
            }).findAny();
            if(!anyNonAbstractSubtype.isPresent())
            {
                currentType.getSubtypes().stream()
                                .forEach(subtypeCode -> expandFirstNonAbstractSubtype(treeModel, loadType(subtypeCode)));
            }
        }
    }


    private DataType loadType(final String givenTypeCode)
    {
        DataType ret = null;
        try
        {
            ret = typeFacade.load(givenTypeCode);
        }
        catch(final TypeNotFoundException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Type not found", e);
            }
        }
        return ret;
    }


    @Override
    protected EventListener<? extends Event> createEventListener(final CockpitAction<Object, Object> action,
                    final ActionContext<Object> context, final ActionListener<Object> listener)
    {
        final EventListener createEventListener = super.createEventListener(action, context, listener);
        return (EventListener<Event>)event -> {
            final DataType effectiveType = getEffectiveType(context);
            if(effectiveType != null && effectiveType.isAbstract() && event.getTarget() != null
                            && event.getTarget().getParent() != null)
            {
                final Component typeSelector = event.getTarget().getParent()
                                .query(String.format(".%s", SCLASS_YA_CREATE_TYPE_SELECTOR_BUTTON));
                if(typeSelector != null)
                {
                    Events.sendEvent(typeSelector, new Event(Events.ON_CLICK));
                    return;
                }
            }
            createEventListener.onEvent(event);
        };
    }


    protected TreeitemRenderer createTreeItemRenderer(final ActionContext<Object> context)
    {
        return new TypeSelectorTreeItemRenderer(labelService, context)
        {
            @Override
            protected boolean isDisabled(final DataType type)
            {
                return !permissionFacade.canCreateTypeInstance(type.getCode());
            }


            @Override
            public void render(final Treeitem item, final DataType type, final int index)
            {
                super.render(item, type, index);
                UITools.modifySClass(item.getTreerow(), "ya-create-type-selector-abstracttype", type.isAbstract());
            }
        };
    }


    protected Toolbarbutton getOrCreateTypeChooserButton(final HtmlBasedComponent container)
    {
        for(final Component component : container.getChildren())
        {
            if(component instanceof Toolbarbutton)
            {
                return (Toolbarbutton)component;
            }
        }
        final Toolbarbutton openTypeChooser = new Toolbarbutton();
        openTypeChooser.setSclass(SCLASS_YA_CREATE_TYPE_SELECTOR_BUTTON);
        container.appendChild(openTypeChooser);
        return openTypeChooser;
    }


    protected Popup getOrCreatePopup(final HtmlBasedComponent container, final Label label,
                    final CockpitAction<Object, Object> action, final ActionContext<Object> context, final ActionListener<Object> listener)
    {
        for(final Component component : container.getChildren())
        {
            if(component instanceof Popup)
            {
                return (Popup)component;
            }
        }
        final Popup popup = new Popup();
        popup.setSclass("ya-create-type-selector-popup");
        YTestTools.modifyYTestId(popup, "create-type-selector-popup");
        container.appendChild(popup);
        final Tree typeSelector = new Tree();
        popup.appendChild(typeSelector);
        final Label noSubtypesLabel = new Label();
        noSubtypesLabel.setVisible(false);
        noSubtypesLabel.setSclass("ya-create-type-no-visible-subtypes");
        popup.appendChild(noSubtypesLabel);
        typeSelector.setItemRenderer(createTreeItemRenderer(context));
        typeSelector.addEventListener(Events.ON_CLICK, event -> {
            final int[][] openPaths = ((TypeSelectorTreeModel)typeSelector.<DataType>getModel()).getOpenPaths();
            storeInParentModel(openPaths, context, CREATE_ACTION_EXPANDED_PATHS);
            final Treeitem selectedItem = typeSelector.getSelectedItem();
            if(selectedItem != null)
            {
                final DataType selectedDataType = selectedItem.getValue();
                if(selectedDataType != null)
                {
                    if(selectedDataType.isAbstract())
                    {
                        selectedItem.setOpen(!selectedItem.isOpen());
                        selectedItem.setSelected(false);
                    }
                    else
                    {
                        storeInParentModel(selectedDataType, context, CREATE_ACTION_USER_CHOSEN_TYPE);
                        label.setValue(selectedDataType.getCode());
                        popup.close();
                        UITools.modifySClass(getOrCreateTypeChooserButton(container), "ya-create-type-selector-button-opened", false);
                        perform(action, context, listener);
                    }
                }
            }
        });
        container.appendChild(popup);
        return popup;
    }


    @Override
    protected void perform(final CockpitAction<Object, Object> action, final ActionContext<Object> context,
                    final ActionListener<Object> listener)
    {
        final DataType effectiveType = getEffectiveType(context);
        if(effectiveType != null)
        {
            if(effectiveType.isAbstract() || !permissionFacade.canCreateTypeInstance(effectiveType.getCode()))
            {
                Messagebox.show(context.getLabel("create.action.cannot.instantiate.type.selected", new Object[]
                                {effectiveType.getCode()}), null, Messagebox.OK, Messagebox.EXCLAMATION);
            }
            else
            {
                final ActionContext<Object> clonedContext = new ActionContext<>(context);
                if(context.getData() instanceof Map)
                {
                    clonedContext.setData(context.getData());
                }
                else
                {
                    clonedContext.setData(effectiveType.getCode());
                }
                super.perform(action, clonedContext, listener);
            }
        }
    }


    protected DataType getEffectiveType(final ActionContext<?> context)
    {
        DataType effectiveType = loadFromParentModel(context, CREATE_ACTION_USER_CHOSEN_TYPE, DataType.class);
        if(effectiveType == null)
        {
            effectiveType = loadFromParentModel(context, CREATE_ACTION_ROOT_TYPE, DataType.class);
        }
        return effectiveType;
    }


    protected DataType getType(final String currentType)
    {
        if(StringUtils.isNotBlank(currentType))
        {
            try
            {
                return getTypeFacade().load(currentType);
            }
            catch(final TypeNotFoundException tnfe)
            {
                LOG.warn(StringUtils.EMPTY, tnfe);
            }
        }
        return null;
    }


    @Override
    protected String getLocalizedName(final ActionContext<?> context)
    {
        final String localizedName = super.getLocalizedName(context);
        final String nameForType = getLocalizedNameForCreationType(context);
        return localizedName + StringUtils.SPACE + nameForType;
    }


    private String getLocalizedNameForCreationType(final ActionContext<?> context)
    {
        final DataType effectiveType = getEffectiveType(context);
        if(effectiveType == null)
        {
            return context.getLabel("create.action.no.type.selected");
        }
        return labelService.getObjectLabel(effectiveType.getCode());
    }


    @Override
    protected HtmlBasedComponent getOrCreateContainer(final Component parent)
    {
        HtmlBasedComponent container;
        final List<Component> children = parent.getChildren();
        final Iterator iterator = children.iterator();
        while(iterator.hasNext())
        {
            container = (HtmlBasedComponent)iterator.next();
            if(container.hasAttribute(ACTION_CONTAINER_ATTRIBUTE) && isTrue(container.getAttribute(ACTION_CONTAINER_ATTRIBUTE)))
            {
                return container;
            }
        }
        container = new Div();
        container.setParent(parent);
        container.setAttribute(ACTION_CONTAINER_ATTRIBUTE, Boolean.TRUE);
        UITools.modifySClass(container, SCLASS_YA_CREATE_CONTAINER, true);
        return container;
    }


    private boolean isTrue(final Object object)
    {
        return object instanceof Boolean && ((Boolean)object).booleanValue();
    }


    public CockpitSessionService getCockpitSessionService()
    {
        return cockpitSessionService;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }
}
