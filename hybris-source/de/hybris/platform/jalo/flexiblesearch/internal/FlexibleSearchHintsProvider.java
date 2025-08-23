package de.hybris.platform.jalo.flexiblesearch.internal;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.hints.Hint;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import java.util.List;

public interface FlexibleSearchHintsProvider
{
    List<Hint> injectFlexibleSearchHints(SessionContext paramSessionContext, List<Hint> paramList, HybrisDataSource paramHybrisDataSource);
}
