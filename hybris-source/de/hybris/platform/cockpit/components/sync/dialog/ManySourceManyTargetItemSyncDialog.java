package de.hybris.platform.cockpit.components.sync.dialog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

public class ManySourceManyTargetItemSyncDialog extends AbstractSyncDialog
{
    protected final Collection<Object> data;


    public ManySourceManyTargetItemSyncDialog(Map<String, String>[] syncRules, Collection<Object> data)
    {
        super((Map[])syncRules);
        this.data = data;
    }


    public void performAction()
    {
        List<String> selectedRules = new ArrayList<>();
        for(Object item : getAvailableCatalogVersions().getSelectedItems())
        {
            SyncRule rule = (SyncRule)((Listitem)item).getValue();
            selectedRules.add(rule.getPk());
        }
        getSynchronizationService().performSynchronization(this.data, selectedRules, null, null);
        setVisible(false);
        getParent().removeChild((Component)this);
        updateBackground(selectedRules);
    }


    public void performReturn()
    {
        setVisible(false);
        getParent().removeChild((Component)this);
    }


    public List<SyncRule> getForbidenRulesListModel()
    {
        List<SyncRule> syncArrays = new ArrayList<>();
        for(Map.Entry<String, String> entry : getSyncRules()[1].entrySet())
        {
            syncArrays.add(new SyncRule(entry.getValue(), entry.getKey(), null));
        }
        return syncArrays;
    }


    public List<SyncRule> getAccessibleRulesListModel()
    {
        List<SyncRule> syncArrays = new ArrayList<>();
        for(Map.Entry<String, String> entry : getSyncRules()[0].entrySet())
        {
            syncArrays.add(new SyncRule(entry.getValue(), entry.getKey(), null));
        }
        return syncArrays;
    }


    public void itemListRenderer(Listitem item, Object data)
    {
        SyncRule syncRule = (SyncRule)data;
        Listcell cell = new Listcell();
        Label label = new Label(syncRule.getName());
        cell.appendChild((Component)label);
        item.setValue(syncRule);
        item.appendChild((Component)cell);
    }
}
