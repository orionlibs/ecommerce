package de.hybris.platform.ticket.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.store.BaseStore;
import de.hybris.platform.ticket.constants.GeneratedTicketsystemConstants;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCsAgentGroup extends UserGroup
{
    public static final String EMAILDISTRIBUTIONLIST = "emailDistributionList";
    public static final String DEFAULTASSIGNEE = "defaultAssignee";
    public static final String TICKETSTORES = "ticketstores";
    protected static String CSAGENTGROUP2BASESTORE_SRC_ORDERED = "relation.CsAgentGroup2BaseStore.source.ordered";
    protected static String CSAGENTGROUP2BASESTORE_TGT_ORDERED = "relation.CsAgentGroup2BaseStore.target.ordered";
    protected static String CSAGENTGROUP2BASESTORE_MARKMODIFIED = "relation.CsAgentGroup2BaseStore.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(UserGroup.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("emailDistributionList", Item.AttributeMode.INITIAL);
        tmp.put("defaultAssignee", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Employee getDefaultAssignee(SessionContext ctx)
    {
        return (Employee)getProperty(ctx, "defaultAssignee");
    }


    public Employee getDefaultAssignee()
    {
        return getDefaultAssignee(getSession().getSessionContext());
    }


    public void setDefaultAssignee(SessionContext ctx, Employee value)
    {
        setProperty(ctx, "defaultAssignee", value);
    }


    public void setDefaultAssignee(Employee value)
    {
        setDefaultAssignee(getSession().getSessionContext(), value);
    }


    public String getEmailDistributionList(SessionContext ctx)
    {
        return (String)getProperty(ctx, "emailDistributionList");
    }


    public String getEmailDistributionList()
    {
        return getEmailDistributionList(getSession().getSessionContext());
    }


    public void setEmailDistributionList(SessionContext ctx, String value)
    {
        setProperty(ctx, "emailDistributionList", value);
    }


    public void setEmailDistributionList(String value)
    {
        setEmailDistributionList(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("BaseStore");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CSAGENTGROUP2BASESTORE_MARKMODIFIED);
        }
        return true;
    }


    public List<BaseStore> getTicketstores(SessionContext ctx)
    {
        List<BaseStore> items = getLinkedItems(ctx, true, GeneratedTicketsystemConstants.Relations.CSAGENTGROUP2BASESTORE, "BaseStore", null,
                        Utilities.getRelationOrderingOverride(CSAGENTGROUP2BASESTORE_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CSAGENTGROUP2BASESTORE_TGT_ORDERED, true));
        return items;
    }


    public List<BaseStore> getTicketstores()
    {
        return getTicketstores(getSession().getSessionContext());
    }


    public long getTicketstoresCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedTicketsystemConstants.Relations.CSAGENTGROUP2BASESTORE, "BaseStore", null);
    }


    public long getTicketstoresCount()
    {
        return getTicketstoresCount(getSession().getSessionContext());
    }


    public void setTicketstores(SessionContext ctx, List<BaseStore> value)
    {
        setLinkedItems(ctx, true, GeneratedTicketsystemConstants.Relations.CSAGENTGROUP2BASESTORE, null, value,
                        Utilities.getRelationOrderingOverride(CSAGENTGROUP2BASESTORE_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CSAGENTGROUP2BASESTORE_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CSAGENTGROUP2BASESTORE_MARKMODIFIED));
    }


    public void setTicketstores(List<BaseStore> value)
    {
        setTicketstores(getSession().getSessionContext(), value);
    }


    public void addToTicketstores(SessionContext ctx, BaseStore value)
    {
        addLinkedItems(ctx, true, GeneratedTicketsystemConstants.Relations.CSAGENTGROUP2BASESTORE, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CSAGENTGROUP2BASESTORE_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CSAGENTGROUP2BASESTORE_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CSAGENTGROUP2BASESTORE_MARKMODIFIED));
    }


    public void addToTicketstores(BaseStore value)
    {
        addToTicketstores(getSession().getSessionContext(), value);
    }


    public void removeFromTicketstores(SessionContext ctx, BaseStore value)
    {
        removeLinkedItems(ctx, true, GeneratedTicketsystemConstants.Relations.CSAGENTGROUP2BASESTORE, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CSAGENTGROUP2BASESTORE_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(CSAGENTGROUP2BASESTORE_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(CSAGENTGROUP2BASESTORE_MARKMODIFIED));
    }


    public void removeFromTicketstores(BaseStore value)
    {
        removeFromTicketstores(getSession().getSessionContext(), value);
    }
}
