package de.hybris.bootstrap.typesystem.dto;

import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.xml.ModelTagListener;
import java.util.Map;

public class AttributeDTO
{
    private final String extensionName;
    private final String enclosingTypeCode;
    private final String qualifier;
    private final String type;
    private final AttributeModifierDTO modifiers;
    private final boolean redeclare;
    private final String selectionOfQualifier;
    private final YAttributeDescriptor.PersistenceType persistenceType;
    private final String persistenceQualifier;
    private final Map<String, String> persistenceMapping;
    private final String defaultValueDef;
    private final String description;
    private final Map<String, String> props;
    private final String metaType;
    private final boolean autocreate;
    private final boolean generate;
    private final ModelTagListener.ModelData modelData;
    private final boolean unique;
    private final String attributeHandler;


    public AttributeDTO(String extensionName, String enclosingTypeCode, String qualifier, String type, AttributeModifierDTO modifiers, boolean redeclare, String selectionOfQualifier, YAttributeDescriptor.PersistenceType persistenceType, String persistenceQualifier,
                    Map<String, String> persistenceMapping, String defaultValueDef, String description, Map<String, String> props, String metaType, boolean autocreate, boolean generate, ModelTagListener.ModelData modelData, boolean unique, String attributeHandler)
    {
        this.extensionName = extensionName;
        this.enclosingTypeCode = enclosingTypeCode;
        this.qualifier = qualifier;
        this.type = type;
        this.modifiers = modifiers;
        this.redeclare = redeclare;
        this.selectionOfQualifier = selectionOfQualifier;
        this.persistenceType = persistenceType;
        this.persistenceQualifier = persistenceQualifier;
        this.persistenceMapping = persistenceMapping;
        this.defaultValueDef = defaultValueDef;
        this.description = description;
        this.props = props;
        this.metaType = metaType;
        this.autocreate = autocreate;
        this.generate = generate;
        this.modelData = modelData;
        this.unique = unique;
        this.attributeHandler = attributeHandler;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public String getEnclosingTypeCode()
    {
        return this.enclosingTypeCode;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public String getType()
    {
        return this.type;
    }


    public AttributeModifierDTO getModifiers()
    {
        return this.modifiers;
    }


    public boolean isRedeclare()
    {
        return this.redeclare;
    }


    public String getSelectionOfQualifier()
    {
        return this.selectionOfQualifier;
    }


    public YAttributeDescriptor.PersistenceType getPersistenceType()
    {
        return this.persistenceType;
    }


    public String getPersistenceQualifier()
    {
        return this.persistenceQualifier;
    }


    public Map<String, String> getPersistenceMapping()
    {
        return this.persistenceMapping;
    }


    public String getDefaultValueDef()
    {
        return this.defaultValueDef;
    }


    public String getDescription()
    {
        return this.description;
    }


    public Map<String, String> getProps()
    {
        return this.props;
    }


    public String getMetaType()
    {
        return this.metaType;
    }


    public boolean isAutocreate()
    {
        return this.autocreate;
    }


    public boolean isGenerate()
    {
        return this.generate;
    }


    public ModelTagListener.ModelData getModelData()
    {
        return this.modelData;
    }


    public boolean isUnique()
    {
        return this.unique;
    }


    public String getAttributeHandler()
    {
        return this.attributeHandler;
    }


    public AttributeDTO withPersistenceType(YAttributeDescriptor.PersistenceType givenPersistenceType)
    {
        return new AttributeDTO(this.extensionName, this.enclosingTypeCode, this.qualifier, this.type, this.modifiers, this.redeclare, this.selectionOfQualifier, givenPersistenceType, this.persistenceQualifier, this.persistenceMapping, this.defaultValueDef, this.description, this.props,
                        this.metaType, this.autocreate, this.generate, this.modelData, this.unique, this.attributeHandler);
    }


    public AttributeDTO withPersistenceQualifier(String givenPersistenceQualifier)
    {
        return new AttributeDTO(this.extensionName, this.enclosingTypeCode, this.qualifier, this.type, this.modifiers, this.redeclare, this.selectionOfQualifier, this.persistenceType, givenPersistenceQualifier, this.persistenceMapping, this.defaultValueDef, this.description, this.props,
                        this.metaType, this.autocreate, this.generate, this.modelData, this.unique, this.attributeHandler);
    }


    public AttributeDTO withUniqueAttribute(boolean givenUniqueAttribute)
    {
        return new AttributeDTO(this.extensionName, this.enclosingTypeCode, this.qualifier, this.type, this.modifiers, this.redeclare, this.selectionOfQualifier, this.persistenceType, this.persistenceQualifier, this.persistenceMapping, this.defaultValueDef, this.description, this.props,
                        this.metaType, this.autocreate, this.generate, this.modelData, givenUniqueAttribute, this.attributeHandler);
    }


    public AttributeDTO withModifiers(AttributeModifierDTO givenModifiers)
    {
        return new AttributeDTO(this.extensionName, this.enclosingTypeCode, this.qualifier, this.type, givenModifiers, this.redeclare, this.selectionOfQualifier, this.persistenceType, this.persistenceQualifier, this.persistenceMapping, this.defaultValueDef, this.description, this.props,
                        this.metaType, this.autocreate, this.generate, this.modelData, this.unique, this.attributeHandler);
    }
}
