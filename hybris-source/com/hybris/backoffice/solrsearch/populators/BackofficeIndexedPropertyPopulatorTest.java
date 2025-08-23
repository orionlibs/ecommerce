package com.hybris.backoffice.solrsearch.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.catalog.model.classification.ClassAttributeAssignmentModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import org.assertj.core.api.BDDAssertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class BackofficeIndexedPropertyPopulatorTest
{
    private BackofficeIndexedPropertyPopulator testSubject;
    @Mock
    private SolrIndexedPropertyModel solrIndexedPropertyModelMock;
    @Mock
    private IndexedProperty indexedPropertyMock;
    @Captor
    private ArgumentCaptor<String> stringCaptor;
    @Captor
    private ArgumentCaptor<ClassAttributeAssignmentModel> classAttributeAssignmentModelArgumentCaptor;


    @Before
    public void setUp()
    {
        this.testSubject = new BackofficeIndexedPropertyPopulator();
    }


    @Test
    public void shouldPopulateBackofficeDisplayName()
    {
        this.testSubject.populate(this.solrIndexedPropertyModelMock, this.indexedPropertyMock);
        ((IndexedProperty)BDDMockito.then(this.indexedPropertyMock).should()).setBackofficeDisplayName((String)this.stringCaptor.capture());
        BDDAssertions.then((String)this.stringCaptor.getValue()).isEqualTo(this.solrIndexedPropertyModelMock.getBackofficeDisplayName());
    }


    @Test
    public void shouldClassAttributeAssignment()
    {
        this.testSubject.populate(this.solrIndexedPropertyModelMock, this.indexedPropertyMock);
        ((IndexedProperty)BDDMockito.then(this.indexedPropertyMock).should()).setClassAttributeAssignment((ClassAttributeAssignmentModel)this.classAttributeAssignmentModelArgumentCaptor.capture());
        BDDAssertions.then(this.classAttributeAssignmentModelArgumentCaptor.getValue())
                        .isEqualTo(this.solrIndexedPropertyModelMock.getClassAttributeAssignment());
    }
}
