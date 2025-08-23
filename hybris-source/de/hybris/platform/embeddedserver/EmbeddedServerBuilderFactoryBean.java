package de.hybris.platform.embeddedserver;

import de.hybris.platform.embeddedserver.api.EmbeddedServerBuilder;
import java.util.List;
import org.springframework.beans.factory.FactoryBean;

public class EmbeddedServerBuilderFactoryBean implements FactoryBean<EmbeddedServerBuilder>
{
    private List<EmbeddedServerBuilder> availableBuilders;
    private static final EmbeddedServerBuilder NULL_BUILDER = (EmbeddedServerBuilder)new NullEmbeddedServerBuilder();


    public EmbeddedServerBuilder getObject() throws Exception
    {
        if(this.availableBuilders == null || this.availableBuilders.isEmpty())
        {
            return NULL_BUILDER;
        }
        return this.availableBuilders.stream().filter(b -> b.isAvailable()).findFirst().orElse(NULL_BUILDER);
    }


    public Class<?> getObjectType()
    {
        return EmbeddedServerBuilder.class;
    }


    public boolean isSingleton()
    {
        return false;
    }


    public void setAvailableBuilders(List<EmbeddedServerBuilder> availableBuilders)
    {
        this.availableBuilders = availableBuilders;
    }
}
