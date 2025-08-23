package de.hybris.platform.payment.strategy.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.payment.CreditCardPaymentInfoModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.payment.dto.BillingInfo;
import de.hybris.platform.payment.dto.CardInfo;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.payment.strategy.PaymentInfoCreatorStrategy;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;
import de.hybris.platform.servicelayer.model.ModelService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCreditCardPaymentInfoStrategy implements PaymentInfoCreatorStrategy
{
    private KeyGenerator keyGenerator;
    private ModelService modelService;
    private CommonI18NService commonI18NService;


    public void attachPaymentInfo(PaymentTransactionModel paymentTransactionModel, UserModel userModel, CardInfo cardInfo, BigDecimal amount)
    {
        CreditCardPaymentInfoModel creditCardModel = (CreditCardPaymentInfoModel)getModelService().create(CreditCardPaymentInfoModel.class);
        creditCardModel.setUser(userModel);
        creditCardModel.setCode(getKeyGenerator().generate().toString());
        creditCardModel.setType(cardInfo.getCardType());
        creditCardModel.setNumber(cardInfo.getCardNumber());
        creditCardModel.setValidFromMonth((cardInfo.getIssueMonth() != null) ? String.valueOf(cardInfo.getIssueMonth()) : "");
        creditCardModel.setValidFromYear((cardInfo.getIssueYear() != null) ? String.valueOf(cardInfo.getIssueYear()) : "");
        creditCardModel.setValidToMonth(String.valueOf(cardInfo.getExpirationMonth()));
        creditCardModel.setValidToYear(String.valueOf(cardInfo.getExpirationYear()));
        creditCardModel.setCcOwner(cardInfo.getCardHolderFullName());
        if(cardInfo.getBillingInfo() != null)
        {
            BillingInfo billingInfo = cardInfo.getBillingInfo();
            AddressModel addressModel = (AddressModel)getModelService().create(AddressModel.class);
            addressModel.setOwner((ItemModel)userModel);
            addressModel.setFirstname(billingInfo.getFirstName());
            addressModel.setLastname(billingInfo.getLastName());
            addressModel.setStreetnumber(billingInfo.getStreet1());
            addressModel.setStreetname(billingInfo.getStreet2());
            addressModel.setTown(billingInfo.getCity());
            addressModel.setDistrict(billingInfo.getState());
            addressModel.setPostalcode(billingInfo.getPostalCode());
            addressModel.setCountry(getCommonI18NService().getCountry(billingInfo.getCountry()));
            addressModel.setEmail(billingInfo.getEmail());
            addressModel.setPhone1(billingInfo.getPhoneNumber());
            creditCardModel.setBillingAddress(addressModel);
            getModelService().save(addressModel);
        }
        getModelService().save(creditCardModel);
        getModelService().refresh(userModel);
        paymentTransactionModel.setInfo((PaymentInfoModel)creditCardModel);
        getModelService().save(paymentTransactionModel);
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected KeyGenerator getKeyGenerator()
    {
        return this.keyGenerator;
    }


    @Required
    public void setKeyGenerator(KeyGenerator keyGenerator)
    {
        this.keyGenerator = keyGenerator;
    }


    protected CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }
}
