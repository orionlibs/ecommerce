package de.hybris.platform.commercefacades.product.data;

import de.hybris.platform.acceleratorfacades.order.data.PriceRangeData;
import de.hybris.platform.commerceservices.product.data.SolrFirstVariantCategoryEntryData;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ProductData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private String url;
    private String description;
    private Boolean purchasable;
    private StockData stock;
    private List<FutureStockData> futureStocks;
    private Boolean availableForPickup;
    private Double averageRating;
    private Integer numberOfReviews;
    private String summary;
    private String manufacturer;
    private String variantType;
    private PriceData price;
    private String baseProduct;
    private Collection<ImageData> images;
    private Collection<CategoryData> categories;
    private Collection<ReviewData> reviews;
    private Collection<ClassificationData> classifications;
    private Collection<PromotionData> potentialPromotions;
    private List<VariantOptionData> variantOptions;
    private List<BaseOptionData> baseOptions;
    private Boolean volumePricesFlag;
    private List<PriceData> volumePrices;
    private List<ProductReferenceData> productReferences;
    private List<VariantMatrixElementData> variantMatrix;
    private PriceRangeData priceRange;
    private List<SolrFirstVariantCategoryEntryData> firstCategoryNameList;
    private Boolean multidimensional;
    private Boolean configurable;
    private String configuratorType;
    private Boolean addToCartDisabled;
    private String addToCartDisabledMessage;
    private Set<String> tags;
    private Set<String> keywords;
    private boolean purchaseEnabled;
    private boolean searchEnabled;
    private List<ProductData> otherVariants;
    private String baseProductName;
    private String nonPurchasableDisplayStatus;
    private String firstVariantCode;
    private String firstVariantImage;


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getCode()
    {
        return this.code;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getUrl()
    {
        return this.url;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getDescription()
    {
        return this.description;
    }


    public void setPurchasable(Boolean purchasable)
    {
        this.purchasable = purchasable;
    }


    public Boolean getPurchasable()
    {
        return this.purchasable;
    }


    public void setStock(StockData stock)
    {
        this.stock = stock;
    }


    public StockData getStock()
    {
        return this.stock;
    }


    public void setFutureStocks(List<FutureStockData> futureStocks)
    {
        this.futureStocks = futureStocks;
    }


    public List<FutureStockData> getFutureStocks()
    {
        return this.futureStocks;
    }


    public void setAvailableForPickup(Boolean availableForPickup)
    {
        this.availableForPickup = availableForPickup;
    }


    public Boolean getAvailableForPickup()
    {
        return this.availableForPickup;
    }


    public void setAverageRating(Double averageRating)
    {
        this.averageRating = averageRating;
    }


    public Double getAverageRating()
    {
        return this.averageRating;
    }


    public void setNumberOfReviews(Integer numberOfReviews)
    {
        this.numberOfReviews = numberOfReviews;
    }


    public Integer getNumberOfReviews()
    {
        return this.numberOfReviews;
    }


    public void setSummary(String summary)
    {
        this.summary = summary;
    }


    public String getSummary()
    {
        return this.summary;
    }


    public void setManufacturer(String manufacturer)
    {
        this.manufacturer = manufacturer;
    }


    public String getManufacturer()
    {
        return this.manufacturer;
    }


    public void setVariantType(String variantType)
    {
        this.variantType = variantType;
    }


    public String getVariantType()
    {
        return this.variantType;
    }


    public void setPrice(PriceData price)
    {
        this.price = price;
    }


    public PriceData getPrice()
    {
        return this.price;
    }


    public void setBaseProduct(String baseProduct)
    {
        this.baseProduct = baseProduct;
    }


    public String getBaseProduct()
    {
        return this.baseProduct;
    }


    public void setImages(Collection<ImageData> images)
    {
        this.images = images;
    }


    public Collection<ImageData> getImages()
    {
        return this.images;
    }


    public void setCategories(Collection<CategoryData> categories)
    {
        this.categories = categories;
    }


    public Collection<CategoryData> getCategories()
    {
        return this.categories;
    }


    public void setReviews(Collection<ReviewData> reviews)
    {
        this.reviews = reviews;
    }


    public Collection<ReviewData> getReviews()
    {
        return this.reviews;
    }


    public void setClassifications(Collection<ClassificationData> classifications)
    {
        this.classifications = classifications;
    }


    public Collection<ClassificationData> getClassifications()
    {
        return this.classifications;
    }


    public void setPotentialPromotions(Collection<PromotionData> potentialPromotions)
    {
        this.potentialPromotions = potentialPromotions;
    }


    public Collection<PromotionData> getPotentialPromotions()
    {
        return this.potentialPromotions;
    }


    public void setVariantOptions(List<VariantOptionData> variantOptions)
    {
        this.variantOptions = variantOptions;
    }


    public List<VariantOptionData> getVariantOptions()
    {
        return this.variantOptions;
    }


    public void setBaseOptions(List<BaseOptionData> baseOptions)
    {
        this.baseOptions = baseOptions;
    }


    public List<BaseOptionData> getBaseOptions()
    {
        return this.baseOptions;
    }


    public void setVolumePricesFlag(Boolean volumePricesFlag)
    {
        this.volumePricesFlag = volumePricesFlag;
    }


    public Boolean getVolumePricesFlag()
    {
        return this.volumePricesFlag;
    }


    public void setVolumePrices(List<PriceData> volumePrices)
    {
        this.volumePrices = volumePrices;
    }


    public List<PriceData> getVolumePrices()
    {
        return this.volumePrices;
    }


    public void setProductReferences(List<ProductReferenceData> productReferences)
    {
        this.productReferences = productReferences;
    }


    public List<ProductReferenceData> getProductReferences()
    {
        return this.productReferences;
    }


    public void setVariantMatrix(List<VariantMatrixElementData> variantMatrix)
    {
        this.variantMatrix = variantMatrix;
    }


    public List<VariantMatrixElementData> getVariantMatrix()
    {
        return this.variantMatrix;
    }


    public void setPriceRange(PriceRangeData priceRange)
    {
        this.priceRange = priceRange;
    }


    public PriceRangeData getPriceRange()
    {
        return this.priceRange;
    }


    public void setFirstCategoryNameList(List<SolrFirstVariantCategoryEntryData> firstCategoryNameList)
    {
        this.firstCategoryNameList = firstCategoryNameList;
    }


    public List<SolrFirstVariantCategoryEntryData> getFirstCategoryNameList()
    {
        return this.firstCategoryNameList;
    }


    public void setMultidimensional(Boolean multidimensional)
    {
        this.multidimensional = multidimensional;
    }


    public Boolean getMultidimensional()
    {
        return this.multidimensional;
    }


    public void setConfigurable(Boolean configurable)
    {
        this.configurable = configurable;
    }


    public Boolean getConfigurable()
    {
        return this.configurable;
    }


    public void setConfiguratorType(String configuratorType)
    {
        this.configuratorType = configuratorType;
    }


    public String getConfiguratorType()
    {
        return this.configuratorType;
    }


    public void setAddToCartDisabled(Boolean addToCartDisabled)
    {
        this.addToCartDisabled = addToCartDisabled;
    }


    public Boolean getAddToCartDisabled()
    {
        return this.addToCartDisabled;
    }


    public void setAddToCartDisabledMessage(String addToCartDisabledMessage)
    {
        this.addToCartDisabledMessage = addToCartDisabledMessage;
    }


    public String getAddToCartDisabledMessage()
    {
        return this.addToCartDisabledMessage;
    }


    public void setTags(Set<String> tags)
    {
        this.tags = tags;
    }


    public Set<String> getTags()
    {
        return this.tags;
    }


    public void setKeywords(Set<String> keywords)
    {
        this.keywords = keywords;
    }


    public Set<String> getKeywords()
    {
        return this.keywords;
    }


    public void setPurchaseEnabled(boolean purchaseEnabled)
    {
        this.purchaseEnabled = purchaseEnabled;
    }


    public boolean isPurchaseEnabled()
    {
        return this.purchaseEnabled;
    }


    public void setSearchEnabled(boolean searchEnabled)
    {
        this.searchEnabled = searchEnabled;
    }


    public boolean isSearchEnabled()
    {
        return this.searchEnabled;
    }


    public void setOtherVariants(List<ProductData> otherVariants)
    {
        this.otherVariants = otherVariants;
    }


    public List<ProductData> getOtherVariants()
    {
        return this.otherVariants;
    }


    public void setBaseProductName(String baseProductName)
    {
        this.baseProductName = baseProductName;
    }


    public String getBaseProductName()
    {
        return this.baseProductName;
    }


    public void setNonPurchasableDisplayStatus(String nonPurchasableDisplayStatus)
    {
        this.nonPurchasableDisplayStatus = nonPurchasableDisplayStatus;
    }


    public String getNonPurchasableDisplayStatus()
    {
        return this.nonPurchasableDisplayStatus;
    }


    public void setFirstVariantCode(String firstVariantCode)
    {
        this.firstVariantCode = firstVariantCode;
    }


    public String getFirstVariantCode()
    {
        return this.firstVariantCode;
    }


    public void setFirstVariantImage(String firstVariantImage)
    {
        this.firstVariantImage = firstVariantImage;
    }


    public String getFirstVariantImage()
    {
        return this.firstVariantImage;
    }
}
