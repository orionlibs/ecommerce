package de.hybris.platform.adaptivesearchbackoffice.common;

import com.hybris.cockpitng.editors.EditorContext;

public interface ReplaceableDataHandler<T>
{
    boolean isReplaceable(EditorContext<T> paramEditorContext);


    T getValue(EditorContext<T> paramEditorContext);
}
