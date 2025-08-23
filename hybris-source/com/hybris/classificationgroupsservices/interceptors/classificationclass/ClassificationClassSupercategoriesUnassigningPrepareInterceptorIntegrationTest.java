package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import com.hybris.classificationgroupsservices.interceptors.AbstractGroupAssignmentPrepareInterceptorIntegrationTest;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupModel;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class ClassificationClassSupercategoriesUnassigningPrepareInterceptorIntegrationTest extends AbstractGroupAssignmentPrepareInterceptorIntegrationTest
{
    @Before
    public void setUp() throws ImpExException
    {
        importCsv("/impex/test/testAssignmentPrepareInterceptor.impex", "UTF-8");
    }


    @Test
    public void shouldRemoveGroupAssignmentsIfUserReplaceSupercategory()
    {
        CategoryModel samsungCategory = this.categoryService.getCategoryForCode("Samsung");
        CategoryModel deviceCategory = this.categoryService.getCategoryForCode("Device");
        samsungCategory.setSupercategories(List.of(deviceCategory));
        this.modelService.save(samsungCategory);
        List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Device", List.of("Price")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Phone", List.of("Price", "RAM")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Samsung", List.of("Price", "TouchId")), groupAssignments);
        Assertions.assertThat(groupAssignments).hasSize(5);
    }


    @Test
    public void shouldRemoveGroupAssignmentsIfUserUnassignsSupercategory()
    {
        CategoryModel samsungCategory = this.categoryService.getCategoryForCode("Samsung");
        samsungCategory.setSupercategories(List.of());
        this.modelService.save(samsungCategory);
        List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Device", List.of("Price")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Phone", List.of("Price", "RAM")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Samsung", List.of("TouchId")), groupAssignments);
        Assertions.assertThat(groupAssignments).hasSize(4);
    }


    @Test
    public void shouldRemoveGroupAssignmentsInSubcategoriesDuringEditingSupercategories()
    {
        CategoryModel phoneCategory = this.categoryService.getCategoryForCode("Phone");
        phoneCategory.setSupercategories(List.of());
        this.modelService.save(phoneCategory);
        List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Device", List.of("Price")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Phone", List.of("RAM")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Samsung", List.of("TouchId", "RAM")), groupAssignments);
        Assertions.assertThat(groupAssignments).hasSize(4);
    }


    @Test
    public void shouldNotCreateDuplicateGroupAssignments()
    {
        CategoryModel deviceCategory = this.categoryService.getCategoryForCode("Device");
        CategoryModel phoneCategory = this.categoryService.getCategoryForCode("Phone");
        CategoryModel samsungCategory = this.categoryService.getCategoryForCode("Samsung");
        samsungCategory.setSupercategories(List.of(deviceCategory, phoneCategory));
        this.modelService.save(phoneCategory);
        List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Device", List.of("Price")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Phone", List.of("Price", "RAM")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Samsung",
                        List.of("Price", "TouchId", "RAM")), groupAssignments);
        Assertions.assertThat(groupAssignments).hasSize(6);
    }


    @Test
    public void shouldNotRemoveGroupAssignmentsDuringRemovingSupercategoryWithDuplicateAttributeAssignment()
    {
        CategoryModel deviceCategory = this.categoryService.getCategoryForCode("Device");
        CategoryModel phoneCategory = this.categoryService.getCategoryForCode("Phone");
        CategoryModel samsungCategory = this.categoryService.getCategoryForCode("Samsung");
        samsungCategory.setSupercategories(List.of(deviceCategory, phoneCategory));
        this.modelService.save(samsungCategory);
        samsungCategory.setSupercategories(List.of(deviceCategory));
        this.modelService.save(samsungCategory);
        List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Device", List.of("Price")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Phone", List.of("Price", "RAM")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Samsung", List.of("Price", "TouchId")), groupAssignments);
        Assertions.assertThat(groupAssignments).hasSize(5);
    }


    @Test
    public void shouldNotRemoveClassFeatureGroupAssignmentInSubCategoryWhileDuplicateSupercategories() throws ImpExException
    {
        importCsv("/impex/test/shouldNotRemoveClassFeatureGroupAssignmentInSubCategoryWhileDuplicateSupercategories.impex", "UTF-8");
        ClassificationClassModel samsungCategory = (ClassificationClassModel)this.categoryService.getCategoryForCode("Samsung");
        ClassificationClassModel phoneCategory = (ClassificationClassModel)this.categoryService.getCategoryForCode("Phone");
        ClassificationClassModel objectCategory = (ClassificationClassModel)this.categoryService.getCategoryForCode("Object");
        ClassificationClassModel deviceCategory = (ClassificationClassModel)this.categoryService.getCategoryForCode("Device");
        ClassFeatureGroupAssignmentModel samsungDimensionsFeatureGroupAssignment = findClassFeatureGroupAssignment(objectCategory
                        .getDeclaredClassificationAttributeAssignments().get(0), samsungCategory);
        createClassFeatureGroup(samsungCategory, List.of(samsungDimensionsFeatureGroupAssignment), "samsungGroup");
        ClassificationClassModel samsungCategoryUpdated = (ClassificationClassModel)this.categoryService.getCategoryForCode("Samsung");
        samsungCategoryUpdated.setSupercategories(List.of(phoneCategory, objectCategory));
        this.modelService.save(samsungCategoryUpdated);
        deviceCategory.setSupercategories(List.of());
        this.modelService.save(deviceCategory);
        List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Device", List.of("Price")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Phone", List.of("Price", "RAM")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Samsung",
                        List.of("Price", "TouchId", "RAM", "Dimensions")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Object", List.of("Dimensions")), groupAssignments);
        Assertions.assertThat(groupAssignments).hasSize(8);
        Assertions.assertThat(findClassGroupFeature("samsungGroup").getClassFeatureGroupAssignments()).hasSize(1);
    }


    @Test
    public void shouldNotRemoveClassFeatureGroupAssignmentFromGroupDuringMergingSupercategories() throws ImpExException
    {
        importCsv("/impex/test/shouldNotRemoveClassFeatureGroupAssignmentFromGroupDuringMergingSupercategories.impex", "UTF-8");
        ClassificationClassModel samsungCategory = (ClassificationClassModel)this.categoryService.getCategoryForCode("Samsung");
        ClassificationClassModel phoneCategory = (ClassificationClassModel)this.categoryService.getCategoryForCode("Phone");
        ClassificationClassModel objectCategory = (ClassificationClassModel)this.categoryService.getCategoryForCode("Object");
        ClassificationClassModel deviceCategory = (ClassificationClassModel)this.categoryService.getCategoryForCode("Device");
        ClassificationClassModel itemCategory = (ClassificationClassModel)this.categoryService.getCategoryForCode("Item");
        ClassFeatureGroupAssignmentModel samsungPriceFeatureGroupAssignment = findClassFeatureGroupAssignment(
                        findClassAttributeAssignment("Price", deviceCategory), samsungCategory);
        ClassFeatureGroupAssignmentModel samsungDimensionsFeatureGroupAssignment = findClassFeatureGroupAssignment(
                        findClassAttributeAssignment("Dimensions", objectCategory), samsungCategory);
        ClassFeatureGroupAssignmentModel samsungWeightFeatureGroupAssignment = findClassFeatureGroupAssignment(
                        findClassAttributeAssignment("Weight", itemCategory), samsungCategory);
        ClassFeatureGroupAssignmentModel samsungRamFeatureGroupAssignment = findClassFeatureGroupAssignment(
                        findClassAttributeAssignment("RAM", phoneCategory), samsungCategory);
        ClassFeatureGroupAssignmentModel samsungTouchIdFeatureGroupAssignment = findClassFeatureGroupAssignment(
                        findClassAttributeAssignment("TouchId", samsungCategory), samsungCategory);
        createClassFeatureGroup(samsungCategory,
                        List.of(samsungPriceFeatureGroupAssignment, samsungDimensionsFeatureGroupAssignment, samsungWeightFeatureGroupAssignment, samsungRamFeatureGroupAssignment, samsungTouchIdFeatureGroupAssignment), "samsungGroup");
        deviceCategory.setSupercategories(List.of(objectCategory, itemCategory));
        this.modelService.save(deviceCategory);
        samsungCategory.setSupercategories(List.of(phoneCategory));
        this.modelService.save(samsungCategory);
        List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Device",
                        List.of("Price", "Weight", "Dimensions")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Phone",
                        List.of("Price", "RAM", "Weight", "Dimensions")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Samsung",
                        List.of("Price", "TouchId", "RAM", "Dimensions", "Weight")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Object", List.of("Dimensions")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Item", List.of("Dimensions", "Weight")), groupAssignments);
        Assertions.assertThat(groupAssignments).hasSize(15);
        Assertions.assertThat(findClassGroupFeature("samsungGroup").getClassFeatureGroupAssignments()).hasSize(5);
    }


    private void createClassFeatureGroup(ClassificationClassModel samsungCategory, List<ClassFeatureGroupAssignmentModel> featureGorupAssignments, String groupCode)
    {
        ClassFeatureGroupModel samsungGroup = (ClassFeatureGroupModel)this.modelService.create(ClassFeatureGroupModel.class);
        samsungGroup.setClassificationClass(samsungCategory);
        samsungGroup.setCode(groupCode);
        samsungGroup.setClassFeatureGroupAssignments(featureGorupAssignments);
        this.modelService.save(samsungGroup);
    }


    protected ClassFeatureGroupModel findClassGroupFeature(String groupCode)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("select {pk} from {ClassFeatureGroup} where {code} = ?code");
        fQuery.addQueryParameter("code", groupCode);
        return (ClassFeatureGroupModel)this.flexibleSearchService.searchUnique(fQuery);
    }


    protected ClassFeatureGroupAssignmentModel findClassFeatureGroupAssignment(ClassAttributeAssignmentModel classAttributeAssignment, ClassificationClassModel category)
    {
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("select {pk} from {ClassFeatureGroupAssignment} where {classAttributeAssignment} = ?classAttributeAssignment and {classificationClass} = ?classificationClass");
        fQuery.addQueryParameter("classificationClass", category.getPk());
        fQuery.addQueryParameter("classAttributeAssignment", classAttributeAssignment.getPk());
        return (ClassFeatureGroupAssignmentModel)this.flexibleSearchService.searchUnique(fQuery);
    }


    protected ClassAttributeAssignmentModel findClassAttributeAssignment(String attributeCode, ClassificationClassModel category)
    {
        FlexibleSearchQuery attributeQuery = new FlexibleSearchQuery("select {pk} from {ClassificationAttribute} where {code} = ?attributeCode");
        attributeQuery.addQueryParameter("attributeCode", attributeCode);
        ClassificationAttributeModel classificationAttribute = (ClassificationAttributeModel)this.flexibleSearchService.searchUnique(attributeQuery);
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery("select {pk} from {ClassAttributeAssignment} where {classificationClass} = ?classificationClass and {classificationAttribute} = ?classificationAttribute");
        fQuery.addQueryParameter("classificationClass", category.getPk());
        fQuery.addQueryParameter("classificationAttribute", classificationAttribute.getPk());
        return (ClassAttributeAssignmentModel)this.flexibleSearchService.searchUnique(fQuery);
    }
}
