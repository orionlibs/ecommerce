package de.hybris.platform.commerceservices.service.data;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.storelocator.model.PointOfServiceModel;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public class CommerceCartParameter implements Serializable
{
    public static final long DEFAULT_ENTRY_NUMBER = -1L;
    private CartModel cart;
    private ProductModel product;
    private long quantity;
    private UnitModel unit;
    private boolean createNewEntry;
    private PointOfServiceModel pointOfService;
    private long entryNumber = -1L;
    private boolean enableHooks;
    private UserModel user;
    private BaseSiteModel baseSite;
    private String guid;
    private boolean recalculate;
    private String deliveryCountryIso;
    private String deliveryZipCode;
    private Set<Integer> entryGroupNumbers;
    private Collection<ProductConfigurationItem> productConfiguration;
    private boolean ignoreRecalculation;


    public void setCart(CartModel cart)
    {
        this.cart = cart;
    }


    public CartModel getCart()
    {
        return this.cart;
    }


    public void setProduct(ProductModel product)
    {
        this.product = product;
    }


    public ProductModel getProduct()
    {
        return this.product;
    }


    public void setQuantity(long quantity)
    {
        this.quantity = quantity;
    }


    public long getQuantity()
    {
        return this.quantity;
    }


    public void setUnit(UnitModel unit)
    {
        this.unit = unit;
    }


    public UnitModel getUnit()
    {
        return this.unit;
    }


    public void setCreateNewEntry(boolean createNewEntry)
    {
        this.createNewEntry = createNewEntry;
    }


    public boolean isCreateNewEntry()
    {
        return this.createNewEntry;
    }


    public void setPointOfService(PointOfServiceModel pointOfService)
    {
        this.pointOfService = pointOfService;
    }


    public PointOfServiceModel getPointOfService()
    {
        return this.pointOfService;
    }


    public void setEntryNumber(long entryNumber)
    {
        this.entryNumber = entryNumber;
    }


    public long getEntryNumber()
    {
        return this.entryNumber;
    }


    public void setEnableHooks(boolean enableHooks)
    {
        this.enableHooks = enableHooks;
    }


    public boolean isEnableHooks()
    {
        return this.enableHooks;
    }


    public void setUser(UserModel user)
    {
        this.user = user;
    }


    public UserModel getUser()
    {
        return this.user;
    }


    public void setBaseSite(BaseSiteModel baseSite)
    {
        this.baseSite = baseSite;
    }


    public BaseSiteModel getBaseSite()
    {
        return this.baseSite;
    }


    public void setGuid(String guid)
    {
        this.guid = guid;
    }


    public String getGuid()
    {
        return this.guid;
    }


    public void setRecalculate(boolean recalculate)
    {
        this.recalculate = recalculate;
    }


    public boolean isRecalculate()
    {
        return this.recalculate;
    }


    public void setDeliveryCountryIso(String deliveryCountryIso)
    {
        this.deliveryCountryIso = deliveryCountryIso;
    }


    public String getDeliveryCountryIso()
    {
        return this.deliveryCountryIso;
    }


    public void setDeliveryZipCode(String deliveryZipCode)
    {
        this.deliveryZipCode = deliveryZipCode;
    }


    public String getDeliveryZipCode()
    {
        return this.deliveryZipCode;
    }


    public void setEntryGroupNumbers(Set<Integer> entryGroupNumbers)
    {
        this.entryGroupNumbers = entryGroupNumbers;
    }


    public Set<Integer> getEntryGroupNumbers()
    {
        return this.entryGroupNumbers;
    }


    public void setProductConfiguration(Collection<ProductConfigurationItem> productConfiguration)
    {
        this.productConfiguration = productConfiguration;
    }


    public Collection<ProductConfigurationItem> getProductConfiguration()
    {
        return this.productConfiguration;
    }


    public void setIgnoreRecalculation(boolean ignoreRecalculation)
    {
        this.ignoreRecalculation = ignoreRecalculation;
    }


    public boolean isIgnoreRecalculation()
    {
        return this.ignoreRecalculation;
    }
}
