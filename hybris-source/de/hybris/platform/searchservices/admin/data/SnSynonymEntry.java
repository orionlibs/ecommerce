package de.hybris.platform.searchservices.admin.data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SnSynonymEntry implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private List<String> input;
    private List<String> synonyms;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setInput(List<String> input)
    {
        this.input = input;
    }


    public List<String> getInput()
    {
        return this.input;
    }


    public void setSynonyms(List<String> synonyms)
    {
        this.synonyms = synonyms;
    }


    public List<String> getSynonyms()
    {
        return this.synonyms;
    }


    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }
        if(o == this)
        {
            return true;
        }
        if(getClass() != o.getClass())
        {
            return false;
        }
        SnSynonymEntry other = (SnSynonymEntry)o;
        return (Objects.equals(getId(), other.getId()) &&
                        Objects.equals(getInput(), other.getInput()) &&
                        Objects.equals(getSynonyms(), other.getSynonyms()));
    }


    public int hashCode()
    {
        int result = 1;
        Object<String> attribute = (Object<String>)this.id;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<String>)this.input;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        List<String> list = this.synonyms;
        result = 31 * result + ((list == null) ? 0 : list.hashCode());
        return result;
    }
}
