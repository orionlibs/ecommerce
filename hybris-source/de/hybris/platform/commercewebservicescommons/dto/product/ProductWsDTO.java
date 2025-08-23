package de.hybris.platform.commercewebservicescommons.dto.product;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@ApiModel(value = "Product", description = "Representation of a Product")
public class ProductWsDTO implements Serializable
{
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(name = "code", value = "Code of the product")
    private String code;
    @ApiModelProperty(name = "name", value = "Name of the product")
    private String name;
    @ApiModelProperty(name = "url", value = "Url address of the product")
    private String url;
    @ApiModelProperty(name = "description", value = "Description of the product")
    private String description;
    @ApiModelProperty(name = "purchasable", value = "Flag defining if product is purchasable")
    private Boolean purchasable;
    @ApiModelProperty(name = "stock", value = "Stock value of the product")
    private StockWsDTO stock;
    @ApiModelProperty(name = "futureStocks", value = "List of future stocks")
    private List<FutureStockWsDTO> futureStocks;
    @ApiModelProperty(name = "availableForPickup", value = "Flag defining if product is available for pickup")
    private Boolean availableForPickup;
    @ApiModelProperty(name = "averageRating", value = "Rating number of average value")
    private Double averageRating;
    @ApiModelProperty(name = "numberOfReviews", value = "Number of reviews associated with the product")
    private Integer numberOfReviews;
    @ApiModelProperty(name = "summary", value = "Product summary")
    private String summary;
    @ApiModelProperty(name = "manufacturer", value = "Data of product manufacturer")
    private String manufacturer;
    @ApiModelProperty(name = "variantType", value = "Variant type of the product")
    private String variantType;
    @ApiModelProperty(name = "price", value = "Price of the product")
    private PriceWsDTO price;
    @ApiModelProperty(name = "baseProduct", value = "Information about base product")
    private String baseProduct;
    @ApiModelProperty(name = "images", value = "List of images linked to product")
    private Collection<ImageWsDTO> images;
    @ApiModelProperty(name = "categories", value = "List of categories product belongs to")
    private Collection<CategoryWsDTO> categories;
    @ApiModelProperty(name = "reviews", value = "List of reviews associated with the product")
    private Collection<ReviewWsDTO> reviews;
    @ApiModelProperty(name = "classifications", value = "List of classifications related to the product")
    private Collection<ClassificationWsDTO> classifications;
    @ApiModelProperty(name = "potentialPromotions", value = "List of potential promotions related to the product")
    private Collection<PromotionWsDTO> potentialPromotions;
    @ApiModelProperty(name = "variantOptions", value = "List of variant options related to the product")
    private List<VariantOptionWsDTO> variantOptions;
    @ApiModelProperty(name = "baseOptions", value = "List of base options related to the product")
    private List<BaseOptionWsDTO> baseOptions;
    @ApiModelProperty(name = "volumePricesFlag", value = "Flag stating if volume price should be displayed")
    private Boolean volumePricesFlag;
    @ApiModelProperty(name = "volumePrices", value = "List of volume prices")
    private List<PriceWsDTO> volumePrices;
    @ApiModelProperty(name = "productReferences", value = "List of product references")
    private List<ProductReferenceWsDTO> productReferences;
    @ApiModelProperty(name = "variantMatrix", value = "List of variant matrixes associated with the product")
    private List<VariantMatrixElementWsDTO> variantMatrix;
    @ApiModelProperty(name = "priceRange", value = "Price range assigned to the product")
    private PriceRangeWsDTO priceRange;
    @ApiModelProperty(name = "multidimensional", value = "Flag stating if product is multidimensional")
    private Boolean multidimensional;
    @ApiModelProperty(name = "configuratorType", value = "Configurator type related to the product")
    private String configuratorType;
    @ApiModelProperty(name = "configurable", value = "Flag stating if product is configurable")
    private Boolean configurable;
    @ApiModelProperty(name = "tags", value = "Tags associated with the product")
    private Set<String> tags;
    @ApiModelProperty(name = "firstVariantCode")
    private String firstVariantCode;
    @ApiModelProperty(name = "firstVariantImage")
    private String firstVariantImage;
    @ApiModelProperty(name = "purchaseEnabled", value = "Flag defining if product is available for add to cart")
    private boolean purchaseEnabled;
    @ApiModelProperty(name = "otherVariants")
    private List<ProductWsDTO> otherVariants;
    @ApiModelProperty(name = "baseProductName")
    private String baseProductName;
    @ApiModelProperty(name = "nonPurchasableDisplayStatus")
    private String nonPurchasableDisplayStatus;


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


    public void setStock(StockWsDTO stock)
    {
        this.stock = stock;
    }


    public StockWsDTO getStock()
    {
        return this.stock;
    }


    public void setFutureStocks(List<FutureStockWsDTO> futureStocks)
    {
        this.futureStocks = futureStocks;
    }


    public List<FutureStockWsDTO> getFutureStocks()
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


    public void setPrice(PriceWsDTO price)
    {
        this.price = price;
    }


    public PriceWsDTO getPrice()
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


    public void setImages(Collection<ImageWsDTO> images)
    {
        this.images = images;
    }


    public Collection<ImageWsDTO> getImages()
    {
        return this.images;
    }


    public void setCategories(Collection<CategoryWsDTO> categories)
    {
        this.categories = categories;
    }


    public Collection<CategoryWsDTO> getCategories()
    {
        return this.categories;
    }


    public void setReviews(Collection<ReviewWsDTO> reviews)
    {
        this.reviews = reviews;
    }


    public Collection<ReviewWsDTO> getReviews()
    {
        return this.reviews;
    }


    public void setClassifications(Collection<ClassificationWsDTO> classifications)
    {
        this.classifications = classifications;
    }


    public Collection<ClassificationWsDTO> getClassifications()
    {
        return this.classifications;
    }


    public void setPotentialPromotions(Collection<PromotionWsDTO> potentialPromotions)
    {
        this.potentialPromotions = potentialPromotions;
    }


    public Collection<PromotionWsDTO> getPotentialPromotions()
    {
        return this.potentialPromotions;
    }


    public void setVariantOptions(List<VariantOptionWsDTO> variantOptions)
    {
        this.variantOptions = variantOptions;
    }


    public List<VariantOptionWsDTO> getVariantOptions()
    {
        return this.variantOptions;
    }


    public void setBaseOptions(List<BaseOptionWsDTO> baseOptions)
    {
        this.baseOptions = baseOptions;
    }


    public List<BaseOptionWsDTO> getBaseOptions()
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


    public void setVolumePrices(List<PriceWsDTO> volumePrices)
    {
        this.volumePrices = volumePrices;
    }


    public List<PriceWsDTO> getVolumePrices()
    {
        return this.volumePrices;
    }


    public void setProductReferences(List<ProductReferenceWsDTO> productReferences)
    {
        this.productReferences = productReferences;
    }


    public List<ProductReferenceWsDTO> getProductReferences()
    {
        return this.productReferences;
    }


    public void setVariantMatrix(List<VariantMatrixElementWsDTO> variantMatrix)
    {
        this.variantMatrix = variantMatrix;
    }


    public List<VariantMatrixElementWsDTO> getVariantMatrix()
    {
        return this.variantMatrix;
    }


    public void setPriceRange(PriceRangeWsDTO priceRange)
    {
        this.priceRange = priceRange;
    }


    public PriceRangeWsDTO getPriceRange()
    {
        return this.priceRange;
    }


    public void setMultidimensional(Boolean multidimensional)
    {
        this.multidimensional = multidimensional;
    }


    public Boolean getMultidimensional()
    {
        return this.multidimensional;
    }


    public void setConfiguratorType(String configuratorType)
    {
        this.configuratorType = configuratorType;
    }


    public String getConfiguratorType()
    {
        return this.configuratorType;
    }


    public void setConfigurable(Boolean configurable)
    {
        this.configurable = configurable;
    }


    public Boolean getConfigurable()
    {
        return this.configurable;
    }


    public void setTags(Set<String> tags)
    {
        this.tags = tags;
    }


    public Set<String> getTags()
    {
        return this.tags;
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


    public void setPurchaseEnabled(boolean purchaseEnabled)
    {
        this.purchaseEnabled = purchaseEnabled;
    }


    public boolean isPurchaseEnabled()
    {
        return this.purchaseEnabled;
    }


    public void setOtherVariants(List<ProductWsDTO> otherVariants)
    {
        this.otherVariants = otherVariants;
    }


    public List<ProductWsDTO> getOtherVariants()
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
}
