/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.widgets.compare;

import com.google.common.collect.Sets;
import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.annotations.SocketEvent;
import com.hybris.cockpitng.annotations.ViewEvent;
import com.hybris.cockpitng.collections.impl.IdentityArrayList;
import com.hybris.cockpitng.common.messagebox.WidgetModalMessageBox;
import com.hybris.cockpitng.compare.ItemComparisonFacade;
import com.hybris.cockpitng.compare.model.CompareAttributeDescriptor;
import com.hybris.cockpitng.compare.model.CompareLocalizedAttributeDescriptor;
import com.hybris.cockpitng.compare.model.ComparisonResult;
import com.hybris.cockpitng.compare.model.GroupDescriptor;
import com.hybris.cockpitng.config.compareview.jaxb.CompareView;
import com.hybris.cockpitng.config.compareview.jaxb.Section;
import com.hybris.cockpitng.core.async.Operation;
import com.hybris.cockpitng.core.async.Progress;
import com.hybris.cockpitng.core.async.impl.AbstractOperation;
import com.hybris.cockpitng.core.config.CockpitConfigurationException;
import com.hybris.cockpitng.core.config.CockpitConfigurationService;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEventQueue;
import com.hybris.cockpitng.core.events.impl.DefaultCockpitEvent;
import com.hybris.cockpitng.core.model.StandardModelKeys;
import com.hybris.cockpitng.core.user.CockpitUserService;
import com.hybris.cockpitng.data.TypeAwareSelectionContext;
import com.hybris.cockpitng.dataaccess.context.Context;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectCRUDHandler;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectNotFoundException;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectSavingException;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.i18n.CockpitLocaleService;
import com.hybris.cockpitng.labels.LabelService;
import com.hybris.cockpitng.testing.annotation.InextensibleMethod;
import com.hybris.cockpitng.util.DefaultWidgetController;
import com.hybris.cockpitng.util.UITools;
import com.hybris.cockpitng.util.impl.IdentifiableMarkDataConsumer;
import com.hybris.cockpitng.util.impl.IdentifiableMarkEventConsumer;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.cockpitng.util.notifications.event.NotificationEvent;
import com.hybris.cockpitng.util.notifications.event.NotificationEventTypes;
import com.hybris.cockpitng.validation.ValidationHandler;
import com.hybris.cockpitng.widgets.compare.model.CompareViewData;
import com.hybris.cockpitng.widgets.compare.model.ComparisonState;
import com.hybris.cockpitng.widgets.compare.model.DefaultComparisonState;
import com.hybris.cockpitng.widgets.compare.model.PartialComparisonOperationResult;
import com.hybris.cockpitng.widgets.compare.renderer.CompareViewLayout;
import com.hybris.cockpitng.widgets.compare.renderer.CompareViewValidationPopupHandler;
import com.hybris.cockpitng.widgets.compare.renderer.ElementConfigurationProvider;
import com.hybris.cockpitng.widgets.summaryview.CustomRendererClassUtil;
import com.hybris.cockpitng.widgets.util.WidgetRenderingUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.select.annotation.WireVariable;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Messagebox;

public class CompareViewController extends DefaultWidgetController
{
    public static final String MARK_NAME_DIFF_ONLY = "diff-only";
    public static final String MARK_NAME_PIN = "pin";
    public static final String MARK_NAME_HYPERLINK = "hyperlink";
    public static final String MARK_NAME_ADD_ITEMS = "add-items";
    public static final String MARK_NAME_REMOVE_ITEM = "remove-item";
    public static final String MODEL_COMPARISON_REQUEST_ID = "comparison-request-id";
    public static final String MODEL_COMPARE_VIEW_BEFORE_SAVE_LISTENERS_MAP = "_compareViewBeforeSaveListenersMap";
    public static final String MODEL_VALUE_CHANGED_MAP = "valueChangedMap";
    public static final String MODEL_COMPARISON_OBJECT_FEATURELIST_MAP = "comparison-object-featureList-map";
    protected static final String SOCKET_INPUT_REFERENCE = "referenceObject";
    protected static final String SOCKET_INPUT_OBJECTS = "setObjects";
    protected static final String SOCKET_INPUT_OBJECTS_REMOVE = "removeObjects";
    protected static final String SOCKET_INPUT_OBJECTS_ADD = "addObjects";
    protected static final String SOCKET_OUTPUT_SELECTED_ITEM = "selectedItem";
    protected static final String SOCKET_OUTPUT_ADD_ITEMS_REQUEST = "addItemsRequest";
    protected static final String SOCKET_OUTPUT_OBJECTS_LIST_UPDATED = "objectsListUpdated";
    protected static final String COMP_ID_SAVE_BTN = "saveButton";
    protected static final String COMP_ID_CANCEL_BTN = "cancelButton";
    protected static final String SETTING_LAYOUT = "layout";
    /**
     * @deprecated since 2105, use {@link ElementConfigurationProvider#getConfiguration} instead
     */
    @Deprecated(since = "2105", forRemoval = true)
    protected static final String SETTING_CONFIGURATION_CONTEXT = "configCtx";
    protected static final String SETTING_DEFAULT_TYPE_CODE = "defaultTypeCode";
    protected static final String LABEL_LOADING = "info.loading";
    private static final String COMPARE_VIEW_MODIFICATION_MSG_BOX = "compareViewModificationMsgBox";
    private static final String COMPARE_VIEW_CANCEL_MSG_BOX = "compareViewCancelMsgBox";
    private static final String COMPARE_VIEW_CHANGE_MSG_BOX = "compareViewChangeMsgBox";
    private static final String COMPARE_VIEW_TITLE = "compareView.title";
    private static final String COMPARE_VIEW_BTH_YES = "compareView.bth.yes";
    private static final String COMPARE_VIEW_BTH_NO = "compareView.bth.no";
    private static final String COMPARE_VIEW_BTH_SAVE = "compareView.bth.save";
    private static final String COMPARE_VIEW_BTH_CANCEL = "compareView.bth.cancel";
    private static final String COMPARE_VIEW_MODIFICATION_BTH_CONTINUE = "compareView.modification.btn.continue";
    private static final String COMPARE_VIEW_MODIFICATION_BTH_UPDATE = "compareView.modification.btn.update";
    private static final String COMPARE_VIEW_MODIFICATION_MSG_HEADER = "compareView.modification.msg.header";
    private static final String COMPARE_VIEW_MODIFICATION_MSG_TEXT = "compareView.modification.msg.text";
    private static final String COMPARE_VIEW_MODIFICATION_MSG_FOOTER = "compareView.modification.msg.footer";
    private static final String COMPARE_VIEW_CANCEL_MSG_TEXT = "compareView.cancel.msg.text";
    private static final String COMPARE_VIEW_CHANGE_MSG_TEXT = "compareView.change.msg.text";
    private static final String LISTENER_PIN = "pinListener";
    private static final String LISTENER_HYPERLINK = "hyperlinkListener";
    private static final String LISTENER_ADD_ITEMS = "addItemsListener";
    private static final String LISTENER_REMOVE_ITEM = "removeItemListener";
    private static final String LISTENER_DIFF_ONLY = "diffOnlyListener";
    private static final String MODEL_SELECTED_OBJECT = StandardModelKeys.CONTEXT_OBJECT;
    private static final String MODEL_SELECTED_OBJECT_ID = MODEL_SELECTED_OBJECT + "Id";
    private static final String MODEL_OBJECTS = "objects";
    private static final String MODEL_OBJECTS_IDS = MODEL_OBJECTS + "IDs";
    private static final String MODEL_ITEM_TYPE = "itemType";
    private static final String MODEL_VIEW_DATA = "viewData";
    private static final String MODEL_DIFF_ONLY = "diffOnly";
    private static final String MODEL_COMPARISON_STATUS = "comparisonStatus";
    private static final String SCLASS_CALCULATION_IN_PROGRESS = "yw-compareview-calculating";
    private static final String SCLASS_DIFF_ONLY = "yw-compareview-diff-only";
    private static final String NOTIFICATION_EVENT_TYPE_COMPARED_OBJECT_REMOVED = "comparedObjectRemoved";
    private static final String NOTIFICATION_EVENT_TYPE_ITEMS_DO_NOT_MATCH_DEFAULT_TYPE_CODE = "itemsDoNotMatchDefaultTypeCode";
    private static final Logger LOGGER = LoggerFactory.getLogger(CompareViewController.class);
    @Wire
    private Div view;
    @Wire
    private Button saveButton;
    @Wire
    private Button cancelButton;
    @WireVariable("compareViewLayout")
    private transient CompareViewLayout defaultLayout;
    @WireVariable
    private transient TypeFacade typeFacade;
    @WireVariable
    private transient ItemComparisonFacade itemComparisonFacade;
    @WireVariable
    private transient CockpitLocaleService cockpitLocaleService;
    @WireVariable
    private transient CockpitUserService cockpitUserService;
    @WireVariable
    private transient WidgetRenderingUtils widgetRenderingUtils;
    @WireVariable
    private transient NotificationService notificationService;
    @WireVariable
    private transient ValidationHandler validationHandler;
    @WireVariable
    private transient LabelService labelService;
    @WireVariable
    private transient ObjectFacade objectFacade;
    @WireVariable
    private transient ElementConfigurationProvider elementConfigurationProvider;
    @WireVariable
    private transient CockpitEventQueue cockpitEventQueue;
    private boolean saveAndCancelButtonActive = false;
    private CompareViewValidationPopupHandler compareViewValidationPopupHandler;


    @Override
    public void initialize(final Component comp)
    {
        super.initialize(comp);
        setComparisonRequestId();
        getLayout().onUpdateItemType();
        updateView(getCurrentViewData());
        saveAndCancelButtonActive = false;
        saveButton.setDisabled(!saveAndCancelButtonActive);
        cancelButton.setDisabled(!saveAndCancelButtonActive);
        setValue(MODEL_VALUE_CHANGED_MAP, new HashMap<>());
        setValue(MODEL_COMPARISON_OBJECT_FEATURELIST_MAP, new HashMap<>());
        getModel().addObserver(MODEL_VALUE_CHANGED_MAP, this::updateSaveAndCancelButtonsState);
        setCompareViewValidationPopupHandler(new CompareViewValidationPopupHandler(getValidationHandler(), getLabelService(),
                        getTypeFacade(), getCockpitLocaleService()));
        getCompareViewValidationPopupHandler().prepareValidationPopup(getSaveButton().getParent());
    }


    @InextensibleMethod
    private void updateSaveAndCancelButtonsState()
    {
        final Map<Object, Boolean> modelValueChangedMap = getValue(MODEL_VALUE_CHANGED_MAP, Map.class);
        final boolean enable = modelValueChangedMap.containsValue(true);
        if(enable != saveAndCancelButtonActive)
        {
            saveAndCancelButtonActive = enable;
            if(getSaveButton() != null)
            {
                getSaveButton().setDisabled(!saveAndCancelButtonActive);
            }
            if(getCancelButton() != null)
            {
                getCancelButton().setDisabled(!saveAndCancelButtonActive);
            }
        }
        if(saveAndCancelButtonActive)
        {
            revalidateView();
        }
    }


    /**
     * Sets unique comparison request ID in the model
     */
    protected void setComparisonRequestId()
    {
        setValue(MODEL_COMPARISON_REQUEST_ID, UUID.randomUUID());
    }


    /**
     * Clears whole view built up till now, invalidates current comparison result and triggers view build from scratch.
     */
    protected void resetView()
    {
        getView().getChildren().clear();
        revalidateView();
    }


    /**
     * Forces view to validate - new comparison is being performed and whole view is being validated from scratch.
     */
    protected void revalidateView()
    {
        invalidateView();
        updateView(getCurrentViewData());
    }


    /**
     * Marks current view as invalid - future calls to {@link #validateView()} will trigger full comparison from scratch
     */
    protected void invalidateView()
    {
        clearViewData();
        clearComparisonStatus();
    }


    /**
     * Checks if current view reflects data marked for comparison and comparison result. If any comparison is required, then
     * it is triggered in background (non-blocking operation).
     */
    protected void validateView()
    {
        final DefaultComparisonState comparisonState = getComparisonState();
        comparisonState.setStatus(ComparisonState.Status.RUNNING);
        addInProgressMarker();
        getWidgetInstanceManager().executeOperationInParallel(createBuildDataOperation(),
                        event -> handlePartialComparisonResult(comparisonState, (PartialComparisonOperationResult)event.getData()));
    }


    /**
     * Marks UI component as in progress
     */
    protected void addInProgressMarker()
    {
        UITools.addSClass(getView(), SCLASS_CALCULATION_IN_PROGRESS);
    }


    /**
     * @deprecated since 1811. Please use {@link #createBuildDataOperation()}
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected Operation createBuildDataOperation(final DefaultComparisonState status)
    {
        return createBuildDataOperation();
    }


    /**
     * Creates an operation responsible for performing a partial comparison in background. As a result of this operation a
     * pair &lt;Object, {@link ComparisonResult}&gt; is expected with compared object as key and result of comparison as
     * value.
     *
     * @return operation to be performed in background
     */
    protected Operation createBuildDataOperation()
    {
        return new PartialComparisonOperation();
    }


    protected void handlePartialComparisonResult(final DefaultComparisonState state,
                    final PartialComparisonOperationResult partialComparisonResult)
    {
        if(partialComparisonResult != null && !partialComparisonResult.isReferenceObjectValid())
        {
            handleNotValidObject(partialComparisonResult.getReferenceObjectId());
        }
        if(partialComparisonResult != null && !partialComparisonResult.isComparedObjectValid())
        {
            handleNotValidObject(partialComparisonResult.getComparedObjectId());
        }
        final Object comparedObjectId = partialComparisonResult != null ? partialComparisonResult.getComparedObjectId() : null;
        final ComparisonResult comparisonResult = partialComparisonResult != null ? partialComparisonResult.getComparisonResult()
                        : null;
        final Pair<Object, ComparisonResult> compareObjectIdAndResult = partialComparisonResult == null ? null
                        : Pair.of(comparedObjectId, comparisonResult);
        handlePartialComparisonResult(state, compareObjectIdAndResult);
    }


    protected void handleNotValidObject(final Object objectId)
    {
        if(objectId == null)
        {
            return;
        }
        final Optional<Object> objectToRemove = getObjects().stream()
                        .filter(object -> objectId.equals(getObjectFacade().getObjectId(object))).findFirst();
        if(objectToRemove.isPresent())
        {
            removeObjectsToCompare(Collections.singleton(objectToRemove.get()));
            getNotificationService().notifyUser(getWidgetInstanceManager(), NOTIFICATION_EVENT_TYPE_COMPARED_OBJECT_REMOVED,
                            NotificationEvent.Level.WARNING, objectToRemove.get());
        }
    }


    protected void handlePartialComparisonResult(final DefaultComparisonState state,
                    final Pair<Object, ComparisonResult> compareObjectIdAndResult)
    {
        Object comparedObjectId = null;
        if(compareObjectIdAndResult != null)
        {
            final Optional<ComparisonResult> nullableResult = Optional.ofNullable(compareObjectIdAndResult.getValue());
            nullableResult.ifPresent(result -> getCurrentViewData().merge(state, result, compareObjectIdAndResult.getKey()));
            comparedObjectId = compareObjectIdAndResult.getKey();
        }
        updateComparisonState(state, comparedObjectId);
        if(isValidComparison(state))
        {
            updateView(getCurrentViewData());
        }
    }


    /**
     * Checks if current view reflects provided comparison data. If any comparison is still required, then it is triggered
     * in background (non-blocking operation).
     *
     * @param data
     *           comparison data to validate view against
     * @see #assureComparisonNotFinished(CompareViewData)
     */
    protected void updateView(final CompareViewData data)
    {
        if(data != null && isValidViewData(data))
        {
            try
            {
                updateViewInternal(data);
            }
            catch(final TypeNotFoundException e)
            {
                LOGGER.error(e.getLocalizedMessage(), e);
                final String source = getNotificationService().getWidgetNotificationSource(getWidgetInstanceManager());
                getNotificationService().notifyUser(source, NotificationEventTypes.EVENT_TYPE_GENERAL,
                                NotificationEvent.Level.FAILURE, e);
            }
            catch(final CockpitConfigurationException e)
            {
                LOGGER.error(e.getLocalizedMessage(), e);
            }
        }
    }


    protected void updateViewInternal(final CompareViewData data) throws TypeNotFoundException, CockpitConfigurationException
    {
        removeObjectsNotMatchingDefaultTypeFromComparison();
        final String typeCode = getItemType();
        final DataType dataType = getTypeFacade().load(typeCode);
        final CompareView configuration;
        if(getReferenceObject() != null)
        {
            configuration = loadConfiguration();
        }
        else
        {
            configuration = new CompareView();
        }
        getLayout().render(view, configuration, data, dataType, getWidgetInstanceManager());
        registerListeners();
        updateDiffOnlyTrigger();
        assureComparisonNotFinished(data);
        if(ComparisonState.Status.FINISHED.equals(data.getComparisonState().getStatus()))
        {
            UITools.postponeExecution(view, this::removeInProgressMarker);
        }
    }


    protected void removeObjectsNotMatchingDefaultTypeFromComparison()
    {
        final Collection<Object> objectsInComparisonWithAllowedTypeCode = new HashSet<>(getObjects());
        removeObjectsNotMatchingDefaultTypeFromComparison(objectsInComparisonWithAllowedTypeCode);
        final Collection<Object> objectsToRemoveFromComparison = CollectionUtils.subtract(getObjects(),
                        objectsInComparisonWithAllowedTypeCode);
        if(!objectsToRemoveFromComparison.isEmpty())
        {
            removeObjectsImmediately(objectsToRemoveFromComparison);
        }
    }


    protected void registerListeners()
    {
        registerHyperlinkEventListeners();
        registerPinEventListeners();
        registerAddItemsListeners();
        registerRemoveItemListeners();
        registerDiffOnlyListeners();
    }


    protected void registerDiffOnlyListeners()
    {
        getWidgetRenderingUtils().registerMarkedComponentsListener(getView(), MARK_NAME_DIFF_ONLY, Events.ON_CHECK,
                        new IdentifiableMarkEventConsumer(LISTENER_DIFF_ONLY, event -> handleDiffOnlyChecked(getView(), (CheckEvent)event)));
    }


    protected void handleDiffOnlyChecked(final HtmlBasedComponent component, final CheckEvent checkEvent)
    {
        final boolean isDiffOnlyChecked = checkEvent.isChecked();
        updateDiffOnlyState(getCurrentViewData(), isDiffOnlyChecked);
        updateDiffOnlyTrigger();
    }


    protected void updateDiffOnlyTrigger()
    {
        UITools.modifySClass(getView(), SCLASS_DIFF_ONLY, getCurrentViewData().isDiffOnly());
    }


    /**
     * @deprecated since 1811. Please use {@link #assureComparisonNotFinished(CompareViewData)}
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected void assureComparisonFinished(final CompareViewData<?> data)
    {
        assureComparisonNotFinished(data);
    }


    /**
     * Checks if any other comparisons are required to meet all data marked for comparison. If so view validation is
     * triggered.
     *
     * @param data
     *           comparison data to validate view against
     * @see #validateView()
     */
    protected void assureComparisonNotFinished(final CompareViewData<?> data)
    {
        if(isValidViewData(data) && !ComparisonState.Status.FINISHED.equals(data.getComparisonState().getStatus()))
        {
            validateView();
        }
    }


    protected void registerPinEventListeners()
    {
        getWidgetRenderingUtils().registerMarkedComponentsListener(getView(), MARK_NAME_PIN, Events.ON_CLICK,
                        new IdentifiableMarkDataConsumer(LISTENER_PIN, this::handlePinClicked));
    }


    protected void handlePinClicked(final Object object)
    {
        if(!getItemComparisonFacade().isEqualItem(object, getReferenceObject()))
        {
            if(saveAndCancelButtonActive)
            {
                getMessageBoxBuilder(COMPARE_VIEW_CHANGE_MSG_BOX)//
                                .withCancel(getLabel(COMPARE_VIEW_BTH_CANCEL))//
                                .withConfirm(getLabel(COMPARE_VIEW_BTH_SAVE), () -> {
                                    saveObjects();
                                    changeReference(object);
                                })//
                                .withTitle(getLabel(COMPARE_VIEW_TITLE))//
                                .withMessage(getLabel(COMPARE_VIEW_CHANGE_MSG_TEXT))//
                                .build()//
                                .show(this.getWidgetslot());
            }
            else
            {
                changeReference(object);
            }
        }
    }


    @SocketEvent(socketId = SOCKET_INPUT_REFERENCE)
    public void changeReference(final Object object)
    {
        setReferenceObjectImmediately(object);
        revalidateView();
    }


    @ViewEvent(componentID = COMP_ID_SAVE_BTN, eventName = Events.ON_CLICK)
    public void saveObjects()
    {
        final List<Object> currentObjects = new ArrayList<>(getObjects());
        final Map<Object, Boolean> modelValueChangedMap = getValue(MODEL_VALUE_CHANGED_MAP, Map.class);
        final List<Object> needToSaveObjects = currentObjects.stream()
                        .filter(item -> modelValueChangedMap.containsKey(item) && Boolean.TRUE.equals(modelValueChangedMap.get(item)))
                        .collect(Collectors.toList());
        if(compareViewValidationPopupHandler.doValidate(needToSaveObjects, this::performSaveObjects))
        {
            performSaveObjects(needToSaveObjects);
        }
    }


    @InextensibleMethod
    private void performSaveObjects(final List<Object> needToSaveObjects)
    {
        final List<Object> updateObjects = new ArrayList<>();
        final Map<Object, Boolean> modelValueChangedMap = getValue(MODEL_VALUE_CHANGED_MAP, Map.class);
        notifyBeforeSaveListeners();
        for(Object item : needToSaveObjects)
        {
            try
            {
                final Context ctx = new DefaultContext();
                ctx.addAttribute(ObjectFacade.CTX_PARAM_SUPPRESS_EVENT, Boolean.TRUE);
                final Object savedObject = getObjectFacade().save(item, ctx);
                final Object reloadedForNotification = getObjectFacade().reload(savedObject);
                handleObjectSavingSuccess(reloadedForNotification);
                updateObjects.add(reloadedForNotification);
                modelValueChangedMap.remove(reloadedForNotification);
            }
            catch(final ObjectSavingException ex)
            {
                handleObjectSavingException(ex, Collections.singleton(item));
            }
            catch(final ObjectNotFoundException ex)
            {
                handleObjectNotFoundException(ex, item);
            }
        }
        refreshObjects(updateObjects);
        setValue(MODEL_VALUE_CHANGED_MAP, modelValueChangedMap);
    }


    protected void handleObjectSavingSuccess(final Object savedObject)
    {
        getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(getWidgetInstanceManager()),
                        NotificationEventTypes.EVENT_TYPE_OBJECT_UPDATE, NotificationEvent.Level.SUCCESS,
                        Collections.singletonList(savedObject));
    }


    protected void handleObjectSavingException(final ObjectSavingException savingException, final Collection<Object> objectInput)
    {
        getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(getWidgetInstanceManager()),
                        NotificationEventTypes.EVENT_TYPE_OBJECT_UPDATE, NotificationEvent.Level.FAILURE,
                        Collections.singletonMap(objectInput, savingException));
        final List<Object> updateObjects = new ArrayList<>();
        objectInput.forEach(item -> {
            try
            {
                final Object reloadedForNotification = getObjectFacade().load((String)getObjectFacade().getObjectId(item));
                updateObjects.add(reloadedForNotification);
            }
            catch(ObjectNotFoundException e)
            {
                handleObjectNotFoundException(e, item);
            }
        });
        refreshObjects(updateObjects);
    }


    protected void handleObjectNotFoundException(final ObjectNotFoundException exception, final Object objectInput)
    {
        getNotificationService().notifyUser(getNotificationService().getWidgetNotificationSource(getWidgetInstanceManager()),
                        NotificationEventTypes.EVENT_TYPE_OBJECT_LOAD, NotificationEvent.Level.FAILURE,
                        Collections.singletonMap(objectInput, exception));
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
        return getWidgetslot().getWidgetInstance().getId() + view.getUuid();
    }


    protected void populateEventContext(final Context source, final DefaultCockpitEvent destination)
    {
        if(source != null)
        {
            source.getAttributeNames().forEach(a -> destination.getContext().put(a, source.getAttribute(a)));
        }
    }


    protected void notifyBeforeSaveListeners()
    {
        final Map<String, EventListener<Event>> beforeSaveListeners = getWidgetInstanceManager().getModel()
                        .getValue(MODEL_COMPARE_VIEW_BEFORE_SAVE_LISTENERS_MAP, Map.class);
        if(beforeSaveListeners != null)
        {
            for(final EventListener<Event> listener : beforeSaveListeners.values())
            {
                try
                {
                    listener.onEvent(new Event("beforeSave"));
                }
                catch(final Exception ex)
                {
                    handleObjectSavingException(new ObjectSavingException(ex.getMessage(), ex), getObjects());
                }
            }
        }
    }


    @ViewEvent(componentID = COMP_ID_CANCEL_BTN, eventName = Events.ON_CLICK)
    public void cancelObjectModification()
    {
        getMessageBoxBuilder(COMPARE_VIEW_CANCEL_MSG_BOX)//
                        .withCancel(getLabel(COMPARE_VIEW_BTH_NO))//
                        .withConfirm(getLabel(COMPARE_VIEW_BTH_YES), this::handleCancelObjectModification)//
                        .withTitle(getLabel(COMPARE_VIEW_TITLE))//
                        .withMessage(getLabel(COMPARE_VIEW_CANCEL_MSG_TEXT))//
                        .build()//
                        .show(this.getWidgetslot());
    }


    @InextensibleMethod
    private void handleCancelObjectModification()
    {
        final List<Object> currentObjects = new ArrayList<>(getObjects());
        final List<Object> updateObjects = new ArrayList<>();
        for(Object item : currentObjects)
        {
            try
            {
                final Object updatedItem = getObjectFacade().load((String)getObjectFacade().getObjectId(item));
                updateObjects.add(updatedItem);
            }
            catch(ObjectNotFoundException ex)
            {
                handleObjectNotFoundException(ex, item);
            }
        }
        refreshObjects(updateObjects);
    }


    public void refreshObjects(final Collection<Object> objects)
    {
        final Collection<Object> currentObjects = new ArrayList<>(getObjects());
        final Map<Object, Boolean> modelValueChangedMap = getValue(MODEL_VALUE_CHANGED_MAP, Map.class);
        final Map<Object, Object> compareObjectsFeatureListMap = getValue(MODEL_COMPARISON_OBJECT_FEATURELIST_MAP, Map.class);
        if(CollectionUtils.containsAny(currentObjects, objects))
        {
            final List<Object> changedObjects = new ArrayList<>(objects);
            updateObjectsImmediately(changedObjects);
            if(changedObjects.contains(getReferenceObject()))
            {
                final int index = changedObjects.indexOf(getReferenceObject());
                changeReference(changedObjects.get(index));
            }
            else
            {
                validateView();
            }
            objects.forEach(item -> {
                modelValueChangedMap.remove(item);
                compareObjectsFeatureListMap.remove(item);
            });
            setValue(MODEL_VALUE_CHANGED_MAP, modelValueChangedMap);
            setValue(MODEL_COMPARISON_OBJECT_FEATURELIST_MAP, compareObjectsFeatureListMap);
        }
    }


    protected void registerHyperlinkEventListeners()
    {
        getWidgetRenderingUtils().registerMarkedComponentsListener(getView(), MARK_NAME_HYPERLINK, Events.ON_CLICK,
                        new IdentifiableMarkDataConsumer(LISTENER_HYPERLINK, this::handleHyperLinkClicked));
    }


    protected void handleHyperLinkClicked(final Object item)
    {
        final TypeAwareSelectionContext context = createSelectionContext(item);
        sendOutput(SOCKET_OUTPUT_SELECTED_ITEM, context);
    }


    protected void registerAddItemsListeners()
    {
        getWidgetRenderingUtils().registerMarkedComponentsListener(getView(), MARK_NAME_ADD_ITEMS, Events.ON_CLICK,
                        new IdentifiableMarkDataConsumer(LISTENER_ADD_ITEMS, data -> handleAddItemsClicked()));
    }


    protected void handleAddItemsClicked()
    {
        final TypeAwareSelectionContext context = createAddSelectionContext(getReferenceObject());
        context.setMultiSelect(true);
        sendOutput(SOCKET_OUTPUT_ADD_ITEMS_REQUEST, context);
    }


    protected void registerRemoveItemListeners()
    {
        getWidgetRenderingUtils().registerMarkedComponentsListener(getView(), MARK_NAME_REMOVE_ITEM, Events.ON_CLICK,
                        new IdentifiableMarkDataConsumer(LISTENER_REMOVE_ITEM, this::handleRemoveItemClicked));
    }


    protected void handleRemoveItemClicked(final Object item)
    {
        if(item != getReferenceObject())
        {
            removeObjectsToCompare(Collections.singletonList(item));
        }
        else
        {
            Messagebox.show(getLabel("remove.referenced.object.confirmation"), getLabel("remove.referenced.object.title"),
                            new Messagebox.Button[]
                                            {Messagebox.Button.YES, Messagebox.Button.NO}, Messagebox.QUESTION, event -> {
                                if(Messagebox.Button.YES.equals(event.getButton()))
                                {
                                    removeObjectsToCompare(Collections.singletonList(item));
                                }
                            });
        }
    }


    protected TypeAwareSelectionContext<Object> createSelectionContext(final Object selectedItem)
    {
        final String typeCode = getItemType();
        return new TypeAwareSelectionContext<>(typeCode, selectedItem, new ArrayList<>(getObjects()));
    }


    protected TypeAwareSelectionContext<Object> createAddSelectionContext(final Object selectedItem)
    {
        final String typeCode = getSelectionItemType();
        return new TypeAwareSelectionContext<>(typeCode, selectedItem, new ArrayList<>(getObjects()));
    }


    protected void assureReferenceInObjectsList()
    {
        final List<Object> objects = getObjects();
        final var referenceObject = getReferenceObject();
        if(referenceObject == null
                        || objects.stream().noneMatch(object -> getItemComparisonFacade().isEqualItem(object, referenceObject)))
        {
            if(CollectionUtils.isNotEmpty(objects))
            {
                setReferenceObjectImmediately(objects.get(0));
            }
            else if(referenceObject != null)
            {
                setReferenceObjectImmediately(null);
            }
        }
    }


    protected void setReferenceObjectImmediately(final Object reference)
    {
        setValue(MODEL_SELECTED_OBJECT, reference);
        setValue(MODEL_SELECTED_OBJECT_ID, getObjectFacade().getObjectId(reference));
        if(reference != null && !getObjects().contains(reference))
        {
            addObjectsImmediately(Collections.singleton(reference));
        }
        assureReferenceInObjectsList();
        updateItemType();
    }


    protected Object getReferenceObject()
    {
        return getValue(MODEL_SELECTED_OBJECT, Object.class);
    }


    protected Object getReferenceObjectId()
    {
        return getValue(MODEL_SELECTED_OBJECT_ID, Object.class);
    }


    protected void updateItemType()
    {
        final var referenceObject = getReferenceObject();
        final String oldType = getItemType();
        String type = null;
        if(referenceObject != null)
        {
            type = getTypeFacade().getType(referenceObject);
        }
        if(!StringUtils.equals(oldType, type))
        {
            setValue(MODEL_ITEM_TYPE, type);
            getLayout().onUpdateItemType();
        }
    }


    protected String getItemType()
    {
        Object itemType = getValue(MODEL_ITEM_TYPE, String.class);
        if(itemType == null)
        {
            itemType = getSelectionItemType();
        }
        return itemType.toString();
    }


    protected String getSelectionItemType()
    {
        Object itemType = getWidgetSettings().get(SETTING_DEFAULT_TYPE_CODE);
        if(!(itemType instanceof String) || StringUtils.isEmpty((String)itemType))
        {
            itemType = getFallbackItemType();
        }
        return itemType.toString();
    }


    protected String getFallbackItemType()
    {
        return Object.class.getName();
    }


    @SocketEvent(socketId = SOCKET_INPUT_OBJECTS_ADD)
    public void addObjectsToCompare(final Collection<Object> objects)
    {
        if(getReferenceObject() == null)
        {
            setObjects(objects);
        }
        else if(objects != null)
        {
            addObjectsImmediately(objects);
            validateView();
        }
    }


    @SocketEvent(socketId = SOCKET_INPUT_OBJECTS_REMOVE)
    public void removeObjectsToCompare(final Collection<Object> objects)
    {
        if(objects != null)
        {
            removeObjectsImmediately(objects);
            revalidateView();
        }
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_UPDATED_EVENT, scope = CockpitEvent.SESSION)
    public void objectsUpdated(final CockpitEvent event)
    {
        final Collection<Object> currentObjects = new ArrayList<>(getObjects());
        if(event == null || event.getDataAsCollection() == null
                        || !CollectionUtils.containsAny(currentObjects, event.getDataAsCollection()))
        {
            return;
        }
        if(Objects.equals(event.getSource(), getCRUDNotificationSource()) || !saveAndCancelButtonActive)
        {
            refreshObjects(event.getDataAsCollection());
        }
        else
        {
            getMessageBoxBuilder(COMPARE_VIEW_MODIFICATION_MSG_BOX)//
                            .withCancel(getLabel(COMPARE_VIEW_MODIFICATION_BTH_CONTINUE))//
                            .withConfirm(getLabel(COMPARE_VIEW_MODIFICATION_BTH_UPDATE), this::handleCancelObjectModification)//
                            .withHeader(getLabel(COMPARE_VIEW_MODIFICATION_MSG_HEADER))//
                            .withMessage(getLabel(COMPARE_VIEW_MODIFICATION_MSG_TEXT))//
                            .withFooter(getLabel(COMPARE_VIEW_MODIFICATION_MSG_FOOTER), WidgetModalMessageBox.SCLASS_WARNING)//
                            .withWarningTitle()//
                            .build()//
                            .show(this.getWidgetslot());
        }
    }


    protected WidgetModalMessageBox.Builder getMessageBoxBuilder(final String id)
    {
        return new WidgetModalMessageBox.Builder().withId(id);
    }


    @GlobalCockpitEvent(eventName = ObjectCRUDHandler.OBJECTS_DELETED_EVENT)
    public void objectsDeleted(final CockpitEvent event)
    {
        if(event != null)
        {
            final Collection<Object> objects = event.getDataAsCollection();
            final Collection<Object> currentObjects = new ArrayList<>(getObjects());
            if(CollectionUtils.containsAny(currentObjects, objects))
            {
                removeObjectsImmediately(objects);
                revalidateView();
            }
        }
    }


    protected Div getView()
    {
        return view;
    }


    protected boolean isValidComparison(final ComparisonState<?> status)
    {
        return getComparisonState() == status;
    }


    protected boolean isValidViewData(final CompareViewData<?> data)
    {
        return getComparisonState() == data.getComparisonState();
    }


    protected CompareViewData getCurrentViewData()
    {
        CompareViewData<?> comparisonData = getValue(MODEL_VIEW_DATA, CompareViewData.class);
        if(comparisonData == null)
        {
            Set<GroupDescriptor> groupDescriptors = null;
            try
            {
                groupDescriptors = loadGroupDescriptors();
            }
            catch(final CockpitConfigurationException ex)
            {
                LOGGER.error(ex.getLocalizedMessage(), ex);
            }
            final ComparisonResult comparisonResult = createComparisonResult(groupDescriptors);
            comparisonData = new CompareViewData<>(comparisonResult, (ComparisonState<?>)getComparisonStatus(),
                            isDiffOnlyEnabled());
            setValue(MODEL_VIEW_DATA, comparisonData);
        }
        return comparisonData;
    }


    protected ComparisonResult createComparisonResult(final Set<GroupDescriptor> groupDescriptors)
    {
        return new ComparisonResult(getReferenceObjectId(), Collections.emptyMap(),
                        groupDescriptors != null ? groupDescriptors : Collections.emptySet());
    }


    protected boolean isDiffOnlyEnabled()
    {
        return BooleanUtils.toBooleanDefaultIfNull(getValue(MODEL_DIFF_ONLY, Boolean.class), Boolean.FALSE);
    }


    protected void updateDiffOnlyState(final CompareViewData currentData, final boolean diffOnly)
    {
        setValue(MODEL_DIFF_ONLY, diffOnly);
        setValue(MODEL_VIEW_DATA,
                        new CompareViewData<>(currentData.getComparisonResult(), currentData.getComparisonState(), diffOnly));
    }


    protected ComparisonResult performPartialComparison(final Object reference, final Object itemToCompare)
                    throws CockpitConfigurationException
    {
        final Set<GroupDescriptor> groupDescriptors = loadGroupDescriptors();
        return getItemComparisonFacade()
                        .getCompareViewResult(reference, Collections.singletonList(itemToCompare), groupDescriptors,
                                        () -> getWidgetInstanceManager().getModel().getValue(MODEL_COMPARISON_OBJECT_FEATURELIST_MAP, Map.class))
                        .orElse(new ComparisonResult(reference, Collections.emptyMap(), groupDescriptors));
    }


    protected Set<GroupDescriptor> loadGroupDescriptors() throws CockpitConfigurationException
    {
        return loadConfiguration().getSection().stream().map(this::groupDescriptorFromSection).collect(Collectors.toSet());
    }


    protected GroupDescriptor groupDescriptorFromSection(final Section section)
    {
        final List<CompareAttributeDescriptor> attributes = section.getAttribute().stream()
                        .flatMap(attribute -> compareAttributeDescriptorFromQualifier(attribute.getQualifier(), attribute.isReadonly()))
                        .collect(Collectors.toList());
        return new GroupDescriptor(section.getName(), attributes);
    }


    /**
     * @deprecated since 1811. Please use {@link #getComparisonState()} instead.
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected DefaultComparisonState getComparisonStatus()
    {
        return getComparisonState();
    }


    protected DefaultComparisonState getComparisonState()
    {
        DefaultComparisonState<? super Object> comparisonStatus = getValue(MODEL_COMPARISON_STATUS, DefaultComparisonState.class);
        if(comparisonStatus == null)
        {
            final var referenceObject = getReferenceObject();
            comparisonStatus = new DefaultComparisonState<>(referenceObject, getObjects());
            if(referenceObject != null)
            {
                comparisonStatus.setObjectCompared(getReferenceObjectId(), referenceObject);
            }
            else
            {
                comparisonStatus.setStatus(ComparisonState.Status.FINISHED);
            }
            setValue(MODEL_COMPARISON_STATUS, comparisonStatus);
        }
        return comparisonStatus;
    }


    protected void clearViewData()
    {
        setValue(MODEL_VIEW_DATA, null);
    }


    protected void clearComparisonStatus()
    {
        setValue(MODEL_COMPARISON_STATUS, null);
    }


    /**
     * @deprecated since 1811. Please use {@link #updateComparisonState(DefaultComparisonState, Object)}
     */
    @Deprecated(since = "1811", forRemoval = true)
    protected void updateComparisonStatus(final DefaultComparisonState<? super Object> comparisonStatus,
                    final Object comparedObject)
    {
        updateComparisonState(comparisonStatus, comparedObject);
    }


    protected void updateComparisonState(final DefaultComparisonState<? super Object> comparisonState, final Object objectId)
    {
        if(objectId != null)
        {
            final Optional<Object> object = getObjectById(objectId);
            comparisonState.setObjectCompared(objectId, object.orElse(null));
        }
        if(CollectionUtils.isEqualCollection(comparisonState.getComparedObjectIds(), getObjectIds()))
        {
            comparisonState.setStatus(ComparisonState.Status.FINISHED);
        }
    }


    /**
     * Removes in progress marker from UI component
     */
    protected void removeInProgressMarker()
    {
        UITools.removeSClass(getView(), SCLASS_CALCULATION_IN_PROGRESS);
    }


    /**
     * @deprecated since 2205. Please use {@link #compareAttributeDescriptorFromQualifier(String, Boolean)} instead.
     */
    @Deprecated(since = "2205", forRemoval = true)
    protected Stream<CompareAttributeDescriptor> compareAttributeDescriptorFromQualifier(final String qualifier)
    {
        return compareAttributeDescriptorFromQualifier(qualifier, false);
    }


    protected Stream<CompareAttributeDescriptor> compareAttributeDescriptorFromQualifier(final String qualifier,
                    final Boolean readonly)
    {
        final var referenceObject = getReferenceObject();
        final CompareAttributeDescriptor compareAttributeDescriptor = new CompareAttributeDescriptor(qualifier);
        compareAttributeDescriptor.setReadonly(readonly);
        final Stream<CompareAttributeDescriptor> nonLocalizedDescriptor = Stream.of(compareAttributeDescriptor);
        if(referenceObject != null)
        {
            final String referenceObjectTypeCode = getItemType();
            final DataAttribute referenceObjectDataAttribute = getTypeFacade().getAttribute(referenceObjectTypeCode, qualifier);
            final boolean isLocalized = referenceObjectDataAttribute != null && referenceObjectDataAttribute.isLocalized();
            if(isLocalized)
            {
                return Stream.concat(nonLocalizedDescriptor, getCockpitLocaleService()
                                .getEnabledDataLocales(getCockpitUserService().getCurrentUser()).stream().map(locale -> {
                                    final CompareLocalizedAttributeDescriptor compareLocalizedAttributeDescriptor = new CompareLocalizedAttributeDescriptor(
                                                    qualifier, null, locale);
                                    compareLocalizedAttributeDescriptor.setReadonly(readonly);
                                    return compareLocalizedAttributeDescriptor;
                                }));
            }
        }
        return nonLocalizedDescriptor;
    }


    protected CompareView loadConfiguration() throws CockpitConfigurationException
    {
        return getElementConfigurationProvider().getConfiguration(getWidgetInstanceManager(), getItemType());
    }


    protected List<Object> getObjects()
    {
        return Collections.unmodifiableList(getObjectsMutable());
    }


    @SocketEvent(socketId = SOCKET_INPUT_OBJECTS)
    public void setObjects(final Collection<Object> objects)
    {
        removeObjectsNotMatchingDefaultTypeFromComparison(objects);
        setComparisonRequestId();
        setObjectsImmediately(objects);
        resetView();
    }


    protected void removeObjectsNotMatchingDefaultTypeFromComparison(final Collection<Object> objects)
    {
        if(CollectionUtils.isNotEmpty(objects))
        {
            final int initialItemsToCompareSize = objects.size();
            final String defaultTypeCode = getSelectionItemType();
            final Predicate<Object> notMatchingDefaultType = object -> !getTypeWithSuperTypes(object).contains(defaultTypeCode);
            objects.removeIf(notMatchingDefaultType);
            if(initialItemsToCompareSize != objects.size())
            {
                getNotificationService().notifyUser(getWidgetInstanceManager(),
                                NOTIFICATION_EVENT_TYPE_ITEMS_DO_NOT_MATCH_DEFAULT_TYPE_CODE, NotificationEvent.Level.WARNING, defaultTypeCode);
            }
        }
    }


    protected Set<String> getTypeWithSuperTypes(final Object object)
    {
        final Set<String> matchingTypes = Sets.newHashSet();
        try
        {
            final String type = typeFacade.getType(object);
            matchingTypes.add(type);
            final List<String> superTypes = type != null ? typeFacade.load(type).getAllSuperTypes() : Lists.emptyList();
            matchingTypes.addAll(superTypes);
        }
        catch(final TypeNotFoundException e)
        {
            LOGGER.debug("Could not load type", e);
        }
        return matchingTypes;
    }


    @InextensibleMethod
    private List<Object> getObjectsMutable()
    {
        List<? super Object> objects = getValue(MODEL_OBJECTS, List.class);
        if(objects == null)
        {
            objects = new IdentityArrayList<>();
            setValue(MODEL_OBJECTS, objects);
        }
        return objects;
    }


    protected List<Object> getObjectIds()
    {
        return Collections.unmodifiableList(getObjectIdsMutable());
    }


    @InextensibleMethod
    protected List<Object> getObjectIdsMutable()
    {
        List<? super Object> objectIds = getValue(MODEL_OBJECTS_IDS, List.class);
        if(objectIds == null)
        {
            objectIds = new ArrayList<>();
            setValue(MODEL_OBJECTS_IDS, objectIds);
        }
        return objectIds;
    }


    protected void removeObjectsImmediately(final Collection<Object> objects)
    {
        final List<Object> objectsBeforeRemoval = new ArrayList<>(getObjects());
        final Object referenceObject = getReferenceObject();
        if(referenceObject != null && saveAndCancelButtonActive
                        && objectsBeforeRemoval.stream().anyMatch(object -> getItemComparisonFacade().isEqualItem(object, referenceObject)))
        {
            getMessageBoxBuilder(COMPARE_VIEW_CHANGE_MSG_BOX)//
                            .withCancel(getLabel(COMPARE_VIEW_BTH_CANCEL))//
                            .withConfirm(getLabel(COMPARE_VIEW_BTH_SAVE), () -> {
                                saveObjects();
                                handleRemoveObjectsImmediately(objects, objectsBeforeRemoval);
                            })//
                            .withTitle(getLabel(COMPARE_VIEW_TITLE))//
                            .withMessage(getLabel(COMPARE_VIEW_CHANGE_MSG_TEXT))//
                            .build()//
                            .show(this.getWidgetslot());
        }
        else
        {
            handleRemoveObjectsImmediately(objects, objectsBeforeRemoval);
        }
    }


    @InextensibleMethod
    private void handleRemoveObjectsImmediately(final Collection<Object> objects, List<Object> objectsBeforeRemoval)
    {
        getObjectsMutable().removeAll(objects);
        removeObjectIdsImmediately(objects);
        assureReferenceInObjectsList();
        final List<Object> objectsAfterRemoval = new ArrayList<>(getObjects());
        sendOutput(SOCKET_OUTPUT_OBJECTS_LIST_UPDATED, Pair.of(objectsBeforeRemoval, objectsAfterRemoval));
    }


    protected void removeObjectIdsImmediately(final Collection<Object> objects)
    {
        final Collection<Object> idsToRemove = objects.stream().map(getObjectFacade()::getObjectId).collect(Collectors.toList());
        getObjectIdsMutable().removeAll(idsToRemove);
    }


    protected void setObjectsImmediately(final Collection<Object> objects)
    {
        clearObjectsAndIds();
        addObjectsImmediately(objects);
        setReferenceObjectImmediately(null);
    }


    protected void clearObjectsAndIds()
    {
        getObjectsMutable().clear();
        getObjectIdsMutable().clear();
    }


    protected void addObjectsImmediately(final Collection<Object> objects)
    {
        if(CollectionUtils.isNotEmpty(objects))
        {
            final List<Object> objectsMutable = getObjectsMutable();
            final List<Object> objectsBeforeAddition = new ArrayList<>(objectsMutable);
            final List<Object> nonIdentityObjectsMutableList = new ArrayList<>(objectsMutable);
            final Predicate<Object> objectsContains = nonIdentityObjectsMutableList::contains;
            objects.stream().filter(objectsContains.negate()).forEach(objectsMutable::add);
            assureReferenceInObjectsList();
            addObjectIdsImmediately(objects);
            final List<Object> objectsAfterAddition = new ArrayList<>(getObjects());
            sendOutput(SOCKET_OUTPUT_OBJECTS_LIST_UPDATED, Pair.of(objectsBeforeAddition, objectsAfterAddition));
        }
    }


    protected void addObjectIdsImmediately(final Collection<Object> objects)
    {
        if(CollectionUtils.isNotEmpty(objects))
        {
            final List<Object> objectIdsMutable = getObjectIdsMutable();
            final List<Object> nonObjectIdsMutableList = new ArrayList<>(objectIdsMutable);
            final Predicate<Object> objectsContains = nonObjectIdsMutableList::contains;
            objects.stream().map(getObjectFacade()::getObjectId).filter(objectsContains.negate()).forEach(objectIdsMutable::add);
        }
    }


    protected void updateObjectsImmediately(final List<Object> changedObjects)
    {
        getObjectsMutable().replaceAll(object -> {
            final int index = changedObjects.indexOf(object);
            if(index > -1)
            {
                return changedObjects.get(index);
            }
            else
            {
                return object;
            }
        });
        final DefaultComparisonState<? super Object> comparisonState = getComparisonState();
        changedObjects.forEach(object -> comparisonState.removeObjectFromCompared(getObjectFacade().getObjectId(object), object));
    }


    protected CompareViewLayout getLayout()
    {
        if(StringUtils.isNotBlank(getWidgetSettings().getString(SETTING_LAYOUT)))
        {
            return CustomRendererClassUtil.createRenderer(getWidgetSettings().getString(SETTING_LAYOUT), CompareViewLayout.class);
        }
        return getDefaultLayout();
    }


    protected CompareViewLayout getDefaultLayout()
    {
        return defaultLayout;
    }


    public void setDefaultLayout(final CompareViewLayout compareViewLayout)
    {
        this.defaultLayout = compareViewLayout;
    }


    protected TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    public Button getSaveButton()
    {
        return saveButton;
    }


    public Button getCancelButton()
    {
        return cancelButton;
    }


    protected CockpitConfigurationService getCockpitConfigurationService()
    {
        return getElementConfigurationProvider().getCockpitConfigurationService();
    }


    protected ItemComparisonFacade getItemComparisonFacade()
    {
        return itemComparisonFacade;
    }


    protected CockpitLocaleService getCockpitLocaleService()
    {
        return cockpitLocaleService;
    }


    public void setCockpitLocaleService(final CockpitLocaleService cockpitLocaleService)
    {
        this.cockpitLocaleService = cockpitLocaleService;
    }


    protected CockpitUserService getCockpitUserService()
    {
        return cockpitUserService;
    }


    public void setCockpitUserService(final CockpitUserService cockpitUserService)
    {
        this.cockpitUserService = cockpitUserService;
    }


    protected WidgetRenderingUtils getWidgetRenderingUtils()
    {
        return widgetRenderingUtils;
    }


    public void setWidgetRenderingUtils(final WidgetRenderingUtils widgetRenderingUtils)
    {
        this.widgetRenderingUtils = widgetRenderingUtils;
    }


    protected NotificationService getNotificationService()
    {
        return notificationService;
    }


    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    protected ElementConfigurationProvider getElementConfigurationProvider()
    {
        return elementConfigurationProvider;
    }


    public void setElementConfigurationProvider(final ElementConfigurationProvider elementConfigurationProvider)
    {
        this.elementConfigurationProvider = elementConfigurationProvider;
    }


    protected ValidationHandler getValidationHandler()
    {
        return validationHandler;
    }


    public void setValidationHandler(final ValidationHandler validationHandler)
    {
        this.validationHandler = validationHandler;
    }


    protected LabelService getLabelService()
    {
        return labelService;
    }


    public void setLabelService(final LabelService labelService)
    {
        this.labelService = labelService;
    }


    protected CompareViewValidationPopupHandler getCompareViewValidationPopupHandler()
    {
        return compareViewValidationPopupHandler;
    }


    public void setCompareViewValidationPopupHandler(final CompareViewValidationPopupHandler compareViewValidationPopupHandler)
    {
        this.compareViewValidationPopupHandler = compareViewValidationPopupHandler;
    }


    protected Optional<Object> getObjectById(final Object id)
    {
        if(id == null)
        {
            return Optional.empty();
        }
        try
        {
            return Optional.ofNullable(getObjectFacade().load(String.valueOf(id)));
        }
        catch(final ObjectNotFoundException e)
        {
            LOGGER.error("Could not load object for id {}. Comparison results may be invalid", id, e);
        }
        return Optional.empty();
    }


    protected class PartialComparisonOperation extends AbstractOperation
    {
        public PartialComparisonOperation()
        {
            super(getWidgetInstanceManager().getLabel(LABEL_LOADING), true, Progress.ProgressType.NONE);
        }


        /**
         * @deprecated since 1811 Please use {@link #PartialComparisonOperation()} instead
         */
        @Deprecated(since = "1811", forRemoval = true)
        public PartialComparisonOperation(final DefaultComparisonState state)
        {
            this();
        }


        @Override
        public Object execute(final Progress progress)
        {
            final var referenceObjectId = getReferenceObjectId();
            final Optional<Object> notComparedObjectId = getObjectIds().stream().filter(objectId -> objectId != referenceObjectId)
                            .filter(objectId -> !getComparisonState().getComparedObjectIds().contains(objectId)).findFirst();
            final Object referenceObject = getReferenceObject();
            final Optional<Object> notComparedObject = getObjects().stream().filter(object -> object != referenceObject)
                            .filter(object -> !getComparisonState().getComparedObjectIds().contains(getObjectFacade().getObjectId(object)))
                            .findFirst();
            ComparisonResult comparisonResult = null;
            if(notComparedObject.isPresent() && referenceObject != null)
            {
                try
                {
                    comparisonResult = performPartialComparison(referenceObject, notComparedObject.orElse(null));
                }
                catch(final CockpitConfigurationException ex)
                {
                    LOGGER.error(ex.getLocalizedMessage(), ex);
                }
            }
            return new PartialComparisonOperationResult(comparisonResult, referenceObjectId, notComparedObjectId.orElse(null),
                            referenceObject != null, notComparedObject.isPresent());
        }
    }
}
