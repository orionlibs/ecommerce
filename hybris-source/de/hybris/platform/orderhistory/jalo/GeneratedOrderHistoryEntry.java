package de.hybris.platform.orderhistory.jalo;

import de.hybris.platform.basecommerce.constants.GeneratedBasecommerceConstants;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedOrderHistoryEntry extends GenericItem
{
    public static final String TIMESTAMP = "timestamp";
    public static final String EMPLOYEE = "employee";
    public static final String DESCRIPTION = "description";
    public static final String PREVIOUSORDERVERSION = "previousOrderVersion";
    public static final String ORDERPOS = "orderPOS";
    public static final String ORDER = "order";
    public static final String DOCUMENTS = "documents";
    protected static String HISTORYDOCUMENTRELATION_SRC_ORDERED = "relation.HistoryDocumentRelation.source.ordered";
    protected static String HISTORYDOCUMENTRELATION_TGT_ORDERED = "relation.HistoryDocumentRelation.target.ordered";
    protected static String HISTORYDOCUMENTRELATION_MARKMODIFIED = "relation.HistoryDocumentRelation.markmodified";
    protected static final BidirectionalOneToManyHandler<GeneratedOrderHistoryEntry> ORDERHANDLER = new BidirectionalOneToManyHandler(GeneratedBasecommerceConstants.TC.ORDERHISTORYENTRY, false, "order", "orderPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("timestamp", Item.AttributeMode.INITIAL);
        tmp.put("employee", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        tmp.put("previousOrderVersion", Item.AttributeMode.INITIAL);
        tmp.put("orderPOS", Item.AttributeMode.INITIAL);
        tmp.put("order", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        ORDERHANDLER.newInstance(ctx, allAttributes);
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


    public Set<Media> getDocuments(SessionContext ctx)
    {
        List<Media> items = getLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.HISTORYDOCUMENTRELATION, "Media", null, false, false);
        return new LinkedHashSet<>(items);
    }


    public Set<Media> getDocuments()
    {
        return getDocuments(getSession().getSessionContext());
    }


    public long getDocumentsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedBasecommerceConstants.Relations.HISTORYDOCUMENTRELATION, "Media", null);
    }


    public long getDocumentsCount()
    {
        return getDocumentsCount(getSession().getSessionContext());
    }


    public void setDocuments(SessionContext ctx, Set<Media> value)
    {
        setLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.HISTORYDOCUMENTRELATION, null, value, false, false,
                        Utilities.getMarkModifiedOverride(HISTORYDOCUMENTRELATION_MARKMODIFIED));
    }


    public void setDocuments(Set<Media> value)
    {
        setDocuments(getSession().getSessionContext(), value);
    }


    public void addToDocuments(SessionContext ctx, Media value)
    {
        addLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.HISTORYDOCUMENTRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(HISTORYDOCUMENTRELATION_MARKMODIFIED));
    }


    public void addToDocuments(Media value)
    {
        addToDocuments(getSession().getSessionContext(), value);
    }


    public void removeFromDocuments(SessionContext ctx, Media value)
    {
        removeLinkedItems(ctx, true, GeneratedBasecommerceConstants.Relations.HISTORYDOCUMENTRELATION, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(HISTORYDOCUMENTRELATION_MARKMODIFIED));
    }


    public void removeFromDocuments(Media value)
    {
        removeFromDocuments(getSession().getSessionContext(), value);
    }


    public Employee getEmployee(SessionContext ctx)
    {
        return (Employee)getProperty(ctx, "employee");
    }


    public Employee getEmployee()
    {
        return getEmployee(getSession().getSessionContext());
    }


    public void setEmployee(SessionContext ctx, Employee value)
    {
        setProperty(ctx, "employee", value);
    }


    public void setEmployee(Employee value)
    {
        setEmployee(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Media");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(HISTORYDOCUMENTRELATION_MARKMODIFIED);
        }
        return true;
    }


    public Order getOrder(SessionContext ctx)
    {
        return (Order)getProperty(ctx, "order");
    }


    public Order getOrder()
    {
        return getOrder(getSession().getSessionContext());
    }


    protected void setOrder(SessionContext ctx, Order value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'order' is not changeable", 0);
        }
        ORDERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    protected void setOrder(Order value)
    {
        setOrder(getSession().getSessionContext(), value);
    }


    Integer getOrderPOS(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "orderPOS");
    }


    Integer getOrderPOS()
    {
        return getOrderPOS(getSession().getSessionContext());
    }


    int getOrderPOSAsPrimitive(SessionContext ctx)
    {
        Integer value = getOrderPOS(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    int getOrderPOSAsPrimitive()
    {
        return getOrderPOSAsPrimitive(getSession().getSessionContext());
    }


    void setOrderPOS(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "orderPOS", value);
    }


    void setOrderPOS(Integer value)
    {
        setOrderPOS(getSession().getSessionContext(), value);
    }


    void setOrderPOS(SessionContext ctx, int value)
    {
        setOrderPOS(ctx, Integer.valueOf(value));
    }


    void setOrderPOS(int value)
    {
        setOrderPOS(getSession().getSessionContext(), value);
    }


    public Order getPreviousOrderVersion(SessionContext ctx)
    {
        return (Order)getProperty(ctx, "previousOrderVersion");
    }


    public Order getPreviousOrderVersion()
    {
        return getPreviousOrderVersion(getSession().getSessionContext());
    }


    public void setPreviousOrderVersion(SessionContext ctx, Order value)
    {
        setProperty(ctx, "previousOrderVersion", value);
    }


    public void setPreviousOrderVersion(Order value)
    {
        setPreviousOrderVersion(getSession().getSessionContext(), value);
    }


    public Date getTimestamp(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "timestamp");
    }


    public Date getTimestamp()
    {
        return getTimestamp(getSession().getSessionContext());
    }


    protected void setTimestamp(SessionContext ctx, Date value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'timestamp' is not changeable", 0);
        }
        setProperty(ctx, "timestamp", value);
    }


    protected void setTimestamp(Date value)
    {
        setTimestamp(getSession().getSessionContext(), value);
    }
}
