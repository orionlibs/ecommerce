package de.hybris.platform.voucher.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

public class SerialVoucherModel extends VoucherModel
{
    public static final String _TYPECODE = "SerialVoucher";
    public static final String CODES = "codes";


    public SerialVoucherModel()
    {
    }


    public SerialVoucherModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SerialVoucherModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SerialVoucherModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "codes", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getCodes()
    {
        return (Collection<MediaModel>)getPersistenceContext().getPropertyValue("codes");
    }


    @Accessor(qualifier = "codes", type = Accessor.Type.SETTER)
    public void setCodes(Collection<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("codes", value);
    }
}
