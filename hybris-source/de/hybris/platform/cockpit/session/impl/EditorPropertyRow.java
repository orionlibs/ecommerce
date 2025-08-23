package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.sectionpanel.PropertyValueHolder;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.impl.ClassAttributePropertyDescriptor;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.session.UISessionUtils;

public class EditorPropertyRow extends PropertyValueHolder
{
    private final EditorRowConfiguration rowConf;
    private boolean valid;


    public EditorPropertyRow(EditorRowConfiguration rowConf)
    {
        super(rowConf.getPropertyDescriptor().getQualifier());
        PropertyDescriptor desc = rowConf.getPropertyDescriptor();
        this.rowConf = rowConf;
        setVisible(rowConf.isVisible());
        setEditable(rowConf.isEditable());
        setLocalized(desc.isLocalized());
        String label = null;
        try
        {
            label = desc.getName();
            this.valid = true;
        }
        catch(IllegalStateException e)
        {
            this.valid = false;
        }
        if(this.valid)
        {
            setLabel((label == null || label.isEmpty()) ? ("[" + desc.getQualifier() + "]") : label);
            if(desc instanceof ClassAttributePropertyDescriptor)
            {
                ClassAttributePropertyDescriptor classAttributePropertyDescriptor = (ClassAttributePropertyDescriptor)desc;
                setValueInfo(classAttributePropertyDescriptor.getClassificationAttributeValueInfo());
            }
            else
            {
                setValueInfo(
                                UISessionUtils.getCurrentSession().getTypeService().getAttributeCodeFromPropertyQualifier(desc.getQualifier()));
            }
        }
    }


    public EditorRowConfiguration getRowConfiguration()
    {
        return this.rowConf;
    }


    public boolean isValid()
    {
        return this.valid;
    }
}
