package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.comments.model.ComponentModel;
import de.hybris.platform.comments.model.DomainModel;

public class ComponentModelBuilder
{
    private final ComponentModel model = new ComponentModel();


    private ComponentModel getModel()
    {
        return this.model;
    }


    public static ComponentModelBuilder aModel()
    {
        return new ComponentModelBuilder();
    }


    public ComponentModel build()
    {
        return getModel();
    }


    public ComponentModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public ComponentModelBuilder withName(String name)
    {
        getModel().setName(name);
        return this;
    }


    public ComponentModelBuilder withDomain(DomainModel domain)
    {
        getModel().setDomain(domain);
        return this;
    }
}
