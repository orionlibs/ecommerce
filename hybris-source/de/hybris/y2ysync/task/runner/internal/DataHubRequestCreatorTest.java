package de.hybris.y2ysync.task.runner.internal;

import com.google.common.collect.Lists;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.y2ysync.model.media.SyncImpExMediaModel;
import de.hybris.y2ysync.task.dao.Y2YSyncDAO;
import de.hybris.y2ysync.task.runner.Y2YSyncContext;
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.ResponseCreator;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

@RunWith(MockitoJUnitRunner.class)
@UnitTest
public class DataHubRequestCreatorTest
{
    private static final String SYNC_EXECUTION_ID = "testExecutionId";
    private static final String HOME_URL = "http://localhost:9001";
    private static final String CONSUME_CHANGES_WEBROOT = "/y2ysync";
    private static final String DATAHUB_URI = "/datahub-webapp/v1/data-feeds/y2ysync";
    private static final String FEED_NAME = "Y2YSYNC_FEED";
    private static final String POOL_NAME = "Y2YSYNC_POOL";
    private static final String TARGET_SYSTEMS = "";
    private DataHubRequestCreator requestCreator;
    private final RestTemplate restTemplate = getRestTemplate();
    @Mock
    private Y2YSyncDAO dao;
    @Mock
    private SyncImpExMediaModel m1;
    @Mock
    private SyncImpExMediaModel m2;
    @Mock
    private SyncImpExMediaModel m3;
    @Mock
    private SyncImpExMediaModel m4;
    @Mock
    private SyncImpExMediaModel m5;
    @Mock
    private ComposedTypeModel productType;
    @Mock
    private ComposedTypeModel titleType;
    private Y2YSyncContext ctx;


    @Before
    public void setUp() throws Exception
    {
        this.requestCreator = (DataHubRequestCreator)new Object(this);
        this.requestCreator.setY2YSyncDAO(this.dao);
        this.requestCreator.setRestTemplate(this.restTemplate);
        BDDMockito.given(this.productType.getCode()).willReturn("Product");
        BDDMockito.given(this.titleType.getCode()).willReturn("Title");
        BDDMockito.given(this.m1.getImpexHeader()).willReturn("INSERT_UPDATE Product;code[unique=true];description");
        BDDMockito.given(this.m1.getDataHubColumns()).willReturn("code;description");
        BDDMockito.given(this.m1.getSyncType()).willReturn(this.productType);
        BDDMockito.given(this.m1.getURL()).willReturn("http://localhost:9001/y2ysync/medias/m1");
        BDDMockito.given(this.m2.getImpexHeader()).willReturn("INSERT_UPDATE Product;code[unique=true];description");
        BDDMockito.given(this.m2.getDataHubColumns()).willReturn("code;description");
        BDDMockito.given(this.m2.getSyncType()).willReturn(this.productType);
        BDDMockito.given(this.m2.getURL()).willReturn("http://localhost:9001/y2ysync/medias/m2");
        BDDMockito.given(this.m3.getImpexHeader()).willReturn("DELETE Product;code[unique=true]");
        BDDMockito.given(this.m3.getDataHubColumns()).willReturn("code");
        BDDMockito.given(this.m3.getSyncType()).willReturn(this.productType);
        BDDMockito.given(this.m3.getURL()).willReturn("http://localhost:9001/y2ysync/medias/m3");
        BDDMockito.given(this.m4.getImpexHeader()).willReturn("INSERT_UPDATE Title;code[unique=true];");
        BDDMockito.given(this.m4.getDataHubColumns()).willReturn("code");
        BDDMockito.given(this.m4.getSyncType()).willReturn(this.titleType);
        BDDMockito.given(this.m4.getURL()).willReturn("http://localhost:9001/y2ysync/medias/m4");
        BDDMockito.given(this.m5.getImpexHeader()).willReturn("INSERT_UPDATE Title;code[unique=true];");
        BDDMockito.given(this.m5.getDataHubColumns()).willReturn("code");
        BDDMockito.given(this.m5.getSyncType()).willReturn(this.titleType);
        BDDMockito.given(this.m5.getURL()).willReturn("http://localhost:9001/y2ysync/medias/m5");
        BDDMockito.given(this.dao.findSyncMediasBySyncCronJob("testExecutionId")).willReturn(Lists.newArrayList((Object[])new SyncImpExMediaModel[] {this.m1, this.m2, this.m3, this.m4, this.m5}));
        this
                        .ctx = Y2YSyncContext.builder().withSyncExecutionId("testExecutionId").withUri("/datahub-webapp/v1/data-feeds/y2ysync").withFeed("Y2YSYNC_FEED").withPool("Y2YSYNC_POOL").withAutoPublishTargetSystems("").build();
    }


    @Test
    public void shouldSuccessfullySendSyncMediaUrlsGrouppedByImpExHeaderAsJSON() throws Exception
    {
        String expectedJson = getExpectedJson();
        MockRestServiceServer server = MockRestServiceServer.createServer(this.restTemplate);
        server.expect(MockRestRequestMatchers.requestTo("/datahub-webapp/v1/data-feeds/y2ysync"))
                        .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
                        .andExpect(MockRestRequestMatchers.content().contentType("application/json"))
                        .andExpect(MockRestRequestMatchers.content().string((Matcher)new JSONMatcher(this, expectedJson)))
                        .andRespond((ResponseCreator)MockRestResponseCreators.withSuccess());
        this.requestCreator.sendRequest(this.ctx);
        server.verify();
    }


    @Test
    public void shouldThrowAnExceptionWhenRemoteDataHubControllerRespondsWithNot200OK() throws Exception
    {
        try
        {
            MockRestServiceServer server = MockRestServiceServer.createServer(this.restTemplate);
            server.expect(MockRestRequestMatchers.requestTo("/datahub-webapp/v1/data-feeds/y2ysync"))
                            .andRespond((ResponseCreator)MockRestResponseCreators.withBadRequest());
            this.requestCreator.sendRequest(this.ctx);
            Assert.fail("Should throw IllegalStateException");
        }
        catch(HttpStatusCodeException httpStatusCodeException)
        {
        }
    }


    private RestTemplate getRestTemplate()
    {
        RestTemplate template = new RestTemplate();
        template.setMessageConverters(Lists.newArrayList((Object[])new HttpMessageConverter[] {(HttpMessageConverter)new MappingJackson2HttpMessageConverter()}));
        return template;
    }


    private String getExpectedJson()
    {
        return "{\"syncExecutionId\":\"testExecutionId\",\"sourcePlatformUrl\":\"http://localhost:9001/y2ysync\",\"dataStreams\":[{\"itemType\":\"Product\",\"dataHubType\":\"\",\"columns\":\"code;description\",\"delete\":false,\"urls\":[\"http://localhost:9001/y2ysync/medias/m1\",\"http://localhost:9001/y2ysync/medias/m2\"]},{\"itemType\":\"Product\",\"dataHubType\":\"\",\"columns\":\"code\",\"delete\":false,\"urls\":[\"http://localhost:9001/y2ysync/medias/m3\"]},{\"itemType\":\"Title\",\"dataHubType\":\"\",\"columns\":\"code\",\"delete\":false,\"urls\":[\"http://localhost:9001/y2ysync/medias/m4\",\"http://localhost:9001/y2ysync/medias/m5\"]}],\"pool\":\"Y2YSYNC_POOL\",\"feed\":\"Y2YSYNC_FEED\",\"autoPublishTargetSystems\":\"\"}";
    }
}
