package com.hybris.pcmbackoffice.classificationgroupsbackoffice.renderers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassFeatureGroupIndexRendererTest
{
    @InjectMocks
    private ClassFeatureGroupIndexRenderer renderer;


    @Test
    public void shouldReturnEmptyValueIfIndexIsNull()
    {
        ClassFeatureGroupModel classFeatureGroup = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        BDDMockito.given(classFeatureGroup.getIndex()).willReturn(null);
        String value = this.renderer.getValue(classFeatureGroup, (WidgetInstanceManager)Mockito.mock(WidgetInstanceManager.class));
        Assertions.assertThat(value).isEqualTo("");
    }


    @Test
    public void shouldReturnEmptyValueIfIndexHasNegativeValue()
    {
        ClassFeatureGroupModel classFeatureGroup = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        BDDMockito.given(classFeatureGroup.getIndex()).willReturn(Integer.valueOf(-1));
        String value = this.renderer.getValue(classFeatureGroup, (WidgetInstanceManager)Mockito.mock(WidgetInstanceManager.class));
        Assertions.assertThat(value).isEqualTo("");
    }


    @Test
    public void shouldReturnIndex()
    {
        ClassFeatureGroupModel classFeatureGroup = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        BDDMockito.given(classFeatureGroup.getIndex()).willReturn(null);
        String value = this.renderer.getValue(classFeatureGroup, (WidgetInstanceManager)Mockito.mock(WidgetInstanceManager.class));
        Assertions.assertThat(value).isEqualTo("");
    }
}
