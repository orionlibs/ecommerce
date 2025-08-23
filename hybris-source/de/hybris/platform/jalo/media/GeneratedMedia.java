package de.hybris.platform.jalo.media;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedMedia extends AbstractMedia
{
    public static final String CODE = "code";
    public static final String INTERNALURL = "internalURL";
    public static final String DESCRIPTION = "description";
    public static final String ALTTEXT = "altText";
    public static final String REMOVABLE = "removable";
    public static final String MEDIAFORMAT = "mediaFormat";
    public static final String FOLDER = "folder";
    public static final String SUBFOLDERPATH = "subFolderPath";
    public static final String MEDIACONTAINER = "mediaContainer";
    public static final String DERIVEDMEDIAS = "derivedMedias";
    protected static final BidirectionalOneToManyHandler<GeneratedMedia> MEDIACONTAINERHANDLER = new BidirectionalOneToManyHandler(GeneratedCoreConstants.TC.MEDIA, false, "mediaContainer", null, false, true, 0);
    protected static final OneToManyHandler<DerivedMedia> DERIVEDMEDIASHANDLER = new OneToManyHandler(GeneratedCoreConstants.TC.DERIVEDMEDIA, true, "media", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractMedia.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("code", Item.AttributeMode.INITIAL);
        tmp.put("internalURL", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("altText", Item.AttributeMode.INITIAL);
        tmp.put("removable", Item.AttributeMode.INITIAL);
        tmp.put("mediaFormat", Item.AttributeMode.INITIAL);
        tmp.put("folder", Item.AttributeMode.INITIAL);
        tmp.put("subFolderPath", Item.AttributeMode.INITIAL);
        tmp.put("mediaContainer", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getAltText(SessionContext ctx)
    {
        return (String)getProperty(ctx, "altText");
    }


    public String getAltText()
    {
        return getAltText(getSession().getSessionContext());
    }


    public void setAltText(SessionContext ctx, String value)
    {
        setProperty(ctx, "altText", value);
    }


    public void setAltText(String value)
    {
        setAltText(getSession().getSessionContext(), value);
    }


    public String getCode(SessionContext ctx)
    {
        return (String)getProperty(ctx, "code");
    }


    public String getCode()
    {
        return getCode(getSession().getSessionContext());
    }


    public void setCode(SessionContext ctx, String value)
    {
        setProperty(ctx, "code", value);
    }


    public void setCode(String value)
    {
        setCode(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        MEDIACONTAINERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Collection<DerivedMedia> getDerivedMedias(SessionContext ctx)
    {
        return DERIVEDMEDIASHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<DerivedMedia> getDerivedMedias()
    {
        return getDerivedMedias(getSession().getSessionContext());
    }


    public void setDerivedMedias(SessionContext ctx, Collection<DerivedMedia> value)
    {
        DERIVEDMEDIASHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setDerivedMedias(Collection<DerivedMedia> value)
    {
        setDerivedMedias(getSession().getSessionContext(), value);
    }


    public void addToDerivedMedias(SessionContext ctx, DerivedMedia value)
    {
        DERIVEDMEDIASHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToDerivedMedias(DerivedMedia value)
    {
        addToDerivedMedias(getSession().getSessionContext(), value);
    }


    public void removeFromDerivedMedias(SessionContext ctx, DerivedMedia value)
    {
        DERIVEDMEDIASHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromDerivedMedias(DerivedMedia value)
    {
        removeFromDerivedMedias(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        return (String)getProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        setProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public MediaFolder getFolder(SessionContext ctx)
    {
        return (MediaFolder)getProperty(ctx, "folder");
    }


    public MediaFolder getFolder()
    {
        return getFolder(getSession().getSessionContext());
    }


    public void setFolder(SessionContext ctx, MediaFolder value)
    {
        setProperty(ctx, "folder", value);
    }


    public void setFolder(MediaFolder value)
    {
        setFolder(getSession().getSessionContext(), value);
    }


    public String getInternalURL(SessionContext ctx)
    {
        return (String)getProperty(ctx, "internalURL");
    }


    public String getInternalURL()
    {
        return getInternalURL(getSession().getSessionContext());
    }


    public void setInternalURL(SessionContext ctx, String value)
    {
        setProperty(ctx, "internalURL", value);
    }


    public void setInternalURL(String value)
    {
        setInternalURL(getSession().getSessionContext(), value);
    }


    public MediaContainer getMediaContainer(SessionContext ctx)
    {
        return (MediaContainer)getProperty(ctx, "mediaContainer");
    }


    public MediaContainer getMediaContainer()
    {
        return getMediaContainer(getSession().getSessionContext());
    }


    public void setMediaContainer(SessionContext ctx, MediaContainer value)
    {
        MEDIACONTAINERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setMediaContainer(MediaContainer value)
    {
        setMediaContainer(getSession().getSessionContext(), value);
    }


    public MediaFormat getMediaFormat(SessionContext ctx)
    {
        return (MediaFormat)getProperty(ctx, "mediaFormat");
    }


    public MediaFormat getMediaFormat()
    {
        return getMediaFormat(getSession().getSessionContext());
    }


    public void setMediaFormat(SessionContext ctx, MediaFormat value)
    {
        setProperty(ctx, "mediaFormat", value);
    }


    public void setMediaFormat(MediaFormat value)
    {
        setMediaFormat(getSession().getSessionContext(), value);
    }


    public Boolean isRemovable(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "removable");
    }


    public Boolean isRemovable()
    {
        return isRemovable(getSession().getSessionContext());
    }


    public boolean isRemovableAsPrimitive(SessionContext ctx)
    {
        Boolean value = isRemovable(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isRemovableAsPrimitive()
    {
        return isRemovableAsPrimitive(getSession().getSessionContext());
    }


    public void setRemovable(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "removable", value);
    }


    public void setRemovable(Boolean value)
    {
        setRemovable(getSession().getSessionContext(), value);
    }


    public void setRemovable(SessionContext ctx, boolean value)
    {
        setRemovable(ctx, Boolean.valueOf(value));
    }


    public void setRemovable(boolean value)
    {
        setRemovable(getSession().getSessionContext(), value);
    }


    public String getSubFolderPath(SessionContext ctx)
    {
        return (String)getProperty(ctx, "subFolderPath");
    }


    public String getSubFolderPath()
    {
        return getSubFolderPath(getSession().getSessionContext());
    }


    public void setSubFolderPath(SessionContext ctx, String value)
    {
        setProperty(ctx, "subFolderPath", value);
    }


    public void setSubFolderPath(String value)
    {
        setSubFolderPath(getSession().getSessionContext(), value);
    }
}
