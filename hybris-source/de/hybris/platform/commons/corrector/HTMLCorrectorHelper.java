package de.hybris.platform.commons.corrector;

import de.hybris.platform.commons.corrector.strategy.HTMLCorrectorReplaceStrategy;

public class HTMLCorrectorHelper
{
    public static HTMLCorrectorStrategy getStrategy(String strategy)
    {
        HTMLCorrectorReplaceStrategy hTMLCorrectorReplaceStrategy;
        HTMLCorrectorStrategy htmlCorrectorStrategy = null;
        if(strategy.equals("replace.strategy"))
        {
            hTMLCorrectorReplaceStrategy = new HTMLCorrectorReplaceStrategy();
        }
        return (HTMLCorrectorStrategy)hTMLCorrectorReplaceStrategy;
    }
}
