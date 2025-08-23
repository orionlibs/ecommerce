package com.hybris.backoffice.excel.template.sheet;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.template.AttributeNameFormatter;
import com.hybris.backoffice.excel.template.CollectionFormatter;
import com.hybris.backoffice.excel.template.ExcelSheetNamingStrategy;
import com.hybris.backoffice.excel.template.ExcelTemplateConstants;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.populator.DefaultExcelAttributeContext;
import com.hybris.backoffice.excel.template.populator.ExcelAttributeContext;
import com.hybris.backoffice.excel.template.workbook.ExcelWorkbookService;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import java.util.Collection;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import javax.annotation.WillNotClose;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.openxmlformats.schemas.officeDocument.x2006.customProperties.CTProperty;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExcelSheetService implements ExcelSheetService
{
    static final String ISO_CODE_KEY = "isoCode";
    private Collection<ExcelTemplateConstants.UtilitySheet> excludedSheets = Collections.emptyList();
    private ExcelTemplateConstants.Header headerRowIndex = ExcelTemplateConstants.Header.DISPLAY_NAME;
    private ExcelTemplateConstants.UtilitySheet typeTemplate = ExcelTemplateConstants.UtilitySheet.TYPE_TEMPLATE;
    private AttributeNameFormatter<ExcelAttributeDescriptorAttribute> attributeNameFormatter;
    private CollectionFormatter collectionFormatter;
    private ExcelCellService excelCellService;
    private ExcelSheetNamingStrategy excelSheetNamingStrategy;
    private ExcelWorkbookService excelWorkbookService;


    public Collection<String> getSheetsNames(@WillNotClose Workbook workbook)
    {
        Objects.requireNonNull(workbook);
        return (Collection<String>)IntStream.range(0, workbook.getNumberOfSheets()).mapToObj(workbook::getSheetName)
                        .filter(sheetName -> !ExcelTemplateConstants.UtilitySheet.isUtilitySheet(this.excludedSheets, sheetName))
                        .collect(ImmutableList.toImmutableList());
    }


    public Collection<Sheet> getSheets(@WillNotClose Workbook workbook)
    {
        Objects.requireNonNull(workbook);
        return (Collection<Sheet>)getSheetsNames(workbook).stream().map(workbook::getSheet)
                        .collect(ImmutableList.toImmutableList());
    }


    public Sheet createOrGetTypeSheet(@WillNotClose Workbook workbook, @Nonnull String typeCode)
    {
        String sheetName = this.excelSheetNamingStrategy.generateName(workbook, typeCode);
        this.excelWorkbookService.addProperty(workbook, sheetName, typeCode);
        return getSheet(workbook, sheetName).orElseGet(() -> createTypeSheet(workbook, sheetName));
    }


    public Sheet createTypeSheet(@WillNotClose Workbook workbook, @Nonnull String sheetName)
    {
        int typeSheetTemplateIndex = workbook.getSheetIndex(this.typeTemplate.getSheetName());
        Sheet clonedSheet = workbook.cloneSheet(typeSheetTemplateIndex);
        workbook.setSheetName(workbook.getSheetIndex(clonedSheet), sheetName);
        return clonedSheet;
    }


    public Sheet createOrGetUtilitySheet(@WillNotClose Workbook workbook, @Nonnull String sheetName)
    {
        return Optional.<Sheet>ofNullable(workbook.getSheet(sheetName)).orElseGet(() -> workbook.createSheet(sheetName));
    }


    public int findColumnIndex(Sheet typeSystemSheet, @Nonnull Sheet sheet, ExcelAttribute excelAttribute)
    {
        String attributeDisplayName = findAttributeDisplayNameInTypeSystemSheet(typeSystemSheet, excelAttribute,
                        findTypeCodeForSheetName(sheet.getWorkbook(), sheet.getSheetName()));
        if(StringUtils.isBlank(attributeDisplayName))
        {
            return -1;
        }
        Set<String> attributeDisplayNames = this.collectionFormatter.formatToCollection(attributeDisplayName);
        Row headerRow = sheet.getRow(this.headerRowIndex.getIndex());
        for(int i = 0; i <= headerRow.getLastCellNum(); i++)
        {
            String cellValue = this.excelCellService.getCellValue(headerRow.getCell(i));
            if(attributeDisplayNames.contains(cellValue) && StringUtils.equals(cellValue, this.attributeNameFormatter
                            .format(getExcelAttributeContext(sheet.getWorkbook(), excelAttribute))))
            {
                return i;
            }
        }
        return -1;
    }


    public String findTypeCodeForSheetName(@WillNotClose Workbook workbook, String sheetName)
    {
        return this.excelWorkbookService.getProperty(workbook, sheetName).orElse(sheetName);
    }


    public String findSheetNameForTypeCode(@WillNotClose Workbook workbook, String typeCode)
    {
        Predicate<CTProperty> equals = property -> StringUtils.equals(property.getLpwstr(), typeCode);
        Predicate<CTProperty> notBlank = property -> StringUtils.isNotBlank(property.getName());
        return this.excelWorkbookService.getUnderlyingProperties(workbook)
                        .stream()
                        .filter(equals.and(notBlank))
                        .findAny()
                        .map(CTProperty::getName)
                        .orElse(typeCode);
    }


    protected String findAttributeDisplayNameInTypeSystemSheet(Sheet typeSystemSheet, ExcelAttribute excelAttribute, String typeCode)
    {
        if(typeSystemSheet != null && excelAttribute instanceof ExcelAttributeDescriptorAttribute)
        {
            ExcelAttributeDescriptorAttribute descAttr = (ExcelAttributeDescriptorAttribute)excelAttribute;
            Objects.requireNonNull(typeSystemSheet);
            return IntStream.rangeClosed(0, typeSystemSheet.getLastRowNum()).mapToObj(typeSystemSheet::getRow)
                            .filter(row -> (qualifierEquality(row).and(nameEquality(row)).and(languageEquality(row)).test(descAttr) && typeCodeEquality(row, typeCode)))
                            .map(row -> this.excelCellService.getCellValue(row.getCell(ExcelTemplateConstants.TypeSystem.ATTR_DISPLAYED_NAME.getIndex())))
                            .findFirst()
                            .orElse("");
        }
        return "";
    }


    private boolean typeCodeEquality(Row row, String typeCode)
    {
        String rowTypeCode = this.excelCellService.getCellValue(row.getCell(ExcelTemplateConstants.TypeSystem.TYPE_CODE.getIndex()));
        return rowTypeCode.contains(String.format("{%s}", new Object[] {typeCode}));
    }


    private Predicate<ExcelAttributeDescriptorAttribute> qualifierEquality(Row row)
    {
        return attr -> {
            String cellQualifier = this.excelCellService.getCellValue(row.getCell(ExcelTemplateConstants.TypeSystem.ATTR_QUALIFIER.getIndex()));
            String descQualifier = attr.getAttributeDescriptorModel().getQualifier();
            return StringUtils.equals(cellQualifier, descQualifier);
        };
    }


    private Predicate<ExcelAttributeDescriptorAttribute> nameEquality(Row row)
    {
        return attr -> {
            String descName;
            String cellName = this.excelCellService.getCellValue(row.getCell(ExcelTemplateConstants.TypeSystem.ATTR_NAME.getIndex()));
            AttributeDescriptorModel attributeDescriptor = attr.getAttributeDescriptorModel();
            Optional<String> workbookIsoCode = this.excelWorkbookService.getProperty(row.getSheet().getWorkbook(), "isoCode");
            if(workbookIsoCode.isPresent())
            {
                descName = attributeDescriptor.getName(Locale.forLanguageTag(workbookIsoCode.get()));
            }
            else
            {
                descName = attributeDescriptor.getName();
            }
            return (StringUtils.equals(cellName, descName) || StringUtils.isAllBlank(new CharSequence[] {cellName, descName}));
        };
    }


    private Predicate<ExcelAttributeDescriptorAttribute> languageEquality(Row row)
    {
        return attr -> {
            String cellLanguage = this.excelCellService.getCellValue(row.getCell(ExcelTemplateConstants.TypeSystem.ATTR_LOC_LANG.getIndex()));
            String descLanguage = (attr.getIsoCode() != null) ? attr.getIsoCode() : "";
            return (StringUtils.equals(cellLanguage, descLanguage) || this.collectionFormatter.formatToCollection(cellLanguage).contains(descLanguage));
        };
    }


    private ExcelAttributeContext<ExcelAttributeDescriptorAttribute> getExcelAttributeContext(Workbook workbook, ExcelAttribute excelAttribute)
    {
        return DefaultExcelAttributeContext.ofMap(excelAttribute,
                        (Map)ImmutableMap.of("isoCode", this.excelWorkbookService.getProperty(workbook, "isoCode").orElse("")));
    }


    @Required
    public void setCollectionFormatter(CollectionFormatter collectionFormatter)
    {
        this.collectionFormatter = collectionFormatter;
    }


    @Required
    public void setExcelCellService(ExcelCellService excelCellService)
    {
        this.excelCellService = excelCellService;
    }


    @Required
    public void setExcelSheetNamingStrategy(ExcelSheetNamingStrategy excelSheetNamingStrategy)
    {
        this.excelSheetNamingStrategy = excelSheetNamingStrategy;
    }


    @Required
    public void setAttributeNameFormatter(AttributeNameFormatter<ExcelAttributeDescriptorAttribute> attributeNameFormatter)
    {
        this.attributeNameFormatter = attributeNameFormatter;
    }


    @Required
    public void setExcelWorkbookService(ExcelWorkbookService excelWorkbookService)
    {
        this.excelWorkbookService = excelWorkbookService;
    }


    public void setExcludedSheets(Collection<ExcelTemplateConstants.UtilitySheet> excludedSheets)
    {
        this.excludedSheets = excludedSheets;
    }


    public void setTypeTemplate(ExcelTemplateConstants.UtilitySheet typeTemplate)
    {
        this.typeTemplate = typeTemplate;
    }


    public void setHeaderRowIndex(ExcelTemplateConstants.Header headerRowIndex)
    {
        this.headerRowIndex = headerRowIndex;
    }
}
