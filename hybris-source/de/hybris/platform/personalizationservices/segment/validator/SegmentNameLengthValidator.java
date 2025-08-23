package de.hybris.platform.personalizationservices.segment.validator;

import de.hybris.platform.personalizationservices.data.BaseSegmentData;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SegmentNameLengthValidator implements Validator
{
    private static final String SEGMENT_CODE_EMPTY_OR_TOO_LONG = "error.segment.code.empty.or.too.long";
    private static final int DEFAULT_MAX_LENGTH = 100;
    private int maxLength = 100;


    public boolean supports(Class<?> clazz)
    {
        return BaseSegmentData.class.equals(clazz);
    }


    public void validate(Object target, Errors errors)
    {
        Assert.notNull(errors, "Errors object must not be null");
        BaseSegmentData segmentData = (BaseSegmentData)target;
        String code = segmentData.getCode();
        if(StringUtils.isBlank(code) || StringUtils.length(code) > this.maxLength)
        {
            errors.rejectValue("code", "error.segment.code.empty.or.too.long", (Object[])new String[] {String.valueOf(this.maxLength)}, "Segment code is required and must be between 1 and {0} characters long.");
        }
    }


    public int getMaxLength()
    {
        return this.maxLength;
    }


    public void setMaxLength(int maxLength)
    {
        this.maxLength = maxLength;
    }
}
