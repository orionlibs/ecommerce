package de.hybris.platform.platformbackoffice.widgets.catalogsynchronization;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Combobox;
import org.zkoss.zul.Comboitem;
import org.zkoss.zul.Div;

public class SyncJobSelectionEditor extends AbstractCockpitEditorRenderer<SyncItemJobModel>
{
    public void render(Component parent, EditorContext<SyncItemJobModel> context, EditorListener<SyncItemJobModel> listener)
    {
        Validate.notNull("All parameters are mandatory", new Object[] {parent, context, listener});
        WidgetInstanceManager widgetInstanceManager = ((Editor)parent).getWidgetInstanceManager();
        Map<String, Object> startSyncMap = (Map<String, Object>)widgetInstanceManager.getModel().getValue("startSyncForm", Map.class);
        List<SyncItemJobModel> availableSyncJobs = (List<SyncItemJobModel>)startSyncMap.get("syncItemJobs");
        SyncItemJobModel selectedSyncItemJob = (SyncItemJobModel)startSyncMap.get("selectedSyncItemJob");
        Div syncJobSelectionView = new Div();
        syncJobSelectionView.setParent(parent);
        Combobox syncJobsComboBox = createComboBox(parent, listener);
        fillComboBox(syncJobsComboBox, availableSyncJobs, selectedSyncItemJob);
    }


    protected Combobox createComboBox(Component parent, EditorListener<SyncItemJobModel> listener)
    {
        Combobox syncJobsComboBox = new Combobox();
        syncJobsComboBox.addEventListener("onChange", event -> {
            if(syncJobsComboBox.getSelectedItem() != null)
            {
                listener.onValueChanged(syncJobsComboBox.getSelectedItem().getValue());
            }
        });
        syncJobsComboBox.setParent(parent);
        return syncJobsComboBox;
    }


    protected void fillComboBox(Combobox syncJobsComboBox, List<SyncItemJobModel> availableSyncJobs, SyncItemJobModel selectedSyncItemJob)
    {
        for(SyncItemJobModel syncJob : availableSyncJobs)
        {
            Comboitem cmbItem = new Comboitem(syncJob.getCode());
            cmbItem.setValue(syncJob);
            syncJobsComboBox.appendChild((Component)cmbItem);
            if(syncJob.equals(selectedSyncItemJob))
            {
                syncJobsComboBox.setSelectedItem(cmbItem);
            }
        }
    }
}
