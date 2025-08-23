package com.hybris.backoffice.excel.template;

import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.template.populator.ExcelAttributeContext;
import com.hybris.backoffice.excel.template.populator.appender.ExcelMarkAppender;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.OrderComparator;

public class DisplayNameAttributeNameFormatter implements AttributeNameFormatter<ExcelAttributeDescriptorAttribute>
{
    private CommonI18NService commonI18NService;
    private TypeService typeService;
    private List<ExcelMarkAppender<ExcelAttributeDescriptorAttribute>> appenders = Collections.emptyList();


    public String format(@Nonnull ExcelAttributeContext<ExcelAttributeDescriptorAttribute> excelAttributeContext)
    {
        String isoCode = (String)excelAttributeContext.getAttribute("isoCode", String.class);
        return Optional.<ExcelAttributeDescriptorAttribute>of((ExcelAttributeDescriptorAttribute)excelAttributeContext.getExcelAttribute(ExcelAttributeDescriptorAttribute.class))
                        .map(attr -> getAttributeDisplayName(attr, isoCode))
                        .orElse("");
    }


    protected String getAttributeDisplayName(ExcelAttributeDescriptorAttribute excelAttributeDescriptorAttribute, String workbookIsoCode)
    {
        AttributeDescriptorModel attributeDescriptor = excelAttributeDescriptorAttribute.getAttributeDescriptorModel();
        String langIsoCode = excelAttributeDescriptorAttribute.getIsoCode();
        Supplier<String> currentLang = () -> StringUtils.isNotEmpty(langIsoCode) ? langIsoCode : this.commonI18NService.getCurrentLanguage().getIsocode();
        boolean isNameUnique = (this.typeService.getAttributeDescriptorsForType(attributeDescriptor.getEnclosingType()).stream().map(this::getAttributeDescriptorName).filter(attrName -> StringUtils.equals(getAttributeDescriptorName(attributeDescriptor), attrName)).count() == 1L);
        String value = getAttributeDescriptorName(attributeDescriptor, workbookIsoCode) + (isNameUnique ? "" : String.format("[%s]", new Object[] {attributeDescriptor.getQualifier()})) + (attributeDescriptor.getLocalized().booleanValue()
                        ? String.format("[%s]", new Object[] {currentLang.get()})
                        : "");
        for(ExcelMarkAppender<ExcelAttributeDescriptorAttribute> appender : this.appenders)
        {
            value = (String)appender.apply(value, excelAttributeDescriptorAttribute);
        }
        return value;
    }


    protected String getAttributeDescriptorName(AttributeDescriptorModel attributeDescriptor)
    {
        return getAttributeDescriptorName(attributeDescriptor, null);
    }


    protected String getAttributeDescriptorName(AttributeDescriptorModel attributeDescriptor, String isoCode)
    {
        if(StringUtils.isNotEmpty(isoCode))
        {
            Locale locale = this.commonI18NService.getLocaleForIsoCode(isoCode);
            if(StringUtils.isNotEmpty(attributeDescriptor.getName(locale)))
            {
                return attributeDescriptor.getName(locale);
            }
        }
        return StringUtils.isNotEmpty(attributeDescriptor.getName()) ? attributeDescriptor.getName() :
                        attributeDescriptor.getQualifier();
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setAppenders(List<ExcelMarkAppender<ExcelAttributeDescriptorAttribute>> appenders)
    {
        if(appenders != null)
        {
            OrderComparator.sort(appenders);
        }
        this.appenders = ListUtils.emptyIfNull(appenders);
    }
}
