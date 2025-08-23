package de.hybris.platform.searchservices.spi.data;

import de.hybris.platform.searchservices.admin.data.SnIndexConfiguration;
import de.hybris.platform.searchservices.admin.data.SnIndexType;
import de.hybris.platform.searchservices.admin.data.SnSynonymDictionary;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SnExportConfiguration implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<SnSynonymDictionary> synonymDictionaries;
    private SnIndexConfiguration indexConfiguration;
    private List<SnIndexType> indexTypes;


    public void setSynonymDictionaries(List<SnSynonymDictionary> synonymDictionaries)
    {
        this.synonymDictionaries = synonymDictionaries;
    }


    public List<SnSynonymDictionary> getSynonymDictionaries()
    {
        return this.synonymDictionaries;
    }


    public void setIndexConfiguration(SnIndexConfiguration indexConfiguration)
    {
        this.indexConfiguration = indexConfiguration;
    }


    public SnIndexConfiguration getIndexConfiguration()
    {
        return this.indexConfiguration;
    }


    public void setIndexTypes(List<SnIndexType> indexTypes)
    {
        this.indexTypes = indexTypes;
    }


    public List<SnIndexType> getIndexTypes()
    {
        return this.indexTypes;
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
        SnExportConfiguration other = (SnExportConfiguration)o;
        return (Objects.equals(getSynonymDictionaries(), other.getSynonymDictionaries()) &&
                        Objects.equals(getIndexConfiguration(), other.getIndexConfiguration()) &&
                        Objects.equals(getIndexTypes(), other.getIndexTypes()));
    }


    public int hashCode()
    {
        int result = 1;
        Object<SnSynonymDictionary> attribute = (Object<SnSynonymDictionary>)this.synonymDictionaries;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        SnIndexConfiguration snIndexConfiguration = this.indexConfiguration;
        result = 31 * result + ((snIndexConfiguration == null) ? 0 : snIndexConfiguration.hashCode());
        List<SnIndexType> list = this.indexTypes;
        result = 31 * result + ((list == null) ? 0 : list.hashCode());
        return result;
    }
}
