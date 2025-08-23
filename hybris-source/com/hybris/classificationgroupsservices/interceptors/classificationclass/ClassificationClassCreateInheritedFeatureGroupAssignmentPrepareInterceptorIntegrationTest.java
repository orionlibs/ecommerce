package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import com.hybris.classificationgroupsservices.interceptors.AbstractGroupAssignmentPrepareInterceptorIntegrationTest;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.impex.jalo.ImpExException;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class ClassificationClassCreateInheritedFeatureGroupAssignmentPrepareInterceptorIntegrationTest extends AbstractGroupAssignmentPrepareInterceptorIntegrationTest
{
    private static final String IPHONE_CATEGORY = "Iphone";


    @Before
    public void setUp() throws ImpExException
    {
        importCsv("/impex/test/testAssignmentPrepareInterceptor.impex", "UTF-8");
    }


    @Test
    public void shouldCreateGroupAssignmentsForNewCategory()
    {
        CatalogVersionModel catalogVersion = this.catalogVersionService.getCatalogVersion("classAttributeAssignmentCatalog", "Staged");
        CategoryModel phoneCategory = this.categoryService.getCategoryForCode("Phone");
        createCategory(catalogVersion, "Iphone", List.of(phoneCategory));
        List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Device", List.of("Price")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Phone", List.of("Price", "RAM")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Iphone", List.of("Price", "RAM")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Samsung",
                        List.of("Price", "RAM", "TouchId")), groupAssignments);
        Assertions.assertThat(groupAssignments).hasSize(8);
    }
}
