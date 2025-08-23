package com.hybris.classificationgroupsservices.interceptors.classattributeassignment;

import com.hybris.classificationgroupsservices.interceptors.AbstractGroupAssignmentPrepareInterceptorIntegrationTest;
import com.hybris.classificationgroupsservices.model.ClassFeatureGroupAssignmentModel;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.enums.ClassificationAttributeTypeEnum;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.TestImportCsvUtil;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class ClassAttributeAssignmentCreateGroupAssignmentPrepareInterceptorIntegrationTest extends AbstractGroupAssignmentPrepareInterceptorIntegrationTest
{
    @Resource(name = "testImportCsvUtil")
    protected TestImportCsvUtil importCsvUtil;


    @Before
    public void setUp() throws ImpExException
    {
        this.importCsvUtil.importCsv("/impex/test/testAssignmentPrepareInterceptor.impex", "UTF-8");
    }


    @Test
    public void shouldCreateGroupAssignmentForCategoryAndSubcategories()
    {
        CatalogVersionModel catalogVersion = this.catalogVersionService.getCatalogVersion("classAttributeAssignmentCatalog", "Staged");
        ClassificationClassModel phone = (ClassificationClassModel)this.categoryService.getCategoryForCode("Phone");
        ClassificationAttributeModel classificationAttribute = (ClassificationAttributeModel)this.modelService.create(ClassificationAttributeModel.class);
        classificationAttribute.setCode("weight");
        classificationAttribute.setSystemVersion((ClassificationSystemVersionModel)catalogVersion);
        this.modelService.save(classificationAttribute);
        ClassAttributeAssignmentModel classAttributeAssignment = (ClassAttributeAssignmentModel)this.modelService.create(ClassAttributeAssignmentModel.class);
        classAttributeAssignment.setClassificationAttribute(classificationAttribute);
        classAttributeAssignment.setSystemVersion((ClassificationSystemVersionModel)catalogVersion);
        classAttributeAssignment.setClassificationClass(phone);
        classAttributeAssignment.setAttributeType(ClassificationAttributeTypeEnum.STRING);
        this.modelService.save(classAttributeAssignment);
        List<ClassFeatureGroupAssignmentModel> groupAssignments = finaAllClassFeatureGroupAssignments();
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Device", List.of("Price")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Phone", List.of("Price", "RAM", "weight")), groupAssignments);
        assertClassFeatureGroupAssignments((Pair)new ImmutablePair("Samsung", List.of("Price", "RAM", "TouchId", "weight")), groupAssignments);
        Assertions.assertThat(groupAssignments).hasSize(8);
    }
}
