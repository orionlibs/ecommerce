package de.hybris.platform.solrfacetsearch.solr.impl;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.solr.client.solrj.ResponseParser;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.RequestWriter;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.params.CommonParams;
import org.apache.solr.common.params.SolrParams;
import org.apache.solr.common.util.ExecutorUtil;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.common.util.SolrNamedThreadFactory;
import org.slf4j.MDC;

public abstract class PatchedLBSolrClient extends SolrClient
{
    private static final Set<Integer> RETRY_CODES = new HashSet<>(Arrays.asList(new Integer[] {Integer.valueOf(404), Integer.valueOf(403), Integer.valueOf(503), Integer.valueOf(500)}));
    private static final int CHECK_INTERVAL = 60000;
    private static final int NONSTANDARD_PING_LIMIT = 5;
    private final Map<String, ServerWrapper> aliveServers = new LinkedHashMap<>();
    private final Map<String, ServerWrapper> zombieServers = new ConcurrentHashMap<>();
    private volatile ServerWrapper[] aliveServerList = new ServerWrapper[0];
    private volatile ScheduledExecutorService aliveCheckExecutor;
    private int interval = 60000;
    private final AtomicInteger counter = new AtomicInteger(-1);
    private static final SolrQuery solrQuery = new SolrQuery("*:*");
    protected volatile ResponseParser parser;
    protected volatile RequestWriter requestWriter;
    protected Set<String> queryParams = new HashSet<>();

    static
    {
        solrQuery.setRows(Integer.valueOf(0));
        solrQuery.setSort("_docid_", SolrQuery.ORDER.asc);
        solrQuery.setDistrib(false);
    }

    public PatchedLBSolrClient(List<String> baseSolrUrls)
    {
        if(!baseSolrUrls.isEmpty())
        {
            for(String s : baseSolrUrls)
            {
                ServerWrapper wrapper = createServerWrapper(s);
                this.aliveServers.put(wrapper.getBaseUrl(), wrapper);
            }
            updateAliveList();
        }
    }


    protected void updateAliveList()
    {
        synchronized(this.aliveServers)
        {
            this.aliveServerList = (ServerWrapper[])this.aliveServers.values().toArray((Object[])new ServerWrapper[0]);
        }
    }


    protected ServerWrapper createServerWrapper(String baseUrl)
    {
        return new ServerWrapper(baseUrl);
    }


    public Set<String> getQueryParams()
    {
        return this.queryParams;
    }


    public void setQueryParams(Set<String> queryParams)
    {
        this.queryParams = queryParams;
    }


    public void addQueryParams(String queryOnlyParam)
    {
        this.queryParams.add(queryOnlyParam);
    }


    public static String normalize(String server)
    {
        if(server.endsWith("/"))
        {
            server = server.substring(0, server.length() - 1);
        }
        return server;
    }


    public Rsp request(Req req) throws SolrServerException, IOException
    {
        String solrServerExceptionMessage;
        Rsp rsp = new Rsp();
        Exception ex = null;
        boolean isNonRetryable = (req.request instanceof org.apache.solr.client.solrj.request.IsUpdateRequest || CommonParams.ADMIN_PATHS.contains(req.request.getPath()));
        List<ServerWrapper> skipped = null;
        Integer numServersToTry = req.getNumServersToTry();
        int numServersTried = 0;
        boolean timeAllowedExceeded = false;
        long timeAllowedNano = getTimeAllowedInNanos(req.getRequest());
        long timeOutTime = System.nanoTime() + timeAllowedNano;
        Iterator<String> iterator;
        String serverStr;
        for(iterator = req.getServers().iterator(), serverStr = iterator.next(); iterator.hasNext() &&
                        !(timeAllowedExceeded = isTimeExceeded(timeAllowedNano, timeOutTime)); )
        {
            serverStr = normalize(serverStr);
            ServerWrapper wrapper = this.zombieServers.get(serverStr);
            if(wrapper != null)
            {
                int numDeadServersToTry = req.getNumDeadServersToTry();
                if(numDeadServersToTry > 0)
                {
                    if(skipped == null)
                    {
                        skipped = new ArrayList<>(numDeadServersToTry);
                        skipped.add(wrapper);
                        continue;
                    }
                    if(skipped.size() < numDeadServersToTry)
                    {
                        skipped.add(wrapper);
                    }
                }
                continue;
            }
            try
            {
                MDC.put("LBSolrClient.url", serverStr);
                if(numServersToTry != null && numServersTried > numServersToTry.intValue())
                {
                    MDC.remove("LBSolrClient.url");
                    break;
                }
                numServersTried++;
                ex = doRequest(serverStr, req, rsp, isNonRetryable, false);
                if(ex == null)
                {
                    return rsp;
                }
            }
            finally
            {
                MDC.remove("LBSolrClient.url");
            }
        }
        if(skipped != null)
        {
            ServerWrapper wrapper;
            for(iterator = (Iterator)skipped.iterator(), wrapper = (ServerWrapper)iterator.next(); iterator.hasNext() &&
                            !(timeAllowedExceeded = isTimeExceeded(timeAllowedNano, timeOutTime)); )
            {
                if(numServersToTry != null && numServersTried > numServersToTry.intValue())
                {
                    break;
                }
                try
                {
                    MDC.put("LBSolrClient.url", wrapper.getBaseUrl());
                    numServersTried++;
                    ex = doRequest(wrapper.baseUrl, req, rsp, isNonRetryable, true);
                    if(ex == null)
                    {
                        return rsp;
                    }
                }
                finally
                {
                    MDC.remove("LBSolrClient.url");
                }
            }
        }
        if(timeAllowedExceeded)
        {
            solrServerExceptionMessage = "Time allowed to handle this request exceeded";
        }
        else if(numServersToTry != null && numServersTried > numServersToTry.intValue())
        {
            solrServerExceptionMessage = "No live SolrServers available to handle this request: numServersTried=" + numServersTried + " numServersToTry=" + numServersToTry.intValue();
        }
        else
        {
            solrServerExceptionMessage = "No live SolrServers available to handle this request";
        }
        if(ex == null)
        {
            throw new SolrServerException(solrServerExceptionMessage);
        }
        throw new SolrServerException(solrServerExceptionMessage + ":" + solrServerExceptionMessage, ex);
    }


    private long getTimeAllowedInNanos(SolrRequest req)
    {
        SolrParams reqParams = req.getParams();
        return (reqParams == null) ? -1L :
                        TimeUnit.NANOSECONDS.convert(reqParams.getInt("timeAllowed", -1), TimeUnit.MILLISECONDS);
    }


    private boolean isTimeExceeded(long timeAllowedNano, long timeOutTime)
    {
        return (timeAllowedNano > 0L && System.nanoTime() > timeOutTime);
    }


    protected Exception doRequest(String baseUrl, Req req, Rsp rsp, boolean isNonRetryable, boolean isZombie) throws SolrServerException, IOException
    {
        Exception ex = null;
        try
        {
            rsp.server = baseUrl;
            req.getRequest().setBasePath(baseUrl);
            rsp.rsp = getClient(baseUrl).request(req.getRequest(), (String)null);
            if(isZombie)
            {
                this.zombieServers.remove(baseUrl);
            }
        }
        catch(org.apache.solr.client.solrj.impl.HttpSolrClient.RemoteExecutionException e)
        {
            throw e;
        }
        catch(SolrException e)
        {
            if(!isNonRetryable && RETRY_CODES.contains(Integer.valueOf(e.code())))
            {
                ex = !isZombie ? addZombie(baseUrl, (Exception)e) : (Exception)e;
            }
            else
            {
                if(isZombie)
                {
                    this.zombieServers.remove(baseUrl);
                }
                throw e;
            }
        }
        catch(SocketException e)
        {
            if(!isNonRetryable || e instanceof java.net.ConnectException)
            {
                ex = !isZombie ? addZombie(baseUrl, e) : e;
            }
            else
            {
                throw e;
            }
        }
        catch(SocketTimeoutException e)
        {
            if(!isNonRetryable)
            {
                ex = !isZombie ? addZombie(baseUrl, e) : e;
            }
            else
            {
                throw e;
            }
        }
        catch(SolrServerException e)
        {
            Throwable rootCause = e.getRootCause();
            if(!isNonRetryable && rootCause instanceof IOException)
            {
                ex = !isZombie ? addZombie(baseUrl, (Exception)e) : (Exception)e;
            }
            else if(isNonRetryable && rootCause instanceof java.net.ConnectException)
            {
                ex = !isZombie ? addZombie(baseUrl, (Exception)e) : (Exception)e;
            }
            else
            {
                throw e;
            }
        }
        catch(Exception e)
        {
            throw new SolrServerException(e);
        }
        return ex;
    }


    private Exception addZombie(String serverStr, Exception e)
    {
        ServerWrapper wrapper = createServerWrapper(serverStr);
        wrapper.standard = false;
        this.zombieServers.put(serverStr, wrapper);
        startAliveCheckExecutor();
        return e;
    }


    public void setAliveCheckInterval(int interval)
    {
        if(interval <= 0)
        {
            throw new IllegalArgumentException("Alive check interval must be positive, specified value = " + interval);
        }
        this.interval = interval;
    }


    private void startAliveCheckExecutor()
    {
        if(this.aliveCheckExecutor == null)
        {
            synchronized(this)
            {
                if(this.aliveCheckExecutor == null)
                {
                    this.aliveCheckExecutor = Executors.newSingleThreadScheduledExecutor((ThreadFactory)new SolrNamedThreadFactory("aliveCheckExecutor"));
                    this.aliveCheckExecutor.scheduleAtFixedRate(
                                    getAliveCheckRunner(new WeakReference<>(this)), this.interval, this.interval, TimeUnit.MILLISECONDS);
                }
            }
        }
    }


    private static Runnable getAliveCheckRunner(WeakReference<PatchedLBSolrClient> lbRef)
    {
        return () -> {
            PatchedLBSolrClient lb = lbRef.get();
            if(lb != null && lb.zombieServers != null)
            {
                for(ServerWrapper zombieServer : lb.zombieServers.values())
                {
                    lb.checkAZombieServer(zombieServer);
                }
            }
        };
    }


    public ResponseParser getParser()
    {
        return this.parser;
    }


    public void setParser(ResponseParser parser)
    {
        this.parser = parser;
    }


    public void setRequestWriter(RequestWriter requestWriter)
    {
        this.requestWriter = requestWriter;
    }


    public RequestWriter getRequestWriter()
    {
        return this.requestWriter;
    }


    private void checkAZombieServer(ServerWrapper zombieServer)
    {
        try
        {
            QueryRequest queryRequest = new QueryRequest((SolrParams)solrQuery);
            queryRequest.setBasePath(zombieServer.baseUrl);
            QueryResponse resp = (QueryResponse)queryRequest.process(getClient(zombieServer.getBaseUrl()));
            if(resp.getStatus() == 0)
            {
                addDeadServerToAlive(zombieServer);
            }
        }
        catch(SolrException e)
        {
            addDeadServerToAlive(zombieServer);
        }
        catch(Exception e)
        {
            zombieServer.failedPings++;
            if(!zombieServer.standard && zombieServer.failedPings >= 5)
            {
                this.zombieServers.remove(zombieServer.getBaseUrl());
            }
        }
    }


    private void addDeadServerToAlive(ServerWrapper zombieServer)
    {
        ServerWrapper wrapper = this.zombieServers.remove(zombieServer.getBaseUrl());
        if(wrapper != null)
        {
            wrapper.failedPings = 0;
            if(wrapper.standard)
            {
                addToAlive(wrapper);
            }
        }
    }


    private ServerWrapper removeFromAlive(String key)
    {
        synchronized(this.aliveServers)
        {
            ServerWrapper wrapper = this.aliveServers.remove(key);
            if(wrapper != null)
            {
                updateAliveList();
            }
            return wrapper;
        }
    }


    private void addToAlive(ServerWrapper wrapper)
    {
        synchronized(this.aliveServers)
        {
            ServerWrapper prev = this.aliveServers.put(wrapper.getBaseUrl(), wrapper);
            updateAliveList();
        }
    }


    public void addSolrServer(String server) throws MalformedURLException
    {
        addToAlive(createServerWrapper(server));
    }


    public String removeSolrServer(String server)
    {
        try
        {
            server = (new URL(server)).toExternalForm();
        }
        catch(MalformedURLException e)
        {
            throw new RuntimeException(e);
        }
        if(server.endsWith("/"))
        {
            server = server.substring(0, server.length() - 1);
        }
        removeFromAlive(server);
        this.zombieServers.remove(server);
        return null;
    }


    public NamedList<Object> request(SolrRequest request, String collection) throws SolrServerException, IOException
    {
        return request(request, collection, null);
    }


    public NamedList<Object> request(SolrRequest request, String collection, Integer numServersToTry) throws SolrServerException, IOException
    {
        SolrServerException solrServerException;
        String solrServerExceptionMessage;
        Exception ex = null;
        ServerWrapper[] serverList = this.aliveServerList;
        int maxTries = (numServersToTry == null) ? serverList.length : numServersToTry.intValue();
        int numServersTried = 0;
        Map<String, ServerWrapper> justFailed = null;
        boolean timeAllowedExceeded = false;
        long timeAllowedNano = getTimeAllowedInNanos(request);
        long timeOutTime = System.nanoTime() + timeAllowedNano;
        for(int attempts = 0; attempts < maxTries &&
                        !(timeAllowedExceeded = isTimeExceeded(timeAllowedNano, timeOutTime)); attempts++)
        {
            ServerWrapper serverWrapper = pickServer(serverList, request);
            try
            {
                numServersTried++;
                request.setBasePath(serverWrapper.baseUrl);
                return getClient(serverWrapper.getBaseUrl()).request(request, collection);
            }
            catch(SolrException e)
            {
                throw e;
            }
            catch(SolrServerException e)
            {
                if(e.getRootCause() instanceof IOException)
                {
                    solrServerException = e;
                    moveAliveToDead(serverWrapper);
                    if(justFailed == null)
                    {
                        justFailed = new HashMap<>();
                    }
                    justFailed.put(serverWrapper.getBaseUrl(), serverWrapper);
                }
                else
                {
                    throw e;
                }
            }
            catch(Exception e)
            {
                throw new SolrServerException(e);
            }
        }
        Iterator<ServerWrapper> iterator;
        ServerWrapper wrapper;
        for(iterator = this.zombieServers.values().iterator(), wrapper = iterator.next(); iterator.hasNext() &&
                        !(timeAllowedExceeded = isTimeExceeded(timeAllowedNano, timeOutTime)); )
        {
            if(!wrapper.standard || (justFailed != null && justFailed.containsKey(wrapper.getBaseUrl())))
            {
                continue;
            }
            try
            {
                numServersTried++;
                request.setBasePath(wrapper.baseUrl);
                NamedList<Object> rsp = getClient(wrapper.baseUrl).request(request, collection);
                this.zombieServers.remove(wrapper.getBaseUrl());
                addToAlive(wrapper);
                return rsp;
            }
            catch(SolrException e)
            {
                throw e;
            }
            catch(SolrServerException e)
            {
                if(e.getRootCause() instanceof IOException)
                {
                    solrServerException = e;
                    continue;
                }
                throw e;
            }
            catch(Exception e)
            {
                throw new SolrServerException(e);
            }
        }
        if(timeAllowedExceeded)
        {
            solrServerExceptionMessage = "Time allowed to handle this request exceeded";
        }
        else if(numServersToTry != null && numServersTried > numServersToTry.intValue())
        {
            solrServerExceptionMessage = "No live SolrServers available to handle this request: numServersTried=" + numServersTried + " numServersToTry=" + numServersToTry.intValue();
        }
        else
        {
            solrServerExceptionMessage = "No live SolrServers available to handle this request";
        }
        if(solrServerException == null)
        {
            throw new SolrServerException(solrServerExceptionMessage);
        }
        throw new SolrServerException(solrServerExceptionMessage, solrServerException);
    }


    protected ServerWrapper pickServer(ServerWrapper[] aliveServerList, SolrRequest request)
    {
        int count = this.counter.incrementAndGet() & Integer.MAX_VALUE;
        return aliveServerList[count % aliveServerList.length];
    }


    private void moveAliveToDead(ServerWrapper wrapper)
    {
        wrapper = removeFromAlive(wrapper.getBaseUrl());
        if(wrapper == null)
        {
            return;
        }
        this.zombieServers.put(wrapper.getBaseUrl(), wrapper);
        startAliveCheckExecutor();
    }


    public void close()
    {
        synchronized(this)
        {
            if(this.aliveCheckExecutor != null)
            {
                this.aliveCheckExecutor.shutdownNow();
                ExecutorUtil.shutdownAndAwaitTermination(this.aliveCheckExecutor);
            }
        }
    }


    protected abstract SolrClient getClient(String paramString);
}
