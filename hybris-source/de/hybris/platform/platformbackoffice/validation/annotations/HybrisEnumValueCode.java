package de.hybris.platform.platformbackoffice.validation.annotations;

import de.hybris.platform.platformbackoffice.validation.validators.HybrisEnumValueCodeValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {HybrisEnumValueCodeValidator.class})
@Documented
public @interface HybrisEnumValueCode
{
    String message() default "{de.hybris.platform.platformbackoffice.validation.annotations.HybrisEnumValueCode.message}";


    Class<?>[] groups() default {};


    String value();


    Class<? extends Payload>[] payload() default {};
}
