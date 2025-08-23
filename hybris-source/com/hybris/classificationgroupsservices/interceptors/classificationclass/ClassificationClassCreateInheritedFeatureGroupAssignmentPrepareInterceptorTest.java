package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
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
public class ClassificationClassCreateInheritedFeatureGroupAssignmentPrepareInterceptorTest
{
    @Mock
    ModelService modelService;
    @Mock
    ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;
    @Mock
    ClassificationClassModel classificationClass;
    @Mock
    InterceptorContext ctx;
    @InjectMocks
    ClassificationClassCreateInheritedFeatureGroupAssignmentPrepareInterceptor interceptor;


    @Test
    public void shouldNotCreateFeatureGroupAssignmentIfThereIsNoClassAttributeAssignment()
    {
        BDDMockito.given(this.classificationClass.getAllClassificationAttributeAssignments()).willReturn(List.of());
        BDDMockito.given(Boolean.valueOf(this.classFeatureGroupAssignmentService.isInstanceOfClassificationClass(this.classificationClass))).willReturn(Boolean.valueOf(true));
        this.interceptor.onPrepare(this.classificationClass, this.ctx);
        ((InterceptorContext)BDDMockito.then(this.ctx).should(Mockito.never())).registerElementFor(Matchers.any(), (PersistenceOperation)Matchers.any());
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should(Mockito.never())).createClassFeatureGroupAssignment((ClassAttributeAssignmentModel)Matchers.any(), (ClassificationClassModel)Matchers.any());
    }


    @Test
    public void shouldCreateLackingFeatureGroupAssignmentsForAllClassAttributeAssignments()
    {
        ClassAttributeAssignmentModel attribute1 = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassAttributeAssignmentModel attribute2 = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(this.classificationClass.getAllClassificationAttributeAssignments()).willReturn(List.of(attribute1, attribute2));
        BDDMockito.given(Boolean.valueOf(this.classFeatureGroupAssignmentService.isInstanceOfClassificationClass(this.classificationClass))).willReturn(Boolean.valueOf(true));
        this.interceptor.onPrepare(this.classificationClass, this.ctx);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should()).createLackingFeatureGroupAssignments(this.ctx, this.classificationClass, attribute1);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should()).createLackingFeatureGroupAssignments(this.ctx, this.classificationClass, attribute2);
    }
}
