package de.hybris.platform.hac.data.validator;

import de.hybris.platform.hac.data.dto.TenantData;
import java.util.regex.Pattern;
import org.apache.commons.validator.GenericValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TenantDataValidator implements Validator
{
    private final Pattern tablePrefixPattern = Pattern.compile("^[A-Za-z]+[A-Za-z0-9_]*$");
    private final Pattern tenanIdPattern = Pattern.compile("^[A-Za-z]+[A-Za-z0-9_]*$");


    public boolean supports(Class<?> clazz)
    {
        return TenantData.class.isAssignableFrom(clazz);
    }


    public void validate(Object target, Errors errors)
    {
        TenantData tenantData = (TenantData)target;
        if(GenericValidator.isBlankOrNull(tenantData.getTablePrefix()))
        {
            errors.rejectValue("tablePrefix", "tablePrefix", "Table prefix is required");
        }
        if(!"{tenantID}".equals(tenantData.getTablePrefix()))
        {
            if(tenantData.getTablePrefix().length() > 6)
            {
                errors.rejectValue("tablePrefix", "tablePrefix", "Table prefix must be maximum of 5 characters length");
            }
            if(!this.tablePrefixPattern.matcher(tenantData.getTablePrefix()).matches())
            {
                errors.rejectValue("tablePrefix", "tablePrefix", "Table prefix contains invalid characters");
            }
        }
        if(GenericValidator.isBlankOrNull(tenantData.getTenantID()))
        {
            errors.rejectValue("tenantID", "tenantID", "Tenant ID is required");
        }
        if(tenantData.getTenantID().length() > 24)
        {
            errors.rejectValue("tenantID", "tenantID", "Tenant ID must be maximum of 24 characters length");
        }
        if(!this.tenanIdPattern.matcher(tenantData.getTenantID()).matches())
        {
            errors.rejectValue("tenantID", "tenantID", "Tenant ID contains invalid characters");
        }
    }
}
