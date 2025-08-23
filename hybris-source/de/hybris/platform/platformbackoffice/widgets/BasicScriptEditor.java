package de.hybris.platform.platformbackoffice.widgets;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.servicelayer.impex.ExportScriptGenerationService;
import de.hybris.platform.servicelayer.impex.ExportService;
import de.hybris.platform.servicelayer.impex.ImpExValidationResult;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Resource;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zul.Button;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Messagebox;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;

public class BasicScriptEditor extends AbstractCockpitEditorRenderer<String>
{
    protected String modelContentPath;
    protected String modelScriptContentPath;
    protected String modelImpexMediaPath;
    protected WidgetInstanceManager widgetInstanceManager;
    @Resource
    protected ExportScriptGenerationService exportScriptGenerationService;
    @Resource
    protected ExportService exportService;
    @Resource
    protected ModelService modelService;
    @Resource
    protected MediaService mediaService;
    protected Textbox scriptTextbox;
    protected Hbox actionsBox;
    protected Button validateBtn;
    protected Button saveBtn;


    public void render(Component parent, EditorContext<String> context, EditorListener<String> listener)
    {
        Validate.notNull("All parameters are mandatory", new Object[] {parent, context, listener});
        initModelPaths(context);
        Vbox vbox = new Vbox();
        vbox.setParent(parent);
        vbox.setWidth("100%");
        this.scriptTextbox = createScriptTextbox(parent, (String)context.getInitialValue());
        createContentButtons(parent, context);
        this.widgetInstanceManager = ((Editor)parent).getWidgetInstanceManager();
        registerModelObservers();
        this.validateBtn.addEventListener("onClick", event -> handleValidateEvent(context));
        this.saveBtn.addEventListener("onClick", event -> handleSaveEvent());
        activateContentButtons(getScriptTextbox().getText());
    }


    protected void activateContentButtons(String content)
    {
        boolean blankContent = StringUtils.isBlank(content);
        this.validateBtn.setDisabled(blankContent);
        this.saveBtn.setDisabled(blankContent);
    }


    protected void initModelPaths(EditorContext<String> context)
    {
        this.modelContentPath = (String)context.getParameterAs("formModel");
        this.modelScriptContentPath = (String)context.getParameterAs("scriptModel");
        this.modelImpexMediaPath = (String)context.getParameterAs("mediaModel");
    }


    protected void registerModelObservers()
    {
        this.widgetInstanceManager.getModel().addObserver(this.modelImpexMediaPath, () -> {
            ImpExMediaModel impExModel = (ImpExMediaModel)this.widgetInstanceManager.getModel().getValue(this.modelImpexMediaPath, ImpExMediaModel.class);
            if(impExModel != null)
            {
                byte[] byteScript = this.mediaService.getDataFromMedia((MediaModel)impExModel);
                String exportScriptContent = new String(byteScript);
                updateScriptContent(exportScriptContent);
            }
            else
            {
                updateScriptContent("");
            }
        });
    }


    protected void updateScriptContent(String content)
    {
        getScriptTextbox().setText(content);
        this.widgetInstanceManager.getModel().setValue(this.modelScriptContentPath, content);
    }


    protected void handleSaveEvent()
    {
        ImpExMediaModel impExModel = (ImpExMediaModel)this.widgetInstanceManager.getModel().getValue(this.modelImpexMediaPath, ImpExMediaModel.class);
        if(impExModel == null)
        {
            impExModel = createImpExMedia();
        }
        updateMediaScript(impExModel, getScriptTextbox().getText());
        this.widgetInstanceManager.getModel().setValue(this.modelImpexMediaPath, impExModel);
        this.widgetInstanceManager.getModel().changed();
    }


    protected ImpExMediaModel createImpExMedia()
    {
        ImpExMediaModel impExModel = (ImpExMediaModel)this.modelService.create(ImpExMediaModel.class);
        String timeStamp = (new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")).format(new Date());
        String targetName = "export_script_" + timeStamp + ".impex";
        impExModel.setCode(targetName);
        this.modelService.save(impExModel);
        return impExModel;
    }


    protected void updateMediaScript(ImpExMediaModel impExModel, String exportScript)
    {
        this.mediaService.setStreamForMedia((MediaModel)impExModel, IOUtils.toInputStream(exportScript, "UTF-8"));
        this.modelService.save(impExModel);
    }


    protected void handleValidateEvent(EditorContext<String> context)
    {
        String exportScript = getScriptTextbox().getText();
        if(exportScript != null)
        {
            ImpExValidationResult result = this.exportService.validateExportScript(exportScript, ImpExValidationModeEnum.EXPORT_ONLY);
            showValidationResult(context, result);
        }
    }


    protected void showValidationResult(EditorContext<String> context, ImpExValidationResult result)
    {
        if(result.isSuccessful())
        {
            Messagebox.show(context.getLabel("validation.successful"));
        }
        else
        {
            Messagebox.show(result.getFailureCause());
        }
    }


    protected Textbox createScriptTextbox(Component parent, String initialValue)
    {
        Textbox scriptInput = new Textbox();
        scriptInput.setWidth("100%");
        scriptInput.setRows(5);
        scriptInput.setCols(80);
        scriptInput.setMultiline(true);
        scriptInput.setParent(parent);
        scriptInput.setText(initialValue);
        scriptInput.addEventListener("onChanging", event -> activateContentButtons(((InputEvent)event).getValue()));
        return scriptInput;
    }


    protected void createContentButtons(Component parent, EditorContext<String> context)
    {
        Hbox hbox = new Hbox();
        this.actionsBox = hbox;
        hbox.setParent(parent);
        this.validateBtn = new Button(context.getLabel("validate.button.label"));
        this.validateBtn.setParent((Component)hbox);
        this.saveBtn = new Button(context.getLabel("save.button.label"));
        this.saveBtn.setParent((Component)hbox);
    }


    public Hbox getActionsBox()
    {
        return this.actionsBox;
    }


    public Button getSaveBtn()
    {
        return this.saveBtn;
    }


    public Button getValidateBtn()
    {
        return this.validateBtn;
    }


    public Textbox getScriptTextbox()
    {
        return this.scriptTextbox;
    }
}
