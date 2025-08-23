package de.hybris.platform.voucher.jalo;

import de.hybris.platform.jalo.ConsistencyCheckAction;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public class SerialVoucher extends GeneratedSerialVoucher
{
    private static final Logger LOG = Logger.getLogger(SerialVoucher.class.getName());
    private static final String LASTVOUCHERNUMBER = "lastVoucherNumber";


    public boolean checkVoucherCode(String aVoucherCode)
    {
        String pattern = ".{3}-.{4}-.{4}-.{4}";
        boolean valid = Pattern.matches(".{3}-.{4}-.{4}-.{4}", aVoucherCode);
        if(!valid)
        {
            LOG.warn("Pattern of voucher code not valid: [" + aVoucherCode + "]");
            return false;
        }
        String voucherCode = removeDividers(aVoucherCode);
        String code = extractCode(voucherCode);
        if(getCode().equals(code))
        {
            String clearText = voucherCode.substring(0, voucherCode.length() - 6);
            String sig = voucherCode.substring(voucherCode.length() - 6);
            try
            {
                if(threeByteSig(clearText).equals(sig))
                {
                    int lastVoucherNumber = getLastVoucherNumber(getSession().getSessionContext());
                    try
                    {
                        int voucherNumber = getVoucherNumber(voucherCode);
                        return (0 <= voucherNumber && voucherNumber <= lastVoucherNumber);
                    }
                    catch(InvalidVoucherKeyException e)
                    {
                        return false;
                    }
                }
            }
            catch(NoSuchAlgorithmException e)
            {
                throw new JaloSystemException(e, "!!", 0);
            }
        }
        return false;
    }


    public static String extractCode(String aVoucherCode)
    {
        String voucherCode = removeDividers(aVoucherCode);
        if(voucherCode.length() > 3)
        {
            return voucherCode.substring(0, 3);
        }
        return null;
    }


    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        allAttributes.addConsistencyCheck((ConsistencyCheckAction)new Object(this, type));
        return super.createItem(ctx, type, allAttributes);
    }


    private int getLastVoucherNumber(SessionContext ctx)
    {
        Object lastVoucherNumber = getProperty(ctx, "lastVoucherNumber");
        if(lastVoucherNumber != null)
        {
            return ((Integer)lastVoucherNumber).intValue();
        }
        return -1;
    }


    protected int getNextVoucherNumber(SessionContext ctx)
    {
        int result = getLastVoucherNumber(ctx) + 1;
        setProperty(ctx, "lastVoucherNumber", Integer.valueOf(result));
        return result;
    }


    public boolean isReservable(String aVoucherCode, User user)
    {
        return getInvalidations(aVoucherCode).isEmpty();
    }


    public void setCodes(Collection param)
    {
        setProperty("codes", param);
    }
}
