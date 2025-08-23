package de.hybris.platform.cms2.version.service.impl;

import com.google.common.collect.Lists;
import de.hybris.platform.cms2.version.service.CMSVersionHelper;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCMSVersionHelper implements CMSVersionHelper
{
    private TypeService typeService;
    private Map<Predicate<ItemModel>, List<String>> cmsAttributesNotVersion;


    public boolean isCollectionAttribute(AttributeDescriptorModel attribute)
    {
        return attribute.getAttributeType().getItemtype().contains("CollectionType");
    }


    public List<AttributeDescriptorModel> getSerializableAttributes(ItemModel itemModel)
    {
        ComposedTypeModel composedType = getTypeService().getComposedTypeForClass(itemModel.getClass());
        Predicate<AttributeDescriptorModel> isNotTypeModel = attribute -> !getTypeService().isAssignableFrom("Type", attribute.getAttributeType().getCode());
        Predicate<AttributeDescriptorModel> isWritable = AttributeDescriptorModel::getWritable;
        Predicate<AttributeDescriptorModel> isRelation = attribute -> attribute instanceof de.hybris.platform.core.model.type.RelationDescriptorModel;
        Predicate<AttributeDescriptorModel> isNotPk = attribute -> !"pk".equals(attribute.getQualifier());
        List<AttributeDescriptorModel> attributeDescriptorModels = Lists.newArrayList(composedType
                        .getDeclaredattributedescriptors());
        List<String> attributesNotVersionList = getAttributesNotVersion(itemModel);
        attributeDescriptorModels.addAll((Collection<? extends AttributeDescriptorModel>)composedType
                        .getAllSuperTypes()
                        .stream()
                        .map(ComposedTypeModel::getDeclaredattributedescriptors)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList()));
        return (List<AttributeDescriptorModel>)attributeDescriptorModels
                        .stream()
                        .filter(isRelation.or(isNotTypeModel.and(isNotPk).and(isWritable)))
                        .filter(attribute -> !attributesNotVersionList.stream().filter(()).findAny().isPresent())
                        .collect(Collectors.toList());
    }


    public List<String> getAttributesNotVersion(ItemModel itemModel)
    {
        List<String> attributesVersionInSession = new ArrayList<>();
        getCmsAttributesNotVersion().forEach((genericTypePredicate, strings) -> {
            if(genericTypePredicate.test(itemModel))
            {
                attributesVersionInSession.addAll(strings);
            }
        });
        return attributesVersionInSession;
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


    protected Map<Predicate<ItemModel>, List<String>> getCmsAttributesNotVersion()
    {
        return this.cmsAttributesNotVersion;
    }


    @Required
    public void setCmsAttributesNotVersion(Map<Predicate<ItemModel>, List<String>> cmsAttributesNotVersion)
    {
        this.cmsAttributesNotVersion = cmsAttributesNotVersion;
    }
}
