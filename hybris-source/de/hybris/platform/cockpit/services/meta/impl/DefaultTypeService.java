package de.hybris.platform.cockpit.services.meta.impl;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.cockpit.jalo.CockpitManager;
import de.hybris.platform.cockpit.jalo.template.CockpitItemTemplate;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.RedeclarableObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ClassAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ClassificationClassPath;
import de.hybris.platform.cockpit.model.meta.impl.ClassificationType;
import de.hybris.platform.cockpit.model.meta.impl.ItemAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ItemTemplate;
import de.hybris.platform.cockpit.model.meta.impl.ItemType;
import de.hybris.platform.cockpit.model.meta.impl.WidgetParameterPropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.WidgetType;
import de.hybris.platform.cockpit.model.template.CockpitItemTemplateModel;
import de.hybris.platform.cockpit.services.meta.PropertyDescriptorCodeResolver;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.search.impl.ItemAttributeSearchDescriptor;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;

public class DefaultTypeService implements TypeService
{
    Map<String, Set<String>> usedInSelectionOf = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(DefaultTypeService.class);
    private TypeService typeService;
    private ModelService modelService;
    private TypeCache baseTypeCache;
    private PropertyCache baseTypePropCache;
    private TypeCache extTypeCache;
    private TypeCache templTypeCache;
    private MyInvalidationListener myInvalidationListener;


    public boolean checkItemAlive(TypedObject typedItem)
    {
        if(typedItem != null)
        {
            if(typedItem.getObject() == null)
            {
                return false;
            }
            if(typedItem.getObject() instanceof ItemModel)
            {
                ItemModel item = (ItemModel)typedItem.getObject();
                return !getModelService().isRemoved(item);
            }
            return true;
        }
        return false;
    }


    public int countAllInstancesOf(ObjectType type)
    {
        int ret = -1;
        String typeCode = type.getCode();
        String cntQry = "SELECT COUNT({PK}) FROM {" + typeCode + "}";
        try
        {
            List<Integer> cntResult = FlexibleSearch.getInstance().search(cntQry, Collections.EMPTY_MAP, Integer.class).getResult();
            ret = ((Integer)cntResult.iterator().next()).intValue();
        }
        catch(Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return ret;
    }


    public Collection<TypedObject> getAllInstancesOf(ObjectType type)
    {
        String typeCode = type.getCode();
        String query = "SELECT {pk} FROM {" + typeCode + "}";
        List<Item> list = FlexibleSearch.getInstance().search(query, Collections.EMPTY_MAP, Item.class).getResult();
        return wrapItems((Collection)list);
    }


    public Collection<TypedObject> getAllInstancesOf(ObjectType type, String sortBy, String sortOrder)
    {
        if(StringUtils.isBlank(sortBy))
        {
            return getAllInstancesOf(type);
        }
        String typeCode = type.getCode();
        String query = "SELECT {pk} FROM {" + typeCode + "} ORDER BY {" + sortBy + " } " + (StringUtils.isBlank(sortOrder) ? " ASC " : (" " + sortOrder));
        List<Item> list = FlexibleSearch.getInstance().search(query, Collections.EMPTY_MAP, Item.class).getResult();
        return wrapItems((Collection)list);
    }


    public void clear()
    {
        InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.removeInvalidationListener(getItemInvalidationListener());
    }


    public Set<ObjectType> getAllSubtypes(ObjectType type)
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


    public List<ObjectType> getAllSupertypes(ObjectType type)
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
                    ret = new ArrayList<>(10);
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


    public String getAttributeCodeFromPropertyQualifier(String propertyQualifier)
    {
        if(propertyQualifier.startsWith("_widget_"))
        {
            return propertyQualifier.substring(propertyQualifier.indexOf(".") + 1);
        }
        return (propertyQualifier.indexOf('/') == -1) ?
                        propertyQualifier.substring(propertyQualifier.indexOf(".") + 1) :
                        propertyQualifier.substring(propertyQualifier.lastIndexOf(".") + 1);
    }


    public List<Object> getAvailableValues(PropertyDescriptor propertyDescriptor)
    {
        List<Object> ret = new ArrayList();
        TypeModel type = getValueType(propertyDescriptor);
        if(type instanceof CollectionTypeModel)
        {
            type = ((CollectionTypeModel)type).getElementType();
        }
        if(type instanceof EnumerationMetaTypeModel)
        {
            Collection<ItemModel> vals = ((EnumerationMetaTypeModel)type).getValues();
            for(ItemModel enumerationValue : vals)
            {
                if(enumerationValue instanceof EnumerationValueModel)
                {
                    ret.add(this.modelService.get(((EnumerationValueModel)enumerationValue).getPk()));
                    continue;
                }
                ret.add(wrapItem(enumerationValue));
            }
        }
        else if(type != null &&
                        getTypeService().isAssignableFrom((TypeModel)getTypeService().getComposedType(ClassificationAttributeValueModel.class), type))
        {
            SessionContext ctx = null;
            try
            {
                ctx = JaloSession.getCurrentSession().createLocalSessionContext();
                ctx.setAttribute("disableRestrictions", Boolean.TRUE);
                ret.addAll(wrapItems(((ClassAttributePropertyDescriptor)propertyDescriptor).getAttributeAssignment()
                                .getAttributeValues()));
                if(ret.isEmpty())
                {
                    ret.addAll(wrapItems(((ClassAttributePropertyDescriptor)propertyDescriptor).getAttributeAssignment()
                                    .getClassificationAttribute().getDefaultAttributeValues()));
                }
            }
            finally
            {
                if(ctx != null)
                {
                    JaloSession.getCurrentSession().removeLocalSessionContext();
                }
            }
        }
        else if(type instanceof ComposedTypeModel)
        {
            String typeCode = type.getCode();
            Map<Object, Object> values = new HashMap<>();
            String query = "SELECT {pk} FROM {" + typeCode + "}";
            Collection<Item> items = FlexibleSearch.getInstance().search(query, values, Item.class).getResult();
            ret.addAll(wrapItems((Collection)items));
        }
        return ret;
    }


    public List<Object> getAvailableValues(PropertyDescriptor propertyDescriptor, TypedObject object)
    {
        List<Object> ret = new ArrayList();
        TypeModel type = getValueType(propertyDescriptor);
        PropertyDescriptor selectionOf = getSelectionOf(propertyDescriptor);
        if(type instanceof ComposedTypeModel && selectionOf != null)
        {
            String qualifier = getAttributeCodeFromPropertyQualifier(selectionOf.getQualifier());
            Object value = TypeTools.getObjectAttributeValue(object, qualifier, this);
            if(value instanceof Collection)
            {
                ret.addAll((Collection)value);
            }
        }
        else
        {
            ret = getAvailableValues(propertyDescriptor);
        }
        return ret;
    }


    public BaseType getBaseType(String code)
    {
        ItemType itemType;
        BaseType type = getBaseTypeCached(code);
        if(type == null)
        {
            itemType = loadItemType(code);
            addToCache((ObjectType)itemType);
        }
        return (BaseType)itemType;
    }


    public ObjectTemplate getBestTemplate(TypedObject object)
    {
        Collection<ObjectTemplate> templates = object.getAssignedTemplates();
        if(CollectionUtils.isNotEmpty(templates))
        {
            return templates.iterator().next();
        }
        templates = object.getPotentialTemplates();
        if(CollectionUtils.isNotEmpty(templates))
        {
            ObjectTemplate template = templates.iterator().next();
            Collection<? extends ExtendedType> extTypes = template.getExtendedTypes();
            if(extTypes != null && !extTypes.isEmpty())
            {
                return template;
            }
        }
        return getObjectTemplate(object.getType().getCode());
    }


    public ExtendedType getExtendedType(String code)
    {
        ClassificationType classificationType;
        if(code.startsWith("_widget_"))
        {
            return (ExtendedType)loadWidgetType(code);
        }
        ExtendedType type = getExtendedTypeCached(code);
        if(type == null)
        {
            classificationType = loadClassificationType(code);
            addToCache((ObjectType)classificationType);
        }
        return (ExtendedType)classificationType;
    }


    public ObjectTemplate getObjectTemplate(String code)
    {
        ObjectTemplate template = getObjectTemplateCached(code);
        if(template == null)
        {
            template = loadItemTemplate(code);
            addToCache((ObjectType)template);
        }
        return template;
    }


    public List<ObjectTemplate> getObjectTemplates(BaseType type)
    {
        Collection<CockpitItemTemplate> templates = CockpitManager.getInstance().getCockpitItemTemplates((ComposedType)
                        getModelService().getSource(((ItemType)type).getComposedType()));
        if(templates.isEmpty())
        {
            return Collections.singletonList(getObjectTemplate(type.getCode()));
        }
        List<ObjectTemplate> wrapped = new LinkedList<>();
        boolean defaultAdded = false;
        for(CockpitItemTemplate template : templates)
        {
            String templateCode = template.getCode();
            if(templateCode.equalsIgnoreCase(type.getCode()))
            {
                wrapped.add(0, getObjectTemplate(type.getCode()));
                defaultAdded = true;
                continue;
            }
            wrapped.add(getObjectTemplate(type.getCode() + "." + type.getCode()));
        }
        if(!defaultAdded)
        {
            wrapped.add(0, getObjectTemplate(type.getCode()));
        }
        return wrapped;
    }


    public ObjectType getObjectType(String code)
    {
        BaseType baseType;
        if(code.startsWith("_widget_") && !StringUtils.equals(code, "_widget_"))
        {
            ExtendedType extendedType = getExtendedType(code);
        }
        else if(code.indexOf('/') != -1)
        {
            ExtendedType extendedType = getExtendedType(code);
        }
        else if(code.indexOf(".") != -1)
        {
            ObjectTemplate objectTemplate = getObjectTemplate(code);
        }
        else
        {
            baseType = getBaseType(code);
        }
        return (ObjectType)baseType;
    }


    public ObjectType getObjectTypeFromPropertyQualifier(String propertyQualifier)
    {
        return getObjectType(getTypeCodeFromPropertyQualifier(propertyQualifier));
    }


    public PropertyDescriptor getPropertyDescriptor(ObjectType type, String qualifier)
    {
        ItemAttributePropertyDescriptor itemAttributePropertyDescriptor;
        PropertyDescriptor propertyDescriptor = getBaseTypePropertyCached(type.getCode() + type.getCode());
        if(propertyDescriptor == null)
        {
            String normalizedQualifier = getNormalizedQualifier(qualifier);
            if(normalizedQualifier != null)
            {
                propertyDescriptor = getPropertyDescriptorFromNormalizedQualifier(type, normalizedQualifier);
            }
            if(propertyDescriptor == null)
            {
                String attributeQuali = getAttributeCodeFromPropertyQualifier(qualifier);
                if(attributeQuali.indexOf(".") != -1)
                {
                    String typeCode = getTypeCodeFromPropertyQualifier(qualifier);
                    ComposedTypeModel enclosingType = getTypeService().getComposedType(typeCode);
                    List<String> qualis = Arrays.asList(attributeQuali
                                    .split("\\."));
                    List<AttributeDescriptorModel> attributes = new ArrayList<>(qualis.size());
                    ComposedTypeModel composedTypeModel = enclosingType;
                    int index = 0;
                    do
                    {
                        AttributeDescriptorModel attributeDescriptorModel = getTypeService().getAttributeDescriptor(composedTypeModel, qualis.get(index++));
                        attributes.add(attributeDescriptorModel);
                        TypeModel valueType = getValueType(attributeDescriptorModel);
                        composedTypeModel = (valueType instanceof ComposedTypeModel) ? (ComposedTypeModel)valueType : null;
                    }
                    while(index < qualis.size() && composedTypeModel != null);
                    ItemAttributePropertyDescriptor property = new ItemAttributePropertyDescriptor(enclosingType, attributes);
                    property.setEditorType(getDefaultEditorType(property.getLastAttributeDescriptor()));
                    itemAttributePropertyDescriptor = property;
                    if(!attributes.isEmpty())
                    {
                        AttributeDescriptorModel lastAttribute = attributes.get(attributes.size() - 1);
                        addSelectionOf(property, lastAttribute);
                    }
                }
            }
        }
        if(itemAttributePropertyDescriptor instanceof ItemAttributePropertyDescriptor)
        {
            addBaseTypePropertyToCache(type.getCode() + type.getCode(), (PropertyDescriptor)itemAttributePropertyDescriptor);
        }
        return (PropertyDescriptor)itemAttributePropertyDescriptor;
    }


    private String getNormalizedQualifier(String qualifier)
    {
        String attrQuali = getAttributeCodeFromPropertyQualifier(qualifier);
        List<ObjectType> types = new ArrayList<>();
        ObjectType type = getObjectTypeFromPropertyQualifier(qualifier);
        types.add(type);
        types.addAll(getAllSupertypes(type));
        PropertyDescriptor propertyDescriptor = null;
        for(ObjectType t : types)
        {
            String quali = t.getCode() + "." + t.getCode();
            Set<PropertyDescriptor> pds = t.getDeclaredPropertyDescriptors();
            for(PropertyDescriptor pd : pds)
            {
                if(quali.equalsIgnoreCase(pd.getQualifier()))
                {
                    propertyDescriptor = pd;
                    break;
                }
            }
            if(propertyDescriptor != null)
            {
                break;
            }
        }
        return (propertyDescriptor == null) ? null : propertyDescriptor.getQualifier();
    }


    private PropertyDescriptor getPropertyDescriptorFromNormalizedQualifier(ObjectType type, String normalizedQualifier)
    {
        PropertyDescriptor result = getDirectPropertyDescriptorFromNormalizedQualifier(type, normalizedQualifier);
        if(result == null)
        {
            List<ObjectType> supertypes = getAllSupertypes(type);
            Collections.reverse(supertypes);
            for(ObjectType supertype : supertypes)
            {
                result = getDirectPropertyDescriptorFromNormalizedQualifier(supertype, normalizedQualifier);
                if(result != null)
                {
                    break;
                }
            }
        }
        return result;
    }


    private PropertyDescriptor getDirectPropertyDescriptorFromNormalizedQualifier(ObjectType type, String normalizedQualifier)
    {
        PropertyDescriptor result = null;
        for(PropertyDescriptor pd : type.getDeclaredPropertyDescriptors())
        {
            if(normalizedQualifier.equalsIgnoreCase(pd.getQualifier()))
            {
                result = pd;
                break;
            }
        }
        if(result == null && type instanceof RedeclarableObjectType)
        {
            for(PropertyDescriptor pd : ((RedeclarableObjectType)type).getRedeclaredPropertyDescriptors())
            {
                if(normalizedQualifier.equalsIgnoreCase(pd.getQualifier()))
                {
                    result = pd;
                    break;
                }
            }
        }
        return result;
    }


    private TypeModel getValueType(AttributeDescriptorModel attributeDescriptorModel)
    {
        TypeModel type = attributeDescriptorModel.getAttributeType();
        if(type instanceof CollectionTypeModel)
        {
            type = ((CollectionTypeModel)type).getElementType();
        }
        else if(type instanceof MapTypeModel)
        {
            type = ((MapTypeModel)type).getReturntype();
        }
        return type;
    }


    public PropertyDescriptor getPropertyDescriptor(String propertyQualifier)
    {
        return getPropertyDescriptor(getObjectType(getTypeCodeFromPropertyQualifier(propertyQualifier)), propertyQualifier);
    }


    public String getTypeCodeFromPropertyQualifier(String propertyQualifier)
    {
        if(propertyQualifier.startsWith("_widget_"))
        {
            return propertyQualifier;
        }
        return (propertyQualifier.indexOf('/') == -1) ? propertyQualifier.substring(0, propertyQualifier
                        .indexOf(".")) : propertyQualifier.substring(0, propertyQualifier
                        .lastIndexOf("."));
    }


    public String getValueTypeCode(PropertyDescriptor propertyDescriptor)
    {
        String valueTypeCode = null;
        TypeModel type = getValueType(propertyDescriptor);
        if(type instanceof de.hybris.platform.core.model.type.AtomicTypeModel || type instanceof ComposedTypeModel)
        {
            valueTypeCode = type.getCode();
        }
        else if(type instanceof CollectionTypeModel)
        {
            valueTypeCode = ((CollectionTypeModel)type).getElementType().getCode();
        }
        else if(type instanceof MapTypeModel)
        {
            valueTypeCode = ((MapTypeModel)type).getReturntype().getCode();
        }
        return valueTypeCode;
    }


    public void init()
    {
        InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.addInvalidationListener(getItemInvalidationListener());
    }


    public void setBaseTypeCache(TypeCache typeCache)
    {
        this.baseTypeCache = typeCache;
    }


    public void setBaseTypePropertyCache(PropertyCache propCache)
    {
        this.baseTypePropCache = propCache;
    }


    public void setExtendedTypeCache(TypeCache typeCache)
    {
        this.extTypeCache = typeCache;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setObjectTemplateCache(TypeCache typeCache)
    {
        this.templTypeCache = typeCache;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public TypedObject wrapItem(Object itemObject)
    {
        MyTypedObject myTypedObject;
        TypedObject ret = null;
        if(itemObject instanceof TypedObject)
        {
            ret = (TypedObject)itemObject;
        }
        else if(itemObject instanceof ItemModel)
        {
            ItemModel item = (ItemModel)itemObject;
            myTypedObject = new MyTypedObject(this, getBaseType(item.getItemtype()), item);
        }
        else if(itemObject instanceof Item)
        {
            ItemModel item = (ItemModel)getModelService().get(itemObject);
            myTypedObject = new MyTypedObject(this, getBaseType(item.getItemtype()), item);
        }
        else if(itemObject instanceof PK)
        {
            PK itemPk = (PK)itemObject;
            ItemModel item = (ItemModel)getModelService().get(itemPk);
            myTypedObject = new MyTypedObject(this, getBaseType(item.getItemtype()), item);
        }
        return (TypedObject)myTypedObject;
    }


    public List<TypedObject> wrapItems(Collection<? extends Object> itemObjects)
    {
        List<TypedObject> ret = null;
        if(itemObjects == null || itemObjects.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        ret = new ArrayList<>(itemObjects.size());
        for(Object itemObject : itemObjects)
        {
            ret.add(wrapItem(itemObject));
        }
        return ret;
    }


    public List<ItemModel> unwrapItems(Collection<TypedObject> typedObjects)
    {
        List<ItemModel> models = null;
        if(typedObjects == null || typedObjects.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        models = new ArrayList<>(typedObjects.size());
        for(TypedObject typed : typedObjects)
        {
            try
            {
                models.add((ItemModel)typed.getObject());
            }
            catch(ClassCastException cce)
            {
                log.error("Passed typed object " + typed + " does not contain instance of type ItemModel");
            }
        }
        return models;
    }


    protected void addToCache(ObjectType type)
    {
        if(type instanceof BaseType && this.baseTypeCache != null)
        {
            this.baseTypeCache.addType(type.getCode(), type);
        }
        else if(type instanceof ExtendedType && this.extTypeCache != null)
        {
            this.extTypeCache.addType(type.getCode(), type);
        }
        else if(type instanceof ObjectTemplate && this.templTypeCache != null)
        {
            this.templTypeCache.addType(type.getCode(), type);
        }
    }


    protected TypeCache getBaseTypeCache()
    {
        return this.baseTypeCache;
    }


    protected BaseType getBaseTypeCached(String code)
    {
        if(this.baseTypeCache == null)
        {
            return null;
        }
        return (BaseType)this.baseTypeCache.getType(code);
    }


    protected void addBaseTypePropertyToCache(String qualifier, PropertyDescriptor propDesc)
    {
        if(this.baseTypePropCache != null)
        {
            this.baseTypePropCache.addProperty(qualifier, propDesc);
        }
    }


    protected PropertyCache getBaseTypePropertyCache()
    {
        return this.baseTypePropCache;
    }


    protected PropertyDescriptor getBaseTypePropertyCached(String qualifier)
    {
        if(this.baseTypePropCache == null)
        {
            return null;
        }
        return this.baseTypePropCache.getProperty(qualifier);
    }


    protected String getDefaultEditorType(AttributeDescriptorModel attributeDescriptorModel)
    {
        return getDefaultEditorType(attributeDescriptorModel.getAttributeType(), TypeTools.primitiveValue(attributeDescriptorModel.getLocalized()));
    }


    protected String getDefaultEditorType(TypeModel type, boolean localized)
    {
        TypeModel basicType = null;
        if(localized && type instanceof MapTypeModel)
        {
            type = ((MapTypeModel)type).getReturntype();
        }
        if(type instanceof de.hybris.platform.core.model.type.AtomicTypeModel || type instanceof ComposedTypeModel)
        {
            basicType = type;
        }
        else if(type instanceof CollectionTypeModel)
        {
            basicType = ((CollectionTypeModel)type).getElementType();
        }
        else if(type instanceof MapTypeModel)
        {
            basicType = ((MapTypeModel)type).getReturntype();
        }
        String returnType = "DUMMY";
        if(basicType instanceof de.hybris.platform.core.model.type.AtomicTypeModel)
        {
            String code = basicType.getCode();
            if("java.lang.String".equals(code))
            {
                returnType = "TEXT";
            }
            else if("java.lang.Integer".equals(code))
            {
                returnType = "INTEGER";
            }
            else if("java.lang.Long".equals(code))
            {
                returnType = "LONG";
            }
            else if("de.hybris.platform.core.PK".equals(code))
            {
                returnType = "PK";
            }
            else if("java.lang.Double".equals(code) || "java.lang.Float".equals(code))
            {
                returnType = "DECIMAL";
            }
            else if("java.lang.Boolean".equals(code))
            {
                returnType = "BOOLEAN";
            }
            else if("java.util.Date".equals(code))
            {
                returnType = "DATE";
            }
        }
        else if(basicType instanceof EnumerationMetaTypeModel)
        {
            returnType = "ENUM";
        }
        else if(basicType instanceof ComposedTypeModel)
        {
            returnType = "REFERENCE";
        }
        return returnType;
    }


    protected String getDefaultEditorType(ClassAttributeAssignment caa)
    {
        return "FEATURE";
    }


    protected TypeCache getExtendedTypeCache()
    {
        return this.extTypeCache;
    }


    protected ExtendedType getExtendedTypeCached(String code)
    {
        if(this.extTypeCache == null)
        {
            return null;
        }
        return (ExtendedType)this.extTypeCache.getType(code);
    }


    protected InvalidationListener getItemInvalidationListener()
    {
        if(this.myInvalidationListener == null)
        {
            this.myInvalidationListener = new MyInvalidationListener(this);
        }
        return (InvalidationListener)this.myInvalidationListener;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected TypeCache getObjectTemplateCache()
    {
        return this.templTypeCache;
    }


    protected ObjectTemplate getObjectTemplateCached(String code)
    {
        if(this.templTypeCache == null)
        {
            return null;
        }
        return (ObjectTemplate)this.templTypeCache.getType(code);
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    protected TypeModel getValueType(PropertyDescriptor propertyDescriptor)
    {
        TypeModel valueType = null;
        String typeCode = getTypeCodeFromPropertyQualifier(propertyDescriptor.getQualifier());
        ObjectType type = getObjectType(typeCode);
        if(type instanceof ItemType)
        {
            AttributeDescriptorModel attrDescr = null;
            if(propertyDescriptor instanceof ItemAttributePropertyDescriptor)
            {
                attrDescr = ((ItemAttributePropertyDescriptor)propertyDescriptor).getLastAttributeDescriptor();
            }
            else if(propertyDescriptor instanceof ItemAttributeSearchDescriptor)
            {
                attrDescr = ((ItemAttributeSearchDescriptor)propertyDescriptor).getLastAttributeDescriptor();
            }
            if(attrDescr != null)
            {
                valueType = attrDescr.getAttributeType();
                if(TypeTools.primitiveValue(attrDescr.getLocalized()) && valueType instanceof MapTypeModel)
                {
                    valueType = ((MapTypeModel)valueType).getReturntype();
                }
            }
        }
        else if(type instanceof ClassificationType)
        {
            if(propertyDescriptor instanceof ClassAttributePropertyDescriptor)
            {
                ClassAttributeAssignment caa = ((ClassAttributePropertyDescriptor)propertyDescriptor).getAttributeAssignment();
                if(caa != null)
                {
                    EnumerationValue caaType = caa.getAttributeType();
                    if("enum".equalsIgnoreCase(caaType.getCode()))
                    {
                        ComposedTypeModel composedTypeModel = getTypeService().getComposedType(ClassificationAttributeValueModel.class);
                    }
                }
            }
        }
        else if(type instanceof WidgetType)
        {
            if(propertyDescriptor instanceof WidgetParameterPropertyDescriptor)
            {
                valueType = ((WidgetParameterPropertyDescriptor)propertyDescriptor).getWidgetParameter().getType();
                if(propertyDescriptor.isLocalized() && valueType instanceof MapTypeModel)
                {
                    valueType = ((MapTypeModel)valueType).getReturntype();
                }
            }
        }
        else
        {
            log.warn("Could not get value type since type is not an item/class. class or property/ not an item attribute/class. attribute.");
        }
        return valueType;
    }


    protected ClassificationType loadClassificationType(String code)
    {
        MyClassificationType myClassificationType = new MyClassificationType(this, code);
        ClassificationClass classificationClass = myClassificationType.getClassificationClass();
        Set<PropertyDescriptor> descriptors = new HashSet<>();
        SessionContext ctx = null;
        try
        {
            ctx = JaloSession.getCurrentSession().createLocalSessionContext();
            ctx.setAttribute("disableRestrictions", Boolean.TRUE);
            for(ClassAttributeAssignment caa : classificationClass.getDeclaredClassificationAttributeAssignments())
            {
                String propertyPath = resolveClassificationPropertyPath(code, caa.getClassificationAttribute().getCode());
                ClassAttributePropertyDescriptor property = new ClassAttributePropertyDescriptor(propertyPath);
                property.setEditorType(getDefaultEditorType(caa));
                descriptors.add(property);
            }
        }
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
        myClassificationType.setDeclaredPropertyDescriptors(descriptors);
        return (ClassificationType)myClassificationType;
    }


    protected String resolveClassificationPropertyPath(String code, String classificationAttributeCode)
    {
        try
        {
            PropertyDescriptorCodeResolver resolver = getPropertyDescriptorCodeResolver();
            return resolver.resolveClassificationPropertyCode(code, classificationAttributeCode);
        }
        catch(Exception e)
        {
            String classCodeWithEscapedDots = ClassAttributePropertyDescriptor.escapeDots(code);
            String propertyCodeWithEscapedDots = ClassAttributePropertyDescriptor.escapeDots(classificationAttributeCode);
            return classCodeWithEscapedDots + "." + classCodeWithEscapedDots;
        }
    }


    protected WidgetType loadWidgetType(String code)
    {
        ComposedTypeModel composedTypeModel = getTypeService().getComposedType(code
                        .substring("_widget_".length(), code
                                        .indexOf(".")));
        String pkString = code.substring(code.indexOf(".") + 1);
        return (WidgetType)new Object(this, composedTypeModel, pkString, composedTypeModel);
    }


    protected ObjectTemplate loadItemTemplate(String code)
    {
        int delimPosition = code.lastIndexOf(".");
        boolean typeOnly = (delimPosition == -1);
        if(!typeOnly)
        {
            Collection<ClassificationType> extTypes;
            String str1 = code.substring(0, delimPosition);
            String templateCode = code.substring(delimPosition + 1);
            ItemType itemType = (ItemType)getBaseType(str1);
            CockpitItemTemplate itemTemplate = CockpitManager.getInstance().getCockpitItemTemplate((ComposedType)
                            getModelService().getSource(itemType.getComposedType()), templateCode);
            if(itemTemplate == null)
            {
                throw new NullPointerException("No CockpitItemTemplate found for type " + itemType.getCode() + " and code " + templateCode);
            }
            SessionContext ctx = null;
            try
            {
                ctx = JaloSession.getCurrentSession().createLocalSessionContext();
                ctx.setAttribute("disableRestrictions", Boolean.TRUE);
                Collection<ClassificationClass> classes = itemTemplate.getClassificationClasses();
                if(classes.isEmpty())
                {
                    extTypes = Collections.EMPTY_LIST;
                }
                else
                {
                    extTypes = new ArrayList<>(classes.size());
                    for(ClassificationClass cc : classes)
                    {
                        extTypes.add((ClassificationType)getExtendedType(ClassificationClassPath.getClassCode(cc)));
                    }
                }
            }
            finally
            {
                if(ctx != null)
                {
                    JaloSession.getCurrentSession().removeLocalSessionContext();
                }
            }
            return (ObjectTemplate)new ItemTemplate((CockpitItemTemplateModel)getModelService().get(itemTemplate), itemType, extTypes, code);
        }
        String typeCode = code;
        ItemType type = (ItemType)getBaseType(typeCode);
        return (ObjectTemplate)new ItemTemplate(null, type, Collections.EMPTY_LIST, code);
    }


    protected ItemType loadItemType(String code)
    {
        ComposedTypeModel composedTypeModel = getTypeService().getComposedType(code);
        MyItemType myItemType = new MyItemType(this, composedTypeModel);
        if(composedTypeModel == null)
        {
            throw new IllegalArgumentException("Could not load item type (Reason: No composed type found for item with code '" + code + "').");
        }
        Set<PropertyDescriptor> descriptors = new HashSet<>();
        Set<PropertyDescriptor> redeclared = new HashSet<>();
        for(AttributeDescriptorModel ad : getTypeService().getAttributeDescriptors(composedTypeModel))
        {
            if((TypeTools.primitiveValue(ad.getModifiers()) & 0x400) == 0)
            {
                ItemAttributePropertyDescriptor property = new ItemAttributePropertyDescriptor(composedTypeModel, Collections.singletonList(ad));
                property.setEditorType(getDefaultEditorType(ad));
                descriptors.add(property);
                addSelectionOf(property, ad);
                continue;
            }
            if(isRedeclared(ad))
            {
                ItemAttributePropertyDescriptor property = new ItemAttributePropertyDescriptor(composedTypeModel, getDeclaringEnclosingType(composedTypeModel, ad), Collections.singletonList(ad));
                property.setEditorType(getDefaultEditorType(ad));
                redeclared.add(property);
                addSelectionOf(property, ad);
            }
        }
        myItemType.setDeclaredPropertyDescriptors(descriptors);
        myItemType.setRedeclaredPropertyDescriptors(redeclared);
        return (ItemType)myItemType;
    }


    private boolean isRedeclared(AttributeDescriptorModel adModel)
    {
        AttributeDescriptor attributeDescriptorModel = (AttributeDescriptor)getModelService().getSource(adModel);
        boolean redeclared = attributeDescriptorModel.isRedeclared();
        if(!redeclared)
        {
            AttributeDescriptorModel superADModel = getSuperAttributeDescriptor(adModel);
            if(superADModel != null)
            {
                AttributeDescriptor superAD = (AttributeDescriptor)getModelService().getSource(superADModel);
                if(!attributeDescriptorModel.getAllNames().equals(superAD.getAllNames()))
                {
                    redeclared = true;
                }
                if(!redeclared && !attributeDescriptorModel.getAllDescriptions().equals(superAD.getAllDescriptions()))
                {
                    redeclared = true;
                }
                if(!redeclared)
                {
                    redeclared = ObjectUtils.equals(attributeDescriptorModel.getDefaultValue(), superAD.getDefaultValue());
                }
            }
        }
        return redeclared;
    }


    private AttributeDescriptorModel getSuperAttributeDescriptor(AttributeDescriptorModel adModel)
    {
        ComposedTypeModel superEnclosingType = adModel.getEnclosingType().getSuperType();
        try
        {
            return (superEnclosingType == null) ? null : getTypeService().getAttributeDescriptor(superEnclosingType, adModel
                            .getQualifier());
        }
        catch(UnknownIdentifierException e)
        {
            return null;
        }
    }


    private ComposedTypeModel getDeclaringEnclosingType(ComposedTypeModel enclosingType, AttributeDescriptorModel adModel)
    {
        AttributeDescriptor attributeDescriptorModel = (AttributeDescriptor)getModelService().getSource(adModel);
        return (ComposedTypeModel)getModelService().get(attributeDescriptorModel.getDeclaringEnclosingType());
    }


    private void addSelectionOf(ItemAttributePropertyDescriptor property, AttributeDescriptorModel adm)
    {
        AttributeDescriptor attdesc = (AttributeDescriptor)getModelService().getSource(adm);
        AttributeDescriptor selectionOf = attdesc.getSelectionOf();
        if(selectionOf != null)
        {
            String keyString, valueString;
            Set<String> value;
            int posLastDelimiter = property.getQualifier().lastIndexOf(".");
            if(posLastDelimiter == -1)
            {
                keyString = property.getTypeCode() + "." + property.getTypeCode();
                valueString = property.getTypeCode() + "." + property.getTypeCode();
            }
            else
            {
                keyString = property.getQualifier().substring(0, posLastDelimiter) + "." + property.getQualifier().substring(0, posLastDelimiter);
                valueString = property.getQualifier().substring(0, posLastDelimiter) + "." + property.getQualifier().substring(0, posLastDelimiter);
            }
            property.setSelectionOf(keyString);
            if(this.usedInSelectionOf.containsKey(keyString) && this.usedInSelectionOf.get(keyString) != null)
            {
                value = this.usedInSelectionOf.get(keyString);
            }
            else
            {
                value = new HashSet();
            }
            value.add(valueString);
            this.usedInSelectionOf.put(keyString, value);
        }
    }


    public PropertyDescriptor getSelectionOf(PropertyDescriptor descriptor)
    {
        if(descriptor.getSelectionOf() != null && descriptor.getSelectionOf().length() > 0)
        {
            return getPropertyDescriptor(descriptor.getSelectionOf());
        }
        return null;
    }


    public Collection<PropertyDescriptor> getReverseSelectionOf(PropertyDescriptor descriptor)
    {
        Collection<PropertyDescriptor> ret = Collections.EMPTY_LIST;
        Set<String> selections = this.usedInSelectionOf.get(descriptor.getQualifier());
        if(selections != null && !selections.isEmpty())
        {
            ret = new ArrayList();
            for(String selection : selections)
            {
                ret.add(getPropertyDescriptor(selection));
            }
        }
        return ret;
    }


    protected PropertyDescriptorCodeResolver getPropertyDescriptorCodeResolver()
    {
        return (PropertyDescriptorCodeResolver)SpringUtil.getBean("classificationPropertyPathResolver");
    }
}
