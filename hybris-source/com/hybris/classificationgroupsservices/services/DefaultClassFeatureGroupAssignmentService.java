package com.hybris.classificationgroupsservices.services;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultClassFeatureGroupAssignmentService implements ClassFeatureGroupAssignmentService
{
    private static final String ATTRIBUTE_PARAM = "attributePk";
    private static final String CATEGORY_PARAM = "categoryPk";
    private static final String FEATURE_GROUP_ASSIGNMENTS_QUERY = String.format("select {cfga.%1$s} from {%2$s as cfga} where {cfga.%3$s} in ({{select {c.%1$s} from {%4$s as c} where {c.%1$s} = ?pk}})",
                    new Object[] {"pk", "ClassFeatureGroupAssignment", "classAttributeAssignment", "ClassAttributeAssignment"});
    private static final String FEATURE_GROUP_ASSIGNMENTS_FROM_ATTRIBUTE_QUERY = String.format("select {caa.%1$s} from {%2$s as caa} where {caa.%3$s}  = ?pk", new Object[] {"pk", "ClassAttributeAssignment", "classificationAttribute"});
    private static final String UNIQUE_FEATURE_GROUP_ASSIGNMENT_QUERY = String.format("select {cfga.%1$s} from {%2$s as cfga} where {cfga.%3$s} in ({{select {c.%1$s} from {%4$s as c} where {c.%1$s} = ?%5$s}}) and {cfga.%6$s} in ({{ select {cc.%1$s} from {%7$s as cc} where {cc.%1$s} = ?%8$s}})",
                    new Object[] {"pk", "ClassFeatureGroupAssignment", "classAttributeAssignment", "ClassAttributeAssignment", "attributePk", "classificationClass", "ClassificationClass", "categoryPk"});
    private static final String ALL_FEATURE_GROUP_ASSIGNMENTS_QUERY = String.format("select {%s} from {%s}", new Object[] {"pk", "ClassFeatureGroupAssignment"});
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultClassFeatureGroupAssignmentService.class);
    private ModelService modelService;
    private FlexibleSearchService flexibleSearchService;


    public ClassFeatureGroupAssignmentModel createClassFeatureGroupAssignment(ClassAttributeAssignmentModel classAttributeAssignmentModel, ClassificationClassModel classificationClass)
    {
        ClassFeatureGroupAssignmentModel featureGroupAssignment = (ClassFeatureGroupAssignmentModel)this.modelService.create(ClassFeatureGroupAssignmentModel.class);
        featureGroupAssignment.setClassAttributeAssignment(classAttributeAssignmentModel);
        featureGroupAssignment.setClassificationClass(classificationClass);
        return featureGroupAssignment;
    }


    public Optional<ClassFeatureGroupAssignmentModel> findFeatureGroupAssignment(ClassAttributeAssignmentModel classAttributeAssignment, ClassificationClassModel classificationClass)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(UNIQUE_FEATURE_GROUP_ASSIGNMENT_QUERY);
        query.addQueryParameter("attributePk", classAttributeAssignment.getPk());
        query.addQueryParameter("categoryPk", classificationClass.getPk());
        try
        {
            return Optional.of((ClassFeatureGroupAssignmentModel)this.flexibleSearchService.searchUnique(query));
        }
        catch(ModelNotFoundException e)
        {
            LOGGER.info("Model not found", (Throwable)e);
            return Optional.empty();
        }
    }


    public Collection<ClassFeatureGroupAssignmentModel> findAllFeatureGroupAssignments(ClassAttributeAssignmentModel classAttributeAssignment)
    {
        SearchResult<ClassFeatureGroupAssignmentModel> searchResult = this.flexibleSearchService.search(FEATURE_GROUP_ASSIGNMENTS_QUERY, Map.of("pk", classAttributeAssignment.getPk()));
        return searchResult.getResult();
    }


    public List<ClassFeatureGroupAssignmentModel> findAllFeatureGroupAssignments()
    {
        SearchResult<ClassFeatureGroupAssignmentModel> searchResult = this.flexibleSearchService.search(ALL_FEATURE_GROUP_ASSIGNMENTS_QUERY);
        return searchResult.getResult();
    }


    public void createLackingFeatureGroupAssignments(InterceptorContext ctx, ClassificationClassModel classificationClass, ClassAttributeAssignmentModel classAttributeAssignment)
    {
        if(isGroupAssignmentNeeded(classificationClass, classAttributeAssignment))
        {
            registerGroupAssignmentsForCreation(ctx,
                            createClassFeatureGroupAssignment(classAttributeAssignment, classificationClass));
            createFeatureGroupAssignmentsForAllSubCategories(classificationClass, ctx, classAttributeAssignment);
        }
    }


    private boolean isGroupAssignmentNeeded(ClassificationClassModel classificationClassModel, ClassAttributeAssignmentModel classAttributeAssignment)
    {
        return (this.modelService.isNew(classificationClassModel) || (!this.modelService.isNew(classAttributeAssignment) &&
                        !hasClassFeatureGroupAssignment(classAttributeAssignment, classificationClassModel)));
    }


    private boolean hasClassFeatureGroupAssignment(ClassAttributeAssignmentModel classAttributeAssignment, ClassificationClassModel classificationClass)
    {
        return findFeatureGroupAssignment(classAttributeAssignment, classificationClass).isPresent();
    }


    private void createFeatureGroupAssignmentsForAllSubCategories(ClassificationClassModel classificationClass, InterceptorContext ctx, ClassAttributeAssignmentModel classAttributeAssignment)
    {
        Objects.requireNonNull(ClassificationClassModel.class);
        Objects.requireNonNull(ClassificationClassModel.class);
        classificationClass.getAllSubcategories().stream().filter(ClassificationClassModel.class::isInstance).map(ClassificationClassModel.class::cast).forEach(subCategory -> {
            if(isGroupAssignmentNeeded(subCategory, classAttributeAssignment))
            {
                registerGroupAssignmentsForCreation(ctx, createClassFeatureGroupAssignment(classAttributeAssignment, subCategory));
            }
        });
    }


    private void registerGroupAssignmentsForCreation(InterceptorContext ctx, ClassFeatureGroupAssignmentModel classFeatureGroupAssignment)
    {
        if(!isClassFeatureGroupAssignmentAlreadyRegister(ctx, classFeatureGroupAssignment))
        {
            ctx.registerElementFor(classFeatureGroupAssignment, PersistenceOperation.SAVE);
        }
    }


    private boolean isClassFeatureGroupAssignmentAlreadyRegister(InterceptorContext ctx, ClassFeatureGroupAssignmentModel classFeatureGroupAssignment)
    {
        Objects.requireNonNull(ClassFeatureGroupAssignmentModel.class);
        Objects.requireNonNull(ClassFeatureGroupAssignmentModel.class);
        return ctx.getElementsRegisteredFor(PersistenceOperation.SAVE).stream().filter(ClassFeatureGroupAssignmentModel.class::isInstance).map(ClassFeatureGroupAssignmentModel.class::cast)
                        .anyMatch(registeredFeatureGroupAssignment -> (registeredFeatureGroupAssignment.getClassificationClass().equals(classFeatureGroupAssignment.getClassificationClass()) && registeredFeatureGroupAssignment.getClassAttributeAssignment()
                                        .equals(classFeatureGroupAssignment.getClassAttributeAssignment())));
    }


    public void removeFeatureGroupAssignments(InterceptorContext ctx, ClassificationClassModel classificationClassModel)
    {
        List<ClassAttributeAssignmentModel> removedAttributesAssignments = findRemovedAttributeAssignments(classificationClassModel);
        removeAllFeatureGroupAssignments(ctx, removedAttributesAssignments);
    }


    public void removeAllFeatureGroupAssignments(InterceptorContext ctx, List<ClassAttributeAssignmentModel> removedAttributesAssignments)
    {
        removedAttributesAssignments.forEach(attribute -> {
            Collection<ClassFeatureGroupAssignmentModel> featureGroupAssignments = findAllFeatureGroupAssignments(attribute);
            featureGroupAssignments.forEach(());
        });
    }


    public void removeAllFeatureGroupAssignments(InterceptorContext ctx, ClassificationAttributeModel removedClassificationAttribute)
    {
        List<ClassAttributeAssignmentModel> classAttributeAssignments = (List<ClassAttributeAssignmentModel>)findAllFeatureGroupAssignments(removedClassificationAttribute).stream().filter(classAttributeAssignment -> isInstanceOfClassificationClass(classAttributeAssignment.getClassificationClass()))
                        .collect(Collectors.toList());
        removeAllFeatureGroupAssignments(ctx, classAttributeAssignments);
    }


    private List<ClassAttributeAssignmentModel> findAllFeatureGroupAssignments(ClassificationAttributeModel classificationAttribute)
    {
        SearchResult<ClassAttributeAssignmentModel> searchResult = this.flexibleSearchService.search(FEATURE_GROUP_ASSIGNMENTS_FROM_ATTRIBUTE_QUERY, Map.of("pk", classificationAttribute.getPk()));
        return searchResult.getResult();
    }


    private List<ClassAttributeAssignmentModel> findRemovedAttributeAssignments(ClassificationClassModel classificationClassModel)
    {
        List<ClassAttributeAssignmentModel> currentAttributeAssignments = classificationClassModel.getDeclaredClassificationAttributeAssignments();
        List<ClassAttributeAssignmentModel> declaredClassificationAttributeAssignments = (List<ClassAttributeAssignmentModel>)classificationClassModel.getItemModelContext().getOriginalValue("declaredClassificationAttributeAssignments");
        return (List<ClassAttributeAssignmentModel>)declaredClassificationAttributeAssignments.stream()
                        .filter(attribute -> !currentAttributeAssignments.contains(attribute)).collect(Collectors.toList());
    }


    public List<ClassificationClassModel> findUnassignedSupercategories(ClassificationClassModel classificationClassModel)
    {
        Objects.requireNonNull(ClassificationClassModel.class);
        Objects.requireNonNull(ClassificationClassModel.class);
        Collection<ClassificationClassModel> currentSupercategories = (Collection<ClassificationClassModel>)classificationClassModel.getSupercategories().stream().filter(ClassificationClassModel.class::isInstance).map(ClassificationClassModel.class::cast).collect(Collectors.toList());
        Collection<CategoryModel> declaredSupercategories = (Collection<CategoryModel>)classificationClassModel.getItemModelContext().getOriginalValue("supercategories");
        Objects.requireNonNull(ClassificationClassModel.class);
        Objects.requireNonNull(ClassificationClassModel.class);
        return (List<ClassificationClassModel>)declaredSupercategories.stream().filter(ClassificationClassModel.class::isInstance).map(ClassificationClassModel.class::cast)
                        .filter(category -> !currentSupercategories.contains(category))
                        .collect(Collectors.toList());
    }


    public void removeFeatureGroupAssignmentsInSubCategories(InterceptorContext ctx, ClassificationClassModel classificationClassModel, List<ClassAttributeAssignmentModel> classAttributeAssignments)
    {
        Objects.requireNonNull(ClassificationClassModel.class);
        Objects.requireNonNull(ClassificationClassModel.class);
        classificationClassModel.getAllSubcategories().stream().filter(ClassificationClassModel.class::isInstance).map(ClassificationClassModel.class::cast).forEach(subCategory -> removeFeatureGroupAssignmentsInCategory(ctx, subCategory, classAttributeAssignments));
    }


    public void removeFeatureGroupAssignmentsInCategory(InterceptorContext ctx, ClassificationClassModel classificationClass, List<ClassAttributeAssignmentModel> classificationAttributeAssignments)
    {
        classificationAttributeAssignments.stream().filter(classAttributeAssignment -> !classificationClass.getAllClassificationAttributeAssignments().contains(classAttributeAssignment))
                        .forEach(classAttributeAssignment -> findFeatureGroupAssignment(classAttributeAssignment, classificationClass).ifPresent(()));
    }


    public void createGroupAssignmentsForSubcategories(InterceptorContext ctx, ClassAttributeAssignmentModel classAttributeAssignmentModel)
    {
        Objects.requireNonNull(ClassificationClassModel.class);
        Objects.requireNonNull(ClassificationClassModel.class);
        Collection<ClassificationClassModel> allSubcategories = (Collection<ClassificationClassModel>)classAttributeAssignmentModel.getClassificationClass().getAllSubcategories().stream().filter(ClassificationClassModel.class::isInstance).map(ClassificationClassModel.class::cast)
                        .collect(Collectors.toList());
        for(ClassificationClassModel category : allSubcategories)
        {
            createGroupAssignmentsForCategory(ctx, category, classAttributeAssignmentModel);
        }
    }


    public void createGroupAssignmentsForCategory(InterceptorContext ctx, ClassificationClassModel classificationClass, ClassAttributeAssignmentModel classAttributeAssignment)
    {
        ClassFeatureGroupAssignmentModel featureGroupAssignment = createClassFeatureGroupAssignment(classAttributeAssignment, classificationClass);
        registerGroupAssignmentsForCreation(ctx, featureGroupAssignment);
    }


    public boolean isInstanceOfClassificationClass(ClassificationClassModel classificationClass)
    {
        return (classificationClass != null && classificationClass.getClass().equals(ClassificationClassModel.class));
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
