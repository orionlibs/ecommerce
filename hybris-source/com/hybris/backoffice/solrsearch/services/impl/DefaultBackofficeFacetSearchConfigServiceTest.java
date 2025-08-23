package com.hybris.backoffice.solrsearch.services.impl;

import com.hybris.backoffice.solrsearch.model.BackofficeIndexedTypeToSolrFacetSearchConfigModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfigService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultBackofficeFacetSearchConfigServiceTest
{
    public static final String TYPE = "Type";
    public static final String SUB_TYPE = "SubType";
    @Spy
    @InjectMocks
    private BackofficeSolrFacetSearchConfigService service;
    @Mock
    private ComposedTypeModel typeModel;
    @Mock
    private ComposedTypeModel subTypeModel;
    @Mock
    private BackofficeIndexedTypeToSolrFacetSearchConfigModel configType;
    @Mock
    private BackofficeIndexedTypeToSolrFacetSearchConfigModel configSubtype;
    @Mock
    private FacetSearchConfigService facetSearchConfigService;


    @Test
    public void findMatchingConfigShouldReturnResultOfRecursiveLookup()
    {
    }


    @Test
    public void findMatchingConfigShouldReturnNullOnEmptyConfig()
    {
    }


    @Test
    public void findMatchingConfigShouldReturnTheOnlyElementWithNoFurtherChecks()
    {
    }


    @Test
    public void shouldReturnEmptyCollectionWhenNoFacetSearchConfigIsPresent()
    {
    }


    @Test
    public void shouldReturnEmptyCollectionWhenFacetSearchConfigIsPresentAndDoesNotHaveIndexedType()
    {
    }


    @Test
    public void shouldReturnComposedTypeModelWhenFacetSearchConfigIsPresentAndHasIndexedType()
    {
    }
}
