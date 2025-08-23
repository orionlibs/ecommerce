package de.hybris.platform.warehousing.util.builder;

import com.google.common.collect.Lists;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.store.BaseStoreModel;
import java.util.Locale;

public class LanguageModelBuilder
{
    private final LanguageModel model = new LanguageModel();


    private LanguageModel getModel()
    {
        return this.model;
    }


    public static LanguageModelBuilder aModel()
    {
        return new LanguageModelBuilder();
    }


    public LanguageModel build()
    {
        return getModel();
    }


    public LanguageModelBuilder withIsocode(String isocode)
    {
        getModel().setIsocode(isocode);
        return this;
    }


    public LanguageModelBuilder withName(String name, Locale locale)
    {
        getModel().setName(name, locale);
        return this;
    }


    public LanguageModelBuilder withActive(Boolean active)
    {
        getModel().setActive(active);
        return this;
    }


    public LanguageModelBuilder withBaseStores(BaseStoreModel... baseStores)
    {
        getModel().setBaseStores(Lists.newArrayList((Object[])baseStores));
        return this;
    }
}
