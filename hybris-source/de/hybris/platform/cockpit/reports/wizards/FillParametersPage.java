package de.hybris.platform.cockpit.reports.wizards;

import de.hybris.platform.cockpit.reports.model.JasperMediaModel;
import de.hybris.platform.cockpit.wizards.generic.AbstractGenericItemPage;
import org.zkoss.zhtml.Input;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class FillParametersPage extends AbstractGenericItemPage
{
    private final Textbox titleTextBox = new Textbox("");


    public Textbox getTitleTextBox()
    {
        return this.titleTextBox;
    }


    public FillParametersPage()
    {
        Label label = new Label("Title:");
        Hbox vbox = new Hbox(new Component[] {(Component)label, (Component)this.titleTextBox});
        this.pageContainer.getChildren().add(vbox);
    }


    public void addAttributes(JasperMediaModel media)
    {
        this.pageContainer.getChildren().clear();
        Label label = new Label("Title:");
        Input input = new Input();
        input.setValue(media.getCode());
        Hbox vbox = new Hbox(new Component[] {(Component)label, (Component)input});
        this.pageContainer.getChildren().add(vbox);
    }


    public Component createRepresentationItself()
    {
        return (Component)this.pageContainer;
    }
}
