package de.hybris.platform.jalo.flexiblesearch.internal;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.hints.Hint;
import de.hybris.platform.jalo.flexiblesearch.hints.impl.SQLServerQueryHints;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SQLServerHintProvider implements FlexibleSearchHintsProvider
{
    private final ReadOnlyConditionsHelper readOnlyConditionsHelper;
    private ConfigIntf.ConfigChangeListener configChangeListener;
    private final Tenant tenant;
    private static final String HINTS_SQLSERVER_MAXDOP_PROPERTY = "flexiblesearch.hints.sqlserver.maxdop";
    private int maxDOPFromConfig;


    public SQLServerHintProvider(ReadOnlyConditionsHelper readOnlyConditionsHelper)
    {
        this.readOnlyConditionsHelper = readOnlyConditionsHelper;
        this.maxDOPFromConfig = getInitialMaxDOPValueFromConfig();
        this.tenant = Objects.<Tenant>requireNonNull(Registry.getCurrentTenantNoFallback(),
                        String.format("cannot create %s without tenant", new Object[] {SQLServerHintProvider.class.getSimpleName()}));
        registerConfigChangeListener();
    }


    public List<Hint> injectFlexibleSearchHints(SessionContext localCtx, List<Hint> hints, HybrisDataSource dataSource) throws MaxDOPWrongValueException
    {
        if(hintsContainMaxDOP(hints))
        {
            return hints;
        }
        Integer sessionMaxDOPValue = getMaxDOPValueFromSession(localCtx);
        if(sessionMaxDOPValue != null)
        {
            if(sessionMaxDOPValue.intValue() == -1)
            {
                return hints;
            }
            if(sessionMaxDOPValue.intValue() > -1)
            {
                return addMaxDOPValueToHints(hints, sessionMaxDOPValue.intValue());
            }
        }
        int globalMaxDOPValue = getMaxDOPValueFromConfig();
        if(globalMaxDOPValue > -1 && !isReadOnlyDataSourceUsed(dataSource))
        {
            return addMaxDOPValueToHints(hints, globalMaxDOPValue);
        }
        return hints;
    }


    private boolean hintsContainMaxDOP(List<Hint> hints)
    {
        Objects.requireNonNull(SQLServerQueryHints.class);
        Objects.requireNonNull(SQLServerQueryHints.class);
        return hints.stream().filter(SQLServerQueryHints.class::isInstance).map(SQLServerQueryHints.class::cast)
                        .anyMatch(val -> val.getHints().stream().anyMatch(()));
    }


    private Integer getMaxDOPValueFromSession(SessionContext localCtx) throws MaxDOPWrongValueException
    {
        if(localCtx == null)
        {
            return null;
        }
        Object value = localCtx.getAttribute("flexiblesearch.hints.sqlserver.maxdop");
        if(value == null)
        {
            return null;
        }
        if(value instanceof Integer)
        {
            return Integer.valueOf(defineCorrectMaxDOPValue(((Integer)value).intValue()));
        }
        if(value instanceof String)
        {
            try
            {
                return Integer.valueOf(defineCorrectMaxDOPValue(Integer.parseInt(((String)value).trim())));
            }
            catch(NumberFormatException e)
            {
                throw new MaxDOPWrongValueException(e, "MaxDOP Value is not correct: " + e.getMessage(), 0);
            }
        }
        throw new MaxDOPWrongValueException("MaxDOP Value is not correct - value of MAXDOP is not Integer, cannot parse: " + value
                        .getClass());
    }


    private int getMaxDOPValueFromConfig()
    {
        return defineCorrectMaxDOPValue(this.maxDOPFromConfig);
    }


    private int defineCorrectMaxDOPValue(int value)
    {
        if(value < -1 || value > 32767)
        {
            throw new MaxDOPWrongValueException("MAXDOP value: " + value + " is not correct!");
        }
        return value;
    }


    private boolean isReadOnlyDataSourceUsed(HybrisDataSource dataSource)
    {
        return ((Boolean)this.readOnlyConditionsHelper.getReadOnlyDataSource(Registry.getCurrentTenant())
                        .map(ds -> Boolean.valueOf(ds.getID().equals(dataSource.getID())))
                        .orElse(Boolean.valueOf(false))).booleanValue();
    }


    private List<Hint> addMaxDOPValueToHints(List<Hint> hints, int maxDOPValue)
    {
        List<Hint> newHints = new ArrayList<>(hints);
        Objects.requireNonNull(SQLServerQueryHints.class);
        Objects.requireNonNull(SQLServerQueryHints.class);
        newHints.stream().filter(SQLServerQueryHints.class::isInstance).map(SQLServerQueryHints.class::cast)
                        .findFirst()
                        .ifPresentOrElse(h -> h.addMaxDOPHint(maxDOPValue), () -> newHints.add(createNewMaxDOPHint(maxDOPValue)));
        return Collections.unmodifiableList(newHints);
    }


    private SQLServerQueryHints createNewMaxDOPHint(int maxDOPValue)
    {
        SQLServerQueryHints newSqlServerQueryHint = SQLServerQueryHints.create();
        newSqlServerQueryHint.addMaxDOPHint(maxDOPValue);
        return newSqlServerQueryHint;
    }


    private void registerConfigChangeListener()
    {
        this.configChangeListener = ((key, newValue) -> {
            if(key.equals("flexiblesearch.hints.sqlserver.maxdop"))
            {
                this.maxDOPFromConfig = convertMaxDOPValueToInt(newValue);
            }
        });
        this.tenant.getConfig().registerConfigChangeListener(this.configChangeListener);
    }


    private int getInitialMaxDOPValueFromConfig()
    {
        return convertMaxDOPValueToInt(Config.getString("flexiblesearch.hints.sqlserver.maxdop",
                        String.valueOf(-1)));
    }


    private int convertMaxDOPValueToInt(String maxDOPValue)
    {
        try
        {
            return Integer.parseInt(maxDOPValue.trim());
        }
        catch(NumberFormatException | NullPointerException e)
        {
            Config.setParameter("flexiblesearch.hints.sqlserver.maxdop", String.valueOf(this.maxDOPFromConfig));
            throw new MaxDOPWrongValueException("MaxDOP Value is not correct:" + e.getMessage());
        }
    }
}
