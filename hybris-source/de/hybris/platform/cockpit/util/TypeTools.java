package de.hybris.platform.cockpit.util;

import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.listview.impl.DefaultValueHandler;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.config.BaseConfiguration;
import de.hybris.platform.cockpit.services.config.InitialPropertyConfiguration;
import de.hybris.platform.cockpit.services.config.UIConfigurationService;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.meta.PropertyService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueHandler;
import de.hybris.platform.cockpit.services.values.ObjectValueHandlerRegistry;
import de.hybris.platform.cockpit.services.values.ObjectValueLazyLoader;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueHandlerPermissionException;
import de.hybris.platform.cockpit.services.values.ValueService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.model.ModelService;
import java.lang.reflect.Constructor;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;

public class TypeTools
{
    private static final String DEFAULT_DISPLAY_MAXCOLLECTIONENTRIES = "default.display.maxCollectionEntries";
    private static final Logger LOG = LoggerFactory.getLogger(TypeTools.class);


    public static String getValueAsString(LabelService labelService, Object value)
    {
        int collectionMaxEntries = 1;
        String rawParameter = UITools.getCockpitParameter("default.display.maxCollectionEntries", Executions.getCurrent());
        if(rawParameter != null)
        {
            collectionMaxEntries = Integer.parseInt(rawParameter);
        }
        return getValueAsString(labelService, value, collectionMaxEntries);
    }


    public static String getValueAsString(LabelService labelService, Object value, int size)
    {
        String text = "";
        if(value instanceof Collection)
        {
            int index = 1;
            StringBuilder stringBuilder = new StringBuilder();
            List rawCollection = new ArrayList((Collection)value);
            if(CollectionUtils.isNotEmpty(rawCollection))
            {
                stringBuilder.append(getLabel(rawCollection.get(0), labelService));
                int maxIndex = (size > 0) ? Math.min(rawCollection.size(), size) : rawCollection.size();
                for(; index < maxIndex; index++)
                {
                    stringBuilder.append(", ");
                    stringBuilder.append(getLabel(rawCollection.get(index), labelService));
                }
                if(index < rawCollection.size())
                {
                    stringBuilder.append(", ...");
                }
            }
            text = stringBuilder.toString();
        }
        else
        {
            text = getLabel(value, labelService);
        }
        return text;
    }


    @Deprecated
    public static String getTypeName(BaseType type, TypeService typeService)
    {
        return type.getName();
    }


    @Deprecated
    public static String getTypeDescription(BaseType type, TypeService typeService)
    {
        return type.getDescription();
    }


    public static Map<String, Object> getInitialValues(ObjectType type, TypedObject baseItem, TypeService typeService, UIConfigurationService configurationService)
    {
        Map<String, Object> initialValues = new HashMap<>();
        ObjectTemplate template = typeService.getObjectTemplate(type.getCode());
        BaseConfiguration baseConfiguration = (BaseConfiguration)configurationService.getComponentConfiguration(template, "base", BaseConfiguration.class);
        InitialPropertyConfiguration initialPropertyConfig = baseConfiguration.getInitialPropertyConfiguration(typeService.getObjectTemplate(baseItem.getType().getCode()), typeService);
        if(initialPropertyConfig != null)
        {
            initialValues = initialPropertyConfig.getInitialProperties(baseItem, typeService);
        }
        return initialValues;
    }


    private static String getLabel(Object value, LabelService labelService)
    {
        String ret = null;
        if(value instanceof TypedObject)
        {
            ret = labelService.getObjectTextLabel((TypedObject)value);
        }
        else if(value instanceof Boolean)
        {
            String locBoolean = "booleaneditor." + value.toString() + ".name";
            ret = Labels.getLabel(locBoolean, value.toString());
        }
        else if(value instanceof ClassificationAttributeValueModel)
        {
            ret = ((ClassificationAttributeValueModel)value).getName();
            if(ret == null)
            {
                ret = ((ClassificationAttributeValueModel)value).getCode();
            }
        }
        else if(value instanceof FeatureValue)
        {
            FeatureValue featureValue = (FeatureValue)value;
            String valueString = null;
            if(featureValue.getValue() != null)
            {
                if(featureValue.getValue() instanceof ClassificationAttributeValueModel)
                {
                    valueString = getLabel(featureValue.getValue(), labelService);
                }
                else
                {
                    valueString = featureValue.getValue().toString();
                }
            }
            String unitString = (featureValue.getUnit() == null) ? null : featureValue.getUnit().getName();
            StringBuilder builder = new StringBuilder();
            if(valueString != null)
            {
                builder.append(valueString);
            }
            if(unitString != null)
            {
                builder.append(" " + unitString);
            }
            ret = builder.toString();
        }
        else if(value instanceof Double)
        {
            NumberFormat instance = NumberFormat.getInstance(UISessionUtils.getCurrentSession().getLocale());
            instance.setMaximumFractionDigits(10);
            ret = instance.format(((Double)value).doubleValue());
        }
        else if(value instanceof Float)
        {
            ret = NumberFormat.getInstance(UISessionUtils.getCurrentSession().getLocale()).format(((Float)value).floatValue());
        }
        else if(value instanceof Date)
        {
            ret = DateFormat.getDateTimeInstance(3, 3, UISessionUtils.getCurrentSession().getLocale()).format((Date)value);
        }
        else if(value instanceof HybrisEnumValue)
        {
            ret = getEnumName((HybrisEnumValue)value);
            if(ret == null)
            {
                ret = value.toString();
            }
        }
        else if(value != null)
        {
            ret = value.toString();
        }
        return ret;
    }


    @Deprecated
    public static String getMultiplicityString(PropertyDescriptor propertyDescriptor)
    {
        return getPropertyService().getMultiplicityString(propertyDescriptor);
    }


    public static ObjectTemplate getValueTypeAsObjectTemplate(PropertyDescriptor propertyDescriptor, TypeService typeService)
    {
        String typeCode = typeService.getValueTypeCode(propertyDescriptor);
        Type type = (typeCode == null) ? null : TypeManager.getInstance().getType(typeCode);
        return (type instanceof ComposedType) ? typeService.getObjectTemplate(typeCode) : null;
    }


    @Deprecated
    public static Object getObjectAttributeValue(TypedObject object, String attributeQualifier, TypeService typeService)
    {
        Object ret = null;
        if(object != null)
        {
            if(!getModelService().isNew(object.getObject()))
            {
                Object realObject = getModelService().getSource(object.getObject());
                try
                {
                    ret = (realObject instanceof Item) ? ((Item)realObject).getAttribute(attributeQualifier) : null;
                    if(ret instanceof EnumerationValue)
                    {
                        ret = getModelService().get(((EnumerationValue)ret).getPK());
                    }
                    else if(ret instanceof Item)
                    {
                        ret = typeService.wrapItem(ret);
                    }
                    else if(ret instanceof Collection)
                    {
                        Collection tmp = (Collection)ret;
                        if(!tmp.isEmpty())
                        {
                            Object first = tmp.iterator().next();
                            if(first instanceof Item)
                            {
                                ret = typeService.wrapItems(tmp);
                            }
                        }
                    }
                }
                catch(JaloInvalidParameterException e)
                {
                    LOG.error(e.getMessage());
                }
                catch(JaloSecurityException e)
                {
                    LOG.error(e.getMessage());
                }
            }
            else
            {
                ret = getModelService().getAttributeValue(object.getObject(), attributeQualifier);
            }
        }
        return ret;
    }


    public static ObjectValueContainer createValueContainer(TypedObject typedObject, Set<PropertyDescriptor> propertyDescriptors, Set<String> langIsos)
    {
        return createValueContainer(typedObject, propertyDescriptors, langIsos, false);
    }


    public static ObjectValueContainer createValueContainer(ObjectValueHandlerRegistry valueHandlerRegistry, TypedObject typedObject, Set<PropertyDescriptor> propertyDescriptors, Set<String> langIsos, boolean lazyLoad)
    {
        ObjectValueContainer ret;
        if(lazyLoad)
        {
            ret = new ObjectValueContainer((ObjectType)typedObject.getType(), typedObject.getObject(), propertyDescriptors, langIsos, (ObjectValueLazyLoader)new Object(valueHandlerRegistry, typedObject));
        }
        else
        {
            ret = new ObjectValueContainer((ObjectType)typedObject.getType(), typedObject.getObject());
            for(ObjectValueHandler valueHandler : valueHandlerRegistry.getValueHandlerChain((ObjectType)typedObject.getType()))
            {
                try
                {
                    valueHandler.loadValues(ret, (ObjectType)typedObject.getType(), typedObject, propertyDescriptors, langIsos);
                }
                catch(ValueHandlerPermissionException valueHandlerPermissionException)
                {
                }
                catch(ValueHandlerException e)
                {
                    LOG.error("error loading object values", (Throwable)e);
                }
            }
        }
        return ret;
    }


    public static ObjectValueContainer createValueContainer(TypedObject typedObject, Set<PropertyDescriptor> propertyDescriptors, Set<String> langIsos, boolean lazyLoad)
    {
        ObjectValueHandlerRegistry valueHandlerRegistry = UISessionUtils.getCurrentSession().getValueHandlerRegistry();
        return createValueContainer(valueHandlerRegistry, typedObject, propertyDescriptors, langIsos, lazyLoad);
    }


    public static List<ObjectType> getAllSupertypes(ObjectType type)
    {
        List<ObjectType> ret = null;
        Set<ObjectType> current = new HashSet<>(type.getSupertypes());
        do
        {
            Set<ObjectType> nextLevel = null;
            for(ObjectType st : current)
            {
                if(ret == null)
                {
                    ret = new ArrayList<>();
                }
                if(ret.add(st))
                {
                    Set<ObjectType> superTypes = st.getSupertypes();
                    if(!superTypes.isEmpty())
                    {
                        if(nextLevel == null)
                        {
                            nextLevel = new LinkedHashSet<>();
                        }
                        nextLevel.addAll(superTypes);
                    }
                }
            }
            current = nextLevel;
        }
        while(current != null && !current.isEmpty());
        if(ret != null)
        {
            Collections.reverse(ret);
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }


    public static Set<ObjectType> getAllSubtypes(ObjectType type)
    {
        Set<ObjectType> ret = new LinkedHashSet<>();
        Collection<ObjectType> types = Collections.singleton(type), next = new LinkedHashSet<>();
        for(; types != null && !types.isEmpty(); types = next, next = new LinkedHashSet<>())
        {
            for(ObjectType tt : types)
            {
                ret.add(tt);
                next.addAll(tt.getSubtypes());
            }
        }
        return ret;
    }


    public static boolean hasSupertype(ObjectType type, ObjectType supertype)
    {
        Set<? extends ObjectType> current = type.getSupertypes();
        do
        {
            Set<ObjectType> nextLevel = null;
            for(ObjectType st : current)
            {
                if(st.equals(supertype))
                {
                    return true;
                }
                Set<ObjectType> superTypes = st.getSupertypes();
                if(!superTypes.isEmpty())
                {
                    if(nextLevel == null)
                    {
                        nextLevel = new LinkedHashSet<>();
                    }
                    nextLevel.addAll(superTypes);
                }
            }
            current = nextLevel;
        }
        while(current != null && !current.isEmpty());
        return false;
    }


    @Deprecated
    public static List<PK> itemToPkList(Collection<? extends Item> items)
    {
        if(items == null || items.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<PK> pkList = new ArrayList<>(items.size());
        for(Item item : items)
        {
            pkList.add(item.getPK());
        }
        return pkList;
    }


    public static List<ObjectTemplate> getTemplatesForCreation(TypeService typeService, BaseType type)
    {
        List<ObjectTemplate> ret = new ArrayList<>();
        List<Map<ObjectTemplate, Integer>> templatesForCreationWithDepth = getTemplatesForCreationWithDepth(typeService, type, 0, false);
        for(Map<ObjectTemplate, Integer> map : templatesForCreationWithDepth)
        {
            ret.add((ObjectTemplate)((Map.Entry)map.entrySet().iterator().next()).getKey());
        }
        return ret;
    }


    public static List<Map<ObjectTemplate, Integer>> getTemplatesForCreationWithDepth(TypeService typeService, BaseType type, int initialDepth, boolean showAbstract)
    {
        List<Map<ObjectTemplate, Integer>> templates = new ArrayList<>();
        for(ObjectTemplate templ : typeService.getObjectTemplates(type))
        {
            if(showAbstract || !templ.isAbstract())
            {
                templates.add(
                                Collections.singletonMap(templ, Integer.valueOf(templ.isDefaultTemplate() ? initialDepth : (initialDepth + 1))));
            }
        }
        for(ObjectType subtype : type.getSubtypes())
        {
            templates.addAll(getTemplatesForCreationWithDepth(typeService, (BaseType)subtype, initialDepth + 1, showAbstract));
        }
        return templates;
    }


    @Deprecated
    public static boolean isEditable(SystemService systemService, ObjectType type, PropertyDescriptor propertyDescriptor, boolean creationMode)
    {
        boolean writable = (checkAccessRights(systemService, type, propertyDescriptor, creationMode, "change") && checkAccessRights(systemService, type, propertyDescriptor, creationMode, "read"));
        if(writable)
        {
            writable = (propertyDescriptor.isReadable() || (creationMode && isInitial(propertyDescriptor)));
        }
        return writable;
    }


    @Deprecated
    public static boolean isReadable(SystemService systemService, ObjectType type, PropertyDescriptor propertyDescriptor, boolean creationMode)
    {
        boolean readable = checkAccessRights(systemService, type, propertyDescriptor, creationMode, "read");
        if(readable)
        {
            readable = (propertyDescriptor.isReadable() || (creationMode && isInitial(propertyDescriptor)));
        }
        return readable;
    }


    @Deprecated
    private static boolean checkAccessRights(SystemService systemService, ObjectType type, PropertyDescriptor propertyDescriptor, boolean creationMode, String permissionCode)
    {
        boolean hasRight = true;
        if(propertyDescriptor instanceof ItemAttributePropertyDescriptor)
        {
            String baseTypeCode = null;
            if(((ItemAttributePropertyDescriptor)propertyDescriptor).getEnclosingType() instanceof de.hybris.platform.variants.model.VariantTypeModel)
            {
                baseTypeCode = ((ItemAttributePropertyDescriptor)propertyDescriptor).getTypeCode();
            }
            else if(type instanceof BaseType)
            {
                baseTypeCode = ((BaseType)type).getCode();
            }
            else if(type instanceof ObjectTemplate)
            {
                baseTypeCode = ((ObjectTemplate)type).getBaseType().getCode();
            }
            ItemAttributePropertyDescriptor iapd = (ItemAttributePropertyDescriptor)propertyDescriptor;
            List<AttributeDescriptorModel> ads = iapd.getAttributeDescriptors();
            if(ads.size() == 1)
            {
                hasRight = systemService.checkAttributePermissionOn(baseTypeCode, iapd.getAttributeQualifier(), permissionCode);
            }
            else
            {
                AttributeDescriptorModel attributeDescriptorModel = iapd.getLastAttributeDescriptor();
                ComposedTypeModel composedTypeModel = attributeDescriptorModel.getEnclosingType();
                hasRight = systemService.checkAttributePermissionOn(composedTypeModel.getCode(), attributeDescriptorModel
                                .getQualifier(), permissionCode);
            }
        }
        return hasRight;
    }


    @Deprecated
    public static boolean isInitial(PropertyDescriptor propertyDescriptor)
    {
        return getPropertyService().isInitial(propertyDescriptor);
    }


    @Deprecated
    public static boolean isPartof(PropertyDescriptor propertyDescriptor)
    {
        return getPropertyService().isPartof(propertyDescriptor);
    }


    public static Object item2Container(TypeService typeService, Object<TypedObject> value)
    {
        TypedObject typedObject;
        Object<TypedObject> wrappedObject = value;
        if(value instanceof Collection && !((Collection)value).isEmpty())
        {
            boolean wrapped = false;
            int size = ((Collection)value).size();
            Collection<TypedObject> wrappedColl = (value instanceof Set) ? new HashSet(size) : new ArrayList(size);
            for(Object o : value)
            {
                if(o instanceof de.hybris.platform.core.model.ItemModel)
                {
                    wrapped = true;
                    wrappedColl.add(typeService.wrapItem(o));
                    continue;
                }
                wrappedColl.add(o);
            }
            if(wrapped)
            {
                wrappedObject = (Object<TypedObject>)wrappedColl;
            }
        }
        else if(value instanceof de.hybris.platform.core.model.ItemModel)
        {
            typedObject = typeService.wrapItem(value);
        }
        return typedObject;
    }


    public static Object container2Item(TypeService typeService, Object containerValue)
    {
        Object<Object> unwrappedItem = null;
        if(containerValue instanceof Collection)
        {
            Iterator<Object> iterator = ((Collection<Object>)containerValue).iterator();
            int size = ((Collection)containerValue).size();
            Collection<Object> unwrappedColl = (containerValue instanceof Set) ? new HashSet(size) : new ArrayList(size);
            while(iterator.hasNext())
            {
                Object item = iterator.next();
                if(item instanceof TypedObject)
                {
                    unwrappedColl.add(((TypedObject)item).getObject());
                    continue;
                }
                unwrappedColl.add(item);
            }
            unwrappedItem = (Object<Object>)unwrappedColl;
        }
        else if(containerValue instanceof TypedObject)
        {
            unwrappedItem = (Object<Object>)((TypedObject)containerValue).getObject();
        }
        else
        {
            unwrappedItem = (Object<Object>)containerValue;
        }
        return unwrappedItem;
    }


    public static boolean checkInstanceOfCategory(TypeService typeService, TypedObject object)
    {
        return typeService.getBaseType("Category").isAssignableFrom((ObjectType)object.getType());
    }


    public static boolean checkInstanceOfCatalogVersion(TypeService typeService, TypedObject object)
    {
        return typeService.getBaseType("CatalogVersion").isAssignableFrom((ObjectType)object.getType());
    }


    public static boolean checkInstanceOfProduct(TypeService typeService, TypedObject object)
    {
        return typeService.getBaseType("Product").isAssignableFrom((ObjectType)object.getType());
    }


    public static boolean checkInstanceOfMedia(TypeService typeService, TypedObject object)
    {
        return typeService.getBaseType("Media").isAssignableFrom((ObjectType)object.getType());
    }


    public static List<String> multiEdit(PropertyDescriptor propertyDesc, String languageIso, List<TypedObject> items, Object value)
    {
        List<String> errors = new ArrayList<>();
        for(TypedObject item : items)
        {
            DefaultValueHandler valueHandler = new DefaultValueHandler(propertyDesc);
            try
            {
                valueHandler.setValue(item, value, languageIso);
            }
            catch(ValueHandlerException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.error("Could not set the value '" + value + "' for the property '" + propertyDesc.getName() + "' of object '" + item + "' (Reason: " + e
                                    .getMessage() + ").", (Throwable)e);
                }
                if(item.getObject() instanceof de.hybris.platform.core.model.ItemModel && !getModelService().isNew(item.getObject()))
                {
                    errors.add(e.getMessage());
                }
            }
        }
        return errors;
    }


    @Deprecated
    public static boolean primitiveValue(Boolean value)
    {
        return (value == null) ? false : value.booleanValue();
    }


    public static int primitiveValue(Integer value)
    {
        return (value == null) ? 0 : value.intValue();
    }


    @Deprecated
    public static TypeService getCoreTypeService()
    {
        return (TypeService)SpringUtil.getBean("typeService");
    }


    @Deprecated
    public static ModelService getModelService()
    {
        return (ModelService)SpringUtil.getBean("modelService");
    }


    private static PropertyService getPropertyService()
    {
        return (PropertyService)SpringUtil.getBean("cockpitPropertyService");
    }


    public static Set<PropertyDescriptor> getOmittedProperties(ObjectValueContainer valueContainer, ObjectTemplate template, boolean creationMode)
    {
        Set<PropertyDescriptor> mandatorySet = getMandatoryAttributes((ObjectType)template, creationMode);
        Set<PropertyDescriptor> omittedSet = new HashSet<>();
        String dataLangIso = UISessionUtils.getCurrentSession().getGlobalDataLanguageIso();
        if(dataLangIso == null)
        {
            Language lang = JaloSession.getCurrentSession().getSessionContext().getLanguage();
            if(lang != null)
            {
                dataLangIso = lang.getIsoCode();
            }
        }
        for(PropertyDescriptor prop : mandatorySet)
        {
            boolean omitted = false;
            if(valueContainer.hasProperty(prop))
            {
                ObjectValueContainer.ObjectValueHolder valueHolder = valueContainer.getValue(prop, prop.isLocalized() ? dataLangIso : null);
                Object value = (valueHolder == null) ? null : valueHolder.getCurrentValue();
                if(value == null)
                {
                    omitted = true;
                }
                else if(PropertyDescriptor.Multiplicity.LIST.equals(prop.getMultiplicity()) || PropertyDescriptor.Multiplicity.SET
                                .equals(prop.getMultiplicity()))
                {
                    if(value instanceof Collection && ((Collection)value).isEmpty())
                    {
                        omitted = true;
                    }
                }
                else if("TEXT".equals(prop.getEditorType()) && value.toString().length() == 0)
                {
                    omitted = true;
                }
            }
            else
            {
                omitted = true;
            }
            if(omitted)
            {
                omittedSet.add(prop);
            }
        }
        return omittedSet;
    }


    public static Set<PropertyDescriptor> getOmittedProperties(ObjectValueContainer valueContainer, Set<PropertyDescriptor> descriptors, boolean creationMode)
    {
        Set<PropertyDescriptor> mandatoryDescriptors = new HashSet<>();
        for(PropertyDescriptor pd : descriptors)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.info("Check if mandatory: " + pd.getQualifier());
            }
            if(isMandatory(pd, creationMode))
            {
                mandatoryDescriptors.add(pd);
                if(LOG.isDebugEnabled())
                {
                    LOG.info("Attribute is mandatory: " + pd.getQualifier());
                }
            }
        }
        Set<PropertyDescriptor> omittedSet = new HashSet<>();
        String dataLangIso = UISessionUtils.getCurrentSession().getGlobalDataLanguageIso();
        if(dataLangIso == null)
        {
            Language lang = JaloSession.getCurrentSession().getSessionContext().getLanguage();
            if(lang != null)
            {
                dataLangIso = lang.getIsoCode();
            }
        }
        for(PropertyDescriptor prop : mandatoryDescriptors)
        {
            boolean omitted = false;
            if(valueContainer.hasProperty(prop))
            {
                ObjectValueContainer.ObjectValueHolder valueHolder = valueContainer.getValue(prop, prop.isLocalized() ? dataLangIso : null);
                Object value = (valueHolder == null) ? null : valueHolder.getCurrentValue();
                if(value == null)
                {
                    omitted = true;
                }
                else if(PropertyDescriptor.Multiplicity.LIST.equals(prop.getMultiplicity()) || PropertyDescriptor.Multiplicity.SET
                                .equals(prop.getMultiplicity()))
                {
                    if(value instanceof Collection && ((Collection)value).isEmpty())
                    {
                        omitted = true;
                    }
                }
                else if("TEXT".equals(prop.getEditorType()) && value.toString().length() == 0)
                {
                    omitted = true;
                }
                else if("FEATURE".equals(prop.getEditorType()) && value instanceof FeatureValue && ((FeatureValue)value)
                                .getValue().toString().length() == 0)
                {
                    omitted = true;
                }
            }
            else
            {
                omitted = true;
            }
            if(omitted)
            {
                omittedSet.add(prop);
            }
        }
        return omittedSet;
    }


    public static Set<PropertyDescriptor> getMandatoryAttributes(ObjectType type, boolean creationMode)
    {
        Set<PropertyDescriptor> descriptors = new HashSet<>();
        for(PropertyDescriptor pd : type.getPropertyDescriptors())
        {
            if(isMandatory(pd, creationMode))
            {
                descriptors.add(pd);
            }
        }
        return descriptors;
    }


    public static Set<PropertyDescriptor> getMandatoryAttributes(ObjectType type, boolean creationMode, PropertyService propertyService)
    {
        Set<PropertyDescriptor> descriptors = new HashSet<>();
        for(PropertyDescriptor pd : type.getPropertyDescriptors())
        {
            if(propertyService.isMandatory(pd, creationMode))
            {
                descriptors.add(pd);
            }
        }
        return descriptors;
    }


    @Deprecated
    public static boolean isMandatory(PropertyDescriptor propertyDescriptor, boolean creationMode)
    {
        return getPropertyService().isMandatory(propertyDescriptor, creationMode);
    }


    public static boolean propertyBelongsTo(TypeService typeService, Set<ObjectType> types, PropertyDescriptor propertyDescriptor)
    {
        ObjectType enclosingType = typeService.getObjectType(typeService.getTypeCodeFromPropertyQualifier(propertyDescriptor.getQualifier()));
        for(ObjectType type : types)
        {
            if(enclosingType.isAssignableFrom(type))
            {
                return true;
            }
        }
        return false;
    }


    public static Map<PropertyDescriptor, Object> getAllDefaultValues(TypeService typeService, ObjectTemplate template, Set<String> languageIsos)
    {
        String typeCode = template.getBaseType().getCode();
        ComposedType type = TypeManager.getInstance().getComposedType(typeCode);
        Map coreDefaultValues = Collections.EMPTY_MAP;
        SessionContext localCtx = null;
        try
        {
            localCtx = JaloSession.getCurrentSession().createLocalSessionContext();
            localCtx.setLanguage(null);
            localCtx.setAttribute("core.types.creation.initial", Boolean.TRUE);
            coreDefaultValues = type.getAllDefaultValues();
        }
        finally
        {
            if(localCtx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
        Map<PropertyDescriptor, Object> defaultValues = Collections.EMPTY_MAP;
        if(!coreDefaultValues.isEmpty())
        {
            defaultValues = new HashMap<>();
            for(Iterator<Map.Entry> it = coreDefaultValues.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry entry = it.next();
                PropertyDescriptor propertyDescriptor = typeService.getPropertyDescriptor(typeCode + "." + typeCode);
                if(propertyDescriptor != null)
                {
                    Object rawValue = entry.getValue();
                    if(propertyDescriptor.isLocalized())
                    {
                        C2LManager c2l = C2LManager.getInstance();
                        Map<Language, Object> rawLocalizedMap = (Map<Language, Object>)rawValue;
                        Map<String, Object> localizedMap = new HashMap<>(languageIsos.size());
                        for(String langIso : languageIsos)
                        {
                            Language lang = c2l.getLanguageByIsoCode(langIso);
                            Object rawLocalizedValue = (rawLocalizedMap == null) ? null : rawLocalizedMap.get(lang);
                            localizedMap.put(langIso,
                                            item2Container(typeService, getModelService().toModelLayer(rawLocalizedValue)));
                        }
                        if(!isEmptyLocalizedValue(localizedMap))
                        {
                            defaultValues.put(propertyDescriptor, localizedMap);
                        }
                        continue;
                    }
                    if(!isEmptyValue(rawValue))
                    {
                        defaultValues.put(propertyDescriptor,
                                        item2Container(typeService, getModelService().toModelLayer(rawValue)));
                    }
                }
            }
        }
        return defaultValues;
    }


    protected static boolean isEmptyValue(Object value)
    {
        if(value == null)
        {
            return true;
        }
        if(value instanceof Collection)
        {
            return ((Collection)value).isEmpty();
        }
        if(value instanceof Map)
        {
            return ((Map)value).isEmpty();
        }
        return false;
    }


    protected static boolean isEmptyLocalizedValue(Map<String, Object> value)
    {
        if(value == null)
        {
            return true;
        }
        if(value.isEmpty())
        {
            return true;
        }
        boolean empty = true;
        for(Map.Entry<String, Object> entry : value.entrySet())
        {
            if(!isEmptyValue(entry.getValue()))
            {
                empty = false;
                break;
            }
        }
        return empty;
    }


    public static Map<String, Object> getAllAttributes(TypedObject typedObject)
    {
        HashMap<String, Object> result = new HashMap<>();
        Set<PropertyDescriptor> propertyDescriptors = typedObject.getType().getPropertyDescriptors();
        ObjectValueContainer valueContainer = createValueContainer(typedObject, propertyDescriptors,
                        UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos(), false);
        for(ObjectValueContainer.ObjectValueHolder valueHolder : valueContainer.getAllValues())
        {
            PropertyDescriptor propertyDescriptor = valueHolder.getPropertyDescriptor();
            if(propertyDescriptor.isLocalized())
            {
                Map<String, Object> locMap = (Map<String, Object>)result.get(propertyDescriptor.getQualifier());
                if(locMap == null)
                {
                    locMap = new HashMap<>();
                }
                locMap.put(valueHolder.getLanguageIso(), valueHolder.getCurrentValue());
                result.put(propertyDescriptor.getQualifier(), locMap);
                continue;
            }
            result.put(propertyDescriptor.getQualifier(), valueHolder.getCurrentValue());
        }
        return result;
    }


    public static String getEnumName(HybrisEnumValue hybrisEnum)
    {
        EnumerationService enumerationService = getEnumerationService();
        String name = enumerationService.getEnumerationName(hybrisEnum,
                        UISessionUtils.getCurrentSession().getGlobalDataLocale());
        return StringUtils.isEmpty(name) ? hybrisEnum.getCode() : name;
    }


    private static EnumerationService getEnumerationService()
    {
        return (EnumerationService)SpringUtil.getBean("enumerationService");
    }


    public static void filterOutRemovedItems(List<? extends TypedObject> items)
    {
        CollectionUtils.filter(items, (Predicate)new Object());
    }


    public static String getPropertyValueAsString(ValueService valueService, TypedObject object, PropertyDescriptor propertyDescriptor)
    {
        try
        {
            Object value = valueService.getValue(object, propertyDescriptor);
            return (value == null) ? "" : value.toString();
        }
        catch(ValueHandlerPermissionException e)
        {
            return ValueHandler.NOT_READABLE_VALUE.toString();
        }
        catch(ValueHandlerException e)
        {
            return "";
        }
    }


    public static Object getPropertyValue(ValueService valueService, TypedObject object, PropertyDescriptor propertyDescriptor)
    {
        try
        {
            return valueService.getValue(object, propertyDescriptor);
        }
        catch(ValueHandlerPermissionException e)
        {
            return ValueHandler.NOT_READABLE_VALUE;
        }
        catch(ValueHandlerException e)
        {
            return null;
        }
    }


    public static <E> Collection<E> createCollection(Class<? extends Collection> clazz, Collection<E> items)
    {
        try
        {
            Constructor<? extends Collection> constructor = clazz.getConstructor(new Class[] {Collection.class});
            return constructor.newInstance(new Object[] {items});
        }
        catch(Exception e)
        {
            LOG.error("Cannot instantiate " + clazz, e);
            if(Set.class.isAssignableFrom(clazz))
            {
                return new HashSet<>(items);
            }
            return new ArrayList<>(items);
        }
    }
}
