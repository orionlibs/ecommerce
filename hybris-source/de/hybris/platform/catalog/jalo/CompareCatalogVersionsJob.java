package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.user.User;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class CompareCatalogVersionsJob extends GeneratedCompareCatalogVersionsJob
{
    private static final Logger LOG = Logger.getLogger(CompareCatalogVersionsJob.class.getName());
    private static final int BLOCK_SIZE = 100;
    private int processedStepsCounter;
    private double maximumPriceTolerance = 0.0D;


    public int allreadyProcessedSteps()
    {
        return this.processedStepsCounter;
    }


    protected CronJob.CronJobResult performCronJob(CronJob cronJob)
    {
        CompareCatalogVersionsCronJob ccvCronJob = (CompareCatalogVersionsCronJob)cronJob;
        CatalogVersion oldVersion = ccvCronJob.getSourceVersion();
        CatalogVersion newVersion = ccvCronJob.getTargetVersion();
        this.processedStepsCounter = 0;
        if(ccvCronJob.isSearchMissingProductsAsPrimitive() || ccvCronJob.isSearchNewProductsAsPrimitive())
        {
            findProductsDifferences(ccvCronJob, oldVersion, newVersion);
        }
        if(ccvCronJob.isSearchMissingCategoriesAsPrimitive() || ccvCronJob.isSearchNewCategoriesAsPrimitive())
        {
            findCategoryDifferences(ccvCronJob, oldVersion, newVersion);
        }
        if(ccvCronJob.isSearchPriceDifferencesAsPrimitive())
        {
            double maxPriceTolerance = ccvCronJob.getMaxPriceToleranceAsPrimitive();
            comparePrices(ccvCronJob, oldVersion, newVersion, maxPriceTolerance);
        }
        if(ccvCronJob.isOverwriteProductApprovalStatusAsPrimitive())
        {
            overwriteProductApprovalStatus(oldVersion, newVersion, (CronJob)ccvCronJob);
        }
        return cronJob.getFinishedResult(true);
    }


    private Collection getMissingProducts(int start, int count, CompareCatalogVersionsCronJob ccvCronJob)
    {
        return search4ProductDiffs(start, count, ccvCronJob.getTargetVersion(), ccvCronJob.getSourceVersion());
    }


    private Collection getNewProducts(int start, int count, CompareCatalogVersionsCronJob ccvCronJob)
    {
        return search4ProductDiffs(start, count, ccvCronJob.getSourceVersion(), ccvCronJob.getTargetVersion());
    }


    private Collection search4ProductDiffs(int start, int count, CatalogVersion version1, CatalogVersion version2)
    {
        ComposedType productType = getSession().getTypeManager().getComposedType(Product.class);
        Map<Object, Object> values = new HashMap<>();
        values.put("version1", version1);
        values.put("version2", version2);
        return getSession().getFlexibleSearch().search("SELECT {p1:" + Item.PK + "} FROM {" + productType
                                        .getCode() + " AS p1} WHERE NOT EXISTS ({{SELECT {p2:" + Item.PK + "} FROM {" + productType
                                        .getCode() + " AS p2} WHERE {p2:" + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION + "} = ?version1 AND {p2:code} = {p1:code} }}) AND {p1:" + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION + "} = ?version2 ", values,
                        Collections.singletonList(Product.class), true, true, start, count).getResult();
    }


    private Collection getSameProductsAsPair(int start, int count, CompareCatalogVersionsCronJob ccvCronJob)
    {
        ComposedType productType = getSession().getTypeManager().getComposedType(Product.class);
        Map<Object, Object> values = new HashMap<>();
        values.put("version1", ccvCronJob.getSourceVersion());
        values.put("version2", ccvCronJob.getTargetVersion());
        return getSession().getFlexibleSearch().search("SELECT {p1:" + Item.PK + "}, {p2:" + Item.PK + "} FROM {" + productType
                                                        .getCode() + " AS p1}, {" + productType
                                                        .getCode() + " AS p2} WHERE EXISTS ({{SELECT {p3:" + Item.PK + "} FROM {" + productType
                                                        .getCode() + " AS p3} WHERE {p3:" + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION + "} = ?version1 AND {p1:code} = {p3:code} AND {p2:code} = {p3:code} }}) AND {p1:" + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION + "} = ?version2 AND {p2:"
                                                        + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION + "} = ?version1 ", values,
                                        Arrays.asList((Class<?>[][])new Class[] {Product.class, Product.class}, ), true, true, start, count)
                        .getResult();
    }


    private void findProductsDifferences(CompareCatalogVersionsCronJob cronJob, CatalogVersion srcVersion, CatalogVersion targetVersion)
    {
        int missingProductsCount = 0;
        int newProductsCount = 0;
        int start = 0;
        int range = 1000;
        if(cronJob.isSearchMissingProductsAsPrimitive())
        {
            Collection missingProducts;
            do
            {
                missingProducts = getMissingProducts(start, 1000, cronJob);
                missingProductsCount += missingProducts.size();
                start += 1000;
                for(Iterator<Product> it = missingProducts.iterator(); it.hasNext(); )
                {
                    Product product = it.next();
                    EnumerationValue productRemoved = getSession().getEnumerationManager().getEnumerationValue(GeneratedCatalogConstants.TC.PRODUCTDIFFERENCEMODE, GeneratedCatalogConstants.Enumerations.ProductDifferenceMode.PRODUCT_REMOVED);
                    ProductCatalogVersionDifference productCatalogVersionDifference = getCatalogManager().createProductCatalogVersionDifference(srcVersion, targetVersion, (CronJob)cronJob, product, null, productRemoved);
                    productCatalogVersionDifference.setDifferenceText("Product " + product.getCode() + " not found in version: " + targetVersion
                                    .getVersion());
                    this.processedStepsCounter++;
                }
            }
            while(missingProducts.size() == 1000);
        }
        cronJob.setMissingProducts(missingProductsCount);
        start = 0;
        if(cronJob.isSearchNewProductsAsPrimitive())
        {
            Collection newProducts;
            do
            {
                newProducts = getNewProducts(start, 1000, cronJob);
                newProductsCount += newProducts.size();
                start += 1000;
                for(Iterator<Product> it = newProducts.iterator(); it.hasNext(); )
                {
                    Product product = it.next();
                    EnumerationValue productNew = getSession().getEnumerationManager().getEnumerationValue(GeneratedCatalogConstants.TC.PRODUCTDIFFERENCEMODE, GeneratedCatalogConstants.Enumerations.ProductDifferenceMode.PRODUCT_NEW);
                    ProductCatalogVersionDifference productCatalogVersionDifference = getCatalogManager().createProductCatalogVersionDifference(srcVersion, targetVersion, (CronJob)cronJob, null, product, productNew);
                    productCatalogVersionDifference.setDifferenceText("Product " + product.getCode() + " new in version: " + targetVersion
                                    .getVersion());
                    this.processedStepsCounter++;
                }
            }
            while(newProducts.size() == 1000);
        }
        cronJob.setNewProducts(newProductsCount);
    }


    private void findCategoryDifferences(CompareCatalogVersionsCronJob cronJob, CatalogVersion srcVersion, CatalogVersion targetVersion)
    {
        int srcCategoriesCount = srcVersion.getAllCategoryCount();
        int targetCategoriesCount = targetVersion.getAllCategoryCount();
        if(srcCategoriesCount != targetCategoriesCount)
        {
            LOG.info("categories count is different! srcCategoriesCount: " + srcCategoriesCount + " targetCategoriesCount: " + targetCategoriesCount);
        }
        if(cronJob.isSearchMissingCategoriesAsPrimitive())
        {
            int i;
            for(i = 0; i <= srcCategoriesCount; i += 100)
            {
                Collection categories = srcVersion.getAllCategories(i, 100);
                for(Iterator<Category> it = categories.iterator(); it.hasNext(); )
                {
                    Category category = it.next();
                    Collection sameCategories = targetVersion.getSameCategories(category);
                    if(sameCategories == null || sameCategories.isEmpty())
                    {
                        LOG.info("Category " + category.getCode() + " not found in version: " + targetVersion.getVersion());
                        EnumerationValue categoryNew = getSession().getEnumerationManager().getEnumerationValue(GeneratedCatalogConstants.TC.CATEGORYDIFFERENCEMODE, GeneratedCatalogConstants.Enumerations.CategoryDifferenceMode.CATEGORY_NEW);
                        CategoryCatalogVersionDifference categoryCatalogVersionDifference = getCatalogManager().createCategoryCatalogVersionDifference(srcVersion, targetVersion, (CronJob)cronJob, category, null, categoryNew);
                        categoryCatalogVersionDifference.setDifferenceText("Category " + category
                                        .getCode() + " not found in version: " + targetVersion
                                        .getVersion());
                    }
                }
            }
        }
        if(cronJob.isSearchNewCategoriesAsPrimitive())
        {
            int i;
            for(i = 0; i <= targetCategoriesCount; i += 100)
            {
                Collection categories = targetVersion.getAllCategories(i, 100);
                for(Iterator<Category> it = categories.iterator(); it.hasNext(); )
                {
                    Category category = it.next();
                    Collection sameCategories = srcVersion.getSameCategories(category);
                    if(sameCategories == null || sameCategories.isEmpty())
                    {
                        LOG.info("Category " + category.getCode() + " new in version: " + targetVersion.getVersion());
                        EnumerationValue categoryRemoved = getSession().getEnumerationManager().getEnumerationValue(GeneratedCatalogConstants.TC.CATEGORYDIFFERENCEMODE, GeneratedCatalogConstants.Enumerations.CategoryDifferenceMode.CATEGORY_REMOVED);
                        CategoryCatalogVersionDifference categoryCatalogVersionDifference = getCatalogManager().createCategoryCatalogVersionDifference(srcVersion, targetVersion, (CronJob)cronJob, null, category, categoryRemoved);
                        categoryCatalogVersionDifference.setDifferenceText("Category " + category.getCode() + " new in version: " + targetVersion
                                        .getVersion());
                    }
                }
            }
        }
    }


    private void comparePrices(CompareCatalogVersionsCronJob cronJob, CatalogVersion oldVersion, CatalogVersion newVersion, double maxPriceTolerance)
    {
        Collection productPairs;
        User original = JaloSession.getCurrentSession().getUser();
        int start = 0;
        int range = 1000;
        do
        {
            productPairs = getSameProductsAsPair(start, 1000, cronJob);
            start += 1000;
            for(Iterator<List> it = productPairs.iterator(); it.hasNext(); )
            {
                List<Product> pair = it.next();
                Product product1 = pair.get(0);
                Product product2 = pair.get(1);
                try
                {
                    if(cronJob.getPriceCompareCustomer() != null)
                    {
                        JaloSession.getCurrentSession().setUser(cronJob.getPriceCompareCustomer());
                    }
                    Collection newPriceInfos = product1.getPriceInformations(true);
                    Collection oldPriceInfos = product2.getPriceInformations(true);
                    if(cronJob.getPriceCompareCustomer() != null)
                    {
                        JaloSession.getCurrentSession().setUser(original);
                    }
                    ProductCatalogVersionDifference diff = null;
                    EnumerationValue priceDiff = getSession().getEnumerationManager().getEnumerationValue(GeneratedCatalogConstants.TC.PRODUCTDIFFERENCEMODE, GeneratedCatalogConstants.Enumerations.ProductDifferenceMode.PRODUCT_PRICEDIFFERENCE);
                    if(newPriceInfos.size() != oldPriceInfos.size())
                    {
                        diff = getCatalogManager().createProductCatalogVersionDifference(oldVersion, newVersion, (CronJob)cronJob, product1, product2, priceDiff);
                        diff.setDifferenceText("Difference in price info count! oldPrices: " + oldPriceInfos
                                        .size() + " newPrices: " + newPriceInfos
                                        .size());
                    }
                    for(Iterator<PriceInformation> priceIt = newPriceInfos.iterator(); priceIt.hasNext(); )
                    {
                        PriceInformation priceInfo = priceIt.next();
                        PriceInformation equivalentPriceInfo = findEquivalentPriceInfo(priceInfo, oldPriceInfos);
                        if(equivalentPriceInfo != null)
                        {
                            double newPrice = priceInfo.getPriceValue().getValue();
                            double oldPrice = equivalentPriceInfo.getPriceValue().getValue();
                            double difference = oldPrice - newPrice;
                            double tolerance = Math.abs(difference * 100.0D / oldPrice);
                            if(tolerance > maxPriceTolerance)
                            {
                                if(tolerance > this.maximumPriceTolerance)
                                {
                                    this.maximumPriceTolerance = tolerance;
                                }
                                StringBuilder diffText = new StringBuilder();
                                if(diff == null)
                                {
                                    diff = getCatalogManager().createProductCatalogVersionDifference(oldVersion, newVersion, (CronJob)cronJob, product1, product2, priceDiff);
                                }
                                else
                                {
                                    diffText.append(diff.getDifferenceText()).append("\n");
                                }
                                diffText.append("Difference above max tolerance ( " + maxPriceTolerance + " ) in price: (new: " + newPrice + " old: " + oldPrice + " for PriceInfo: " + priceInfo + ".");
                                diff.setDifferenceText(diffText.toString());
                                diff.setDifferenceValue(new Double(tolerance));
                            }
                            continue;
                        }
                        LOG.warn("No equivalent PriceInfo found for PriceInfo:" + priceInfo);
                    }
                }
                catch(JaloPriceFactoryException e)
                {
                    e.printStackTrace();
                }
            }
        }
        while(productPairs.size() == 1000);
    }


    private PriceInformation findEquivalentPriceInfo(PriceInformation priceInfo, Collection priceInfos)
    {
        PriceInformation equivalentPriceInfo = null;
        for(Iterator<PriceInformation> it = priceInfos.iterator(); it.hasNext(); )
        {
            PriceInformation tempPriceInfo = it.next();
            if(tempPriceInfo.equalsWithoutPriceRow(priceInfo))
            {
                equivalentPriceInfo = tempPriceInfo;
                break;
            }
        }
        return equivalentPriceInfo;
    }


    private CatalogManager getCatalogManager()
    {
        return CatalogManager.getInstance();
    }


    private void overwriteProductApprovalStatus(CatalogVersion sourceVersion, CatalogVersion targetVersion, CronJob cronJob)
    {
        Collection products;
        CatalogManager catalogManager = getCatalogManager();
        int start = 0;
        int range = 1000;
        do
        {
            products = getProductsToOverwriteApprovalStatus(start, 1000, sourceVersion, targetVersion, cronJob);
            start += 1000;
            for(Iterator<List> it = products.iterator(); it.hasNext(); )
            {
                List<Product> productList = it.next();
                Product productFromSourceVersion = productList.get(0);
                Product productFromTargetVersion = productList.get(1);
                LOG.debug("Setting approval status of product: " + productFromTargetVersion + " to " + catalogManager
                                .getApprovalStatus(productFromSourceVersion).getCode());
                catalogManager
                                .setApprovalStatus(productFromTargetVersion, catalogManager.getApprovalStatus(productFromSourceVersion));
            }
        }
        while(products.size() == 1000);
    }


    private Collection getProductsToOverwriteApprovalStatus(int start, int count, CatalogVersion sourceVersion, CatalogVersion targetVersion, CronJob cronJob)
    {
        ComposedType productType = getSession().getTypeManager().getComposedType(Product.class);
        ComposedType catalogversiondifferenceType = getSession().getTypeManager().getComposedType(ProductCatalogVersionDifference.class);
        String query = "SELECT {p1:" + Item.PK + "}, {p2:" + Item.PK + "} FROM {" + productType.getCode() + " AS p1 JOIN " + productType.getCode() + " AS p2 ON {p1:code} = {p2:code} AND {p1:" + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION + "} = ?version1 AND {p2:"
                        + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION + "} = ?version2 } WHERE not exists ( {{ select * from {" + catalogversiondifferenceType.getCode() + " AS c} WHERE {c:sourceProduct} = {p1:" + Item.PK + "} AND {c:targetProduct} = {p2:" + Item.PK
                        + "} AND {c:cronJob} = ?cronjob }} )  AND {p1:" + GeneratedCatalogConstants.Attributes.Product.APPROVALSTATUS + "} <> {p2:" + GeneratedCatalogConstants.Attributes.Product.APPROVALSTATUS + "}";
        Map<Object, Object> params = new HashMap<>();
        params.put("version1", sourceVersion);
        params.put("version2", targetVersion);
        params.put("cronjob", cronJob);
        return getSession().getFlexibleSearch().search(query, params, Arrays.asList((Class<?>[][])new Class[] {Product.class, Product.class}, ), true, true, start, count)
                        .getResult();
    }


    public double getMaximumPriceTolerance()
    {
        return this.maximumPriceTolerance;
    }
}
