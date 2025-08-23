package de.hybris.platform.category.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.product.jalo.AbstractConfiguratorSetting;
import de.hybris.platform.util.OneToManyHandler;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedConfigurationCategory extends Category
{
    public static final String CONFIGURATORSETTINGS = "configuratorSettings";
    protected static final OneToManyHandler<AbstractConfiguratorSetting> CONFIGURATORSETTINGSHANDLER = new OneToManyHandler(GeneratedCatalogConstants.TC.ABSTRACTCONFIGURATORSETTING, true, "configurationCategory", "configurationCategoryPOS", true, true, 2);
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(Category.DEFAULT_INITIAL_ATTRIBUTES);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public List<AbstractConfiguratorSetting> getConfiguratorSettings(SessionContext ctx)
    {
        return (List<AbstractConfiguratorSetting>)CONFIGURATORSETTINGSHANDLER.getValues(ctx, (Item)this);
    }


    public List<AbstractConfiguratorSetting> getConfiguratorSettings()
    {
        return getConfiguratorSettings(getSession().getSessionContext());
    }


    public void setConfiguratorSettings(SessionContext ctx, List<AbstractConfiguratorSetting> value)
    {
        CONFIGURATORSETTINGSHANDLER.setValues(ctx, (Item)this, value);
    }


    public void setConfiguratorSettings(List<AbstractConfiguratorSetting> value)
    {
        setConfiguratorSettings(getSession().getSessionContext(), value);
    }


    public void addToConfiguratorSettings(SessionContext ctx, AbstractConfiguratorSetting value)
    {
        CONFIGURATORSETTINGSHANDLER.addValue(ctx, (Item)this, (Item)value);
    }


    public void addToConfiguratorSettings(AbstractConfiguratorSetting value)
    {
        addToConfiguratorSettings(getSession().getSessionContext(), value);
    }


    public void removeFromConfiguratorSettings(SessionContext ctx, AbstractConfiguratorSetting value)
    {
        CONFIGURATORSETTINGSHANDLER.removeValue(ctx, (Item)this, (Item)value);
    }


    public void removeFromConfiguratorSettings(AbstractConfiguratorSetting value)
    {
        removeFromConfiguratorSettings(getSession().getSessionContext(), value);
    }
}
