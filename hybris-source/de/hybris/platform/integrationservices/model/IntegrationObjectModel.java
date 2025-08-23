package de.hybris.platform.integrationservices.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.odata2webservices.enums.IntegrationType;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Set;

public class IntegrationObjectModel extends ItemModel
{
    public static final String _TYPECODE = "IntegrationObject";
    public static final String CODE = "code";
    public static final String ROOTITEM = "rootItem";
    public static final String CLASSIFICATIONATTRIBUTESPRESENT = "classificationAttributesPresent";
    public static final String ITEMS = "items";
    public static final String INTEGRATIONTYPE = "integrationType";


    public IntegrationObjectModel()
    {
    }


    public IntegrationObjectModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IntegrationObjectModel(String _code)
    {
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public IntegrationObjectModel(String _code, ItemModel _owner)
    {
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "classificationAttributesPresent", type = Accessor.Type.GETTER)
    public Boolean getClassificationAttributesPresent()
    {
        return (Boolean)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "classificationAttributesPresent");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "integrationType", type = Accessor.Type.GETTER)
    public IntegrationType getIntegrationType()
    {
        return (IntegrationType)getPersistenceContext().getPropertyValue("integrationType");
    }


    @Accessor(qualifier = "items", type = Accessor.Type.GETTER)
    public Set<IntegrationObjectItemModel> getItems()
    {
        return (Set<IntegrationObjectItemModel>)getPersistenceContext().getPropertyValue("items");
    }


    @Accessor(qualifier = "rootItem", type = Accessor.Type.GETTER)
    public IntegrationObjectItemModel getRootItem()
    {
        return (IntegrationObjectItemModel)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "rootItem");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "integrationType", type = Accessor.Type.SETTER)
    public void setIntegrationType(IntegrationType value)
    {
        getPersistenceContext().setPropertyValue("integrationType", value);
    }


    @Accessor(qualifier = "items", type = Accessor.Type.SETTER)
    public void setItems(Set<IntegrationObjectItemModel> value)
    {
        getPersistenceContext().setPropertyValue("items", value);
    }
}
