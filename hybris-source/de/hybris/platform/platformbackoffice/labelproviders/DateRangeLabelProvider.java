package de.hybris.platform.platformbackoffice.labelproviders;

import com.hybris.cockpitng.editor.instant.labelprovider.impl.AbstractInstantEditorLabelProvider;
import com.hybris.cockpitng.labels.LabelProvider;
import com.hybris.cockpitng.util.Range;
import de.hybris.platform.util.StandardDateRange;
import java.util.Date;
import org.springframework.beans.factory.annotation.Required;

public class DateRangeLabelProvider extends AbstractInstantEditorLabelProvider
{
    private LabelProvider<Range<Date>> rangeLabelProvider;


    public String getLabel(String editorType, Object value)
    {
        if(value instanceof StandardDateRange)
        {
            StandardDateRange standardDateRange = (StandardDateRange)value;
            return getRangeLabelProvider().getLabel(new Range(standardDateRange.getStart(), standardDateRange.getEnd()));
        }
        return "";
    }


    public boolean canHandle(String editorType)
    {
        return StandardDateRange.class.getName().equals(editorType);
    }


    protected LabelProvider<Range<Date>> getRangeLabelProvider()
    {
        return this.rangeLabelProvider;
    }


    @Required
    public void setRangeLabelProvider(LabelProvider<Range<Date>> rangeLabelProvider)
    {
        this.rangeLabelProvider = rangeLabelProvider;
    }
}
