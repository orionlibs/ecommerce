package de.hybris.platform.cockpit.components.sync.dialog;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.zkoss.zhtml.Table;
import org.zkoss.zhtml.Td;
import org.zkoss.zhtml.Tr;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Image;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;

public class OneSourceManyTargetVersionSyncDialog extends AbstractSyncDialog
{
    public OneSourceManyTargetVersionSyncDialog(CatalogVersionModel sourceCatalogVersion, List<SyncItemJobModel>[] matrixRules)
    {
        super(sourceCatalogVersion, (List[])matrixRules);
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
                    getSynchronizationService().performCatalogVersionSynchronization(
                                    Collections.singletonList(getSourceCatalogVersion()), null, (CatalogVersionModel)selectedRule.getSource(), selectedRule
                                                    .getName());
                    Events.postEvent("onAfterSynchronization", (Component)getAvailableCatalogVersions(), null);
                    updateBackground(Collections.singletonList((String)getAvailableCatalogVersions().getSelectedItem().getAttribute("name")));
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
        Image syncStatus = new Image();
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
        containerSecCell.setStyle("verticla-align:middle");
        containerSecCell.appendChild((Component)syncStatus);
        cell.appendChild((Component)cellContainer);
        item.setValue(rule);
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


    public void updateBackground(List<String> chosenRules)
    {
        ((BaseUICockpitPerspective)UISessionUtils.getCurrentSession().getCurrentPerspective()).getBrowserArea().update();
    }
}
