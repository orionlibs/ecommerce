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
@SpecMetadata(filename = "SnAsSearchSpec.groovy", line = 15)
public class SnAsSearchSpec extends AbstractSnSearchDataDrivenSpec
{
    private static final String CATALOG_ID = "hwcatalog";
    private static final String CATALOG_VERSION_STAGED = "Staged";
    @Resource
    @FieldMetadata(line = 21, name = "catalogVersionService", ordinal = 0, initializer = false)
    private CatalogVersionService catalogVersionService;


    public Object loadData()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[0].callCurrent((GroovyObject)this);
        arrayOfCallSite[1].callStatic(SnAsSearchSpec.class, "/searchservices/test/integration/snIndexTypeAddCatalogs.impex", arrayOfCallSite[2].call(arrayOfCallSite[3].callGetProperty(StandardCharsets.class)));
        arrayOfCallSite[4].callStatic(SnAsSearchSpec.class, "/adaptivesearch/test/integration/searchservices/asSimpleSearchProfile.impex", arrayOfCallSite[5].call(arrayOfCallSite[6].callGetProperty(StandardCharsets.class)));
        return arrayOfCallSite[7].callStatic(SnAsSearchSpec.class, "/adaptivesearch/test/integration/asSearchProfileActivation.impex", arrayOfCallSite[8].call(arrayOfCallSite[9].callGetProperty(StandardCharsets.class)));
    }


    private Object setup()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return arrayOfCallSite[10].call(this.catalogVersionService, arrayOfCallSite[11].call(Collections.class));
    }


    private Object cleanup()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        return arrayOfCallSite[12].call(this.catalogVersionService, arrayOfCallSite[13].call(Collections.class));
    }


    @Test
    @FeatureMetadata(line = 40, name = "Search", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_7_0()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[14].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        SnSearchResult searchResult = (SnSearchResult)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[15].callCurrent((GroovyObject)this, arrayOfCallSite[16].callGroovyObjectGetProperty(this), ScriptBytecodeAdapter.getMethodPointer(this, "createSearchQuery"), ScriptBytecodeAdapter.getMethodPointer(this, "addDefaultSearchQuerySort")), SnSearchResult.class);
        arrayOfCallSite[17].callCurrent((GroovyObject)this, searchResult, new __spock_feature_7_0_closure1(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 55, name = "Search with promoted item", ordinal = 1, blocks = {@BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_7_1()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[18].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        SnSearchResult searchResult = (SnSearchResult)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[19].callCurrent((GroovyObject)this, arrayOfCallSite[20].callGroovyObjectGetProperty(this), ScriptBytecodeAdapter.getMethodPointer(this, "createSearchQuery"), ScriptBytecodeAdapter.getMethodPointer(this, "addDefaultSearchQuerySort"),
                                        new __spock_feature_7_1_closure2(this, this)), SnSearchResult.class);
        arrayOfCallSite[21].callCurrent((GroovyObject)this, searchResult, new __spock_feature_7_1_closure3(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 74, name = "Search with excluded item", ordinal = 2, blocks = {@BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_7_2()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[22].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        SnSearchResult searchResult = (SnSearchResult)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[23].callCurrent((GroovyObject)this, arrayOfCallSite[24].callGroovyObjectGetProperty(this), ScriptBytecodeAdapter.getMethodPointer(this, "createSearchQuery"), ScriptBytecodeAdapter.getMethodPointer(this, "addDefaultSearchQuerySort"),
                                        new __spock_feature_7_2_closure4(this, this)), SnSearchResult.class);
        arrayOfCallSite[25].callCurrent((GroovyObject)this, searchResult, new __spock_feature_7_2_closure5(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 90, name = "Apply search profile", ordinal = 3, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_7_3()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[26].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        arrayOfCallSite[27].call(this.catalogVersionService, CATALOG_ID, CATALOG_VERSION_STAGED);
        SnSearchResult searchResult = (SnSearchResult)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[28].callCurrent((GroovyObject)this, arrayOfCallSite[29].callGroovyObjectGetProperty(this), ScriptBytecodeAdapter.getMethodPointer(this, "createSearchQuery"), ScriptBytecodeAdapter.getMethodPointer(this, "addDefaultSearchQuerySort")), SnSearchResult.class);
        arrayOfCallSite[30].callCurrent((GroovyObject)this, searchResult, new __spock_feature_7_3_closure6(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 107, name = "Apply search profile, promote a document if it matches the search query", ordinal = 4, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_7_4()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[31].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        arrayOfCallSite[32].call(this.catalogVersionService, CATALOG_ID, CATALOG_VERSION_STAGED);
        SnSearchResult searchResult = (SnSearchResult)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[33].callCurrent((GroovyObject)this, arrayOfCallSite[34].callGroovyObjectGetProperty(this), ScriptBytecodeAdapter.getMethodPointer(this, "createSearchQuery"), ScriptBytecodeAdapter.getMethodPointer(this, "addDefaultSearchQuerySort"),
                                        new __spock_feature_7_4_closure7(this, this)), SnSearchResult.class);
        arrayOfCallSite[35].callCurrent((GroovyObject)this, searchResult, new __spock_feature_7_4_closure8(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 125, name = "Apply search profile, don't promote a document if it doesn't match the search query", ordinal = 5, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_7_5()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[36].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        arrayOfCallSite[37].call(this.catalogVersionService, CATALOG_ID, CATALOG_VERSION_STAGED);
        SnSearchResult searchResult = (SnSearchResult)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[38].callCurrent((GroovyObject)this, arrayOfCallSite[39].callGroovyObjectGetProperty(this), ScriptBytecodeAdapter.getMethodPointer(this, "createSearchQuery"), ScriptBytecodeAdapter.getMethodPointer(this, "addDefaultSearchQuerySort"),
                                        new __spock_feature_7_5_closure9(this, this)), SnSearchResult.class);
        arrayOfCallSite[40].callCurrent((GroovyObject)this, searchResult, new __spock_feature_7_5_closure10(this, this, $spock_errorCollector));
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
