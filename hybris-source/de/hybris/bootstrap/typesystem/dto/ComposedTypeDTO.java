package de.hybris.bootstrap.typesystem.dto;

import de.hybris.bootstrap.typesystem.xml.ModelTagListener;
import java.util.Map;

public class ComposedTypeDTO
{
    private final String extensionName;
    private final String code;
    private final String superTypeCode;
    private final String jaloClassName;
    private final boolean isAbstract;
    private final boolean isSingleton;
    private final boolean isJaloOnly;
    private final String metaType;
    private final String deploymentName;
    private final boolean autocreate;
    private final boolean generate;
    private final Map<String, String> props;
    private final ModelTagListener.ModelData modelData;
    private final String typeDescription;
    private final boolean legacyPersistence;


    public ComposedTypeDTO(String extensionName, String code, String superTypeCode, String jaloClassName, boolean anAbstract, boolean singleton, boolean jaloOnly, String metaType, String deploymentName, boolean autocreate, boolean generate, Map<String, String> props,
                    ModelTagListener.ModelData modelData, String typeDescription, boolean legacyPersistence)
    {
        this.extensionName = extensionName;
        this.code = code;
        this.superTypeCode = superTypeCode;
        this.jaloClassName = jaloClassName;
        this.isAbstract = anAbstract;
        this.isSingleton = singleton;
        this.isJaloOnly = jaloOnly;
        this.metaType = metaType;
        this.deploymentName = deploymentName;
        this.autocreate = autocreate;
        this.generate = generate;
        this.props = props;
        this.modelData = modelData;
        this.typeDescription = typeDescription;
        this.legacyPersistence = legacyPersistence;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public String getCode()
    {
        return this.code;
    }


    public String getSuperTypeCode()
    {
        return this.superTypeCode;
    }


    public String getJaloClassName()
    {
        return this.jaloClassName;
    }


    public boolean isAbstract()
    {
        return this.isAbstract;
    }


    public boolean isSingleton()
    {
        return this.isSingleton;
    }


    public boolean isJaloOnly()
    {
        return this.isJaloOnly;
    }


    public boolean isLegacyPersistence()
    {
        return this.legacyPersistence;
    }


    public String getMetaType()
    {
        return this.metaType;
    }


    public String getDeploymentName()
    {
        return this.deploymentName;
    }


    public boolean isAutocreate()
    {
        return this.autocreate;
    }


    public boolean isGenerate()
    {
        return this.generate;
    }


    public Map<String, String> getProps()
    {
        return this.props;
    }


    public ModelTagListener.ModelData getModelData()
    {
        return this.modelData;
    }


    public String getTypeDescription()
    {
        return this.typeDescription;
    }


    public ComposedTypeDTO withDeploymentName(String effectiveDeploymentName)
    {
        return new ComposedTypeDTO(this.extensionName, this.code, this.superTypeCode, this.jaloClassName, this.isAbstract, this.isSingleton, this.isJaloOnly, this.metaType, effectiveDeploymentName, this.autocreate, this.generate, this.props, this.modelData, this.typeDescription,
                        this.legacyPersistence);
    }
}
