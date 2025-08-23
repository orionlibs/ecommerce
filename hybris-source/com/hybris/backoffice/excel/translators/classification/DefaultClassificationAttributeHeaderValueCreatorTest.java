package com.hybris.backoffice.excel.translators.classification;

import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.importing.ExcelImportContext;
import com.hybris.backoffice.excel.translators.generic.RequiredAttribute;
import com.hybris.backoffice.excel.translators.generic.factory.RequiredAttributesFactory;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.Arrays;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultClassificationAttributeHeaderValueCreatorTest
{
    @Mock
    RequiredAttributesFactory mockedRequiredAttributesFactory;
    @InjectMocks
    ClassificationAttributeHeaderValueCreator classificationAttributeHeaderValueCreator = (ClassificationAttributeHeaderValueCreator)new DefaultClassificationAttributeHeaderValueCreator();


    @Test
    public void shouldCreateClassificationAttributeHeaderValue()
    {
        ClassAttributeAssignmentModel classAttributeAssignmentModel = prepareAssignment();
        ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
        attribute.setName("name");
        attribute.setIsoCode("isoCode");
        attribute.setAttributeAssignment(classAttributeAssignmentModel);
        String result = this.classificationAttributeHeaderValueCreator.create(attribute, new ExcelImportContext());
        Assertions.assertThat(result).isEqualTo("@column[class='classificationClass',system='ClassificationSystem',version='ClassificationVersion',translator=de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator]");
    }


    @Test
    public void shouldCreateClassificationAttributeHeaderValueWithReferences()
    {
        ComposedTypeModel referenceType = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        RequiredAttribute requiredAttribute = prepareRequiredAttribute("", new RequiredAttribute[] {prepareRequiredAttribute("catalog", new RequiredAttribute[] {prepareRequiredAttribute("id", new RequiredAttribute[0]), prepareRequiredAttribute("version", new RequiredAttribute[0])})});
        ClassAttributeAssignmentModel classAttributeAssignmentModel = prepareAssignment();
        BDDMockito.given(classAttributeAssignmentModel.getReferenceType()).willReturn(referenceType);
        BDDMockito.given(this.mockedRequiredAttributesFactory.create(referenceType)).willReturn(requiredAttribute);
        ExcelClassificationAttribute attribute = new ExcelClassificationAttribute();
        attribute.setName("name");
        attribute.setIsoCode("isoCode");
        attribute.setAttributeAssignment(classAttributeAssignmentModel);
        String result = this.classificationAttributeHeaderValueCreator.create(attribute, new ExcelImportContext());
        Assertions.assertThat(result).isEqualTo("@column(catalog(id,version))[class='classificationClass',system='ClassificationSystem',version='ClassificationVersion',translator=de.hybris.platform.catalog.jalo.classification.impex.ClassificationAttributeTranslator]");
    }


    private RequiredAttribute prepareRequiredAttribute(String qualifier, RequiredAttribute... children)
    {
        RequiredAttribute requiredAttribute = (RequiredAttribute)Mockito.mock(RequiredAttribute.class);
        BDDMockito.given(requiredAttribute.getQualifier()).willReturn(qualifier);
        BDDMockito.given(requiredAttribute.getChildren()).willReturn(Arrays.asList(children));
        return requiredAttribute;
    }


    private ClassAttributeAssignmentModel prepareAssignment()
    {
        ClassificationAttributeModel classificationAttributeModel = (ClassificationAttributeModel)Mockito.mock(ClassificationAttributeModel.class);
        BDDMockito.given(classificationAttributeModel.getCode()).willReturn("column");
        ClassificationSystemModel classificationSystemModel = (ClassificationSystemModel)Mockito.mock(ClassificationSystemModel.class);
        BDDMockito.given(classificationSystemModel.getId()).willReturn("ClassificationSystem");
        ClassificationClassModel classificationClassModel = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        BDDMockito.given(classificationClassModel.getCode()).willReturn("classificationClass");
        ClassificationSystemVersionModel classificationSystemVersionModel = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        BDDMockito.given(classificationSystemVersionModel.getCatalog()).willReturn(classificationSystemModel);
        BDDMockito.given(classificationSystemVersionModel.getVersion()).willReturn("ClassificationVersion");
        ClassAttributeAssignmentModel classAttributeAssignmentModel = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(classAttributeAssignmentModel.getClassificationAttribute()).willReturn(classificationAttributeModel);
        BDDMockito.given(classAttributeAssignmentModel.getSystemVersion()).willReturn(classificationSystemVersionModel);
        BDDMockito.given(classAttributeAssignmentModel.getClassificationClass()).willReturn(classificationClassModel);
        return classAttributeAssignmentModel;
    }
}
