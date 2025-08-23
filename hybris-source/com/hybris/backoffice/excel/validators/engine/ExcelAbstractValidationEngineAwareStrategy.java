package com.hybris.backoffice.excel.validators.engine;

import com.hybris.backoffice.daos.BackofficeValidationDao;
import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.engine.converters.ExcelValueConverter;
import com.hybris.backoffice.excel.validators.engine.converters.ExcelValueConverterRegistry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.validation.enums.Severity;
import de.hybris.platform.validation.exceptions.HybrisConstraintViolation;
import de.hybris.platform.validation.model.constraints.ConstraintGroupModel;
import de.hybris.platform.validation.services.ValidationService;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class ExcelAbstractValidationEngineAwareStrategy implements ExcelValidationEngineAwareStrategy
{
    private static final Logger LOG = LoggerFactory.getLogger(ExcelAbstractValidationEngineAwareStrategy.class);
    private TypeService typeService;
    private ValidationService validationService;
    private BackofficeValidationDao validationDao;
    private ExcelValueConverterRegistry converterRegistry;
    private List<String> constraintGroups;
    private List<Severity> severities;


    protected Collection<HybrisConstraintViolation> validateValue(ImportParameters importParameters, ExcelAttribute excelAttribute)
    {
        Collection<ConstraintGroupModel> loadedGroups = getValidationDao().getConstraintGroups(getConstraintGroups());
        Class<ItemModel> modelClass = getTypeService().getModelClass(importParameters.getTypeCode());
        Object convertedValue = convertValue(excelAttribute, importParameters);
        try
        {
            return (Collection<HybrisConstraintViolation>)getValidationService().validateValue(modelClass, excelAttribute.getQualifier(), convertedValue, loadedGroups)
                            .stream().filter(violation -> getSeverities().contains(violation.getViolationSeverity()))
                            .collect(Collectors.toList());
        }
        catch(RuntimeException ex)
        {
            LOG.debug(String.format("Cannot validate field: '%s' of item: '%s' for value: '%s'", new Object[] {excelAttribute.getQualifier(), modelClass, convertedValue}), ex);
            return Collections.emptyList();
        }
    }


    protected Object convertValue(ExcelAttribute excelAttribute, ImportParameters importParameters)
    {
        Optional<ExcelValueConverter> foundConverter = getConverterRegistry().getConverter(excelAttribute, importParameters, new Class[0]);
        if(foundConverter.isPresent())
        {
            try
            {
                return ((ExcelValueConverter)foundConverter.get()).convert(excelAttribute, importParameters);
            }
            catch(RuntimeException ex)
            {
                LOG.debug(String.format("Cannot convert %s to %s", new Object[] {importParameters.getCellValue(), excelAttribute.getType()}), ex);
            }
        }
        return importParameters.getCellValue();
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


    public ValidationService getValidationService()
    {
        return this.validationService;
    }


    @Required
    public void setValidationService(ValidationService validationService)
    {
        this.validationService = validationService;
    }


    public BackofficeValidationDao getValidationDao()
    {
        return this.validationDao;
    }


    @Required
    public void setValidationDao(BackofficeValidationDao validationDao)
    {
        this.validationDao = validationDao;
    }


    public ExcelValueConverterRegistry getConverterRegistry()
    {
        return this.converterRegistry;
    }


    @Required
    public void setConverterRegistry(ExcelValueConverterRegistry converterRegistry)
    {
        this.converterRegistry = converterRegistry;
    }


    public List<String> getConstraintGroups()
    {
        return this.constraintGroups;
    }


    @Required
    public void setConstraintGroups(List<String> constraintGroups)
    {
        this.constraintGroups = constraintGroups;
    }


    public List<Severity> getSeverities()
    {
        return this.severities;
    }


    @Required
    public void setSeverities(List<Severity> severities)
    {
        this.severities = severities;
    }
}
