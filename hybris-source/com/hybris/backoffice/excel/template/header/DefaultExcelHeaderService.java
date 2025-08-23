package com.hybris.backoffice.excel.template.header;

import com.google.common.collect.ImmutableList;
import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.data.SelectedAttributeQualifier;
import com.hybris.backoffice.excel.template.AttributeNameFormatter;
import com.hybris.backoffice.excel.template.CollectionFormatter;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.populator.DefaultExcelAttributeContext;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.translators.ExcelTranslatorRegistry;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExcelHeaderService implements ExcelHeaderService
{
    private static final Pattern LANG_EXTRACT_PATTERN = Pattern.compile(".+\\[(.+)]");
    private ExcelTemplateConstants.Header headerRowIndex = ExcelTemplateConstants.Header.DISPLAY_NAME;
    private ExcelTemplateConstants.Header referencePatternRowIndex = ExcelTemplateConstants.Header.REFERENCE_PATTERN;
    private ExcelTemplateConstants.Header defaultValueIndex = ExcelTemplateConstants.Header.DEFAULT_VALUE;
    private ExcelCellService excelCellService;
    private ExcelSheetService excelSheetService;
    private AttributeNameFormatter<ExcelAttributeDescriptorAttribute> attributeNameFormatter;
    private CollectionFormatter collectionFormatter;
    private ExcelTranslatorRegistry excelTranslatorRegistry;
    private TypeService typeService;


    public Collection<SelectedAttribute> getHeaders(Sheet metaInformationSheet, Sheet typeSheet)
    {
        if(typeSheet.getLastRowNum() <= this.defaultValueIndex.getIndex())
        {
            return Collections.emptyList();
        }
        Collection<SelectedAttribute> selectedAttributes = new ArrayList<>();
        Row headerRow = typeSheet.getRow(this.headerRowIndex.getIndex());
        Row valuesRow = typeSheet.getRow(this.defaultValueIndex.getIndex());
        for(int columnIndex = 0; columnIndex < headerRow.getLastCellNum(); columnIndex++)
        {
            String headerValue = this.excelCellService.getCellValue(headerRow.getCell(columnIndex));
            if(StringUtils.isNotBlank(headerValue))
            {
                String typeCode = this.excelSheetService.findTypeCodeForSheetName(typeSheet.getWorkbook(), typeSheet.getSheetName());
                Optional<Row> typeSystemRow = findTypeSystemRowForGivenHeader(metaInformationSheet, typeCode, headerValue);
                if(typeSystemRow.isPresent())
                {
                    Row row = typeSystemRow.get();
                    AttributeDescriptorModel attributeDescriptor = loadAttributeDescriptor(row, typeCode);
                    SelectedAttribute selectedAttribute = new SelectedAttribute(loadIsoCode(row, headerValue), attributeDescriptor);
                    this.excelTranslatorRegistry.getTranslator(attributeDescriptor).ifPresent(translator -> selectedAttribute.setReferenceFormat(translator.referenceFormat(attributeDescriptor)));
                    selectedAttribute.setDefaultValues(this.excelCellService.getCellValue(valuesRow.getCell(columnIndex)));
                    selectedAttributes.add(selectedAttribute);
                }
            }
        }
        return (Collection<SelectedAttribute>)ImmutableList.copyOf(selectedAttributes);
    }


    public Collection<String> getHeaderDisplayNames(Sheet sheet)
    {
        List<String> attributeDisplayNames = new ArrayList<>();
        Row headerRow = sheet.getRow(this.headerRowIndex.getIndex());
        for(int columnIndex = 0; columnIndex < headerRow.getLastCellNum(); columnIndex++)
        {
            String headerValue = this.excelCellService.getCellValue(headerRow.getCell(columnIndex));
            if(StringUtils.isNotBlank(headerValue))
            {
                attributeDisplayNames.add(headerValue);
            }
        }
        return attributeDisplayNames;
    }


    public Collection<SelectedAttributeQualifier> getSelectedAttributesQualifiers(Sheet metaInformationSheet, Sheet typeSheet)
    {
        if(typeSheet.getLastRowNum() <= this.defaultValueIndex.getIndex())
        {
            return Collections.emptyList();
        }
        Collection<SelectedAttributeQualifier> selectedAttributesQualifiers = new ArrayList<>();
        Row headerRow = typeSheet.getRow(this.headerRowIndex.getIndex());
        for(int columnIndex = 0; columnIndex < headerRow.getLastCellNum(); columnIndex++)
        {
            String headerValue = this.excelCellService.getCellValue(headerRow.getCell(columnIndex));
            String headerValueWithoutMetadata = getHeaderValueWithoutSpecialMarks(headerValue);
            if(StringUtils.isNotBlank(headerValue))
            {
                Optional<Row> typeSystemRow = findTypeSystemRowForGivenHeader(metaInformationSheet, typeSheet.getSheetName(), headerValue);
                if(typeSystemRow.isPresent())
                {
                    Row row = typeSystemRow.get();
                    selectedAttributesQualifiers.add(new SelectedAttributeQualifier(headerValueWithoutMetadata, this.excelCellService
                                    .getCellValue(row.getCell(ExcelTemplateConstants.TypeSystem.ATTR_QUALIFIER.getIndex()))));
                }
                else
                {
                    selectedAttributesQualifiers.add(new SelectedAttributeQualifier(headerValueWithoutMetadata, null));
                }
            }
        }
        return (Collection<SelectedAttributeQualifier>)ImmutableList.copyOf(selectedAttributesQualifiers);
    }


    public void insertAttributeHeader(Sheet sheet, ExcelAttribute excelAttribute, int columnIndex)
    {
        String nameToDisplay = this.attributeNameFormatter.format(DefaultExcelAttributeContext.ofExcelAttribute(excelAttribute));
        Row row = sheet.getRow(this.headerRowIndex.getIndex());
        Cell cell = row.createCell(row.getFirstCellNum() + columnIndex);
        this.excelCellService.insertAttributeValue(cell, nameToDisplay);
        Row patternRow = sheet.getRow(this.referencePatternRowIndex.getIndex());
        Cell patternCell = patternRow.getCell(row.getFirstCellNum() + columnIndex);
        patternCell.setCellFormula(patternCell.getCellFormula());
    }


    public void insertAttributesHeader(Sheet sheet, Collection<? extends ExcelAttribute> excelAttributes)
    {
        int idx = 0;
        for(ExcelAttribute attribute : excelAttributes)
        {
            insertAttributeHeader(sheet, attribute, idx++);
        }
    }


    protected Optional<Row> findTypeSystemRowForGivenHeader(Sheet metaInformationSheet, String typeCode, String header)
    {
        Predicate<Row> isRowForGivenTypeCode = row -> this.collectionFormatter.formatToCollection(this.excelCellService.getCellValue(row.getCell(ExcelTemplateConstants.TypeSystem.TYPE_CODE.getIndex()))).contains(typeCode);
        Predicate<Row> isRowForGivenHeader = row -> this.collectionFormatter.formatToCollection(this.excelCellService.getCellValue(row.getCell(ExcelTemplateConstants.TypeSystem.ATTR_DISPLAYED_NAME.getIndex()))).contains(header);
        Objects.requireNonNull(metaInformationSheet);
        return IntStream.rangeClosed(0, metaInformationSheet.getLastRowNum()).<Row>mapToObj(metaInformationSheet::getRow)
                        .filter(isRowForGivenHeader.and(isRowForGivenTypeCode))
                        .findFirst();
    }


    protected String loadIsoCode(Row row, String header)
    {
        boolean isLocalized = BooleanUtils.toBoolean(this.excelCellService.getCellValue(row.getCell(ExcelTemplateConstants.TypeSystem.ATTR_LOCALIZED.getIndex())));
        if(isLocalized)
        {
            Matcher matcher = LANG_EXTRACT_PATTERN.matcher(header);
            if(matcher.find())
            {
                return matcher.group(1);
            }
        }
        return null;
    }


    protected AttributeDescriptorModel loadAttributeDescriptor(Row row, String typeCode)
    {
        String qualifier = this.excelCellService.getCellValue(row.getCell(ExcelTemplateConstants.TypeSystem.ATTR_QUALIFIER.getIndex()));
        return this.typeService.getAttributeDescriptor(typeCode, qualifier);
    }


    @Required
    public void setAttributeNameFormatter(AttributeNameFormatter<ExcelAttributeDescriptorAttribute> attributeNameFormatter)
    {
        this.attributeNameFormatter = attributeNameFormatter;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Required
    public void setCollectionFormatter(CollectionFormatter collectionFormatter)
    {
        this.collectionFormatter = collectionFormatter;
    }


    @Required
    public void setExcelSheetService(ExcelSheetService excelSheetService)
    {
        this.excelSheetService = excelSheetService;
    }


    @Required
    public void setExcelCellService(ExcelCellService excelCellService)
    {
        this.excelCellService = excelCellService;
    }


    @Required
    public void setExcelTranslatorRegistry(ExcelTranslatorRegistry excelTranslatorRegistry)
    {
        this.excelTranslatorRegistry = excelTranslatorRegistry;
    }


    public void setHeaderRowIndex(ExcelTemplateConstants.Header headerRowIndex)
    {
        this.headerRowIndex = headerRowIndex;
    }


    public void setReferencePatternRowIndex(ExcelTemplateConstants.Header referencePatternRowIndex)
    {
        this.referencePatternRowIndex = referencePatternRowIndex;
    }


    public void setDefaultValueIndex(ExcelTemplateConstants.Header defaultValueIndex)
    {
        this.defaultValueIndex = defaultValueIndex;
    }
}
