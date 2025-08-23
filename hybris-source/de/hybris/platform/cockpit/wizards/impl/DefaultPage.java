package de.hybris.platform.cockpit.wizards.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.cockpit.wizards.Message;
import de.hybris.platform.cockpit.wizards.Wizard;
import de.hybris.platform.cockpit.wizards.WizardPage;
import de.hybris.platform.cockpit.wizards.WizardPageController;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Messagebox;

public class DefaultPage implements WizardPage
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultPage.class);
    private String componentURI;
    private WizardPageController controller;
    private String id;
    protected String title;
    protected Wizard wizard;
    protected String width;
    protected String height;
    private Map<String, Object> attributes;
    private Component pageComponent;


    public String getWidth()
    {
        return this.width;
    }


    public String getHeight()
    {
        return this.height;
    }


    public String getComponentURI()
    {
        return (this.componentURI == null) ? "/cockpit/wizards/defaultPage.zul" : this.componentURI;
    }


    public void setComponentURI(String componentURI)
    {
        this.componentURI = componentURI;
    }


    public void setTitle(String i3key)
    {
        this.title = i3key;
    }


    public String getTitle()
    {
        String ret = this.title;
        if(this.title != null)
        {
            ret = Labels.getLabel(this.title);
        }
        if(ret == null && this.title != null)
        {
            ret = "[" + this.title + "]";
        }
        return ret;
    }


    public WizardPageController getController()
    {
        return this.controller;
    }


    public void setController(WizardPageController controller)
    {
        this.controller = controller;
    }


    public String getId()
    {
        return this.id;
    }


    @Required
    public void setId(String id)
    {
        this.id = id;
    }


    public Wizard getWizard()
    {
        Preconditions.checkArgument((this.wizard != null));
        return this.wizard;
    }


    public Component getPageComponent()
    {
        Preconditions.checkArgument((this.pageComponent != null));
        return this.pageComponent;
    }


    public WizardPageController getCurrentController()
    {
        return getWizard().getCurrentController();
    }


    public void initView(Wizard wizard, Component comp)
    {
        this.wizard = wizard;
        this.pageComponent = comp;
        handleMessages(wizard.getMessages());
    }


    public void setWizard(Wizard wizard)
    {
        this.wizard = wizard;
    }


    protected void setPageComponent(Component pageComponent)
    {
        this.pageComponent = pageComponent;
    }


    public Map<String, Object> getAttributes()
    {
        if(this.attributes == null)
        {
            this.attributes = new ConcurrentHashMap<>();
        }
        return this.attributes;
    }


    private String getMessageboxIcon(Message message)
    {
        String ret = "z-msgbox z-msgbox-information";
        if(message.getLevel() == 2)
        {
            ret = "z-msgbox z-msgbox-exclamation";
        }
        else if(message.getLevel() == 3)
        {
            ret = "z-msgbox z-msgbox-error";
        }
        return ret;
    }


    protected void handleMessages(List<Message> messages)
    {
        if(messages != null)
        {
            for(Message m : messages)
            {
                try
                {
                    Messagebox.show(m.getMessageText(), Labels.getLabel("wizard.defaultMessageTitle_level" + m.getLevel()), 1,
                                    getMessageboxIcon(m));
                }
                catch(InterruptedException e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.warn("Caught exception: ", e);
                    }
                }
            }
        }
    }


    public void renderView(Component parent)
    {
        parent.appendChild((Component)new Label(Labels.getLabel("general.nothingtodisplay")));
    }
}
