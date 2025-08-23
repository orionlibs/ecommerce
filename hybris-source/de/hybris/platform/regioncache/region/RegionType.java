package de.hybris.platform.regioncache.region;

public enum RegionType
{
    ALL_TYPES("__ALL_TYPES__"),
    NON_REGISTRABLE("__NO_QUERY__"),
    QUERY_CACHE_TYPE("__QUERY_CACHE__", true);
    private final String value;
    private boolean requiresRegistry;


    RegionType(String value)
    {
        this.value = value;
        this.requiresRegistry = false;
    }


    RegionType(String value, boolean requiresRegistry)
    {
        this.value = value;
        this.requiresRegistry = requiresRegistry;
    }


    public String value()
    {
        return this.value;
    }


    @Deprecated(since = "5.0", forRemoval = true)
    public boolean requiresRegistry()
    {
        return this.requiresRegistry;
    }
}
