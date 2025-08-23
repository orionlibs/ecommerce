package de.hybris.platform.cmsfacades.data;

import java.util.Map;

@Deprecated(since = "6.6", forRemoval = true)
public class EmailPageData extends AbstractPageData
{
    private Map<String, String> fromEmail;
    private Map<String, String> fromName;


    public void setFromEmail(Map<String, String> fromEmail)
    {
        this.fromEmail = fromEmail;
    }


    public Map<String, String> getFromEmail()
    {
        return this.fromEmail;
    }


    public void setFromName(Map<String, String> fromName)
    {
        this.fromName = fromName;
    }


    public Map<String, String> getFromName()
    {
        return this.fromName;
    }
}
