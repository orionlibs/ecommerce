package de.hybris.platform.jalo.order.payment;

import de.hybris.platform.core.Constants;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.Config;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class PaymentInfo extends GeneratedPaymentInfo
{
    private static final Logger LOG = Logger.getLogger(PaymentInfo.class.getName());


    @SLDSafe(portingClass = "CreditCardPaymentInfoValidator, MandatoryAttributesValidator", portingMethod = "onValidate(final Object model, final InterceptorContext ctx)")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        if(!allAttributes.containsKey("code") || !allAttributes.containsKey("user"))
        {
            throw new JaloInvalidParameterException("Missing parameter to create a PaymentInfo! Code and User are mandatory", 0);
        }
        if(!(allAttributes.get("user") instanceof de.hybris.platform.jalo.user.User))
        {
            throw new JaloInvalidParameterException("Parameter user should be instance of User", 0);
        }
        allAttributes.setAttributeMode("code", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("user", Item.AttributeMode.INITIAL);
        if(Config.getBoolean("paymentinfo.creditcard.checknumber", true) && type
                        .getCode().equals(Constants.TYPES.CreditCardPaymentInfo))
        {
            plausabilityCheck((String)allAttributes.get("number"), (EnumerationValue)allAttributes
                            .get("type"));
        }
        return super.createItem(ctx, type, allAttributes);
    }


    protected void setAllAttributesInternal(SessionContext ctx, Map values) throws JaloInvalidParameterException, JaloSecurityException, JaloBusinessException
    {
        if(Config.getBoolean("paymentinfo.creditcard.checknumber", true) &&
                        getComposedType().getCode().equals(Constants.TYPES.CreditCardPaymentInfo))
        {
            if(values.containsKey("number") || values.containsKey("type"))
            {
                String number = (String)values.get("number");
                if(number == null)
                {
                    number = (String)getAttribute("number");
                }
                EnumerationValue type = (EnumerationValue)values.get("type");
                if(type == null)
                {
                    type = (EnumerationValue)getAttribute("type");
                }
                plausabilityCheck(number, type);
            }
        }
        super.setAllAttributesInternal(ctx, values);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void plausabilityCheck(String number, EnumerationValue type) throws JaloBusinessException
    {
        if(number == null || type == null)
        {
            throw new JaloBusinessException("number or type is null!");
        }
        if("visa".equals(type.getCode()) && !isVisa(number))
        {
            throw new JaloInvalidParameterException("The CreditCard number is not a valid VISA creditcard number!", 5532);
        }
        if("master".equals(type.getCode()) && !isMaster(number))
        {
            throw new JaloInvalidParameterException("The CreditCard number is not a valid Master creditcard number!", 5532);
        }
        if("diners".equals(type.getCode()) && !isDiners(number))
        {
            throw new JaloInvalidParameterException("The CreditCard number is not a valid Diners creditcard number!", 5532);
        }
        if("amex".equals(type.getCode()) && !isAmericanExpress(number))
        {
            throw new JaloInvalidParameterException("The CreditCard number is not a valid AmericanExpress creditcard number!", 5532);
        }
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return "PaymentInfo[" + getUser() + ":" + getCode() + "]";
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static synchronized String maskCreditCardNumber(String cn)
    {
        if(cn == null)
        {
            return cn;
        }
        Pattern pattern = Pattern.compile("[^0-9]", 2);
        Matcher matcher = pattern.matcher(cn);
        cn = matcher.replaceAll("");
        int len = cn.length();
        if(len < 13)
        {
            LOG.error("Invalid length ( shorter than 13 characters) of the submitted credit card number!");
        }
        pattern = Pattern.compile("[0-9]", 2);
        matcher = pattern.matcher(cn.substring(0, len - 4));
        String _cn = matcher.replaceAll("*");
        return _cn + _cn;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static synchronized String normalizeCreditCardNumber(String s)
    {
        Pattern pattern = Pattern.compile("[^0-9]", 2);
        Matcher matcher = pattern.matcher(s);
        return matcher.replaceAll("");
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static synchronized boolean isVisa(String s) throws JaloBusinessException
    {
        s = normalizeCreditCardNumber(s);
        if(checkRegEx("^4\\d{12}|\\d{15}", s))
        {
            return luhnTest(s);
        }
        return false;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static synchronized boolean isMaster(String s) throws JaloBusinessException
    {
        s = normalizeCreditCardNumber(s);
        if(checkRegEx("^5[1-5]\\d{14}$", s))
        {
            return luhnTest(s);
        }
        return false;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static synchronized boolean isDiners(String s) throws JaloBusinessException
    {
        s = normalizeCreditCardNumber(s);
        if(checkRegEx("^3[0,6,8]\\d{12}$", s))
        {
            return luhnTest(s);
        }
        return false;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static synchronized boolean isAmericanExpress(String s) throws JaloBusinessException
    {
        s = normalizeCreditCardNumber(s);
        if(checkRegEx("^3[4,7]\\d{13}$", s))
        {
            return luhnTest(s);
        }
        return false;
    }


    private static synchronized boolean checkRegEx(String p, String expr) throws JaloBusinessException
    {
        if(StringUtils.isBlank(expr))
        {
            return false;
        }
        Pattern pattern = Pattern.compile(p, 2);
        Matcher matcher = pattern.matcher(expr);
        boolean result = matcher.find();
        return result;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public static boolean luhnTest(String s) throws JaloBusinessException
    {
        if(s == null)
        {
            return false;
        }
        int len = s.length();
        int[] digits = new int[len];
        for(int i = 0; i < len; i++)
        {
            try
            {
                digits[i] = Integer.parseInt(s.substring(i, i + 1));
            }
            catch(NumberFormatException e)
            {
                LOG.error(e.getMessage());
                throw new JaloBusinessException(e.getMessage());
            }
        }
        int sum = 0;
        while(len > 0)
        {
            sum += digits[len - 1];
            len--;
            if(len > 0)
            {
                int digit = 2 * digits[len - 1];
                sum += (digit > 9) ? (digit - 9) : digit;
                len--;
            }
        }
        return (sum % 10 == 0);
    }


    @SLDSafe
    public Boolean isDuplicate(SessionContext ctx)
    {
        Boolean result = super.isDuplicate(ctx);
        return (result != null) ? result : Boolean.FALSE;
    }
}
