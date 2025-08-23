package de.hybris.bootstrap.typesystem;

import de.hybris.bootstrap.typesystem.dto.AttributeDeploymentDTO;
import de.hybris.bootstrap.typesystem.dto.DeploymentDTO;
import de.hybris.bootstrap.typesystem.dto.IndexDeploymentDTO;
import de.hybris.bootstrap.util.LocaleHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.log4j.Logger;

class AdditionalPropsDeploymentCreator
{
    private static final Logger LOG = Logger.getLogger(AdditionalPropsDeploymentCreator.class);
    private static final String REFERENCE_PROPS_DEPLOYMENT = "property.props";
    private final Map<String, DeploymentDTO> neededPropsDeployments = new HashMap<>();
    private final Map<String, DeploymentDTO> loadedPropsDeployments = new HashMap<>();
    private final List<AttributeDeploymentDTO> referenceAttributes = new ArrayList<>();
    private final List<IndexDeploymentDTO> referenceIndices = new ArrayList<>();


    public void markDeploymentAsLoaded(DeploymentDTO deployment)
    {
        if(isPropsTableDeployment(deployment))
        {
            addPropsDeployment(deployment);
        }
        if(requirePropsTable(deployment))
        {
            DeploymentDTO propsDeployment = createPropsDeploymentBasedOn(deployment);
            markDeploymentAsNeeded(propsDeployment);
        }
    }


    public void markAttributeDeploymentAsLoaded(AttributeDeploymentDTO attributeDeployment)
    {
        if(isPartOfReferenceDeployment(attributeDeployment))
        {
            this.referenceAttributes.add(copyOf(attributeDeployment, null));
        }
    }


    public void markIndexDeploymentAsLoaded(IndexDeploymentDTO indexDeployment)
    {
        if(isPartOfReferenceDeployment(indexDeployment))
        {
            this.referenceIndices.add(copyOf(indexDeployment, null));
        }
    }


    private AttributeDeploymentDTO copyOf(AttributeDeploymentDTO attributeDeployment, String beanName)
    {
        return new AttributeDeploymentDTO(attributeDeployment.getExtensionName(), beanName, attributeDeployment.getQualifier(), attributeDeployment
                        .getType(), attributeDeployment.isPK(), attributeDeployment
                        .getColumnMappings());
    }


    private IndexDeploymentDTO copyOf(IndexDeploymentDTO indexDeployment, String itemDeploymentName)
    {
        return new IndexDeploymentDTO(indexDeployment.getExtensionName(), itemDeploymentName, indexDeployment.getName(), indexDeployment
                        .isUnique(), indexDeployment.isSqlserverclustered(), indexDeployment
                        .getFields());
    }


    public void loadDeployments(YTypeSystemHandler typeSystemHandler)
    {
        Objects.requireNonNull(typeSystemHandler);
        for(DeploymentDTO deployment : getPropsDeploymentsToCreate())
        {
            LOG.info("Loading additional props deployment " + deployment.getName());
            typeSystemHandler.loadDeployment(deployment);
            for(AttributeDeploymentDTO attribute : this.referenceAttributes)
            {
                LOG.debug("Loading additional attribute deployment " + attribute.getQualifier());
                typeSystemHandler.loadAttributeDeployment(copyOf(attribute, deployment.getName()));
            }
            for(IndexDeploymentDTO index : this.referenceIndices)
            {
                LOG.debug("Loading additional attribute deployment " + index.getName());
                typeSystemHandler.loadIndexDeployment(copyOf(index, deployment.getName()));
            }
        }
    }


    private boolean isPartOfReferenceDeployment(AttributeDeploymentDTO attributeDeployment)
    {
        return "property.props".equalsIgnoreCase(attributeDeployment.getBeanName());
    }


    private boolean isPartOfReferenceDeployment(IndexDeploymentDTO indexDeployment)
    {
        return "property.props".equalsIgnoreCase(indexDeployment.getItemDeploymentName());
    }


    private boolean isPropsTableDeployment(DeploymentDTO deployment)
    {
        String name = deployment.getName();
        return (name != null && name.toLowerCase(LocaleHelper.getPersistenceLocale()).startsWith("property."));
    }


    private void addPropsDeployment(DeploymentDTO deployment)
    {
        this.loadedPropsDeployments.put(deployment.getName().toLowerCase(LocaleHelper.getPersistenceLocale()), deployment);
    }


    private boolean requirePropsTable(DeploymentDTO deployment)
    {
        return (deployment.getPropsTableName() != null);
    }


    private DeploymentDTO createPropsDeploymentBasedOn(DeploymentDTO deployment)
    {
        return new DeploymentDTO(deployment.getExtensionName(), deployment.getPackageName(), "property." + deployment
                        .getPropsTableName(), null, 0, false, false, false, deployment.getPropsTableName(), null, true);
    }


    private void markDeploymentAsNeeded(DeploymentDTO deployment)
    {
        this.neededPropsDeployments.put(deployment.getName().toLowerCase(LocaleHelper.getPersistenceLocale()), deployment);
    }


    private Iterable<DeploymentDTO> getPropsDeploymentsToCreate()
    {
        List<DeploymentDTO> result = new LinkedList<>();
        for(Map.Entry<String, DeploymentDTO> deployment : this.neededPropsDeployments.entrySet())
        {
            if(!this.loadedPropsDeployments.containsKey(deployment.getKey()))
            {
                result.add(deployment.getValue());
            }
        }
        return result;
    }
}
