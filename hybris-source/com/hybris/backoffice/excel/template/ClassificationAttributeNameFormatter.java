package com.hybris.backoffice.excel.template;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.template.populator.ExcelAttributeContext;
import com.hybris.backoffice.excel.template.populator.appender.ExcelMarkAppender;
import com.hybris.backoffice.excel.template.populator.extractor.ClassificationFullNameExtractor;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nonnull;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.OrderComparator;

public class ClassificationAttributeNameFormatter implements AttributeNameFormatter<ExcelClassificationAttribute>
{
    private ClassificationFullNameExtractor classificationFullNameExtractor;
    private List<ExcelMarkAppender<ExcelClassificationAttribute>> appenders = Collections.emptyList();


    public String format(@Nonnull ExcelAttributeContext<ExcelClassificationAttribute> context)
    {
        ExcelClassificationAttribute excelClassificationAttribute = (ExcelClassificationAttribute)context.getExcelAttribute(ExcelClassificationAttribute.class);
        String formattedName = this.classificationFullNameExtractor.extract(excelClassificationAttribute);
        for(ExcelMarkAppender<ExcelClassificationAttribute> appender : this.appenders)
        {
            formattedName = (String)appender.apply(formattedName, excelClassificationAttribute);
        }
        return formattedName;
    }


    @Required
    public void setClassificationFullNameExtractor(ClassificationFullNameExtractor classificationFullNameExtractor)
    {
        this.classificationFullNameExtractor = classificationFullNameExtractor;
    }


    public void setAppenders(List<ExcelMarkAppender<ExcelClassificationAttribute>> appenders)
    {
        if(appenders != null)
        {
            OrderComparator.sort(appenders);
        }
        this.appenders = ListUtils.emptyIfNull(appenders);
    }
}
