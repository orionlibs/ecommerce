package de.hybris.platform.storelocator.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaContainer;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.store.BaseStore;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPointOfService extends GenericItem
{
    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String DESCRIPTION = "description";
    public static final String TYPE = "type";
    public static final String MAPICON = "mapIcon";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String GEOCODETIMESTAMP = "geocodeTimestamp";
    public static final String OPENINGSCHEDULE = "openingSchedule";
    public static final String STORECONTENT = "storeContent";
    public static final String STOREIMAGE = "storeImage";
    public static final String BUSINESSCATEGORY = "businessCategory";
    public static final String BASESTORE = "baseStore";
    protected static final BidirectionalOneToManyHandler<GeneratedPointOfService> BASESTOREHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.POINTOFSERVICE, false, "baseStore", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("address", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("type", Item.AttributeMode.INITIAL);
        tmp.put("mapIcon", Item.AttributeMode.INITIAL);
        tmp.put("latitude", Item.AttributeMode.INITIAL);
        tmp.put("longitude", Item.AttributeMode.INITIAL);
        tmp.put("geocodeTimestamp", Item.AttributeMode.INITIAL);
        tmp.put("openingSchedule", Item.AttributeMode.INITIAL);
        tmp.put("storeContent", Item.AttributeMode.INITIAL);
        tmp.put("storeImage", Item.AttributeMode.INITIAL);
        tmp.put("businessCategory", Item.AttributeMode.INITIAL);
        tmp.put("baseStore", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Address getAddress(SessionContext ctx)
    {
        return (Address)getProperty(ctx, "address");
    }


    public Address getAddress()
    {
        return getAddress(getSession().getSessionContext());
    }


    public void setAddress(SessionContext ctx, Address value)
    {
        setProperty(ctx, "address", value);
    }


    public void setAddress(Address value)
    {
        setAddress(getSession().getSessionContext(), value);
    }


    public BaseStore getBaseStore(SessionContext ctx)
    {
        return (BaseStore)getProperty(ctx, "baseStore");
    }


    public BaseStore getBaseStore()
    {
        return getBaseStore(getSession().getSessionContext());
    }


    public void setBaseStore(SessionContext ctx, BaseStore value)
    {
        BASESTOREHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setBaseStore(BaseStore value)
    {
        setBaseStore(getSession().getSessionContext(), value);
    }


    public String getBusinessCategory(SessionContext ctx)
    {
        return (String)getProperty(ctx, "businessCategory");
    }


    public String getBusinessCategory()
    {
        return getBusinessCategory(getSession().getSessionContext());
    }


    public void setBusinessCategory(SessionContext ctx, String value)
    {
        setProperty(ctx, "businessCategory", value);
    }


    public void setBusinessCategory(String value)
    {
        setBusinessCategory(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        BASESTOREHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
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


    public Date getGeocodeTimestamp(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "geocodeTimestamp");
    }


    public Date getGeocodeTimestamp()
    {
        return getGeocodeTimestamp(getSession().getSessionContext());
    }


    public void setGeocodeTimestamp(SessionContext ctx, Date value)
    {
        setProperty(ctx, "geocodeTimestamp", value);
    }


    public void setGeocodeTimestamp(Date value)
    {
        setGeocodeTimestamp(getSession().getSessionContext(), value);
    }


    public Double getLatitude(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "latitude");
    }


    public Double getLatitude()
    {
        return getLatitude(getSession().getSessionContext());
    }


    public double getLatitudeAsPrimitive(SessionContext ctx)
    {
        Double value = getLatitude(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getLatitudeAsPrimitive()
    {
        return getLatitudeAsPrimitive(getSession().getSessionContext());
    }


    public void setLatitude(SessionContext ctx, Double value)
    {
        setProperty(ctx, "latitude", value);
    }


    public void setLatitude(Double value)
    {
        setLatitude(getSession().getSessionContext(), value);
    }


    public void setLatitude(SessionContext ctx, double value)
    {
        setLatitude(ctx, Double.valueOf(value));
    }


    public void setLatitude(double value)
    {
        setLatitude(getSession().getSessionContext(), value);
    }


    public Double getLongitude(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "longitude");
    }


    public Double getLongitude()
    {
        return getLongitude(getSession().getSessionContext());
    }


    public double getLongitudeAsPrimitive(SessionContext ctx)
    {
        Double value = getLongitude(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getLongitudeAsPrimitive()
    {
        return getLongitudeAsPrimitive(getSession().getSessionContext());
    }


    public void setLongitude(SessionContext ctx, Double value)
    {
        setProperty(ctx, "longitude", value);
    }


    public void setLongitude(Double value)
    {
        setLongitude(getSession().getSessionContext(), value);
    }


    public void setLongitude(SessionContext ctx, double value)
    {
        setLongitude(ctx, Double.valueOf(value));
    }


    public void setLongitude(double value)
    {
        setLongitude(getSession().getSessionContext(), value);
    }


    public Media getMapIcon(SessionContext ctx)
    {
        return (Media)getProperty(ctx, "mapIcon");
    }


    public Media getMapIcon()
    {
        return getMapIcon(getSession().getSessionContext());
    }


    public void setMapIcon(SessionContext ctx, Media value)
    {
        setProperty(ctx, "mapIcon", value);
    }


    public void setMapIcon(Media value)
    {
        setMapIcon(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    protected void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'name' is not changeable", 0);
        }
        setProperty(ctx, "name", value);
    }


    protected void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public OpeningSchedule getOpeningSchedule(SessionContext ctx)
    {
        return (OpeningSchedule)getProperty(ctx, "openingSchedule");
    }


    public OpeningSchedule getOpeningSchedule()
    {
        return getOpeningSchedule(getSession().getSessionContext());
    }


    public void setOpeningSchedule(SessionContext ctx, OpeningSchedule value)
    {
        setProperty(ctx, "openingSchedule", value);
    }


    public void setOpeningSchedule(OpeningSchedule value)
    {
        setOpeningSchedule(getSession().getSessionContext(), value);
    }


    public String getStoreContent(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedPointOfService.getStoreContent requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "storeContent");
    }


    public String getStoreContent()
    {
        return getStoreContent(getSession().getSessionContext());
    }


    public Map<Language, String> getAllStoreContent(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "storeContent", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllStoreContent()
    {
        return getAllStoreContent(getSession().getSessionContext());
    }


    public void setStoreContent(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedPointOfService.setStoreContent requires a session language", 0);
        }
        setLocalizedProperty(ctx, "storeContent", value);
    }


    public void setStoreContent(String value)
    {
        setStoreContent(getSession().getSessionContext(), value);
    }


    public void setAllStoreContent(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "storeContent", value);
    }


    public void setAllStoreContent(Map<Language, String> value)
    {
        setAllStoreContent(getSession().getSessionContext(), value);
    }


    public MediaContainer getStoreImage(SessionContext ctx)
    {
        return (MediaContainer)getProperty(ctx, "storeImage");
    }


    public MediaContainer getStoreImage()
    {
        return getStoreImage(getSession().getSessionContext());
    }


    public void setStoreImage(SessionContext ctx, MediaContainer value)
    {
        setProperty(ctx, "storeImage", value);
    }


    public void setStoreImage(MediaContainer value)
    {
        setStoreImage(getSession().getSessionContext(), value);
    }


    public EnumerationValue getType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "type");
    }


    public EnumerationValue getType()
    {
        return getType(getSession().getSessionContext());
    }


    public void setType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "type", value);
    }


    public void setType(EnumerationValue value)
    {
        setType(getSession().getSessionContext(), value);
    }
}
