package de.hybris.platform.persistence.polyglot;

import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;

public class ChangeParentOfEntityFeature implements PolyglotFeature
{
    private final SingleAttributeKey key;
    private final Identity type;


    public ChangeParentOfEntityFeature(Identity type, SingleAttributeKey key)
    {
        this.key = key;
        this.type = type;
    }


    public SingleAttributeKey getKey()
    {
        return this.key;
    }


    public Identity getType()
    {
        return this.type;
    }
}
