package com.hybris.backoffice.excel.template.populator.extractor;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class ClassificationFullNameExtractorTest
{
    ClassificationFullNameExtractor extractor = new ClassificationFullNameExtractor();


    @Test
    public void shouldFormatNonLocalizedName()
    {
        ExcelClassificationAttribute excelAttribute = prepareExcelClassificationAttribute();
        String result = this.extractor.extract(excelAttribute);
        Assertions.assertThat(result).isEqualTo("class.attribute - systemId/systemVersion");
    }


    @Test
    public void shouldFormatLocalizedName()
    {
        ExcelClassificationAttribute excelAttribute = prepareExcelClassificationAttribute();
        BDDMockito.given(Boolean.valueOf(excelAttribute.isLocalized())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(excelAttribute.getIsoCode()).willReturn("lang");
        String result = this.extractor.extract(excelAttribute);
        Assertions.assertThat(result).isEqualTo("class.attribute[lang] - systemId/systemVersion");
    }


    @Test
    public void shouldFormatWithoutClassificationClassName()
    {
        ExcelClassificationAttribute excelAttribute = prepareExcelClassificationAttribute();
        BDDMockito.given(excelAttribute.getAttributeAssignment().getClassificationClass().getCode()).willReturn(null);
        BDDMockito.given(Boolean.valueOf(excelAttribute.isLocalized())).willReturn(Boolean.valueOf(true));
        BDDMockito.given(excelAttribute.getIsoCode()).willReturn("lang");
        String result = this.extractor.extract(excelAttribute);
        Assertions.assertThat(result).isEqualTo(".attribute[lang] - systemId/systemVersion");
    }


    @Test
    public void shouldFormatWithoutAttributeName()
    {
        ExcelClassificationAttribute excelAttribute = prepareExcelClassificationAttribute();
        BDDMockito.given(excelAttribute.getAttributeAssignment().getClassificationAttribute().getCode()).willReturn(null);
        String result = this.extractor.extract(excelAttribute);
        Assertions.assertThat(result).isEqualTo("class. - systemId/systemVersion");
    }


    @Test
    public void shouldFormatWithoutSystemVersion()
    {
        ExcelClassificationAttribute excelAttribute = prepareExcelClassificationAttribute();
        BDDMockito.given(excelAttribute.getAttributeAssignment().getSystemVersion().getVersion()).willReturn(null);
        String result = this.extractor.extract(excelAttribute);
        Assertions.assertThat(result).isEqualTo("class.attribute - systemId/");
    }


    @Test
    public void shouldFormatWithoutSystemId()
    {
        ExcelClassificationAttribute excelAttribute = prepareExcelClassificationAttribute();
        BDDMockito.given(excelAttribute.getAttributeAssignment().getSystemVersion().getCatalog().getId()).willReturn(null);
        String result = this.extractor.extract(excelAttribute);
        Assertions.assertThat(result).isEqualTo("class.attribute - /systemVersion");
    }


    private ExcelClassificationAttribute prepareExcelClassificationAttribute()
    {
        ClassificationSystemModel classificationSystemModel = (ClassificationSystemModel)Mockito.mock(ClassificationSystemModel.class);
        BDDMockito.given(classificationSystemModel.getId()).willReturn("systemId");
        ClassificationSystemVersionModel classificationSystemVersionModel = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        BDDMockito.given(classificationSystemVersionModel.getVersion()).willReturn("systemVersion");
        BDDMockito.given(classificationSystemVersionModel.getCatalog()).willReturn(classificationSystemModel);
        ClassificationAttributeModel classificationAttributeModel = (ClassificationAttributeModel)Mockito.mock(ClassificationAttributeModel.class);
        BDDMockito.given(classificationAttributeModel.getSystemVersion()).willReturn(classificationSystemVersionModel);
        BDDMockito.given(classificationAttributeModel.getCode()).willReturn("attribute");
        ClassificationClassModel classificationClassModel = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        BDDMockito.given(classificationClassModel.getCode()).willReturn("class");
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(classAttributeAssignmentModel.getClassificationAttribute()).willReturn(classificationAttributeModel);
        BDDMockito.given(classAttributeAssignmentModel.getClassificationClass()).willReturn(classificationClassModel);
        BDDMockito.given(classAttributeAssignmentModel.getSystemVersion()).willReturn(classificationSystemVersionModel);
        ExcelClassificationAttribute excelAttribute = (ExcelClassificationAttribute)Mockito.mock(ExcelClassificationAttribute.class);
        BDDMockito.given(excelAttribute.getAttributeAssignment()).willReturn(classAttributeAssignmentModel);
        return excelAttribute;
    }
}
