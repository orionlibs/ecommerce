package de.hybris.platform.warehousing.allocation.decline.action;

import de.hybris.platform.warehousing.data.allocation.DeclineEntry;
import java.util.Collection;

public interface DeclineActionStrategy
{
    void execute(DeclineEntry paramDeclineEntry);


    void execute(Collection<DeclineEntry> paramCollection);
}
