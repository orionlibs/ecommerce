package de.hybris.platform.cockpit.jalo;

import de.hybris.platform.cockpit.constants.GeneratedCockpitConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCockpitObjectAbstractCollection extends GenericItem
{
    public static final String QUALIFIER = "qualifier";
    public static final String LABEL = "label";
    public static final String DESCRIPTION = "description";
    public static final String USER = "user";
    public static final String READPRINCIPALS = "readPrincipals";
    protected static String READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_SRC_ORDERED = "relation.ReadPrincipal2CockpitObjectAbstractCollectionRelation.source.ordered";
    protected static String READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_TGT_ORDERED = "relation.ReadPrincipal2CockpitObjectAbstractCollectionRelation.target.ordered";
    protected static String READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED = "relation.ReadPrincipal2CockpitObjectAbstractCollectionRelation.markmodified";
    public static final String WRITEPRINCIPALS = "writePrincipals";
    protected static String WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_SRC_ORDERED = "relation.WritePrincipal2CockpitObjectAbstractCollectionRelation.source.ordered";
    protected static String WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_TGT_ORDERED = "relation.WritePrincipal2CockpitObjectAbstractCollectionRelation.target.ordered";
    protected static String WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED = "relation.WritePrincipal2CockpitObjectAbstractCollectionRelation.markmodified";
    public static final String ELEMENTS = "elements";
    protected static final BidirectionalOneToManyHandler<GeneratedCockpitObjectAbstractCollection> USERHANDLER = new BidirectionalOneToManyHandler(GeneratedCockpitConstants.TC.COCKPITOBJECTABSTRACTCOLLECTION, false, "user", null, false, true, 0);
    protected static final OneToManyHandler<ObjectCollectionElement> ELEMENTSHANDLER = new OneToManyHandler(GeneratedCockpitConstants.TC.OBJECTCOLLECTIONELEMENT, true, "collection", null, false, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("qualifier", Item.AttributeMode.INITIAL);
        tmp.put("label", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("user", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        USERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCockpitObjectAbstractCollection.getDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "description", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllDescription()
    {
        return getAllDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCockpitObjectAbstractCollection.setDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public void setAllDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "description", value);
    }


    public void setAllDescription(Map<Language, String> value)
    {
        setAllDescription(getSession().getSessionContext(), value);
    }


    public List<ObjectCollectionElement> getElements(SessionContext ctx)
    {
        return (List<ObjectCollectionElement>)ELEMENTSHANDLER.getValues(ctx, (Item)this);
    }


    public List<ObjectCollectionElement> getElements()
    {
        return getElements(getSession().getSessionContext());
    }


    public void setElements(SessionContext ctx, List<ObjectCollectionElement> value)
    {
        ELEMENTSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setElements(List<ObjectCollectionElement> value)
    {
        setElements(getSession().getSessionContext(), value);
    }


    public void addToElements(SessionContext ctx, ObjectCollectionElement value)
    {
        ELEMENTSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToElements(ObjectCollectionElement value)
    {
        addToElements(getSession().getSessionContext(), value);
    }


    public void removeFromElements(SessionContext ctx, ObjectCollectionElement value)
    {
        ELEMENTSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromElements(ObjectCollectionElement value)
    {
        removeFromElements(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Principal");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("Principal");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED);
        }
        return true;
    }


    public String getLabel(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCockpitObjectAbstractCollection.getLabel requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "label");
    }


    public String getLabel()
    {
        return getLabel(getSession().getSessionContext());
    }


    public Map<Language, String> getAllLabel(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "label", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllLabel()
    {
        return getAllLabel(getSession().getSessionContext());
    }


    public void setLabel(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCockpitObjectAbstractCollection.setLabel requires a session language", 0);
        }
        setLocalizedProperty(ctx, "label", value);
    }


    public void setLabel(String value)
    {
        setLabel(getSession().getSessionContext(), value);
    }


    public void setAllLabel(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "label", value);
    }


    public void setAllLabel(Map<Language, String> value)
    {
        setAllLabel(getSession().getSessionContext(), value);
    }


    public String getQualifier(SessionContext ctx)
    {
        return (String)getProperty(ctx, "qualifier");
    }


    public String getQualifier()
    {
        return getQualifier(getSession().getSessionContext());
    }


    public void setQualifier(SessionContext ctx, String value)
    {
        setProperty(ctx, "qualifier", value);
    }


    public void setQualifier(String value)
    {
        setQualifier(getSession().getSessionContext(), value);
    }


    public Collection<Principal> getReadPrincipals(SessionContext ctx)
    {
        List<Principal> items = getLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, "Principal", null, false, false);
        return items;
    }


    public Collection<Principal> getReadPrincipals()
    {
        return getReadPrincipals(getSession().getSessionContext());
    }


    public long getReadPrincipalsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, "Principal", null);
    }


    public long getReadPrincipalsCount()
    {
        return getReadPrincipalsCount(getSession().getSessionContext());
    }


    public void setReadPrincipals(SessionContext ctx, Collection<Principal> value)
    {
        setLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED));
    }


    public void setReadPrincipals(Collection<Principal> value)
    {
        setReadPrincipals(getSession().getSessionContext(), value);
    }


    public void addToReadPrincipals(SessionContext ctx, Principal value)
    {
        addLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED));
    }


    public void addToReadPrincipals(Principal value)
    {
        addToReadPrincipals(getSession().getSessionContext(), value);
    }


    public void removeFromReadPrincipals(SessionContext ctx, Principal value)
    {
        removeLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(READPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED));
    }


    public void removeFromReadPrincipals(Principal value)
    {
        removeFromReadPrincipals(getSession().getSessionContext(), value);
    }


    public User getUser(SessionContext ctx)
    {
        return (User)getProperty(ctx, "user");
    }


    public User getUser()
    {
        return getUser(getSession().getSessionContext());
    }


    public void setUser(SessionContext ctx, User value)
    {
        USERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setUser(User value)
    {
        setUser(getSession().getSessionContext(), value);
    }


    public Collection<Principal> getWritePrincipals(SessionContext ctx)
    {
        List<Principal> items = getLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, "Principal", null, false, false);
        return items;
    }


    public Collection<Principal> getWritePrincipals()
    {
        return getWritePrincipals(getSession().getSessionContext());
    }


    public long getWritePrincipalsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCockpitConstants.Relations.WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, "Principal", null);
    }


    public long getWritePrincipalsCount()
    {
        return getWritePrincipalsCount(getSession().getSessionContext());
    }


    public void setWritePrincipals(SessionContext ctx, Collection<Principal> value)
    {
        setLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED));
    }


    public void setWritePrincipals(Collection<Principal> value)
    {
        setWritePrincipals(getSession().getSessionContext(), value);
    }


    public void addToWritePrincipals(SessionContext ctx, Principal value)
    {
        addLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED));
    }


    public void addToWritePrincipals(Principal value)
    {
        addToWritePrincipals(getSession().getSessionContext(), value);
    }


    public void removeFromWritePrincipals(SessionContext ctx, Principal value)
    {
        removeLinkedItems(ctx, false, GeneratedCockpitConstants.Relations.WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(WRITEPRINCIPAL2COCKPITOBJECTABSTRACTCOLLECTIONRELATION_MARKMODIFIED));
    }


    public void removeFromWritePrincipals(Principal value)
    {
        removeFromWritePrincipals(getSession().getSessionContext(), value);
    }
}
