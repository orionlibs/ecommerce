package de.hybris.platform.ruleengineservices.validation.constraints;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ObjectPatternValidator implements ConstraintValidator<ObjectPattern, Object>
{
    private Pattern pattern;


    public void initialize(ObjectPattern parameters)
    {
        Pattern.Flag[] flags = parameters.flags();
        int intFlag = 0;
        for(Pattern.Flag flag : flags)
        {
            intFlag |= flag.getValue();
        }
        try
        {
            this.pattern = Pattern.compile(parameters.regexp(), intFlag);
        }
        catch(PatternSyntaxException e)
        {
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Invalid regular expression.", e);
            StackTraceElement[] st = illegalArgumentException.getStackTrace();
            illegalArgumentException.setStackTrace(Arrays.<StackTraceElement>copyOfRange(st, 1, st.length));
            throw illegalArgumentException;
        }
    }


    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext)
    {
        if(value == null)
        {
            return true;
        }
        Matcher m = this.pattern.matcher(value.toString());
        return m.matches();
    }
}
