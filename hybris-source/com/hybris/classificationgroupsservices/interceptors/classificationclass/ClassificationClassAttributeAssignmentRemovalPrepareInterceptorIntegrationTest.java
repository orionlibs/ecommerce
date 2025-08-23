package com.hybris.classificationgroupsservices.interceptors.classificationclass;

import com.hybris.classificationgroupsservices.interceptors.AbstractGroupAssignmentPrepareInterceptorIntegrationTest;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.impex.jalo.ImpExException;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class ClassificationClassAttributeAssignmentRemovalPrepareInterceptorIntegrationTest extends AbstractGroupAssignmentPrepareInterceptorIntegrationTest
{
    @Before
    public void setUp() throws ImpExException
    {
        importCsv("/impex/test/testAssignmentPrepareInterceptor.impex", "UTF-8");
    }


    @Test
    public void shouldRemoveClassFeatureGroupAssignmentsInSubcategoriesDuringUnassigningAttributeAssignment()
    {
        ClassificationClassModel phoneCategory = (ClassificationClassModel)this.categoryService.getCategoryForCode("Phone");
        phoneCategory.setDeclaredClassificationAttributeAssignments(List.of());
        this.modelService.save(phoneCategory);
        List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Device", List.of("Price")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Phone", List.of("Price")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Samsung", List.of("Price", "TouchId")), groupAssignments);
        Assertions.assertThat(groupAssignments).hasSize(4);
    }
}
