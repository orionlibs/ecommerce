package de.hybris.platform.cockpit.util;

import java.net.URL;
import java.util.Locale;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class ConfigurableCockpitLabelLocator extends CockpitLocator
{
    private String resourceFolder = null;


    public URL locate(Locale locale) throws Exception
    {
        if(StringUtils.isBlank(this.resourceFolder))
        {
            return null;
        }
        return super.locate(locale);
    }


    @Required
    public void setResourceFolder(String resourceFolder)
    {
        this.resourceFolder = resourceFolder;
    }


    protected String getResourceFolder()
    {
        return this.resourceFolder;
    }
}
