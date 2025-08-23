package com.hybris.backoffice.solrsearch.dataaccess;

import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException;
import de.hybris.platform.solrfacetsearch.model.config.SolrFacetSearchConfigModel;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class SolrSearchStrategyTest
{
    @Mock
    private BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;
    @InjectMocks
    private SolrSearchStrategy solrSearchStrategy;
    final String testTypeCode = "typeCode";
    final String testFieldName = "testFieldName";
    final String testType = "java.lang.String";


    @Before
    public void setUp()
    {
        Map<String, String> typeMappings = new HashMap<>();
        typeMappings.put("text", "java.lang.String");
        typeMappings.put("sortabletext", "java.lang.String");
        typeMappings.put("string", "java.lang.String");
        this.solrSearchStrategy.setTypeMappings(typeMappings);
    }


    @Test
    public void checkGetFieldType() throws FacetConfigServiceException
    {
        FacetSearchConfig facetSearchConfig = createFacetSearchConfigData();
        Mockito.when(this.backofficeFacetSearchConfigService.getFacetSearchConfig("typeCode")).thenReturn(facetSearchConfig);
        Assertions.assertThat(this.solrSearchStrategy.getFieldType("typeCode", "testFieldName")).isEqualTo("java.lang.String");
        Assertions.assertThat(this.solrSearchStrategy.isLocalized("typeCode", "testFieldName")).isEqualTo(true);
    }


    @Test
    public void shouldGetOnlyIndexedLanguages() throws FacetConfigServiceException
    {
        LanguageModel languageEnglish = new LanguageModel(Locale.ENGLISH.getLanguage());
        LanguageModel languageGerman = new LanguageModel(Locale.GERMAN.getLanguage());
        SolrFacetSearchConfigModel solrFacetSearchConfigModel = (SolrFacetSearchConfigModel)Mockito.mock(SolrFacetSearchConfigModel.class);
        Mockito.when(this.backofficeFacetSearchConfigService.getFacetSearchConfigModel("typeCode")).thenReturn(solrFacetSearchConfigModel);
        Mockito.when(solrFacetSearchConfigModel.getLanguages()).thenReturn(List.of(languageEnglish, languageGerman));
        Collection<String> availableLanguages = this.solrSearchStrategy.getAvailableLanguages("typeCode");
        Assertions.assertThat(availableLanguages).containsOnly((Object[])new String[] {Locale.ENGLISH.getLanguage(), Locale.GERMAN.getLanguage()});
    }


    private FacetSearchConfig createFacetSearchConfigData()
    {
        FacetSearchConfig facetConfig = new FacetSearchConfig();
        IndexConfig indexConfig = (IndexConfig)Mockito.mock(IndexConfig.class);
        IndexedType indexedType = (IndexedType)Mockito.mock(IndexedType.class);
        ComposedTypeModel composedTypeModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        Mockito.when(indexedType.getComposedType()).thenReturn(composedTypeModel);
        Mockito.when(indexedType.getComposedType().getCode()).thenReturn("typeCode");
        Map<String, IndexedProperty> indexedPropertyMap = (Map<String, IndexedProperty>)Mockito.mock(Map.class);
        Mockito.when(indexedPropertyMap.get(Matchers.anyString())).thenAnswer(invocationOnMock -> {
            String name = (String)invocationOnMock.getArguments()[0];
            IndexedProperty indexedProperty = new IndexedProperty();
            indexedProperty.setName(name);
            indexedProperty.setBackofficeDisplayName(name);
            indexedProperty.setLocalized(true);
            indexedProperty.setType("string");
            return indexedProperty;
        });
        Mockito.when(indexedType.getIndexedProperties()).thenReturn(indexedPropertyMap);
        Map<String, IndexedType> indexedTypeMap = new HashMap<>();
        indexedTypeMap.put("typeCode", indexedType);
        Mockito.when(indexConfig.getIndexedTypes()).thenReturn(indexedTypeMap);
        facetConfig.setName("facetConfigName");
        facetConfig.setIndexConfig(indexConfig);
        return facetConfig;
    }
}
