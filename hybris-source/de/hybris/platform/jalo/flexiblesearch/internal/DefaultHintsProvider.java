package de.hybris.platform.jalo.flexiblesearch.internal;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.hints.Hint;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import java.util.List;

public class DefaultHintsProvider implements FlexibleSearchHintsProvider
{
    public List<Hint> injectFlexibleSearchHints(SessionContext localCtx, List<Hint> hints, HybrisDataSource dataSource)
    {
        return hints;
    }
}
