package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.config.impl.DefaultListViewConfiguration;
import de.hybris.platform.cockpit.services.config.impl.JaxbBasedUIComponentConfigurationContext;
import de.hybris.platform.cockpit.services.config.impl.PropertyColumnConfiguration;
import de.hybris.platform.cockpit.services.config.jaxb.listview.Parameter;
import de.hybris.platform.cockpit.services.config.jaxb.listview.Property;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;

public class DefaultTextCellRenderer implements CellRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultTextCellRenderer.class);
    private TypeService typeService;


    public void render(TableModel model, int colIndex, int rowIndex, Component parent)
    {
        if(model == null || parent == null)
        {
            throw new IllegalArgumentException("Model and parent can not be null.");
        }
        String text = "";
        Object value = null;
        try
        {
            value = model.getValueAt(colIndex, rowIndex);
        }
        catch(IllegalArgumentException iae)
        {
            LOG.warn("Could not render cell (Reason: '" + iae.getMessage() + "').", iae);
        }
        if(ValueHandler.NOT_READABLE_VALUE.equals(value))
        {
            Label label = new Label(Labels.getLabel("listview.cell.readprotected"));
            label.setSclass("listview_notreadable_cell");
            parent.appendChild((Component)label);
        }
        else if(value != null)
        {
            if(value instanceof java.util.Date)
            {
                text = "";
                DefaultColumnModel colModel = (DefaultColumnModel)model.getColumnComponentModel();
                if(colModel != null)
                {
                    DefaultListViewConfiguration lvConf = (DefaultListViewConfiguration)colModel.getConfiguration();
                    DefaultColumnDescriptor colDescr = (DefaultColumnDescriptor)colModel.getVisibleColumn(colIndex);
                    if(lvConf != null && colDescr != null)
                    {
                        PropertyDescriptor propertyDescriptor = colModel.getPropertyDescriptor((ColumnDescriptor)colDescr);
                        JaxbBasedUIComponentConfigurationContext<ListView> context = (JaxbBasedUIComponentConfigurationContext<ListView>)lvConf.getContext();
                        if(propertyDescriptor != null && context != null)
                        {
                            PropertyColumnConfiguration propertyColumnConfiguration = new PropertyColumnConfiguration(propertyDescriptor);
                            Property parameters = (Property)context.getJaxbElement(propertyColumnConfiguration);
                            if(parameters != null)
                            {
                                String dateFormat = getParameterValue(parameters.getParameter(), "dateFormat");
                                if(dateFormat != null && !dateFormat.isEmpty())
                                {
                                    try
                                    {
                                        text = (new SimpleDateFormat(dateFormat)).format(value);
                                    }
                                    catch(IllegalArgumentException e)
                                    {
                                        LOG.warn("'" + dateFormat + "' is not a valid dateFormat, using default");
                                        text = "";
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(text.isEmpty())
            {
                LabelService labelService = UISessionUtils.getCurrentSession().getLabelService();
                text = TypeTools.getValueAsString(labelService, value);
            }
            parent.appendChild((Component)new Label(text));
        }
    }


    private String getParameterValue(List<Parameter> params, String key)
    {
        if(params.isEmpty())
        {
            return null;
        }
        Iterator<Parameter> itParam = params.iterator();
        while(itParam.hasNext())
        {
            Parameter parameter = itParam.next();
            if(parameter.getName().equals(key))
            {
                return parameter.getValue();
            }
        }
        return null;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }
}
