package de.hybris.platform.cms2.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;
import java.util.List;

public class CMSVersionModel extends ItemModel
{
    public static final String _TYPECODE = "CMSVersion";
    public static final String _CMSVERSION2CMSVERSION = "CMSVersion2CMSVersion";
    public static final String UID = "uid";
    public static final String TRANSACTIONID = "transactionId";
    public static final String ITEMUID = "itemUid";
    public static final String ITEMTYPECODE = "itemTypeCode";
    public static final String ITEMCATALOGVERSION = "itemCatalogVersion";
    public static final String LABEL = "label";
    public static final String DESCRIPTION = "description";
    public static final String RETAIN = "retain";
    public static final String PAYLOAD = "payload";
    public static final String RELATEDPARENTS = "relatedParents";
    public static final String RELATEDCHILDREN = "relatedChildren";


    public CMSVersionModel()
    {
    }


    public CMSVersionModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSVersionModel(String _uid)
    {
        setUid(_uid);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public CMSVersionModel(ItemModel _owner, String _uid)
    {
        setOwner(_owner);
        setUid(_uid);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return (String)getPersistenceContext().getPropertyValue("description");
    }


    @Accessor(qualifier = "itemCatalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getItemCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("itemCatalogVersion");
    }


    @Accessor(qualifier = "itemTypeCode", type = Accessor.Type.GETTER)
    public String getItemTypeCode()
    {
        return (String)getPersistenceContext().getPropertyValue("itemTypeCode");
    }


    @Accessor(qualifier = "itemUid", type = Accessor.Type.GETTER)
    public String getItemUid()
    {
        return (String)getPersistenceContext().getPropertyValue("itemUid");
    }


    @Accessor(qualifier = "label", type = Accessor.Type.GETTER)
    public String getLabel()
    {
        return (String)getPersistenceContext().getPropertyValue("label");
    }


    @Accessor(qualifier = "payload", type = Accessor.Type.GETTER)
    public String getPayload()
    {
        return (String)getPersistenceContext().getPropertyValue("payload");
    }


    @Accessor(qualifier = "relatedChildren", type = Accessor.Type.GETTER)
    public List<CMSVersionModel> getRelatedChildren()
    {
        return (List<CMSVersionModel>)getPersistenceContext().getPropertyValue("relatedChildren");
    }


    @Accessor(qualifier = "relatedParents", type = Accessor.Type.GETTER)
    public Collection<CMSVersionModel> getRelatedParents()
    {
        return (Collection<CMSVersionModel>)getPersistenceContext().getPropertyValue("relatedParents");
    }


    @Accessor(qualifier = "retain", type = Accessor.Type.GETTER)
    public Boolean getRetain()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("retain");
    }


    @Accessor(qualifier = "transactionId", type = Accessor.Type.GETTER)
    public String getTransactionId()
    {
        return (String)getPersistenceContext().getPropertyValue("transactionId");
    }


    @Accessor(qualifier = "uid", type = Accessor.Type.GETTER)
    public String getUid()
    {
        return (String)getPersistenceContext().getPropertyValue("uid");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        getPersistenceContext().setPropertyValue("description", value);
    }


    @Accessor(qualifier = "itemCatalogVersion", type = Accessor.Type.SETTER)
    public void setItemCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("itemCatalogVersion", value);
    }


    @Accessor(qualifier = "itemTypeCode", type = Accessor.Type.SETTER)
    public void setItemTypeCode(String value)
    {
        getPersistenceContext().setPropertyValue("itemTypeCode", value);
    }


    @Accessor(qualifier = "itemUid", type = Accessor.Type.SETTER)
    public void setItemUid(String value)
    {
        getPersistenceContext().setPropertyValue("itemUid", value);
    }


    @Accessor(qualifier = "label", type = Accessor.Type.SETTER)
    public void setLabel(String value)
    {
        getPersistenceContext().setPropertyValue("label", value);
    }


    @Accessor(qualifier = "payload", type = Accessor.Type.SETTER)
    public void setPayload(String value)
    {
        getPersistenceContext().setPropertyValue("payload", value);
    }


    @Accessor(qualifier = "relatedChildren", type = Accessor.Type.SETTER)
    public void setRelatedChildren(List<CMSVersionModel> value)
    {
        getPersistenceContext().setPropertyValue("relatedChildren", value);
    }


    @Accessor(qualifier = "relatedParents", type = Accessor.Type.SETTER)
    public void setRelatedParents(Collection<CMSVersionModel> value)
    {
        getPersistenceContext().setPropertyValue("relatedParents", value);
    }


    @Accessor(qualifier = "retain", type = Accessor.Type.SETTER)
    public void setRetain(Boolean value)
    {
        getPersistenceContext().setPropertyValue("retain", value);
    }


    @Accessor(qualifier = "transactionId", type = Accessor.Type.SETTER)
    public void setTransactionId(String value)
    {
        getPersistenceContext().setPropertyValue("transactionId", value);
    }


    @Accessor(qualifier = "uid", type = Accessor.Type.SETTER)
    public void setUid(String value)
    {
        getPersistenceContext().setPropertyValue("uid", value);
    }
}
