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
public class ClassFeatureGroupNameRendererTest
{
    @InjectMocks
    private ClassFeatureGroupNameRenderer renderer;


    @Test
    public void shouldReturnGroupIdIfNameIsNull()
    {
        String groupCode = "group_code";
        ClassFeatureGroupModel classFeatureGroup = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        BDDMockito.given(classFeatureGroup.getName()).willReturn(null);
        BDDMockito.given(classFeatureGroup.getCode()).willReturn("group_code");
        String value = this.renderer.getValue(classFeatureGroup, (WidgetInstanceManager)Mockito.mock(WidgetInstanceManager.class));
        Assertions.assertThat(value).isEqualTo("group_code");
    }


    @Test
    public void shouldReturnGroupName()
    {
        String groupName = "group_name";
        ClassFeatureGroupModel classFeatureGroup = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        BDDMockito.given(classFeatureGroup.getName()).willReturn("group_name");
        String value = this.renderer.getValue(classFeatureGroup, (WidgetInstanceManager)Mockito.mock(WidgetInstanceManager.class));
        Assertions.assertThat(value).isEqualTo("group_name");
    }
}
