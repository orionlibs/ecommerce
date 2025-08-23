package de.hybris.platform.core.model.product;

import com.olympus.oca.commerce.core.enums.DistributionChainStatus;
import com.olympus.oca.commerce.core.enums.ExternalMaterialGroup;
import com.olympus.oca.commerce.core.enums.GeneralItemCategory;
import com.olympus.oca.commerce.core.enums.LoadingGroup;
import com.olympus.oca.commerce.core.enums.MaterialGroup;
import com.olympus.oca.commerce.core.enums.MaterialGroup4;
import com.olympus.oca.commerce.core.enums.MaterialType;
import com.olympus.oca.commerce.core.enums.NonPurchasableDisplayStatus;
import com.olympus.oca.commerce.core.enums.ProductUsageType;
import com.olympus.oca.commerce.core.model.PlantModel;
import com.olympus.oca.commerce.core.model.SalesOrganizationModel;
import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.acceleratorcms.model.components.ProductFeatureComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SimpleBannerComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.catalog.enums.ArticleApprovalStatus;
import de.hybris.platform.catalog.enums.ArticleStatus;
import de.hybris.platform.catalog.enums.IDType;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.KeywordModel;
import de.hybris.platform.catalog.model.ProductFeatureModel;
import de.hybris.platform.catalog.model.ProductReferenceModel;
import de.hybris.platform.catalog.model.classification.ClassificationClassModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2.model.contents.components.VideoComponentModel;
import de.hybris.platform.cms2.model.restrictions.CMSProductRestrictionModel;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.cms2lib.model.components.ProductDetailComponentModel;
import de.hybris.platform.cms2lib.model.components.ProductListComponentModel;
import de.hybris.platform.commerceservices.model.FutureStockModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaContainerModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.customerreview.model.CustomerReviewModel;
import de.hybris.platform.deeplink.model.media.BarcodeMediaModel;
import de.hybris.platform.europe1.enums.ProductDiscountGroup;
import de.hybris.platform.europe1.enums.ProductPriceGroup;
import de.hybris.platform.europe1.enums.ProductTaxGroup;
import de.hybris.platform.europe1.model.DiscountRowModel;
import de.hybris.platform.europe1.model.PriceRowModel;
import de.hybris.platform.europe1.model.TaxRowModel;
import de.hybris.platform.fraud.model.ProductOrderLimitModel;
import de.hybris.platform.ordersplitting.model.StockLevelModel;
import de.hybris.platform.ordersplitting.model.VendorModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.promotions.model.ProductPromotionModel;
import de.hybris.platform.sap.sapmodel.enums.SAPProductType;
import de.hybris.platform.sap.sapmodel.model.SAPExternalIdModel;
import de.hybris.platform.servicelayer.model.AbstractItemModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.variants.model.VariantProductModel;
import de.hybris.platform.variants.model.VariantTypeModel;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ProductModel extends ItemModel
{
    public static final String _TYPECODE = "Product";
    public static final String _PRODUCT2KEYWORDRELATION = "Product2KeywordRelation";
    public static final String _CATEGORYPRODUCTRELATION = "CategoryProductRelation";
    public static final String _STOCKLEVELPRODUCTRELATION = "StockLevelProductRelation";
    public static final String _PRODUCTSFORRESTRICTION = "ProductsForRestriction";
    public static final String _PRODUCTSFORPRODUCTLISTCOMPONENT = "ProductsForProductListComponent";
    public static final String _PRODUCTDETAILCOMPONENTSFORPRODUCT = "ProductDetailComponentsForProduct";
    public static final String _PRODUCTSFORPRODUCTCAROUSELCOMPONENT = "ProductsForProductCarouselComponent";
    public static final String _FUTURESTOCKPRODUCTRELATION = "FutureStockProductRelation";
    public static final String _PRODUCTFEATURECOMPONENTS2PRODUCTREL = "ProductFeatureComponents2ProductRel";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String UNIT = "unit";
    public static final String DESCRIPTION = "description";
    public static final String THUMBNAIL = "thumbnail";
    public static final String PICTURE = "picture";
    public static final String CATALOG = "catalog";
    public static final String CATALOGVERSION = "catalogVersion";
    public static final String ONLINEDATE = "onlineDate";
    public static final String OFFLINEDATE = "offlineDate";
    public static final String EAN = "ean";
    public static final String SUPPLIERALTERNATIVEAID = "supplierAlternativeAID";
    public static final String BUYERIDS = "buyerIDS";
    public static final String MANUFACTURERAID = "manufacturerAID";
    public static final String MANUFACTURERNAME = "manufacturerName";
    public static final String MANUFACTURERTYPEDESCRIPTION = "manufacturerTypeDescription";
    public static final String ERPGROUPBUYER = "erpGroupBuyer";
    public static final String ERPGROUPSUPPLIER = "erpGroupSupplier";
    public static final String DELIVERYTIME = "deliveryTime";
    public static final String SPECIALTREATMENTCLASSES = "specialTreatmentClasses";
    public static final String REMARKS = "remarks";
    public static final String SEGMENT = "segment";
    public static final String ORDER = "order";
    public static final String ARTICLESTATUS = "articleStatus";
    public static final String APPROVALSTATUS = "approvalStatus";
    public static final String CONTENTUNIT = "contentUnit";
    public static final String NUMBERCONTENTUNITS = "numberContentUnits";
    public static final String MINORDERQUANTITY = "minOrderQuantity";
    public static final String MAXORDERQUANTITY = "maxOrderQuantity";
    public static final String ORDERQUANTITYINTERVAL = "orderQuantityInterval";
    public static final String PRICEQUANTITY = "priceQuantity";
    public static final String NORMAL = "normal";
    public static final String THUMBNAILS = "thumbnails";
    public static final String DETAIL = "detail";
    public static final String LOGO = "logo";
    public static final String DATA_SHEET = "data_sheet";
    public static final String OTHERS = "others";
    public static final String UNTYPEDFEATURES = "untypedFeatures";
    public static final String CLASSIFICATIONCLASSES = "classificationClasses";
    public static final String STARTLINENUMBER = "startLineNumber";
    public static final String ENDLINENUMBER = "endLineNumber";
    public static final String XMLCONTENT = "xmlcontent";
    public static final String VARIANTTYPE = "variantType";
    public static final String KEYWORDS = "keywords";
    public static final String FEATURES = "features";
    public static final String VARIANTS = "variants";
    public static final String PRODUCTREFERENCES = "productReferences";
    public static final String SUPERCATEGORIES = "supercategories";
    public static final String EUROPE1PRICEFACTORY_PPG = "Europe1PriceFactory_PPG";
    public static final String EUROPE1PRICEFACTORY_PTG = "Europe1PriceFactory_PTG";
    public static final String EUROPE1PRICEFACTORY_PDG = "Europe1PriceFactory_PDG";
    public static final String EUROPE1PRICES = "europe1Prices";
    public static final String EUROPE1TAXES = "europe1Taxes";
    public static final String EUROPE1DISCOUNTS = "europe1Discounts";
    public static final String OWNEUROPE1DISCOUNTS = "ownEurope1Discounts";
    public static final String OWNEUROPE1PRICES = "ownEurope1Prices";
    public static final String OWNEUROPE1TAXES = "ownEurope1Taxes";
    public static final String BARCODES = "barcodes";
    public static final String PRODUCTORDERLIMIT = "productOrderLimit";
    public static final String VENDORS = "vendors";
    public static final String DELIVERYMODES = "deliveryModes";
    public static final String STOCKLEVELS = "stockLevels";
    public static final String RESTRICTIONS = "restrictions";
    public static final String LINKCOMPONENTS = "linkComponents";
    public static final String VIDEOCOMPONENTS = "videoComponents";
    public static final String PRODUCTLISTCOMPONENTS = "productListComponents";
    public static final String PRODUCTDETAILCOMPONENTS = "productDetailComponents";
    public static final String PRODUCTCAROUSELCOMPONENTS = "productCarouselComponents";
    public static final String NUMBEROFREVIEWS = "numberOfReviews";
    public static final String AVERAGERATING = "averageRating";
    public static final String PRODUCTREVIEWS = "productReviews";
    public static final String PROMOTIONS = "promotions";
    public static final String SUMMARY = "summary";
    public static final String GALLERYIMAGES = "galleryImages";
    public static final String REVIEWCOUNT = "reviewCount";
    public static final String REVIEWRATING = "reviewRating";
    public static final String FUTURESTOCKS = "futureStocks";
    public static final String SEQUENCEID = "sequenceId";
    public static final String SIMPLEBANNERCOMPONENTS = "simpleBannerComponents";
    public static final String SIMPLERESPONSIVEBANNERCOMPONENTS = "simpleResponsiveBannerComponents";
    public static final String PRODUCTFEATURECOMPONENTS = "productFeatureComponents";
    public static final String USAGETYPE = "usageType";
    public static final String EXTERNALMATERIALGROUP = "externalMaterialGroup";
    public static final String MATERIALNUMBER = "materialNumber";
    public static final String SALESORG = "salesOrg";
    public static final String MATERIALGROUP = "materialGroup";
    public static final String MATERIALGROUP4 = "materialGroup4";
    public static final String MATERIALTYPE = "materialType";
    public static final String GENERALITEMCATEGORY = "generalItemCategory";
    public static final String PLANT = "plant";
    public static final String DISTRIBUTIONCHAINSTATUS = "distributionChainStatus";
    public static final String GROSSWEIGHT = "grossWeight";
    public static final String GROSSWEIGHTUNIT = "grossWeightUnit";
    public static final String LOADINGGROUP = "loadingGroup";
    public static final String SEARCHENABLED = "searchEnabled";
    public static final String PURCHASEENABLED = "purchaseEnabled";
    public static final String NONPURCHASABLEDISPLAYSTATUS = "nonPurchasableDisplayStatus";
    public static final String SAPBLOCKED = "sapBlocked";
    public static final String SAPBLOCKEDDATE = "sapBlockedDate";
    public static final String SAPCONFIGURABLE = "sapConfigurable";
    public static final String SAPEAN = "sapEAN";
    public static final String SAPBASEUNITCONVERSION = "sapBaseUnitConversion";
    public static final String SAPPRODUCTID = "sapProductID";
    public static final String SAPPRODUCTTYPES = "sapProductTypes";
    public static final String SAPPLANT = "sapPlant";
    public static final String EXTERNALIDS = "externalIds";


    public ProductModel()
    {
    }


    public ProductModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductModel(CatalogVersionModel _catalogVersion, String _code)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public ProductModel(CatalogVersionModel _catalogVersion, String _code, ItemModel _owner)
    {
        setCatalogVersion(_catalogVersion);
        setCode(_code);
        setOwner(_owner);
    }


    @Accessor(qualifier = "approvalStatus", type = Accessor.Type.GETTER)
    public ArticleApprovalStatus getApprovalStatus()
    {
        return (ArticleApprovalStatus)getPersistenceContext().getPropertyValue("approvalStatus");
    }


    @Accessor(qualifier = "articleStatus", type = Accessor.Type.GETTER)
    public Map<ArticleStatus, String> getArticleStatus()
    {
        return getArticleStatus(null);
    }


    @Accessor(qualifier = "articleStatus", type = Accessor.Type.GETTER)
    public Map<ArticleStatus, String> getArticleStatus(Locale loc)
    {
        return (Map<ArticleStatus, String>)getPersistenceContext().getLocalizedValue("articleStatus", loc);
    }


    @Accessor(qualifier = "averageRating", type = Accessor.Type.GETTER)
    public Double getAverageRating()
    {
        return (Double)getPersistenceContext().getPropertyValue("averageRating");
    }


    @Accessor(qualifier = "barcodes", type = Accessor.Type.GETTER)
    public Collection<BarcodeMediaModel> getBarcodes()
    {
        return (Collection<BarcodeMediaModel>)getPersistenceContext().getPropertyValue("barcodes");
    }


    @Accessor(qualifier = "buyerIDS", type = Accessor.Type.GETTER)
    public Map<IDType, String> getBuyerIDS()
    {
        return (Map<IDType, String>)getPersistenceContext().getPropertyValue("buyerIDS");
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.GETTER)
    public CatalogVersionModel getCatalogVersion()
    {
        return (CatalogVersionModel)getPersistenceContext().getPropertyValue("catalogVersion");
    }


    @Accessor(qualifier = "classificationClasses", type = Accessor.Type.GETTER)
    public List<ClassificationClassModel> getClassificationClasses()
    {
        return (List<ClassificationClassModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "classificationClasses");
    }


    @Accessor(qualifier = "code", type = Accessor.Type.GETTER)
    public String getCode()
    {
        return (String)getPersistenceContext().getPropertyValue("code");
    }


    @Accessor(qualifier = "contentUnit", type = Accessor.Type.GETTER)
    public UnitModel getContentUnit()
    {
        return (UnitModel)getPersistenceContext().getPropertyValue("contentUnit");
    }


    @Accessor(qualifier = "data_sheet", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getData_sheet()
    {
        return (Collection<MediaModel>)getPersistenceContext().getPropertyValue("data_sheet");
    }


    @Accessor(qualifier = "deliveryModes", type = Accessor.Type.GETTER)
    public Set<DeliveryModeModel> getDeliveryModes()
    {
        return (Set<DeliveryModeModel>)getPersistenceContext().getPropertyValue("deliveryModes");
    }


    @Accessor(qualifier = "deliveryTime", type = Accessor.Type.GETTER)
    public Double getDeliveryTime()
    {
        return (Double)getPersistenceContext().getPropertyValue("deliveryTime");
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


    @Accessor(qualifier = "detail", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getDetail()
    {
        return (Collection<MediaModel>)getPersistenceContext().getPropertyValue("detail");
    }


    @Accessor(qualifier = "distributionChainStatus", type = Accessor.Type.GETTER)
    public DistributionChainStatus getDistributionChainStatus()
    {
        return (DistributionChainStatus)getPersistenceContext().getPropertyValue("distributionChainStatus");
    }


    @Accessor(qualifier = "ean", type = Accessor.Type.GETTER)
    public String getEan()
    {
        return (String)getPersistenceContext().getPropertyValue("ean");
    }


    @Accessor(qualifier = "endLineNumber", type = Accessor.Type.GETTER)
    public Integer getEndLineNumber()
    {
        return (Integer)getPersistenceContext().getPropertyValue("endLineNumber");
    }


    @Accessor(qualifier = "erpGroupBuyer", type = Accessor.Type.GETTER)
    public String getErpGroupBuyer()
    {
        return (String)getPersistenceContext().getPropertyValue("erpGroupBuyer");
    }


    @Accessor(qualifier = "erpGroupSupplier", type = Accessor.Type.GETTER)
    public String getErpGroupSupplier()
    {
        return (String)getPersistenceContext().getPropertyValue("erpGroupSupplier");
    }


    @Accessor(qualifier = "europe1Discounts", type = Accessor.Type.GETTER)
    public Collection<DiscountRowModel> getEurope1Discounts()
    {
        return (Collection<DiscountRowModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "europe1Discounts");
    }


    @Accessor(qualifier = "Europe1PriceFactory_PDG", type = Accessor.Type.GETTER)
    public ProductDiscountGroup getEurope1PriceFactory_PDG()
    {
        return (ProductDiscountGroup)getPersistenceContext().getPropertyValue("Europe1PriceFactory_PDG");
    }


    @Accessor(qualifier = "Europe1PriceFactory_PPG", type = Accessor.Type.GETTER)
    public ProductPriceGroup getEurope1PriceFactory_PPG()
    {
        return (ProductPriceGroup)getPersistenceContext().getPropertyValue("Europe1PriceFactory_PPG");
    }


    @Accessor(qualifier = "Europe1PriceFactory_PTG", type = Accessor.Type.GETTER)
    public ProductTaxGroup getEurope1PriceFactory_PTG()
    {
        return (ProductTaxGroup)getPersistenceContext().getPropertyValue("Europe1PriceFactory_PTG");
    }


    @Accessor(qualifier = "europe1Prices", type = Accessor.Type.GETTER)
    public Collection<PriceRowModel> getEurope1Prices()
    {
        return (Collection<PriceRowModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "europe1Prices");
    }


    @Accessor(qualifier = "europe1Taxes", type = Accessor.Type.GETTER)
    public Collection<TaxRowModel> getEurope1Taxes()
    {
        return (Collection<TaxRowModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "europe1Taxes");
    }


    @Accessor(qualifier = "externalIds", type = Accessor.Type.GETTER)
    public List<SAPExternalIdModel> getExternalIds()
    {
        return (List<SAPExternalIdModel>)getPersistenceContext().getPropertyValue("externalIds");
    }


    @Accessor(qualifier = "externalMaterialGroup", type = Accessor.Type.GETTER)
    public ExternalMaterialGroup getExternalMaterialGroup()
    {
        return (ExternalMaterialGroup)getPersistenceContext().getPropertyValue("externalMaterialGroup");
    }


    @Accessor(qualifier = "features", type = Accessor.Type.GETTER)
    public List<ProductFeatureModel> getFeatures()
    {
        return (List<ProductFeatureModel>)getPersistenceContext().getPropertyValue("features");
    }


    @Accessor(qualifier = "futureStocks", type = Accessor.Type.GETTER)
    public Set<FutureStockModel> getFutureStocks()
    {
        return (Set<FutureStockModel>)getPersistenceContext().getPropertyValue("futureStocks");
    }


    @Accessor(qualifier = "galleryImages", type = Accessor.Type.GETTER)
    public List<MediaContainerModel> getGalleryImages()
    {
        return (List<MediaContainerModel>)getPersistenceContext().getPropertyValue("galleryImages");
    }


    @Accessor(qualifier = "generalItemCategory", type = Accessor.Type.GETTER)
    public GeneralItemCategory getGeneralItemCategory()
    {
        return (GeneralItemCategory)getPersistenceContext().getPropertyValue("generalItemCategory");
    }


    @Accessor(qualifier = "grossWeight", type = Accessor.Type.GETTER)
    public Double getGrossWeight()
    {
        return (Double)getPersistenceContext().getPropertyValue("grossWeight");
    }


    @Accessor(qualifier = "grossWeightUnit", type = Accessor.Type.GETTER)
    public UnitModel getGrossWeightUnit()
    {
        return (UnitModel)getPersistenceContext().getPropertyValue("grossWeightUnit");
    }


    @Accessor(qualifier = "keywords", type = Accessor.Type.GETTER)
    public List<KeywordModel> getKeywords()
    {
        return getKeywords(null);
    }


    @Accessor(qualifier = "keywords", type = Accessor.Type.GETTER)
    public List<KeywordModel> getKeywords(Locale loc)
    {
        return (List<KeywordModel>)getPersistenceContext().getLocalizedRelationValue("keywords", loc);
    }


    @Accessor(qualifier = "linkComponents", type = Accessor.Type.GETTER)
    public List<CMSLinkComponentModel> getLinkComponents()
    {
        return (List<CMSLinkComponentModel>)getPersistenceContext().getPropertyValue("linkComponents");
    }


    @Accessor(qualifier = "loadingGroup", type = Accessor.Type.GETTER)
    public LoadingGroup getLoadingGroup()
    {
        return (LoadingGroup)getPersistenceContext().getPropertyValue("loadingGroup");
    }


    @Accessor(qualifier = "logo", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getLogo()
    {
        return (Collection<MediaModel>)getPersistenceContext().getPropertyValue("logo");
    }


    @Accessor(qualifier = "manufacturerAID", type = Accessor.Type.GETTER)
    public String getManufacturerAID()
    {
        return (String)getPersistenceContext().getPropertyValue("manufacturerAID");
    }


    @Accessor(qualifier = "manufacturerName", type = Accessor.Type.GETTER)
    public String getManufacturerName()
    {
        return (String)getPersistenceContext().getPropertyValue("manufacturerName");
    }


    @Accessor(qualifier = "manufacturerTypeDescription", type = Accessor.Type.GETTER)
    public String getManufacturerTypeDescription()
    {
        return getManufacturerTypeDescription(null);
    }


    @Accessor(qualifier = "manufacturerTypeDescription", type = Accessor.Type.GETTER)
    public String getManufacturerTypeDescription(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("manufacturerTypeDescription", loc);
    }


    @Accessor(qualifier = "materialGroup", type = Accessor.Type.GETTER)
    public MaterialGroup getMaterialGroup()
    {
        return (MaterialGroup)getPersistenceContext().getPropertyValue("materialGroup");
    }


    @Accessor(qualifier = "materialGroup4", type = Accessor.Type.GETTER)
    public MaterialGroup4 getMaterialGroup4()
    {
        return (MaterialGroup4)getPersistenceContext().getPropertyValue("materialGroup4");
    }


    @Accessor(qualifier = "materialNumber", type = Accessor.Type.GETTER)
    public String getMaterialNumber()
    {
        return (String)getPersistenceContext().getPropertyValue("materialNumber");
    }


    @Accessor(qualifier = "materialType", type = Accessor.Type.GETTER)
    public MaterialType getMaterialType()
    {
        return (MaterialType)getPersistenceContext().getPropertyValue("materialType");
    }


    @Accessor(qualifier = "maxOrderQuantity", type = Accessor.Type.GETTER)
    public Integer getMaxOrderQuantity()
    {
        return (Integer)getPersistenceContext().getPropertyValue("maxOrderQuantity");
    }


    @Accessor(qualifier = "minOrderQuantity", type = Accessor.Type.GETTER)
    public Integer getMinOrderQuantity()
    {
        return (Integer)getPersistenceContext().getPropertyValue("minOrderQuantity");
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


    @Accessor(qualifier = "nonPurchasableDisplayStatus", type = Accessor.Type.GETTER)
    public NonPurchasableDisplayStatus getNonPurchasableDisplayStatus()
    {
        return (NonPurchasableDisplayStatus)getPersistenceContext().getPropertyValue("nonPurchasableDisplayStatus");
    }


    @Accessor(qualifier = "normal", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getNormal()
    {
        return (Collection<MediaModel>)getPersistenceContext().getPropertyValue("normal");
    }


    @Accessor(qualifier = "numberContentUnits", type = Accessor.Type.GETTER)
    public Double getNumberContentUnits()
    {
        return (Double)getPersistenceContext().getPropertyValue("numberContentUnits");
    }


    @Accessor(qualifier = "numberOfReviews", type = Accessor.Type.GETTER)
    public Integer getNumberOfReviews()
    {
        return (Integer)getPersistenceContext().getPropertyValue("numberOfReviews");
    }


    @Accessor(qualifier = "offlineDate", type = Accessor.Type.GETTER)
    public Date getOfflineDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("offlineDate");
    }


    @Accessor(qualifier = "onlineDate", type = Accessor.Type.GETTER)
    public Date getOnlineDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("onlineDate");
    }


    @Accessor(qualifier = "order", type = Accessor.Type.GETTER)
    public Integer getOrder()
    {
        return (Integer)getPersistenceContext().getPropertyValue("order");
    }


    @Accessor(qualifier = "orderQuantityInterval", type = Accessor.Type.GETTER)
    public Integer getOrderQuantityInterval()
    {
        return (Integer)getPersistenceContext().getPropertyValue("orderQuantityInterval");
    }


    @Accessor(qualifier = "others", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getOthers()
    {
        return (Collection<MediaModel>)getPersistenceContext().getPropertyValue("others");
    }


    @Accessor(qualifier = "ownEurope1Discounts", type = Accessor.Type.GETTER)
    public Collection<DiscountRowModel> getOwnEurope1Discounts()
    {
        return (Collection<DiscountRowModel>)getPersistenceContext().getPropertyValue("ownEurope1Discounts");
    }


    @Accessor(qualifier = "ownEurope1Prices", type = Accessor.Type.GETTER)
    public Collection<PriceRowModel> getOwnEurope1Prices()
    {
        return (Collection<PriceRowModel>)getPersistenceContext().getPropertyValue("ownEurope1Prices");
    }


    @Accessor(qualifier = "ownEurope1Taxes", type = Accessor.Type.GETTER)
    public Collection<TaxRowModel> getOwnEurope1Taxes()
    {
        return (Collection<TaxRowModel>)getPersistenceContext().getPropertyValue("ownEurope1Taxes");
    }


    @Accessor(qualifier = "picture", type = Accessor.Type.GETTER)
    public MediaModel getPicture()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("picture");
    }


    @Accessor(qualifier = "plant", type = Accessor.Type.GETTER)
    public PlantModel getPlant()
    {
        return (PlantModel)getPersistenceContext().getPropertyValue("plant");
    }


    @Accessor(qualifier = "priceQuantity", type = Accessor.Type.GETTER)
    public Double getPriceQuantity()
    {
        return (Double)getPersistenceContext().getPropertyValue("priceQuantity");
    }


    @Accessor(qualifier = "productCarouselComponents", type = Accessor.Type.GETTER)
    public Collection<ProductCarouselComponentModel> getProductCarouselComponents()
    {
        return (Collection<ProductCarouselComponentModel>)getPersistenceContext().getPropertyValue("productCarouselComponents");
    }


    @Accessor(qualifier = "productDetailComponents", type = Accessor.Type.GETTER)
    public List<ProductDetailComponentModel> getProductDetailComponents()
    {
        return (List<ProductDetailComponentModel>)getPersistenceContext().getPropertyValue("productDetailComponents");
    }


    @Accessor(qualifier = "productFeatureComponents", type = Accessor.Type.GETTER)
    public List<ProductFeatureComponentModel> getProductFeatureComponents()
    {
        return (List<ProductFeatureComponentModel>)getPersistenceContext().getPropertyValue("productFeatureComponents");
    }


    @Accessor(qualifier = "productListComponents", type = Accessor.Type.GETTER)
    public Collection<ProductListComponentModel> getProductListComponents()
    {
        return (Collection<ProductListComponentModel>)getPersistenceContext().getPropertyValue("productListComponents");
    }


    @Accessor(qualifier = "productOrderLimit", type = Accessor.Type.GETTER)
    public ProductOrderLimitModel getProductOrderLimit()
    {
        return (ProductOrderLimitModel)getPersistenceContext().getPropertyValue("productOrderLimit");
    }


    @Accessor(qualifier = "productReferences", type = Accessor.Type.GETTER)
    public Collection<ProductReferenceModel> getProductReferences()
    {
        return (Collection<ProductReferenceModel>)getPersistenceContext().getPropertyValue("productReferences");
    }


    @Accessor(qualifier = "productReviews", type = Accessor.Type.GETTER)
    public Collection<CustomerReviewModel> getProductReviews()
    {
        return (Collection<CustomerReviewModel>)getPersistenceContext().getPropertyValue("productReviews");
    }


    @Accessor(qualifier = "promotions", type = Accessor.Type.GETTER)
    public Collection<ProductPromotionModel> getPromotions()
    {
        return (Collection<ProductPromotionModel>)getPersistenceContext().getPropertyValue("promotions");
    }


    @Accessor(qualifier = "remarks", type = Accessor.Type.GETTER)
    public String getRemarks()
    {
        return getRemarks(null);
    }


    @Accessor(qualifier = "remarks", type = Accessor.Type.GETTER)
    public String getRemarks(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("remarks", loc);
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.GETTER)
    public Collection<CMSProductRestrictionModel> getRestrictions()
    {
        return (Collection<CMSProductRestrictionModel>)getPersistenceContext().getPropertyValue("restrictions");
    }


    @Accessor(qualifier = "reviewCount", type = Accessor.Type.GETTER)
    public Integer getReviewCount()
    {
        return (Integer)getPersistenceContext().getPropertyValue("reviewCount");
    }


    @Accessor(qualifier = "reviewRating", type = Accessor.Type.GETTER)
    public Double getReviewRating()
    {
        return (Double)getPersistenceContext().getPropertyValue("reviewRating");
    }


    @Accessor(qualifier = "salesOrg", type = Accessor.Type.GETTER)
    public SalesOrganizationModel getSalesOrg()
    {
        return (SalesOrganizationModel)getPersistenceContext().getPropertyValue("salesOrg");
    }


    @Accessor(qualifier = "sapBaseUnitConversion", type = Accessor.Type.GETTER)
    public Double getSapBaseUnitConversion()
    {
        return (Double)getPersistenceContext().getPropertyValue("sapBaseUnitConversion");
    }


    @Accessor(qualifier = "sapBlocked", type = Accessor.Type.GETTER)
    public Boolean getSapBlocked()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("sapBlocked");
    }


    @Accessor(qualifier = "sapBlockedDate", type = Accessor.Type.GETTER)
    public Date getSapBlockedDate()
    {
        return (Date)getPersistenceContext().getPropertyValue("sapBlockedDate");
    }


    @Accessor(qualifier = "sapConfigurable", type = Accessor.Type.GETTER)
    public Boolean getSapConfigurable()
    {
        return (Boolean)getPersistenceContext().getPropertyValue("sapConfigurable");
    }


    @Accessor(qualifier = "sapEAN", type = Accessor.Type.GETTER)
    public String getSapEAN()
    {
        return (String)getPersistenceContext().getPropertyValue("sapEAN");
    }


    @Accessor(qualifier = "sapPlant", type = Accessor.Type.GETTER)
    public WarehouseModel getSapPlant()
    {
        return (WarehouseModel)getPersistenceContext().getPropertyValue("sapPlant");
    }


    @Accessor(qualifier = "sapProductID", type = Accessor.Type.GETTER)
    public String getSapProductID()
    {
        return (String)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "sapProductID");
    }


    @Accessor(qualifier = "sapProductTypes", type = Accessor.Type.GETTER)
    public Set<SAPProductType> getSapProductTypes()
    {
        return (Set<SAPProductType>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "sapProductTypes");
    }


    @Accessor(qualifier = "segment", type = Accessor.Type.GETTER)
    public String getSegment()
    {
        return getSegment(null);
    }


    @Accessor(qualifier = "segment", type = Accessor.Type.GETTER)
    public String getSegment(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("segment", loc);
    }


    @Accessor(qualifier = "sequenceId", type = Accessor.Type.GETTER)
    public Long getSequenceId()
    {
        return (Long)getPersistenceContext().getPropertyValue("sequenceId");
    }


    @Accessor(qualifier = "simpleBannerComponents", type = Accessor.Type.GETTER)
    public List<SimpleBannerComponentModel> getSimpleBannerComponents()
    {
        return (List<SimpleBannerComponentModel>)getPersistenceContext().getPropertyValue("simpleBannerComponents");
    }


    @Accessor(qualifier = "simpleResponsiveBannerComponents", type = Accessor.Type.GETTER)
    public List<SimpleResponsiveBannerComponentModel> getSimpleResponsiveBannerComponents()
    {
        return (List<SimpleResponsiveBannerComponentModel>)getPersistenceContext().getPropertyValue("simpleResponsiveBannerComponents");
    }


    @Accessor(qualifier = "specialTreatmentClasses", type = Accessor.Type.GETTER)
    public Map<String, String> getSpecialTreatmentClasses()
    {
        return (Map<String, String>)getPersistenceContext().getPropertyValue("specialTreatmentClasses");
    }


    @Accessor(qualifier = "startLineNumber", type = Accessor.Type.GETTER)
    public Integer getStartLineNumber()
    {
        return (Integer)getPersistenceContext().getPropertyValue("startLineNumber");
    }


    @Accessor(qualifier = "stockLevels", type = Accessor.Type.GETTER)
    public Set<StockLevelModel> getStockLevels()
    {
        return (Set<StockLevelModel>)getPersistenceContext().getPropertyValue("stockLevels");
    }


    @Accessor(qualifier = "summary", type = Accessor.Type.GETTER)
    public String getSummary()
    {
        return getSummary(null);
    }


    @Accessor(qualifier = "summary", type = Accessor.Type.GETTER)
    public String getSummary(Locale loc)
    {
        return (String)getPersistenceContext().getLocalizedValue("summary", loc);
    }


    @Accessor(qualifier = "supercategories", type = Accessor.Type.GETTER)
    public Collection<CategoryModel> getSupercategories()
    {
        return (Collection<CategoryModel>)getPersistenceContext().getPropertyValue("supercategories");
    }


    @Accessor(qualifier = "supplierAlternativeAID", type = Accessor.Type.GETTER)
    public String getSupplierAlternativeAID()
    {
        return (String)getPersistenceContext().getPropertyValue("supplierAlternativeAID");
    }


    @Accessor(qualifier = "thumbnail", type = Accessor.Type.GETTER)
    public MediaModel getThumbnail()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("thumbnail");
    }


    @Accessor(qualifier = "thumbnails", type = Accessor.Type.GETTER)
    public Collection<MediaModel> getThumbnails()
    {
        return (Collection<MediaModel>)getPersistenceContext().getPropertyValue("thumbnails");
    }


    @Accessor(qualifier = "unit", type = Accessor.Type.GETTER)
    public UnitModel getUnit()
    {
        return (UnitModel)getPersistenceContext().getPropertyValue("unit");
    }


    @Accessor(qualifier = "untypedFeatures", type = Accessor.Type.GETTER)
    public List<ProductFeatureModel> getUntypedFeatures()
    {
        return (List<ProductFeatureModel>)getPersistenceContext().getDynamicValue((AbstractItemModel)this, "untypedFeatures");
    }


    @Accessor(qualifier = "usageType", type = Accessor.Type.GETTER)
    public ProductUsageType getUsageType()
    {
        return (ProductUsageType)getPersistenceContext().getPropertyValue("usageType");
    }


    @Accessor(qualifier = "variants", type = Accessor.Type.GETTER)
    public Collection<VariantProductModel> getVariants()
    {
        return (Collection<VariantProductModel>)getPersistenceContext().getPropertyValue("variants");
    }


    @Accessor(qualifier = "variantType", type = Accessor.Type.GETTER)
    public VariantTypeModel getVariantType()
    {
        return (VariantTypeModel)getPersistenceContext().getPropertyValue("variantType");
    }


    @Accessor(qualifier = "vendors", type = Accessor.Type.GETTER)
    public Set<VendorModel> getVendors()
    {
        return (Set<VendorModel>)getPersistenceContext().getPropertyValue("vendors");
    }


    @Accessor(qualifier = "videoComponents", type = Accessor.Type.GETTER)
    public List<VideoComponentModel> getVideoComponents()
    {
        return (List<VideoComponentModel>)getPersistenceContext().getPropertyValue("videoComponents");
    }


    @Accessor(qualifier = "xmlcontent", type = Accessor.Type.GETTER)
    public String getXmlcontent()
    {
        return (String)getPersistenceContext().getPropertyValue("xmlcontent");
    }


    @Accessor(qualifier = "purchaseEnabled", type = Accessor.Type.GETTER)
    public boolean isPurchaseEnabled()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("purchaseEnabled"));
    }


    @Accessor(qualifier = "searchEnabled", type = Accessor.Type.GETTER)
    public boolean isSearchEnabled()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("searchEnabled"));
    }


    @Accessor(qualifier = "approvalStatus", type = Accessor.Type.SETTER)
    public void setApprovalStatus(ArticleApprovalStatus value)
    {
        getPersistenceContext().setPropertyValue("approvalStatus", value);
    }


    @Accessor(qualifier = "articleStatus", type = Accessor.Type.SETTER)
    public void setArticleStatus(Map<ArticleStatus, String> value)
    {
        setArticleStatus(value, null);
    }


    @Accessor(qualifier = "articleStatus", type = Accessor.Type.SETTER)
    public void setArticleStatus(Map<ArticleStatus, String> value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("articleStatus", loc, value);
    }


    @Accessor(qualifier = "buyerIDS", type = Accessor.Type.SETTER)
    public void setBuyerIDS(Map<IDType, String> value)
    {
        getPersistenceContext().setPropertyValue("buyerIDS", value);
    }


    @Accessor(qualifier = "catalogVersion", type = Accessor.Type.SETTER)
    public void setCatalogVersion(CatalogVersionModel value)
    {
        getPersistenceContext().setPropertyValue("catalogVersion", value);
    }


    @Accessor(qualifier = "code", type = Accessor.Type.SETTER)
    public void setCode(String value)
    {
        getPersistenceContext().setPropertyValue("code", value);
    }


    @Accessor(qualifier = "contentUnit", type = Accessor.Type.SETTER)
    public void setContentUnit(UnitModel value)
    {
        getPersistenceContext().setPropertyValue("contentUnit", value);
    }


    @Accessor(qualifier = "data_sheet", type = Accessor.Type.SETTER)
    public void setData_sheet(Collection<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("data_sheet", value);
    }


    @Accessor(qualifier = "deliveryModes", type = Accessor.Type.SETTER)
    public void setDeliveryModes(Set<DeliveryModeModel> value)
    {
        getPersistenceContext().setPropertyValue("deliveryModes", value);
    }


    @Accessor(qualifier = "deliveryTime", type = Accessor.Type.SETTER)
    public void setDeliveryTime(Double value)
    {
        getPersistenceContext().setPropertyValue("deliveryTime", value);
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


    @Accessor(qualifier = "detail", type = Accessor.Type.SETTER)
    public void setDetail(Collection<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("detail", value);
    }


    @Accessor(qualifier = "distributionChainStatus", type = Accessor.Type.SETTER)
    public void setDistributionChainStatus(DistributionChainStatus value)
    {
        getPersistenceContext().setPropertyValue("distributionChainStatus", value);
    }


    @Accessor(qualifier = "ean", type = Accessor.Type.SETTER)
    public void setEan(String value)
    {
        getPersistenceContext().setPropertyValue("ean", value);
    }


    @Accessor(qualifier = "endLineNumber", type = Accessor.Type.SETTER)
    public void setEndLineNumber(Integer value)
    {
        getPersistenceContext().setPropertyValue("endLineNumber", value);
    }


    @Accessor(qualifier = "erpGroupBuyer", type = Accessor.Type.SETTER)
    public void setErpGroupBuyer(String value)
    {
        getPersistenceContext().setPropertyValue("erpGroupBuyer", value);
    }


    @Accessor(qualifier = "erpGroupSupplier", type = Accessor.Type.SETTER)
    public void setErpGroupSupplier(String value)
    {
        getPersistenceContext().setPropertyValue("erpGroupSupplier", value);
    }


    @Accessor(qualifier = "europe1Discounts", type = Accessor.Type.SETTER)
    public void setEurope1Discounts(Collection<DiscountRowModel> value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "europe1Discounts", value);
    }


    @Accessor(qualifier = "Europe1PriceFactory_PDG", type = Accessor.Type.SETTER)
    public void setEurope1PriceFactory_PDG(ProductDiscountGroup value)
    {
        getPersistenceContext().setPropertyValue("Europe1PriceFactory_PDG", value);
    }


    @Accessor(qualifier = "Europe1PriceFactory_PPG", type = Accessor.Type.SETTER)
    public void setEurope1PriceFactory_PPG(ProductPriceGroup value)
    {
        getPersistenceContext().setPropertyValue("Europe1PriceFactory_PPG", value);
    }


    @Accessor(qualifier = "Europe1PriceFactory_PTG", type = Accessor.Type.SETTER)
    public void setEurope1PriceFactory_PTG(ProductTaxGroup value)
    {
        getPersistenceContext().setPropertyValue("Europe1PriceFactory_PTG", value);
    }


    @Accessor(qualifier = "europe1Prices", type = Accessor.Type.SETTER)
    public void setEurope1Prices(Collection<PriceRowModel> value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "europe1Prices", value);
    }


    @Accessor(qualifier = "europe1Taxes", type = Accessor.Type.SETTER)
    public void setEurope1Taxes(Collection<TaxRowModel> value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "europe1Taxes", value);
    }


    @Accessor(qualifier = "externalIds", type = Accessor.Type.SETTER)
    public void setExternalIds(List<SAPExternalIdModel> value)
    {
        getPersistenceContext().setPropertyValue("externalIds", value);
    }


    @Accessor(qualifier = "externalMaterialGroup", type = Accessor.Type.SETTER)
    public void setExternalMaterialGroup(ExternalMaterialGroup value)
    {
        getPersistenceContext().setPropertyValue("externalMaterialGroup", value);
    }


    @Accessor(qualifier = "features", type = Accessor.Type.SETTER)
    public void setFeatures(List<ProductFeatureModel> value)
    {
        getPersistenceContext().setPropertyValue("features", value);
    }


    @Accessor(qualifier = "futureStocks", type = Accessor.Type.SETTER)
    public void setFutureStocks(Set<FutureStockModel> value)
    {
        getPersistenceContext().setPropertyValue("futureStocks", value);
    }


    @Accessor(qualifier = "galleryImages", type = Accessor.Type.SETTER)
    public void setGalleryImages(List<MediaContainerModel> value)
    {
        getPersistenceContext().setPropertyValue("galleryImages", value);
    }


    @Accessor(qualifier = "generalItemCategory", type = Accessor.Type.SETTER)
    public void setGeneralItemCategory(GeneralItemCategory value)
    {
        getPersistenceContext().setPropertyValue("generalItemCategory", value);
    }


    @Accessor(qualifier = "grossWeight", type = Accessor.Type.SETTER)
    public void setGrossWeight(Double value)
    {
        getPersistenceContext().setPropertyValue("grossWeight", value);
    }


    @Accessor(qualifier = "grossWeightUnit", type = Accessor.Type.SETTER)
    public void setGrossWeightUnit(UnitModel value)
    {
        getPersistenceContext().setPropertyValue("grossWeightUnit", value);
    }


    @Accessor(qualifier = "keywords", type = Accessor.Type.SETTER)
    public void setKeywords(List<KeywordModel> value)
    {
        setKeywords(value, null);
    }


    @Accessor(qualifier = "keywords", type = Accessor.Type.SETTER)
    public void setKeywords(List<KeywordModel> value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("keywords", loc, value);
    }


    @Accessor(qualifier = "linkComponents", type = Accessor.Type.SETTER)
    public void setLinkComponents(List<CMSLinkComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("linkComponents", value);
    }


    @Accessor(qualifier = "loadingGroup", type = Accessor.Type.SETTER)
    public void setLoadingGroup(LoadingGroup value)
    {
        getPersistenceContext().setPropertyValue("loadingGroup", value);
    }


    @Accessor(qualifier = "logo", type = Accessor.Type.SETTER)
    public void setLogo(Collection<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("logo", value);
    }


    @Accessor(qualifier = "manufacturerAID", type = Accessor.Type.SETTER)
    public void setManufacturerAID(String value)
    {
        getPersistenceContext().setPropertyValue("manufacturerAID", value);
    }


    @Accessor(qualifier = "manufacturerName", type = Accessor.Type.SETTER)
    public void setManufacturerName(String value)
    {
        getPersistenceContext().setPropertyValue("manufacturerName", value);
    }


    @Accessor(qualifier = "manufacturerTypeDescription", type = Accessor.Type.SETTER)
    public void setManufacturerTypeDescription(String value)
    {
        setManufacturerTypeDescription(value, null);
    }


    @Accessor(qualifier = "manufacturerTypeDescription", type = Accessor.Type.SETTER)
    public void setManufacturerTypeDescription(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("manufacturerTypeDescription", loc, value);
    }


    @Accessor(qualifier = "materialGroup", type = Accessor.Type.SETTER)
    public void setMaterialGroup(MaterialGroup value)
    {
        getPersistenceContext().setPropertyValue("materialGroup", value);
    }


    @Accessor(qualifier = "materialGroup4", type = Accessor.Type.SETTER)
    public void setMaterialGroup4(MaterialGroup4 value)
    {
        getPersistenceContext().setPropertyValue("materialGroup4", value);
    }


    @Accessor(qualifier = "materialNumber", type = Accessor.Type.SETTER)
    public void setMaterialNumber(String value)
    {
        getPersistenceContext().setPropertyValue("materialNumber", value);
    }


    @Accessor(qualifier = "materialType", type = Accessor.Type.SETTER)
    public void setMaterialType(MaterialType value)
    {
        getPersistenceContext().setPropertyValue("materialType", value);
    }


    @Accessor(qualifier = "maxOrderQuantity", type = Accessor.Type.SETTER)
    public void setMaxOrderQuantity(Integer value)
    {
        getPersistenceContext().setPropertyValue("maxOrderQuantity", value);
    }


    @Accessor(qualifier = "minOrderQuantity", type = Accessor.Type.SETTER)
    public void setMinOrderQuantity(Integer value)
    {
        getPersistenceContext().setPropertyValue("minOrderQuantity", value);
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


    @Accessor(qualifier = "nonPurchasableDisplayStatus", type = Accessor.Type.SETTER)
    public void setNonPurchasableDisplayStatus(NonPurchasableDisplayStatus value)
    {
        getPersistenceContext().setPropertyValue("nonPurchasableDisplayStatus", value);
    }


    @Accessor(qualifier = "normal", type = Accessor.Type.SETTER)
    public void setNormal(Collection<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("normal", value);
    }


    @Accessor(qualifier = "numberContentUnits", type = Accessor.Type.SETTER)
    public void setNumberContentUnits(Double value)
    {
        getPersistenceContext().setPropertyValue("numberContentUnits", value);
    }


    @Accessor(qualifier = "offlineDate", type = Accessor.Type.SETTER)
    public void setOfflineDate(Date value)
    {
        getPersistenceContext().setPropertyValue("offlineDate", value);
    }


    @Accessor(qualifier = "onlineDate", type = Accessor.Type.SETTER)
    public void setOnlineDate(Date value)
    {
        getPersistenceContext().setPropertyValue("onlineDate", value);
    }


    @Accessor(qualifier = "order", type = Accessor.Type.SETTER)
    public void setOrder(Integer value)
    {
        getPersistenceContext().setPropertyValue("order", value);
    }


    @Accessor(qualifier = "orderQuantityInterval", type = Accessor.Type.SETTER)
    public void setOrderQuantityInterval(Integer value)
    {
        getPersistenceContext().setPropertyValue("orderQuantityInterval", value);
    }


    @Accessor(qualifier = "others", type = Accessor.Type.SETTER)
    public void setOthers(Collection<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("others", value);
    }


    @Accessor(qualifier = "ownEurope1Discounts", type = Accessor.Type.SETTER)
    public void setOwnEurope1Discounts(Collection<DiscountRowModel> value)
    {
        getPersistenceContext().setPropertyValue("ownEurope1Discounts", value);
    }


    @Accessor(qualifier = "ownEurope1Prices", type = Accessor.Type.SETTER)
    public void setOwnEurope1Prices(Collection<PriceRowModel> value)
    {
        getPersistenceContext().setPropertyValue("ownEurope1Prices", value);
    }


    @Accessor(qualifier = "ownEurope1Taxes", type = Accessor.Type.SETTER)
    public void setOwnEurope1Taxes(Collection<TaxRowModel> value)
    {
        getPersistenceContext().setPropertyValue("ownEurope1Taxes", value);
    }


    @Accessor(qualifier = "picture", type = Accessor.Type.SETTER)
    public void setPicture(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("picture", value);
    }


    @Accessor(qualifier = "plant", type = Accessor.Type.SETTER)
    public void setPlant(PlantModel value)
    {
        getPersistenceContext().setPropertyValue("plant", value);
    }


    @Accessor(qualifier = "priceQuantity", type = Accessor.Type.SETTER)
    public void setPriceQuantity(Double value)
    {
        getPersistenceContext().setPropertyValue("priceQuantity", value);
    }


    @Accessor(qualifier = "productCarouselComponents", type = Accessor.Type.SETTER)
    public void setProductCarouselComponents(Collection<ProductCarouselComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("productCarouselComponents", value);
    }


    @Accessor(qualifier = "productDetailComponents", type = Accessor.Type.SETTER)
    public void setProductDetailComponents(List<ProductDetailComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("productDetailComponents", value);
    }


    @Accessor(qualifier = "productFeatureComponents", type = Accessor.Type.SETTER)
    public void setProductFeatureComponents(List<ProductFeatureComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("productFeatureComponents", value);
    }


    @Accessor(qualifier = "productListComponents", type = Accessor.Type.SETTER)
    public void setProductListComponents(Collection<ProductListComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("productListComponents", value);
    }


    @Accessor(qualifier = "productOrderLimit", type = Accessor.Type.SETTER)
    public void setProductOrderLimit(ProductOrderLimitModel value)
    {
        getPersistenceContext().setPropertyValue("productOrderLimit", value);
    }


    @Accessor(qualifier = "productReferences", type = Accessor.Type.SETTER)
    public void setProductReferences(Collection<ProductReferenceModel> value)
    {
        getPersistenceContext().setPropertyValue("productReferences", value);
    }


    @Accessor(qualifier = "productReviews", type = Accessor.Type.SETTER)
    public void setProductReviews(Collection<CustomerReviewModel> value)
    {
        getPersistenceContext().setPropertyValue("productReviews", value);
    }


    @Accessor(qualifier = "promotions", type = Accessor.Type.SETTER)
    public void setPromotions(Collection<ProductPromotionModel> value)
    {
        getPersistenceContext().setPropertyValue("promotions", value);
    }


    @Accessor(qualifier = "purchaseEnabled", type = Accessor.Type.SETTER)
    public void setPurchaseEnabled(boolean value)
    {
        getPersistenceContext().setPropertyValue("purchaseEnabled", toObject(value));
    }


    @Accessor(qualifier = "remarks", type = Accessor.Type.SETTER)
    public void setRemarks(String value)
    {
        setRemarks(value, null);
    }


    @Accessor(qualifier = "remarks", type = Accessor.Type.SETTER)
    public void setRemarks(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("remarks", loc, value);
    }


    @Accessor(qualifier = "restrictions", type = Accessor.Type.SETTER)
    public void setRestrictions(Collection<CMSProductRestrictionModel> value)
    {
        getPersistenceContext().setPropertyValue("restrictions", value);
    }


    @Accessor(qualifier = "reviewCount", type = Accessor.Type.SETTER)
    public void setReviewCount(Integer value)
    {
        getPersistenceContext().setPropertyValue("reviewCount", value);
    }


    @Accessor(qualifier = "reviewRating", type = Accessor.Type.SETTER)
    public void setReviewRating(Double value)
    {
        getPersistenceContext().setPropertyValue("reviewRating", value);
    }


    @Accessor(qualifier = "salesOrg", type = Accessor.Type.SETTER)
    public void setSalesOrg(SalesOrganizationModel value)
    {
        getPersistenceContext().setPropertyValue("salesOrg", value);
    }


    @Accessor(qualifier = "sapBaseUnitConversion", type = Accessor.Type.SETTER)
    public void setSapBaseUnitConversion(Double value)
    {
        getPersistenceContext().setPropertyValue("sapBaseUnitConversion", value);
    }


    @Accessor(qualifier = "sapBlocked", type = Accessor.Type.SETTER)
    public void setSapBlocked(Boolean value)
    {
        getPersistenceContext().setPropertyValue("sapBlocked", value);
    }


    @Accessor(qualifier = "sapBlockedDate", type = Accessor.Type.SETTER)
    public void setSapBlockedDate(Date value)
    {
        getPersistenceContext().setPropertyValue("sapBlockedDate", value);
    }


    @Accessor(qualifier = "sapConfigurable", type = Accessor.Type.SETTER)
    public void setSapConfigurable(Boolean value)
    {
        getPersistenceContext().setPropertyValue("sapConfigurable", value);
    }


    @Accessor(qualifier = "sapEAN", type = Accessor.Type.SETTER)
    public void setSapEAN(String value)
    {
        getPersistenceContext().setPropertyValue("sapEAN", value);
    }


    @Accessor(qualifier = "sapPlant", type = Accessor.Type.SETTER)
    public void setSapPlant(WarehouseModel value)
    {
        getPersistenceContext().setPropertyValue("sapPlant", value);
    }


    @Accessor(qualifier = "searchEnabled", type = Accessor.Type.SETTER)
    public void setSearchEnabled(boolean value)
    {
        getPersistenceContext().setPropertyValue("searchEnabled", toObject(value));
    }


    @Accessor(qualifier = "segment", type = Accessor.Type.SETTER)
    public void setSegment(String value)
    {
        setSegment(value, null);
    }


    @Accessor(qualifier = "segment", type = Accessor.Type.SETTER)
    public void setSegment(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("segment", loc, value);
    }


    @Accessor(qualifier = "sequenceId", type = Accessor.Type.SETTER)
    public void setSequenceId(Long value)
    {
        getPersistenceContext().setPropertyValue("sequenceId", value);
    }


    @Accessor(qualifier = "simpleBannerComponents", type = Accessor.Type.SETTER)
    public void setSimpleBannerComponents(List<SimpleBannerComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("simpleBannerComponents", value);
    }


    @Accessor(qualifier = "simpleResponsiveBannerComponents", type = Accessor.Type.SETTER)
    public void setSimpleResponsiveBannerComponents(List<SimpleResponsiveBannerComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("simpleResponsiveBannerComponents", value);
    }


    @Accessor(qualifier = "specialTreatmentClasses", type = Accessor.Type.SETTER)
    public void setSpecialTreatmentClasses(Map<String, String> value)
    {
        getPersistenceContext().setPropertyValue("specialTreatmentClasses", value);
    }


    @Accessor(qualifier = "startLineNumber", type = Accessor.Type.SETTER)
    public void setStartLineNumber(Integer value)
    {
        getPersistenceContext().setPropertyValue("startLineNumber", value);
    }


    @Accessor(qualifier = "stockLevels", type = Accessor.Type.SETTER)
    public void setStockLevels(Set<StockLevelModel> value)
    {
        getPersistenceContext().setPropertyValue("stockLevels", value);
    }


    @Accessor(qualifier = "summary", type = Accessor.Type.SETTER)
    public void setSummary(String value)
    {
        setSummary(value, null);
    }


    @Accessor(qualifier = "summary", type = Accessor.Type.SETTER)
    public void setSummary(String value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("summary", loc, value);
    }


    @Accessor(qualifier = "supercategories", type = Accessor.Type.SETTER)
    public void setSupercategories(Collection<CategoryModel> value)
    {
        getPersistenceContext().setPropertyValue("supercategories", value);
    }


    @Accessor(qualifier = "supplierAlternativeAID", type = Accessor.Type.SETTER)
    public void setSupplierAlternativeAID(String value)
    {
        getPersistenceContext().setPropertyValue("supplierAlternativeAID", value);
    }


    @Accessor(qualifier = "thumbnail", type = Accessor.Type.SETTER)
    public void setThumbnail(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("thumbnail", value);
    }


    @Accessor(qualifier = "thumbnails", type = Accessor.Type.SETTER)
    public void setThumbnails(Collection<MediaModel> value)
    {
        getPersistenceContext().setPropertyValue("thumbnails", value);
    }


    @Accessor(qualifier = "unit", type = Accessor.Type.SETTER)
    public void setUnit(UnitModel value)
    {
        getPersistenceContext().setPropertyValue("unit", value);
    }


    @Accessor(qualifier = "untypedFeatures", type = Accessor.Type.SETTER)
    public void setUntypedFeatures(List<ProductFeatureModel> value)
    {
        getPersistenceContext().setDynamicValue((AbstractItemModel)this, "untypedFeatures", value);
    }


    @Accessor(qualifier = "usageType", type = Accessor.Type.SETTER)
    public void setUsageType(ProductUsageType value)
    {
        getPersistenceContext().setPropertyValue("usageType", value);
    }


    @Accessor(qualifier = "variants", type = Accessor.Type.SETTER)
    public void setVariants(Collection<VariantProductModel> value)
    {
        getPersistenceContext().setPropertyValue("variants", value);
    }


    @Accessor(qualifier = "variantType", type = Accessor.Type.SETTER)
    public void setVariantType(VariantTypeModel value)
    {
        getPersistenceContext().setPropertyValue("variantType", value);
    }


    @Accessor(qualifier = "vendors", type = Accessor.Type.SETTER)
    public void setVendors(Set<VendorModel> value)
    {
        getPersistenceContext().setPropertyValue("vendors", value);
    }


    @Accessor(qualifier = "videoComponents", type = Accessor.Type.SETTER)
    public void setVideoComponents(List<VideoComponentModel> value)
    {
        getPersistenceContext().setPropertyValue("videoComponents", value);
    }


    @Accessor(qualifier = "xmlcontent", type = Accessor.Type.SETTER)
    public void setXmlcontent(String value)
    {
        getPersistenceContext().setPropertyValue("xmlcontent", value);
    }
}
