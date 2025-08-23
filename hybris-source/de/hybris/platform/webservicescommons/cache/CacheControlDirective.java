package de.hybris.platform.webservicescommons.cache;

public enum CacheControlDirective
{
    PUBLIC("public"),
    PRIVATE("private"),
    NO_CACHE("no-cache"),
    NO_STORE("no-store"),
    NO_TRANSFORM("no-transform"),
    MUST_REVALIDATE("must-revalidate"),
    PROXY_REVALIDATE("proxy-revalidate");
    private final String directive;


    CacheControlDirective(String directive)
    {
        this.directive = directive;
    }


    public String toString()
    {
        return this.directive;
    }
}
