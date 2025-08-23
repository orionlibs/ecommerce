package de.hybris.platform.commons.translator;

import de.hybris.platform.commons.jalo.translator.JaloTranslatorConfiguration;
import de.hybris.platform.commons.jalo.translator.JaloVelocityRenderer;
import de.hybris.platform.commons.translator.renderers.AbstractRenderer;
import de.hybris.platform.commons.translator.renderers.VelocityRenderer;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class RenderersFactoryFromDB implements RenderersFactory
{
    private final Map<String, VelocityRenderer> renderersMap = new HashMap<>();


    public RenderersFactoryFromDB(JaloTranslatorConfiguration config, SessionContext ctx)
    {
        Properties properties = config.getRenderersPropertiesAsMap();
        Collection<JaloVelocityRenderer> renderers = config.getRenderers(ctx);
        for(JaloVelocityRenderer renderer : renderers)
        {
            this.renderersMap.put(renderer.getName(), createRenderer(renderer, properties));
        }
    }


    public AbstractRenderer get(String name)
    {
        return (AbstractRenderer)this.renderersMap.get(name);
    }


    public Set<String> keySet()
    {
        return this.renderersMap.keySet();
    }


    public VelocityRenderer createRenderer(JaloVelocityRenderer renderer, Properties properties)
    {
        VelocityRenderer velocityRenderer = new VelocityRenderer();
        velocityRenderer.setProperties(properties);
        velocityRenderer.setTemplate(renderer.getTemplate());
        return velocityRenderer;
    }
}
