package com.hybris.backoffice.excel.translators.generic.factory;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.testframework.Transactional;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Test;

@Transactional
@IntegrationTest
public class DefaultExportDataFactoryIntegrationTest extends ServicelayerTest
{
    @Resource
    DefaultExportDataFactory defaultExportDataFactory;


    @Test
    public void shouldExportValueForCatalogVersion()
    {
        CatalogModel catalogModel = new CatalogModel();
        catalogModel.setId("Default");
        CatalogVersionModel catalogVersionModel = new CatalogVersionModel();
        catalogVersionModel.setVersion("Online");
        catalogVersionModel.setCatalog(catalogModel);
        Optional<String> exportedValue = this.defaultExportDataFactory.create(RequiredAttributeTestFactory.prepareStructureForCatalogVersion(), catalogVersionModel);
        Assertions.assertThat(exportedValue).isPresent();
        Assertions.assertThat(exportedValue.get()).isEqualTo("Online:Default");
    }


    @Test
    public void shouldExportValueForSupercategories()
    {
        CatalogModel catalogModel = new CatalogModel();
        catalogModel.setId("Default");
        CatalogVersionModel catalogVersionModel = new CatalogVersionModel();
        catalogVersionModel.setVersion("Online");
        catalogVersionModel.setCatalog(catalogModel);
        CategoryModel firstCategory = new CategoryModel();
        firstCategory.setCode("firstCategory");
        firstCategory.setCatalogVersion(catalogVersionModel);
        CategoryModel secondCategory = new CategoryModel();
        secondCategory.setCode("secondCategory");
        secondCategory.setCatalogVersion(catalogVersionModel);
        Optional<String> exportedValue = this.defaultExportDataFactory.create(
                        RequiredAttributeTestFactory.prepareStructureForSupercategories(), Arrays.asList(new CategoryModel[] {firstCategory, secondCategory}));
        Assertions.assertThat(exportedValue).isPresent();
        Assertions.assertThat(exportedValue.get()).isEqualTo("firstCategory:Online:Default,secondCategory:Online:Default");
    }


    @Test
    public void shouldExportValueForPrices()
    {
        Collection<PriceRowModel> priceRows = Arrays.asList(new PriceRowModel[] {preparePriceRow(Double.valueOf(3.14D), "PLN", "pieces"),
                        preparePriceRow(Double.valueOf(1.1D), "USD", "pieces")});
        Optional<String> exportedValue = this.defaultExportDataFactory.create(RequiredAttributeTestFactory.prepareStructureForPrices(), priceRows);
        Assertions.assertThat(exportedValue).isPresent();
        Assertions.assertThat(exportedValue.get()).isEqualTo("3.14:pieces:PLN,1.1:pieces:USD");
    }


    private static PriceRowModel preparePriceRow(Double value, String currency, String unit)
    {
        CurrencyModel currencyModel = new CurrencyModel();
        currencyModel.setIsocode(currency);
        UnitModel unitModel = new UnitModel();
        unitModel.setCode(unit);
        PriceRowModel priceRowModel = new PriceRowModel();
        priceRowModel.setPrice(value);
        priceRowModel.setCurrency(currencyModel);
        priceRowModel.setUnit(unitModel);
        return priceRowModel;
    }
}
