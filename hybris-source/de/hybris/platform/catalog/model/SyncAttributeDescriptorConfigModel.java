package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

public class SyncAttributeDescriptorConfigModel extends ItemModel
{
    public static final String _TYPECODE = "SyncAttributeDescriptorConfig";
    public static final String SYNCJOB = "syncJob";
    public static final String ATTRIBUTEDESCRIPTOR = "attributeDescriptor";
    public static final String INCLUDEDINSYNC = "includedInSync";
    public static final String COPYBYVALUE = "copyByValue";
    public static final String UNTRANSLATABLE = "untranslatable";
    public static final String TRANSLATEVALUE = "translateValue";
    public static final String PRESETVALUE = "presetValue";
    public static final String PARTIALLYTRANSLATABLE = "partiallyTranslatable";


    public SyncAttributeDescriptorConfigModel()
    {
    }


    public SyncAttributeDescriptorConfigModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SyncAttributeDescriptorConfigModel(AttributeDescriptorModel _attributeDescriptor, Boolean _copyByValue, SyncItemJobModel _syncJob)
    {
        setAttributeDescriptor(_attributeDescriptor);
        setCopyByValue(_copyByValue);
        setSyncJob(_syncJob);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SyncAttributeDescriptorConfigModel(AttributeDescriptorModel _attributeDescriptor, Boolean _copyByValue, ItemModel _owner, SyncItemJobModel _syncJob)
    {
        setAttributeDescriptor(_attributeDescriptor);
        setCopyByValue(_copyByValue);
        setOwner(_owner);
        setSyncJob(_syncJob);
    }


    @Accessor(qualifier = "attributeDescriptor", type = Accessor.Type.GETTER)
    public AttributeDescriptorModel getAttributeDescriptor()
    {
        return (AttributeDescriptorModel)getPersistenceContext().getPropertyValue("attributeDescriptor");
    }


    @Accessor(qualifier = "copyByValue", type = Accessor.Type.GETTER)
    public Boolean getCopyByValue()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("copyByValue");
    }


    @Accessor(qualifier = "includedInSync", type = Accessor.Type.GETTER)
    public Boolean getIncludedInSync()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("includedInSync");
    }


    @Accessor(qualifier = "partiallyTranslatable", type = Accessor.Type.GETTER)
    public Boolean getPartiallyTranslatable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("partiallyTranslatable");
    }


    @Accessor(qualifier = "presetValue", type = Accessor.Type.GETTER)
    public Object getPresetValue()
    {
        return getPersistenceContext().getPropertyValue("presetValue");
    }


    @Accessor(qualifier = "syncJob", type = Accessor.Type.GETTER)
    public SyncItemJobModel getSyncJob()
    {
        return (SyncItemJobModel)getPersistenceContext().getPropertyValue("syncJob");
    }


    @Accessor(qualifier = "translateValue", type = Accessor.Type.GETTER)
    public Boolean getTranslateValue()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("translateValue");
    }


    @Accessor(qualifier = "untranslatable", type = Accessor.Type.GETTER)
    public Boolean getUntranslatable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("untranslatable");
    }


    @Accessor(qualifier = "attributeDescriptor", type = Accessor.Type.SETTER)
    public void setAttributeDescriptor(AttributeDescriptorModel value)
    {
        getPersistenceContext().setPropertyValue("attributeDescriptor", value);
    }


    @Accessor(qualifier = "copyByValue", type = Accessor.Type.SETTER)
    public void setCopyByValue(Boolean value)
    {
        getPersistenceContext().setPropertyValue("copyByValue", value);
    }


    @Accessor(qualifier = "includedInSync", type = Accessor.Type.SETTER)
    public void setIncludedInSync(Boolean value)
    {
        getPersistenceContext().setPropertyValue("includedInSync", value);
    }


    @Accessor(qualifier = "partiallyTranslatable", type = Accessor.Type.SETTER)
    public void setPartiallyTranslatable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("partiallyTranslatable", value);
    }


    @Accessor(qualifier = "presetValue", type = Accessor.Type.SETTER)
    public void setPresetValue(Object value)
    {
        getPersistenceContext().setPropertyValue("presetValue", value);
    }


    @Accessor(qualifier = "syncJob", type = Accessor.Type.SETTER)
    public void setSyncJob(SyncItemJobModel value)
    {
        getPersistenceContext().setPropertyValue("syncJob", value);
    }


    @Accessor(qualifier = "translateValue", type = Accessor.Type.SETTER)
    public void setTranslateValue(Boolean value)
    {
        getPersistenceContext().setPropertyValue("translateValue", value);
    }


    @Accessor(qualifier = "untranslatable", type = Accessor.Type.SETTER)
    public void setUntranslatable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("untranslatable", value);
    }
}
