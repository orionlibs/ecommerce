package de.hybris.platform.adaptivesearchbackoffice.editors.configurableintegereditor;

import com.hybris.cockpitng.editor.defaultinteger.DefaultIntegerEditor;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import de.hybris.platform.adaptivesearchbackoffice.common.ReplaceableDataHandler;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorRuntimeException;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.impl.InputElement;

public class ConfigurableIntegerEditor extends DefaultIntegerEditor
{
    protected static final String REPLACEABLE_DATA_HANDLER = "replaceableDataHandler";
    private ReplaceableDataHandler<Integer> replaceableDataHandler;


    public void render(Component parent, EditorContext<Integer> context, EditorListener<Integer> listener)
    {
        setReplaceableDataHandler(createReplaceableDataHandler(context));
        EditorContext<Integer> clone = EditorContext.clone(context, loadInitialValue(context));
        super.render(parent, clone, listener);
    }


    protected void initViewComponent(InputElement editorView, EditorContext<Integer> context, EditorListener<Integer> listener)
    {
        editorView.setDisabled(getReplaceableDataHandler().isReplaceable(context));
        super.initViewComponent(editorView, context, listener);
    }


    protected Integer loadInitialValue(EditorContext<Integer> context)
    {
        return (Integer)getReplaceableDataHandler().getValue(context);
    }


    protected ReplaceableDataHandler<Integer> createReplaceableDataHandler(EditorContext<Integer> context)
    {
        String dataProviderId = ObjectUtils.toString(context.getParameter("replaceableDataHandler"));
        if(StringUtils.isBlank(dataProviderId))
        {
            throw new EditorRuntimeException(String.format("%s parameter has not been configured for %s editor (%s)", new Object[] {"replaceableDataHandler", context.getDefinition().getName(), context.getEditorLabel()}));
        }
        return (ReplaceableDataHandler<Integer>)BackofficeSpringUtil.getBean(dataProviderId);
    }


    protected ReplaceableDataHandler<Integer> getReplaceableDataHandler()
    {
        return this.replaceableDataHandler;
    }


    @Required
    protected void setReplaceableDataHandler(ReplaceableDataHandler<Integer> replaceableDataHandler)
    {
        this.replaceableDataHandler = replaceableDataHandler;
    }
}
