package de.hybris.platform.cockpit.components.sync.dialog;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Table;
import org.zkoss.zhtml.Td;
import org.zkoss.zhtml.Tr;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

public class OneSourceManyTargetItemSyncDialog extends AbstractSyncDialog
{
    private final TypedObject sourceItem;


    public OneSourceManyTargetItemSyncDialog(TypedObject sourceItem, CatalogVersionModel sourceCatalogVersion, List<SyncItemJobModel>[] matrixRules)
    {
        super(sourceCatalogVersion, (List[])matrixRules);
        this.sourceItem = sourceItem;
    }


    public void performAction()
    {
        Set<Listitem> selection = getAvailableCatalogVersions().getSelectedItems();
        if(!selection.isEmpty())
        {
            for(Listitem listitem : selection)
            {
                int itemIndex = getAvailableCatalogVersions().getIndexOfItem(listitem);
                if(itemIndex != -1)
                {
                    SyncRule selectedRule = (SyncRule)getAvailableCatalogVersions().getModel().getElementAt(itemIndex);
                    getSynchronizationService().performSynchronization(Collections.singletonList(this.sourceItem), null, (CatalogVersionModel)selectedRule
                                    .getSource(), selectedRule.getName());
                    Events.postEvent("onAfterSynchronization", (Component)getAvailableCatalogVersions(), null);
                    updateBackground(Collections.singletonList(selectedRule.getName()));
                }
            }
        }
    }


    public void performReturn()
    {
        setVisible(false);
        getParent().removeChild((Component)this);
    }


    public void itemListRenderer(Listitem item, Object data)
    {
        SyncRule rule = (SyncRule)data;
        Listcell cell = new Listcell();
        Hbox labelBox = prepareCatalogVersionLabels((CatalogVersionModel)rule.getSource(), rule, item);
        labelBox.setWidth("100%");
        Image syncStatus = new Image();
        List<String> okSyncStatuses = getSynchronizationService().getSynchronizationStatuses(getTargetCatalogVersions()[0], this.sourceItem);
        if(okSyncStatuses.contains(rule.getName()))
        {
            syncStatus.setSrc("cockpit/images//synchronization_ok.gif");
            syncStatus.setTooltiptext(Labels.getLabel("gridview.item.synchronized.true"));
        }
        else
        {
            syncStatus.setSrc("cockpit/images//synchronization_notok.gif");
            syncStatus.setTooltiptext(Labels.getLabel("gridview.item.synchronized.false"));
        }
        Table cellContainer = new Table();
        cellContainer.setDynamicProperty("width", "100%");
        cellContainer.setDynamicProperty("height", "100%");
        Tr cellRow = new Tr();
        cellRow.setParent((Component)cellContainer);
        Td containerFirstCell = new Td();
        containerFirstCell.setParent((Component)cellRow);
        containerFirstCell.setDynamicProperty("width", "100%");
        containerFirstCell.appendChild((Component)labelBox);
        Td containerSecCell = new Td();
        containerSecCell.setParent((Component)cellRow);
        containerSecCell.setDynamicProperty("width", "100%");
        containerSecCell.setStyle("vertical-align:middle");
        containerSecCell.appendChild((Component)syncStatus);
        cell.appendChild((Component)cellContainer);
        item.setValue(rule.getSource());
        item.appendChild((Component)cell);
    }


    public List<SyncRule> getForbidenRulesListModel()
    {
        List<SyncRule> ret = new ArrayList<>();
        for(SyncItemJobModel syncRule : getTargetCatalogVersions()[1])
        {
            ret.add(new SyncRule(syncRule.getCode(), syncRule.getTargetVersion().getPk().toString(), syncRule.getTargetVersion()));
        }
        return ret;
    }


    public List<SyncRule> getAccessibleRulesListModel()
    {
        List<SyncRule> ret = new ArrayList<>();
        for(SyncItemJobModel syncRule : getTargetCatalogVersions()[0])
        {
            ret.add(new SyncRule(syncRule.getCode(), syncRule.getTargetVersion().getPk().toString(), syncRule.getTargetVersion()));
        }
        return ret;
    }
}
