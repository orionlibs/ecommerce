package de.hybris.platform.catalog.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Date;

public class AgreementModel extends ItemModel
{
    public static final String _TYPECODE = "Agreement";
    public static final String _CATALOGVERSION2AGREEMENTS = "CatalogVersion2Agreements";
    public static final String ID = "id";
    public static final String STARTDATE = "startdate";
    public static final String ENDDATE = "enddate";
    public static final String CATALOG = "Catalog";
    public static final String BUYER = "buyer";
    public static final String SUPPLIER = "supplier";
    public static final String BUYERCONTACT = "buyerContact";
    public static final String SUPPLIERCONTACT = "supplierContact";
    public static final String CURRENCY = "currency";
    public static final String CATALOGVERSION = "catalogVersion";


    public AgreementModel()
    {
    }


    public AgreementModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AgreementModel(Date _enddate, String _id)
    {
        setEnddate(_enddate);
        setId(_id);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public AgreementModel(Date _enddate, String _id, ItemModel _owner)
    {
        setEnddate(_enddate);
        setId(_id);
        setOwner(_owner);
    }


    @Accessor(qualifier = "buyer", type = Accessor.Type.GETTER)
    public CompanyModel getBuyer()
    {
        return (CompanyModel)getPersistenceContext().getPropertyValue("buyer");
    }


    @Accessor(qualifier = "buyerContact", type = Accessor.Type.GETTER)
    public UserModel getBuyerContact()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("buyerContact");
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("catalogVersion");
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.GETTER)
    public CurrencyModel getCurrency()
    {
        return (CurrencyModel)getPersistenceContext().getPropertyValue("currency");
    }


    @Accessor(qualifier = "enddate", type = Accessor.Type.GETTER)
    public Date getEnddate()
    {
        return (Date)getPersistenceContext().getPropertyValue("enddate");
    }


    @Accessor(qualifier = "id", type = Accessor.Type.GETTER)
    public String getId()
    {
        return (String)getPersistenceContext().getPropertyValue("id");
    }


    @Accessor(qualifier = "startdate", type = Accessor.Type.GETTER)
    public Date getStartdate()
    {
        return (Date)getPersistenceContext().getPropertyValue("startdate");
    }


    @Accessor(qualifier = "supplier", type = Accessor.Type.GETTER)
    public CompanyModel getSupplier()
    {
        return (CompanyModel)getPersistenceContext().getPropertyValue("supplier");
    }


    @Accessor(qualifier = "supplierContact", type = Accessor.Type.GETTER)
    public UserModel getSupplierContact()
    {
        return (UserModel)getPersistenceContext().getPropertyValue("supplierContact");
    }


    @Accessor(qualifier = "buyer", type = Accessor.Type.SETTER)
    public void setBuyer(CompanyModel value)
    {
        getPersistenceContext().setPropertyValue("buyer", value);
    }


    @Accessor(qualifier = "buyerContact", type = Accessor.Type.SETTER)
    public void setBuyerContact(UserModel value)
    {
        getPersistenceContext().setPropertyValue("buyerContact", value);
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
    public void setCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("catalogVersion", value);
    }


    @Accessor(qualifier = "currency", type = Accessor.Type.SETTER)
    public void setCurrency(CurrencyModel value)
    {
        getPersistenceContext().setPropertyValue("currency", value);
    }


    @Accessor(qualifier = "enddate", type = Accessor.Type.SETTER)
    public void setEnddate(Date value)
    {
        getPersistenceContext().setPropertyValue("enddate", value);
    }


    @Accessor(qualifier = "id", type = Accessor.Type.SETTER)
    public void setId(String value)
    {
        getPersistenceContext().setPropertyValue("id", value);
    }


    @Accessor(qualifier = "startdate", type = Accessor.Type.SETTER)
    public void setStartdate(Date value)
    {
        getPersistenceContext().setPropertyValue("startdate", value);
    }


    @Accessor(qualifier = "supplier", type = Accessor.Type.SETTER)
    public void setSupplier(CompanyModel value)
    {
        getPersistenceContext().setPropertyValue("supplier", value);
    }


    @Accessor(qualifier = "supplierContact", type = Accessor.Type.SETTER)
    public void setSupplierContact(UserModel value)
    {
        getPersistenceContext().setPropertyValue("supplierContact", value);
    }
}
