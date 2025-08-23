package de.hybris.platform.inboundservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.integrationservices.enums.HttpMethod;
import de.hybris.platform.integrationservices.enums.IntegrationRequestStatus;
import de.hybris.platform.integrationservices.model.MonitoredRequestModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class InboundRequestModel extends MonitoredRequestModel
{
    public static final String _TYPECODE = "InboundRequest";
    public static final String ERRORS = "errors";


    public InboundRequestModel()
    {
    }


    public InboundRequestModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public InboundRequestModel(IntegrationRequestStatus _status, String _type)
    {
        setStatus(_status);
        setType(_type);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public InboundRequestModel(HttpMethod _httpMethod, ItemModel _owner, String _sapPassport, IntegrationRequestStatus _status, String _type, UserModel _user)
    {
        setHttpMethod(_httpMethod);
        setOwner(_owner);
        setSapPassport(_sapPassport);
        setStatus(_status);
        setType(_type);
        setUser(_user);
    }


    @Accessor(qualifier = "errors", type = Accessor.Type.GETTER)
    public Set<InboundRequestErrorModel> getErrors()
    {
        return (Set<InboundRequestErrorModel>)getPersistenceContext().getPropertyValue("errors");
    }


    @Accessor(qualifier = "errors", type = Accessor.Type.SETTER)
    public void setErrors(Set<InboundRequestErrorModel> value)
    {
        getPersistenceContext().setPropertyValue("errors", value);
    }
}
