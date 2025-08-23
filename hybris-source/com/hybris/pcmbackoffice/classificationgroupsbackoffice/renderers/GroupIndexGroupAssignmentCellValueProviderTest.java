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
public class GroupIndexGroupAssignmentCellValueProviderTest
{
    private final GroupIndexGroupAssignmentCellValueProvider provider = new GroupIndexGroupAssignmentCellValueProvider();


    @Test
    public void shouldIndexBeRetrievedFromModel()
    {
        ClassFeatureGroupAssignmentModel groupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassFeatureGroupModel group = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        BDDMockito.given(groupAssignment.getClassFeatureGroup()).willReturn(group);
        int index = 7;
        BDDMockito.given(group.getIndex()).willReturn(Integer.valueOf(7));
        String output = this.provider.apply(groupAssignment);
        Assertions.assertThat(output).isEqualTo(String.valueOf(7));
    }


    @Test
    public void shouldReturnEmptyValueWhenIndexNotFound()
    {
        ClassFeatureGroupAssignmentModel groupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassFeatureGroupModel group = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        BDDMockito.given(groupAssignment.getClassFeatureGroup()).willReturn(group);
        BDDMockito.given(group.getIndex()).willReturn(Integer.valueOf(-1));
        String output = this.provider.apply(groupAssignment);
        Assertions.assertThat(output).isEmpty();
    }
}
