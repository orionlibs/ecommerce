package de.hybris.platform.voucher.backoffice.cockpitng.editor.insets;

import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.catalog.model.CatalogUnawareMediaModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.model.SerialVoucherModel;
import de.hybris.platform.voucher.model.VoucherModel;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Intbox;

public class SerialVoucherCodesGeneratorInset implements CockpitEditorRenderer<Object>
{
    protected static final String ERROR_WRONG_PARENT_TYPE_MSG = "serialVoucherCodesGenerator.wrongParentType";
    protected static final String EDITOR_PLACEHOLDER = "hmc.text.serialvoucher.setquantity.label";
    protected static final String GENERATE_BUTTON_LABEL = "hmc.btn.generate.voucher.codes";
    protected static final String UNEXPECTED_ERROR_MSG = "serialVoucherCodesGenerator.unexpectedError";
    public static final String INSET_SCLASS = "serial-voucher-codes-generator";
    public static final String BUTTON_SCLASS = "inset-button";
    public static final String TEXTBOX_SCLASS = "inset-textbox";
    protected static final String PARENT_OBJECT_PARAM = "parentObject";
    protected static final String CURRENT_OBJECT_PARAM = "currentObject";
    private ModelService modelService;
    private VoucherModelService voucherModelService;
    private MediaService mediaService;


    public void render(Component parent, EditorContext<Object> context, EditorListener<Object> listener)
    {
        if(parent == null || context == null || listener == null)
        {
            return;
        }
        Div insetContainer = new Div();
        insetContainer.setSclass("inset serial-voucher-codes-generator");
        insetContainer.setParent(parent);
        WidgetInstanceManager wim = (WidgetInstanceManager)context.getParameter("wim");
        Object parentObject = context.getParameter("parentObject");
        if(!(parentObject instanceof SerialVoucherModel))
        {
            throw new IllegalStateException(Labels.getLabel("serialVoucherCodesGenerator.wrongParentType"));
        }
        SerialVoucherModel serialVoucher = (SerialVoucherModel)parentObject;
        Intbox editorView = new Intbox();
        editorView.setPlaceholder(Labels.getLabel("hmc.text.serialvoucher.setquantity.label"));
        editorView.setSclass("inset-textbox");
        editorView.setParent((Component)insetContainer);
        editorView.setConstraint("no negative, no zero");
        editorView.setDisabled(isDisabled(serialVoucher));
        Button button = new Button(Labels.getLabel("hmc.btn.generate.voucher.codes"));
        button.setSclass("inset-button");
        button.setParent((Component)insetContainer);
        button.setDisabled(true);
        button.addEventListener("onClick", (EventListener)new Object(this, editorView, serialVoucher, wim));
        editorView.addEventListener("onChanging", (EventListener)new Object(this, button));
    }


    protected void generateVoucherCodes(SerialVoucherModel serialVoucher, Integer quantity) throws IOException, JaloBusinessException
    {
        List<String> generatedCodes = new ArrayList<>();
        for(int i = 0; i < quantity.intValue(); i++)
        {
            try
            {
                generatedCodes.add(getVoucherModelService().generateVoucherCode((VoucherModel)serialVoucher));
            }
            catch(NoSuchAlgorithmException ex)
            {
                throw new IllegalStateException("serialVoucherCodesGenerator.unexpectedError", ex);
            }
        }
        MediaModel voucherCodesMedia = createMedia(generatedCodes, serialVoucher.getCode(), quantity.intValue());
        Collection<MediaModel> voucherCodes = (serialVoucher.getCodes() != null) ? new ArrayList(serialVoucher.getCodes()) : new ArrayList();
        voucherCodes.add(voucherCodesMedia);
        serialVoucher.setCodes(voucherCodes);
    }


    protected MediaModel createMedia(List<String> codes, String actionCode, int quantity)
    {
        String mediaCode = "" + quantity + " " + quantity + " vouchercode" + actionCode + " (" + ((quantity == 1) ? "" : "s") + ")";
        CatalogUnawareMediaModel media = new CatalogUnawareMediaModel();
        media.setCode(mediaCode);
        getModelService().save(media);
        InputStream is = IOUtils.toInputStream(StringUtils.join(codes, "\n"), "UTF-8");
        getMediaService().setStreamForMedia((MediaModel)media, is, mediaCode + ".csv", "text/csv");
        return (MediaModel)media;
    }


    protected boolean isDisabled(SerialVoucherModel serialVoucher)
    {
        return getModelService().isNew(serialVoucher);
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected VoucherModelService getVoucherModelService()
    {
        return this.voucherModelService;
    }


    @Required
    public void setVoucherModelService(VoucherModelService voucherModelService)
    {
        this.voucherModelService = voucherModelService;
    }


    protected MediaService getMediaService()
    {
        return this.mediaService;
    }


    @Required
    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
