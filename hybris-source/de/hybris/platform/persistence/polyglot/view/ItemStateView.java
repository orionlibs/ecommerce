package de.hybris.platform.persistence.polyglot.view;

import de.hybris.platform.persistence.polyglot.model.Key;
import de.hybris.platform.util.RelationsInfo;

public interface ItemStateView
{
    <T> T get(Key paramKey);


    default <T> T get(RelationsInfo relationsInfo)
    {
        return null;
    }
}
