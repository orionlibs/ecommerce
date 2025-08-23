package com.hybris.backoffice.excel.importing;

import com.google.common.collect.Sets;
import com.hybris.backoffice.excel.importing.data.ClassificationTypeSystemRow;
import com.hybris.backoffice.excel.template.CollectionFormatter;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelClassificationTypeSystemServiceTest
{
    @Mock
    private Workbook workbook;
    @Mock
    private Sheet classificationTypeSystemSheet;
    @Mock
    private ExcelCellService cellService;
    @Mock
    private CollectionFormatter collectionFormatter;
    @InjectMocks
    private ExcelClassificationTypeSystemService service;


    @Before
    public void setUp()
    {
        BDDMockito.given(this.workbook.getSheet(ExcelTemplateConstants.UtilitySheet.CLASSIFICATION_TYPE_SYSTEM.getSheetName())).willReturn(this.classificationTypeSystemSheet);
        BDDMockito.given(Integer.valueOf(this.classificationTypeSystemSheet.getLastRowNum())).willReturn(Integer.valueOf(1));
    }


    @Test
    public void shouldCreateClassificationTypeSystemRow()
    {
        String dimensions = "Electronics.Dimensions - SampleClassification/1.0";
        mockClassificationTypeSystemRow("Electronics.Dimensions - SampleClassification/1.0", "dimensions", "false", "");
        ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem excelClassificationTypeSystem = this.service.loadTypeSystem(this.workbook);
        assertClassificationTypeSystemRow(excelClassificationTypeSystem, "Electronics.Dimensions - SampleClassification/1.0", "dimensions", false, "");
    }


    @Test
    public void shouldCreateClassificationTypeSystemRowForEachLanguageOfLocalizedAttribute()
    {
        String weightEn = "Electronics.Weight[en] - SampleClassification/1.0";
        String weightDe = "Electronics.Weight[de] - SampleClassification/1.0";
        String fullName = "{Electronics.Weight[en] - SampleClassification/1.0},{Electronics.Weight[de] - SampleClassification/1.0}";
        BDDMockito.given(this.collectionFormatter.formatToCollection("{Electronics.Weight[en] - SampleClassification/1.0},{Electronics.Weight[de] - SampleClassification/1.0}"))
                        .willReturn(Sets.newHashSet((Object[])new String[] {"Electronics.Weight[en] - SampleClassification/1.0", "Electronics.Weight[de] - SampleClassification/1.0"}));
        mockClassificationTypeSystemRow("{Electronics.Weight[en] - SampleClassification/1.0},{Electronics.Weight[de] - SampleClassification/1.0}", "weight", "true", "{en},{de}");
        ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem classificationTypeSystem = this.service.loadTypeSystem(this.workbook);
        assertClassificationTypeSystemRow(classificationTypeSystem, "Electronics.Weight[en] - SampleClassification/1.0", "weight", true, "en");
        assertClassificationTypeSystemRow(classificationTypeSystem, "Electronics.Weight[de] - SampleClassification/1.0", "weight", true, "de");
    }


    protected void assertClassificationTypeSystemRow(ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem classificationTypeSystem, String fullName, String attribute, boolean isLocalized, String isoCode)
    {
        Optional<ClassificationTypeSystemRow> optionalRow = classificationTypeSystem.findRow(fullName);
        Assertions.assertThat(optionalRow).isPresent();
        ClassificationTypeSystemRow row = optionalRow.get();
        Assertions.assertThat(row.getFullName()).isEqualTo(fullName);
        Assertions.assertThat(row.getClassificationSystem()).isEqualTo("SampleClassification");
        Assertions.assertThat(row.getClassificationVersion()).isEqualTo("1.0");
        Assertions.assertThat(row.getClassificationClass()).isEqualTo("electronics");
        Assertions.assertThat(row.getClassificationAttribute()).isEqualTo(attribute);
        Assertions.assertThat(row.isLocalized()).isEqualTo(isLocalized);
        Assertions.assertThat(row.getIsoCode()).isEqualTo(isoCode);
        Assertions.assertThat(row.isMandatory()).isFalse();
    }


    protected void mockClassificationTypeSystemRow(String fullName, String classificationAttribute, String attrLocalized, String attrLocLang)
    {
        Row row = (Row)Mockito.mock(Row.class);
        BDDMockito.given(this.classificationTypeSystemSheet.getRow(1)).willReturn(row);
        mockCell(row, 0, fullName);
        mockCell(row, 1, "SampleClassification");
        mockCell(row, 2, "1.0");
        mockCell(row, 3, "electronics");
        mockCell(row, 4, classificationAttribute);
        mockCell(row, 5, attrLocalized);
        mockCell(row, 6, attrLocLang);
        mockCell(row, 7, "false");
    }


    private void mockCell(Row row, int index, String cellValue)
    {
        Cell cell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(row.getCell(index)).willReturn(cell);
        BDDMockito.given(this.cellService.getCellValue(cell)).willReturn(cellValue);
    }
}
