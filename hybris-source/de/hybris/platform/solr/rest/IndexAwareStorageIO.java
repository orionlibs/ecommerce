package de.hybris.platform.solr.rest;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.atomic.AtomicReference;
import org.apache.solr.cloud.ZkSolrResourceLoader;
import org.apache.solr.common.SolrException;
import org.apache.solr.common.cloud.SolrZkClient;
import org.apache.solr.common.util.NamedList;
import org.apache.solr.core.SolrResourceLoader;
import org.apache.solr.rest.ManagedResourceStorage;

public class IndexAwareStorageIO implements ManagedResourceStorage.StorageIO
{
    public static final String MANAGED_RESOURCES_DIR = "managed";
    public static final String COLLECTION_PROPERTY = "collectionName";
    public static final String INSTANCE_DIR_PROPERTY = "instanceDir";
    private final AtomicReference<ManagedResourceStorage.StorageIO> delegateReference = new AtomicReference<>();


    protected ManagedResourceStorage.StorageIO getDelegate()
    {
        return this.delegateReference.get();
    }


    public void configure(SolrResourceLoader resourceLoader, NamedList<String> initArgs) throws SolrException
    {
        if(resourceLoader instanceof ZkSolrResourceLoader)
        {
            String collection = (String)initArgs.get("collectionName");
            if(collection == null || collection.isEmpty())
            {
                throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Failed to detect collection");
            }
            SolrZkClient zkClient = ((ZkSolrResourceLoader)resourceLoader).getZkController().getZkClient();
            String znodeBase = "/collections/" + collection + "/" + "managed";
            this.delegateReference.updateAndGet(d -> new ManagedResourceStorage.ZooKeeperStorageIO(zkClient, znodeBase));
        }
        else
        {
            initArgs.remove("storageDir");
            String instanceDir = (String)initArgs.get("instanceDir");
            if(instanceDir == null || instanceDir.isEmpty())
            {
                throw new SolrException(SolrException.ErrorCode.SERVER_ERROR, "Failed to detect instance dir");
            }
            File storageDir = new File(instanceDir, "managed");
            initArgs.add("storageDir", storageDir.getAbsolutePath());
            this.delegateReference.updateAndGet(d -> new ManagedResourceStorage.FileStorageIO());
        }
        getDelegate().configure(resourceLoader, initArgs);
    }


    public boolean delete(String storedResourceId) throws IOException
    {
        return getDelegate().delete(storedResourceId);
    }


    public boolean exists(String storedResourceId) throws IOException
    {
        return getDelegate().exists(storedResourceId);
    }


    public String getInfo()
    {
        return getDelegate().getInfo();
    }


    public InputStream openInputStream(String storedResourceId) throws IOException
    {
        return getDelegate().openInputStream(storedResourceId);
    }


    public OutputStream openOutputStream(String storedResourceId) throws IOException
    {
        return getDelegate().openOutputStream(storedResourceId);
    }
}
