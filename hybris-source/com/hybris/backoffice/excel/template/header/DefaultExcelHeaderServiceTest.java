package com.hybris.backoffice.excel.template.header;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.data.SelectedAttributeQualifier;
import com.hybris.backoffice.excel.template.AttributeNameFormatter;
import com.hybris.backoffice.excel.template.CollectionFormatter;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.populator.ExcelAttributeContext;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.translators.ExcelTranslatorRegistry;
import com.hybris.backoffice.excel.translators.ExcelValueTranslator;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DefaultExcelHeaderServiceTest
{
    @Mock
    private ExcelCellService cellService;
    @Mock
    private ExcelSheetService sheetService;
    @Mock
    private AttributeNameFormatter<ExcelAttributeDescriptorAttribute> attributeNameFormatter;
    @Mock
    private CollectionFormatter collectionFormatter;
    @Mock
    private ExcelTranslatorRegistry registry;
    @Mock
    private TypeService typeService;
    @Spy
    @InjectMocks
    private DefaultExcelHeaderService headerService;


    @Test
    public void testGetHeaders()
    {
        testGetHeadersAndGetSelectedAttributesQualifiers((typeSheet, data) -> {
            Collection<SelectedAttribute> selectedAttributes = this.headerService.getHeaders(null, typeSheet);
            Assertions.assertThat(selectedAttributes.size()).isEqualTo(data.size());
            data.forEach(());
        });
    }


    @Test
    public void testGetSelectedAttributesQualifiers()
    {
        testGetHeadersAndGetSelectedAttributesQualifiers((typeSheet, data) -> {
            Collection<SelectedAttributeQualifier> selectedAttributeQualifiers = this.headerService.getSelectedAttributesQualifiers(null, typeSheet);
            Assertions.assertThat(selectedAttributeQualifiers.size()).isEqualTo(data.size());
            data.forEach(());
        });
    }


    protected void testGetHeadersAndGetSelectedAttributesQualifiers(BiConsumer<Sheet, Collection<Triple<String, Integer, String>>> whenThen)
    {
        Collection<Triple<String, Integer, String>> data = Lists.newArrayList((Object[])new Triple[] {(Triple)ImmutableTriple.of("catalogVersion", Integer.valueOf(0),
                        String.format("%s:%s", new Object[] {"catalog", "version"})), (Triple)ImmutableTriple.of("approvalStatus", Integer.valueOf(1), ""),
                        (Triple)ImmutableTriple.of("code", Integer.valueOf(2), ""),
                        (Triple)ImmutableTriple.of("order", Integer.valueOf(3), "")});
        Row headerRow = prepareHeaderRow((Collection<Pair<Integer, String>>)data
                        .stream().map(triple -> ImmutablePair.of(triple.getMiddle(), triple.getLeft())).collect(Collectors.toList()));
        data.stream()
                        .map(pair -> prepareTypeSystemRow((String)pair.getLeft(), (String)pair.getRight()))
                        .forEach(pair -> prepareReferenceFormat((Row)pair.getLeft(), (String)pair.getMiddle(), (String)pair.getRight()));
        Sheet typeSheet = (Sheet)Mockito.mock(Sheet.class);
        Row valueRow = (Row)Mockito.mock(Row.class);
        BDDMockito.given(typeSheet.getRow(ExcelTemplateConstants.Header.DISPLAY_NAME.getIndex())).willReturn(headerRow);
        BDDMockito.given(Integer.valueOf(typeSheet.getLastRowNum())).willReturn(Integer.valueOf(data.size()));
        BDDMockito.given(typeSheet.getRow(ExcelTemplateConstants.Header.DEFAULT_VALUE.getIndex())).willReturn(valueRow);
        whenThen.accept(typeSheet, data);
    }


    @Test
    public void testHeaderValueWithoutMetadata()
    {
        String qualifier = "xxx";
        String uniqueAttr = String.format("%s%s", new Object[] {"xxx", Character.valueOf(ExcelTemplateConstants.SpecialMark.UNIQUE.getMark())});
        String mandatoryAttr = String.format("%s%s", new Object[] {"xxx", Character.valueOf(ExcelTemplateConstants.SpecialMark.MANDATORY.getMark())});
        Objects.requireNonNull(this.headerService);
        Lists.newArrayList((Object[])new String[] {"xxx", uniqueAttr, mandatoryAttr}).stream().map(this.headerService::getHeaderValueWithoutSpecialMarks)
                        .forEach(attr -> Assertions.assertThat(attr).isEqualTo("xxx"));
    }


    @Test
    public void shouldNullBeReturnedWhenAttrIsNotLocalized()
    {
        BDDMockito.given(this.cellService.getCellValue((Cell)Matchers.any())).willReturn(String.valueOf(false));
        String isoCode = this.headerService.loadIsoCode((Row)Mockito.mock(Row.class), null);
        Assertions.assertThat(isoCode).isNull();
    }


    @Test
    public void shouldCorrectIsoCodeBeReturnedWhenAttrIsLocalized()
    {
        String isoCode = "en";
        String header = String.format("qualifier[%s]", new Object[] {"en"});
        BDDMockito.given(this.cellService.getCellValue((Cell)Matchers.any())).willReturn(String.valueOf(true));
        String returnedIsoCode = this.headerService.loadIsoCode((Row)Mockito.mock(Row.class), header);
        Assertions.assertThat(returnedIsoCode).isEqualTo("en");
    }


    @Test
    public void testInsertAttributesHeader()
    {
        Collection<ExcelAttribute> excelAttributes = mockExcelAttributes(5);
        this.headerService.insertAttributesHeader(null, excelAttributes);
        ((DefaultExcelHeaderService)Mockito.verify(this.headerService, Mockito.times(excelAttributes.size()))).insertAttributeHeader((Sheet)Matchers.any(), (ExcelAttribute)Matchers.any(), Matchers.anyInt());
    }


    @Test
    public void testInsertAttributeHeader()
    {
        String headerValue = "Article Number*^";
        String patternValue = "some formula";
        ExcelAttributeDescriptorAttribute excelAttribute = (ExcelAttributeDescriptorAttribute)Mockito.mock(ExcelAttributeDescriptorAttribute.class);
        Sheet sheet = (Sheet)Mockito.mock(Sheet.class);
        Row headerRow = (Row)Mockito.mock(Row.class);
        Row patternRow = (Row)Mockito.mock(Row.class);
        Cell headerCell = (Cell)Mockito.mock(Cell.class);
        Cell patternCell = (Cell)Mockito.mock(Cell.class);
        short firstCellNum = Integer.valueOf(1).shortValue();
        BDDMockito.given(this.attributeNameFormatter.format((ExcelAttributeContext)Matchers.any())).willReturn("Article Number*^");
        BDDMockito.given(sheet.getRow(ExcelTemplateConstants.Header.DISPLAY_NAME.getIndex())).willReturn(headerRow);
        BDDMockito.given(sheet.getRow(ExcelTemplateConstants.Header.REFERENCE_PATTERN.getIndex())).willReturn(patternRow);
        BDDMockito.given(Short.valueOf(headerRow.getFirstCellNum())).willReturn(Short.valueOf(firstCellNum));
        BDDMockito.given(headerRow.createCell(firstCellNum)).willReturn(headerCell);
        Mockito.lenient().when(Short.valueOf(patternRow.getFirstCellNum())).thenReturn(Short.valueOf(firstCellNum));
        BDDMockito.given(patternRow.getCell(firstCellNum)).willReturn(patternCell);
        BDDMockito.given(patternCell.getCellFormula()).willReturn("some formula");
        this.headerService.insertAttributeHeader(sheet, (ExcelAttribute)excelAttribute, 0);
        ((ExcelCellService)Mockito.verify(this.cellService)).insertAttributeValue((Cell)Matchers.eq(headerCell), Matchers.argThat((ArgumentMatcher)new Object(this)));
        ((Cell)Mockito.verify(patternCell)).setCellFormula((String)Matchers.argThat((ArgumentMatcher)new Object(this)));
    }


    @Test
    public void shouldGetHeadersNames()
    {
        Sheet sheet = (Sheet)Mockito.mock(Sheet.class);
        Row headerRow = prepareHeaderRow(new String[] {"Approval", "Catalog version*^", "Identifier[en]", ""});
        Mockito.when(sheet.getRow(0)).thenReturn(headerRow);
        Collection<String> attributeNames = this.headerService.getHeaderDisplayNames(sheet);
        Assertions.assertThat(attributeNames).containsExactly((Object[])new String[] {"Approval", "Catalog version*^", "Identifier[en]"});
    }


    @Test
    public void shouldMetadataBeRemovedFromHeaderValue()
    {
        String articleNumber = "Article Number";
        String headerValue = "Article Number" + ExcelTemplateConstants.SpecialMark.MANDATORY.getMark() + ExcelTemplateConstants.SpecialMark.UNIQUE.getMark();
        String output = this.headerService.getHeaderValueWithoutSpecialMarks(headerValue);
        Assertions.assertThat(output).isEqualTo("Article Number");
    }


    protected Row prepareHeaderRow(String... attributes)
    {
        Row row = (Row)Mockito.mock(Row.class);
        for(int index = 0; index < attributes.length; index++)
        {
            Cell cell = (Cell)Mockito.mock(Cell.class);
            BDDMockito.given(row.getCell(index)).willReturn(cell);
            BDDMockito.given(this.cellService.getCellValue(cell)).willReturn(attributes[index]);
        }
        BDDMockito.given(Short.valueOf(row.getLastCellNum())).willReturn(Short.valueOf((short)(attributes.length - 1)));
        return row;
    }


    protected Row prepareHeaderRow(Collection<Pair<Integer, String>> index_value)
    {
        Row row = (Row)Mockito.mock(Row.class);
        index_value.forEach(iv -> {
            Cell cell = (Cell)Mockito.mock(Cell.class);
            BDDMockito.given(row.getCell(((Integer)iv.getKey()).intValue())).willReturn(cell);
            BDDMockito.given(this.cellService.getCellValue(cell)).willReturn(iv.getValue());
        });
        short lastIndex = (new Integer(((Integer)((Pair)(new ArrayList<>((Collection)index_value)).get(index_value.size() - 1)).getKey()).intValue() + 1)).shortValue();
        BDDMockito.given(Short.valueOf(row.getLastCellNum())).willReturn(Short.valueOf(lastIndex));
        return row;
    }


    protected Triple<Row, String, String> prepareTypeSystemRow(String headerValue, String referenceFormat)
    {
        Row row = (Row)Mockito.mock(Row.class);
        Cell cell = (Cell)Mockito.mock(Cell.class);
        ((DefaultExcelHeaderService)Mockito.doReturn(Optional.of(row)).when(this.headerService)).findTypeSystemRowForGivenHeader((Sheet)Matchers.any(), (String)Matchers.any(), (String)Matchers.eq(headerValue));
        BDDMockito.given(row.getCell(ExcelTemplateConstants.TypeSystem.ATTR_QUALIFIER.getIndex())).willReturn(cell);
        BDDMockito.given(this.cellService.getCellValue(cell)).willReturn(headerValue);
        return (Triple<Row, String, String>)ImmutableTriple.of(row, headerValue, referenceFormat);
    }


    protected void prepareReferenceFormat(Row row, String qualifier, String referenceFormat)
    {
        AttributeDescriptorModel attributeDescriptor = (AttributeDescriptorModel)Mockito.mock(AttributeDescriptorModel.class);
        BDDMockito.given(attributeDescriptor.getQualifier()).willReturn(qualifier);
        ((DefaultExcelHeaderService)Mockito.doReturn(attributeDescriptor).when(this.headerService)).loadAttributeDescriptor((Row)Matchers.eq(row), (String)Matchers.any());
        ExcelValueTranslator translator = (ExcelValueTranslator)Mockito.mock(ExcelValueTranslator.class);
        BDDMockito.given(translator.referenceFormat(attributeDescriptor)).willReturn(referenceFormat);
        BDDMockito.given(this.registry.getTranslator(attributeDescriptor)).willReturn(Optional.of(translator));
    }


    protected Collection<ExcelAttribute> mockExcelAttributes(int size)
    {
        return (Collection<ExcelAttribute>)IntStream.range(0, size).mapToObj(idx -> {
            ExcelAttribute excelAttribute = (ExcelAttribute)Mockito.mock(ExcelAttribute.class);
            ((DefaultExcelHeaderService)Mockito.doNothing().when(this.headerService)).insertAttributeHeader(null, excelAttribute, idx);
            return excelAttribute;
        }).collect(Collectors.toList());
    }
}
