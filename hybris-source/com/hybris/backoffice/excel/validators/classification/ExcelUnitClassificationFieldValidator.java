package com.hybris.backoffice.excel.validators.classification;

import com.hybris.backoffice.excel.data.ExcelAttribute;
import com.hybris.backoffice.excel.data.ExcelClassificationAttribute;
import com.hybris.backoffice.excel.data.ImportParameters;
import com.hybris.backoffice.excel.validators.ExcelAttributeValidator;
import com.hybris.backoffice.excel.validators.data.ExcelValidationResult;
import com.hybris.backoffice.excel.validators.data.ValidationMessage;
import de.hybris.platform.catalog.model.classification.ClassificationAttributeUnitModel;
import de.hybris.platform.catalog.model.classification.ClassificationSystemVersionModel;
import de.hybris.platform.classification.ClassificationSystemService;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Required;

public class ExcelUnitClassificationFieldValidator implements ExcelAttributeValidator<ExcelClassificationAttribute>
{
    public static final String INVALID_UNIT_MESSAGE_KEY = "excel.import.validation.unit.invalid";
    private static final String CACHE_KEY_PATTERN = "PossibleUnitsOf:%s:%s:%s";
    private ClassificationSystemService classificationSystemService;
    private List<ExcelAttributeValidator<ExcelClassificationAttribute>> validators = Collections.emptyList();


    private static String createUnitsCacheKey(ClassificationSystemVersionModel systemVersion, String unitType)
    {
        return String.format("PossibleUnitsOf:%s:%s:%s", new Object[] {systemVersion.getCatalog().getId(), systemVersion.getVersion(), unitType});
    }


    public boolean canHandle(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters)
    {
        return (ExcelValidatorUtils.hasUnit(importParameters) && !ExcelValidatorUtils.isMultivalue(importParameters) &&
                        ExcelValidatorUtils.isNotRange(importParameters));
    }


    public ExcelValidationResult validate(@Nonnull ExcelClassificationAttribute excelAttribute, @Nonnull ImportParameters importParameters, @Nonnull Map<String, Object> context)
    {
        LinkedList<ValidationMessage> validationErrors = new LinkedList<>();
        List<String> allPossibleUnits = getAllPossibleUnitsForType(context, excelAttribute);
        Map<String, String> singleValueParams = importParameters.getSingleValueParameters();
        String unit = ExcelUnitUtils.extractUnitFromParams(singleValueParams);
        String value = ExcelUnitUtils.extractValueFromParams(singleValueParams);
        ImportParameters importParametersForValue = ExcelUnitUtils.getImportParametersForValue(importParameters, value);
        List<ValidationMessage> validationMessages = executeValidators(excelAttribute, importParametersForValue, context);
        validationErrors.addAll(validationMessages);
        if(StringUtils.isNoneBlank(new CharSequence[] {value, unit}) && !allPossibleUnits.contains(unit))
        {
            validationErrors.add(new ValidationMessage("excel.import.validation.unit.invalid", new Serializable[] {unit, excelAttribute
                            .getAttributeAssignment().getUnit().getUnitType()}));
        }
        return new ExcelValidationResult(validationErrors);
    }


    private List<String> getAllPossibleUnitsForType(Map<String, Object> context, ExcelClassificationAttribute excelAttribute)
    {
        ClassificationSystemVersionModel systemVersion = excelAttribute.getAttributeAssignment().getSystemVersion();
        ClassificationAttributeUnitModel unit = excelAttribute.getAttributeAssignment().getUnit();
        String cacheKey = createUnitsCacheKey(systemVersion, unit.getUnitType());
        if(!context.containsKey(cacheKey))
        {
            List<String> units = loadUnitsForTypeOf(systemVersion, unit);
            context.put(cacheKey, units);
            return units;
        }
        return (List<String>)context.get(cacheKey);
    }


    private List<String> loadUnitsForTypeOf(ClassificationSystemVersionModel systemVersion, ClassificationAttributeUnitModel unit)
    {
        if(StringUtils.isNotBlank(unit.getUnitType()))
        {
            return (List<String>)this.classificationSystemService.getUnitsOfTypeForSystemVersion(systemVersion, unit.getUnitType())
                            .stream()
                            .map(ClassificationAttributeUnitModel::getCode)
                            .collect(Collectors.toList());
        }
        return Lists.newArrayList((Object[])new String[] {unit.getCode()});
    }


    private List<ValidationMessage> executeValidators(ExcelClassificationAttribute excelAttribute, ImportParameters importParameters, Map<String, Object> context)
    {
        return (List<ValidationMessage>)this.validators.stream()
                        .filter(validator -> validator.canHandle((ExcelAttribute)excelAttribute, importParameters))
                        .map(validator -> validator.validate((ExcelAttribute)excelAttribute, importParameters, context))
                        .map(ExcelValidationResult::getValidationErrors)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
    }


    @Required
    public void setClassificationSystemService(ClassificationSystemService classificationSystemService)
    {
        this.classificationSystemService = classificationSystemService;
    }


    public void setValidators(List<ExcelAttributeValidator<ExcelClassificationAttribute>> validators)
    {
        this.validators = validators;
    }
}
