package de.hybris.platform.catalog.jalo;

import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCatalogVersionDifference extends GenericItem
{
    public static final String SOURCEVERSION = "sourceVersion";
    public static final String TARGETVERSION = "targetVersion";
    public static final String CRONJOB = "cronJob";
    public static final String DIFFERENCETEXT = "differenceText";
    public static final String DIFFERENCEVALUE = "differenceValue";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("sourceVersion", Item.AttributeMode.INITIAL);
        tmp.put("targetVersion", Item.AttributeMode.INITIAL);
        tmp.put("cronJob", Item.AttributeMode.INITIAL);
        tmp.put("differenceText", Item.AttributeMode.INITIAL);
        tmp.put("differenceValue", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public CompareCatalogVersionsCronJob getCronJob(SessionContext ctx)
    {
        return (CompareCatalogVersionsCronJob)getProperty(ctx, "cronJob");
    }


    public CompareCatalogVersionsCronJob getCronJob()
    {
        return getCronJob(getSession().getSessionContext());
    }


    protected void setCronJob(SessionContext ctx, CompareCatalogVersionsCronJob value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'cronJob' is not changeable", 0);
        }
        setProperty(ctx, "cronJob", value);
    }


    protected void setCronJob(CompareCatalogVersionsCronJob value)
    {
        setCronJob(getSession().getSessionContext(), value);
    }


    public String getDifferenceText(SessionContext ctx)
    {
        return (String)getProperty(ctx, "differenceText");
    }


    public String getDifferenceText()
    {
        return getDifferenceText(getSession().getSessionContext());
    }


    public void setDifferenceText(SessionContext ctx, String value)
    {
        setProperty(ctx, "differenceText", value);
    }


    public void setDifferenceText(String value)
    {
        setDifferenceText(getSession().getSessionContext(), value);
    }


    public Double getDifferenceValue(SessionContext ctx)
    {
        return (Double)getProperty(ctx, "differenceValue");
    }


    public Double getDifferenceValue()
    {
        return getDifferenceValue(getSession().getSessionContext());
    }


    public double getDifferenceValueAsPrimitive(SessionContext ctx)
    {
        Double value = getDifferenceValue(ctx);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getDifferenceValueAsPrimitive()
    {
        return getDifferenceValueAsPrimitive(getSession().getSessionContext());
    }


    public void setDifferenceValue(SessionContext ctx, Double value)
    {
        setProperty(ctx, "differenceValue", value);
    }


    public void setDifferenceValue(Double value)
    {
        setDifferenceValue(getSession().getSessionContext(), value);
    }


    public void setDifferenceValue(SessionContext ctx, double value)
    {
        setDifferenceValue(ctx, Double.valueOf(value));
    }


    public void setDifferenceValue(double value)
    {
        setDifferenceValue(getSession().getSessionContext(), value);
    }


    public CatalogVersion getSourceVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "sourceVersion");
    }


    public CatalogVersion getSourceVersion()
    {
        return getSourceVersion(getSession().getSessionContext());
    }


    protected void setSourceVersion(SessionContext ctx, CatalogVersion value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'sourceVersion' is not changeable", 0);
        }
        setProperty(ctx, "sourceVersion", value);
    }


    protected void setSourceVersion(CatalogVersion value)
    {
        setSourceVersion(getSession().getSessionContext(), value);
    }


    public CatalogVersion getTargetVersion(SessionContext ctx)
    {
        return (CatalogVersion)getProperty(ctx, "targetVersion");
    }


    public CatalogVersion getTargetVersion()
    {
        return getTargetVersion(getSession().getSessionContext());
    }


    protected void setTargetVersion(SessionContext ctx, CatalogVersion value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getAttribute("core.types.creation.initial") != Boolean.TRUE)
        {
            throw new JaloInvalidParameterException("attribute 'targetVersion' is not changeable", 0);
        }
        setProperty(ctx, "targetVersion", value);
    }


    protected void setTargetVersion(CatalogVersion value)
    {
        setTargetVersion(getSession().getSessionContext(), value);
    }
}
