package com.hybris.pcmbackoffice.classificationgroupsbackoffice.renderers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassFeatureGroupAssignmentIndexRendererTest
{
    @InjectMocks
    private ClassFeatureGroupAssignmentIndexRenderer renderer;


    @Test
    public void shouldReturnEmptyValueIfIndexIsNull()
    {
        ClassFeatureGroupAssignmentModel classFeatureGroupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        BDDMockito.given(classFeatureGroupAssignment.getIndex()).willReturn(null);
        String value = this.renderer.getValue(classFeatureGroupAssignment, (WidgetInstanceManager)Mockito.mock(WidgetInstanceManager.class));
        Assertions.assertThat(value).isEqualTo("");
    }


    @Test
    public void shouldReturnEmptyValueIfIndexHasNegativeValue()
    {
        ClassFeatureGroupAssignmentModel classFeatureGroupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        BDDMockito.given(classFeatureGroupAssignment.getIndex()).willReturn(Integer.valueOf(-1));
        String value = this.renderer.getValue(classFeatureGroupAssignment, (WidgetInstanceManager)Mockito.mock(WidgetInstanceManager.class));
        Assertions.assertThat(value).isEqualTo("");
    }


    @Test
    public void shouldReturnIndex()
    {
        ClassFeatureGroupAssignmentModel classFeatureGroupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        BDDMockito.given(classFeatureGroupAssignment.getIndex()).willReturn(null);
        String value = this.renderer.getValue(classFeatureGroupAssignment, (WidgetInstanceManager)Mockito.mock(WidgetInstanceManager.class));
        Assertions.assertThat(value).isEqualTo("");
    }
}
