/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.baseeditorarea;

import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.ActionResult.StatusFlag;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.components.Action;
import com.hybris.cockpitng.components.Actions;
import com.hybris.cockpitng.components.Widgetslot;
import com.hybris.cockpitng.components.validation.ValidationRenderer;
import com.hybris.cockpitng.core.Executable;
import com.hybris.cockpitng.core.config.impl.jaxb.editorarea.EditorArea;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.core.model.StandardModelKeys;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.renderers.header.WidgetVisibilityState;
import com.hybris.cockpitng.renderers.header.WidgetVisibilityStateAware;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import com.hybris.cockpitng.validation.ValidationContext;
import com.hybris.cockpitng.validation.model.ValidationResult;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import com.hybris.cockpitng.widgets.common.NotifyingWidgetComponentRenderer;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import com.hybris.cockpitng.widgets.util.ReferenceModelProperties;
import com.hybris.cockpitng.widgets.util.impl.DefaultReferenceModelProperties;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.validation.constraints.NotNull;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbarbutton;
import org.zkoss.zul.Vlayout;
import org.zkoss.zul.Window;

public class DefaultEditorAreaController extends DefaultWidgetController implements WidgetVisibilityStateAware
{
    /**
     * Event fired after editors components are detached from EditorArea UI
     */
    public static final String ON_EDITORS_DETACHED = "onEditorsDetached";
    public static final String MODEL_LAST_PINNED_TAB = "lastPinnedTab";
    public static final String MODEL_CURRENT_OBJECT = StandardModelKeys.CONTEXT_OBJECT;
    public static final String MODEL_CURRENT_OBJECT_TYPE = "currentType";
    /**
     * @deprecated since 6.6
     * @see DefaultReferenceModelProperties#MODEL_ALL_REFERENCED_OBJECTS
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String MODEL_ALL_REFERENCED_OBJECTS = DefaultReferenceModelProperties.MODEL_ALL_REFERENCED_OBJECTS;
    public static final String MODEL_VALUE_CHANGED = "valueChanged";
    /**
     * @deprecated since 6.6
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String VALIDATION_PATH_PREFIX = String.format("%s.%s", StandardModelKeys.VALIDATION_RESULT_KEY,
                    MODEL_CURRENT_OBJECT);
    public static final String SOCKET_INPUT_RESIZE = "resize";
    public static final String SOCKET_INPUT_OBJECT = "inputObject";
    public static final String SOCKET_INPUT_RESET = "reset";
    public static final String SOCKET_INPUT_FOCUS = "focus";
    public static final String SOCKET_OUTPUT_WIDGET_TITLE = "widgetTitle";
    public static final String SOCKET_OUTPUT_OBJECT_SAVED = "objectSaved";
    public static final String EDITOR_AREA_CONFIGURATION = "editorAreaConfiguration";
    public static final String EDITOR_AREA_WIDTH_REQUEST = "onEditorAreaWidthRequest";
    public static final String RESPONSIVE_IMAGE_URL = "/images/icon_action_view_fluid_default.png";
    public static final String STATIC_IMAGE_URL = "/images/icon_action_view_static_default.png";
    public static final String RESPONSIVE_IMAGE_URL_HOVER = "/images/icon_action_view_fluid_hover.png";
    public static final String STATIC_IMAGE_URL_HOVER = "/images/icon_action_view_static_hover.png";
    public static final String MODEL_INPUT_OBJECT_IS_NEW = "inputObjectIsNew";
    public static final String MODEL_INPUT_OBJECT_IS_MODIFIED = "inputObjectIsModified";
    /**
     * @deprecated since 6.6
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String MODEL_SUCCESS_NOTIFICATION_ID = "successNotificationId";
    /**
     * @deprecated since 6.6
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String MODEL_FAILURE_NOTIFICATION_ID = "failureNotificationId";
    public static final String MODEL_EDITOR_AREA_CONFIG_CTX = "editorAreaConfigCtx";
    public static final String MODEL_EDITOR_INPUT_DATA = "editorAreaInputData";
    public static final String SETTING_DISABLE_RESET_FOR_MODIFIED_INPUT_OBJECT = "disableResetForModifiedInputObject";
    public static final String SETTING_MOLD_ACCORDION = "accordionMold";
    /**
     * @deprecated since 6.6
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String COCKPIT_EVENT_CALLBACK_MANAGER = "cockpitEventCallbackManager";
    /**
     * @deprecated since 6.6
     */
    @Deprecated(since = "6.6", forRemoval = true)
    public static final String USER_NOTIFICATION_CURRENT_ENTITY_AUTOSAVED = "user.notification.current.entity.autosaved";
    public static final String SELECTOR_STATIC_SECTION_SLOT = "#staticSectionSlot";
    public static final String YW_STATIC_SECTION_ACTIVE = "yw-static-section-active";
    public static final String YW_STATIC_SECTION_INACTIVE = "yw-static-section-inactive";
    public static final int FLUID_MIN_WIDTH = 700;
    public static final int FLUID_MAX_WIDTH = 1000;
    public static final String GLOBAL_EVENT_ITEM_LOCKED_STATE_CHANGED = "itemLockedStateChangedGlobalEvent";
    public static final String GLOBAL_EVENT_ITEM_DYNAMIC_ATTRIBUTE_VALUE_CHANGED = "dynamicAttributeValueChanged";
    public static final String EDITOR_AREA_TAB_IS_DYNAMIC = "editorAreaTabIsDynamic";
    /**
     * @deprecated since 6.6
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected static final String ITEM_VALIDATION_ERROR_MSG = "user.notification.item.validation.error";
    /**
     * @deprecated since 6.6
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected static final String ITEM_VALIDATION_WARNING_MSG = "user.notification.item.validation.warning";
    protected static final String COMP_ID_SAVE_BTN = "saveButton";
    protected static final String COMP_ID_CANCEL_BTN = "cancelButton";
    protected static final String COMP_ID_VIEW_BTN = "viewModeButton";
    protected static final String COMP_ID_ATTRIBUTES_DIV = "attributesDiv";
    protected static final String COMP_ID_ACTION_SLOT = "actionSlot";
    protected static final String MODEL_FLUID_VIEW_MODE = "fluidViewMode";
    protected static final String ACTION_SLOT_COMPONENT_ID = "actionSlotComponentId";
    protected static final String SETTING_CONFIGURATION_CONTEXT = "editorAreaConfigCtx";
    protected static final String SETTING_FLUID_VIEW_CODE = "fluidViewCode";
    protected static final String SETTING_ESSENTIALS_IN_OVERVIEW_TAB = "essentialsInOverviewTab";
    protected static final String SETTING_TITLE_BY_CTX = "enableTitleByCtxName";
    private static final String SETTING_BOTTOM_TOOLBAR = "bottomToolbar";
    private static final String SETTING_TOOLBAR_DISABLED = "toolbarDisabled";
    private static final String SCLASS_CONTAINER = "yw-editorarea";
    static final String SCLASS_ATTRIBUTES_DIV_SCLASS = "y-editor-area-attributes-cnt";
    private static final String SCLASS_ATTRIBUTES_DIV_EMPTY = "yw-editorarea-empty";
    private static final String SCLASS_PREFIX_CONTAINER_COMPONENT = "yw-editor-area-ctx-";
    private static final String SCLASS_PREFIX_CONTAINER_TYPE = "yw-editor-area-";
    private static final String SCLASS_BODY = "yw-editorarea-body";
    private static final String LABEL_EMPTY_EDITOR_AREA = "editorArea.empty";
    private static final String EDITOR_AREA_LOGIC_HANDLER = "editorAreaLogicHandler";
    private static final Logger LOG = LoggerFactory.getLogger(DefaultEditorAreaController.class);
    @WireVariable
    private transient ValidationRenderer validationRenderer;
    @WireVariable
    private transient ReferenceModelProperties referenceModelProperties;
    @WireVariable
    private transient CockpitEventQueue cockpitEventQueue;
    @WireVariable
    private transient ObjectFacade objectFacade;
    @WireVariable
    private transient LabelService labelService;
    @WireVariable
    private transient TypeFacade typeFacade;
    @WireVariable
    private transient NotifyingWidgetComponentRenderer<Component, EditorArea, Object> editorAreaRenderer;
    @WireVariable
    private transient CockpitLocaleService cockpitLocaleService;
    @WireVariable
    private transient NotificationService notificationService;
    @Wire
    private Vlayout body;
    @Wire
    private Button saveButton;
    @Wire
    private Button cancelButton;
    @Wire
    private Toolbarbutton viewModeButton;
    @Wire
    private Div attributesDiv;
    @Wire
    private Component toolbarContainer;
    @Wire
    private Label editorAreaTitle;
    @Wire
    private Actions actionSlot;
    private transient DefaultEditorAreaControllerSettingsLoader settingsLoader;
    private transient DefaultEditorAreaControllerConfigurationLoader configurationLoader;
    private transient DefaultEditorAreaControllerFluidModeSwitcherDelegate fluidModeSwitcherDelegate;
    private transient DefaultEditorAreaControllerModelOperationsDelegate modelOperationsDelegate;
    private transient DefaultEditorAreaControllerPersistenceListenersDelegate persistenceListeners;
    private transient DefaultEditorAreaControllerComponentsSizeDelegate componentsSizeDelegate;
    private transient DefaultEditorAreaControllerValidationPopupDelegate validationPopupDelegate;
    private transient DefaultEditorAreaControllerWidgetTitleDelegate widgetTitleDelegate;
    private transient DefaultEditorAreaControllerConcurrentModificationDelegate concurrentModificationDelegate;
    private transient EditorAreaLogicHandler logicHandler;
    private transient Set<EditorAreaBeforeLogicHandler> beforeLogicHandlers;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        initConfigCtx();
        beforeLogicHandlers = new HashSet<>();
        getPersistenceListeners().resetListeners();
        addInitialObservers();
        prepareValidationResultModel();
        prepareValidationResultsPopup(comp);
        prepareReferenceModelProperties();
        getConcurrentModificationDelegate().initializeConcurrentModificationMessageboxVisibility();
        final Object currentObject = getCurrentObject();
        if(currentObject != null)
        {
            try
            {
                setValue(MODEL_CURRENT_OBJECT, objectFacade.reload(currentObject));
                getModelOperationsDelegate().resetValueChangeState();
            }
            catch(final ObjectNotFoundException e)
            {
                handleObjectNotFoundException(e, currentObject);
            }
        }
        // current type is set in socket event setObject()
        updateView(getModelOperationsDelegate().getCurrentType());
        // update toolbarContainer state according to settings
        if(getWidgetSettings().getBoolean(SETTING_BOTTOM_TOOLBAR))
        {
            final Component parent = toolbarContainer.getParent();
            toolbarContainer.detach();
            parent.appendChild(toolbarContainer);
        }
    }


    @Override
    public void handleVisibilityState(final WidgetVisibilityState state)
    {
        if(WidgetVisibilityState.VISIBLE.equals(state))
        {
            DefaultEditorAreaControllerClientOperationsDelegate.fireAttributesDivWidthRequest(attributesDiv.getUuid());
        }
    }


    @ViewEvent(componentID = COMP_ID_ATTRIBUTES_DIV, eventName = EDITOR_AREA_WIDTH_REQUEST)
    public void onParentWidthRequest(final Event event)
    {
        final int width = Integer.parseInt(Objects.toString(event.getData()));
        applySclassAccordingToWidth(attributesDiv, width);
    }


    @ViewEvent(componentID = COMP_ID_ACTION_SLOT, eventName = Action.ON_ACTION_PERFORMED)
    public void onActionPerformed(final Event event)
    {
        final ActionResult actionResult = (ActionResult)event.getData();
        final EnumSet statusFlags = actionResult.getStatusFlags();
        if(statusFlags.contains(StatusFlag.OBJECT_PERSISTED) || statusFlags.contains(StatusFlag.OBJECT_MODIFIED))
        {
            setObjectInternal(getCurrentObject());
        }
    }


    @ViewEvent(componentID = COMP_ID_VIEW_BTN, eventName = Events.ON_CLICK)
    public void handleViewModeButtonClick(final Event event) throws TypeNotFoundException
    {
        getFluidModeSwitcherDelegate().changeViewMode();
    }


    @ViewEvent(componentID = COMP_ID_SAVE_BTN, eventName = Events.ON_CLICK)
    public void saveObject()
    {
        if(isModelValueChanged() && getSaveButton() != null && !getSaveButton().isDisabled())
        {
            final ValidationResult validation = doValidate(getCurrentObject(), false);
            if(ValidationSeverity.WARN.isHigherThan(validation.getHighestNotConfirmedSeverity()))
            {
                executeSaveWithConfirmation(this::saveObjectInternal);
            }
        }
        else
        {
            LOG.warn("Save operation requested on unchanged object or disable save button");
        }
    }


    @ViewEvent(componentID = COMP_ID_CANCEL_BTN, eventName = Events.ON_CLICK)
    public void cancelObjectModification()
    {
        final Object currentObject = getCurrentObject();
        if(currentObject != null && getCancelButton() != null && !getCancelButton().isDisabled())
        {
            try
            {
                getPersistenceListeners().executeBeforeCancelModificationCallbacks();
                beforeRefresh(currentObject);
                final EditorArea editorArea = getConfigurationLoader().getEditorAreaConfiguration();
                final Object reloadedCurrentObject = performRefresh(currentObject, editorArea);
                setValue(MODEL_CURRENT_OBJECT, reloadedCurrentObject);
                doValidate(reloadedCurrentObject, true, false);
                final Collection reloadedForNotification = Collections.singleton(performRefresh(currentObject, editorArea));
                publishCRUDCockpitEventNotification(ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, reloadedForNotification, null);
                getPersistenceListeners().executeAfterCancelModificationCallbacks();
                getModelOperationsDelegate().resetValueChangeState();
            }
            catch(final ObjectNotFoundException exc)
            {
                handleObjectNotFoundException(exc, currentObject);
            }
            catch(final Exception ex)
            {
                LOG.error("Refresh object error:", ex);
                throw ex;
            }
        }
        else
        {
            LOG.warn("Cancel operation requested on null object or disable save button");
        }
    }


    @SocketEvent(socketId = SOCKET_INPUT_RESIZE)
    public void handleResizeParent()
    {
        if(getModelOperationsDelegate().isFluidViewMode())
        {
            DefaultEditorAreaControllerClientOperationsDelegate.fireAttributesDivWidthRequest(attributesDiv.getUuid());
        }
    }


    @SocketEvent(socketId = SOCKET_INPUT_FOCUS)
    public void focusAttribute(final FocusRequest focusRequest)
    {
        if(focusRequest != null)
        {
            if(focusRequest.getObject() != null && ObjectUtils.notEqual(focusRequest.getObject(), getCurrentObject()))
            {
                setObjectInternal(focusRequest.getObject());
            }
            focusAttribute(focusRequest.getQualifier());
        }
    }


    /**
     * @deprecated since 6.7 use {@link DefaultEditorAreaControllerValidationPopupDelegate#focusAttribute(String)} instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void focusAttribute(final String qualifier)
    {
        getValidationPopupDelegate().focusAttribute(qualifier);
    }


    /**
     * Resets widget to initial state, displaying no data.
     */
    @SocketEvent(socketId = SOCKET_INPUT_RESET)
    public void reset()
    {
        clearAttributesDiv();
        getPersistenceListeners().resetListeners();
        getModelOperationsDelegate().resetModel();
        resetView();
    }


    @SocketEvent(socketId = SOCKET_INPUT_OBJECT)
    public void setObject(final Object inputData)
    {
        clearBeforeLogicHandlers();
        if(inputDataAlreadySet(inputData))
        {
            return;
        }
        getModelOperationsDelegate().retrieveAndSetCurrentContext(inputData);
        setObjectInternal(inputData);
    }


    protected void setObjectInternal(final Object inputData)
    {
        if(inputData == null)
        {
            reset();
            return;
        }
        getConcurrentModificationDelegate().resetConcurrentModificationState();
        final Object objectInput = getModelOperationsDelegate().retrieveInputObject(inputData);
        final Object currentObject;
        final boolean inputObjectIsNew = getObjectFacade().isNew(objectInput);
        final boolean inputObjectIsModified = getObjectFacade().isModified(objectInput);
        try
        {
            currentObject = reloadIfNecessary(objectInput, inputObjectIsModified, inputObjectIsNew);
            adjustValueState(inputObjectIsModified, inputObjectIsNew);
        }
        catch(final ObjectNotFoundException e)
        {
            handleObjectNotFoundException(e, objectInput);
            return;
        }
        setValue(DefaultReferenceModelProperties.MODEL_ALL_REFERENCED_OBJECTS, null);
        setValue(MODEL_FLUID_VIEW_MODE, null);
        getPersistenceListeners().resetListeners();
        clearAttributesDiv();
        clearActionSlot();
        final DataType genericType = adjustCurrentType(currentObject);
        setValue(MODEL_CURRENT_OBJECT, currentObject);
        doValidate(currentObject, true, false);
        adjustValueState(inputObjectIsModified, inputObjectIsNew);
        updateView(genericType);
    }


    protected boolean inputDataAlreadySet(final Object inputData)
    {
        final Object storedData = getValue(MODEL_EDITOR_INPUT_DATA, Object.class);
        if(ObjectUtils.notEqual(inputData, storedData))
        {
            setValue(MODEL_EDITOR_INPUT_DATA, inputData);
            return false;
        }
        else
        {
            return true;
        }
    }


    protected DataType adjustCurrentType(final Object currentObject)
    {
        final DataType oldType = getModelOperationsDelegate().getCurrentType();
        try
        {
            final String type = getTypeFacade().getType(currentObject);
            final DataType genericType = getTypeFacade().load(type);
            if(!typesEqual(oldType, genericType))
            {
                getModelOperationsDelegate().onTypeChange(genericType);
            }
            logicHandler = lookupEditorAreaLogicHandler(getConfigurationLoader().getEditorAreaConfiguration());
            return genericType;
        }
        catch(final TypeNotFoundException ex)
        {
            LOG.warn("Cannot find generic type", ex);
            return oldType;
        }
    }


    protected Object reloadIfNecessary(final Object objectInput, final boolean inputObjectIsModified,
                    final boolean inputObjectIsNew) throws ObjectNotFoundException
    {
        if(inputObjectIsModified || inputObjectIsNew)
        {
            return objectInput;
        }
        return objectFacade.reload(objectInput);
    }


    protected void adjustValueState(final boolean inputObjectIsModified, final boolean inputObjectIsNew)
    {
        if(inputObjectIsModified || inputObjectIsNew)
        {
            getModelOperationsDelegate().setValueChangeState(inputObjectIsNew, inputObjectIsModified);
        }
        else
        {
            getModelOperationsDelegate().resetValueChangeState();
        }
    }


    @GlobalCockpitEvent(eventName = Events.ON_CLIENT_INFO, scope = CockpitEvent.SESSION)
    public void handleResizeBrowser(final CockpitEvent event)
    {
        if(getModelOperationsDelegate().isFluidViewMode())
        {
            DefaultEditorAreaControllerClientOperationsDelegate.fireAttributesDivWidthRequest(attributesDiv.getUuid());
        }
        if(getValidationResults() != null)
        {
            getValidationResults().invalidate();
        }
    }


    @GlobalCockpitEvent(eventName = GLOBAL_EVENT_ITEM_DYNAMIC_ATTRIBUTE_VALUE_CHANGED, scope = CockpitEvent.SESSION)
    public void handleDynamicAttributeValueChanged(final CockpitEvent event)
    {
        if(event.getData() != null)
        {
            setValue(MODEL_VALUE_CHANGED, Boolean.TRUE);
            updateSaveAndCancelButtonsState();
            if(((HashMap)event.getData()).get("isAddSelectedObject") == Boolean.TRUE)
            {
                onAddSelectedObject(event.getData());
            }
        }
    }


    @GlobalCockpitEvent(eventName = GLOBAL_EVENT_ITEM_LOCKED_STATE_CHANGED, scope = CockpitEvent.SESSION)
    public void itemLockingChanged(final CockpitEvent event)
    {
        if(event != null)
        {
            final Object data = event.getData();
            final boolean isValidObject = data != null && data.equals(getCurrentObject());
            final boolean isValidCollection = data instanceof Collection && ((Collection)data).contains(getCurrentObject());
            if(isValidObject || isValidCollection)
            {
                try
                {
                    final Object refreshedObject = objectFacade.reload(getCurrentObject());
                    setObjectInternal(refreshedObject);
                }
                catch(final ObjectNotFoundException e)
                {
                    LOG.warn("Could not reload object after item locked state has been changed.", e);
                }
            }
        }
    }


    /**
     * Object deleted global event handler.<br>
     * Checks if deleted object is equal to current object or if it's referenced by any editor within the editor area. If
     * it's equal to current one, <code>null</code> is set as new current. If it's referenced, the corresponding editors are
     * reloaded.
     *
     * @param event
     *           delete event
     */
    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_DELETED_EVENT, scope = CockpitEvent.SESSION)
    public void handleObjectDeletedEvent(final CockpitEvent event)
    {
        if(!canProcessEvent(event))
        {
            return;
        }
        if(getReferenceModelProperties().isEdited(event.getData(), getCurrentObject()))
        {
            setObjectInternal(null);
        }
        else
        {
            getReferenceModelProperties().handleReferencedObjectDeletedEvent(event.getData());
        }
    }


    /**
     * Object updated global event handler.<br>
     * Default implementation checks if deleted object is referenced by any editor within the editor area, and if it is, the
     * corresponding editors are reloaded.
     *
     * @param event
     *           update event
     */
    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, scope = CockpitEvent.SESSION)
    public void handleObjectUpdatedEvent(final CockpitEvent event)
    {
        final Object currentObject = getCurrentObject();
        if(!canProcessEvent(event) || currentObject == null)
        {
            return;
        }
        final Optional<Object> updateCurrentObject = event.getDataAsCollection().stream().filter(currentObject::equals).findAny();
        if(updateCurrentObject.isPresent() && !Objects.equals(event.getSource(), getCRUDNotificationSource()))
        {
            if(isModelValueChanged())
            {
                getConcurrentModificationDelegate().showConcurrentModificationMessagebox();
            }
            else
            {
                setObjectInternal(updateCurrentObject.get());
            }
            return;
        }
        event.getDataAsCollection().forEach(data -> {
            final Set<String> properties = getReferenceModelProperties().getReferencedModelProperties(data);
            if(CollectionUtils.isNotEmpty(properties))
            {
                getReferenceModelProperties().updateReferenceProperties(properties, data, this::handleObjectNotFoundException);
            }
        });
    }


    /** @deprecated since 6.7 use {@link DefaultEditorAreaControllerWidgetTitleDelegate#updateWidgetTitle()} instead. */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void updateWidgetTitle()
    {
        getWidgetTitleDelegate().updateWidgetTitle();
    }


    /**
     * @deprecated since 6.7, use {@link DefaultEditorAreaControllerComponentsSizeDelegate#applySclassAccordingToWidth(int)}
     *             insted.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void applySclassAccordingToWidth(final HtmlBasedComponent targetComponent, final int width)
    {
        getComponentsSizeDelegate().applySclassAccordingToWidth(width);
    }


    /**
     * @deprecated since 6.6 - not used
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected boolean showLinksInNotifications()
    {
        return false;
    }


    /** @deprecated since 6.7 use {@link DefaultEditorAreaControllerModelOperationsDelegate#initContext()} instead. */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void initConfigCtx()
    {
        getModelOperationsDelegate().initContext();
    }


    @InextensibleMethod
    private void addInitialObservers()
    {
        getModel().addObserver(MODEL_CURRENT_OBJECT, () -> {
            if(getCurrentObject() != null)
            {
                setValue(MODEL_VALUE_CHANGED, Boolean.TRUE);
            }
        });
        getModel().addObserver(MODEL_VALUE_CHANGED, this::updateSaveAndCancelButtonsState);
    }


    @InextensibleMethod
    private void clearActionSlot()
    {
        actionSlot.getChildren().clear();
    }


    @InextensibleMethod
    private void initializeActionSlot()
    {
        final DataType currentType = getModelOperationsDelegate().getCurrentType();
        if(currentType != null)
        {
            final String componentContext = getWidgetSettings().getString(ACTION_SLOT_COMPONENT_ID);
            actionSlot.setConfig(String.format("component=%s,type=%s", componentContext, currentType.getCode()));
        }
        else
        {
            actionSlot.setConfig(null);
        }
        actionSlot.reload();
    }


    /** @deprecated since 6.7 not used, see {@link DefaultEditorAreaControllerModelOperationsDelegate#isFluidViewMode()}. */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void prepareViewModeButton()
    {
        getFluidModeSwitcherDelegate().prepareViewModeButton();
    }


    /** @deprecated since 6.7 not used, see {@link DefaultEditorAreaControllerValidationPopupDelegate} */
    @Deprecated(since = "6.7", forRemoval = true)
    protected ValidationContext createValidationContext(final boolean applyConfirmed)
    {
        return null;
    }


    protected ValidationResult doValidate(final Object objectToValidate, final boolean preventBroadcast)
    {
        return getValidationPopupDelegate().doValidate(objectToValidate, preventBroadcast);
    }


    protected ValidationResult doValidate(final Object objectToValidate, final boolean preventBroadcast,
                    final boolean applyConfirmed)
    {
        return getValidationPopupDelegate().doValidate(objectToValidate, preventBroadcast, applyConfirmed);
    }


    protected void saveObjectInternal()
    {
        final Object currentObject = getCurrentObject();
        try
        {
            final EditorArea editorArea = getConfigurationLoader().getEditorAreaConfiguration();
            beforeSave(currentObject);
            final Object savedObject = performSave(currentObject, editorArea);
            getPersistenceListeners().notifyAfterSaveListeners();
            final Object reloadedCurrentObject = performRefresh(savedObject, editorArea);
            setValue(MODEL_CURRENT_OBJECT, reloadedCurrentObject);
            handleObjectSavingSuccess(reloadedCurrentObject);
            final Object reloadedForNotification = performRefresh(savedObject, editorArea);
            sendOutput(SOCKET_OUTPUT_OBJECT_SAVED, reloadedForNotification);
            publishCRUDCockpitEventNotification(ObjectFacade.OBJECTS_UPDATED_EVENT, Collections.singleton(reloadedForNotification),
                            new DefaultContext());
            if(BooleanUtils.isTrue(getValue(EDITOR_AREA_TAB_IS_DYNAMIC, Boolean.class)))
            {
                setObjectInternal(reloadedCurrentObject);
            }
            if(BooleanUtils.isTrue(getValue(MODEL_INPUT_OBJECT_IS_NEW, Boolean.class)))
            {
                setObjectInternal(currentObject);
            }
            else
            {
                getModelOperationsDelegate().resetValueChangeState();
                getWidgetTitleDelegate().updateWidgetTitle();
            }
        }
        catch(final ObjectSavingException ex)
        {
            handleObjectSavingException(ex);
        }
        catch(final ObjectNotFoundException ex)
        {
            handleObjectNotFoundException(ex, currentObject);
        }
        catch(final Exception ex)
        {
            LOG.error("Save object error:", ex);
            throw ex;
        }
    }


    protected Object performRefresh(final Object currentObject, final EditorArea editorArea) throws ObjectNotFoundException
    {
        if(getLogicHandler() != null)
        {
            return getLogicHandler().performRefresh(getWidgetInstanceManager(), currentObject);
        }
        return null;
    }


    protected Object performSave(final Object currentObject, final EditorArea editorArea) throws ObjectSavingException
    {
        if(getLogicHandler() != null)
        {
            return getLogicHandler().performSave(getWidgetInstanceManager(), currentObject);
        }
        return null;
    }


    protected void executeSaveWithConfirmation(final Executable save)
    {
        if(getLogicHandler() != null)
        {
            getLogicHandler().executeSaveWithConfirmation(getWidgetInstanceManager(), save, getCurrentObject());
        }
        else
        {
            save.execute();
        }
    }


    protected void beforeSave(final Object currentObject)
    {
        for(final EditorAreaBeforeLogicHandler handler : getBeforeLogicHandlers())
        {
            handler.beforeSave(getWidgetInstanceManager(), currentObject);
        }
    }


    protected void beforeRefresh(final Object currentObject)
    {
        for(final EditorAreaBeforeLogicHandler handler : getBeforeLogicHandlers())
        {
            handler.beforeRefresh(getWidgetInstanceManager(), currentObject);
        }
    }


    protected void onAddSelectedObject(final Object addedObject)
    {
        for(final EditorAreaBeforeLogicHandler handler : getBeforeLogicHandlers())
        {
            handler.onAddSelectedObject(addedObject);
        }
    }


    /** @deprecated since 6.7 use {@link DefaultEditorAreaControllerConfigurationLoader#getEditorAreaConfiguration()} */
    @Deprecated(since = "6.7", forRemoval = true)
    protected EditorArea getStoredEditorAreaConfiguration()
    {
        return getEditorAreaConfiguration();
    }


    protected EditorAreaLogicHandler lookupEditorAreaLogicHandler(final EditorArea editorArea)
    {
        final String editorAreaHandlerBeanId = resolveEditorAreaLogicHandler(editorArea);
        return BackofficeSpringUtil.getBean(editorAreaHandlerBeanId, EditorAreaLogicHandler.class);
    }


    protected String resolveEditorAreaLogicHandler(final EditorArea editorArea)
    {
        final String editorAreaHandlerBeanId = editorArea != null ? editorArea.getLogicHandler() : StringUtils.EMPTY;
        return StringUtils.defaultIfBlank(editorAreaHandlerBeanId, EDITOR_AREA_LOGIC_HANDLER);
    }


    /**
     * @deprecated since 6.7 dont store configuration in model, use
     *             {@link DefaultEditorAreaControllerModelOperationsDelegate#prepareValidationResultModel() }
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void prepareValidationResultModel()
    {
        getModelOperationsDelegate().prepareValidationResultModel();
    }


    /**
     * @deprecated since 6.7 dont store configuration in model, use
     *             {@link DefaultEditorAreaControllerValidationPopupDelegate#prepareValidationResultsPopup(Component, Executable) }
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void prepareValidationResultsPopup(final Component parent)
    {
        getValidationPopupDelegate().prepareValidationResultsPopup(parent, this::saveObject);
    }


    @InextensibleMethod
    private void prepareReferenceModelProperties()
    {
        getReferenceModelProperties().initialize(getModel());
    }


    protected boolean isModelValueChanged()
    {
        return Boolean.TRUE.equals(getValue(MODEL_VALUE_CHANGED, Boolean.class));
    }


    @InextensibleMethod
    private void publishCRUDCockpitEventNotification(final String info, final Collection obj, final Context ctx)
    {
        final DefaultCockpitEvent event = new DefaultCockpitEvent(info, obj, getCRUDNotificationSource());
        populateEventContext(ctx, event);
        cockpitEventQueue.publishEvent(event);
    }


    protected String getCRUDNotificationSource()
    {
        return getWidgetslot().getWidgetInstance().getId();
    }


    protected void populateEventContext(final Context source, final DefaultCockpitEvent destination)
    {
        if(source != null)
        {
            source.getAttributeNames().forEach(a -> destination.getContext().put(a, source.getAttribute(a)));
        }
    }


    /**
     * The default implementation just notifies user via
     * {@link NotificationService#notifyUser(String, String, com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level, Object...)}.
     *
     * @param savedObject
     *           the item that has been successfully saved.
     */
    protected void handleObjectSavingSuccess(final Object savedObject)
    {
        getNotificationService().notifyUser(getNotificationSource(), NotificationEventTypes.EVENT_TYPE_OBJECT_UPDATE,
                        NotificationEvent.Level.SUCCESS, Collections.singletonList(savedObject));
    }


    /**
     * The default implementation just notifies the user via
     * {@link NotificationService#notifyUser(String, String, com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent.Level, Object...)}.
     *
     * @param savingException
     *           the exception to handle.
     */
    protected void handleObjectSavingException(final ObjectSavingException savingException)
    {
        LOG.error(savingException.getLocalizedMessage(), savingException);
        getNotificationService().notifyUser(getNotificationSource(), NotificationEventTypes.EVENT_TYPE_OBJECT_UPDATE,
                        NotificationEvent.Level.FAILURE, Collections.singletonMap(getCurrentObject(), savingException));
    }


    /**
     * @deprecated since 6.7, use
     *             {@link NotificationService#getWidgetNotificationSource(com.hybris.cockpitng.engine.WidgetInstanceManager)}
     *             instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected String getNotificationSource()
    {
        return getNotificationService().getWidgetNotificationSource(getWidgetInstanceManager());
    }


    /**
     * @deprecated since 6.6
     */
    @Deprecated(since = "6.6", forRemoval = true)
    protected String getSuccessNotificationId()
    {
        return StringUtils.EMPTY;
    }


    /**
     * @deprecated since 6.7 use {@link DefaultEditorAreaControllerPersistenceListenersDelegate#notifyAfterSaveListeners()}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void notifyAfterSaveListeners() throws ObjectSavingException
    {
        getPersistenceListeners().notifyAfterSaveListeners();
    }


    @InextensibleMethod
    private static boolean canProcessEvent(final CockpitEvent event)
    {
        return event != null && event.getData() != null;
    }


    @InextensibleMethod
    private void resetView()
    {
        updateView(null);
    }


    @InextensibleMethod
    void updateView(final DataType currentType)
    {
        getWidgetTitleDelegate().updateWidgetTitle();
        updateSaveAndCancelButtonsState();
        getFluidModeSwitcherDelegate().prepareViewModeButton();
        initializeActionSlot();
        updateToolbarState();
        showAllAttributes(currentType);
        getBody().setSclass(SCLASS_BODY);
        if(currentType != null)
        {
            UITools.addSClass(getBody(), SCLASS_PREFIX_CONTAINER_TYPE + formatSClass(currentType.getCode()));
        }
        if(StringUtils.isNoneBlank(getModelOperationsDelegate().getCurrentContext()))
        {
            UITools.addSClass(getBody(),
                            SCLASS_PREFIX_CONTAINER_COMPONENT + formatSClass(getModelOperationsDelegate().getCurrentContext()));
        }
    }


    protected String formatSClass(final String sclass)
    {
        return sclass.replace(".", "-").replace("_", "-").toLowerCase(Locale.ENGLISH);
    }


    @InextensibleMethod
    private void updateSaveAndCancelButtonsState()
    {
        if(getSaveButton() != null)
        {
            getSaveButton().setDisabled(BooleanUtils.isNotTrue(getValue(MODEL_VALUE_CHANGED, Boolean.class)));
        }
        if(getCancelButton() != null)
        {
            getCancelButton().setDisabled(shouldDisableCancelButton());
        }
    }


    @InextensibleMethod
    private boolean shouldDisableCancelButton()
    {
        final boolean currentObjectIsNull = getCurrentObject() == null;
        final boolean inputObjectIsNew = BooleanUtils.isTrue(getValue(MODEL_INPUT_OBJECT_IS_NEW, Boolean.class));
        return currentObjectIsNull || inputObjectIsNew;
    }


    @InextensibleMethod
    private void updateToolbarState()
    {
        final Boolean isToolbarDisabled = Boolean.valueOf(getWidgetSettings().getBoolean(SETTING_TOOLBAR_DISABLED));
        final boolean isToolbarEnabled = BooleanUtils.isNotTrue(isToolbarDisabled);
        final boolean currentObjectIsNotNull = getCurrentObject() != null;
        final boolean isToolbarContainerVisible = isToolbarEnabled && currentObjectIsNotNull;
        toolbarContainer.setVisible(isToolbarContainerVisible);
    }


    protected Object getCurrentObject()
    {
        return getModelOperationsDelegate().getCurrentObject();
    }


    /** @deprecated since 6.7 use {@link DefaultEditorAreaControllerModelOperationsDelegate#getCurrentValidationResult()} */
    @Deprecated(since = "6.7", forRemoval = true)
    protected ValidationResult getCurrentValidationResult()
    {
        return getModelOperationsDelegate().getCurrentValidationResult();
    }


    @InextensibleMethod
    private static boolean typesEqual(final DataType t1, final DataType t2)
    {
        if(t1 == null && t2 == null)
        {
            return true;
        }
        if(t1 == null || t2 == null)
        {
            return false;
        }
        return StringUtils.equals(t2.getCode(), t1.getCode());
    }


    @InextensibleMethod
    private void handleObjectNotFoundException(final ObjectNotFoundException exception, final Object objectInput)
    {
        reset();
        if(LOG.isWarnEnabled())
        {
            LOG.warn("Object could not be found: " + objectInput, exception);
        }
        getNotificationService().notifyUser(getNotificationSource(), NotificationEventTypes.EVENT_TYPE_OBJECT_LOAD,
                        NotificationEvent.Level.FAILURE, Collections.singletonMap(objectInput, exception));
        sendOutput(SOCKET_OUTPUT_OBJECT_SAVED, null);
    }


    /** @deprecated since 6.7 use {@link DefaultEditorAreaControllerModelOperationsDelegate#onTypeChange(DataType)} */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void onTypeChanged()
    {
        setValue(MODEL_LAST_PINNED_TAB, null);
    }


    @InextensibleMethod
    private void clearAttributesDiv()
    {
        final Widgetslot staticSectionSlot = findStaticSectionSlot();
        attributesDiv.getChildren().clear();
        if(staticSectionSlot != null)
        {
            UITools.modifySClass(staticSectionSlot, YW_STATIC_SECTION_ACTIVE, false);
            UITools.modifySClass(staticSectionSlot, YW_STATIC_SECTION_INACTIVE, true);
            attributesDiv.appendChild(staticSectionSlot);
        }
        sendEditorsDetachedEvent();
    }


    protected void sendEditorsDetachedEvent()
    {
        Events.sendEvent(ON_EDITORS_DETACHED, getWidgetslot(), new Event(ON_EDITORS_DETACHED));
    }


    protected Widgetslot findStaticSectionSlot()
    {
        return attributesDiv != null ? (Widgetslot)attributesDiv.query(SELECTOR_STATIC_SECTION_SLOT) : null;
    }


    /** @deprecated since 6.7 use {@link DefaultEditorAreaControllerConfigurationLoader#getEditorAreaConfiguration()} */
    @Deprecated(since = "6.7", forRemoval = true)
    protected EditorArea getEditorAreaConfiguration()
    {
        return getConfigurationLoader().getEditorAreaConfiguration();
    }


    /**
     * @deprecated since 6.7 use
     *             {@link DefaultEditorAreaControllerConfigurationLoader#getEditorAreaConfiguration(String, String)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected EditorArea getEditorAreaConfiguration(final String componentContext)
    {
        return getConfigurationLoader().getEditorAreaConfiguration(getCurrentContext(), componentContext);
    }


    /**
     * @deprecated since 6.7 use
     *             {@link DefaultEditorAreaControllerConfigurationLoader#getEditorAreaConfiguration(String, String)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected EditorArea lookupEditorAreaConfiguration(final DataType dataType, final String componentContext)
    {
        if(dataType != null)
        {
            //there should be call for EditorAreaConfigurationLoader#getEditorAreaConfiguration(DataType, String)
            //but need to stay to not break API
            return loadEditorAreaConfiguration(dataType.getCode(), componentContext);
        }
        else
        {
            return null;
        }
    }


    /**
     * @deprecated since 6.7 not used, see {@link DefaultEditorAreaControllerWidgetTitleDelegate#setWidgetTitleByCtxName()}.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void setWidgetTitleByCtxName()
    {
        getWidgetTitleDelegate().setWidgetTitleByCtxName();
    }


    /**
     * Loads the (editor-area) UI configuration for the provided type with code <p>typeCode</p>.
     *
     * @param typeCode
     *           type code
     * @param component
     *           component code
     * @return the column UI configuration
     * @deprecated since 6.7 use
     *             {@link DefaultEditorAreaControllerConfigurationLoader#getEditorAreaConfiguration(String, String)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected EditorArea loadEditorAreaConfiguration(final String typeCode, final String component)
    {
        return getConfigurationLoader().getEditorAreaConfiguration(typeCode, component);
    }


    /**
     * @deprecated since 6.7 don't need any more see
     *             {@link DefaultEditorAreaControllerConfigurationLoader#getEditorAreaConfiguration(String, String)}
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected EditorArea decorateConfigWithOverviewTab(final EditorArea config)
    {
        return config != null && config.getEssentials() != null ? new EditorAreaConfigWithOverviewTab(config) : config;
    }


    protected void showAllAttributes(final DataType dataType)
    {
        if(dataType != null)
        {
            renderAttributes(dataType);
        }
        else
        {
            renderEmpty();
        }
    }


    @InextensibleMethod
    private void renderAttributes(@NotNull final DataType dataType)
    {
        final String componentContext = getCurrentContext();
        final EditorArea configuration = lookupEditorAreaConfiguration(dataType, componentContext);
        if(configuration == null)
        {
            LOG.error("Could not load {} configuration for type {}", componentContext, dataType.getCode());
        }
        else
        {
            final Div tabboxContainer = new Div();
            tabboxContainer.setParent(attributesDiv);
            tabboxContainer.setSclass(SCLASS_CONTAINER);
            final EditorAreaLogicHandler editorAreaLogicHandler = getLogicHandler();
            if(editorAreaLogicHandler != null)
            {
                synchronized(this)
                {
                    // configuration is needed by unbound section renderer
                    getModelOperationsDelegate().setCurrentConfiguration(configuration);
                    editorAreaLogicHandler.beforeEditorAreaRender(getWidgetInstanceManager(), getCurrentObject());
                    getRenderer().render(tabboxContainer, configuration, getCurrentObject(), dataType, getWidgetInstanceManager());
                }
            }
            else
            {
                final String editorAreaHandlerBean = resolveEditorAreaLogicHandler(configuration);
                final NoSuchBeanDefinitionException exception = new NoSuchBeanDefinitionException(editorAreaHandlerBean,
                                "Unable to load editor area handler bean");
                LOG.error(exception.getLocalizedMessage(), exception);
                getNotificationService().notifyUser(getNotificationSource(), NotificationEventTypes.EVENT_TYPE_GENERAL,
                                NotificationEvent.Level.FAILURE, exception);
            }
        }
    }


    @InextensibleMethod
    private void renderEmpty()
    {
        final Label emptyEditorAreaLabel = new Label(getLabel(LABEL_EMPTY_EDITOR_AREA));
        UITools.modifySClass(emptyEditorAreaLabel, SCLASS_ATTRIBUTES_DIV_EMPTY, true);
        attributesDiv.appendChild(emptyEditorAreaLabel);
    }


    /** @deprecated since 6.7 use {@link DefaultEditorAreaControllerModelOperationsDelegate#getCurrentContext()} instead. */
    @Deprecated(since = "6.7", forRemoval = true)
    protected String getCurrentContext()
    {
        return getModelOperationsDelegate().getCurrentContext();
    }


    /**
     * @deprecated since 6.7 use {@link DefaultEditorAreaControllerModelOperationsDelegate#setCurrentContext(String)}
     *             instead.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected void setCurrentContext(final String configCtx)
    {
        getModelOperationsDelegate().setCurrentContext(configCtx);
    }


    /** @deprecated since 6.7 use {@link DefaultEditorAreaControllerModelOperationsDelegate#getDefaultContext()} instead. */
    @Deprecated(since = "6.7", forRemoval = true)
    protected String getDefaultContext()
    {
        return getModelOperationsDelegate().getDefaultContext();
    }


    protected ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    protected LabelService getLabelService()
    {
        return labelService;
    }


    protected CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    protected NotifyingWidgetComponentRenderer<Component, EditorArea, Object> getRenderer()
    {
        return (NotifyingWidgetComponentRenderer<Component, EditorArea, Object>)getEditorAreaRenderer();
    }


    /**
     * @return the editorAreaRenderer
     * @deprecated since 6.5
     * @see #getRenderer()
     */
    @Deprecated(since = "6.5", forRemoval = true)
    protected WidgetComponentRenderer<Component, EditorArea, Object> getEditorAreaRenderer()
    {
        return editorAreaRenderer;
    }


    /**
     * @deprecated since 6.7 not used, see
     *             {@link DefaultEditorAreaControllerModelOperationsDelegate#getFluidViewModeCode()}.
     */
    @Deprecated(since = "6.7", forRemoval = true)
    protected String getFluidViewModeCode()
    {
        return getModelOperationsDelegate().getFluidViewModeCode();
    }


    /** @deprecated since 6.7 not used, see {@link DefaultEditorAreaControllerModelOperationsDelegate#isFluidViewMode()}. */
    @Deprecated(since = "6.7", forRemoval = true)
    protected boolean isFluidViewMode()
    {
        return getModelOperationsDelegate().isFluidViewMode();
    }


    public Vlayout getBody()
    {
        return body;
    }


    public Button getSaveButton()
    {
        return saveButton;
    }


    public Button getCancelButton()
    {
        return cancelButton;
    }


    public Toolbarbutton getViewModeButton()
    {
        return viewModeButton;
    }


    public Div getAttributesDiv()
    {
        return attributesDiv;
    }


    public Component getToolbarContainer()
    {
        return toolbarContainer;
    }


    public Actions getActionSlot()
    {
        return actionSlot;
    }


    public Label getEditorAreaTitle()
    {
        return editorAreaTitle;
    }


    public Window getValidationResults()
    {
        return getValidationPopupDelegate().getValidationResultsWindow();
    }


    public ValidationRenderer getValidationRenderer()
    {
        return validationRenderer;
    }


    public ReferenceModelProperties getReferenceModelProperties()
    {
        return referenceModelProperties;
    }


    public void setReferenceModelProperties(final ReferenceModelProperties referenceModelProperties)
    {
        this.referenceModelProperties = referenceModelProperties;
    }


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }


    protected EditorAreaLogicHandler getLogicHandler()
    {
        if(logicHandler == null)
        {
            logicHandler = lookupEditorAreaLogicHandler(getConfigurationLoader().getEditorAreaConfiguration());
        }
        return logicHandler;
    }


    protected Set<EditorAreaBeforeLogicHandler> getBeforeLogicHandlers()
    {
        return beforeLogicHandlers;
    }


    public void registerBeforeLogicHandler(final EditorAreaBeforeLogicHandler editorAreaBeforeLogicHandler)
    {
        if(editorAreaBeforeLogicHandler != null && !getBeforeLogicHandlers().contains(editorAreaBeforeLogicHandler))
        {
            beforeLogicHandlers.add(editorAreaBeforeLogicHandler);
        }
    }


    protected void clearBeforeLogicHandlers()
    {
        beforeLogicHandlers.clear();
    }


    protected DefaultEditorAreaControllerSettingsLoader getSettingsLoader()
    {
        if(settingsLoader == null)
        {
            settingsLoader = BackofficeSpringUtil.getBean("editorAreaSettingsLoader", this);
        }
        return settingsLoader;
    }


    protected void setSettingsLoader(final DefaultEditorAreaControllerSettingsLoader settingsLoader)
    {
        this.settingsLoader = settingsLoader;
    }


    protected DefaultEditorAreaControllerConfigurationLoader getConfigurationLoader()
    {
        if(configurationLoader == null)
        {
            configurationLoader = BackofficeSpringUtil.getBean("editorAreaConfigurationLoader", this);
        }
        return configurationLoader;
    }


    protected void setConfigurationLoader(final DefaultEditorAreaControllerConfigurationLoader configurationLoader)
    {
        this.configurationLoader = configurationLoader;
    }


    protected DefaultEditorAreaControllerFluidModeSwitcherDelegate getFluidModeSwitcherDelegate()
    {
        if(fluidModeSwitcherDelegate == null)
        {
            fluidModeSwitcherDelegate = BackofficeSpringUtil.getBean("editorAreaFluidModeSwitcherDelegate", this);
        }
        return fluidModeSwitcherDelegate;
    }


    protected void setFluidModeSwitcherDelegate(
                    final DefaultEditorAreaControllerFluidModeSwitcherDelegate fluidModeSwitcherDelegate)
    {
        this.fluidModeSwitcherDelegate = fluidModeSwitcherDelegate;
    }


    protected DefaultEditorAreaControllerModelOperationsDelegate getModelOperationsDelegate()
    {
        if(modelOperationsDelegate == null)
        {
            modelOperationsDelegate = BackofficeSpringUtil.getBean("editorAreaModelOperationsDelegate", this);
        }
        return modelOperationsDelegate;
    }


    protected void setModelOperationsDelegate(final DefaultEditorAreaControllerModelOperationsDelegate modelOperationsDelegate)
    {
        this.modelOperationsDelegate = modelOperationsDelegate;
    }


    protected DefaultEditorAreaControllerPersistenceListenersDelegate getPersistenceListeners()
    {
        if(persistenceListeners == null)
        {
            persistenceListeners = BackofficeSpringUtil.getBean("editorAreaPersistenceListeners", this);
        }
        return persistenceListeners;
    }


    protected void setPersistenceListeners(final DefaultEditorAreaControllerPersistenceListenersDelegate persistenceListeners)
    {
        this.persistenceListeners = persistenceListeners;
    }


    protected DefaultEditorAreaControllerComponentsSizeDelegate getComponentsSizeDelegate()
    {
        if(componentsSizeDelegate == null)
        {
            componentsSizeDelegate = BackofficeSpringUtil.getBean("editorAreaComponentsSizeDelegate", this);
        }
        return componentsSizeDelegate;
    }


    protected void setComponentsSizeDelegate(final DefaultEditorAreaControllerComponentsSizeDelegate componentsSizeDelegate)
    {
        this.componentsSizeDelegate = componentsSizeDelegate;
    }


    protected DefaultEditorAreaControllerValidationPopupDelegate getValidationPopupDelegate()
    {
        if(validationPopupDelegate == null)
        {
            validationPopupDelegate = BackofficeSpringUtil.getBean("editorAreaValidationPopupDelegate", this);
        }
        return validationPopupDelegate;
    }


    protected void setValidationPopupDelegate(final DefaultEditorAreaControllerValidationPopupDelegate validationPopupDelegate)
    {
        this.validationPopupDelegate = validationPopupDelegate;
    }


    protected DefaultEditorAreaControllerWidgetTitleDelegate getWidgetTitleDelegate()
    {
        if(widgetTitleDelegate == null)
        {
            widgetTitleDelegate = BackofficeSpringUtil.getBean("editorAreaWidgetTitleDelegate", this);
        }
        return widgetTitleDelegate;
    }


    protected void setWidgetTitleDelegate(final DefaultEditorAreaControllerWidgetTitleDelegate widgetTitleDelegate)
    {
        this.widgetTitleDelegate = widgetTitleDelegate;
    }


    public DefaultEditorAreaControllerConcurrentModificationDelegate getConcurrentModificationDelegate()
    {
        if(concurrentModificationDelegate == null)
        {
            concurrentModificationDelegate = BackofficeSpringUtil.getBean("editorAreaConcurrentModificationDelegate", this);
        }
        return concurrentModificationDelegate;
    }


    public void setConcurrentModificationDelegate(
                    final DefaultEditorAreaControllerConcurrentModificationDelegate concurrentModificationDelegate)
    {
        this.concurrentModificationDelegate = concurrentModificationDelegate;
    }
}
