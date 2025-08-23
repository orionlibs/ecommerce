package de.hybris.platform.persistence.flexiblesearch.polyglot;

import de.hybris.platform.persistence.polyglot.model.ChangeSet;
import de.hybris.platform.persistence.polyglot.model.ItemState;
import de.hybris.platform.persistence.polyglot.model.Key;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;
import java.util.Map;

public class FlexibleSearchResultItemState implements ItemState
{
    private final Map<SingleAttributeKey, Object> values;


    public FlexibleSearchResultItemState(Map<SingleAttributeKey, Object> values)
    {
        this.values = values;
    }


    public <T> T get(Key key)
    {
        return (T)this.values.get(key);
    }


    public ChangeSet beginModification()
    {
        throw new UnsupportedOperationException("This is a readonly implementation");
    }
}
