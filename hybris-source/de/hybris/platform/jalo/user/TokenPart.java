package de.hybris.platform.jalo.user;

public enum TokenPart
{
    USER("user"),
    LANGUAGE("language"),
    PASSWORD("password"),
    TTLTIMESTAMP("ttlTimestamp"),
    SALT("salt"),
    RANDOM_TOKEN("randomToken");
    private final String code;


    TokenPart(String code)
    {
        this.code = code;
    }


    public String getKey()
    {
        return this.code;
    }
}
