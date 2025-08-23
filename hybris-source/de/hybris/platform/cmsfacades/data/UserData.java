package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.Set;

public class UserData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String uid;
    private Set<String> readableLanguages;
    private Set<String> writeableLanguages;


    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public String getUid()
    {
        return this.uid;
    }


    public void setReadableLanguages(Set<String> readableLanguages)
    {
        this.readableLanguages = readableLanguages;
    }


    public Set<String> getReadableLanguages()
    {
        return this.readableLanguages;
    }


    public void setWriteableLanguages(Set<String> writeableLanguages)
    {
        this.writeableLanguages = writeableLanguages;
    }


    public Set<String> getWriteableLanguages()
    {
        return this.writeableLanguages;
    }
}
