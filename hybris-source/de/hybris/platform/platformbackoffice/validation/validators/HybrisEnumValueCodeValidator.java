package de.hybris.platform.platformbackoffice.validation.validators;

import de.hybris.platform.core.HybrisEnumValue;
import de.hybris.platform.platformbackoffice.validation.annotations.HybrisEnumValueCode;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class HybrisEnumValueCodeValidator implements ConstraintValidator<HybrisEnumValueCode, HybrisEnumValue>
{
    private HybrisEnumValueCode hybrisEnumValueCode;


    public void initialize(HybrisEnumValueCode hybrisEnumValueCode)
    {
        this.hybrisEnumValueCode = hybrisEnumValueCode;
    }


    public boolean isValid(HybrisEnumValue value, ConstraintValidatorContext constraintValidatorContext)
    {
        return this.hybrisEnumValueCode.value().equals(value.getCode());
    }
}
