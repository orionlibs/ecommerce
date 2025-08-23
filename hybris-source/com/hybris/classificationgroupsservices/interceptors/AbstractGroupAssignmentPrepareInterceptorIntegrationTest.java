package com.hybris.classificationgroupsservices.interceptors;

import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.CategoryService;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;

@IntegrationTest
public abstract class AbstractGroupAssignmentPrepareInterceptorIntegrationTest extends ServicelayerTest
{
    protected static final String OBJECT_CATEGORY = "Object";
    protected static final String ITEM_CATEGORY = "Item";
    protected static final String DIMENSIONS_ATTRIBUTE = "Dimensions";
    protected static final String WEIGHT_ATTRIBUTE = "Weight";
    protected static final String PHONE_CATEGORY = "Phone";
    protected static final String SAMSUNG_CATEGORY = "Samsung";
    protected static final String DEVICE_CATEGORY = "Device";
    protected static final String PRICE_ATTRIBUTE = "Price";
    protected static final String TOUCH_ID_ATTRIBUTE = "TouchId";
    protected static final String RAM_ATTRIBUTE = "RAM";
    private static final Pattern PATTERN = Pattern.compile("(?<catalogId>[^/]+)/(?<systemVersion>[^/]+)/(?<classificationClassCode>[^.]+)\\.(?<classificationAttributeCode>.+)");
    private static final String FIND_CLASSIFICATION_ATTRIBUTE_ASSIGNMENT = String.format(
                    "SELECT {caa:%s} FROM {%s as caa JOIN %s as cc ON {cc:%s} = {caa:%s} JOIN %s as ca ON {caa:%s} = {ca:%s} JOIN %s as csv ON {caa:%s} = {csv:%s} JOIN %s as c ON {csv:%s} = {c:%s}} WHERE {cc:%s} = ?%s AND LOWER({ca:%s}) = ?%s AND {csv:%s} = ?%s AND {c:%s} = ?%s", new Object[] {
                                    "pk", "ClassAttributeAssignment", "ClassificationClass", "pk", "classificationClass", "ClassificationAttribute", "classificationAttribute", "pk", "ClassificationSystemVersion", "systemVersion",
                                    "pk", "Catalog", "catalog", "pk", "code", "ClassificationClasscode", "code", "ClassificationAttributecode", "version", "version",
                                    "id", "id"});
    @Resource
    protected ModelService modelService;
    @Resource
    protected FlexibleSearchService flexibleSearchService;
    @Resource
    protected CategoryService categoryService;
    @Resource
    protected CatalogVersionService catalogVersionService;


    protected void assertClassFeatureGroupAssignments(Pair<String, List<String>> pair, List<ClassFeatureGroupAssignmentModel> groupAssignments)
    {
        List<String> classificationAttributeCodes = (List<String>)groupAssignments.stream().filter(groupAssignment -> groupAssignment.getClassificationClass().getCode().equals(pair.getLeft()))
                        .map(groupAssignment -> groupAssignment.getClassAttributeAssignment().getClassificationAttribute().getCode()).collect(Collectors.toList());
        Assertions.assertThat((List)pair.getRight()).containsExactlyInAnyOrder((Object[])classificationAttributeCodes.<String>toArray(new String[0]));
    }


    protected void createCategory(CatalogVersionModel catalogVersion, String categoryCode, List<CategoryModel> categories)
    {
        ClassificationClassModel classificationClass = (ClassificationClassModel)this.modelService.create(ClassificationClassModel.class);
        classificationClass.setCatalogVersion(catalogVersion);
        classificationClass.setCode(categoryCode);
        classificationClass.setSupercategories(categories);
        this.modelService.save(classificationClass);
    }


    protected List<ClassFeatureGroupAssignmentModel> finaAllClassFeatureGroupAssignments()
    {
        SearchResult<ClassFeatureGroupAssignmentModel> searchResult = this.flexibleSearchService.search("select {pk} from {ClassFeatureGroupAssignment}");
        return searchResult.getResult();
    }


    protected ClassAttributeAssignmentModel findClassAttributeAssignment(String classificationAttributeQualifier)
    {
        Matcher matcher = PATTERN.matcher(classificationAttributeQualifier);
        if(matcher.find())
        {
            String catalogId = matcher.group("catalogId");
            String systemVersion = matcher.group("systemVersion");
            String classificationClassCode = matcher.group("classificationClassCode");
            String classificationAttributeCode = matcher.group("classificationAttributeCode");
            if(StringUtils.isNotBlank(catalogId) && StringUtils.isNotBlank(systemVersion) &&
                            StringUtils.isNotBlank(classificationClassCode) && StringUtils.isNotBlank(classificationAttributeCode))
            {
                return findClassAttributeAssignment(catalogId, systemVersion, classificationClassCode, classificationAttributeCode);
            }
        }
        return null;
    }


    protected ClassAttributeAssignmentModel findClassAttributeAssignment(String catalogId, String systemVersionId, String classificationClassCode, String attributeCode)
    {
        return getClassificationAttributeAssignment(catalogId, systemVersionId, classificationClassCode, attributeCode);
    }


    protected ClassAttributeAssignmentModel getClassificationAttributeAssignment(String catalogId, String systemVersion, String classificationClass, String attribute)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_CLASSIFICATION_ATTRIBUTE_ASSIGNMENT);
        query.addQueryParameter("id", catalogId);
        query.addQueryParameter("version", systemVersion);
        query.addQueryParameter("ClassificationClasscode", classificationClass);
        query.addQueryParameter("ClassificationAttributecode", attribute
                        .toLowerCase(Locale.US));
        return (ClassAttributeAssignmentModel)this.flexibleSearchService.searchUnique(query);
    }
}
