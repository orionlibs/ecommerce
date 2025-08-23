package com.hybris.backoffice.excel.validators;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelAttributeDescriptorAttribute;
import com.hybris.backoffice.excel.data.SelectedAttribute;
import com.hybris.backoffice.excel.template.DisplayNameAttributeNameFormatter;
import com.hybris.backoffice.excel.template.ExcelTemplateService;
import com.hybris.backoffice.excel.template.mapper.ExcelMapper;
import com.hybris.backoffice.excel.template.populator.DefaultExcelAttributeContext;
import com.hybris.backoffice.excel.template.sheet.ExcelSheetService;
import com.hybris.backoffice.excel.template.workbook.ExcelWorkbookService;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class WorkbookMandatoryColumnsValidator implements WorkbookValidator
{
    public static final String VALIDATION_MESSAGE_HEADER = "excel.import.validation.workbook.mandatory.column.header";
    public static final String VALIDATION_MESSAGE_DESCRIPTION = "excel.import.validation.workbook.mandatory.column.description";
    private static final Logger LOG = LoggerFactory.getLogger(WorkbookMandatoryColumnsValidator.class);
    @Deprecated(since = "1808", forRemoval = true)
    private ExcelTemplateService excelTemplateService;
    private ExcelWorkbookService excelWorkbookService;
    private ExcelSheetService excelSheetService;
    private TypeService typeService;
    private CommonI18NService commonI18NService;
    private DisplayNameAttributeNameFormatter displayNameAttributeNameFormatter;
    private ExcelMapper<String, AttributeDescriptorModel> mapper;


    public List<ExcelValidationResult> validate(Workbook workbook)
    {
        List<ExcelValidationResult> validationResults = new ArrayList<>();
        Sheet typeSystemSheet = getExcelWorkbookService().getMetaInformationSheet(workbook);
        Objects.requireNonNull(validationResults);
        getExcelSheetService().getSheets(workbook).stream().map(sheet -> validateSheet(typeSystemSheet, sheet)).filter(Optional::isPresent).map(Optional::get).forEach(validationResults::add);
        return validationResults;
    }


    protected Optional<ExcelValidationResult> validateSheet(Sheet typeSystemSheet, Sheet sheet)
    {
        List<ValidationMessage> messages = new ArrayList<>();
        String typeCode = getExcelSheetService().findTypeCodeForSheetName(sheet.getWorkbook(), sheet.getSheetName());
        Set<AttributeDescriptorModel> mandatoryFields = findAllMandatoryFields(typeCode);
        for(AttributeDescriptorModel mandatoryField : mandatoryFields)
        {
            SelectedAttribute selectedAttribute = prepareSelectedAttribute(mandatoryField);
            int columnIndex = getExcelSheetService().findColumnIndex(typeSystemSheet, sheet, (ExcelAttribute)
                            prepareExcelAttribute(selectedAttribute.getAttributeDescriptor(), selectedAttribute.getIsoCode()));
            if(columnIndex == -1)
            {
                messages.add(new ValidationMessage("excel.import.validation.workbook.mandatory.column.description", new Serializable[] {getAttributeDisplayedName(mandatoryField), sheet
                                .getSheetName()}));
            }
        }
        if(messages.isEmpty())
        {
            return Optional.empty();
        }
        ValidationMessage header = new ValidationMessage("excel.import.validation.workbook.mandatory.column.header", new Serializable[] {sheet.getSheetName()});
        return Optional.of(new ExcelValidationResult(header, messages));
    }


    protected ExcelAttributeDescriptorAttribute prepareExcelAttribute(AttributeDescriptorModel attributeDescriptor, String isoCode)
    {
        return new ExcelAttributeDescriptorAttribute(attributeDescriptor, isoCode);
    }


    protected SelectedAttribute prepareSelectedAttribute(AttributeDescriptorModel attributeDescriptor)
    {
        String isoCode = BooleanUtils.isTrue(attributeDescriptor.getLocalized()) ? getCommonI18NService().getCurrentLanguage().getIsocode() : "";
        return new SelectedAttribute(isoCode, attributeDescriptor);
    }


    protected String getAttributeDisplayedName(AttributeDescriptorModel attributeDescriptor)
    {
        String isoCode = BooleanUtils.isTrue(attributeDescriptor.getLocalized()) ? getCommonI18NService().getCurrentLanguage().getIsocode() : "";
        return getDisplayNameAttributeNameFormatter()
                        .format(DefaultExcelAttributeContext.ofExcelAttribute((ExcelAttribute)prepareExcelAttribute(attributeDescriptor, isoCode)));
    }


    public Set<AttributeDescriptorModel> findAllMandatoryFields(String typeCode)
    {
        try
        {
            return new HashSet<>((Collection<? extends AttributeDescriptorModel>)this.mapper.apply(typeCode));
        }
        catch(UnknownIdentifierException ex)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Error occurred while finding all mandatory attributes", (Throwable)ex);
            }
            return new HashSet<>();
        }
    }


    @Deprecated(since = "1808", forRemoval = true)
    protected boolean hasNotDefaultValue(AttributeDescriptorModel attributeDescriptorModel)
    {
        return (attributeDescriptorModel.getDefaultValue() == null);
    }


    @Deprecated(since = "1808", forRemoval = true)
    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Deprecated(since = "1808", forRemoval = true)
    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
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


    public DisplayNameAttributeNameFormatter getDisplayNameAttributeNameFormatter()
    {
        return this.displayNameAttributeNameFormatter;
    }


    @Required
    public void setDisplayNameAttributeNameFormatter(DisplayNameAttributeNameFormatter displayNameAttributeNameFormatter)
    {
        this.displayNameAttributeNameFormatter = displayNameAttributeNameFormatter;
    }


    public ExcelMapper<String, AttributeDescriptorModel> getMapper()
    {
        return this.mapper;
    }


    @Required
    public void setMapper(ExcelMapper<String, AttributeDescriptorModel> mapper)
    {
        this.mapper = mapper;
    }
}
