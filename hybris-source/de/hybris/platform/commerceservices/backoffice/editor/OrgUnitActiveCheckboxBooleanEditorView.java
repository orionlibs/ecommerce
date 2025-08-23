package de.hybris.platform.commerceservices.backoffice.editor;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.CheckEvent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;

public class OrgUnitActiveCheckboxBooleanEditorView
{
    private static final String ACTIVATING_NOTICE = "organization.unit.note.enable";
    private static final String DEACTIVATING_NOTICE = "organization.unit.note.disable";
    private static final String BLOCK_BY_PARENT_NOTICE = "organization.unit.enable.cannotactivate";
    private final Div topDiv;
    private final Checkbox checkbox;
    private final Div noteContainer;
    private final Label noteLabel;


    public OrgUnitActiveCheckboxBooleanEditorView()
    {
        this.topDiv = new Div();
        this.checkbox = createCheckboxView();
        this.noteContainer = new Div();
        this.noteLabel = new Label();
        this.noteContainer.appendChild((Component)this.noteLabel);
        this.topDiv.appendChild((Component)this.checkbox);
        this.topDiv.appendChild((Component)this.noteContainer);
    }


    public void update(ViewMode viewMode)
    {
        switch(null.$SwitchMap$de$hybris$platform$commerceservices$backoffice$editor$OrgUnitActiveCheckboxBooleanEditorView$ViewMode[viewMode.ordinal()])
        {
            case 1:
                this.checkbox.setDisabled(false);
                this.noteContainer.setVisible(false);
                break;
            case 2:
                this.checkbox.setDisabled(false);
                this.noteContainer.setVisible(true);
                this.noteLabel.setValue(Labels.getLabel("organization.unit.note.enable"));
                break;
            case 3:
                this.checkbox.setDisabled(false);
                this.noteContainer.setVisible(true);
                this.noteLabel.setValue(Labels.getLabel("organization.unit.note.disable"));
                break;
            case 4:
                this.checkbox.setDisabled(true);
                this.noteContainer.setVisible(false);
                break;
            case 5:
                this.checkbox.setDisabled(true);
                this.noteContainer.setVisible(true);
                this.noteLabel.setValue(Labels.getLabel("organization.unit.enable.cannotactivate"));
                break;
        }
    }


    public void setChecked(boolean checked)
    {
        this.checkbox.setChecked(checked);
    }


    public Component getComponent()
    {
        return (Component)this.topDiv;
    }


    public void addCheckEventListener(EventListener<CheckEvent> wrapperListener)
    {
        this.checkbox.addEventListener("onCheck", wrapperListener);
    }


    public boolean isChecked()
    {
        return this.checkbox.isChecked();
    }


    public void setEditorLabel(String label)
    {
        this.checkbox.setLabel(label);
    }


    private Checkbox createCheckboxView()
    {
        Checkbox box = new Checkbox();
        String viewClass = "ye-switch-checkbox";
        box.addSclass("ye-switch-checkbox");
        return box;
    }
}
