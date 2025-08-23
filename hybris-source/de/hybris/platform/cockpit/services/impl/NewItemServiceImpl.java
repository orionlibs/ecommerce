package de.hybris.platform.cockpit.services.impl;

import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.cockpit.jalo.CockpitManager;
import de.hybris.platform.cockpit.jalo.template.CockpitItemTemplate;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ClassificationType;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.MediaStreamPropertyMarker;
import de.hybris.platform.cockpit.services.MediaUpdateService;
import de.hybris.platform.cockpit.services.NewItemService;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.config.impl.MediaEditorSectionConfiguration;
import de.hybris.platform.cockpit.services.validation.CockpitValidationService;
import de.hybris.platform.cockpit.services.validation.pojos.CockpitValidationDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueHandler;
import de.hybris.platform.cockpit.services.values.ObjectValueHandlerRegistry;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueHandlerNotValidationFrameworkException;
import de.hybris.platform.cockpit.services.values.ValueHandlerPermissionException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.WizardUtils;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.validation.exceptions.ValidationViolationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;

public class NewItemServiceImpl extends AbstractServiceImpl implements NewItemService
{
    private static final Logger LOG = LoggerFactory.getLogger(NewItemServiceImpl.class);
    private SystemService systemService;
    private MediaUpdateService mediaUpdateService;
    private ObjectValueHandlerRegistry valueHandlerRegistry;
    private I18NService i18NService;
    private boolean useServiceLayer = true;
    private CockpitValidationService validationService;


    public TypedObject createNewItem(ObjectValueContainer valueContainer, ObjectTemplate template) throws ValueHandlerException
    {
        if(!getSystemService().checkPermissionOn(template.getBaseType().getCode(), "create"))
        {
            throw new ValueHandlerPermissionException("Error: User '" + getSystemService().getCurrentUser() + "' is not allowed to create items of type '" + template
                            .getBaseType().getCode() + "'", Collections.EMPTY_SET);
        }
        ObjectValueContainer clonedValueContainer = valueContainer.clone();
        TypedObject ret = this.useServiceLayer ? createNewItemServiceLayer(clonedValueContainer, template) : createNewItemJalo(clonedValueContainer, template);
        synchronizeValueContainers(clonedValueContainer, valueContainer);
        return ret;
    }


    private void synchronizeValueContainers(ObjectValueContainer source, ObjectValueContainer target) throws ValueHandlerException
    {
        BaseType baseType;
        target.setObject(source.getObject());
        Set<String> allLanguages = new HashSet<>();
        for(ObjectValueContainer.ObjectValueHolder holder : target.getAllValues())
        {
            String languageIso = holder.getLanguageIso();
            if(StringUtils.isNotBlank(languageIso))
            {
                allLanguages.add(languageIso);
            }
        }
        ObjectType type = target.getType();
        if(CollectionUtils.isEmpty(type.getSupertypes()) && type instanceof ObjectTemplate)
        {
            baseType = ((ObjectTemplate)type).getBaseType();
        }
        for(ObjectValueHandler valueHandler : getValueHandlerRegistry().getValueHandlerChain((ObjectType)baseType))
        {
            valueHandler.updateValues(target, allLanguages);
        }
    }


    @Deprecated
    public TypedObject createNewItemJalo(ObjectValueContainer valueContainer, ObjectTemplate template) throws ValueHandlerException
    {
        BaseType itemType = template.getBaseType();
        JaloSession jaloSession = JaloSession.getCurrentSession();
        Map<String, Object> newItemValues = new HashMap<>();
        Map<String, Map<Language, Object>> localizedValues = new HashMap<>();
        Set<PropertyDescriptor> mandatoryAttributes = getMandatoryAttributes((ObjectType)itemType);
        checkMandatoryAttributes(valueContainer, mandatoryAttributes);
        Set<PropertyDescriptor> creationProperties = new HashSet<>();
        Set<ObjectValueContainer.ObjectValueHolder> allValues = valueContainer.getAllValues();
        for(ObjectValueContainer.ObjectValueHolder ovh : allValues)
        {
            PropertyDescriptor propertyDescriptor = ovh.getPropertyDescriptor();
            Object currentValue = ovh.getCurrentValue();
            if(propertyDescriptor instanceof ItemAttributePropertyDescriptor && currentValue != null && !currentValue.equals(""))
            {
                ItemAttributePropertyDescriptor iapd = (ItemAttributePropertyDescriptor)propertyDescriptor;
                if(iapd.isSingleAttribute())
                {
                    String attrQualifier = iapd.getAttributeQualifier();
                    currentValue = getModelService().toPersistenceLayer(TypeTools.container2Item(getTypeService(), currentValue));
                    if(propertyDescriptor.isLocalized())
                    {
                        addLocalizedValue(localizedValues, attrQualifier, ovh.getLanguageIso(), currentValue);
                    }
                    else
                    {
                        newItemValues.put(attrQualifier, currentValue);
                    }
                    creationProperties.add(propertyDescriptor);
                }
            }
        }
        for(Map.Entry<String, Map<Language, Object>> entry : localizedValues.entrySet())
        {
            newItemValues.put(entry.getKey(), entry.getValue());
        }
        Item item = null;
        ComposedType type = jaloSession.getTypeManager().getComposedType(itemType.getCode());
        SessionContext ctx = null;
        try
        {
            ctx = JaloSession.getCurrentSession().createLocalSessionContext();
            if(UITools.searchRestrictionsDisabledInCockpit())
            {
                ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            }
            ctx.setLanguage(null);
            item = type.newInstance(newItemValues);
        }
        catch(JaloBusinessException e)
        {
            throw new ValueHandlerException(e.getLocalizedMessage(), e, Collections.EMPTY_SET);
        }
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
        JaloConnection.getInstance().logItemCreation(item, newItemValues);
        ItemModel itemModel = (ItemModel)getModelService().get(item);
        valueContainer.setObject(itemModel);
        TypedObject wrappedItem = getTypeService().wrapItem(itemModel);
        if(getTypeService().getBaseType("Media").isAssignableFrom((ObjectType)wrappedItem.getType()))
        {
            MediaStreamPropertyMarker mediaStreamPropertyMarker = new MediaStreamPropertyMarker();
            if(valueContainer.getPropertyDescriptors().contains(mediaStreamPropertyMarker))
            {
                ObjectValueContainer.ObjectValueHolder valueHolder = valueContainer.getValue((PropertyDescriptor)mediaStreamPropertyMarker, null);
                MediaEditorSectionConfiguration.MediaContent mediaContent = (MediaEditorSectionConfiguration.MediaContent)valueHolder.getLocalValue();
                getMediaUpdateService().updateMediaBinaryStream(wrappedItem, mediaContent.getData(), mediaContent.getMimeType(), mediaContent
                                .getName());
            }
        }
        for(ObjectValueContainer.ObjectValueHolder ovh : new HashSet(allValues))
        {
            if(creationProperties.contains(ovh.getPropertyDescriptor()))
            {
                valueContainer.removeValue(ovh);
            }
        }
        for(ObjectValueHandler valueHandler : getValueHandlerRegistry().getValueHandlerChain((ObjectType)wrappedItem.getType()))
        {
            valueHandler.storeValues(valueContainer);
        }
        return wrappedItem;
    }


    protected TypedObject createNewItemServiceLayer(ObjectValueContainer valueContainer, ObjectTemplate template) throws ValueHandlerException
    {
        Set<CockpitValidationDescriptor> validationInfo;
        BaseType itemType = template.getBaseType();
        Map<String, Object> newItemValues = new HashMap<>();
        Map<String, Map<Language, Object>> localizedValues = new HashMap<>();
        ItemModel newItemModel = null;
        Object object = valueContainer.getObject();
        if(object instanceof ItemModel)
        {
            newItemModel = (ItemModel)object;
        }
        if(newItemModel == null)
        {
            newItemModel = (ItemModel)getModelService().create(itemType.getCode());
        }
        Set<PropertyDescriptor> creationProperties = new HashSet<>();
        ObjectValueContainer.ObjectValueHolder itemTypeValueHolder = null;
        Set<ObjectValueContainer.ObjectValueHolder> allValues = valueContainer.getAllValues();
        for(ObjectValueContainer.ObjectValueHolder ovh : allValues)
        {
            PropertyDescriptor propertyDescriptor = ovh.getPropertyDescriptor();
            Object currentValue = ovh.getCurrentValue();
            if(propertyDescriptor instanceof ItemAttributePropertyDescriptor && currentValue != null && !currentValue.equals("") &&
                            !ValueHandler.NOT_READABLE_VALUE.equals(currentValue))
            {
                ItemAttributePropertyDescriptor iapd = (ItemAttributePropertyDescriptor)propertyDescriptor;
                if("itemtype".equals(iapd.getAttributeQualifier()))
                {
                    itemTypeValueHolder = ovh;
                    continue;
                }
                if(iapd.isSingleAttribute())
                {
                    try
                    {
                        String attrQualifier = iapd.getAttributeQualifier();
                        currentValue = getModelService().toPersistenceLayer(TypeTools.container2Item(getTypeService(), currentValue));
                        checkWriteableCatalogVersions(propertyDescriptor, currentValue, iapd);
                        if(propertyDescriptor.isLocalized())
                        {
                            addLocalizedValue(localizedValues, attrQualifier, ovh.getLanguageIso(), currentValue);
                        }
                        else
                        {
                            newItemValues.put(attrQualifier, currentValue);
                        }
                        if(propertyDescriptor.isLocalized())
                        {
                            setModelLocalizedValue(newItemModel, attrQualifier, ovh.getLanguageIso(), currentValue);
                        }
                        else
                        {
                            setModelSingleValue(newItemModel, attrQualifier, currentValue);
                        }
                        creationProperties.add(propertyDescriptor);
                    }
                    catch(AttributeNotSupportedException attributeNotSupportedException)
                    {
                    }
                }
            }
        }
        if(itemTypeValueHolder != null)
        {
            valueContainer.removeValue(itemTypeValueHolder);
        }
        for(Map.Entry<String, Map<Language, Object>> entry : localizedValues.entrySet())
        {
            newItemValues.put(entry.getKey(), entry.getValue());
        }
        try
        {
            validationInfo = getValidationService().validateModel(newItemModel);
        }
        catch(Exception e)
        {
            throw new ValueHandlerNotValidationFrameworkException(e.getMessage(), e, null);
        }
        checkOmittedProperties(valueContainer, template, validationInfo, newItemModel);
        if(validationInfo != null)
        {
            restatusIgnoredDescriptors(valueContainer, validationInfo);
            if(!validationInfo.isEmpty() && (getValidationService().containsLevel(validationInfo, 3) ||
                            getValidationService().containsLevel(validationInfo, 2)))
            {
                throw new ValueHandlerException(validationInfo);
            }
        }
        SessionContext ctx = null;
        try
        {
            ctx = JaloSession.getCurrentSession().createLocalSessionContext();
            if(UITools.searchRestrictionsDisabledInCockpit())
            {
                ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            }
            getModelService().save(newItemModel);
        }
        catch(ModelSavingException e)
        {
            validationInfo = new HashSet<>();
            if(e.getCause() instanceof ValidationViolationException)
            {
                validationInfo = getValidationService().convertToValidationDescriptors(((ValidationViolationException)e.getCause()).getHybrisConstraintViolations());
            }
            else
            {
                throw new ValueHandlerNotValidationFrameworkException(e.getMessage(), e,
                                getValidationService().validateModel(newItemModel));
            }
            restatusIgnoredDescriptors(valueContainer, validationInfo);
            throw new ValueHandlerException(e, validationInfo);
        }
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
        Item item = (Item)getModelService().getSource(newItemModel);
        JaloConnection.getInstance().logItemCreation(item, newItemValues);
        ItemModel itemModel = (ItemModel)getModelService().get(item);
        valueContainer.setObject(itemModel);
        TypedObject wrappedItem = getTypeService().wrapItem(itemModel);
        if(getTypeService().getBaseType("Media").isAssignableFrom((ObjectType)wrappedItem.getType()))
        {
            MediaStreamPropertyMarker mediaStreamPropertyMarker = new MediaStreamPropertyMarker();
            if(valueContainer.getPropertyDescriptors().contains(mediaStreamPropertyMarker))
            {
                ObjectValueContainer.ObjectValueHolder valueHolder = valueContainer.getValue((PropertyDescriptor)mediaStreamPropertyMarker, null);
                MediaEditorSectionConfiguration.MediaContent mediaContent = (MediaEditorSectionConfiguration.MediaContent)valueHolder.getLocalValue();
                getMediaUpdateService().updateMediaBinaryStream(wrappedItem, mediaContent.getData(), mediaContent.getMimeType(), mediaContent
                                .getName());
            }
        }
        for(ObjectValueContainer.ObjectValueHolder ovh : new HashSet(allValues))
        {
            PropertyDescriptor propertyDescriptor = ovh.getPropertyDescriptor();
            if(creationProperties.contains(propertyDescriptor))
            {
                valueContainer.removeValue(ovh);
                continue;
            }
            Object currentValue = ovh.getCurrentValue();
            if(propertyDescriptor instanceof ItemAttributePropertyDescriptor && (currentValue == null || currentValue
                            .equals("")))
            {
                ItemAttributePropertyDescriptor iapd = (ItemAttributePropertyDescriptor)propertyDescriptor;
                if(iapd.isSingleAttribute())
                {
                    valueContainer.removeValue(ovh);
                }
            }
        }
        for(ObjectValueHandler valueHandler : getValueHandlerRegistry().getValueHandlerChain((ObjectType)wrappedItem.getType()))
        {
            valueHandler.storeValues(valueContainer);
        }
        return wrappedItem;
    }


    private void checkWriteableCatalogVersions(PropertyDescriptor propertyDescriptor, Object currentValue, ItemAttributePropertyDescriptor iapd) throws ValueHandlerException
    {
        if(iapd.getEnclosingType().getCatalogVersionAttribute() != null && currentValue instanceof de.hybris.platform.catalog.jalo.CatalogVersion)
        {
            CatalogVersionModel catalogVersionModel = (CatalogVersionModel)getModelService().get(currentValue);
            if(!UISessionUtils.getCurrentSession().getUser().getAllWriteableCatalogVersions().contains(catalogVersionModel))
            {
                Set<CockpitValidationDescriptor> validationInfo = new HashSet<>();
                validationInfo.add(new CockpitValidationDescriptor(propertyDescriptor, 3,
                                Labels.getLabel("no_permission_for_current_catalog_version"), null));
                throw new ValueHandlerException(validationInfo);
            }
        }
    }


    private void checkOmittedProperties(ObjectValueContainer valueContainer, ObjectTemplate template, Set<CockpitValidationDescriptor> validationInfo, ItemModel newItemModel)
    {
        Set<PropertyDescriptor> omittedProperties = TypeTools.getOmittedProperties(valueContainer, template, true);
        for(PropertyDescriptor propertyDescriptor : omittedProperties)
        {
            String attrCode = getTypeService().getAttributeCodeFromPropertyQualifier(propertyDescriptor.getQualifier());
            try
            {
                if(attrCode != null && getModelService().getAttributeValue(newItemModel, attrCode) == null)
                {
                    CockpitValidationDescriptor cockpitValidationDescriptor = createCockpitValidationDescriptor(propertyDescriptor);
                    validationInfo.add(cockpitValidationDescriptor);
                }
            }
            catch(AttributeNotSupportedException e)
            {
                if(newItemModel instanceof de.hybris.platform.variants.model.VariantProductModel)
                {
                    CockpitValidationDescriptor cockpitValidationDescriptor = createCockpitValidationDescriptor(propertyDescriptor);
                    validationInfo.add(cockpitValidationDescriptor);
                    continue;
                }
                String logMsg = "Attribute " + propertyDescriptor + " is not accessible for type " + newItemModel;
                LOG.warn(logMsg);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(logMsg, (Throwable)e);
                }
            }
        }
    }


    private CockpitValidationDescriptor createCockpitValidationDescriptor(PropertyDescriptor propertyDescriptor)
    {
        StringBuilder msg = new StringBuilder();
        msg.append(Labels.getLabel("required_attribute_missing")).append(": '").append(propertyDescriptor.getQualifier())
                        .append("'");
        return new CockpitValidationDescriptor(propertyDescriptor, 3, msg.toString(), null);
    }


    private void restatusIgnoredDescriptors(ObjectValueContainer valueContainer, Set<CockpitValidationDescriptor> validationInfo)
    {
        for(String ignoredConstraint : valueContainer.getIgnoredValidationConstraints())
        {
            for(CockpitValidationDescriptor cockpitValidationDescriptor : validationInfo)
            {
                if(cockpitValidationDescriptor.getConstraint() != null && cockpitValidationDescriptor
                                .getConstraint().getPk().toString().equals(ignoredConstraint))
                {
                    cockpitValidationDescriptor.setCockpitMessageLevel(1);
                }
            }
        }
    }


    public void setModelSingleValue(ItemModel model, String qualifier, Object value) throws AttributeNotSupportedException
    {
        try
        {
            Object valueModel = getModelService().toModelLayer(value);
            getModelService().setAttributeValue(model, qualifier, valueModel);
        }
        catch(IllegalArgumentException e)
        {
            throw new AttributeNotSupportedException(e.getMessage(), e, qualifier);
        }
    }


    public void setModelLocalizedValue(ItemModel model, String qualifier, String languageIso, Object value) throws AttributeNotSupportedException
    {
        I18NService i18n = getI18NService();
        Locale backup = i18n.getCurrentLocale();
        try
        {
            i18n.setCurrentLocale(JaloSession.getCurrentSession().getC2LManager().getLanguageByIsoCode(languageIso).getLocale());
            Object valueModel = getModelService().toModelLayer(value);
            getModelService().setAttributeValue(model, qualifier, valueModel);
        }
        catch(IllegalArgumentException e)
        {
            throw new AttributeNotSupportedException(e.getMessage(), e, qualifier);
        }
        finally
        {
            i18n.setCurrentLocale(backup);
        }
    }


    private void addLocalizedValue(Map<String, Map<Language, Object>> localizedValues, String attrName, String languageIso, Object currentValue)
    {
        if(localizedValues.containsKey(attrName))
        {
            ((Map<Language, Object>)localizedValues.get(attrName)).put(JaloSession.getCurrentSession().getC2LManager().getLanguageByIsoCode(languageIso), currentValue);
        }
        else
        {
            Map<Language, Object> values = new HashMap<>();
            values.put(JaloSession.getCurrentSession().getC2LManager().getLanguageByIsoCode(languageIso), currentValue);
            localizedValues.put(attrName, values);
        }
    }


    public void setDefaultValues(ObjectValueContainer valueContainer, ObjectTemplate template)
    {
        Set<String> languageIsos = getSystemService().getAvailableLanguageIsos();
        Map<PropertyDescriptor, Object> defaultValues = TypeTools.getAllDefaultValues(getTypeService(), template, languageIsos);
        for(Map.Entry<PropertyDescriptor, Object> defaultVal : defaultValues.entrySet())
        {
            PropertyDescriptor propertyDescriptor = defaultVal.getKey();
            if(propertyDescriptor.isLocalized())
            {
                Map<String, Object> localizedMap = (Map<String, Object>)defaultVal.getValue();
                for(String langIso : languageIsos)
                {
                    try
                    {
                        ObjectValueContainer.ObjectValueHolder ovh = valueContainer.getValue(propertyDescriptor, langIso);
                        if(WizardUtils.isLocalValueEmpty(ovh.getLocalValue()))
                        {
                            ovh.setLocalValue((localizedMap == null) ? null : localizedMap.get(langIso));
                        }
                    }
                    catch(IllegalArgumentException iae)
                    {
                        valueContainer.addValue(propertyDescriptor, langIso, (localizedMap == null) ? null : localizedMap.get(langIso));
                    }
                }
                continue;
            }
            try
            {
                ObjectValueContainer.ObjectValueHolder ovh = valueContainer.getValue(propertyDescriptor, null);
                if(WizardUtils.isLocalValueEmpty(ovh.getLocalValue()))
                {
                    ovh.setLocalValue(defaultVal.getValue());
                }
            }
            catch(IllegalArgumentException iae)
            {
                valueContainer.addValue(propertyDescriptor, null, defaultVal.getValue());
            }
        }
        setTemplate(valueContainer, template);
        setDefaultCategories(valueContainer, template);
    }


    protected void setTemplate(ObjectValueContainer objectValueContainer, ObjectTemplate template)
    {
        if(!template.isDefaultTemplate())
        {
            PropertyDescriptor propertyDescriptor = getTypeService().getPropertyDescriptor("Item." + GeneratedCockpitConstants.Attributes.Item.ASSIGNEDCOCKPITITEMTEMPLATES);
            if(propertyDescriptor != null)
            {
                List<TypedObject> templates = new ArrayList<>();
                try
                {
                    ObjectValueContainer.ObjectValueHolder ovh = objectValueContainer.getValue(propertyDescriptor, null);
                    Object object = ovh.getCurrentValue();
                    if(object != null)
                    {
                        templates.addAll((Collection<? extends TypedObject>)object);
                    }
                }
                catch(IllegalArgumentException illegalArgumentException)
                {
                }
                String templCode = template.getCode();
                int idx = templCode.indexOf('.');
                templCode = templCode.substring(idx + 1);
                CockpitItemTemplate cit = CockpitManager.getInstance().getCockpitItemTemplate(TypeManager.getInstance().getComposedType(template.getBaseType().getCode()), templCode);
                if(cit != null)
                {
                    templates.add(getTypeService().wrapItem(cit));
                }
                if(!templates.isEmpty())
                {
                    try
                    {
                        ObjectValueContainer.ObjectValueHolder ovh = objectValueContainer.getValue(propertyDescriptor, null);
                        ovh.setLocalValue(templates);
                    }
                    catch(IllegalArgumentException iae)
                    {
                        objectValueContainer.addValue(propertyDescriptor, null, templates);
                    }
                }
            }
        }
    }


    private void setDefaultCategories(ObjectValueContainer objectValueContainer, ObjectTemplate template)
    {
        PropertyDescriptor propertyDescriptor = getTypeService().getPropertyDescriptor(template.getBaseType().getCode() + ".supercategories");
        if(propertyDescriptor != null)
        {
            List<TypedObject> categories = new ArrayList<>();
            try
            {
                ObjectValueContainer.ObjectValueHolder ovh = objectValueContainer.getValue(propertyDescriptor, null);
                Object object = ovh.getCurrentValue();
                if(object != null)
                {
                    categories.addAll((Collection<? extends TypedObject>)object);
                }
            }
            catch(IllegalArgumentException illegalArgumentException)
            {
            }
            Collection<? extends ExtendedType> extendedTypes = template.getExtendedTypes();
            for(ExtendedType extType : extendedTypes)
            {
                if(extType instanceof ClassificationType)
                {
                    ClassificationClass classifiactionClass = ((ClassificationType)extType).getClassificationClass();
                    if(classifiactionClass != null)
                    {
                        categories.add(getTypeService().wrapItem(classifiactionClass));
                    }
                }
            }
            if(!categories.isEmpty())
            {
                try
                {
                    ObjectValueContainer.ObjectValueHolder ovh = objectValueContainer.getValue(propertyDescriptor, null);
                    ovh.setLocalValue(categories);
                }
                catch(IllegalArgumentException iae)
                {
                    objectValueContainer.addValue(propertyDescriptor, null, categories);
                }
            }
        }
    }


    private void checkMandatoryAttributes(ObjectValueContainer valueContainer, Set<PropertyDescriptor> mandatoryAttributes) throws ValueHandlerException
    {
        boolean result = true;
        for(PropertyDescriptor pd : mandatoryAttributes)
        {
            if(pd != null)
            {
                result = isAttributePresent(valueContainer, pd);
            }
            if(!result)
            {
                throw new ValueHandlerException("Missing parameter [" + pd.getQualifier() + "] to create item.",
                                Collections.singleton(pd));
            }
        }
    }


    private boolean isAttributePresent(ObjectValueContainer valueContainer, PropertyDescriptor propertyDescriptor)
    {
        Object currentValue = null;
        try
        {
            ObjectValueContainer.ObjectValueHolder cvHolder = valueContainer.getValue(propertyDescriptor, propertyDescriptor.isLocalized() ?
                            JaloSession.getCurrentSession().getSessionContext().getLanguage().getIsoCode() : null);
            currentValue = cvHolder.getCurrentValue();
        }
        catch(IllegalArgumentException e)
        {
            LOG.warn("Could not find value holder for mandatory attribute '" + propertyDescriptor.getQualifier() + "'. Check configuration!");
        }
        if(currentValue == null)
        {
            return false;
        }
        if(currentValue instanceof String)
        {
            return !((String)currentValue).isEmpty();
        }
        return true;
    }


    @Required
    public void setMediaUpdateService(MediaUpdateService mediaUpdateService)
    {
        this.mediaUpdateService = mediaUpdateService;
    }


    public MediaUpdateService getMediaUpdateService()
    {
        if(this.mediaUpdateService == null)
        {
            this.mediaUpdateService = (MediaUpdateService)SpringUtil.getBean("mediaUpdateService");
        }
        return this.mediaUpdateService;
    }


    @Required
    public void setSystemService(SystemService systemService)
    {
        this.systemService = systemService;
    }


    public SystemService getSystemService()
    {
        return this.systemService;
    }


    public Set<PropertyDescriptor> getMandatoryAttributes(ObjectType type)
    {
        return TypeTools.getMandatoryAttributes(type, true);
    }


    public ObjectValueHandlerRegistry getValueHandlerRegistry()
    {
        return this.valueHandlerRegistry;
    }


    @Required
    public void setValueHandlerRegistry(ObjectValueHandlerRegistry valueHandlerRegistry)
    {
        this.valueHandlerRegistry = valueHandlerRegistry;
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


    public void setUseServiceLayer(boolean useServiceLayer)
    {
        this.useServiceLayer = useServiceLayer;
    }


    public boolean isUseServiceLayer()
    {
        return this.useServiceLayer;
    }


    private CockpitValidationService getValidationService()
    {
        if(this.validationService == null)
        {
            this.validationService = (CockpitValidationService)SpringUtil.getBean("cockpitValidationService");
        }
        return this.validationService;
    }
}
