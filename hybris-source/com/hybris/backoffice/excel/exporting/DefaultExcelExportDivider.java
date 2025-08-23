package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.template.mapper.ExcelMapper;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExcelExportDivider implements ExcelExportDivider
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultExcelExportDivider.class);
    private ExcelMapper<ComposedTypeModel, AttributeDescriptorModel> mapper;
    private TypeService typeService;
    private ModelService modelService;
    private CommonI18NService commonI18NService;
    private PermissionCRUDService permissionCRUDService;


    public Map<String, Set<ItemModel>> groupItemsByType(List<ItemModel> items)
    {
        Objects.requireNonNull(this.modelService);
        return (Map<String, Set<ItemModel>>)items.stream().collect(Collectors.groupingBy(this.modelService::getModelType, Collectors.toSet()));
    }


    public Map<String, Set<SelectedAttribute>> groupAttributesByType(Set<String> typeCodes, List<SelectedAttribute> selectedAttributes)
    {
        return (Map<String, Set<SelectedAttribute>>)typeCodes.stream()
                        .collect(Collectors.toMap(Function.identity(), typeCode -> getAttributes(selectedAttributes, typeCode), (a, b) -> a));
    }


    protected Set<SelectedAttribute> getAttributes(List<SelectedAttribute> selectedAttributes, String typeCode)
    {
        ComposedTypeModel composedType = this.typeService.getComposedTypeForCode(typeCode);
        Set<String> selectedQualifiers = (Set<String>)selectedAttributes.stream().map(selectedAttribute -> selectedAttribute.getAttributeDescriptor().getQualifier()).collect(Collectors.toSet());
        Set<SelectedAttribute> attributes = new LinkedHashSet<>();
        Set<SelectedAttribute> required = getMissingRequiredAndUniqueAttributes(composedType, selectedQualifiers);
        attributes.addAll(filterByPermissions(required));
        attributes.addAll(createSelectedAttributesForGivenType(typeCode, selectedAttributes));
        return attributes;
    }


    private List<SelectedAttribute> createSelectedAttributesForGivenType(String typeCode, List<SelectedAttribute> attributes)
    {
        return (List<SelectedAttribute>)attributes.stream().map(attr -> createSelectedAttributeForGivenType(typeCode, attr)).collect(Collectors.toList());
    }


    private SelectedAttribute createSelectedAttributeForGivenType(String typeCode, SelectedAttribute attr)
    {
        AttributeDescriptorModel attributeDescriptorForType = this.typeService.getAttributeDescriptor(typeCode, attr
                        .getQualifier());
        return new SelectedAttribute(attr.getIsoCode(), attr.getReferenceFormat(), attr.getDefaultValues(), attributeDescriptorForType);
    }


    protected List<SelectedAttribute> filterByPermissions(Collection<SelectedAttribute> selectedAttributes)
    {
        List<SelectedAttribute> filtered = (List<SelectedAttribute>)selectedAttributes.stream().filter(attr -> this.permissionCRUDService.canReadAttribute(attr.getAttributeDescriptor())).collect(Collectors.toList());
        Collection<SelectedAttribute> removed = CollectionUtils.removeAll(selectedAttributes, filtered);
        if(CollectionUtils.isNotEmpty(removed))
        {
            List<String> removedAttributes = (List<String>)removed.stream().map(SelectedAttribute::getQualifier).collect(Collectors.toList());
            LOG.warn("Insufficient permissions for attributes: {}", removedAttributes);
        }
        return filtered;
    }


    protected Set<SelectedAttribute> getMissingRequiredAndUniqueAttributes(ComposedTypeModel composedType, Set<String> selectedQualifiers)
    {
        Predicate<AttributeDescriptorModel> skipAlreadySelected = attribute -> !selectedQualifiers.contains(attribute.getQualifier());
        String language = this.commonI18NService.getCurrentLanguage().getIsocode();
        Function<AttributeDescriptorModel, String> langIsoCode = attributeDescriptor -> attributeDescriptor.getLocalized().booleanValue() ? language : null;
        return (Set<SelectedAttribute>)((Collection<AttributeDescriptorModel>)this.mapper.apply(composedType)).stream().filter(skipAlreadySelected)
                        .map(attributeDescriptor -> new SelectedAttribute(langIsoCode.apply(attributeDescriptor), attributeDescriptor))
                        .collect(Collectors.toSet());
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


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public PermissionCRUDService getPermissionCRUDService()
    {
        return this.permissionCRUDService;
    }


    @Required
    public void setPermissionCRUDService(PermissionCRUDService permissionCRUDService)
    {
        this.permissionCRUDService = permissionCRUDService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ExcelMapper<ComposedTypeModel, AttributeDescriptorModel> getMapper()
    {
        return this.mapper;
    }


    @Required
    public void setMapper(ExcelMapper<ComposedTypeModel, AttributeDescriptorModel> mapper)
    {
        this.mapper = mapper;
    }
}
