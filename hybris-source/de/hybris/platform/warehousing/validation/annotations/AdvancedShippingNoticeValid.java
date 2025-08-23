package de.hybris.platform.warehousing.validation.annotations;

import de.hybris.platform.warehousing.validation.validators.AdvancedShippingNoticeValidator;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {AdvancedShippingNoticeValidator.class})
@Documented
public @interface AdvancedShippingNoticeValid
{
    String message() default "{de.hybris.platform.warehousing.validation.annotations.AdvancedShippingNoticeValid.message}";


    Class<?>[] groups() default {};


    Class<? extends Payload>[] payload() default {};
}
