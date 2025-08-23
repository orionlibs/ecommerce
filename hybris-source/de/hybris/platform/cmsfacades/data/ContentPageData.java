package de.hybris.platform.cmsfacades.data;

@Deprecated(since = "6.6", forRemoval = true)
public class ContentPageData extends AbstractPageData
{
    private String label;
    private String path;


    public void setLabel(String label)
    {
        this.label = label;
    }


    public String getLabel()
    {
        return this.label;
    }


    public void setPath(String path)
    {
        this.path = path;
    }


    public String getPath()
    {
        return this.path;
    }
}
