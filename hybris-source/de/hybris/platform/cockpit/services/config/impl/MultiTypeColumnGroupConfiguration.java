package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.impl.DefaultTextCellRenderer;
import de.hybris.platform.cockpit.model.listview.impl.ImageCellRenderer;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.util.resource.Labels;

public class MultiTypeColumnGroupConfiguration extends DefaultColumnGroupConfiguration
{
    public static final String COLUMN_CODE = "code";
    public static final String COLUMN_PREVIEWIMAGE = "previewimage";
    public static final String COLUMN_SHORTINFO = "shortinfo";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_TYPE = "type";


    public MultiTypeColumnGroupConfiguration(String name)
    {
        super(name);
        setColumnConfigurations(createColumsConfigurations());
    }


    private List<ColumnConfiguration> createColumsConfigurations()
    {
        List<ColumnConfiguration> colsConfs = new ArrayList<>();
        LineNumberColumn lineNumberColumn = new LineNumberColumn(Labels.getLabel("listview.linenr"));
        lineNumberColumn.setVisible(true);
        colsConfs.add(lineNumberColumn);
        colsConfs.add(new MultiTypeColumnConfiguration("type", Labels.getLabel("listview.type"), (CellRenderer)new DefaultTextCellRenderer(), true));
        colsConfs.add(new MultiTypeColumnConfiguration("previewimage", Labels.getLabel("listview.preview"), (CellRenderer)new ImageCellRenderer(), true));
        colsConfs.add(new MultiTypeColumnConfiguration("code", Labels.getLabel("listview.code"), (CellRenderer)new DefaultTextCellRenderer(), true));
        colsConfs.add(new MultiTypeColumnConfiguration("shortinfo", Labels.getLabel("listview.shortinfo"), (CellRenderer)new DefaultTextCellRenderer(), true));
        colsConfs.add(new MultiTypeColumnConfiguration("description", Labels.getLabel("listview.description"), (CellRenderer)new DefaultTextCellRenderer(), true));
        MultiTypeActionColumnConfiguration actionColumn = new MultiTypeActionColumnConfiguration(this, Labels.getLabel("listview.actions"));
        actionColumn.setVisible(true);
        actionColumn.setSelectable(true);
        colsConfs.add(actionColumn);
        return colsConfs;
    }
}
