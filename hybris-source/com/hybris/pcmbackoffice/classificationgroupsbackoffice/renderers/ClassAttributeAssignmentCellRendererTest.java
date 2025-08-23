package com.hybris.pcmbackoffice.classificationgroupsbackoffice.renderers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassAttributeAssignmentCellRendererTest
{
    private ClassAttributeAssignmentCellRenderer renderer = new ClassAttributeAssignmentCellRenderer();


    @Test
    public void shouldFindMatchGroupAssignment()
    {
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassFeatureGroupModel group = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        ClassFeatureGroupAssignmentModel assignmentGroup = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassAttributeAssignmentModel assignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(classificationClass.getClassFeatureGroups()).willReturn(List.of(group));
        BDDMockito.given(group.getClassFeatureGroupAssignments()).willReturn(List.of(assignmentGroup));
        BDDMockito.given(assignmentGroup.getClassAttributeAssignment()).willReturn(assignment);
        BDDMockito.given(assignment.getClassificationClass()).willReturn(classificationClass);
        Optional<ClassFeatureGroupAssignmentModel> output = this.renderer.findMatchingGroupAssignment(assignment);
        Assertions.assertThat(output).isPresent();
    }


    @Test
    public void shouldReturnEmptyOptionalWhenFeatureIsNotAssignedToGroup()
    {
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassAttributeAssignmentModel assignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(assignment.getClassificationClass()).willReturn(classificationClass);
        BDDMockito.given(classificationClass.getClassFeatureGroups()).willReturn(List.of());
        Optional<ClassFeatureGroupAssignmentModel> output = this.renderer.findMatchingGroupAssignment(assignment);
        Assertions.assertThat(output).isEmpty();
    }
}
