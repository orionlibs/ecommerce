package de.hybris.platform.cockpit.model.listview;

import java.util.List;

public interface ColumnGroup
{
    String getName();


    List<ColumnDescriptor> getColumns();


    List<ColumnDescriptor> getAllColumns();


    int getSize();


    List<ColumnDescriptor> getVisibleColumns();


    List<ColumnDescriptor> getAllVisibleColumns();


    List<ColumnDescriptor> getHiddenColumns();


    List<ColumnDescriptor> getAllHiddenColumns();


    int getTotalSize();


    ColumnGroup getParentColumnGroup();


    List<ColumnGroup> getColumnGroups();


    List<ColumnGroup> getAllColumnGroups();
}
