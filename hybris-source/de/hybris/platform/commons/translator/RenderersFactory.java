package de.hybris.platform.commons.translator;

import de.hybris.platform.commons.translator.renderers.AbstractRenderer;
import java.util.Set;

public interface RenderersFactory
{
    AbstractRenderer get(String paramString);


    Set<String> keySet();
}
