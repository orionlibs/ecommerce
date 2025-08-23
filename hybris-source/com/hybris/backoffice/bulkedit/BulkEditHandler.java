/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.bulkedit;

import static com.hybris.backoffice.bulkedit.BulkEditConstants.VARIANT_ATTRIBUTES_MAP_MODEL;

import com.google.common.collect.ImmutableMap;
import com.hybris.backoffice.attributechooser.Attribute;
import com.hybris.backoffice.widgets.notificationarea.NotificationService;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEventTypes;
import com.hybris.cockpitng.config.jaxb.wizard.CustomType;
import com.hybris.cockpitng.config.jaxb.wizard.StepType;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.dataaccess.context.impl.DefaultContext;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacadeOperationResult;
import com.hybris.cockpitng.dataaccess.facades.object.exceptions.ObjectAccessException;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.TypeFacade;
import com.hybris.cockpitng.dataaccess.facades.type.exceptions.TypeNotFoundException;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.type.ObjectValueService;
import com.hybris.cockpitng.validation.model.ValidationInfo;
import com.hybris.cockpitng.validation.model.ValidationSeverity;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandler;
import com.hybris.cockpitng.widgets.configurableflow.FlowActionHandlerAdapter;
import com.hybris.cockpitng.widgets.configurableflow.FlowCancelActionHandler;
import de.hybris.platform.servicelayer.interceptor.impl.InterceptorExecutionPolicy;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.tx.Transaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

/**
 * Bulk edit handler which is responsible for applying changes defined in the {@link BulkEditForm}. Params:
 * <ul>
 * <li>bulkEditFormModelKey - path to bulk edit for {@link BulkEditForm} in widget model - required</li>
 * </ul>
 * Applies changes on every item defined in bulk edit form {@link BulkEditForm#getItemsToEdit()}. If any of those
 * operation fails changes will be reverted on all items.
 */
public class BulkEditHandler implements FlowActionHandler, FlowCancelActionHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(BulkEditHandler.class);
    private static final String PARAM_BULK_EDIT_FORM_MODEL_KEY = "bulkEditFormModelKey";
    private static final String TEMPLATE_OBJECT_TYPE = "templateObjectType";
    protected static final String PROPERTY_HAS_VALIDATION_ERRORS = "hasValidationErrors";
    private ObjectValueService objectValueService;
    private ObjectFacade objectFacade;
    private TypeFacade typeFacade;
    private PermissionFacade permissionFacade;
    private NotificationService notificationService;
    private ModelService modelService;
    private SessionService sessionService;
    private BulkEditValidationHelper bulkEditValidationHelper;
    private Set<String> disabledInterceptorsBeanNames;
    private transient Set<DynamicAttributeBulkEditHandler> dynamicAttributeBulkEditHandlers = new HashSet<>();


    @Override
    public void perform(final CustomType customType, final FlowActionHandlerAdapter adapter, final Map<String, String> parameters)
    {
        final BulkEditForm bulkEditForm = getBulkEditForm(adapter.getWidgetInstanceManager().getModel(), parameters);
        if(bulkEditForm == null)
        {
            notificationService.notifyUser(BulkEditConstants.NOTIFICATION_SOURCE_BULK_EDIT,
                            BulkEditConstants.NOTIFICATION_EVENT_TYPE_MISSING_FORM, NotificationEvent.Level.FAILURE);
            storeInformationAboutValidationError(parameters);
            return;
        }
        if(CollectionUtils.isEmpty(bulkEditForm.getItemsToEdit()))
        {
            return;
        }
        loadType(bulkEditForm.getTemplateObject()).ifPresent(type -> bulkEditForm.getParameters().put(TEMPLATE_OBJECT_TYPE, type));
        copyVariantAttributesToForm(adapter.getWidgetInstanceManager(), bulkEditForm);
        bulkEditForm.getItemsToEdit()
                        .forEach(item -> loadType(item).ifPresent(dataType -> applyChanges(item, dataType, bulkEditForm)));
        final Map<Object, List<ValidationInfo>> validations = bulkEditValidationHelper.validateModifiedItems(bulkEditForm,
                        ValidationSeverity.WARN);
        if(MapUtils.isEmpty(validations))
        {
            if(!hasModifyPermission(bulkEditForm, parameters))
            {
                adapter.cancel();
                return;
            }
            final ObjectFacadeOperationResult<Object> saveResult = saveChanges(bulkEditForm, parameters);
            if(saveResult.hasError())
            {
                revertAppliedChanges(bulkEditForm);
                notifySaveFailed(adapter, saveResult);
                storeInformationAboutValidationError(parameters);
            }
            else
            {
                notifySaveSucceeded(adapter, bulkEditForm);
                adapter.cancel();
            }
        }
        else
        {
            revertAppliedChanges(bulkEditForm);
            adjustValidationStep(customType, adapter);
            showValidationErrors(validations, adapter, bulkEditForm);
            storeInformationAboutValidationError(parameters);
        }
    }


    private void adjustValidationStep(final CustomType customType, final FlowActionHandlerAdapter adapter)
    {
        final ArrayList<StepType> wizardSteps = adapter.getWidgetInstanceManager().getModel().getValue("allSteps", ArrayList.class);
        final StepType lastStep = wizardSteps.get(wizardSteps.size() - 1);
        customType.setDefaultTarget(lastStep.getId());
    }


    private void storeInformationAboutValidationError(final Map<String, String> parameters)
    {
        parameters.put(PROPERTY_HAS_VALIDATION_ERRORS, "true");
    }


    protected void showValidationErrors(final Map<Object, List<ValidationInfo>> validations,
                    final FlowActionHandlerAdapter adapter, final BulkEditForm bulkEditForm)
    {
        final List<ValidationResult> validationResults = validations.entrySet().stream()
                        .map(entry -> new ValidationResult(entry.getKey(), entry.getValue())).collect(Collectors.toList());
        bulkEditForm.setValidations(validationResults);
        adapter.custom();
    }


    private static void copyVariantAttributesToForm(final WidgetInstanceManager widgetInstanceManager,
                    final BulkEditForm bulkEditForm)
    {
        final Map variantsMap = widgetInstanceManager.getModel().getValue(VARIANT_ATTRIBUTES_MAP_MODEL, Map.class);
        bulkEditForm.getParameters().put(VARIANT_ATTRIBUTES_MAP_MODEL, variantsMap);
    }


    protected ObjectFacadeOperationResult<Object> saveChanges(final BulkEditForm bulkEditForm,
                    final Map<String, String> parameters)
    {
        return bulkEditForm.isValidateAllAttributes() ? saveChangesWithInterceptors(bulkEditForm)
                        : saveChangesWithoutInterceptors(bulkEditForm);
    }


    protected boolean hasModifyPermission(final BulkEditForm bulkEditForm,
                    final Map<String, String> parameters)
    {
        return bulkEditForm != null && permissionFacade.canChangeInstances(bulkEditForm.getItemsToEdit());
    }


    protected ObjectFacadeOperationResult<Object> saveChangesWithoutInterceptors(final BulkEditForm bulkEditForm)
    {
        final Map<String, Object> params = ImmutableMap.of(InterceptorExecutionPolicy.DISABLED_INTERCEPTOR_BEANS,
                        Collections.unmodifiableSet(getDisabledInterceptorsBeanNames()));
        return sessionService.executeInLocalViewWithParams(params, new SessionExecutionBody()
        {
            @Override
            public Object execute()
            {
                return getObjectFacade().save(bulkEditForm.getItemsToEdit(), new DefaultContext());
            }
        });
    }


    protected ObjectFacadeOperationResult<Object> saveChangesWithInterceptors(final BulkEditForm bulkEditForm)
    {
        return getObjectFacade().save(bulkEditForm.getItemsToEdit(), new DefaultContext());
    }


    protected void applyChanges(final Object item, final DataType dataType, final BulkEditForm bulkEditForm)
    {
        bulkEditForm.getAttributesForm().getChosenAttributes().forEach(attr -> applyChanges(item, dataType, attr, bulkEditForm));
    }


    protected void applyChanges(final Object item, final DataType dataType, final Attribute attribute, final BulkEditForm form)
    {
        final DataAttribute dataAttribute = dataType.getAttribute(attribute.getQualifier());
        if(form.isClearAttribute(attribute.getQualifier()))
        {
            clearAttribute(item, dataAttribute, attribute);
        }
        else if(dataAttribute.isLocalized() && attribute.hasSubAttributes())
        {
            setLocalizedValue(item, dataAttribute, attribute, form);
        }
        else
        {
            switch(dataAttribute.getAttributeType())
            {
                case SINGLE:
                case RANGE:
                    setSingleValue(item, attribute, form);
                    break;
                case COLLECTION:
                case LIST:
                case SET:
                    setCollectionValue(item, attribute, form);
                    break;
                case MAP:
                    setMapValue(item, attribute, form);
                    break;
            }
        }
    }


    protected void setLocalizedValue(final Object item, final DataAttribute dataAttribute, final Attribute attribute,
                    final BulkEditForm form)
    {
        final Object newValue = getNewValue(attribute.getQualifier(), form);
        if(dataAttribute.isLocalized() && newValue instanceof HashMap)
        {
            if(form.isQualifierToMerge(attribute.getQualifier()))
            {
                final Map<Locale, Object> currentValue = getObjectValueService().getValue(attribute.getQualifier(), item);
                final Map<Locale, Object> newLocalizedValue = mergeMaps((Map)newValue, currentValue);
                getObjectValueService().setValue(attribute.getQualifier(), item, newLocalizedValue);
            }
            else
            {
                final Set<Locale> locales = attribute.getSubAttributes().stream()
                                .filter(attr -> StringUtils.isNotBlank(attr.getIsoCode())).map(attr -> Locale.forLanguageTag(attr.getIsoCode()))
                                .collect(Collectors.toSet());
                setLocalizedValue(item, locales, attribute.getQualifier(), (Map<Locale, Object>)newValue);
            }
        }
    }


    private Object getNewValue(final String qualifier, final BulkEditForm form)
    {
        if(!isVariantAttribute(qualifier, form))
        {
            return getObjectValueService().getValue(qualifier, form.getTemplateObject());
        }
        else
        {
            return getObjectValueService().getValue(qualifier, form.getParameters().get(VARIANT_ATTRIBUTES_MAP_MODEL));
        }
    }


    private static boolean isVariantAttribute(final String qualifier, final BulkEditForm form)
    {
        final DataType templateObjectType = (DataType)form.getParameters().get(TEMPLATE_OBJECT_TYPE);
        final DataAttribute dataAttribute = templateObjectType.getAttribute(qualifier);
        return dataAttribute.isVariantAttribute();
    }


    protected void clearAttribute(final Object item, final DataAttribute dataAttribute, final Attribute attribute)
    {
        if(dataAttribute.isLocalized() && attribute.hasSubAttributes())
        {
            final Map<Locale, Object> currentValue = getObjectValueService().getValue(attribute.getQualifier(), item);
            attribute.getSubAttributes().stream().filter(attr -> StringUtils.isNotBlank(attr.getIsoCode()))
                            .map(attr -> Locale.forLanguageTag(attr.getIsoCode())).forEach(locale -> currentValue.put(locale, null));
            getObjectValueService().setValue(attribute.getQualifier(), item, currentValue);
        }
        else
        {
            getObjectValueService().setValue(attribute.getQualifier(), item, null);
        }
    }


    /**
     * @deprecated since 1808 please use {@link #setSingleValue(Object, Attribute, BulkEditForm)}
     */
    @Deprecated(since = "1808", forRemoval = true)
    protected void setSingleValue(final Object item, final Attribute attribute, final Object templateObject)
    {
        final Object newValue = getObjectValueService().getValue(attribute.getQualifier(), templateObject);
        if(isNotEmptyValue(newValue))
        {
            getObjectValueService().setValue(attribute.getQualifier(), item, newValue);
        }
    }


    protected void setSingleValue(final Object item, final Attribute attribute, final BulkEditForm form)
    {
        if(!isVariantAttribute(attribute.getQualifier(), form))
        {
            setSingleValue(item, attribute, form.getTemplateObject());
        }
        else
        {
            final Object newValue = getNewValue(attribute.getQualifier(), form);
            if(isNotEmptyValue(newValue))
            {
                getObjectValueService().setValue(attribute.getQualifier(), item, newValue);
            }
        }
    }


    protected void setLocalizedValue(final Object item, final Set<Locale> locales, final String qualifier,
                    final Object templateObject)
    {
        setLocalizedValue(item, locales, qualifier, getObjectValueService().getValue(qualifier, templateObject));
    }


    protected void setLocalizedValue(final Object item, final Set<Locale> locales, final String qualifier,
                    final Map<Locale, Object> newValue)
    {
        final Map<Locale, Object> currentValue = getObjectValueService().getValue(qualifier, item);
        final Map<Locale, Object> mergedValue = new HashMap<>();
        if(currentValue != null)
        {
            mergedValue.putAll(currentValue);
        }
        locales.forEach(locale -> {
            final Object newLocalizedValue = newValue.get(locale);
            if(isNotEmptyValue(newLocalizedValue))
            {
                mergedValue.put(locale, newLocalizedValue);
            }
        });
        getObjectValueService().setValue(qualifier, item, mergedValue);
    }


    protected void setCollectionValue(final Object item, final Attribute attribute, final BulkEditForm form)
    {
        final DynamicAttributeBulkEditHandler dynamicAttributeBulkEditHandler = getDynamicAttributeBulkEditHandlers().stream()
                        .filter(handler -> handler.canHandle(attribute.getQualifier())).findFirst().orElse(null);
        final Object newValue;
        if(dynamicAttributeBulkEditHandler != null)
        {
            newValue = dynamicAttributeBulkEditHandler.getSelectedItems();
        }
        else
        {
            newValue = getNewValue(attribute.getQualifier(), form);
        }
        if(newValue instanceof Collection && !((Collection)newValue).isEmpty())
        {
            final Collection newCollection;
            if(form.isQualifierToMerge(attribute.getQualifier()))
            {
                final Collection<Object> currentCollection = getObjectValueService().getValue(attribute.getQualifier(), item);
                newCollection = mergeCollections(currentCollection, (Collection<Object>)newValue);
            }
            else
            {
                newCollection = ((Collection)newValue);
            }
            getObjectValueService().setValue(attribute.getQualifier(), item, newCollection);
        }
    }


    protected Collection<? super Object> mergeCollections(final Collection<? super Object> current,
                    final Collection<? super Object> valuesToAdd)
    {
        final Collection<? super Object> newCollection;
        if(current instanceof Set)
        {
            newCollection = new HashSet<>(current);
        }
        else
        {
            newCollection = current != null ? new ArrayList<>(current) : new ArrayList();
        }
        valuesToAdd.stream().filter(newElement -> current == null || !current.contains(newElement)).forEach(newCollection::add);
        return newCollection;
    }


    protected void setMapValue(final Object item, final Attribute attribute, final BulkEditForm form)
    {
        final Object newValue = getNewValue(attribute.getQualifier(), form);
        if(newValue instanceof Map && !((Map)newValue).isEmpty())
        {
            final Map valueToSet;
            if(form.isQualifierToMerge(attribute.getQualifier()))
            {
                final Map currentValue = getObjectValueService().getValue(attribute.getQualifier(), item);
                valueToSet = mergeMaps((Map)newValue, currentValue);
            }
            else
            {
                valueToSet = ((Map)newValue);
            }
            getObjectValueService().setValue(attribute.getQualifier(), item, valueToSet);
        }
    }


    protected Map mergeMaps(final Map newValue, final Map currentValue)
    {
        final Map valueToSet = new HashMap();
        if(currentValue != null)
        {
            valueToSet.putAll(currentValue);
        }
        if(newValue != null)
        {
            newValue.forEach((key, value) -> {
                if(isNotEmptyValue(value))
                {
                    valueToSet.put(key, value);
                }
            });
        }
        return valueToSet;
    }


    protected void revertAppliedChanges(final BulkEditForm bulkEditForm)
    {
        bulkEditForm.getItemsToEdit().forEach(getModelService()::refresh);
    }


    protected void notifySaveFailed(final FlowActionHandlerAdapter adapter, final ObjectFacadeOperationResult<Object> saveResult)
    {
        final Map<Object, ObjectAccessException> notificationReferences = saveResult.getFailedObjects().stream()
                        .collect(Collectors.toMap(Function.identity(), saveResult::getErrorForObject, ObjectUtils::defaultIfNull));
        final String notificationSource = getNotificationService().getWidgetNotificationSource(adapter.getWidgetInstanceManager());
        getNotificationService().notifyUser(notificationSource, NotificationEventTypes.EVENT_TYPE_OBJECT_UPDATE,
                        NotificationEvent.Level.FAILURE, notificationReferences);
    }


    protected void notifySaveSucceeded(final FlowActionHandlerAdapter adapter, final BulkEditForm bulkEditForm)
    {
        getNotificationService().notifyUser(
                        getNotificationService().getWidgetNotificationSource(adapter.getWidgetInstanceManager()),
                        NotificationEventTypes.EVENT_TYPE_OBJECT_UPDATE, NotificationEvent.Level.SUCCESS, bulkEditForm.getItemsToEdit());
    }


    protected void notifyCannotSaveValidationFailed(final BulkEditForm bulkEditForm,
                    final Map<Object, List<ValidationInfo>> validations)
    {
        validations.forEach((item, infos) -> getNotificationService().notifyUser(BulkEditConstants.NOTIFICATION_SOURCE_BULK_EDIT,
                        BulkEditConstants.NOTIFICATION_EVENT_TYPE_VALIDATION_ERROR, NotificationEvent.Level.FAILURE, item, infos));
    }


    protected boolean isNotEmptyValue(final Object newLocalizedValue)
    {
        return newLocalizedValue != null && !isEmptyString(newLocalizedValue) && !isEmptyCollection(newLocalizedValue)
                        && !isEmptyMap(newLocalizedValue);
    }


    protected boolean isEmptyMap(final Object newLocalizedValue)
    {
        return newLocalizedValue instanceof Map && ((Map)newLocalizedValue).isEmpty();
    }


    protected boolean isEmptyCollection(final Object newLocalizedValue)
    {
        return newLocalizedValue instanceof Collection && ((Collection)newLocalizedValue).isEmpty();
    }


    protected boolean isEmptyString(final Object newLocalizedValue)
    {
        return newLocalizedValue instanceof String && StringUtils.isBlank((CharSequence)newLocalizedValue);
    }


    protected Optional<DataType> loadType(final Object item)
    {
        try
        {
            final String typeCode = getTypeFacade().getType(item);
            return Optional.ofNullable(getTypeFacade().load(typeCode));
        }
        catch(final TypeNotFoundException tnfe)
        {
            LOG.error("Cannot load type", tnfe);
            return Optional.empty();
        }
    }


    public void clearDynamicAttributeBulkEditHandlers()
    {
        dynamicAttributeBulkEditHandlers.clear();
    }


    @Override
    public void perform()
    {
        clearDynamicAttributeBulkEditHandlers();
    }


    protected Set<DynamicAttributeBulkEditHandler> getDynamicAttributeBulkEditHandlers()
    {
        return dynamicAttributeBulkEditHandlers;
    }


    public void registerDynamicAttributeBulkEditHandler(final DynamicAttributeBulkEditHandler dynamicAttributeBulkEditHandler)
    {
        if(dynamicAttributeBulkEditHandler != null)
        {
            dynamicAttributeBulkEditHandlers.add(dynamicAttributeBulkEditHandler);
        }
    }


    public BulkEditForm getBulkEditForm(final WidgetModel widgetModel, final Map<String, String> parameters)
    {
        return widgetModel.getValue(parameters.get(PARAM_BULK_EDIT_FORM_MODEL_KEY), BulkEditForm.class);
    }


    public ObjectValueService getObjectValueService()
    {
        return objectValueService;
    }


    /**
     * @deprecated since 1808, not used anymore. {@link #saveChangesWithoutInterceptors} uses save method which became
     *             transactional in 1808
     */
    @Deprecated(since = "1808", forRemoval = true)
    protected void commitTransaction()
    {
        Transaction.current().commit();
    }


    /**
     * @deprecated since 1808, not used anymore. {@link #saveChangesWithoutInterceptors} uses save method which became
     *             transactional in 1808
     */
    @Deprecated(since = "1808", forRemoval = true)
    protected void rollbackTransaction()
    {
        Transaction.current().rollback();
    }


    /**
     * @deprecated since 1808, not used anymore. {@link #saveChangesWithoutInterceptors} uses save method which became
     *             transactional in 1808
     */
    @Deprecated(since = "1808", forRemoval = true)
    protected void beginTransaction()
    {
        Transaction.current().begin();
    }


    @Required
    public void setObjectValueService(final ObjectValueService objectValueService)
    {
        this.objectValueService = objectValueService;
    }


    public ObjectFacade getObjectFacade()
    {
        return objectFacade;
    }


    public PermissionFacade getPermissionFacade()
    {
        return permissionFacade;
    }


    public void setPermissionFacade(PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }


    @Required
    public void setObjectFacade(final ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    @Required
    public void setTypeFacade(final TypeFacade typeFacade)
    {
        this.typeFacade = typeFacade;
    }


    public TypeFacade getTypeFacade()
    {
        return typeFacade;
    }


    public NotificationService getNotificationService()
    {
        return notificationService;
    }


    @Required
    public void setNotificationService(final NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    public ModelService getModelService()
    {
        return modelService;
    }


    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }


    public SessionService getSessionService()
    {
        return sessionService;
    }


    @Required
    public void setSessionService(final SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public BulkEditValidationHelper getBulkEditValidationHelper()
    {
        return bulkEditValidationHelper;
    }


    @Required
    public void setBulkEditValidationHelper(final BulkEditValidationHelper bulkEditValidationHelper)
    {
        this.bulkEditValidationHelper = bulkEditValidationHelper;
    }


    public Set<String> getDisabledInterceptorsBeanNames()
    {
        if(disabledInterceptorsBeanNames == null)
        {
            disabledInterceptorsBeanNames = new HashSet<>();
        }
        return disabledInterceptorsBeanNames;
    }


    public void setDisabledInterceptorsBeanNames(final Set<String> disabledInterceptorsBeanNames)
    {
        this.disabledInterceptorsBeanNames = disabledInterceptorsBeanNames;
    }
}
