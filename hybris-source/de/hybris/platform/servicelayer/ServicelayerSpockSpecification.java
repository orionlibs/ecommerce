package de.hybris.platform.servicelayer;

import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.servicelayer.impex.ImpExResource;
import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.ImportService;
import java.io.InputStream;
import javax.annotation.Resource;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;
import org.junit.Ignore;
import org.spockframework.runtime.model.FieldMetadata;
import org.spockframework.runtime.model.SpecMetadata;

@Ignore
@SpecMetadata(filename = "ServicelayerSpockSpecification.groovy", line = 15)
public class ServicelayerSpockSpecification extends ServicelayerBaseSpecification
{
    @Resource
    @FieldMetadata(line = 17, name = "importService", ordinal = 0, initializer = false)
    protected ImportService importService;
    @FieldMetadata(line = 20, name = "servicelayerTestLogic", ordinal = 1, initializer = false)
    private ServicelayerTestLogic servicelayerTestLogic;


    private Object setup()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Object object = arrayOfCallSite[0].callConstructor(ServicelayerTestLogic.class);
        this.servicelayerTestLogic = (ServicelayerTestLogic)ScriptBytecodeAdapter.castToType(object, ServicelayerTestLogic.class);
        return object;
    }


    public static void createCoreData() throws Exception
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[1].call(ServicelayerTestLogic.class);
    }


    public static void createDefaultCatalog() throws Exception
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[2].call(ServicelayerTestLogic.class);
    }


    public static void createHardwareCatalog() throws Exception
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[3].call(ServicelayerTestLogic.class);
    }


    public static void createDefaultUsers() throws Exception
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[4].call(ServicelayerTestLogic.class);
    }


    public void importData(String resource, String encoding) throws ImpExException
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[5].call(this.servicelayerTestLogic, resource, encoding, this.importService);
    }


    public void importData(ImpExResource resource) throws ImpExException
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[6].call(this.servicelayerTestLogic, resource, this.importService);
    }


    public ImportResult importData(ImportConfig config) throws ImpExException
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return (ImportResult)ScriptBytecodeAdapter.castToType(arrayOfCallSite[7].call(this.servicelayerTestLogic, config, this.importService), ImportResult.class);
    }


    protected static void importCsv(String csvFile, String encoding) throws ImpExException
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[8].call(ServicelayerTestLogic.class, csvFile, encoding);
    }


    protected static void importStream(InputStream is, String encoding, String resourceName) throws ImpExException
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[9].call(ServicelayerTestLogic.class, is, encoding, resourceName);
    }


    protected static void importStream(InputStream is, String encoding, String resourceName, boolean hijackExceptions) throws ImpExException
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[10].call(ServicelayerTestLogic.class, is, encoding, resourceName, Boolean.valueOf(hijackExceptions));
    }


    protected boolean isPrefetchModeNone()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[11].call(this.servicelayerTestLogic));
    }
}
