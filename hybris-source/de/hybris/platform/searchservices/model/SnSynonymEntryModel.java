package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;

public class SnSynonymEntryModel extends ItemModel
{
    public static final String _TYPECODE = "SnSynonymEntry";
    public static final String _SNSYNONYMDICTIONARY2SYNONYMENTRY = "SnSynonymDictionary2SynonymEntry";
    public static final String ID = "id";
    public static final String INPUT = "input";
    public static final String SYNONYMS = "synonyms";
    public static final String SYNONYMDICTIONARYPOS = "synonymDictionaryPOS";
    public static final String SYNONYMDICTIONARY = "synonymDictionary";


    public SnSynonymEntryModel()
    {
    }


    public SnSynonymEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SnSynonymEntryModel(String _id, SnSynonymDictionaryModel _synonymDictionary)
    {
        setId(_id);
        setSynonymDictionary(_synonymDictionary);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SnSynonymEntryModel(String _id, ItemModel _owner, SnSynonymDictionaryModel _synonymDictionary)
    {
        setId(_id);
        setOwner(_owner);
        setSynonymDictionary(_synonymDictionary);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "input", type = Accessor.Type.GETTER)
    public List<String> getInput()
    {
        return (List<String>)getPersistenceContext().getPropertyValue("input");
    }


    @Accessor(qualifier = "synonymDictionary", type = Accessor.Type.GETTER)
    public SnSynonymDictionaryModel getSynonymDictionary()
    {
        return (SnSynonymDictionaryModel)getPersistenceContext().getPropertyValue("synonymDictionary");
    }


    @Accessor(qualifier = "synonyms", type = Accessor.Type.GETTER)
    public List<String> getSynonyms()
    {
        return (List<String>)getPersistenceContext().getPropertyValue("synonyms");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "input", type = Accessor.Type.SETTER)
    public void setInput(List<String> value)
    {
        getPersistenceContext().setPropertyValue("input", value);
    }


    @Accessor(qualifier = "synonymDictionary", type = Accessor.Type.SETTER)
    public void setSynonymDictionary(SnSynonymDictionaryModel value)
    {
        getPersistenceContext().setPropertyValue("synonymDictionary", value);
    }


    @Accessor(qualifier = "synonyms", type = Accessor.Type.SETTER)
    public void setSynonyms(List<String> value)
    {
        getPersistenceContext().setPropertyValue("synonyms", value);
    }
}
