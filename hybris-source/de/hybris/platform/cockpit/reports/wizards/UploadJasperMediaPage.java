package de.hybris.platform.cockpit.reports.wizards;

import de.hybris.platform.cockpit.model.editor.EditorHelper;
import de.hybris.platform.cockpit.model.editor.EditorListener;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.generic.UploadMediaPage;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;

public class UploadJasperMediaPage extends UploadMediaPage
{
    private static final Logger LOG = LoggerFactory.getLogger(UploadJasperMediaPage.class);
    private ObjectValueContainer valueContainer;
    private String mediaCode = null;
    private String mediaTitle = null;
    private String mediaDescription = null;


    public Component createRepresentationItself()
    {
        Div content = new Div();
        content.setSclass("jasperWizardUploadPage");
        content.setStyle("padding: 10px;");
        for(PropertyDescriptor propertyDescriptor : getProperties())
        {
            Component rowCmp = createRowComponent();
            String name = propertyDescriptor.getName();
            rowCmp.appendChild((Component)new Label((name == null) ? ("[" + propertyDescriptor.getQualifier() + "]") : name));
            rowCmp.appendChild(createEditorRow(propertyDescriptor));
            Div div = new Div();
            div.setStyle("margin: 5px; margin-top: 15px;");
            div.setSclass("rowCnt");
            div.appendChild(rowCmp);
            content.appendChild((Component)div);
        }
        Component rowComp = createRowComponent();
        rowComp.appendChild((Component)new Label(Labels.getLabel("cockpit.wizard.createwidget.uploadpage.attribute.file")));
        Hbox uploadEditorHbox = new Hbox();
        uploadEditorHbox.setWidths("none,30px");
        uploadEditorHbox.setWidth("100%");
        uploadEditorHbox.setStyle("table-layout:fixed;");
        rowComp.appendChild((Component)uploadEditorHbox);
        Textbox uploadTextbox = new Textbox();
        uploadTextbox.setDisabled(true);
        uploadTextbox.setText((getMedia() == null) ? null : getMedia().getName());
        uploadEditorHbox.appendChild((Component)uploadTextbox);
        Button uploadButton = new Button("+");
        uploadButton.setSclass("btnblue");
        uploadButton.addEventListener("onClick", (EventListener)new Object(this, uploadTextbox));
        uploadEditorHbox.appendChild((Component)uploadButton);
        Div rowCmpCnt = new Div();
        rowCmpCnt.setStyle("margin: 5px; margin-top: 15px;");
        rowCmpCnt.setSclass("rowCnt");
        rowCmpCnt.appendChild(rowComp);
        content.appendChild((Component)rowCmpCnt);
        return (Component)content;
    }


    protected Component createEditorRow(PropertyDescriptor propertyDescriptor)
    {
        Div editorContainer = new Div();
        EditorHelper.createEditor(null, propertyDescriptor, (HtmlBasedComponent)editorContainer, getValueContainer(), (EditorListener)new Object(this, propertyDescriptor), true);
        return (Component)editorContainer;
    }


    protected List<PropertyDescriptor> getProperties()
    {
        List<PropertyDescriptor> ret = new ArrayList<>();
        for(String attrCode : getDisplayedAttributes())
        {
            try
            {
                ret.add(UISessionUtils.getCurrentSession().getTypeService().getPropertyDescriptor(attrCode));
            }
            catch(Exception e)
            {
                LOG.error("Could not load property descriptor, reason: ", e);
            }
        }
        return ret;
    }


    public void setDisplayedAttributes(List<String> displayedAttributes)
    {
        super.setDisplayedAttributes(displayedAttributes);
        this.valueContainer = new ObjectValueContainer(UISessionUtils.getCurrentSession().getTypeService().getObjectType("JasperMedia"), null);
        Set<String> availableLanguageIsos = UISessionUtils.getCurrentSession().getSystemService().getAvailableLanguageIsos();
        for(PropertyDescriptor pd : getProperties())
        {
            if(pd.isLocalized())
            {
                for(String langIso : availableLanguageIsos)
                {
                    getValueContainer().addValue(pd, langIso, null);
                }
                continue;
            }
            getValueContainer().addValue(pd, null, null);
        }
    }


    private void doValidate()
    {
        getCurrentController().validate((Wizard)getWizard(), (WizardPage)this);
    }


    public String getMediaCode()
    {
        return this.mediaCode;
    }


    public void setMediaCode(String mediaCode)
    {
        this.mediaCode = mediaCode;
    }


    public String getMediaTitle()
    {
        return this.mediaTitle;
    }


    public void setMediaTitle(String mediaTitle)
    {
        this.mediaTitle = mediaTitle;
    }


    public String getMediaDescription()
    {
        return this.mediaDescription;
    }


    public void setMediaDescription(String mediaDescription)
    {
        this.mediaDescription = mediaDescription;
    }


    public ObjectValueContainer getValueContainer()
    {
        return this.valueContainer;
    }
}
