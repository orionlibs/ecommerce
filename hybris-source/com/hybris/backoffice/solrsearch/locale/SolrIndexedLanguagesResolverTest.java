package com.hybris.backoffice.solrsearch.locale;

import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.backoffice.widgets.fulltextsearch.FullTextSearchStrategy;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SolrIndexedLanguagesResolverTest
{
    private static final String ISO_CODE = "ISO_CODE";
    private static final String TYPE_CODE = "TYPE_CODE";
    private static final String SECOND_TYPE_CODE = "TYPE_CODE";
    @InjectMocks
    private SolrIndexedLanguagesResolver testSubject;
    @Mock
    private BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;
    @Mock
    private FullTextSearchStrategy fullTextSearchStrategy;


    @Test
    public void shouldInformThatLanguageIsNotIndexedIfNoConfigurationExists()
    {
        Mockito.when(this.backofficeFacetSearchConfigService.getAllMappedTypes()).thenReturn(Collections.emptyList());
        boolean isIndexed = this.testSubject.isIndexed("ISO_CODE");
        Assertions.assertThat(isIndexed).isTrue();
    }


    @Test
    public void shouldInformThatLanguageIsNotIndexedIfOneConfigurationExistsAndLanguageIsNotAvailable()
    {
        ComposedTypeModel model = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        Collection<ComposedTypeModel> models = Collections.singletonList(model);
        Mockito.when(this.backofficeFacetSearchConfigService.getAllMappedTypes()).thenReturn(models);
        boolean isIndexed = this.testSubject.isIndexed("ISO_CODE");
        Assertions.assertThat(isIndexed).isFalse();
    }


    @Test
    public void shouldInformThatLanguageIsIndexedIfOneConfigurationExistsAndLanguageIsAvailable()
    {
        ComposedTypeModel model = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        Mockito.when(model.getCode()).thenReturn("TYPE_CODE");
        Collection<ComposedTypeModel> models = Collections.singletonList(model);
        Mockito.when(this.backofficeFacetSearchConfigService.getAllMappedTypes()).thenReturn(models);
        List<String> languages = Collections.singletonList("ISO_CODE");
        Mockito.when(this.fullTextSearchStrategy.getAvailableLanguages("TYPE_CODE")).thenReturn(languages);
        boolean isIndexed = this.testSubject.isIndexed("ISO_CODE");
        Assertions.assertThat(isIndexed).isTrue();
    }


    @Test
    public void shouldInformThatLanguageIsNotIndexedIfTwoConfigurationsExistAndLanguageIsNotAvailableInOneOfThem()
    {
        ComposedTypeModel firstModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        Mockito.when(firstModel.getCode()).thenReturn("TYPE_CODE");
        ComposedTypeModel secondModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        Collection<ComposedTypeModel> models = List.of(firstModel, secondModel);
        Mockito.when(this.backofficeFacetSearchConfigService.getAllMappedTypes()).thenReturn(models);
        List<String> languagesForFirstTypeCode = Collections.singletonList("ISO_CODE");
        Mockito.when(this.fullTextSearchStrategy.getAvailableLanguages("TYPE_CODE")).thenReturn(languagesForFirstTypeCode);
        List<String> languagesForSecondTypeCode = Collections.emptyList();
        Mockito.when(this.fullTextSearchStrategy.getAvailableLanguages("TYPE_CODE")).thenReturn(languagesForSecondTypeCode);
        boolean isIndexed = this.testSubject.isIndexed("ISO_CODE");
        Assertions.assertThat(isIndexed).isFalse();
    }


    @Test
    public void shouldInformThatLanguageIsIndexedIfTwoConfigurationsExistAndLanguageIsAvailableInBothOfThem()
    {
        ComposedTypeModel firstModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        Mockito.when(firstModel.getCode()).thenReturn("TYPE_CODE");
        ComposedTypeModel secondModel = (ComposedTypeModel)Mockito.mock(ComposedTypeModel.class);
        Mockito.when(secondModel.getCode()).thenReturn("TYPE_CODE");
        Collection<ComposedTypeModel> models = List.of(firstModel, secondModel);
        Mockito.when(this.backofficeFacetSearchConfigService.getAllMappedTypes()).thenReturn(models);
        List<String> languagesForFirstTypeCode = Collections.singletonList("ISO_CODE");
        Mockito.when(this.fullTextSearchStrategy.getAvailableLanguages("TYPE_CODE")).thenReturn(languagesForFirstTypeCode);
        List<String> languagesForSecondTypeCode = Collections.singletonList("ISO_CODE");
        Mockito.when(this.fullTextSearchStrategy.getAvailableLanguages("TYPE_CODE")).thenReturn(languagesForSecondTypeCode);
        boolean isIndexed = this.testSubject.isIndexed("ISO_CODE");
        Assertions.assertThat(isIndexed).isTrue();
    }
}
