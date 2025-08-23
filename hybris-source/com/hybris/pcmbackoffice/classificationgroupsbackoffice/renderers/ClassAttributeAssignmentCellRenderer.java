package com.hybris.pcmbackoffice.classificationgroupsbackoffice.renderers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class ClassAttributeAssignmentCellRenderer extends AbstractClassificationCellRenderer<ClassAttributeAssignmentModel>
{
    private GroupAssignmentCellValueProvider cellValueProvider;


    protected String getValue(ClassAttributeAssignmentModel classAttributeAssignment, WidgetInstanceManager widgetInstanceManager)
    {
        Optional<ClassFeatureGroupAssignmentModel> matchingGroupAssignment = findMatchingGroupAssignment(classAttributeAssignment);
        if(matchingGroupAssignment.isPresent())
        {
            return (String)this.cellValueProvider.apply(matchingGroupAssignment.get());
        }
        return "";
    }


    protected Optional<ClassFeatureGroupAssignmentModel> findMatchingGroupAssignment(ClassAttributeAssignmentModel classAttributeAssignment)
    {
        ClassificationClassModel classificationClass = classAttributeAssignment.getClassificationClass();
        if(classificationClass == null || classificationClass.getClassFeatureGroups() == null)
        {
            return Optional.empty();
        }
        return classificationClass.getClassFeatureGroups().stream()
                        .flatMap(group -> group.getClassFeatureGroupAssignments().stream())
                        .filter(container -> container.getClassAttributeAssignment().equals(classAttributeAssignment)).findFirst();
    }


    @Required
    public void setCellValueProvider(GroupAssignmentCellValueProvider cellValueProvider)
    {
        this.cellValueProvider = cellValueProvider;
    }
}
