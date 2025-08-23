package com.hybris.backoffice.excel.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.template.populator.extractor.ClassificationFullNameExtractor;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import org.springframework.beans.factory.annotation.Required;

public class ExcelClassificationAttributeFactory
{
    private ClassificationFullNameExtractor classificationFullNameExtractor;


    public ExcelClassificationAttribute create(ClassAttributeAssignmentModel attributeAssignment, String isoCode)
    {
        ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
        attribute.setIsoCode(isoCode);
        attribute.setAttributeAssignment(attributeAssignment);
        attribute.setName(this.classificationFullNameExtractor.extract(attribute));
        return attribute;
    }


    public ExcelClassificationAttribute create(ClassAttributeAssignmentModel attributeAssignment)
    {
        return create(attributeAssignment, null);
    }


    @Required
    public void setClassificationFullNameExtractor(ClassificationFullNameExtractor classificationFullNameExtractor)
    {
        this.classificationFullNameExtractor = classificationFullNameExtractor;
    }
}
