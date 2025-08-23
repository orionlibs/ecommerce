package de.hybris.platform.security.impl;

import com.sap.security.core.server.csi.XSSEncoder;
import de.hybris.platform.security.XssEncodeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.io.UnsupportedEncodingException;

public class DefaultXssEncodeService implements XssEncodeService
{
    public String encodeHtml(String input)
    {
        ServicesUtil.validateParameterNotNull(input, "Input html can't be null");
        try
        {
            return XSSEncoder.encodeHTML(input);
        }
        catch(UnsupportedEncodingException e)
        {
            throw new IllegalArgumentException("cannot encode input string due to unsupported encoding:" + e.getMessage(), e);
        }
    }
}
