package de.hybris.platform.catalog.jalo;

import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttribute;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystem;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.catalog.jalo.classification.util.FeatureContainer;
import de.hybris.platform.catalog.jalo.classification.util.TypedFeature;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.category.jalo.CategoryManager;
import de.hybris.platform.core.Constants;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericConditionList;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.core.Operator;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.cronjob.jalo.ChangeDescriptor;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.CronJobManager;
import de.hybris.platform.cronjob.jalo.JobLog;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.impex.jalo.ImpExManager;
import de.hybris.platform.impex.jalo.ImpExMedia;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.link.LinkManager;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.media.MediaFolder;
import de.hybris.platform.jalo.media.MediaManager;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.Principal;
import de.hybris.platform.jalo.security.PrincipalGroup;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.type.SearchRestriction;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.type.ViewType;
import de.hybris.platform.jalo.user.Address;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserGroup;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.JspContext;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.WebRequestInterceptor;
import de.hybris.platform.util.WebSessionFunctions;
import de.hybris.platform.util.config.PropertyActionReader;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.util.zip.SafeZipEntry;
import de.hybris.platform.variants.jalo.VariantProduct;
import de.hybris.platform.variants.jalo.VariantsManager;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class CatalogManager extends GeneratedCatalogManager implements WebRequestInterceptor, Extension.RightsProvider, Extension.RestrictedLanguagesProvider
{
    private static final Logger LOG = Logger.getLogger(CatalogManager.class);
    public static final String MEDIA_CODE_DELIMITER = "_";
    public static final String DEFAULT_CATALOG_ID = "Default";
    public static final String DEFAULT_CATALOG_NAME = "default catalog";
    public static final String OFFLINE_VERSION = "Staged";
    public static final String ONLINE_VERSION = "Online";
    public static final String DEFAULT_COMPANY_ID = "testfirma";
    public static final String DEFAULT_COMPANY_NAME = "a test firma";
    private static final String SYNC_TIMESTAMPS_REMOVED_FOR = "sync.timestamps.removed.for.";
    private final CategoryManager categoryManager = new CategoryManager();
    private final VariantsManager variantsManager = new VariantsManager();
    private static final String DELIMITER = "/";


    @Deprecated(since = "ages", forRemoval = false)
    public CategoryManager getCategoryManager()
    {
        return this.categoryManager;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public VariantsManager getVariantsManager()
    {
        return this.variantsManager;
    }


    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
    }


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
        if(item instanceof Product)
        {
            removeProductReferencesFor(ctx, (Product)item);
            for(Iterator<ProductFeature> it = getFeatures(ctx, (Product)item).iterator(); it.hasNext(); )
            {
                try
                {
                    ((ProductFeature)it.next()).remove(ctx);
                }
                catch(ConsistencyCheckException e)
                {
                    LOG.error(e.getMessage(), (Throwable)e);
                }
            }
        }
        if(item instanceof CatalogVersion)
        {
            removeSyncTimestampsFor(ctx, (CatalogVersion)item);
        }
        else if(shouldRemoveSyncItemsForType(item))
        {
            if(item.getPK() != null && shouldApplyQueryRemovalOptimisation(ctx, item))
            {
                ctx.removeAttribute("sync.timestamps.removed.for." + item.getPK().toString());
                return;
            }
            removeSyncTimestampsFor(ctx, item);
        }
    }


    protected boolean shouldApplyQueryRemovalOptimisation(SessionContext ctx, Item item)
    {
        return (Config.getBoolean("synctimestamp.query.removal.optimisation", true) &&
                        syncTimestampsAlreadyRemovedFor(ctx, item));
    }


    protected boolean shouldRemoveSyncItemsForType(Item item)
    {
        return !PropertyActionReader.getPropertyActionReader().isActionDisabledForType("synctimestamp.removal", item.getComposedType().getCode());
    }


    protected boolean syncTimestampsAlreadyRemovedFor(SessionContext ctx, Item item)
    {
        return (item.getPK() != null && Boolean.TRUE
                        .equals(ctx.getAttribute("sync.timestamps.removed.for." + item.getPK().toString())));
    }


    public static CatalogManager getInstance()
    {
        return (CatalogManager)Registry.getCurrentTenant().getJaloConnection().getExtensionManager()
                        .getExtension("catalog");
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setAllowedPrincipals(SessionContext ctx, Category cat, List<Principal> newOnes)
    {
        cat.setAllowedPrincipals(ctx, newOnes);
    }


    public String getMediaCode(String catalogID, String catalogVersion, String mediaCode)
    {
        return catalogID + "_" + catalogID + "_" + catalogVersion;
    }


    public Keyword createKeyword(CatalogVersion catalogVersion, String keyword, Language lang)
    {
        try
        {
            Item.ItemAttributeMap<String, String> itemAttributeMap = new Item.ItemAttributeMap();
            itemAttributeMap.put("keyword", keyword);
            itemAttributeMap.put("language", lang);
            if(catalogVersion != null)
            {
                itemAttributeMap.put("catalogVersion", catalogVersion);
            }
            return createKeyword((Map)itemAttributeMap);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e, "Could not create new Keyword instance. ErrorMessage: " + e.getMessage(), 2357);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Catalog getDefaultCatalog()
    {
        List<Catalog> resultlist = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CATALOG + "} WHERE {defaultCatalog} = ?t", Collections.singletonMap("t", Boolean.TRUE), Collections.singletonList(Catalog.class), true, true, 0, -1)
                        .getResult();
        if(resultlist.isEmpty())
        {
            return null;
        }
        Catalog firstOne = resultlist.get(0);
        if(resultlist.size() > 1)
        {
            LOG.warn("Multiple default catalogs found [" + resultlist + "] - choosing " + firstOne.getId());
        }
        return firstOne;
    }


    public Keyword getKeyword(CatalogVersion catalogVersion, String keywordValue)
    {
        return getKeyword(GeneratedCatalogConstants.TC.KEYWORD, catalogVersion, keywordValue);
    }


    public Keyword getKeyword(String typeCode, CatalogVersion catalogVersion, String keywordValue)
    {
        GenericCondition catalogVersionCondition, keywordCondition;
        if(catalogVersion == null)
        {
            catalogVersionCondition = GenericCondition.createIsNullCondition(new GenericSearchField(typeCode, "catalogVersion"));
        }
        else
        {
            catalogVersionCondition = GenericCondition.equals(new GenericSearchField(typeCode, "catalogVersion"), catalogVersion);
        }
        if(keywordValue == null)
        {
            keywordCondition = GenericCondition.createIsNullCondition(new GenericSearchField(typeCode, "keyword"));
        }
        else
        {
            keywordCondition = GenericCondition.equals(new GenericSearchField(typeCode, "keyword"), keywordValue);
        }
        GenericConditionList genConList = GenericCondition.createConditionList(catalogVersionCondition);
        genConList.addToConditionList(keywordCondition);
        GenericQuery query = new GenericQuery(typeCode, (GenericCondition)genConList);
        List<Keyword> result = getSession().search(query, getSession().createSearchContext()).getResult();
        if(result.isEmpty())
        {
            return null;
        }
        if(result.size() > 1)
        {
            LOG.error("Warning: " + result.size() + " matching keywords for CatalogVersion '" + (
                            (catalogVersion == null) ? "null" : catalogVersion.toString()) + "' and keyword value '" + keywordValue + "'! ");
        }
        return result.iterator().next();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Catalog createCatalog(String id)
    {
        try
        {
            Item.ItemAttributeMap<String, String> itemAttributeMap = new Item.ItemAttributeMap();
            itemAttributeMap.put("id", id);
            return createCatalog((Map)itemAttributeMap);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    public ClassificationSystem createClassificationSystem(String id) throws ConsistencyCheckException
    {
        if(getClassificationSystem(id) != null)
        {
            throw new ConsistencyCheckException("classification system '" + id + "' already exists", 0);
        }
        Item.ItemAttributeMap<String, String> itemAttributeMap = new Item.ItemAttributeMap();
        itemAttributeMap.put("id", id);
        return createClassificationSystem((Map)itemAttributeMap);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Catalog createCatalog(String id, String name, Company supplier)
    {
        try
        {
            Item.ItemAttributeMap<String, String> itemAttributeMap = new Item.ItemAttributeMap();
            itemAttributeMap.put("id", id);
            itemAttributeMap.put("name", name);
            itemAttributeMap.put("supplier", supplier);
            return createCatalog((Map)itemAttributeMap);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CatalogVersion createCatalogVersion(Catalog catalog, String version, Language importLanguage)
    {
        try
        {
            Item.ItemAttributeMap<String, Catalog> itemAttributeMap = new Item.ItemAttributeMap();
            itemAttributeMap.put("catalog", catalog);
            itemAttributeMap.put("version", version);
            if(importLanguage != null)
            {
                itemAttributeMap.put("languages", (Catalog)Collections.singletonList(importLanguage));
            }
            return createCatalogVersion((Map)itemAttributeMap);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    public ClassificationSystemVersion createClassificationSystemVersion(ClassificationSystem system, String version, Language importLanguage) throws ConsistencyCheckException
    {
        return createClassificationSystemVersion(system, version,
                        (importLanguage == null) ? null : Collections.<Language>singleton(importLanguage));
    }


    public ClassificationSystemVersion createClassificationSystemVersion(ClassificationSystem system, String version, Collection<Language> languages) throws ConsistencyCheckException
    {
        if(system.getCatalogVersion(version) != null)
        {
            throw new ConsistencyCheckException("classification system version '" + system
                            .getId() + "." + version + "' already exists", 0);
        }
        Item.ItemAttributeMap<String, ClassificationSystem> itemAttributeMap = new Item.ItemAttributeMap();
        itemAttributeMap.put("catalog", system);
        itemAttributeMap.put("version", version);
        if(languages != null && !languages.isEmpty())
        {
            itemAttributeMap.put("languages", languages);
        }
        return createClassificationSystemVersion((Map)itemAttributeMap);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Agreement createAgreement(String id, Date startDate, Date endDate)
    {
        try
        {
            Item.ItemAttributeMap<String, String> itemAttributeMap = new Item.ItemAttributeMap();
            itemAttributeMap.put("id", id);
            itemAttributeMap.put("startdate", startDate);
            itemAttributeMap.put("enddate", endDate);
            return createAgreement((Map)itemAttributeMap);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Agreement createAgreement(CatalogVersion catalogVersion, String id, Date startDate, Date endDate)
    {
        try
        {
            Item.ItemAttributeMap<String, String> itemAttributeMap = new Item.ItemAttributeMap();
            itemAttributeMap.put("id", id);
            itemAttributeMap.put("catalogVersion", catalogVersion);
            itemAttributeMap.put("startdate", startDate);
            itemAttributeMap.put("enddate", endDate);
            return createAgreement((Map)itemAttributeMap);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ProductReference createProductReference(String qualifier, Product source, Product target, Integer quantity)
    {
        try
        {
            Item.ItemAttributeMap<String, String> itemAttributeMap = new Item.ItemAttributeMap();
            itemAttributeMap.put("qualifier", qualifier);
            itemAttributeMap.put("source", source);
            itemAttributeMap.put("target", target);
            itemAttributeMap.put("quantity", quantity);
            return createProductReference((Map)itemAttributeMap);
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ProductReference> getProductReferences(String qualifier, Product source, Product target, Integer quantity, boolean forceQuantitySearch)
    {
        Map<Object, Object> args = new HashMap<>();
        int params = 0;
        StringBuilder query = (new StringBuilder("SELECT {")).append(Item.PK).append("} FROM {").append(getSession().getTypeManager().getComposedType(ProductReference.class).getCode()).append("}");
        if(qualifier != null)
        {
            query.append((params++ == 0) ? " WHERE " : " AND ");
            query.append("{").append("qualifier").append("} = ?qualifier");
            args.put("qualifier", qualifier);
        }
        if(source != null)
        {
            query.append((params++ == 0) ? " WHERE " : " AND ");
            query.append("{").append("source").append("} = ?source ");
            args.put("source", source);
        }
        if(target != null)
        {
            query.append((params++ == 0) ? " WHERE " : " AND ");
            query.append("{").append("target").append("} = ?target ");
            args.put("target", target);
        }
        if(forceQuantitySearch || quantity != null)
        {
            query.append((params++ == 0) ? " WHERE " : " AND ");
            if(quantity == null)
            {
                query.append("{").append("quantity").append("} IS NULL ");
            }
            else
            {
                query.append("{").append("quantity").append("} = ?quantity ");
                args.put("quantity", quantity);
            }
        }
        query.append(" ORDER BY {sourcePOS} ASC");
        return FlexibleSearch.getInstance().search(query.toString(), args, ProductReference.class).getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ProductReference> getProductReferences(String qualifier, Product source, Product target)
    {
        return getProductReferences(qualifier, source, target, null, false);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Catalog getCatalog(String catalogID)
    {
        GenericCondition idCondition = GenericCondition.equals(new GenericSearchField(GeneratedCatalogConstants.TC.CATALOG, "id"), catalogID);
        GenericConditionList gclist = GenericCondition.createConditionList(idCondition);
        GenericQuery query = new GenericQuery(GeneratedCatalogConstants.TC.CATALOG, (GenericCondition)gclist);
        Collection<Catalog> result = getSession().search(query, getSession().createSearchContext()).getResult();
        if(result.isEmpty())
        {
            return null;
        }
        if(result.size() > 1)
        {
            LOG.error("Warning: " + result.size() + " matching catalog for catalog id " + catalogID + "!");
        }
        return result.iterator().next();
    }


    public ClassificationSystem getClassificationSystem(String classificationSystemId)
    {
        GenericQuery query = new GenericQuery(GeneratedCatalogConstants.TC.CLASSIFICATIONSYSTEM, (GenericCondition)GenericCondition.createConditionList(
                        GenericCondition.equals(new GenericSearchField(GeneratedCatalogConstants.TC.CLASSIFICATIONSYSTEM, "id"), classificationSystemId)));
        Collection<ClassificationSystem> result = getSession().search(query, getSession().createSearchContext()).getResult();
        if(result.isEmpty())
        {
            return null;
        }
        if(result.size() > 1)
        {
            LOG.error("Warning: " + result.size() + " matching classificationsystems for id " + classificationSystemId + "!");
        }
        return result.iterator().next();
    }


    public ClassificationClass getClassificationClass(String systemName, String versionName, String classID)
    {
        GenericQuery genquery = new GenericQuery(GeneratedCatalogConstants.TC.CLASSIFICATIONCLASS);
        genquery.addInnerJoin(GeneratedCatalogConstants.TC.CLASSIFICATIONSYSTEMVERSION, "ver",
                        GenericCondition.createConditionForFieldComparison(new GenericSearchField(CatalogConstants.Attributes.Category.CATALOGVERSION), Operator.EQUAL, new GenericSearchField("ver", CatalogVersion.PK)));
        genquery.addInnerJoin(GeneratedCatalogConstants.TC.CLASSIFICATIONSYSTEM, "sys",
                        GenericCondition.createConditionForFieldComparison(new GenericSearchField("ver", "catalog"), Operator.EQUAL, new GenericSearchField("sys", ClassificationSystem.PK)));
        genquery.addConditions(new GenericCondition[] {GenericCondition.equals(new GenericSearchField("sys", "id"), systemName),
                        GenericCondition.equals(new GenericSearchField("ver", "version"), versionName),
                        GenericCondition.equals(new GenericSearchField("code"), classID)});
        List<ClassificationClass> result = getSession().search(genquery, getSession().createSearchContext()).getResult();
        if(result.isEmpty())
        {
            return null;
        }
        if(result.size() > 1)
        {
            LOG.error("Found more than one classification class for " + systemName + "/" + versionName + "/" + classID);
        }
        return result.get(0);
    }


    public List<ClassificationClass> getClassificationClasses(Product product)
    {
        return getClassificationClasses((SessionContext)null, product);
    }


    @SLDSafe(portingClass = "ProductClassificationClassesAttributeHandler", portingMethod = "get(final ProductModel model)")
    public List<ClassificationClass> getClassificationClasses(SessionContext ctx, Product product)
    {
        return getClassificationClasses(Collections.EMPTY_LIST, product);
    }


    public List<ClassificationClass> getClassificationClasses(ClassificationSystemVersion systemVersion, Product product)
    {
        return getClassificationClasses(Collections.singleton(systemVersion), product);
    }


    public List<ClassificationClass> getClassificationClasses(Collection<ClassificationSystemVersion> systemVersions, Product product)
    {
        return new ArrayList<>((new Object(this, product, systemVersions))
                        .resolve());
    }


    public List<ClassificationClass> getClassificationClasses(Category src)
    {
        return getClassificationClasses(Collections.EMPTY_LIST, src);
    }


    public List<ClassificationClass> getClassificationClasses(ClassificationSystemVersion systemVersion, Category src)
    {
        return getClassificationClasses(Collections.singleton(systemVersion), src);
    }


    public List<ClassificationClass> getClassificationClasses(Collection<ClassificationSystemVersion> systemVersions, Category src)
    {
        return new ArrayList<>((new Object(this, src, systemVersions))
                        .resolve());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Catalog getCatalog(String supplierUID, String catalogID)
    {
        Company supplier = getCompanyByUID(supplierUID);
        if(supplier == null)
        {
            return null;
        }
        return getCatalog(supplier, catalogID);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Catalog getCatalog(Company supplier, String catalogID)
    {
        GenericCondition idCondition = GenericCondition.equals(new GenericSearchField(GeneratedCatalogConstants.TC.CATALOG, "id"), catalogID);
        GenericConditionList gclist = GenericCondition.createConditionList(idCondition);
        if(supplier != null)
        {
            GenericCondition supplierNameCondition = GenericCondition.equals(new GenericSearchField(GeneratedCatalogConstants.TC.CATALOG, "supplier"), supplier);
            gclist.addToConditionList(supplierNameCondition);
        }
        GenericQuery query = new GenericQuery(GeneratedCatalogConstants.TC.CATALOG, (GenericCondition)gclist);
        Collection<Catalog> result = getSession().search(query, getSession().createSearchContext()).getResult();
        if(result.isEmpty())
        {
            return null;
        }
        if(result.size() > 1)
        {
            LOG.error("Warning: " + result.size() + " matching catalog for supplier '" + (
                            (supplier == null) ? "null" : supplier.getUID()) + "' and catalog id '" + catalogID + "'!");
        }
        return result.iterator().next();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Category getCatalogCategory(CatalogVersion catalogVersion, String code)
    {
        GenericCondition codeCondition = GenericCondition.equals(new GenericSearchField(GeneratedCatalogConstants.TC.CATEGORY, "code"), code);
        GenericCondition catalogVersionCondition = GenericCondition.equals(new GenericSearchField(GeneratedCatalogConstants.TC.CATEGORY, CatalogConstants.Attributes.Category.CATALOGVERSION), catalogVersion);
        GenericConditionList genlist = GenericCondition.createConditionList(codeCondition);
        genlist.addToConditionList(catalogVersionCondition);
        GenericQuery query = new GenericQuery(GeneratedCatalogConstants.TC.CATEGORY, (GenericCondition)genlist);
        Collection<Category> result = getSession().search(query, getSession().createSearchContext()).getResult();
        if(result.isEmpty())
        {
            return null;
        }
        if(result.size() > 1)
        {
            LOG.error("Warning: " + result.size() + " matching categories for CatalogVersion " + catalogVersion.getVersion() + " (CatalogID " + catalogVersion
                            .getCatalog().getId() + ") and code " + code + "! ");
        }
        return result.iterator().next();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Category> getAllCatalogCategories(CatalogVersion catalogVersion)
    {
        GenericCondition catalogVersionCondition = GenericCondition.equals(new GenericSearchField(GeneratedCatalogConstants.TC.CATEGORY, CatalogConstants.Attributes.Category.CATALOGVERSION), catalogVersion);
        GenericConditionList genlist = GenericCondition.createConditionList(catalogVersionCondition);
        GenericQuery query = new GenericQuery(GeneratedCatalogConstants.TC.CATEGORY, (GenericCondition)genlist);
        return getSession().search(query, getSession().createSearchContext()).getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Company getCompanyByUID(String uid)
    {
        GenericCondition nameCondition = GenericCondition.equals(new GenericSearchField(GeneratedCatalogConstants.TC.COMPANY, "uid"), uid);
        GenericConditionList genlist = GenericCondition.createConditionList(nameCondition);
        GenericQuery query = new GenericQuery(GeneratedCatalogConstants.TC.COMPANY, (GenericCondition)genlist);
        Collection<Company> result = getSession().search(query, getSession().createSearchContext()).getResult();
        if(result != null && result.size() == 1)
        {
            return result.iterator().next();
        }
        return null;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Company createCompany(String uid)
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("name", uid);
        values.put("uid", uid);
        return createCompany(values);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Category createCatalogCategory(CatalogVersion catalogVersion, String code, String name, String description, Integer order, Collection keywords, Category parent)
    {
        Item.ItemAttributeMap<String, CatalogVersion> itemAttributeMap = new Item.ItemAttributeMap();
        if(catalogVersion != null)
        {
            itemAttributeMap.put(CatalogConstants.Attributes.Category.CATALOGVERSION, catalogVersion);
        }
        if(parent != null)
        {
            itemAttributeMap.put("supercategories", (CatalogVersion)Collections.singletonList(parent));
        }
        itemAttributeMap.put("code", code);
        itemAttributeMap.put("name", name);
        itemAttributeMap.put(CatalogConstants.Attributes.Category.DESCRIPTION, description);
        itemAttributeMap.put(CatalogConstants.Attributes.Category.ORDER, order);
        itemAttributeMap.put(CatalogConstants.Attributes.Category.KEYWORDS, keywords);
        return createCategory((Map)itemAttributeMap);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe
    public void setCatalogVersion(SessionContext ctx, Product product, CatalogVersion version)
    {
        super.setCatalogVersion(ctx, product, version);
        product.setProperty(ctx, GeneratedCatalogConstants.Attributes.Product.CATALOG, (version == null) ? null : version.getCatalog(ctx));
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe
    public void setCatalogVersion(SessionContext ctx, Category cat, CatalogVersion catver)
    {
        cat.setCatalogVersion(ctx, catver);
        cat.setProperty(ctx, CatalogConstants.Attributes.Category.CATALOG, (catver == null) ? null : catver.getCatalog(ctx));
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe
    public void setCatalogVersion(SessionContext ctx, Media media, CatalogVersion version)
    {
        super.setCatalogVersion(ctx, media, version);
        media.setProperty(ctx, GeneratedCatalogConstants.Attributes.Media.CATALOG, (version == null) ? null : version.getCatalog(ctx));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setSessionCatalogVersions(Collection<CatalogVersion> versions)
    {
        setSessionCatalogVersions(getSession().getSessionContext(), versions);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setSessionCatalogVersions(SessionContext ctx, Collection<CatalogVersion> versions)
    {
        ctx.setAttribute("catalogversions", (versions != null && !versions.isEmpty()) ?
                        new ArrayList<>(versions) : Collections.<PK>singletonList(CatalogConstants.NO_VERSIONS_AVAILABLE_DUMMY));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isActivatingPreviewMode(JaloSession jalosession)
    {
        return isActivatingPreviewMode(jalosession.getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isActivatingPreviewMode(SessionContext ctx)
    {
        return Boolean.TRUE.equals(ctx.getAttribute("previewModeActivation"));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<CatalogVersion> getSessionCatalogVersions(JaloSession jalosession)
    {
        return getSessionCatalogVersions(jalosession.getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<CatalogVersion> getSessionCatalogVersions(SessionContext ctx)
    {
        Collection<? extends CatalogVersion> coll = (Collection)ctx.getAttribute("catalogversions");
        if(coll == null || (coll.size() == 1 && CatalogConstants.NO_VERSIONS_AVAILABLE_DUMMY.equals(coll.iterator().next())))
        {
            return Collections.EMPTY_LIST;
        }
        return Collections.unmodifiableCollection(coll);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<CatalogVersion> getAvailableCatalogVersions(JaloSession jalosession)
    {
        List<CatalogVersion> readableCatVersions = new ArrayList<>(getAllReadableCatalogVersions(jalosession.getSessionContext(), jalosession.getUser()));
        return Collections.unmodifiableList(readableCatVersions);
    }


    public void createCatalogSyncMediaFolder()
    {
        if(MediaManager.getInstance().getMediaFolderByQualifier("catalogsync").size() < 1)
        {
            MediaManager.getInstance().createMediaFolder("catalogsync", "catalogsync");
        }
    }


    public MediaFolder getCatalogSyncMediaFolder()
    {
        Collection<MediaFolder> folders = MediaManager.getInstance().getMediaFolderByQualifier("catalogsync");
        if(folders.isEmpty())
        {
            return null;
        }
        return folders.iterator().next();
    }


    public void notifyInitializationEnd(Map<String, String> params, JspContext ctx) throws Exception
    {
        super.notifyInitializationEnd(params, ctx);
    }


    public void createSavedQueries(JspContext jspc)
    {
        TypeManager typeman = getSession().getTypeManager();
        FlexibleSearch fsearch = getSession().getFlexibleSearch();
        Language lang_de = getLanguageIfExists("de");
        Language lang_en = getLanguageIfExists("en");
        Map<Object, Object> names = new HashMap<>();
        if(fsearch.getSavedQuery("ProductsByFeatureString") == null)
        {
            String column;
            if(lang_de != null)
            {
                names.put(lang_de, "Klassifikationsattributs-Suche");
            }
            if(lang_en != null)
            {
                names.put(lang_en, "Classification attribute search");
            }
            boolean useToCharForClob = (Config.isOracleUsed() || Config.isHanaUsed());
            if(useToCharForClob)
            {
                column = "to_char({pf:stringValue})";
            }
            else
            {
                column = "{pf:stringValue}";
            }
            Map<Object, Object> params = new HashMap<>();
            params.put("qualifier", typeman.getType("java.lang.String"));
            params.put("valueString", typeman.getType("java.lang.String"));
            fsearch.createSavedQuery("ProductsByFeatureString", typeman.getComposedType(Product.class),
                                            "SELECT {p:" + Item.PK + "} FROM {$$$ AS p} \nWHERE EXISTS({{ \n   SELECT {pf:" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.PRODUCTFEATURE + " AS pf} \n   WHERE \n      {pf:product}={p:" + Item.PK + "} AND \n      lower(" + column
                                                            + ") LIKE lower(?valueString) AND \n      ( \n         lower({pf:qualifier}) LIKE lower(?qualifier) OR \n         {pf:qualifier} IN ({{\n            SELECT DISTINCT {ca:code} \n            FROM {" + GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTE
                                                            + " AS ca} \n            WHERE lower({ca:name}) LIKE lower(?qualifier) \n         }}) \n      ) \n}})", params)
                            .setAllName(names);
        }
        if(fsearch.getSavedQuery("DuplicateProductSearch") == null)
        {
            if(lang_de != null)
            {
                names.put(lang_de, "Doppelte Produkte in einer Katalogversion");
            }
            if(lang_en != null)
            {
                names.put(lang_en, "Duplicate products within one catalog version");
            }
            fsearch
                            .createSavedQuery("DuplicateProductSearch", typeman.getComposedType(Product.class),
                                            generateDuplicateQuery(Product.class),
                                            Collections.singletonMap("version", typeman
                                                            .getComposedType(GeneratedCatalogConstants.TC.CATALOGVERSION)))
                            .setAllName(names);
        }
        if(fsearch.getSavedQuery("DuplicateCategorySearch") == null)
        {
            if(lang_de != null)
            {
                names.put(lang_de, "Doppelte Kategorien in einer Katalogversion");
            }
            if(lang_en != null)
            {
                names.put(lang_en, "Duplicate categories within one catalog version");
            }
            fsearch
                            .createSavedQuery("DuplicateCategorySearch", typeman.getComposedType(Category.class),
                                            generateDuplicateQuery(Category.class),
                                            Collections.singletonMap("version", typeman
                                                            .getComposedType(GeneratedCatalogConstants.TC.CATALOGVERSION)))
                            .setAllName(names);
        }
        if(fsearch.getSavedQuery("DuplicateMediaSearch") == null)
        {
            if(lang_de != null)
            {
                names.put(lang_de, "Doppelte Medien in einer Katalogversion");
            }
            if(lang_en != null)
            {
                names.put(lang_en, "Duplicate medias within one catalog version2");
            }
            fsearch
                            .createSavedQuery("DuplicateMediaSearch", typeman.getComposedType(Media.class),
                                            generateDuplicateQuery(Media.class),
                                            Collections.singletonMap("version", typeman
                                                            .getComposedType(GeneratedCatalogConstants.TC.CATALOGVERSION)))
                            .setAllName(names);
        }
        if(fsearch.getSavedQuery("DuplicateKeywordSearch") == null)
        {
            if(lang_de != null)
            {
                names.put(lang_de, "Doppelte Schlüsselwörter in einer Katalogversion2");
            }
            if(lang_en != null)
            {
                names.put(lang_en, "Duplicate keywords within one catalog version2");
            }
            fsearch
                            .createSavedQuery("DuplicateKeywordSearch", typeman.getComposedType(Keyword.class),
                                            generateDuplicateQuery(Keyword.class),
                                            Collections.singletonMap("version", typeman
                                                            .getComposedType(GeneratedCatalogConstants.TC.CATALOGVERSION)))
                            .setAllName(names);
        }
    }


    protected InputStream getZipEntryInputStream(ZipFile zipfile, String entryName) throws IOException
    {
        ZipEntry zentry = zipfile.getEntry(entryName);
        if(zentry == null)
        {
            LOG.error("Missing zip entry '" + entryName + "' in file " + zipfile.getName());
            return null;
        }
        return zipfile.getInputStream((ZipEntry)new SafeZipEntry(zentry));
    }


    protected ImpExMedia getBatchMedia(String fileName, String fileExt) throws IOException, JaloBusinessException
    {
        String adjustedFileName = (fileName.indexOf('/') == 0) ? fileName : ("/" + fileName);
        InputStream istr = CatalogManager.class.getResourceAsStream(adjustedFileName);
        if(istr == null)
        {
            throw new IllegalArgumentException("Missing classification data file: " + adjustedFileName + "!\nPlease be sure that the 'classificationsystems' extension is installed.");
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Loading file: " + fileName);
        }
        return createBatchMedia(fileName, fileExt, istr);
    }


    protected ImpExMedia createBatchMedia(String fileName, String mime, InputStream istr)
    {
        ImpExMedia batchMedia = null;
        try
        {
            Map<Object, Object> values = new HashMap<>();
            values.put("code", fileName);
            values.put("removable", Boolean.TRUE);
            values.put("encoding", Utilities.resolveEncoding("windows-1252"));
            batchMedia = ImpExManager.getInstance().createImpExMedia(values);
            if(istr == null)
            {
                batchMedia.setMime(mime);
                batchMedia.setRealFileName(fileName);
            }
            else
            {
                batchMedia.setData(new DataInputStream(istr), fileName, mime);
            }
            return batchMedia;
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            if(cause == null)
            {
                throw new JaloSystemException(e);
            }
            if(cause instanceof RuntimeException)
            {
                throw (RuntimeException)cause;
            }
            throw new JaloSystemException(cause);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e);
        }
        catch(UnsupportedEncodingException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ClassificationAttribute getClassificationAttribute(String code, ClassificationSystemVersion version) throws JaloItemNotFoundException
    {
        return version.getClassificationAttribute(code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ClassificationAttributeValue getClassificationAttributeValue(String code, ClassificationSystemVersion version)
    {
        return version.getClassificationAttributeValue(code);
    }


    public Collection<Category> getCategoriesByProduct(CatalogVersion catver, Product product, SessionContext ctx)
    {
        String query = "SELECT {" + Category.PK + "}  FROM {" + GeneratedCatalogConstants.TC.CATEGORY + " AS cat    JOIN " + GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION + " AS c2pRel    ON {c2pRel:source}={cat:" + Category.PK + "} }  WHERE {cat:"
                        + CatalogConstants.Attributes.Category.CATALOGVERSION + "} = ?cv  AND {c2pRel:target} = ?product";
        Map<String, Object> values = new HashMap<>();
        values.put("cv", catver);
        values.put("product", product);
        return FlexibleSearch.getInstance().search(ctx, query, values, Category.class).getResult();
    }


    public Collection<Category> getCategoriesByProduct(CatalogVersion catver, Product product)
    {
        return getCategoriesByProduct(catver, product, JaloSession.getCurrentSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Link> getCategoryProductLinks(CatalogVersion catalogVersion, Category category, Product product)
    {
        Collection<Link> links = getSession().getLinkManager().getLinks(GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION, (Item)category, (Item)product);
        if(catalogVersion != null && (category == null || product == null))
        {
            List<Link> filteredLinks = new LinkedList<>();
            for(Iterator<Link> it = links.iterator(); it.hasNext(); )
            {
                try
                {
                    Link link = it.next();
                    Category linkCategory = (Category)link.getSource();
                    Product linkProduct = (Product)link.getTarget();
                    boolean add = true;
                    if(category == null &&
                                    !catalogVersion.equals(linkCategory
                                                    .getAttribute(CatalogConstants.Attributes.Category.CATALOGVERSION)))
                    {
                        add = false;
                    }
                    if(product == null &&
                                    !catalogVersion.equals(linkProduct
                                                    .getAttribute(CatalogConstants.Attributes.Category.CATALOGVERSION)))
                    {
                        add = false;
                    }
                    if(add)
                    {
                        filteredLinks.add(link);
                    }
                }
                catch(JaloSecurityException x)
                {
                    throw new JaloSystemException(x, "", 0);
                }
            }
            return filteredLinks;
        }
        return links;
    }


    public Product getProductByCatalogVersion(CatalogVersion catalogVersion, String productCode)
    {
        HashMap<Object, Object> args = new HashMap<>();
        ComposedType productType = getSession().getTypeManager().getRootComposedType(1);
        StringBuilder query = new StringBuilder();
        query.append("SELECT {").append(Item.PK).append("} FROM {").append(productType.getCode()).append("}");
        query.append(" WHERE ").append("{code} =?productCode");
        args.put("productCode", productCode);
        query.append(" AND {").append(GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION);
        query.append("} =?catalogVersion");
        args.put("catalogVersion", catalogVersion);
        Collection<Product> products = getSession().getFlexibleSearch().search(getSession().getSessionContext(), query.toString(), args, Collections.singletonList(Product.class), true, true, 0, -1).getResult();
        switch(products.size())
        {
            case 0:
                return null;
            case 1:
                return products.iterator().next();
        }
        LOG.warn("More than one product with code '" + productCode + "' found!");
        return products.iterator().next();
    }


    public Collection<VariantProduct> getVariants(SessionContext ctx, Product baseProduct, CatalogVersion catalogVersion)
    {
        String query = "SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.VARIANTPRODUCT + "} WHERE {baseProduct} = ?base AND {" + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION + "} =?catalogVersion ORDER BY {code} ASC";
        HashMap<Object, Object> params = new HashMap<>();
        params.put("base", baseProduct);
        params.put("catalogVersion", catalogVersion);
        return getSession().getFlexibleSearch().search(ctx, query, params, VariantProduct.class).getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Product> getCategoryProductsByState(SessionContext ctx, Category category, boolean offline)
    {
        String productType = getSession().getTypeManager().getComposedType(Product.class).getCode();
        StringBuilder query = new StringBuilder();
        query.append("SELECT {ccr:target} FROM { " + GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION);
        query.append(" AS ccr JOIN " + productType + " AS p ON {ccr:target} = {p:" + Item.PK + "} } ");
        query.append("WHERE {ccr:source} = ?category ");
        if(offline)
        {
            query.append("AND ( {p:" + GeneratedCatalogConstants.Attributes.Product.ONLINEDATE + "} > ?session.user");
            query.append(".currentDate  OR {p:" + GeneratedCatalogConstants.Attributes.Product.OFFLINEDATE + "} < ?session.");
            query.append("user.currentDate ) ");
        }
        else
        {
            query.append(" AND  ( {p:" + GeneratedCatalogConstants.Attributes.Product.ONLINEDATE + "} IS NULL OR {p:");
            query.append(GeneratedCatalogConstants.Attributes.Product.ONLINEDATE + "} < ?session.user.currentDate");
            query.append(" ) AND ( {p:" + GeneratedCatalogConstants.Attributes.Product.OFFLINEDATE + "} IS NULL OR {p:");
            query.append(GeneratedCatalogConstants.Attributes.Product.OFFLINEDATE + "} > ?session.user.");
            query.append("currentDate ) ");
        }
        query.append("ORDER BY sequenceNumber ASC ");
        Map<Object, Object> params = new HashMap<>();
        params.put("category", category);
        params.put("currentdate", new Date());
        return FlexibleSearch.getInstance().search(ctx, query.toString(), params, Product.class).getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "ProductUntypedFeaturesAttributeHandler", portingMethod = "get(final ProductModel model)")
    public List<ProductFeature> getUntypedFeatures(Product item)
    {
        return getUntypedFeatures(getSession().getSessionContext(), item);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "ProductUntypedFeaturesAttributeHandler", portingMethod = "get(final ProductModel model)")
    public List<ProductFeature> getUntypedFeatures(SessionContext ctx, Product prod)
    {
        return FlexibleSearch.getInstance()
                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.PRODUCTFEATURE + "} WHERE {product}=?p AND {classificationAttributeAssignment} IS NULL ORDER BY {featurePosition} ASC, {valuePosition} ASC, {" + Item.CREATION_TIME + "} ASC ",
                                        Collections.singletonMap("p", prod), Collections.singletonList(ProductFeature.class), true, true, 0, -1)
                        .getResult();
    }


    @SLDSafe(portingClass = "ProductUntypedFeaturesAttributeHandler", portingMethod = "set(final ProductModel model, final List<ProductFeatureModel> incomingFeatures)")
    public void setUntypedFeatures(SessionContext ctx, Product product, List<ProductFeature> features)
    {
        Set<ProductFeature> toRemove = new HashSet<>(getUntypedFeatures(ctx, product));
        if(features != null)
        {
            toRemove.removeAll(features);
            for(Iterator<ProductFeature> iterator = features.iterator(); iterator.hasNext(); )
            {
                ProductFeature feature = iterator.next();
                if(!product.equals(feature.getProduct(ctx)))
                {
                    throw new JaloInvalidParameterException("feature " + feature + " of " + features + " does not belong to product " + product, 0);
                }
                if(feature.getClassificationAttributeAssignment() != null)
                {
                    throw new JaloInvalidParameterException("feature " + feature + " of " + features + " is not untyped by belongs to attribute " + feature
                                    .getClassificationAttributeAssignment(), 0);
                }
            }
        }
        for(Iterator<ProductFeature> it = toRemove.iterator(); it.hasNext(); )
        {
            ProductFeature feature = it.next();
            try
            {
                feature.remove(ctx);
            }
            catch(ConsistencyCheckException e)
            {
                e.printStackTrace();
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<Catalog> getAllCatalogs()
    {
        return new LinkedHashSet<>(FlexibleSearch.getInstance()
                        .search("SELECT {PK} FROM {" + GeneratedCatalogConstants.TC.CATALOG + "} ORDER BY {" + Item.PK + "} ASC", Catalog.class)
                        .getResult());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<CatalogVersion> getAllCatalogVersions()
    {
        return new LinkedHashSet<>(FlexibleSearch.getInstance()
                        .search("SELECT {PK} FROM {" + GeneratedCatalogConstants.TC.CATALOGVERSION + "} ORDER BY {" + Item.PK + "} ASC", CatalogVersion.class)
                        .getResult());
    }


    public Set<ClassificationSystem> getAllClassificationSystems()
    {
        return new LinkedHashSet<>(
                        FlexibleSearch.getInstance()
                                        .search("SELECT {PK} FROM {" + GeneratedCatalogConstants.TC.CLASSIFICATIONSYSTEM + "} ORDER BY {" + Item.PK + "} ASC", ClassificationSystem.class)
                                        .getResult());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<ClassificationSystemVersion> getAllClassificationSystemVersions()
    {
        return new LinkedHashSet<>(FlexibleSearch.getInstance().search("SELECT {PK} FROM {" + GeneratedCatalogConstants.TC.CLASSIFICATIONSYSTEMVERSION + "} ORDER BY {" + Item.PK + "} ASC", ClassificationSystemVersion.class)
                        .getResult());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Language getLanguageIfExists(String isoCode)
    {
        try
        {
            return C2LManager.getInstance().getLanguageByIsoCode(isoCode);
        }
        catch(JaloItemNotFoundException e)
        {
            return null;
        }
    }


    public void createUsersAndGroupsAndRights() throws Exception
    {
        LOG.info("Importing resource /catalog/cataloguserrights.csv");
        InputStream istr = CatalogManager.class.getResourceAsStream("/catalog/cataloguserrights.csv");
        ImpExManager.getInstance().importData(istr, "utf-8", ';', '"', true);
    }


    public boolean createDefaultCatalog(JspContext jspc) throws Exception
    {
        boolean newDefaultCatalog = false;
        Catalog catalog = getCatalog("Default");
        if(catalog == null)
        {
            LOG.info("Importing resource /catalog/essentialdatacatalog.csv");
            InputStream istr = CatalogManager.class.getResourceAsStream("/catalog/essentialdatacatalog.csv");
            ImpExManager.getInstance().importData(istr, "utf-8", ';', '"', true);
            catalog = getCatalog("Default");
            newDefaultCatalog = true;
        }
        if(catalog != null)
        {
            CatalogVersion offlineVersion = catalog.getCatalogVersion("Staged");
            CatalogVersion onlineVersion = catalog.getCatalogVersion("Online");
            assignProducts2DefaultCatalog(offlineVersion);
            assignCategories2DefaultCatalog(offlineVersion);
            assignMedias2DefaultCatalog(offlineVersion);
            catalog.setActiveCatalogVersion(onlineVersion);
        }
        return newDefaultCatalog;
    }


    public void configureDefaultSynchronization(JspContext jspc)
    {
        Catalog cat = getDefaultCatalog();
        if(cat.getId().equals("Default") && cat.getCatalogVersion("Staged") != null && cat
                        .getCatalogVersion("Online") != null)
        {
            configureSynchronizationJob(null, getDefaultCatalog(), "Staged", "Online", true, false);
        }
    }


    public SyncItemJob getSyncJobFromSource(CatalogVersion source)
    {
        Collection coll = source.getSyncJobs(true);
        return coll.isEmpty() ? null : source.getSyncJobs(true).iterator().next();
    }


    public SyncItemJob getSyncJob(CatalogVersion source, CatalogVersion target)
    {
        return getSyncJob(source, target, null);
    }


    public SyncItemJob getSyncJob(CatalogVersion source, CatalogVersion target, String qualifier)
    {
        Preconditions.checkArgument((source != null));
        Preconditions.checkArgument((target != null));
        Preconditions.checkArgument(!source.equals(target));
        Map<Object, Object> params = new HashMap<>();
        params.put("src", source);
        params.put("tgt", target);
        if(qualifier != null)
        {
            params.put("code", qualifier);
        }
        List<SyncItemJob> jobs = FlexibleSearch.getInstance().search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.SYNCITEMJOB + "} WHERE {sourceVersion}=?src AND {targetVersion}=?tgt " + ((qualifier == null) ? "" : " AND {code}=?code"), params, SyncItemJob.class).getResult();
        return jobs.isEmpty() ? null : jobs.get(0);
    }


    public SyncItemJob configureSynchronizationJob(String code, Catalog catalog, String sourceVersion, String targetVersion, boolean createNewItems, boolean removeMissingItems)
    {
        CatalogVersion source = catalog.getCatalogVersion(sourceVersion);
        CatalogVersion target = catalog.getCatalogVersion(targetVersion);
        if(source == null)
        {
            throw new JaloInvalidParameterException("cannot find source version '" + sourceVersion + "' within catalog " + catalog
                            .getId(), 0);
        }
        if(target == null)
        {
            throw new JaloInvalidParameterException("cannot find target version '" + targetVersion + "' within catalog " + catalog
                            .getId(), 0);
        }
        SyncItemJob ret = getSyncJob(source, target);
        if(ret == null)
        {
            Item.ItemAttributeMap<String, String> itemAttributeMap = new Item.ItemAttributeMap();
            itemAttributeMap.put("code", code);
            itemAttributeMap.put("sourceVersion", source);
            itemAttributeMap.put("targetVersion", target);
            itemAttributeMap.put("createNewItems", Boolean.valueOf(createNewItems));
            itemAttributeMap.put("removeMissingItems", Boolean.valueOf(removeMissingItems));
            ret = newDefaultSyncJobInstance((Map)itemAttributeMap);
        }
        else
        {
            ret.setCreateNewItems(createNewItems);
            ret.setRemoveMissingItems(removeMissingItems);
        }
        return ret;
    }


    protected SyncItemJob newDefaultSyncJobInstance(Map attributes)
    {
        if(Config.getBoolean("sync.compatibility", false))
        {
            return createSyncItemJob(attributes);
        }
        return (SyncItemJob)createCatalogVersionSyncJob(attributes);
    }


    private Collection<Product> getAllUnassignedProducts(int start, int count)
    {
        return FlexibleSearch.getInstance()
                        .search("SELECT {" + Item.PK + "} FROM {" +
                                                        TypeManager.getInstance()
                                                                        .getComposedType(Product.class)
                                                                        .getCode() + "} WHERE {" + GeneratedCatalogConstants.Attributes.Media.CATALOGVERSION + "} IS NULL ORDER BY {code} ASC, {" + Item.PK + "} ASC", Collections.EMPTY_MAP,
                                        Collections.singletonList(Product.class), true, true, start, count)
                        .getResult();
    }


    private void assignProducts2DefaultCatalog(CatalogVersion catver)
    {
        JaloSession jalosession = getSession();
        EnumerationValue approvalStatus = jalosession.getEnumerationManager().getEnumerationValue(GeneratedCatalogConstants.TC.ARTICLEAPPROVALSTATUS, GeneratedCatalogConstants.Enumerations.ArticleApprovalStatus.APPROVED);
        int start = 0;
        int range = 1000;
        Collection<Product> products = null;
        do
        {
            products = getAllUnassignedProducts(0, 1000);
            for(Product prod : products)
            {
                assignproduct2DefaultCatalog(prod, catver, approvalStatus);
            }
        }
        while(products.size() == 1000);
    }


    private Collection<CatalogVersion> getAllActiveCatalogVersionsIgnoringRestrictions()
    {
        return FlexibleSearch.getInstance()
                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CATALOGVERSION + "*} WHERE {active} = ?active ORDER BY {" + Item.CREATION_TIME + "}",
                                        Collections.singletonMap("active", Boolean.TRUE), CatalogVersion.class)
                        .getResult();
    }


    private void assignproduct2DefaultCatalog(Product prod, CatalogVersion catver, EnumerationValue approvalStatus)
    {
        setCatalogVersion(prod, catver);
        setApprovalStatus(prod, approvalStatus);
    }


    private Collection<Category> getAllUnassignedCategories(int start, int count)
    {
        return getSession().getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {" +
                                                        TypeManager.getInstance()
                                                                        .getComposedType(Category.class)
                                                                        .getCode() + "} WHERE {" + GeneratedCatalogConstants.Attributes.Media.CATALOGVERSION + "} IS NULL ORDER BY {code} ASC, {" + Item.PK + "} ASC", Collections.EMPTY_MAP,
                                        Collections.singletonList(Category.class), true, true, start, count)
                        .getResult();
    }


    private void assignCategories2DefaultCatalog(CatalogVersion catver)
    {
        Collection<UserGroup> allowedlist = new ArrayList();
        UserGroup usergroup = UserManager.getInstance().getUserGroupByGroupID(Constants.USER.CUSTOMER_USERGROUP);
        if(usergroup != null)
        {
            allowedlist.add(usergroup);
        }
        int start = 0;
        int range = 1000;
        Collection<Category> cats = null;
        do
        {
            cats = getAllUnassignedCategories(0, 1000);
            for(Iterator<Category> it = cats.iterator(); it.hasNext(); )
            {
                Category cat = it.next();
                cat.setCatalogVersion(catver);
                if(cat.isRootAsPrimitive())
                {
                    try
                    {
                        cat.setAttribute(CatalogConstants.Attributes.Category.ALLOWEDPRINCIPALS, allowedlist);
                    }
                    catch(Exception e)
                    {
                        LOG.error("Cannot add customergroup to allowedprincipals of root category:" + e);
                    }
                }
            }
        }
        while(cats.size() == 1000);
    }


    private Collection<Media> getAllUnassignedMedias(int start, int count, ComposedType media_ct, String catalogversionqualifier)
    {
        return getSession().getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {" + media_ct.getCode() + "!} WHERE {" + catalogversionqualifier + "} IS NULL", Collections.EMPTY_MAP,
                                        Collections.singletonList(Media.class), true, true, start, count)
                        .getResult();
    }


    private void assignMedias2DefaultCatalog(CatalogVersion catver)
    {
        ComposedType superMediaCT = TypeManager.getInstance().getComposedType(Media.class);
        Collection<ComposedType> mediatypes = superMediaCT.getAllSubTypes();
        mediatypes.add(superMediaCT);
        label16:
        for(ComposedType mediassubtype : mediatypes)
        {
            AttributeDescriptor carverAD = getInstance().getCatalogVersionAttribute(mediassubtype);
            if(!carverAD.isOptional())
            {
                int start = 0;
                int range = 1000;
                Collection<Media> medias = null;
                while(true)
                {
                    medias = getAllUnassignedMedias(0, 1000, mediassubtype, carverAD.getQualifier());
                    for(Media media : medias)
                    {
                        setCatalogVersion(media, catver);
                    }
                    if(medias.size() != 1000)
                    {
                        continue label16;
                    }
                }
            }
        }
    }


    public String getCreatorParameterDefault(String param)
    {
        if(param.equals("sample data"))
        {
            return Boolean.TRUE.toString();
        }
        return null;
    }


    public List<String> getCreatorParameterPossibleValues(String param)
    {
        if(param.equals("sample data"))
        {
            return Arrays.asList(new String[] {Boolean.TRUE
                            .toString(), Boolean.FALSE.toString()});
        }
        return Collections.EMPTY_LIST;
    }


    @SLDSafe(portingClass = "AddressTypeQualifierAttributeHandler", portingMethod = "get(final AddressModel model)")
    public String getTypeQualifier(Address address)
    {
        return getTypeQualifier(getSession().getSessionContext(), address);
    }


    @SLDSafe(portingClass = "AddressTypeQualifierAttributeHandler", portingMethod = "get(final AddressModel model)")
    public String getTypeQualifier(SessionContext ctx, Address address)
    {
        StringBuilder typeQualifer = new StringBuilder();
        if(isUnloadingAddressAsPrimitive(ctx, address))
        {
            typeQualifer.append(localize("address.is.unloading"));
        }
        if(isShippingAddressAsPrimitive(ctx, address))
        {
            if(typeQualifer.length() > 0)
            {
                typeQualifer.append("/");
            }
            typeQualifer.append(localize("address.is.shipping"));
        }
        if(isBillingAddressAsPrimitive(ctx, address))
        {
            if(typeQualifer.length() > 0)
            {
                typeQualifer.append("/");
            }
            typeQualifer.append(localize("address.is.billing"));
        }
        if(isContactAddressAsPrimitive(ctx, address))
        {
            if(typeQualifer.length() > 0)
            {
                typeQualifer.append("/");
            }
            typeQualifer.append(localize("address.is.contact"));
        }
        if(typeQualifer.length() == 0)
        {
            typeQualifer.append("/");
        }
        return typeQualifer.toString();
    }


    private String localize(String key)
    {
        return Localization.getLocalizedString(key);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setApprovalStatus(Category cat, EnumerationValue status, boolean recursively, boolean asTransaction)
    {
        Object object = new Object(this, cat, status, recursively);
        try
        {
            if(asTransaction)
            {
                Transaction.current().execute((TransactionBody)object);
            }
            else
            {
                object.execute();
            }
        }
        catch(Exception e)
        {
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            throw new JaloSystemException(e);
        }
    }


    public Date getLastSyncModifiedTime(SyncItemJob job, Item src, Item copy)
    {
        boolean excl = job.isExclusiveModeAsPrimitive();
        Map<Object, Object> params = new HashMap<>();
        params.put("src", src);
        if(excl)
        {
            params.put("job", job);
        }
        else
        {
            params.put("tgtVer", job.getTargetVersion());
        }
        List<ItemSyncTimestamp> rows = FlexibleSearch.getInstance()
                        .search("SELECT {" + ItemSyncTimestamp.PK + "} FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + "*} WHERE {syncJob}" + (excl ? "=?job" : "=0") + " AND " + (excl ? "" : "{targetVersion}=?tgtVer AND ") + "{sourceItem}=?src", params, ItemSyncTimestamp.class)
                        .getResult();
        ItemSyncTimestamp ists = null;
        for(ItemSyncTimestamp match : rows)
        {
            if(copy.equals(match.getTargetItem()))
            {
                if(ists == null)
                {
                    ists = match;
                    continue;
                }
                if(LOG.isEnabledFor((Priority)Level.WARN))
                {
                    LOG.warn("Found multiple sync timestamps for " + src
                                    .getPK() + " -> (" + ists + "," + match + ") - ignored");
                }
                if(ists.getCreationTime().after(match.getCreationTime()))
                {
                    ists = match;
                }
            }
        }
        return (ists == null) ? null : ists.getLastSyncSourceModifiedTime();
    }


    public Item getSynchronizedCopy(Item source, SyncItemJob sij)
    {
        boolean excl = sij.isExclusiveModeAsPrimitive();
        Map<Object, Object> params = new HashMap<>();
        params.put("src", source);
        if(excl)
        {
            params.put("job", sij);
        }
        else
        {
            params.put("tgtVer", sij.getTargetVersion());
        }
        List<ItemSyncTimestamp> rows = FlexibleSearch.getInstance()
                        .search(null, "SELECT {" + ItemSyncTimestamp.PK + "} FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + "*} WHERE {sourceItem}=?src AND " + (excl ? "" : "{targetVersion}=?tgtVer AND ") + "{syncJob}" + (excl ? "=?job" : "=0"), params, ItemSyncTimestamp.class)
                        .getResult();
        ItemSyncTimestamp ists = null;
        for(ItemSyncTimestamp match : rows)
        {
            if(ists == null)
            {
                ists = match;
                continue;
            }
            if(LOG.isEnabledFor((Priority)Level.WARN))
            {
                LOG.warn("Found multiple sync timestamps for " + source
                                .getPK() + " -> (" + ists + "," + match + ") - ignored");
            }
            if(ists.getCreationTime().after(match.getCreationTime()))
            {
                ists = match;
            }
        }
        return (ists == null) ? null : ists.getTargetItem();
    }


    protected void removeProductReferencesFor(SessionContext ctx, Product prod)
    {
        int RANGE = 1000;
        List<ProductReference> rows = null;
        Map<PK, String> failed = null;
        do
        {
            rows = getSession().getFlexibleSearch()
                            .search(ctx, "SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.PRODUCTREFERENCE + "*} WHERE {target}=?item OR {source}=?item", Collections.singletonMap("item", prod), Collections.singletonList(ProductReference.class), true, true, 0, 1000).getResult();
            for(ProductReference ref : rows)
            {
                try
                {
                    ref.remove(ctx);
                }
                catch(Exception e)
                {
                    if(failed == null)
                    {
                        failed = new HashMap<>();
                    }
                    failed.put(ref.getPK(), e.getMessage());
                }
            }
        }
        while(rows.size() == 1000);
        if(failed != null)
        {
            for(ProductReference notRemoved : getSession().getItems(ctx, failed.keySet(), true, false))
            {
                if(notRemoved != null)
                {
                    PK pk = notRemoved.getPK();
                    LOG.error("Error removing product reference " + pk + " : " + (String)failed.get(pk) + " - ignored");
                }
            }
        }
    }


    protected void removeSyncTimestampsFor(SessionContext ctx, CatalogVersion catver)
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Removing sync timestamps for catalog version '" + catver.toString() + "' - this may take some time!");
        }
        int RANGE = 1000;
        List<ItemSyncTimestamp> rows = null;
        int total = 0;
        while(true)
        {
            rows = getSession().getFlexibleSearch()
                            .search(ctx, "SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + "*} WHERE {sourceVersion}=?item OR {targetVersion}=?item", Collections.singletonMap("item", catver), Collections.singletonList(ItemSyncTimestamp.class), true, true, 0, 1000)
                            .getResult();
            for(ItemSyncTimestamp ts : rows)
            {
                try
                {
                    ts.remove(ctx);
                }
                catch(ConsistencyCheckException consistencyCheckException)
                {
                }
            }
            total += rows.size();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Removed " + total + " sync timestamps for catalog version '" + catver.toString() + "' by now ...");
            }
            if(rows.size() != 1000)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Done removing sync timestamps for catalog version '" + catver.toString() + "'.");
                }
                return;
            }
        }
    }


    protected void removeSyncTimestampsFor(SessionContext ctx, Item forItem)
    {
        String query;
        if(Config.isMySQLUsed())
        {
            query = "SELECT tbl.pk FROM ({{SELECT {" + Item.PK + "} as pk FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + "*} WHERE {sourceItem}=?item }} UNION ALL {{SELECT {" + Item.PK + "} as pk FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP
                            + "*} WHERE {targetItem}=?item}}) tbl";
        }
        else
        {
            query = "SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + "*} WHERE {sourceItem}=?item OR {targetItem}=?item";
        }
        List<ItemSyncTimestamp> rows = getSession().getFlexibleSearch().search(ctx, query, Collections.singletonMap("item", forItem), Collections.singletonList(ItemSyncTimestamp.class), true, true, 0, -1).getResult();
        for(ItemSyncTimestamp ts : rows)
        {
            try
            {
                ts.remove(ctx);
            }
            catch(ConsistencyCheckException e)
            {
                LOG.error(e.getMessage(), (Throwable)e);
            }
        }
    }


    public Map<PK, ItemSyncTimestamp> getSyncTimestampMap(Item source, SyncItemJob sij)
    {
        boolean exclusive = sij.isExclusiveModeAsPrimitive();
        Map<Object, Object> params = new HashMap<>();
        params.put("src", source);
        if(exclusive)
        {
            params.put("job", sij);
        }
        else
        {
            params.put("tgtVer", sij.getTargetVersion());
        }
        List<ItemSyncTimestamp> rows = FlexibleSearch.getInstance()
                        .search("SELECT {" + Item.PK + "}FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + "*} WHERE {sourceItem}=?src AND " + (exclusive ? "" : "{targetVersion}=?tgtVer AND ") + "{syncJob}" + (exclusive ? "=?job" : "=0"), params, ItemSyncTimestamp.class).getResult();
        if(rows.isEmpty())
        {
            return Collections.EMPTY_MAP;
        }
        if(rows.size() == 1)
        {
            ItemSyncTimestamp ists = rows.get(0);
            return Collections.singletonMap(ists.getTargetItemPK(), ists);
        }
        Map<PK, ItemSyncTimestamp> ret = new HashMap<>(rows.size());
        for(ItemSyncTimestamp ts : rows)
        {
            ret.put(ts.getTargetItemPK(), ts);
        }
        return ret;
    }


    public ItemSyncTimestamp createSyncTimestamp(SyncItemJob job, Item source, Item copy)
    {
        return createSyncTimestamp(job, source, (copy == null) ? null : copy.getPK());
    }


    public ItemSyncTimestamp createSyncTimestamp(SyncItemJob job, Item source, PK copy)
    {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sourceItem", source);
        attributes.put("targetItem", (copy == null) ? null : new ItemPropertyValue(copy));
        if(job.isExclusiveModeAsPrimitive())
        {
            attributes.put("syncJob", job);
        }
        else
        {
            attributes.put("sourceVersion", job.getSourceVersion());
            attributes.put("targetVersion", job.getTargetVersion());
        }
        attributes.put("lastSyncSourceModifiedTime", source.getModificationTime());
        attributes.put("lastSyncTime", new Date());
        return createItemSyncTimestamp(attributes);
    }


    public List<ItemSyncTimestamp> getSynchronizedCopies(SessionContext ctx, Item source)
    {
        return getSession().getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + "} WHERE {sourceItem}=?src ORDER BY {lastSyncTime} DESC, {" + Item.CREATION_TIME + "} DESC",
                                        Collections.singletonMap("src", source), ItemSyncTimestamp.class)
                        .getResult();
    }


    public List<ItemSyncTimestamp> getSynchronizationSources(SessionContext ctx, Item target)
    {
        return getSession().getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + "} WHERE {targetItem}=?tgt ORDER BY {lastSyncTime} DESC, {" + Item.CREATION_TIME + "} DESC",
                                        Collections.singletonMap("tgt", target), ItemSyncTimestamp.class)
                        .getResult();
    }


    public ProductCatalogVersionDifference createProductCatalogVersionDifference(CatalogVersion sourceVersion, CatalogVersion targetVersion, CronJob cronJob, Product srcProduct, Product targetProduct, EnumerationValue mode)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("sourceVersion", sourceVersion);
        params.put("targetVersion", targetVersion);
        params.put("cronJob", cronJob);
        params.put("sourceProduct", srcProduct);
        params.put("targetProduct", targetProduct);
        params.put("mode", mode);
        return createProductCatalogVersionDifference(params);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CategoryCatalogVersionDifference createCategoryCatalogVersionDifference(CatalogVersion sourceVersion, CatalogVersion targetVersion, CronJob cronJob, Category srcCategory, Category targetCategory, EnumerationValue mode)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("sourceVersion", sourceVersion);
        params.put("targetVersion", targetVersion);
        params.put("cronJob", cronJob);
        params.put("sourceCategory", srcCategory);
        params.put("targetCategory", targetCategory);
        params.put("mode", mode);
        return createCategoryCatalogVersionDifference(params);
    }


    public CompareCatalogVersionsCronJob createDefaultCompareCatalogVersionCronJob(CatalogVersion source, CatalogVersion target, Customer priceCompareCustomer, Double tolerance, Boolean searchMissingProducts, Boolean searchMissingCategories, Boolean searchNewProducts, Boolean searchNewCategories,
                    Boolean searchPriceDiffs, Boolean overwriteStatus)
    {
        JaloSession jalosession = JaloSession.getCurrentSession();
        ComposedType compareCronJobType = jalosession.getTypeManager().getComposedType(CompareCatalogVersionsCronJob.class);
        Map<Object, Object> values = new HashMap<>();
        try
        {
            values.put("job", getOrCreateDefaultCompareCatalogVersionJob());
            values.put("sourceVersion", source);
            values.put("targetVersion", target);
            values.put("maxPriceTolerance", tolerance);
            values.put("searchMissingProducts", searchMissingProducts);
            values.put("searchMissingCategories", searchMissingCategories);
            values.put("searchNewProducts", searchNewProducts);
            values.put("searchNewCategories", searchNewCategories);
            values.put("searchPriceDifferences", searchPriceDiffs);
            values.put("priceCompareCustomer", priceCompareCustomer);
            return (CompareCatalogVersionsCronJob)compareCronJobType.newInstance(values);
        }
        catch(JaloGenericCreationException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public CompareCatalogVersionsJob getOrCreateDefaultCompareCatalogVersionJob()
    {
        CompareCatalogVersionsJob compareJob = (CompareCatalogVersionsJob)CronJobManager.getInstance().getJob(GeneratedCatalogConstants.TC.COMPARECATALOGVERSIONSJOB);
        if(compareJob == null)
        {
            compareJob = createCompareCatalogVersionsJob(
                            Collections.singletonMap("code", GeneratedCatalogConstants.TC.COMPARECATALOGVERSIONSJOB));
        }
        return compareJob;
    }


    public RemoveCatalogVersionJob getOrCreateDefaultRemoveCatalogVersionJob()
    {
        RemoveCatalogVersionJob job = (RemoveCatalogVersionJob)CronJobManager.getInstance().getJob(GeneratedCatalogConstants.TC.REMOVECATALOGVERSIONJOB);
        if(job == null)
        {
            Map<Object, Object> values = new HashMap<>();
            values.put("code", "RemoveCatalogVersionJob");
            job = createRemoveCatalogVersionJob(values);
        }
        return job;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<ComposedType> getAllCatalogItemRootTypes()
    {
        Set<ComposedType> returnSet = new HashSet<>();
        for(ComposedType comptype : getAllCatalogItemTypes())
        {
            ComposedType superComposedType = comptype.getSuperType();
            if(!isCatalogItem(superComposedType))
            {
                returnSet.add(comptype);
                continue;
            }
            Set<String> compColl = getUniqueKeyAttributeQualifiers(comptype);
            Set<String> superCompColl = getUniqueKeyAttributeQualifiers(superComposedType);
            if(compColl.size() != superCompColl.size() || !compColl.containsAll(superCompColl))
            {
                returnSet.add(comptype);
            }
        }
        return returnSet;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<ComposedType> getAllCatalogItemTypes()
    {
        return new HashSet<>(FlexibleSearch.getInstance()
                        .search("SELECT {" + Item.PK + "} FROM {" +
                                                        TypeManager.getInstance()
                                                                        .getComposedType(ComposedType.class)
                                                                        .getCode() + "} WHERE {" + GeneratedCatalogConstants.Attributes.ComposedType.CATALOGITEMTYPE + "}=?bool ",
                                        Collections.singletonMap("bool", Boolean.TRUE), ComposedType.class)
                        .getResult());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isCatalogItem(Item item)
    {
        return isCatalogItem(item.getComposedType());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isCatalogItem(ComposedType comptyp)
    {
        return ((CatalogItem.class.isAssignableFrom(comptyp.getJaloClass()) || isCatalogItemTypeAsPrimitive(comptyp)) &&
                        getCatalogVersionAttribute(comptyp) != null && hasUniqueKeyAttributes(comptyp));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Boolean isCatalogItemType(SessionContext ctx, ComposedType item)
    {
        Boolean check = super.isCatalogItemType(ctx, item);
        if(check == null)
        {
            for(ComposedType st = item.getSuperType(); check == null && st != null; st = st.getSuperType())
            {
                check = super.isCatalogItemType(ctx, st);
            }
        }
        return check;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isCatalogItem(Class itemclass)
    {
        return isCatalogItem(TypeManager.getInstance().getComposedType(itemclass));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setCatalogItemType(SessionContext ctx, ComposedType item, Boolean param)
    {
        item.setProperty(ctx, GeneratedCatalogConstants.Attributes.ComposedType.CATALOGITEMTYPE, param);
    }


    protected void checkCatalogItemTypeAttributeDescriptor(ComposedType comptyp, AttributeDescriptor atrdesc, boolean forVersion) throws JaloInvalidParameterException
    {
        if(atrdesc != null)
        {
            boolean isEnclosing = false;
            if(comptyp.equals(atrdesc.getEnclosingType()))
            {
                isEnclosing = true;
            }
            else
            {
                ComposedType superType = comptyp.getSuperType();
                while(superType != null && !isEnclosing)
                {
                    if(superType.equals(atrdesc.getEnclosingType()))
                    {
                        isEnclosing = true;
                        continue;
                    }
                    superType = superType.getSuperType();
                }
            }
            if(!isEnclosing)
            {
                throw new JaloInvalidParameterException("attribute " + atrdesc + " does not belong to catalog item type " + comptyp
                                .getCode(), 0);
            }
            if(forVersion)
            {
                Type type = atrdesc.getRealAttributeType();
                if(!(type instanceof ComposedType) || !CatalogVersion.class.isAssignableFrom(((ComposedType)type)
                                .getJaloClass()))
                {
                    throw new JaloInvalidParameterException("illegal type " + type + " for catalog version attribute " + atrdesc + " - must be assignable from CatalogVersion type", 0);
                }
            }
            if(!atrdesc.isSearchable())
            {
                throw new JaloInvalidParameterException("attribute " + atrdesc + " is not searchable", 0);
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CatalogVersion getCatalogVersion(Item item)
    {
        return getCatalogVersion(getSession().getSessionContext(), item);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CatalogVersion getCatalogVersion(SessionContext ctx, Item item) throws JaloInvalidParameterException
    {
        if(item == null)
        {
            return null;
        }
        if(item instanceof CatalogItem)
        {
            return ((CatalogItem)item).getCatalogVersion(ctx);
        }
        if(item instanceof Product)
        {
            return getCatalogVersion((Product)item);
        }
        if(item instanceof Media)
        {
            return getCatalogVersion((Media)item);
        }
        if(item instanceof Category)
        {
            return ((Category)item).getCatalogVersion();
        }
        if(isCatalogItem(item.getComposedType()))
        {
            AttributeDescriptor versionAttr = getCatalogVersionAttribute(ctx, item.getComposedType());
            if(versionAttr == null)
            {
                throw new JaloInvalidParameterException("catalog item " + item + " (type " + item
                                .getComposedType()
                                .getCode() + ") has no version attribute", 0);
            }
            try
            {
                return (CatalogVersion)item.getAttribute(ctx, versionAttr.getQualifier());
            }
            catch(Exception e)
            {
                if(e instanceof RuntimeException)
                {
                    throw (RuntimeException)e;
                }
                if(e instanceof JaloInvalidParameterException)
                {
                    throw (JaloInvalidParameterException)e;
                }
                throw new JaloSystemException(e);
            }
        }
        return null;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Item getCounterpartItem(Item sourceItem, CatalogVersion targetVersion) throws JaloInvalidParameterException
    {
        return getCounterpartItem(getSession().getSessionContext(), sourceItem, targetVersion);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Item getCounterpartItem(SessionContext ctx, Item sourceItem, CatalogVersion targetVersion) throws JaloInvalidParameterException
    {
        ComposedType comptyp = sourceItem.getComposedType();
        if(!isCatalogItem(comptyp))
        {
            throw new JaloInvalidParameterException("source item " + sourceItem + " is no instance of a catalog item type but of " + comptyp
                            .getCode(), 0);
        }
        if(sourceItem instanceof CatalogItem)
        {
            return ((CatalogItem)sourceItem).getCounterpartItem(targetVersion);
        }
        AttributeDescriptor versionAD = getCatalogVersionAttribute(ctx, comptyp);
        Collection<AttributeDescriptor> keyAttributes = getUniqueKeyAttributes(ctx, comptyp);
        if(keyAttributes == null || keyAttributes.isEmpty())
        {
            throw new JaloInvalidParameterException("no key attribute(s) defined for catalog item type " + comptyp.getCode(), 0);
        }
        StringBuilder strbuil = new StringBuilder();
        strbuil.append("SELECT {").append(Item.PK).append("} FROM {").append(comptyp.getCode()).append("} ");
        strbuil.append("WHERE {").append(versionAD.getQualifier()).append("}=?tgtVer ");
        Map<String, Object> params = new HashMap<>();
        params.put("tgtVer", targetVersion);
        for(AttributeDescriptor keyAD : keyAttributes)
        {
            if(!keyAD.isSearchable())
            {
                throw new JaloInvalidParameterException("key attribute " + keyAD.getQualifier() + " out of " + keyAttributes + " of catalog item type " + comptyp
                                .getCode() + " is not searchable", 0);
            }
            try
            {
                Object value = sourceItem.getAttribute(ctx, keyAD.getQualifier());
                strbuil.append(" AND {").append(keyAD.getQualifier()).append("}");
                if(value == null)
                {
                    strbuil.append(" IS NULL ");
                    continue;
                }
                String token = keyAD.getQualifier() + "KeyValue";
                strbuil.append("=?").append(token).append(" ");
                params.put(token, value);
            }
            catch(Exception e)
            {
                throw new JaloInvalidParameterException(e, 0);
            }
        }
        strbuil.append("ORDER BY {").append(Item.CREATION_TIME).append("} DESC");
        List<Item> items = FlexibleSearch.getInstance().search(strbuil.toString(), params, comptyp.getJaloClass()).getResult();
        return items.isEmpty() ? null : items.get(0);
    }


    public String getCatalogItemID(Item catalogItem) throws JaloInvalidParameterException
    {
        if(catalogItem instanceof Product)
        {
            return ((Product)catalogItem).getCode();
        }
        if(catalogItem instanceof Category)
        {
            return ((Category)catalogItem).getCode();
        }
        if(catalogItem instanceof Media)
        {
            return ((Media)catalogItem).getCode();
        }
        if(catalogItem instanceof CatalogItem)
        {
            return ((CatalogItem)catalogItem).getCatalogItemID();
        }
        throw new JaloInvalidParameterException("item " + catalogItem + " is no catalog item", 0);
    }


    public AttributeDescriptor getCatalogVersionAttribute(SessionContext ctx, ComposedType type)
    {
        try
        {
            String qual = getCatalogVersionAttributeQualifier(ctx, type);
            if(qual == null)
            {
                for(ComposedType st = type.getSuperType(); qual == null && st != null; st = st.getSuperType())
                {
                    qual = getCatalogVersionAttributeQualifier(ctx, st);
                }
            }
            return (qual == null) ? null : type.getAttributeDescriptorIncludingPrivate(qual);
        }
        catch(JaloItemNotFoundException e)
        {
            return null;
        }
    }


    public void setCatalogVersionAttribute(SessionContext ctx, ComposedType item, AttributeDescriptor attrdesc)
    {
        checkCatalogItemTypeAttributeDescriptor(item, attrdesc, true);
        setCatalogVersionAttributeQualifier(ctx, item, (attrdesc == null) ? null : attrdesc.getQualifier());
    }


    public Collection<AttributeDescriptor> getUniqueKeyAttributes(ComposedType item)
    {
        return super.getUniqueKeyAttributes(item);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean hasUniqueKeyAttributes(ComposedType comptyp)
    {
        String qual = getUniqueKeyAttributeQualifier(null, comptyp);
        if(qual == null)
        {
            for(ComposedType st = comptyp.getSuperType(); qual == null && st != null; st = st.getSuperType())
            {
                qual = getUniqueKeyAttributeQualifier(null, st);
            }
        }
        if(qual != null)
        {
            for(String quali : qual.split(","))
            {
                try
                {
                    if(comptyp.getAttributeDescriptorIncludingPrivate(quali) != null)
                    {
                        return true;
                    }
                }
                catch(JaloItemNotFoundException e)
                {
                    LOG.warn("Catalog version: invalid unique key attribute '" + quali + "' within " + comptyp
                                    .getCode() + " - ignoring");
                }
            }
        }
        return false;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<AttributeDescriptor> getUniqueKeyAttributes(SessionContext ctx, ComposedType item)
    {
        String qual = getUniqueKeyAttributeQualifier(ctx, item);
        if(qual == null)
        {
            for(ComposedType st = item.getSuperType(); qual == null && st != null; st = st.getSuperType())
            {
                qual = getUniqueKeyAttributeQualifier(ctx, st);
            }
        }
        Set<AttributeDescriptor> ret = null;
        if(qual != null)
        {
            for(String quali : qual.split(","))
            {
                String subqual = quali.trim();
                if(subqual.length() > 0)
                {
                    try
                    {
                        AttributeDescriptor attrdesc = item.getAttributeDescriptorIncludingPrivate(subqual);
                        if(attrdesc != null)
                        {
                            if(ret == null)
                            {
                                ret = new HashSet<>();
                            }
                            ret.add(attrdesc);
                        }
                    }
                    catch(JaloItemNotFoundException e)
                    {
                        LOG.warn("Catalog version: invalid unique key attribute '" + quali + "' within " + item
                                        .getCode() + " - ignoring");
                    }
                }
            }
        }
        return (ret == null) ? Collections.EMPTY_SET : ret;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<String> getUniqueKeyAttributeQualifiers(ComposedType comptyp)
    {
        String qual = getUniqueKeyAttributeQualifier(null, comptyp);
        if(qual == null)
        {
            for(ComposedType st = comptyp.getSuperType(); qual == null && st != null; st = st.getSuperType())
            {
                qual = getUniqueKeyAttributeQualifier(null, st);
            }
        }
        Set<String> ret = null;
        if(qual != null)
        {
            for(String quali : qual.split(","))
            {
                String subqual = quali.trim();
                if(subqual.length() > 0)
                {
                    if(ret == null)
                    {
                        ret = new HashSet<>(10);
                    }
                    ret.add(subqual);
                }
            }
        }
        return (ret == null) ? Collections.EMPTY_SET : ret;
    }


    public void setUniqueKeyAttributes(SessionContext ctx, ComposedType item, Collection<AttributeDescriptor> value)
    {
        StringBuilder sbuilder = new StringBuilder();
        for(AttributeDescriptor ad : value)
        {
            checkCatalogItemTypeAttributeDescriptor(item, ad, false);
            if(sbuilder.length() > 0)
            {
                sbuilder.append(",");
            }
            sbuilder.append(ad.getQualifier());
        }
        setUniqueKeyAttributeQualifier(ctx, item, (sbuilder.length() > 0) ? sbuilder.toString() : null);
    }


    public static Item getOldest(Collection<Item> items)
    {
        if(items == null || items.isEmpty())
        {
            return null;
        }
        if(items.size() == 1)
        {
            return items.iterator().next();
        }
        Item oldest = null;
        Date oldestDate = null;
        for(Iterator<Item> it = items.iterator(); it.hasNext(); )
        {
            Item item = it.next();
            Date date = null;
            if(oldest == null || oldestDate.after(date = item.getCreationTime()))
            {
                oldest = item;
                oldestDate = (date == null) ? item.getCreationTime() : date;
            }
        }
        return oldest;
    }


    public static final Object getFirstOne(Collection coll)
    {
        return (coll == null || coll.isEmpty()) ? null : coll.iterator().next();
    }


    protected Product findInCatalogVersion(CatalogVersion ver, Product original)
    {
        Collection coll = ver.getSameProducts(original);
        if(coll.size() > 1)
        {
            if(LOG.isEnabledFor((Priority)Level.WARN))
            {
                LOG.warn("Multiple products found in version " + ver
                                .getPK() + "(" + ver.getVersion() + ") for original " + original + " ( found " + coll + ") - returning oldest one");
            }
            return (Product)getOldest(coll);
        }
        return (Product)getFirstOne(coll);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected Category findInCatalogVersion(CatalogVersion ver, Category original)
    {
        Collection coll = ver.getSameCategories(original);
        if(coll.size() > 1)
        {
            if(LOG.isEnabledFor((Priority)Level.WARN))
            {
                LOG.warn("Multiple categories found in version " + ver
                                .getPK() + "(" + ver.getVersion() + ") for original " + original + " ( found " + coll + ") - returning oldest one");
            }
            return (Category)getOldest(coll);
        }
        return (Category)getFirstOne(coll);
    }


    protected Media findInCatalogVersion(CatalogVersion ver, Media original)
    {
        Collection coll = ver.getSameMedias(original);
        if(coll.size() > 1)
        {
            if(LOG.isEnabledFor((Priority)Level.WARN))
            {
                LOG.warn("Multiple medias found in version " + ver.getPK() + "(" + ver.getVersion() + ") for original " + original + " ( found " + coll + ") - returning oldest one");
            }
            return (Media)getOldest(coll);
        }
        return (Media)getFirstOne(coll);
    }


    public static boolean mustSetCatalogVersions(JaloSession jaloSession, HttpServletRequest req)
    {
        Collection coll = (Collection)jaloSession.getAttribute("catalogversions");
        return (coll == null || coll.isEmpty() || (coll
                        .size() == 1 && CatalogConstants.NO_VERSIONS_AVAILABLE_DUMMY.equals(coll.iterator().next())));
    }


    protected Collection filterByURL(HttpServletRequest request, Collection<?> allActiveVersions)
    {
        Collection ret = new ArrayList(allActiveVersions);
        String requestURL = request.getRequestURL().toString();
        String requestQuery = request.getQueryString();
        String requestStr = requestURL + requestURL;
        for(Iterator<CatalogVersion> it = ret.iterator(); it.hasNext(); )
        {
            CatalogVersion catalogVersion = it.next();
            Catalog catalog = catalogVersion.getCatalog();
            Collection patterns = catalog.getUrlPatterns();
            if(patterns != null && !patterns.isEmpty())
            {
                boolean matched = false;
                for(Iterator<String> urlIt = patterns.iterator(); !matched && urlIt.hasNext(); )
                {
                    String expr = urlIt.next();
                    try
                    {
                        matched = Pattern.matches(expr, requestStr);
                    }
                    catch(PatternSyntaxException e)
                    {
                        LOG.error("Illegal catalog pattern '" + expr + "'");
                        matched = false;
                    }
                }
                if(!matched)
                {
                    it.remove();
                }
                if(LOG.isDebugEnabled())
                {
                    if(matched)
                    {
                        LOG.debug("Request " + requestStr + " matched url patterns " + patterns + ". Added version " + catalogVersion + ".");
                        continue;
                    }
                    LOG.debug("Request " + requestStr + " did not match url patterns " + patterns + ". Filtered version " + catalogVersion + ".");
                }
                continue;
            }
            it.remove();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Hiding catalogversion: " + catalogVersion + " since it is not restricted by URL pattern.");
            }
        }
        return ret;
    }


    public PreviewTicket createPreviewTicket(CatalogVersion version)
    {
        return createPreviewTicket(version, getSession().getUser(), new Date(
                        System.currentTimeMillis() + Config.getLong("catalog.previewticket.timeout", 300L) * 1000L));
    }


    public PreviewTicket createPreviewTicket(CatalogVersion version, User createdBy, Date validTo)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("previewCatalogVersion", version);
        params.put("createdBy", createdBy);
        params.put("validTo", validTo);
        return createPreviewTicket(params);
    }


    protected Collection<PreviewTicket> collectPreviewTicketVersions(HttpServletRequest request, JaloSession jaloSession)
    {
        long currentTime = System.currentTimeMillis();
        String querystring = request.getQueryString();
        String requestURL = "" + request.getRequestURL() + request.getRequestURL();
        if(requestURL.length() == 0)
        {
            return Collections.EMPTY_SET;
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Checking request '" + requestURL + "' for preview tickets");
        }
        Collection<PreviewTicket> tickets = new HashSet<>();
        int offset = "{[y]PreviewTicket:".length();
        int pos;
        for(pos = requestURL.indexOf("{[y]PreviewTicket:"); pos >= 0;
                        pos = requestURL.indexOf("{[y]PreviewTicket:", pos + offset))
        {
            int endPos = requestURL.indexOf(":[y]}", pos + offset);
            if(endPos < 0)
            {
                String error = "incomplete ticket code in request '" + requestURL + "' at " + pos + " - ignored";
                if(LOG.isEnabledFor((Priority)Level.ERROR))
                {
                    LOG.error(error);
                }
                throw new JaloSystemException(error);
            }
            String ticketPK = requestURL.substring(pos + offset, endPos);
            try
            {
                PreviewTicket ticket = (PreviewTicket)jaloSession.getItem(PK.parse(ticketPK));
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Found ticket " + ticket.getTicketCode());
                }
                if(ticket.getValidTo().getTime() >= currentTime)
                {
                    tickets.add(ticket);
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug("Added ticket " + ticket.getTicketCode() + " to list");
                    }
                }
                else
                {
                    String error = "ticket {[y]PreviewTicket:" + ticketPK + ":[y]} is expired - cannot use for request " + requestURL;
                    if(LOG.isEnabledFor((Priority)Level.WARN))
                    {
                        LOG.warn(error);
                    }
                    throw new JaloSystemException(error);
                }
            }
            catch(JaloItemNotFoundException e)
            {
                String error = "ticket {[y]PreviewTicket:" + ticketPK + ":[y]} is no longer available - cannot use for request " + requestURL;
                if(LOG.isEnabledFor((Priority)Level.WARN))
                {
                    LOG.warn(error);
                }
                throw new JaloSystemException(error);
            }
            catch(IllegalArgumentException e)
            {
                String error = "ticket {[y]PreviewTicket:" + ticketPK + ":[y]} is invalid (pk invalid) - cannot use for request " + requestURL;
                if(LOG.isEnabledFor((Priority)Level.ERROR))
                {
                    LOG.error(error);
                }
                throw new JaloSystemException(error);
            }
            catch(ClassCastException e)
            {
                String error = "ticket {[y]PreviewTicket:" + ticketPK + ":[y]} is invalid (wrong class) - cannot use for request " + requestURL;
                if(LOG.isEnabledFor((Priority)Level.ERROR))
                {
                    LOG.error(error);
                }
                throw new JaloSystemException(error);
            }
        }
        return tickets;
    }


    protected void assignCatalogVersions(HttpServletRequest request, JaloSession jaloSession, Collection<CatalogVersion> versions, boolean previewMode)
    {
        if(versions == null || versions.isEmpty())
        {
            Catalog def = getDefaultCatalog();
            CatalogVersion defact = null;
            if(def != null)
            {
                defact = def.getActiveCatalogVersion();
            }
            jaloSession.setAttribute("catalogversions",
                            Collections.singletonList(
                                            (defact == null) ? CatalogConstants.NO_VERSIONS_AVAILABLE_DUMMY : defact));
            if(LOG.isEnabledFor((Priority)Level.DEBUG))
            {
                String reqURI = (request == null) ? "<n/a>" : request.getRequestURI();
                LOG.debug("No active versions available for request " + reqURI + "!");
            }
        }
        else
        {
            jaloSession.setAttribute("catalogversions", versions);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Active versions now are " + jaloSession.getAttribute("catalogversions"));
            }
        }
        if(previewMode)
        {
            jaloSession.setAttribute("previewModeActivation", Boolean.TRUE);
        }
        else
        {
            jaloSession.removeAttribute("previewModeActivation");
        }
    }


    public void doPreRequest(HttpServletRequest request, HttpServletResponse response)
    {
        JaloSession jaloSession = WebSessionFunctions.tryGetJaloSession(request.getSession());
        if(jaloSession == null)
        {
            if(LOG.isEnabledFor((Priority)Level.WARN))
            {
                LOG.warn("Missing jalosession for request " + request + " - cannot select catalog versions");
            }
            return;
        }
        Collection<PreviewTicket> tickets = collectPreviewTicketVersions(request, jaloSession);
        if(tickets.isEmpty())
        {
            if(mustSetCatalogVersions(jaloSession, request))
            {
                assignCatalogVersions(request, jaloSession,
                                filterByURL(request, getAllActiveCatalogVersionsIgnoringRestrictions()), false);
            }
            else if(LOG.isDebugEnabled())
            {
                LOG.debug("Jalosession " + jaloSession + " already got its catalog versions " + jaloSession
                                .getAttribute("catalogversions"));
            }
        }
        else
        {
            Collection<CatalogVersion> versions = new HashSet<>();
            for(PreviewTicket ticket : tickets)
            {
                versions.add(ticket.getPreviewCatalogVersion());
                ticket.notifyTicketTaken(request, response, jaloSession);
            }
            assignCatalogVersions(request, jaloSession, versions, true);
        }
    }


    public void doPostRequest(HttpServletRequest request, HttpServletResponse response)
    {
        JaloSession jaloSession = WebSessionFunctions.tryGetJaloSession(request.getSession());
        if(jaloSession != null)
        {
            jaloSession.removeAttribute("previewModeActivation");
        }
    }


    public boolean canSync(SessionContext ctx, User sessionUser, SyncItemJob sync)
    {
        if(sync.getSyncPrincipals(ctx) == null || sync.getSyncPrincipals(ctx).isEmpty())
        {
            return canWrite(ctx, sessionUser, sync
                            .getTargetVersion());
        }
        if(sync.getSyncPrincipals(ctx).contains(sessionUser))
        {
            return true;
        }
        for(Iterator it = sessionUser.getGroups().iterator(); it.hasNext(); )
        {
            if(sync.getSyncPrincipals(ctx).contains(it.next()))
            {
                return true;
            }
        }
        if(sync.isSyncPrincipalsOnly(ctx).booleanValue())
        {
            return false;
        }
        return canWrite(ctx, sessionUser, sync.getTargetVersion());
    }


    public boolean canWrite(SessionContext ctx, User sessionUser, CatalogVersion catalogVersion)
    {
        return (sessionUser.isAdmin() || catalogVersion == null ||
                        getAllWriteableCatalogVersions(ctx, sessionUser).contains(catalogVersion));
    }


    public boolean canRead(SessionContext ctx, User sessionUser, CatalogVersion catalogVersion)
    {
        return (sessionUser.isAdmin() || getAllReadableCatalogVersions(ctx, sessionUser).contains(catalogVersion));
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "UserAllWriteableCatalogVersionsAttributeHandler", portingMethod = "get(final UserModel model)")
    public Collection<CatalogVersion> getAllWriteableCatalogVersions(User user)
    {
        return getAllWriteableCatalogVersions(getSession().getSessionContext(), user);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "UserAllWriteableCatalogVersionsAttributeHandler", portingMethod = "get(final UserModel model)")
    public Collection<CatalogVersion> getAllWriteableCatalogVersions(SessionContext ctx, User user)
    {
        SessionContext searchCtx = JaloSession.getCurrentSession().createSessionContext();
        searchCtx.setAttribute("disableRestrictions", Boolean.TRUE);
        Collection<Principal> principals = new HashSet<>(10);
        principals.add(user);
        principals.addAll(user.getAllGroups());
        return new LinkedHashSet<>(LinkManager.getInstance().getAllLinkedItems(searchCtx, principals, true, GeneratedCatalogConstants.Relations.PRINCIPAL2WRITEABLECATALOGVERSIONRELATION, null));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<CatalogVersion> getAllReadableCatalogVersions(SessionContext ctx, User user)
    {
        SessionContext searchCtx = JaloSession.getCurrentSession().createSessionContext();
        searchCtx.setAttribute("disableRestrictions", Boolean.TRUE);
        Collection<Principal> principals = new HashSet<>(10);
        principals.add(user);
        principals.addAll(user.getAllGroups());
        Collection<CatalogVersion> ret = new LinkedHashSet<>(30);
        ret.addAll(LinkManager.getInstance().getAllLinkedItems(searchCtx, principals, true, GeneratedCatalogConstants.Relations.PRINCIPAL2READABLECATALOGVERSIONRELATION, null));
        ret.addAll(LinkManager.getInstance().getAllLinkedItems(searchCtx, principals, true, GeneratedCatalogConstants.Relations.PRINCIPAL2WRITEABLECATALOGVERSIONRELATION, null));
        return ret;
    }


    public void addToCatalogReadersAndWriters()
    {
        Catalog defaultCatalog = getDefaultCatalog();
        CatalogVersion onlineVersion = defaultCatalog.getCatalogVersion("Online");
        CatalogVersion offlineVersion = defaultCatalog.getCatalogVersion("Staged");
        if(onlineVersion != null && offlineVersion != null)
        {
            UserGroup editors = getSession().getUserManager().getUserGroupByGroupID(Constants.USER.EMPLOYEE_USERGROUP);
            Collection test = offlineVersion.getReadPrincipals();
            if(!test.contains(editors))
            {
                offlineVersion.addToReadPrincipals((Principal)editors);
            }
            test = offlineVersion.getWritePrincipals();
            if(!test.contains(editors))
            {
                offlineVersion.addToWritePrincipals((Principal)editors);
            }
            test = onlineVersion.getReadPrincipals();
            if(!test.contains(editors))
            {
                onlineVersion.addToReadPrincipals((Principal)editors);
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public int getTotalObjectsCount(Catalog catalog)
    {
        int count = 0;
        Collection versions = catalog.getCatalogVersions();
        if(versions != null && !versions.isEmpty())
        {
            for(Iterator<CatalogVersion> it = versions.iterator(); it.hasNext(); )
            {
                count += getTotalObjectsCount(it.next());
            }
        }
        return count;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public int getTotalObjectsCount(CatalogVersion version)
    {
        int count = 0;
        count += version.getAllProductCount();
        count += version.getAllCategoryCount();
        count += version.getAllMediaCount();
        count += version.getAllKeywordCount();
        return count;
    }


    public boolean isEditorUser(User user)
    {
        try
        {
            UserGroup editors = UserManager.getInstance().getUserGroupByGroupID(Constants.USER.EMPLOYEE_USERGROUP);
            return (editors != null && user.getAllGroups().contains(editors));
        }
        catch(JaloItemNotFoundException jaloItemNotFoundException)
        {
            return false;
        }
    }


    public boolean isEditable(Item item, AttributeDescriptor descriptor)
    {
        String quali = (descriptor == null) ? null : descriptor.getQualifier();
        if(GeneratedCatalogConstants.Attributes.Principal.WRITABLECATALOGVERSIONS.equalsIgnoreCase(quali) && Principal.class
                        .isAssignableFrom(descriptor.getEnclosingType().getJaloClass()))
        {
            UserGroup empl;
            Principal principal = (Principal)item;
            if(principal == null)
            {
                return true;
            }
            try
            {
                empl = UserManager.getInstance().getUserGroupByGroupID(Constants.USER.EMPLOYEE_USERGROUP);
            }
            catch(JaloItemNotFoundException e)
            {
                return true;
            }
            return (empl == null || empl.equals(principal) || principal.isMemberOf((PrincipalGroup)empl, true));
        }
        if(GeneratedCatalogConstants.Attributes.Principal.READABLECATALOGVERSIONS.equalsIgnoreCase(quali) && Principal.class
                        .isAssignableFrom(descriptor.getEnclosingType().getJaloClass()))
        {
            UserGroup empl;
            Principal principal = (Principal)item;
            if(principal == null)
            {
                return true;
            }
            try
            {
                empl = UserManager.getInstance().getUserGroupByGroupID(Constants.USER.EMPLOYEE_USERGROUP);
            }
            catch(JaloItemNotFoundException e)
            {
                return true;
            }
            return (empl == null || empl.equals(principal) || principal.isMemberOf((PrincipalGroup)empl, true));
        }
        JaloSession session = JaloSession.getCurrentSession();
        ComposedType comptyp = (item == null) ? ((descriptor == null) ? null : descriptor.getEnclosingType()) : item.getComposedType();
        User user = session.getUser();
        if(item == null || !isEditorUser(user) || comptyp == null || !isCatalogItem(comptyp))
        {
            return true;
        }
        try
        {
            return canWrite(session.getSessionContext(), user, getCatalogVersion(session.getSessionContext(), item));
        }
        catch(JaloInvalidParameterException jaloInvalidParameterException)
        {
            return true;
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<Language> getRestrictedLanguages(Item item)
    {
        CatalogVersion ver = getCatalogVersion(getSession().getSessionContext(), item);
        Collection<Language> storedLanguages = (ver == null) ? null : ver.getLanguages();
        return (ver != null && storedLanguages != null && !storedLanguages.isEmpty()) ? new HashSet<>(storedLanguages) : null;
    }


    public void createProjectData(Map params, JspContext jspc)
    {
    }


    public String getClassificationIndexString(SessionContext ctx, Product prod)
    {
        Collection<ClassificationClass> classes = getClassificationClasses(prod);
        if(classes != null && !classes.isEmpty())
        {
            StringBuilder result = new StringBuilder();
            FeatureContainer all = FeatureContainer.loadTyped(prod, new HashSet<>(classes));
            for(ClassificationClass cl : all.getClasses())
            {
                String clName = cl.getName(ctx);
                if(clName != null)
                {
                    result.append(clName).append("/");
                }
                result.append(cl.getCode() + " : ");
                for(TypedFeature tf : all.getFeatures(cl))
                {
                    ClassAttributeAssignment asgnmt = tf.getClassAttributeAssignment();
                    if(!asgnmt.isSearchableAsPrimitive())
                    {
                        continue;
                    }
                    if(tf.isEmpty())
                    {
                        continue;
                    }
                    ClassificationAttribute attr = asgnmt.getClassificationAttribute();
                    String attrName = attr.getName(ctx);
                    if(attrName != null)
                    {
                        result.append(attrName).append("/");
                    }
                    result.append(attr.getCode()).append("=").append(tf.getValuesFormatted(ctx)).append(" ; ");
                }
            }
            return (result.length() > 0) ? result.toString() : null;
        }
        return null;
    }


    public Map<Language, String> getAllClassificationIndexString(SessionContext ctx, Product item)
    {
        Collection<? extends Language> languages = C2LManager.getInstance().getAllLanguages();
        HashMap<Object, Object> result = new HashMap<>();
        SessionContext _ctx = new SessionContext(ctx);
        for(Language lang : languages)
        {
            _ctx.setLanguage(lang);
            result.put(lang, getClassificationIndexString(_ctx, item));
        }
        return (Map)result;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public String getCatalogVersionAttributeQualifier(ComposedType item)
    {
        return super.getCatalogVersionAttributeQualifier(item);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "PrincipalCatalogVersionsPrepareInterceptor", portingMethod = "onPrepare(final PrincipalModel principalModel, final InterceptorContext ctx)")
    public void setReadableCatalogVersions(SessionContext ctx, Principal principal, List<CatalogVersion> value)
    {
        super.setReadableCatalogVersions(ctx, principal,
                        (value == null) ? null : new ArrayList(new LinkedHashSet(value)));
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "PrincipalCatalogVersionsPrepareInterceptor", portingMethod = "onPrepare(final PrincipalModel principalModel, final InterceptorContext ctx)")
    public void setWritableCatalogVersions(SessionContext ctx, Principal principal, List<CatalogVersion> value)
    {
        super.setWritableCatalogVersions(ctx, principal, value);
        if(value != null)
        {
            Set<CatalogVersion> readable = new LinkedHashSet<>(getReadableCatalogVersions(ctx, principal));
            if(readable.addAll(value))
            {
                super.setReadableCatalogVersions(ctx, principal, new ArrayList<>(readable));
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Map<Language, List<Keyword>> getAllKeywords(SessionContext ctx, Category item)
    {
        return item.getAllKeywords(ctx);
    }


    protected String generateDuplicateQuery(Class jaloClass)
    {
        ComposedType catalogComposedType = TypeManager.getInstance().getComposedType(jaloClass);
        StringBuilder query = new StringBuilder();
        String catverqualifier = getCatalogVersionAttribute(catalogComposedType).getQualifier();
        String uniqueQuery = createUniquePart(getUniqueKeyAttributes(catalogComposedType));
        query.append("SELECT {ct:").append(Item.PK).append("} FROM {");
        query.append(catalogComposedType.getCode());
        query.append(" AS ct}\nWHERE\n\t{ct:");
        query.append(catverqualifier);
        query.append("} = ?version AND\n\tEXISTS ({{\n\tSELECT {ct:").append(Item.PK).append("} FROM {");
        query.append(catalogComposedType.getCode());
        query.append("}\n\tWHERE\n\t{").append(catverqualifier).append("} = ?version AND\n\t{ct:");
        query.append(Item.PK).append("} <> {");
        query.append(Item.PK);
        query.append("} AND\n\t");
        query.append(uniqueQuery);
        query.append("\n}})");
        return query.toString();
    }


    private String createUniquePart(Collection<AttributeDescriptor> attrDescColl)
    {
        StringBuffer query = new StringBuffer(256);
        int index = 0;
        for(AttributeDescriptor attrDesc : attrDescColl)
        {
            query.append("{ct:");
            query.append(attrDesc.getQualifier());
            query.append("} = {");
            query.append(attrDesc.getQualifier());
            query.append("} ");
            if(index++ < attrDescColl.size() - 1)
            {
                query.append("AND\n\t");
            }
        }
        return query.toString();
    }


    public List<Principal> getAllowedPrincipals(SessionContext ctx, Category item)
    {
        return item.getAllowedPrincipals(ctx);
    }


    public List<Principal> getAllowedPrincipals(Category item)
    {
        return item.getAllowedPrincipals();
    }


    public long getAllowedPrincipalsCount(SessionContext ctx, Category item)
    {
        return item.getAllowedPrincipalsCount(ctx);
    }


    public long getAllowedPrincipalsCount(Category item)
    {
        return item.getAllowedPrincipalsCount();
    }


    public void setAllowedPrincipalsOnlyForGivenCategory(SessionContext ctx, Category item, List<Principal> value)
    {
        item.setAllowedPrincipalsOnlyForPassedCategory(ctx, value);
    }


    public void setAllowedPrincipals(Category item, List<Principal> value)
    {
        item.setAllowedPrincipals(value);
    }


    public void addToAllowedPrincipals(SessionContext ctx, Category item, Principal value)
    {
        item.addToAllowedPrincipals(ctx, value);
    }


    public void addToAllowedPrincipals(Category item, Principal value)
    {
        item.addToAllowedPrincipals(value);
    }


    public void removeFromAllowedPrincipals(SessionContext ctx, Category item, Principal value)
    {
        item.removeFromAllowedPrincipals(ctx, value);
    }


    public void removeFromAllowedPrincipals(Category item, Principal value)
    {
        item.removeFromAllowedPrincipals(value);
    }


    public CatalogVersion getCatalogVersion(SessionContext ctx, Category item)
    {
        return item.getCatalogVersion(ctx);
    }


    public CatalogVersion getCatalogVersion(Category item)
    {
        return item.getCatalogVersion();
    }


    public void setCatalogVersion(Category item, CatalogVersion value)
    {
        item.setCatalogVersion(value);
    }


    public Collection<Media> getData_sheet(SessionContext ctx, Category item)
    {
        return item.getData_sheet(ctx);
    }


    public Collection<Media> getData_sheet(Category item)
    {
        return item.getData_sheet();
    }


    public void setData_sheet(SessionContext ctx, Category item, Collection<Media> value)
    {
        item.setData_sheet(ctx, value);
    }


    public void setData_sheet(Category item, Collection<Media> value)
    {
        item.setData_sheet(value);
    }


    public String getDescription(SessionContext ctx, Category item)
    {
        return item.getDescription(ctx);
    }


    public String getDescription(Category item)
    {
        return item.getDescription();
    }


    public Map<Language, String> getAllDescription(SessionContext ctx, Category item)
    {
        return item.getAllDescription(ctx);
    }


    public Map<Language, String> getAllDescription(Category item)
    {
        return item.getAllDescription();
    }


    public void setDescription(SessionContext ctx, Category item, String value)
    {
        item.setDescription(ctx, value);
    }


    public void setDescription(Category item, String value)
    {
        item.setDescription(value);
    }


    public void setAllDescription(SessionContext ctx, Category item, Map<Language, String> value)
    {
        item.setAllDescription(ctx, value);
    }


    public void setAllDescription(Category item, Map<Language, String> value)
    {
        item.setAllDescription(value);
    }


    public Collection<Media> getDetail(SessionContext ctx, Category item)
    {
        return item.getDetail(ctx);
    }


    public Collection<Media> getDetail(Category item)
    {
        return item.getDetail();
    }


    public void setDetail(SessionContext ctx, Category item, Collection<Media> value)
    {
        item.setDetail(ctx, value);
    }


    public void setDetail(Category item, Collection<Media> value)
    {
        item.setDetail(value);
    }


    public List<Keyword> getKeywords(SessionContext ctx, Category item)
    {
        return item.getKeywords(ctx);
    }


    public List<Keyword> getKeywords(Category item)
    {
        return item.getKeywords();
    }


    public Map<Language, List<Keyword>> getAllKeywords(Category item)
    {
        return item.getAllKeywords();
    }


    public long getKeywordsCount(SessionContext ctx, Category item, Language lang)
    {
        return item.getKeywordsCount(ctx, lang);
    }


    public long getKeywordsCount(Category item, Language lang)
    {
        return item.getKeywordsCount(lang);
    }


    public long getKeywordsCount(SessionContext ctx, Category item)
    {
        return item.getKeywordsCount(ctx);
    }


    public long getKeywordsCount(Category item)
    {
        return item.getKeywordsCount();
    }


    public void setKeywords(SessionContext ctx, Category item, List<Keyword> value)
    {
        item.setKeywords(ctx, value);
    }


    public void setKeywords(Category item, List<Keyword> value)
    {
        item.setKeywords(value);
    }


    public void setAllKeywords(SessionContext ctx, Category item, Map<Language, List<Keyword>> value)
    {
        item.setAllKeywords(ctx, value);
    }


    public void setAllKeywords(Category item, Map<Language, List<Keyword>> value)
    {
        item.setAllKeywords(value);
    }


    public void addToKeywords(SessionContext ctx, Category item, Language lang, Keyword value)
    {
        item.addToKeywords(ctx, lang, value);
    }


    public void addToKeywords(Category item, Language lang, Keyword value)
    {
        item.addToKeywords(lang, value);
    }


    public void removeFromKeywords(SessionContext ctx, Category item, Language lang, Keyword value)
    {
        item.removeFromKeywords(ctx, lang, value);
    }


    public void removeFromKeywords(Category item, Language lang, Keyword value)
    {
        item.removeFromKeywords(lang, value);
    }


    public Collection<Media> getLogo(SessionContext ctx, Category item)
    {
        return item.getLogo(ctx);
    }


    public Collection<Media> getLogo(Category item)
    {
        return item.getLogo();
    }


    public void setLogo(SessionContext ctx, Category item, Collection<Media> value)
    {
        item.setLogo(ctx, value);
    }


    public void setLogo(Category item, Collection<Media> value)
    {
        item.setLogo(value);
    }


    public Collection<Media> getNormal(SessionContext ctx, Category item)
    {
        return item.getNormal(ctx);
    }


    public Collection<Media> getNormal(Category item)
    {
        return item.getNormal();
    }


    public void setNormal(SessionContext ctx, Category item, Collection<Media> value)
    {
        item.setNormal(ctx, value);
    }


    public void setNormal(Category item, Collection<Media> value)
    {
        item.setNormal(value);
    }


    public Integer getOrder(SessionContext ctx, Category item)
    {
        return item.getOrder(ctx);
    }


    public Integer getOrder(Category item)
    {
        return item.getOrder();
    }


    public int getOrderAsPrimitive(SessionContext ctx, Category item)
    {
        return item.getOrderAsPrimitive(ctx);
    }


    public int getOrderAsPrimitive(Category item)
    {
        return item.getOrderAsPrimitive();
    }


    public void setOrder(SessionContext ctx, Category item, Integer value)
    {
        item.setOrder(ctx, value);
    }


    public void setOrder(Category item, Integer value)
    {
        item.setOrder(value);
    }


    public void setOrder(SessionContext ctx, Category item, int value)
    {
        item.setOrder(ctx, value);
    }


    public void setOrder(Category item, int value)
    {
        item.setOrder(value);
    }


    public Collection<Media> getOthers(SessionContext ctx, Category item)
    {
        return item.getOthers(ctx);
    }


    public Collection<Media> getOthers(Category item)
    {
        return item.getOthers();
    }


    public void setOthers(SessionContext ctx, Category item, Collection<Media> value)
    {
        item.setOthers(ctx, value);
    }


    public void setOthers(Category item, Collection<Media> value)
    {
        item.setOthers(value);
    }


    public Collection<Media> getThumbnails(SessionContext ctx, Category item)
    {
        return item.getThumbnails(ctx);
    }


    public Collection<Media> getThumbnails(Category item)
    {
        return item.getThumbnails();
    }


    public void setThumbnails(SessionContext ctx, Category item, Collection<Media> value)
    {
        item.setThumbnails(ctx, value);
    }


    public void setThumbnails(Category item, Collection<Media> value)
    {
        item.setThumbnails(value);
    }


    public static Boolean isSyncInProgress(SessionContext ctx)
    {
        if(ctx == null)
        {
            return Boolean.FALSE;
        }
        Boolean result = (Boolean)ctx.getAttribute("catalog.sync.active");
        return (result == null) ? Boolean.FALSE : result;
    }


    public static boolean isSyncInProgressAsPrimitive(SessionContext ctx)
    {
        return isSyncInProgress(ctx).booleanValue();
    }


    public List<ComposedType> getDefaultRootTypes()
    {
        ComposedType type = TypeManager.getInstance().getComposedType("Item");
        List<ComposedType> rootTypes = checkForRootTypes(type.getSubTypes());
        Collections.sort(rootTypes, (Comparator<? super ComposedType>)new Object(this));
        return rootTypes;
    }


    private List<ComposedType> checkForRootTypes(Set<ComposedType> subTypes)
    {
        List<ComposedType> rootTypes = new ArrayList<>();
        for(ComposedType type : subTypes)
        {
            if(type.getSubTypes().isEmpty())
            {
                if(checkForRootType(type))
                {
                    rootTypes.add(type);
                }
                continue;
            }
            if(checkForRootType(type))
            {
                rootTypes.add(type);
                continue;
            }
            rootTypes.addAll(checkForRootTypes(type
                            .getSubTypes()));
        }
        return rootTypes;
    }


    private boolean checkForRootType(ComposedType type)
    {
        CatalogManager catalogManager = getInstance();
        return (catalogManager.isCatalogItem(type) && type.getProperty("catalog.sync.default.root.type") != null && ((Boolean)type
                        .getProperty("catalog.sync.default.root.type")).booleanValue());
    }


    public void createEssentialData(Map params, JspContext jspc) throws Exception
    {
        getInstance().getVariantsManager().createEssentialData(params, jspc);
        getInstance().getCategoryManager().createEssentialData(params, jspc);
        getInstance().createUsersAndGroupsAndRights();
        if(getInstance().createDefaultCatalog(jspc))
        {
            getInstance().configureDefaultSynchronization(jspc);
            getInstance().addToCatalogReadersAndWriters();
        }
        if("init".equals(params.get("initmethod")))
        {
            createSearchRestrictions(jspc);
        }
        getInstance().createSavedQueries(jspc);
        getInstance().getOrCreateDefaultCompareCatalogVersionJob();
        getInstance().getOrCreateDefaultRemoveCatalogVersionJob();
        getInstance().createCatalogSyncMediaFolder();
        TypeViewUtilities tvu = new TypeViewUtilities();
        ViewType dcicv = (ViewType)getFirstItemByAttribute(ViewType.class, ViewType.CODE, "DuplicateCatalogItemCodesView");
        tvu.generateQuery(dcicv);
        ViewType cov = (ViewType)getFirstItemByAttribute(ViewType.class, ViewType.CODE, "CatalogOverview");
        tvu.generateQuery(cov);
    }


    private void createSearchRestrictions(JspContext jspc)
    {
        String alias = "item";
        TypeManager typeman = getSession().getTypeManager();
        UserGroup catalogViewers = getSession().getUserManager().getUserGroupByGroupID(Constants.USER.CUSTOMER_USERGROUP);
        if(typeman.getSearchRestriction(typeman.getComposedType(Category.class), "Frontend_Category") == null)
        {
            typeman.createRestriction("Frontend_Category", (Principal)catalogViewers, typeman.getComposedType(Category.class), "{item:" + CatalogConstants.Attributes.Category.CATALOGVERSION + "} IN (?session.catalogversions)");
        }
        if(typeman.getSearchRestriction(typeman.getComposedType(Category.class), "Frontend_RestrictedCategory") == null)
        {
            typeman.createRestriction("Frontend_RestrictedCategory", (Principal)catalogViewers, typeman.getComposedType(Category.class),
                            "EXISTS ({{SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.Relations.CATEGORY2PRINCIPALRELATION + "} WHERE {source}={item:" + Item.PK + "} AND {target} IN ( ?session.user, ?session.user.allGroups ) }})");
        }
        if(typeman.getSearchRestriction(typeman.getComposedType(Product.class), "Frontend_ProductOfflineDate") == null)
        {
            typeman.createRestriction("Frontend_ProductOfflineDate", (Principal)catalogViewers, typeman.getComposedType(Product.class),
                            "( {" + GeneratedCatalogConstants.Attributes.Product.ONLINEDATE + "} IS NULL OR {" + GeneratedCatalogConstants.Attributes.Product.ONLINEDATE + "} <= ?session.user.currentDate) AND ( {" + GeneratedCatalogConstants.Attributes.Product.OFFLINEDATE + "} IS NULL OR {"
                                            + GeneratedCatalogConstants.Attributes.Product.OFFLINEDATE + "} >= ?session.user.currentDate)");
        }
        if(typeman.getSearchRestriction(typeman.getComposedType(Product.class), "Frontend_ProductApprovalStatus") == null)
        {
            typeman
                            .createRestriction("Frontend_ProductApprovalStatus", (Principal)catalogViewers, typeman
                                            .getComposedType(Product.class), "{" + GeneratedCatalogConstants.Attributes.Product.APPROVALSTATUS + "} = " +
                                            JDBCValueMappings.getInstance()
                                                            .pkToSQL(EnumerationManager.getInstance()
                                                                            .getEnumerationValue(GeneratedCatalogConstants.TC.ARTICLEAPPROVALSTATUS, GeneratedCatalogConstants.Enumerations.ArticleApprovalStatus.APPROVED)
                                                                            .getPK()) + " ");
        }
        if(typeman.getSearchRestriction(typeman.getComposedType(Product.class), "Frontend_Product") == null)
        {
            SearchRestriction res = typeman.createRestriction("Frontend_Product", (Principal)catalogViewers, typeman
                            .getComposedType(Product.class), "{item:catalogVersion} IN (?session.catalogversions)");
            if(typeman.getSearchRestriction(typeman.getComposedType(Product.class), "Frontend_Assignment") != null)
            {
                res.setActive(false);
            }
        }
        if(typeman.getSearchRestriction(typeman.getComposedType(Product.class), "Frontend_Assignment") == null)
        {
            SearchRestriction res = typeman.createRestriction("Frontend_Assignment", (Principal)catalogViewers, typeman
                                            .getComposedType(Product.class),
                            "EXISTS ({{SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CATEGORY + " AS cat JOIN " + GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION + " AS c2p ON {cat:" + Item.PK + "}={c2p:source} } WHERE {c2p:target}={item:" + Item.PK + "} AND {cat."
                                            + CatalogConstants.Attributes.Category.CATALOGVERSION + "} NOT IN ({{SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CLASSIFICATIONSYSTEMVERSION + "} }})}})");
            res.setActive(false);
        }
        if(typeman.getSearchRestriction(typeman.getComposedType(VariantProduct.class), "Frontend_Assignment") == null)
        {
            SearchRestriction res = typeman.createRestriction("Frontend_Assignment", (Principal)catalogViewers, typeman.getComposedType(VariantProduct.class), "EXISTS ({{SELECT {" + Item.PK + "} FROM {" + typeman
                            .getComposedType(Product.class)
                            .getCode() + " AS base} WHERE {base:" + Item.PK + "}={item:baseProduct} }})");
            res.setActive(false);
        }
        if(typeman.getSearchRestriction(typeman.getComposedType(Keyword.class), "Frontend_Keyword") == null)
        {
            typeman.createRestriction("Frontend_Keyword", (Principal)catalogViewers, typeman.getComposedType(Keyword.class), "{item:" + CatalogConstants.Attributes.Category.CATALOGVERSION + "} IN (?session.catalogversions)");
        }
        if(typeman.getSearchRestriction(typeman.getComposedType(ProductReference.class), "Frontend_ProductReference") == null)
        {
            typeman.createRestriction("Frontend_ProductReference", (Principal)catalogViewers, typeman
                            .getComposedType(ProductReference.class), "EXISTS ({{SELECT {" + Item.PK + "} FROM {" + typeman
                            .getComposedType(Product.class)
                            .getCode() + "} WHERE {" + Item.PK + "}={item:target} }})");
        }
        UserGroup editors = getSession().getUserManager().getUserGroupByGroupID(Constants.USER.EMPLOYEE_USERGROUP);
        ComposedType prodCT = typeman.getComposedType(Product.class);
        ComposedType variantprodCT = typeman.getComposedType(VariantProduct.class);
        for(ComposedType rootitemtype : getInstance().getAllCatalogItemRootTypes())
        {
            if(!rootitemtype.isAssignableFrom((Type)prodCT))
            {
                String cvQualifier = getInstance().getCatalogVersionAttribute(rootitemtype).getQualifier();
                typeman.createRestriction("Backend_visibility", (Principal)editors, rootitemtype, "{" + cvQualifier + "} IN ( ?session.catalogversions ) OR  {" + cvQualifier + "} IS NULL");
            }
        }
        if(typeman.getSearchRestriction(typeman.getComposedType(Product.class), "Backend_visibility") == null)
        {
            typeman.createRestriction("Backend_visibility", (Principal)editors, prodCT,
                            "{" + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION + "} IN ( ?session.catalogversions ) OR EXISTS ({{SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CATEGORY + " AS cat JOIN " + GeneratedCatalogConstants.Relations.CATEGORYPRODUCTRELATION
                                            + " AS c2p ON {cat:" + Item.PK + "}={c2p:source} } WHERE {c2p:target}={item:" + Item.PK + "} AND {cat:" + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION + "} NOT IN({{SELECT {" + Item.PK + "} FROM { "
                                            + GeneratedCatalogConstants.TC.CLASSIFICATIONSYSTEMVERSION + "} }})}})");
        }
        if(typeman.getSearchRestriction(typeman.getComposedType(VariantProduct.class), "Backend_visibility") == null)
        {
            typeman.createRestriction("Backend_visibility", (Principal)editors, variantprodCT, "{" + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION + "} IN ( ?session.catalogversions ) OR EXISTS ({{SELECT {" + Item.PK + "} FROM {" + typeman
                            .getComposedType(Product.class)
                            .getCode() + " AS base} WHERE {base:" + Item.PK + "}={item:baseProduct} }})");
        }
        createCatalogSearchRestriction("Backend_visibility", editors);
        if(typeman.getSearchRestriction(typeman.getComposedType(CatalogVersion.class), "Backend_visibility") == null)
        {
            typeman.createRestriction("Backend_visibility", (Principal)editors, typeman.getComposedType(CatalogVersion.class), "{" + Item.PK + "} IN ( ?session.catalogversions )");
        }
    }


    void createCatalogSearchRestriction(String restrictionName, UserGroup userGroup)
    {
        String alias = "item";
        TypeManager typeman = getSession().getTypeManager();
        if(typeman.getSearchRestriction(typeman.getComposedType(Catalog.class), restrictionName) == null)
        {
            typeman.createRestriction("Backend_visibility", (Principal)userGroup, typeman.getComposedType(Catalog.class),
                            "{" + Item.PK + "} IN ({{SELECT {catalog} FROM {" + GeneratedCatalogConstants.TC.CATALOGVERSION + "} }}) OR NOT EXISTS ({{SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CATALOGVERSION + "* AS cv} WHERE {cv:catalog}={item:" + Item.PK + "}}})");
        }
    }


    public void beforeItemCreation(SessionContext ctx, ComposedType type, Item.ItemAttributeMap attributes) throws JaloBusinessException
    {
        super.beforeItemCreation(ctx, type, attributes);
        getInstance().getVariantsManager().beforeItemCreation(ctx, type, attributes);
        Class<?> jaloClass = type.getJaloClass();
        if(JobLog.class.isAssignableFrom(jaloClass) || ItemSyncTimestamp.class.isAssignableFrom(jaloClass) || ProductFeature.class
                        .isAssignableFrom(jaloClass) || ChangeDescriptor.class.isAssignableFrom(jaloClass))
        {
            return;
        }
        if(Product.class.isAssignableFrom(jaloClass))
        {
            CatalogVersion catver = (CatalogVersion)attributes.get(GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION);
            if(catver != null)
            {
                Catalog cat = (Catalog)attributes.get(GeneratedCatalogConstants.Attributes.Product.CATALOG);
                if(cat == null)
                {
                    attributes.addInitialProperty(GeneratedCatalogConstants.Attributes.Product.CATALOG, catver.getCatalog(ctx));
                }
                attributes.setAttributeMode(GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION, Item.AttributeMode.INITIAL);
            }
            attributes.setAttributeMode(GeneratedCatalogConstants.Attributes.Product.APPROVALSTATUS, Item.AttributeMode.INITIAL);
            attributes.setAttributeMode(GeneratedCatalogConstants.Attributes.Product.PRICEQUANTITY, Item.AttributeMode.INITIAL);
        }
        else if(Media.class.isAssignableFrom(jaloClass))
        {
            CatalogVersion catver = (CatalogVersion)attributes.get(GeneratedCatalogConstants.Attributes.Media.CATALOGVERSION);
            if(catver != null)
            {
                Catalog cat = (Catalog)attributes.get(GeneratedCatalogConstants.Attributes.Media.CATALOG);
                if(cat == null)
                {
                    attributes.addInitialProperty(GeneratedCatalogConstants.Attributes.Media.CATALOG, catver.getCatalog(ctx));
                }
                attributes.setAttributeMode(GeneratedCatalogConstants.Attributes.Media.CATALOGVERSION, Item.AttributeMode.INITIAL);
            }
        }
        else if(Category.class.isAssignableFrom(jaloClass))
        {
            CatalogVersion catver = (CatalogVersion)attributes.get(CatalogConstants.Attributes.Category.CATALOGVERSION);
            if(catver != null)
            {
                Catalog cat = (Catalog)attributes.get(CatalogConstants.Attributes.Category.CATALOG);
                if(cat == null)
                {
                    attributes.addInitialProperty(CatalogConstants.Attributes.Category.CATALOG, catver.getCatalog(ctx));
                }
                attributes.setAttributeMode(CatalogConstants.Attributes.Category.CATALOGVERSION, Item.AttributeMode.INITIAL);
            }
        }
        else if(GenericItem.class.isAssignableFrom(jaloClass) && getInstance().isCatalogItem(type))
        {
            AttributeDescriptor attrdesc = getInstance().getCatalogVersionAttribute(ctx, type);
            if(attrdesc != null)
            {
                attributes.setAttributeMode(attrdesc.getQualifier(), Item.AttributeMode.INITIAL);
            }
        }
    }
}
