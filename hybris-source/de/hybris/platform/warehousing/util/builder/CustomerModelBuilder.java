package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.core.model.user.CustomerModel;

public class CustomerModelBuilder
{
    private final CustomerModel model = new CustomerModel();


    private CustomerModel getModel()
    {
        return this.model;
    }


    public static CustomerModelBuilder aModel()
    {
        return new CustomerModelBuilder();
    }


    public CustomerModel build()
    {
        return getModel();
    }


    public CustomerModelBuilder withUid(String uid)
    {
        getModel().setUid(uid);
        return this;
    }


    public CustomerModelBuilder withName(String name)
    {
        getModel().setName(name);
        return this;
    }
}
