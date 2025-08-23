package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

public class SnSynonymDictionaryModel extends ItemModel
{
    public static final String _TYPECODE = "SnSynonymDictionary";
    public static final String _SNINDEXCONFIGURATION2SYNONYMDICTIONARY = "SnIndexConfiguration2SynonymDictionary";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String LANGUAGES = "languages";
    public static final String INDEXCONFIGURATIONS = "indexConfigurations";
    public static final String ENTRIES = "entries";


    public SnSynonymDictionaryModel()
    {
    }


    public SnSynonymDictionaryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SnSynonymDictionaryModel(String _id)
    {
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SnSynonymDictionaryModel(String _id, ItemModel _owner)
    {
        setId(_id);
        setOwner(_owner);
    }


    @Accessor(qualifier = "entries", type = Accessor.Type.GETTER)
    public List<SnSynonymEntryModel> getEntries()
    {
        return (List<SnSynonymEntryModel>)getPersistenceContext().getPropertyValue("entries");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "indexConfigurations", type = Accessor.Type.GETTER)
    public List<SnIndexConfigurationModel> getIndexConfigurations()
    {
        return (List<SnIndexConfigurationModel>)getPersistenceContext().getPropertyValue("indexConfigurations");
    }


    @Accessor(qualifier = "languages", type = Accessor.Type.GETTER)
    public List<LanguageModel> getLanguages()
    {
        return (List<LanguageModel>)getPersistenceContext().getPropertyValue("languages");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "entries", type = Accessor.Type.SETTER)
    public void setEntries(List<SnSynonymEntryModel> value)
    {
        getPersistenceContext().setPropertyValue("entries", value);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "indexConfigurations", type = Accessor.Type.SETTER)
    public void setIndexConfigurations(List<SnIndexConfigurationModel> value)
    {
        getPersistenceContext().setPropertyValue("indexConfigurations", value);
    }


    @Accessor(qualifier = "languages", type = Accessor.Type.SETTER)
    public void setLanguages(List<LanguageModel> value)
    {
        getPersistenceContext().setPropertyValue("languages", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }
}
