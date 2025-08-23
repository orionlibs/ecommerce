package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.acceleratorservices.email.dao.EmailAddressDao;
import de.hybris.platform.acceleratorservices.model.email.EmailAddressModel;
import de.hybris.platform.warehousing.util.builder.EmailAddressModelBuilder;
import org.springframework.beans.factory.annotation.Required;

public class EmailAddresses extends AbstractItems<EmailAddressModel>
{
    public static final String DISPLAYED_NAME = "polo";
    public static final String EMAIL_ADDRESS = "polo@polo.com";
    private EmailAddressDao emailAddressDao;


    public EmailAddressModel polo()
    {
        return getOrCreateEmail("polo@polo.com", "polo");
    }


    protected EmailAddressModel getOrCreateEmail(String name, String email)
    {
        return (EmailAddressModel)getOrSaveAndReturn(() -> getEmailAddressDao().findEmailAddressByEmailAndDisplayName(email, name), () -> EmailAddressModelBuilder.aModel().withDisplayedName("polo").withEmailAddress("polo@polo.com").build());
    }


    public EmailAddressDao getEmailAddressDao()
    {
        return this.emailAddressDao;
    }


    @Required
    public void setEmailAddressDao(EmailAddressDao emailAddressDao)
    {
        this.emailAddressDao = emailAddressDao;
    }
}
