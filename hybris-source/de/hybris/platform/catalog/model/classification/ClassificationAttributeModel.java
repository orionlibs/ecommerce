package de.hybris.platform.catalog.model.classification;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.List;
import java.util.Locale;

public class ClassificationAttributeModel extends ItemModel
{
    public static final String _TYPECODE = "ClassificationAttribute";
    public static final String SYSTEMVERSION = "systemVersion";
    public static final String CODE = "code";
    public static final String EXTERNALID = "externalID";
    public static final String NAME = "name";
    public static final String CLASSES = "classes";
    public static final String DEFAULTATTRIBUTEVALUES = "defaultAttributeValues";
    public static final String SAPERPCHARACTERISTICLONGTEXT = "sapERPCharacteristicLongText";
    public static final String DESCRIPTION = "description";


    public ClassificationAttributeModel()
    {
    }


    public ClassificationAttributeModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ClassificationAttributeModel(String _code, ClassificationSystemVersionModel _systemVersion)
    {
        setCode(_code);
        setSystemVersion(_systemVersion);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ClassificationAttributeModel(String _code, ItemModel _owner, ClassificationSystemVersionModel _systemVersion)
    {
        setCode(_code);
        setOwner(_owner);
        setSystemVersion(_systemVersion);
    }


    @Accessor(qualifier = "classes", type = Accessor.Type.GETTER)
    public List<ClassificationClassModel> getClasses()
    {
        return (List<ClassificationClassModel>)getPersistenceContext().getPropertyValue("classes");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "defaultAttributeValues", type = Accessor.Type.GETTER)
    public List<ClassificationAttributeValueModel> getDefaultAttributeValues()
    {
        return (List<ClassificationAttributeValueModel>)getPersistenceContext().getPropertyValue("defaultAttributeValues");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return getDescription(null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("description", loc);
    }


    @Accessor(qualifier = "externalID", type = Accessor.Type.GETTER)
    public String getExternalID()
    {
        return (String)getPersistenceContext().getPropertyValue("externalID");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return getName(null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("name", loc);
    }


    @Accessor(qualifier = "sapERPCharacteristicLongText", type = Accessor.Type.GETTER)
    public String getSapERPCharacteristicLongText()
    {
        return getSapERPCharacteristicLongText(null);
    }


    @Accessor(qualifier = "sapERPCharacteristicLongText", type = Accessor.Type.GETTER)
    public String getSapERPCharacteristicLongText(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("sapERPCharacteristicLongText", loc);
    }


    @Accessor(qualifier = "systemVersion", type = Accessor.Type.GETTER)
    public ClassificationSystemVersionModel getSystemVersion()
    {
        return (ClassificationSystemVersionModel)getPersistenceContext().getPropertyValue("systemVersion");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "defaultAttributeValues", type = Accessor.Type.SETTER)
    public void setDefaultAttributeValues(List<ClassificationAttributeValueModel> value)
    {
        getPersistenceContext().setPropertyValue("defaultAttributeValues", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        setDescription(value, null);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("description", loc, value);
    }


    @Accessor(qualifier = "externalID", type = Accessor.Type.SETTER)
    public void setExternalID(String value)
    {
        getPersistenceContext().setPropertyValue("externalID", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        setName(value, null);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("name", loc, value);
    }


    @Accessor(qualifier = "sapERPCharacteristicLongText", type = Accessor.Type.SETTER)
    public void setSapERPCharacteristicLongText(String value)
    {
        setSapERPCharacteristicLongText(value, null);
    }


    @Accessor(qualifier = "sapERPCharacteristicLongText", type = Accessor.Type.SETTER)
    public void setSapERPCharacteristicLongText(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("sapERPCharacteristicLongText", loc, value);
    }


    @Accessor(qualifier = "systemVersion", type = Accessor.Type.SETTER)
    public void setSystemVersion(ClassificationSystemVersionModel value)
    {
        getPersistenceContext().setPropertyValue("systemVersion", value);
    }
}
