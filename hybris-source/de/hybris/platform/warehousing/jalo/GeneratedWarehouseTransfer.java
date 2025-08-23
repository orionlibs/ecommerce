package de.hybris.platform.warehousing.jalo;

import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.warehousing.constants.GeneratedWarehousingConstants;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedWarehouseTransfer extends GenericItem
{
    public static final String COMPLETIONDATE = "completionDate";
    public static final String WAREHOUSETRANSFERENTRIES = "warehouseTransferEntries";
    public static final String PARENTTRANSFER = "parentTransfer";
    public static final String DEPENDENTTRANSFERS = "dependentTransfers";
    protected static final OneToManyHandler<WarehouseTransferEntry> WAREHOUSETRANSFERENTRIESHANDLER = new OneToManyHandler(GeneratedWarehousingConstants.TC.WAREHOUSETRANSFERENTRY, false, "warehouseTransfer", null, false, true, 0);
    protected static final BidirectionalOneToManyHandler<GeneratedWarehouseTransfer> PARENTTRANSFERHANDLER = new BidirectionalOneToManyHandler(GeneratedWarehousingConstants.TC.WAREHOUSETRANSFER, false, "parentTransfer", null, false, true, 0);
    protected static final OneToManyHandler<WarehouseTransfer> DEPENDENTTRANSFERSHANDLER = new OneToManyHandler(GeneratedWarehousingConstants.TC.WAREHOUSETRANSFER, false, "parentTransfer", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("completionDate", Item.AttributeMode.INITIAL);
        tmp.put("parentTransfer", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Date getCompletionDate(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "completionDate");
    }


    public Date getCompletionDate()
    {
        return getCompletionDate(getSession().getSessionContext());
    }


    public void setCompletionDate(SessionContext ctx, Date value)
    {
        setProperty(ctx, "completionDate", value);
    }


    public void setCompletionDate(Date value)
    {
        setCompletionDate(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PARENTTRANSFERHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public Collection<WarehouseTransfer> getDependentTransfers(SessionContext ctx)
    {
        return DEPENDENTTRANSFERSHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<WarehouseTransfer> getDependentTransfers()
    {
        return getDependentTransfers(getSession().getSessionContext());
    }


    public void setDependentTransfers(SessionContext ctx, Collection<WarehouseTransfer> value)
    {
        DEPENDENTTRANSFERSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setDependentTransfers(Collection<WarehouseTransfer> value)
    {
        setDependentTransfers(getSession().getSessionContext(), value);
    }


    public void addToDependentTransfers(SessionContext ctx, WarehouseTransfer value)
    {
        DEPENDENTTRANSFERSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToDependentTransfers(WarehouseTransfer value)
    {
        addToDependentTransfers(getSession().getSessionContext(), value);
    }


    public void removeFromDependentTransfers(SessionContext ctx, WarehouseTransfer value)
    {
        DEPENDENTTRANSFERSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromDependentTransfers(WarehouseTransfer value)
    {
        removeFromDependentTransfers(getSession().getSessionContext(), value);
    }


    public WarehouseTransfer getParentTransfer(SessionContext ctx)
    {
        return (WarehouseTransfer)getProperty(ctx, "parentTransfer");
    }


    public WarehouseTransfer getParentTransfer()
    {
        return getParentTransfer(getSession().getSessionContext());
    }


    public void setParentTransfer(SessionContext ctx, WarehouseTransfer value)
    {
        PARENTTRANSFERHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setParentTransfer(WarehouseTransfer value)
    {
        setParentTransfer(getSession().getSessionContext(), value);
    }


    public Collection<WarehouseTransferEntry> getWarehouseTransferEntries(SessionContext ctx)
    {
        return WAREHOUSETRANSFERENTRIESHANDLER.getValues(ctx, (Item)this);
    }


    public Collection<WarehouseTransferEntry> getWarehouseTransferEntries()
    {
        return getWarehouseTransferEntries(getSession().getSessionContext());
    }


    public void setWarehouseTransferEntries(SessionContext ctx, Collection<WarehouseTransferEntry> value)
    {
        WAREHOUSETRANSFERENTRIESHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setWarehouseTransferEntries(Collection<WarehouseTransferEntry> value)
    {
        setWarehouseTransferEntries(getSession().getSessionContext(), value);
    }


    public void addToWarehouseTransferEntries(SessionContext ctx, WarehouseTransferEntry value)
    {
        WAREHOUSETRANSFERENTRIESHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToWarehouseTransferEntries(WarehouseTransferEntry value)
    {
        addToWarehouseTransferEntries(getSession().getSessionContext(), value);
    }


    public void removeFromWarehouseTransferEntries(SessionContext ctx, WarehouseTransferEntry value)
    {
        WAREHOUSETRANSFERENTRIESHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromWarehouseTransferEntries(WarehouseTransferEntry value)
    {
        removeFromWarehouseTransferEntries(getSession().getSessionContext(), value);
    }
}
