package de.hybris.platform.platformbackoffice.editors.standarddaterange;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.core.util.Validate;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.util.Range;
import de.hybris.platform.util.StandardDateRange;
import java.util.Date;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;

public class DefaultStandardDateRangeEditor extends AbstractCockpitEditorRenderer<StandardDateRange>
{
    public static final String SUBEDITOR_DEFINITION_STRING = "Range(java.util.Date)";
    protected Editor editor;


    public void render(Component parent, EditorContext<StandardDateRange> context, EditorListener<StandardDateRange> listener)
    {
        Validate.notNull("parent component can not be null", new Object[] {parent});
        Validate.notNull("context can not be null", new Object[] {context});
        this.editor = new Editor();
        this.editor.setReadableLocales(context.getReadableLocales());
        this.editor.setWritableLocales(context.getWritableLocales());
        this.editor.setSclass("yw-std-range-editor");
        this.editor.setType("Range(java.util.Date)");
        this.editor.setReadOnly(!context.isEditable());
        this.editor.setOptional(context.isOptional());
        this.editor.addEventListener("onValueChanged", event -> processValueChange(listener, context));
        this.editor.setOrdered(context.isOrdered());
        this.editor.addParameters(context.getParameters());
        StandardDateRange value = (StandardDateRange)context.getInitialValue();
        if(value != null)
        {
            this.editor.setValue(new Range(value.getStart(), value.getEnd()));
        }
        this.editor.afterCompose();
        parent.appendChild((Component)this.editor);
    }


    protected void processValueChange(EditorListener<StandardDateRange> listener, EditorContext<StandardDateRange> context)
    {
        Range<Date> dateRange = (Range<Date>)this.editor.getValue();
        if(dateRange == null)
        {
            resetAndThrowAnError(listener, context);
        }
        Date start = (Date)dateRange.getStart();
        Date end = (Date)dateRange.getEnd();
        if(start == null && end == null)
        {
            listener.onValueChanged(null);
        }
        else if(start != null && end != null && start.before(end))
        {
            listener.onValueChanged(new StandardDateRange(start, end));
        }
        else
        {
            resetAndThrowAnError(listener, context);
        }
    }


    private void resetAndThrowAnError(EditorListener<StandardDateRange> listener, EditorContext<StandardDateRange> context)
    {
        listener.onValueChanged(null);
        throw new WrongValueException(this.editor, context.getLabel("wrong.date.validation.message"));
    }
}
