package com.hybris.classificationgroupsservices.handlers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassFeatureGroupIndexHandlerTest
{
    private final ClassFeatureGroupIndexHandler handler = new ClassFeatureGroupIndexHandler();


    @Test
    public void shouldBeHandledCaseWhenGroupIsNotAssignedToAnyCategory()
    {
        ClassFeatureGroupModel groupModel = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        BDDMockito.given(groupModel.getClassificationClass()).willReturn(null);
        int index = this.handler.get(groupModel).intValue();
        Assertions.assertThat(index).isEqualTo(-1);
    }


    @Test
    public void shouldCorrectIndexBeReturned()
    {
        ClassFeatureGroupModel group02 = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassFeatureGroupModel group01 = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        ClassFeatureGroupModel group03 = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        BDDMockito.given(group02.getClassificationClass()).willReturn(classificationClass);
        BDDMockito.given(classificationClass.getClassFeatureGroups()).willReturn(List.of(group01, group02, group03));
        int index = this.handler.get(group02).intValue();
        Assertions.assertThat(index).isEqualTo(2);
    }


    @Test
    public void shouldBeHandledCaseWhenGroupIsAssignedToCategoryButNotYetSaved()
    {
        ClassFeatureGroupModel group02 = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassFeatureGroupModel group01 = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        ClassFeatureGroupModel group03 = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        BDDMockito.given(group02.getClassificationClass()).willReturn(classificationClass);
        BDDMockito.given(classificationClass.getClassFeatureGroups()).willReturn(List.of(group01, group03));
        int index = this.handler.get(group02).intValue();
        Assertions.assertThat(index).isEqualTo(-1);
    }
}
