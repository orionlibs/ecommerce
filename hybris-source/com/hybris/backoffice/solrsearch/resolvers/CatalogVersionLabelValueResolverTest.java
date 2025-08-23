package com.hybris.backoffice.solrsearch.resolvers;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.mockito.Mockito;

public class CatalogVersionLabelValueResolverTest
{
    private CatalogVersionLabelValueResolver resolver = new CatalogVersionLabelValueResolver();


    @Test
    public void resolveIndexKey()
    {
        LanguageModel language = (LanguageModel)Mockito.mock(LanguageModel.class);
        ((LanguageModel)Mockito.doReturn("ISOCODE").when(language)).getIsocode();
        IndexedProperty property = (IndexedProperty)Mockito.mock(IndexedProperty.class);
        ((IndexedProperty)Mockito.doReturn("propertyName").when(property)).getName();
        ((IndexedProperty)Mockito.doReturn("propertyType").when(property)).getType();
        String key = this.resolver.resolveIndexKey(property, language);
        Assertions.assertThat(key).isEqualTo("propertyName_propertyType_isocode");
    }
}
