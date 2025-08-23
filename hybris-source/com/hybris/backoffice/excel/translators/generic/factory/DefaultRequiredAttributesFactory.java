package com.hybris.backoffice.excel.translators.generic.factory;

import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.CollectionTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.MapTypeModel;
import de.hybris.platform.core.model.type.RelationDescriptorModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRequiredAttributesFactory implements RequiredAttributesFactory
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultRequiredAttributesFactory.class);
    private int maxDepth = 50;
    private TypeService typeService;
    private ExcelFilter<AttributeDescriptorModel> uniqueFilter;
    private ExcelFilter<AttributeDescriptorModel> mandatoryFilter;
    private ExcelFilter<AttributeDescriptorModel> filter;


    public RequiredAttribute create(AttributeDescriptorModel attributeDescriptorModel)
    {
        return traverseRecursively(attributeDescriptorModel, null, 0);
    }


    public RequiredAttribute create(ComposedTypeModel composedTypeModel)
    {
        RequiredAttribute rootAttribute = new RequiredAttribute((TypeModel)composedTypeModel, composedTypeModel.getCode(), "", false, false, false);
        return traverse(composedTypeModel, rootAttribute, 0);
    }


    private RequiredAttribute traverseRecursively(AttributeDescriptorModel attributeDescriptorModel, RequiredAttribute parentAttribute, int depth)
    {
        if(depth > getMaxDepth())
        {
            return parentAttribute;
        }
        TypeModel attributeType = attributeDescriptorModel.getAttributeType();
        boolean isCollectionOfAtomicTypes = (attributeType instanceof CollectionTypeModel && ((CollectionTypeModel)attributeType).getElementType() instanceof de.hybris.platform.core.model.type.AtomicTypeModel);
        boolean isAtomicType = attributeType instanceof de.hybris.platform.core.model.type.AtomicTypeModel;
        if(isAtomicType || isCollectionOfAtomicTypes)
        {
            return handleAtomicType(attributeDescriptorModel, parentAttribute, attributeType);
        }
        RequiredAttribute currentUniqueAttribute = new RequiredAttribute(attributeType, attributeDescriptorModel.getEnclosingType().getCode(), attributeDescriptorModel.getQualifier(), BooleanUtils.isTrue(Boolean.valueOf(this.uniqueFilter.test(attributeDescriptorModel))),
                        BooleanUtils.isTrue(Boolean.valueOf(this.mandatoryFilter.test(attributeDescriptorModel))), BooleanUtils.isTrue(attributeDescriptorModel.getPartOf()));
        if(parentAttribute != null)
        {
            parentAttribute.addChild(currentUniqueAttribute);
        }
        ComposedTypeModel composedType = findComposedType(attributeDescriptorModel);
        traverse(composedType, currentUniqueAttribute, depth + 1);
        return (parentAttribute != null) ? parentAttribute : currentUniqueAttribute;
    }


    private ComposedTypeModel findComposedType(AttributeDescriptorModel attributeDescriptorModel)
    {
        TypeModel attributeType = attributeDescriptorModel.getAttributeType();
        if(attributeDescriptorModel instanceof RelationDescriptorModel)
        {
            return handleRelationType((RelationDescriptorModel)attributeDescriptorModel);
        }
        if(attributeType instanceof CollectionTypeModel)
        {
            return handleCollectionType((CollectionTypeModel)attributeType);
        }
        if(attributeDescriptorModel.getLocalized().booleanValue() && attributeType instanceof MapTypeModel && isNotAtomic(attributeDescriptorModel))
        {
            return handleLocalizedType(attributeType);
        }
        return loadComposedType(attributeDescriptorModel);
    }


    private boolean isNotAtomic(AttributeDescriptorModel attributeDescriptorModel)
    {
        boolean isAtomic = attributeDescriptorModel.getAttributeType() instanceof de.hybris.platform.core.model.type.AtomicTypeModel;
        boolean isLocalizedAtomic = (attributeDescriptorModel.getAttributeType() instanceof MapTypeModel && ((MapTypeModel)attributeDescriptorModel.getAttributeType()).getReturntype() instanceof de.hybris.platform.core.model.type.AtomicTypeModel);
        return (!isAtomic && !isLocalizedAtomic);
    }


    private ComposedTypeModel loadComposedType(AttributeDescriptorModel attributeDescriptorModel)
    {
        try
        {
            return this.typeService.getComposedTypeForCode(attributeDescriptorModel.getAttributeType().getCode());
        }
        catch(RuntimeException ex)
        {
            LOG.debug(String.format("Cannot load composed type for %s.%s", new Object[] {attributeDescriptorModel.getEnclosingType().getCode(), attributeDescriptorModel
                            .getQualifier()}), ex);
            return null;
        }
    }


    private static ComposedTypeModel handleRelationType(RelationDescriptorModel attributeDescriptorModel)
    {
        if(BooleanUtils.isFalse(attributeDescriptorModel.getIsSource()))
        {
            return attributeDescriptorModel.getRelationType().getSourceType();
        }
        return attributeDescriptorModel.getRelationType().getTargetType();
    }


    private ComposedTypeModel handleCollectionType(CollectionTypeModel attributeType)
    {
        return this.typeService.getComposedTypeForCode(attributeType.getElementType().getCode());
    }


    private ComposedTypeModel handleLocalizedType(TypeModel attributeType)
    {
        String localizedAttributeType = ((MapTypeModel)attributeType).getReturntype().getCode();
        return this.typeService.getComposedTypeForCode(localizedAttributeType);
    }


    private RequiredAttribute handleAtomicType(AttributeDescriptorModel attributeDescriptorModel, RequiredAttribute parentAttribute, TypeModel attributeType)
    {
        RequiredAttribute uniqueAttribute = new RequiredAttribute(attributeType, attributeDescriptorModel.getEnclosingType().getCode(), attributeDescriptorModel.getQualifier(), BooleanUtils.isTrue(Boolean.valueOf(this.uniqueFilter.test(attributeDescriptorModel))),
                        BooleanUtils.isTrue(Boolean.valueOf(this.mandatoryFilter.test(attributeDescriptorModel))), BooleanUtils.isTrue(attributeDescriptorModel.getPartOf()));
        if(parentAttribute != null)
        {
            parentAttribute.addChild(uniqueAttribute);
        }
        return (parentAttribute != null) ? parentAttribute : uniqueAttribute;
    }


    private RequiredAttribute traverse(ComposedTypeModel composedTypeModel, RequiredAttribute rootAttribute, int depth)
    {
        Collection<AttributeDescriptorModel> allAttributes = new HashSet<>();
        if(composedTypeModel != null)
        {
            allAttributes.addAll(composedTypeModel.getDeclaredattributedescriptors());
            allAttributes.addAll(composedTypeModel.getInheritedattributedescriptors());
        }
        List<AttributeDescriptorModel> uniqueAttributesModels = filterAttributes(allAttributes);
        for(AttributeDescriptorModel uniqueAttributesModel : uniqueAttributesModels)
        {
            traverseRecursively(uniqueAttributesModel, rootAttribute, depth + 1);
        }
        return rootAttribute;
    }


    private List<AttributeDescriptorModel> filterAttributes(Collection<AttributeDescriptorModel> allAttributes)
    {
        return (List<AttributeDescriptorModel>)allAttributes.stream()
                        .filter(attribute -> this.filter.test(attribute)).collect(Collectors.toList());
    }


    public int getMaxDepth()
    {
        return this.maxDepth;
    }


    public void setMaxDepth(int maxDepth)
    {
        this.maxDepth = maxDepth;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public ExcelFilter<AttributeDescriptorModel> getFilter()
    {
        return this.filter;
    }


    @Required
    public void setFilter(ExcelFilter<AttributeDescriptorModel> filter)
    {
        this.filter = filter;
    }


    public ExcelFilter<AttributeDescriptorModel> getUniqueFilter()
    {
        return this.uniqueFilter;
    }


    @Required
    public void setUniqueFilter(ExcelFilter<AttributeDescriptorModel> uniqueFilter)
    {
        this.uniqueFilter = uniqueFilter;
    }


    public ExcelFilter<AttributeDescriptorModel> getMandatoryFilter()
    {
        return this.mandatoryFilter;
    }


    @Required
    public void setMandatoryFilter(ExcelFilter<AttributeDescriptorModel> mandatoryFilter)
    {
        this.mandatoryFilter = mandatoryFilter;
    }
}
