package de.hybris.platform.cockpit.components.sync.dialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Listitem;

public class ManySourceManyTargetVersionSyncDialog extends ManySourceManyTargetItemSyncDialog
{
    public ManySourceManyTargetVersionSyncDialog(Map<String, String>[] syncRules, Collection data)
    {
        super((Map[])syncRules, data);
    }


    public void performAction()
    {
        List<String> selectedRules = new ArrayList<>();
        List<String> names = new ArrayList<>();
        for(Object item : getAvailableCatalogVersions().getSelectedItems())
        {
            SyncRule rule = (SyncRule)((Listitem)item).getValue();
            selectedRules.add(rule.getPk());
            names.add(rule.getName());
        }
        getSynchronizationService().performCatalogVersionSynchronization(this.data, selectedRules, null, null);
        setVisible(false);
        getParent().removeChild((Component)this);
        updateBackground(names);
    }
}
