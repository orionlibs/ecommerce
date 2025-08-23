package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.SelectedAttributeQualifier;
import com.hybris.backoffice.excel.template.ExcelTemplateService;
import com.hybris.backoffice.excel.template.header.ExcelHeaderService;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.template.workbook.ExcelWorkbookService;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.security.permissions.PermissionCRUDService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class WorkbookTypeCodeAndSelectedAttributeValidator implements WorkbookValidator
{
    public static final String EXCEL_IMPORT_VALIDATION_METADATA_UNKNOWN_TYPE_DESCRIPTION = "excel.import.validation.workbook.type.unknown.type";
    public static final String EXCEL_IMPORT_VALIDATION_METADATA_UNKNOWN_TYPE_HEADER = "excel.import.validation.workbook.type.unknown.header";
    public static final String EXCEL_IMPORT_VALIDATION_WORKBOOK_UNKNOWN_ATTRIBUTE_DESCRIPTION = "excel.import.validation.workbook.attribute.unknown.type";
    public static final String EXCEL_IMPORT_VALIDATION_WORKBOOK_UNKNOWN_ATTRIBUTE_HEADER = "excel.import.validation.workbook.attribute.unknown.header";
    public static final String EXCEL_IMPORT_VALIDATION_WORKBOOK_DUPLICATED_COLUMNS_HEADER = "excel.import.validation.workbook.attribute.duplicated.header";
    public static final String EXCEL_IMPORT_VALIDATION_WORKBOOK_DUPLICATED_COLUMNS_DESCRIPTION = "excel.import.validation.workbook.attribute.duplicated.description";
    private static final Logger LOG = LoggerFactory.getLogger(WorkbookTypeCodeAndSelectedAttributeValidator.class);
    @Deprecated(since = "1808", forRemoval = true)
    private ExcelTemplateService excelTemplateService;
    private ExcelWorkbookService excelWorkbookService;
    private ExcelSheetService excelSheetService;
    private ExcelHeaderService excelHeaderService;
    private PermissionCRUDService permissionCRUDService;


    public List<ExcelValidationResult> validate(Workbook workbook)
    {
        List<ExcelValidationResult> validationResults = new ArrayList<>();
        Sheet typeSystemSheet = getExcelWorkbookService().getMetaInformationSheet(workbook);
        Collection<Sheet> sheets = getExcelSheetService().getSheets(workbook);
        for(Sheet sheet : sheets)
        {
            Objects.requireNonNull(validationResults);
            validateSheet(typeSystemSheet, sheet).stream().filter(result -> !result.getValidationErrors().isEmpty()).forEach(validationResults::add);
        }
        return validationResults;
    }


    protected List<ExcelValidationResult> validateSheet(Sheet typeSystemSheet, Sheet sheet)
    {
        List<ExcelValidationResult> validationResults = new ArrayList<>();
        String typeCode = getExcelSheetService().findTypeCodeForSheetName(sheet.getWorkbook(), sheet.getSheetName());
        Optional<ExcelValidationResult> typeCodeValidationResult = validateTypeCode(typeCode);
        if(typeCodeValidationResult.isPresent())
        {
            validationResults.add(typeCodeValidationResult.get());
        }
        else
        {
            Objects.requireNonNull(validationResults);
            validateSelectedColumns(typeSystemSheet, sheet).stream().filter(ExcelValidationResult::hasErrors).forEach(validationResults::add);
        }
        return validationResults;
    }


    protected Optional<ExcelValidationResult> validateTypeCode(String typeCode)
    {
        try
        {
            if(!hasPermissionsToTypeCode(typeCode))
            {
                return Optional.of(prepareValidationResultForUnknownType(typeCode));
            }
        }
        catch(UnknownIdentifierException ex)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(String.format("Unknown type for code %s", new Object[] {typeCode}), (Throwable)ex);
            }
            return Optional.of(prepareValidationResultForUnknownType(typeCode));
        }
        return Optional.empty();
    }


    protected List<ExcelValidationResult> validateSelectedColumns(Sheet typeSystemSheet, Sheet sheet)
    {
        String typeCode = getExcelSheetService().findTypeCodeForSheetName(sheet.getWorkbook(), sheet.getSheetName());
        List<SelectedAttributeQualifier> qualifiers = new ArrayList<>(getExcelHeaderService().getSelectedAttributesQualifiers(typeSystemSheet, sheet));
        List<ExcelValidationResult> validationResults = new ArrayList<>();
        Objects.requireNonNull(validationResults);
        validateColumnUniqueness(sheet.getSheetName(), qualifiers).ifPresent(validationResults::add);
        Objects.requireNonNull(validationResults);
        validateWhetherColumnExistAndUserHasPermission(typeCode, qualifiers).ifPresent(validationResults::add);
        return validationResults;
    }


    protected Optional<ExcelValidationResult> validateWhetherColumnExistAndUserHasPermission(String typeCode, List<SelectedAttributeQualifier> qualifiers)
    {
        List<ValidationMessage> messages = new ArrayList<>();
        for(SelectedAttributeQualifier qualifier : qualifiers)
        {
            if(StringUtils.isBlank(qualifier.getQualifier()))
            {
                messages.add(new ValidationMessage("excel.import.validation.workbook.attribute.unknown.type", new Serializable[] {typeCode, qualifier
                                .getName()}));
                continue;
            }
            Objects.requireNonNull(messages);
            validateSingleAttribute(typeCode, qualifier).ifPresent(messages::add);
        }
        if(messages.isEmpty())
        {
            return Optional.empty();
        }
        ExcelValidationResult validationResult = new ExcelValidationResult(messages);
        validationResult.setHeader(new ValidationMessage("excel.import.validation.workbook.attribute.unknown.header", new Serializable[] {typeCode}));
        return Optional.of(validationResult);
    }


    private Optional<ValidationMessage> validateSingleAttribute(String typeCode, SelectedAttributeQualifier qualifier)
    {
        return validateSingleAttribute(typeCode, qualifier.getName(), qualifier.getQualifier());
    }


    protected Optional<ValidationMessage> validateSingleAttribute(String typeCode, String columnName, String qualifier)
    {
        try
        {
            if(!hasPermissionsToAttribute(typeCode, qualifier))
            {
                return Optional.of(new ValidationMessage("excel.import.validation.workbook.attribute.unknown.type", new Serializable[] {typeCode, columnName}));
            }
        }
        catch(UnknownIdentifierException ex)
        {
            return
                            Optional.of(new ValidationMessage("excel.import.validation.workbook.attribute.unknown.type", new Serializable[] {typeCode, columnName}));
        }
        return Optional.empty();
    }


    protected Optional<ExcelValidationResult> validateColumnUniqueness(String sheetName, List<SelectedAttributeQualifier> selectedColumns)
    {
        Set<SelectedAttributeQualifier> duplicatedColumns = findDuplicatedColumns(selectedColumns);
        List<String> duplicateColumnsNames = (List<String>)duplicatedColumns.stream().map(SelectedAttributeQualifier::getName).collect(Collectors.toList());
        return createValidationResult(sheetName, duplicateColumnsNames);
    }


    protected Optional<ExcelValidationResult> createValidationResult(String sheetName, List<String> duplicateColumnNames)
    {
        List<ValidationMessage> messages = new ArrayList<>();
        for(String columnName : duplicateColumnNames)
        {
            messages.add(new ValidationMessage("excel.import.validation.workbook.attribute.duplicated.description", new Serializable[] {columnName}));
        }
        if(messages.isEmpty())
        {
            return Optional.empty();
        }
        ExcelValidationResult validationResult = new ExcelValidationResult(messages);
        validationResult.setHeader(new ValidationMessage("excel.import.validation.workbook.attribute.duplicated.header", new Serializable[] {sheetName}));
        return Optional.of(validationResult);
    }


    protected Set<SelectedAttributeQualifier> findDuplicatedColumns(List<SelectedAttributeQualifier> selectedColumns)
    {
        return findDuplicates(selectedColumns);
    }


    protected <T> Set<T> findDuplicates(Collection<T> collection)
    {
        Set<T> uniques = new HashSet<>();
        return (Set<T>)collection.stream().filter(e -> !uniques.add(e)).collect(Collectors.toSet());
    }


    protected boolean hasPermissionsToTypeCode(String typeCode)
    {
        return (getPermissionCRUDService().canReadType(typeCode) && getPermissionCRUDService().canChangeType(typeCode) &&
                        getPermissionCRUDService().canCreateTypeInstance(typeCode));
    }


    protected boolean hasPermissionsToAttribute(String typeCode, SelectedAttributeQualifier qualifier)
    {
        return hasPermissionsToAttribute(typeCode, qualifier.getQualifier());
    }


    protected boolean hasPermissionsToAttribute(String typeCode, String qualifier)
    {
        return (getPermissionCRUDService().canReadAttribute(typeCode, qualifier) &&
                        getPermissionCRUDService().canChangeAttribute(typeCode, qualifier));
    }


    protected ExcelValidationResult prepareValidationResultForUnknownType(String sheetName)
    {
        ExcelValidationResult validationResult = new ExcelValidationResult(new ValidationMessage("excel.import.validation.workbook.type.unknown.type", new Serializable[] {sheetName}));
        validationResult.setHeader(new ValidationMessage("excel.import.validation.workbook.type.unknown.header", new Serializable[] {sheetName}));
        return validationResult;
    }


    @Deprecated(since = "1808", forRemoval = true)
    public ExcelTemplateService getExcelTemplateService()
    {
        return this.excelTemplateService;
    }


    @Deprecated(since = "1808", forRemoval = true)
    @Required
    public void setExcelTemplateService(ExcelTemplateService excelTemplateService)
    {
        this.excelTemplateService = excelTemplateService;
    }


    public ExcelWorkbookService getExcelWorkbookService()
    {
        return this.excelWorkbookService;
    }


    @Required
    public void setExcelWorkbookService(ExcelWorkbookService excelWorkbookService)
    {
        this.excelWorkbookService = excelWorkbookService;
    }


    public ExcelSheetService getExcelSheetService()
    {
        return this.excelSheetService;
    }


    @Required
    public void setExcelSheetService(ExcelSheetService excelSheetService)
    {
        this.excelSheetService = excelSheetService;
    }


    public ExcelHeaderService getExcelHeaderService()
    {
        return this.excelHeaderService;
    }


    @Required
    public void setExcelHeaderService(ExcelHeaderService excelHeaderService)
    {
        this.excelHeaderService = excelHeaderService;
    }


    public PermissionCRUDService getPermissionCRUDService()
    {
        return this.permissionCRUDService;
    }


    @Required
    public void setPermissionCRUDService(PermissionCRUDService permissionCRUDService)
    {
        this.permissionCRUDService = permissionCRUDService;
    }
}
