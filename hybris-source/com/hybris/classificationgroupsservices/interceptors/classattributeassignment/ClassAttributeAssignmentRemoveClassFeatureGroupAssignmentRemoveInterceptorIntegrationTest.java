package com.hybris.classificationgroupsservices.interceptors.classattributeassignment;

import com.hybris.classificationgroupsservices.interceptors.AbstractGroupAssignmentPrepareInterceptorIntegrationTest;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.impex.jalo.ImpExException;
import java.util.List;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class ClassAttributeAssignmentRemoveClassFeatureGroupAssignmentRemoveInterceptorIntegrationTest extends AbstractGroupAssignmentPrepareInterceptorIntegrationTest
{
    @Before
    public void setUp() throws ImpExException
    {
        importCsv("/impex/test/testAssignmentPrepareInterceptor.impex", "UTF-8");
    }


    @Test
    public void shouldRemoveGroupAssignmentsInSubcategoriesDuringRemovingAttributeAssignment()
    {
        ClassAttributeAssignmentModel classAttributeAssignment = findClassAttributeAssignment("classAttributeAssignmentCatalog/Staged/Phone.RAM");
        this.modelService.remove(classAttributeAssignment);
        List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
        assertClassFeatureGroupAssignments((Pair)ImmutablePair.of("Device", List.of("Price")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)ImmutablePair.of("Phone", List.of("Price")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)ImmutablePair.of("Samsung", List.of("Price", "TouchId")), groupAssignments);
        Assertions.assertThat(groupAssignments).hasSize(4);
    }
}
