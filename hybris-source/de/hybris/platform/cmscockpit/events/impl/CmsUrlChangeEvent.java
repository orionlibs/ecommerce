package de.hybris.platform.cmscockpit.events.impl;

import de.hybris.platform.cmscockpit.session.impl.FrontendAttributes;
import de.hybris.platform.cockpit.events.impl.AbstractCockpitEvent;

public class CmsUrlChangeEvent extends AbstractCockpitEvent
{
    private final String url;
    private final String relatedPagePk;
    private final String frontendUserUid;
    private final String jaloSessionUid;
    private FrontendAttributes addOnFrontEndAttributes;


    public CmsUrlChangeEvent(Object source, String url, String relatedPagePk, String frontendUserPk, String jaloSessionUid)
    {
        super(source);
        this.url = url;
        this.relatedPagePk = relatedPagePk;
        this.frontendUserUid = frontendUserPk;
        this.jaloSessionUid = jaloSessionUid;
    }


    public CmsUrlChangeEvent(Object source, String url, String relatedPagePk, String frontendUserPk, String jaloSessionUid, FrontendAttributes addOnFrontEndAttributes)
    {
        this(source, url, relatedPagePk, frontendUserPk, jaloSessionUid);
        this.addOnFrontEndAttributes = addOnFrontEndAttributes;
    }


    public String getJaloSessionUid()
    {
        return this.jaloSessionUid;
    }


    public String getUrl()
    {
        return (this.url == null) ? "" : this.url;
    }


    public String getRelatedPagePk()
    {
        return (this.relatedPagePk == null) ? "" : this.relatedPagePk;
    }


    public String getFrontendUserUid()
    {
        return (this.frontendUserUid == null) ? "" : this.frontendUserUid;
    }


    public void setExtendedFrontendAttributes(FrontendAttributes addOnFrontEndAttributes)
    {
        this.addOnFrontEndAttributes = addOnFrontEndAttributes;
    }


    public FrontendAttributes getExtendedFrontendAttributes()
    {
        return this.addOnFrontEndAttributes;
    }
}
