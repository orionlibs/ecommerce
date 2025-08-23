package com.hybris.backoffice.excel.importing;

import com.hybris.backoffice.excel.ExcelIntegrationTest;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.io.IOException;
import java.util.Collection;
import javax.annotation.Resource;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;

public class ExcelImportServiceIntegrationTest extends ExcelIntegrationTest
{
    private static final String QUERY_FOR_IMPORTED_PRODUCTS = "SELECT {product:PK} FROM {Product AS product} WHERE {product:code} like 'product'";
    @Rule
    public JUnitSoftAssertions soft = new JUnitSoftAssertions();
    @Resource
    ExcelImportService excelImportService;
    @Resource(name = "excelImpexConverter")
    ImpexConverter impexConverter;
    @Resource
    ImportService importService;
    @Resource
    FlexibleSearchService flexibleSearchService;


    @Test
    public void shouldImportExcelFile() throws IOException
    {
        saveItem((ItemModel)createCatalogVersionModel("catalog", "1.0"));
        setAttributeDescriptorNamesForProductCodeAndCatalogVersion();
        setAttributeDescriptorNameForOrder();
        XSSFWorkbook xSSFWorkbook = new XSSFWorkbook(getClass().getResourceAsStream("/test/excel/import.xlsx"));
        try
        {
            ImportResult importResult = this.importService.importData(
                            createImportConfig(this.impexConverter
                                            .convert(this.excelImportService
                                                            .convertToImpex((Workbook)xSSFWorkbook))));
            Assertions.assertThat(importResult).isNotNull();
            this.soft.assertThat(importResult.isFinished()).isTrue();
            this.soft.assertThat(importResult.isError()).isFalse();
            this.soft.assertThat(importResult.isSuccessful()).isTrue();
            Collection<ProductModel> importedProducts = getResult("SELECT {product:PK} FROM {Product AS product} WHERE {product:code} like 'product'");
            this.soft.assertThat(importedProducts).extracting("code").containsOnly(new Object[] {"product"});
            this.soft.assertThat(importedProducts).extracting("order").containsOnly(new Object[] {Integer.valueOf(123)});
            xSSFWorkbook.close();
        }
        catch(Throwable throwable)
        {
            try
            {
                xSSFWorkbook.close();
            }
            catch(Throwable throwable1)
            {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
    }


    private void setAttributeDescriptorNameForOrder()
    {
        AttributeDescriptorModel order = getTypeService().getAttributeDescriptor("Product", "order");
        order.setName("Article order");
        getModelService().saveAll(new Object[] {order});
    }


    private <T> Collection<T> getResult(String query)
    {
        return this.flexibleSearchService.search(query).getResult();
    }
}
