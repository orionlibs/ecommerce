package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.List;

public class ClonePageData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private AbstractPageData pageData;
    private boolean cloneComponents;
    private List<String> restrictions;


    public void setPageData(AbstractPageData pageData)
    {
        this.pageData = pageData;
    }


    public AbstractPageData getPageData()
    {
        return this.pageData;
    }


    public void setCloneComponents(boolean cloneComponents)
    {
        this.cloneComponents = cloneComponents;
    }


    public boolean isCloneComponents()
    {
        return this.cloneComponents;
    }


    public void setRestrictions(List<String> restrictions)
    {
        this.restrictions = restrictions;
    }


    public List<String> getRestrictions()
    {
        return this.restrictions;
    }
}
