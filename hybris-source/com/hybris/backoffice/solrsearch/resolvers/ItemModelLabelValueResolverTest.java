package com.hybris.backoffice.solrsearch.resolvers;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import org.fest.assertions.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ItemModelLabelValueResolverTest
{
    @Spy
    private ItemModelLabelValueResolver resolver;


    @Test
    public void resolveIndexKeyShouldCreateFieldWithLowercaseIsoCode()
    {
        IndexedProperty indexProperty = new IndexedProperty();
        indexProperty.setName("catalogVersion");
        indexProperty.setType("string");
        LanguageModel language = new LanguageModel();
        language.setIsocode("es_CO");
        String result = this.resolver.resolveIndexKey(indexProperty, language);
        Assertions.assertThat(result).isEqualTo("catalogVersion_es_co_string");
    }
}
