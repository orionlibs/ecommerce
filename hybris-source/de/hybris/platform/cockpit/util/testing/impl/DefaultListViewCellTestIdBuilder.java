package de.hybris.platform.cockpit.util.testing.impl;

import de.hybris.platform.cockpit.model.listview.ColumnDescriptor;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.util.testing.TestIdBuilder;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultListViewCellTestIdBuilder implements TestIdBuilder<ListViewCellTestIdContext>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultListViewCellTestIdBuilder.class);


    public String buildTestId(ListViewCellTestIdContext context)
    {
        TableModel model = context.getTableModel();
        try
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
            test_id.append("_row_").append(context.getRow());
            return test_id.toString();
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
