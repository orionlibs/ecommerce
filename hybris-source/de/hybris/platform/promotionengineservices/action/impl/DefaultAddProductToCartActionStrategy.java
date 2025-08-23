package de.hybris.platform.promotionengineservices.action.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.OrderService;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.promotionengineservices.model.AbstractRuleBasedPromotionActionModel;
import de.hybris.platform.promotionengineservices.model.RuleBasedOrderAddProductActionModel;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengineservices.calculation.RuleEngineCalculationService;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.FreeProductRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import de.hybris.platform.ruleengineservices.util.OrderUtils;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.apache.commons.collections.map.SingletonMap;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAddProductToCartActionStrategy extends AbstractRuleActionStrategy<RuleBasedOrderAddProductActionModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultAddProductToCartActionStrategy.class);
    private CartService cartService;
    private OrderService orderService;
    private ProductService productService;
    private RuleEngineCalculationService ruleEngineCalculationService;
    private OrderUtils orderUtils;


    public List<PromotionResultModel> apply(AbstractRuleActionRAO action)
    {
        AbstractOrderEntryModel entry;
        if(!(action instanceof FreeProductRAO))
        {
            LOG.error("cannot apply {}, action is not of type FreeProductRAO, but {}", getClass().getSimpleName(), action);
            return Collections.emptyList();
        }
        FreeProductRAO freeAction = (FreeProductRAO)action;
        if(!(freeAction.getAppliedToObject() instanceof de.hybris.platform.ruleengineservices.rao.CartRAO))
        {
            LOG.error("cannot apply {}, appliedToObject is not of type CartRAO, but {}", getClass().getSimpleName(), action
                            .getAppliedToObject());
            return Collections.emptyList();
        }
        if(freeAction.getAddedOrderEntry() == null || freeAction.getAddedOrderEntry().getProductCode() == null)
        {
            LOG.error("cannot apply {}, addedOrderEntry.product.code is not set.", getClass().getSimpleName());
            return Collections.emptyList();
        }
        OrderEntryRAO addedOrderEntryRao = freeAction.getAddedOrderEntry();
        int quantityToAdd = freeAction.getQuantityAdded();
        ProductModel product = null;
        try
        {
            product = getProductService().getProductForCode(addedOrderEntryRao.getProductCode());
        }
        catch(UnknownIdentifierException | de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException e)
        {
            LOG.error("cannot apply {}, product for code: {} cannot be retrieved due to exception {}.", new Object[] {getClass().getSimpleName(), addedOrderEntryRao
                            .getProductCode(), e.getClass().getSimpleName(), e});
            return Collections.emptyList();
        }
        PromotionResultModel promoResult = getPromotionActionService().createPromotionResult(action);
        if(promoResult == null)
        {
            LOG.error("cannot apply {}, promotionResult could not be created.", getClass().getSimpleName());
            return Collections.emptyList();
        }
        AbstractOrderModel order = getPromotionResultUtils().getOrder(promoResult);
        if(Objects.isNull(order))
        {
            LOG.error("cannot apply {}, order or cart not found: {}", getClass().getSimpleName(), order);
            if(getModelService().isNew(promoResult))
            {
                getModelService().detach(promoResult);
            }
            return Collections.emptyList();
        }
        Optional<AbstractOrderEntryModel> existingEntry = getExistingGiveAwayEntry(order, product);
        if(existingEntry.isPresent())
        {
            entry = existingEntry.get();
            entry.setQuantity(Long.valueOf(entry.getQuantity().longValue() + quantityToAdd));
        }
        else
        {
            entry = addNewEntry(order, product, quantityToAdd);
            addedOrderEntryRao.setEntryNumber(entry.getEntryNumber());
        }
        RuleBasedOrderAddProductActionModel actionModel = createOrderAddProductAction(action, quantityToAdd, product, promoResult);
        handleActionMetadata(action, (AbstractRuleBasedPromotionActionModel)actionModel);
        getModelService().saveAll(new Object[] {promoResult, actionModel, order, entry});
        return Collections.singletonList(promoResult);
    }


    protected AbstractOrderEntryModel addNewEntry(AbstractOrderModel order, ProductModel product, int quantityToAdd)
    {
        OrderEntryModel orderEntryModel;
        if(order instanceof CartModel)
        {
            AbstractOrderEntryModel entry = getCartService().addNewEntry(order, product, quantityToAdd, getUnit(product), -1, false);
        }
        else
        {
            orderEntryModel = getOrderService().addNewEntry((OrderModel)order, product, quantityToAdd, getUnit(product), -1, false);
        }
        orderEntryModel.setGiveAway(Boolean.TRUE);
        return (AbstractOrderEntryModel)orderEntryModel;
    }


    protected Optional<AbstractOrderEntryModel> getExistingGiveAwayEntry(AbstractOrderModel order, ProductModel product)
    {
        return order.getEntries().stream().filter(oe -> oe.getProduct().equals(product))
                        .filter(oe -> BooleanUtils.toBoolean(oe.getGiveAway())).findFirst();
    }


    protected RuleBasedOrderAddProductActionModel createOrderAddProductAction(AbstractRuleActionRAO action, int quantity, ProductModel product, PromotionResultModel promoResult)
    {
        RuleBasedOrderAddProductActionModel actionModel = (RuleBasedOrderAddProductActionModel)createPromotionAction(promoResult, action);
        actionModel.setProduct(product);
        actionModel.setQuantity(Long.valueOf(quantity));
        return actionModel;
    }


    public void undo(ItemModel item)
    {
        if(item instanceof RuleBasedOrderAddProductActionModel)
        {
            RuleBasedOrderAddProductActionModel action = (RuleBasedOrderAddProductActionModel)item;
            handleUndoActionMetadata((AbstractRuleBasedPromotionActionModel)action);
            ProductModel product = action.getProduct();
            Long quantity = action.getQuantity();
            AbstractOrderModel order = getPromotionResultUtils().getOrder(action.getPromotionResult());
            AbstractOrderEntryModel undoEntry = findOrderEntryForUndo(order, action);
            if(undoEntry == null)
            {
                LOG.error("cannot undo {}, cannot find order entry for undo(). Looking for product {} with quantity {}", new Object[] {getClass()
                                .getSimpleName(), product.getCode(), quantity});
                return;
            }
            Long newQuantity = (undoEntry.getQuantity() != null) ? Long.valueOf(undoEntry.getQuantity().longValue() - quantity.longValue()) : Long.valueOf(0L - quantity.longValue());
            if(order instanceof CartModel)
            {
                getCartService().updateQuantities((CartModel)order, (Map)new SingletonMap(undoEntry.getEntryNumber(), newQuantity));
            }
            else if(order instanceof OrderModel)
            {
                getOrderUtils().updateOrderQuantities((OrderModel)order, (Map)new SingletonMap(undoEntry.getEntryNumber(), newQuantity));
            }
            if(!getModelService().isRemoved(undoEntry))
            {
                undoInternal((AbstractRuleBasedPromotionActionModel)action);
                getModelService().save(undoEntry);
            }
            else
            {
                normalizeEntryNumbers(order);
            }
            recalculateIfNeeded(order);
        }
    }


    protected void normalizeEntryNumbers(AbstractOrderModel order)
    {
        List<AbstractOrderEntryModel> entries = new ArrayList<>(order.getEntries());
        Collections.sort(entries, (Comparator<? super AbstractOrderEntryModel>)new BeanComparator("entryNumber", (Comparator)new ComparableComparator()));
        for(int i = 0; i < entries.size(); i++)
        {
            ((AbstractOrderEntryModel)entries.get(i)).setEntryNumber(Integer.valueOf(i));
            getModelService().save(entries.get(i));
        }
    }


    protected AbstractOrderEntryModel findOrderEntryForUndo(AbstractOrderModel order, RuleBasedOrderAddProductActionModel action)
    {
        AbstractOrderEntryModel giveAwayEntry = findMatchingGiveAwayEntry(order, action, (a, e) -> a.getQuantity().equals(e.getQuantity()));
        if(giveAwayEntry != null)
        {
            return giveAwayEntry;
        }
        giveAwayEntry = findMatchingGiveAwayEntry(order, action, (a, e) -> (a.getQuantity().compareTo(e.getQuantity()) < 0));
        if(giveAwayEntry != null)
        {
            return giveAwayEntry;
        }
        return null;
    }


    protected AbstractOrderEntryModel findMatchingGiveAwayEntry(AbstractOrderModel order, RuleBasedOrderAddProductActionModel action, BiPredicate<RuleBasedOrderAddProductActionModel, AbstractOrderEntryModel> matchingCondition)
    {
        for(AbstractOrderEntryModel entry : order.getEntries())
        {
            if(BooleanUtils.isTrue(entry.getGiveAway()) && action.getProduct().equals(entry.getProduct()) && matchingCondition
                            .test(action, entry))
            {
                return entry;
            }
        }
        return null;
    }


    protected UnitModel getUnit(ProductModel productModel)
    {
        try
        {
            return getProductService().getOrderableUnit(productModel);
        }
        catch(ModelNotFoundException e)
        {
            LOG.error("Cannot find sales unit for product: {}", productModel.getCode());
            return null;
        }
    }


    protected CartService getCartService()
    {
        return this.cartService;
    }


    @Required
    public void setCartService(CartService cartService)
    {
        this.cartService = cartService;
    }


    protected ProductService getProductService()
    {
        return this.productService;
    }


    @Required
    public void setProductService(ProductService productService)
    {
        this.productService = productService;
    }


    protected RuleEngineCalculationService getRuleEngineCalculationService()
    {
        return this.ruleEngineCalculationService;
    }


    @Required
    public void setRuleEngineCalculationService(RuleEngineCalculationService ruleEngineCalculationService)
    {
        this.ruleEngineCalculationService = ruleEngineCalculationService;
    }


    protected OrderService getOrderService()
    {
        return this.orderService;
    }


    @Required
    public void setOrderService(OrderService orderService)
    {
        this.orderService = orderService;
    }


    protected OrderUtils getOrderUtils()
    {
        return this.orderUtils;
    }


    @Required
    public void setOrderUtils(OrderUtils orderUtils)
    {
        this.orderUtils = orderUtils;
    }
}
