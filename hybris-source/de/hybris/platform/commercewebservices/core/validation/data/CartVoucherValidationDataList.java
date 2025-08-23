package de.hybris.platform.commercewebservices.core.validation.data;

import java.io.Serializable;
import java.util.List;

public class CartVoucherValidationDataList implements Serializable
{
    private static final long serialVersionUID = 1L;
    private List<CartVoucherValidationData> CartVoucherValidationDataList;


    public void setCartVoucherValidationDataList(List<CartVoucherValidationData> list)
    {
        this.CartVoucherValidationDataList = list;
    }


    public List<CartVoucherValidationData> getCartVoucherValidationDataList()
    {
        return this.CartVoucherValidationDataList;
    }
}
