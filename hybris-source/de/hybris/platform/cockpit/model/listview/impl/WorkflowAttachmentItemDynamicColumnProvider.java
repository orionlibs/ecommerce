package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.components.listview.impl.AddItemCommentAction;
import de.hybris.platform.cockpit.components.listview.impl.DefaultActionColumnConfiguration;
import de.hybris.platform.cockpit.components.listview.impl.OpenCommentAction;
import de.hybris.platform.cockpit.model.general.ListModel;
import de.hybris.platform.cockpit.model.listview.DynamicColumnProvider;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.util.resource.Labels;

public class WorkflowAttachmentItemDynamicColumnProvider implements DynamicColumnProvider
{
    private String commentTypeCode = "workflow";


    public List<ColumnConfiguration> getDynamicColums(ListModel listModel)
    {
        List<ColumnConfiguration> result = new ArrayList<>();
        if(listModel != null && listModel.getElements() != null)
        {
            List<ListViewAction> commentActions = new ArrayList<>();
            OpenCommentAction openCommentAction = new OpenCommentAction();
            openCommentAction.setCommentTypeCode(this.commentTypeCode);
            commentActions.add(openCommentAction);
            result.add(createActionColumnConfiguration(Labels.getLabel("listview.comment"), commentActions, true));
            List<ListViewAction> actions = new ArrayList<>();
            AddItemCommentAction addItemCommentAction = new AddItemCommentAction();
            addItemCommentAction.setCommentTypeCode(this.commentTypeCode);
            actions.add(addItemCommentAction);
            result.add(createActionColumnConfiguration(Labels.getLabel("listview.actions"), actions, true));
        }
        return result;
    }


    private ColumnConfiguration createActionColumnConfiguration(String label, List<ListViewAction> actions, boolean visibilty)
    {
        DefaultActionColumnConfiguration actionColumnConfig = new DefaultActionColumnConfiguration(label);
        actionColumnConfig.setVisible(visibilty);
        actionColumnConfig.setActions(actions);
        return (ColumnConfiguration)actionColumnConfig;
    }


    public String getCommentTypeCode()
    {
        return this.commentTypeCode;
    }


    public void setCommentTypeCode(String commentTypeCode)
    {
        this.commentTypeCode = commentTypeCode;
    }
}
