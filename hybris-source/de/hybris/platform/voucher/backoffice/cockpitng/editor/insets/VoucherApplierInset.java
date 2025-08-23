package de.hybris.platform.voucher.backoffice.cockpitng.editor.insets;

import com.hybris.cockpitng.dataaccess.facades.object.ObjectFacade;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.model.VoucherModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Textbox;

public class VoucherApplierInset implements CockpitEditorRenderer<Object>
{
    protected static final String VOUCHER_CODE_PLACEHOLDER_LABEL = "voucherApplierInset.voucherCode.placeholder";
    protected static final String REDEEM_BUTTON_MSG = "hmc.btn.redeem.voucher";
    protected static final String RELEASE_BUTTON_MSG = "hmc.btn.release.voucher";
    protected static final String ERROR_WRONG_PARENT_TYPE_MSG = "voucherApplierInset.wrongParentType";
    protected static final String INVALID_VOUCHER_CODE_MSG = "hmc.error.voucher.invalid.vouchercode";
    protected static final String EMPTY_VOUCHER_CODE_MSG = "hmc.error.voucher.vouchercode.empty";
    protected static final String VOUCHER_VIOLATION_HEADER_MSG = "hmc.voucher.violation.header";
    protected static final String ERROR_INVALID_VOUCHER_CODE_MSG = "hmc.error.voucher.invalid.vouchercode";
    protected static final String ERROR_VOUCHER_ALREADY_APPLIED_MSG = "hmc.error.voucher.already.applied";
    protected static final String ERROR_VOUCHER_ALREADY_USED_MSG = "hmc.error.voucher.vouchercode.already.used";
    protected static final String ERROR_VOUCHER_TOTALPRICE_EXCEEDED_MSG = "hmc.error.voucher.totalprice.exceeded";
    protected static final String ERROR_UNKNOWN_MSG = "hmc.error.voucher.unknown";
    public static final String INSET_SCLASS = "voucher-applier";
    public static final String TEXTBOX_SCLASS = "inset-textbox";
    public static final String TEXTBOX_WRAPPER_SCLASS = "z-textbox-wrapper";
    public static final String BUTTON_SCLASS = "inset-button";
    public static final String REDEEM_BUTTON_SCLASS = "inset-button-redeem";
    public static final String RELEASE_BUTTON_SCLASS = "inset-button-release";
    public static final String BUTTON_WRAPPER_SCLASS = "z-button-wrapper";
    protected static final String PARENT_OBJECT_PARAM = "parentObject";
    protected static final String CURRENT_OBJECT_PARAM = "currentObject";
    private VoucherService voucherService;
    private VoucherModelService voucherModelService;
    private ObjectFacade objectFacade;
    private static final String ERROR_WHILE_APPLYING_VOUCHER = "Error while applying voucher: ";
    private static final String ERROR_VOUCHER_CANNOT_BE_REDEEMED = "Voucher cannot be redeemed: ";


    public void render(Component parent, EditorContext<Object> context, EditorListener<Object> listener)
    {
        if(parent == null || context == null || listener == null)
        {
            return;
        }
        Div insetContainer = new Div();
        insetContainer.setSclass("inset voucher-applier");
        insetContainer.setParent(parent);
        WidgetInstanceManager wim = (WidgetInstanceManager)context.getParameter("wim");
        Object parentObject = context.getParameter("parentObject");
        if(!(parentObject instanceof AbstractOrderModel))
        {
            throw new IllegalStateException(Labels.getLabel("voucherApplierInset.wrongParentType"));
        }
        AbstractOrderModel abstractOrder = (AbstractOrderModel)parentObject;
        Div textboxWrapper = new Div();
        textboxWrapper.setSclass("z-textbox-wrapper");
        textboxWrapper.setParent((Component)insetContainer);
        Textbox editorView = new Textbox();
        editorView.setSclass("inset-textbox");
        editorView.setPlaceholder(Labels.getLabel("voucherApplierInset.voucherCode.placeholder"));
        editorView.setParent((Component)textboxWrapper);
        Div redeemButtonWrapper = new Div();
        redeemButtonWrapper.setSclass("z-button-wrapper");
        redeemButtonWrapper.setParent((Component)insetContainer);
        Button redeemButton = new Button(Labels.getLabel("hmc.btn.redeem.voucher"));
        redeemButton.setSclass("inset-button inset-button-redeem");
        redeemButton.setParent((Component)redeemButtonWrapper);
        redeemButton.setDisabled(true);
        redeemButton.addEventListener("onClick", (EventListener)new RedeemVoucherEventListener(this, editorView, abstractOrder, wim));
        Div releaseButtonWrapper = new Div();
        releaseButtonWrapper.setSclass("z-button-wrapper");
        releaseButtonWrapper.setParent((Component)insetContainer);
        Button releaseButton = new Button(Labels.getLabel("hmc.btn.release.voucher"));
        releaseButton.setSclass("inset-button inset-button-release");
        releaseButton.setParent((Component)releaseButtonWrapper);
        releaseButton.setDisabled(true);
        releaseButton.addEventListener("onClick", (EventListener)new Object(this, editorView, abstractOrder, wim));
        editorView.addEventListener("onChanging", (EventListener)new Object(this, redeemButton, releaseButton));
    }


    protected void applyVoucher(String voucherCode, AbstractOrderModel abstractOrder) throws VoucherException, VoucherViolationException, SimpleVoucherException
    {
        validateVoucherCode(voucherCode);
        synchronized(abstractOrder)
        {
            VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
            if(!getVoucherModelService().isApplicable(voucher, abstractOrder))
            {
                throw new VoucherViolationException("Voucher cannot be redeemed: " + voucherCode);
            }
            if(!getVoucherModelService().isReservable(voucher, voucherCode, abstractOrder))
            {
                throw new VoucherException("Voucher cannot be redeemed: " + voucherCode);
            }
            if(abstractOrder instanceof OrderModel)
            {
                if(getVoucherModelService().redeem(voucher, voucherCode, (OrderModel)abstractOrder) == null)
                {
                    throw new VoucherException("Error while applying voucher: " + voucherCode);
                }
            }
            else if(abstractOrder instanceof CartModel)
            {
                try
                {
                    if(!getVoucherModelService().redeem(voucher, voucherCode, (CartModel)abstractOrder))
                    {
                        throw new VoucherException("Error while applying voucher: " + voucherCode);
                    }
                }
                catch(JaloPriceFactoryException ex)
                {
                    throw new VoucherException("Error while applying voucher: " + voucherCode, ex);
                }
            }
            else
            {
                throw new UnsupportedOperationException("Unable to release voucher from " + abstractOrder.getItemtype() + "!");
            }
            checkOrderAfterRedeem(voucher, voucherCode, abstractOrder);
            return;
        }
    }


    protected void releaseVoucher(String voucherCode, AbstractOrderModel abstractOrder) throws VoucherException, SimpleVoucherException
    {
        validateVoucherCode(voucherCode);
        VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
        try
        {
            if(abstractOrder instanceof OrderModel)
            {
                getVoucherModelService().release(voucher, voucherCode, (OrderModel)abstractOrder);
            }
            else if(abstractOrder instanceof CartModel)
            {
                getVoucherModelService().release(voucher, voucherCode, (CartModel)abstractOrder);
            }
            else
            {
                throw new UnsupportedOperationException("Unable to release voucher from " + abstractOrder.getItemtype() + "!");
            }
        }
        catch(JaloPriceFactoryException | de.hybris.platform.jalo.ConsistencyCheckException ex)
        {
            throw new VoucherException("Couldn't release voucher: " + voucherCode, ex);
        }
    }


    protected void validateVoucherCode(String voucherCode) throws SimpleVoucherException
    {
        if(StringUtils.isBlank(voucherCode))
        {
            throw new SimpleVoucherException(Labels.getLabel("hmc.error.voucher.vouchercode.empty"));
        }
        VoucherModel voucher = getVoucherService().getVoucher(voucherCode);
        if(voucher == null)
        {
            throw new SimpleVoucherException(Labels.getLabel("hmc.error.voucher.invalid.vouchercode"));
        }
    }


    protected void checkOrderAfterRedeem(VoucherModel voucher, String voucherCode, AbstractOrderModel abstractOrder) throws VoucherException, SimpleVoucherException
    {
        double cartTotal = abstractOrder.getTotalPrice().doubleValue();
        double voucherValue = voucher.getValue().doubleValue();
        double voucherCalcValue = voucher.getAbsolute().equals(Boolean.TRUE) ? voucherValue : (cartTotal * voucherValue / 100.0D);
        if(abstractOrder.getTotalPrice().doubleValue() - voucherCalcValue < 0.0D)
        {
            releaseVoucher(voucherCode, abstractOrder);
            throw new SimpleVoucherException(Labels.getLabel("hmc.error.voucher.totalprice.exceeded"));
        }
    }


    @Required
    public void setVoucherService(VoucherService voucherService)
    {
        this.voucherService = voucherService;
    }


    protected VoucherService getVoucherService()
    {
        return this.voucherService;
    }


    @Required
    public void setVoucherModelService(VoucherModelService voucherModelService)
    {
        this.voucherModelService = voucherModelService;
    }


    protected VoucherModelService getVoucherModelService()
    {
        return this.voucherModelService;
    }


    @Required
    public void setObjectFacade(ObjectFacade objectFacade)
    {
        this.objectFacade = objectFacade;
    }


    protected ObjectFacade getObjectFacade()
    {
        return this.objectFacade;
    }
}
