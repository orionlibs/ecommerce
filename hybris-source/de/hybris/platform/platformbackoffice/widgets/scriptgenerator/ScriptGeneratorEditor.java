package de.hybris.platform.platformbackoffice.widgets.scriptgenerator;

import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import de.hybris.platform.platformbackoffice.widgets.BasicScriptEditor;
import de.hybris.platform.servicelayer.impex.ScriptGenerationConfig;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;

public class ScriptGeneratorEditor extends BasicScriptEditor
{
    private Button generateBtn;


    public void render(Component parent, EditorContext<String> context, EditorListener<String> listener)
    {
        Validate.notNull("All parameters are mandatory", new Object[] {parent, context, listener});
        super.render(parent, context, listener);
        this.generateBtn = new Button(context.getLabel("generate.button.label"));
        getActionsBox().getChildren().add(1, this.generateBtn);
        this.generateBtn.addEventListener("onClick", event -> handleGenerateEvent());
    }


    private void handleGenerateEvent()
    {
        ExportScriptGeneratorForm scriptForm = (ExportScriptGeneratorForm)this.widgetInstanceManager.getModel().getValue(this.modelContentPath, ExportScriptGeneratorForm.class);
        ScriptGenerationConfig scriptConfig = new ScriptGenerationConfig(scriptForm.getScriptType(), scriptForm.isDocumentId(), scriptForm.isIncludeSystemTypes(), scriptForm.getLanguages());
        String generatedScript = this.exportScriptGenerationService.generateScript(scriptConfig);
        updateScriptContent(generatedScript);
    }
}
