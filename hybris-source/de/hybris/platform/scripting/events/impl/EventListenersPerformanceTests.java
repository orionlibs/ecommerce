package de.hybris.platform.scripting.events.impl;

import com.google.common.base.Stopwatch;
import de.hybris.bootstrap.annotations.PerformanceTest;
import de.hybris.platform.scripting.events.TestDummyEvent;
import de.hybris.platform.scripting.events.TestPerformanceEvent;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.user.daos.TitleDao;
import javax.annotation.Resource;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationEvent;

@PerformanceTest
public class EventListenersPerformanceTests extends ServicelayerTransactionalBaseTest
{
    @Resource
    private TitleDao titleDao;
    @Resource
    private DefaultScriptingEventService scriptingEventService;
    private Stopwatch stopwatch;
    private TestPerformanceEvent testPerformanceEvent;
    private TestDummyEvent testDummyEvent;
    private static final String URI_FOR_SCRIPT = "classpath://test/test-script-performance-listener.groovy";
    private static final int ITEMS_TO_SAVE = 30;
    private static final int DUMMY_EVENTS_TO_BE_EXECUTED = 200;


    @Before
    public void setUp() throws Exception
    {
        this.stopwatch = Stopwatch.createUnstarted();
        this.testPerformanceEvent = new TestPerformanceEvent(30);
        this.testDummyEvent = new TestDummyEvent();
        for(int i = 0; i < 30; i++)
        {
            Assertions.assertThat(this.titleDao.findTitleByCode("testTitle" + i)).isNull();
        }
    }


    @Test
    public void testStandardListenerOneSlowEvent() throws Exception
    {
        TestStandardEventListener myStandardEventListener = new TestStandardEventListener();
        this.stopwatch.start();
        myStandardEventListener.onEvent((AbstractEvent)this.testPerformanceEvent);
        this.stopwatch.stop();
        System.out.println("##########standard event listener result#############");
        System.out.println("One slow event handling processed in: " + this.stopwatch.toString());
        System.out.println("########################################");
        assertAllItemsCreated();
    }


    @Test
    public void testStandardListenerManyFastEvents() throws Exception
    {
        TestStandardEventListener myStandardEventListener = new TestStandardEventListener();
        this.stopwatch.start();
        for(int i = 0; i < 200; i++)
        {
            myStandardEventListener.onEvent((AbstractEvent)this.testDummyEvent);
        }
        this.stopwatch.stop();
        System.out.println("##########standard event listener result#############");
        System.out.println("Many fast events handling processed in: " + this.stopwatch.toString());
        System.out.println("########################################");
    }


    private void assertAllItemsCreated()
    {
        for(int i = 0; i < 30; i++)
        {
            Assertions.assertThat(this.titleDao.findTitleByCode("testTitle" + i)).isNotNull();
        }
    }


    @Test
    public void testScriptingListenerOneSlowEvent() throws Exception
    {
        ScriptingListenerWrapper myScriptingListener = getScriptingListenerWrapper();
        this.stopwatch.start();
        myScriptingListener.onApplicationEvent((ApplicationEvent)this.testPerformanceEvent);
        this.stopwatch.stop();
        System.out.println("##########scripting event listener result#############");
        System.out.println("One slow event handling processed in: " + this.stopwatch.toString());
        System.out.println("########################################");
        assertAllItemsCreated();
    }


    @Test
    public void testScriptingListenerManyFastEvents() throws Exception
    {
        ScriptingListenerWrapper myScriptingListener = getScriptingListenerWrapper();
        this.stopwatch.start();
        for(int i = 0; i < 200; i++)
        {
            myScriptingListener.onApplicationEvent((ApplicationEvent)this.testDummyEvent);
        }
        this.stopwatch.stop();
        System.out.println("##########scripting event listener result#############");
        System.out.println("Many fast events handling processed in: " + this.stopwatch.toString());
        System.out.println("########################################");
    }


    private ScriptingListenerWrapper getScriptingListenerWrapper()
    {
        return new ScriptingListenerWrapper("classpath://test/test-script-performance-listener.groovy", this.scriptingEventService);
    }
}
