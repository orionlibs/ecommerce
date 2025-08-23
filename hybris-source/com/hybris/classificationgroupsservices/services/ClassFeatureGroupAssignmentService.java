package com.hybris.classificationgroupsservices.services;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ClassFeatureGroupAssignmentService
{
    ClassFeatureGroupAssignmentModel createClassFeatureGroupAssignment(ClassAttributeAssignmentModel paramClassAttributeAssignmentModel, ClassificationClassModel paramClassificationClassModel);


    Optional<ClassFeatureGroupAssignmentModel> findFeatureGroupAssignment(ClassAttributeAssignmentModel paramClassAttributeAssignmentModel, ClassificationClassModel paramClassificationClassModel);


    Collection<ClassFeatureGroupAssignmentModel> findAllFeatureGroupAssignments(ClassAttributeAssignmentModel paramClassAttributeAssignmentModel);


    List<ClassFeatureGroupAssignmentModel> findAllFeatureGroupAssignments();


    void createLackingFeatureGroupAssignments(InterceptorContext paramInterceptorContext, ClassificationClassModel paramClassificationClassModel, ClassAttributeAssignmentModel paramClassAttributeAssignmentModel);


    void removeAllFeatureGroupAssignments(InterceptorContext paramInterceptorContext, ClassificationAttributeModel paramClassificationAttributeModel);


    void removeAllFeatureGroupAssignments(InterceptorContext paramInterceptorContext, List<ClassAttributeAssignmentModel> paramList);


    List<ClassificationClassModel> findUnassignedSupercategories(ClassificationClassModel paramClassificationClassModel);


    void removeFeatureGroupAssignments(InterceptorContext paramInterceptorContext, ClassificationClassModel paramClassificationClassModel);


    void removeFeatureGroupAssignmentsInSubCategories(InterceptorContext paramInterceptorContext, ClassificationClassModel paramClassificationClassModel, List<ClassAttributeAssignmentModel> paramList);


    void removeFeatureGroupAssignmentsInCategory(InterceptorContext paramInterceptorContext, ClassificationClassModel paramClassificationClassModel, List<ClassAttributeAssignmentModel> paramList);


    void createGroupAssignmentsForSubcategories(InterceptorContext paramInterceptorContext, ClassAttributeAssignmentModel paramClassAttributeAssignmentModel);


    void createGroupAssignmentsForCategory(InterceptorContext paramInterceptorContext, ClassificationClassModel paramClassificationClassModel, ClassAttributeAssignmentModel paramClassAttributeAssignmentModel);


    boolean isInstanceOfClassificationClass(ClassificationClassModel paramClassificationClassModel);
}
