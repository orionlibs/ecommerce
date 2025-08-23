package de.hybris.platform.adaptivesearchbackoffice.editors;

import com.hybris.cockpitng.editors.EditorContext;
import com.hybris.cockpitng.editors.EditorListener;
import com.hybris.cockpitng.engine.WidgetInstanceManager;

public interface EditorLogic<T>
{
    WidgetInstanceManager getWidgetInstanceManager();


    EditorContext<T> getEditorContext();


    EditorListener<T> getEditorListener();
}
