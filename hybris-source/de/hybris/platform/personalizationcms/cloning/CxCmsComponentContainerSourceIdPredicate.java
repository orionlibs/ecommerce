package de.hybris.platform.personalizationcms.cloning;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import java.util.UUID;
import java.util.function.BiPredicate;
import java.util.function.Supplier;

public class CxCmsComponentContainerSourceIdPredicate implements BiPredicate<ItemModel, String>, Supplier<String>
{
    private static final String KEEP_SOURCE_ID = "personalizationcms.containers.deepclone.keep";
    private ConfigurationService configurationService;


    public boolean test(ItemModel item, String attribute)
    {
        return (item instanceof de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel && "sourceId".equals(attribute) &&
                        provideDefaultValue());
    }


    private boolean provideDefaultValue()
    {
        boolean keepSourceId = this.configurationService.getConfiguration().getBoolean("personalizationcms.containers.deepclone.keep", true);
        return !keepSourceId;
    }


    public String get()
    {
        return UUID.randomUUID().toString();
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }
}
