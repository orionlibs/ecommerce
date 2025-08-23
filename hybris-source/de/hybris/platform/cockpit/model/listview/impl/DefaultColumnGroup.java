package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.ColumnGroup;
import de.hybris.platform.cockpit.services.config.ColumnGroupConfiguration;
import de.hybris.platform.cockpit.services.config.impl.DefaultColumnGroupConfiguration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DefaultColumnGroup implements ColumnGroup
{
    private List<ColumnDescriptor> columns = new ArrayList<>();
    private List<ColumnGroup> groups = new ArrayList<>();
    private final ColumnGroupConfiguration groupConfig;
    private ColumnGroup parent;
    private String name;


    public DefaultColumnGroup(ColumnGroupConfiguration groupConfig)
    {
        this.groupConfig = groupConfig;
        String label = (groupConfig instanceof DefaultColumnGroupConfiguration) ? ((DefaultColumnGroupConfiguration)groupConfig).getLabelWithFallback() : groupConfig.getLabel();
        this.name = label;
    }


    public ColumnGroupConfiguration getColumnGroupConfiguration()
    {
        return this.groupConfig;
    }


    public void setColumnGroups(List<ColumnGroup> groups)
    {
        this.groups = groups;
        if(this.groups != null)
        {
            for(ColumnGroup group : groups)
            {
                if(group instanceof DefaultColumnGroup)
                {
                    ((DefaultColumnGroup)group).setParentColumnGroup(this);
                }
            }
        }
    }


    public List<ColumnGroup> getColumnGroups()
    {
        return this.groups;
    }


    public void addColumn(ColumnDescriptor column)
    {
        if(!getAllColumns().contains(column))
        {
            this.columns.add(column);
        }
    }


    public void clearDynamicColumns()
    {
        Iterator<ColumnDescriptor> iterator = this.columns.iterator();
        while(iterator.hasNext())
        {
            if(((ColumnDescriptor)iterator.next()).isDynamic())
            {
                iterator.remove();
            }
        }
    }


    public void addColumnGroup(ColumnGroup group)
    {
        if(!getAllColumnGroups().contains(group))
        {
            this.groups.add(group);
            if(group instanceof DefaultColumnGroup)
            {
                ((DefaultColumnGroup)group).setParentColumnGroup(this);
            }
        }
    }


    public void setColumns(List<ColumnDescriptor> columns)
    {
        this.columns = columns;
    }


    public List<ColumnDescriptor> getColumns()
    {
        return this.columns;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setParentColumnGroup(ColumnGroup parentGroup)
    {
        this.parent = parentGroup;
    }


    public ColumnGroup getParentColumnGroup()
    {
        return this.parent;
    }


    public int getSize()
    {
        return this.columns.size();
    }


    public int getTotalSize()
    {
        int totalSize = getSize();
        for(ColumnGroup group : this.groups)
        {
            totalSize += group.getTotalSize();
        }
        return totalSize;
    }


    public List<ColumnDescriptor> getAllColumns()
    {
        List<ColumnDescriptor> allColumns = new ArrayList<>();
        allColumns.addAll(this.columns);
        for(ColumnGroup group : this.groups)
        {
            allColumns.addAll(group.getAllColumns());
        }
        return allColumns;
    }


    public List<ColumnDescriptor> getAllHiddenColumns()
    {
        List<ColumnDescriptor> allHiddenCols = new ArrayList<>();
        allHiddenCols.addAll(getHiddenColumns());
        for(ColumnGroup group : this.groups)
        {
            allHiddenCols.addAll(group.getAllHiddenColumns());
        }
        return Collections.unmodifiableList(allHiddenCols);
    }


    public List<ColumnDescriptor> getAllVisibleColumns()
    {
        List<ColumnDescriptor> allVisibleCols = new ArrayList<>();
        allVisibleCols.addAll(getVisibleColumns());
        for(ColumnGroup group : this.groups)
        {
            allVisibleCols.addAll(group.getAllVisibleColumns());
        }
        return Collections.unmodifiableList(allVisibleCols);
    }


    public List<ColumnDescriptor> getHiddenColumns()
    {
        List<ColumnDescriptor> hiddenCols = new ArrayList<>();
        hiddenCols.addAll(getColumns());
        hiddenCols.removeAll(getVisibleColumns());
        return Collections.unmodifiableList(hiddenCols);
    }


    public List<ColumnDescriptor> getVisibleColumns()
    {
        List<ColumnDescriptor> visibleCols = new ArrayList<>();
        for(ColumnDescriptor col : getColumns())
        {
            if(col.isVisible())
            {
                visibleCols.add(col);
            }
        }
        return Collections.unmodifiableList(visibleCols);
    }


    public List<ColumnGroup> getAllColumnGroups()
    {
        List<ColumnGroup> allGroups = new ArrayList<>();
        allGroups.add(this);
        for(ColumnGroup subGroup : getColumnGroups())
        {
            allGroups.addAll(subGroup.getAllColumnGroups());
        }
        return Collections.unmodifiableList(allGroups);
    }
}
