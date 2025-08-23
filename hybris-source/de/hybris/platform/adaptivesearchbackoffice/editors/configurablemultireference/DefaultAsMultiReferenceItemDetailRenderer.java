package de.hybris.platform.adaptivesearchbackoffice.editors.configurablemultireference;

import de.hybris.platform.adaptivesearchbackoffice.data.AbstractEditorData;
import de.hybris.platform.adaptivesearchbackoffice.editors.EditorRenderer;
import org.zkoss.zk.ui.Component;

public class DefaultAsMultiReferenceItemDetailRenderer<D extends AbstractEditorData, V> implements EditorRenderer<MultiReferenceEditorLogic<D, V>, D>
{
    public boolean isEnabled(MultiReferenceEditorLogic<D, V> logic)
    {
        return false;
    }


    public boolean canRender(MultiReferenceEditorLogic<D, V> logic, Component parent, D data)
    {
        return false;
    }


    public void render(MultiReferenceEditorLogic<D, V> logic, Component parent, D data)
    {
        throw new UnsupportedOperationException("This renderer does not support rendering of items");
    }
}
