package de.hybris.platform.europe1;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.UUID;

public class PDTRowTestDataBuilder
{
    private final ModelService modelService;


    public PDTRowTestDataBuilder(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public EnumerationValueModel createProductPriceGroup(String priceGroupCode)
    {
        EnumerationValueModel productPriceGroup = (EnumerationValueModel)this.modelService.create("ProductPriceGroup");
        productPriceGroup.setCode(priceGroupCode);
        productPriceGroup.setName("ProductPriceGroup");
        this.modelService.save(productPriceGroup);
        return productPriceGroup;
    }


    public EnumerationValueModel createProductDiscountGroup(String discountGroupCode)
    {
        EnumerationValueModel productDiscountGroup = (EnumerationValueModel)this.modelService.create("ProductDiscountGroup");
        productDiscountGroup.setCode(discountGroupCode);
        productDiscountGroup.setName("ProductPriceGroup");
        this.modelService.save(productDiscountGroup);
        return productDiscountGroup;
    }


    public EnumerationValueModel createUserPriceGroup(String priceGroupCode)
    {
        EnumerationValueModel userPriceGroup = (EnumerationValueModel)this.modelService.create("UserPriceGroup");
        userPriceGroup.setCode(priceGroupCode);
        userPriceGroup.setName("userPriceGroup");
        this.modelService.save(userPriceGroup);
        return userPriceGroup;
    }


    public CurrencyModel createCurrency(String isocode, String symbol)
    {
        CurrencyModel currency = (CurrencyModel)this.modelService.create(CurrencyModel.class);
        currency.setIsocode(isocode);
        currency.setSymbol(symbol);
        this.modelService.save(currency);
        return currency;
    }


    public UserModel createUser(String uid)
    {
        UserModel user = (UserModel)this.modelService.create(UserModel.class);
        user.setUid(uid);
        this.modelService.save(user);
        return user;
    }


    public UnitModel createUnit(String code, String unitType)
    {
        UnitModel unit = (UnitModel)this.modelService.create(UnitModel.class);
        unit.setCode(code);
        unit.setUnitType(unitType);
        this.modelService.save(unit);
        return unit;
    }


    public ProductModel createProduct(String code)
    {
        CatalogModel catalog = (CatalogModel)this.modelService.create(CatalogModel.class);
        catalog.setId(UUID.randomUUID().toString());
        CatalogVersionModel catalogVersion = (CatalogVersionModel)this.modelService.create(CatalogVersionModel.class);
        catalogVersion.setActive(Boolean.TRUE);
        catalogVersion.setCatalog(catalog);
        catalogVersion.setVersion(UUID.randomUUID().toString());
        this.modelService.save(catalog);
        this.modelService.save(catalogVersion);
        ProductModel product = (ProductModel)this.modelService.create(ProductModel.class);
        product.setCode(code);
        product.setCatalogVersion(catalogVersion);
        this.modelService.save(product);
        return product;
    }
}
