package de.hybris.platform.adaptivesearch.integration.searchservices;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.adaptivesearch.model.AbstractAsSearchProfileModel;
import de.hybris.platform.adaptivesearch.services.AsSearchProfileService;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.searchservices.integration.search.AbstractSnSearchDataDrivenSpec;
import de.hybris.platform.searchservices.search.data.SnSearchResult;
import de.hybris.platform.servicelayer.model.ModelService;
import groovy.lang.GroovyObject;
import groovy.lang.Reference;
import groovy.transform.Generated;
import java.nio.charset.StandardCharsets;
import java.util.List;
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
@SpecMetadata(filename = "SnAsSearchQueryContextSpec.groovy", line = 21)
public class SnAsSearchQueryContextSpec extends AbstractSnSearchDataDrivenSpec
{
    private static final String CATALOG_ID = "hwcatalog";
    private static final String CATALOG_VERSION_STAGED = "Staged";
    private static final String SEARCH_PROFILE_CODE = "searchProfile";
    @Resource
    @FieldMetadata(line = 29, name = "modelService", ordinal = 0, initializer = false)
    private ModelService modelService;
    @Resource
    @FieldMetadata(line = 32, name = "catalogVersionService", ordinal = 1, initializer = false)
    private CatalogVersionService catalogVersionService;
    @Resource
    @FieldMetadata(line = 35, name = "asSearchProfileService", ordinal = 2, initializer = false)
    private AsSearchProfileService asSearchProfileService;


    public Object loadData()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[0].callCurrent((GroovyObject)this);
        arrayOfCallSite[1].callStatic(SnAsSearchQueryContextSpec.class, "/searchservices/test/integration/snIndexTypeAddCatalogs.impex", arrayOfCallSite[2].call(arrayOfCallSite[3].callGetProperty(StandardCharsets.class)));
        arrayOfCallSite[4].callStatic(SnAsSearchQueryContextSpec.class, "/adaptivesearch/test/integration/searchservices/asSimpleSearchProfile.impex", arrayOfCallSite[5].call(arrayOfCallSite[6].callGetProperty(StandardCharsets.class)));
        return arrayOfCallSite[7].callStatic(SnAsSearchQueryContextSpec.class, "/adaptivesearch/test/integration/asSearchProfileActivation.impex", arrayOfCallSite[8].call(arrayOfCallSite[9].callGetProperty(StandardCharsets.class)));
    }


    @Test
    @FeatureMetadata(line = 46, name = "Search with query context #testId", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {}), @BlockMetadata(kind = BlockKind.WHERE, texts = {})}, parameterNames = {"testId", "queryContexts",
                    "searchProfileQueryContext", "searchProfileEnabled"})
    public void $spock_feature_7_0(Object testId, Object queryContexts, Object searchProfileQueryContext, Object searchProfileEnabled)
    {
        Reference reference1 = new Reference(queryContexts), reference2 = new Reference(searchProfileEnabled);
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference $spock_errorCollector = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[10].callGetProperty(ErrorRethrower.class), ErrorCollector.class));
        CatalogVersionModel catalogVersion = (CatalogVersionModel)ScriptBytecodeAdapter.castToType(arrayOfCallSite[11].call(this.catalogVersionService, CATALOG_ID, CATALOG_VERSION_STAGED), CatalogVersionModel.class);
        arrayOfCallSite[12].call(this.catalogVersionService, arrayOfCallSite[13].call(List.class, catalogVersion));
        AbstractAsSearchProfileModel searchProfile = (AbstractAsSearchProfileModel)ScriptBytecodeAdapter.castToType(arrayOfCallSite[14].call(arrayOfCallSite[15].call(this.asSearchProfileService, catalogVersion, SEARCH_PROFILE_CODE)), AbstractAsSearchProfileModel.class);
        arrayOfCallSite[16].call(searchProfile, searchProfileQueryContext);
        arrayOfCallSite[17].call(this.modelService, searchProfile);
        SnSearchResult searchResult = (SnSearchResult)ScriptBytecodeAdapter.castToType(
                        arrayOfCallSite[18].callCurrent((GroovyObject)this, arrayOfCallSite[19].callGroovyObjectGetProperty(this), ScriptBytecodeAdapter.getMethodPointer(this, "createSearchQuery"), ScriptBytecodeAdapter.getMethodPointer(this, "addDefaultSearchQuerySort"),
                                        new __spock_feature_7_0_closure1(this, this, reference1)), SnSearchResult.class);
        arrayOfCallSite[20].callCurrent((GroovyObject)this, searchResult, new __spock_feature_7_0_closure2(this, this, $spock_errorCollector, reference2));
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
    public static String getSEARCH_PROFILE_CODE()
    {
        return SEARCH_PROFILE_CODE;
    }


    @Generated
    public ModelService getModelService()
    {
        return this.modelService;
    }


    @Generated
    public void setModelService(ModelService paramModelService)
    {
        this.modelService = paramModelService;
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


    @Generated
    public AsSearchProfileService getAsSearchProfileService()
    {
        return this.asSearchProfileService;
    }


    @Generated
    public void setAsSearchProfileService(AsSearchProfileService paramAsSearchProfileService)
    {
        this.asSearchProfileService = paramAsSearchProfileService;
    }
}
