package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassificationClassSupercategoriesUnassigningPrepareInterceptorTest
{
    @Mock
    private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;
    @Mock
    private ModelService modelService;
    @Mock
    private InterceptorContext interceptorContext;
    @Mock
    private ClassificationClassModel classificationClass;
    @InjectMocks
    private ClassificationClassSupercategoriesUnassigningPrepareInterceptor interceptor;


    @Test
    public void shouldNotRemoveFeatureGroupAssignmentsIfThereIsNoUnassignedSupercategories()
    {
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(this.classificationClass))).willReturn(Boolean.valueOf(false));
        BDDMockito.given(this.classFeatureGroupAssignmentService.findUnassignedSupercategories(this.classificationClass)).willReturn(List.of());
        BDDMockito.given(Boolean.valueOf(this.classFeatureGroupAssignmentService.isInstanceOfClassificationClass(this.classificationClass))).willReturn(Boolean.valueOf(true));
        this.interceptor.onPrepare(this.classificationClass, this.interceptorContext);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should(Mockito.never())).removeFeatureGroupAssignmentsInCategory((InterceptorContext)Matchers.any(), (ClassificationClassModel)Matchers.any(), (List)Matchers.any());
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should(Mockito.never())).removeFeatureGroupAssignmentsInSubCategories((InterceptorContext)Matchers.any(), (ClassificationClassModel)Matchers.any(), (List)Matchers.any());
    }


    @Test
    public void shouldNotRemoveFeatureGroupAssignmentsIfClassificationClassIsNew()
    {
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(this.classificationClass))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(Boolean.valueOf(this.classFeatureGroupAssignmentService.isInstanceOfClassificationClass(this.classificationClass))).willReturn(Boolean.valueOf(true));
        this.interceptor.onPrepare(this.classificationClass, this.interceptorContext);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should(Mockito.never())).removeFeatureGroupAssignmentsInCategory((InterceptorContext)Matchers.any(), (ClassificationClassModel)Matchers.any(), (List)Matchers.any());
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should(Mockito.never())).removeFeatureGroupAssignmentsInSubCategories((InterceptorContext)Matchers.any(), (ClassificationClassModel)Matchers.any(), (List)Matchers.any());
    }


    @Test
    public void shouldRemoveFeatureGroupAssignments()
    {
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(this.classificationClass))).willReturn(Boolean.valueOf(false));
        ClassificationClassModel unassignedCategory1 = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassificationClassModel unassignedCategory2 = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        BDDMockito.given(this.classFeatureGroupAssignmentService.findUnassignedSupercategories(this.classificationClass))
                        .willReturn(List.of(unassignedCategory1, unassignedCategory2));
        List attributeAssignmentOfCategory1 = (List)Mockito.mock(List.class);
        List attributeAssignmentOfCategory2 = (List)Mockito.mock(List.class);
        BDDMockito.given(unassignedCategory1.getAllClassificationAttributeAssignments()).willReturn(attributeAssignmentOfCategory1);
        BDDMockito.given(unassignedCategory2.getAllClassificationAttributeAssignments()).willReturn(attributeAssignmentOfCategory2);
        BDDMockito.given(Boolean.valueOf(this.classFeatureGroupAssignmentService.isInstanceOfClassificationClass(this.classificationClass))).willReturn(Boolean.valueOf(true));
        this.interceptor.onPrepare(this.classificationClass, this.interceptorContext);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should()).removeFeatureGroupAssignmentsInCategory(this.interceptorContext, this.classificationClass, attributeAssignmentOfCategory1);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should()).removeFeatureGroupAssignmentsInSubCategories(this.interceptorContext, this.classificationClass, attributeAssignmentOfCategory1);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should()).removeFeatureGroupAssignmentsInCategory(this.interceptorContext, this.classificationClass, attributeAssignmentOfCategory2);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should()).removeFeatureGroupAssignmentsInSubCategories(this.interceptorContext, this.classificationClass, attributeAssignmentOfCategory2);
    }
}
