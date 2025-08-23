package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationClassAttributeAssignmentRemovalPrepareInterceptor implements PrepareInterceptor<ClassificationClassModel>
{
    private ModelService modelService;
    private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;


    public void onPrepare(ClassificationClassModel classificationClassModel, InterceptorContext ctx)
    {
        if(this.classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClassModel) &&
                        !this.modelService.isNew(classificationClassModel))
        {
            this.classFeatureGroupAssignmentService.removeFeatureGroupAssignments(ctx, classificationClassModel);
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
