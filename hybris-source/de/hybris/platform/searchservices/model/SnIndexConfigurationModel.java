package de.hybris.platform.searchservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

public class SnIndexConfigurationModel extends ItemModel
{
    public static final String _TYPECODE = "SnIndexConfiguration";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String USER = "user";
    public static final String LISTENERS = "listeners";
    public static final String LANGUAGES = "languages";
    public static final String CURRENCIES = "currencies";
    public static final String SEARCHPROVIDERCONFIGURATION = "searchProviderConfiguration";
    public static final String INDEXTYPES = "indexTypes";
    public static final String SYNONYMDICTIONARIES = "synonymDictionaries";


    public SnIndexConfigurationModel()
    {
    }


    public SnIndexConfigurationModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SnIndexConfigurationModel(String _id)
    {
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SnIndexConfigurationModel(String _id, ItemModel _owner)
    {
        setId(_id);
        setOwner(_owner);
    }


    @Accessor(qualifier = "currencies", type = Accessor.Type.GETTER)
    public List<CurrencyModel> getCurrencies()
    {
        return (List<CurrencyModel>)getPersistenceContext().getPropertyValue("currencies");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "indexTypes", type = Accessor.Type.GETTER)
    public List<SnIndexTypeModel> getIndexTypes()
    {
        return (List<SnIndexTypeModel>)getPersistenceContext().getPropertyValue("indexTypes");
    }


    @Accessor(qualifier = "languages", type = Accessor.Type.GETTER)
    public List<LanguageModel> getLanguages()
    {
        return (List<LanguageModel>)getPersistenceContext().getPropertyValue("languages");
    }


    @Accessor(qualifier = "listeners", type = Accessor.Type.GETTER)
    public List<String> getListeners()
    {
        return (List<String>)getPersistenceContext().getPropertyValue("listeners");
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


    @Accessor(qualifier = "searchProviderConfiguration", type = Accessor.Type.GETTER)
    public AbstractSnSearchProviderConfigurationModel getSearchProviderConfiguration()
    {
        return (AbstractSnSearchProviderConfigurationModel)getPersistenceContext().getPropertyValue("searchProviderConfiguration");
    }


    @Accessor(qualifier = "synonymDictionaries", type = Accessor.Type.GETTER)
    public List<SnSynonymDictionaryModel> getSynonymDictionaries()
    {
        return (List<SnSynonymDictionaryModel>)getPersistenceContext().getPropertyValue("synonymDictionaries");
    }


    @Accessor(qualifier = "user", type = Accessor.Type.GETTER)
    public UserModel getUser()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("user");
    }


    @Accessor(qualifier = "currencies", type = Accessor.Type.SETTER)
    public void setCurrencies(List<CurrencyModel> value)
    {
        getPersistenceContext().setPropertyValue("currencies", value);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "indexTypes", type = Accessor.Type.SETTER)
    public void setIndexTypes(List<SnIndexTypeModel> value)
    {
        getPersistenceContext().setPropertyValue("indexTypes", value);
    }


    @Accessor(qualifier = "languages", type = Accessor.Type.SETTER)
    public void setLanguages(List<LanguageModel> value)
    {
        getPersistenceContext().setPropertyValue("languages", value);
    }


    @Accessor(qualifier = "listeners", type = Accessor.Type.SETTER)
    public void setListeners(List<String> value)
    {
        getPersistenceContext().setPropertyValue("listeners", value);
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


    @Accessor(qualifier = "searchProviderConfiguration", type = Accessor.Type.SETTER)
    public void setSearchProviderConfiguration(AbstractSnSearchProviderConfigurationModel value)
    {
        getPersistenceContext().setPropertyValue("searchProviderConfiguration", value);
    }


    @Accessor(qualifier = "synonymDictionaries", type = Accessor.Type.SETTER)
    public void setSynonymDictionaries(List<SnSynonymDictionaryModel> value)
    {
        getPersistenceContext().setPropertyValue("synonymDictionaries", value);
    }


    @Accessor(qualifier = "user", type = Accessor.Type.SETTER)
    public void setUser(UserModel value)
    {
        getPersistenceContext().setPropertyValue("user", value);
    }
}
