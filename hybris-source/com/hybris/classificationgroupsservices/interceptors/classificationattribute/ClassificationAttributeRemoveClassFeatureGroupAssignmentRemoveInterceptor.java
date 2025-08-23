package com.hybris.classificationgroupsservices.interceptors.classificationattribute;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.RemoveInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationAttributeRemoveClassFeatureGroupAssignmentRemoveInterceptor implements RemoveInterceptor<ClassificationAttributeModel>
{
    private ModelService modelService;
    private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;


    public void onRemove(ClassificationAttributeModel classificationAttribute, InterceptorContext ctx)
    {
        if(!this.modelService.isNew(classificationAttribute))
        {
            this.classFeatureGroupAssignmentService.removeAllFeatureGroupAssignments(ctx, classificationAttribute);
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
