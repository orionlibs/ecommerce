package de.hybris.platform.platformbackoffice.widgets.impex;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.io.IOException;
import javax.annotation.Resource;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FileUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.UploadEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.Window;

public class ImpExFileUploadEditor extends AbstractCockpitEditorRenderer<ImpExFileUploadResult>
{
    private static final String UPLOAD_TRUE = "true,maxsize=-1";
    private static final int NUM_OF_PREVIEW_LINES = 50;
    private static final String TOOLBAR_CSS_CLASS = "yw-import-wizard-toolbar";
    private static final String SCLASS_UPLOAD_BUTTON = "y-btn-secondary";
    private static final String BACKING_OBJECT = "importForm";
    @Resource
    private ModelService modelService;
    @Resource
    private MediaService mediaService;
    private MediaExtractor mediaExtractor;
    private Button uploadButton;
    private Button createMediaButton;
    private Button previewButton;
    private Button resetButton;
    private Div uploadedFileDetails;
    private String bindProperty;
    private boolean preview = true;


    public void render(Component parent, EditorContext<ImpExFileUploadResult> context, EditorListener<ImpExFileUploadResult> listener)
    {
        Validate.notNull("All parameters are mandatory", new Object[] {parent, context, listener});
        initProperties(context);
        Div fileUploadView = new Div();
        fileUploadView.setParent(parent);
        HtmlBasedComponent vbox = createMainLayout();
        vbox.setParent((Component)fileUploadView);
        generateUploadToolbar(vbox, context);
        this.uploadedFileDetails = new Div();
        this.uploadedFileDetails.setParent((Component)vbox);
        showUploadedFileDetails((Component)this.uploadedFileDetails, context);
        this.uploadButton.addEventListener("onUpload", event -> handleUploadEvent(context, (UploadEvent)event));
        this.previewButton.addEventListener("onClick", event -> showPreviewWindow(parent, context));
        this.createMediaButton.addEventListener("onClick", event -> {
            ImpExMediaModel createdMedia = createMediaFromUploadedFile();
            changedSelectedImpExMedia(parent, context, createdMedia);
        });
        this.resetButton.addEventListener("onClick", event -> resetUploadedFile(context));
    }


    protected HtmlBasedComponent createMainLayout()
    {
        return (HtmlBasedComponent)new Vbox();
    }


    protected void initProperties(EditorContext<ImpExFileUploadResult> context)
    {
        this.bindProperty = (String)context.getParameter("bindProperty");
        this.preview = Boolean.valueOf((String)context.getParameter("preview")).booleanValue();
    }


    protected void generateUploadToolbar(HtmlBasedComponent vbox, EditorContext<ImpExFileUploadResult> context)
    {
        HtmlBasedComponent toolbar = createUploadToolBarLayout();
        toolbar.setParent((Component)vbox);
        toolbar.setSclass("yw-import-wizard-toolbar");
        this.uploadButton = new Button(getL10nDecorator(context, "actionUpload", "action.upload"));
        this.uploadButton.setUpload("true,maxsize=-1");
        this.uploadButton.setSclass("y-btn-secondary");
        toolbar.appendChild((Component)this.uploadButton);
        this.createMediaButton = new Button(getL10nDecorator(context, "actionCreateMedia", "action.createmedia"));
        toolbar.appendChild((Component)this.createMediaButton);
        this.previewButton = new Button(getL10nDecorator(context, "actionReset", "action.preview"));
        toolbar.appendChild((Component)this.previewButton);
        if(!this.preview)
        {
            this.previewButton.setVisible(false);
        }
        this.resetButton = new Button(getL10nDecorator(context, "actionReset", "action.reset"));
        toolbar.appendChild((Component)this.resetButton);
        buttonsCleanState();
    }


    protected HtmlBasedComponent createUploadToolBarLayout()
    {
        return (HtmlBasedComponent)new Hbox();
    }


    protected void showPreviewWindow(Component parent, EditorContext<ImpExFileUploadResult> context)
    {
        Window previewWindow = new Window();
        previewWindow.setId("ImpExPreview");
        previewWindow.setTitle(context.getLabel("impex.preview.title"));
        previewWindow.setWidth("50%");
        previewWindow.setHeight("50%");
        previewWindow.setParent(parent);
        previewWindow.setClosable(true);
        previewWindow.doHighlighted();
        Textbox textbox = new Textbox();
        textbox.setParent((Component)previewWindow);
        textbox.setWidth("100%");
        textbox.setHeight("100%");
        textbox.setMultiline(true);
        textbox.setReadonly(true);
        if(this.mediaExtractor != null)
        {
            textbox.setText(extractUploadedFilePreview());
        }
    }


    protected String extractUploadedFilePreview()
    {
        if(this.mediaExtractor == null)
        {
            return "";
        }
        int i = 0;
        String uploadedMediaStringData = getUploadedMediaStringData();
        StringBuilder builder = new StringBuilder();
        for(String line : uploadedMediaStringData.split("\n"))
        {
            builder.append(line).append("\n");
            if(i++ == 50)
            {
                break;
            }
        }
        if(i > 50)
        {
            builder.append("\n\n# Skipped the rest of the file");
        }
        return builder.toString();
    }


    private String getUploadedMediaStringData()
    {
        if(this.mediaExtractor == null)
        {
            return "";
        }
        return this.mediaExtractor.getDataAsString();
    }


    protected void handleUploadEvent(EditorContext<ImpExFileUploadResult> context, UploadEvent event)
    {
        this.mediaExtractor = new MediaExtractor(event.getMedia());
        String name = this.mediaExtractor.getName();
        String size = FileUtils.byteCountToDisplaySize(this.mediaExtractor.getDataSize());
        showUploadedFileDetails((Component)this.uploadedFileDetails, context, name, size);
        buttonsUploadedState();
    }


    protected void changedSelectedImpExMedia(Component parent, EditorContext<ImpExFileUploadResult> context, ImpExMediaModel newMedia)
    {
        showUploadedFileDetails((Component)this.uploadedFileDetails, context);
        buttonsCleanState();
        WidgetInstanceManager widgetInstanceManager = ((Editor)parent).getWidgetInstanceManager();
        ImpExImportForm form = (ImpExImportForm)widgetInstanceManager.getModel().getValue("importForm", ImpExImportForm.class);
        try
        {
            BeanUtils.setProperty(form, this.bindProperty, newMedia);
        }
        catch(IllegalAccessException | java.lang.reflect.InvocationTargetException e)
        {
            throw new IllegalStateException("Couldn't set " + this.bindProperty + " property on " + form.getClass().getName(), e);
        }
        widgetInstanceManager.getModel().changed();
    }


    protected ImpExMediaModel createMediaFromUploadedFile() throws IOException
    {
        ImpExMediaModel newMedia = (ImpExMediaModel)this.modelService.create(ImpExMediaModel.class);
        String targetName = this.mediaExtractor.getTargetName();
        newMedia.setCode(targetName);
        this.modelService.save(newMedia);
        this.mediaService.setStreamForMedia((MediaModel)newMedia, this.mediaExtractor.getDataAsStream(), this.mediaExtractor.getName(), this.mediaExtractor
                        .getContentType());
        this.modelService.save(newMedia);
        return newMedia;
    }


    protected void resetUploadedFile(EditorContext<ImpExFileUploadResult> context)
    {
        showUploadedFileDetails((Component)this.uploadedFileDetails, context);
        buttonsCleanState();
    }


    protected void buttonsCleanState()
    {
        this.uploadButton.setDisabled(false);
        this.createMediaButton.setDisabled(true);
        this.previewButton.setDisabled(true);
        this.resetButton.setDisabled(true);
    }


    protected void buttonsUploadedState()
    {
        this.uploadButton.setDisabled(true);
        this.createMediaButton.setDisabled(false);
        this.previewButton.setDisabled(false);
        this.resetButton.setDisabled(false);
    }


    protected void showUploadedFileDetails(Component parent, EditorContext<ImpExFileUploadResult> context)
    {
        showUploadedFileDetails(parent, context, "", "");
    }


    protected void showUploadedFileDetails(Component parent, EditorContext<ImpExFileUploadResult> context, String filename, String size)
    {
        Validate.notNull("All parameters are mandatory", new Object[] {parent, filename, size});
        parent.getChildren().clear();
        String fileDetails = "";
        if(!filename.trim().isEmpty())
        {
            fileDetails = context.getLabel("uploaded.media.size.label") + ": " + context.getLabel("uploaded.media.size.label");
            if(!size.trim().isEmpty())
            {
                fileDetails = fileDetails + " (" + fileDetails + ")";
            }
        }
        Label fileDetailsText = new Label(fileDetails);
        fileDetailsText.setParent(parent);
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    protected MediaService getMediaService()
    {
        return this.mediaService;
    }


    protected Button getUploadButton()
    {
        return this.uploadButton;
    }


    protected Button getCreateMediaButton()
    {
        return this.createMediaButton;
    }


    protected Button getPreviewButton()
    {
        return this.previewButton;
    }


    protected Button getResetButton()
    {
        return this.resetButton;
    }


    protected Div getUploadedFileDetails()
    {
        return this.uploadedFileDetails;
    }


    protected MediaExtractor getMediaExtractor()
    {
        return this.mediaExtractor;
    }
}
