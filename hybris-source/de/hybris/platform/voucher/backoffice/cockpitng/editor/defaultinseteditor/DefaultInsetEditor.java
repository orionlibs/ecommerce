package de.hybris.platform.voucher.backoffice.cockpitng.editor.defaultinseteditor;

import com.hybris.cockpitng.components.Editor;
import com.hybris.cockpitng.editors.CockpitEditorRenderer;
import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.editors.impl.AbstractCockpitEditorRenderer;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.util.BackofficeSpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.WrongValueException;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;

public class DefaultInsetEditor extends AbstractCockpitEditorRenderer<Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultInsetEditor.class);
    public static final String EDITOR_ID_PARAM = "editor";
    public static final String INSET_BEAN_PARAM = "insetBean";
    public static final String INSERT_MODE_PARAM = "insertMode";
    public static final String INSERT_BEFORE_MODE = "before";
    public static final String INSERT_AFTER_MODE = "after";
    protected static final String IS_NESTED_OBJECT_CREATION_DISABLED_SETTING = "isNestedObjectCreationDisabled";
    public static final String INSET_EDITOR_CONTAINER_SCLASS = "ye-inset-editor";
    public static final String GENERAL_INSET_SCLASS = "inset";


    public void render(Component parent, EditorContext<Object> context, EditorListener<Object> listener)
    {
        if(parent != null && context != null && listener != null)
        {
            Div insetEditorContainer = new Div();
            insetEditorContainer.setSclass("ye-inset-editor");
            insetEditorContainer.setParent(parent);
            String insertMode = "after";
            Object insertModeObject = context.getParameter("insertMode");
            if(insertModeObject instanceof String)
            {
                insertMode = (String)insertModeObject;
            }
            if("before".equals(insertMode))
            {
                renderInset((Component)insetEditorContainer, context, listener);
            }
            renderEditor((Component)insetEditorContainer, context, listener);
            if(!"before".equals(insertMode))
            {
                renderInset((Component)insetEditorContainer, context, listener);
            }
        }
    }


    protected void renderEditor(Component parent, EditorContext<Object> context, EditorListener<Object> listener)
    {
        String editorId = "";
        Object editorIdObject = context.getParameter("editor");
        if(editorIdObject instanceof String)
        {
            editorId = (String)editorIdObject;
        }
        Object initialValue = context.getInitialValue();
        Editor subEditor = new Editor();
        subEditor.setReadableLocales(context.getReadableLocales());
        subEditor.setWritableLocales(context.getWritableLocales());
        subEditor.setType(context.getValueType());
        subEditor.setDefaultEditor(editorId);
        subEditor.setReadOnly(!context.isEditable());
        subEditor.setOrdered(context.isOrdered());
        subEditor.setValue(initialValue);
        subEditor.setOptional(context.isOptional());
        subEditor.setWidgetInstanceManager((WidgetInstanceManager)context.getParameter("wim"));
        subEditor.setNestedObjectCreationDisabled(isNestedObjectCreationDisabled(context));
        subEditor.addEventListener("onValueChanged", (EventListener)new Object(this, subEditor, listener));
        subEditor.addEventListener("onEditorEvent", (EventListener)new Object(this, listener));
        subEditor.addParameters(context.getParameters());
        subEditor.afterCompose();
        parent.appendChild((Component)subEditor);
    }


    protected void renderInset(Component parent, EditorContext<Object> context, EditorListener<Object> listener)
    {
        Object insetBeanName = context.getParameter("insetBean");
        if(insetBeanName instanceof String)
        {
            try
            {
                CockpitEditorRenderer<Object> editorInset = (CockpitEditorRenderer<Object>)BackofficeSpringUtil.getBean((String)insetBeanName, CockpitEditorRenderer.class);
                if(editorInset != null)
                {
                    editorInset.render(parent, context, (EditorListener)new Object(this, listener, parent));
                }
                else
                {
                    LOG.error("Inset bean {} could not be found.", insetBeanName);
                }
            }
            catch(WrongValueException e)
            {
                LOG.error(e.getMessage(), (Throwable)e);
            }
        }
        else
        {
            LOG.error("Parameter {} is not specified.", "insetBean");
        }
    }


    protected boolean isNestedObjectCreationDisabled(EditorContext<Object> context)
    {
        boolean isDisabled = false;
        Object paramObject = context.getParameter("isNestedObjectCreationDisabled");
        if(paramObject instanceof String)
        {
            isDisabled = Boolean.parseBoolean((String)paramObject);
        }
        return isDisabled;
    }


    protected Editor findAncestorEditor(Component component)
    {
        Component current = component;
        while(current != null && !(current instanceof Editor) && !(current instanceof com.hybris.cockpitng.core.Widget))
        {
            current = current.getParent();
        }
        if(current instanceof Editor)
        {
            return (Editor)current;
        }
        return null;
    }
}
