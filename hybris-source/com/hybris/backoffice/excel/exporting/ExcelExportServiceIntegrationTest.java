package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.ExcelIntegrationTest;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;
import java.io.IOException;
import java.util.Collections;
import javax.annotation.Resource;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.JUnitSoftAssertions;
import org.junit.Rule;
import org.junit.Test;

public class ExcelExportServiceIntegrationTest extends ExcelIntegrationTest
{
    @Rule
    public JUnitSoftAssertions soft = new JUnitSoftAssertions();
    @Resource
    DefaultExcelExportService excelExportService;


    @Test
    public void shouldExportSelectedItems() throws IOException
    {
        CatalogVersionModel catalogVersion = createCatalogVersionModel("catalog", "1.0");
        ProductModel product = prepareProduct("product", catalogVersion);
        product.setEan("123");
        saveItem((ItemModel)product);
        SelectedAttribute selectedAttribute = new SelectedAttribute();
        selectedAttribute.setAttributeDescriptor(getAttributeDescriptorOf((ItemModel)product, "ean"));
        Workbook workbook = this.excelExportService.exportData(Collections.singletonList(product),
                        Collections.singletonList(selectedAttribute));
        try
        {
            Assertions.assertThat((Iterable)workbook.getSheet("Product")).isNotNull();
            this.soft.assertThat(getCellAt(workbook, "Product", 0, 3)).isEqualTo("1.0:catalog");
            this.soft.assertThat(getCellAt(workbook, "Product", 1, 3)).isEqualTo("product");
            this.soft.assertThat(getCellAt(workbook, "Product", 2, 3)).isEqualTo("123");
            Assertions.assertThat((Iterable)workbook.getSheet("TypeSystem")).isNotNull();
            if(workbook != null)
            {
                workbook.close();
            }
        }
        catch(Throwable throwable)
        {
            if(workbook != null)
            {
                try
                {
                    workbook.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    @Test
    public void shouldExportTemplate() throws IOException
    {
        VariantTypeModel variantTypeModel = (VariantTypeModel)saveItem((ItemModel)prepareVariant());
        ProductModel product = (ProductModel)saveItem((ItemModel)
                        prepareProductWithVariant(prepareProduct("product", createCatalogVersionModel("catalog", "1.0")), variantTypeModel));
        saveItem((ItemModel)prepareVariantProductModel(product, variantTypeModel));
        Workbook workbook = this.excelExportService.exportTemplate("Product");
        try
        {
            Assertions.assertThat((Iterable)workbook.getSheet("Product")).isNotNull();
            this.soft.assertThat(getCellAt(workbook, "Product", 0, 0)).isEqualTo("catalogVersion*^");
            this.soft.assertThat(getCellAt(workbook, "Product", 1, 0)).isEqualTo("code*^");
            if(workbook != null)
            {
                workbook.close();
            }
        }
        catch(Throwable throwable)
        {
            if(workbook != null)
            {
                try
                {
                    workbook.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
    }


    protected String getCellAt(Workbook workbook, String sheetName, int column, int row)
    {
        return workbook.getSheet(sheetName).getRow(row).getCell(column).getStringCellValue();
    }
}
