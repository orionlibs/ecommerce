package com.hybris.pcmbackoffice.classificationgroupsbackoffice.renderers;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationAttributeCellRenderer extends AbstractClassificationCellRenderer<ClassificationAttributeModel>
{
    private ModelService modelService;
    private GroupAssignmentCellValueProvider cellValueProvider;


    protected String getValue(ClassificationAttributeModel classificationAttributeModel, WidgetInstanceManager widgetInstanceManager)
    {
        Optional<ClassFeatureGroupAssignmentModel> matchingGroupAssignment = findMatchingGroupAssignment(classificationAttributeModel, widgetInstanceManager);
        if(matchingGroupAssignment.isPresent())
        {
            return (String)this.cellValueProvider.apply(matchingGroupAssignment.get());
        }
        return "";
    }


    protected Optional<ClassFeatureGroupAssignmentModel> findMatchingGroupAssignment(ClassificationAttributeModel classificationAttributeModel, WidgetInstanceManager widgetInstanceManager)
    {
        ClassificationClassModel classificationClass = (ClassificationClassModel)widgetInstanceManager.getModel().getValue("inlinecurrentObject", ClassificationClassModel.class);
        this.modelService.refresh(classificationClass);
        if(classificationClass == null)
        {
            return Optional.empty();
        }
        return classificationClass.getClassFeatureGroups().stream()
                        .flatMap(group -> group.getClassFeatureGroupAssignments().stream()).filter(container -> container.getClassAttributeAssignment().getClassificationAttribute().equals(classificationAttributeModel))
                        .findFirst();
    }


    @Required
    public void setCellValueProvider(GroupAssignmentCellValueProvider cellValueProvider)
    {
        this.cellValueProvider = cellValueProvider;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
