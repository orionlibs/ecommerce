package de.hybris.platform.personalizationpromotionsweb.validator;

import de.hybris.platform.personalizationpromotionsweb.data.CxPromotionActionData;
import de.hybris.platform.personalizationwebservices.validator.ActionValidator;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

public class PromotionActionValidator implements ActionValidator
{
    public boolean supports(Class<?> clazz)
    {
        return CxPromotionActionData.class.isAssignableFrom(clazz);
    }


    public void validate(Object target, Errors errors)
    {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "promotionId", "field.required");
    }
}
