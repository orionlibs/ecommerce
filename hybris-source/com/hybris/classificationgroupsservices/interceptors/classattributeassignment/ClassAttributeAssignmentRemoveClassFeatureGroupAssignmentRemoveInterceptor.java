package com.hybris.classificationgroupsservices.interceptors.classattributeassignment;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class ClassAttributeAssignmentRemoveClassFeatureGroupAssignmentRemoveInterceptor implements RemoveInterceptor<ClassAttributeAssignmentModel>
{
    private ModelService modelService;
    private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;


    public void onRemove(ClassAttributeAssignmentModel classAttributeAssignment, InterceptorContext ctx)
    {
        if(!this.modelService.isNew(classAttributeAssignment) && this.classFeatureGroupAssignmentService
                        .isInstanceOfClassificationClass(classAttributeAssignment.getClassificationClass()))
        {
            this.classFeatureGroupAssignmentService.removeAllFeatureGroupAssignments(ctx, List.of(classAttributeAssignment));
        }
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setClassFeatureGroupAssignmentService(ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService)
    {
        this.classFeatureGroupAssignmentService = classFeatureGroupAssignmentService;
    }
}
