package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.type.ComposedTypeModel;

public class ComposedTypeModelBuilder
{
    private final ComposedTypeModel model = new ComposedTypeModel();


    public static ComposedTypeModelBuilder aModel()
    {
        return new ComposedTypeModelBuilder();
    }


    private ComposedTypeModel getModel()
    {
        return this.model;
    }


    public ComposedTypeModel build()
    {
        return getModel();
    }


    public ComposedTypeModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public ComposedTypeModelBuilder withName(String name)
    {
        getModel().setName(name);
        return this;
    }
}
