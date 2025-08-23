package de.hybris.platform.cockpit.services.values.impl;

import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ItemType;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.services.validation.CockpitValidationService;
import de.hybris.platform.cockpit.services.validation.pojos.CockpitValidationDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueHandler;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueHandlerNotValidationFrameworkException;
import de.hybris.platform.cockpit.services.values.ValueHandlerPermissionException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.CannotDecodePasswordException;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.validation.exceptions.ValidationViolationException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.spring.SpringUtil;

public class GenericItemObjectValueHandler implements ObjectValueHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(GenericItemObjectValueHandler.class);
    private static final String MATCHING_DESCRIPTORS_REQUEST_CACHE = "matchingDescriptorsRequestCache";
    private TypeService cockpitTypeService;
    private TypeService typeService;
    private ModelService modelService;
    private I18NService i18NService;
    private CommonI18NService commonI18NService;
    private UIAccessRightService accessService;
    private CockpitValidationService validationService;
    private static final Object NOT_READABLE_VALUE = new Object();


    public void loadValues(ObjectValueContainer valueContainer, ObjectType type, Object source, Set<PropertyDescriptor> descriptors, Set<String> languageIsoCodes) throws ValueHandlerException
    {
        ComposedTypeModel itemType = ((ItemType)type).getComposedType();
        ItemModel itemModel = fetchItem(source);
        if(itemModel == null)
        {
            throw new ValueHandlerException("Item does not exist.", Collections.EMPTY_SET);
        }
        if(!getModelService().isNew(itemModel) && !getTypeService().isInstance((TypeModel)itemType, itemModel))
        {
            throw new ValueHandlerException("item " + itemModel + " is no instance of item type " + type, Collections.EMPTY_SET);
        }
        if(!getModelService().isNew(itemModel) && this.modelService.isRemoved(itemModel))
        {
            throw new ValueHandlerException("Values of item " + itemModel + " can not be loaded. Reason: Item has been removed.", Collections.EMPTY_SET);
        }
        Set<ItemAttributePropertyDescriptor> notReadable = new HashSet<>();
        Set<ItemAttributePropertyDescriptor> matchingProperties = getMatchingDescriptors(itemModel, descriptors);
        Map<ItemAttributePropertyDescriptor, Object> values = getValues(itemModel, matchingProperties, languageIsoCodes);
        for(Map.Entry<ItemAttributePropertyDescriptor, Object> valueEntry : values.entrySet())
        {
            ItemAttributePropertyDescriptor propertyDescriptor = valueEntry.getKey();
            if(propertyDescriptor != null)
            {
                Object value = valueEntry.getValue();
                if(NOT_READABLE_VALUE.equals(value))
                {
                    value = null;
                    notReadable.add(propertyDescriptor);
                }
                if(propertyDescriptor.isLocalized())
                {
                    Map<String, Object> langMap = (Map<String, Object>)value;
                    for(String langIso : languageIsoCodes)
                    {
                        valueContainer.addValue((PropertyDescriptor)propertyDescriptor, langIso,
                                        TypeTools.item2Container(getCockpitTypeService(), (langMap != null) ? langMap.get(langIso) : null));
                    }
                    continue;
                }
                valueContainer.addValue((PropertyDescriptor)propertyDescriptor, null, TypeTools.item2Container(getCockpitTypeService(), value));
                continue;
            }
            LOG.warn("No ItemAttributePropertyDescriptor found for " + valueEntry.getKey());
        }
        if(!notReadable.isEmpty())
        {
            throw new ValueHandlerPermissionException("No sufficient permission to load all values.", notReadable);
        }
    }


    protected Map<ItemAttributePropertyDescriptor, Object> getValues(ItemModel item, Set<ItemAttributePropertyDescriptor> descriptors, Set<String> languageIsos) throws ValueHandlerException
    {
        List<String> visibleLanguages = EditorHelper.removeHiddenLanguages(getCockpitTypeService().wrapItem(item), languageIsos);
        Map<ItemAttributePropertyDescriptor, Object> values = new HashMap<>();
        boolean creationMode = getModelService().isNew(item);
        BaseType itemType = getCockpitTypeService().getBaseType(item.getItemtype());
        for(ItemAttributePropertyDescriptor pd : descriptors)
        {
            if(getUiAccessRightService().isReadable((ObjectType)itemType, (PropertyDescriptor)pd, creationMode))
            {
                if(pd.isSingleAttribute())
                {
                    values.put(pd, getValue(item, pd, languageIsos, visibleLanguages));
                    continue;
                }
                values.put(pd, getPathValue(item, pd, languageIsos));
                continue;
            }
            values.put(pd, NOT_READABLE_VALUE);
        }
        return values;
    }


    protected Object getValue(ItemModel item, ItemAttributePropertyDescriptor propertyDescriptor, Set<String> languageIsos, Collection<String> visibleLanguageIsos) throws ValueHandlerException
    {
        if(propertyDescriptor.isLocalized())
        {
            Locale backup = getI18NService().getCurrentLocale();
            try
            {
                Map<String, Object> locMap = null;
                for(String langIso : languageIsos)
                {
                    LanguageModel languageModel = new LanguageModel();
                    languageModel.setIsocode(langIso);
                    getI18NService().setCurrentLocale(getCommonI18NService().getLocaleForLanguage(languageModel));
                    Object value = null;
                    if(visibleLanguageIsos.contains(langIso))
                    {
                        value = getValueSimple(item, propertyDescriptor);
                    }
                    else
                    {
                        value = ValueHandler.NOT_READABLE_VALUE;
                    }
                    if(locMap == null)
                    {
                        locMap = new HashMap<>();
                    }
                    locMap.put(langIso, value);
                }
                return locMap;
            }
            finally
            {
                getI18NService().setCurrentLocale(backup);
            }
        }
        return getValueSimple(item, propertyDescriptor);
    }


    protected Object getPathValue(ItemModel item, ItemAttributePropertyDescriptor propertyDescriptor, Set<String> languageIsos) throws ValueHandlerException
    {
        ItemModel currentItem = item;
        int tokenPosition = 0;
        Object value = null;
        List<AttributeDescriptorModel> descriptors = propertyDescriptor.getAttributeDescriptors();
        TypedObject object = getCockpitTypeService().wrapItem(currentItem);
        for(AttributeDescriptorModel attributeDescriptor : propertyDescriptor.getAttributeDescriptors())
        {
            boolean isLastToken = (tokenPosition + 1 == descriptors.size());
            ItemAttributePropertyDescriptor desc = (ItemAttributePropertyDescriptor)getCockpitTypeService().getPropertyDescriptor(attributeDescriptor
                            .getEnclosingType().getCode() + "." + attributeDescriptor.getEnclosingType().getCode());
            if(isLastToken)
            {
                value = getValue(currentItem, desc, languageIsos, EditorHelper.removeHiddenLanguages(object, languageIsos));
            }
            else
            {
                TypeModel valueType = attributeDescriptor.getAttributeType();
                if(TypeTools.primitiveValue(attributeDescriptor.getLocalized()) && valueType instanceof MapTypeModel)
                {
                    valueType = ((MapTypeModel)valueType).getReturntype();
                }
                if(valueType instanceof ComposedTypeModel)
                {
                    Object itemval = getValue(currentItem, desc, languageIsos,
                                    EditorHelper.removeHiddenLanguages(object, languageIsos));
                    if(desc.isLocalized() && itemval != null)
                    {
                        itemval = ((Map)itemval).get(UISessionUtils.getCurrentSession().getGlobalDataLanguageIso());
                    }
                    if(itemval instanceof ItemModel)
                    {
                        currentItem = (ItemModel)itemval;
                    }
                    else
                    {
                        value = null;
                        break;
                    }
                }
                else
                {
                    value = getValue(currentItem, desc, languageIsos, Collections.EMPTY_SET);
                    if(!isLastToken)
                    {
                        LOG.error("Got atomic value but there are still tokens left! Value: " + value + " / current token: " + attributeDescriptor + " / path: " + descriptors);
                    }
                    break;
                }
            }
            tokenPosition++;
        }
        return value;
    }


    protected Object getValueSimple(ItemModel item, ItemAttributePropertyDescriptor propertyDescriptor) throws ValueHandlerException
    {
        Object result = null;
        try
        {
            result = getModelService().getAttributeValue(item, propertyDescriptor.getAttributeQualifier());
            if("itemtype".equalsIgnoreCase(propertyDescriptor.getLastAttributeDescriptor().getQualifier()))
            {
                result = getTypeService().getComposedTypeForCode((String)result);
            }
        }
        catch(AttributeNotSupportedException e)
        {
            try
            {
                if(!getModelService().isNew(item))
                {
                    result = getModelService().toModelLayer(((Item)
                                    getModelService().getSource(item)).getAttribute(propertyDescriptor.getAttributeQualifier()));
                }
            }
            catch(CannotDecodePasswordException cdpe)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Password is encoded using MD5 method, cannot decode password");
                }
            }
            catch(Exception e1)
            {
                throw new ValueHandlerException(e1.getLocalizedMessage(), e1,
                                Collections.singleton(propertyDescriptor));
            }
        }
        catch(Exception e)
        {
            LOG.error("Cannot get attribute " + propertyDescriptor + " from item " + item + ".", e);
        }
        return result;
    }


    protected Set<ItemAttributePropertyDescriptor> getMatchingDescriptors(ItemModel itemModel, Set<PropertyDescriptor> descriptors)
    {
        String code = itemModel.getItemtype();
        StringBuilder key = new StringBuilder(128);
        key.append(code);
        for(PropertyDescriptor descriptor : descriptors)
        {
            if(descriptor instanceof ItemAttributePropertyDescriptor)
            {
                key.append(descriptor.getQualifier());
            }
        }
        return (Set<ItemAttributePropertyDescriptor>)(new Object(this, "matchingDescriptorsRequestCache", itemModel, descriptors))
                        .get(key.toString());
    }


    private String getCallStack()
    {
        StringBuilder strBuilder = new StringBuilder();
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        for(int i = 2; i < stackTraceElements.length; i++)
        {
            StackTraceElement ste = stackTraceElements[i];
            String className = ste.getClassName();
            String methodName = ste.getMethodName();
            int lineNumber = ste.getLineNumber();
            strBuilder.append(className).append('.').append(methodName).append(':').append(lineNumber).append('\n');
        }
        return strBuilder.toString();
    }


    protected ItemModel fetchItem(Object source) throws ValueHandlerException
    {
        if(source instanceof Item)
        {
            return (ItemModel)getModelService().get(source);
        }
        if(source instanceof ItemModel)
        {
            return (ItemModel)source;
        }
        if(source instanceof PK)
        {
            return (ItemModel)getModelService().get((PK)source);
        }
        if(source instanceof TypedObject)
        {
            return (ItemModel)((TypedObject)source).getObject();
        }
        throw new ValueHandlerException("invalid source object " + source + " (class = " + (
                        (source != null) ? source.getClass().getName() : "n/a") + ") - cannot fetch item", Collections.EMPTY_SET);
    }


    public void storeValues(ObjectValueContainer container) throws ValueHandlerException
    {
        storeValues(container, false);
    }


    public void storeValues(ObjectValueContainer container, boolean forceWrite) throws ValueHandlerException
    {
        Map<ItemAttributePropertyDescriptor, Object> originalValues = new HashMap<>();
        Map<ItemAttributePropertyDescriptor, Object> modifiedValues = new HashMap<>();
        collectValues(container, forceWrite, originalValues, modifiedValues);
        if(!modifiedValues.isEmpty())
        {
            ItemModel item = fetchItem(container.getObject());
            try
            {
                setValues(item, modifiedValues);
            }
            catch(Exception e)
            {
                Set<PropertyDescriptor> descriptors = new HashSet<>((Collection)modifiedValues.keySet());
                throw new ValueHandlerException(e.getLocalizedMessage(), e, descriptors);
            }
            if(!getModelService().isNew(item))
            {
                try
                {
                    saveModel(item);
                    logChanges(item, modifiedValues, originalValues);
                }
                catch(Exception e)
                {
                    if(e.getCause() instanceof ValidationViolationException)
                    {
                        Set<CockpitValidationDescriptor> validationInfo = getValidationService().convertToValidationDescriptors(((ValidationViolationException)e
                                        .getCause()).getHybrisConstraintViolations());
                        throw new ValueHandlerException(e.getCause(), validationInfo);
                    }
                    throw new ValueHandlerNotValidationFrameworkException(e.getMessage(), e,
                                    Collections.emptySet());
                }
            }
        }
    }


    private CockpitValidationService getValidationService()
    {
        if(this.validationService == null)
        {
            this.validationService = (CockpitValidationService)SpringUtil.getBean("cockpitValidationService");
        }
        return this.validationService;
    }


    protected void setValues(ItemModel item, Map<ItemAttributePropertyDescriptor, Object> values) throws ValueHandlerException
    {
        for(Map.Entry<ItemAttributePropertyDescriptor, Object> entry : values.entrySet())
        {
            ItemAttributePropertyDescriptor propertyDescriptor = entry.getKey();
            boolean creationMode = getModelService().isNew(item);
            if(getUiAccessRightService().isWritable((ObjectType)getCockpitTypeService().getBaseType(item.getItemtype()),
                            getCockpitTypeService().wrapItem(item), (PropertyDescriptor)propertyDescriptor, creationMode))
            {
                if(propertyDescriptor.isSingleAttribute())
                {
                    setValue(item, propertyDescriptor, entry.getValue());
                    continue;
                }
                setPathValue(item, propertyDescriptor, entry.getValue());
                continue;
            }
            LOG.warn("Can not save non-editable attribute '" + propertyDescriptor.getAttributeQualifier() + "'");
        }
    }


    protected void setValue(ItemModel item, ItemAttributePropertyDescriptor propertyDescriptor, Object value) throws ValueHandlerException
    {
        if(propertyDescriptor.isLocalized())
        {
            if(value instanceof Map)
            {
                Map<String, Object> locmap = (Map<String, Object>)value;
                Locale backup = getI18NService().getCurrentLocale();
                try
                {
                    for(String langIso : locmap.keySet())
                    {
                        LanguageModel languageModel = new LanguageModel();
                        languageModel.setIsocode(langIso);
                        getI18NService().setCurrentLocale(getCommonI18NService().getLocaleForLanguage(languageModel));
                        Object locval = ((Map)value).get(langIso);
                        setValueSimple(item, propertyDescriptor, locval);
                    }
                }
                finally
                {
                    getI18NService().setCurrentLocale(backup);
                }
            }
        }
        else
        {
            setValueSimple(item, propertyDescriptor, value);
        }
    }


    protected void setPathValue(ItemModel item, ItemAttributePropertyDescriptor propertyDescriptor, Object value) throws ValueHandlerException
    {
        ItemModel currentItem = item;
        int tokenPosition = 0;
        String currentLangIso = UISessionUtils.getCurrentSession().getGlobalDataLanguageIso();
        List<AttributeDescriptorModel> descriptors = propertyDescriptor.getAttributeDescriptors();
        for(AttributeDescriptorModel attributeDescriptor : propertyDescriptor.getAttributeDescriptors())
        {
            TypeModel valueType = attributeDescriptor.getAttributeType();
            if(TypeTools.primitiveValue(attributeDescriptor.getLocalized()) && valueType instanceof MapTypeModel)
            {
                valueType = ((MapTypeModel)valueType).getReturntype();
            }
            ItemAttributePropertyDescriptor desc = (ItemAttributePropertyDescriptor)getCockpitTypeService().getPropertyDescriptor(attributeDescriptor
                            .getEnclosingType().getCode() + "." + attributeDescriptor.getEnclosingType().getCode());
            if(valueType instanceof ComposedTypeModel)
            {
                if(tokenPosition + 1 == descriptors.size())
                {
                    setValue(currentItem, desc, value);
                    saveModel(currentItem);
                }
                else
                {
                    Object itemval = getValue(currentItem, desc, desc.isLocalized() ? Collections.<String>singleton(currentLangIso) :
                                    Collections.EMPTY_SET, Collections.EMPTY_SET);
                    if(desc.isLocalized() && itemval instanceof Map)
                    {
                        itemval = ((Map)itemval).get(currentLangIso);
                    }
                    if(itemval instanceof ItemModel)
                    {
                        currentItem = (ItemModel)itemval;
                    }
                    else
                    {
                        currentItem = null;
                    }
                }
            }
            else
            {
                setValue(currentItem, desc, value);
                saveModel(currentItem);
                if(tokenPosition + 1 != descriptors.size())
                {
                    if(LOG.isInfoEnabled())
                    {
                        LOG.info("Got atomic value but there are still tokens left! Value: " + value + " / current token: " + attributeDescriptor + " / path: " + descriptors);
                    }
                }
            }
            tokenPosition++;
        }
    }


    protected void setValueSimple(ItemModel item, ItemAttributePropertyDescriptor propertyDescriptor, Object value) throws ValueHandlerException
    {
        try
        {
            if("itemtype".equalsIgnoreCase(propertyDescriptor.getLastAttributeDescriptor().getQualifier()) &&
                            !getModelService().isNew(item))
            {
                getModelService().setAttributeValue(item, propertyDescriptor.getAttributeQualifier(), ((ComposedTypeModel)value)
                                .getCode());
            }
            else
            {
                getModelService().setAttributeValue(item, propertyDescriptor.getAttributeQualifier(), value);
            }
        }
        catch(Exception e)
        {
            try
            {
                if(!getModelService().isNew(item))
                {
                    Item _item = (Item)getModelService().getSource(item);
                    _item.setAttribute(propertyDescriptor.getAttributeQualifier(), getModelService().toPersistenceLayer(value));
                }
            }
            catch(Exception e1)
            {
                throw new ValueHandlerException(e1.getLocalizedMessage(), e1,
                                Collections.singleton(propertyDescriptor));
            }
        }
    }


    protected void collectValues(ObjectValueContainer container, boolean allValues, Map<ItemAttributePropertyDescriptor, Object> originalValues, Map<ItemAttributePropertyDescriptor, Object> modifiedValues)
    {
        Set<ObjectValueContainer.ObjectValueHolder> valueHolders = allValues ? container.getAllValues() : container.getModifiedValues();
        for(ObjectValueContainer.ObjectValueHolder vh : valueHolders)
        {
            PropertyDescriptor propertyDescriptor = vh.getPropertyDescriptor();
            if(propertyDescriptor instanceof ItemAttributePropertyDescriptor)
            {
                ItemAttributePropertyDescriptor iapd = (ItemAttributePropertyDescriptor)vh.getPropertyDescriptor();
                if(TypeTools.primitiveValue(iapd.getLastAttributeDescriptor().getWritable()))
                {
                    if(propertyDescriptor.isLocalized())
                    {
                        addLocalizedValue(originalValues, iapd, vh.getLanguageIso(), vh.getOriginalValue());
                        addLocalizedValue(modifiedValues, iapd, vh.getLanguageIso(), vh.getLocalValue());
                        continue;
                    }
                    addValue(originalValues, iapd, vh.getOriginalValue());
                    addValue(modifiedValues, iapd, vh.getLocalValue());
                }
            }
        }
    }


    private void addValue(Map<ItemAttributePropertyDescriptor, Object> values, ItemAttributePropertyDescriptor propertyDescriptor, Object value)
    {
        values.put(propertyDescriptor, TypeTools.container2Item(getCockpitTypeService(), value));
    }


    private void addLocalizedValue(Map<ItemAttributePropertyDescriptor, Object> values, ItemAttributePropertyDescriptor propertyDescriptor, String languageIso, Object value)
    {
        Map<String, Object> locMap = (Map<String, Object>)values.get(propertyDescriptor);
        if(locMap == null)
        {
            values.put(propertyDescriptor, locMap = new HashMap<>());
        }
        locMap.put(languageIso, TypeTools.container2Item(getCockpitTypeService(), value));
    }


    public void updateValues(ObjectValueContainer container, Set<String> languageIsoCodes, Set<PropertyDescriptor> descriptors) throws ValueHandlerException
    {
        ObjectType type = container.getType();
        Object source = container.getObject();
        ItemModel itemModel = fetchItem(source);
        ComposedTypeModel itemType = this.typeService.getComposedTypeForCode(itemModel.getItemtype());
        if(!getTypeService().isInstance((TypeModel)itemType, itemModel))
        {
            throw new ValueHandlerException("item " + itemModel + " is no instance of item type " + type, Collections.EMPTY_SET);
        }
        if(getModelService().isRemoved(itemModel))
        {
            throw new ValueHandlerException("Values of item " + itemModel + " can not be loaded. Reason: Item has been removed.", Collections.EMPTY_SET);
        }
        Set<ItemAttributePropertyDescriptor> matchingProperties = getMatchingDescriptors(itemModel, descriptors);
        if(!matchingProperties.isEmpty())
        {
            getModelService().refresh(itemModel);
        }
        Set<ItemAttributePropertyDescriptor> notReadable = new HashSet<>();
        Map<ItemAttributePropertyDescriptor, Object> values = getValues(itemModel, matchingProperties, languageIsoCodes);
        for(Map.Entry<ItemAttributePropertyDescriptor, Object> valueEntry : values.entrySet())
        {
            ItemAttributePropertyDescriptor propertyDescriptor = valueEntry.getKey();
            if(propertyDescriptor != null)
            {
                Object value = valueEntry.getValue();
                if(NOT_READABLE_VALUE.equals(value))
                {
                    value = null;
                    notReadable.add(propertyDescriptor);
                }
                if(propertyDescriptor.isLocalized())
                {
                    Map<String, Object> langMap = (Map<String, Object>)value;
                    for(String langIso : languageIsoCodes)
                    {
                        ObjectValueContainer.ObjectValueHolder objectValueHolder = container.getValue((PropertyDescriptor)propertyDescriptor, langIso);
                        objectValueHolder.setLocalValue(TypeTools.item2Container(getCockpitTypeService(),
                                        (langMap != null) ? langMap.get(langIso) : null));
                        objectValueHolder.stored();
                    }
                    continue;
                }
                ObjectValueContainer.ObjectValueHolder valueHolder = container.getValue((PropertyDescriptor)propertyDescriptor, null);
                valueHolder.setLocalValue(TypeTools.item2Container(getCockpitTypeService(), value));
                valueHolder.stored();
                continue;
            }
            LOG.warn("No ItemAttributePropertyDescriptor found for " + propertyDescriptor);
        }
        if(!notReadable.isEmpty())
        {
            throw new ValueHandlerPermissionException("No sufficient permission to load all values.", notReadable);
        }
    }


    public void updateValues(ObjectValueContainer container, Set<String> languageIsoCodes) throws ValueHandlerException
    {
        updateValues(container, languageIsoCodes, container.getPropertyDescriptors());
    }


    protected void logChanges(ItemModel item, Map<ItemAttributePropertyDescriptor, Object> modifiedValues, Map<ItemAttributePropertyDescriptor, Object> originalValues)
    {
        CaseInsensitiveMap<String, Object> caseInsensitiveMap1 = new CaseInsensitiveMap();
        CaseInsensitiveMap<String, Object> caseInsensitiveMap2 = new CaseInsensitiveMap();
        for(Map.Entry<ItemAttributePropertyDescriptor, Object> entry : originalValues.entrySet())
        {
            ItemAttributePropertyDescriptor iapd = entry.getKey();
            if(iapd.isSingleAttribute())
            {
                caseInsensitiveMap1.put(iapd.getAttributeQualifier().toLowerCase(), toPersistenceLayer(iapd, entry.getValue()));
                caseInsensitiveMap2.put(iapd.getAttributeQualifier().toLowerCase(),
                                toPersistenceLayer(iapd, modifiedValues.get(entry.getKey())));
            }
        }
        if(!caseInsensitiveMap2.isEmpty())
        {
            JaloConnection.getInstance().logItemModification((Item)this.modelService.getSource(item), (Map)caseInsensitiveMap2, (Map)caseInsensitiveMap1, false);
        }
    }


    protected Object toPersistenceLayer(ItemAttributePropertyDescriptor propertyDescriptor, Object value)
    {
        Object<Language, Object> ret = null;
        if(propertyDescriptor.isLocalized())
        {
            if(value instanceof Map)
            {
                Map<String, Object> locmap = (Map<String, Object>)value;
                Map<Language, Object> wrapped = new HashMap<>();
                for(String langIso : locmap.keySet())
                {
                    Object locval = ((Map)value).get(langIso);
                    wrapped.put((Language)getModelService().getSource(getCommonI18NService().getLanguage(langIso)),
                                    toPersistenceLayerSimple(locval));
                }
                ret = (Object<Language, Object>)wrapped;
            }
        }
        else
        {
            ret = (Object<Language, Object>)toPersistenceLayerSimple(value);
        }
        return ret;
    }


    protected void saveModel(ItemModel model)
    {
        getModelService().save(model);
        getModelService().detach(model);
    }


    protected Object toPersistenceLayerSimple(Object value)
    {
        return getModelService().toPersistenceLayer(value);
    }


    protected TypeService getCockpitTypeService()
    {
        return this.cockpitTypeService;
    }


    @Required
    public void setCockpitTypeService(TypeService cockpitTypeService)
    {
        this.cockpitTypeService = cockpitTypeService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setI18nService(I18NService i18NService)
    {
        this.i18NService = i18NService;
    }


    public I18NService getI18NService()
    {
        return this.i18NService;
    }


    public void setUiAccessRightService(UIAccessRightService accessService)
    {
        this.accessService = accessService;
    }


    protected UIAccessRightService getUiAccessRightService()
    {
        if(this.accessService == null)
        {
            this.accessService = UISessionUtils.getCurrentSession().getUiAccessRightService();
        }
        return this.accessService;
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
}
