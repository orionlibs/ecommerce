package com.hybris.pcmbackoffice.classificationgroupsbackoffice.renderers;

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
public class IndexGroupAssignmentCellValueProviderTest
{
    private final IndexGroupAssignmentCellValueProvider provider = new IndexGroupAssignmentCellValueProvider();


    @Test
    public void shouldReturnCorrectIndex()
    {
        ClassFeatureGroupAssignmentModel groupAssignment02 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassFeatureGroupModel group = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        BDDMockito.given(groupAssignment02.getClassFeatureGroup()).willReturn(group);
        ClassFeatureGroupAssignmentModel groupAssignment01 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassFeatureGroupAssignmentModel groupAssignment03 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        BDDMockito.given(group.getClassFeatureGroupAssignments()).willReturn(List.of(groupAssignment01, groupAssignment02, groupAssignment03));
        String output = this.provider.apply(groupAssignment02);
        Assertions.assertThat(output).isEqualTo("2");
    }


    @Test
    public void shouldReturnEmptyValueWhenFeatureIsNotAssignedToAnyGroup()
    {
        ClassFeatureGroupAssignmentModel model = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        BDDMockito.given(model.getClassFeatureGroup()).willReturn(null);
        String output = this.provider.apply(model);
        Assertions.assertThat(output).isEmpty();
    }


    @Test
    public void shouldReturnEmptyValueWhenFeatureIsAssignedToGroupButNotYetSaved()
    {
        ClassFeatureGroupAssignmentModel groupAssignment02 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassFeatureGroupModel group = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        BDDMockito.given(groupAssignment02.getClassFeatureGroup()).willReturn(group);
        ClassFeatureGroupAssignmentModel groupAssignment01 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassFeatureGroupAssignmentModel groupAssignment03 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        BDDMockito.given(group.getClassFeatureGroupAssignments()).willReturn(List.of(groupAssignment01, groupAssignment03));
        String output = this.provider.apply(groupAssignment02);
        Assertions.assertThat(output).isEmpty();
    }
}
