package de.hybris.platform.cockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.contentbrowser.MultiTypeListMainAreaComponentFactory;
import de.hybris.platform.cockpit.components.listview.AbstractMultiSelectOnlyAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.ColumnModel;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.ColumnConfiguration;
import de.hybris.platform.cockpit.services.config.impl.MultiTypeColumnConfiguration;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Filedownload;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class ExportCsvAction extends AbstractMultiSelectOnlyAction
{
    protected static final String ICON_EXPORT_CSV_ACTION_AVAILABLE = "cockpit/images/icon_func_export_csv_22.png";
    protected static final String ICON_EXPORT_CSV_ACTION_UNAVAILABLE = "cockpit/images/icon_func_export_csv_unavailable_22.png";
    private boolean truncateCollections;
    private LabelService labelService;


    public EventListener getMultiSelectEventListener(ListViewAction.Context context)
    {
        Object object = null;
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(context.getModel() != null && selectedItems != null && !selectedItems.isEmpty() && selectedItems.size() >= 1)
        {
            object = new Object(this, context, selectedItems);
        }
        return (EventListener)object;
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(context.getModel() != null && selectedItems != null && !selectedItems.isEmpty() && selectedItems.size() >= 1)
        {
            return "cockpit/images/icon_func_export_csv_22.png";
        }
        return "cockpit/images/icon_func_export_csv_unavailable_22.png";
    }


    public Menupopup getMultiSelectPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("exportcsvaction.tooltip");
    }


    private void createAndSaveCSVForMultitype(MultiTypeListMainAreaComponentFactory.MultiTypeListViewConfiguration multiTypeListViewConfiguration, List<TypedObject> list) throws ValueHandlerException
    {
        StringBuilder csvContent = new StringBuilder();
        List<MultiTypeColumnConfiguration> columnConfigurations = new ArrayList<>();
        for(ColumnConfiguration columnConfiguration : multiTypeListViewConfiguration.getRootColumnGroupConfiguration()
                        .getColumnConfigurations())
        {
            if(columnConfiguration instanceof MultiTypeColumnConfiguration && ((MultiTypeColumnConfiguration)columnConfiguration)
                            .getColumnDescriptor().isVisible())
            {
                columnConfigurations.add((MultiTypeColumnConfiguration)columnConfiguration);
            }
        }
        for(MultiTypeColumnConfiguration columnConfiguration : columnConfigurations)
        {
            csvContent.append(columnConfiguration.getColumnDescriptor().getName()).append(";");
        }
        csvContent.append("\n");
        for(TypedObject item : list)
        {
            for(ColumnConfiguration columnConfiguration : columnConfigurations)
            {
                String columnValue = getEscapedCsvColumnValue(
                                String.valueOf(((MultiTypeColumnConfiguration)columnConfiguration).getValueHandler().getValue(item)));
                csvContent.append(columnValue).append(";");
            }
            csvContent.append("\n");
        }
        Filedownload.save(csvContent.toString(), "text/comma-separated-values;charset=UTF-8", "list.csv");
    }


    private String getEscapedCsvColumnValue(String value)
    {
        String ret = value;
        if(value.contains(";") || value.contains("\""))
        {
            ret = "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return ret.replace('\n', ' ');
    }


    private void createAndSaveCSV(ListViewAction.Context context)
    {
        BrowserModel browser = context.getBrowserModel();
        TableModel tableModel = context.getModel();
        if(browser != null && tableModel != null)
        {
            List<TypedObject> allItems = browser.getSelectedItems();
            List<ColumnDescriptor> visibleColumns = tableModel.getColumnComponentModel().getVisibleColumns();
            if(allItems != null)
            {
                List<LocalizedProperty> localizedPropertyDescriptors = new ArrayList<>();
                Set<PropertyDescriptor> propertyDescriptors = new HashSet<>();
                StringBuffer buffer = new StringBuffer();
                for(ColumnDescriptor columnDescriptor : visibleColumns)
                {
                    ColumnModel columnComponentModel = tableModel.getColumnComponentModel();
                    PropertyDescriptor propertyDescriptor = columnComponentModel.getPropertyDescriptor(columnDescriptor);
                    if(propertyDescriptor != null)
                    {
                        localizedPropertyDescriptors.add(new LocalizedProperty(this, propertyDescriptor,
                                        (columnDescriptor.getLanguage() == null) ? null : columnDescriptor.getLanguage().getIsocode()));
                        propertyDescriptors.add(propertyDescriptor);
                        buffer.append(columnDescriptor.getName() + ";");
                    }
                }
                buffer.append('\n');
                for(TypedObject item : allItems)
                {
                    buffer.append(getCSVRow(item, localizedPropertyDescriptors, propertyDescriptors));
                }
                Filedownload.save(buffer.toString(), "text/comma-separated-values;charset=UTF-8", "list.csv");
            }
        }
    }


    private String getCSVRow(TypedObject item, List<LocalizedProperty> localizedPropertyDescriptors, Set<PropertyDescriptor> descriptors)
    {
        StringBuffer buffer = new StringBuffer();
        ObjectValueContainer valueContainer = TypeTools.createValueContainer(item, descriptors,
                        UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos());
        for(LocalizedProperty localizedProperty : localizedPropertyDescriptors)
        {
            Object value = null;
            if(valueContainer.hasProperty(localizedProperty.getPropDesc(), localizedProperty.getLang()))
            {
                ObjectValueContainer.ObjectValueHolder valueHolder = valueContainer.getValue(localizedProperty.getPropDesc(), localizedProperty
                                .getLang());
                value = valueHolder.getCurrentValue();
            }
            if(value != null)
            {
                String valueAsString = "";
                if(this.truncateCollections)
                {
                    valueAsString = TypeTools.getValueAsString(this.labelService, value);
                }
                else
                {
                    valueAsString = TypeTools.getValueAsString(this.labelService, value, -1);
                }
                if(valueAsString == null)
                {
                    valueAsString = "";
                }
                else
                {
                    valueAsString = getEscapedCsvColumnValue(valueAsString);
                }
                buffer.append(valueAsString);
            }
            buffer.append(';');
        }
        buffer.append('\n');
        return buffer.toString();
    }


    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }


    public LabelService getLabelService()
    {
        if(this.labelService == null)
        {
            this.labelService = UISessionUtils.getCurrentSession().getLabelService();
        }
        return this.labelService;
    }


    public boolean isTruncateCollections()
    {
        return this.truncateCollections;
    }


    public void setTruncateCollections(boolean truncateCollections)
    {
        this.truncateCollections = truncateCollections;
    }
}
