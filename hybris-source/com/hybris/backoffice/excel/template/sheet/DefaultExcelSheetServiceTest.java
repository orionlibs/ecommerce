package com.hybris.backoffice.excel.template.sheet;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.workbook.ExcelWorkbookService;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Locale;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelSheetServiceTest
{
    @Mock
    ExcelCellService excelCellService;
    @Mock
    ExcelWorkbookService excelWorkbookService;
    @InjectMocks
    DefaultExcelSheetService excelSheetService;


    @Test
    public void shouldFindDisplayNameInTypeSystemSheet()
    {
        String typeCode = "Media";
        String qualifier = "CatalogVersion";
        String name = "Catalog Version";
        String displayName = "Catalog version*";
        Sheet typeSystemSheet = (Sheet)Mockito.mock(Sheet.class);
        ExcelAttributeDescriptorAttribute attribute = mockAttribute("CatalogVersion", "Catalog Version", Locale.ENGLISH);
        prepareTypeSystemSheet(typeSystemSheet, Locale.ENGLISH);
        Row catalogVersionRow = mockRow(typeSystemSheet, String.format("{%s}", new Object[] {"Media"}), "CatalogVersion", "Catalog Version", "Catalog version*", "");
        BDDMockito.given(Integer.valueOf(typeSystemSheet.getLastRowNum())).willReturn(Integer.valueOf(0));
        BDDMockito.given(typeSystemSheet.getRow(0)).willReturn(catalogVersionRow);
        String result = this.excelSheetService.findAttributeDisplayNameInTypeSystemSheet(typeSystemSheet, (ExcelAttribute)attribute, "Media");
        Assertions.assertThat(result).isEqualTo("Catalog version*");
    }


    @Test
    public void shouldFindDisplayNameInTypeSystemSheetForAttributeWithEmptyName()
    {
        String typeCode = "Media";
        String qualifier = "CatalogVersion";
        String name = "";
        String displayName = "Catalog version*";
        Sheet typeSystemSheet = (Sheet)Mockito.mock(Sheet.class);
        ExcelAttributeDescriptorAttribute attribute = mockAttribute("CatalogVersion", "", Locale.ENGLISH);
        prepareTypeSystemSheet(typeSystemSheet, Locale.ENGLISH);
        Row catalogVersionRow = mockRow(typeSystemSheet, String.format("{%s}", new Object[] {"Media"}), "CatalogVersion", "", "Catalog version*", "");
        BDDMockito.given(Integer.valueOf(typeSystemSheet.getLastRowNum())).willReturn(Integer.valueOf(0));
        BDDMockito.given(typeSystemSheet.getRow(0)).willReturn(catalogVersionRow);
        String result = this.excelSheetService.findAttributeDisplayNameInTypeSystemSheet(typeSystemSheet, (ExcelAttribute)attribute, "Media");
        Assertions.assertThat(result).isEqualTo("Catalog version*");
    }


    @Test
    public void shouldFindDisplayNameInTypeSystemSheetWithSameQualifierButDifferentTypeCode()
    {
        String typeCode = "Media";
        String qualifier = "CatalogVersion";
        String name = "Catalog Version";
        String uniqueDisplayName = "Catalog version^";
        String mandatoryDisplayName = "Catalog version*";
        Sheet typeSystemSheet = (Sheet)Mockito.mock(Sheet.class);
        ExcelAttributeDescriptorAttribute attribute = mockAttribute("CatalogVersion", "Catalog Version", Locale.ENGLISH);
        prepareTypeSystemSheet(typeSystemSheet, Locale.ENGLISH);
        Row uniqueCatalogVersionRow = mockRow(typeSystemSheet, String.format("{%s}", new Object[] {"Media"}), "CatalogVersion", "Catalog Version", "Catalog version^", "");
        Row mandatoryCatalogVersionRow = mockRow(typeSystemSheet, "{CatalogUnawareMedia}", "CatalogVersion", "Catalog Version", "Catalog version*", "");
        BDDMockito.given(Integer.valueOf(typeSystemSheet.getLastRowNum())).willReturn(Integer.valueOf(1));
        Mockito.lenient().when(typeSystemSheet.getRow(0)).thenReturn(uniqueCatalogVersionRow);
        Mockito.lenient().when(typeSystemSheet.getRow(1)).thenReturn(mandatoryCatalogVersionRow);
        String result = this.excelSheetService.findAttributeDisplayNameInTypeSystemSheet(typeSystemSheet, (ExcelAttribute)attribute, "Media");
        Assertions.assertThat(result).isEqualTo("Catalog version^");
    }


    private void prepareTypeSystemSheet(Sheet typeSystemSheet, Locale workbookLocale)
    {
        Workbook workbook = (Workbook)Mockito.mock(Workbook.class);
        BDDMockito.given(this.excelWorkbookService.getProperty(workbook, "isoCode"))
                        .willReturn(Optional.of(workbookLocale.toLanguageTag()));
        BDDMockito.given(typeSystemSheet.getWorkbook()).willReturn(workbook);
    }


    private ExcelAttributeDescriptorAttribute mockAttribute(String qualifier, String name, Locale workbookLocale)
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptor.getQualifier()).willReturn(qualifier);
        BDDMockito.given(attributeDescriptor.getName(workbookLocale)).willReturn(name);
        ExcelAttributeDescriptorAttribute attribute = (ExcelAttributeDescriptorAttribute)Mockito.mock(ExcelAttributeDescriptorAttribute.class);
        BDDMockito.given(attribute.getAttributeDescriptorModel()).willReturn(attributeDescriptor);
        return attribute;
    }


    private Row mockRow(Sheet typeSystemSheet, String typeCode, String qualifier, String name, String displayName, String lang)
    {
        Row row = (Row)Mockito.mock(Row.class);
        Cell typeCodeCell = mockCell(typeCode);
        Cell qualifierCell = mockCell(qualifier);
        Cell displayNameCell = mockCell(displayName);
        Cell nameCell = mockCell(name);
        Cell langCell = mockCell(lang);
        BDDMockito.given(row.getCell(ExcelTemplateConstants.TypeSystem.TYPE_CODE.getIndex())).willReturn(typeCodeCell);
        BDDMockito.given(row.getCell(ExcelTemplateConstants.TypeSystem.ATTR_QUALIFIER.getIndex())).willReturn(qualifierCell);
        BDDMockito.given(row.getCell(ExcelTemplateConstants.TypeSystem.ATTR_NAME.getIndex())).willReturn(nameCell);
        BDDMockito.given(row.getCell(ExcelTemplateConstants.TypeSystem.ATTR_LOC_LANG.getIndex())).willReturn(langCell);
        BDDMockito.given(row.getCell(ExcelTemplateConstants.TypeSystem.ATTR_DISPLAYED_NAME.getIndex())).willReturn(displayNameCell);
        BDDMockito.given(row.getSheet()).willReturn(typeSystemSheet);
        return row;
    }


    private Cell mockCell(String cellValue)
    {
        Cell cell = (Cell)Mockito.mock(Cell.class);
        BDDMockito.given(this.excelCellService.getCellValue(cell)).willReturn(cellValue);
        return cell;
    }
}
