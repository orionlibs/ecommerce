package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.localization.Localization;
import java.text.MessageFormat;
import org.apache.commons.lang.StringUtils;

public class PromotionVoucher extends GeneratedPromotionVoucher
{
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        String voucherCode = (String)allAttributes.get("voucherCode");
        if(StringUtils.isNotBlank(voucherCode))
        {
            Voucher voucher = VoucherManager.getInstance(getSession()).getVoucher(voucherCode);
            if(voucher != null)
            {
                throw new JaloInvalidParameterException(
                                MessageFormat.format(Localization.getLocalizedString("type.promotionvoucher.error.vouchercode.not.unique"), new Object[] {voucherCode, voucher
                                                .getName()}), 0);
            }
        }
        return super.createItem(ctx, type, allAttributes);
    }


    public boolean checkVoucherCode(String aVoucherCode)
    {
        return aVoucherCode.equals(getVoucherCode());
    }


    protected int getNextVoucherNumber(SessionContext ctx)
    {
        return 1;
    }


    public boolean isReservable(String aVoucherCode, User user)
    {
        return (getInvalidations(aVoucherCode, user).size() < getRedemptionQuantityLimitPerUserAsPrimitive() &&
                        getInvalidations(aVoucherCode).size() < getRedemptionQuantityLimitAsPrimitive());
    }


    public void setVoucherCode(SessionContext ctx, String param)
    {
        if(StringUtils.isNotBlank(param))
        {
            Voucher voucher = VoucherManager.getInstance(getSession()).getVoucher(param);
            if(voucher != null && voucher != this)
            {
                throw new JaloInvalidParameterException(
                                MessageFormat.format(Localization.getLocalizedString("type.promotionvoucher.error.vouchercode.not.unique"), new Object[] {param, voucher
                                                .getName()}), 0);
            }
        }
        super.setVoucherCode(ctx, param);
    }
}
