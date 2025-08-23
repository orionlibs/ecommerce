package de.hybris.platform.persistence.polyglot.model;

import java.util.Objects;

public final class LocalizedKey extends SingleAttributeKey
{
    private final String langCode;


    LocalizedKey(String qualifier, String langCode)
    {
        super(qualifier);
        this.langCode = Objects.<String>requireNonNull(langCode);
    }


    public String getLanguageCode()
    {
        return this.langCode;
    }


    public boolean equals(Object obj)
    {
        if(obj == null || obj == this || !(obj instanceof LocalizedKey))
        {
            return (obj == this);
        }
        LocalizedKey other = (LocalizedKey)obj;
        return (other.qualifier.equals(this.qualifier) && other.langCode.equals(this.langCode));
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.qualifier, this.langCode});
    }


    public String toString()
    {
        return String.format("LocalizedKey(%s, %s)", new Object[] {this.qualifier, this.langCode});
    }
}
