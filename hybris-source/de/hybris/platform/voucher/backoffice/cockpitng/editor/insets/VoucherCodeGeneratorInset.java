package de.hybris.platform.voucher.backoffice.cockpitng.editor.insets;

import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.model.VoucherModel;
import java.security.NoSuchAlgorithmException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;

public class VoucherCodeGeneratorInset extends AbstractSingleButtonInset<String>
{
    private static final Logger LOG = LoggerFactory.getLogger(VoucherCodeGeneratorInset.class);
    public static final String VOUCHER_CODE_GENERATOR_INSET = "voucher-code-generator";
    protected static final String PARENT_OBJECT_PARAM = "parentObject";
    private VoucherModelService voucherModelService;
    private ModelService modelService;


    protected EventListener<Event> getInsetListener(Component parent, EditorContext<String> context, EditorListener<String> listener)
    {
        return event -> {
            Object parentObject = context.getParameter("parentObject");
            if(parentObject instanceof VoucherModel)
            {
                try
                {
                    String newVoucherCode = getVoucherModelService().generateVoucherCode((VoucherModel)parentObject);
                    if(newVoucherCode != null && !newVoucherCode.equals(findAncestorEditor(parent).getValue()))
                    {
                        listener.onValueChanged(newVoucherCode);
                    }
                }
                catch(NoSuchAlgorithmException e)
                {
                    LOG.error(e.getMessage(), e);
                }
            }
        };
    }


    protected boolean isEnabled(EditorContext<String> context)
    {
        Object parentObject = context.getParameter("parentObject");
        return (parentObject instanceof de.hybris.platform.core.model.ItemModel && !getModelService().isNew(parentObject));
    }


    protected String getSclass()
    {
        return "voucher-code-generator";
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
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }
}
