package de.hybris.platform.acceleratorcms.data;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.preview.PreviewDataModel;
import de.hybris.platform.core.model.user.UserModel;
import java.io.Serializable;
import java.util.Map;

public class CmsPageRequestContextData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private AbstractPageModel page;
    private UserModel user;
    private PreviewDataModel previewData;
    private String sessionId;
    private boolean preview;
    private boolean liveEdit;
    private Map<String, Object> positionToSlot;
    private Object restrictionData;
    private AbstractCMSComponentModel parentComponent;


    public void setPage(AbstractPageModel page)
    {
        this.page = page;
    }


    public AbstractPageModel getPage()
    {
        return this.page;
    }


    public void setUser(UserModel user)
    {
        this.user = user;
    }


    public UserModel getUser()
    {
        return this.user;
    }


    public void setPreviewData(PreviewDataModel previewData)
    {
        this.previewData = previewData;
    }


    public PreviewDataModel getPreviewData()
    {
        return this.previewData;
    }


    public void setSessionId(String sessionId)
    {
        this.sessionId = sessionId;
    }


    public String getSessionId()
    {
        return this.sessionId;
    }


    public void setPreview(boolean preview)
    {
        this.preview = preview;
    }


    public boolean isPreview()
    {
        return this.preview;
    }


    @Deprecated(since = "1811", forRemoval = true)
    public void setLiveEdit(boolean liveEdit)
    {
        this.liveEdit = liveEdit;
    }


    @Deprecated(since = "1811", forRemoval = true)
    public boolean isLiveEdit()
    {
        return this.liveEdit;
    }


    public void setPositionToSlot(Map<String, Object> positionToSlot)
    {
        this.positionToSlot = positionToSlot;
    }


    public Map<String, Object> getPositionToSlot()
    {
        return this.positionToSlot;
    }


    public void setRestrictionData(Object restrictionData)
    {
        this.restrictionData = restrictionData;
    }


    public Object getRestrictionData()
    {
        return this.restrictionData;
    }


    public void setParentComponent(AbstractCMSComponentModel parentComponent)
    {
        this.parentComponent = parentComponent;
    }


    public AbstractCMSComponentModel getParentComponent()
    {
        return this.parentComponent;
    }
}
