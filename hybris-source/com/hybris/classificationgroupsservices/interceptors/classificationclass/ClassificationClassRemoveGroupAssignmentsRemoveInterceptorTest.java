package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassificationClassRemoveGroupAssignmentsRemoveInterceptorTest
{
    @Mock
    private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;
    @InjectMocks
    private ClassificationClassRemoveGroupAssignmentsRemoveInterceptor interceptor;


    @Test
    public void shouldRemoveAllClassFeatureGroupAssignmentsFromRemovingCategory()
    {
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassAttributeAssignmentModel attributeAssignment1 = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassAttributeAssignmentModel attributeAssignment2 = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        InterceptorContext ctx = (InterceptorContext)Mockito.mock(InterceptorContext.class);
        ClassFeatureGroupAssignmentModel classFeaturegroupAssignment1 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassFeatureGroupAssignmentModel classFeaturegroupAssignment2 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        BDDMockito.given(classificationClass.getAllClassificationAttributeAssignments())
                        .willReturn(List.of(attributeAssignment1, attributeAssignment2));
        BDDMockito.given(this.classFeatureGroupAssignmentService.findFeatureGroupAssignment(attributeAssignment1, classificationClass))
                        .willReturn(Optional.of(classFeaturegroupAssignment1));
        BDDMockito.given(this.classFeatureGroupAssignmentService.findFeatureGroupAssignment(attributeAssignment2, classificationClass))
                        .willReturn(Optional.of(classFeaturegroupAssignment2));
        BDDMockito.given(Boolean.valueOf(this.classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass))).willReturn(Boolean.valueOf(true));
        this.interceptor.onRemove(classificationClass, ctx);
        ((InterceptorContext)Mockito.verify(ctx)).registerElementFor(classFeaturegroupAssignment1, PersistenceOperation.DELETE);
        ((InterceptorContext)Mockito.verify(ctx)).registerElementFor(classFeaturegroupAssignment2, PersistenceOperation.DELETE);
        Mockito.verifyNoMoreInteractions(new Object[] {ctx});
    }
}
