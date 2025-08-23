package com.hybris.pcmbackoffice.classificationgroupsbackoffice.renderers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GroupNameGroupAssignmentCellValueProviderTest
{
    private final GroupNameGroupAssignmentCellValueProvider provider = new GroupNameGroupAssignmentCellValueProvider();


    @Test
    public void shouldReturnCodeWhenNameIsEmpty()
    {
        ClassFeatureGroupAssignmentModel groupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassFeatureGroupModel group = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        BDDMockito.given(groupAssignment.getClassFeatureGroup()).willReturn(group);
        String code = "group01";
        BDDMockito.given(group.getName()).willReturn(null);
        BDDMockito.given(group.getCode()).willReturn("group01");
        String output = this.provider.apply(groupAssignment);
        Assertions.assertThat(output).isEqualTo("group01");
    }


    @Test
    public void shouldReturnNameWhenItIsNotEmpty()
    {
        ClassFeatureGroupAssignmentModel groupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassFeatureGroupModel group = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        BDDMockito.given(groupAssignment.getClassFeatureGroup()).willReturn(group);
        String name = "group 01";
        String code = "group01";
        BDDMockito.given(group.getName()).willReturn("group 01");
        String output = this.provider.apply(groupAssignment);
        Assertions.assertThat(output).isEqualTo("group 01");
    }
}
