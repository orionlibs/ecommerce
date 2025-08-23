package com.hybris.classificationgroupsservices.interceptors.classattributeassignment;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassAttributeAssignmentRemoveClassFeatureGroupAssignmentRemoveInterceptorTest
{
    @Mock
    private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;
    @Mock
    private ModelService modelService;
    @Mock
    private InterceptorContext interceptorContext;
    @InjectMocks
    private ClassAttributeAssignmentRemoveClassFeatureGroupAssignmentRemoveInterceptor interceptor;


    @Test
    public void shouldNotRemoveFeatureGroupAssignmentIfModelIsNew()
    {
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(classAttributeAssignment))).willReturn(Boolean.valueOf(true));
        this.interceptor.onRemove(classAttributeAssignment, this.interceptorContext);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should(Mockito.never())).removeAllFeatureGroupAssignments((InterceptorContext)Matchers.any(InterceptorContext.class),
                        (List)Matchers.any(List.class));
    }


    @Test
    public void shouldNotRemoveFeatureGroupAssignmentIfCategoryIsNotInstanceOfClassificationClass()
    {
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(MockFlexibleTypeClass.class);
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(classAttributeAssignment))).willReturn(Boolean.valueOf(false));
        BDDMockito.given(classAttributeAssignment.getClassificationClass()).willReturn(classificationClass);
        BDDMockito.given(Boolean.valueOf(this.classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass))).willReturn(Boolean.valueOf(false));
        this.interceptor.onRemove(classAttributeAssignment, this.interceptorContext);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should(Mockito.never())).removeAllFeatureGroupAssignments((InterceptorContext)Matchers.any(InterceptorContext.class),
                        (List)Matchers.any(List.class));
    }


    @Test
    public void shouldRemoveFeatureGroupAssignment()
    {
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(classAttributeAssignment))).willReturn(Boolean.valueOf(false));
        BDDMockito.given(classAttributeAssignment.getClassificationClass()).willReturn(classificationClass);
        BDDMockito.given(Boolean.valueOf(this.classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass))).willReturn(Boolean.valueOf(true));
        this.interceptor.onRemove(classAttributeAssignment, this.interceptorContext);
        ArgumentCaptor<List> captor = ArgumentCaptor.forClass(List.class);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should()).removeAllFeatureGroupAssignments((InterceptorContext)Matchers.eq(this.interceptorContext), (List)captor
                        .capture());
        Assertions.assertThat((List)captor.getValue()).containsExactly(new Object[] {classAttributeAssignment});
    }
}
