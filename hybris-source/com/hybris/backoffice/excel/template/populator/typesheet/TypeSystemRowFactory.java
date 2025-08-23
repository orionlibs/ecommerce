package com.hybris.backoffice.excel.template.populator.typesheet;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.template.AttributeNameFormatter;
import com.hybris.backoffice.excel.template.CollectionFormatter;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import com.hybris.backoffice.excel.template.populator.DefaultExcelAttributeContext;
import com.hybris.backoffice.excel.translators.ExcelTranslatorRegistry;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Required;

public class TypeSystemRowFactory
{
    private CommonI18NService commonI18NService;
    private ExcelFilter<AttributeDescriptorModel> excelUniqueFilter;
    private ExcelTranslatorRegistry excelTranslatorRegistry;
    private CollectionFormatter collectionFormatter;
    private AttributeNameFormatter<ExcelAttributeDescriptorAttribute> attributeNameFormatter;


    public TypeSystemRow create(@Nonnull AttributeDescriptorModel attributeDescriptor)
    {
        String attributeDisplayName = getAttributeDisplayName(attributeDescriptor);
        TypeSystemRow created = new TypeSystemRow();
        created.setTypeCode(this.collectionFormatter.formatToString(new String[] {attributeDescriptor.getEnclosingType().getCode()}));
        created.setTypeName(attributeDescriptor.getEnclosingType().getName());
        created.setAttrQualifier(attributeDescriptor.getQualifier());
        created.setAttrName(attributeDescriptor.getName());
        created.setAttrOptional(attributeDescriptor.getOptional());
        created.setAttrTypeCode(attributeDescriptor.getAttributeType().getCode());
        created.setAttrTypeItemType(attributeDescriptor.getDeclaringEnclosingType().getCode());
        created.setAttrLocalized(attributeDescriptor.getLocalized());
        created.setAttrLocLang(getAttrLocLang(attributeDescriptor.getLocalized().booleanValue()));
        created.setAttrDisplayName(attributeDisplayName);
        created.setAttrUnique(Boolean.valueOf(this.excelUniqueFilter.test(attributeDescriptor)));
        created.setAttrReferenceFormat(getReferenceFormat(attributeDescriptor));
        return created;
    }


    protected String getAttributeDisplayName(AttributeDescriptorModel attributeDescriptor)
    {
        Collection<String> attributeNames = (Collection<String>)this.commonI18NService.getAllLanguages().stream().filter(C2LItemModel::getActive).map(C2LItemModel::getIsocode)
                        .map(lang -> this.attributeNameFormatter.format(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)new ExcelAttributeDescriptorAttribute(attributeDescriptor, lang)))).collect(Collectors.toList());
        return this.collectionFormatter.formatToString(attributeNames);
    }


    private String getAttrLocLang(boolean localized)
    {
        if(!localized)
        {
            return "";
        }
        List<String> isoCodes = (List<String>)this.commonI18NService.getAllLanguages().stream().filter(C2LItemModel::getActive).map(C2LItemModel::getIsocode).collect(Collectors.toList());
        return this.collectionFormatter.formatToString(isoCodes);
    }


    private String getReferenceFormat(AttributeDescriptorModel attributeDescriptor)
    {
        return this.excelTranslatorRegistry.getTranslator(attributeDescriptor)
                        .map(excelValueTranslator -> excelValueTranslator.referenceFormat(attributeDescriptor))
                        .orElse("");
    }


    public TypeSystemRow merge(@Nonnull TypeSystemRow first, @Nonnull TypeSystemRow second)
    {
        TypeSystemRow merged = new TypeSystemRow();
        BeanUtils.copyProperties(first, merged);
        if(!first.getTypeCode().contains(second.getTypeCode()))
        {
            merged.setTypeCode(String.join(",", new CharSequence[] {first.getTypeCode(), second.getTypeCode()}));
        }
        return merged;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setExcelUniqueFilter(ExcelFilter<AttributeDescriptorModel> excelUniqueFilter)
    {
        this.excelUniqueFilter = excelUniqueFilter;
    }


    @Required
    public void setExcelTranslatorRegistry(ExcelTranslatorRegistry excelTranslatorRegistry)
    {
        this.excelTranslatorRegistry = excelTranslatorRegistry;
    }


    @Required
    public void setCollectionFormatter(CollectionFormatter collectionFormatter)
    {
        this.collectionFormatter = collectionFormatter;
    }


    @Required
    public void setAttributeNameFormatter(AttributeNameFormatter<ExcelAttributeDescriptorAttribute> attributeNameFormatter)
    {
        this.attributeNameFormatter = attributeNameFormatter;
    }
}
