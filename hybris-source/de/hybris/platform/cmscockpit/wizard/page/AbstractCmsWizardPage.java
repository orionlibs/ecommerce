package de.hybris.platform.cmscockpit.wizard.page;

import de.hybris.platform.cmscockpit.wizard.CmsWizard;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.util.UITools;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.impl.DefaultPage;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Label;
import org.zkoss.zul.Vbox;

public abstract class AbstractCmsWizardPage extends DefaultPage implements CmsWizardPage
{
    protected static final String ERROR_SCLASS_CMSWIZARD_CNT = "cmsWizardErrorMessage";
    protected static final String SCLASS_ELEMENT_BOX_EDITOR = "contentElementBoxEditor";
    protected static final String ERROR_SCLASS_ELEMENT_BOX_EDITOR = "contentElementErrorBoxEditor";
    protected static final String CMSWIZARD_PAGE_CNT_SCLASS = "cmsWizardPageContainer";
    protected static final String CMSWIZARD_ERROR_CNT_SCLASS = "cmsWizardErrorContainer";
    protected static final String CMSWIZARD_PAGE_CONTENT_CNT_SCLASS = "cmsWizardPageContentContainer";
    public static final String CONTENT_ELEMENT_CONFIG = "contentElement";
    protected Div pageContainer = null;
    private List<String> displayedAttributes = new ArrayList<>();
    protected Div pageContent = null;
    protected Div errorContainer = null;


    public AbstractCmsWizardPage(String pageTitle, Wizard wizard)
    {
        this.title = pageTitle;
        this.wizard = wizard;
        this.pageContainer = new Div();
        this.pageContainer.setSclass("cmsWizardPageContainer");
        this.errorContainer = new Div();
        this.errorContainer.setParent((Component)this.pageContainer);
        this.errorContainer.setSclass("cmsWizardErrorContainer");
        this.pageContent = new Div();
        this.pageContent.setParent((Component)this.pageContainer);
        this.pageContent.setSclass("cmsWizardPageContentContainer");
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


    public boolean isCauseError(PropertyDescriptor descriptor)
    {
        for(Message message : getWizard().getMessages())
        {
            if(3 != message.getLevel())
            {
                continue;
            }
            if(descriptor.getQualifier().equals(message.getComponentId()))
            {
                return true;
            }
        }
        return false;
    }


    protected void handleMessages(List<Message> messages)
    {
        UITools.detachChildren((Component)this.errorContainer);
        Vbox errorMessages = new Vbox();
        errorMessages.setWidth("100%");
        errorMessages.setSclass("cmsWizardErrorMessage");
        for(Message message : messages)
        {
            errorMessages.appendChild((Component)new Label(message.getMessageText() + " " + message.getMessageText()));
        }
        this.errorContainer.appendChild((Component)errorMessages);
    }


    public CmsWizard getWizard()
    {
        CmsWizard ret = null;
        if(this.wizard instanceof CmsWizard)
        {
            ret = (CmsWizard)this.wizard;
        }
        return ret;
    }


    public abstract Component createRepresentationItself();
}
