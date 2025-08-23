package de.hybris.platform.ruleengineservices.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {ObjectPatternValidator.class})
public @interface ObjectPattern
{
    String regexp() default "";


    Pattern.Flag[] flags() default {Pattern.Flag.UNICODE_CASE};


    String message() default "{de.hybris.platform.ruleengineservices.de.hybris.platform.ruleengineservices.validation.constraints.ObjectPattern.message}";


    Class<?>[] groups() default {};


    Class<? extends Payload>[] payload() default {};
}
