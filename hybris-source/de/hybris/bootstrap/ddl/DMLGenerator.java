package de.hybris.bootstrap.ddl;

import bsh.EvalError;
import de.hybris.bootstrap.ddl.model.YRecord;
import de.hybris.bootstrap.typesystem.YAtomicType;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YEnumType;
import de.hybris.bootstrap.typesystem.YMapType;
import de.hybris.bootstrap.typesystem.YRelation;
import de.hybris.bootstrap.typesystem.YTypeSystem;
import de.hybris.bootstrap.util.LocaleHelper;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.apache.ddlutils.DdlUtilsException;
import org.apache.ddlutils.dynabean.SqlDynaException;
import org.apache.log4j.Logger;

public class DMLGenerator
{
    private static final Logger LOG = Logger.getLogger(DMLGenerator.class);
    private final YTypeSystem typeSystem;
    private final DMLRecordFactory recordFactory;


    public DMLGenerator(YTypeSystem typeSystem, DMLRecordFactory recordFactory)
    {
        this.typeSystem = typeSystem;
        this.recordFactory = recordFactory;
    }


    public Collection<YRecord> generateRecords() throws DdlUtilsException, IOException, EvalError
    {
        YComposedType rootType = (YComposedType)this.typeSystem.getType("Item");
        createDeploymentAndComposedTypes(rootType);
        this.recordFactory.prepareDefaultLanguage();
        this.recordFactory.prepareDefaultCurrency();
        prepareAtomicTypes();
        prepareEnumerationValues();
        prepareCollectionTypes();
        prepareMapTypes();
        this.recordFactory.prepareMetaInformation("master");
        this.recordFactory.prepareMediaFolders();
        this.recordFactory.addUserRights();
        this.recordFactory.prepareNumberSeries();
        return this.recordFactory.getYrecords();
    }


    public Collection<YRecord> generateRecordsForUpdate() throws DdlUtilsException, IOException, EvalError
    {
        YComposedType rootType = (YComposedType)this.typeSystem.getType("Item");
        createDeploymentAndComposedTypes(rootType);
        prepareAtomicTypes();
        prepareEnumerationValues();
        prepareCollectionTypes();
        prepareMapTypes();
        this.recordFactory.prepareNumberSeries();
        return this.recordFactory.getYrecords();
    }


    private void createDeploymentAndComposedTypes(YComposedType type) throws EvalError
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("#####Creating deployments and descriptors for Type (%s) #####", new Object[] {type.getCode()}));
        }
        if(type instanceof YRelation)
        {
            createRelation((YRelation)type);
        }
        else if(type instanceof YEnumType)
        {
            createEnum((YEnumType)type);
        }
        else
        {
            createComposedType(type);
        }
        createAttributeDescriptors(type);
        if(LOG.isDebugEnabled())
        {
            LOG.debug(String.format("#####Collected deployments and descriptors for Tables (%s) #####", new Object[] {type.getCode()}));
        }
        for(YComposedType subType : type.getSubtypes())
        {
            createDeploymentAndComposedTypes(subType);
        }
    }


    private void createEnum(YEnumType type) throws EvalError
    {
        this.recordFactory.addEnumType(type);
    }


    private void createComposedType(YComposedType type) throws EvalError
    {
        this.recordFactory.addComposedType(type);
    }


    private void createAttributeDescriptors(YComposedType type) throws SqlDynaException
    {
        Set<String> declaredQualifiers = new HashSet<>();
        for(YComposedType t = type; t != null; t = t.getSuperType())
        {
            for(YAttributeDescriptor yAttributeDescriptor : t.getAttributes())
            {
                if(declaredQualifiers.add(yAttributeDescriptor.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale())))
                {
                    this.recordFactory.addAttributeDescriptor(type, yAttributeDescriptor);
                }
            }
        }
    }


    private void prepareAtomicTypes() throws SqlDynaException
    {
        for(YAtomicType yAtomicType : this.typeSystem.getAtomicTypes())
        {
            this.recordFactory.addAtomicType(yAtomicType);
        }
    }


    private void prepareCollectionTypes() throws SqlDynaException
    {
        for(YCollectionType yCollectionType : this.typeSystem.getCollectionTypes())
        {
            this.recordFactory.addCollectionType(yCollectionType);
        }
    }


    private void prepareEnumerationValues() throws SqlDynaException
    {
        for(YEnumType yEnumType : this.typeSystem.getEnumTypes())
        {
            this.recordFactory.addEnumerationValues(yEnumType);
        }
    }


    private void createRelation(YRelation relation) throws SqlDynaException, EvalError
    {
        this.recordFactory.addRelationType(relation);
    }


    private void prepareMapTypes()
    {
        for(YMapType yMapType : this.typeSystem.getMapTypes())
        {
            this.recordFactory.addMapType(yMapType);
        }
    }
}
