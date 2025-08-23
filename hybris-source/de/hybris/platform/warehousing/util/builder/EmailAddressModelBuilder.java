package de.hybris.platform.warehousing.util.builder;

import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;

public class EmailAddressModelBuilder
{
    private final EmailAddressModel model = new EmailAddressModel();


    private EmailAddressModel getModel()
    {
        return this.model;
    }


    public static EmailAddressModelBuilder aModel()
    {
        return new EmailAddressModelBuilder();
    }


    public EmailAddressModel build()
    {
        return getModel();
    }


    public EmailAddressModelBuilder withDisplayedName(String name)
    {
        getModel().setDisplayName(name);
        return this;
    }


    public EmailAddressModelBuilder withEmailAddress(String email)
    {
        getModel().setEmailAddress(email);
        return this;
    }
}
