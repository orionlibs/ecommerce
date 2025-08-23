package de.hybris.platform.store;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.storelocator.jalo.PointOfService;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedBaseStore extends GenericItem
{
    public static final String UID = "uid";
    public static final String NAME = "name";
    public static final String STORELOCATORDISTANCEUNIT = "storelocatorDistanceUnit";
    public static final String CMSSITES = "cmsSites";
    protected static String STORESFORCMSSITE_SRC_ORDERED = "relation.StoresForCMSSite.source.ordered";
    protected static String STORESFORCMSSITE_TGT_ORDERED = "relation.StoresForCMSSite.target.ordered";
    protected static String STORESFORCMSSITE_MARKMODIFIED = "relation.StoresForCMSSite.markmodified";
    public static final String CATALOGS = "catalogs";
    protected static String CATALOGSFORBASESTORES_SRC_ORDERED = "relation.CatalogsForBaseStores.source.ordered";
    protected static String CATALOGSFORBASESTORES_TGT_ORDERED = "relation.CatalogsForBaseStores.target.ordered";
    protected static String CATALOGSFORBASESTORES_MARKMODIFIED = "relation.CatalogsForBaseStores.markmodified";
    public static final String POINTSOFSERVICE = "pointsOfService";
    protected static final OneToManyHandler<PointOfService> POINTSOFSERVICEHANDLER = new OneToManyHandler(GeneratedBasecommerceConstants.TC.POINTOFSERVICE, false, "baseStore", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("uid", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("storelocatorDistanceUnit", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<Catalog> getCatalogs(SessionContext ctx)
    {
        List<Catalog> items = getLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.CATALOGSFORBASESTORES, "Catalog", null,
                        Utilities.getRelationOrderingOverride(CATALOGSFORBASESTORES_SRC_ORDERED, true), false);
        return items;
    }


    public List<Catalog> getCatalogs()
    {
        return getCatalogs(getSession().getSessionContext());
    }


    public long getCatalogsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedBasecommerceConstants.Relations.CATALOGSFORBASESTORES, "Catalog", null);
    }


    public long getCatalogsCount()
    {
        return getCatalogsCount(getSession().getSessionContext());
    }


    public void setCatalogs(SessionContext ctx, List<Catalog> value)
    {
        setLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.CATALOGSFORBASESTORES, null, value,
                        Utilities.getRelationOrderingOverride(CATALOGSFORBASESTORES_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORBASESTORES_MARKMODIFIED));
    }


    public void setCatalogs(List<Catalog> value)
    {
        setCatalogs(getSession().getSessionContext(), value);
    }


    public void addToCatalogs(SessionContext ctx, Catalog value)
    {
        addLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.CATALOGSFORBASESTORES, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATALOGSFORBASESTORES_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORBASESTORES_MARKMODIFIED));
    }


    public void addToCatalogs(Catalog value)
    {
        addToCatalogs(getSession().getSessionContext(), value);
    }


    public void removeFromCatalogs(SessionContext ctx, Catalog value)
    {
        removeLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.CATALOGSFORBASESTORES, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATALOGSFORBASESTORES_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATALOGSFORBASESTORES_MARKMODIFIED));
    }


    public void removeFromCatalogs(Catalog value)
    {
        removeFromCatalogs(getSession().getSessionContext(), value);
    }


    public Collection<BaseSite> getCmsSites(SessionContext ctx)
    {
        List<BaseSite> items = getLinkedItems(ctx, false, GeneratedBasecommerceConstants.Relations.STORESFORCMSSITE, "BaseSite", null,
                        Utilities.getRelationOrderingOverride(STORESFORCMSSITE_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<BaseSite> getCmsSites()
    {
        return getCmsSites(getSession().getSessionContext());
    }


    public long getCmsSitesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedBasecommerceConstants.Relations.STORESFORCMSSITE, "BaseSite", null);
    }


    public long getCmsSitesCount()
    {
        return getCmsSitesCount(getSession().getSessionContext());
    }


    public void setCmsSites(SessionContext ctx, Collection<BaseSite> value)
    {
        setLinkedItems(ctx, false, GeneratedBasecommerceConstants.Relations.STORESFORCMSSITE, null, value,
                        Utilities.getRelationOrderingOverride(STORESFORCMSSITE_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(STORESFORCMSSITE_MARKMODIFIED));
    }


    public void setCmsSites(Collection<BaseSite> value)
    {
        setCmsSites(getSession().getSessionContext(), value);
    }


    public void addToCmsSites(SessionContext ctx, BaseSite value)
    {
        addLinkedItems(ctx, false, GeneratedBasecommerceConstants.Relations.STORESFORCMSSITE, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(STORESFORCMSSITE_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(STORESFORCMSSITE_MARKMODIFIED));
    }


    public void addToCmsSites(BaseSite value)
    {
        addToCmsSites(getSession().getSessionContext(), value);
    }


    public void removeFromCmsSites(SessionContext ctx, BaseSite value)
    {
        removeLinkedItems(ctx, false, GeneratedBasecommerceConstants.Relations.STORESFORCMSSITE, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(STORESFORCMSSITE_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(STORESFORCMSSITE_MARKMODIFIED));
    }


    public void removeFromCmsSites(BaseSite value)
    {
        removeFromCmsSites(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("BaseSite");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(STORESFORCMSSITE_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("Catalog");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CATALOGSFORBASESTORES_MARKMODIFIED);
        }
        return true;
    }


    public String getName(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedBaseStore.getName requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public Map<Language, String> getAllName(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "name", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllName()
    {
        return getAllName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedBaseStore.setName requires a session language", 0);
        }
        setLocalizedProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public void setAllName(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "name", value);
    }


    public void setAllName(Map<Language, String> value)
    {
        setAllName(getSession().getSessionContext(), value);
    }


    public List<PointOfService> getPointsOfService(SessionContext ctx)
    {
        return (List<PointOfService>)POINTSOFSERVICEHANDLER.getValues(ctx, (Item)this);
    }


    public List<PointOfService> getPointsOfService()
    {
        return getPointsOfService(getSession().getSessionContext());
    }


    public void setPointsOfService(SessionContext ctx, List<PointOfService> value)
    {
        POINTSOFSERVICEHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setPointsOfService(List<PointOfService> value)
    {
        setPointsOfService(getSession().getSessionContext(), value);
    }


    public void addToPointsOfService(SessionContext ctx, PointOfService value)
    {
        POINTSOFSERVICEHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToPointsOfService(PointOfService value)
    {
        addToPointsOfService(getSession().getSessionContext(), value);
    }


    public void removeFromPointsOfService(SessionContext ctx, PointOfService value)
    {
        POINTSOFSERVICEHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromPointsOfService(PointOfService value)
    {
        removeFromPointsOfService(getSession().getSessionContext(), value);
    }


    public EnumerationValue getStorelocatorDistanceUnit(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "storelocatorDistanceUnit");
    }


    public EnumerationValue getStorelocatorDistanceUnit()
    {
        return getStorelocatorDistanceUnit(getSession().getSessionContext());
    }


    public void setStorelocatorDistanceUnit(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "storelocatorDistanceUnit", value);
    }


    public void setStorelocatorDistanceUnit(EnumerationValue value)
    {
        setStorelocatorDistanceUnit(getSession().getSessionContext(), value);
    }


    public String getUid(SessionContext ctx)
    {
        return (String)getProperty(ctx, "uid");
    }


    public String getUid()
    {
        return getUid(getSession().getSessionContext());
    }


    public void setUid(SessionContext ctx, String value)
    {
        setProperty(ctx, "uid", value);
    }


    public void setUid(String value)
    {
        setUid(getSession().getSessionContext(), value);
    }
}
