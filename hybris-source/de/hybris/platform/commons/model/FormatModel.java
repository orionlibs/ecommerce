package de.hybris.platform.commons.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.commons.enums.DocumentTypeEnum;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.Locale;

public class FormatModel extends ItemModel
{
    public static final String _TYPECODE = "Format";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String INITIAL = "initial";
    public static final String DOCUMENTTYPE = "documentType";
    public static final String CHAINED = "chained";
    public static final String VALIDFOR = "validFor";


    public FormatModel()
    {
    }


    public FormatModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FormatModel(String _code, DocumentTypeEnum _documentType, ItemFormatterModel _initial)
    {
        setCode(_code);
        setDocumentType(_documentType);
        setInitial(_initial);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public FormatModel(String _code, DocumentTypeEnum _documentType, ItemFormatterModel _initial, ItemModel _owner)
    {
        setCode(_code);
        setDocumentType(_documentType);
        setInitial(_initial);
        setOwner(_owner);
    }


    @Accessor(qualifier = "chained", type = Accessor.Type.GETTER)
    public Collection<MediaFormatterModel> getChained()
    {
        return (Collection<MediaFormatterModel>)getPersistenceContext().getPropertyValue("chained");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "documentType", type = Accessor.Type.GETTER)
    public DocumentTypeEnum getDocumentType()
    {
        return (DocumentTypeEnum)getPersistenceContext().getPropertyValue("documentType");
    }


    @Accessor(qualifier = "initial", type = Accessor.Type.GETTER)
    public ItemFormatterModel getInitial()
    {
        return (ItemFormatterModel)getPersistenceContext().getPropertyValue("initial");
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


    @Accessor(qualifier = "validFor", type = Accessor.Type.GETTER)
    public Collection<ComposedTypeModel> getValidFor()
    {
        return (Collection<ComposedTypeModel>)getPersistenceContext().getPropertyValue("validFor");
    }


    @Accessor(qualifier = "chained", type = Accessor.Type.SETTER)
    public void setChained(Collection<MediaFormatterModel> value)
    {
        getPersistenceContext().setPropertyValue("chained", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "documentType", type = Accessor.Type.SETTER)
    public void setDocumentType(DocumentTypeEnum value)
    {
        getPersistenceContext().setPropertyValue("documentType", value);
    }


    @Accessor(qualifier = "initial", type = Accessor.Type.SETTER)
    public void setInitial(ItemFormatterModel value)
    {
        getPersistenceContext().setPropertyValue("initial", value);
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


    @Accessor(qualifier = "validFor", type = Accessor.Type.SETTER)
    public void setValidFor(Collection<ComposedTypeModel> value)
    {
        getPersistenceContext().setPropertyValue("validFor", value);
    }
}
