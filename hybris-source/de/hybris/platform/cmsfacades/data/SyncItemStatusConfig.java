package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public class SyncItemStatusConfig implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Integer maxDepth;


    public void setMaxDepth(Integer maxDepth)
    {
        this.maxDepth = maxDepth;
    }


    public Integer getMaxDepth()
    {
        return this.maxDepth;
    }
}
