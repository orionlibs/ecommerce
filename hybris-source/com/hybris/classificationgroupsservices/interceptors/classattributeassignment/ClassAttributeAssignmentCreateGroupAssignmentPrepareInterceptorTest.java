package com.hybris.classificationgroupsservices.interceptors.classattributeassignment;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassAttributeAssignmentCreateGroupAssignmentPrepareInterceptorTest
{
    @Mock
    ModelService modelService;
    @Mock
    ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;
    @InjectMocks
    ClassAttributeAssignmentCreateGroupAssignmentPrepareInterceptor interceptor;


    @Test
    public void shouldNotCreateFeatureGroupAssignmentForExistingAttributeAssignment()
    {
        ClassAttributeAssignmentModel attributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        InterceptorContext interceptorContext = (InterceptorContext)Mockito.mock(InterceptorContext.class);
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(attributeAssignment))).willReturn(Boolean.valueOf(false));
        BDDMockito.given(Boolean.valueOf(this.classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(attributeAssignment.getClassificationClass()).willReturn(classificationClass);
        this.interceptor.onPrepare(attributeAssignment, interceptorContext);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should(Mockito.never())).createClassFeatureGroupAssignment((ClassAttributeAssignmentModel)Matchers.any(), (ClassificationClassModel)Matchers.any());
    }


    @Test
    public void shouldCreateFeatureGroupAssignmentForNewAttributeAssignment()
    {
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassAttributeAssignmentModel attributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        InterceptorContext interceptorContext = (InterceptorContext)Mockito.mock(InterceptorContext.class);
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(attributeAssignment))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(attributeAssignment.getClassificationClass()).willReturn(classificationClass);
        BDDMockito.given(Boolean.valueOf(this.classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass))).willReturn(Boolean.valueOf(true));
        this.interceptor.onPrepare(attributeAssignment, interceptorContext);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should()).createGroupAssignmentsForCategory(interceptorContext, classificationClass, attributeAssignment);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should()).createGroupAssignmentsForSubcategories(interceptorContext, attributeAssignment);
    }
}
