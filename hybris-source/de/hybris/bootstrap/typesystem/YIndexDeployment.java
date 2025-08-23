package de.hybris.bootstrap.typesystem;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class YIndexDeployment extends YDeploymentElement
{
    private final String indexName;
    private boolean unique;
    private boolean sqlserverclustered;
    private List<String> indexed;
    private BitSet lower;


    public YIndexDeployment(YIndex index)
    {
        this(index.getNamespace(), index.getEnclosingType().getDeployment().getName(), index.getEnclosingType().getCode() + "_" + index.getEnclosingType().getCode());
        this.unique = index.isUnique();
        for(YAttributeDescriptor ad : index.getIndexedAttributes())
        {
            addIndexedAttribute(ad.getPersistenceQualifier(), index.isLower(ad.getQualifier()));
        }
        setLoaderInfo(index.getLoaderInfo());
    }


    public YIndexDeployment(YNamespace container, String deploymentName, String indexName)
    {
        super(container, deploymentName);
        this.indexName = indexName.toLowerCase(Locale.ENGLISH).intern();
    }


    public void validate()
    {
        super.validate();
        getIndexedAttributes();
    }


    public String getIndexName()
    {
        return this.indexName;
    }


    public final void addIndexedAttribute(String persistenceQualifier, boolean lower)
    {
        if(this.lower == null)
        {
            this.lower = new BitSet();
            this.indexed = new ArrayList<>();
        }
        this.indexed.add(persistenceQualifier);
        if(lower)
        {
            this.lower.set(this.indexed.size() - 1);
        }
    }


    public List<String> getIndexedAttributeQualifiers()
    {
        return (this.indexed != null) ? this.indexed : Collections.EMPTY_LIST;
    }


    public List<YAttributeDeployment> getIndexedAttributes()
    {
        if(this.indexed == null || this.indexed.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<YAttributeDeployment> ret = new ArrayList<>(this.indexed.size());
        for(String pq : this.indexed)
        {
            YAttributeDeployment attDesc = getDeployment().getAttributeDeployment(pq);
            if(attDesc == null)
            {
                throw new IllegalStateException("invalid index deployment due to missing attribute deployment " + pq + " from " + this.indexed + " in " +
                                getDeployment().getFullName() + " ( got " +
                                getDeployment().getAttributeDeployments() + ")");
            }
            ret.add(attDesc);
        }
        return ret;
    }


    public boolean isLower(String persistenceQualifier)
    {
        if(this.indexed == null)
        {
            return false;
        }
        int index = 0;
        for(String pq : this.indexed)
        {
            if(pq.equalsIgnoreCase(persistenceQualifier))
            {
                return isLower(index);
            }
            index++;
        }
        return false;
    }


    public boolean isLower(int attributeIndex)
    {
        return (this.lower != null && this.lower.get(attributeIndex));
    }


    public boolean isUnique()
    {
        return this.unique;
    }


    public void setUnique(boolean unique)
    {
        this.unique = unique;
    }


    public void setSQLServerClustered(boolean sqlserverclustered)
    {
        this.sqlserverclustered = sqlserverclustered;
    }


    public boolean isSQLServerClustered()
    {
        return this.sqlserverclustered;
    }
}
