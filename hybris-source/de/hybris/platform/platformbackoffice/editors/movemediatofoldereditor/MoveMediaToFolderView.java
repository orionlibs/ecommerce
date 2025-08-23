package de.hybris.platform.platformbackoffice.editors.movemediatofoldereditor;

import de.hybris.platform.platformbackoffice.editors.AbstractDecoratedEditorView;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;

public class MoveMediaToFolderView extends AbstractDecoratedEditorView
{
    final Button moveMediaToFolderButton;


    public MoveMediaToFolderView(Component component)
    {
        this.moveMediaToFolderButton = new Button();
        this.moveMediaToFolderButton.setDisabled(true);
        this.moveMediaToFolderButton.setLabel(Labels.getLabel("buttons.move.media.to.folder"));
        this.moveMediaToFolderButton.setParent(component);
    }


    public Button getMoveMediaToFolderButton()
    {
        return this.moveMediaToFolderButton;
    }


    public void setUIState(boolean... flags)
    {
        getMoveMediaToFolderButton().setDisabled(flags[0]);
    }
}
