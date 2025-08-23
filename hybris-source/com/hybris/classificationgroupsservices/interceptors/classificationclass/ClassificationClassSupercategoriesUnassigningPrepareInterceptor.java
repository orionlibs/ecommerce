package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationClassSupercategoriesUnassigningPrepareInterceptor implements PrepareInterceptor<ClassificationClassModel>
{
    private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;
    private ModelService modelService;


    public void onPrepare(ClassificationClassModel classificationClass, InterceptorContext ctx)
    {
        if(this.classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass) &&
                        !this.modelService.isNew(classificationClass))
        {
            List<ClassificationClassModel> unassignedCategories = this.classFeatureGroupAssignmentService.findUnassignedSupercategories(classificationClass);
            unassignedCategories.forEach(unassignedCategory -> {
                List<ClassAttributeAssignmentModel> classificationAttributeAssignments = unassignedCategory.getAllClassificationAttributeAssignments();
                this.classFeatureGroupAssignmentService.removeFeatureGroupAssignmentsInCategory(ctx, classificationClass, classificationAttributeAssignments);
                this.classFeatureGroupAssignmentService.removeFeatureGroupAssignmentsInSubCategories(ctx, classificationClass, classificationAttributeAssignments);
            });
        }
    }


    @Required
    public void setClassFeatureGroupAssignmentService(ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService)
    {
        this.classFeatureGroupAssignmentService = classFeatureGroupAssignmentService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
