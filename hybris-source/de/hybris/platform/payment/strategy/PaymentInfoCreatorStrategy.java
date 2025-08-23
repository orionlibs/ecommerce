package de.hybris.platform.payment.strategy;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import java.math.BigDecimal;

public interface PaymentInfoCreatorStrategy
{
    void attachPaymentInfo(PaymentTransactionModel paramPaymentTransactionModel, UserModel paramUserModel, CardInfo paramCardInfo, BigDecimal paramBigDecimal);
}
