package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationClassRemoveGroupAssignmentsRemoveInterceptor implements RemoveInterceptor<ClassificationClassModel>
{
    private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;


    public void onRemove(ClassificationClassModel classificationClassModel, InterceptorContext ctx)
    {
        if(this.classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClassModel))
        {
            classificationClassModel.getAllClassificationAttributeAssignments().forEach(classAttributeAssignment -> this.classFeatureGroupAssignmentService.findFeatureGroupAssignment(classAttributeAssignment, classificationClassModel).ifPresent(()));
        }
    }


    @Required
    public void setClassFeatureGroupAssignmentService(ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService)
    {
        this.classFeatureGroupAssignmentService = classFeatureGroupAssignmentService;
    }
}
