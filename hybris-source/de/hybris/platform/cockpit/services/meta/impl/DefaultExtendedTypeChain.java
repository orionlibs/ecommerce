package de.hybris.platform.cockpit.services.meta.impl;

import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ItemType;
import de.hybris.platform.cockpit.services.meta.CachingTypeLoader;
import de.hybris.platform.cockpit.services.meta.ExtendedTypeLoader;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.core.model.type.TypeModel;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExtendedTypeChain implements ExtendedTypeLoader, CachingTypeLoader
{
    private List<ExtendedTypeLoader> extendedTypeLoaders;
    private List<String> defaultClearCacheTypeCodes;


    public ExtendedType loadType(String code, TypeService typeService)
    {
        for(ExtendedTypeLoader typeLoader : this.extendedTypeLoaders)
        {
            ExtendedType type = typeLoader.loadType(code, typeService);
            if(type != null)
            {
                return type;
            }
        }
        return null;
    }


    public TypeModel getValueType(ObjectType enclosingType, PropertyDescriptor propertyDescriptor, TypeService typeService)
    {
        for(ExtendedTypeLoader typeLoader : this.extendedTypeLoaders)
        {
            TypeModel type = typeLoader.getValueType(enclosingType, propertyDescriptor, typeService);
            if(type != null)
            {
                return type;
            }
        }
        return null;
    }


    public List<Object> getAvailableValues(TypeModel type, PropertyDescriptor propertyDescriptor, TypeService typeService)
    {
        for(ExtendedTypeLoader typeLoader : this.extendedTypeLoaders)
        {
            List<Object> availableValues = typeLoader.getAvailableValues(type, propertyDescriptor, typeService);
            if(availableValues != null)
            {
                return availableValues;
            }
        }
        return null;
    }


    public String getAttributeCodeFromPropertyQualifier(String propertyQualifier)
    {
        for(ExtendedTypeLoader typeLoader : this.extendedTypeLoaders)
        {
            String attributeCode = typeLoader.getAttributeCodeFromPropertyQualifier(propertyQualifier);
            if(attributeCode != null)
            {
                return attributeCode;
            }
        }
        return null;
    }


    public String getTypeCodeFromPropertyQualifier(String propertyQualifier)
    {
        for(ExtendedTypeLoader typeLoader : this.extendedTypeLoaders)
        {
            String typeCode = typeLoader.getTypeCodeFromPropertyQualifier(propertyQualifier);
            if(typeCode != null)
            {
                return typeCode;
            }
        }
        return null;
    }


    public Set<String> getExtendedTypeCodes(TypedObject item)
    {
        Set<String> ret = new HashSet<>();
        for(ExtendedTypeLoader typeLoader : this.extendedTypeLoaders)
        {
            Set<String> extendedTypeCodes = typeLoader.getExtendedTypeCodes(item);
            if(extendedTypeCodes != null)
            {
                ret.addAll(extendedTypeCodes);
            }
        }
        return ret;
    }


    public Collection<ExtendedType> getExtendedTypesForTemplate(ItemType type, String templateCode, TypeService typeService)
    {
        Collection<ExtendedType> extendedTypes = new HashSet<>();
        for(ExtendedTypeLoader typeLoader : this.extendedTypeLoaders)
        {
            Collection<ExtendedType> extendedTypesForTemplate = typeLoader.getExtendedTypesForTemplate(type, templateCode, typeService);
            if(extendedTypesForTemplate != null)
            {
                extendedTypes.addAll(extendedTypesForTemplate);
            }
        }
        return extendedTypes;
    }


    @Required
    public void setExtendedTypeLoaders(List<ExtendedTypeLoader> extendedTypeLoaders)
    {
        this.extendedTypeLoaders = extendedTypeLoaders;
    }


    protected List<ExtendedTypeLoader> getExtendedTypeLoaders()
    {
        return (this.extendedTypeLoaders == null) ? Collections.EMPTY_LIST : this.extendedTypeLoaders;
    }


    public void removeCachedType(String code)
    {
        for(ExtendedTypeLoader typeLoader : this.extendedTypeLoaders)
        {
            if(typeLoader instanceof CachingTypeLoader)
            {
                ((CachingTypeLoader)typeLoader).removeCachedType(code);
            }
        }
    }


    public void removeDefaultCachedTypes()
    {
        for(ExtendedTypeLoader typeLoader : this.extendedTypeLoaders)
        {
            if(typeLoader instanceof CachingTypeLoader)
            {
                if(this.defaultClearCacheTypeCodes != null)
                {
                    for(String code : this.defaultClearCacheTypeCodes)
                    {
                        ((CachingTypeLoader)typeLoader).removeCachedType(code);
                    }
                }
            }
        }
    }


    public void removeAllCachedTypes()
    {
        for(ExtendedTypeLoader typeLoader : this.extendedTypeLoaders)
        {
            if(typeLoader instanceof CachingTypeLoader)
            {
                ((CachingTypeLoader)typeLoader).removeAllCachedTypes();
            }
        }
    }


    public void setDefaultClearCacheTypeCodes(List<String> defaultClearCacheTypeCodes)
    {
        this.defaultClearCacheTypeCodes = defaultClearCacheTypeCodes;
    }
}
