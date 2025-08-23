package com.hybris.cis.api.validation;

import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

public class XSSSafeConstraintValidator implements ConstraintValidator<XSSSafe, String>
{
    public void initialize(XSSSafe constraintAnnotation)
    {
    }


    public boolean isValid(String input, ConstraintValidatorContext constraintValidatorContext)
    {
        if(StringUtils.isEmpty(input))
        {
            return true;
        }
        if(!input.equals(input.replace("", "")))
        {
            return false;
        }
        if(matches(input, "<script>(.*?)</script>", 2))
        {
            return false;
        }
        if(matches(input, "src[\r\n]*=[\r\n]*\\'(.*?)\\'", 42))
        {
            return false;
        }
        if(matches(input, "src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", 42))
        {
            return false;
        }
        if(matches(input, "<script(.*?)>", 42))
        {
            return false;
        }
        if(matches(input, "eval\\((.*?)\\)", 42))
        {
            return false;
        }
        if(matches(input, "expression\\((.*?)\\)", 42))
        {
            return false;
        }
        if(matches(input, "javascript:", 2))
        {
            return false;
        }
        if(matches(input, "vbscript:", 2))
        {
            return false;
        }
        if(matches(input, "onload(.*?)=", 42))
        {
            return false;
        }
        return true;
    }


    protected boolean matches(String input, String regex, int flags)
    {
        return Pattern.compile(regex, flags).matcher(input).find();
    }
}
