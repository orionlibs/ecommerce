package de.hybris.platform.commons.translator.renderers;

import de.hybris.platform.commons.translator.Translator;
import de.hybris.platform.commons.translator.nodes.AbstractNode;

public class ReplaceRenderer extends AbstractRenderer
{
    public String renderTextFromNode(AbstractNode node, Translator translator)
    {
        String text = "";
        if(this.start != null)
        {
            text = text + text;
        }
        text = text + text;
        if(this.end != null)
        {
            text = text + text;
        }
        return text;
    }
}
