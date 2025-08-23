package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class Keyword extends GeneratedKeyword implements CatalogItem<Keyword>
{
    private static final Logger log = Logger.getLogger(Keyword.class.getName());


    @SLDSafe(portingClass = "MandatoryAttributesValidator", portingMethod = "onValidate(final Object model, final InterceptorContext ctx)")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(!checkMandatoryAttribute("keyword", allAttributes, missing))
        {
            throw new JaloInvalidParameterException("Missing " + missing + " for creating a new Keyword", 0);
        }
        allAttributes.setAttributeMode("keyword", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("language", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return "Keyword '" + getKeyword() + "'(" + super.toString() + ")";
    }


    public String getIDAttributeQualifier()
    {
        return "keyword";
    }


    public String getCatalogVersionAttributeQualifier()
    {
        return "catalogVersion";
    }


    public Keyword getCounterpartItem(CatalogVersion targetVersion)
    {
        Collection coll = targetVersion.getSameKeywords(this);
        if(coll.size() > 1)
        {
            if(log.isEnabledFor((Priority)Level.WARN))
            {
                log.warn("multiple keywords found in version " + targetVersion.getPK() + "(" + targetVersion.getVersion() + ") for original " + this + " ( found " + coll + ") - returning oldest one");
            }
            return (Keyword)CatalogManager.getOldest(coll);
        }
        return (Keyword)CatalogManager.getFirstOne(coll);
    }


    public String getCatalogItemID()
    {
        return getKeyword();
    }


    @SLDSafe(portingClass = "UniqueCatalogItemInterceptor", portingMethod = "onPrepare(final Object model, final InterceptorContext ctx)")
    public void setCatalogVersion(SessionContext ctx, CatalogVersion catalogVersion)
    {
        super.setCatalogVersion(ctx, catalogVersion);
        setProperty(ctx, "catalog", (catalogVersion != null) ? catalogVersion.getCatalog() : null);
    }


    @SLDSafe(portingClass = "KeywordModel", portingMethod = "getProducts().size()")
    public long getProductsCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCatalogConstants.Relations.PRODUCT2KEYWORDRELATION, ctx.getLanguage());
    }


    @SLDSafe(portingClass = "KeywordModel", portingMethod = "getCategories().size()")
    public long getCategoriesCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, false, GeneratedCatalogConstants.Relations.CATEGORY2KEYWORDRELATION, ctx.getLanguage());
    }
}
