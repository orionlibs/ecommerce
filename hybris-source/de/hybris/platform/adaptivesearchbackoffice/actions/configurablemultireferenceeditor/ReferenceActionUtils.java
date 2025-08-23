package de.hybris.platform.adaptivesearchbackoffice.actions.configurablemultireferenceeditor;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.adaptivesearchbackoffice.data.AbstractEditorData;
import de.hybris.platform.adaptivesearchbackoffice.data.SearchRequestData;
import de.hybris.platform.adaptivesearchbackoffice.editors.configurablemultireference.MultiReferenceEditorLogic;

public final class ReferenceActionUtils
{
    protected static final String EDITOR_LOGIC_KEY = "editorLogic";


    public static WidgetInstanceManager resolveWidgetInstanceManager(ActionContext<? extends AbstractEditorData> ctx)
    {
        return (WidgetInstanceManager)ctx.getParameter("widgetInstanceManager");
    }


    public static <D extends AbstractEditorData, V> MultiReferenceEditorLogic<D, V> resolveEditorLogic(ActionContext<? extends AbstractEditorData> ctx)
    {
        return (MultiReferenceEditorLogic<D, V>)ctx.getParameter("editorLogic");
    }


    public static <T> T resolveCurrentObject(ActionContext<? extends AbstractEditorData> ctx, Class<T> type)
    {
        WidgetInstanceManager widgetInstanceManager = resolveWidgetInstanceManager(ctx);
        return (T)widgetInstanceManager.getModel().getValue("currentObject", type);
    }


    public static void updateCurrentObject(ActionContext<? extends AbstractEditorData> ctx, Object currentObject)
    {
        WidgetInstanceManager widgetInstanceManager = resolveWidgetInstanceManager(ctx);
        widgetInstanceManager.getModel().setValue("currentObject", currentObject);
        widgetInstanceManager.getModel().setValue("valueChanged", Boolean.TRUE);
    }


    public static void refreshCurrentObject(ActionContext<? extends AbstractEditorData> ctx)
    {
        WidgetInstanceManager widgetInstanceManager = resolveWidgetInstanceManager(ctx);
        widgetInstanceManager.sendOutput("searchRequest", new SearchRequestData());
    }
}
