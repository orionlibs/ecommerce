package com.hybris.classificationgroupsservices.services;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.interceptor.InterceptorContext;
import de.hybris.platform.servicelayer.interceptor.PersistenceOperation;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultClassFeatureGroupAssignmentServiceTest
{
    private static final String UNIQUE_FEATURE_GROUP_ASSIGNMENT_TEST_QUERY = "select {cfga.pk} from {ClassFeatureGroupAssignment as cfga} where {cfga.classAttributeAssignment} in ({{select {c.pk} from {ClassAttributeAssignment as c} where {c.pk} = ?attributePk}}) and {cfga.classificationClass} in ({{ select {cc.pk} from {ClassificationClass as cc} where {cc.pk} = ?categoryPk}})";
    private static final String FEATURE_GROUP_ASSIGNMENTS_TEST_QUERY = "select {cfga.pk} from {ClassFeatureGroupAssignment as cfga} where {cfga.classAttributeAssignment} in ({{select {c.pk} from {ClassAttributeAssignment as c} where {c.pk} = ?pk}})";
    private static final String FEATURE_GROUP_ASSIGNMENTS_FROM_ATTRIBUTE_TEST_QUERY = "select {caa.pk} from {ClassAttributeAssignment as caa} where {caa.classificationAttribute}  = ?pk";
    @Mock
    ModelService modelService;
    @Mock
    FlexibleSearchService flexibleSearchService;
    @Mock
    InterceptorContext interceptorContext;
    @Mock
    ClassificationClassModel classificationClass;
    @InjectMocks
    @Spy
    DefaultClassFeatureGroupAssignmentService classFeatureGroupAssignmentService;


    @Test
    public void shouldCreateNewFeatureGroupAssignment()
    {
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationClassModel classificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        BDDMockito.given(this.modelService.create(ClassFeatureGroupAssignmentModel.class)).willReturn(new ClassFeatureGroupAssignmentModel());
        ClassFeatureGroupAssignmentModel result = this.classFeatureGroupAssignmentService.createClassFeatureGroupAssignment(classAttributeAssignment, classificationClass);
        Assertions.assertThat(result.getClassAttributeAssignment()).isEqualTo(classAttributeAssignment);
        Assertions.assertThat(result.getClassificationClass()).isEqualTo(classificationClass);
    }


    @Test
    public void shouldSearchForUniqueClassFeatureGroupAssignment()
    {
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        PK attributeAssignmentPK = PK.parse("12345");
        PK classificationClassPK = PK.parse("56789");
        BDDMockito.given(classAttributeAssignment.getPk()).willReturn(attributeAssignmentPK);
        BDDMockito.given(this.classificationClass.getPk()).willReturn(classificationClassPK);
        FlexibleSearchQuery query = new FlexibleSearchQuery(
                        "select {cfga.pk} from {ClassFeatureGroupAssignment as cfga} where {cfga.classAttributeAssignment} in ({{select {c.pk} from {ClassAttributeAssignment as c} where {c.pk} = ?attributePk}}) and {cfga.classificationClass} in ({{ select {cc.pk} from {ClassificationClass as cc} where {cc.pk} = ?categoryPk}})");
        query.addQueryParameter("attributePk", attributeAssignmentPK);
        query.addQueryParameter("categoryPk", classificationClassPK);
        BDDMockito.given(this.flexibleSearchService.searchUnique((FlexibleSearchQuery)Matchers.any())).willReturn(Mockito.mock(ClassFeatureGroupAssignmentModel.class));
        this.classFeatureGroupAssignmentService.findFeatureGroupAssignment(classAttributeAssignment, this.classificationClass);
        ((FlexibleSearchService)BDDMockito.then(this.flexibleSearchService).should()).searchUnique(query);
    }


    @Test
    public void shouldSearchAllFeatureGroupAssignmentForClassAttributeAssignment()
    {
        PK attributeAssignmentPK = PK.parse("12345");
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        BDDMockito.given(classAttributeAssignment.getPk()).willReturn(attributeAssignmentPK);
        BDDMockito.given(this.flexibleSearchService.search((String)Matchers.any(), (Map)Matchers.any())).willReturn(Mockito.mock(SearchResult.class));
        this.classFeatureGroupAssignmentService.findAllFeatureGroupAssignments(classAttributeAssignment);
        ((FlexibleSearchService)BDDMockito.then(this.flexibleSearchService).should()).search((String)Matchers.eq("select {cfga.pk} from {ClassFeatureGroupAssignment as cfga} where {cfga.classAttributeAssignment} in ({{select {c.pk} from {ClassAttributeAssignment as c} where {c.pk} = ?pk}})"),
                        (Map)Matchers.eq(Map.of("pk", attributeAssignmentPK)));
    }


    @Test
    public void shouldCreateFeatureGroupAssignmentsForNewClassificationClass()
    {
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassFeatureGroupAssignmentModel classFeatureGroupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(this.classificationClass))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(this.classificationClass.getAllSubcategories()).willReturn(List.of());
        ((DefaultClassFeatureGroupAssignmentService)Mockito.doReturn(classFeatureGroupAssignment).when(this.classFeatureGroupAssignmentService))
                        .createClassFeatureGroupAssignment(classAttributeAssignment, this.classificationClass);
        this.classFeatureGroupAssignmentService.createLackingFeatureGroupAssignments(this.interceptorContext, this.classificationClass, classAttributeAssignment);
        ((InterceptorContext)BDDMockito.then(this.interceptorContext).should()).registerElementFor(classFeatureGroupAssignment, PersistenceOperation.SAVE);
    }


    @Test
    public void shouldNotCreateFeatureGroupAssignemntsIfItIsAlreadyAssignedToClassFeatureAssignment()
    {
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassFeatureGroupAssignmentModel classFeatureGroupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(this.classificationClass))).willReturn(Boolean.valueOf(false));
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(classAttributeAssignment))).willReturn(Boolean.valueOf(false));
        ((DefaultClassFeatureGroupAssignmentService)Mockito.doReturn(Optional.of(classFeatureGroupAssignment)).when(this.classFeatureGroupAssignmentService))
                        .findFeatureGroupAssignment(classAttributeAssignment, this.classificationClass);
        this.classFeatureGroupAssignmentService.createLackingFeatureGroupAssignments(this.interceptorContext, this.classificationClass, classAttributeAssignment);
        ((InterceptorContext)BDDMockito.then(this.interceptorContext).should(Mockito.never())).registerElementFor(Matchers.any(), (PersistenceOperation)Matchers.any());
    }


    @Test
    public void shouldCreateFeatureGroupsAssignmentsForAllSubCategories()
    {
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassFeatureGroupAssignmentModel classFeatureGroupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassificationClassModel subCategory = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassFeatureGroupAssignmentModel subCategoryClassFeatureGroupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(this.classificationClass))).willReturn(Boolean.valueOf(false));
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(classAttributeAssignment))).willReturn(Boolean.valueOf(false));
        ((DefaultClassFeatureGroupAssignmentService)Mockito.doReturn(Optional.empty()).when(this.classFeatureGroupAssignmentService)).findFeatureGroupAssignment(classAttributeAssignment, this.classificationClass);
        BDDMockito.given(this.classificationClass.getAllSubcategories()).willReturn(List.of(subCategory));
        ((DefaultClassFeatureGroupAssignmentService)Mockito.doReturn(classFeatureGroupAssignment).when(this.classFeatureGroupAssignmentService))
                        .createClassFeatureGroupAssignment(classAttributeAssignment, this.classificationClass);
        BDDMockito.given(Boolean.valueOf(this.modelService.isNew(subCategory))).willReturn(Boolean.valueOf(false));
        ((DefaultClassFeatureGroupAssignmentService)Mockito.doReturn(Optional.empty()).when(this.classFeatureGroupAssignmentService)).findFeatureGroupAssignment(classAttributeAssignment, subCategory);
        ((DefaultClassFeatureGroupAssignmentService)Mockito.doReturn(subCategoryClassFeatureGroupAssignment).when(this.classFeatureGroupAssignmentService))
                        .createClassFeatureGroupAssignment(classAttributeAssignment, subCategory);
        this.classFeatureGroupAssignmentService.createLackingFeatureGroupAssignments(this.interceptorContext, this.classificationClass, classAttributeAssignment);
        ((InterceptorContext)BDDMockito.then(this.interceptorContext).should()).registerElementFor(classFeatureGroupAssignment, PersistenceOperation.SAVE);
        ((InterceptorContext)BDDMockito.then(this.interceptorContext).should()).registerElementFor(subCategoryClassFeatureGroupAssignment, PersistenceOperation.SAVE);
    }


    @Test
    public void shouldRemoveAllFeatureGroupAssignments()
    {
        ClassFeatureGroupAssignmentModel classFeatureGroupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassAttributeAssignmentModel notRemovedAttribute = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassAttributeAssignmentModel removedAttribute = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ItemModelContext itemModelContext = (ItemModelContext)Mockito.mock(ItemModelContext.class);
        BDDMockito.given(this.classificationClass.getDeclaredClassificationAttributeAssignments()).willReturn(List.of(notRemovedAttribute));
        BDDMockito.given(this.classificationClass.getItemModelContext()).willReturn(itemModelContext);
        BDDMockito.given(itemModelContext.getOriginalValue("declaredClassificationAttributeAssignments"))
                        .willReturn(List.of(notRemovedAttribute, removedAttribute));
        ((DefaultClassFeatureGroupAssignmentService)Mockito.doReturn(List.of(classFeatureGroupAssignment)).when(this.classFeatureGroupAssignmentService))
                        .findAllFeatureGroupAssignments(removedAttribute);
        this.classFeatureGroupAssignmentService.removeFeatureGroupAssignments(this.interceptorContext, this.classificationClass);
        ((InterceptorContext)BDDMockito.then(this.interceptorContext).should()).registerElementFor(classFeatureGroupAssignment, PersistenceOperation.DELETE);
    }


    @Test
    public void shouldFindUnassignedSupercategories()
    {
        ClassificationClassModel superCategory1 = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        CategoryModel superCategory2 = (CategoryModel)Mockito.mock(CategoryModel.class);
        ClassificationClassModel unassignedCategory1 = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassificationClassModel unassignedCategory2 = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ItemModelContext itemModelContext = (ItemModelContext)Mockito.mock(ItemModelContext.class);
        BDDMockito.given(this.classificationClass.getSupercategories()).willReturn(List.of(superCategory1, superCategory2));
        BDDMockito.given(this.classificationClass.getItemModelContext()).willReturn(itemModelContext);
        BDDMockito.given(itemModelContext.getOriginalValue("supercategories"))
                        .willReturn(List.of(unassignedCategory1, unassignedCategory2, superCategory1, superCategory2));
        List<ClassificationClassModel> result = this.classFeatureGroupAssignmentService.findUnassignedSupercategories(this.classificationClass);
        Assertions.assertThat(result).containsExactlyInAnyOrder((Object[])new ClassificationClassModel[] {unassignedCategory1, unassignedCategory2});
    }


    @Test
    public void shouldRemoveNotInheritedFeatureGroupAssignments()
    {
        ClassAttributeAssignmentModel attributeAssignmentForRemoval = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassAttributeAssignmentModel attributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationClassModel subCategory = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassFeatureGroupAssignmentModel featureGroupAssignmentModel = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        BDDMockito.given(this.classificationClass.getAllSubcategories()).willReturn(List.of(subCategory));
        BDDMockito.given(subCategory.getAllClassificationAttributeAssignments()).willReturn(List.of(attributeAssignment));
        ((DefaultClassFeatureGroupAssignmentService)Mockito.doReturn(Optional.of(featureGroupAssignmentModel)).when(this.classFeatureGroupAssignmentService))
                        .findFeatureGroupAssignment(attributeAssignmentForRemoval, subCategory);
        this.classFeatureGroupAssignmentService.removeFeatureGroupAssignmentsInSubCategories(this.interceptorContext, this.classificationClass,
                        List.of(attributeAssignment, attributeAssignmentForRemoval));
        ((InterceptorContext)BDDMockito.then(this.interceptorContext).should()).registerElementFor(featureGroupAssignmentModel, PersistenceOperation.DELETE);
    }


    @Test
    public void shouldCreateFeatureGroupAssignmentsForSubcategories()
    {
        ClassFeatureGroupAssignmentModel featureGroupAssignment1 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassFeatureGroupAssignmentModel featureGroupAssignment2 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassAttributeAssignmentModel attributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationClassModel subCategory1 = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassificationClassModel subCategory2 = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ((DefaultClassFeatureGroupAssignmentService)Mockito.doReturn(featureGroupAssignment1).when(this.classFeatureGroupAssignmentService))
                        .createClassFeatureGroupAssignment(attributeAssignment, subCategory1);
        ((DefaultClassFeatureGroupAssignmentService)Mockito.doReturn(featureGroupAssignment2).when(this.classFeatureGroupAssignmentService))
                        .createClassFeatureGroupAssignment(attributeAssignment, subCategory2);
        BDDMockito.given(attributeAssignment.getClassificationClass()).willReturn(this.classificationClass);
        BDDMockito.given(this.classificationClass.getAllSubcategories()).willReturn(List.of(subCategory1, subCategory2));
        this.classFeatureGroupAssignmentService.createGroupAssignmentsForSubcategories(this.interceptorContext, attributeAssignment);
        ((InterceptorContext)BDDMockito.then(this.interceptorContext).should()).registerElementFor(featureGroupAssignment1, PersistenceOperation.SAVE);
        ((InterceptorContext)BDDMockito.then(this.interceptorContext).should()).registerElementFor(featureGroupAssignment2, PersistenceOperation.SAVE);
    }


    @Test
    public void shouldNotCreateFeatureGroupAssignmentsIfModelIsRegisteredInContext()
    {
        ClassFeatureGroupAssignmentModel featureGroupAssignment1 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassFeatureGroupAssignmentModel featureGroupAssignment2 = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassFeatureGroupAssignmentModel registeredGroupAssignment = (ClassFeatureGroupAssignmentModel)Mockito.mock(ClassFeatureGroupAssignmentModel.class);
        ClassAttributeAssignmentModel attributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationClassModel subCategory1 = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassificationClassModel subCategory2 = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ((DefaultClassFeatureGroupAssignmentService)Mockito.doReturn(featureGroupAssignment1).when(this.classFeatureGroupAssignmentService))
                        .createClassFeatureGroupAssignment(attributeAssignment, subCategory1);
        ((DefaultClassFeatureGroupAssignmentService)Mockito.doReturn(featureGroupAssignment2).when(this.classFeatureGroupAssignmentService))
                        .createClassFeatureGroupAssignment(attributeAssignment, subCategory2);
        BDDMockito.given(registeredGroupAssignment.getClassificationClass()).willReturn(subCategory1);
        BDDMockito.given(registeredGroupAssignment.getClassAttributeAssignment()).willReturn(attributeAssignment);
        BDDMockito.given(featureGroupAssignment2.getClassificationClass()).willReturn(subCategory1);
        BDDMockito.given(featureGroupAssignment2.getClassAttributeAssignment()).willReturn(attributeAssignment);
        BDDMockito.given(this.interceptorContext.getElementsRegisteredFor(PersistenceOperation.SAVE)).willReturn(Set.of(registeredGroupAssignment));
        BDDMockito.given(attributeAssignment.getClassificationClass()).willReturn(this.classificationClass);
        BDDMockito.given(this.classificationClass.getAllSubcategories()).willReturn(List.of(subCategory1, subCategory2));
        this.classFeatureGroupAssignmentService.createGroupAssignmentsForSubcategories(this.interceptorContext, attributeAssignment);
        ((InterceptorContext)BDDMockito.then(this.interceptorContext).should()).registerElementFor(featureGroupAssignment1, PersistenceOperation.SAVE);
    }


    @Test
    public void shouldRemoveClassFeatureGroupAssignmentsRelatedWithClassificationAttribute()
    {
        PK classificationAttributePK = PK.parse("12345");
        ClassificationAttributeModel classificationAttribute = (ClassificationAttributeModel)Mockito.mock(ClassificationAttributeModel.class);
        SearchResult searchResult = (SearchResult)Mockito.mock(SearchResult.class);
        ClassAttributeAssignmentModel correctClassAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassAttributeAssignmentModel incorrectClassAttributeAssignment = (ClassAttributeAssignmentModel)Mockito.mock(ClassAttributeAssignmentModel.class);
        ClassificationClassModel correctClassificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        ClassificationClassModel incorrectClassificationClass = (ClassificationClassModel)Mockito.mock(ClassificationClassModel.class);
        BDDMockito.given(classificationAttribute.getPk()).willReturn(classificationAttributePK);
        BDDMockito.given(this.flexibleSearchService.search((String)Matchers.any(), (Map)Matchers.any())).willReturn(searchResult);
        BDDMockito.given(searchResult.getResult()).willReturn(List.of(correctClassAttributeAssignment, incorrectClassAttributeAssignment));
        BDDMockito.given(correctClassAttributeAssignment.getClassificationClass()).willReturn(correctClassificationClass);
        BDDMockito.given(incorrectClassAttributeAssignment.getClassificationClass()).willReturn(incorrectClassificationClass);
        ((DefaultClassFeatureGroupAssignmentService)Mockito.doReturn(Boolean.valueOf(true)).when(this.classFeatureGroupAssignmentService)).isInstanceOfClassificationClass(correctClassificationClass);
        ((DefaultClassFeatureGroupAssignmentService)Mockito.doReturn(Boolean.valueOf(false)).when(this.classFeatureGroupAssignmentService)).isInstanceOfClassificationClass(incorrectClassificationClass);
        ((DefaultClassFeatureGroupAssignmentService)Mockito.doNothing().when(this.classFeatureGroupAssignmentService)).removeAllFeatureGroupAssignments((InterceptorContext)Matchers.any(InterceptorContext.class),
                        Matchers.anyList());
        this.classFeatureGroupAssignmentService.removeAllFeatureGroupAssignments(this.interceptorContext, classificationAttribute);
        ((FlexibleSearchService)BDDMockito.then(this.flexibleSearchService).should()).search((String)Matchers.eq("select {caa.pk} from {ClassAttributeAssignment as caa} where {caa.classificationAttribute}  = ?pk"),
                        (Map)Matchers.eq(Map.of("pk", classificationAttributePK)));
        ((DefaultClassFeatureGroupAssignmentService)BDDMockito.then(this.classFeatureGroupAssignmentService).should()).removeAllFeatureGroupAssignments(this.interceptorContext,
                        List.of(correctClassAttributeAssignment));
    }
}
