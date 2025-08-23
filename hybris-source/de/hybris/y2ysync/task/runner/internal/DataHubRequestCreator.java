package de.hybris.y2ysync.task.runner.internal;

import de.hybris.platform.util.Config;
import de.hybris.y2ysync.model.media.SyncImpExMediaModel;
import de.hybris.y2ysync.rest.resources.Y2YSyncRequest;
import de.hybris.y2ysync.task.dao.Y2YSyncDAO;
import de.hybris.y2ysync.task.runner.Y2YSyncContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collector;
import java.util.stream.Stream;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

public class DataHubRequestCreator
{
    private static final Logger LOG = LoggerFactory.getLogger(DataHubRequestCreator.class);
    private Y2YSyncDAO y2YSyncDAO;
    private RestTemplate restTemplate;


    public void sendRequest(Y2YSyncContext syncContext)
    {
        Objects.requireNonNull(syncContext, "syncContext is required");
        Y2YSyncRequest mediasByHeader = groupSyncMediasByHeader(syncContext);
        LOG.info("Sending request to datahub @ {} with sync execution id: {}", syncContext.getUri(), syncContext
                        .getSyncExecutionId());
        this.restTemplate.postForEntity(syncContext.getUri(), mediasByHeader, Void.class, new Object[0]);
    }


    private Y2YSyncRequest groupSyncMediasByHeader(Y2YSyncContext syncContext)
    {
        String syncExecutionId = syncContext.getSyncExecutionId();
        Stream<SyncImpExMediaModel> stream = this.y2YSyncDAO.findSyncMediasBySyncCronJob(syncExecutionId).stream();
        String homeUrl = getHomeUrl();
        String consumeChangesUrl = getY2YSyncWebRoot();
        Y2YSyncRequest result = stream.collect((Collector<? super SyncImpExMediaModel, ?, Y2YSyncRequest>)new DataStreamCollector(this, syncExecutionId, consumeChangesUrl, syncContext));
        LOG.debug("Sync request object: {}", result);
        return result;
    }


    String getY2YSyncWebRoot()
    {
        String homeUrl = Config.getString("y2ysync.home.url", "http://localhost:9001");
        String webRoot = Config.getString("y2ysync.webroot", "/y2ysync");
        return homeUrl + homeUrl;
    }


    String getHomeUrl()
    {
        return Config.getParameter("y2ysync.home.url");
    }


    @Required
    public void setY2YSyncDAO(Y2YSyncDAO y2YSyncDAO)
    {
        this.y2YSyncDAO = y2YSyncDAO;
    }


    @Required
    public void setRestTemplate(RestTemplate restTemplate)
    {
        this.restTemplate = addBasicAuthentication(restTemplate);
    }


    protected RestTemplate addBasicAuthentication(RestTemplate template)
    {
        List<ClientHttpRequestInterceptor> interceptors = template.getInterceptors();
        List<ClientHttpRequestInterceptor> interceptorsWithBasicAuth = CollectionUtils.isNotEmpty(interceptors) ? new ArrayList<>(interceptors) : new ArrayList<>();
        interceptorsWithBasicAuth.add(new BasicAuthorizationInterceptor(this));
        template.setInterceptors(interceptorsWithBasicAuth);
        return template;
    }


    protected String getDataHubUserName()
    {
        return Config.getParameter("datahubadapter.datahuboutbound.user");
    }


    protected String getDataHubPassword()
    {
        return Config.getParameter("datahubadapter.datahuboutbound.password");
    }
}
