package de.hybris.platform.platformbackoffice.widgets.catalogsynchronization;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.config.jaxb.wizard.ViewType;
import com.hybris.cockpitng.dataaccess.facades.permissions.PermissionFacade;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.configurableflow.renderer.DefaultCustomViewRenderer;
import de.hybris.platform.catalog.synchronization.SyncResult;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vlayout;

public class CustomSyncResultRenderer extends DefaultCustomViewRenderer
{
    private PermissionFacade permissionFacade;
    private static final String LABEL_CURRENT_CONTEXT_SYNC_RESULT = "currentContext.syncResult.cronJob";


    public void render(Component parent, ViewType customView, Map<String, String> parameters, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        parent.getChildren().clear();
        Vlayout cnt = new Vlayout();
        cnt.setParent(parent);
        SyncResult syncResult = (SyncResult)widgetInstanceManager.getModel().getValue("currentContext.syncResult", SyncResult.class);
        Label syncFinishedLabel = new Label("Synchronization finished: " + syncResult.isFinished());
        if(BooleanUtils.isTrue((Boolean)widgetInstanceManager.getModel().getValue("startSyncForm.runInBackground", Boolean.class)))
        {
            cnt.appendChild((Component)new Label("Asynchronous mode - check the cronjob to get the latest status..."));
            cnt.appendChild((Component)syncFinishedLabel);
            cnt.appendChild((Component)createEditor(widgetInstanceManager, "Reference(SyncItemCronJob)", "currentContext.syncResult.cronJob",
                            Labels.getLabel("currentContext.syncResult.cronJob")));
        }
        else
        {
            cnt.appendChild((Component)syncFinishedLabel);
            cnt.appendChild((Component)createEditor(widgetInstanceManager, "Reference(SyncItemCronJob)", "currentContext.syncResult.cronJob",
                            Labels.getLabel("currentContext.syncResult.cronJob")));
            cnt.appendChild((Component)new Label("Status: " + syncResult.getCronJob().getStatus()));
            cnt.appendChild((Component)new Label("Result: " + syncResult.getCronJob().getResult()));
        }
    }


    private Div createEditor(WidgetInstanceManager wim, String editorType, String property, String label)
    {
        Editor editor = new Editor();
        editor.setWidgetInstanceManager(wim);
        editor.setType(editorType);
        Set<Locale> writableLocales = this.permissionFacade.getAllWritableLocalesForCurrentUser();
        Set<Locale> readableLocales = this.permissionFacade.getAllReadableLocalesForCurrentUser();
        editor.setReadableLocales(readableLocales);
        editor.setWritableLocales(writableLocales);
        editor.setNestedObjectCreationDisabled(false);
        editor.setReadOnly(true);
        editor.setProperty(property);
        editor.initialize();
        Div row = new Div();
        row.appendChild((Component)new Label(label));
        row.appendChild((Component)editor);
        return row;
    }


    @Required
    public void setPermissionFacade(PermissionFacade permissionFacade)
    {
        this.permissionFacade = permissionFacade;
    }
}
