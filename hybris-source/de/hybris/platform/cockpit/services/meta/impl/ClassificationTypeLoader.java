package de.hybris.platform.cockpit.services.meta.impl;

import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.cockpit.jalo.CockpitManager;
import de.hybris.platform.cockpit.jalo.template.CockpitItemTemplate;
import de.hybris.platform.cockpit.model.meta.ExtendedType;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.meta.impl.ClassAttributePropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ClassificationClassPath;
import de.hybris.platform.cockpit.model.meta.impl.ItemType;
import de.hybris.platform.cockpit.services.meta.CachingTypeLoader;
import de.hybris.platform.cockpit.services.meta.PropertyDescriptorCodeResolver;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.search.restriction.SearchRestrictionService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationTypeLoader extends AbstractTypeLoader implements CachingTypeLoader
{
    private ClassificationService classificationService;
    private SessionService sessionService;
    private SearchRestrictionService searchRestrictionService;
    private TypeCache extTypeCache;
    private PropertyDescriptorCodeResolver propertyDescriptorCodeResolver;


    public ExtendedType loadType(String code, TypeService typeService)
    {
        if(code.indexOf('/') != -1)
        {
            return loadClassificationType(code);
        }
        return null;
    }


    public TypeModel getValueType(ObjectType enclosingType, PropertyDescriptor propertyDescriptor, TypeService typeService)
    {
        if(enclosingType instanceof de.hybris.platform.cockpit.model.meta.impl.ClassificationType)
        {
            if(propertyDescriptor instanceof ClassAttributePropertyDescriptor)
            {
                ClassAttributeAssignment caa = ((ClassAttributePropertyDescriptor)propertyDescriptor).getAttributeAssignment();
                if(caa != null)
                {
                    EnumerationValue caaType = caa.getAttributeType();
                    if("enum".equalsIgnoreCase(caaType.getCode()))
                    {
                        return (TypeModel)getTypeService().getComposedTypeForClass(ClassificationAttributeValueModel.class);
                    }
                }
            }
        }
        return null;
    }


    public List<Object> getAvailableValues(TypeModel type, PropertyDescriptor propertyDescriptor, TypeService typeService)
    {
        if(type != null &&
                        getTypeService().isAssignableFrom((TypeModel)
                                        getTypeService().getComposedTypeForClass(ClassificationAttributeValueModel.class), type))
        {
            return (List<Object>)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, typeService, propertyDescriptor));
        }
        return null;
    }


    public Set<String> getExtendedTypeCodes(TypedObject item)
    {
        ItemModel itemModel = (ItemModel)item.getObject();
        if(itemModel == null)
        {
            return Collections.EMPTY_SET;
        }
        Collection<ClassificationClass> ccs = (Collection<ClassificationClass>)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, itemModel));
        Set<String> codes = new HashSet<>();
        for(ClassificationClass cc : ccs)
        {
            codes.add(ClassificationClassPath.getClassCode(cc));
        }
        return codes;
    }


    public String getAttributeCodeFromPropertyQualifier(String propertyQualifier)
    {
        return (propertyQualifier.indexOf('/') == -1) ? null :
                        propertyQualifier.substring(propertyQualifier.lastIndexOf(".") + 1);
    }


    public String getTypeCodeFromPropertyQualifier(String propertyQualifier)
    {
        return (propertyQualifier.indexOf('/') == -1) ? null : propertyQualifier.substring(0, propertyQualifier
                        .lastIndexOf("."));
    }


    protected String getDefaultEditorType(AttributeDescriptorModel attributeDescriptor)
    {
        return getPropertyService().getDefaultEditorType(attributeDescriptor.getAttributeType().getCode(),
                        BooleanUtils.toBoolean(attributeDescriptor.getLocalized()));
    }


    protected String getDefaultEditorType(ClassAttributeAssignment caa)
    {
        return "FEATURE";
    }


    protected ExtendedType loadClassificationType(String code)
    {
        MyClassificationType myClassificationType;
        ExtendedType type = getExtendedTypeCached(code);
        if(type == null)
        {
            MyClassificationType myClassificationType1 = new MyClassificationType(this, code);
            ClassificationClass cClass = myClassificationType1.getClassificationClass();
            Set<PropertyDescriptor> descriptors = new HashSet<>();
            getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, cClass, code, descriptors));
            myClassificationType1.setDeclaredPropertyDescriptors(descriptors);
            myClassificationType = myClassificationType1;
            getExtendedTypeCache().addType(code, (ObjectType)myClassificationType);
        }
        return (ExtendedType)myClassificationType;
    }


    public Collection<ExtendedType> getExtendedTypesForTemplate(ItemType type, String templateCode, TypeService typeService)
    {
        CockpitItemTemplate itemTemplate = CockpitManager.getInstance().getCockpitItemTemplate((ComposedType)
                        getModelService().getSource(type.getComposedType()), templateCode);
        if(itemTemplate == null)
        {
            throw new IllegalArgumentException("No CockpitItemTemplate found for type " + type.getCode() + " and code " + templateCode);
        }
        return (Collection<ExtendedType>)getSessionService().executeInLocalView((SessionExecutionBody)new Object(this, itemTemplate, typeService));
    }


    @Required
    public void setClassificationService(ClassificationService classificationService)
    {
        this.classificationService = classificationService;
    }


    protected ClassificationService getClassificationService()
    {
        return this.classificationService;
    }


    public void setExtendedTypeCache(TypeCache typeCache)
    {
        this.extTypeCache = typeCache;
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


    public void removeCachedType(String code)
    {
        if(this.extTypeCache != null)
        {
            this.extTypeCache.removeType(code);
        }
    }


    public void removeDefaultCachedTypes()
    {
        throw new UnsupportedOperationException();
    }


    public void removeAllCachedTypes()
    {
        if(this.extTypeCache != null)
        {
            this.extTypeCache.clear();
        }
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    protected SessionService getSessionService()
    {
        return this.sessionService;
    }


    @Required
    public void setSearchRestrictionService(SearchRestrictionService searchRestrictionService)
    {
        this.searchRestrictionService = searchRestrictionService;
    }


    protected SearchRestrictionService getSearchRestrictionService()
    {
        return this.searchRestrictionService;
    }


    protected PropertyDescriptorCodeResolver getPropertyDescriptorCodeResolver()
    {
        return this.propertyDescriptorCodeResolver;
    }


    @Required
    public void setPropertyDescriptorCodeResolver(PropertyDescriptorCodeResolver propertyDescriptorCodeResolver)
    {
        this.propertyDescriptorCodeResolver = propertyDescriptorCodeResolver;
    }
}
