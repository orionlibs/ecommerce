package de.hybris.deltadetection;

import com.google.common.base.Stopwatch;
import de.hybris.deltadetection.model.StreamConfigurationContainerModel;
import de.hybris.deltadetection.model.StreamConfigurationModel;
import de.hybris.platform.catalog.model.CatalogModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.user.TitleModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Y2YTestDataGenerator
{
    private final ModelService modelService;
    private final TypeService typeService;


    public Y2YTestDataGenerator(ModelService modelService, TypeService typeService)
    {
        this.modelService = modelService;
        this.typeService = typeService;
    }


    public ProductsFixture generateProducts(int itemsNumber)
    {
        return generateProducts(itemsNumber, itemsNumber, true);
    }


    public ProductsFixture generateProducts(int itemsNumber, int batchSaveSize, boolean generate)
    {
        if(generate)
        {
            generateProductItems(itemsNumber, batchSaveSize);
        }
        StreamConfigFixture fixture = getStreamConfigFixture((Class)ProductModel.class);
        return new ProductsFixture(fixture.getStreamId(), fixture.getComposedType());
    }


    public TitlesFixture generateTitles(int itemsNumber)
    {
        return generateTitles(itemsNumber, itemsNumber, true);
    }


    public TitlesFixture generateTitles(int itemsNumber, int batchSaveSize, boolean generate)
    {
        if(generate)
        {
            generateTitleItems(itemsNumber, batchSaveSize);
        }
        StreamConfigFixture fixture = getStreamConfigFixture((Class)TitleModel.class);
        return new TitlesFixture(fixture.getStreamId(), fixture.getComposedType());
    }


    private StreamConfigFixture getStreamConfigFixture(Class<? extends ItemModel> clazz)
    {
        ComposedTypeModel unitComposedType = this.typeService.getComposedTypeForClass(clazz);
        StreamConfigurationContainerModel streamCfgContainer = (StreamConfigurationContainerModel)this.modelService.create(StreamConfigurationContainerModel.class);
        streamCfgContainer.setId(UUID.randomUUID().toString());
        this.modelService.save(streamCfgContainer);
        String streamId = UUID.randomUUID().toString();
        StreamConfigurationModel streamCfg = (StreamConfigurationModel)this.modelService.create(StreamConfigurationModel.class);
        streamCfg.setStreamId(streamId);
        streamCfg.setContainer(streamCfgContainer);
        streamCfg.setItemTypeForStream(unitComposedType);
        streamCfg.setWhereClause("not used");
        streamCfg.setInfoExpression("#{getPk()}");
        this.modelService.save(streamCfg);
        return new StreamConfigFixture(streamId, unitComposedType);
    }


    private void generateTitleItems(int titlesNumber, int batchSaveSize)
    {
        List<TitleModel> titles = new ArrayList<>();
        Stopwatch started = Stopwatch.createStarted();
        System.out.println("Starting titles generation");
        for(int i = 0; i < titlesNumber; i++)
        {
            TitleModel title = (TitleModel)this.modelService.create(TitleModel.class);
            title.setCode(UUID.randomUUID().toString() + UUID.randomUUID().toString());
            titles.add(title);
            if(i > 0 && batchSaveSize < titlesNumber && i % batchSaveSize == 0)
            {
                this.modelService.saveAll(titles);
                titles.clear();
                System.out.println("Batch save " + i + " of " + titlesNumber + " elapsed: " + started.elapsed(TimeUnit.SECONDS) + " " + TimeUnit.SECONDS);
            }
        }
        this.modelService.saveAll(titles);
        System.out.println("Saved titles: " + started.elapsed(TimeUnit.SECONDS) + " " + TimeUnit.SECONDS);
    }


    private void generateProductItems(int productsNumber, int batchSaveSize)
    {
        List<ProductModel> products = new ArrayList<>();
        CatalogModel catalogModel = (CatalogModel)this.modelService.create(CatalogModel.class);
        CatalogVersionModel staged = (CatalogVersionModel)this.modelService.create(CatalogVersionModel.class);
        catalogModel.setId("id");
        staged.setCatalog(catalogModel);
        staged.setVersion("staged");
        this.modelService.saveAll(new Object[] {catalogModel, staged});
        Stopwatch started = Stopwatch.createStarted();
        System.out.println("Starting products generation");
        for(int i = 0; i < productsNumber; i++)
        {
            ProductModel product = (ProductModel)this.modelService.create(ProductModel.class);
            product.setCode(UUID.randomUUID().toString() + UUID.randomUUID().toString());
            product.setCatalogVersion(staged);
            products.add(product);
            if(i > 0 && batchSaveSize < productsNumber && i % batchSaveSize == 0)
            {
                this.modelService.saveAll(products);
                products.clear();
                System.out.println("Batch save " + i + " of " + productsNumber + " elapsed: " + started.elapsed(TimeUnit.SECONDS) + " " + TimeUnit.SECONDS);
            }
        }
        this.modelService.saveAll(products);
        System.out.println("Saved products: " + started.elapsed(TimeUnit.SECONDS) + " " + TimeUnit.SECONDS);
    }
}
