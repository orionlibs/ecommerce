package com.hybris.backoffice.excel.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.template.populator.extractor.ClassificationFullNameExtractor;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelClassificationAttributeFactoryTest
{
    @Mock
    ClassificationFullNameExtractor mockedFullNameExtractor;
    @InjectMocks
    ExcelClassificationAttributeFactory attributeFactory;


    @Test
    public void shouldCreateExcelClassificationAttributeFromAssignmentWithIsoCode()
    {
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        String isoCode = "en";
        BDDMockito.given(this.mockedFullNameExtractor.extract((ExcelClassificationAttribute)Matchers.any(ExcelClassificationAttribute.class))).willReturn("name");
        ExcelClassificationAttribute attribute = this.attributeFactory.create(classAttributeAssignmentModel, "en");
        Assertions.assertThat(attribute.getIsoCode()).isEqualTo("en");
        Assertions.assertThat(attribute.getAttributeAssignment()).isEqualTo(classAttributeAssignmentModel);
        Assertions.assertThat(attribute.getName()).isEqualTo("name");
    }


    @Test
    public void shouldCreateExcelClassificationAttributeFromAssignment()
    {
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(this.mockedFullNameExtractor.extract((ExcelClassificationAttribute)Matchers.any(ExcelClassificationAttribute.class))).willReturn("name");
        ExcelClassificationAttribute attribute = this.attributeFactory.create(classAttributeAssignmentModel);
        Assertions.assertThat(attribute.getIsoCode()).isNull();
        Assertions.assertThat(attribute.getAttributeAssignment()).isEqualTo(classAttributeAssignmentModel);
        Assertions.assertThat(attribute.getName()).isEqualTo("name");
    }
}
