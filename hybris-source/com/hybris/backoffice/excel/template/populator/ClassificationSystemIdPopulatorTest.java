package com.hybris.backoffice.excel.template.populator;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class ClassificationSystemIdPopulatorTest
{
    ClassificationSystemIdPopulator populator = new ClassificationSystemIdPopulator();


    @Test
    public void shouldGetCategorySystemId()
    {
        ClassificationSystemModel catalog = (ClassificationSystemModel)Mockito.mock(ClassificationSystemModel.class);
        BDDMockito.given(catalog.getId()).willReturn("systemId");
        ClassificationSystemVersionModel systemVersion = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        BDDMockito.given(systemVersion.getCatalog()).willReturn(catalog);
        ClassAttributeAssignmentModel assignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(assignment.getSystemVersion()).willReturn(systemVersion);
        ExcelClassificationAttribute attribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(attribute.getAttributeAssignment()).willReturn(assignment);
        String result = this.populator.apply(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)attribute));
        Assertions.assertThat(result).isEqualTo("systemId");
    }
}
