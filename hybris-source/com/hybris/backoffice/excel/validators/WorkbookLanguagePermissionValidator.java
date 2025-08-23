package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.importing.ExcelAttributeTypeSystemService;
import com.hybris.backoffice.excel.importing.ExcelClassificationTypeSystemService;
import com.hybris.backoffice.excel.importing.ExcelTypeSystemService;
import com.hybris.backoffice.excel.importing.data.ClassificationTypeSystemRow;
import com.hybris.backoffice.excel.template.header.ExcelHeaderService;
import com.hybris.backoffice.excel.template.populator.typesheet.TypeSystemRow;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.internal.jalo.ServicelayerManager;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Required;

public class WorkbookLanguagePermissionValidator implements WorkbookValidator
{
    public static final String EXCEL_IMPORT_VALIDATION_WORKBOOK_LANGUAGE_PERMISSION_HEADER = "excel.import.validation.workbook.insufficient.permissions.language.header";
    public static final String EXCEL_IMPORT_VALIDATION_WORKBOOK_LANGUAGE_PERMISSION_DETAIL = "excel.import.validation.workbook.insufficient.permissions.language.detail";
    private CommonI18NService commonI18NService;
    private ModelService modelService;
    private ExcelSheetService excelSheetService;
    private ExcelHeaderService excelHeaderService;
    private ExcelTypeSystemService<ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem> excelClassificationTypeSystemService;
    private ExcelTypeSystemService<ExcelAttributeTypeSystemService.ExcelTypeSystem> excelAttributeTypeSystemService;


    public List<ExcelValidationResult> validate(Workbook workbook)
    {
        List<ExcelValidationResult> validationResults = new ArrayList<>();
        List<ValidationMessage> messages = new ArrayList<>();
        Set<String> isoCodeSet = new HashSet<>();
        Objects.requireNonNull(isoCodeSet);
        getExcelSheetService().getSheets(workbook).stream().map(sheet -> extractIsoCodeFromSheet(sheet)).forEach(isoCodeSet::addAll);
        Objects.requireNonNull(messages);
        isoCodeSet.stream().filter(isoCode -> !hasPermissionsToLanguage(isoCode)).map(isoCode -> new ValidationMessage("excel.import.validation.workbook.insufficient.permissions.language.detail", new Serializable[] {isoCode})).forEach(messages::add);
        if(!messages.isEmpty())
        {
            ValidationMessage header = new ValidationMessage("excel.import.validation.workbook.insufficient.permissions.language.header");
            validationResults.add(new ExcelValidationResult(header, messages));
        }
        return validationResults;
    }


    private Set<String> extractIsoCodeFromSheet(Sheet sheet)
    {
        ExcelAttributeTypeSystemService.ExcelTypeSystem typeSystem = (ExcelAttributeTypeSystemService.ExcelTypeSystem)getExcelAttributeTypeSystemService().loadTypeSystem(sheet.getWorkbook());
        ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem classificationTypeSystem = (ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem)getExcelClassificationTypeSystemService().loadTypeSystem(sheet.getWorkbook());
        return (Set<String>)getExcelHeaderService().getHeaderDisplayNames(sheet).stream().map(headerName -> {
            Optional<TypeSystemRow> typeSystemRow = typeSystem.findRow(headerName);
            if(typeSystemRow.isPresent())
            {
                return ((TypeSystemRow)typeSystemRow.get()).getAttrLocLang();
            }
            Optional<ClassificationTypeSystemRow> classificationTypeSystemRow = classificationTypeSystem.findRow(headerName);
            return classificationTypeSystemRow.isPresent() ? ((ClassificationTypeSystemRow)classificationTypeSystemRow.get()).getIsoCode() : null;
        }).filter(isoCode -> !StringUtils.isBlank(isoCode)).collect(Collectors.toSet());
    }


    protected boolean hasPermissionsToLanguage(String language)
    {
        Locale local = getCommonI18NService().getLocaleForIsoCode(language);
        return (extractLocales(getServicelayerManager().getAllWritableLanguages()).contains(local) &&
                        extractLocales(getServicelayerManager().getAllReadableLanguages()).contains(local));
    }


    private Set<Locale> extractLocales(Set<Language> languages)
    {
        return (Set<Locale>)((Set)Optional.<Set>ofNullable(languages).orElse(new HashSet())).stream()
                        .map(language -> Optional.of((LanguageModel)getModelService().get(language))).filter(Optional::isPresent)
                        .map(Optional::get).map(languageModel -> getCommonI18NService().getLocaleForLanguage(languageModel))
                        .collect(Collectors.toSet());
    }


    protected ServicelayerManager getServicelayerManager()
    {
        return ServicelayerManager.getInstance();
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    private CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    private ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setExcelSheetService(ExcelSheetService excelSheetService)
    {
        this.excelSheetService = excelSheetService;
    }


    private ExcelSheetService getExcelSheetService()
    {
        return this.excelSheetService;
    }


    private ExcelHeaderService getExcelHeaderService()
    {
        return this.excelHeaderService;
    }


    @Required
    public void setExcelHeaderService(ExcelHeaderService excelHeaderService)
    {
        this.excelHeaderService = excelHeaderService;
    }


    @Required
    public void setExcelClassificationTypeSystemService(ExcelTypeSystemService<ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem> excelClassificationTypeSystemService)
    {
        this.excelClassificationTypeSystemService = excelClassificationTypeSystemService;
    }


    private ExcelTypeSystemService<ExcelClassificationTypeSystemService.ExcelClassificationTypeSystem> getExcelClassificationTypeSystemService()
    {
        return this.excelClassificationTypeSystemService;
    }


    @Required
    public void setExcelAttributeTypeSystemService(ExcelTypeSystemService<ExcelAttributeTypeSystemService.ExcelTypeSystem> excelAttributeTypeSystemService)
    {
        this.excelAttributeTypeSystemService = excelAttributeTypeSystemService;
    }


    private ExcelTypeSystemService<ExcelAttributeTypeSystemService.ExcelTypeSystem> getExcelAttributeTypeSystemService()
    {
        return this.excelAttributeTypeSystemService;
    }
}
