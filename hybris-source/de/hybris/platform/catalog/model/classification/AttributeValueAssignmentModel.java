package de.hybris.platform.catalog.model.classification;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class AttributeValueAssignmentModel extends ItemModel
{
    public static final String _TYPECODE = "AttributeValueAssignment";
    public static final String VALUE = "value";
    public static final String ATTRIBUTEASSIGNMENT = "attributeAssignment";
    public static final String ATTRIBUTE = "attribute";
    public static final String SYSTEMVERSION = "systemVersion";
    public static final String POSITION = "position";
    public static final String EXTERNALID = "externalID";


    public AttributeValueAssignmentModel()
    {
    }


    public AttributeValueAssignmentModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AttributeValueAssignmentModel(ClassificationAttributeValueModel _value)
    {
        setValue(_value);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AttributeValueAssignmentModel(ClassificationAttributeModel _attribute, ClassAttributeAssignmentModel _attributeAssignment, ItemModel _owner, ClassificationSystemVersionModel _systemVersion, ClassificationAttributeValueModel _value)
    {
        setAttribute(_attribute);
        setAttributeAssignment(_attributeAssignment);
        setOwner(_owner);
        setSystemVersion(_systemVersion);
        setValue(_value);
    }


    @Accessor(qualifier = "attribute", type = Accessor.Type.GETTER)
    public ClassificationAttributeModel getAttribute()
    {
        return (ClassificationAttributeModel)getPersistenceContext().getPropertyValue("attribute");
    }


    @Accessor(qualifier = "attributeAssignment", type = Accessor.Type.GETTER)
    public ClassAttributeAssignmentModel getAttributeAssignment()
    {
        return (ClassAttributeAssignmentModel)getPersistenceContext().getPropertyValue("attributeAssignment");
    }


    @Accessor(qualifier = "externalID", type = Accessor.Type.GETTER)
    public String getExternalID()
    {
        return (String)getPersistenceContext().getPropertyValue("externalID");
    }


    @Accessor(qualifier = "position", type = Accessor.Type.GETTER)
    public Integer getPosition()
    {
        return (Integer)getPersistenceContext().getPropertyValue("position");
    }


    @Accessor(qualifier = "systemVersion", type = Accessor.Type.GETTER)
    public ClassificationSystemVersionModel getSystemVersion()
    {
        return (ClassificationSystemVersionModel)getPersistenceContext().getPropertyValue("systemVersion");
    }


    @Accessor(qualifier = "value", type = Accessor.Type.GETTER)
    public ClassificationAttributeValueModel getValue()
    {
        return (ClassificationAttributeValueModel)getPersistenceContext().getPropertyValue("value");
    }


    @Accessor(qualifier = "attribute", type = Accessor.Type.SETTER)
    public void setAttribute(ClassificationAttributeModel value)
    {
        getPersistenceContext().setPropertyValue("attribute", value);
    }


    @Accessor(qualifier = "attributeAssignment", type = Accessor.Type.SETTER)
    public void setAttributeAssignment(ClassAttributeAssignmentModel value)
    {
        getPersistenceContext().setPropertyValue("attributeAssignment", value);
    }


    @Accessor(qualifier = "externalID", type = Accessor.Type.SETTER)
    public void setExternalID(String value)
    {
        getPersistenceContext().setPropertyValue("externalID", value);
    }


    @Accessor(qualifier = "position", type = Accessor.Type.SETTER)
    public void setPosition(Integer value)
    {
        getPersistenceContext().setPropertyValue("position", value);
    }


    @Accessor(qualifier = "systemVersion", type = Accessor.Type.SETTER)
    public void setSystemVersion(ClassificationSystemVersionModel value)
    {
        getPersistenceContext().setPropertyValue("systemVersion", value);
    }


    @Accessor(qualifier = "value", type = Accessor.Type.SETTER)
    public void setValue(ClassificationAttributeValueModel value)
    {
        getPersistenceContext().setPropertyValue("value", value);
    }
}
