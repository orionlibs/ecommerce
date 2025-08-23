package de.hybris.platform.mediaconversionbackoffice.editors.extractdeletemetadata.media;

import de.hybris.platform.platformbackoffice.editors.AbstractDecoratedEditorView;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

public class ExtractDeleteMetadataView extends AbstractDecoratedEditorView
{
    private final Button extractButton;
    private final Button deleteButton;


    public ExtractDeleteMetadataView(Component component)
    {
        Div container = new Div();
        container.setParent(component);
        this.extractButton = new Button();
        this.extractButton.setLabel(Labels.getLabel("button.extract.metadata"));
        this.extractButton.setParent((Component)container);
        this.deleteButton = new Button();
        this.deleteButton.setLabel(Labels.getLabel("button.delete.metadata"));
        this.deleteButton.setParent((Component)container);
    }


    public Button getExtractButton()
    {
        return this.extractButton;
    }


    public Button getDeleteButton()
    {
        return this.deleteButton;
    }


    public void setUIState(boolean... flags)
    {
        getDeleteButton().setDisabled(flags[0]);
        getExtractButton().setDisabled(flags[1]);
    }
}
