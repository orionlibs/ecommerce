package de.hybris.platform.payment.methods;

import de.hybris.platform.core.enums.CreditCardType;
import de.hybris.platform.payment.commands.result.CardValidationResult;
import de.hybris.platform.payment.dto.CardInfo;
import java.util.List;

public interface CardValidator
{
    boolean luhnCheck(String paramString);


    CardValidationResult checkCard(CardInfo paramCardInfo);


    List<CreditCardType> getSupportedCardSchemes();


    boolean isCardSchemeSupported(CreditCardType paramCreditCardType);
}
