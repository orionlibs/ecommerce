package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.importing.ExcelAttributeTypeSystemService;
import com.hybris.backoffice.excel.importing.ExcelClassificationTypeSystemService;
import com.hybris.backoffice.excel.importing.ExcelTypeSystemService;
import com.hybris.backoffice.excel.importing.data.ClassificationTypeSystemRow;
import com.hybris.backoffice.excel.template.header.ExcelHeaderService;
import com.hybris.backoffice.excel.template.populator.typesheet.TypeSystemRow;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.user.UserService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.poi.ss.usermodel.Sheet;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Required;

public class WorkbookTypeCodeAndAttributesValidator extends WorkbookTypeCodeAndSelectedAttributeValidator
{
    protected static final String CLASSIFICATION_SYSTEM_ERRORS_HEADER = "excel.import.validation.workbook.classification.header";
    protected static final String UNKNOWN_CLASSIFICATION_SYSTEM_VERSION = "excel.import.validation.workbook.classification.unknown.system.version";
    protected static final String INSUFFICIENT_PERMISSIONS_TO_TYPE = "excel.import.validation.workbook.insufficient.permissions.to.type";
    private ExcelHeaderService excelHeaderService;
    private CatalogVersionService catalogVersionService;
    @Deprecated(since = "1905", forRemoval = true)
    private UserService userService;
    private ExcelTypeSystemService<ExcelAttributeTypeSystemService.ExcelTypeSystem> excelAttributeTypeSystemService;
    private ExcelTypeSystemService<ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem> excelClassificationTypeSystemService;


    protected List<ExcelValidationResult> validateSelectedColumns(Sheet typeSystemSheet, Sheet sheet)
    {
        List<ExcelValidationResult> validationResults = new ArrayList<>();
        String typeCode = getExcelSheetService().findTypeCodeForSheetName(sheet.getWorkbook(), sheet.getSheetName());
        Collection<String> headerNames = this.excelHeaderService.getHeaderDisplayNames(sheet);
        List<TypeSystemRow> standardAttributes = new ArrayList<>();
        List<ClassificationTypeSystemRow> classificationAttributes = new ArrayList<>();
        List<String> unknownAttributes = new ArrayList<>();
        ExcelAttributeTypeSystemService.ExcelTypeSystem typeSystem = (ExcelAttributeTypeSystemService.ExcelTypeSystem)this.excelAttributeTypeSystemService.loadTypeSystem(sheet.getWorkbook());
        ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem classificationTypeSystem = (ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem)this.excelClassificationTypeSystemService.loadTypeSystem(sheet.getWorkbook());
        for(String headerName : headerNames)
        {
            Optional<TypeSystemRow> typeSystemRow = typeSystem.findRow(headerName);
            Optional<ClassificationTypeSystemRow> classificationTypeSystemRow = classificationTypeSystem.findRow(headerName);
            if(typeSystemRow.isPresent())
            {
                standardAttributes.add(typeSystemRow.get());
                continue;
            }
            if(classificationTypeSystemRow.isPresent())
            {
                classificationAttributes.add(classificationTypeSystemRow.get());
                continue;
            }
            unknownAttributes.add(headerName);
        }
        Objects.requireNonNull(validationResults);
        validateColumnUniqueness(typeCode, sheet).ifPresent(validationResults::add);
        Objects.requireNonNull(validationResults);
        validateThatColumnsExistAndUserHasPermission(typeCode, standardAttributes, unknownAttributes).ifPresent(validationResults::add);
        if(classificationTypeSystem.exists())
        {
            Objects.requireNonNull(validationResults);
            validateClassificationAttributes(typeCode, classificationAttributes).ifPresent(validationResults::add);
        }
        return validationResults;
    }


    protected Optional<ExcelValidationResult> validateColumnUniqueness(String typeCode, Sheet sheet)
    {
        List<ValidationMessage> messages = new ArrayList<>();
        Collection<String> attributeNames = this.excelHeaderService.getHeaderDisplayNames(sheet);
        Set<String> duplicates = findDuplicates(attributeNames);
        duplicates.forEach(attributeName -> messages.add(new ValidationMessage("excel.import.validation.workbook.attribute.duplicated.description", new Serializable[] {attributeName})));
        if(messages.isEmpty())
        {
            return Optional.empty();
        }
        ExcelValidationResult validationResult = new ExcelValidationResult(messages);
        validationResult.setHeader(new ValidationMessage("excel.import.validation.workbook.attribute.duplicated.header", new Serializable[] {typeCode}));
        return Optional.of(validationResult);
    }


    protected Optional<ExcelValidationResult> validateThatColumnsExistAndUserHasPermission(String typeCode, List<TypeSystemRow> standardAttributes, List<String> unknownAttributes)
    {
        List<ValidationMessage> messages = new ArrayList<>();
        messages.addAll(createValidationMessagesForUnknownAttributes(typeCode, unknownAttributes));
        Set<TypeSystemRow> attributesWithoutDuplicates = new HashSet<>(standardAttributes);
        Objects.requireNonNull(messages);
        attributesWithoutDuplicates.stream().map(attribute -> validateSingleAttribute(typeCode, attribute.getAttrName(), attribute.getAttrQualifier())).filter(Optional::isPresent).map(Optional::get).forEach(messages::add);
        if(messages.isEmpty())
        {
            return Optional.empty();
        }
        ExcelValidationResult validationResult = new ExcelValidationResult(messages);
        validationResult.setHeader(new ValidationMessage("excel.import.validation.workbook.attribute.unknown.header", new Serializable[] {typeCode}));
        return Optional.of(validationResult);
    }


    protected List<ValidationMessage> createValidationMessagesForUnknownAttributes(String typeCode, List<String> attributes)
    {
        List<ValidationMessage> messages = new ArrayList<>();
        for(String unknownColumnName : attributes)
        {
            messages.add(new ValidationMessage("excel.import.validation.workbook.attribute.unknown.type", new Serializable[] {typeCode, unknownColumnName}));
        }
        return messages;
    }


    protected Optional<ExcelValidationResult> validateClassificationAttributes(String typeCode, List<ClassificationTypeSystemRow> classificationAttributes)
    {
        List<ValidationMessage> messages = new ArrayList<>();
        messages.addAll(validateClassificationSystemVersionsExistAndUserHasPermissions(classificationAttributes));
        if(CollectionUtils.isNotEmpty(classificationAttributes))
        {
            messages.addAll(validatePermissionsToTypes());
        }
        if(messages.isEmpty())
        {
            return Optional.empty();
        }
        ExcelValidationResult validationResult = new ExcelValidationResult(messages);
        validationResult.setHeader(new ValidationMessage("excel.import.validation.workbook.classification.header", new Serializable[] {typeCode}));
        return Optional.of(validationResult);
    }


    protected List<ValidationMessage> validateClassificationSystemVersionsExistAndUserHasPermissions(List<ClassificationTypeSystemRow> classificationAttributes)
    {
        List<ValidationMessage> messages = new ArrayList<>();
        Set<Pair<String, String>> classificationSystemVersions = extractUniqueClassificationSystemVersions(classificationAttributes);
        for(Pair<String, String> version : classificationSystemVersions)
        {
            String classificationSystem = (String)version.getLeft();
            String classificationVersion = (String)version.getRight();
            try
            {
                getCatalogVersionService().getCatalogVersion(classificationSystem, classificationVersion);
            }
            catch(UnknownIdentifierException ex)
            {
                messages
                                .add(new ValidationMessage("excel.import.validation.workbook.classification.unknown.system.version", new Serializable[] {classificationSystem, classificationVersion}));
            }
        }
        return messages;
    }


    private static Set<Pair<String, String>> extractUniqueClassificationSystemVersions(List<ClassificationTypeSystemRow> classificationAttributes)
    {
        return (Set<Pair<String, String>>)classificationAttributes.stream()
                        .map(attr -> new ImmutablePair(attr.getClassificationSystem(), attr.getClassificationVersion()))
                        .collect(Collectors.toSet());
    }


    protected List<ValidationMessage> validatePermissionsToTypes()
    {
        List<ValidationMessage> messages = new ArrayList<>();
        List<String> readWriteTypes = Lists.newArrayList((Object[])new String[] {"ProductFeature"});
        readWriteTypes.forEach(type -> {
            if(!getPermissionCRUDService().canReadType(type) || !getPermissionCRUDService().canChangeType(type) || !getPermissionCRUDService().canCreateTypeInstance(type))
            {
                messages.add(new ValidationMessage("excel.import.validation.workbook.insufficient.permissions.to.type", new Serializable[] {type}));
            }
        });
        return messages;
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


    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    @Deprecated(since = "1905", forRemoval = true)
    public UserService getUserService()
    {
        return this.userService;
    }


    @Deprecated(since = "1905", forRemoval = true)
    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setExcelAttributeTypeSystemService(ExcelTypeSystemService<ExcelAttributeTypeSystemService.ExcelTypeSystem> excelAttributeTypeSystemService)
    {
        this.excelAttributeTypeSystemService = excelAttributeTypeSystemService;
    }


    @Required
    public void setExcelClassificationTypeSystemService(ExcelTypeSystemService<ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem> excelClassificationTypeSystemService)
    {
        this.excelClassificationTypeSystemService = excelClassificationTypeSystemService;
    }
}
