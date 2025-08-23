package de.hybris.y2ysync.task.runner.internal;

import com.google.common.collect.ImmutableList;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.deltadetection.ItemChangeDTO;
import de.hybris.deltadetection.enums.ChangeType;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.ProductService;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.impex.ExportService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@IntegrationTest
public class ImportScriptCreatorTest extends ServicelayerBaseTest
{
    private static final String TYPE_CODE = "Product";
    private static final String IMPEX_HEADER = "code[unique=true];ean";
    @Resource
    private ModelService modelService;
    @Resource
    private ExportService exportService;
    @Resource
    private ProductService productService;
    @Resource
    private UserService userService;
    private ProductModel product1;
    private ProductModel product2;
    private ProductModel product3;
    private ProductModel product4;


    @Before
    public void setUp()
    {
        CatalogModel testCatalog = (CatalogModel)this.modelService.create(CatalogModel.class);
        testCatalog.setId(uniqueId());
        CatalogVersionModel testVersion = (CatalogVersionModel)this.modelService.create(CatalogVersionModel.class);
        testVersion.setCatalog(testCatalog);
        testVersion.setVersion(uniqueId());
        this.product1 = createProduct(testVersion);
        this.product2 = createProduct(testVersion);
        this.product3 = createProduct(testVersion);
        this.product4 = createProduct(testVersion);
        this.modelService.saveAll();
    }


    @Test
    public void shouldNotGenerateImportScriptsWhenThereAreNoChanges()
    {
        ImportScriptCreator creator = givenImportScriptCreator(new ItemChangeDTO[0]);
        Collection<ImportScript> scripts = creator.createImportScripts();
        Assertions.assertThat(scripts).isEmpty();
    }


    @Test
    public void shouldGenerateOnlyRemoveScriptForRemovedItem()
    {
        ImportScriptCreator creator = givenImportScriptCreator(new ItemChangeDTO[] {removed(this.product1)});
        Collection<ImportScript> scripts = creator.createImportScripts();
        TestableImportScript removeScripts = getRemoveScript(scripts);
        TestableImportScript insertUpdateScript = getInsertUpdateScript(scripts);
        Assertions.assertThat(scripts).hasSize(1);
        Assertions.assertThat((Iterable)removeScripts).isNotNull();
        Assertions.assertThat((Iterable)insertUpdateScript).isNull();
        Assertions.assertThat((Iterable)removeScripts).containsOnly((Object[])new PK[] {this.product1.getPk()});
    }


    @Test
    public void shouldGenerateOnlyInsertUpdateScriptForAddedItem()
    {
        ImportScriptCreator creator = givenImportScriptCreator(new ItemChangeDTO[] {added(this.product2)});
        Collection<ImportScript> scripts = creator.createImportScripts();
        TestableImportScript removeScripts = getRemoveScript(scripts);
        TestableImportScript insertUpdateScript = getInsertUpdateScript(scripts);
        Assertions.assertThat(scripts).hasSize(1);
        Assertions.assertThat((Iterable)removeScripts).isNull();
        Assertions.assertThat((Iterable)insertUpdateScript).isNotNull();
        Assertions.assertThat((Iterable)insertUpdateScript).containsOnly((Object[])new PK[] {this.product2.getPk()});
    }


    @Test
    public void shouldGenerateInsertUpdateScriptForModifiedItem()
    {
        ImportScriptCreator creator = givenImportScriptCreator(new ItemChangeDTO[] {modified(this.product3)});
        Collection<ImportScript> scripts = creator.createImportScripts();
        TestableImportScript removeScripts = getRemoveScript(scripts);
        TestableImportScript insertUpdateScript = getInsertUpdateScript(scripts);
        Assertions.assertThat(scripts).hasSize(1);
        Assertions.assertThat((Iterable)removeScripts).isNull();
        Assertions.assertThat((Iterable)insertUpdateScript).isNotNull();
        Assertions.assertThat((Iterable)insertUpdateScript).containsOnly((Object[])new PK[] {this.product3.getPk()});
    }


    @Test
    public void shouldGenerateInsertUpdateScriptForModifiedAndAddedItem()
    {
        ImportScriptCreator creator = givenImportScriptCreator(new ItemChangeDTO[] {modified(this.product3), added(this.product4)});
        Collection<ImportScript> scripts = creator.createImportScripts();
        TestableImportScript removeScripts = getRemoveScript(scripts);
        TestableImportScript insertUpdateScript = getInsertUpdateScript(scripts);
        Assertions.assertThat(scripts).hasSize(1);
        Assertions.assertThat((Iterable)removeScripts).isNull();
        Assertions.assertThat((Iterable)insertUpdateScript).isNotNull();
        Assertions.assertThat((Iterable)insertUpdateScript).containsOnly((Object[])new PK[] {this.product4.getPk(), this.product3.getPk()});
    }


    @Test
    public void shouldGenerateInsertUpdateAndRemoveScripts()
    {
        ImportScriptCreator creator = givenImportScriptCreator(new ItemChangeDTO[] {modified(this.product3), removed(this.product2), added(this.product4),
                        removed(this.product1)});
        Collection<ImportScript> scripts = creator.createImportScripts();
        TestableImportScript removeScripts = getRemoveScript(scripts);
        TestableImportScript insertUpdateScript = getInsertUpdateScript(scripts);
        Assertions.assertThat(scripts).hasSize(2);
        Assertions.assertThat((Iterable)removeScripts).isNotNull();
        Assertions.assertThat((Iterable)insertUpdateScript).isNotNull();
        Assertions.assertThat((Iterable)removeScripts).containsOnly((Object[])new PK[] {this.product2.getPk(), this.product1.getPk()});
        Assertions.assertThat((Iterable)insertUpdateScript).containsOnly((Object[])new PK[] {this.product4.getPk(), this.product3.getPk()});
    }


    private TestableImportScript getRemoveScript(Collection<ImportScript> scripts)
    {
        return scripts.stream().map(x$0 -> new TestableImportScript(this, x$0)).filter(TestableImportScript::isRemoveScript).findFirst()
                        .orElse(null);
    }


    private TestableImportScript getInsertUpdateScript(Collection<ImportScript> scripts)
    {
        return scripts.stream().map(x$0 -> new TestableImportScript(this, x$0)).filter(TestableImportScript::isInsertUpdateScript).findFirst()
                        .orElse(null);
    }


    private ProductModel createProduct(CatalogVersionModel catalogVersion)
    {
        ProductModel product = (ProductModel)this.modelService.create(ProductModel.class);
        String id = uniqueId();
        product.setCatalogVersion(catalogVersion);
        product.setCode("CODE_" + id);
        product.setEan("EAN_" + id);
        return product;
    }


    private ImportScriptCreator givenImportScriptCreator(ItemChangeDTO... changes)
    {
        ExportScriptCreator exportScriptCreator = new ExportScriptCreator("code[unique=true];ean", "Product", (Collection)ImmutableList.copyOf((Object[])changes));
        return new ImportScriptCreator(this.modelService, this.exportService, exportScriptCreator, this.userService);
    }


    private static ItemChangeDTO added(ProductModel item)
    {
        return new ItemChangeDTO(item.getPk().getLong(), new Date(), ChangeType.NEW, "Added", "Product", "testStream");
    }


    private static ItemChangeDTO modified(ProductModel item)
    {
        return new ItemChangeDTO(item.getPk().getLong(), new Date(), ChangeType.MODIFIED, "Modified", "Product", "testStream");
    }


    private static ItemChangeDTO removed(ProductModel item)
    {
        return new ItemChangeDTO(item.getPk().getLong(), new Date(), ChangeType.DELETED, item.getCode(), "Product", "testStream");
    }


    private static String uniqueId()
    {
        return UUID.randomUUID().toString();
    }
}
