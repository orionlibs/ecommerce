package de.hybris.platform.couponservices.order.hooks;

import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook;
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter;
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.promotionengineservices.util.ActionUtils;
import de.hybris.platform.promotions.model.AbstractPromotionActionModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.DiscountValue;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiscountValueCleanupMethodHook implements CommercePlaceOrderMethodHook
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DiscountValueCleanupMethodHook.class);
    private final ModelService modelService;
    private final ActionUtils actionUtils;
    private final CalculationService calculationService;


    public DiscountValueCleanupMethodHook(ModelService modelService, ActionUtils actionUtils, CalculationService calculationService)
    {
        this.modelService = modelService;
        this.actionUtils = actionUtils;
        this.calculationService = calculationService;
    }


    public void afterPlaceOrder(CommerceCheckoutParameter parameter, CommerceOrderResult orderModel) throws InvalidCartException
    {
    }


    public void beforePlaceOrder(CommerceCheckoutParameter parameter) throws InvalidCartException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("cart", parameter.getCart());
        boolean hasInvalidDiscount = false;
        CartModel cart = parameter.getCart();
        List<String> allPromotionActions = (List<String>)cart.getAllPromotionResults().stream().flatMap(promotionResult -> promotionResult.getAllPromotionActions().stream()).map(AbstractPromotionActionModel::getGuid).collect(Collectors.toList());
        List<DiscountValue> validCartDVs = (List<DiscountValue>)cart.getGlobalDiscountValues().stream().filter(dv -> isValidDiscountValue(dv.getCode(), allPromotionActions)).collect(Collectors.toList());
        if(validCartDVs.size() < cart.getGlobalDiscountValues().size())
        {
            cart.setGlobalDiscountValues(validCartDVs);
            hasInvalidDiscount = true;
        }
        for(AbstractOrderEntryModel entry : cart.getEntries())
        {
            List<DiscountValue> validEntryDVs = (List<DiscountValue>)entry.getDiscountValues().stream().filter(dv -> isValidDiscountValue(dv.getCode(), allPromotionActions)).collect(Collectors.toList());
            if(validEntryDVs.size() < entry.getDiscountValues().size())
            {
                entry.setDiscountValues(validEntryDVs);
                hasInvalidDiscount = true;
            }
        }
        if(hasInvalidDiscount)
        {
            recalculateCartWithoutInvalidDiscountValues(cart);
        }
    }


    public void beforeSubmitOrder(CommerceCheckoutParameter parameter, CommerceOrderResult result) throws InvalidCartException
    {
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public ActionUtils getActionUtils()
    {
        return this.actionUtils;
    }


    public CalculationService getCalculationService()
    {
        return this.calculationService;
    }


    protected void recalculateCartWithoutInvalidDiscountValues(CartModel cart) throws InvalidCartException
    {
        String cartCode = cart.getCode();
        try
        {
            getCalculationService().calculateTotals((AbstractOrderModel)cart, true);
        }
        catch(CalculationException e)
        {
            LOGGER.error(String.format("Recalculation of cart with code '%s' failed.", new Object[] {cartCode}), (Throwable)e);
            cart.setCalculated(Boolean.FALSE);
        }
        getModelService().save(cart);
        throw new InvalidCartException(
                        String.format("Invalid discount found on cart with code '%s', please try to place-order again", new Object[] {cartCode}));
    }


    private boolean isValidDiscountValue(String discountValueCode, List<String> allPromotionActions)
    {
        if(getActionUtils().isActionUUID(discountValueCode))
        {
            return allPromotionActions.contains(discountValueCode);
        }
        return true;
    }
}
