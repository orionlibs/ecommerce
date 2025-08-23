package de.hybris.platform.patches.jalo;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.patches.constants.GeneratedPatchesConstants;
import de.hybris.platform.util.BidirectionalOneToManyHandler;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedPatchExecutionUnit extends GenericItem
{
    public static final String IDHASH = "idHash";
    public static final String NAME = "name";
    public static final String ORDERNUMBER = "orderNumber";
    public static final String ORGANISATION = "organisation";
    public static final String EXECUTIONSTATUS = "executionStatus";
    public static final String EXECUTIONTYPE = "executionType";
    public static final String EXECUTIONTIME = "executionTime";
    public static final String CONTENTHASH = "contentHash";
    public static final String ERRORLOG = "errorLog";
    public static final String CRONJOB = "cronjob";
    public static final String PATCH = "patch";
    protected static final BidirectionalOneToManyHandler<GeneratedPatchExecutionUnit> PATCHHANDLER = new BidirectionalOneToManyHandler(GeneratedPatchesConstants.TC.PATCHEXECUTIONUNIT, false, "patch", null, false, true, 0);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("idHash", Item.AttributeMode.INITIAL);
        tmp.put("name", Item.AttributeMode.INITIAL);
        tmp.put("orderNumber", Item.AttributeMode.INITIAL);
        tmp.put("organisation", Item.AttributeMode.INITIAL);
        tmp.put("executionStatus", Item.AttributeMode.INITIAL);
        tmp.put("executionType", Item.AttributeMode.INITIAL);
        tmp.put("executionTime", Item.AttributeMode.INITIAL);
        tmp.put("contentHash", Item.AttributeMode.INITIAL);
        tmp.put("errorLog", Item.AttributeMode.INITIAL);
        tmp.put("cronjob", Item.AttributeMode.INITIAL);
        tmp.put("patch", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public String getContentHash(SessionContext ctx)
    {
        return (String)getProperty(ctx, "contentHash");
    }


    public String getContentHash()
    {
        return getContentHash(getSession().getSessionContext());
    }


    public void setContentHash(SessionContext ctx, String value)
    {
        setProperty(ctx, "contentHash", value);
    }


    public void setContentHash(String value)
    {
        setContentHash(getSession().getSessionContext(), value);
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        PATCHHANDLER.newInstance(ctx, allAttributes);
        return super.createItem(ctx, type, allAttributes);
    }


    public CronJob getCronjob(SessionContext ctx)
    {
        return (CronJob)getProperty(ctx, "cronjob");
    }


    public CronJob getCronjob()
    {
        return getCronjob(getSession().getSessionContext());
    }


    public void setCronjob(SessionContext ctx, CronJob value)
    {
        setProperty(ctx, "cronjob", value);
    }


    public void setCronjob(CronJob value)
    {
        setCronjob(getSession().getSessionContext(), value);
    }


    public String getErrorLog(SessionContext ctx)
    {
        return (String)getProperty(ctx, "errorLog");
    }


    public String getErrorLog()
    {
        return getErrorLog(getSession().getSessionContext());
    }


    public void setErrorLog(SessionContext ctx, String value)
    {
        setProperty(ctx, "errorLog", value);
    }


    public void setErrorLog(String value)
    {
        setErrorLog(getSession().getSessionContext(), value);
    }


    public EnumerationValue getExecutionStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "executionStatus");
    }


    public EnumerationValue getExecutionStatus()
    {
        return getExecutionStatus(getSession().getSessionContext());
    }


    public void setExecutionStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "executionStatus", value);
    }


    public void setExecutionStatus(EnumerationValue value)
    {
        setExecutionStatus(getSession().getSessionContext(), value);
    }


    public Date getExecutionTime(SessionContext ctx)
    {
        return (Date)getProperty(ctx, "executionTime");
    }


    public Date getExecutionTime()
    {
        return getExecutionTime(getSession().getSessionContext());
    }


    public void setExecutionTime(SessionContext ctx, Date value)
    {
        setProperty(ctx, "executionTime", value);
    }


    public void setExecutionTime(Date value)
    {
        setExecutionTime(getSession().getSessionContext(), value);
    }


    public EnumerationValue getExecutionType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "executionType");
    }


    public EnumerationValue getExecutionType()
    {
        return getExecutionType(getSession().getSessionContext());
    }


    public void setExecutionType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "executionType", value);
    }


    public void setExecutionType(EnumerationValue value)
    {
        setExecutionType(getSession().getSessionContext(), value);
    }


    public String getIdHash(SessionContext ctx)
    {
        return (String)getProperty(ctx, "idHash");
    }


    public String getIdHash()
    {
        return getIdHash(getSession().getSessionContext());
    }


    public void setIdHash(SessionContext ctx, String value)
    {
        setProperty(ctx, "idHash", value);
    }


    public void setIdHash(String value)
    {
        setIdHash(getSession().getSessionContext(), value);
    }


    public String getName(SessionContext ctx)
    {
        return (String)getProperty(ctx, "name");
    }


    public String getName()
    {
        return getName(getSession().getSessionContext());
    }


    public void setName(SessionContext ctx, String value)
    {
        setProperty(ctx, "name", value);
    }


    public void setName(String value)
    {
        setName(getSession().getSessionContext(), value);
    }


    public Integer getOrderNumber(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "orderNumber");
    }


    public Integer getOrderNumber()
    {
        return getOrderNumber(getSession().getSessionContext());
    }


    public int getOrderNumberAsPrimitive(SessionContext ctx)
    {
        Integer value = getOrderNumber(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getOrderNumberAsPrimitive()
    {
        return getOrderNumberAsPrimitive(getSession().getSessionContext());
    }


    public void setOrderNumber(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "orderNumber", value);
    }


    public void setOrderNumber(Integer value)
    {
        setOrderNumber(getSession().getSessionContext(), value);
    }


    public void setOrderNumber(SessionContext ctx, int value)
    {
        setOrderNumber(ctx, Integer.valueOf(value));
    }


    public void setOrderNumber(int value)
    {
        setOrderNumber(getSession().getSessionContext(), value);
    }


    public String getOrganisation(SessionContext ctx)
    {
        return (String)getProperty(ctx, "organisation");
    }


    public String getOrganisation()
    {
        return getOrganisation(getSession().getSessionContext());
    }


    public void setOrganisation(SessionContext ctx, String value)
    {
        setProperty(ctx, "organisation", value);
    }


    public void setOrganisation(String value)
    {
        setOrganisation(getSession().getSessionContext(), value);
    }


    public PatchExecution getPatch(SessionContext ctx)
    {
        return (PatchExecution)getProperty(ctx, "patch");
    }


    public PatchExecution getPatch()
    {
        return getPatch(getSession().getSessionContext());
    }


    public void setPatch(SessionContext ctx, PatchExecution value)
    {
        PATCHHANDLER.addValue(ctx, (Item)value, (ExtensibleItem)this);
    }


    public void setPatch(PatchExecution value)
    {
        setPatch(getSession().getSessionContext(), value);
    }
}
