package de.hybris.platform.commons.translator.renderers;

import de.hybris.platform.commons.translator.Translator;
import de.hybris.platform.commons.translator.nodes.AbstractNode;

public class SimpleRenderer extends AbstractRenderer
{
    public String renderTextFromNode(AbstractNode node, Translator translator)
    {
        return translator.renderTextFromNode(node);
    }
}
