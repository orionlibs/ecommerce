package de.hybris.platform.acceleratorservices.model.process;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commerceservices.model.process.StoreFrontCustomerProcessModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SavedCartFileUploadProcessModel extends StoreFrontCustomerProcessModel
{
    public static final String _TYPECODE = "SavedCartFileUploadProcess";
    public static final String UPLOADEDFILE = "uploadedFile";
    public static final String SAVEDCART = "savedCart";


    public SavedCartFileUploadProcessModel()
    {
    }


    public SavedCartFileUploadProcessModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SavedCartFileUploadProcessModel(String _code, String _processDefinitionName)
    {
        setCode(_code);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SavedCartFileUploadProcessModel(String _code, ItemModel _owner, String _processDefinitionName)
    {
        setCode(_code);
        setOwner(_owner);
        setProcessDefinitionName(_processDefinitionName);
    }


    @Accessor(qualifier = "savedCart", type = Accessor.Type.GETTER)
    public CartModel getSavedCart()
    {
        return (CartModel)getPersistenceContext().getPropertyValue("savedCart");
    }


    @Accessor(qualifier = "uploadedFile", type = Accessor.Type.GETTER)
    public MediaModel getUploadedFile()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("uploadedFile");
    }


    @Accessor(qualifier = "savedCart", type = Accessor.Type.SETTER)
    public void setSavedCart(CartModel value)
    {
        getPersistenceContext().setPropertyValue("savedCart", value);
    }


    @Accessor(qualifier = "uploadedFile", type = Accessor.Type.SETTER)
    public void setUploadedFile(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("uploadedFile", value);
    }
}
