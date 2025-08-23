package de.hybris.platform.hmc.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SavedValueEntryModel extends ItemModel
{
    public static final String _TYPECODE = "SavedValueEntry";
    public static final String _SAVEDVALUEENTRIESRELATION = "SavedValueEntriesRelation";
    public static final String MODIFIEDATTRIBUTE = "modifiedAttribute";
    public static final String OLDVALUEATTRIBUTEDESCRIPTOR = "OldValueAttributeDescriptor";
    public static final String OLDVALUE = "oldValue";
    public static final String NEWVALUE = "newValue";
    public static final String PARENT = "parent";


    public SavedValueEntryModel()
    {
    }


    public SavedValueEntryModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SavedValueEntryModel(AttributeDescriptorModel _OldValueAttributeDescriptor, String _modifiedAttribute, SavedValuesModel _parent)
    {
        setOldValueAttributeDescriptor(_OldValueAttributeDescriptor);
        setModifiedAttribute(_modifiedAttribute);
        setParent(_parent);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SavedValueEntryModel(AttributeDescriptorModel _OldValueAttributeDescriptor, String _modifiedAttribute, ItemModel _owner, SavedValuesModel _parent)
    {
        setOldValueAttributeDescriptor(_OldValueAttributeDescriptor);
        setModifiedAttribute(_modifiedAttribute);
        setOwner(_owner);
        setParent(_parent);
    }


    @Accessor(qualifier = "modifiedAttribute", type = Accessor.Type.GETTER)
    public String getModifiedAttribute()
    {
        return (String)getPersistenceContext().getPropertyValue("modifiedAttribute");
    }


    @Accessor(qualifier = "newValue", type = Accessor.Type.GETTER)
    public Object getNewValue()
    {
        return getPersistenceContext().getPropertyValue("newValue");
    }


    @Accessor(qualifier = "oldValue", type = Accessor.Type.GETTER)
    public Object getOldValue()
    {
        return getPersistenceContext().getPropertyValue("oldValue");
    }


    @Accessor(qualifier = "OldValueAttributeDescriptor", type = Accessor.Type.GETTER)
    public AttributeDescriptorModel getOldValueAttributeDescriptor()
    {
        return (AttributeDescriptorModel)getPersistenceContext().getPropertyValue("OldValueAttributeDescriptor");
    }


    @Accessor(qualifier = "parent", type = Accessor.Type.GETTER)
    public SavedValuesModel getParent()
    {
        return (SavedValuesModel)getPersistenceContext().getPropertyValue("parent");
    }


    @Accessor(qualifier = "modifiedAttribute", type = Accessor.Type.SETTER)
    public void setModifiedAttribute(String value)
    {
        getPersistenceContext().setPropertyValue("modifiedAttribute", value);
    }


    @Accessor(qualifier = "newValue", type = Accessor.Type.SETTER)
    public void setNewValue(Object value)
    {
        getPersistenceContext().setPropertyValue("newValue", value);
    }


    @Accessor(qualifier = "oldValue", type = Accessor.Type.SETTER)
    public void setOldValue(Object value)
    {
        getPersistenceContext().setPropertyValue("oldValue", value);
    }


    @Accessor(qualifier = "OldValueAttributeDescriptor", type = Accessor.Type.SETTER)
    public void setOldValueAttributeDescriptor(AttributeDescriptorModel value)
    {
        getPersistenceContext().setPropertyValue("OldValueAttributeDescriptor", value);
    }


    @Accessor(qualifier = "parent", type = Accessor.Type.SETTER)
    public void setParent(SavedValuesModel value)
    {
        getPersistenceContext().setPropertyValue("parent", value);
    }
}
