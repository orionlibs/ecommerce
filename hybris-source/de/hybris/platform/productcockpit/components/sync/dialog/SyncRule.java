package de.hybris.platform.productcockpit.components.sync.dialog;

@Deprecated
public class SyncRule
{
    private Object source;
    private String name;
    private String pk;


    SyncRule(String name, String pk, Object source)
    {
        this.name = name;
        this.pk = pk;
        this.source = source;
    }


    public Object getSource()
    {
        return this.source;
    }


    public void setSource(Object source)
    {
        this.source = source;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getPk()
    {
        return this.pk;
    }


    public void setPk(String pk)
    {
        this.pk = pk;
    }
}
