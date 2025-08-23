package de.hybris.platform.cms2.jalo.restrictions;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.jalo.contents.CMSItem;
import de.hybris.platform.cms2.jalo.contents.components.AbstractCMSComponent;
import de.hybris.platform.cms2.jalo.pages.AbstractPage;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedAbstractRestriction extends CMSItem
{
    public static final String TYPE = "type";
    public static final String TYPECODE = "typeCode";
    public static final String PAGES = "pages";
    protected static String RESTRICTIONSFORPAGES_SRC_ORDERED = "relation.RestrictionsForPages.source.ordered";
    protected static String RESTRICTIONSFORPAGES_TGT_ORDERED = "relation.RestrictionsForPages.target.ordered";
    protected static String RESTRICTIONSFORPAGES_MARKMODIFIED = "relation.RestrictionsForPages.markmodified";
    public static final String COMPONENTS = "components";
    protected static String RESTRICTIONSFORCOMPONENTS_SRC_ORDERED = "relation.RestrictionsForComponents.source.ordered";
    protected static String RESTRICTIONSFORCOMPONENTS_TGT_ORDERED = "relation.RestrictionsForComponents.target.ordered";
    protected static String RESTRICTIONSFORCOMPONENTS_MARKMODIFIED = "relation.RestrictionsForComponents.markmodified";
    public static final String INVERSERESTRICTIONS = "inverseRestrictions";
    protected static final OneToManyHandler<CMSInverseRestriction> INVERSERESTRICTIONSHANDLER = new OneToManyHandler(GeneratedCms2Constants.TC.CMSINVERSERESTRICTION, true, "originalRestriction", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CMSItem.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Collection<AbstractCMSComponent> getComponents(SessionContext ctx)
    {
        List<AbstractCMSComponent> items = getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.RESTRICTIONSFORCOMPONENTS, "AbstractCMSComponent", null,
                        Utilities.getRelationOrderingOverride(RESTRICTIONSFORCOMPONENTS_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<AbstractCMSComponent> getComponents()
    {
        return getComponents(getSession().getSessionContext());
    }


    public long getComponentsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.RESTRICTIONSFORCOMPONENTS, "AbstractCMSComponent", null);
    }


    public long getComponentsCount()
    {
        return getComponentsCount(getSession().getSessionContext());
    }


    public void setComponents(SessionContext ctx, Collection<AbstractCMSComponent> value)
    {
        setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.RESTRICTIONSFORCOMPONENTS, null, value,
                        Utilities.getRelationOrderingOverride(RESTRICTIONSFORCOMPONENTS_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(RESTRICTIONSFORCOMPONENTS_MARKMODIFIED));
    }


    public void setComponents(Collection<AbstractCMSComponent> value)
    {
        setComponents(getSession().getSessionContext(), value);
    }


    public void addToComponents(SessionContext ctx, AbstractCMSComponent value)
    {
        addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.RESTRICTIONSFORCOMPONENTS, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(RESTRICTIONSFORCOMPONENTS_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(RESTRICTIONSFORCOMPONENTS_MARKMODIFIED));
    }


    public void addToComponents(AbstractCMSComponent value)
    {
        addToComponents(getSession().getSessionContext(), value);
    }


    public void removeFromComponents(SessionContext ctx, AbstractCMSComponent value)
    {
        removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.RESTRICTIONSFORCOMPONENTS, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(RESTRICTIONSFORCOMPONENTS_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(RESTRICTIONSFORCOMPONENTS_MARKMODIFIED));
    }


    public void removeFromComponents(AbstractCMSComponent value)
    {
        removeFromComponents(getSession().getSessionContext(), value);
    }


    public Collection<CMSInverseRestriction> getInverseRestrictions(SessionContext ctx)
    {
        return INVERSERESTRICTIONSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<CMSInverseRestriction> getInverseRestrictions()
    {
        return getInverseRestrictions(getSession().getSessionContext());
    }


    public void setInverseRestrictions(SessionContext ctx, Collection<CMSInverseRestriction> value)
    {
        INVERSERESTRICTIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setInverseRestrictions(Collection<CMSInverseRestriction> value)
    {
        setInverseRestrictions(getSession().getSessionContext(), value);
    }


    public void addToInverseRestrictions(SessionContext ctx, CMSInverseRestriction value)
    {
        INVERSERESTRICTIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToInverseRestrictions(CMSInverseRestriction value)
    {
        addToInverseRestrictions(getSession().getSessionContext(), value);
    }


    public void removeFromInverseRestrictions(SessionContext ctx, CMSInverseRestriction value)
    {
        INVERSERESTRICTIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromInverseRestrictions(CMSInverseRestriction value)
    {
        removeFromInverseRestrictions(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("AbstractPage");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(RESTRICTIONSFORPAGES_MARKMODIFIED);
        }
        ComposedType relationSecondEnd1 = TypeManager.getInstance().getComposedType("AbstractCMSComponent");
        if(relationSecondEnd1.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(RESTRICTIONSFORCOMPONENTS_MARKMODIFIED);
        }
        return true;
    }


    public Collection<AbstractPage> getPages(SessionContext ctx)
    {
        List<AbstractPage> items = getLinkedItems(ctx, false, GeneratedCms2Constants.Relations.RESTRICTIONSFORPAGES, "AbstractPage", null,
                        Utilities.getRelationOrderingOverride(RESTRICTIONSFORPAGES_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<AbstractPage> getPages()
    {
        return getPages(getSession().getSessionContext());
    }


    public long getPagesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCms2Constants.Relations.RESTRICTIONSFORPAGES, "AbstractPage", null);
    }


    public long getPagesCount()
    {
        return getPagesCount(getSession().getSessionContext());
    }


    public void setPages(SessionContext ctx, Collection<AbstractPage> value)
    {
        setLinkedItems(ctx, false, GeneratedCms2Constants.Relations.RESTRICTIONSFORPAGES, null, value,
                        Utilities.getRelationOrderingOverride(RESTRICTIONSFORPAGES_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(RESTRICTIONSFORPAGES_MARKMODIFIED));
    }


    public void setPages(Collection<AbstractPage> value)
    {
        setPages(getSession().getSessionContext(), value);
    }


    public void addToPages(SessionContext ctx, AbstractPage value)
    {
        addLinkedItems(ctx, false, GeneratedCms2Constants.Relations.RESTRICTIONSFORPAGES, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(RESTRICTIONSFORPAGES_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(RESTRICTIONSFORPAGES_MARKMODIFIED));
    }


    public void addToPages(AbstractPage value)
    {
        addToPages(getSession().getSessionContext(), value);
    }


    public void removeFromPages(SessionContext ctx, AbstractPage value)
    {
        removeLinkedItems(ctx, false, GeneratedCms2Constants.Relations.RESTRICTIONSFORPAGES, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(RESTRICTIONSFORPAGES_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(RESTRICTIONSFORPAGES_MARKMODIFIED));
    }


    public void removeFromPages(AbstractPage value)
    {
        removeFromPages(getSession().getSessionContext(), value);
    }


    public String getType()
    {
        return getType(getSession().getSessionContext());
    }


    public Map<Language, String> getAllType()
    {
        return getAllType(getSession().getSessionContext());
    }


    public String getTypeCode()
    {
        return getTypeCode(getSession().getSessionContext());
    }


    public abstract String getType(SessionContext paramSessionContext);


    public abstract Map<Language, String> getAllType(SessionContext paramSessionContext);


    public abstract String getTypeCode(SessionContext paramSessionContext);
}
