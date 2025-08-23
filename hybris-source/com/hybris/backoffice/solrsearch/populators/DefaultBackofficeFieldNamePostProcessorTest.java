package com.hybris.backoffice.solrsearch.populators;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.solrfacetsearch.search.SearchQuery;
import java.util.Locale;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class DefaultBackofficeFieldNamePostProcessorTest
{
    protected static final String ISOCODE_DE = "de";
    protected static final String ISOCODE_EN = "en";
    protected static final String ISOCODE_ES_CO = "es_CO";
    protected static final String VALUE = "*dededede*";
    protected static final String ENGLISH_FIELD_NAME_WITHOUT_VALUE = "name_text_en";
    protected static final String ENGLISH_FIELD_NAME_WITH_VALUE = "name_text_en".concat(":")
                    .concat("*dededede*");
    protected static final String GERMAN_FIELD_NAME_WITHOUT_VALUE = "name_text_de";
    protected static final String GERMAN_FIELD_NAME_WITH_VALUE = "name_text_de".concat(":")
                    .concat("*dededede*");
    protected static final String SPANISH_FIELD_NAME_WITHOUT_VALUE = "name_text_es_CO";
    @Spy
    private DefaultBackofficeFieldNamePostProcessor namePostProcessor;
    @Mock
    private SearchQuery searchQuery;
    @Mock
    private I18NService i18nService;
    @Mock
    private CommonI18NService commonI18NService;
    @Mock
    private LanguageModel conditionLanguage;


    @Before
    public void setup()
    {
        this.namePostProcessor.setCommonI18NService(this.commonI18NService);
        this.namePostProcessor.setI18nService(this.i18nService);
        Mockito.when(this.i18nService.getBestMatchingLocale((Locale)ArgumentMatchers.nullable(Locale.class))).thenReturn(Locale.GERMAN);
    }


    @Test
    public void shouldReplaceOnlyIsoCode()
    {
        String replacement = "de".concat(":");
        String result = ENGLISH_FIELD_NAME_WITH_VALUE.replaceFirst("([^:_]*):", replacement);
        Assertions.assertThat(result).isEqualTo(GERMAN_FIELD_NAME_WITH_VALUE);
    }


    @Test
    public void shouldProcessFieldLocaleWithValue()
    {
        Mockito.when(this.namePostProcessor.retrieveLanguageModel((Locale)ArgumentMatchers.nullable(Locale.class))).thenReturn(this.conditionLanguage);
        Mockito.when(this.searchQuery.getLanguage()).thenReturn("en");
        Mockito.when(this.conditionLanguage.getIsocode()).thenReturn("de");
        String result = this.namePostProcessor.process(this.searchQuery, Locale.GERMAN, ENGLISH_FIELD_NAME_WITH_VALUE);
        Assertions.assertThat(result).isEqualTo(GERMAN_FIELD_NAME_WITH_VALUE);
    }


    @Test
    public void shouldProcessFieldLocaleWithoutValue()
    {
        Mockito.when(this.namePostProcessor.retrieveLanguageModel((Locale)ArgumentMatchers.nullable(Locale.class))).thenReturn(this.conditionLanguage);
        Mockito.when(this.conditionLanguage.getIsocode()).thenReturn("de");
        Mockito.when(this.searchQuery.getLanguage()).thenReturn("en");
        String result = this.namePostProcessor.process(this.searchQuery, Locale.ENGLISH, "name_text_en");
        Assertions.assertThat(result).isEqualTo("name_text_de");
    }


    @Test
    public void shouldProcessFieldLocaleWithRegion()
    {
        Mockito.when(this.namePostProcessor.retrieveLanguageModel((Locale)ArgumentMatchers.nullable(Locale.class))).thenReturn(this.conditionLanguage);
        Mockito.when(this.searchQuery.getLanguage()).thenReturn("en");
        Mockito.when(this.conditionLanguage.getIsocode()).thenReturn("es_CO");
        String result = this.namePostProcessor.process(this.searchQuery, Locale.GERMAN, "name_text_es_CO");
        Assertions.assertThat(result).isEqualTo("name_text_es_CO");
    }
}
