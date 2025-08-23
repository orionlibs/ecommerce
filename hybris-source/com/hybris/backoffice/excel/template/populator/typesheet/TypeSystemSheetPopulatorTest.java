package com.hybris.backoffice.excel.template.populator.typesheet;

import com.hybris.backoffice.excel.data.ExcelExportResult;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.cell.DefaultExcelCellService;
import com.hybris.backoffice.excel.template.mapper.ExcelMapper;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TypeSystemSheetPopulatorTest
{
    @Spy
    DefaultExcelCellService excelCellService;
    @Mock
    ExcelMapper<ExcelExportResult, AttributeDescriptorModel> mapper;
    @Mock
    TypeSystemRowFactory mockedTypeSystemRowFactory;
    @InjectMocks
    TypeSystemSheetPopulator populator;


    @Test
    public void shouldPopulateRowsWithAttributeDescriptorsData()
    {
        Sheet typeSystemSheet = (Sheet)Mockito.mock(Sheet.class);
        Map<Integer, Cell> row = createRowMock(typeSystemSheet, 1);
        Workbook workbook = (Workbook)Mockito.mock(Workbook.class);
        BDDMockito.given(workbook.getSheet("TypeSystem")).willReturn(typeSystemSheet);
        AttributeDescriptorModel attributeDescriptorModel = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptorModel.getQualifier()).willReturn("product");
        TypeSystemRow typeSystemRow = createTypeSystemRow();
        BDDMockito.given(this.mockedTypeSystemRowFactory.create(attributeDescriptorModel)).willReturn(typeSystemRow);
        this.populator.populate(workbook.getSheet("TypeSystem"), Collections.singletonList(attributeDescriptorModel));
        ((DefaultExcelCellService)Mockito.verify(this.excelCellService)).insertAttributeValue(row.get(Integer.valueOf(ExcelTemplateConstants.TypeSystem.TYPE_CODE.getIndex())), "typeCode");
        ((DefaultExcelCellService)Mockito.verify(this.excelCellService)).insertAttributeValue(row.get(Integer.valueOf(ExcelTemplateConstants.TypeSystem.TYPE_NAME.getIndex())), "typeName");
        ((DefaultExcelCellService)Mockito.verify(this.excelCellService)).insertAttributeValue(row.get(Integer.valueOf(ExcelTemplateConstants.TypeSystem.ATTR_QUALIFIER.getIndex())), "attrQualifier");
        ((DefaultExcelCellService)Mockito.verify(this.excelCellService)).insertAttributeValue(row.get(Integer.valueOf(ExcelTemplateConstants.TypeSystem.ATTR_NAME.getIndex())), "attrName");
        ((DefaultExcelCellService)Mockito.verify(this.excelCellService)).insertAttributeValue(row.get(Integer.valueOf(ExcelTemplateConstants.TypeSystem.ATTR_TYPE_CODE.getIndex())), "attrTypeCode");
        ((DefaultExcelCellService)Mockito.verify(this.excelCellService)).insertAttributeValue(row.get(Integer.valueOf(ExcelTemplateConstants.TypeSystem.ATTR_TYPE_ITEMTYPE.getIndex())), "attrTypeItemType");
        ((DefaultExcelCellService)Mockito.verify(this.excelCellService)).insertAttributeValue(row.get(Integer.valueOf(ExcelTemplateConstants.TypeSystem.ATTR_LOC_LANG.getIndex())), "attrLocLang");
        ((DefaultExcelCellService)Mockito.verify(this.excelCellService)).insertAttributeValue(row.get(Integer.valueOf(ExcelTemplateConstants.TypeSystem.ATTR_DISPLAYED_NAME.getIndex())), "attrDisplayName");
        ((DefaultExcelCellService)Mockito.verify(this.excelCellService)).insertAttributeValue(row.get(Integer.valueOf(ExcelTemplateConstants.TypeSystem.REFERENCE_FORMAT.getIndex())), "attrReferenceFormat");
        ((DefaultExcelCellService)Mockito.verify(this.excelCellService)).insertAttributeValue(row.get(Integer.valueOf(ExcelTemplateConstants.TypeSystem.ATTR_OPTIONAL.getIndex())), Boolean.valueOf(true));
        ((DefaultExcelCellService)Mockito.verify(this.excelCellService)).insertAttributeValue(row.get(Integer.valueOf(ExcelTemplateConstants.TypeSystem.ATTR_LOCALIZED.getIndex())), Boolean.valueOf(true));
        ((DefaultExcelCellService)Mockito.verify(this.excelCellService)).insertAttributeValue(row.get(Integer.valueOf(ExcelTemplateConstants.TypeSystem.ATTR_UNIQUE.getIndex())), Boolean.valueOf(true));
    }


    @Test
    public void shouldMergeAttributesByQualifierAndNameAndDisplayName()
    {
        String code = "code";
        String articleNumber = "Article Number";
        String shoeNumber = "Shoe Number";
        String catalogVersionQualifier = "catalogVersion";
        String catalogVersionName = "Catalog Version";
        String uniqueCatalogVersionDisplayName = "Catalog version*^";
        String readOnlyCatalogVersionDisplayName = "Catalog version*=";
        TypeSystemRow productCodeTypeSystemRow = (TypeSystemRow)Mockito.mock(TypeSystemRow.class);
        TypeSystemRow shoeCodeTypeSystemRow = (TypeSystemRow)Mockito.mock(TypeSystemRow.class);
        TypeSystemRow uniqueCatalogVersionTypeSystemRow = (TypeSystemRow)Mockito.mock(TypeSystemRow.class);
        TypeSystemRow readOnlyCatalogVersionTypeSystemRow = (TypeSystemRow)Mockito.mock(TypeSystemRow.class);
        AttributeDescriptorModel productCodeAttr = mockAttributeDescriptor("code", "Article Number", "Catalog version*^", productCodeTypeSystemRow);
        AttributeDescriptorModel shoeCodeAttr = mockAttributeDescriptor("code", "Shoe Number", "Catalog version*^", shoeCodeTypeSystemRow);
        AttributeDescriptorModel productUniqueCatalogVersionAttr = mockAttributeDescriptor("catalogVersion", "Catalog Version", "Catalog version*^", uniqueCatalogVersionTypeSystemRow);
        AttributeDescriptorModel productReadOnlyCatalogVersionAttr = mockAttributeDescriptor("catalogVersion", "Catalog Version", "Catalog version*^", uniqueCatalogVersionTypeSystemRow);
        AttributeDescriptorModel shoeCatalogVersionAttr = mockAttributeDescriptor("catalogVersion", "Catalog Version", "Catalog version*=", readOnlyCatalogVersionTypeSystemRow);
        List<AttributeDescriptorModel> attributes = List.of(productCodeAttr, shoeCodeAttr, productUniqueCatalogVersionAttr, productReadOnlyCatalogVersionAttr, shoeCatalogVersionAttr);
        BDDMockito.given(this.mockedTypeSystemRowFactory.merge((TypeSystemRow)Matchers.any(), (TypeSystemRow)Matchers.any()))
                        .willAnswer(invocationOnMock -> invocationOnMock.getArgument(0, TypeSystemRow.class));
        Map<String, TypeSystemRow> result = this.populator.mergeAttributesByQualifier(attributes);
        Assertions.assertThat(result).hasSize(4);
        Assertions.assertThat(result.get("code:Article Number:Catalog version*^"))
                        .isEqualTo(productCodeTypeSystemRow);
        Assertions.assertThat(result.get("code:Shoe Number:Catalog version*^")).isEqualTo(shoeCodeTypeSystemRow);
        Assertions.assertThat(result.get("catalogVersion:Catalog Version:Catalog version*^"))
                        .isEqualTo(uniqueCatalogVersionTypeSystemRow);
        Assertions.assertThat(result.get("catalogVersion:Catalog Version:Catalog version*="))
                        .isEqualTo(readOnlyCatalogVersionTypeSystemRow);
    }


    private AttributeDescriptorModel mockAttributeDescriptor(String qualifier, String name, String displayName, TypeSystemRow typeSystemRow)
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptor.getQualifier()).willReturn(qualifier);
        BDDMockito.given(attributeDescriptor.getName()).willReturn(name);
        BDDMockito.given(this.mockedTypeSystemRowFactory.getAttributeDisplayName(attributeDescriptor)).willReturn(displayName);
        BDDMockito.given(this.mockedTypeSystemRowFactory.create(attributeDescriptor)).willReturn(typeSystemRow);
        return attributeDescriptor;
    }


    private Map<Integer, Cell> createRowMock(Sheet sheet, int rowIndex)
    {
        Row row = (Row)Mockito.mock(Row.class);
        Function<Integer, Cell> cellProducer = index -> {
            Cell cell = (Cell)Mockito.mock(Cell.class);
            BDDMockito.given(row.createCell(index.intValue())).willReturn(cell);
            return cell;
        };
        BDDMockito.given(sheet.createRow(rowIndex)).willReturn(row);
        return IntStream.rangeClosed(ExcelTemplateConstants.TypeSystem.TYPE_CODE.getIndex(), ExcelTemplateConstants.TypeSystem.REFERENCE_FORMAT.getIndex())
                        .boxed()
                        .collect(Collectors.toMap((Function)Function.identity(), cellProducer));
    }


    private static TypeSystemRow createTypeSystemRow()
    {
        TypeSystemRow typeSystemRow = new TypeSystemRow();
        typeSystemRow.setTypeCode("typeCode");
        typeSystemRow.setTypeName("typeName");
        typeSystemRow.setAttrQualifier("attrQualifier");
        typeSystemRow.setAttrName("attrName");
        typeSystemRow.setAttrOptional(Boolean.valueOf(true));
        typeSystemRow.setAttrTypeCode("attrTypeCode");
        typeSystemRow.setAttrTypeItemType("attrTypeItemType");
        typeSystemRow.setAttrLocalized(Boolean.valueOf(true));
        typeSystemRow.setAttrLocLang("attrLocLang");
        typeSystemRow.setAttrDisplayName("attrDisplayName");
        typeSystemRow.setAttrUnique(Boolean.valueOf(true));
        typeSystemRow.setAttrReferenceFormat("attrReferenceFormat");
        return typeSystemRow;
    }
}
