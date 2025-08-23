package de.hybris.platform.cockpit.model.referenceeditor.collection;

import de.hybris.platform.cockpit.model.general.UIViewComponent;
import de.hybris.platform.cockpit.model.referenceeditor.collection.model.CollectionEditorModel;
import java.util.List;
import org.zkoss.zul.Div;

public abstract class AbstractCollectionEditor extends Div implements UIViewComponent
{
    protected CollectionEditorController collectionControler;


    public abstract void updateCollectionItems();


    public abstract void updateRootTypeChanged();


    public abstract void updateRootSearchTypeChanged();


    public abstract CollectionEditorModel getModel();


    protected void fireAddCollectionItem(Object item)
    {
        this.collectionControler.addCollectionItem(item);
    }


    protected void fireAddCollectionItems(List<Object> items)
    {
        this.collectionControler.addCollectionItems(items);
    }


    protected void fireRemoveCollectionItem(int index)
    {
        this.collectionControler.removeCollectionItem(index);
    }


    protected void fireRemoveCollectionItem(Object item)
    {
        this.collectionControler.removeCollectionItem(item);
    }


    protected void fireMoveCollectionItem(int fromIndex, int toIndex)
    {
        this.collectionControler.moveCollectionItem(fromIndex, toIndex);
    }


    public CollectionEditorController getCollectionControler()
    {
        return this.collectionControler;
    }


    public void setCollectionControler(CollectionEditorController collectionControler)
    {
        this.collectionControler = collectionControler;
    }
}
