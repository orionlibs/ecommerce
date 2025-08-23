package de.hybris.platform.validation.annotations;

import de.hybris.platform.validation.validators.RegExpValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {RegExpValidator.class})
@Documented
public @interface RegExp
{
    String message() default "{de.hybris.platform.validation.annotations.RegExp.message}";


    Class<?>[] groups() default {};


    Class<? extends Payload>[] payload() default {};


    boolean notEmpty() default false;
}
