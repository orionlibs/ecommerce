package de.hybris.platform.productcockpit.util.testing.impl;

import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.util.testing.impl.DefaultListViewCellTestIdBuilder;
import de.hybris.platform.cockpit.util.testing.impl.ListViewCellTestIdContext;
import de.hybris.platform.core.model.product.ProductModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductListViewCellTestIdBuilder extends DefaultListViewCellTestIdBuilder
{
    private static final String UNDERSCORE = "_";
    private static final Logger LOG = LoggerFactory.getLogger(ProductListViewCellTestIdBuilder.class);


    public String buildTestId(ListViewCellTestIdContext context)
    {
        TableModel model = context.getTableModel();
        try
        {
            Object listItem = model.getListComponentModel().getValueAt(context.getRow());
            Object object = ((TypedObject)listItem).getObject();
            if(listItem instanceof TypedObject && object instanceof ProductModel)
            {
                ColumnDescriptor column = model.getColumnComponentModel().getVisibleColumn(context.getColumn());
                String name = column.getName();
                if(StringUtils.isBlank(name))
                {
                    name = "unnamed";
                }
                else
                {
                    name = StringUtils.deleteWhitespace(name);
                }
                StringBuilder test_id = new StringBuilder("Listview_" + name);
                ProductModel product = (ProductModel)object;
                test_id.append("_").append(product.getCode()).append("_")
                                .append(product.getCatalogVersion().getMnemonic());
                return test_id.toString();
            }
            return super.buildTestId(context);
        }
        catch(Exception e)
        {
            LOG.debug("Could not apply test id, reason: ", e);
            return null;
        }
    }


    public Class getContextClass()
    {
        return ListViewCellTestIdContext.class;
    }
}
