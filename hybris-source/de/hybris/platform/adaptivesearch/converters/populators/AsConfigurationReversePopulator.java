package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsConfigurationReverseConverterContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsConfiguration;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.strategies.AsUidGenerator;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class AsConfigurationReversePopulator implements ContextAwarePopulator<AbstractAsConfiguration, AbstractAsConfigurationModel, AsConfigurationReverseConverterContext>
{
    private AsUidGenerator asUidGenerator;


    public void populate(AbstractAsConfiguration source, AbstractAsConfigurationModel target, AsConfigurationReverseConverterContext context)
    {
        target.setCatalogVersion(context.getCatalogVersion());
        if(StringUtils.isNotBlank(source.getUid()))
        {
            target.setUid(this.asUidGenerator.generateUid());
        }
    }


    public AsUidGenerator getAsUidGenerator()
    {
        return this.asUidGenerator;
    }


    @Required
    public void setAsUidGenerator(AsUidGenerator asUidGenerator)
    {
        this.asUidGenerator = asUidGenerator;
    }
}
