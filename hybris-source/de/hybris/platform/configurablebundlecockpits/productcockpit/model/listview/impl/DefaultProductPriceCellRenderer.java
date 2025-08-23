package de.hybris.platform.configurablebundlecockpits.productcockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.product.ProductModel;
import java.util.Collection;
import java.util.Iterator;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class DefaultProductPriceCellRenderer implements CellRenderer
{
    private static final Logger LOG = Logger.getLogger(DefaultProductPriceCellRenderer.class);
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
            LOG.warn("Could not render cell (Reason: '" + iae.getMessage() + "').");
        }
        Div div = new Div();
        div.setStyle("overflow: hidden;height: 100%;");
        if(ValueHandler.NOT_READABLE_VALUE.equals(value))
        {
            div.setSclass("listview_notreadable_cell");
            text = Labels.getLabel("listview.cell.readprotected");
        }
        else if(text.isEmpty())
        {
            Collection<TypedObject> products = (Collection)value;
            text = getProductNamesShortened(products);
        }
        Label label = new Label(text);
        div.appendChild((Component)label);
        parent.appendChild((Component)div);
    }


    protected String getProductNamesShortened(Collection<TypedObject> associatedProducts)
    {
        StringBuilder productsBuilder = new StringBuilder();
        if(!associatedProducts.isEmpty())
        {
            Iterator<TypedObject> pmit = associatedProducts.iterator();
            ProductModel pm = (ProductModel)((TypedObject)pmit.next()).getObject();
            if(pmit.hasNext())
            {
                productsBuilder.append(pm.getName()).append(",...");
            }
            else
            {
                productsBuilder.append(pm.getName());
            }
        }
        return productsBuilder.toString();
    }


    protected String getBasePrice(ProductModel pm)
    {
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
