package com.hybris.backoffice.excel.exporting;

import com.hybris.backoffice.excel.ExcelIntegrationTest;
import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.ExcelTemplateService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.annotation.Resource;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.AssertionsForClassTypes;
import org.assertj.core.api.JUnitSoftAssertions;
import org.assertj.core.util.Lists;
import org.junit.Rule;
import org.junit.Test;

public class HeaderPromptWorkbookDecoratorIntegrationTest extends ExcelIntegrationTest
{
    @Rule
    public JUnitSoftAssertions soft = new JUnitSoftAssertions();
    @Resource
    ExcelTemplateService excelTemplateService;
    @Resource
    HeaderPromptWorkbookDecorator headerPromptWorkbookDecorator;


    @Test
    public void testDecoratorDataPreparing() throws Exception
    {
        Workbook workbook = this.excelTemplateService.createWorkbook(loadResource("/excel/excelImpExMasterTemplate.xlsx"));
        try
        {
            ExcelExportResult result = prepareExcelExportResult(workbook);
            this.headerPromptWorkbookDecorator.decorate(result);
            List<Row> rows = getRows(workbook);
            AssertionsForClassTypes.assertThat(rows).isNotNull();
            String attributeWithReferenceFormat = "catalogVersion";
            String attributeLocalized = "description";
            Function<String, List<Row>> converter = attribute -> (List)rows.stream().filter(()).collect(Collectors.toList());
            AssertionsForClassTypes.assertThat(((Row)((List<Row>)converter.apply("catalogVersion")).get(0)).getCell(ExcelTemplateConstants.HeaderPrompt.HEADER_REFERENCE_FORMAT.getIndex())
                            .getStringCellValue()).isNotEmpty();
            AssertionsForClassTypes.assertThat(((Row)((List<Row>)converter
                                            .apply("description")).get(0)).getCell(ExcelTemplateConstants.HeaderPrompt.HEADER_ATTR_DISPLAYED_NAME.getIndex()).getStringCellValue())
                            .containsPattern(
                                            Pattern.compile(String.format("%s\\[.*\\]", new Object[] {"description"})));
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


    protected ExcelExportResult prepareExcelExportResult(Workbook workbook)
    {
        ProductModel productModel = prepareProduct("product", createCatalogVersionModel("catalog", "1.0"));
        VariantTypeModel variantTypeModel = (VariantTypeModel)saveItem((ItemModel)prepareVariant());
        ProductModel product = (ProductModel)saveItem((ItemModel)prepareProductWithVariant(productModel, variantTypeModel));
        ItemModel variant = saveItem((ItemModel)prepareVariantProductModel(product, variantTypeModel));
        workbook.createSheet(getModelService().getModelType(product));
        workbook.createSheet(getModelService().getModelType(variant));
        return new ExcelExportResult(workbook, Lists.newArrayList((Object[])new ItemModel[] {(ItemModel)product, variant}, ), null, null, null);
    }


    protected List<Row> getRows(Workbook workbook)
    {
        Sheet headerPrompt = workbook.getSheet(ExcelTemplateConstants.UtilitySheet.HEADER_PROMPT.getSheetName());
        int rowLastNum = headerPrompt.getLastRowNum();
        Objects.requireNonNull(headerPrompt);
        return (List<Row>)IntStream.range(1, rowLastNum).mapToObj(headerPrompt::getRow).collect(Collectors.toList());
    }


    private InputStream loadResource(String path)
    {
        return (InputStream)Optional.<InputStream>ofNullable(getClass().getResourceAsStream(path))
                        .orElseThrow(() -> new AssertionError("Could not load resource: " + path));
    }
}
