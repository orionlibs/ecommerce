package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.helpers.LocaleHelper;
import de.hybris.platform.cockpit.helpers.impl.DefaultLocaleHelper;
import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.services.label.LabelService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import java.text.NumberFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class PriceRowRenderer implements CellRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(PriceRowRenderer.class);
    private LocaleHelper localeHelper = (LocaleHelper)new DefaultLocaleHelper();


    public void render(TableModel model, int colIndex, int rowIndex, Component parent)
    {
        if(model == null || parent == null)
        {
            throw new IllegalArgumentException("Model and parent can not be null.");
        }
        String text = "";
        Double value = null;
        try
        {
            value = (Double)model.getValueAt(colIndex, rowIndex);
        }
        catch(IllegalArgumentException iae)
        {
            LOG.warn("Could not render cell (Reason: '" + iae.getMessage() + "').", iae);
        }
        Div div = new Div();
        div.setStyle("overflow: hidden;height: 100%;");
        if(ValueHandler.NOT_READABLE_VALUE.equals(value))
        {
            div.setSclass("listview_notreadable_cell");
            text = Labels.getLabel("listview.cell.readprotected");
        }
        else if(value != null)
        {
            LabelService labelService = UISessionUtils.getCurrentSession().getLabelService();
            text = TypeTools.getValueAsString(labelService, getFormattedPrice(value));
        }
        Label label = new Label(text);
        div.appendChild((Component)label);
        parent.appendChild((Component)div);
    }


    private String getCurrentLangCode()
    {
        return UISessionUtils.getCurrentSession().getSystemService().getCurrentLanguage().getIsocode();
    }


    private String getFormattedPrice(Double val)
    {
        return NumberFormat.getInstance(this.localeHelper.getLocale(getCurrentLangCode())).format(val);
    }
}
