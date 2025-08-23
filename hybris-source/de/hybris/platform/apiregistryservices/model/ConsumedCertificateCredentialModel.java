package de.hybris.platform.apiregistryservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class ConsumedCertificateCredentialModel extends AbstractCredentialModel
{
    public static final String _TYPECODE = "ConsumedCertificateCredential";
    public static final String CERTIFICATEDATA = "certificateData";
    public static final String PRIVATEKEY = "privateKey";


    public ConsumedCertificateCredentialModel()
    {
    }


    public ConsumedCertificateCredentialModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsumedCertificateCredentialModel(String _id)
    {
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ConsumedCertificateCredentialModel(String _id, ItemModel _owner)
    {
        setId(_id);
        setOwner(_owner);
    }


    @Accessor(qualifier = "certificateData", type = Accessor.Type.GETTER)
    public String getCertificateData()
    {
        return (String)getPersistenceContext().getPropertyValue("certificateData");
    }


    @Accessor(qualifier = "privateKey", type = Accessor.Type.GETTER)
    public String getPrivateKey()
    {
        return (String)getPersistenceContext().getPropertyValue("privateKey");
    }


    @Accessor(qualifier = "certificateData", type = Accessor.Type.SETTER)
    public void setCertificateData(String value)
    {
        getPersistenceContext().setPropertyValue("certificateData", value);
    }


    @Accessor(qualifier = "privateKey", type = Accessor.Type.SETTER)
    public void setPrivateKey(String value)
    {
        getPersistenceContext().setPropertyValue("privateKey", value);
    }
}
