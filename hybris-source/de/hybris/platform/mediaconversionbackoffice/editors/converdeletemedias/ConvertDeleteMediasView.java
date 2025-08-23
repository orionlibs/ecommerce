package de.hybris.platform.mediaconversionbackoffice.editors.converdeletemedias;

import de.hybris.platform.platformbackoffice.editors.AbstractDecoratedEditorView;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;

public class ConvertDeleteMediasView extends AbstractDecoratedEditorView
{
    private final Button convertButton;
    private final Button deleteButton;


    public ConvertDeleteMediasView(Component component)
    {
        Div container = new Div();
        container.setParent(component);
        this.convertButton = new Button();
        this.convertButton.setLabel(Labels.getLabel("button.convert.missing.medias"));
        this.convertButton.setParent((Component)container);
        this.convertButton.setWidth("200px");
        this.deleteButton = new Button();
        this.deleteButton.setLabel(Labels.getLabel("button.delete.converted.medias"));
        this.deleteButton.setParent((Component)container);
        this.deleteButton.setWidth("200px");
    }


    public Button getConvertButton()
    {
        return this.convertButton;
    }


    public Button getDeleteButton()
    {
        return this.deleteButton;
    }


    public void setUIState(boolean... flags)
    {
        getDeleteButton().setDisabled(flags[0]);
        getConvertButton().setDisabled(flags[1]);
    }
}
