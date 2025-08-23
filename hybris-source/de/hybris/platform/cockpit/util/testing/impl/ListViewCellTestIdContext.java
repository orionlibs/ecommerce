package de.hybris.platform.cockpit.util.testing.impl;

import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.util.testing.TestIdContext;

public class ListViewCellTestIdContext implements TestIdContext
{
    private final TableModel tableModel;
    private final int column;
    private final int row;


    public ListViewCellTestIdContext(TableModel tableModel, int column, int row)
    {
        this.tableModel = tableModel;
        this.row = row;
        this.column = column;
    }


    public TableModel getTableModel()
    {
        return this.tableModel;
    }


    public int getRow()
    {
        return this.row;
    }


    public int getColumn()
    {
        return this.column;
    }
}
