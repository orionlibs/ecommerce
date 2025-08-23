package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.classification.AttributeValueAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttribute;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeUnit;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.jalo.classification.ClassificationKeyword;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystem;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncCronJob;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncCronJobHistory;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncJob;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncScheduleMedia;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.ConfigurationCategory;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaContainer;
import de.hybris.platform.jalo.order.AbstractOrderEntry;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.product.Unit;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.order.jalo.AbstractOrderEntryProductInfo;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.variants.jalo.VariantAttributeDescriptor;
import de.hybris.platform.variants.jalo.VariantProduct;
import de.hybris.platform.variants.jalo.VariantType;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCatalogManager extends Extension
{
    protected static String SYNCJOB2TYPEREL_SRC_ORDERED = "relation.SyncJob2TypeRel.source.ordered";
    protected static String SYNCJOB2TYPEREL_TGT_ORDERED = "relation.SyncJob2TypeRel.target.ordered";
    protected static String SYNCJOB2TYPEREL_MARKMODIFIED = "relation.SyncJob2TypeRel.markmodified";
    protected static String PRODUCT2KEYWORDRELATION_SRC_ORDERED = "relation.Product2KeywordRelation.source.ordered";
    protected static String PRODUCT2KEYWORDRELATION_TGT_ORDERED = "relation.Product2KeywordRelation.target.ordered";
    protected static String PRODUCT2KEYWORDRELATION_MARKMODIFIED = "relation.Product2KeywordRelation.markmodified";
    protected static String CATEGORYPRODUCTRELATION_SRC_ORDERED = "relation.CategoryProductRelation.source.ordered";
    protected static String CATEGORYPRODUCTRELATION_TGT_ORDERED = "relation.CategoryProductRelation.target.ordered";
    protected static String CATEGORYPRODUCTRELATION_MARKMODIFIED = "relation.CategoryProductRelation.markmodified";
    protected static final OneToManyHandler<ProductFeature> PRODUCT2FEATURERELATIONFEATURESHANDLER = new OneToManyHandler(GeneratedCatalogConstants.TC.PRODUCTFEATURE, true, "product", "featurePosition", true, true, 2);
    protected static final OneToManyHandler<VariantProduct> PRODUCT2VARIANTRELATIONVARIANTSHANDLER = new OneToManyHandler(GeneratedCatalogConstants.TC.VARIANTPRODUCT, true, "baseProduct", "code", false, true, 0);
    protected static final OneToManyHandler<ProductReference> PRODUCTREFERENCERELATIONPRODUCTREFERENCESHANDLER = new OneToManyHandler(GeneratedCatalogConstants.TC.PRODUCTREFERENCE, true, "source", "sourcePOS", true, true, 0);
    protected static String CATEGORYMEDIARELATION_SRC_ORDERED = "relation.CategoryMediaRelation.source.ordered";
    protected static String CATEGORYMEDIARELATION_TGT_ORDERED = "relation.CategoryMediaRelation.target.ordered";
    protected static String CATEGORYMEDIARELATION_MARKMODIFIED = "relation.CategoryMediaRelation.markmodified";
    protected static final OneToManyHandler<AbstractOrderEntryProductInfo> ABSTRACTORDERENTRY2ABSTRACTORDERENTRYPRODUCTINFORELATIONPRODUCTINFOSHANDLER = new OneToManyHandler(GeneratedCatalogConstants.TC.ABSTRACTORDERENTRYPRODUCTINFO, true, "orderEntry", "orderEntryPOS", true, true, 2);
    protected static String CATEGORY2PRINCIPALRELATION_SRC_ORDERED = "relation.Category2PrincipalRelation.source.ordered";
    protected static String CATEGORY2PRINCIPALRELATION_TGT_ORDERED = "relation.Category2PrincipalRelation.target.ordered";
    protected static String CATEGORY2PRINCIPALRELATION_MARKMODIFIED = "relation.Category2PrincipalRelation.markmodified";
    protected static String PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_SRC_ORDERED = "relation.Principal2WriteableCatalogVersionRelation.source.ordered";
    protected static String PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_TGT_ORDERED = "relation.Principal2WriteableCatalogVersionRelation.target.ordered";
    protected static String PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_MARKMODIFIED = "relation.Principal2WriteableCatalogVersionRelation.markmodified";
    protected static String PRINCIPAL2READABLECATALOGVERSIONRELATION_SRC_ORDERED = "relation.Principal2ReadableCatalogVersionRelation.source.ordered";
    protected static String PRINCIPAL2READABLECATALOGVERSIONRELATION_TGT_ORDERED = "relation.Principal2ReadableCatalogVersionRelation.target.ordered";
    protected static String PRINCIPAL2READABLECATALOGVERSIONRELATION_MARKMODIFIED = "relation.Principal2ReadableCatalogVersionRelation.markmodified";
    protected static String SYNCITEMJOB2PRINCIPAL_SRC_ORDERED = "relation.SyncItemJob2Principal.source.ordered";
    protected static String SYNCITEMJOB2PRINCIPAL_TGT_ORDERED = "relation.SyncItemJob2Principal.target.ordered";
    protected static String SYNCITEMJOB2PRINCIPAL_MARKMODIFIED = "relation.SyncItemJob2Principal.markmodified";
    protected static String SYNCJOB2LANGREL_SRC_ORDERED = "relation.SyncJob2LangRel.source.ordered";
    protected static String SYNCJOB2LANGREL_TGT_ORDERED = "relation.SyncJob2LangRel.target.ordered";
    protected static String SYNCJOB2LANGREL_MARKMODIFIED = "relation.SyncJob2LangRel.markmodified";
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("catalogItemType", Item.AttributeMode.INITIAL);
        tmp.put("catalogVersionAttributeQualifier", Item.AttributeMode.INITIAL);
        tmp.put("uniqueKeyAttributeQualifier", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.type.ComposedType", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("remarks", Item.AttributeMode.INITIAL);
        tmp.put("publicKey", Item.AttributeMode.INITIAL);
        tmp.put("url", Item.AttributeMode.INITIAL);
        tmp.put("shippingAddress", Item.AttributeMode.INITIAL);
        tmp.put("unloadingAddress", Item.AttributeMode.INITIAL);
        tmp.put("billingAddress", Item.AttributeMode.INITIAL);
        tmp.put("contactAddress", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.user.Address", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("catalog", Item.AttributeMode.INITIAL);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        tmp.put("onlineDate", Item.AttributeMode.INITIAL);
        tmp.put("offlineDate", Item.AttributeMode.INITIAL);
        tmp.put("ean", Item.AttributeMode.INITIAL);
        tmp.put("supplierAlternativeAID", Item.AttributeMode.INITIAL);
        tmp.put("buyerIDS", Item.AttributeMode.INITIAL);
        tmp.put("manufacturerAID", Item.AttributeMode.INITIAL);
        tmp.put("manufacturerName", Item.AttributeMode.INITIAL);
        tmp.put("manufacturerTypeDescription", Item.AttributeMode.INITIAL);
        tmp.put("erpGroupBuyer", Item.AttributeMode.INITIAL);
        tmp.put("erpGroupSupplier", Item.AttributeMode.INITIAL);
        tmp.put("deliveryTime", Item.AttributeMode.INITIAL);
        tmp.put("specialTreatmentClasses", Item.AttributeMode.INITIAL);
        tmp.put("remarks", Item.AttributeMode.INITIAL);
        tmp.put("segment", Item.AttributeMode.INITIAL);
        tmp.put("order", Item.AttributeMode.INITIAL);
        tmp.put("articleStatus", Item.AttributeMode.INITIAL);
        tmp.put("approvalStatus", Item.AttributeMode.INITIAL);
        tmp.put("contentUnit", Item.AttributeMode.INITIAL);
        tmp.put("numberContentUnits", Item.AttributeMode.INITIAL);
        tmp.put("minOrderQuantity", Item.AttributeMode.INITIAL);
        tmp.put("maxOrderQuantity", Item.AttributeMode.INITIAL);
        tmp.put("orderQuantityInterval", Item.AttributeMode.INITIAL);
        tmp.put("priceQuantity", Item.AttributeMode.INITIAL);
        tmp.put("normal", Item.AttributeMode.INITIAL);
        tmp.put("thumbnails", Item.AttributeMode.INITIAL);
        tmp.put("detail", Item.AttributeMode.INITIAL);
        tmp.put("logo", Item.AttributeMode.INITIAL);
        tmp.put("data_sheet", Item.AttributeMode.INITIAL);
        tmp.put("others", Item.AttributeMode.INITIAL);
        tmp.put("startLineNumber", Item.AttributeMode.INITIAL);
        tmp.put("endLineNumber", Item.AttributeMode.INITIAL);
        tmp.put("xmlcontent", Item.AttributeMode.INITIAL);
        tmp.put("variantType", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.product.Product", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("catalog", Item.AttributeMode.INITIAL);
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.media.Media", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("catalogVersion", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.media.MediaContainer", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("previewCatalogVersions", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.user.Customer", Collections.unmodifiableMap(tmp));
        tmp = new HashMap<>();
        tmp.put("dontCopy", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.type.AttributeDescriptor", Collections.unmodifiableMap(tmp));
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public Collection<Category> getAccessibleCategories(SessionContext ctx, Principal item)
    {
        List<Category> items = item.getLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORY2PRINCIPALRELATION, "Category", null,
                        Utilities.getRelationOrderingOverride(CATEGORY2PRINCIPALRELATION_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<Category> getAccessibleCategories(Principal item)
    {
        return getAccessibleCategories(getSession().getSessionContext(), item);
    }


    public long getAccessibleCategoriesCount(SessionContext ctx, Principal item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCatalogConstants.Relations.CATEGORY2PRINCIPALRELATION, "Category", null);
    }


    public long getAccessibleCategoriesCount(Principal item)
    {
        return getAccessibleCategoriesCount(getSession().getSessionContext(), item);
    }


    public EnumerationValue getApprovalStatus(SessionContext ctx, Product item)
    {
        return (EnumerationValue)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.APPROVALSTATUS);
    }


    public EnumerationValue getApprovalStatus(Product item)
    {
        return getApprovalStatus(getSession().getSessionContext(), item);
    }


    public void setApprovalStatus(SessionContext ctx, Product item, EnumerationValue value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.APPROVALSTATUS, value);
    }


    public void setApprovalStatus(Product item, EnumerationValue value)
    {
        setApprovalStatus(getSession().getSessionContext(), item, value);
    }


    public Map<EnumerationValue, String> getArticleStatus(SessionContext ctx, Product item)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProduct.getArticleStatus requires a session language", 0);
        }
        Map<EnumerationValue, String> map = (Map<EnumerationValue, String>)item.getLocalizedProperty(ctx, GeneratedCatalogConstants.Attributes.Product.ARTICLESTATUS);
        return (map != null) ? map : Collections.EMPTY_MAP;
    }


    public Map<EnumerationValue, String> getArticleStatus(Product item)
    {
        return getArticleStatus(getSession().getSessionContext(), item);
    }


    public Map<Language, Map<EnumerationValue, String>> getAllArticleStatus(SessionContext ctx, Product item)
    {
        return item.getAllLocalizedProperties(ctx, GeneratedCatalogConstants.Attributes.Product.ARTICLESTATUS, C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, Map<EnumerationValue, String>> getAllArticleStatus(Product item)
    {
        return getAllArticleStatus(getSession().getSessionContext(), item);
    }


    public void setArticleStatus(SessionContext ctx, Product item, Map<EnumerationValue, String> value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProduct.setArticleStatus requires a session language", 0);
        }
        item.setLocalizedProperty(ctx, GeneratedCatalogConstants.Attributes.Product.ARTICLESTATUS, value);
    }


    public void setArticleStatus(Product item, Map<EnumerationValue, String> value)
    {
        setArticleStatus(getSession().getSessionContext(), item, value);
    }


    public void setAllArticleStatus(SessionContext ctx, Product item, Map<Language, Map<EnumerationValue, String>> value)
    {
        item.setAllLocalizedProperties(ctx, GeneratedCatalogConstants.Attributes.Product.ARTICLESTATUS, value);
    }


    public void setAllArticleStatus(Product item, Map<Language, Map<EnumerationValue, String>> value)
    {
        setAllArticleStatus(getSession().getSessionContext(), item, value);
    }


    public Boolean isBillingAddress(SessionContext ctx, Address item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Address.BILLINGADDRESS);
    }


    public Boolean isBillingAddress(Address item)
    {
        return isBillingAddress(getSession().getSessionContext(), item);
    }


    public boolean isBillingAddressAsPrimitive(SessionContext ctx, Address item)
    {
        Boolean value = isBillingAddress(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isBillingAddressAsPrimitive(Address item)
    {
        return isBillingAddressAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setBillingAddress(SessionContext ctx, Address item, Boolean value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Address.BILLINGADDRESS, value);
    }


    public void setBillingAddress(Address item, Boolean value)
    {
        setBillingAddress(getSession().getSessionContext(), item, value);
    }


    public void setBillingAddress(SessionContext ctx, Address item, boolean value)
    {
        setBillingAddress(ctx, item, Boolean.valueOf(value));
    }


    public void setBillingAddress(Address item, boolean value)
    {
        setBillingAddress(getSession().getSessionContext(), item, value);
    }


    public Map<EnumerationValue, String> getAllBuyerIDS(SessionContext ctx, Product item)
    {
        Map<EnumerationValue, String> map = (Map<EnumerationValue, String>)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.BUYERIDS);
        return (map != null) ? map : Collections.EMPTY_MAP;
    }


    public Map<EnumerationValue, String> getAllBuyerIDS(Product item)
    {
        return getAllBuyerIDS(getSession().getSessionContext(), item);
    }


    public void setAllBuyerIDS(SessionContext ctx, Product item, Map<EnumerationValue, String> value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.BUYERIDS, value);
    }


    public void setAllBuyerIDS(Product item, Map<EnumerationValue, String> value)
    {
        setAllBuyerIDS(getSession().getSessionContext(), item, value);
    }


    Catalog getCatalog(SessionContext ctx, Product item)
    {
        return (Catalog)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.CATALOG);
    }


    Catalog getCatalog(Product item)
    {
        return getCatalog(getSession().getSessionContext(), item);
    }


    void setCatalog(SessionContext ctx, Product item, Catalog value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.CATALOG, value);
    }


    void setCatalog(Product item, Catalog value)
    {
        setCatalog(getSession().getSessionContext(), item, value);
    }


    Catalog getCatalog(SessionContext ctx, Media item)
    {
        return (Catalog)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Media.CATALOG);
    }


    Catalog getCatalog(Media item)
    {
        return getCatalog(getSession().getSessionContext(), item);
    }


    void setCatalog(SessionContext ctx, Media item, Catalog value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Media.CATALOG, value);
    }


    void setCatalog(Media item, Catalog value)
    {
        setCatalog(getSession().getSessionContext(), item, value);
    }


    public Boolean isCatalogItemType(SessionContext ctx, ComposedType item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.ComposedType.CATALOGITEMTYPE);
    }


    public Boolean isCatalogItemType(ComposedType item)
    {
        return isCatalogItemType(getSession().getSessionContext(), item);
    }


    public boolean isCatalogItemTypeAsPrimitive(SessionContext ctx, ComposedType item)
    {
        Boolean value = isCatalogItemType(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isCatalogItemTypeAsPrimitive(ComposedType item)
    {
        return isCatalogItemTypeAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setCatalogItemType(SessionContext ctx, ComposedType item, Boolean value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.ComposedType.CATALOGITEMTYPE, value);
    }


    public void setCatalogItemType(ComposedType item, Boolean value)
    {
        setCatalogItemType(getSession().getSessionContext(), item, value);
    }


    public void setCatalogItemType(SessionContext ctx, ComposedType item, boolean value)
    {
        setCatalogItemType(ctx, item, Boolean.valueOf(value));
    }


    public void setCatalogItemType(ComposedType item, boolean value)
    {
        setCatalogItemType(getSession().getSessionContext(), item, value);
    }


    public CatalogVersion getCatalogVersion(SessionContext ctx, Product item)
    {
        return (CatalogVersion)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION);
    }


    public CatalogVersion getCatalogVersion(Product item)
    {
        return getCatalogVersion(getSession().getSessionContext(), item);
    }


    public void setCatalogVersion(SessionContext ctx, Product item, CatalogVersion value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION, value);
    }


    public void setCatalogVersion(Product item, CatalogVersion value)
    {
        setCatalogVersion(getSession().getSessionContext(), item, value);
    }


    public CatalogVersion getCatalogVersion(SessionContext ctx, Media item)
    {
        return (CatalogVersion)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Media.CATALOGVERSION);
    }


    public CatalogVersion getCatalogVersion(Media item)
    {
        return getCatalogVersion(getSession().getSessionContext(), item);
    }


    public void setCatalogVersion(SessionContext ctx, Media item, CatalogVersion value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Media.CATALOGVERSION, value);
    }


    public void setCatalogVersion(Media item, CatalogVersion value)
    {
        setCatalogVersion(getSession().getSessionContext(), item, value);
    }


    public CatalogVersion getCatalogVersion(SessionContext ctx, MediaContainer item)
    {
        return (CatalogVersion)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.MediaContainer.CATALOGVERSION);
    }


    public CatalogVersion getCatalogVersion(MediaContainer item)
    {
        return getCatalogVersion(getSession().getSessionContext(), item);
    }


    public void setCatalogVersion(SessionContext ctx, MediaContainer item, CatalogVersion value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.MediaContainer.CATALOGVERSION, value);
    }


    public void setCatalogVersion(MediaContainer item, CatalogVersion value)
    {
        setCatalogVersion(getSession().getSessionContext(), item, value);
    }


    public AttributeDescriptor getCatalogVersionAttribute(ComposedType item)
    {
        return getCatalogVersionAttribute(getSession().getSessionContext(), item);
    }


    public void setCatalogVersionAttribute(ComposedType item, AttributeDescriptor value)
    {
        setCatalogVersionAttribute(getSession().getSessionContext(), item, value);
    }


    String getCatalogVersionAttributeQualifier(SessionContext ctx, ComposedType item)
    {
        return (String)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.ComposedType.CATALOGVERSIONATTRIBUTEQUALIFIER);
    }


    String getCatalogVersionAttributeQualifier(ComposedType item)
    {
        return getCatalogVersionAttributeQualifier(getSession().getSessionContext(), item);
    }


    void setCatalogVersionAttributeQualifier(SessionContext ctx, ComposedType item, String value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.ComposedType.CATALOGVERSIONATTRIBUTEQUALIFIER, value);
    }


    void setCatalogVersionAttributeQualifier(ComposedType item, String value)
    {
        setCatalogVersionAttributeQualifier(getSession().getSessionContext(), item, value);
    }


    public String getClassificationIndexString(Product item)
    {
        return getClassificationIndexString(getSession().getSessionContext(), item);
    }


    public Map<Language, String> getAllClassificationIndexString(Product item)
    {
        return getAllClassificationIndexString(getSession().getSessionContext(), item);
    }


    public Boolean isContactAddress(SessionContext ctx, Address item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Address.CONTACTADDRESS);
    }


    public Boolean isContactAddress(Address item)
    {
        return isContactAddress(getSession().getSessionContext(), item);
    }


    public boolean isContactAddressAsPrimitive(SessionContext ctx, Address item)
    {
        Boolean value = isContactAddress(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isContactAddressAsPrimitive(Address item)
    {
        return isContactAddressAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setContactAddress(SessionContext ctx, Address item, Boolean value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Address.CONTACTADDRESS, value);
    }


    public void setContactAddress(Address item, Boolean value)
    {
        setContactAddress(getSession().getSessionContext(), item, value);
    }


    public void setContactAddress(SessionContext ctx, Address item, boolean value)
    {
        setContactAddress(ctx, item, Boolean.valueOf(value));
    }


    public void setContactAddress(Address item, boolean value)
    {
        setContactAddress(getSession().getSessionContext(), item, value);
    }


    public Unit getContentUnit(SessionContext ctx, Product item)
    {
        return (Unit)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.CONTENTUNIT);
    }


    public Unit getContentUnit(Product item)
    {
        return getContentUnit(getSession().getSessionContext(), item);
    }


    public void setContentUnit(SessionContext ctx, Product item, Unit value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.CONTENTUNIT, value);
    }


    public void setContentUnit(Product item, Unit value)
    {
        setContentUnit(getSession().getSessionContext(), item, value);
    }


    public Agreement createAgreement(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.AGREEMENT);
            return (Agreement)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Agreement : " + e.getMessage(), 0);
        }
    }


    public Agreement createAgreement(Map attributeValues)
    {
        return createAgreement(getSession().getSessionContext(), attributeValues);
    }


    public AttributeValueAssignment createAttributeValueAssignment(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.ATTRIBUTEVALUEASSIGNMENT);
            return (AttributeValueAssignment)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating AttributeValueAssignment : " + e.getMessage(), 0);
        }
    }


    public AttributeValueAssignment createAttributeValueAssignment(Map attributeValues)
    {
        return createAttributeValueAssignment(getSession().getSessionContext(), attributeValues);
    }


    public Catalog createCatalog(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CATALOG);
            return (Catalog)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Catalog : " + e.getMessage(), 0);
        }
    }


    public Catalog createCatalog(Map attributeValues)
    {
        return createCatalog(getSession().getSessionContext(), attributeValues);
    }


    public CatalogUnawareMedia createCatalogUnawareMedia(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CATALOGUNAWAREMEDIA);
            return (CatalogUnawareMedia)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CatalogUnawareMedia : " + e.getMessage(), 0);
        }
    }


    public CatalogUnawareMedia createCatalogUnawareMedia(Map attributeValues)
    {
        return createCatalogUnawareMedia(getSession().getSessionContext(), attributeValues);
    }


    public CatalogVersion createCatalogVersion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CATALOGVERSION);
            return (CatalogVersion)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CatalogVersion : " + e.getMessage(), 0);
        }
    }


    public CatalogVersion createCatalogVersion(Map attributeValues)
    {
        return createCatalogVersion(getSession().getSessionContext(), attributeValues);
    }


    public CatalogVersionSyncCronJob createCatalogVersionSyncCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CATALOGVERSIONSYNCCRONJOB);
            return (CatalogVersionSyncCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CatalogVersionSyncCronJob : " + e.getMessage(), 0);
        }
    }


    public CatalogVersionSyncCronJob createCatalogVersionSyncCronJob(Map attributeValues)
    {
        return createCatalogVersionSyncCronJob(getSession().getSessionContext(), attributeValues);
    }


    public CatalogVersionSyncCronJobHistory createCatalogVersionSyncCronJobHistory(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CATALOGVERSIONSYNCCRONJOBHISTORY);
            return (CatalogVersionSyncCronJobHistory)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CatalogVersionSyncCronJobHistory : " + e.getMessage(), 0);
        }
    }


    public CatalogVersionSyncCronJobHistory createCatalogVersionSyncCronJobHistory(Map attributeValues)
    {
        return createCatalogVersionSyncCronJobHistory(getSession().getSessionContext(), attributeValues);
    }


    public CatalogVersionSyncJob createCatalogVersionSyncJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CATALOGVERSIONSYNCJOB);
            return (CatalogVersionSyncJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CatalogVersionSyncJob : " + e.getMessage(), 0);
        }
    }


    public CatalogVersionSyncJob createCatalogVersionSyncJob(Map attributeValues)
    {
        return createCatalogVersionSyncJob(getSession().getSessionContext(), attributeValues);
    }


    public CatalogVersionSyncScheduleMedia createCatalogVersionSyncScheduleMedia(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CATALOGVERSIONSYNCSCHEDULEMEDIA);
            return (CatalogVersionSyncScheduleMedia)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CatalogVersionSyncScheduleMedia : " + e.getMessage(), 0);
        }
    }


    public CatalogVersionSyncScheduleMedia createCatalogVersionSyncScheduleMedia(Map attributeValues)
    {
        return createCatalogVersionSyncScheduleMedia(getSession().getSessionContext(), attributeValues);
    }


    public Category createCategory(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CATEGORY);
            return (Category)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Category : " + e.getMessage(), 0);
        }
    }


    public Category createCategory(Map attributeValues)
    {
        return createCategory(getSession().getSessionContext(), attributeValues);
    }


    public CategoryCatalogVersionDifference createCategoryCatalogVersionDifference(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CATEGORYCATALOGVERSIONDIFFERENCE);
            return (CategoryCatalogVersionDifference)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CategoryCatalogVersionDifference : " + e.getMessage(), 0);
        }
    }


    public CategoryCatalogVersionDifference createCategoryCatalogVersionDifference(Map attributeValues)
    {
        return createCategoryCatalogVersionDifference(getSession().getSessionContext(), attributeValues);
    }


    public ClassAttributeAssignment createClassAttributeAssignment(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CLASSATTRIBUTEASSIGNMENT);
            return (ClassAttributeAssignment)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ClassAttributeAssignment : " + e.getMessage(), 0);
        }
    }


    public ClassAttributeAssignment createClassAttributeAssignment(Map attributeValues)
    {
        return createClassAttributeAssignment(getSession().getSessionContext(), attributeValues);
    }


    public ClassificationAttribute createClassificationAttribute(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTE);
            return (ClassificationAttribute)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ClassificationAttribute : " + e.getMessage(), 0);
        }
    }


    public ClassificationAttribute createClassificationAttribute(Map attributeValues)
    {
        return createClassificationAttribute(getSession().getSessionContext(), attributeValues);
    }


    public ClassificationAttributeUnit createClassificationAttributeUnit(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTEUNIT);
            return (ClassificationAttributeUnit)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ClassificationAttributeUnit : " + e.getMessage(), 0);
        }
    }


    public ClassificationAttributeUnit createClassificationAttributeUnit(Map attributeValues)
    {
        return createClassificationAttributeUnit(getSession().getSessionContext(), attributeValues);
    }


    public ClassificationAttributeValue createClassificationAttributeValue(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTEVALUE);
            return (ClassificationAttributeValue)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ClassificationAttributeValue : " + e.getMessage(), 0);
        }
    }


    public ClassificationAttributeValue createClassificationAttributeValue(Map attributeValues)
    {
        return createClassificationAttributeValue(getSession().getSessionContext(), attributeValues);
    }


    public ClassificationClass createClassificationClass(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CLASSIFICATIONCLASS);
            return (ClassificationClass)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ClassificationClass : " + e.getMessage(), 0);
        }
    }


    public ClassificationClass createClassificationClass(Map attributeValues)
    {
        return createClassificationClass(getSession().getSessionContext(), attributeValues);
    }


    public ClassificationKeyword createClassificationKeyword(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CLASSIFICATIONKEYWORD);
            return (ClassificationKeyword)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ClassificationKeyword : " + e.getMessage(), 0);
        }
    }


    public ClassificationKeyword createClassificationKeyword(Map attributeValues)
    {
        return createClassificationKeyword(getSession().getSessionContext(), attributeValues);
    }


    public ClassificationSystem createClassificationSystem(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CLASSIFICATIONSYSTEM);
            return (ClassificationSystem)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ClassificationSystem : " + e.getMessage(), 0);
        }
    }


    public ClassificationSystem createClassificationSystem(Map attributeValues)
    {
        return createClassificationSystem(getSession().getSessionContext(), attributeValues);
    }


    public ClassificationSystemVersion createClassificationSystemVersion(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CLASSIFICATIONSYSTEMVERSION);
            return (ClassificationSystemVersion)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ClassificationSystemVersion : " + e.getMessage(), 0);
        }
    }


    public ClassificationSystemVersion createClassificationSystemVersion(Map attributeValues)
    {
        return createClassificationSystemVersion(getSession().getSessionContext(), attributeValues);
    }


    public Company createCompany(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.COMPANY);
            return (Company)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Company : " + e.getMessage(), 0);
        }
    }


    public Company createCompany(Map attributeValues)
    {
        return createCompany(getSession().getSessionContext(), attributeValues);
    }


    public CompareCatalogVersionsCronJob createCompareCatalogVersionsCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.COMPARECATALOGVERSIONSCRONJOB);
            return (CompareCatalogVersionsCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CompareCatalogVersionsCronJob : " + e.getMessage(), 0);
        }
    }


    public CompareCatalogVersionsCronJob createCompareCatalogVersionsCronJob(Map attributeValues)
    {
        return createCompareCatalogVersionsCronJob(getSession().getSessionContext(), attributeValues);
    }


    public CompareCatalogVersionsJob createCompareCatalogVersionsJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.COMPARECATALOGVERSIONSJOB);
            return (CompareCatalogVersionsJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CompareCatalogVersionsJob : " + e.getMessage(), 0);
        }
    }


    public CompareCatalogVersionsJob createCompareCatalogVersionsJob(Map attributeValues)
    {
        return createCompareCatalogVersionsJob(getSession().getSessionContext(), attributeValues);
    }


    public ConfigurationCategory createConfigurationCategory(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.CONFIGURATIONCATEGORY);
            return (ConfigurationCategory)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ConfigurationCategory : " + e.getMessage(), 0);
        }
    }


    public ConfigurationCategory createConfigurationCategory(Map attributeValues)
    {
        return createConfigurationCategory(getSession().getSessionContext(), attributeValues);
    }


    public ItemSyncDescriptor createItemSyncDescriptor(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.ITEMSYNCDESCRIPTOR);
            return (ItemSyncDescriptor)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ItemSyncDescriptor : " + e.getMessage(), 0);
        }
    }


    public ItemSyncDescriptor createItemSyncDescriptor(Map attributeValues)
    {
        return createItemSyncDescriptor(getSession().getSessionContext(), attributeValues);
    }


    public ItemSyncTimestamp createItemSyncTimestamp(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP);
            return (ItemSyncTimestamp)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ItemSyncTimestamp : " + e.getMessage(), 0);
        }
    }


    public ItemSyncTimestamp createItemSyncTimestamp(Map attributeValues)
    {
        return createItemSyncTimestamp(getSession().getSessionContext(), attributeValues);
    }


    public Keyword createKeyword(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.KEYWORD);
            return (Keyword)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating Keyword : " + e.getMessage(), 0);
        }
    }


    public Keyword createKeyword(Map attributeValues)
    {
        return createKeyword(getSession().getSessionContext(), attributeValues);
    }


    public PreviewTicket createPreviewTicket(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.PREVIEWTICKET);
            return (PreviewTicket)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating PreviewTicket : " + e.getMessage(), 0);
        }
    }


    public PreviewTicket createPreviewTicket(Map attributeValues)
    {
        return createPreviewTicket(getSession().getSessionContext(), attributeValues);
    }


    public ProductCatalogVersionDifference createProductCatalogVersionDifference(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.PRODUCTCATALOGVERSIONDIFFERENCE);
            return (ProductCatalogVersionDifference)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ProductCatalogVersionDifference : " + e.getMessage(), 0);
        }
    }


    public ProductCatalogVersionDifference createProductCatalogVersionDifference(Map attributeValues)
    {
        return createProductCatalogVersionDifference(getSession().getSessionContext(), attributeValues);
    }


    public ProductFeature createProductFeature(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.PRODUCTFEATURE);
            return (ProductFeature)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ProductFeature : " + e.getMessage(), 0);
        }
    }


    public ProductFeature createProductFeature(Map attributeValues)
    {
        return createProductFeature(getSession().getSessionContext(), attributeValues);
    }


    public ProductReference createProductReference(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.PRODUCTREFERENCE);
            return (ProductReference)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating ProductReference : " + e.getMessage(), 0);
        }
    }


    public ProductReference createProductReference(Map attributeValues)
    {
        return createProductReference(getSession().getSessionContext(), attributeValues);
    }


    public RemoveCatalogVersionCronJob createRemoveCatalogVersionCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.REMOVECATALOGVERSIONCRONJOB);
            return (RemoveCatalogVersionCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RemoveCatalogVersionCronJob : " + e.getMessage(), 0);
        }
    }


    public RemoveCatalogVersionCronJob createRemoveCatalogVersionCronJob(Map attributeValues)
    {
        return createRemoveCatalogVersionCronJob(getSession().getSessionContext(), attributeValues);
    }


    public RemoveCatalogVersionJob createRemoveCatalogVersionJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.REMOVECATALOGVERSIONJOB);
            return (RemoveCatalogVersionJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating RemoveCatalogVersionJob : " + e.getMessage(), 0);
        }
    }


    public RemoveCatalogVersionJob createRemoveCatalogVersionJob(Map attributeValues)
    {
        return createRemoveCatalogVersionJob(getSession().getSessionContext(), attributeValues);
    }


    public SyncAttributeDescriptorConfig createSyncAttributeDescriptorConfig(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.SYNCATTRIBUTEDESCRIPTORCONFIG);
            return (SyncAttributeDescriptorConfig)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SyncAttributeDescriptorConfig : " + e.getMessage(), 0);
        }
    }


    public SyncAttributeDescriptorConfig createSyncAttributeDescriptorConfig(Map attributeValues)
    {
        return createSyncAttributeDescriptorConfig(getSession().getSessionContext(), attributeValues);
    }


    public SyncItemCronJob createSyncItemCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.SYNCITEMCRONJOB);
            return (SyncItemCronJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SyncItemCronJob : " + e.getMessage(), 0);
        }
    }


    public SyncItemCronJob createSyncItemCronJob(Map attributeValues)
    {
        return createSyncItemCronJob(getSession().getSessionContext(), attributeValues);
    }


    public SyncItemJob createSyncItemJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.SYNCITEMJOB);
            return (SyncItemJob)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating SyncItemJob : " + e.getMessage(), 0);
        }
    }


    public SyncItemJob createSyncItemJob(Map attributeValues)
    {
        return createSyncItemJob(getSession().getSessionContext(), attributeValues);
    }


    public VariantAttributeDescriptor createVariantAttributeDescriptor(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.VARIANTATTRIBUTEDESCRIPTOR);
            return (VariantAttributeDescriptor)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating VariantAttributeDescriptor : " + e.getMessage(), 0);
        }
    }


    public VariantAttributeDescriptor createVariantAttributeDescriptor(Map attributeValues)
    {
        return createVariantAttributeDescriptor(getSession().getSessionContext(), attributeValues);
    }


    public VariantProduct createVariantProduct(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.VARIANTPRODUCT);
            return (VariantProduct)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating VariantProduct : " + e.getMessage(), 0);
        }
    }


    public VariantProduct createVariantProduct(Map attributeValues)
    {
        return createVariantProduct(getSession().getSessionContext(), attributeValues);
    }


    public VariantType createVariantType(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCatalogConstants.TC.VARIANTTYPE);
            return (VariantType)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating VariantType : " + e.getMessage(), 0);
        }
    }


    public VariantType createVariantType(Map attributeValues)
    {
        return createVariantType(getSession().getSessionContext(), attributeValues);
    }


    public Collection<Media> getData_sheet(SessionContext ctx, Product item)
    {
        Collection<Media> coll = (Collection<Media>)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.DATA_SHEET);
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Media> getData_sheet(Product item)
    {
        return getData_sheet(getSession().getSessionContext(), item);
    }


    public void setData_sheet(SessionContext ctx, Product item, Collection<Media> value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.DATA_SHEET, (value == null || !value.isEmpty()) ? value : null);
    }


    public void setData_sheet(Product item, Collection<Media> value)
    {
        setData_sheet(getSession().getSessionContext(), item, value);
    }


    public Double getDeliveryTime(SessionContext ctx, Product item)
    {
        return (Double)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.DELIVERYTIME);
    }


    public Double getDeliveryTime(Product item)
    {
        return getDeliveryTime(getSession().getSessionContext(), item);
    }


    public double getDeliveryTimeAsPrimitive(SessionContext ctx, Product item)
    {
        Double value = getDeliveryTime(ctx, item);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getDeliveryTimeAsPrimitive(Product item)
    {
        return getDeliveryTimeAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setDeliveryTime(SessionContext ctx, Product item, Double value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.DELIVERYTIME, value);
    }


    public void setDeliveryTime(Product item, Double value)
    {
        setDeliveryTime(getSession().getSessionContext(), item, value);
    }


    public void setDeliveryTime(SessionContext ctx, Product item, double value)
    {
        setDeliveryTime(ctx, item, Double.valueOf(value));
    }


    public void setDeliveryTime(Product item, double value)
    {
        setDeliveryTime(getSession().getSessionContext(), item, value);
    }


    public Collection<Media> getDetail(SessionContext ctx, Product item)
    {
        Collection<Media> coll = (Collection<Media>)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.DETAIL);
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Media> getDetail(Product item)
    {
        return getDetail(getSession().getSessionContext(), item);
    }


    public void setDetail(SessionContext ctx, Product item, Collection<Media> value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.DETAIL, (value == null || !value.isEmpty()) ? value : null);
    }


    public void setDetail(Product item, Collection<Media> value)
    {
        setDetail(getSession().getSessionContext(), item, value);
    }


    public Boolean isDontCopy(SessionContext ctx, AttributeDescriptor item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.AttributeDescriptor.DONTCOPY);
    }


    public Boolean isDontCopy(AttributeDescriptor item)
    {
        return isDontCopy(getSession().getSessionContext(), item);
    }


    public boolean isDontCopyAsPrimitive(SessionContext ctx, AttributeDescriptor item)
    {
        Boolean value = isDontCopy(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isDontCopyAsPrimitive(AttributeDescriptor item)
    {
        return isDontCopyAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setDontCopy(SessionContext ctx, AttributeDescriptor item, Boolean value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.AttributeDescriptor.DONTCOPY, value);
    }


    public void setDontCopy(AttributeDescriptor item, Boolean value)
    {
        setDontCopy(getSession().getSessionContext(), item, value);
    }


    public void setDontCopy(SessionContext ctx, AttributeDescriptor item, boolean value)
    {
        setDontCopy(ctx, item, Boolean.valueOf(value));
    }


    public void setDontCopy(AttributeDescriptor item, boolean value)
    {
        setDontCopy(getSession().getSessionContext(), item, value);
    }


    public String getEan(SessionContext ctx, Product item)
    {
        return (String)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.EAN);
    }


    public String getEan(Product item)
    {
        return getEan(getSession().getSessionContext(), item);
    }


    public void setEan(SessionContext ctx, Product item, String value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.EAN, value);
    }


    public void setEan(Product item, String value)
    {
        setEan(getSession().getSessionContext(), item, value);
    }


    public Integer getEndLineNumber(SessionContext ctx, Product item)
    {
        return (Integer)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.ENDLINENUMBER);
    }


    public Integer getEndLineNumber(Product item)
    {
        return getEndLineNumber(getSession().getSessionContext(), item);
    }


    public int getEndLineNumberAsPrimitive(SessionContext ctx, Product item)
    {
        Integer value = getEndLineNumber(ctx, item);
        return (value != null) ? value.intValue() : 0;
    }


    public int getEndLineNumberAsPrimitive(Product item)
    {
        return getEndLineNumberAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setEndLineNumber(SessionContext ctx, Product item, Integer value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.ENDLINENUMBER, value);
    }


    public void setEndLineNumber(Product item, Integer value)
    {
        setEndLineNumber(getSession().getSessionContext(), item, value);
    }


    public void setEndLineNumber(SessionContext ctx, Product item, int value)
    {
        setEndLineNumber(ctx, item, Integer.valueOf(value));
    }


    public void setEndLineNumber(Product item, int value)
    {
        setEndLineNumber(getSession().getSessionContext(), item, value);
    }


    public String getErpGroupBuyer(SessionContext ctx, Product item)
    {
        return (String)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.ERPGROUPBUYER);
    }


    public String getErpGroupBuyer(Product item)
    {
        return getErpGroupBuyer(getSession().getSessionContext(), item);
    }


    public void setErpGroupBuyer(SessionContext ctx, Product item, String value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.ERPGROUPBUYER, value);
    }


    public void setErpGroupBuyer(Product item, String value)
    {
        setErpGroupBuyer(getSession().getSessionContext(), item, value);
    }


    public String getErpGroupSupplier(SessionContext ctx, Product item)
    {
        return (String)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.ERPGROUPSUPPLIER);
    }


    public String getErpGroupSupplier(Product item)
    {
        return getErpGroupSupplier(getSession().getSessionContext(), item);
    }


    public void setErpGroupSupplier(SessionContext ctx, Product item, String value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.ERPGROUPSUPPLIER, value);
    }


    public void setErpGroupSupplier(Product item, String value)
    {
        setErpGroupSupplier(getSession().getSessionContext(), item, value);
    }


    public List<ProductFeature> getFeatures(SessionContext ctx, Product item)
    {
        return (List<ProductFeature>)PRODUCT2FEATURERELATIONFEATURESHANDLER.getValues(ctx, (Item)item);
    }


    public List<ProductFeature> getFeatures(Product item)
    {
        return getFeatures(getSession().getSessionContext(), item);
    }


    public void setFeatures(SessionContext ctx, Product item, List<ProductFeature> value)
    {
        PRODUCT2FEATURERELATIONFEATURESHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setFeatures(Product item, List<ProductFeature> value)
    {
        setFeatures(getSession().getSessionContext(), item, value);
    }


    public void addToFeatures(SessionContext ctx, Product item, ProductFeature value)
    {
        PRODUCT2FEATURERELATIONFEATURESHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToFeatures(Product item, ProductFeature value)
    {
        addToFeatures(getSession().getSessionContext(), item, value);
    }


    public void removeFromFeatures(SessionContext ctx, Product item, ProductFeature value)
    {
        PRODUCT2FEATURERELATIONFEATURESHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromFeatures(Product item, ProductFeature value)
    {
        removeFromFeatures(getSession().getSessionContext(), item, value);
    }


    public String getName()
    {
        return "catalog";
    }


    public List<Keyword> getKeywords(SessionContext ctx, Product item)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProduct.getKeywords requires a session language", 0);
        }
        List<Keyword> items = item.getLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.PRODUCT2KEYWORDRELATION, "Keyword", ctx
                                        .getLanguage(), false,
                        Utilities.getRelationOrderingOverride(PRODUCT2KEYWORDRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<Keyword> getKeywords(Product item)
    {
        return getKeywords(getSession().getSessionContext(), item);
    }


    public Map<Language, List<Keyword>> getAllKeywords(SessionContext ctx, Product item)
    {
        Map<Language, List<Keyword>> values = item.getAllLinkedItems(false, GeneratedCatalogConstants.Relations.PRODUCT2KEYWORDRELATION);
        return values;
    }


    public Map<Language, List<Keyword>> getAllKeywords(Product item)
    {
        return getAllKeywords(getSession().getSessionContext(), item);
    }


    public long getKeywordsCount(SessionContext ctx, Product item, Language lang)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCatalogConstants.Relations.PRODUCT2KEYWORDRELATION, "Keyword", lang);
    }


    public long getKeywordsCount(Product item, Language lang)
    {
        return getKeywordsCount(getSession().getSessionContext(), item, lang);
    }


    public long getKeywordsCount(SessionContext ctx, Product item)
    {
        return getKeywordsCount(ctx, item, ctx.getLanguage());
    }


    public long getKeywordsCount(Product item)
    {
        return getKeywordsCount(getSession().getSessionContext(), item, getSession().getSessionContext().getLanguage());
    }


    public void setKeywords(SessionContext ctx, Product item, List<Keyword> value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProduct.setKeywords requires a session language", 0);
        }
        item.setLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.PRODUCT2KEYWORDRELATION, ctx
                                        .getLanguage(), value, false,
                        Utilities.getRelationOrderingOverride(PRODUCT2KEYWORDRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRODUCT2KEYWORDRELATION_MARKMODIFIED));
    }


    public void setKeywords(Product item, List<Keyword> value)
    {
        setKeywords(getSession().getSessionContext(), item, value);
    }


    public void setAllKeywords(SessionContext ctx, Product item, Map<Language, List<Keyword>> value)
    {
        item.setAllLinkedItems(
                        getAllValuesSessionContext(ctx), false, GeneratedCatalogConstants.Relations.PRODUCT2KEYWORDRELATION, value);
    }


    public void setAllKeywords(Product item, Map<Language, List<Keyword>> value)
    {
        setAllKeywords(getSession().getSessionContext(), item, value);
    }


    public void addToKeywords(SessionContext ctx, Product item, Language lang, Keyword value)
    {
        if(lang == null)
        {
            throw new JaloInvalidParameterException("GeneratedProduct.addToKeywords requires a language language", 0);
        }
        item.addLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.PRODUCT2KEYWORDRELATION, lang,
                        Collections.singletonList(value), false,
                        Utilities.getRelationOrderingOverride(PRODUCT2KEYWORDRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRODUCT2KEYWORDRELATION_MARKMODIFIED));
    }


    public void addToKeywords(Product item, Language lang, Keyword value)
    {
        addToKeywords(getSession().getSessionContext(), item, lang, value);
    }


    public void removeFromKeywords(SessionContext ctx, Product item, Language lang, Keyword value)
    {
        if(lang == null)
        {
            throw new JaloInvalidParameterException("GeneratedProduct.removeFromKeywords requires a session language", 0);
        }
        item.removeLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.PRODUCT2KEYWORDRELATION, lang,
                        Collections.singletonList(value), false,
                        Utilities.getRelationOrderingOverride(PRODUCT2KEYWORDRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRODUCT2KEYWORDRELATION_MARKMODIFIED));
    }


    public void removeFromKeywords(Product item, Language lang, Keyword value)
    {
        removeFromKeywords(getSession().getSessionContext(), item, lang, value);
    }


    public Collection<Media> getLogo(SessionContext ctx, Product item)
    {
        Collection<Media> coll = (Collection<Media>)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.LOGO);
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Media> getLogo(Product item)
    {
        return getLogo(getSession().getSessionContext(), item);
    }


    public void setLogo(SessionContext ctx, Product item, Collection<Media> value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.LOGO, (value == null || !value.isEmpty()) ? value : null);
    }


    public void setLogo(Product item, Collection<Media> value)
    {
        setLogo(getSession().getSessionContext(), item, value);
    }


    public String getManufacturerAID(SessionContext ctx, Product item)
    {
        return (String)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.MANUFACTURERAID);
    }


    public String getManufacturerAID(Product item)
    {
        return getManufacturerAID(getSession().getSessionContext(), item);
    }


    public void setManufacturerAID(SessionContext ctx, Product item, String value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.MANUFACTURERAID, value);
    }


    public void setManufacturerAID(Product item, String value)
    {
        setManufacturerAID(getSession().getSessionContext(), item, value);
    }


    public String getManufacturerName(SessionContext ctx, Product item)
    {
        return (String)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.MANUFACTURERNAME);
    }


    public String getManufacturerName(Product item)
    {
        return getManufacturerName(getSession().getSessionContext(), item);
    }


    public void setManufacturerName(SessionContext ctx, Product item, String value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.MANUFACTURERNAME, value);
    }


    public void setManufacturerName(Product item, String value)
    {
        setManufacturerName(getSession().getSessionContext(), item, value);
    }


    public String getManufacturerTypeDescription(SessionContext ctx, Product item)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProduct.getManufacturerTypeDescription requires a session language", 0);
        }
        return (String)item.getLocalizedProperty(ctx, GeneratedCatalogConstants.Attributes.Product.MANUFACTURERTYPEDESCRIPTION);
    }


    public String getManufacturerTypeDescription(Product item)
    {
        return getManufacturerTypeDescription(getSession().getSessionContext(), item);
    }


    public Map<Language, String> getAllManufacturerTypeDescription(SessionContext ctx, Product item)
    {
        return item.getAllLocalizedProperties(ctx, GeneratedCatalogConstants.Attributes.Product.MANUFACTURERTYPEDESCRIPTION, C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllManufacturerTypeDescription(Product item)
    {
        return getAllManufacturerTypeDescription(getSession().getSessionContext(), item);
    }


    public void setManufacturerTypeDescription(SessionContext ctx, Product item, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProduct.setManufacturerTypeDescription requires a session language", 0);
        }
        item.setLocalizedProperty(ctx, GeneratedCatalogConstants.Attributes.Product.MANUFACTURERTYPEDESCRIPTION, value);
    }


    public void setManufacturerTypeDescription(Product item, String value)
    {
        setManufacturerTypeDescription(getSession().getSessionContext(), item, value);
    }


    public void setAllManufacturerTypeDescription(SessionContext ctx, Product item, Map<Language, String> value)
    {
        item.setAllLocalizedProperties(ctx, GeneratedCatalogConstants.Attributes.Product.MANUFACTURERTYPEDESCRIPTION, value);
    }


    public void setAllManufacturerTypeDescription(Product item, Map<Language, String> value)
    {
        setAllManufacturerTypeDescription(getSession().getSessionContext(), item, value);
    }


    public Integer getMaxOrderQuantity(SessionContext ctx, Product item)
    {
        return (Integer)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.MAXORDERQUANTITY);
    }


    public Integer getMaxOrderQuantity(Product item)
    {
        return getMaxOrderQuantity(getSession().getSessionContext(), item);
    }


    public int getMaxOrderQuantityAsPrimitive(SessionContext ctx, Product item)
    {
        Integer value = getMaxOrderQuantity(ctx, item);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxOrderQuantityAsPrimitive(Product item)
    {
        return getMaxOrderQuantityAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setMaxOrderQuantity(SessionContext ctx, Product item, Integer value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.MAXORDERQUANTITY, value);
    }


    public void setMaxOrderQuantity(Product item, Integer value)
    {
        setMaxOrderQuantity(getSession().getSessionContext(), item, value);
    }


    public void setMaxOrderQuantity(SessionContext ctx, Product item, int value)
    {
        setMaxOrderQuantity(ctx, item, Integer.valueOf(value));
    }


    public void setMaxOrderQuantity(Product item, int value)
    {
        setMaxOrderQuantity(getSession().getSessionContext(), item, value);
    }


    public Integer getMinOrderQuantity(SessionContext ctx, Product item)
    {
        return (Integer)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.MINORDERQUANTITY);
    }


    public Integer getMinOrderQuantity(Product item)
    {
        return getMinOrderQuantity(getSession().getSessionContext(), item);
    }


    public int getMinOrderQuantityAsPrimitive(SessionContext ctx, Product item)
    {
        Integer value = getMinOrderQuantity(ctx, item);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMinOrderQuantityAsPrimitive(Product item)
    {
        return getMinOrderQuantityAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setMinOrderQuantity(SessionContext ctx, Product item, Integer value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.MINORDERQUANTITY, value);
    }


    public void setMinOrderQuantity(Product item, Integer value)
    {
        setMinOrderQuantity(getSession().getSessionContext(), item, value);
    }


    public void setMinOrderQuantity(SessionContext ctx, Product item, int value)
    {
        setMinOrderQuantity(ctx, item, Integer.valueOf(value));
    }


    public void setMinOrderQuantity(Product item, int value)
    {
        setMinOrderQuantity(getSession().getSessionContext(), item, value);
    }


    public Collection<Media> getNormal(SessionContext ctx, Product item)
    {
        Collection<Media> coll = (Collection<Media>)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.NORMAL);
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Media> getNormal(Product item)
    {
        return getNormal(getSession().getSessionContext(), item);
    }


    public void setNormal(SessionContext ctx, Product item, Collection<Media> value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.NORMAL, (value == null || !value.isEmpty()) ? value : null);
    }


    public void setNormal(Product item, Collection<Media> value)
    {
        setNormal(getSession().getSessionContext(), item, value);
    }


    public Double getNumberContentUnits(SessionContext ctx, Product item)
    {
        return (Double)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.NUMBERCONTENTUNITS);
    }


    public Double getNumberContentUnits(Product item)
    {
        return getNumberContentUnits(getSession().getSessionContext(), item);
    }


    public double getNumberContentUnitsAsPrimitive(SessionContext ctx, Product item)
    {
        Double value = getNumberContentUnits(ctx, item);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getNumberContentUnitsAsPrimitive(Product item)
    {
        return getNumberContentUnitsAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setNumberContentUnits(SessionContext ctx, Product item, Double value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.NUMBERCONTENTUNITS, value);
    }


    public void setNumberContentUnits(Product item, Double value)
    {
        setNumberContentUnits(getSession().getSessionContext(), item, value);
    }


    public void setNumberContentUnits(SessionContext ctx, Product item, double value)
    {
        setNumberContentUnits(ctx, item, Double.valueOf(value));
    }


    public void setNumberContentUnits(Product item, double value)
    {
        setNumberContentUnits(getSession().getSessionContext(), item, value);
    }


    public Date getOfflineDate(SessionContext ctx, Product item)
    {
        return (Date)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.OFFLINEDATE);
    }


    public Date getOfflineDate(Product item)
    {
        return getOfflineDate(getSession().getSessionContext(), item);
    }


    public void setOfflineDate(SessionContext ctx, Product item, Date value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.OFFLINEDATE, value);
    }


    public void setOfflineDate(Product item, Date value)
    {
        setOfflineDate(getSession().getSessionContext(), item, value);
    }


    public Date getOnlineDate(SessionContext ctx, Product item)
    {
        return (Date)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.ONLINEDATE);
    }


    public Date getOnlineDate(Product item)
    {
        return getOnlineDate(getSession().getSessionContext(), item);
    }


    public void setOnlineDate(SessionContext ctx, Product item, Date value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.ONLINEDATE, value);
    }


    public void setOnlineDate(Product item, Date value)
    {
        setOnlineDate(getSession().getSessionContext(), item, value);
    }


    public Integer getOrder(SessionContext ctx, Product item)
    {
        return (Integer)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.ORDER);
    }


    public Integer getOrder(Product item)
    {
        return getOrder(getSession().getSessionContext(), item);
    }


    public int getOrderAsPrimitive(SessionContext ctx, Product item)
    {
        Integer value = getOrder(ctx, item);
        return (value != null) ? value.intValue() : 0;
    }


    public int getOrderAsPrimitive(Product item)
    {
        return getOrderAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setOrder(SessionContext ctx, Product item, Integer value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.ORDER, value);
    }


    public void setOrder(Product item, Integer value)
    {
        setOrder(getSession().getSessionContext(), item, value);
    }


    public void setOrder(SessionContext ctx, Product item, int value)
    {
        setOrder(ctx, item, Integer.valueOf(value));
    }


    public void setOrder(Product item, int value)
    {
        setOrder(getSession().getSessionContext(), item, value);
    }


    public Integer getOrderQuantityInterval(SessionContext ctx, Product item)
    {
        return (Integer)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.ORDERQUANTITYINTERVAL);
    }


    public Integer getOrderQuantityInterval(Product item)
    {
        return getOrderQuantityInterval(getSession().getSessionContext(), item);
    }


    public int getOrderQuantityIntervalAsPrimitive(SessionContext ctx, Product item)
    {
        Integer value = getOrderQuantityInterval(ctx, item);
        return (value != null) ? value.intValue() : 0;
    }


    public int getOrderQuantityIntervalAsPrimitive(Product item)
    {
        return getOrderQuantityIntervalAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setOrderQuantityInterval(SessionContext ctx, Product item, Integer value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.ORDERQUANTITYINTERVAL, value);
    }


    public void setOrderQuantityInterval(Product item, Integer value)
    {
        setOrderQuantityInterval(getSession().getSessionContext(), item, value);
    }


    public void setOrderQuantityInterval(SessionContext ctx, Product item, int value)
    {
        setOrderQuantityInterval(ctx, item, Integer.valueOf(value));
    }


    public void setOrderQuantityInterval(Product item, int value)
    {
        setOrderQuantityInterval(getSession().getSessionContext(), item, value);
    }


    public Collection<Media> getOthers(SessionContext ctx, Product item)
    {
        Collection<Media> coll = (Collection<Media>)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.OTHERS);
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Media> getOthers(Product item)
    {
        return getOthers(getSession().getSessionContext(), item);
    }


    public void setOthers(SessionContext ctx, Product item, Collection<Media> value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.OTHERS, (value == null || !value.isEmpty()) ? value : null);
    }


    public void setOthers(Product item, Collection<Media> value)
    {
        setOthers(getSession().getSessionContext(), item, value);
    }


    public Collection<CatalogVersion> getPreviewCatalogVersions(SessionContext ctx, Customer item)
    {
        Collection<CatalogVersion> coll = (Collection<CatalogVersion>)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Customer.PREVIEWCATALOGVERSIONS);
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<CatalogVersion> getPreviewCatalogVersions(Customer item)
    {
        return getPreviewCatalogVersions(getSession().getSessionContext(), item);
    }


    public void setPreviewCatalogVersions(SessionContext ctx, Customer item, Collection<CatalogVersion> value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Customer.PREVIEWCATALOGVERSIONS, (value == null || !value.isEmpty()) ? value : null);
    }


    public void setPreviewCatalogVersions(Customer item, Collection<CatalogVersion> value)
    {
        setPreviewCatalogVersions(getSession().getSessionContext(), item, value);
    }


    public Double getPriceQuantity(SessionContext ctx, Product item)
    {
        return (Double)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.PRICEQUANTITY);
    }


    public Double getPriceQuantity(Product item)
    {
        return getPriceQuantity(getSession().getSessionContext(), item);
    }


    public double getPriceQuantityAsPrimitive(SessionContext ctx, Product item)
    {
        Double value = getPriceQuantity(ctx, item);
        return (value != null) ? value.doubleValue() : 0.0D;
    }


    public double getPriceQuantityAsPrimitive(Product item)
    {
        return getPriceQuantityAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setPriceQuantity(SessionContext ctx, Product item, Double value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.PRICEQUANTITY, value);
    }


    public void setPriceQuantity(Product item, Double value)
    {
        setPriceQuantity(getSession().getSessionContext(), item, value);
    }


    public void setPriceQuantity(SessionContext ctx, Product item, double value)
    {
        setPriceQuantity(ctx, item, Double.valueOf(value));
    }


    public void setPriceQuantity(Product item, double value)
    {
        setPriceQuantity(getSession().getSessionContext(), item, value);
    }


    public List<AbstractOrderEntryProductInfo> getProductInfos(SessionContext ctx, AbstractOrderEntry item)
    {
        return (List<AbstractOrderEntryProductInfo>)ABSTRACTORDERENTRY2ABSTRACTORDERENTRYPRODUCTINFORELATIONPRODUCTINFOSHANDLER.getValues(ctx, (Item)item);
    }


    public List<AbstractOrderEntryProductInfo> getProductInfos(AbstractOrderEntry item)
    {
        return getProductInfos(getSession().getSessionContext(), item);
    }


    public void setProductInfos(SessionContext ctx, AbstractOrderEntry item, List<AbstractOrderEntryProductInfo> value)
    {
        ABSTRACTORDERENTRY2ABSTRACTORDERENTRYPRODUCTINFORELATIONPRODUCTINFOSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setProductInfos(AbstractOrderEntry item, List<AbstractOrderEntryProductInfo> value)
    {
        setProductInfos(getSession().getSessionContext(), item, value);
    }


    public void addToProductInfos(SessionContext ctx, AbstractOrderEntry item, AbstractOrderEntryProductInfo value)
    {
        ABSTRACTORDERENTRY2ABSTRACTORDERENTRYPRODUCTINFORELATIONPRODUCTINFOSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToProductInfos(AbstractOrderEntry item, AbstractOrderEntryProductInfo value)
    {
        addToProductInfos(getSession().getSessionContext(), item, value);
    }


    public void removeFromProductInfos(SessionContext ctx, AbstractOrderEntry item, AbstractOrderEntryProductInfo value)
    {
        ABSTRACTORDERENTRY2ABSTRACTORDERENTRYPRODUCTINFORELATIONPRODUCTINFOSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromProductInfos(AbstractOrderEntry item, AbstractOrderEntryProductInfo value)
    {
        removeFromProductInfos(getSession().getSessionContext(), item, value);
    }


    public Collection<ProductReference> getProductReferences(SessionContext ctx, Product item)
    {
        return PRODUCTREFERENCERELATIONPRODUCTREFERENCESHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<ProductReference> getProductReferences(Product item)
    {
        return getProductReferences(getSession().getSessionContext(), item);
    }


    public void setProductReferences(SessionContext ctx, Product item, Collection<ProductReference> value)
    {
        PRODUCTREFERENCERELATIONPRODUCTREFERENCESHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setProductReferences(Product item, Collection<ProductReference> value)
    {
        setProductReferences(getSession().getSessionContext(), item, value);
    }


    public void addToProductReferences(SessionContext ctx, Product item, ProductReference value)
    {
        PRODUCTREFERENCERELATIONPRODUCTREFERENCESHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToProductReferences(Product item, ProductReference value)
    {
        addToProductReferences(getSession().getSessionContext(), item, value);
    }


    public void removeFromProductReferences(SessionContext ctx, Product item, ProductReference value)
    {
        PRODUCTREFERENCERELATIONPRODUCTREFERENCESHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromProductReferences(Product item, ProductReference value)
    {
        removeFromProductReferences(getSession().getSessionContext(), item, value);
    }


    public String getPublicKey(SessionContext ctx, Address item)
    {
        return (String)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Address.PUBLICKEY);
    }


    public String getPublicKey(Address item)
    {
        return getPublicKey(getSession().getSessionContext(), item);
    }


    public void setPublicKey(SessionContext ctx, Address item, String value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Address.PUBLICKEY, value);
    }


    public void setPublicKey(Address item, String value)
    {
        setPublicKey(getSession().getSessionContext(), item, value);
    }


    public List<CatalogVersion> getReadableCatalogVersions(SessionContext ctx, Principal item)
    {
        List<CatalogVersion> items = item.getLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.PRINCIPAL2READABLECATALOGVERSIONRELATION, "CatalogVersion", null,
                        Utilities.getRelationOrderingOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<CatalogVersion> getReadableCatalogVersions(Principal item)
    {
        return getReadableCatalogVersions(getSession().getSessionContext(), item);
    }


    public long getReadableCatalogVersionsCount(SessionContext ctx, Principal item)
    {
        return item.getLinkedItemsCount(ctx, true, GeneratedCatalogConstants.Relations.PRINCIPAL2READABLECATALOGVERSIONRELATION, "CatalogVersion", null);
    }


    public long getReadableCatalogVersionsCount(Principal item)
    {
        return getReadableCatalogVersionsCount(getSession().getSessionContext(), item);
    }


    public void setReadableCatalogVersions(SessionContext ctx, Principal item, List<CatalogVersion> value)
    {
        item.setLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.PRINCIPAL2READABLECATALOGVERSIONRELATION, null, value,
                        Utilities.getRelationOrderingOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void setReadableCatalogVersions(Principal item, List<CatalogVersion> value)
    {
        setReadableCatalogVersions(getSession().getSessionContext(), item, value);
    }


    public void addToReadableCatalogVersions(SessionContext ctx, Principal item, CatalogVersion value)
    {
        item.addLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.PRINCIPAL2READABLECATALOGVERSIONRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void addToReadableCatalogVersions(Principal item, CatalogVersion value)
    {
        addToReadableCatalogVersions(getSession().getSessionContext(), item, value);
    }


    public void removeFromReadableCatalogVersions(SessionContext ctx, Principal item, CatalogVersion value)
    {
        item.removeLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.PRINCIPAL2READABLECATALOGVERSIONRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRINCIPAL2READABLECATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void removeFromReadableCatalogVersions(Principal item, CatalogVersion value)
    {
        removeFromReadableCatalogVersions(getSession().getSessionContext(), item, value);
    }


    public String getRemarks(SessionContext ctx, Address item)
    {
        return (String)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Address.REMARKS);
    }


    public String getRemarks(Address item)
    {
        return getRemarks(getSession().getSessionContext(), item);
    }


    public void setRemarks(SessionContext ctx, Address item, String value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Address.REMARKS, value);
    }


    public void setRemarks(Address item, String value)
    {
        setRemarks(getSession().getSessionContext(), item, value);
    }


    public String getRemarks(SessionContext ctx, Product item)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProduct.getRemarks requires a session language", 0);
        }
        return (String)item.getLocalizedProperty(ctx, GeneratedCatalogConstants.Attributes.Product.REMARKS);
    }


    public String getRemarks(Product item)
    {
        return getRemarks(getSession().getSessionContext(), item);
    }


    public Map<Language, String> getAllRemarks(SessionContext ctx, Product item)
    {
        return item.getAllLocalizedProperties(ctx, GeneratedCatalogConstants.Attributes.Product.REMARKS, C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllRemarks(Product item)
    {
        return getAllRemarks(getSession().getSessionContext(), item);
    }


    public void setRemarks(SessionContext ctx, Product item, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProduct.setRemarks requires a session language", 0);
        }
        item.setLocalizedProperty(ctx, GeneratedCatalogConstants.Attributes.Product.REMARKS, value);
    }


    public void setRemarks(Product item, String value)
    {
        setRemarks(getSession().getSessionContext(), item, value);
    }


    public void setAllRemarks(SessionContext ctx, Product item, Map<Language, String> value)
    {
        item.setAllLocalizedProperties(ctx, GeneratedCatalogConstants.Attributes.Product.REMARKS, value);
    }


    public void setAllRemarks(Product item, Map<Language, String> value)
    {
        setAllRemarks(getSession().getSessionContext(), item, value);
    }


    public String getSegment(SessionContext ctx, Product item)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProduct.getSegment requires a session language", 0);
        }
        return (String)item.getLocalizedProperty(ctx, GeneratedCatalogConstants.Attributes.Product.SEGMENT);
    }


    public String getSegment(Product item)
    {
        return getSegment(getSession().getSessionContext(), item);
    }


    public Map<Language, String> getAllSegment(SessionContext ctx, Product item)
    {
        return item.getAllLocalizedProperties(ctx, GeneratedCatalogConstants.Attributes.Product.SEGMENT, C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllSegment(Product item)
    {
        return getAllSegment(getSession().getSessionContext(), item);
    }


    public void setSegment(SessionContext ctx, Product item, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedProduct.setSegment requires a session language", 0);
        }
        item.setLocalizedProperty(ctx, GeneratedCatalogConstants.Attributes.Product.SEGMENT, value);
    }


    public void setSegment(Product item, String value)
    {
        setSegment(getSession().getSessionContext(), item, value);
    }


    public void setAllSegment(SessionContext ctx, Product item, Map<Language, String> value)
    {
        item.setAllLocalizedProperties(ctx, GeneratedCatalogConstants.Attributes.Product.SEGMENT, value);
    }


    public void setAllSegment(Product item, Map<Language, String> value)
    {
        setAllSegment(getSession().getSessionContext(), item, value);
    }


    public Boolean isShippingAddress(SessionContext ctx, Address item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Address.SHIPPINGADDRESS);
    }


    public Boolean isShippingAddress(Address item)
    {
        return isShippingAddress(getSession().getSessionContext(), item);
    }


    public boolean isShippingAddressAsPrimitive(SessionContext ctx, Address item)
    {
        Boolean value = isShippingAddress(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isShippingAddressAsPrimitive(Address item)
    {
        return isShippingAddressAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setShippingAddress(SessionContext ctx, Address item, Boolean value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Address.SHIPPINGADDRESS, value);
    }


    public void setShippingAddress(Address item, Boolean value)
    {
        setShippingAddress(getSession().getSessionContext(), item, value);
    }


    public void setShippingAddress(SessionContext ctx, Address item, boolean value)
    {
        setShippingAddress(ctx, item, Boolean.valueOf(value));
    }


    public void setShippingAddress(Address item, boolean value)
    {
        setShippingAddress(getSession().getSessionContext(), item, value);
    }


    public Map<String, String> getAllSpecialTreatmentClasses(SessionContext ctx, Product item)
    {
        Map<String, String> map = (Map<String, String>)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.SPECIALTREATMENTCLASSES);
        return (map != null) ? map : Collections.EMPTY_MAP;
    }


    public Map<String, String> getAllSpecialTreatmentClasses(Product item)
    {
        return getAllSpecialTreatmentClasses(getSession().getSessionContext(), item);
    }


    public void setAllSpecialTreatmentClasses(SessionContext ctx, Product item, Map<String, String> value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.SPECIALTREATMENTCLASSES, value);
    }


    public void setAllSpecialTreatmentClasses(Product item, Map<String, String> value)
    {
        setAllSpecialTreatmentClasses(getSession().getSessionContext(), item, value);
    }


    public Integer getStartLineNumber(SessionContext ctx, Product item)
    {
        return (Integer)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.STARTLINENUMBER);
    }


    public Integer getStartLineNumber(Product item)
    {
        return getStartLineNumber(getSession().getSessionContext(), item);
    }


    public int getStartLineNumberAsPrimitive(SessionContext ctx, Product item)
    {
        Integer value = getStartLineNumber(ctx, item);
        return (value != null) ? value.intValue() : 0;
    }


    public int getStartLineNumberAsPrimitive(Product item)
    {
        return getStartLineNumberAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setStartLineNumber(SessionContext ctx, Product item, Integer value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.STARTLINENUMBER, value);
    }


    public void setStartLineNumber(Product item, Integer value)
    {
        setStartLineNumber(getSession().getSessionContext(), item, value);
    }


    public void setStartLineNumber(SessionContext ctx, Product item, int value)
    {
        setStartLineNumber(ctx, item, Integer.valueOf(value));
    }


    public void setStartLineNumber(Product item, int value)
    {
        setStartLineNumber(getSession().getSessionContext(), item, value);
    }


    public Collection<Category> getSupercategories(SessionContext ctx, Product item)
    {
        List<Category> items = item.getLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION, "Category", null,
                        Utilities.getRelationOrderingOverride(CATEGORYPRODUCTRELATION_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<Category> getSupercategories(Product item)
    {
        return getSupercategories(getSession().getSessionContext(), item);
    }


    public long getSupercategoriesCount(SessionContext ctx, Product item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION, "Category", null);
    }


    public long getSupercategoriesCount(Product item)
    {
        return getSupercategoriesCount(getSession().getSessionContext(), item);
    }


    public void setSupercategories(SessionContext ctx, Product item, Collection<Category> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION, null, value,
                        Utilities.getRelationOrderingOverride(CATEGORYPRODUCTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORYPRODUCTRELATION_MARKMODIFIED));
    }


    public void setSupercategories(Product item, Collection<Category> value)
    {
        setSupercategories(getSession().getSessionContext(), item, value);
    }


    public void addToSupercategories(SessionContext ctx, Product item, Category value)
    {
        item.addLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORYPRODUCTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORYPRODUCTRELATION_MARKMODIFIED));
    }


    public void addToSupercategories(Product item, Category value)
    {
        addToSupercategories(getSession().getSessionContext(), item, value);
    }


    public void removeFromSupercategories(SessionContext ctx, Product item, Category value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORYPRODUCTRELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORYPRODUCTRELATION_MARKMODIFIED));
    }


    public void removeFromSupercategories(Product item, Category value)
    {
        removeFromSupercategories(getSession().getSessionContext(), item, value);
    }


    public Collection<Category> getSupercategories(SessionContext ctx, Media item)
    {
        List<Category> items = item.getLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYMEDIARELATION, "Category", null,
                        Utilities.getRelationOrderingOverride(CATEGORYMEDIARELATION_SRC_ORDERED, true), false);
        return items;
    }


    public Collection<Category> getSupercategories(Media item)
    {
        return getSupercategories(getSession().getSessionContext(), item);
    }


    public long getSupercategoriesCount(SessionContext ctx, Media item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYMEDIARELATION, "Category", null);
    }


    public long getSupercategoriesCount(Media item)
    {
        return getSupercategoriesCount(getSession().getSessionContext(), item);
    }


    public void setSupercategories(SessionContext ctx, Media item, Collection<Category> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYMEDIARELATION, null, value,
                        Utilities.getRelationOrderingOverride(CATEGORYMEDIARELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORYMEDIARELATION_MARKMODIFIED));
    }


    public void setSupercategories(Media item, Collection<Category> value)
    {
        setSupercategories(getSession().getSessionContext(), item, value);
    }


    public void addToSupercategories(SessionContext ctx, Media item, Category value)
    {
        item.addLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYMEDIARELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORYMEDIARELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORYMEDIARELATION_MARKMODIFIED));
    }


    public void addToSupercategories(Media item, Category value)
    {
        addToSupercategories(getSession().getSessionContext(), item, value);
    }


    public void removeFromSupercategories(SessionContext ctx, Media item, Category value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.CATEGORYMEDIARELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(CATEGORYMEDIARELATION_SRC_ORDERED, true), false,
                        Utilities.getMarkModifiedOverride(CATEGORYMEDIARELATION_MARKMODIFIED));
    }


    public void removeFromSupercategories(Media item, Category value)
    {
        removeFromSupercategories(getSession().getSessionContext(), item, value);
    }


    public String getSupplierAlternativeAID(SessionContext ctx, Product item)
    {
        return (String)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.SUPPLIERALTERNATIVEAID);
    }


    public String getSupplierAlternativeAID(Product item)
    {
        return getSupplierAlternativeAID(getSession().getSessionContext(), item);
    }


    public void setSupplierAlternativeAID(SessionContext ctx, Product item, String value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.SUPPLIERALTERNATIVEAID, value);
    }


    public void setSupplierAlternativeAID(Product item, String value)
    {
        setSupplierAlternativeAID(getSession().getSessionContext(), item, value);
    }


    public List<ItemSyncTimestamp> getSynchronizationSources(Item item)
    {
        return getSynchronizationSources(getSession().getSessionContext(), item);
    }


    public List<ItemSyncTimestamp> getSynchronizedCopies(Item item)
    {
        return getSynchronizedCopies(getSession().getSessionContext(), item);
    }


    Collection<SyncItemJob> getSyncJobs(SessionContext ctx, ComposedType item)
    {
        List<SyncItemJob> items = item.getLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.SYNCJOB2TYPEREL, "SyncItemJob", null,
                        Utilities.getRelationOrderingOverride(SYNCJOB2TYPEREL_SRC_ORDERED, true), false);
        return items;
    }


    Collection<SyncItemJob> getSyncJobs(ComposedType item)
    {
        return getSyncJobs(getSession().getSessionContext(), item);
    }


    long getSyncJobsCount(SessionContext ctx, ComposedType item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCatalogConstants.Relations.SYNCJOB2TYPEREL, "SyncItemJob", null);
    }


    long getSyncJobsCount(ComposedType item)
    {
        return getSyncJobsCount(getSession().getSessionContext(), item);
    }


    public Collection<SyncItemJob> getSyncJobs(SessionContext ctx, Principal item)
    {
        List<SyncItemJob> items = item.getLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.SYNCITEMJOB2PRINCIPAL, "SyncItemJob", null,
                        Utilities.getRelationOrderingOverride(SYNCITEMJOB2PRINCIPAL_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SYNCITEMJOB2PRINCIPAL_TGT_ORDERED, true));
        return items;
    }


    public Collection<SyncItemJob> getSyncJobs(Principal item)
    {
        return getSyncJobs(getSession().getSessionContext(), item);
    }


    public long getSyncJobsCount(SessionContext ctx, Principal item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCatalogConstants.Relations.SYNCITEMJOB2PRINCIPAL, "SyncItemJob", null);
    }


    public long getSyncJobsCount(Principal item)
    {
        return getSyncJobsCount(getSession().getSessionContext(), item);
    }


    public void setSyncJobs(SessionContext ctx, Principal item, Collection<SyncItemJob> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.SYNCITEMJOB2PRINCIPAL, null, value,
                        Utilities.getRelationOrderingOverride(SYNCITEMJOB2PRINCIPAL_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SYNCITEMJOB2PRINCIPAL_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SYNCITEMJOB2PRINCIPAL_MARKMODIFIED));
    }


    public void setSyncJobs(Principal item, Collection<SyncItemJob> value)
    {
        setSyncJobs(getSession().getSessionContext(), item, value);
    }


    public void addToSyncJobs(SessionContext ctx, Principal item, SyncItemJob value)
    {
        item.addLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.SYNCITEMJOB2PRINCIPAL, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SYNCITEMJOB2PRINCIPAL_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SYNCITEMJOB2PRINCIPAL_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SYNCITEMJOB2PRINCIPAL_MARKMODIFIED));
    }


    public void addToSyncJobs(Principal item, SyncItemJob value)
    {
        addToSyncJobs(getSession().getSessionContext(), item, value);
    }


    public void removeFromSyncJobs(SessionContext ctx, Principal item, SyncItemJob value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.SYNCITEMJOB2PRINCIPAL, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(SYNCITEMJOB2PRINCIPAL_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(SYNCITEMJOB2PRINCIPAL_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(SYNCITEMJOB2PRINCIPAL_MARKMODIFIED));
    }


    public void removeFromSyncJobs(Principal item, SyncItemJob value)
    {
        removeFromSyncJobs(getSession().getSessionContext(), item, value);
    }


    Collection<SyncItemJob> getSyncJobs(SessionContext ctx, Language item)
    {
        List<SyncItemJob> items = item.getLinkedItems(ctx, false, GeneratedCatalogConstants.Relations.SYNCJOB2LANGREL, "SyncItemJob", null, false, false);
        return items;
    }


    Collection<SyncItemJob> getSyncJobs(Language item)
    {
        return getSyncJobs(getSession().getSessionContext(), item);
    }


    long getSyncJobsCount(SessionContext ctx, Language item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCatalogConstants.Relations.SYNCJOB2LANGREL, "SyncItemJob", null);
    }


    long getSyncJobsCount(Language item)
    {
        return getSyncJobsCount(getSession().getSessionContext(), item);
    }


    public Collection<Media> getThumbnails(SessionContext ctx, Product item)
    {
        Collection<Media> coll = (Collection<Media>)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.THUMBNAILS);
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }


    public Collection<Media> getThumbnails(Product item)
    {
        return getThumbnails(getSession().getSessionContext(), item);
    }


    public void setThumbnails(SessionContext ctx, Product item, Collection<Media> value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.THUMBNAILS, (value == null || !value.isEmpty()) ? value : null);
    }


    public void setThumbnails(Product item, Collection<Media> value)
    {
        setThumbnails(getSession().getSessionContext(), item, value);
    }


    String getUniqueKeyAttributeQualifier(SessionContext ctx, ComposedType item)
    {
        return (String)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.ComposedType.UNIQUEKEYATTRIBUTEQUALIFIER);
    }


    String getUniqueKeyAttributeQualifier(ComposedType item)
    {
        return getUniqueKeyAttributeQualifier(getSession().getSessionContext(), item);
    }


    void setUniqueKeyAttributeQualifier(SessionContext ctx, ComposedType item, String value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.ComposedType.UNIQUEKEYATTRIBUTEQUALIFIER, value);
    }


    void setUniqueKeyAttributeQualifier(ComposedType item, String value)
    {
        setUniqueKeyAttributeQualifier(getSession().getSessionContext(), item, value);
    }


    public Collection<AttributeDescriptor> getUniqueKeyAttributes(ComposedType item)
    {
        return getUniqueKeyAttributes(getSession().getSessionContext(), item);
    }


    public void setUniqueKeyAttributes(ComposedType item, Collection<AttributeDescriptor> value)
    {
        setUniqueKeyAttributes(getSession().getSessionContext(), item, value);
    }


    public Boolean isUnloadingAddress(SessionContext ctx, Address item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Address.UNLOADINGADDRESS);
    }


    public Boolean isUnloadingAddress(Address item)
    {
        return isUnloadingAddress(getSession().getSessionContext(), item);
    }


    public boolean isUnloadingAddressAsPrimitive(SessionContext ctx, Address item)
    {
        Boolean value = isUnloadingAddress(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isUnloadingAddressAsPrimitive(Address item)
    {
        return isUnloadingAddressAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setUnloadingAddress(SessionContext ctx, Address item, Boolean value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Address.UNLOADINGADDRESS, value);
    }


    public void setUnloadingAddress(Address item, Boolean value)
    {
        setUnloadingAddress(getSession().getSessionContext(), item, value);
    }


    public void setUnloadingAddress(SessionContext ctx, Address item, boolean value)
    {
        setUnloadingAddress(ctx, item, Boolean.valueOf(value));
    }


    public void setUnloadingAddress(Address item, boolean value)
    {
        setUnloadingAddress(getSession().getSessionContext(), item, value);
    }


    public String getUrl(SessionContext ctx, Address item)
    {
        return (String)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Address.URL);
    }


    public String getUrl(Address item)
    {
        return getUrl(getSession().getSessionContext(), item);
    }


    public void setUrl(SessionContext ctx, Address item, String value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Address.URL, value);
    }


    public void setUrl(Address item, String value)
    {
        setUrl(getSession().getSessionContext(), item, value);
    }


    public Collection<VariantProduct> getVariants(SessionContext ctx, Product item)
    {
        return PRODUCT2VARIANTRELATIONVARIANTSHANDLER.getValues(ctx, (Item)item);
    }


    public Collection<VariantProduct> getVariants(Product item)
    {
        return getVariants(getSession().getSessionContext(), item);
    }


    public void setVariants(SessionContext ctx, Product item, Collection<VariantProduct> value)
    {
        PRODUCT2VARIANTRELATIONVARIANTSHANDLER.setValues(ctx, (Item)item, value);
    }


    public void setVariants(Product item, Collection<VariantProduct> value)
    {
        setVariants(getSession().getSessionContext(), item, value);
    }


    public void addToVariants(SessionContext ctx, Product item, VariantProduct value)
    {
        PRODUCT2VARIANTRELATIONVARIANTSHANDLER.addValue(ctx, (Item)item, (Item)value);
    }


    public void addToVariants(Product item, VariantProduct value)
    {
        addToVariants(getSession().getSessionContext(), item, value);
    }


    public void removeFromVariants(SessionContext ctx, Product item, VariantProduct value)
    {
        PRODUCT2VARIANTRELATIONVARIANTSHANDLER.removeValue(ctx, (Item)item, (Item)value);
    }


    public void removeFromVariants(Product item, VariantProduct value)
    {
        removeFromVariants(getSession().getSessionContext(), item, value);
    }


    public VariantType getVariantType(SessionContext ctx, Product item)
    {
        return (VariantType)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.VARIANTTYPE);
    }


    public VariantType getVariantType(Product item)
    {
        return getVariantType(getSession().getSessionContext(), item);
    }


    public void setVariantType(SessionContext ctx, Product item, VariantType value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.VARIANTTYPE, value);
    }


    public void setVariantType(Product item, VariantType value)
    {
        setVariantType(getSession().getSessionContext(), item, value);
    }


    public List<CatalogVersion> getWritableCatalogVersions(SessionContext ctx, Principal item)
    {
        List<CatalogVersion> items = item.getLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.PRINCIPAL2WRITEABLECATALOGVERSIONRELATION, "CatalogVersion", null,
                        Utilities.getRelationOrderingOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_TGT_ORDERED, true));
        return items;
    }


    public List<CatalogVersion> getWritableCatalogVersions(Principal item)
    {
        return getWritableCatalogVersions(getSession().getSessionContext(), item);
    }


    public long getWritableCatalogVersionsCount(SessionContext ctx, Principal item)
    {
        return item.getLinkedItemsCount(ctx, true, GeneratedCatalogConstants.Relations.PRINCIPAL2WRITEABLECATALOGVERSIONRELATION, "CatalogVersion", null);
    }


    public long getWritableCatalogVersionsCount(Principal item)
    {
        return getWritableCatalogVersionsCount(getSession().getSessionContext(), item);
    }


    public void setWritableCatalogVersions(SessionContext ctx, Principal item, List<CatalogVersion> value)
    {
        item.setLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.PRINCIPAL2WRITEABLECATALOGVERSIONRELATION, null, value,
                        Utilities.getRelationOrderingOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void setWritableCatalogVersions(Principal item, List<CatalogVersion> value)
    {
        setWritableCatalogVersions(getSession().getSessionContext(), item, value);
    }


    public void addToWritableCatalogVersions(SessionContext ctx, Principal item, CatalogVersion value)
    {
        item.addLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.PRINCIPAL2WRITEABLECATALOGVERSIONRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void addToWritableCatalogVersions(Principal item, CatalogVersion value)
    {
        addToWritableCatalogVersions(getSession().getSessionContext(), item, value);
    }


    public void removeFromWritableCatalogVersions(SessionContext ctx, Principal item, CatalogVersion value)
    {
        item.removeLinkedItems(ctx, true, GeneratedCatalogConstants.Relations.PRINCIPAL2WRITEABLECATALOGVERSIONRELATION, null,
                        Collections.singletonList(value),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_SRC_ORDERED, true),
                        Utilities.getRelationOrderingOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_TGT_ORDERED, true),
                        Utilities.getMarkModifiedOverride(PRINCIPAL2WRITEABLECATALOGVERSIONRELATION_MARKMODIFIED));
    }


    public void removeFromWritableCatalogVersions(Principal item, CatalogVersion value)
    {
        removeFromWritableCatalogVersions(getSession().getSessionContext(), item, value);
    }


    public String getXmlcontent(SessionContext ctx, Product item)
    {
        return (String)item.getProperty(ctx, GeneratedCatalogConstants.Attributes.Product.XMLCONTENT);
    }


    public String getXmlcontent(Product item)
    {
        return getXmlcontent(getSession().getSessionContext(), item);
    }


    public void setXmlcontent(SessionContext ctx, Product item, String value)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.XMLCONTENT, value);
    }


    public void setXmlcontent(Product item, String value)
    {
        setXmlcontent(getSession().getSessionContext(), item, value);
    }


    public abstract AttributeDescriptor getCatalogVersionAttribute(SessionContext paramSessionContext, ComposedType paramComposedType);


    public abstract void setCatalogVersionAttribute(SessionContext paramSessionContext, ComposedType paramComposedType, AttributeDescriptor paramAttributeDescriptor);


    public abstract String getClassificationIndexString(SessionContext paramSessionContext, Product paramProduct);


    public abstract Map<Language, String> getAllClassificationIndexString(SessionContext paramSessionContext, Product paramProduct);


    public abstract List<ItemSyncTimestamp> getSynchronizationSources(SessionContext paramSessionContext, Item paramItem);


    public abstract List<ItemSyncTimestamp> getSynchronizedCopies(SessionContext paramSessionContext, Item paramItem);


    public abstract Collection<AttributeDescriptor> getUniqueKeyAttributes(SessionContext paramSessionContext, ComposedType paramComposedType);


    public abstract void setUniqueKeyAttributes(SessionContext paramSessionContext, ComposedType paramComposedType, Collection<AttributeDescriptor> paramCollection);
}
