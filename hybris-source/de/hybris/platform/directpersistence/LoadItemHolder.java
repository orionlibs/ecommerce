package de.hybris.platform.directpersistence;

import de.hybris.platform.core.PK;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class LoadItemHolder
{
    private final PK pk;
    private final String type;
    private final Set<String> unlocalizedAttributesToLoad;
    private final Map<String, Set<Locale>> localizedAttributesToLoad;


    public LoadItemHolder(PK pk, String type, Set<String> unlocalizedAttributesToLoad, Map<String, Set<Locale>> localizedAttributesToLoad)
    {
        this.pk = pk;
        this.type = type;
        this.unlocalizedAttributesToLoad = unlocalizedAttributesToLoad;
        this.localizedAttributesToLoad = localizedAttributesToLoad;
    }


    public PK getPk()
    {
        return this.pk;
    }


    public String getType()
    {
        return this.type;
    }


    public Set<String> getUnlocalizedAttributesToLoad()
    {
        return this.unlocalizedAttributesToLoad;
    }


    public Map<String, Set<Locale>> getLocalizedAttributesToLoad()
    {
        return this.localizedAttributesToLoad;
    }
}
