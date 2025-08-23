package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cmscockpit.session.impl.CmsPageBrowserModel;
import de.hybris.platform.cockpit.model.listview.UIListView;
import de.hybris.platform.cockpit.model.listview.impl.DefaultBrowserSectionTableModelListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.ListBrowserSectionModel;
import de.hybris.platform.cockpit.session.Lockable;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.List;

public class CmsBrowserSectionTableModelListener extends DefaultBrowserSectionTableModelListener
{
    public CmsBrowserSectionTableModelListener(ListBrowserSectionModel sectionModel, UIListView view)
    {
        super(sectionModel, view);
    }


    public void selectionChanged(List<Integer> colIndexes, List<Integer> rowIndexes)
    {
        BrowserModel model = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser();
        if(model instanceof CmsPageBrowserModel)
        {
            CmsPageBrowserModel browserModel = (CmsPageBrowserModel)model;
            browserModel.clearSelection(this.sectionModel);
            this.sectionModel.setSelectedIndexes(rowIndexes);
            TypedObject changedItem = (TypedObject)this.view.getModel().getListComponentModel().getValueAt(((Integer)rowIndexes.get(0)).intValue());
            if(browserModel.getContentEditorSection().getRootItem() == null ||
                            !browserModel.getContentEditorSection().getRootItem().equals(changedItem))
            {
                browserModel.getContentEditorSection().setRootItem(changedItem);
                if(this.sectionModel instanceof Lockable)
                {
                    browserModel.getContentEditorSection().setReadOnly(((Lockable)this.sectionModel).isLocked());
                }
                browserModel.getContentEditorSection().update();
            }
            if(!browserModel.getContentEditorSection().isVisible())
            {
                browserModel.getContentEditorSection().setVisible(true);
            }
        }
        this.view.updateSelection();
    }
}
