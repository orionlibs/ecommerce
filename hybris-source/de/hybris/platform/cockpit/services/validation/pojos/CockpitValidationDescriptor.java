package de.hybris.platform.cockpit.services.validation.pojos;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.validation.model.constraints.AbstractConstraintModel;

public class CockpitValidationDescriptor
{
    private PropertyDescriptor propertyDescriptor;
    private int cockpitMessageLevel;
    private String validationMessage;
    private AbstractConstraintModel constraint;


    public CockpitValidationDescriptor(PropertyDescriptor propertyDescriptor, int cockpitMessageLevel, String validationMessage, AbstractConstraintModel constraint)
    {
        this.propertyDescriptor = propertyDescriptor;
        this.cockpitMessageLevel = cockpitMessageLevel;
        this.validationMessage = validationMessage;
        this.constraint = constraint;
    }


    public int getCockpitMessageLevel()
    {
        return this.cockpitMessageLevel;
    }


    public AbstractConstraintModel getConstraint()
    {
        return this.constraint;
    }


    public String getConstraintPk()
    {
        String pk = null;
        if(getConstraint() != null)
        {
            pk = getConstraint().getPk().toString();
        }
        return pk;
    }


    public PropertyDescriptor getPropertyDescriptor()
    {
        return this.propertyDescriptor;
    }


    public String getValidationMessage()
    {
        return this.validationMessage;
    }


    public boolean isAttributeConstraint()
    {
        return (this.constraint instanceof de.hybris.platform.validation.model.constraints.AttributeConstraintModel);
    }


    public boolean isFramevorkValidation()
    {
        return (isTypeConstraint() || isAttributeConstraint());
    }


    public boolean isTypeConstraint()
    {
        return (this.constraint instanceof de.hybris.platform.validation.model.constraints.TypeConstraintModel);
    }


    public void setCockpitMessageLevel(int cockpitMessageLevel)
    {
        this.cockpitMessageLevel = cockpitMessageLevel;
    }


    public void setConstraint(AbstractConstraintModel constraint)
    {
        this.constraint = constraint;
    }


    public void setPropertyDescriptor(PropertyDescriptor propertyDescriptor)
    {
        this.propertyDescriptor = propertyDescriptor;
    }


    public void setValidationMessage(String validationMessage)
    {
        this.validationMessage = validationMessage;
    }


    public String toString()
    {
        String type = "";
        if(this.propertyDescriptor != null)
        {
            type = this.propertyDescriptor.getQualifier();
        }
        return type + "[" + type + "] " + this.cockpitMessageLevel;
    }
}
