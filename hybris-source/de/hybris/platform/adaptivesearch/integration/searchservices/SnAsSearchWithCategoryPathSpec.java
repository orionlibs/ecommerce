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
@SpecMetadata(filename = "SnAsSearchWithCategoryPathSpec.groovy", line = 21)
public class SnAsSearchWithCategoryPathSpec extends AbstractSnSearchDataDrivenSpec
{
    private static final String CATALOG_ID = "hwcatalog";
    private static final String CATALOG_VERSION_STAGED = "Staged";
    private static final String CATEGORY_CAT10_CODE = "cat10";
    private static final String CATEGORY_CAT12_CODE = "cat12";
    @Resource
    @FieldMetadata(line = 30, name = "catalogVersionService", ordinal = 0, initializer = false)
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
        arrayOfCallSite[5].callStatic(SnAsSearchWithCategoryPathSpec.class, "/searchservices/test/integration/snCatalogCategories.impex", arrayOfCallSite[6].call(arrayOfCallSite[7].callGetProperty(StandardCharsets.class)));
        arrayOfCallSite[8].callStatic(SnAsSearchWithCategoryPathSpec.class, "/searchservices/test/integration/snIndexTypeAddCatalogs.impex", arrayOfCallSite[9].call(arrayOfCallSite[10].callGetProperty(StandardCharsets.class)));
        arrayOfCallSite[11].callStatic(SnAsSearchWithCategoryPathSpec.class, "/adaptivesearch/test/integration/searchservices/asCatalogProductCategories.impex", arrayOfCallSite[12].call(arrayOfCallSite[13].callGetProperty(StandardCharsets.class)));
        arrayOfCallSite[14].callStatic(SnAsSearchWithCategoryPathSpec.class, "/adaptivesearch/test/integration/searchservices/asIndexTypeAddAllCategoriesProperty.impex", arrayOfCallSite[15].call(arrayOfCallSite[16].callGetProperty(StandardCharsets.class)));
        arrayOfCallSite[17].callStatic(SnAsSearchWithCategoryPathSpec.class, "/adaptivesearch/test/integration/searchservices/asCategoryAwareSearchProfile.impex", arrayOfCallSite[18].call(arrayOfCallSite[19].callGetProperty(StandardCharsets.class)));
        return arrayOfCallSite[20].callStatic(SnAsSearchWithCategoryPathSpec.class, "/adaptivesearch/test/integration/asSearchProfileActivation.impex", arrayOfCallSite[21].call(arrayOfCallSite[22].callGetProperty(StandardCharsets.class)));
    }


    @Test
    @FeatureMetadata(line = 52, name = "Search profile is activated for Staged catalog version, search for category global", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_7_0()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[23].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        arrayOfCallSite[24].call(this.catalogVersionService, CATALOG_ID, CATALOG_VERSION_STAGED);
        SnSearchResult searchResult = (SnSearchResult)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[25].callCurrent((GroovyObject)this, arrayOfCallSite[26].callGroovyObjectGetProperty(this), ScriptBytecodeAdapter.getMethodPointer(this, "createSearchQuery"), ScriptBytecodeAdapter.getMethodPointer(this, "addDefaultSearchQuerySort")), SnSearchResult.class);
        arrayOfCallSite[27].callCurrent((GroovyObject)this, searchResult, new __spock_feature_7_0_closure1(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 70, name = "Search profile is activated for Staged catalog version, search for category cat10", ordinal = 1, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_7_1()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[28].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        arrayOfCallSite[29].call(this.catalogVersionService, CATALOG_ID, CATALOG_VERSION_STAGED);
        SnSearchResult searchResult = (SnSearchResult)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[30].callCurrent((GroovyObject)this, arrayOfCallSite[31].callGroovyObjectGetProperty(this), ScriptBytecodeAdapter.getMethodPointer(this, "createSearchQuery"), ScriptBytecodeAdapter.getMethodPointer(this, "addDefaultSearchQuerySort"),
                                        new __spock_feature_7_1_closure2(this, this)), SnSearchResult.class);
        arrayOfCallSite[32].callCurrent((GroovyObject)this, searchResult, new __spock_feature_7_1_closure3(this, this, $spock_errorCollector));
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 91, name = "Search profile is activated for Staged catalog version, search for category cat12", ordinal = 2, blocks = {@BlockMetadata(kind = BlockKind.SETUP, texts = {}), @BlockMetadata(kind = BlockKind.WHEN, texts = {}),
                    @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_7_2()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[33].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        arrayOfCallSite[34].call(this.catalogVersionService, CATALOG_ID, CATALOG_VERSION_STAGED);
        SnSearchResult searchResult = (SnSearchResult)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[35].callCurrent((GroovyObject)this, arrayOfCallSite[36].callGroovyObjectGetProperty(this), ScriptBytecodeAdapter.getMethodPointer(this, "createSearchQuery"), ScriptBytecodeAdapter.getMethodPointer(this, "addDefaultSearchQuerySort"),
                                        new __spock_feature_7_2_closure4(this, this)), SnSearchResult.class);
        arrayOfCallSite[37].callCurrent((GroovyObject)this, searchResult, new __spock_feature_7_2_closure5(this, this, $spock_errorCollector));
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
    public static String getCATEGORY_CAT10_CODE()
    {
        return CATEGORY_CAT10_CODE;
    }


    @Generated
    public static String getCATEGORY_CAT12_CODE()
    {
        return CATEGORY_CAT12_CODE;
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
