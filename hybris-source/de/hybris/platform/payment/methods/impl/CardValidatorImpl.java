package de.hybris.platform.payment.methods.impl;

import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.payment.commands.result.CardValidationError;
import de.hybris.platform.payment.commands.result.CardValidationResult;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.methods.CardValidator;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

public class CardValidatorImpl implements CardValidator
{
    private static final Logger LOG = Logger.getLogger(CardValidatorImpl.class.getName());
    public static final int CARD_NUMBER_FIRST_LOWER = 500000;
    public static final int CARD_NUMBER_FIRST_UPPER = 509999;
    public static final int CARD_NUMBER_SECOND_LOWER = 560000;
    public static final int CARD_NUMBER_SECOND_UPPER = 589999;
    public static final int CARD_NUMBER_THIRD_UPPER = 699999;
    public static final int CARD_NUMBER_THIRD_LOWER = 600000;
    public static final int CARD_NUMBER_FORTH_LOWER = 675900;
    public static final int CARD_NUMBER_FORTH_UPPER = 675999;
    public static final int CARD_NUMBER_FIFTH_LOWER = 676700;
    public static final int CARD_NUMBER_FIFTH_UPPER = 676799;
    private List<CreditCardType> supportedCardSchemes;
    private List<CreditCardType> exemptFromCv2 = new ArrayList<>();


    public boolean luhnCheck(String number)
    {
        if(number == null)
        {
            return false;
        }
        int[][] sumTable = {{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}, {0, 2, 4, 6, 8, 1, 3, 5, 7, 9}};
        int sum = 0;
        int flip = 0;
        for(int i = number.length() - 1; i >= 0; i--)
        {
            sum += sumTable[flip++ & 0x1][Character.digit(number.charAt(i), 10)];
        }
        return (sum % 10 == 0);
    }


    public boolean isInternationalMaestro(String cardNumber)
    {
        if(StringUtils.isBlank(cardNumber) || cardNumber.length() < 6)
        {
            return false;
        }
        int val = NumberUtils.toInt(cardNumber.substring(0, 6));
        return (inInternationalMaestroRange(val) &&
                        !inRange(val, 675900, 675999) &&
                        !inRange(val, 676700, 676799));
    }


    private boolean inInternationalMaestroRange(int val)
    {
        return (inRange(val, 500000, 509999) ||
                        inRange(val, 560000, 589999) ||
                        inRange(val, 600000, 699999));
    }


    protected boolean inRange(int num, int lower, int upper)
    {
        return (num >= lower && num <= upper);
    }


    public void setSupportedCardSchemes(List<CreditCardType> supportedCardSchemes)
    {
        this.supportedCardSchemes = supportedCardSchemes;
    }


    public List<CreditCardType> getSupportedCardSchemes()
    {
        return this.supportedCardSchemes;
    }


    public void setCv2ExemptCardSchemes(List<CreditCardType> cv2ExemptCardSchemes)
    {
        this.exemptFromCv2 = cv2ExemptCardSchemes;
    }


    public boolean isCardSchemeSupported(CreditCardType cardScheme)
    {
        return this.supportedCardSchemes.contains(cardScheme);
    }


    protected void validateName(CardValidationResult result, CardInfo cardInfo)
    {
        if(StringUtils.isBlank(cardInfo.getCardHolderFullName()) || (StringUtils.split(cardInfo.getCardHolderFullName())).length <= 1)
        {
            result.addValidationError(CardValidationError.INVALID_NAME);
        }
    }


    protected void validateCardScheme(CardValidationResult result, CardInfo cardInfo)
    {
        if(cardInfo.getCardType() == null)
        {
            result.addValidationError(CardValidationError.MISSING_CARD_SCHEME);
        }
        else if(!isCardSchemeSupported(cardInfo.getCardType()))
        {
            result.addValidationError(CardValidationError.UNSUPPORTED_CARD_SCHEME);
        }
    }


    protected void validateCardNumber(CardValidationResult validationResult, CardInfo cardInfo)
    {
        if(StringUtils.isNumeric(StringUtils.strip(cardInfo.getCardNumber())))
        {
            if(Boolean.parseBoolean(Config.getParameter("validation.luhncheck")))
            {
                if(!isLuhnCheckCompliant(cardInfo))
                {
                    validationResult.addValidationError(CardValidationError.LUHN_CHECK_FAILED);
                }
            }
            else
            {
                LOG.debug("Card not luhn checked as luhn check is disabled");
            }
        }
        else
        {
            validationResult.addValidationError(CardValidationError.INVALID_NUMBER);
        }
    }


    protected boolean isLuhnCheckCompliant(CardInfo cardInfo)
    {
        boolean isLunhCheckCompliant = true;
        if(isInternationalMaestro(cardInfo.getCardNumber()))
        {
            LOG.info("Skipping Luhn check as card is an international maestro card");
        }
        else if(!luhnCheck(cardInfo.getCardNumber()))
        {
            isLunhCheckCompliant = false;
        }
        return isLunhCheckCompliant;
    }


    protected void validateDates(CardValidationResult validationResult, CardInfo cardInfo)
    {
        if(!isValidDate(cardInfo.getExpirationMonth(), cardInfo.getExpirationYear(), true))
        {
            validationResult.addValidationError(CardValidationError.INVALID_EXPIRY_DATE);
        }
        if(!isValidDate(cardInfo.getIssueMonth(), cardInfo.getIssueYear(), false))
        {
            validationResult.addValidationError(CardValidationError.INVALID_ISSUE_DATE);
        }
        if(hasExpirationDate(cardInfo) && isExpired(cardInfo))
        {
            validationResult.addValidationError(CardValidationError.INVALID_EXPIRY_DATE);
        }
    }


    protected boolean isExpired(CardInfo cardInfo)
    {
        return (cardInfo.getExpirationYear().intValue() < cardInfo.getIssueYear().intValue() || (cardInfo
                        .getExpirationYear().intValue() == cardInfo.getIssueYear().intValue() && cardInfo
                        .getExpirationMonth().intValue() <= cardInfo.getIssueMonth().intValue()));
    }


    protected boolean hasExpirationDate(CardInfo cardInfo)
    {
        return (cardInfo.getExpirationYear() != null && cardInfo.getIssueYear() != null);
    }


    protected boolean isValidDate(Integer month, Integer year, boolean expiryDate)
    {
        if(month == null || year == null)
        {
            return !expiryDate;
        }
        int currentYear = Calendar.getInstance().get(1);
        int currentMonth = Calendar.getInstance().get(2) + 1;
        if(month.intValue() < 1 || month.intValue() > 12)
        {
            return false;
        }
        if(expiryDate)
        {
            return isBeforeCurrentYear(year, month, currentYear, currentMonth);
        }
        return isAfterCurrentYear(year, month, currentYear, currentMonth);
    }


    protected boolean isBeforeCurrentYear(Integer year, Integer month, int currentYear, int currentMonth)
    {
        return (year.intValue() >= currentYear && (year.intValue() != currentYear || month.intValue() >= currentMonth));
    }


    protected boolean isAfterCurrentYear(Integer year, Integer month, int currentYear, int currentMonth)
    {
        return (year.intValue() <= currentYear && (year.intValue() != currentYear || month.intValue() <= currentMonth));
    }


    protected void validateCv2(CardValidationResult validationResult, CardInfo cardInfo, boolean isCv2AvsPolicyDisabled)
    {
        if(isCv2AvsPolicyDisabled)
        {
            return;
        }
        if(cardInfo.getCardType() != null && this.exemptFromCv2.contains(cardInfo.getCardType()))
        {
            return;
        }
        if(cardInfo.getCv2Number() == null)
        {
            validationResult.addValidationError(CardValidationError.MISSING_CV2);
        }
        else
        {
            validateCv2Format(validationResult, cardInfo);
        }
    }


    protected void validateCv2Format(CardValidationResult validationResult, CardInfo cardInfo)
    {
        if(!StringUtils.isNumeric(cardInfo.getCv2Number()))
        {
            validationResult.addValidationError(CardValidationError.INVALID_CV2_FORMAT);
        }
        else if(cardInfo.getCardType() != null)
        {
            if(cardInfo.getCardType().equals(CreditCardType.AMEX))
            {
                if(cardInfo.getCv2Number().length() != 4)
                {
                    validationResult.addValidationError(CardValidationError.INVALID_CV2_LENGTH);
                }
            }
            else if(cardInfo.getCv2Number().length() != 3)
            {
                validationResult.addValidationError(CardValidationError.INVALID_CV2_LENGTH);
            }
        }
        else if(cardInfo.getCv2Number().length() != 3 && cardInfo.getCv2Number().length() != 4)
        {
            validationResult.addValidationError(CardValidationError.INVALID_CV2_LENGTH);
        }
    }


    public CardValidationResult checkCard(CardInfo cardInfo)
    {
        CardValidationResult cardValidationResult = new CardValidationResult();
        validateCardNumber(cardValidationResult, cardInfo);
        validateName(cardValidationResult, cardInfo);
        if(!cardValidationResult.isSuccess())
        {
            LOG.info("basic card validation failed" + cardValidationResult);
            return cardValidationResult;
        }
        validateDates(cardValidationResult, cardInfo);
        validateCardScheme(cardValidationResult, cardInfo);
        validateCv2(cardValidationResult, cardInfo, false);
        if(LOG.isInfoEnabled())
        {
            LOG.info("result of card validation:" + cardValidationResult);
        }
        return cardValidationResult;
    }
}
