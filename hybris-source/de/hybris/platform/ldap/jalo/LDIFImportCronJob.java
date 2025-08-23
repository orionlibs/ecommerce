package de.hybris.platform.ldap.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import org.apache.log4j.Logger;

public class LDIFImportCronJob extends GeneratedLDIFImportCronJob
{
    private static final Logger LOG = Logger.getLogger(LDIFImportCronJob.class.getName());


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Item item = super.createItem(ctx, type, allAttributes);
        return item;
    }


    public void setConfigFile2(SessionContext ctx, ConfigurationMedia param)
    {
        setConfigFile(ctx, param);
    }


    public ConfigurationMedia getConfigFile2(SessionContext ctx)
    {
        return getConfigFile(ctx);
    }
}
