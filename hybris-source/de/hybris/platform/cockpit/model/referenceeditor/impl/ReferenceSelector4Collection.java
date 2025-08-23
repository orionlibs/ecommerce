package de.hybris.platform.cockpit.model.referenceeditor.impl;

@Deprecated
public class ReferenceSelector4Collection extends ReferenceSelector
{
    private final ReferenceCollectionEditor parentEditor;


    public ReferenceSelector4Collection(ReferenceCollectionEditor parentEditor)
    {
        this.parentEditor = parentEditor;
    }


    protected void fireSaveActualItems()
    {
        if(this.parentEditor != null)
        {
            ((DefaultReferenceCollectionEditorModel)this.parentEditor.getModel()).addCollectionItems(getModel()
                            .getNotConfirmedItems());
            getModel().reset();
            fireAddTemporaryItems(this.parentEditor.getModel().getCollectionItems());
        }
        else
        {
            super.fireSaveActualItems();
        }
    }


    public void showComponentPopup()
    {
        fireAddTemporaryItems(this.parentEditor.getModel().getCollectionItems());
        super.showComponentPopup();
    }
}
