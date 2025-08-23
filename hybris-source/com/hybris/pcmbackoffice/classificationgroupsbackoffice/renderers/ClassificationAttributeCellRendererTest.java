package com.hybris.pcmbackoffice.classificationgroupsbackoffice.renderers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;
import com.hybris.cockpitng.core.model.WidgetModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ClassificationAttributeCellRendererTest
{
    @Mock
    private ModelService modelService;
    @InjectMocks
    private ClassificationAttributeCellRenderer renderer = new ClassificationAttributeCellRenderer();


    @Test
    public void shouldFindMatchGroupAssignment()
    {
        WidgetInstanceManager wim = (WidgetInstanceManager)Mockito.mock(WidgetInstanceManager.class);
        ClassificationClassModel classificationClass = mockClassificationClass(wim);
        ClassFeatureGroupModel group = (ClassFeatureGroupModel)Mockito.mock(ClassFeatureGroupModel.class);
        ClassFeatureGroupAssignmentModel assignmentGroup = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassAttributeAssignmentModel assignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationAttributeModel attribute = (ClassificationAttributeModel)Mockito.mock(ClassificationAttributeModel.class);
        BDDMockito.given(classificationClass.getClassFeatureGroups()).willReturn(List.of(group));
        BDDMockito.given(group.getClassFeatureGroupAssignments()).willReturn(List.of(assignmentGroup));
        BDDMockito.given(assignmentGroup.getClassAttributeAssignment()).willReturn(assignment);
        BDDMockito.given(assignment.getClassificationAttribute()).willReturn(attribute);
        Optional<ClassFeatureGroupAssignmentModel> output = this.renderer.findMatchingGroupAssignment(attribute, wim);
        ((ModelService)BDDMockito.then(this.modelService).should()).refresh(classificationClass);
        Assertions.assertThat(output).isPresent();
    }


    @Test
    public void shouldReturnEmptyOptionalWhenFeatureIsNotAssignedToGroup()
    {
        WidgetInstanceManager wim = (WidgetInstanceManager)Mockito.mock(WidgetInstanceManager.class);
        ClassificationClassModel classificationClass = mockClassificationClass(wim);
        BDDMockito.given(classificationClass.getClassFeatureGroups()).willReturn(List.of());
        Optional<ClassFeatureGroupAssignmentModel> output = this.renderer.findMatchingGroupAssignment((ClassificationAttributeModel)Mockito.mock(ClassificationAttributeModel.class), wim);
        ((ModelService)BDDMockito.then(this.modelService).should()).refresh(classificationClass);
        Assertions.assertThat(output).isEmpty();
    }


    private ClassificationClassModel mockClassificationClass(WidgetInstanceManager wim)
    {
        WidgetModel widgetModel = (WidgetModel)Mockito.mock(WidgetModel.class);
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        BDDMockito.given(wim.getModel()).willReturn(widgetModel);
        BDDMockito.given(widgetModel.getValue((String)Matchers.any(), (Class)Matchers.any())).willReturn(classificationClass);
        return classificationClass;
    }
}
