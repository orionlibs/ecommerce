package com.hybris.backoffice.excel.translators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImpexValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.importing.ExcelImportContext;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import com.hybris.backoffice.excel.translators.generic.factory.ExportDataFactory;
import com.hybris.backoffice.excel.translators.generic.factory.ReferenceFormatFactory;
import com.hybris.backoffice.excel.translators.generic.factory.RequiredAttributesFactory;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.classification.features.FeatureValue;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ExcelClassificationReferenceTranslatorTest
{
    @Mock
    ExportDataFactory mockedExportDataFactory;
    @Mock
    ReferenceFormatFactory mockedReferenceFormatFactory;
    @Mock
    RequiredAttributesFactory mockedRequiredAttributesFactory;
    @Mock
    ClassificationAttributeHeaderValueCreator mockedHeaderValueCreator;
    @Mock
    ExcelFilter<AttributeDescriptorModel> filter;
    @InjectMocks
    ExcelClassificationReferenceTranslator excelClassificationReferenceTranslator;


    @Test
    public void shouldExportSingleReferenceAttributeValue()
    {
        ComposedTypeModel referenceType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        ExcelClassificationAttribute excelAttribute = prepareExcelAttribute(referenceType);
        RequiredAttribute requiredAttribute = (RequiredAttribute)Mockito.mock(RequiredAttribute.class);
        BDDMockito.given(this.mockedRequiredAttributesFactory.create(referenceType)).willReturn(requiredAttribute);
        String objectToExport = "value";
        FeatureValue featureToExport = (FeatureValue)Mockito.mock(FeatureValue.class);
        BDDMockito.given(featureToExport.getValue()).willReturn("value");
        BDDMockito.given(this.mockedExportDataFactory.create(requiredAttribute, "value")).willReturn(Optional.of("result"));
        Optional<String> result = this.excelClassificationReferenceTranslator.exportSingle(excelAttribute, featureToExport);
        ((ExportDataFactory)BDDMockito.then(this.mockedExportDataFactory).should()).create(requiredAttribute, "value");
        Assertions.assertThat(result).isPresent();
        Assertions.assertThat(result).hasValue("result");
    }


    @Test
    public void shouldGetSingleReferenceFormat()
    {
        ComposedTypeModel referenceType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        ExcelClassificationAttribute excelAttribute = prepareExcelAttribute(referenceType);
        RequiredAttribute requiredAttribute = (RequiredAttribute)Mockito.mock(RequiredAttribute.class);
        BDDMockito.given(this.mockedRequiredAttributesFactory.create(referenceType)).willReturn(requiredAttribute);
        String referenceFormat = "referenceFormat";
        BDDMockito.given(this.mockedReferenceFormatFactory.create(requiredAttribute)).willReturn("referenceFormat");
        String result = this.excelClassificationReferenceTranslator.singleReferenceFormat(excelAttribute);
        ((ReferenceFormatFactory)BDDMockito.then(this.mockedReferenceFormatFactory).should()).create(requiredAttribute);
        Assertions.assertThat(result).isEqualTo("referenceFormat");
    }


    @Test
    public void shouldImportSingleReferenceAttributeValue()
    {
        ComposedTypeModel referenceType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        ExcelClassificationAttribute excelAttribute = prepareExcelAttribute(referenceType);
        ExcelImportContext importContext = (ExcelImportContext)Mockito.mock(ExcelImportContext.class);
        ImportParameters importParameters = (ImportParameters)Mockito.mock(ImportParameters.class);
        BDDMockito.given(importParameters.getSingleValueParameters()).willReturn(Maps.newHashMap("rawValue", "cellValue"));
        BDDMockito.given(importParameters.getIsoCode()).willReturn("isoCode");
        BDDMockito.given(this.mockedHeaderValueCreator.create(excelAttribute, importContext)).willReturn("headerValue");
        ImpexValue impexValue = this.excelClassificationReferenceTranslator.importSingle(excelAttribute, importParameters, importContext);
        Assertions.assertThat(impexValue).isNotNull();
        Assertions.assertThat(impexValue.getValue()).isEqualTo("cellValue");
        Assertions.assertThat(impexValue.getHeaderValue()).isNotNull();
        Assertions.assertThat(impexValue.getHeaderValue().getName()).isEqualTo("headerValue");
        Assertions.assertThat(impexValue.getHeaderValue().getLang()).isEqualTo("isoCode");
        Assertions.assertThat(impexValue.getHeaderValue().isUnique()).isEqualTo(false);
    }


    @Test
    public void shouldHandleReferenceTypesWhenComposedTypeHasAtLeastOneUniqueAttribute()
    {
        ComposedTypeModel composedTypeModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(classAttributeAssignment.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.REFERENCE);
        BDDMockito.given(classAttributeAssignment.getReferenceType()).willReturn(composedTypeModel);
        BDDMockito.given(composedTypeModel.getDeclaredattributedescriptors()).willReturn(Arrays.asList(new AttributeDescriptorModel[] {attributeDescriptorModel}));
        BDDMockito.given(composedTypeModel.getInheritedattributedescriptors()).willReturn(Collections.emptyList());
        BDDMockito.given(Boolean.valueOf(this.filter.test(attributeDescriptorModel))).willReturn(Boolean.valueOf(true));
        ExcelClassificationAttribute referenceExcelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(referenceExcelAttribute.getAttributeAssignment()).willReturn(classAttributeAssignment);
        boolean result = this.excelClassificationReferenceTranslator.canHandle(referenceExcelAttribute);
        Assertions.assertThat(result).isTrue();
    }


    @Test
    public void shouldNotHandleReferenceTypesWhenComposedTypeDoesNotHaveAnyUniqueAttribute()
    {
        ComposedTypeModel composedTypeModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(classAttributeAssignment.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.REFERENCE);
        BDDMockito.given(classAttributeAssignment.getReferenceType()).willReturn(composedTypeModel);
        BDDMockito.given(composedTypeModel.getDeclaredattributedescriptors()).willReturn(Arrays.asList(new AttributeDescriptorModel[] {attributeDescriptorModel}));
        BDDMockito.given(composedTypeModel.getInheritedattributedescriptors()).willReturn(Collections.emptyList());
        BDDMockito.given(Boolean.valueOf(this.filter.test(attributeDescriptorModel))).willReturn(Boolean.valueOf(false));
        ExcelClassificationAttribute referenceExcelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(referenceExcelAttribute.getAttributeAssignment()).willReturn(classAttributeAssignment);
        boolean result = this.excelClassificationReferenceTranslator.canHandle(referenceExcelAttribute);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldNotHandleOtherAttributeTypesThanReference()
    {
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(classAttributeAssignment.getAttributeType()).willReturn(ClassificationAttributeTypeEnum.NUMBER);
        ExcelClassificationAttribute numberExcelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(numberExcelAttribute.getAttributeAssignment()).willReturn(classAttributeAssignment);
        boolean result = this.excelClassificationReferenceTranslator.canHandle(numberExcelAttribute);
        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void shouldOrderHaveADefaultValueOfLowestPrecedenceMinusHundred()
    {
        int order = this.excelClassificationReferenceTranslator.getOrder();
        Assertions.assertThat(order).isEqualTo(2147483547);
    }


    @Test
    public void shouldOrderByInjectableByProperty()
    {
        int givenOrder = 100;
        this.excelClassificationReferenceTranslator.setOrder(100);
        Assertions.assertThat(this.excelClassificationReferenceTranslator.getOrder()).isEqualTo(100);
    }


    private ExcelClassificationAttribute prepareExcelAttribute(ComposedTypeModel referenceType)
    {
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(classAttributeAssignment.getReferenceType()).willReturn(referenceType);
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(excelAttribute.getAttributeAssignment()).willReturn(classAttributeAssignment);
        return excelAttribute;
    }
}
