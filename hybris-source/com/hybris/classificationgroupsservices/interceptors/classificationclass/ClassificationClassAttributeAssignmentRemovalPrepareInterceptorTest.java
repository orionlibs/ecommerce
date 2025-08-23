package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;
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
public class ClassificationClassAttributeAssignmentRemovalPrepareInterceptorTest
{
    @Mock
    private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;
    @Mock
    private ModelService modelService;
    @Mock
    private InterceptorContext interceptorContext;
    @InjectMocks
    private ClassificationClassAttributeAssignmentRemovalPrepareInterceptor interceptor;


    @Test
    public void shouldNotRemoveFeatureGroupAssignmentOfNewClassAttributeAssignment()
    {
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(classificationClass))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(Boolean.valueOf(this.classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass))).willReturn(Boolean.valueOf(true));
        this.interceptor.onPrepare(classificationClass, this.interceptorContext);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should(Mockito.never())).removeFeatureGroupAssignments((InterceptorContext)Matchers.any(), (ClassificationClassModel)Matchers.any());
    }


    @Test
    public void shouldInvokeRemovingFeatureGroupAssignmentIfClassAttributeAssignmentIsNotNew()
    {
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(classificationClass))).willReturn(Boolean.valueOf(false));
        BDDMockito.given(Boolean.valueOf(this.classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass))).willReturn(Boolean.valueOf(true));
        this.interceptor.onPrepare(classificationClass, this.interceptorContext);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should()).removeFeatureGroupAssignments(this.interceptorContext, classificationClass);
    }
}
