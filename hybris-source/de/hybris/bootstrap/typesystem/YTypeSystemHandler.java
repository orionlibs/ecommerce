package de.hybris.bootstrap.typesystem;

import de.hybris.bootstrap.typesystem.dto.AtomicTypeDTO;
import de.hybris.bootstrap.typesystem.dto.AttributeDTO;
import de.hybris.bootstrap.typesystem.dto.AttributeDeploymentDTO;
import de.hybris.bootstrap.typesystem.dto.CollectionTypeDTO;
import de.hybris.bootstrap.typesystem.dto.ComposedTypeDTO;
import de.hybris.bootstrap.typesystem.dto.DBTypeMappingDTO;
import de.hybris.bootstrap.typesystem.dto.DeploymentDTO;
import de.hybris.bootstrap.typesystem.dto.EnumTypeDTO;
import de.hybris.bootstrap.typesystem.dto.EnumValueDTO;
import de.hybris.bootstrap.typesystem.dto.FinderDeploymentDTO;
import de.hybris.bootstrap.typesystem.dto.IndexDTO;
import de.hybris.bootstrap.typesystem.dto.IndexDeploymentDTO;
import de.hybris.bootstrap.typesystem.dto.MapTypeDTO;
import de.hybris.bootstrap.typesystem.dto.RelationTypeDTO;
import java.util.Set;

public interface YTypeSystemHandler
{
    YExtension addExtension(String paramString, Set<String> paramSet);


    YAtomicType loadAtomicType(AtomicTypeDTO paramAtomicTypeDTO);


    YCollectionType loadCollectionType(CollectionTypeDTO paramCollectionTypeDTO);


    YMapType loadMapType(MapTypeDTO paramMapTypeDTO);


    YComposedType loadComposedType(ComposedTypeDTO paramComposedTypeDTO);


    YRelation loadRelation(RelationTypeDTO paramRelationTypeDTO);


    YEnumType loadEnumType(EnumTypeDTO paramEnumTypeDTO);


    YAttributeDescriptor loadAttribute(AttributeDTO paramAttributeDTO);


    YIndex loadIndex(IndexDTO paramIndexDTO);


    YEnumValue loadEnumValue(EnumValueDTO paramEnumValueDTO);


    YDBTypeMapping loadDBTypeMapping(DBTypeMappingDTO paramDBTypeMappingDTO);


    YDeployment loadDeployment(DeploymentDTO paramDeploymentDTO);


    YDeployment registerDeploymentForType(String paramString1, String paramString2);


    YIndexDeployment loadIndexDeployment(IndexDeploymentDTO paramIndexDeploymentDTO);


    YAttributeDeployment loadAttributeDeployment(AttributeDeploymentDTO paramAttributeDeploymentDTO);


    YFinder loadFinderDeployment(FinderDeploymentDTO paramFinderDeploymentDTO);


    void finish();


    void validate();


    YTypeSystem getSystem();
}
