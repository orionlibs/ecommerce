package com.hybris.backoffice.excel.classification;

import com.google.common.collect.Sets;
import com.hybris.backoffice.excel.template.filter.ExcelFilter;
import de.hybris.platform.catalog.CatalogService;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExcelClassificationService implements ExcelClassificationService
{
    private CatalogService catalogService;
    private ClassificationService classificationService;
    private PermissionCRUDService permissionCRUDService;
    private Collection<ExcelFilter<ClassificationSystemVersionModel>> filters;


    public Map<ClassificationSystemVersionModel, List<ClassificationClassModel>> getItemsIntersectedClassificationClasses(Collection<ItemModel> items)
    {
        if(!hasPermissionsToClassification())
        {
            return Collections.emptyMap();
        }
        return getFilteredMap(getItemsClassificationClassesInternal(items, true));
    }


    public Map<ClassificationSystemVersionModel, List<ClassificationClassModel>> getItemsAddedClassificationClasses(Collection<ItemModel> items)
    {
        if(!hasPermissionsToClassification())
        {
            return Collections.emptyMap();
        }
        return getFilteredMap(getItemsClassificationClassesInternal(items, false));
    }


    public Map<ClassificationSystemVersionModel, List<ClassificationClassModel>> getAllClassificationClasses()
    {
        if(!hasPermissionsToClassification())
        {
            return Collections.emptyMap();
        }
        return getFilteredMap(getAllClassificationClassesInternal());
    }


    protected Map<ClassificationSystemVersionModel, List<ClassificationClassModel>> getFilteredMap(Map<ClassificationSystemVersionModel, List<ClassificationClassModel>> map)
    {
        return (Map<ClassificationSystemVersionModel, List<ClassificationClassModel>>)map
                        .entrySet()
                        .stream()
                        .filter(e -> filter((ClassificationSystemVersionModel)e.getKey()))
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }


    protected boolean hasPermissionsToClassification()
    {
        return (this.permissionCRUDService.canReadType("ClassificationClass") && this.permissionCRUDService
                        .canReadType("ClassificationAttribute") && this.permissionCRUDService
                        .canReadType("ClassAttributeAssignment"));
    }


    protected boolean filter(ClassificationSystemVersionModel classificationSystemVersionModel)
    {
        return CollectionUtils.emptyIfNull(this.filters).stream().allMatch(filter -> filter.test(classificationSystemVersionModel));
    }


    private Map<ClassificationSystemVersionModel, List<ClassificationClassModel>> getAllClassificationClassesInternal()
    {
        List<CategoryModel> rootCategories = (List<CategoryModel>)this.catalogService.getAllCatalogsOfType(ClassificationSystemModel.class).stream().map(CatalogModel::getCatalogVersions).flatMap(Collection::stream).map(CatalogVersionModel::getRootCategories).flatMap(Collection::stream)
                        .collect(Collectors.toList());
        List<CategoryModel> allCategories = new ArrayList<>();
        allCategories.addAll(rootCategories);
        allCategories.addAll((Collection<? extends CategoryModel>)rootCategories.stream().map(CategoryModel::getAllSubcategories).flatMap(Collection::stream)
                        .collect(Collectors.toList()));
        Objects.requireNonNull(ClassificationClassModel.class);
        Objects.requireNonNull(ClassificationClassModel.class);
        return (Map<ClassificationSystemVersionModel, List<ClassificationClassModel>>)allCategories.stream().filter(ClassificationClassModel.class::isInstance).map(ClassificationClassModel.class::cast)
                        .collect(
                                        Collectors.groupingBy(ClassificationClassModel::getCatalogVersion,
                                                        Collectors.collectingAndThen(
                                                                        Collectors.toMap(this::getUniqueKeyForCategory, Function.identity(), (left, right) -> left), map -> new ArrayList(map.values()))));
    }


    private Map<ClassificationSystemVersionModel, List<ClassificationClassModel>> getItemsClassificationClassesInternal(Collection<ItemModel> items, boolean useIntersection)
    {
        BinaryOperator<Set<ClassificationClassModel>> reduceOperator = (set1, set2) -> useIntersection ? (Set)Sets.intersection(set1, set2) : (Set)Sets.union(set1, set2).stream().collect(Collectors.collectingAndThen(Collectors.toMap(this::getUniqueKeyForCategory, Function.identity(), ()), ()));
        Objects.requireNonNull(ProductModel.class);
        Objects.requireNonNull(this.classificationService);
        return items.stream().map(ProductModel.class::cast).map(this.classificationService::getFeatures)
                        .map(FeatureList::getClassificationClasses)
                        .reduce(reduceOperator)
                        .map(classes -> (Map)classes.stream().collect(Collectors.groupingBy(ClassificationClassModel::getCatalogVersion, Collectors.toList())))
                        .orElse(Collections.emptyMap());
    }


    private String getUniqueKeyForCategory(CategoryModel categoryModel)
    {
        return StringUtils.joinWith("_", new Object[] {categoryModel.getCode(), categoryModel.getCatalogVersion().getVersion(), categoryModel
                        .getCatalogVersion().getCatalog().getId()});
    }


    @Required
    public void setCatalogService(CatalogService catalogService)
    {
        this.catalogService = catalogService;
    }


    @Required
    public void setClassificationService(ClassificationService classificationService)
    {
        this.classificationService = classificationService;
    }


    @Required
    public void setPermissionCRUDService(PermissionCRUDService permissionCRUDService)
    {
        this.permissionCRUDService = permissionCRUDService;
    }


    public void setFilters(Collection<ExcelFilter<ClassificationSystemVersionModel>> filters)
    {
        this.filters = filters;
    }
}
