package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import com.hybris.classificationgroupsservices.services.ClassFeatureGroupAssignmentService;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PrepareInterceptor;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Required;

public class ClassificationClassCreateInheritedFeatureGroupAssignmentPrepareInterceptor implements PrepareInterceptor<ClassificationClassModel>
{
    private ModelService modelService;
    private ClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;


    public void onPrepare(ClassificationClassModel classificationClass, InterceptorContext ctx)
    {
        if(this.classFeatureGroupAssignmentService.isInstanceOfClassificationClass(classificationClass))
        {
            List<CategoryModel> supercategories = classificationClass.getSupercategories();
            Objects.requireNonNull(this.modelService);
            if(supercategories != null && supercategories.stream().anyMatch(this.modelService::isNew))
            {
                return;
            }
            List<ClassAttributeAssignmentModel> classAttributeAssignments = classificationClass.getAllClassificationAttributeAssignments();
            for(ClassAttributeAssignmentModel classAttributeAssignment : classAttributeAssignments)
            {
                this.classFeatureGroupAssignmentService.createLackingFeatureGroupAssignments(ctx, classificationClass, classAttributeAssignment);
            }
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
