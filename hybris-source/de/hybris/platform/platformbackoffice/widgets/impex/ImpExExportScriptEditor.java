package de.hybris.platform.platformbackoffice.widgets.impex;

import com.hybris.cockpitng.editors.EditorContext;
import de.hybris.platform.impex.enums.ImpExValidationModeEnum;
import de.hybris.platform.platformbackoffice.widgets.BasicScriptEditor;
import de.hybris.platform.servicelayer.impex.ImpExValidationResult;

public class ImpExExportScriptEditor extends BasicScriptEditor
{
    protected void handleValidateEvent(EditorContext<String> context)
    {
        String exportScript = this.scriptTextbox.getText();
        if(exportScript != null)
        {
            ImpExExportForm exportForm = (ImpExExportForm)this.widgetInstanceManager.getModel().getValue(this.modelContentPath, ImpExExportForm.class);
            ImpExValidationModeEnum mode = exportForm.getValidationMode();
            ImpExValidationResult result = this.exportService.validateExportScript(exportScript, mode);
            showValidationResult(context, result);
        }
    }
}
