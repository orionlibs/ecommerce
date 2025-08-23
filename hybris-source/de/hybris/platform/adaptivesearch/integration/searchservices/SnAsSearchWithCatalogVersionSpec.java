package de.hybris.platform.adaptivesearch.integration.searchservices;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.searchservices.integration.search.AbstractSnSearchDataDrivenSpec;
import de.hybris.platform.searchservices.search.data.SnSearchResult;
import groovy.lang.GroovyObject;
import groovy.lang.Reference;
import groovy.transform.Generated;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import javax.annotation.Resource;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.junit.Test;
import org.spockframework.mock.runtime.MockController;
import org.spockframework.runtime.ErrorCollector;
import org.spockframework.runtime.ErrorRethrower;
import org.spockframework.runtime.SpecificationContext;
import org.spockframework.runtime.model.BlockKind;
import org.spockframework.runtime.model.BlockMetadata;
import org.spockframework.runtime.model.FeatureMetadata;
import org.spockframework.runtime.model.FieldMetadata;
import org.spockframework.runtime.model.SpecMetadata;

@IntegrationTest
@SpecMetadata(filename = "SnAsSearchWithCatalogVersionSpec.groovy", line = 17)
public class SnAsSearchWithCatalogVersionSpec extends AbstractSnSearchDataDrivenSpec
{
    private static final String CATALOG_ID = "hwcatalog";
    private static final String CATALOG_VERSION_STAGED = "Staged";
    private static final String CATALOG_VERSION_ONLINE = "Online";
    @Resource
    @FieldMetadata(line = 24, name = "catalogVersionService", ordinal = 0, initializer = false)
    private CatalogVersionService catalogVersionService;


    private Object setup()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return arrayOfCallSite[0].call(this.catalogVersionService, arrayOfCallSite[1].call(Collections.class));
    }


    private Object cleanup()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return arrayOfCallSite[2].call(this.catalogVersionService, arrayOfCallSite[3].call(Collections.class));
    }


    public Object loadData()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[4].callCurrent((GroovyObject)this);
        arrayOfCallSite[5].callStatic(SnAsSearchWithCatalogVersionSpec.class, "/searchservices/test/integration/snCatalogCategories.impex", arrayOfCallSite[6].call(arrayOfCallSite[7].callGetProperty(StandardCharsets.class)));
        arrayOfCallSite[8].callStatic(SnAsSearchWithCatalogVersionSpec.class, "/searchservices/test/integration/snIndexTypeAddCatalogs.impex", arrayOfCallSite[9].call(arrayOfCallSite[10].callGetProperty(StandardCharsets.class)));
        arrayOfCallSite[11].callStatic(SnAsSearchWithCatalogVersionSpec.class, "/adaptivesearch/test/integration/searchservices/asCategoryAwareSearchProfile.impex", arrayOfCallSite[12].call(arrayOfCallSite[13].callGetProperty(StandardCharsets.class)));
        return arrayOfCallSite[14].callStatic(SnAsSearchWithCatalogVersionSpec.class, "/adaptivesearch/test/integration/asSearchProfileActivation.impex", arrayOfCallSite[15].call(arrayOfCallSite[16].callGetProperty(StandardCharsets.class)));
    }


    @Test
    @FeatureMetadata(line = 44, name = "Search profile is not activated without catalog version", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_7_0()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[17].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        SnSearchResult searchResult = (SnSearchResult)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[18].callCurrent((GroovyObject)this, arrayOfCallSite[19].callGroovyObjectGetProperty(this), ScriptBytecodeAdapter.getMethodPointer(this, "createSearchQuery"), ScriptBytecodeAdapter.getMethodPointer(this, "addDefaultSearchQuerySort")), SnSearchResult.class);
        arrayOfCallSite[20].callCurrent((GroovyObject)this, searchResult, new __spock_feature_7_0_closure1(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 59, name = "Search profile is activated for Staged catalog version", ordinal = 1, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_7_1()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[21].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        arrayOfCallSite[22].call(this.catalogVersionService, CATALOG_ID, CATALOG_VERSION_STAGED);
        SnSearchResult searchResult = (SnSearchResult)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[23].callCurrent((GroovyObject)this, arrayOfCallSite[24].callGroovyObjectGetProperty(this), ScriptBytecodeAdapter.getMethodPointer(this, "createSearchQuery"), ScriptBytecodeAdapter.getMethodPointer(this, "addDefaultSearchQuerySort")), SnSearchResult.class);
        arrayOfCallSite[25].callCurrent((GroovyObject)this, searchResult, new __spock_feature_7_1_closure2(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 77, name = "Search profile is not activated for Online catalog version", ordinal = 2, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_7_2()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[26].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        arrayOfCallSite[27].call(this.catalogVersionService, CATALOG_ID, CATALOG_VERSION_ONLINE);
        SnSearchResult searchResult = (SnSearchResult)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[28].callCurrent((GroovyObject)this, arrayOfCallSite[29].callGroovyObjectGetProperty(this), ScriptBytecodeAdapter.getMethodPointer(this, "createSearchQuery"), ScriptBytecodeAdapter.getMethodPointer(this, "addDefaultSearchQuerySort")), SnSearchResult.class);
        arrayOfCallSite[30].callCurrent((GroovyObject)this, searchResult, new __spock_feature_7_2_closure3(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Generated
    public static String getCATALOG_ID()
    {
        return CATALOG_ID;
    }


    @Generated
    public static String getCATALOG_VERSION_STAGED()
    {
        return CATALOG_VERSION_STAGED;
    }


    @Generated
    public static String getCATALOG_VERSION_ONLINE()
    {
        return CATALOG_VERSION_ONLINE;
    }


    @Generated
    public CatalogVersionService getCatalogVersionService()
    {
        return this.catalogVersionService;
    }


    @Generated
    public void setCatalogVersionService(CatalogVersionService paramCatalogVersionService)
    {
        this.catalogVersionService = paramCatalogVersionService;
    }
}
