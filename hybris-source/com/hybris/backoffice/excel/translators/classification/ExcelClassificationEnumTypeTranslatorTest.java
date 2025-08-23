package com.hybris.backoffice.excel.translators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.ExcelImportContext;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeValueModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.core.enums.TestEnum;
import java.util.Optional;
import org.assertj.core.api.AbstractObjectAssert;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelClassificationEnumTypeTranslatorTest
{
    @Mock
    private ClassificationService classificationService;
    @Mock
    private ClassificationAttributeHeaderValueCreator classificationAttributeHeaderValueCreator;
    @InjectMocks
    private ExcelClassificationEnumTypeTranslator translator = new ExcelClassificationEnumTypeTranslator();


    @Test
    public void shouldOrderParamBeInjectable()
    {
        this.translator.setOrder(1337);
        AssertionsForClassTypes.assertThat(this.translator.getOrder()).isEqualTo(1337);
    }


    @Test
    public void shouldImportValueWithClassificationAttributeHeaderCreator()
    {
        ExcelClassificationAttribute excelClassificationAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        BDDMockito.given(importParameters.getCellValue()).willReturn(TestEnum.TESTVALUE1);
        BDDMockito.given(importParameters.getIsoCode()).willReturn("isoCode");
        BDDMockito.given(excelClassificationAttribute.getAttributeAssignment()).willReturn(Mockito.mock(ClassAttributeAssignmentModel.class));
        BDDMockito.given(this.classificationAttributeHeaderValueCreator.create((ExcelClassificationAttribute)Matchers.eq(excelClassificationAttribute), (ExcelImportContext)Matchers.any())).willReturn("headerValue");
        ImpexValue result = this.translator.importValue(excelClassificationAttribute, importParameters, new ExcelImportContext());
        AssertionsForClassTypes.assertThat(result).isNotNull();
        AssertionsForClassTypes.assertThat(result.getValue()).isEqualTo(TestEnum.TESTVALUE1);
        ((AbstractObjectAssert)AssertionsForClassTypes.assertThat(result.getHeaderValue())
                        .isNotNull())
                        .hasFieldOrPropertyWithValue("name", "headerValue")
                        .hasFieldOrPropertyWithValue("lang", "isoCode")
                        .hasFieldOrPropertyWithValue("unique", Boolean.valueOf(false));
    }


    @Test
    public void shouldTranslatorBeHandledWhenAttributeTypeIsEnum()
    {
        ClassAttributeAssignmentModel assignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(assignment.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.ENUM);
        ExcelClassificationAttribute excelClassificationAttribute = new ExcelClassificationAttribute();
        excelClassificationAttribute.setAttributeAssignment(assignment);
        Assert.assertTrue(this.translator.canHandle(excelClassificationAttribute));
    }


    @Test
    public void shouldTranslatorNotBeHandledWhenAttributeTypeIsNotEnum()
    {
        ClassAttributeAssignmentModel assignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(assignment.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.NUMBER);
        ExcelClassificationAttribute excelClassificationAttribute = new ExcelClassificationAttribute();
        excelClassificationAttribute.setAttributeAssignment(assignment);
        Assert.assertFalse(this.translator.canHandle(excelClassificationAttribute));
    }


    @Test
    public void shouldExportEnumValueCorrectly()
    {
        TestEnum testEnum = TestEnum.TESTVALUE1;
        FeatureValue featureValue = (FeatureValue)Mockito.mock(FeatureValue.class);
        BDDMockito.given(featureValue.getValue()).willReturn(testEnum);
        Optional<String> exportedValue = this.translator.exportSingle((ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class), featureValue);
        Assert.assertTrue(exportedValue.isPresent());
        AssertionsForClassTypes.assertThat(exportedValue.get()).isEqualTo(testEnum.getCode());
    }


    @Test
    public void shouldExportClassificationAttributeValue()
    {
        ClassificationAttributeValueModel testEnum = (ClassificationAttributeValueModel)Mockito.mock(ClassificationAttributeValueModel.class);
        BDDMockito.given(testEnum.getCode()).willReturn("testValue1");
        FeatureValue featureValue = (FeatureValue)Mockito.mock(FeatureValue.class);
        BDDMockito.given(featureValue.getValue()).willReturn(testEnum);
        Optional<String> exportedValue = this.translator.exportSingle((ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class), featureValue);
        Assert.assertTrue(exportedValue.isPresent());
        AssertionsForClassTypes.assertThat(exportedValue.get()).isEqualTo(testEnum.getCode());
    }
}
