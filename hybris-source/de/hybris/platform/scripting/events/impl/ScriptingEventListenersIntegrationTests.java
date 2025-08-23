package de.hybris.platform.scripting.events.impl;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.scripting.engine.exception.ScriptNotFoundException;
import de.hybris.platform.scripting.enums.ScriptType;
import de.hybris.platform.scripting.events.TestScriptingEvent;
import de.hybris.platform.scripting.events.TestSingleton;
import de.hybris.platform.scripting.model.ScriptModel;
import de.hybris.platform.servicelayer.ServicelayerTransactionalBaseTest;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.daos.TitleDao;
import de.hybris.platform.util.Config;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import javax.annotation.Resource;
import junit.framework.Assert;
import org.assertj.core.api.Assertions;
import org.junit.Test;

@IntegrationTest
public class ScriptingEventListenersIntegrationTests extends ServicelayerTransactionalBaseTest
{
    @Resource
    private DefaultScriptingEventService scriptingEventService;
    @Resource
    private EventService eventService;
    @Resource
    private TitleDao titleDao;
    @Resource
    private ModelService modelService;
    private static final String URI_FOR_CORRECT_SCRIPT = "classpath://test/test-script-correct-listener-interface.groovy";
    private static final String URI_FOR_CORRECT_SCRIPT_WITH_TYPED_EVENT = "classpath://test/test-script-correct-listener-interface-with-typed-event.groovy";
    private static final String URI_FOR_ERROR_SCRIPT = "classpath://test/test-script-error-listener-interface.groovy";
    final double timeFactor = Math.max(5.0D, Config.getDouble("platform.test.timefactor", 5.0D));


    @Test
    public void testHandleEventForCorrectListener() throws Exception
    {
        String testEventName = "testEventNameAndTitleCodeForScript";
        Assertions.assertThat(this.scriptingEventService.registerScriptingEventListener("classpath://test/test-script-correct-listener-interface.groovy")).isTrue();
        Thread.sleep((long)(1000.0D * this.timeFactor));
        Assertions.assertThat(this.titleDao.findTitleByCode("testEventNameAndTitleCodeForScript")).isNull();
        this.eventService.publishEvent((AbstractEvent)new TestScriptingEvent("testEventNameAndTitleCodeForScript"));
        Thread.sleep((long)(1000.0D * this.timeFactor));
        Assertions.assertThat(this.titleDao.findTitleByCode("testEventNameAndTitleCodeForScript")).isNotNull();
        Assertions.assertThat(this.scriptingEventService.unregisterScriptingEventListener("classpath://test/test-script-correct-listener-interface.groovy")).isTrue();
        this.eventService.publishEvent((AbstractEvent)new TestScriptingEvent("blabla"));
        Thread.sleep((long)(1000.0D * this.timeFactor));
        Assertions.assertThat(this.titleDao.findTitleByCode("blabla")).isNull();
    }


    @Test
    public void testHandleTypedEventForCorrectListener() throws Exception
    {
        String testEventName = "testTypedEventNameAndTitleCodeForScript";
        Assertions.assertThat(this.scriptingEventService.registerScriptingEventListener("classpath://test/test-script-correct-listener-interface-with-typed-event.groovy")).isTrue();
        Thread.sleep((long)(1000.0D * this.timeFactor));
        Assertions.assertThat(this.titleDao.findTitleByCode("testTypedEventNameAndTitleCodeForScript")).isNull();
        this.eventService.publishEvent((AbstractEvent)new TestScriptingEvent("testTypedEventNameAndTitleCodeForScript"));
        Thread.sleep((long)(1000.0D * this.timeFactor));
        Assertions.assertThat(this.titleDao.findTitleByCode("testTypedEventNameAndTitleCodeForScript")).isNotNull();
        Assertions.assertThat(this.scriptingEventService.unregisterScriptingEventListener("classpath://test/test-script-correct-listener-interface-with-typed-event.groovy")).isTrue();
        this.eventService.publishEvent((AbstractEvent)new TestScriptingEvent("blabla"));
        Thread.sleep((long)(1000.0D * this.timeFactor));
        Assertions.assertThat(this.titleDao.findTitleByCode("blabla")).isNull();
    }


    @Test
    public void testHandleEventForListenerThrowingException() throws Exception
    {
        String testEventName = "testEventNameAndTitleCodeForScriptWithErrors";
        Assertions.assertThat(this.scriptingEventService.registerScriptingEventListener("classpath://test/test-script-error-listener-interface.groovy")).isTrue();
        try
        {
            Thread.sleep((long)(1000.0D * this.timeFactor));
            Assertions.assertThat(this.titleDao.findTitleByCode("testEventNameAndTitleCodeForScriptWithErrors")).isNull();
            this.eventService.publishEvent((AbstractEvent)new TestScriptingEvent("testEventNameAndTitleCodeForScriptWithErrors"));
            Thread.sleep((long)(1000.0D * this.timeFactor));
        }
        finally
        {
            Assertions.assertThat(this.titleDao.findTitleByCode("testEventNameAndTitleCodeForScriptWithErrors")).isNull();
            Assertions.assertThat(this.scriptingEventService.unregisterScriptingEventListener("classpath://test/test-script-error-listener-interface.groovy")).isTrue();
            this.eventService.publishEvent((AbstractEvent)new TestScriptingEvent("foobar"));
            Thread.sleep((long)(1000.0D * this.timeFactor));
            Assertions.assertThat(this.titleDao.findTitleByCode("foobar")).isNull();
        }
    }


    @Test
    public void testRegisterListenerUnsuccessfulForNotExistingScript() throws Exception
    {
        try
        {
            this.scriptingEventService.registerScriptingEventListener("classpath://notExistingScript");
            Assert.fail("ScriptNotFoundException was expected but not thrown");
        }
        catch(Exception e)
        {
            Assertions.assertThat(e).isExactlyInstanceOf(ScriptNotFoundException.class);
        }
    }


    @Test
    public void shouldReloadScriptingEventListenerAfterModification() throws Exception
    {
        TestSingleton.value = 0;
        ScriptModel scriptModel = prepareScriptModel("fooBar",
                        contentOf("test/test-script-listener-for-invalidation-v1.groovy"), ScriptType.GROOVY);
        this.scriptingEventService.registerScriptingEventListener("model://fooBar");
        Assertions.assertThat(TestSingleton.value).isEqualTo(0);
        this.eventService.publishEvent((AbstractEvent)new TestScriptingEvent("foobar"));
        Assertions.assertThat(TestSingleton.value).isEqualTo(1);
        scriptModel.setContent(contentOf("test/test-script-listener-for-invalidation-v2.groovy"));
        this.modelService.save(scriptModel);
        this.eventService.publishEvent((AbstractEvent)new TestScriptingEvent("foobar"));
        Assertions.assertThat(TestSingleton.value).isEqualTo(2);
    }


    @Test
    public void shouldUnregisterScriptingEventListenerAfterRemoval() throws Exception
    {
        TestSingleton.value = 0;
        ScriptModel scriptModel = prepareScriptModel("fooBar",
                        contentOf("test/test-script-listener-for-invalidation-v1.groovy"), ScriptType.GROOVY);
        this.scriptingEventService.registerScriptingEventListener("model://fooBar");
        Assertions.assertThat(TestSingleton.value).isEqualTo(0);
        this.eventService.publishEvent((AbstractEvent)new TestScriptingEvent("foobar"));
        Assertions.assertThat(TestSingleton.value).isEqualTo(1);
        TestSingleton.value = -1;
        this.modelService.remove(scriptModel);
        this.eventService.publishEvent((AbstractEvent)new TestScriptingEvent("foobar"));
        Assertions.assertThat(TestSingleton.value).isEqualTo(-1);
        assertListenerNotRegisteredWithUri("model://fooBar");
    }


    private void assertListenerNotRegisteredWithUri(String uri)
    {
        Collection<ScriptingListenerWrapper> registeredWrappers = this.scriptingEventService.getRegisteredWrappers();
        for(ScriptingListenerWrapper registeredWrapper : registeredWrappers)
        {
            if(registeredWrapper.getScriptURI().equals(uri))
            {
                Assert.fail("Found listener registered with uri " + uri);
            }
        }
    }


    private String contentOf(String v1) throws IOException
    {
        URL url = Resources.getResource(v1);
        return Resources.toString(url, Charsets.UTF_8);
    }


    private ScriptModel prepareScriptModel(String code, String content, ScriptType scriptType)
    {
        ScriptModel script = (ScriptModel)this.modelService.create(ScriptModel.class);
        script.setScriptType(scriptType);
        script.setCode(code);
        script.setContent(content);
        this.modelService.save(script);
        return script;
    }
}
