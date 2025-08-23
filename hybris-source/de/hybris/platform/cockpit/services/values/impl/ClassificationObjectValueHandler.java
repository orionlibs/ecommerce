package de.hybris.platform.cockpit.services.values.impl;

import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeUnit;
import de.hybris.platform.catalog.jalo.classification.util.Feature;
import de.hybris.platform.catalog.jalo.classification.util.FeatureContainer;
import de.hybris.platform.catalog.jalo.classification.util.FeatureValue;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ClassAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ItemTemplate;
import de.hybris.platform.cockpit.model.meta.impl.ItemType;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueHandler;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationObjectValueHandler implements ObjectValueHandler
{
    private static final Logger LOG = LoggerFactory.getLogger(ClassificationObjectValueHandler.class);
    private TypeService cockpitTypeService;
    private TypeService typeService;
    private ModelService modelService;
    private I18NService i18NService;
    private CommonI18NService commonI18NService;
    private SessionService sessionService;
    protected ClassificationService classificationService;


    public void loadValues(ObjectValueContainer valueContainer, ObjectType type, Object source, Set<PropertyDescriptor> descriptors, Set<String> languageIsoCodes) throws ValueHandlerException
    {
        if(descriptors != null)
        {
            boolean containsClassAttributes = false;
            for(PropertyDescriptor descriptor : descriptors)
            {
                if(descriptor instanceof ClassAttributePropertyDescriptor)
                {
                    containsClassAttributes = true;
                    break;
                }
            }
            if(containsClassAttributes)
            {
                ComposedTypeModel itemType = ((ItemType)type).getComposedType();
                ItemModel itemModel = fetchItem(source);
                if(!getModelService().isNew(itemModel) && !getTypeService().isInstance((TypeModel)itemType, itemModel))
                {
                    throw new ValueHandlerException("item " + itemModel + " is no instance of item type " + type, Collections.EMPTY_SET);
                }
                Set<ClassAttributePropertyDescriptor> matchingProperties = getMatchingDescriptors(itemModel, descriptors);
                if(!matchingProperties.isEmpty())
                {
                    Map<ClassAttributePropertyDescriptor, Object> values = readValues(itemModel, matchingProperties, languageIsoCodes);
                    for(Map.Entry<ClassAttributePropertyDescriptor, Object> valueEntry : values.entrySet())
                    {
                        ClassAttributePropertyDescriptor propertyDescriptor = valueEntry.getKey();
                        if(propertyDescriptor != null)
                        {
                            if(propertyDescriptor.isLocalized())
                            {
                                Map<String, Object> langMap = (Map<String, Object>)valueEntry.getValue();
                                for(String langIso : languageIsoCodes)
                                {
                                    valueContainer
                                                    .addValue((PropertyDescriptor)propertyDescriptor, langIso, TypeTools.item2Container(getCockpitTypeService(),
                                                                    (langMap != null) ? langMap.get(langIso) : null));
                                }
                                continue;
                            }
                            valueContainer.addValue((PropertyDescriptor)propertyDescriptor, null,
                                            TypeTools.item2Container(getCockpitTypeService(), valueEntry.getValue()));
                            continue;
                        }
                        LOG.warn("No ClassificationPropertyDescriptor found for " + valueEntry.getKey());
                    }
                }
            }
        }
    }


    @Deprecated
    protected Set<Language> getLanguages(Set<String> isoCodes)
    {
        if(isoCodes == null)
        {
            return null;
        }
        if(isoCodes.isEmpty())
        {
            return Collections.emptySet();
        }
        Set<Language> ret = new LinkedHashSet<>(isoCodes.size());
        C2LManager c2lManager = C2LManager.getInstance();
        for(String iso : isoCodes)
        {
            try
            {
                ret.add(c2lManager.getLanguageByIsoCode(iso));
            }
            catch(JaloItemNotFoundException e)
            {
                LOG.error("skipped invalid language iso code '" + iso + "'");
            }
        }
        return ret;
    }


    protected Set<LanguageModel> getLanguageModels(Set<String> isoCodes)
    {
        if(isoCodes == null)
        {
            return null;
        }
        if(isoCodes.isEmpty())
        {
            return Collections.emptySet();
        }
        Set<LanguageModel> ret = new LinkedHashSet<>(isoCodes.size());
        for(String isoCode : isoCodes)
        {
            ret.add(getCommonI18NService().getLanguage(isoCode));
        }
        return ret;
    }


    protected Map<ClassAttributePropertyDescriptor, Object> readValues(ItemModel item, Set<ClassAttributePropertyDescriptor> matchingProperties, Set<String> languageIsoCodes)
    {
        Map<ClassAttributePropertyDescriptor, Object> values = new HashMap<>();
        if(item instanceof de.hybris.platform.core.model.product.ProductModel)
        {
            getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, matchingProperties, item, languageIsoCodes, values));
        }
        return values;
    }


    protected Set<ClassAttributePropertyDescriptor> getMatchingDescriptors(ItemModel itemModel, Set<PropertyDescriptor> descriptors)
    {
        Set<ClassAttributePropertyDescriptor> ret = new LinkedHashSet<>(descriptors.size());
        Set<ClassificationClassModel> cClasses = null;
        for(PropertyDescriptor pd : descriptors)
        {
            if(pd instanceof ClassAttributePropertyDescriptor)
            {
                if(cClasses == null)
                {
                    cClasses = getClassClasses(itemModel);
                }
                if(cClasses
                                .contains(getModelService().get(((ClassAttributePropertyDescriptor)pd).getClassificationClass().getPK())))
                {
                    ret.add((ClassAttributePropertyDescriptor)pd);
                    continue;
                }
                LOG.debug("skipped descriptor " + pd + " since the associated class. class is not assigned to current item");
            }
        }
        return ret;
    }


    private Set<ClassificationClassModel> getClassClasses(ItemModel itemModel)
    {
        Set<ClassificationClassModel> cClasses = new HashSet<>();
        getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, itemModel, cClasses));
        return cClasses;
    }


    protected ItemModel fetchItem(Object source) throws ValueHandlerException
    {
        if(source instanceof Item)
        {
            return (ItemModel)this.modelService.get(source);
        }
        if(source instanceof ItemModel)
        {
            return (ItemModel)source;
        }
        if(source instanceof PK)
        {
            return (ItemModel)this.modelService.get((PK)source);
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
        ItemModel item = fetchItem(container.getObject());
        if(item instanceof de.hybris.platform.core.model.product.ProductModel)
        {
            Object exception = getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, forceWrite, container, item));
            if(exception != null)
            {
                throw (ValueHandlerException)exception;
            }
        }
    }


    protected FeatureContainer getFeatureContainer(ItemModel item, ClassAttributeAssignment attributeAssignment)
    {
        if(item instanceof de.hybris.platform.core.model.product.ProductModel)
        {
            try
            {
                return FeatureContainer.loadTyped((Product)getModelService().getSource(item), new ClassAttributeAssignment[] {attributeAssignment});
            }
            catch(JaloInvalidParameterException jipe)
            {
                if(LOG.isInfoEnabled())
                {
                    LOG.info("Can not create typed feature container for '" + item + "'.");
                }
            }
        }
        return null;
    }


    protected Object getValue(Feature feature, Set<String> languageIsos, boolean multi, boolean localized)
    {
        if(localized)
        {
            Locale backup = getI18NService().getCurrentLocale();
            try
            {
                Map<String, Object> locMap = null;
                for(String langIso : languageIsos)
                {
                    LanguageModel languageModel = new LanguageModel();
                    languageModel.setIsocode(langIso);
                    getI18NService().setCurrentLocale(this.commonI18NService.getLocaleForLanguage(languageModel));
                    Object value = getValueSimple(feature, multi);
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
        return getValueSimple(feature, multi);
    }


    protected Object getValueSimple(Feature feature, boolean multi)
    {
        List values = feature.getValues();
        if(multi)
        {
            List<FeatureValue> featureValues = new ArrayList<>();
            for(Object featureValue : values)
            {
                featureValues.add(convertFeatureValue(featureValue));
            }
            return featureValues;
        }
        return (values != null && !values.isEmpty()) ? convertFeatureValue(values.get(0)) : null;
    }


    protected void setValue(Feature feature, Object value, String langIso, boolean multiple)
    {
        if(langIso != null)
        {
            I18NService i18n = getI18NService();
            Locale backup = i18n.getCurrentLocale();
            try
            {
                LanguageModel languageModel = new LanguageModel();
                languageModel.setIsocode(langIso);
                i18n.setCurrentLocale(this.commonI18NService.getLocaleForLanguage(languageModel));
                setValueSimple(feature, value, multiple);
            }
            finally
            {
                i18n.setCurrentLocale(backup);
            }
        }
        else
        {
            setValueSimple(feature, value, multiple);
        }
    }


    protected void setValueSimple(Feature feature, Object value, boolean multiple)
    {
        if(multiple)
        {
            List<Object> unwrapedValues = removeNullValues((List<Object>)unwrapClassValue(value, feature));
            List savedValues = feature.setValues(unwrapedValues);
            List featureValueList = (value instanceof List) ? (List)value : Collections.EMPTY_LIST;
            for(int i = 0; i < savedValues.size(); i++)
            {
                Object savedValue = savedValues.get(i);
                Object objValue = (featureValueList.size() > i) ? featureValueList.get(i) : null;
                if(savedValue instanceof FeatureValue && objValue instanceof FeatureValue)
                {
                    FeatureValue featureValue = (FeatureValue)savedValue;
                    ClassificationAttributeUnit newUnit = (((FeatureValue)objValue).getUnit() == null) ? null : (ClassificationAttributeUnit)this.modelService.getSource(((FeatureValue)objValue).getUnit());
                    featureValue.setUnit(newUnit);
                    featureValue.setDescription(((FeatureValue)objValue).getDescription());
                }
            }
        }
        else if(value == null)
        {
            feature.clear();
        }
        else
        {
            FeatureValue featureValue = feature.setValue(unwrapClassValue(value, feature));
            if(value instanceof FeatureValue)
            {
                ClassificationAttributeUnit newUnit = (((FeatureValue)value).getUnit() == null) ? null : (ClassificationAttributeUnit)this.modelService.getSource(((FeatureValue)value).getUnit());
                featureValue.setUnit(newUnit);
                featureValue.setDescription(((FeatureValue)value).getDescription());
            }
        }
    }


    private List<Object> removeNullValues(List<Object> listValue)
    {
        if(listValue == null)
        {
            return null;
        }
        if(listValue.isEmpty())
        {
            return listValue;
        }
        ArrayList<Object> processed = new ArrayList(listValue.size());
        for(Object object : listValue)
        {
            if(object != null)
            {
                processed.add(object);
            }
        }
        return processed;
    }


    private FeatureValue convertFeatureValue(Object featureValue)
    {
        if(featureValue instanceof FeatureValue)
        {
            FeatureValue jaloFeatureValue = (FeatureValue)featureValue;
            Object value = jaloFeatureValue.getValue();
            if(value instanceof Item)
            {
                value = this.modelService.get(value);
            }
            String description = jaloFeatureValue.getDescription();
            ClassificationAttributeUnit jaloUnit = jaloFeatureValue.getUnit();
            ClassificationAttributeUnitModel unit = null;
            if(jaloUnit != null)
            {
                unit = (ClassificationAttributeUnitModel)this.modelService.get(jaloFeatureValue.getUnit());
            }
            return new FeatureValue(value, description, unit);
        }
        return null;
    }


    public Object wrapClassValue(Object<Object> value)
    {
        Object<Object> wrappedValue = value;
        if(value instanceof Collection && !((Collection)value).isEmpty())
        {
            boolean wrapped = false;
            List<Object> wrappedList = new ArrayList(((Collection)value).size());
            for(Object o : value)
            {
                if(o instanceof Item)
                {
                    wrapped = true;
                    wrappedList.add(this.modelService.get(((Item)o).getPK()));
                    continue;
                }
                wrappedList.add(o);
            }
            if(wrapped)
            {
                wrappedValue = (Object<Object>)wrappedList;
            }
        }
        else if(value instanceof Item)
        {
            wrappedValue = (Object<Object>)this.modelService.get(((Item)value).getPK());
        }
        return wrappedValue;
    }


    protected Object unwrapClassValue(Object value, Feature feature)
    {
        Object<Object> unwrappedValue = null;
        Object initialValue = value;
        if(initialValue instanceof FeatureValue)
        {
            initialValue = ((FeatureValue)initialValue).getValue();
        }
        if(initialValue instanceof Collection)
        {
            Iterator<Object> iterator = ((Collection<Object>)initialValue).iterator();
            List<Object> unwrappedList = new ArrayList();
            while(iterator.hasNext())
            {
                Object item = iterator.next();
                if(item instanceof FeatureValue)
                {
                    item = ((FeatureValue)item).getValue();
                }
                if(item instanceof ItemModel)
                {
                    unwrappedList.add(this.modelService.getSource(item));
                    continue;
                }
                unwrappedList.add(item);
            }
            unwrappedValue = (Object<Object>)unwrappedList;
        }
        else if(initialValue instanceof ItemModel)
        {
            unwrappedValue = (Object<Object>)this.modelService.getSource(initialValue);
        }
        else
        {
            unwrappedValue = (Object<Object>)initialValue;
        }
        return unwrappedValue;
    }


    public void updateValues(ObjectValueContainer container, Set<String> languageIsoCodes, Set<PropertyDescriptor> descriptors) throws ValueHandlerException
    {
        ObjectType type = container.getType();
        Object source = container.getObject();
        ComposedTypeModel itemType = null;
        if(type instanceof ItemTemplate)
        {
            ItemTemplate itemTemplate = (ItemTemplate)type;
            itemType = itemTemplate.getBaseType().getComposedType();
        }
        else
        {
            itemType = ((ItemType)type).getComposedType();
        }
        ItemModel itemModel = fetchItem(source);
        if(!getTypeService().isInstance((TypeModel)itemType, itemModel))
        {
            throw new ValueHandlerException("item " + itemModel + " is no instance of item type " + type, Collections.EMPTY_SET);
        }
        Set<ClassAttributePropertyDescriptor> matchingProperties = getMatchingDescriptors(itemModel, descriptors);
        if(!matchingProperties.isEmpty())
        {
            Map<ClassAttributePropertyDescriptor, Object> values = readValues(itemModel, matchingProperties, languageIsoCodes);
            for(Map.Entry<ClassAttributePropertyDescriptor, Object> valueEntry : values.entrySet())
            {
                ClassAttributePropertyDescriptor propertyDescriptor = valueEntry.getKey();
                if(propertyDescriptor != null)
                {
                    if(propertyDescriptor.isLocalized())
                    {
                        Map<String, Object> langMap = (Map<String, Object>)valueEntry.getValue();
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
                    valueHolder.setLocalValue(TypeTools.item2Container(getCockpitTypeService(), valueEntry.getValue()));
                    valueHolder.stored();
                    continue;
                }
                LOG.warn("No ClassificationPropertyDescriptor found for " + valueEntry.getKey());
            }
        }
    }


    public void updateValues(ObjectValueContainer container, Set<String> languageIsoCodes) throws ValueHandlerException
    {
        updateValues(container, languageIsoCodes, container.getPropertyDescriptors());
    }


    protected TypeService getCockpitTypeService()
    {
        return this.cockpitTypeService;
    }


    public void setCockpitTypeService(TypeService cockpitTypeService)
    {
        this.cockpitTypeService = cockpitTypeService;
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
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


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected ClassificationService getClassificationService()
    {
        return this.classificationService;
    }


    public void setClassificationService(ClassificationService classificationService)
    {
        this.classificationService = classificationService;
    }
}
