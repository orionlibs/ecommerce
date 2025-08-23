package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import java.util.Arrays;
import java.util.Collection;

public class BeanTagListener extends AbstractTypeSystemTagListener
{
    private final boolean plainTableMode;


    public BeanTagListener(DeploymentModelTagListener.PackageTagListener parent, boolean plainTableMode)
    {
        super((AbstractTypeSystemTagListener)parent, plainTableMode ? "object" : "bean");
        this.plainTableMode = plainTableMode;
    }


    protected String getPackageName()
    {
        return ((DeploymentModelTagListener.PackageTagListener)getParent()).getPackageName();
    }


    protected String getBeanName()
    {
        return getAttribute("name");
    }


    protected Collection createSubTagListeners()
    {
        if(this.plainTableMode)
        {
            return Arrays.asList(new AbstractTypeSystemTagListener[] {(AbstractTypeSystemTagListener)new ObjectMappingTagListener(this, this), (AbstractTypeSystemTagListener)new AttributeTagListener(this, this)});
        }
        return Arrays.asList(new AbstractTypeSystemTagListener[] {(AbstractTypeSystemTagListener)new ExtendsTagListener(this, this), (AbstractTypeSystemTagListener)new BeanMappingTagListener(this, this), (AbstractTypeSystemTagListener)new AttributeTagListener(this, this),
                        (AbstractTypeSystemTagListener)new FinderTagListener(this, this)});
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        if(!"Session".equalsIgnoreCase(getAttribute("type")))
        {
            if(this.plainTableMode)
            {
                PersistenceMapping persistenceMapping = (PersistenceMapping)getSubTagValue("object-mapping");
                if(persistenceMapping != null)
                {
                    Collection<DeploymentAttribute> attributes = getSubTagValueCollection("attribute");
                    for(String tbl : persistenceMapping.getTableNames())
                    {
                        String beanName = getBeanName() + "." + getBeanName();
                        getParser().addPlainTableDeployment(getPackageName(), beanName, tbl);
                        for(DeploymentAttribute attr : attributes)
                        {
                            getParser().addDeploymentAttribute(beanName, attr.qualifier, attr.type, attr.isPrimary, attr.mappings);
                        }
                        for(DeploymentIndex idx : persistenceMapping.getIndexes())
                        {
                            getParser().addDeploymentIndex(beanName, idx.indexName, idx.unique, idx.sqlserverclustered, idx.attributeLowerMap);
                        }
                    }
                }
            }
            else
            {
                PersistenceMapping persistenceMapping = (PersistenceMapping)getSubTagValue("bean-mapping");
                Collection<DeploymentAttribute> attributes = getSubTagValueCollection("attribute");
                getParser().addDeployment(
                                getPackageName(),
                                getBeanName(), (String)
                                                getSubTagValue("extends"),
                                getIntegerAttribute("typecode", 0),
                                getBooleanAttribute("abstract", false),
                                getBooleanAttribute("generic", false),
                                getBooleanAttribute("final", false), (
                                                persistenceMapping != null && !persistenceMapping.getTableNames().isEmpty()) ?
                                                persistenceMapping.getTableNames().iterator().next() : null,
                                (persistenceMapping != null) ? persistenceMapping.getPropsTableName() : null);
                for(DeploymentAttribute attr : attributes)
                {
                    getParser().addDeploymentAttribute(getBeanName(), attr.qualifier, attr.type, attr.isPrimary, attr.mappings);
                }
                if(persistenceMapping != null)
                {
                    for(DeploymentIndex idx : persistenceMapping.getIndexes())
                    {
                        getParser().addDeploymentIndex(getBeanName(), idx.indexName, idx.unique, idx.sqlserverclustered, idx.attributeLowerMap);
                    }
                }
            }
        }
        return null;
    }
}
