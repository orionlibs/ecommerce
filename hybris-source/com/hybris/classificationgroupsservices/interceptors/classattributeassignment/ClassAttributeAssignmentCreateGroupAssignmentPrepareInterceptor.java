package com.hybris.classificationgroupsservices.interceptors.classattributeassignment;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import org.springframework.beans.factory.annotation.Required;

public class ClassAttributeAssignmentCreateGroupAssignmentPrepareInterceptor implements PrepareInterceptor<ClassAttributeAssignmentModel>
{
    private ModelService modelService;
    private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;


    public void onPrepare(ClassAttributeAssignmentModel classAttributeAssignmentModel, InterceptorContext ctx)
    {
        boolean instanceOfClassificationClass = this.classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classAttributeAssignmentModel.getClassificationClass());
        if(instanceOfClassificationClass && this.modelService.isNew(classAttributeAssignmentModel))
        {
            this.classFeatureGroupAssignmentService.createGroupAssignmentsForCategory(ctx, classAttributeAssignmentModel
                            .getClassificationClass(), classAttributeAssignmentModel);
            this.classFeatureGroupAssignmentService.createGroupAssignmentsForSubcategories(ctx, classAttributeAssignmentModel);
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
