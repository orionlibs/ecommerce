package com.hybris.backoffice.excel.importing;

import com.google.common.base.Preconditions;
import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.data.ExcelColumn;
import com.hybris.backoffice.excel.data.ExcelWorkbook;
import com.hybris.backoffice.excel.data.ExcelWorksheet;
import com.hybris.backoffice.excel.data.Impex;
import com.hybris.backoffice.excel.data.ImpexForType;
import com.hybris.backoffice.excel.data.ImpexHeaderValue;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.importing.parser.DefaultValues;
import com.hybris.backoffice.excel.importing.parser.ExcelParserException;
import com.hybris.backoffice.excel.importing.parser.ParsedValues;
import com.hybris.backoffice.excel.importing.parser.ParserRegistry;
import com.hybris.backoffice.excel.template.DisplayNameAttributeNameFormatter;
import com.hybris.backoffice.excel.template.ExcelTemplateService;
import com.hybris.backoffice.excel.template.cell.ExcelCellService;
import com.hybris.backoffice.excel.template.header.ExcelHeaderService;
import com.hybris.backoffice.excel.template.populator.DefaultExcelAttributeContext;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.template.workbook.ExcelWorkbookService;
import com.hybris.backoffice.excel.translators.ExcelTranslatorRegistry;
import com.hybris.backoffice.excel.translators.ExcelValueTranslator;
import com.hybris.backoffice.excel.validators.WorkbookValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import com.hybris.backoffice.excel.validators.engine.ExcelValidationEngineAwareValidator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultExcelImportService implements ExcelImportService
{
    @Deprecated(since = "1808", forRemoval = true)
    public static final String MULTIVALUE_SEPARATOR = ",";
    public static final int FIRST_DATA_ROW_INDEX = 3;
    @Deprecated(since = "1808", forRemoval = true)
    private ExcelTemplateService excelTemplateService;
    private ExcelTranslatorRegistry excelTranslatorRegistry;
    private List<WorkbookValidator> workbookValidators;
    private ExcelValidationEngineAwareValidator excelValidationEngineAwareValidator;
    private ParserRegistry parserRegistry;
    private ExcelWorkbookService excelWorkbookService;
    private ExcelSheetService excelSheetService;
    private ExcelCellService excelCellService;
    private ExcelHeaderService excelHeaderService;
    private DisplayNameAttributeNameFormatter displayNameAttributeNameFormatter;
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultExcelImportService.class);


    public Impex convertToImpex(Workbook workbook)
    {
        Sheet typeSystemSheet = getExcelWorkbookService().getMetaInformationSheet(workbook);
        Impex workbookImpex = new Impex();
        for(Sheet typeSheet : getExcelSheetService().getSheets(workbook))
        {
            Impex sheetImpex = generateImpexForSheet(typeSystemSheet, typeSheet);
            workbookImpex.mergeImpex(sheetImpex);
        }
        return workbookImpex;
    }


    public List<ExcelValidationResult> validate(Workbook workbook, Set<String> mediaContentEntries)
    {
        return validate(workbook, mediaContentEntries, new HashMap<>());
    }


    public List<ExcelValidationResult> validate(Workbook workbook, Set<String> mediaContentEntries, Map<String, Object> context)
    {
        List<ExcelValidationResult> validationWorkbookResults = new ArrayList<>();
        Objects.requireNonNull(validationWorkbookResults);
        validateExcelFileOrigin(workbook).ifPresent(validationWorkbookResults::add);
        if(validationWorkbookResults.isEmpty())
        {
            validationWorkbookResults.addAll(validateWorkbook(workbook));
        }
        if(validationWorkbookResults.isEmpty())
        {
            ExcelWorkbook excelWorkbook = populate(workbook);
            if(mediaContentEntries != null)
            {
                context.put("mediaContentEntries", mediaContentEntries);
            }
            context.put(ExcelWorkbook.class.getCanonicalName(), excelWorkbook);
            excelWorkbook.forEachWorksheet(excelWorksheet -> {
                List<ExcelValidationResult> validationWorksheetResults = validate(excelWorksheet, context);
                validationWorkbookResults.addAll(validationWorksheetResults);
            });
        }
        Collections.sort(validationWorkbookResults);
        return validationWorkbookResults;
    }


    public List<ExcelValidationResult> validate(Workbook workbook)
    {
        return validate(workbook, Collections.emptySet());
    }


    protected Optional<ExcelValidationResult> validateExcelFileOrigin(Workbook workbook)
    {
        if(workbook instanceof XSSFWorkbook)
        {
            POIXMLProperties properties = ((XSSFWorkbook)workbook).getProperties();
            if(!properties.getCustomProperties().contains("SAP Hybris vendor v3"))
            {
                ExcelValidationResult validationResult = new ExcelValidationResult(new ValidationMessage("excel.import.validation.incorrect.file.description"));
                validationResult.setHeader(new ValidationMessage("excel.import.validation.incorrect.file.header"));
                validationResult.setWorkbookValidationResult(true);
                return Optional.of(validationResult);
            }
        }
        return Optional.empty();
    }


    protected List<ExcelValidationResult> validateWorkbook(Workbook workbook)
    {
        Set<ExcelValidationResult> allUniqueValidationResults = new LinkedHashSet<>();
        for(WorkbookValidator workbookValidator : getWorkbookValidators())
        {
            List<ExcelValidationResult> validationResults = workbookValidator.validate(workbook);
            if(!validationResults.isEmpty())
            {
                allUniqueValidationResults.addAll((Collection<? extends ExcelValidationResult>)validationResults.stream()
                                .filter(result -> CollectionUtils.isNotEmpty(result.getValidationErrors()))
                                .peek(result -> result.setWorkbookValidationResult(true))
                                .collect(Collectors.toSet()));
            }
        }
        return new LinkedList<>(allUniqueValidationResults);
    }


    protected ExcelWorkbook populate(Workbook workbook)
    {
        Sheet typeSystemSheet = getExcelWorkbookService().getMetaInformationSheet(workbook);
        ExcelWorkbook excelWorkbook = new ExcelWorkbook(new ExcelWorksheet[0]);
        for(Sheet typeSheet : getExcelSheetService().getSheets(workbook))
        {
            ExcelWorksheet excelWorksheet = populate(typeSystemSheet, typeSheet);
            excelWorkbook.add(excelWorksheet);
        }
        return excelWorkbook;
    }


    protected ExcelWorksheet populate(Sheet typeSystemSheet, Sheet typeSheet)
    {
        String typeCode = getExcelSheetService().findTypeCodeForSheetName(typeSheet.getWorkbook(), typeSheet.getSheetName());
        ExcelWorksheet excelWorksheet = new ExcelWorksheet(typeCode);
        Impex mainImpex = new Impex();
        List<String> entriesRef = generateDocumentRefs(Integer.valueOf(typeSheet.getLastRowNum() - 3));
        Collection<SelectedAttribute> selectedAttributes = getExcelHeaderService().getHeaders(typeSystemSheet, typeSheet);
        insertDocumentReferences(mainImpex, typeCode, entriesRef);
        for(SelectedAttribute selectedAttribute : selectedAttributes)
        {
            int columnIndex = getExcelSheetService().findColumnIndex(typeSystemSheet, typeSheet, (ExcelAttribute)new ExcelAttributeDescriptorAttribute(selectedAttribute
                            .getAttributeDescriptor(), selectedAttribute.getIsoCode()));
            if(columnIndex != -1)
            {
                populateRows(typeSheet, excelWorksheet, entriesRef, selectedAttribute, columnIndex);
            }
        }
        return excelWorksheet;
    }


    protected void populateRows(Sheet typeSheet, ExcelWorksheet excelWorksheet, List<String> entriesRef, SelectedAttribute selectedAttribute, int columnIndex)
    {
        ExcelColumn excelColumn = new ExcelColumn(selectedAttribute, Integer.valueOf(columnIndex));
        for(int rowIndex = 3; rowIndex <= typeSheet.getLastRowNum(); rowIndex++)
        {
            if(hasRowCorrectData(typeSheet.getRow(rowIndex)))
            {
                String cellValue = getExcelCellService().getCellValue(typeSheet.getRow(rowIndex).getCell(columnIndex));
                ImportParameters importParameters = findImportParameters(selectedAttribute, cellValue, excelWorksheet
                                .getSheetName(), entriesRef.get(rowIndex - 3));
                excelWorksheet.add(rowIndex + 1, excelColumn, importParameters);
            }
        }
    }


    protected boolean hasRowCorrectData(Row row)
    {
        if(row == null)
        {
            return false;
        }
        return findFirstNotBlankCell(row);
    }


    protected boolean findFirstNotBlankCell(Row row)
    {
        for(int i = 0; i < row.getLastCellNum(); i++)
        {
            if(StringUtils.isNotBlank(getExcelCellService().getCellValue(row.getCell(i))))
            {
                return true;
            }
        }
        return false;
    }


    protected List<ExcelValidationResult> validate(ExcelWorksheet excelWorksheet, Map<String, Object> context)
    {
        Map<Integer, ExcelValidationResult> rowsValidation = new HashMap<>();
        excelWorksheet.forEachColumn(excelColumn -> {
            SelectedAttribute attribute = excelColumn.getSelectedAttribute();
            String attributeDisplayName = getAttributeDisplayName(attribute);
            excelWorksheet.forEachRow(excelColumn, ());
        });
        return new ArrayList<>(rowsValidation.values());
    }


    private Optional<ExcelValidationResult> validateUsingValidationEngine(SelectedAttribute selectedAttribute, ImportParameters importParameters, CellValidationMetaData metaData)
    {
        ExcelValidationResult singleResult = this.excelValidationEngineAwareValidator.validate((ExcelAttribute)new ExcelAttributeDescriptorAttribute(selectedAttribute.getAttributeDescriptor()), importParameters);
        if(singleResult.hasErrors())
        {
            insertValidationHeaders(metaData, singleResult);
            return Optional.of(singleResult);
        }
        return Optional.empty();
    }


    protected void mergeWithRowValidation(Map<Integer, ExcelValidationResult> rowsValidation, Integer rowIndex, ExcelValidationResult cellValidation)
    {
        ExcelValidationResult excelValidationResult = rowsValidation.get(rowIndex);
        if(excelValidationResult != null)
        {
            Objects.requireNonNull(excelValidationResult);
            cellValidation.getValidationErrors().forEach(excelValidationResult::addValidationError);
        }
        else
        {
            rowsValidation.put(rowIndex, cellValidation);
        }
    }


    protected String getAttributeDisplayName(SelectedAttribute attribute)
    {
        return getDisplayNameAttributeNameFormatter().format(
                        DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)new ExcelAttributeDescriptorAttribute(attribute.getAttributeDescriptor(), attribute.getIsoCode())));
    }


    protected Optional<ExcelValidationResult> validateCell(ExcelValueTranslator translator, CellValidationMetaData metaData)
    {
        ExcelValidationResult singleResult = translator.validate(metaData.getImportParameters(), metaData
                        .getSelectedAttribute().getAttributeDescriptor(), metaData.getContext());
        if(singleResult.hasErrors())
        {
            return Optional.of(insertValidationHeaders(metaData, singleResult));
        }
        return Optional.empty();
    }


    private ExcelValidationResult insertValidationHeaders(CellValidationMetaData metaData, ExcelValidationResult singleResult)
    {
        singleResult.getValidationErrors().forEach(validationError -> populateHeaderMetadata(validationError, metaData));
        singleResult.setHeader(prepareValidationHeader(metaData));
        return singleResult;
    }


    protected ValidationMessage prepareValidationHeader(CellValidationMetaData metaData)
    {
        ValidationMessage header = new ValidationMessage("excel.import.validation.header.title", new Serializable[] {metaData.getImportParameters().getTypeCode(), metaData.getRowIndex()});
        populateHeaderMetadata(header, metaData);
        return header;
    }


    protected void populateHeaderMetadata(ValidationMessage header, CellValidationMetaData metaData)
    {
        header.addMetadataIfAbsent("rowIndex", metaData.getRowIndex());
        header.addMetadataIfAbsent("sheetName", metaData
                        .getImportParameters().getTypeCode());
        header.addMetadataIfAbsent("selectedAttribute", metaData
                        .getSelectedAttribute());
        header.addMetadataIfAbsent("selectedAttributeDisplayedName", metaData
                        .getSelectedAttributeDisplayedName());
    }


    protected Impex generateImpexForSheet(Sheet typeSystemSheet, Sheet typeSheet)
    {
        Impex mainImpex = new Impex();
        String typeCode = getExcelSheetService().findTypeCodeForSheetName(typeSheet.getWorkbook(), typeSheet.getSheetName());
        List<String> entriesRef = generateDocumentRefs(Integer.valueOf(typeSheet.getLastRowNum() - 3));
        Collection<SelectedAttribute> selectedAttributes = getExcelHeaderService().getHeaders(typeSystemSheet, typeSheet);
        insertDocumentReferences(mainImpex, typeCode, entriesRef);
        for(SelectedAttribute selectedAttribute : selectedAttributes)
        {
            int columnIndex = this.excelSheetService.findColumnIndex(typeSystemSheet, typeSheet, (ExcelAttribute)new ExcelAttributeDescriptorAttribute(selectedAttribute
                            .getAttributeDescriptor(), selectedAttribute.getIsoCode()));
            if(columnIndex < 0)
            {
                continue;
            }
            for(int rowIndex = 3; rowIndex <= typeSheet.getLastRowNum(); rowIndex++)
            {
                if(!hasRowCorrectData(typeSheet.getRow(rowIndex)))
                {
                    mainImpex.findUpdates(typeCode).getImpexTable().rowKeySet().remove(Integer.valueOf(rowIndex));
                }
                else
                {
                    String cellValue = getExcelCellService().getCellValue(typeSheet.getRow(rowIndex).getCell(columnIndex));
                    ImportParameters importParameters = findImportParameters(selectedAttribute, cellValue, typeCode, entriesRef
                                    .get(rowIndex - 3));
                    Optional<ExcelValueTranslator<Object>> translator = this.excelTranslatorRegistry.getTranslator(selectedAttribute.getAttributeDescriptor());
                    if(translator.isPresent())
                    {
                        Impex impex = ((ExcelValueTranslator)translator.get()).importData(selectedAttribute.getAttributeDescriptor(), importParameters);
                        mainImpex.mergeImpex(impex, typeCode, Integer.valueOf(rowIndex));
                    }
                }
            }
        }
        return mainImpex;
    }


    protected void insertDocumentReferences(Impex mainImpex, String typeCode, List<String> entriesRef)
    {
        ImpexForType impexForType = mainImpex.findUpdates(typeCode);
        ImpexHeaderValue referenceHeader = (new ImpexHeaderValue.Builder("&ExcelImportRef")).build();
        for(int rowIndex = 0; rowIndex < entriesRef.size(); rowIndex++)
        {
            impexForType.putValue(Integer.valueOf(3 + rowIndex), referenceHeader, entriesRef.get(rowIndex));
        }
    }


    protected List<String> generateDocumentRefs(Integer rowsCount)
    {
        List<String> refs = new ArrayList<>();
        for(int i = 0; i <= rowsCount.intValue(); i++)
        {
            refs.add(UUID.randomUUID().toString());
        }
        return refs;
    }


    protected ImportParameters findImportParameters(SelectedAttribute selectedAttribute, String cellValue, String typeCode, String entryRef)
    {
        Preconditions.checkNotNull(cellValue, "Cell's value cannot be null");
        String referenceFormat = selectedAttribute.getReferenceFormat();
        try
        {
            DefaultValues defaultValues = getParserRegistry().getParser(referenceFormat).parseDefaultValues(referenceFormat, selectedAttribute
                            .getDefaultValues());
            ParsedValues parsedValues = getParserRegistry().getParser(referenceFormat).parseValue(cellValue, defaultValues);
            return new ImportParameters(typeCode, selectedAttribute.getIsoCode(), parsedValues.getCellValue(), entryRef, parsedValues
                            .getParameters());
        }
        catch(ExcelParserException e)
        {
            LOGGER.debug(String.format("%s is in incorrect format. Correct format is: %s", new Object[] {e.getCellValue(), e.getExpectedFormat()}), (Throwable)e);
            return new ImportParameters(typeCode, selectedAttribute.getIsoCode(), e.getCellValue(), entryRef, e.getExpectedFormat());
        }
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


    public ExcelTranslatorRegistry getExcelTranslatorRegistry()
    {
        return this.excelTranslatorRegistry;
    }


    @Required
    public void setExcelTranslatorRegistry(ExcelTranslatorRegistry excelTranslatorRegistry)
    {
        this.excelTranslatorRegistry = excelTranslatorRegistry;
    }


    public List<WorkbookValidator> getWorkbookValidators()
    {
        return this.workbookValidators;
    }


    @Required
    public void setWorkbookValidators(List<WorkbookValidator> workbookValidators)
    {
        this.workbookValidators = workbookValidators;
    }


    public ExcelValidationEngineAwareValidator getExcelValidationEngineAwareValidator()
    {
        return this.excelValidationEngineAwareValidator;
    }


    @Required
    public void setExcelValidationEngineAwareValidator(ExcelValidationEngineAwareValidator excelValidationEngineAwareValidator)
    {
        this.excelValidationEngineAwareValidator = excelValidationEngineAwareValidator;
    }


    public ParserRegistry getParserRegistry()
    {
        return this.parserRegistry;
    }


    @Required
    public void setParserRegistry(ParserRegistry parserRegistry)
    {
        this.parserRegistry = parserRegistry;
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


    public ExcelCellService getExcelCellService()
    {
        return this.excelCellService;
    }


    @Required
    public void setExcelCellService(ExcelCellService excelCellService)
    {
        this.excelCellService = excelCellService;
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


    public DisplayNameAttributeNameFormatter getDisplayNameAttributeNameFormatter()
    {
        return this.displayNameAttributeNameFormatter;
    }


    @Required
    public void setDisplayNameAttributeNameFormatter(DisplayNameAttributeNameFormatter displayNameAttributeNameFormatter)
    {
        this.displayNameAttributeNameFormatter = displayNameAttributeNameFormatter;
    }
}
