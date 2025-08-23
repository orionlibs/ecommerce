package de.hybris.platform.commercewebservices.core.storesession.data;

import de.hybris.platform.commercefacades.storesession.data.LanguageData;
import java.io.Serializable;
import java.util.Collection;

public class LanguageDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Collection<LanguageData> languages;


    public void setLanguages(Collection<LanguageData> languages)
    {
        this.languages = languages;
    }


    public Collection<LanguageData> getLanguages()
    {
        return this.languages;
    }
}
