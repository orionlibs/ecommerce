package de.hybris.platform.validation.validators;

import de.hybris.platform.validation.annotations.RegExp;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class RegExpValidator implements ConstraintValidator<RegExp, String>
{
    private static final Logger LOG = Logger.getLogger(RegExpValidator.class);
    private boolean notEmpty;


    public void initialize(RegExp constraintAnnotation)
    {
        this.notEmpty = constraintAnnotation.notEmpty();
    }


    public boolean isValid(String regexp, ConstraintValidatorContext context)
    {
        if(StringUtils.isBlank(regexp))
        {
            return !this.notEmpty;
        }
        return isRegularExpression(regexp);
    }


    private static boolean isRegularExpression(String value)
    {
        try
        {
            Pattern.compile(value);
        }
        catch(PatternSyntaxException e)
        {
            LOG.debug("Regular expression is invalid.", e);
            return false;
        }
        return true;
    }
}
