package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public final class SynchronizationTestHelper
{
    private final CatalogVersionModel srcCatalogVersion;
    private final CatalogVersionModel dstCatalogVersion;
    private final ConfigureSyncCronJob configure;
    private final SyncOperation[] operations;


    private SynchronizationTestHelper(CatalogVersionModel srcCatalogVersion, CatalogVersionModel dstCatalogVersion, ConfigureSyncCronJob configure, SyncOperation[] operations)
    {
        this.srcCatalogVersion = Objects.<CatalogVersionModel>requireNonNull(srcCatalogVersion);
        this.dstCatalogVersion = Objects.<CatalogVersionModel>requireNonNull(dstCatalogVersion);
        this.configure = Objects.<ConfigureSyncCronJob>requireNonNull(configure);
        this.operations = Objects.<SyncOperation[]>requireNonNull(operations);
    }


    public static Builder builder(CatalogVersionModel srcCatalogVersion, CatalogVersionModel dstCatalogVersion)
    {
        return new Builder(srcCatalogVersion, dstCatalogVersion);
    }


    public static void givenTestCatalogWithVersions(ModelService modelService, String catalogId, String srcCatalogVersion, String dstCatalogVersion, String category, String... product)
    {
        CatalogModel catalog = (CatalogModel)modelService.create(CatalogModel.class);
        catalog.setId(catalogId);
        CatalogVersionModel sourceVersion = (CatalogVersionModel)modelService.create(CatalogVersionModel.class);
        sourceVersion.setCatalog(catalog);
        sourceVersion.setVersion(srcCatalogVersion);
        CatalogVersionModel targetVersion = (CatalogVersionModel)modelService.create(CatalogVersionModel.class);
        targetVersion.setCatalog(catalog);
        targetVersion.setVersion(dstCatalogVersion);
        CategoryModel category1 = (CategoryModel)modelService.create(CategoryModel.class);
        category1.setCatalogVersion(sourceVersion);
        category1.setCode(category);
        List<ProductModel> products = (List<ProductModel>)Arrays.<String>stream(product).map(productCode -> {
            ProductModel p1 = (ProductModel)modelService.create(ProductModel.class);
            p1.setCatalogVersion(sourceVersion);
            p1.setCode(productCode);
            p1.setSupercategories(Collections.singletonList(category1));
            return p1;
        }).collect(Collectors.toList());
        category1.setProducts(products);
        modelService.saveAll();
    }


    public void performSynchronization()
    {
        Map<Object, Object> args = new HashMap<>();
        args.put("code", "[" + System.currentTimeMillis() + "]" + this.srcCatalogVersion.getVersion() + "->" + this.dstCatalogVersion
                        .getVersion());
        args.put("sourceVersion", this.srcCatalogVersion.getItemModelContext().getSource());
        args.put("targetVersion", this.dstCatalogVersion.getItemModelContext().getSource());
        CatalogVersionSyncJob syncJob = CatalogManager.getInstance().createCatalogVersionSyncJob(args);
        CatalogVersionSyncCronJob syncCronJob = (CatalogVersionSyncCronJob)syncJob.newExecution();
        for(SyncOperation operation : this.operations)
        {
            syncCronJob.addPendingItem(operation.getSrcPk(), operation.getDstPk());
        }
        this.configure.configure(syncCronJob);
        syncCronJob.getJob().perform((CronJob)syncCronJob, true);
    }


    public static SyncOperation create(ItemModel item)
    {
        return new SyncOperation(Objects.<ItemModel>requireNonNull(item), null);
    }


    public static SyncOperation remove(ItemModel item)
    {
        return new SyncOperation(null, Objects.<ItemModel>requireNonNull(item));
    }


    public static SyncOperation update(ItemModel srcItem, ItemModel dstItem)
    {
        return new SyncOperation(Objects.<ItemModel>requireNonNull(srcItem), Objects.<ItemModel>requireNonNull(dstItem));
    }
}
