package de.hybris.platform.cmsfacades.data;

public class PageTypeData extends ComposedTypeData
{
    private String type;


    @Deprecated(since = "1811", forRemoval = true)
    public void setType(String type)
    {
        this.type = type;
    }


    @Deprecated(since = "1811", forRemoval = true)
    public String getType()
    {
        return this.type;
    }
}
