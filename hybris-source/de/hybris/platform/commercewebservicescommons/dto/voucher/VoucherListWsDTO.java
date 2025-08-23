package de.hybris.platform.commercewebservicescommons.dto.voucher;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;

@ApiModel(value = "VoucherList", description = "Representation of a Voucher List")
public class VoucherListWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "vouchers", value = "List of vouchers")
    private List<VoucherWsDTO> vouchers;


    public void setVouchers(List<VoucherWsDTO> vouchers)
    {
        this.vouchers = vouchers;
    }


    public List<VoucherWsDTO> getVouchers()
    {
        return this.vouchers;
    }
}
