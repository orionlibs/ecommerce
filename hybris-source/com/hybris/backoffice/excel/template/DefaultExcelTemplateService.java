package com.hybris.backoffice.excel.template;

import com.google.common.base.Preconditions;
import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.data.SelectedAttributeQualifier;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.header.ExcelHeaderService;
import com.hybris.backoffice.excel.template.populator.DefaultExcelAttributeContext;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.template.workbook.ExcelWorkbookService;
import com.hybris.backoffice.excel.translators.ExcelTranslatorRegistry;
import com.hybris.backoffice.excel.util.ExcelDateUtils;
import de.hybris.platform.core.model.c2l.C2LItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.util.Config;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.SheetVisibility;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "1808", forRemoval = true)
public class DefaultExcelTemplateService implements ExcelTemplateService
{
    private static final UnaryOperator<String> EMPTY_PATTERN;
    private static final UnaryOperator<String> NULL_PATTERN;
    private static final String SELECTED_ATTRIBUTE_ARG = "selectedAttribute";
    private static final String SHEET_ARG = "Sheet";
    private static final String WORKBOOK_ARG = "Workbook";
    private static final String VALUE_ARG = "Value";
    private ExcelCellService cellService;
    private ExcelSheetService sheetService;
    private ExcelHeaderService headerService;
    private ExcelWorkbookService workbookService;
    private AttributeNameFormatter<ExcelAttributeDescriptorAttribute> attributeNameFormatter;
    private TypeService typeService;
    @Deprecated(since = "1808", forRemoval = true)
    private ExcelDateUtils excelDateUtils;
    @Deprecated(since = "1808", forRemoval = true)
    private ExcelSheetNamingStrategy excelSheetNamingStrategy;
    @Deprecated(since = "1808", forRemoval = true)
    private CommonI18NService commonI18NService;
    @Deprecated(since = "1808", forRemoval = true)
    private ExcelTranslatorRegistry excelTranslatorRegistry;
    @Deprecated(since = "1808", forRemoval = true)
    private PermissionCRUDService permissionCRUDService;

    static
    {
        EMPTY_PATTERN = (arg -> String.format("%s cannot be empty or null", new Object[] {arg}));
        NULL_PATTERN = (arg -> String.format("%s cannot be null", new Object[] {arg}));
    }

    public Workbook createWorkbook(InputStream is)
    {
        return this.workbookService.createWorkbook(is);
    }


    public Sheet getTypeSystemSheet(Workbook workbook)
    {
        return this.workbookService.getMetaInformationSheet(workbook);
    }


    public List<String> getSheetsNames(Workbook workbook)
    {
        return new ArrayList<>(this.sheetService.getSheetsNames(workbook));
    }


    public List<Sheet> getSheets(Workbook workbook)
    {
        return new ArrayList<>(this.sheetService.getSheets(workbook));
    }


    public String getCellValue(Cell cell)
    {
        return this.cellService.getCellValue(cell);
    }


    public List<SelectedAttribute> getHeaders(Sheet typeSystemSheet, Sheet typeSheet)
    {
        return new ArrayList<>(this.headerService.getHeaders(typeSystemSheet, typeSheet));
    }


    public List<SelectedAttributeQualifier> getSelectedAttributesQualifiers(Sheet typeSystemSheet, Sheet typeSheet)
    {
        return new ArrayList<>(this.headerService.getSelectedAttributesQualifiers(typeSystemSheet, typeSheet));
    }


    public int findColumnIndex(Sheet typeSystemSheet, Sheet sheet, SelectedAttribute selectedAttribute)
    {
        return this.sheetService.findColumnIndex(typeSystemSheet, sheet, (ExcelAttribute)new ExcelAttributeDescriptorAttribute(selectedAttribute
                        .getAttributeDescriptor(), selectedAttribute.getIsoCode()));
    }


    public Sheet createTypeSheet(String typeCode, Workbook workbook)
    {
        Preconditions.checkArgument(StringUtils.isNotEmpty(typeCode), EMPTY_PATTERN.apply("typeCode"));
        Preconditions.checkNotNull(workbook, NULL_PATTERN.apply("Workbook"));
        return this.sheetService.createOrGetTypeSheet(workbook, typeCode);
    }


    public String findTypeCodeForSheetName(String sheetName, Workbook workbook)
    {
        return this.sheetService.findTypeCodeForSheetName(workbook, sheetName);
    }


    public String findSheetNameForTypeCode(String typeCode, Workbook workbook)
    {
        return this.sheetService.findSheetNameForTypeCode(workbook, typeCode);
    }


    public void addTypeSheet(String typeName, Workbook workbook)
    {
        Preconditions.checkArgument(StringUtils.isNotEmpty(typeName), EMPTY_PATTERN.apply("typeName"));
        Preconditions.checkNotNull(workbook, NULL_PATTERN.apply("Workbook"));
        this.sheetService.createTypeSheet(workbook, typeName);
    }


    public void insertAttributeHeader(Sheet sheet, SelectedAttribute selectedAttribute, int columnIndex)
    {
        Preconditions.checkNotNull(sheet, NULL_PATTERN.apply("Sheet"));
        Preconditions.checkNotNull(selectedAttribute, NULL_PATTERN.apply("selectedAttribute"));
        this.headerService.insertAttributeHeader(sheet, (ExcelAttribute)new ExcelAttributeDescriptorAttribute(selectedAttribute
                        .getAttributeDescriptor(), selectedAttribute.getIsoCode()), columnIndex);
    }


    public void insertAttributesHeader(Sheet sheet, Collection<SelectedAttribute> selectedAttributes)
    {
        Preconditions.checkNotNull(sheet, NULL_PATTERN.apply("Sheet"));
        Preconditions.checkNotNull(selectedAttributes, NULL_PATTERN.apply("selectedAttribute"));
        this.headerService.insertAttributesHeader(sheet, (Collection)selectedAttributes
                        .stream()
                        .map(selectedAttribute -> new ExcelAttributeDescriptorAttribute(selectedAttribute.getAttributeDescriptor(), selectedAttribute.getIsoCode()))
                        .collect(Collectors.toList()));
    }


    public void insertAttributeValue(Cell cell, Object object)
    {
        Preconditions.checkNotNull(cell, NULL_PATTERN.apply("Cell"));
        Preconditions.checkNotNull(object, NULL_PATTERN.apply("Value"));
        this.cellService.insertAttributeValue(cell, object);
    }


    public Row createEmptyRow(Sheet sheet)
    {
        Preconditions.checkNotNull(sheet, NULL_PATTERN.apply("Sheet"));
        return this.sheetService.createEmptyRow(sheet);
    }


    public String getAttributeDisplayName(AttributeDescriptorModel attributeDescriptorModel, String isoCode)
    {
        return this.attributeNameFormatter.format(
                        DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)new ExcelAttributeDescriptorAttribute(attributeDescriptorModel, isoCode)));
    }


    @Deprecated(since = "1808", forRemoval = true)
    protected void hideUtilitySheet(Workbook workbook, String sheetName)
    {
        if(ExcelTemplateConstants.isUtilitySheet(sheetName))
        {
            int sheetIndex = workbook.getSheetIndex(sheetName);
            if(!workbook.isSheetHidden(sheetIndex) || workbook.getSheetAt(sheetIndex).isSelected())
            {
                activateFirstNonUtilitySheet(workbook);
                workbook.getSheetAt(sheetIndex).setSelected(false);
                workbook.setSheetVisibility(sheetIndex, SheetVisibility.values()[getUtilitySheetHiddenLevel()]);
            }
        }
    }


    @Deprecated(since = "1808", forRemoval = true)
    protected int getUtilitySheetHiddenLevel()
    {
        return Config.getBoolean("backoffice.excel.utility.sheets.hidden", true) ? SheetVisibility.VERY_HIDDEN.ordinal() :
                        SheetVisibility.HIDDEN.ordinal();
    }


    @Deprecated(since = "1808", forRemoval = true)
    protected void activateFirstNonUtilitySheet(Workbook workbook)
    {
        if(ExcelTemplateConstants.isUtilitySheet(workbook.getSheetName(workbook.getActiveSheetIndex())))
        {
            for(int i = 0; i < workbook.getNumberOfSheets(); i++)
            {
                if(!ExcelTemplateConstants.isUtilitySheet(workbook.getSheetName(i)))
                {
                    workbook.setActiveSheet(i);
                }
            }
        }
    }


    @Deprecated(since = "1808", forRemoval = true)
    protected void populateTypeSystemSheet(ComposedTypeModel composedType, Workbook workbook)
    {
        Sheet typeSystemSheet = workbook.getSheet("TypeSystem");
        Objects.requireNonNull(this.permissionCRUDService);
        this.typeService.getAttributeDescriptorsForType(composedType).stream().filter(attribute -> (BooleanUtils.isTrue(attribute.getReadable()) && BooleanUtils.isTrue(attribute.getWritable()))).filter(this.permissionCRUDService::canReadAttribute)
                        .sorted(Comparator.comparing(this::getAttributeDescriptorName))
                        .forEach(attributeDescriptor -> addAttributeToExcelTypeSystemSheet(attributeDescriptor, typeSystemSheet));
    }


    @Deprecated(since = "1808", forRemoval = true)
    protected void addAttributeToExcelTypeSystemSheet(AttributeDescriptorModel attributeDescriptor, Sheet sheet)
    {
        if(this.excelTranslatorRegistry.getTranslator(attributeDescriptor).isPresent())
        {
            if(attributeDescriptor.getLocalized().booleanValue())
            {
                this.commonI18NService.getAllLanguages().stream().filter(C2LItemModel::getActive)
                                .forEach(lang -> addAttributeToExcelTypeSystemSheet(attributeDescriptor, sheet, lang.getIsocode()));
            }
            else
            {
                addAttributeToExcelTypeSystemSheet(attributeDescriptor, sheet, "");
            }
        }
    }


    @Deprecated(since = "1808", forRemoval = true)
    protected void addAttributeToExcelTypeSystemSheet(AttributeDescriptorModel attributeDescriptor, Sheet sheet, String langIsoCode)
    {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        String sheetNameForTypeCode = findSheetNameForTypeCode(attributeDescriptor.getEnclosingType().getCode(), sheet
                        .getWorkbook());
        row.createCell(0).setCellValue(sheetNameForTypeCode);
        row.createCell(1)
                        .setCellValue(attributeDescriptor.getEnclosingType().getName());
        row.createCell(2).setCellValue(attributeDescriptor.getQualifier());
        row.createCell(3).setCellValue(attributeDescriptor.getName());
        row.createCell(4).setCellValue(attributeDescriptor.getOptional().booleanValue());
        row.createCell(5)
                        .setCellValue(attributeDescriptor.getAttributeType().getCode());
        row.createCell(6)
                        .setCellValue(attributeDescriptor.getDeclaringEnclosingType().getCode());
        row.createCell(7).setCellValue(attributeDescriptor.getLocalized().booleanValue());
        row.createCell(8).setCellValue(langIsoCode);
        row.createCell(9)
                        .setCellValue(getAttributeDisplayName(attributeDescriptor, langIsoCode));
        row.createCell(10).setCellValue(attributeDescriptor.getUnique().booleanValue());
        String referenceFormat = this.excelTranslatorRegistry.getTranslator(attributeDescriptor).map(excelValueTranslator -> excelValueTranslator.referenceFormat(attributeDescriptor)).orElse("");
        row.createCell(11).setCellValue(referenceFormat);
    }


    @Deprecated(since = "1808", forRemoval = true)
    protected boolean isMandatory(AttributeDescriptorModel attributeDescriptor, String langIsoCode)
    {
        if(BooleanUtils.isTrue(attributeDescriptor.getOptional()))
        {
            return false;
        }
        if(attributeDescriptor.getLocalized().booleanValue())
        {
            return this.commonI18NService.getCurrentLanguage().getIsocode().equals(langIsoCode);
        }
        return true;
    }


    @Deprecated(since = "1808", forRemoval = true)
    protected String getAttributeDescriptorName(AttributeDescriptorModel attributeDescriptor)
    {
        return StringUtils.isNotEmpty(attributeDescriptor.getName()) ? attributeDescriptor.getName() :
                        attributeDescriptor.getQualifier();
    }


    @Required
    public void setCellService(ExcelCellService cellService)
    {
        this.cellService = cellService;
    }


    @Required
    public void setHeaderService(ExcelHeaderService headerService)
    {
        this.headerService = headerService;
    }


    @Required
    public void setSheetService(ExcelSheetService sheetService)
    {
        this.sheetService = sheetService;
    }


    @Required
    public void setWorkbookService(ExcelWorkbookService workbookService)
    {
        this.workbookService = workbookService;
    }


    @Required
    public void setAttributeNameFormatter(AttributeNameFormatter<ExcelAttributeDescriptorAttribute> attributeNameFormatter)
    {
        this.attributeNameFormatter = attributeNameFormatter;
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Deprecated(since = "1808", forRemoval = true)
    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public ExcelTranslatorRegistry getExcelTranslatorRegistry()
    {
        return this.excelTranslatorRegistry;
    }


    @Deprecated(since = "1808", forRemoval = true)
    @Required
    public void setExcelTranslatorRegistry(ExcelTranslatorRegistry excelTranslatorRegistry)
    {
        this.excelTranslatorRegistry = excelTranslatorRegistry;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public PermissionCRUDService getPermissionCRUDService()
    {
        return this.permissionCRUDService;
    }


    @Deprecated(since = "1808", forRemoval = true)
    @Required
    public void setPermissionCRUDService(PermissionCRUDService permissionCRUDService)
    {
        this.permissionCRUDService = permissionCRUDService;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public ExcelDateUtils getExcelDateUtils()
    {
        return this.excelDateUtils;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public void setExcelDateUtils(ExcelDateUtils excelDateUtils)
    {
        this.excelDateUtils = excelDateUtils;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public ExcelSheetNamingStrategy getExcelSheetNamingStrategy()
    {
        return this.excelSheetNamingStrategy;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public void setExcelSheetNamingStrategy(ExcelSheetNamingStrategy excelSheetNamingStrategy)
    {
        this.excelSheetNamingStrategy = excelSheetNamingStrategy;
    }
}
