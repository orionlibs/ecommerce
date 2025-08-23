package de.hybris.y2ysync.jalo;

import de.hybris.deltadetection.jalo.StreamConfiguration;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.y2ysync.constants.GeneratedY2ysyncConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class GeneratedY2YStreamConfiguration extends StreamConfiguration
{
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String AUTOGENERATEWHERECLAUSE = "autoGenerateWhereClause";
    public static final String AUTOGENERATEINFOEXPRESSION = "autoGenerateInfoExpression";
    public static final String DATAHUBTYPE = "dataHubType";
    public static final String COLUMNDEFINITIONS = "columnDefinitions";
    protected static final OneToManyHandler<Y2YColumnDefintion> COLUMNDEFINITIONSHANDLER = new OneToManyHandler(GeneratedY2ysyncConstants.TC.Y2YCOLUMNDEFINITION, true, "streamConfiguration", null, false, true, 1);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(StreamConfiguration.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("autoGenerateWhereClause", Item.AttributeMode.INITIAL);
        tmp.put("autoGenerateInfoExpression", Item.AttributeMode.INITIAL);
        tmp.put("dataHubType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAutoGenerateInfoExpression(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "autoGenerateInfoExpression");
    }


    public Boolean isAutoGenerateInfoExpression()
    {
        return isAutoGenerateInfoExpression(getSession().getSessionContext());
    }


    public boolean isAutoGenerateInfoExpressionAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAutoGenerateInfoExpression(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAutoGenerateInfoExpressionAsPrimitive()
    {
        return isAutoGenerateInfoExpressionAsPrimitive(getSession().getSessionContext());
    }


    public void setAutoGenerateInfoExpression(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "autoGenerateInfoExpression", value);
    }


    public void setAutoGenerateInfoExpression(Boolean value)
    {
        setAutoGenerateInfoExpression(getSession().getSessionContext(), value);
    }


    public void setAutoGenerateInfoExpression(SessionContext ctx, boolean value)
    {
        setAutoGenerateInfoExpression(ctx, Boolean.valueOf(value));
    }


    public void setAutoGenerateInfoExpression(boolean value)
    {
        setAutoGenerateInfoExpression(getSession().getSessionContext(), value);
    }


    public Boolean isAutoGenerateWhereClause(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "autoGenerateWhereClause");
    }


    public Boolean isAutoGenerateWhereClause()
    {
        return isAutoGenerateWhereClause(getSession().getSessionContext());
    }


    public boolean isAutoGenerateWhereClauseAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAutoGenerateWhereClause(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAutoGenerateWhereClauseAsPrimitive()
    {
        return isAutoGenerateWhereClauseAsPrimitive(getSession().getSessionContext());
    }


    public void setAutoGenerateWhereClause(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "autoGenerateWhereClause", value);
    }


    public void setAutoGenerateWhereClause(Boolean value)
    {
        setAutoGenerateWhereClause(getSession().getSessionContext(), value);
    }


    public void setAutoGenerateWhereClause(SessionContext ctx, boolean value)
    {
        setAutoGenerateWhereClause(ctx, Boolean.valueOf(value));
    }


    public void setAutoGenerateWhereClause(boolean value)
    {
        setAutoGenerateWhereClause(getSession().getSessionContext(), value);
    }


    public CatalogVersion getCatalogVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "catalogVersion");
    }


    public CatalogVersion getCatalogVersion()
    {
        return getCatalogVersion(getSession().getSessionContext());
    }


    public void setCatalogVersion(SessionContext ctx, CatalogVersion value)
    {
        setProperty(ctx, "catalogVersion", value);
    }


    public void setCatalogVersion(CatalogVersion value)
    {
        setCatalogVersion(getSession().getSessionContext(), value);
    }


    public Set<Y2YColumnDefintion> getColumnDefinitions(SessionContext ctx)
    {
        return (Set<Y2YColumnDefintion>)COLUMNDEFINITIONSHANDLER.getValues(ctx, (Item)this);
    }


    public Set<Y2YColumnDefintion> getColumnDefinitions()
    {
        return getColumnDefinitions(getSession().getSessionContext());
    }


    public void setColumnDefinitions(SessionContext ctx, Set<Y2YColumnDefintion> value)
    {
        COLUMNDEFINITIONSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setColumnDefinitions(Set<Y2YColumnDefintion> value)
    {
        setColumnDefinitions(getSession().getSessionContext(), value);
    }


    public void addToColumnDefinitions(SessionContext ctx, Y2YColumnDefintion value)
    {
        COLUMNDEFINITIONSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToColumnDefinitions(Y2YColumnDefintion value)
    {
        addToColumnDefinitions(getSession().getSessionContext(), value);
    }


    public void removeFromColumnDefinitions(SessionContext ctx, Y2YColumnDefintion value)
    {
        COLUMNDEFINITIONSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromColumnDefinitions(Y2YColumnDefintion value)
    {
        removeFromColumnDefinitions(getSession().getSessionContext(), value);
    }


    public String getDataHubType(SessionContext ctx)
    {
        return (String)getProperty(ctx, "dataHubType");
    }


    public String getDataHubType()
    {
        return getDataHubType(getSession().getSessionContext());
    }


    public void setDataHubType(SessionContext ctx, String value)
    {
        setProperty(ctx, "dataHubType", value);
    }


    public void setDataHubType(String value)
    {
        setDataHubType(getSession().getSessionContext(), value);
    }
}
