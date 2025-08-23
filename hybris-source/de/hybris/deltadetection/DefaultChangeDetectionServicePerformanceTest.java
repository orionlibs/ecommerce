package de.hybris.deltadetection;

import com.google.common.base.Stopwatch;
import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.deltadetection.impl.InMemoryChangesCollector;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.testframework.PropertyConfigSwitcher;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

@PerformanceTest
public class DefaultChangeDetectionServicePerformanceTest extends ServicelayerBaseTest
{
    @Resource
    private ModelService modelService;
    @Resource
    private TypeService typeService;
    @Resource
    private ChangeDetectionService changeDetectionService;
    private final PropertyConfigSwitcher legacyMode = new PropertyConfigSwitcher("persistence.legacy.mode");


    @Before
    public void prepareProducts()
    {
        this.legacyMode.switchToValue("false");
    }


    @After
    public void tearDown()
    {
        this.legacyMode.switchBackToDefault();
    }


    @Test
    public void changeConsumptionPerformanceTest()
    {
        int productsNumber = 1000;
        Y2YTestDataGenerator.ProductsFixture productsFixture = getProductsFixture(1000);
        Stopwatch started = Stopwatch.createStarted();
        InMemoryChangesCollector changesCollector = new InMemoryChangesCollector();
        System.out.println("Starting changes consumption");
        this.changeDetectionService.collectChangesForType(productsFixture.getComposedType(), productsFixture.getStreamId(), (ChangesCollector)changesCollector);
        System.out.println("Changes collected: " + started.elapsed(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS);
        this.changeDetectionService.consumeChanges(changesCollector.getChanges());
        System.out.println("Changes consumed: " + started.elapsed(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS);
    }


    @Test
    public void testVeryLargeQueryItemsCollection()
    {
        int productsNumber = 1000000;
        Y2YTestDataGenerator.ProductsFixture productsFixture = getProductsFixture(1000000, 10000, false);
        Stopwatch started = Stopwatch.createStarted();
        InMemoryChangesCollector changesCollector = new InMemoryChangesCollector();
        System.out.println("Starting changes consumption");
        this.changeDetectionService.collectChangesForType(productsFixture.getComposedType(), productsFixture.getStreamId(), (ChangesCollector)changesCollector);
        System.out.println("Changes collected: " + started.elapsed(TimeUnit.MILLISECONDS) + " " + TimeUnit.MILLISECONDS);
    }


    private Y2YTestDataGenerator.ProductsFixture getProductsFixture(int prodNumber)
    {
        return getProductsFixture(prodNumber, prodNumber, true);
    }


    private Y2YTestDataGenerator.ProductsFixture getProductsFixture(int prodNumber, int batchSaveSize, boolean generate)
    {
        Y2YTestDataGenerator y2YTestDataGenerator = new Y2YTestDataGenerator(this.modelService, this.typeService);
        return y2YTestDataGenerator.generateProducts(prodNumber, batchSaveSize, generate);
    }
}
