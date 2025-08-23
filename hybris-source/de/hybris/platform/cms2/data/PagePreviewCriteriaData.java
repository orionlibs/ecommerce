package de.hybris.platform.cms2.data;

import java.io.Serializable;

public class PagePreviewCriteriaData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String versionUid;


    public void setVersionUid(String versionUid)
    {
        this.versionUid = versionUid;
    }


    public String getVersionUid()
    {
        return this.versionUid;
    }
}
