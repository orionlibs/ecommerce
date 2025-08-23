package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.events.impl.SectionModelEvent;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.meta.ObjectTemplate;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.ListSectionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.List;

public class DefaultListSectionModel extends DefaultSectionModel implements ListSectionModel
{
    protected ObjectTemplate rootType = null;
    private MutableTableModel tableModel = null;
    private String listViewConfigCode;


    public DefaultListSectionModel()
    {
        this("");
    }


    public DefaultListSectionModel(String label)
    {
        this(label, null);
    }


    public DefaultListSectionModel(String label, Object rootItem)
    {
        super(label, rootItem);
    }


    public ObjectTemplate getRootType()
    {
        if(this.rootType == null)
        {
            if(getItems() == null || getItems().isEmpty())
            {
                this.rootType = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate("Item");
            }
            else
            {
                this.rootType = UISessionUtils.getCurrentSession().getTypeService().getObjectTemplate(((TypedObject)
                                getItems().get(0)).getType().getCode());
            }
        }
        return this.rootType;
    }


    public void setRootType(ObjectTemplate rootType)
    {
        if((this.rootType == null && rootType != null) || (this.rootType != null && !this.rootType.equals(rootType)))
        {
            this.rootType = rootType;
            if(this.initialized)
            {
                this.modified = true;
                fireEvent(new SectionModelEvent(this, "changed"));
            }
        }
    }


    public MutableTableModel getTableModel()
    {
        return this.tableModel;
    }


    public void setTableModel(MutableTableModel tableModel)
    {
        this.tableModel = tableModel;
    }


    public void setListViewConfigurationCode(String code)
    {
        this.listViewConfigCode = code;
    }


    public String getListViewConfigurationCode()
    {
        return this.listViewConfigCode;
    }


    public void setSelectedIndex(int index)
    {
        if(this.initialized && getTableModel() != null)
        {
            getTableModel().getListComponentModel().setSelectedIndexDirectly(index);
        }
        super.setSelectedIndex(index);
    }


    public void setSelectedIndexes(List<Integer> indexes)
    {
        if(this.initialized && getTableModel() != null)
        {
            getTableModel().getListComponentModel().setSelectedIndexesDirectly(indexes);
        }
        super.setSelectedIndexes(indexes);
    }
}
