package de.hybris.platform.cockpit.services.config.jaxb.wizard;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "wizard-config")
public class WizardConfig
{
    @XmlElement(name = "displayed-properties")
    protected WizardPropertyList wizardPropertyList;
    @XmlElement(name = "after-done-wizard-script")
    protected AfterDoneWizardScript wizardScript;
    @XmlAttribute
    protected Boolean showPrefilledValues;
    @XmlAttribute
    protected Boolean createMode;
    @XmlAttribute
    protected Boolean selectMode;
    @XmlAttribute
    protected Boolean displaySubtypes;
    @XmlAttribute
    protected Boolean activateAfterCreate;
    @XmlAttribute
    protected Boolean createWithinPopup;
    @XmlAttribute
    protected Boolean createWithinEditor;
    @XmlAttribute
    protected Boolean validationInfoIgnored;


    public boolean getCreateWithinPopup()
    {
        boolean ret = false;
        if(this.createWithinPopup == null)
        {
            ret = true;
        }
        else
        {
            ret = this.createWithinPopup.booleanValue();
        }
        return ret;
    }


    public boolean getCreateWithinEditor()
    {
        boolean ret = false;
        if(this.createWithinEditor == null)
        {
            ret = false;
        }
        else
        {
            ret = this.createWithinEditor.booleanValue();
        }
        return ret;
    }


    public boolean getActivateAfterCreate()
    {
        boolean ret = false;
        if(this.activateAfterCreate == null)
        {
            ret = false;
        }
        else
        {
            ret = this.activateAfterCreate.booleanValue();
        }
        return ret;
    }


    public WizardPropertyList getWizardPropertyList()
    {
        return this.wizardPropertyList;
    }


    public boolean isCreateMode()
    {
        boolean ret = false;
        if(this.createMode == null)
        {
            ret = false;
        }
        else
        {
            ret = this.createMode.booleanValue();
        }
        return ret;
    }


    public boolean isDisplaySubtypes()
    {
        boolean ret = true;
        if(this.displaySubtypes == null)
        {
            ret = true;
        }
        else
        {
            ret = this.displaySubtypes.booleanValue();
        }
        return ret;
    }


    public boolean isSelectMode()
    {
        boolean ret = false;
        if(this.selectMode == null)
        {
            ret = false;
        }
        else
        {
            ret = this.selectMode.booleanValue();
        }
        return ret;
    }


    public boolean isShowPrefilledValues()
    {
        boolean ret = false;
        if(this.showPrefilledValues == null)
        {
            ret = false;
        }
        else
        {
            ret = this.showPrefilledValues.booleanValue();
        }
        return ret;
    }


    public void setActivateAfterCreate(Boolean activateAfterCreate)
    {
        this.activateAfterCreate = activateAfterCreate;
    }


    public void setCreateMode(Boolean createMode)
    {
        this.createMode = createMode;
    }


    public void setSelectMode(Boolean selectMode)
    {
        this.selectMode = selectMode;
    }


    public void setShowPrefilledValues(Boolean showPrefilledValues)
    {
        this.showPrefilledValues = showPrefilledValues;
    }


    public void setWizardPropertyList(WizardPropertyList wizardPropertyList)
    {
        this.wizardPropertyList = wizardPropertyList;
    }


    public AfterDoneWizardScript getWizardScript()
    {
        return this.wizardScript;
    }


    public void setWizardScript(AfterDoneWizardScript wizardScript)
    {
        this.wizardScript = wizardScript;
    }


    public void setValidationInfoIgnored(Boolean validationInfoIgnored)
    {
        this.validationInfoIgnored = validationInfoIgnored;
    }


    public boolean isValidationInfoIgnored()
    {
        boolean ret = false;
        if(this.validationInfoIgnored == null)
        {
            ret = false;
        }
        else
        {
            ret = this.validationInfoIgnored.booleanValue();
        }
        return ret;
    }
}
