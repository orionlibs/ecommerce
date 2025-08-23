package com.hybris.backoffice.excel.importing;

import com.google.common.collect.Sets;
import com.hybris.backoffice.excel.template.CollectionFormatter;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.populator.typesheet.TypeSystemRow;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.impl.DefaultCommonI18NService;
import java.util.Arrays;
import java.util.List;
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
public class DefaultExcelTypeSystemServiceTest
{
    @Mock
    private Workbook workbook;
    @Mock
    private Sheet typeSystemSheet;
    @Mock
    private ExcelCellService cellService;
    @Mock
    private DefaultCommonI18NService commonI18NService;
    @Mock
    private CollectionFormatter collectionFormatter;
    @InjectMocks
    private ExcelAttributeTypeSystemService service;


    @Before
    public void setUp()
    {
        BDDMockito.given(this.workbook.getSheet(ExcelTemplateConstants.UtilitySheet.TYPE_SYSTEM.getSheetName())).willReturn(this.typeSystemSheet);
        BDDMockito.given(Integer.valueOf(this.typeSystemSheet.getLastRowNum())).willReturn(Integer.valueOf(1));
    }


    @Test
    public void shouldCreateTypeSystemRow()
    {
        mockTypeSystemRow("code", "Article Number", "FALSE", "", "{Article Number*^}");
        BDDMockito.given(this.collectionFormatter.formatToCollection("{Article Number*^}")).willReturn(Sets.newHashSet((Object[])new String[] {"Article Number*^"}));
        ExcelAttributeTypeSystemService.ExcelTypeSystem typeSystem = this.service.loadTypeSystem(this.workbook);
        assertTypeSystemRow(typeSystem, "Article Number*^", "code", "Article Number", "", false);
    }


    @Test
    public void shouldCreateTypeSystemRowForEachLanguageOfLocalizedAttribute()
    {
        mockTypeSystemRow("name", "Identifier", "TRUE", "{en},{de}", "{Identifier[en]},{Identifier[de]}");
        BDDMockito.given(this.collectionFormatter.formatToCollection("{Identifier[en]},{Identifier[de]}"))
                        .willReturn(Sets.newHashSet((Object[])new String[] {"Identifier[en]", "Identifier[de]"}));
        mockLanguage();
        ExcelAttributeTypeSystemService.ExcelTypeSystem typeSystem = this.service.loadTypeSystem(this.workbook);
        assertTypeSystemRow(typeSystem, "Identifier[en]", "name", "Identifier", "en", true);
        assertTypeSystemRow(typeSystem, "Identifier[de]", "name", "Identifier", "de", true);
    }


    @Test
    public void shouldCreateTypeSystemRowForNoneSupportLanguageOfLocalizedAttribute()
    {
        mockTypeSystemRow("name", "Identifier", "TRUE", "", "{Identifier[test]}");
        BDDMockito.given(this.collectionFormatter.formatToCollection("{Identifier[test]}")).willReturn(Sets.newHashSet((Object[])new String[] {"Identifier[test]"}));
        mockLanguage();
        ExcelAttributeTypeSystemService.ExcelTypeSystem typeSystem = this.service.loadTypeSystem(this.workbook);
        assertTypeSystemRow(typeSystem, "Identifier[test]", "name", "Identifier", "", true);
    }


    private Row mockTypeSystemRow(String attrQualifier, String attrName, String attrLocalized, String attrLocLang, String attrDisplayedName)
    {
        Row row = (Row)Mockito.mock(Row.class);
        BDDMockito.given(this.typeSystemSheet.getRow(1)).willReturn(row);
        mockCell(row, 0, "{Product}");
        mockCell(row, 1, "Product");
        mockCell(row, 2, attrQualifier);
        mockCell(row, 3, attrName);
        mockCell(row, 4, "FALSE");
        mockCell(row, 5, "java.lang.String");
        mockCell(row, 6, "Product");
        mockCell(row, 7, attrLocalized);
        mockCell(row, 8, attrLocLang);
        mockCell(row, 9, attrDisplayedName);
        mockCell(row, 10, "TRUE");
        mockCell(row, 11, "");
        return row;
    }


    private void mockCell(Row row, int index, String cellValue)
    {
        Cell cell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(row.getCell(index)).willReturn(cell);
        BDDMockito.given(this.cellService.getCellValue(cell)).willReturn(cellValue);
    }


    private void mockLanguage()
    {
        LanguageModel en = new LanguageModel();
        en.setIsocode("en");
        LanguageModel de = new LanguageModel();
        de.setIsocode("de");
        List<LanguageModel> allLanguages = Arrays.asList(new LanguageModel[] {en, de});
        BDDMockito.given(this.commonI18NService.getAllLanguages()).willReturn(allLanguages);
    }


    protected void assertTypeSystemRow(ExcelAttributeTypeSystemService.ExcelTypeSystem typeSystem, String attrDisplayName, String attrQualifier, String attrName, String attrLocLang, boolean attrLocalized)
    {
        Optional<TypeSystemRow> optionalRow = typeSystem.findRow(attrDisplayName);
        Assertions.assertThat(optionalRow).isPresent();
        TypeSystemRow row = optionalRow.get();
        Assertions.assertThat(row.getTypeCode()).isEqualTo("{Product}");
        Assertions.assertThat(row.getTypeName()).isEqualTo("Product");
        Assertions.assertThat(row.getAttrQualifier()).isEqualTo(attrQualifier);
        Assertions.assertThat(row.getAttrName()).isEqualTo(attrName);
        Assertions.assertThat(row.getAttrOptional()).isFalse();
        Assertions.assertThat(row.getAttrTypeCode()).isEqualTo("java.lang.String");
        Assertions.assertThat(row.getAttrTypeItemType()).isEqualTo("Product");
        Assertions.assertThat(row.getAttrLocalized()).isEqualTo(attrLocalized);
        Assertions.assertThat(row.getAttrLocLang()).isEqualTo(attrLocLang);
        Assertions.assertThat(row.getAttrDisplayName()).isEqualTo(attrDisplayName);
        Assertions.assertThat(row.getAttrUnique()).isTrue();
        Assertions.assertThat(row.getAttrReferenceFormat()).isEqualTo("");
    }
}
