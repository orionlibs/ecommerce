package de.hybris.platform.cockpit.helpers.impl;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.helpers.ModelHelper;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.undo.UndoableOperation;
import de.hybris.platform.cockpit.model.undo.impl.BulkUndoableOperation;
import de.hybris.platform.cockpit.model.undo.impl.ItemChangeUndoableOperation;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.services.undo.UndoManager;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueHandlerPermissionException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UndoTools;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.i18n.LocalizationService;
import de.hybris.platform.servicelayer.internal.model.impl.ModelValueHistory;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContextImpl;
import de.hybris.platform.servicelayer.model.ModelContextUtils;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.commons.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultModelHelper implements ModelHelper
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultModelHelper.class.getName());
    private ModelService modelService;
    private TypeService typeService;
    private SystemService systemService;
    private UIAccessRightService uiAccessRightService;
    private I18NService i18nService;
    private CommonI18NService commonI18NService;
    private LocalizationService localizationService;
    private UndoManager undoManager;


    public void saveModel(ItemModel model, boolean createUndoOperations) throws ValueHandlerException
    {
        saveModel(model, createUndoOperations, true);
    }


    public void saveModel(ItemModel model, boolean createUndoOperations, boolean sendChangeEvents) throws ValueHandlerException
    {
        saveModels(Collections.singleton(model), createUndoOperations, sendChangeEvents);
    }


    public void saveModels(Set<ItemModel> models, boolean createUndoOperations) throws ValueHandlerException
    {
        saveModels(models, createUndoOperations, true);
    }


    public void saveModels(Set<ItemModel> models, boolean createUndoOperations, boolean sendChangeEvents) throws ValueHandlerException
    {
        Set<ModificationInfo> modificationInfos = collectModificationInfos(models);
        for(ModificationInfo info : modificationInfos)
        {
            checkUserRights(info.getModel(), info);
        }
        try
        {
            saveModelsInternal(models);
        }
        catch(ModelSavingException e)
        {
            throw new ValueHandlerException(e.getMessage(), e, Collections.EMPTY_SET);
        }
        if(sendChangeEvents)
        {
            for(ModificationInfo info : modificationInfos)
            {
                sendItemChangeEvent(info.getModel(), info);
            }
        }
        logModifications(modificationInfos);
        if(createUndoOperations)
        {
            createUndoOperations(modificationInfos);
        }
    }


    protected void sendItemChangeEvent(ItemModel model, ModificationInfo modificationInfo)
    {
        if(modificationInfo.isNew())
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(null,
                            getTypeService().wrapItem(model), Collections.EMPTY_LIST, ItemChangedEvent.ChangeType.CREATED));
        }
        else if(getModelService().isRemoved(model))
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(null,
                            getTypeService().wrapItem(model), Collections.EMPTY_LIST, ItemChangedEvent.ChangeType.REMOVED));
        }
        else
        {
            Set<String> attributes = modificationInfo.getModifiedAttributes();
            if(!attributes.isEmpty())
            {
                String typeCode = model.getItemtype();
                Set<PropertyDescriptor> props = new HashSet<>(attributes.size());
                for(String attr : attributes)
                {
                    props.add(
                                    getTypeService().getPropertyDescriptor(typeCode + "." + typeCode));
                }
                UISessionUtils.getCurrentSession()
                                .sendGlobalEvent((CockpitEvent)new ItemChangedEvent(null, getTypeService().wrapItem(model), props, ItemChangedEvent.ChangeType.CHANGED));
            }
        }
    }


    protected void checkUserRights(ItemModel model, ModificationInfo modificationInfo) throws ValueHandlerException
    {
        for(String attribute : modificationInfo.getModifiedAttributes())
        {
            checkUserRights(model, attribute, modificationInfo.isNew());
        }
    }


    protected void checkUserRights(ItemModel model, String attribute, boolean creationMode) throws ValueHandlerException
    {
        TypeService typeService = getTypeService();
        BaseType type = typeService.getBaseType(model.getItemtype());
        PropertyDescriptor propertyDescriptor = typeService.getPropertyDescriptor(type.getCode() + "." + type.getCode());
        boolean writable = true;
        if(creationMode)
        {
            boolean canCreate = getSystemService().checkPermissionOn(model.getItemtype(), "create");
            if(!canCreate)
            {
                writable = false;
            }
            else
            {
                writable = getUiAccessRightService().isWritable((ObjectType)type, propertyDescriptor, creationMode);
            }
        }
        else
        {
            writable = getUiAccessRightService().isWritable((ObjectType)type, typeService.wrapItem(model), propertyDescriptor, creationMode);
        }
        if(!writable)
        {
            getModelService().detach(model);
            throw new ValueHandlerPermissionException("Access rights violation.", Collections.singleton(propertyDescriptor));
        }
    }


    private Set<ModificationInfo> collectModificationInfos(Set<ItemModel> models)
    {
        Set<ModificationInfo> infoMap = new LinkedHashSet<>(models.size());
        for(ItemModel model : models)
        {
            infoMap.add(getModificationInfo(model));
        }
        return infoMap;
    }


    protected ModificationInfo getModificationInfo(ItemModel model)
    {
        boolean isNew = getModelService().isNew(model);
        ModificationInfo info = new ModificationInfo(this, model);
        ModelValueHistory history = ((ItemModelContextImpl)ModelContextUtils.getItemModelContext((AbstractItemModel)model)).getValueHistory();
        if(history.isDirty() || isNew)
        {
            info.setNew(isNew);
            ModelService modelService = getModelService();
            I18NService i18n = getI18nService();
            Set<String> dirties = history.getDirtyAttributes();
            for(String attribute : dirties)
            {
                info.addEntry(attribute, false, getOriginalValue(model, attribute, history), modelService
                                .getAttributeValue(model, attribute));
            }
            Map<Locale, Set<String>> localizedDirties = history.getDirtyLocalizedAttributes();
            Map<String, Set<Locale>> reversed = new HashMap<>();
            for(Map.Entry<Locale, Set<String>> entry : localizedDirties.entrySet())
            {
                for(String attribute : entry.getValue())
                {
                    Set<Locale> locales = reversed.get(attribute);
                    if(locales == null)
                    {
                        locales = new HashSet<>(1);
                    }
                    locales.add(entry.getKey());
                    reversed.put(attribute, locales);
                }
            }
            for(Map.Entry<String, Set<Locale>> entry : reversed.entrySet())
            {
                Locale localeBackup = i18n.getCurrentLocale();
                try
                {
                    Map<Locale, Object> originalValue = new HashMap<>(((Set)entry.getValue()).size());
                    Map<Locale, Object> modifiedValue = new HashMap<>(((Set)entry.getValue()).size());
                    for(Locale locale : entry.getValue())
                    {
                        i18n.setCurrentLocale(locale);
                        originalValue.put(locale, getOriginalValue(model, entry.getKey(), locale, history));
                        modifiedValue.put(locale, modelService.getAttributeValue(model, entry.getKey()));
                    }
                    info.addEntry(entry.getKey(), true, originalValue, modifiedValue);
                }
                finally
                {
                    i18n.setCurrentLocale(localeBackup);
                }
            }
        }
        return info;
    }


    protected Object getOriginalValue(ItemModel model, String attribute, ModelValueHistory history)
    {
        Object value = null;
        if(!getModelService().isNew(model) && history.isValueLoaded(attribute))
        {
            value = history.getOriginalValue(attribute);
        }
        return value;
    }


    protected Object getOriginalValue(ItemModel model, String attribute, Locale locale, ModelValueHistory history)
    {
        Object value = null;
        if(!getModelService().isNew(model) && history.isValueLoaded(attribute, locale))
        {
            value = history.getOriginalValue(attribute, locale);
        }
        return value;
    }


    private void logModifications(Set<ModificationInfo> modificationInfos)
    {
        for(ModificationInfo info : modificationInfos)
        {
            logModifications(info.getModel(), info);
        }
    }


    protected void logModifications(ItemModel model, ModificationInfo modificationInfo)
    {
        CaseInsensitiveMap<String, Object> caseInsensitiveMap1 = new CaseInsensitiveMap();
        CaseInsensitiveMap<String, Object> caseInsensitiveMap2 = new CaseInsensitiveMap();
        for(String attribute : modificationInfo.getModifiedAttributes())
        {
            caseInsensitiveMap1.put(attribute,
                            toPersistenceLayer(modificationInfo.getOriginalValue(attribute), modificationInfo.isLocalized(attribute)));
            caseInsensitiveMap2.put(attribute,
                            toPersistenceLayer(modificationInfo.getModifiedValue(attribute), modificationInfo.isLocalized(attribute)));
        }
        if(!caseInsensitiveMap2.isEmpty())
        {
            if(modificationInfo.isNew())
            {
                JaloConnection.getInstance().logItemCreation(model.getPk(), (Map)caseInsensitiveMap2);
            }
            else if(getModelService().isRemoved(model))
            {
                JaloConnection.getInstance().logItemRemoval(model.getPk(), false);
            }
            else
            {
                JaloConnection.getInstance().logItemModification(model.getPk(), (Map)caseInsensitiveMap2, (Map)caseInsensitiveMap1, false);
            }
        }
    }


    private Object toPersistenceLayer(Object value, boolean localized)
    {
        Object<Language, Object> ret = null;
        ModelService modelService = getModelService();
        if(localized)
        {
            if(value instanceof Map)
            {
                I18NService i18nService = getI18nService();
                CommonI18NService commonI18NService = getCommonI18NService();
                Map<Locale, Object> locmap = (Map<Locale, Object>)value;
                Map<Language, Object> wrapped = new HashMap<>();
                for(Locale locale : locmap.keySet())
                {
                    LanguageModel language;
                    Object locval = ((Map)value).get(locale);
                    try
                    {
                        language = commonI18NService.getLanguage(i18nService.getBestMatchingLocale(locale).getLanguage());
                    }
                    catch(UnknownIdentifierException e)
                    {
                        language = commonI18NService.getLanguage(locale.toString());
                    }
                    wrapped.put((Language)modelService.getSource(language), modelService.toPersistenceLayer(locval));
                }
                ret = (Object<Language, Object>)wrapped;
            }
        }
        else
        {
            ret = (Object<Language, Object>)modelService.toPersistenceLayer(value);
        }
        return ret;
    }


    protected void createUndoOperations(Set<ModificationInfo> modificationInfos)
    {
        UndoTools.startUndoGrouping();
        try
        {
            for(ModificationInfo info : modificationInfos)
            {
                createUndoOperation(info.getModel(), info);
            }
        }
        finally
        {
            BulkUndoableOperation bulk = new BulkUndoableOperation(UndoTools.getGroupedOperations());
            bulk.setShowOperationCount(false);
            UndoTools.stopUndoGrouping(getUndoManager(), null, bulk);
        }
    }


    protected void createUndoOperation(ItemModel model, ModificationInfo modificationInfo)
    {
        if(!getModelService().isRemoved(model))
        {
            UndoTools.addUndoOperationAndEvent(UISessionUtils.getCurrentSession().getUndoManager(), (UndoableOperation)new ItemChangeUndoableOperation(
                            getTypeService().wrapItem(model), toValueContainer(model, modificationInfo)), this);
        }
    }


    private ObjectValueContainer toValueContainer(ItemModel model, ModificationInfo modificationInfo)
    {
        TypeService typeService = getTypeService();
        BaseType type = typeService.getBaseType(model.getItemtype());
        ObjectValueContainer container = new ObjectValueContainer((ObjectType)type, model);
        for(String attribute : modificationInfo.getModifiedAttributes())
        {
            PropertyDescriptor propertyDescriptor = typeService.getPropertyDescriptor(type.getCode() + "." + type.getCode());
            if(modificationInfo.isLocalized(attribute))
            {
                Object localizedOriginalValue = modificationInfo.getOriginalValue(attribute);
                Object localizedModifiedValue = modificationInfo.getModifiedValue(attribute);
                Set<Locale> locales = new HashSet<>();
                if(localizedOriginalValue instanceof Map)
                {
                    locales.addAll(((Map)localizedOriginalValue).keySet());
                }
                if(localizedModifiedValue instanceof Map)
                {
                    locales.addAll(((Map)localizedModifiedValue).keySet());
                }
                if(!locales.isEmpty())
                {
                    I18NService i18nService = getI18nService();
                    for(Locale locale : locales)
                    {
                        ObjectValueContainer.ObjectValueHolder objectValueHolder = container.addValue(propertyDescriptor, i18nService
                                        .getBestMatchingLocale(locale).getISO3Language(), (localizedOriginalValue == null) ? null :
                                        TypeTools.item2Container(typeService, ((Map)localizedOriginalValue).get(locale)));
                        objectValueHolder.setLocalValue((localizedModifiedValue == null) ? null :
                                        TypeTools.item2Container(typeService, ((Map)localizedModifiedValue).get(locale)));
                    }
                }
                continue;
            }
            ObjectValueContainer.ObjectValueHolder valueHolder = container.addValue(propertyDescriptor, null, TypeTools.item2Container(typeService,
                            TypeTools.item2Container(typeService, modificationInfo.getOriginalValue(attribute))));
            valueHolder.setLocalValue(TypeTools.item2Container(typeService, modificationInfo.getModifiedValue(attribute)));
        }
        return container;
    }


    public void removeModel(ItemModel model, boolean createUndoOperation) throws ValueHandlerException
    {
        removeModel(model, createUndoOperation, true);
    }


    public void removeModel(ItemModel model, boolean createUndoOperation, boolean sendChangeEvents) throws ValueHandlerException
    {
        boolean canRemove = getSystemService().checkPermissionOn(model.getItemtype(), "remove");
        if(!canRemove)
        {
            throw new ValueHandlerPermissionException("Access rights violation.", Collections.EMPTY_SET);
        }
        JaloConnection.getInstance().logItemRemoval(model.getPk(), false);
        try
        {
            getModelService().remove(model);
        }
        catch(ModelSavingException e)
        {
            throw new ValueHandlerException(e.getMessage(), e, Collections.EMPTY_SET);
        }
        if(sendChangeEvents)
        {
            UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(null,
                            getTypeService().wrapItem(model), Collections.EMPTY_LIST, ItemChangedEvent.ChangeType.REMOVED));
        }
    }


    public boolean isWritable(ItemModel model, String attribute, boolean creationMode)
    {
        try
        {
            checkUserRights(model, attribute, creationMode);
            return true;
        }
        catch(ValueHandlerException e)
        {
            return false;
        }
    }


    protected boolean valuesEqual(Object value1, Object value2, boolean localized)
    {
        return ObjectUtils.equals(value1, value2);
    }


    private void saveModelsInternal(Set<ItemModel> models)
    {
        getModelService().saveAll(models);
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setSystemService(SystemService systemService)
    {
        this.systemService = systemService;
    }


    protected SystemService getSystemService()
    {
        return this.systemService;
    }


    @Required
    public void setUiAccessRightService(UIAccessRightService uiAccessRightService)
    {
        this.uiAccessRightService = uiAccessRightService;
    }


    @Required
    protected UIAccessRightService getUiAccessRightService()
    {
        return this.uiAccessRightService;
    }


    @Required
    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    protected I18NService getI18nService()
    {
        return this.i18nService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Deprecated
    public void setLocalizationService(LocalizationService localizationService)
    {
        this.localizationService = localizationService;
    }


    @Deprecated
    protected LocalizationService getLocalizationService()
    {
        LOG.warn("LocalizationService is deprecated.  Try using CommonI18NService instead.");
        return this.localizationService;
    }


    @Required
    public void setUndoManager(UndoManager undoManager)
    {
        this.undoManager = undoManager;
    }


    protected UndoManager getUndoManager()
    {
        return this.undoManager;
    }
}
