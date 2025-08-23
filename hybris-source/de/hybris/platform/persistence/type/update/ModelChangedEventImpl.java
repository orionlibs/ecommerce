package de.hybris.platform.persistence.type.update;

import de.hybris.platform.persistence.type.AttributeDescriptorRemote;
import de.hybris.platform.util.jdbc.DBColumn;

public class ModelChangedEventImpl implements ModelChangedEvent
{
    private final String sourceType;
    private final String targetType;
    private final DBColumn currentColumnModel;
    private final AttributeDescriptorRemote descriptor;


    public ModelChangedEventImpl(String sourceType, String targetType, DBColumn currentColumnModel, AttributeDescriptorRemote descr)
    {
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.currentColumnModel = currentColumnModel;
        this.descriptor = descr;
    }


    public DBColumn getCurrentColumnModel()
    {
        return this.currentColumnModel;
    }


    public String getSourceType()
    {
        return this.sourceType;
    }


    public String getTargetType()
    {
        return this.targetType;
    }


    public AttributeDescriptorRemote getAttributeDescriptor()
    {
        return this.descriptor;
    }


    public String toString()
    {
        return " attribute " + ((this.descriptor != null && this.descriptor.getComposedType() != null) ? this.descriptor.getComposedType().getCode() : "???") + " definition changed from " + this.sourceType + " to " + this.targetType + " stored in DB table " + this.currentColumnModel
                        .getTable().getName() + " ,column " + this.currentColumnModel.getColumnName();
    }
}
