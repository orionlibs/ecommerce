package de.hybris.platform.cockpit.model.meta;

public interface PropertyEditorBean
{
    PropertyEditorDescriptor getDescriptor();


    void initialize(Object paramObject);


    void reset();


    void setValue(Object paramObject);


    Object getValue();


    boolean isModified();


    void setEditable(boolean paramBoolean);


    boolean isEditable();


    void setInvalid(boolean paramBoolean);


    boolean isInvalid();
}
