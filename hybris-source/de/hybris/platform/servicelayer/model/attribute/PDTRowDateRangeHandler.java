package de.hybris.platform.servicelayer.model.attribute;

import de.hybris.platform.europe1.model.PDTRowModel;
import de.hybris.platform.util.StandardDateRange;
import java.util.Date;

public class PDTRowDateRangeHandler implements DynamicAttributeHandler<StandardDateRange, PDTRowModel>
{
    public StandardDateRange get(PDTRowModel model)
    {
        Date start = model.getStartTime();
        Date end = model.getEndTime();
        if(start != null && end != null)
        {
            return new StandardDateRange(start, end);
        }
        return null;
    }


    public void set(PDTRowModel model, StandardDateRange standardDateRange)
    {
        if(standardDateRange != null)
        {
            model.setStartTime(standardDateRange.getStart());
            model.setEndTime(standardDateRange.getEnd());
        }
        else
        {
            model.setStartTime(null);
            model.setEndTime(null);
        }
    }
}
