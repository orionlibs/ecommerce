package com.hybris.backoffice.excel.classification;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.classification.ClassificationService;
import de.hybris.platform.classification.features.FeatureList;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.collections4.ListUtils;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelClassificationServiceTest
{
    @Mock
    private CatalogService catalogService;
    @Mock
    private ClassificationService classificationService;
    @Mock
    private PermissionCRUDService permissionCRUDService;
    private final DefaultExcelClassificationService service = new DefaultExcelClassificationService();
    public static final String HARDWARE = "Hardware";
    @Mock
    private ClassificationClassModel hardwareModel;
    @Mock
    private ClassificationAttributeModel name;
    @Mock
    private ClassificationAttributeModel manufacturer;
    @Mock
    private ClassificationClassModel cpu;
    @Mock
    private ClassificationAttributeModel speed;
    @Mock
    private ClassificationAttributeModel cores;
    @Mock
    private ClassificationSystemVersionModel hardwareSystemVersion;
    @Mock
    private ClassificationSystemVersionModel cpuSystemVersion;
    @Mock
    private ClassificationSystemModel hardwareSystem;
    @Mock
    private ClassificationSystemModel cpuSystem;


    @Before
    public void setUp()
    {
        this.service.setCatalogService(this.catalogService);
        this.service.setClassificationService(this.classificationService);
        this.service.setPermissionCRUDService(this.permissionCRUDService);
        BDDMockito.given(Boolean.valueOf(this.permissionCRUDService.canReadType("ClassificationClass"))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(Boolean.valueOf(this.permissionCRUDService.canReadType("ClassificationAttribute"))).willReturn(Boolean.valueOf(true));
        BDDMockito.given(Boolean.valueOf(this.permissionCRUDService.canReadType("ClassAttributeAssignment"))).willReturn(Boolean.valueOf(true));
    }


    protected Collection<ClassificationSystemModel> createClassificationSystems(ClassificationSystemPOJO... classificationSystemPOJOS)
    {
        return (Collection<ClassificationSystemModel>)Stream.<ClassificationSystemPOJO>of(classificationSystemPOJOS).map(classificationSystemPOJO -> {
            ClassificationSystemModel classificationSystem = (ClassificationSystemModel)Mockito.mock(ClassificationSystemModel.class);
            List<CategoryModel> categoryModels = (List<CategoryModel>)classificationSystemPOJO.getCategoryPOJOS().stream().map(this::mockCategory).collect(Collectors.toList());
            CatalogVersionModel catalogVersionModel = (CatalogVersionModel)Mockito.mock(CatalogVersionModel.class);
            BDDMockito.given(classificationSystem.getCatalogVersions()).willReturn(Sets.newHashSet((Object[])new CatalogVersionModel[] {catalogVersionModel}));
            BDDMockito.given(catalogVersionModel.getRootCategories()).willReturn(categoryModels);
            return classificationSystem;
        }).collect(Collectors.toList());
    }


    private CategoryModel mockCategory(CategoryPOJO categoryPOJO)
    {
        CategoryModel category = (CategoryModel)Mockito.mock(ClassificationClassModel.class);
        Mockito.lenient().when(category.getName()).thenReturn(categoryPOJO.getName());
        BDDMockito.given(category.getCode()).willReturn(categoryPOJO.getName());
        BDDMockito.given(category.getCatalogVersion()).willReturn(categoryPOJO.getCatalogVersionModel());
        BDDMockito.given(categoryPOJO.getCatalogVersionModel().getCatalog()).willReturn(Mockito.mock(ClassificationSystemModel.class));
        List<CategoryModel> subcategories = new ArrayList<>();
        for(CategoryPOJO subcategory : categoryPOJO.getSubcategories())
        {
            CategoryModel subcategoryModel = mockCategory(subcategory);
            subcategories.add(subcategoryModel);
            subcategories.addAll(subcategoryModel.getAllSubcategories());
        }
        BDDMockito.given(category.getAllSubcategories()).willReturn(subcategories);
        return category;
    }


    @Test
    public void shouldReturnAllClassificationClasses()
    {
        ClassificationSystemVersionModel classificationSystemVersionModel = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        ClassificationSystemPOJO system1 = new ClassificationSystemPOJO(this, new CategoryPOJO[] {new CategoryPOJO(this, (CatalogVersionModel)classificationSystemVersionModel, "name1"), new CategoryPOJO(this, (CatalogVersionModel)classificationSystemVersionModel, "name2"),
                        new CategoryPOJO(this, (CatalogVersionModel)classificationSystemVersionModel, "name3")});
        ClassificationSystemPOJO system2 = new ClassificationSystemPOJO(this, new CategoryPOJO[] {new CategoryPOJO(this, (CatalogVersionModel)classificationSystemVersionModel, "name4", Arrays.asList(new CategoryPOJO[] {
                        new CategoryPOJO(this, (CatalogVersionModel)classificationSystemVersionModel, "name5",
                                        Arrays.asList(new CategoryPOJO[] {new CategoryPOJO(this, (CatalogVersionModel)classificationSystemVersionModel, "name6", Arrays.asList(new CategoryPOJO[] {new CategoryPOJO(this, (CatalogVersionModel)classificationSystemVersionModel, "name7")}))}))}))});
        Collection<ClassificationSystemModel> classificationSystemModels = createClassificationSystems(new ClassificationSystemPOJO[] {system1, system2});
        BDDMockito.given(this.catalogService.getAllCatalogsOfType(ClassificationSystemModel.class)).willReturn(classificationSystemModels);
        Map<ClassificationSystemVersionModel, List<ClassificationClassModel>> returnedValue = this.service.getAllClassificationClasses();
        Assertions.assertThat(returnedValue.size()).isEqualTo(1);
        Assertions.assertThat(((List)returnedValue.values().stream().reduce(ListUtils::union).get()).size()).isEqualTo(7);
    }


    @Test
    public void shouldMergeValuesByCategoryName()
    {
        ClassificationSystemVersionModel classificationSystemVersionModel = (ClassificationSystemVersionModel)Mockito.mock(ClassificationSystemVersionModel.class);
        String name1 = "name1";
        String name2 = "name2";
        String name3 = "name3";
        ClassificationSystemPOJO system1 = new ClassificationSystemPOJO(this, new CategoryPOJO[] {new CategoryPOJO(this, (CatalogVersionModel)classificationSystemVersionModel, "name1"), new CategoryPOJO(this, (CatalogVersionModel)classificationSystemVersionModel, "name2"),
                        new CategoryPOJO(this, (CatalogVersionModel)classificationSystemVersionModel, "name3")});
        ClassificationSystemPOJO system2 = new ClassificationSystemPOJO(this, new CategoryPOJO[] {new CategoryPOJO(this, (CatalogVersionModel)classificationSystemVersionModel, "name1"), new CategoryPOJO(this, (CatalogVersionModel)classificationSystemVersionModel, "name1"),
                        new CategoryPOJO(this, (CatalogVersionModel)classificationSystemVersionModel, "name2"), new CategoryPOJO(this, (CatalogVersionModel)classificationSystemVersionModel, "name3")});
        Collection<ClassificationSystemModel> classificationSystemModels = createClassificationSystems(new ClassificationSystemPOJO[] {system1, system2});
        BDDMockito.given(this.catalogService.getAllCatalogsOfType(ClassificationSystemModel.class)).willReturn(classificationSystemModels);
        Map<ClassificationSystemVersionModel, List<ClassificationClassModel>> returnedValue = this.service.getAllClassificationClasses();
        Assertions.assertThat(((List)returnedValue.values().stream().reduce(ListUtils::union).get()).size()).isEqualTo(3);
    }


    @Test
    public void shouldReturnedValuesBeResultOfIntersection()
    {
        initializeTestData();
        ProductModel intelCore = createProductWithClassificationClasses(new ClassificationClassModel[] {this.hardwareModel, this.cpu});
        ProductModel amdRyzen = createProductWithClassificationClasses(new ClassificationClassModel[] {this.hardwareModel});
        Map<ClassificationSystemVersionModel, List<ClassificationClassModel>> returnedValue = this.service.getItemsIntersectedClassificationClasses(Lists.newArrayList((Object[])new ItemModel[] {(ItemModel)intelCore, (ItemModel)amdRyzen}));
        Assertions.assertThat(returnedValue.size()).isEqualTo(1);
        Assertions.assertThat(((List<ClassificationClassModel>)(new ArrayList<>(returnedValue.values())).get(0)).get(0)).isEqualTo(this.hardwareModel);
    }


    @Test
    public void shouldReturnedValuesBeResultOfUnion()
    {
        initializeTestData();
        ProductModel intelCore = createProductWithClassificationClasses(new ClassificationClassModel[] {this.hardwareModel, this.cpu});
        ProductModel amdRyzen = createProductWithClassificationClasses(new ClassificationClassModel[] {this.hardwareModel});
        Map<ClassificationSystemVersionModel, List<ClassificationClassModel>> returnedValue = this.service.getItemsAddedClassificationClasses(Lists.newArrayList((Object[])new ItemModel[] {(ItemModel)intelCore, (ItemModel)amdRyzen}));
        Assertions.assertThat(returnedValue.size()).isEqualTo(2);
        Assertions.assertThat(returnedValue).containsValues((Object[])new List[] {List.of(this.cpu), List.of(this.hardwareModel)});
    }


    protected void initializeTestData()
    {
        initializeClassificationClass(this.hardwareModel, this.hardwareSystemVersion, this.hardwareSystem, "Hardware");
        initializeClassificationClass(this.cpu, this.cpuSystemVersion, this.cpuSystem, "Cpu");
        Mockito.lenient().when(this.name.getName()).thenReturn("Name");
        Mockito.lenient().when(this.name.getCode()).thenReturn("Name");
        Mockito.lenient().when(this.manufacturer.getName()).thenReturn("Manufacturer");
        Mockito.lenient().when(this.manufacturer.getCode()).thenReturn("Manufacturer");
        Mockito.lenient().when(this.speed.getName()).thenReturn("Speed");
        Mockito.lenient().when(this.speed.getCode()).thenReturn("Speed");
        Mockito.lenient().when(this.cores.getName()).thenReturn("Cores");
        Mockito.lenient().when(this.cores.getCode()).thenReturn("Cores");
    }


    private ProductModel createProductWithClassificationClasses(ClassificationClassModel... classes)
    {
        ProductModel product = (ProductModel)Mockito.mock(ProductModel.class);
        FeatureList features = (FeatureList)Mockito.mock(FeatureList.class);
        Mockito.when(this.classificationService.getFeatures(product)).thenReturn(features);
        Mockito.when(features.getClassificationClasses()).thenReturn(Sets.newHashSet((Object[])classes));
        return product;
    }


    private void initializeClassificationClass(ClassificationClassModel classificationClass, ClassificationSystemVersionModel systemVersion, ClassificationSystemModel system, String name)
    {
        Mockito.lenient().when(classificationClass.getName()).thenReturn(name);
        Mockito.when(classificationClass.getCode()).thenReturn(name);
        Mockito.when(classificationClass.getCatalogVersion()).thenReturn(systemVersion);
        Mockito.when(systemVersion.getCatalog()).thenReturn(system);
        Mockito.when(system.getId()).thenReturn(name);
    }


    @Test
    public void shouldNotGetItemsIntersectedClassificationClassesIfHasNoPermissions()
    {
        Collection<ItemModel> anyItems = null;
        BDDMockito.given(Boolean.valueOf(this.permissionCRUDService.canReadType("ClassificationClass"))).willReturn(Boolean.valueOf(false));
        Map<ClassificationSystemVersionModel, List<ClassificationClassModel>> result = this.service.getItemsIntersectedClassificationClasses(anyItems);
        Assertions.assertThat(result).isEmpty();
    }


    @Test
    public void shouldNotGetItemsAddedClassificationClassesIfHasNoPermissions()
    {
        Collection<ItemModel> anyItems = null;
        BDDMockito.given(Boolean.valueOf(this.permissionCRUDService.canReadType("ClassificationClass"))).willReturn(Boolean.valueOf(false));
        Map<ClassificationSystemVersionModel, List<ClassificationClassModel>> result = this.service.getItemsAddedClassificationClasses(anyItems);
        Assertions.assertThat(result).isEmpty();
    }


    @Test
    public void shouldNotGetAllClassificationClassesIfHasNoPermissions()
    {
        BDDMockito.given(Boolean.valueOf(this.permissionCRUDService.canReadType("ClassificationClass"))).willReturn(Boolean.valueOf(false));
        Map<ClassificationSystemVersionModel, List<ClassificationClassModel>> result = this.service.getAllClassificationClasses();
        Assertions.assertThat(result).isEmpty();
    }
}
