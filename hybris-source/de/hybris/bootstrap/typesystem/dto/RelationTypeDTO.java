package de.hybris.bootstrap.typesystem.dto;

import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YRelationEnd;
import de.hybris.bootstrap.typesystem.xml.ModelTagListener;
import java.util.Map;

public class RelationTypeDTO
{
    private final String extensionName;
    private final String code;
    private final String metaType;
    private final String jaloClassName;
    private final String srcRole;
    private final String srcType;
    private final boolean srcNavigable;
    private final int srcModifiers;
    private final boolean srcUniqueModifier;
    private final YRelationEnd.Cardinality srcCard;
    private final boolean srcOrdered;
    private final YCollectionType.TypeOfCollection srcCollType;
    private final Map<String, String> srcProps;
    private final String srcMetaType;
    private final String srcDescription;
    private final ModelTagListener.ModelData srcModelData;
    private final String tgtRole;
    private final String tgtType;
    private final boolean tgtNavigable;
    private final int tgtModifiers;
    private final boolean tgtUniqueModifier;
    private final YRelationEnd.Cardinality tgtCard;
    private final boolean tgtOrdered;
    private final YCollectionType.TypeOfCollection tgtCollType;
    private final Map<String, String> tgtProps;
    private final String tgtMetaType;
    private final String tgtDescription;
    private final ModelTagListener.ModelData tgtModelData;
    private final String deployment;
    private final boolean localized;
    private final boolean autocreate;
    private final boolean generate;


    public RelationTypeDTO(String extensionName, String code, String metaType, String jaloClassName, String srcRole, String srcType, boolean srcNavigable, int srcModifiers, boolean srcUniqueModifier, YRelationEnd.Cardinality srcCard, boolean srcOrdered, YCollectionType.TypeOfCollection srcCollType,
                    Map<String, String> srcProps, String srcMetaType, String srcDescription, ModelTagListener.ModelData srcModelData, String tgtRole, String tgtType, boolean tgtNavigable, int tgtModifiers, boolean tgtUniqueModifier, YRelationEnd.Cardinality tgtCard, boolean tgtOrdered,
                    YCollectionType.TypeOfCollection tgtCollType, Map<String, String> tgtProps, String tgtMetaType, String tgtDescription, ModelTagListener.ModelData tgtModelData, String deployment, boolean localized, boolean autocreate, boolean generate)
    {
        this.extensionName = extensionName;
        this.code = code;
        this.metaType = metaType;
        this.jaloClassName = jaloClassName;
        this.srcRole = srcRole;
        this.srcType = srcType;
        this.srcNavigable = srcNavigable;
        this.srcModifiers = srcModifiers;
        this.srcUniqueModifier = srcUniqueModifier;
        this.srcCard = srcCard;
        this.srcOrdered = srcOrdered;
        this.srcCollType = srcCollType;
        this.srcProps = srcProps;
        this.srcMetaType = srcMetaType;
        this.srcDescription = srcDescription;
        this.srcModelData = srcModelData;
        this.tgtRole = tgtRole;
        this.tgtType = tgtType;
        this.tgtNavigable = tgtNavigable;
        this.tgtModifiers = tgtModifiers;
        this.tgtUniqueModifier = tgtUniqueModifier;
        this.tgtCard = tgtCard;
        this.tgtOrdered = tgtOrdered;
        this.tgtCollType = tgtCollType;
        this.tgtProps = tgtProps;
        this.tgtMetaType = tgtMetaType;
        this.tgtDescription = tgtDescription;
        this.tgtModelData = tgtModelData;
        this.deployment = deployment;
        this.localized = localized;
        this.autocreate = autocreate;
        this.generate = generate;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public String getCode()
    {
        return this.code;
    }


    public String getMetaType()
    {
        return this.metaType;
    }


    public String getJaloClassName()
    {
        return this.jaloClassName;
    }


    public String getSrcRole()
    {
        return this.srcRole;
    }


    public String getSrcType()
    {
        return this.srcType;
    }


    public boolean isSrcNavigable()
    {
        return this.srcNavigable;
    }


    public int getSrcModifiers()
    {
        return this.srcModifiers;
    }


    public boolean isSrcUniqueModifier()
    {
        return this.srcUniqueModifier;
    }


    public YRelationEnd.Cardinality getSrcCard()
    {
        return this.srcCard;
    }


    public boolean isSrcOrdered()
    {
        return this.srcOrdered;
    }


    public YCollectionType.TypeOfCollection getSrcCollType()
    {
        return this.srcCollType;
    }


    public Map<String, String> getSrcProps()
    {
        return this.srcProps;
    }


    public String getSrcMetaType()
    {
        return this.srcMetaType;
    }


    public String getSrcDescription()
    {
        return this.srcDescription;
    }


    public ModelTagListener.ModelData getSrcModelData()
    {
        return this.srcModelData;
    }


    public String getTgtRole()
    {
        return this.tgtRole;
    }


    public String getTgtType()
    {
        return this.tgtType;
    }


    public boolean isTgtNavigable()
    {
        return this.tgtNavigable;
    }


    public int getTgtModifiers()
    {
        return this.tgtModifiers;
    }


    public boolean isTgtUniqueModifier()
    {
        return this.tgtUniqueModifier;
    }


    public YRelationEnd.Cardinality getTgtCard()
    {
        return this.tgtCard;
    }


    public boolean isTgtOrdered()
    {
        return this.tgtOrdered;
    }


    public YCollectionType.TypeOfCollection getTgtCollType()
    {
        return this.tgtCollType;
    }


    public Map<String, String> getTgtProps()
    {
        return this.tgtProps;
    }


    public String getTgtMetaType()
    {
        return this.tgtMetaType;
    }


    public String getTgtDescription()
    {
        return this.tgtDescription;
    }


    public ModelTagListener.ModelData getTgtModelData()
    {
        return this.tgtModelData;
    }


    public String getDeployment()
    {
        return this.deployment;
    }


    public boolean isLocalized()
    {
        return this.localized;
    }


    public boolean isAutocreate()
    {
        return this.autocreate;
    }


    public boolean isGenerate()
    {
        return this.generate;
    }
}
