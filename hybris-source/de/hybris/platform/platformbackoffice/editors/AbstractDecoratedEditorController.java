package de.hybris.platform.platformbackoffice.editors;

import com.hybris.cockpitng.components.Editor;
import de.hybris.platform.core.model.ItemModel;

@Deprecated(since = "6.6", forRemoval = true)
public abstract class AbstractDecoratedEditorController<M extends ItemModel, V extends AbstractDecoratedEditorView>
{
    protected V view;
    protected M wrappedModel;
    protected Editor ancestorEditor;


    public AbstractDecoratedEditorController(V view, M wrappedModel, Editor ancestorEditor)
    {
        this.view = view;
        this.wrappedModel = wrappedModel;
        this.ancestorEditor = ancestorEditor;
    }


    public V getView()
    {
        return this.view;
    }


    public M getWrappedModel()
    {
        return this.wrappedModel;
    }


    public Editor getAncestorEditor()
    {
        return this.ancestorEditor;
    }


    public abstract void setUIState();
}
