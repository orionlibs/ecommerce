package de.hybris.platform.personalizationfacades.segmentation.impl;

import de.hybris.platform.personalizationfacades.segmentation.SegmentationHelper;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class DefaultSegmentationHelper implements SegmentationHelper
{
    public static final String DEFAULT_SEPARATOR = "~";
    protected static final String NULL_STRING = Base64.getUrlEncoder().encodeToString("null".getBytes(StandardCharsets.UTF_8));
    private final String separator;


    public DefaultSegmentationHelper()
    {
        this.separator = "~";
    }


    public DefaultSegmentationHelper(String seperator)
    {
        if(StringUtils.isEmpty(seperator))
        {
            throw new IllegalArgumentException("Seperator can't be empty.");
        }
        this.separator = seperator;
    }


    public String getSegmentationCode(String... parameters)
    {
        Assert.notNull(parameters, "Parameters can't be null");
        Assert.isTrue((parameters.length > 1), "Segment and customer identifier are required");
        String segmentId = parameters[0];
        String customerId = parameters[1];
        Assert.notNull(segmentId, "Segment identifier can't be null");
        Assert.notNull(customerId, "Customer id can't be null");
        String baseSite = (parameters.length > 2) ? parameters[2] : null;
        String profileId = (parameters.length > 3) ? parameters[3] : null;
        return encode(segmentId, customerId, baseSite, profileId);
    }


    protected String encode(String segmentId, String relatedObjectId, String baseSite, String provider)
    {
        StringBuilder result = new StringBuilder();
        result.append(Base64.getUrlEncoder().encodeToString(segmentId.getBytes(StandardCharsets.UTF_8)));
        result.append(this.separator);
        result.append(Base64.getUrlEncoder().encodeToString(relatedObjectId.getBytes(StandardCharsets.UTF_8)));
        result.append(this.separator);
        if(baseSite != null)
        {
            result.append(Base64.getUrlEncoder().encodeToString(baseSite.getBytes(StandardCharsets.UTF_8)));
        }
        else
        {
            result.append(NULL_STRING);
        }
        result.append(this.separator);
        if(provider != null)
        {
            result.append(Base64.getUrlEncoder().encodeToString(provider.getBytes(StandardCharsets.UTF_8)));
        }
        else
        {
            result.append(NULL_STRING);
        }
        return result.toString();
    }


    public String getCustomerSegmentationCode(CxUserToSegmentModel userSegment)
    {
        Assert.notNull(userSegment, "UserToSegment can't be null");
        Assert.notNull(userSegment.getUser(), "Customer can't be null");
        Assert.notNull(userSegment.getSegment(), "Segment can't be null");
        String baseSiteId = (userSegment.getBaseSite() == null) ? null : userSegment.getBaseSite().getUid();
        return getSegmentationCode(new String[] {userSegment.getSegment().getCode(), userSegment.getUser().getUid(), baseSiteId, userSegment
                        .getProvider()});
    }


    public String[] splitCustomerSegmentationCode(String segmentationId)
    {
        Assert.notNull(segmentationId, "Segmentation identifier can't be null");
        return decode(split(segmentationId));
    }


    protected String[] split(String code)
    {
        if(StringUtils.isBlank(code))
        {
            throw new IllegalArgumentException("code can't be empty");
        }
        if(!code.contains(this.separator))
        {
            throw new IllegalArgumentException("Invalid identifier format");
        }
        return code.split(this.separator);
    }


    protected String[] decode(String[] codes)
    {
        String[] decodedCodes = new String[codes.length];
        for(int i = 0; i < codes.length; i++)
        {
            if(NULL_STRING.equals(codes[i]))
            {
                decodedCodes[i] = null;
            }
            else
            {
                decodedCodes[i] = new String(Base64.getUrlDecoder().decode(codes[i]), StandardCharsets.UTF_8);
            }
        }
        return decodedCodes;
    }


    public String validateSegmentationCode(String code)
    {
        if(StringUtils.isBlank(code))
        {
            return "field.required";
        }
        if((code.split(this.separator)).length != 4)
        {
            return "field.invalid";
        }
        return null;
    }
}
