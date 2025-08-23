package de.hybris.platform.adaptivesearchbackoffice.widgets.searchresultbrowser;

import de.hybris.platform.adaptivesearch.data.AsDocumentData;
import de.hybris.platform.core.PK;
import java.io.Serializable;

public class DocumentModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    private int index;
    private Float score;
    private PK pk;
    private AsDocumentData document;
    private boolean promoted;
    private String promotedItemUid;
    private boolean highlight;
    private boolean showOnTop;
    private boolean fromSearchProfile;
    private boolean fromSearchConfiguration;
    private boolean override;
    private boolean overrideFromSearchProfile;
    private String styleClass;


    public int getIndex()
    {
        return this.index;
    }


    public void setIndex(int index)
    {
        this.index = index;
    }


    public Float getScore()
    {
        return this.score;
    }


    public void setScore(Float score)
    {
        this.score = score;
    }


    public PK getPk()
    {
        return this.pk;
    }


    public void setPk(PK pk)
    {
        this.pk = pk;
    }


    public AsDocumentData getDocument()
    {
        return this.document;
    }


    public void setDocument(AsDocumentData document)
    {
        this.document = document;
    }


    public String getPromotedItemUid()
    {
        return this.promotedItemUid;
    }


    public void setPromotedItemUid(String promotedItemUid)
    {
        this.promotedItemUid = promotedItemUid;
    }


    public boolean isPromoted()
    {
        return this.promoted;
    }


    public void setPromoted(boolean promoted)
    {
        this.promoted = promoted;
    }


    public boolean isFromSearchProfile()
    {
        return this.fromSearchProfile;
    }


    public void setFromSearchProfile(boolean fromSearchProfile)
    {
        this.fromSearchProfile = fromSearchProfile;
    }


    public boolean isFromSearchConfiguration()
    {
        return this.fromSearchConfiguration;
    }


    public void setFromSearchConfiguration(boolean fromSearchConfiguration)
    {
        this.fromSearchConfiguration = fromSearchConfiguration;
    }


    public boolean isOverride()
    {
        return this.override;
    }


    public void setOverride(boolean override)
    {
        this.override = override;
    }


    public boolean isOverrideFromSearchProfile()
    {
        return this.overrideFromSearchProfile;
    }


    public void setOverrideFromSearchProfile(boolean overrideFromSearchProfile)
    {
        this.overrideFromSearchProfile = overrideFromSearchProfile;
    }


    public String getStyleClass()
    {
        return this.styleClass;
    }


    public void setStyleClass(String styleClass)
    {
        this.styleClass = styleClass;
    }


    public boolean isShowOnTop()
    {
        return this.showOnTop;
    }


    public void setShowOnTop(boolean showOnTop)
    {
        this.showOnTop = showOnTop;
    }


    public boolean isHighlight()
    {
        return this.highlight;
    }


    public void setHighlight(boolean highlight)
    {
        this.highlight = highlight;
    }
}
