package com.hybris.classificationgroupsservices.handlers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import org.assertj.core.api.AbstractCharSequenceAssert;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

public class ClassFeatureGroupAssignmentFullQualifierHandlerTest
{
    ClassFeatureGroupAssignmentFullQualifierHandler classAttributeAssignmentFullQualifierAttributeHandler = new ClassFeatureGroupAssignmentFullQualifierHandler();


    @Test
    public void shouldGetFullQualifier()
    {
        ClassificationSystemModel catalog = (ClassificationSystemModel)Mockito.mock(ClassificationSystemModel.class);
        BDDMockito.given(catalog.getId()).willReturn("Catalog $Id");
        ClassificationSystemVersionModel version = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        BDDMockito.given(version.getVersion()).willReturn("Catalog Version");
        BDDMockito.given(version.getCatalog()).willReturn(catalog);
        ClassificationAttributeModel attribute = (ClassificationAttributeModel)Mockito.mock(ClassificationAttributeModel.class);
        BDDMockito.given(attribute.getCode()).willReturn("Attribute Code");
        BDDMockito.given(attribute.getSystemVersion()).willReturn(version);
        ClassificationClassModel category = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        BDDMockito.given(category.getCode()).willReturn("Category Code");
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(classAttributeAssignment.getSystemVersion()).willReturn(version);
        BDDMockito.given(classAttributeAssignment.getClassificationClass()).willReturn(category);
        BDDMockito.given(classAttributeAssignment.getClassificationAttribute()).willReturn(attribute);
        ClassFeatureGroupAssignmentModel classFeatureGroupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        BDDMockito.given(classFeatureGroupAssignment.getClassAttributeAssignment()).willReturn(classAttributeAssignment);
        String fullQualifier = this.classAttributeAssignmentFullQualifierAttributeHandler.get(classFeatureGroupAssignment);
        ((AbstractCharSequenceAssert)Assertions.assertThat(fullQualifier).as("the field should contain full attribute qualifier separated by '_'", new Object[0]))
                        .isEqualTo("CatalogId_CatalogVersion_CategoryCode_AttributeCode");
    }
}
