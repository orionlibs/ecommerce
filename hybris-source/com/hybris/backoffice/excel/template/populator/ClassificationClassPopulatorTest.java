package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class ClassificationClassPopulatorTest
{
    ClassificationClassPopulator populator = new ClassificationClassPopulator();


    @Test
    public void shouldGetClassificationClassName()
    {
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        BDDMockito.given(classificationClass.getCode()).willReturn("classificationClassName");
        ClassAttributeAssignmentModel assignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(assignment.getClassificationClass()).willReturn(classificationClass);
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(attribute.getAttributeAssignment()).willReturn(assignment);
        String result = this.populator.apply(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)attribute));
        Assertions.assertThat(result).isEqualTo("classificationClassName");
    }
}
