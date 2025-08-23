package de.hybris.platform.cockpit.wizards.generic;

import de.hybris.platform.cockpit.helpers.validation.ValidationUIHelper;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.impl.DefaultPage;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Vbox;

public abstract class AbstractGenericItemPage extends DefaultPage implements GenericItemWizardPage
{
    protected static final String ERROR_SCLASS_WIZARD_CNT = "wizardErrorMessage";
    protected static final String WARNING_SCLASS_WIZARD_CNT = "wizardWarningMessage";
    protected static final String INFO_SCLASS_WIZARD_CNT = "wizardInfoMessage";
    protected static final String SCLASS_ELEMENT_BOX_EDITOR = "contentElementBoxEditor";
    protected static final String ERROR_SCLASS_ELEMENT_BOX_EDITOR = "contentElementErrorBoxEditor";
    protected static final String WARNING_SCLASS_ELEMENT_BOX_EDITOR = "contentElementWarningBoxEditor";
    protected static final String INFO_SCLASS_ELEMENT_BOX_EDITOR = "contentElementInfoBoxEditor";
    protected static final String WIZARD_PAGE_CNT_SCLASS = "wizardPageContainer";
    protected static final String WIZARD_ERROR_CNT_SCLASS = "wizardErrorContainer";
    protected static final String WIZARD_PAGE_CONTENT_CNT_SCLASS = "wizardPageContentContainer";
    public static final String CONTENT_ELEMENT_CONFIG = "contentElement";
    protected Div pageContainer = null;
    private List<String> displayedAttributes = new ArrayList<>();
    protected Div pageContent = null;
    protected Div errorContainer = null;
    private String nextPageWizardId;


    public AbstractGenericItemPage()
    {
        this(null, null);
    }


    public AbstractGenericItemPage(String pageTitle)
    {
        this(pageTitle, null);
    }


    public AbstractGenericItemPage(String pageTitle, Wizard wizard)
    {
        this.title = pageTitle;
        this.wizard = wizard;
        this.pageContainer = new Div();
        this.pageContainer.setSclass("wizardPageContainer");
        this.errorContainer = new Div();
        this.errorContainer.setParent((Component)this.pageContainer);
        this.errorContainer.setSclass("wizardErrorContainer");
        this.pageContent = new Div();
        this.pageContent.setParent((Component)this.pageContainer);
        this.pageContent.setSclass("wizardPageContentContainer");
    }


    public String getNextPageWizardId()
    {
        return this.nextPageWizardId;
    }


    public void setNextPageWizardId(String nextPageWizardId)
    {
        this.nextPageWizardId = nextPageWizardId;
    }


    public Div getPageContainer()
    {
        return this.pageContainer;
    }


    public Div getErrorContainer()
    {
        return this.errorContainer;
    }


    public List<String> getDisplayedAttributes()
    {
        return this.displayedAttributes;
    }


    public void setDisplayedAttributes(List<String> displayedAttributes)
    {
        this.displayedAttributes = displayedAttributes;
    }


    public Div getPageContent()
    {
        return this.pageContent;
    }


    protected void clearPageComponents()
    {
        this.pageContent.getChildren().clear();
    }


    public int isCauseError(PropertyDescriptor descriptor)
    {
        for(Message message : getWizard().getMessages())
        {
            if(descriptor.getQualifier().equals(message.getComponentId()))
            {
                return message.getLevel();
            }
        }
        return -1;
    }


    protected void handleMessages(List<Message> messages)
    {
        UITools.detachChildren((Component)this.errorContainer);
        Vbox errorMessages = new Vbox();
        errorMessages.setWidth("100%");
        for(Message message : messages)
        {
            Vbox messageBox = new Vbox();
            messageBox.setWidth("100%");
            ValidationUIHelper validationUIHelper = (ValidationUIHelper)SpringUtil.getBean("validationUIHelper");
            Popup tooltip = validationUIHelper.buildConstraintValidationTooltip(message);
            Label label = new Label(message.getMessageText());
            switch(message.getLevel())
            {
                case 0:
                    messageBox.setSclass("wizardInfoMessage");
                    break;
                case 2:
                    messageBox.setSclass("wizardWarningMessage");
                    break;
                case 3:
                    messageBox.setSclass("wizardErrorMessage");
                    break;
            }
            Menupopup menuPopup = new Menupopup();
            Menuitem viewConstraintMenuItem = validationUIHelper.createViewConstraintMenuItem(message.getConstraintPK());
            menuPopup.appendChild((Component)viewConstraintMenuItem);
            if(message.getLevel() == 2)
            {
                Menuitem menuItem = new Menuitem(Labels.getLabel("validation.menupopup.ignore.warnings"));
                menuItem.setCheckmark(true);
                menuItem.addEventListener("onClick", (EventListener)new Object(this, message, menuItem, validationUIHelper, messageBox));
                menuPopup.appendChild((Component)menuItem);
            }
            messageBox.appendChild((Component)label);
            messageBox.setTooltip(tooltip);
            messageBox.setPopup((Popup)menuPopup);
            errorMessages.appendChild((Component)tooltip);
            errorMessages.appendChild((Component)menuPopup);
            errorMessages.appendChild((Component)messageBox);
        }
        this.errorContainer.appendChild((Component)errorMessages);
    }


    public GenericItemWizard getWizard()
    {
        GenericItemWizard ret = null;
        if(this.wizard instanceof GenericItemWizard)
        {
            ret = (GenericItemWizard)this.wizard;
        }
        return ret;
    }


    public abstract Component createRepresentationItself();
}
