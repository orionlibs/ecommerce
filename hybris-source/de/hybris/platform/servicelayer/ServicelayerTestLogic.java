package de.hybris.platform.servicelayer;

import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.Importer;
import de.hybris.platform.impex.jalo.imp.DumpHandler;
import de.hybris.platform.impex.jalo.media.DefaultMediaDataHandler;
import de.hybris.platform.impex.jalo.media.MediaDataHandler;
import de.hybris.platform.impex.jalo.media.MediaDataTranslator;
import de.hybris.platform.jalo.CoreBasicDataCreator;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import de.hybris.platform.servicelayer.impex.impl.ClasspathImpExResource;
import de.hybris.platform.servicelayer.internal.model.ModelContext;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.util.CSVReader;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import org.apache.log4j.Logger;
import org.junit.Assert;

public class ServicelayerTestLogic
{
    private static final Logger LOG = Logger.getLogger(ServicelayerTestLogic.class);


    public static void createCoreData() throws Exception
    {
        LOG.info("Creating essential data for core ..");
        JaloSession.getCurrentSession().setUser((User)UserManager.getInstance().getAdminEmployee());
        long startTime = System.currentTimeMillis();
        (new CoreBasicDataCreator()).createEssentialData(Collections.EMPTY_MAP, null);
        importCsv("/servicelayer/test/testBasics.csv", "windows-1252");
        LOG.info("Finished creating essential data for core in " + System.currentTimeMillis() - startTime + "ms");
    }


    public static void createDefaultCatalog() throws Exception
    {
        LOG.info("Creating test catalog ..");
        JaloSession.getCurrentSession().setUser((User)UserManager.getInstance().getAdminEmployee());
        long startTime = System.currentTimeMillis();
        FlexibleSearchService flexibleSearchService = (FlexibleSearchService)Registry.getApplicationContext().getBean("flexibleSearchService");
        Assert.assertNotNull(flexibleSearchService);
        ModelService modelService = (ModelService)Registry.getApplicationContext().getBean("modelService");
        Assert.assertNotNull(modelService);
        importCsv("/servicelayer/test/testCatalog.csv", "windows-1252");
        CatalogModel catalog = flexibleSearchService.search("SELECT {PK} FROM {Catalog} WHERE {id}='testCatalog'").getResult().get(0);
        Assert.assertNotNull(catalog);
        CatalogVersionModel version = flexibleSearchService.search("SELECT {PK} FROM {CatalogVersion} WHERE {version}='Online' AND {catalog}=?catalog", Collections.singletonMap("catalog", catalog)).getResult().get(0);
        Assert.assertNotNull(version);
        JaloSession.getCurrentSession().getSessionContext()
                        .setAttribute("catalogversions", modelService.toPersistenceLayer(Collections.singletonList(version)));
        CategoryModel category = flexibleSearchService.search("SELECT {PK} FROM {Category} WHERE {code}='testCategory0'").getResult().get(0);
        Assert.assertNotNull(category);
        ProductModel product = flexibleSearchService.search("SELECT {PK} FROM {Product} WHERE {code}='testProduct0'").getResult().get(0);
        Assert.assertNotNull(product);
        Assert.assertTrue(product.getSupercategories().contains(category));
        LOG.info("Finished creating test catalog in " + System.currentTimeMillis() - startTime + "ms");
    }


    public static void createHardwareCatalog() throws Exception
    {
        LOG.info("Creating hardware catalog ..");
        JaloSession.getCurrentSession().setUser((User)UserManager.getInstance().getAdminEmployee());
        long startTime = System.currentTimeMillis();
        FlexibleSearchService flexibleSearchService = (FlexibleSearchService)Registry.getApplicationContext().getBean("flexibleSearchService");
        Assert.assertNotNull(flexibleSearchService);
        ModelService modelService = (ModelService)Registry.getApplicationContext().getBean("modelService");
        Assert.assertNotNull(modelService);
        importCsv("/servicelayer/test/testHwcatalog.csv", "UTF-8");
        CatalogModel hwCatalog = flexibleSearchService.search("SELECT {PK} FROM {Catalog} WHERE {id}='hwcatalog'").getResult().get(0);
        Assert.assertNotNull(hwCatalog);
        CatalogVersionModel hwVersion = flexibleSearchService.search("SELECT {PK} FROM {CatalogVersion} WHERE {version}='Online' AND {catalog}=?catalog", Collections.singletonMap("catalog", hwCatalog)).getResult().get(0);
        Assert.assertNotNull(hwVersion);
        importCsv("/servicelayer/test/testClassification.csv", "UTF-8");
        CatalogModel classCatalog = flexibleSearchService.search("SELECT {PK} FROM {Catalog} WHERE {id}='SampleClassification'").getResult().get(0);
        Assert.assertNotNull(classCatalog);
        CatalogVersionModel classVersion = flexibleSearchService.search("SELECT {PK} FROM {CatalogVersion} WHERE {version}='1.0' AND {catalog}=?catalog", Collections.singletonMap("catalog", classCatalog)).getResult().get(0);
        Assert.assertNotNull(classVersion);
        JaloSession.getCurrentSession().getSessionContext()
                        .setAttribute("catalogversions", modelService.toPersistenceLayer(Arrays.asList(new CatalogVersionModel[] {hwVersion, classVersion})));
        CategoryModel category = flexibleSearchService.search("SELECT {PK} FROM {Category} WHERE {code}='HW1000'").getResult().get(0);
        Assert.assertNotNull(category);
        ProductModel product = flexibleSearchService.search("SELECT {PK} FROM {Product} WHERE {code}='HW2310-1004'").getResult().get(0);
        Assert.assertNotNull(product);
        LOG.info("Finished creating hardwarecatalog catalog in " + System.currentTimeMillis() - startTime + "ms");
    }


    public static void createDefaultUsers() throws Exception
    {
        LOG.info("Creating test users ..");
        long startTime = System.currentTimeMillis();
        importCsv("/servicelayer/test/testUser.csv", "windows-1252");
        User user = UserManager.getInstance().getUserByLogin("ariel");
        Assert.assertNotNull(user);
        Assert.assertFalse(user.getAllAddresses().isEmpty());
        Assert.assertFalse(user.getPaymentInfos().isEmpty());
        LOG.info("Finished creating test users in " + System.currentTimeMillis() - startTime + "ms");
    }


    public void importData(String resource, String encoding, ImportService importService) throws ImpExException
    {
        importData((ImpExResource)new ClasspathImpExResource(resource, encoding), importService);
    }


    public void importData(ImpExResource resource, ImportService importService) throws ImpExException
    {
        ImportConfig config = new ImportConfig();
        config.setScript(resource);
        importData(config, importService);
    }


    public ImportResult importData(ImportConfig config, ImportService importService) throws ImpExException
    {
        ImportResult result = importService.importData(config);
        if(result.isError() && result.hasUnresolvedLines())
        {
            throw new ImpExException(result.getUnresolvedLines().getPreview());
        }
        if(result.isError())
        {
            throw new ImpExException("Import failed.");
        }
        return result;
    }


    protected static void importCsv(String csvFile, String encoding) throws ImpExException
    {
        LOG.info("importing resource " + csvFile);
        Assert.assertNotNull("Given file is null", csvFile);
        InputStream is = ServicelayerTest.class.getResourceAsStream(csvFile);
        Assert.assertNotNull("Given file " + csvFile + " can not be found in classpath", is);
        importStream(is, encoding, csvFile);
    }


    protected static void importStream(InputStream is, String encoding, String resourceName) throws ImpExException
    {
        importStream(is, encoding, resourceName, true);
    }


    protected static void importStream(InputStream is, String encoding, String resourceName, boolean hijackExceptions) throws ImpExException
    {
        CSVReader reader = null;
        try
        {
            reader = new CSVReader(is, encoding);
        }
        catch(UnsupportedEncodingException e)
        {
            Assert.fail("Given encoding " + encoding + " is not supported");
        }
        MediaDataTranslator.setMediaDataHandler((MediaDataHandler)new DefaultMediaDataHandler());
        Importer importer = null;
        ImpExException error = null;
        try
        {
            importer = new Importer(reader);
            importer.getReader().enableCodeExecution(true);
            importer.setMaxPass(-1);
            importer.setDumpHandler((DumpHandler)new FirstLinesDumpReader());
            importer.importAll();
        }
        catch(ImpExException e)
        {
            if(!hijackExceptions)
            {
                throw e;
            }
            error = e;
        }
        finally
        {
            MediaDataTranslator.unsetMediaDataHandler();
            ((ModelContext)Registry.getCoreApplicationContext().getBean("modelContext", ModelContext.class)).clear();
        }
        if(importer.hasUnresolvedLines())
        {
            Assert.fail("Import has " + importer.getDumpedLineCountPerPass() + "+unresolved lines, first lines are:\n" + importer
                            .getDumpHandler().getDumpAsString());
        }
        Assert.assertNull("Import of resource " + resourceName + " failed" + ((error == null) ? "" : error.getMessage()), error);
        Assert.assertFalse("Import of resource " + resourceName + " failed", importer.hadError());
    }


    protected boolean isPrefetchModeNone()
    {
        String config = Registry.getMasterTenant().getConfig().getParameter("servicelayer.prefetch");
        return ("none".equals(config) || "default"
                        .equals(config));
    }
}
