package com.hybris.classificationgroupsservices.interceptors.classificationattribute;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.model.ModelService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassificationAttributeRemoveClassFeatureGroupAssignmentRemoveInterceptorTest
{
    @Mock
    private ModelService modelService;
    @Mock
    private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;
    @Mock
    private InterceptorContext interceptorContext;
    @InjectMocks
    private ClassificationAttributeRemoveClassFeatureGroupAssignmentRemoveInterceptor interceptor;


    @Test
    public void shouldRemoveAllFeatureGroupAssignments()
    {
        ClassificationAttributeModel classificationAttribute = (ClassificationAttributeModel)Mockito.mock(ClassificationAttributeModel.class);
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(classificationAttribute))).willReturn(Boolean.valueOf(false));
        this.interceptor.onRemove(classificationAttribute, this.interceptorContext);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should()).removeAllFeatureGroupAssignments(this.interceptorContext, classificationAttribute);
    }


    @Test
    public void shouldNotRemoveAllFeatureGroupAssignments()
    {
        ClassificationAttributeModel classificationAttribute = (ClassificationAttributeModel)Mockito.mock(ClassificationAttributeModel.class);
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(classificationAttribute))).willReturn(Boolean.valueOf(true));
        this.interceptor.onRemove(classificationAttribute, this.interceptorContext);
        ((ClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should(Mockito.never())).removeAllFeatureGroupAssignments(this.interceptorContext, classificationAttribute);
    }
}
