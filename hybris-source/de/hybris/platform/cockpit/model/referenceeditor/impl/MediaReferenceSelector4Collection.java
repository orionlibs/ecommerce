package de.hybris.platform.cockpit.model.referenceeditor.impl;

@Deprecated
public class MediaReferenceSelector4Collection extends MediaReferenceSelector
{
    private final ReferenceCollectionEditor parentEditor;


    public MediaReferenceSelector4Collection(ReferenceCollectionEditor parentEditor)
    {
        this.parentEditor = parentEditor;
    }


    protected void fireSaveActualItems()
    {
        if(this.parentEditor != null)
        {
            ((DefaultReferenceCollectionEditorModel)this.parentEditor.getModel())
                            .addCollectionItems(getModel().getNotConfirmedItems());
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
