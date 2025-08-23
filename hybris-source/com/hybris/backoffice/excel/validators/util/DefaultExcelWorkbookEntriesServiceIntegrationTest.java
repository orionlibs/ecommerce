package com.hybris.backoffice.excel.validators.util;

import com.hybris.backoffice.excel.data.ExcelColumn;
import com.hybris.backoffice.excel.data.ExcelWorksheet;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.importing.parser.ImportParameterParser;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.MapAssert;
import org.junit.Test;

@IntegrationTest
public class DefaultExcelWorkbookEntriesServiceIntegrationTest extends ServicelayerTest
{
    @Resource
    private TypeService typeService;
    @Resource(name = "defaultImportParameterParser")
    private ImportParameterParser importParameterParser;
    @Resource
    private ExcelWorkbookEntriesService excelWorkbookEntriesService;


    @Test
    public void shouldGenerateEntryKeysForCatalog()
    {
        ExcelWorksheet worksheet = new ExcelWorksheet("Catalog");
        worksheet.add(0, prepareExcelColumn("Catalog", "id", 0),
                        prepareImportParameters("", "Clothing"));
        worksheet.add(0, prepareExcelColumn("Catalog", "name", 1),
                        prepareImportParameters("", "ClothingName"));
        worksheet.add(1, prepareExcelColumn("Catalog", "id", 0),
                        prepareImportParameters("", "Default"));
        worksheet.add(1, prepareExcelColumn("Catalog", "name", 1),
                        prepareImportParameters("", "DefaultName"));
        List<WorksheetEntryKey> entriesKeys = (List<WorksheetEntryKey>)this.excelWorkbookEntriesService.generateEntryKeys(worksheet);
        Assertions.assertThat(entriesKeys).hasSize(2);
        ((MapAssert)Assertions.assertThat(((WorksheetEntryKey)entriesKeys.get(0)).getUniqueAttributesValues()).containsKey("Catalog.id")).containsValue("Clothing");
        ((MapAssert)Assertions.assertThat(((WorksheetEntryKey)entriesKeys.get(1)).getUniqueAttributesValues()).containsKey("Catalog.id")).containsValue("Default");
    }


    @Test
    public void shouldGenerateEntryKeysForCatalogVersion()
    {
        ExcelWorksheet worksheet = new ExcelWorksheet("CatalogVersion");
        worksheet.add(0, prepareExcelColumn("CatalogVersion", "version", 0),
                        prepareImportParameters("", "Staged"));
        worksheet.add(0, prepareExcelColumn("CatalogVersion", "catalog", 1),
                        prepareImportParameters("Catalog.id", "Clothing"));
        worksheet.add(1, prepareExcelColumn("CatalogVersion", "version", 0),
                        prepareImportParameters("", "Online"));
        worksheet.add(1, prepareExcelColumn("CatalogVersion", "catalog", 1),
                        prepareImportParameters("Catalog.id", "Default"));
        List<WorksheetEntryKey> entriesKeys = (List<WorksheetEntryKey>)this.excelWorkbookEntriesService.generateEntryKeys(worksheet);
        Assertions.assertThat(entriesKeys).hasSize(2);
        Assertions.assertThat(((WorksheetEntryKey)entriesKeys.get(0)).getUniqueAttributesValues()).containsKeys((Object[])new String[] {"Catalog.id", "CatalogVersion.version"}).containsValues((Object[])new String[] {"Clothing", "Staged"});
        Assertions.assertThat(((WorksheetEntryKey)entriesKeys.get(1)).getUniqueAttributesValues()).containsKeys((Object[])new String[] {"Catalog.id", "CatalogVersion.version"}).containsValues((Object[])new String[] {"Default", "Online"});
    }


    @Test
    public void shouldGenerateEntryKeysForCategory()
    {
        ExcelWorksheet worksheet = new ExcelWorksheet("CatalogVersion");
        worksheet.add(0, prepareExcelColumn("Category", "code", 0),
                        prepareImportParameters("", "Cat1"));
        worksheet.add(0, prepareExcelColumn("Category", "catalogVersion", 1),
                        prepareImportParameters("CatalogVersion.version:Catalog.id", "Staged:Default"));
        worksheet.add(0, prepareExcelColumn("Category", "name", 2),
                        prepareImportParameters("", "Category1Name"));
        worksheet.add(1, prepareExcelColumn("Category", "code", 0),
                        prepareImportParameters("", "Cat2"));
        worksheet.add(1, prepareExcelColumn("Category", "catalogVersion", 1),
                        prepareImportParameters("CatalogVersion.version:Catalog.id", "Online:Default"));
        worksheet.add(1, prepareExcelColumn("Category", "name", 2),
                        prepareImportParameters("", "Category2Name"));
        List<WorksheetEntryKey> entriesKeys = (List<WorksheetEntryKey>)this.excelWorkbookEntriesService.generateEntryKeys(worksheet);
        Assertions.assertThat(entriesKeys).hasSize(2);
        Assertions.assertThat(((WorksheetEntryKey)entriesKeys.get(0)).getUniqueAttributesValues())
                        .containsKeys((Object[])new String[] {"Catalog.id", "CatalogVersion.version", "Category.code"}).doesNotContainKeys((Object[])new String[] {"Category.name"}).containsValues((Object[])new String[] {"Default", "Staged", "Cat1"}).doesNotContainValue("Category1Name");
        Assertions.assertThat(((WorksheetEntryKey)entriesKeys.get(1)).getUniqueAttributesValues())
                        .containsKeys((Object[])new String[] {"Catalog.id", "CatalogVersion.version", "Category.code"}).doesNotContainKeys((Object[])new String[] {"Category.name"}).containsValues((Object[])new String[] {"Default", "Online", "Cat2"}).doesNotContainValue("Category2Name");
    }


    private ExcelColumn prepareExcelColumn(String type, String attribute, int columnIndex)
    {
        SelectedAttribute selectedAttribute = new SelectedAttribute();
        selectedAttribute.setAttributeDescriptor(this.typeService.getAttributeDescriptor(type, attribute));
        return new ExcelColumn(selectedAttribute, Integer.valueOf(columnIndex));
    }


    private ImportParameters prepareImportParameters(String referenceFormat, String cellValue)
    {
        List<Map<String, String>> parameters = this.importParameterParser.parseValue(referenceFormat, "", cellValue).getParameters();
        return new ImportParameters("", "", cellValue, "", parameters);
    }
}
