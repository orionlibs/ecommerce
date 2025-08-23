package de.hybris.platform.servicelayer.type.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.internal.converter.ConverterRegistry;
import de.hybris.platform.servicelayer.internal.converter.ModelConverter;
import de.hybris.platform.servicelayer.internal.converter.impl.EnumValueModelConverter;
import de.hybris.platform.servicelayer.internal.converter.impl.ItemModelConverter;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.servicelayer.type.AttributeModifierCriteria;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.type.daos.TypeDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTypeService extends AbstractBusinessService implements TypeService
{
    private ConverterRegistry converterRegistry;
    private EnumerationManager enumerationManager;
    private TypeDao typeDao;


    @Deprecated(since = "ages", forRemoval = true)
    public AtomicTypeModel getAtomicType(String typeCode)
    {
        return getAtomicTypeForCode(typeCode);
    }


    public AtomicTypeModel getAtomicTypeForCode(String code)
    {
        ServicesUtil.validateParameterNotNull(code, "Parameter 'code' must not be null!");
        return getTypeDao().findAtomicTypeByCode(code);
    }


    public AtomicTypeModel getAtomicTypeForJavaClass(Class javaClass)
    {
        ServicesUtil.validateParameterNotNull(javaClass, "Parameter 'javaClass' must not be null!");
        return getTypeDao().findAtomicTypeByJavaClass(javaClass);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public ComposedTypeModel getComposedType(String typeCode)
    {
        return getComposedTypeForCode(typeCode);
    }


    public ComposedTypeModel getComposedTypeForCode(String code)
    {
        ServicesUtil.validateParameterNotNull(code, "Parameter 'code' must not be null!");
        return getTypeDao().findComposedTypeByCode(code);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public ComposedTypeModel getComposedType(Class modelClass)
    {
        return getComposedTypeForClass(modelClass);
    }


    public ComposedTypeModel getComposedTypeForClass(Class modelClass)
    {
        ServicesUtil.validateParameterNotNull(modelClass, "Parameter 'modelClass' must not be null!");
        String type = getModelService().getModelType(modelClass);
        if(type == null)
        {
            throw new UnknownIdentifierException("No type mapped to model class " + modelClass);
        }
        return getComposedTypeForCode(type);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public TypeModel getType(String typeCode)
    {
        return getTypeForCode(typeCode);
    }


    public TypeModel getTypeForCode(String code)
    {
        ServicesUtil.validateParameterNotNull(code, "Parameter 'code' must not be null!");
        return getTypeDao().findTypeByCode(code);
    }


    public AttributeDescriptorModel getAttributeDescriptor(ComposedTypeModel composedType, String qualifier)
    {
        ServicesUtil.validateParameterNotNull(composedType, "Parameter 'composedType' must not be null!");
        return getAttributeDescriptor(composedType.getCode(), qualifier);
    }


    public AttributeDescriptorModel getAttributeDescriptor(String composedType, String qualifier)
    {
        ServicesUtil.validateParameterNotNull(composedType, "Parameter 'composedType' must not be null!");
        ServicesUtil.validateParameterNotNull(qualifier, "Parameter 'qualifier' must not be null!");
        ComposedTypeModel composedTypeModel = getComposedTypeForCode(composedType);
        if(composedTypeModel == null)
        {
            throw new UnknownIdentifierException("No composed type with code " + composedType + " found.");
        }
        for(AttributeDescriptorModel attributeDescriptor : composedTypeModel.getDeclaredattributedescriptors())
        {
            if(qualifier.equalsIgnoreCase(attributeDescriptor.getQualifier()))
            {
                return attributeDescriptor;
            }
        }
        for(AttributeDescriptorModel attributeDescriptor : composedTypeModel.getInheritedattributedescriptors())
        {
            if(qualifier.equalsIgnoreCase(attributeDescriptor.getQualifier()))
            {
                return attributeDescriptor;
            }
        }
        throw new UnknownIdentifierException("No attribute with qualifier " + qualifier + " found.");
    }


    @Deprecated(since = "ages", forRemoval = true)
    public Set<AttributeDescriptorModel> getAttributeDescriptors(ComposedTypeModel composedType)
    {
        ServicesUtil.validateParameterNotNull(composedType, "Parameter 'composedTypeCode' must not be null!");
        try
        {
            ComposedType comptype = (ComposedType)getModelService().getSource(composedType);
            Set<AttributeDescriptor> attrdesc = comptype.getAttributeDescriptors();
            if(attrdesc.isEmpty())
            {
                return Collections.EMPTY_SET;
            }
            return
                            (Set<AttributeDescriptorModel>)ImmutableSet.copyOf(getModelService().getAll(attrdesc, new HashSet(attrdesc.size())));
        }
        catch(JaloItemNotFoundException e)
        {
            throw new UnknownIdentifierException("No composed type'" + composedType.getCode() + "' exists!");
        }
    }


    public Set<AttributeDescriptorModel> getAttributeDescriptorsForType(ComposedTypeModel composedType)
    {
        ServicesUtil.validateParameterNotNull(composedType, "Parameter 'composedType' must not be null!");
        Collection<AttributeDescriptorModel> declaredDescriptors = composedType.getDeclaredattributedescriptors();
        Collection<AttributeDescriptorModel> inheritedDescriptors = composedType.getInheritedattributedescriptors();
        ImmutableSet.Builder<AttributeDescriptorModel> descriptors = new ImmutableSet.Builder();
        descriptors.addAll(declaredDescriptors);
        descriptors.addAll(inheritedDescriptors);
        return (Set<AttributeDescriptorModel>)descriptors.build();
    }


    public Collection<AttributeDescriptorModel> getRuntimeAttributeDescriptorsForType(ComposedTypeModel composedTypeModel)
    {
        ServicesUtil.validateParameterNotNull(composedTypeModel, "Parameter 'composedTypeModel' must not be null!");
        ComposedType composedType = (ComposedType)getModelService().getSource(composedTypeModel);
        return (Collection<AttributeDescriptorModel>)composedType.getAttributeDescriptorsIncludingPrivate().stream().filter(AttributeDescriptor::isRuntime)
                        .map(d -> (AttributeDescriptorModel)getModelService().get(d)).collect(ImmutableList.toImmutableList());
    }


    public Boolean isRuntimeAttribute(AttributeDescriptorModel attributeDescriptorModel)
    {
        ServicesUtil.validateParameterNotNull(attributeDescriptorModel, "Parameter 'attributeDescriptorModel' must not be null!");
        ComposedType composedType = (ComposedType)getModelService().getSource(attributeDescriptorModel.getEnclosingType());
        AttributeDescriptor attributeDescriptor = composedType.getAttributeDescriptorIncludingPrivate(attributeDescriptorModel.getQualifier());
        return attributeDescriptor.isRuntime();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public Set<AttributeDescriptorModel> getAttributeDescriptors(String composedTypeCode)
    {
        ServicesUtil.validateParameterNotNull(composedTypeCode, "Parameter 'composedTypeCode' must not be null!");
        return getAttributeDescriptorsForType(getTypeDao().findComposedTypeByCode(composedTypeCode));
    }


    public Set<AttributeDescriptorModel> getInitialAttributeDescriptorsForType(ComposedTypeModel composedTypeModel)
    {
        ServicesUtil.validateParameterNotNull(composedTypeModel, "Parameter 'composedType' must not be null!");
        ImmutableSet.Builder<AttributeDescriptorModel> initialDescriptors = new ImmutableSet.Builder();
        buildInitialAttributesResult(initialDescriptors, composedTypeModel.getDeclaredattributedescriptors());
        buildInitialAttributesResult(initialDescriptors, composedTypeModel.getInheritedattributedescriptors());
        return (Set<AttributeDescriptorModel>)initialDescriptors.build();
    }


    private void buildInitialAttributesResult(ImmutableSet.Builder<AttributeDescriptorModel> initialDescriptors, Collection<AttributeDescriptorModel> declaredDescriptors)
    {
        for(AttributeDescriptorModel attribute : declaredDescriptors)
        {
            if(Boolean.TRUE.equals(attribute.getInitial()))
            {
                initialDescriptors.add(attribute);
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = true)
    public EnumerationMetaTypeModel getEnumerationType(String code)
    {
        ServicesUtil.validateParameterNotNull(code, "Parameter 'code' must not be null!");
        try
        {
            return (EnumerationMetaTypeModel)getModelService().get(EnumerationManager.getInstance().getEnumerationType(code));
        }
        catch(JaloItemNotFoundException e)
        {
            throw new UnknownIdentifierException("No enumeration type'" + code + "' exists!");
        }
    }


    public EnumerationMetaTypeModel getEnumerationTypeForCode(String code)
    {
        ServicesUtil.validateParameterNotNull(code, "Parameter 'code' must not be null!");
        try
        {
            return (EnumerationMetaTypeModel)getModelService().get(getEnumerationManager().getEnumerationType(code));
        }
        catch(JaloItemNotFoundException e)
        {
            throw new UnknownIdentifierException("No enumeration type'" + code + "' exists!");
        }
    }


    public EnumerationValueModel getEnumerationValue(String type, String code)
    {
        ServicesUtil.validateParameterNotNull(type, "Parameter 'type' must not be null!");
        ServicesUtil.validateParameterNotNull(code, "Parameter 'code' must not be null!");
        try
        {
            return (EnumerationValueModel)getModelService().get(getEnumerationManager().getEnumerationValue(type, code), "EnumerationValue");
        }
        catch(JaloItemNotFoundException e)
        {
            throw new UnknownIdentifierException("No enumeration value '" + type + "." + code + "' exists!");
        }
    }


    public EnumerationValueModel getEnumerationValue(HybrisEnumValue plainEnum)
    {
        ServicesUtil.validateParameterNotNull(plainEnum, "Parameter 'plainEnum' must not be null!");
        EnumerationValue item = (EnumerationValue)getModelService().getSource(plainEnum);
        return (EnumerationValueModel)getModelService().get(item, "EnumerationValue");
    }


    public Set<String> getMandatoryAttributes(String type, boolean forCreation)
    {
        ModelConverter conv = this.converterRegistry.getModelConverterBySourceType(type);
        if(conv instanceof ItemModelConverter)
        {
            if(forCreation)
            {
                return ((ItemModelConverter)conv).getMandatoryAttributesForCreation();
            }
            return ((ItemModelConverter)conv).getMandatoryAttributes();
        }
        try
        {
            int excludeFlags = 65544;
            int includeFlags = 0x2 | (forCreation ? 2048 : 0);
            Set<AttributeDescriptorModel> allAttributeDescriptors = getAttributeDescriptorsForType(getComposedTypeForCode(type));
            ImmutableSet.Builder<String> ret = null;
            for(AttributeDescriptorModel ad : allAttributeDescriptors)
            {
                int mod = (ad.getModifiers() == null) ? 0 : ad.getModifiers().intValue();
                if((mod & 0x10008) == 0 && (mod & includeFlags) != 0)
                {
                    if(ret == null)
                    {
                        ret = new ImmutableSet.Builder();
                    }
                    ret.add(ad.getQualifier());
                }
            }
            return (ret == null) ? Collections.EMPTY_SET : (Set<String>)ret.build();
        }
        catch(UnknownIdentifierException e)
        {
            throw new IllegalArgumentException("unknown type " + type);
        }
    }


    public Set<String> getUniqueAttributes(String type)
    {
        ServicesUtil.validateParameterNotNull(type, "Parameter 'type' must not be null!");
        ModelConverter conv = this.converterRegistry.getModelConverterBySourceType(type);
        if(conv instanceof ItemModelConverter)
        {
            return ((ItemModelConverter)conv).getUniqueAttributes();
        }
        ImmutableSet.Builder<String> ret = null;
        try
        {
            Set<AttributeDescriptorModel> allAttributes = getAttributeDescriptorsForType(getComposedTypeForCode(type));
            for(AttributeDescriptorModel ad : allAttributes)
            {
                if(Boolean.TRUE.equals(ad.getUnique()))
                {
                    if(ret == null)
                    {
                        ret = new ImmutableSet.Builder();
                    }
                    ret.add(ad.getQualifier());
                }
            }
        }
        catch(UnknownIdentifierException e)
        {
            throw new IllegalArgumentException("unknown type " + type);
        }
        return (ret == null) ? Collections.EMPTY_SET : (Set<String>)ret.build();
    }


    public String getUniqueModelRootType(String composedTypeCode)
    {
        ComposedTypeModel compTypeModel;
        Set<String> uniqueAttrQual = getUniqueAttributes(composedTypeCode);
        if(uniqueAttrQual.isEmpty())
        {
            return null;
        }
        try
        {
            compTypeModel = getComposedTypeForCode(composedTypeCode);
        }
        catch(UnknownIdentifierException e)
        {
            throw new IllegalArgumentException("unknown composedtype: " + composedTypeCode);
        }
        String returnval = composedTypeCode;
        for(ComposedTypeModel superct = compTypeModel.getSuperType(); superct != null; )
        {
            if(uniqueAttrQual.equals(getUniqueAttributes(superct.getCode())))
            {
                returnval = superct.getCode();
                superct = superct.getSuperType();
            }
        }
        return returnval;
    }


    public Map<String, Object> getDefaultValues(String type)
    {
        try
        {
            ComposedType comptype = (ComposedType)getModelService().getSource(getTypeDao().findComposedTypeByCode(type));
            Set<Map.Entry<String, Object>> defaulValueKeys = comptype.getAllDefaultValues().entrySet();
            ImmutableMap.Builder<String, Object> ret = null;
            for(Map.Entry<String, Object> e : defaulValueKeys)
            {
                if(ret == null)
                {
                    ret = new ImmutableMap.Builder();
                }
                ret.put(e.getKey(), getModelService().toModelLayer(e.getValue()));
            }
            return (ret == null) ? Collections.EMPTY_MAP : (Map<String, Object>)ret.build();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new IllegalArgumentException("unknown type " + type);
        }
    }


    public Map<String, Object> getDefaultValues(String type, Collection<String> attributes)
    {
        ServicesUtil.validateParameterNotNull(type, "type was null");
        ServicesUtil.validateParameterNotNull(attributes, "attributes were null");
        if(attributes.isEmpty())
        {
            return Collections.EMPTY_MAP;
        }
        try
        {
            ComposedType comptype = (ComposedType)getModelService().getSource(getTypeDao().findComposedTypeByCode(type));
            Set<Map.Entry> defaultValues = comptype.getDefaultValues(Sets.newHashSet(attributes)).entrySet();
            ImmutableMap.Builder<String, Object> ret = null;
            for(Map.Entry e : defaultValues)
            {
                if(ret == null)
                {
                    ret = new ImmutableMap.Builder();
                }
                ret.put(e.getKey(), getModelService().toModelLayer(e.getValue()));
            }
            return (ret == null) ? Collections.EMPTY_MAP : (Map<String, Object>)ret.build();
        }
        catch(JaloItemNotFoundException e)
        {
            throw new IllegalArgumentException("unknown type " + type);
        }
    }


    public boolean isAssignableFrom(TypeModel target, TypeModel source)
    {
        if(target == null || source == null)
        {
            return false;
        }
        Type targettype = (Type)getModelService().getSource(target);
        Type sourcetype = (Type)getModelService().getSource(source);
        return targettype.isAssignableFrom(sourcetype);
    }


    public boolean isInstance(TypeModel typemodel, Object obj)
    {
        if(typemodel == null)
        {
            return false;
        }
        Type type = (Type)getModelService().getSource(typemodel);
        Item item = null;
        if(obj instanceof Item)
        {
            item = (Item)obj;
        }
        else if(obj instanceof de.hybris.platform.core.model.ItemModel)
        {
            item = (Item)getModelService().getSource(obj);
        }
        return (item != null && type.isAssignableFrom((Type)item.getComposedType()));
    }


    public boolean isAssignableFrom(String superModelTypeCode, String subModelTypeCode)
    {
        if(superModelTypeCode == null || subModelTypeCode == null)
        {
            return false;
        }
        TypeModel targetType = getTypeDao().findTypeByCode(superModelTypeCode);
        TypeModel sourceType = getTypeDao().findTypeByCode(subModelTypeCode);
        return isAssignableFrom(targetType, sourceType);
    }


    public Set<String> getPartOfAttributes(String composedTypeCode)
    {
        ImmutableSet.Builder<String> ret = null;
        Collection<AttributeDescriptorModel> allDescriptors = getAttributeDescriptors(composedTypeCode);
        for(AttributeDescriptorModel adm : allDescriptors)
        {
            if(Boolean.TRUE.equals(adm.getPartOf()))
            {
                if(ret == null)
                {
                    ret = new ImmutableSet.Builder();
                }
                ret.add(adm.getQualifier());
            }
        }
        return (ret == null) ? Collections.<String>emptySet() : (Set<String>)ret.build();
    }


    @Required
    public void setConverterRegistry(ConverterRegistry converterRegistry)
    {
        this.converterRegistry = converterRegistry;
    }


    public TypeDao getTypeDao()
    {
        return this.typeDao;
    }


    public void setTypeDao(TypeDao typeDao)
    {
        this.typeDao = typeDao;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public EnumerationManager getEnumerationManager()
    {
        if(this.enumerationManager == null)
        {
            this.enumerationManager = EnumerationManager.getInstance();
        }
        return this.enumerationManager;
    }


    public void setEnumerationManager(EnumerationManager enumerationManager)
    {
        this.enumerationManager = enumerationManager;
    }


    public boolean hasAttribute(ComposedTypeModel type, String qualifier)
    {
        ServicesUtil.validateParameterNotNull(type, "Parameter 'type' cannot be null.");
        String itemType = type.getCode();
        ItemModelConverter conv = (ItemModelConverter)this.converterRegistry.getModelConverterBySourceType(itemType);
        return (conv.getInfo(qualifier) != null);
    }


    public <T extends de.hybris.platform.core.model.ItemModel> Class<T> getModelClass(ComposedTypeModel type)
    {
        ServicesUtil.validateParameterNotNull(type, "Parameter 'type' cannot be null.");
        String itemType = type.getCode();
        return getModelClass(itemType);
    }


    public <T extends de.hybris.platform.core.model.ItemModel> Class<T> getModelClass(String typeCode)
    {
        ServicesUtil.validateParameterNotNull(typeCode, "Parameter 'typeCode' cannot be null.");
        ModelConverter conv = this.converterRegistry.getModelConverterBySourceType(typeCode);
        if(conv instanceof ItemModelConverter)
        {
            return ((ItemModelConverter)conv).getModelClass();
        }
        if(conv instanceof EnumValueModelConverter)
        {
            return ((EnumValueModelConverter)conv).getEnumClass();
        }
        return null;
    }


    public Set<AttributeDescriptorModel> getAttributesForModifiers(String composedTypeCode, AttributeModifierCriteria criteria)
    {
        ServicesUtil.validateParameterNotNull(criteria, "Parameter 'criteria' cannot be null.");
        ServicesUtil.validateParameterNotNull(composedTypeCode, "Parameter 'composedTypeCode' cannot be null.");
        ComposedTypeModel composedTypeModel = getComposedTypeForCode(composedTypeCode);
        Set<AttributeDescriptorModel> attributeDescriptors = new HashSet<>(composedTypeModel.getDeclaredattributedescriptors());
        if(criteria.matches(128))
        {
            ComposedType composedType = (ComposedType)getModelService().getSource(composedTypeModel);
            Set<AttributeDescriptorModel> privateAttributeDescriptors = new HashSet<>();
            for(AttributeDescriptor descriptor : composedType.getAttributeDescriptorsIncludingPrivate())
            {
                if(descriptor.isPrivate())
                {
                    privateAttributeDescriptors.add((AttributeDescriptorModel)getModelService().get(descriptor));
                }
            }
            attributeDescriptors.addAll(privateAttributeDescriptors);
        }
        if(criteria.matches(1024))
        {
            attributeDescriptors.addAll(composedTypeModel.getInheritedattributedescriptors());
        }
        CollectionUtils.filter(attributeDescriptors, (Predicate)new AttributeModifierPredicator(this, criteria));
        return attributeDescriptors;
    }
}
