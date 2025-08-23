package com.hybris.backoffice.excel.translators.generic.factory;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultReferenceFormatFactoryTest
{
    private final ReferenceFormatFactory referenceFormatFactory = (ReferenceFormatFactory)new DefaultReferenceFormatFactory();


    @Test
    public void shouldCalculateReferenceFormatForCatalogVersion()
    {
        String referenceFormat = this.referenceFormatFactory.create(RequiredAttributeTestFactory.prepareStructureForCatalogVersion());
        Assertions.assertThat(referenceFormat).isEqualTo("CatalogVersion.version:Catalog.id");
    }


    @Test
    public void shouldCalculateReferenceFormatForSupercategories()
    {
        String referenceFormat = this.referenceFormatFactory.create(RequiredAttributeTestFactory.prepareStructureForSupercategories());
        Assertions.assertThat(referenceFormat).isEqualTo("Category.code:CatalogVersion.version:Catalog.id");
    }


    @Test
    public void shouldCalculateReferenceFormatForPrices()
    {
        String referenceFormat = this.referenceFormatFactory.create(RequiredAttributeTestFactory.prepareStructureForPrices());
        Assertions.assertThat(referenceFormat).isEqualTo("PriceRow.price:Unit.code:Currency.isocode");
    }
}
