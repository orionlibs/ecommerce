package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.general.MutableListModel;
import de.hybris.platform.cockpit.model.listview.MutableColumnModel;
import java.util.Collections;
import java.util.List;

public class TaskTableModel extends DefaultTableModel
{
    private List<String> attachmentTypes = null;


    public TaskTableModel(MutableListModel listComponentModel, MutableColumnModel columnComponentModel)
    {
        super(listComponentModel, columnComponentModel);
    }


    public TaskTableModel(MutableListModel listComponentModel, MutableColumnModel columnComponentModel, String attachmentType)
    {
        this(listComponentModel, columnComponentModel, Collections.singletonList(attachmentType));
    }


    public TaskTableModel(MutableListModel listComponentModel, MutableColumnModel columnComponentModel, List<String> attachmentTypes)
    {
        super(listComponentModel, columnComponentModel);
        setAttachmentTypes(attachmentTypes);
    }


    public String getAttachmentType()
    {
        if(!getAttachmentTypes().isEmpty())
        {
            return getAttachmentTypes().get(0);
        }
        return null;
    }


    public void setAttachmentType(String attachmentType)
    {
        setAttachmentTypes(Collections.singletonList(attachmentType));
    }


    public void setAttachmentTypes(List<String> attachmentTypes)
    {
        this.attachmentTypes = attachmentTypes;
    }


    public List<String> getAttachmentTypes()
    {
        return this.attachmentTypes;
    }
}
