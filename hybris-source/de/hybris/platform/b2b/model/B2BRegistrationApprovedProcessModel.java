package de.hybris.platform.b2b.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class B2BRegistrationApprovedProcessModel extends B2BRegistrationProcessModel
{
    public static final String _TYPECODE = "B2BRegistrationApprovedProcess";
    public static final String PASSWORDRESETTOKEN = "passwordResetToken";


    public B2BRegistrationApprovedProcessModel()
    {
    }


    public B2BRegistrationApprovedProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BRegistrationApprovedProcessModel(String _code, String _processDefinitionName)
    {
        setCode(_code);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public B2BRegistrationApprovedProcessModel(String _code, ItemModel _owner, String _processDefinitionName)
    {
        setCode(_code);
        setOwner(_owner);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Accessor(qualifier = "passwordResetToken", type = Accessor.Type.GETTER)
    public String getPasswordResetToken()
    {
        return (String)getPersistenceContext().getPropertyValue("passwordResetToken");
    }


    @Accessor(qualifier = "passwordResetToken", type = Accessor.Type.SETTER)
    public void setPasswordResetToken(String value)
    {
        getPersistenceContext().setPropertyValue("passwordResetToken", value);
    }
}
