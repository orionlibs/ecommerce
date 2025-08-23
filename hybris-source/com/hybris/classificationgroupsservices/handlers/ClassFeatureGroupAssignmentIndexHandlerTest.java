package com.hybris.classificationgroupsservices.handlers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassFeatureGroupAssignmentIndexHandlerTest
{
    private final ClassFeatureGroupAssignmentIndexHandler handler = new ClassFeatureGroupAssignmentIndexHandler();


    @Test
    public void shouldBeHandledCaseWhenFeatureIsNotAssignedToAnyGroup()
    {
        ClassFeatureGroupAssignmentModel groupAssignmentModel = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        BDDMockito.given(groupAssignmentModel.getClassFeatureGroup()).willReturn(null);
        int index = this.handler.get(groupAssignmentModel).intValue();
        Assertions.assertThat(index).isEqualTo(-1);
    }


    @Test
    public void shouldCorrectIndexBeReturned()
    {
        ClassFeatureGroupAssignmentModel groupAssignment02 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassFeatureGroupModel groupModel = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        ClassFeatureGroupAssignmentModel groupAssignment01 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassFeatureGroupAssignmentModel groupAssignment03 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        BDDMockito.given(groupModel.getClassFeatureGroupAssignments())
                        .willReturn(List.of(groupAssignment01, groupAssignment02, groupAssignment03));
        BDDMockito.given(groupAssignment02.getClassFeatureGroup()).willReturn(groupModel);
        int index = this.handler.get(groupAssignment02).intValue();
        Assertions.assertThat(index).isEqualTo(2);
    }


    @Test
    public void shouldBeHandledCaseWhenFeatureIsAssignedToGroupButNotYetSaved()
    {
        ClassFeatureGroupAssignmentModel groupAssignment02 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassFeatureGroupModel groupModel = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        ClassFeatureGroupAssignmentModel groupAssignment01 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassFeatureGroupAssignmentModel groupAssignment03 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        BDDMockito.given(groupModel.getClassFeatureGroupAssignments()).willReturn(List.of(groupAssignment01, groupAssignment03));
        BDDMockito.given(groupAssignment02.getClassFeatureGroup()).willReturn(groupModel);
        int index = this.handler.get(groupAssignment02).intValue();
        Assertions.assertThat(index).isEqualTo(-1);
    }
}
