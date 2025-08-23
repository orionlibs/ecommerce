package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.GenericCondition;
import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.core.GenericSearchField;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Country;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.util.localization.Localization;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import org.apache.log4j.Logger;

public class Catalog extends GeneratedCatalog
{
    private static final Logger LOG = Logger.getLogger(Catalog.class.getName());
    public static final int ERROR = 1714;


    @SLDSafe(portingClass = "de.hybris.platform.catalog.interceptors.CatalogPrepareInterceptor", portingMethod = "onPrepare")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!checkMandatoryAttribute("id", allAttributes, missing))
        {
            throw new JaloInvalidParameterException(Localization.getLocalizedString("error.catalog.missing", new Object[] {missing}), 0);
        }
        Boolean isDefault = (Boolean)allAttributes.get("defaultCatalog");
        if(isDefault != null && Boolean.TRUE.equals(isDefault))
        {
            Catalog catalog = CatalogManager.getInstance().getDefaultCatalog();
            if(catalog != null)
            {
                catalog.setDefaultCatalog(ctx, false);
            }
        }
        return super.createItem(ctx, type, allAttributes);
    }


    @SLDSafe(portingClass = "CheckVersionsRemoveInterceptor,DefaultCatalogRemoveInterceptor")
    protected void checkRemovable(SessionContext ctx) throws ConsistencyCheckException
    {
        if(isDefaultCatalogAsPrimitive(ctx))
        {
            throw new ConsistencyCheckException(Localization.getLocalizedString("error.catalog.removing_default_catalog"), 1714);
        }
        Set<CatalogVersion> nonRemovableVersions = null;
        for(Iterator<CatalogVersion> it = getCatalogVersions(ctx).iterator(); it.hasNext(); )
        {
            CatalogVersion catalogVersion = it.next();
            if(!catalogVersion.isRemovable(ctx, false))
            {
                if(nonRemovableVersions == null)
                {
                    nonRemovableVersions = new HashSet();
                }
                nonRemovableVersions.add(catalogVersion);
            }
        }
        if(nonRemovableVersions != null && !nonRemovableVersions.isEmpty())
        {
            throw new ConsistencyCheckException(Localization.getLocalizedString("error.catalog.contains_non_removable_versions", new Object[] {nonRemovableVersions}), 1714);
        }
        super.checkRemovable(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "de.hybris.platform.catalog.interceptors.CatalogPrepareInterceptor", portingMethod = "onPrepare")
    public void setActiveCatalogVersion(SessionContext ctx, CatalogVersion catalogVersion)
    {
        CatalogVersion oldActiveVersion = getActiveCatalogVersion(ctx);
        if(oldActiveVersion != catalogVersion && (oldActiveVersion == null || !oldActiveVersion.equals(catalogVersion)))
        {
            if(oldActiveVersion != null)
            {
                oldActiveVersion.setActiveInternal(ctx, Boolean.FALSE);
            }
            if(catalogVersion != null)
            {
                catalogVersion.setActiveInternal(ctx, Boolean.TRUE);
            }
            super.setActiveCatalogVersion(ctx, catalogVersion);
        }
    }


    protected void setActiveCatalogVersionInternal(SessionContext ctx, CatalogVersion catalogVersion)
    {
        super.setActiveCatalogVersion(ctx, catalogVersion);
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogRootCategoriesHandler", portingMethod = "get")
    public List<Category> getRootCategories(SessionContext ctx)
    {
        if(getActiveCatalogVersion() != null)
        {
            return getActiveCatalogVersion(ctx).getRootCategories(ctx);
        }
        return Collections.EMPTY_LIST;
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogRootCategoriesHandler", portingMethod = "get")
    public List<Category> getRootCategories()
    {
        return getRootCategories(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public CatalogVersion getCatalogVersion(String version)
    {
        GenericQuery query = new GenericQuery(GeneratedCatalogConstants.TC.CATALOGVERSION, (GenericCondition)GenericCondition.createConditionList(new GenericCondition[] {GenericCondition.equals(new GenericSearchField(GeneratedCatalogConstants.TC.CATALOGVERSION, "catalog"), this),
                        GenericCondition.equals(new GenericSearchField(GeneratedCatalogConstants.TC.CATALOGVERSION, "version"), version)}));
        Collection<CatalogVersion> result = getSession().search(query, getSession().createSearchContext()).getResult();
        return (result == null || result.size() != 1) ? null : result.iterator().next();
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogVersionHandler", portingMethod = "get")
    public String getVersion(SessionContext ctx)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getVersion(ctx);
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogVersionHandler", portingMethod = "get")
    public String getVersion()
    {
        return getVersion(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogMimeRootDirectoryHandler", portingMethod = "get")
    public String getMimeRootDirectory(SessionContext ctx)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getMimeRootDirectory(ctx);
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogMimeRootDirectoryHandler", portingMethod = "get")
    public String getMimeRootDirectory()
    {
        return getMimeRootDirectory(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogGenerationDateHandler", portingMethod = "get")
    public Date getGenerationDate(SessionContext ctx)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getGenerationDate(ctx);
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogGenerationDateHandler", portingMethod = "get")
    public Date getGenerationDate()
    {
        return getGenerationDate(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogDefaultCurrencyHandler", portingMethod = "get")
    public Currency getDefaultCurrency(SessionContext ctx)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getDefaultCurrency(ctx);
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogDefaultCurrencyHandler", portingMethod = "get")
    public Currency getDefaultCurrency()
    {
        return getDefaultCurrency(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogIncludeFreightHandler", portingMethod = "get")
    public Boolean isInclFreight(SessionContext ctx)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().isInclFreight(ctx);
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogIncludeFreightHandler", portingMethod = "get")
    public Boolean isInclFreight()
    {
        return isInclFreight(getSession().getSessionContext());
    }


    public boolean isInclFreightAsPrimitive()
    {
        return isInclFreightAsPrimitive(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isInclFreightAsPrimitive(SessionContext ctx)
    {
        if(getActiveCatalogVersion() == null)
        {
            return false;
        }
        return getActiveCatalogVersion().isInclFreightAsPrimitive(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogIncludePackingHandler", portingMethod = "get")
    public Boolean isInclPacking(SessionContext ctx)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().isInclPacking(ctx);
    }


    public Boolean isInclPacking()
    {
        return isInclPacking(getSession().getSessionContext());
    }


    public boolean isInclPackingAsPrimitive()
    {
        return isInclPackingAsPrimitive(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isInclPackingAsPrimitive(SessionContext ctx)
    {
        if(getActiveCatalogVersion() == null)
        {
            return false;
        }
        return getActiveCatalogVersion().isInclPackingAsPrimitive(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "catalogIncludeAssuranceHandler", portingMethod = "get")
    public Boolean isInclAssurance(SessionContext ctx)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().isInclAssurance(ctx);
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogIncludeFreightHandler", portingMethod = "get")
    public Boolean isInclAssurance()
    {
        return isInclAssurance(getSession().getSessionContext());
    }


    public boolean isInclAssuranceAsPrimitive()
    {
        return isInclAssuranceAsPrimitive(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isInclAssuranceAsPrimitive(SessionContext ctx)
    {
        if(getActiveCatalogVersion() == null)
        {
            return false;
        }
        return getActiveCatalogVersion().isInclAssuranceAsPrimitive(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogIncludeDutyHandler", portingMethod = "get")
    public Boolean isInclDuty(SessionContext ctx)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().isInclDuty(ctx);
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogIncludeDutyHandler", portingMethod = "get")
    public Boolean isInclDuty()
    {
        return isInclDuty(getSession().getSessionContext());
    }


    public boolean isInclDutyAsPrimitive()
    {
        return isInclDutyAsPrimitive(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public boolean isInclDutyAsPrimitive(SessionContext ctx)
    {
        if(getActiveCatalogVersion() == null)
        {
            return false;
        }
        return getActiveCatalogVersion().isInclDutyAsPrimitive(ctx);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogTerritoriesHandler", portingMethod = "get")
    public Collection<Country> getTerritories(SessionContext ctx)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getTerritories(ctx);
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogTerritoriesHandler", portingMethod = "get")
    public Collection<Country> getTerritories()
    {
        return getTerritories(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogLanguagesHandler", portingMethod = "get")
    public Collection<Language> getLanguages(SessionContext ctx)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getLanguages(ctx);
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogLanguagesHandler", portingMethod = "get")
    public Collection<Language> getLanguages()
    {
        return getLanguages(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogGeneratorInfoHandler", portingMethod = "get")
    public String getGeneratorInfo(SessionContext ctx)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getGeneratorInfo(ctx);
    }


    @SLDSafe(portingClass = "de.hybris.platform.catalog.model.CatalogGeneratorInfoHandler", portingMethod = "get")
    public String getGeneratorInfo()
    {
        return getGeneratorInfo(getSession().getSessionContext());
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "catalogAgreementsHandler", portingMethod = "get")
    public Collection<Agreement> getAgreements(SessionContext ctx)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getAgreements(ctx);
    }


    @SLDSafe(portingClass = "catalogAgreementsHandler", portingMethod = "get")
    public Collection<Agreement> getAgreements()
    {
        return getAgreements(getSession().getSessionContext());
    }


    public Collection<Category> getAllCategories()
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getAllCategories();
    }


    public Collection<Category> getAllCategories(int start, int count)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getAllCategories(start, count);
    }


    public int getAllCategoryCount()
    {
        if(getActiveCatalogVersion() == null)
        {
            return -1;
        }
        return getActiveCatalogVersion().getAllCategoryCount();
    }


    public Collection<Keyword> getAllKeywords()
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getAllKeywords();
    }


    public Collection<Keyword> getAllKeywords(int start, int count)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getAllKeywords(start, count);
    }


    public int getAllKeywordCount()
    {
        if(getActiveCatalogVersion() == null)
        {
            return -1;
        }
        return getActiveCatalogVersion().getAllKeywordCount();
    }


    public Collection<Media> getAllMedias()
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getAllMedias();
    }


    public Collection<Media> getAllMedias(int start, int count)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getAllMedias(start, count);
    }


    public int getAllMediaCount()
    {
        if(getActiveCatalogVersion() == null)
        {
            return -1;
        }
        return getActiveCatalogVersion().getAllMediaCount();
    }


    public Collection<Product> getAllProducts()
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getAllProducts();
    }


    public Collection<Product> getAllProducts(int start, int count)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getAllProducts(start, count);
    }


    public int getAllProductCount()
    {
        if(getActiveCatalogVersion() == null)
        {
            return -1;
        }
        return getActiveCatalogVersion().getAllProductCount();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Category getCategory(String code)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getCategory(code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Category getCategory(SessionContext ctx, String code)
    {
        if(getActiveCatalogVersion(ctx) == null)
        {
            return null;
        }
        return getActiveCatalogVersion(ctx).getCategory(ctx, code);
    }


    public Collection<Category> getCategories(String code)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getCategories(code);
    }


    public Collection<Category> getCategories(SessionContext ctx, String code)
    {
        if(getActiveCatalogVersion(ctx) == null)
        {
            return null;
        }
        return getActiveCatalogVersion(ctx).getCategories(ctx, code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Product getProduct(String code)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getProduct(code);
    }


    public Product getProduct(SessionContext ctx, String code)
    {
        if(getActiveCatalogVersion(ctx) == null)
        {
            return null;
        }
        return getActiveCatalogVersion(ctx).getProduct(ctx, code);
    }


    public Collection<Product> getProducts(String code)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getProducts(code);
    }


    public Collection<Product> getProducts(SessionContext ctx, String code)
    {
        if(getActiveCatalogVersion(ctx) == null)
        {
            return null;
        }
        return getActiveCatalogVersion(ctx).getProducts(ctx, code);
    }


    public Media getMedia(String code)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getMedia(code);
    }


    public Media getMedia(SessionContext ctx, String code)
    {
        if(getActiveCatalogVersion(ctx) == null)
        {
            return null;
        }
        return getActiveCatalogVersion(ctx).getMedia(ctx, code);
    }


    public Collection<Media> getMedias(String code)
    {
        if(getActiveCatalogVersion() == null)
        {
            return null;
        }
        return getActiveCatalogVersion().getMedias(code);
    }


    public Collection<Media> getMedias(SessionContext ctx, String code)
    {
        if(getActiveCatalogVersion(ctx) == null)
        {
            return null;
        }
        return getActiveCatalogVersion(ctx).getMedias(ctx, code);
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "de.hybris.platform.catalog.interceptors.CatalogPrepareInterceptor", portingMethod = "onPrepare")
    public void setDefaultCatalog(SessionContext ctx, Boolean param)
    {
        if(Boolean.TRUE.equals(param))
        {
            Catalog previousDefault = CatalogManager.getInstance().getDefaultCatalog();
            if(!equals(previousDefault))
            {
                if(previousDefault != null)
                {
                    previousDefault.setProperty(ctx, "defaultCatalog", Boolean.FALSE);
                }
                super.setDefaultCatalog(ctx, Boolean.TRUE);
            }
        }
        else
        {
            super.setDefaultCatalog(ctx, param);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    @SLDSafe(portingClass = "de.hybris.platform.catalog.interceptors.CatalogURLPatternsValidator", portingMethod = "onValidate")
    public void setUrlPatterns(SessionContext ctx, Collection<String> value)
    {
        Collection<String> returncoll = null;
        if(value != null)
        {
            returncoll = new ArrayList<>(value.size());
            Collection<String> illegal = new ArrayList<>(value.size());
            for(String pattern : value)
            {
                if(pattern != null)
                {
                    try
                    {
                        Pattern.compile(pattern);
                        returncoll.add(pattern);
                    }
                    catch(PatternSyntaxException e)
                    {
                        illegal.add(pattern);
                    }
                }
            }
            if(!illegal.isEmpty())
            {
                throw new JaloInvalidParameterException("inllegal patterns found : " + illegal, 0);
            }
        }
        super.setUrlPatterns(ctx, returncoll);
    }
}
