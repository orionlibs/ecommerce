package de.hybris.platform.util.jdbc;

import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YCollectionType;
import de.hybris.bootstrap.typesystem.YRelation;
import de.hybris.bootstrap.typesystem.YRelationEnd;
import de.hybris.bootstrap.typesystem.YTypeSystemHandler;
import de.hybris.bootstrap.typesystem.YTypeSystemLoader;
import de.hybris.bootstrap.typesystem.dto.AtomicTypeDTO;
import de.hybris.bootstrap.typesystem.dto.AttributeDTO;
import de.hybris.bootstrap.typesystem.dto.AttributeModifierDTO;
import de.hybris.bootstrap.typesystem.dto.CollectionTypeDTO;
import de.hybris.bootstrap.typesystem.dto.ComposedTypeDTO;
import de.hybris.bootstrap.typesystem.dto.EnumTypeDTO;
import de.hybris.bootstrap.typesystem.dto.EnumValueDTO;
import de.hybris.bootstrap.typesystem.dto.MapTypeDTO;
import de.hybris.bootstrap.typesystem.dto.RelationTypeDTO;
import de.hybris.bootstrap.typesystem.xml.ModelTagListener;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.SlaveTenant;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.util.typesystem.YPersistedTypeSystem;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

public class JDBCTypeSystemReader
{
    private static final Logger log = Logger.getLogger(JDBCTypeSystemReader.class.getName());
    private final Connection conn;
    private final String tablePrefix;
    private final YTypeSystemHandler handler;
    private final YPersistedTypeSystem ts;
    private final List<String> extensionNames;


    public JDBCTypeSystemReader(Connection conn, YTypeSystemHandler handler, List<String> extensionNames)
    {
        this.conn = conn;
        Tenant t = Registry.getCurrentTenant();
        this.tablePrefix = (t instanceof SlaveTenant) ? ((SlaveTenant)t).getDatabaseTablePrefix() : null;
        this.handler = handler;
        this.extensionNames = extensionNames;
        if(this.handler instanceof YTypeSystemLoader && ((YTypeSystemLoader)this.handler)
                        .getSystem() instanceof YPersistedTypeSystem)
        {
            this.ts = (YPersistedTypeSystem)((YTypeSystemLoader)this.handler).getSystem();
        }
        else
        {
            this.ts = null;
        }
    }


    protected String getTableName(String baseName)
    {
        return (this.tablePrefix != null) ? (this.tablePrefix + this.tablePrefix) : baseName;
    }


    protected Connection getConnection()
    {
        return this.conn;
    }


    protected YTypeSystemHandler getHandler()
    {
        return this.handler;
    }


    public void read()
    {
        if(log.isInfoEnabled())
        {
            log.info("loading core types...");
        }
        readAtomicTypes(null);
        readEnumTypes(null);
        readComposedTypes(null);
        readRelationTypes(null);
        readCollectionTypes(null);
        readMapTypes(null);
        readAttributes(null);
        readEnumValues(null);
        for(String extName : this.extensionNames)
        {
            if(log.isInfoEnabled())
            {
                log.info("loading " + extName + " types...");
            }
            getHandler().addExtension(extName, Collections.EMPTY_SET);
            readAtomicTypes(extName);
            readEnumTypes(extName);
            readComposedTypes(extName);
            readRelationTypes(extName);
            readCollectionTypes(extName);
            readMapTypes(extName);
            readAttributes(extName);
            readEnumValues(extName);
        }
    }


    private void tryToClose(Statement stmt, ResultSet rs)
    {
        if(rs != null)
        {
            try
            {
                rs.close();
            }
            catch(SQLException e)
            {
                if(log.isDebugEnabled())
                {
                    log.debug(e.getMessage());
                }
            }
        }
        if(stmt != null)
        {
            try
            {
                stmt.close();
            }
            catch(SQLException e)
            {
                if(log.isDebugEnabled())
                {
                    log.debug(e.getMessage());
                }
            }
        }
    }


    private void handle(SQLException e, String query)
    {
        System.err.println("query = '" + query + "'");
        System.err.println(e.getMessage());
        e.printStackTrace(System.err);
        throw new RuntimeException("sql error - see log");
    }


    protected void registerType(String typeCode, PK pk)
    {
        if(typeCode != null && this.ts != null)
        {
            this.ts.registerType(typeCode, pk);
        }
    }


    protected void registerAttribute(String enclosing, String qualifier, PK pk)
    {
        if(enclosing != null && qualifier != null && this.ts != null)
        {
            this.ts.registerAttribute(enclosing, qualifier, pk);
        }
    }


    protected void registerEnumValue(String type, String code, PK pk)
    {
        if(type != null && code != null && this.ts != null)
        {
            this.ts.registerEnumValue(type, code, pk);
        }
    }


    protected void readAtomicTypes(String extensionName)
    {
        Statement stmt = null;
        ResultSet rs = null;
        String query = null;
        try
        {
            stmt = this.conn.createStatement();
            rs = stmt.executeQuery(
                            query = "SELECT att.PK,(SELECT meta.InternalCode FROM " + getTableName("composedtypes") + " meta WHERE att.TypePkString=meta.PK),att.InternalCode,(SELECT st.InternalCode FROM " + getTableName("atomictypes")
                                            + " st WHERE att.SuperTypePK=st.PK) ,att.p_autocreate,att.p_generate FROM " + getTableName("atomictypes") + " att WHERE att.p_extensionname" + ((extensionName != null) ? ("='" + extensionName + "'") : " IS NULL"));
            JDBCValueMappings.ValueReader<PK, ?> pkReader = JDBCValueMappings.getInstance().getValueReader(PK.class);
            JDBCValueMappings.ValueReader<Boolean, ?> booleanReader = JDBCValueMappings.getInstance().getValueReader(Boolean.class);
            while(rs.next())
            {
                int i = 1;
                PK pk = (PK)pkReader.getValue(rs, i++);
                rs.getString(i++);
                String code = rs.getString(i++);
                String superTypeCode = rs.getString(i++);
                booleanReader.getBoolean(rs, i++);
                boolean generate = booleanReader.getBoolean(rs, i++);
                AtomicTypeDTO atomicTypeDTO = new AtomicTypeDTO(extensionName, code, superTypeCode, true, generate);
                getHandler().loadAtomicType(atomicTypeDTO);
                registerType(code, pk);
            }
        }
        catch(SQLException e)
        {
            handle(e, query);
        }
        finally
        {
            tryToClose(stmt, rs);
        }
    }


    protected void readCollectionTypes(String extensionName)
    {
        Statement stmt = null;
        ResultSet rs = null;
        String query = null;
        try
        {
            stmt = this.conn.createStatement();
            rs = stmt.executeQuery(
                            query = "SELECT ct.PK,(SELECT InternalCode FROM " + getTableName("composedtypes") + " WHERE ct.TypePkString=PK),ct.InternalCode,(SELECT InternalCode FROM " + getTableName("composedtypes") + " WHERE PK=ct.ElementTypePK UNION SELECT InternalCode FROM " + getTableName(
                                            "atomictypes") + " WHERE PK=ct.ElementTypePK UNION SELECT InternalCode FROM " + getTableName("collectiontypes") + " WHERE PK=ct.ElementTypePK UNION SELECT InternalCode FROM " + getTableName("maptypes")
                                            + " WHERE PK=ct.ElementTypePK ),ct.typeOfCollection,ct.p_autocreate,ct.p_generate FROM " + getTableName("collectiontypes") + " ct WHERE ct.p_extensionname" + ((extensionName != null) ? ("='" + extensionName + "'") : " IS NULL"));
            JDBCValueMappings.ValueReader<PK, ?> pkReader = JDBCValueMappings.getInstance().getValueReader(PK.class);
            JDBCValueMappings.ValueReader<Boolean, ?> booleanReader = JDBCValueMappings.getInstance().getValueReader(Boolean.class);
            while(rs.next())
            {
                int i = 1;
                PK pk = (PK)pkReader.getValue(rs, i++);
                rs.getString(i++);
                String code = rs.getString(i++);
                String elementTypeCode = rs.getString(i++);
                int tocCode = rs.getInt(i++);
                booleanReader.getBoolean(rs, i++);
                boolean generate = booleanReader.getBoolean(rs, i++);
                CollectionTypeDTO collectionTypeDTO = new CollectionTypeDTO(extensionName, code, elementTypeCode, YCollectionType.TypeOfCollection.getTypeOfCollection(tocCode), true, generate);
                getHandler().loadCollectionType(collectionTypeDTO);
                registerType(code, pk);
            }
        }
        catch(SQLException e)
        {
            handle(e, query);
        }
        finally
        {
            tryToClose(stmt, rs);
        }
    }


    protected void readMapTypes(String extensionName)
    {
        Statement stmt = null;
        ResultSet rs = null;
        String query = null;
        try
        {
            stmt = this.conn.createStatement();
            rs = stmt.executeQuery(
                            query = "SELECT mt.PK,(SELECT InternalCode FROM " + getTableName("composedtypes") + " WHERE mt.TypePkString=PK),mt.InternalCode,(SELECT InternalCode FROM " + getTableName("composedtypes") + " WHERE PK=mt.ArgumentTypePK UNION SELECT InternalCode FROM " + getTableName(
                                            "atomictypes") + " WHERE PK=mt.ArgumentTypePK UNION SELECT InternalCode FROM " + getTableName("collectiontypes") + " WHERE PK=mt.ArgumentTypePK UNION SELECT InternalCode FROM " + getTableName("maptypes")
                                            + " WHERE PK=mt.ArgumentTypePK ),(SELECT InternalCode FROM " + getTableName("composedtypes") + " WHERE PK=mt.ReturnTypePK UNION SELECT InternalCode FROM " + getTableName("atomictypes") + " WHERE PK=mt.ReturnTypePK UNION SELECT InternalCode FROM "
                                            + getTableName("collectiontypes") + " WHERE PK=mt.ReturnTypePK UNION SELECT InternalCode FROM " + getTableName("maptypes") + " WHERE PK=mt.ReturnTypePK ),mt.p_autocreate,mt.p_generate FROM " + getTableName("maptypes") + " mt WHERE mt.p_extensionname" + ((
                                            extensionName != null) ? ("='" + extensionName + "'") : " IS NULL"));
            JDBCValueMappings.ValueReader<PK, ?> pkReader = JDBCValueMappings.getInstance().getValueReader(PK.class);
            JDBCValueMappings.ValueReader<Boolean, ?> booleanReader = JDBCValueMappings.getInstance().getValueReader(Boolean.class);
            while(rs.next())
            {
                int i = 1;
                PK pk = (PK)pkReader.getValue(rs, i++);
                rs.getString(i++);
                String code = rs.getString(i++);
                String argumentTypeCode = rs.getString(i++);
                String returnTypeCode = rs.getString(i++);
                booleanReader.getBoolean(rs, i++);
                boolean generate = booleanReader.getBoolean(rs, i++);
                MapTypeDTO mapTypeDTO = new MapTypeDTO(extensionName, code, argumentTypeCode, returnTypeCode, true, generate);
                getHandler().loadMapType(mapTypeDTO);
                registerType(code, pk);
            }
        }
        catch(SQLException e)
        {
            handle(e, query);
        }
        finally
        {
            tryToClose(stmt, rs);
        }
    }


    protected void readComposedTypes(String extensionName)
    {
        Statement stmt = null;
        ResultSet rs = null;
        String query = null;
        try
        {
            stmt = this.conn.createStatement();
            rs = stmt.executeQuery(
                            query = "SELECT ct.PK,t.InternalCode,ct.InternalCode,(SELECT st.InternalCode FROM " + getTableName("composedtypes")
                                            + " st WHERE st.PK=ct.SuperTypePK ),ct.jaloClassName,ct.ItemTypeCode,ct.Singleton,ct.ItemJNDIName,ct.p_jaloonly,ct.p_autocreate,ct.p_generate,ct.p_legacypersistence FROM " + getTableName("composedtypes") + " ct JOIN " + getTableName("composedtypes")
                                            + " t ON ct.TypePkString=t.PK WHERE ct.p_extensionname" + ((extensionName != null) ? ("='" + extensionName + "'") : " IS NULL") + " AND NOT( t.InternalCode IN ( 'RelationMetaType','EnumerationMetaType') )");
            JDBCValueMappings.ValueReader<PK, ?> pkReader = JDBCValueMappings.getInstance().getValueReader(PK.class);
            JDBCValueMappings.ValueReader<Boolean, ?> booleanReader = JDBCValueMappings.getInstance().getValueReader(Boolean.class);
            while(rs.next())
            {
                int i = 1;
                PK pk = (PK)pkReader.getValue(rs, i++);
                String metaType = rs.getString(i++);
                String code = rs.getString(i++);
                String superTypeCode = rs.getString(i++);
                String jaloClassName = rs.getString(i++);
                int tc = rs.getInt(i++);
                boolean isSingleton = booleanReader.getBoolean(rs, i++);
                String deploymentName = rs.getString(i++);
                boolean isJaloOnly = booleanReader.getBoolean(rs, i++);
                booleanReader.getBoolean(rs, i++);
                boolean generate = booleanReader.getBoolean(rs, i++);
                boolean legacyPersistence = booleanReader.getBoolean(rs, i++);
                ComposedTypeDTO composedTypeDTO = new ComposedTypeDTO(extensionName, code, superTypeCode, jaloClassName, (tc == 0), isSingleton, isJaloOnly, metaType, deploymentName, true, generate, null, new ModelTagListener.ModelData(), null, legacyPersistence);
                getHandler().loadComposedType(composedTypeDTO);
                registerType(code, pk);
            }
        }
        catch(SQLException e)
        {
            handle(e, query);
        }
        finally
        {
            tryToClose(stmt, rs);
        }
    }


    protected void readAttributes(String extensionName)
    {
        Statement stmt = null;
        ResultSet rs = null;
        String query = null;
        try
        {
            stmt = this.conn.createStatement();
            rs = stmt.executeQuery(
                            query = "SELECT ad.PK,meta.InternalCode,ad.QualifierInternal,(SELECT InternalCode FROM " + getTableName("composedtypes") + " WHERE PK=ad.EnclosingTypePK),(SELECT InternalCode FROM " + getTableName("composedtypes")
                                            + " WHERE PK=ad.AttributeTypePK UNION SELECT InternalCode FROM " + getTableName("atomictypes") + " WHERE PK=ad.AttributeTypePK UNION SELECT InternalCode FROM " + getTableName("collectiontypes")
                                            + " WHERE PK=ad.AttributeTypePK UNION SELECT InternalCode FROM " + getTableName("maptypes") + " WHERE PK=ad.AttributeTypePK ),ad.modifiers,ad.columnName,ad.SuperAttributeDescriptorPK,(SELECT QualifierInternal FROM " + getTableName("attributedescriptors")
                                            + " WHERE PK=ad.SelectionDescriptorPK),ad.PersistenceQualifierInternal,ad.p_defaultvaluedefinitionstring,ad.p_autocreate,ad.p_generate FROM " + getTableName("attributedescriptors") + " ad  JOIN " + getTableName("composedtypes")
                                            + " meta ON ad.TypePkString=meta.PK WHERE ad.p_extensionname" + ((extensionName != null) ? ("='" + extensionName + "'") : " IS NULL"));
            JDBCValueMappings.ValueReader<PK, ?> pkReader = JDBCValueMappings.getInstance().getValueReader(PK.class);
            JDBCValueMappings.ValueReader<Boolean, ?> booleanReader = JDBCValueMappings.getInstance().getValueReader(Boolean.class);
            while(rs.next())
            {
                int i = 1;
                PK pk = (PK)pkReader.getValue(rs, i++);
                String metaType = rs.getString(i++);
                String qualifier = rs.getString(i++);
                String enclosingTypeCode = rs.getString(i++);
                String typeCode = rs.getString(i++);
                int modifiers = rs.getInt(i++);
                String columnName = rs.getString(i++);
                PK superAttPK = (PK)pkReader.getValue(rs, i++);
                String selectionOfQuali = rs.getString(i++);
                String persistenceQualifer = rs.getString(i++);
                String defaultValueDef = rs.getString(i++);
                booleanReader.getBoolean(rs, i++);
                boolean generate = booleanReader.getBoolean(rs, i++);
                boolean isProperty = ((modifiers & 0x100) == 256);
                boolean isCore = (columnName != null && !isProperty);
                AttributeDTO attributeDTO = new AttributeDTO(extensionName, enclosingTypeCode, qualifier, typeCode, new AttributeModifierDTO(modifiers), false, selectionOfQuali,
                                isCore ? YAttributeDescriptor.PersistenceType.CMP : (isProperty ? YAttributeDescriptor.PersistenceType.PROPERTY : YAttributeDescriptor.PersistenceType.JALO), persistenceQualifer, null, defaultValueDef, null, null, metaType, true, generate,
                                new ModelTagListener.ModelData(), false, null);
                YAttributeDescriptor attr = getHandler().loadAttribute(attributeDTO);
                if(superAttPK != null)
                {
                    attr.setInherited(true);
                }
                if(columnName != null)
                {
                    attr.setRealColumnName(columnName);
                }
                registerAttribute(enclosingTypeCode, qualifier, pk);
            }
        }
        catch(SQLException e)
        {
            handle(e, query);
        }
        finally
        {
            tryToClose(stmt, rs);
        }
    }


    protected void readEnumTypes(String extensionName)
    {
        Statement stmt = null;
        ResultSet rs = null;
        String query = null;
        try
        {
            stmt = this.conn.createStatement();
            rs = stmt.executeQuery(
                            query = "SELECT et.PK,t.InternalCode,et.InternalCode,(SELECT InternalCode FROM " + getTableName("composedtypes") + " WHERE PK=et.SuperTypePK ),et.jaloClassName,et.p_autocreate,et.p_generate FROM " + getTableName("composedtypes") + " et JOIN " + getTableName(
                                            "composedtypes") + " t ON et.TypePkString=t.PK WHERE et.p_extensionname" + ((extensionName != null) ? ("='" + extensionName + "'") : " IS NULL") + " AND t.InternalCode='EnumerationMetaType'");
            JDBCValueMappings.ValueReader<PK, ?> pkReader = JDBCValueMappings.getInstance().getValueReader(PK.class);
            JDBCValueMappings.ValueReader<Boolean, ?> booleanReader = JDBCValueMappings.getInstance().getValueReader(Boolean.class);
            while(rs.next())
            {
                int i = 1;
                PK pk = (PK)pkReader.getValue(rs, i++);
                rs.getString(i++);
                String code = rs.getString(i++);
                rs.getString(i++);
                String jaloClassName = rs.getString(i++);
                booleanReader.getBoolean(rs, i++);
                boolean generate = booleanReader.getBoolean(rs, i++);
                EnumTypeDTO enumTypeDTO = new EnumTypeDTO(extensionName, code, jaloClassName, true, generate, null);
                getHandler().loadEnumType(enumTypeDTO);
                registerType(code, pk);
            }
        }
        catch(SQLException e)
        {
            handle(e, query);
        }
        finally
        {
            tryToClose(stmt, rs);
        }
    }


    protected void readEnumValues(String extensionName)
    {
        Statement stmt = null;
        ResultSet rs = null;
        String query = null;
        try
        {
            stmt = this.conn.createStatement();
            rs = stmt.executeQuery(
                            query = "SELECT ev.PK,t.InternalCode,ev.Code,ev.SequenceNumber FROM " + getTableName("enumerationvalues") + " ev JOIN " + getTableName("composedtypes") + " t ON ev.TypePkString=t.PK WHERE ev.p_extensionname" + ((extensionName != null)
                                            ? ("='" + extensionName + "'")
                                            : " IS NULL") + " ");
            JDBCValueMappings.ValueReader<PK, ?> pkReader = JDBCValueMappings.getInstance().getValueReader(PK.class);
            while(rs.next())
            {
                int i = 1;
                PK pk = (PK)pkReader.getValue(rs, i++);
                String enumType = rs.getString(i++);
                String code = rs.getString(i++);
                int pos = rs.getInt(i++);
                EnumValueDTO enumValueDTO = new EnumValueDTO(extensionName, enumType, code, pos, false, null);
                getHandler().loadEnumValue(enumValueDTO);
                registerEnumValue(enumType, code, pk);
            }
        }
        catch(SQLException e)
        {
            handle(e, query);
        }
        finally
        {
            tryToClose(stmt, rs);
        }
    }


    protected void readRelationTypes(String extensionName)
    {
        Statement stmt = null;
        ResultSet rs = null;
        String query = null;
        try
        {
            stmt = this.conn.createStatement();
            rs = stmt.executeQuery(
                            query = "SELECT rt.PK,t.InternalCode,rt.InternalCode,tgtAttr.PK,tgtAttr.QualifierInternal,(SELECT InternalCode FROM " + getTableName("composedtypes") + " WHERE PK=srcAttr.EnclosingTypePK),tgtAttr.modifiers,(SELECT InternalCode FROM " + getTableName("composedtypes")
                                            + " WHERE PK=tgtAttr.TypePkString),CASE WHEN rt.p_localized=1 THEN ( SELECT ct.typeOfCollection FROM " + getTableName("maptypes") + " mt JOIN " + getTableName("collectiontypes")
                                            + " ct ON mt.ReturnTypePK=ct.PK WHERE mt.PK=tgtAttr.AttributeTypePK ) ELSE ( SELECT ct.typeOfCollection FROM " + getTableName("collectiontypes")
                                            + " ct WHERE ct.PK=tgtAttr.AttributeTypePK ) END,srcAttr.PK,srcAttr.QualifierInternal,(SELECT InternalCode FROM " + getTableName("composedtypes") + " WHERE PK=tgtAttr.EnclosingTypePK),srcAttr.modifiers,(SELECT InternalCode FROM " + getTableName(
                                            "composedtypes") + " WHERE PK=srcAttr.TypePkString),CASE WHEN rt.p_localized=1 THEN ( SELECT ct.typeOfCollection FROM " + getTableName("maptypes") + " mt JOIN " + getTableName("collectiontypes")
                                            + " ct ON mt.ReturnTypePK=ct.PK WHERE mt.PK=srcAttr.AttributeTypePK ) ELSE ( SELECT ct.typeOfCollection FROM " + getTableName("collectiontypes")
                                            + " ct WHERE ct.PK=srcAttr.AttributeTypePK ) END,posAttr.QualifierInternal,locAttr.QualifierInternal,rt.p_localized,rt.jaloClassName,rt.ItemTypeCode,rt.ItemJNDIName,rt.p_autocreate,rt.p_generate FROM " + getTableName("composedtypes") + " rt JOIN "
                                            + getTableName("composedtypes") + " t ON rt.TypePkString=t.PK JOIN " + getTableName("attributedescriptors") + " srcAttr ON rt.p_sourceattribute=srcAttr.PK JOIN " + getTableName("attributedescriptors")
                                            + " tgtAttr ON rt.p_targetattribute=tgtAttr.PK LEFT JOIN " + getTableName("attributedescriptors") + " posAttr ON rt.p_orderingattribute=posAttr.PK LEFT JOIN " + getTableName("attributedescriptors")
                                            + " locAttr ON rt.p_localizationattribute=locAttr.PK WHERE rt.p_extensionname" + ((extensionName != null) ? ("='" + extensionName + "'") : " IS NULL") + " AND t.InternalCode='RelationMetaType'");
            JDBCValueMappings.ValueReader<PK, ?> pkReader = JDBCValueMappings.getInstance().getValueReader(PK.class);
            JDBCValueMappings.ValueReader<Boolean, ?> booleanReader = JDBCValueMappings.getInstance().getValueReader(Boolean.class);
            while(rs.next())
            {
                int i = 1;
                PK pk = (PK)pkReader.getValue(rs, i++);
                String metaType = rs.getString(i++);
                String code = rs.getString(i++);
                PK srcPK = (PK)pkReader.getValue(rs, i++);
                String srcRole = rs.getString(i++);
                String srcType = rs.getString(i++);
                int srcModifiers = rs.getInt(i++);
                String srcMetaType = rs.getString(i++);
                int srcTocInt = rs.getInt(i++);
                if(rs.wasNull())
                {
                    srcTocInt = -1;
                }
                PK tgtPK = (PK)pkReader.getValue(rs, i++);
                String tgtRole = rs.getString(i++);
                String tgtType = rs.getString(i++);
                int tgtModifiers = rs.getInt(i++);
                String tgtMetaType = rs.getString(i++);
                int tgtTocInt = rs.getInt(i++);
                if(rs.wasNull())
                {
                    tgtTocInt = -1;
                }
                String posQualifier = rs.getString(i++);
                rs.getString(i++);
                boolean isLocalized = booleanReader.getBoolean(rs, i++);
                String jaloClassName = rs.getString(i++);
                int tc = rs.getInt(i++);
                boolean isAbstract = (tc == 0);
                String deploymentName = rs.getString(i++);
                booleanReader.getBoolean(rs, i++);
                boolean generate = booleanReader.getBoolean(rs, i++);
                YRelationEnd.Cardinality srcCard = (isAbstract && srcTocInt == -1) ? YRelationEnd.Cardinality.ONE : YRelationEnd.Cardinality.MANY;
                YRelationEnd.Cardinality tgtCard = (isAbstract && tgtTocInt == -1) ? YRelationEnd.Cardinality.ONE : YRelationEnd.Cardinality.MANY;
                RelationTypeDTO relationTypeDTO = new RelationTypeDTO(extensionName, code, metaType, jaloClassName, srcRole, srcType, true, srcModifiers, false, srcCard, (tgtCard == YRelationEnd.Cardinality.ONE && posQualifier != null),
                                (srcTocInt == -1) ? YCollectionType.TypeOfCollection.COLLECTION : YCollectionType.TypeOfCollection.getTypeOfCollection(srcTocInt), null, srcMetaType, null, new ModelTagListener.ModelData(), tgtRole, tgtType, true, tgtModifiers, false, tgtCard,
                                (srcCard == YRelationEnd.Cardinality.ONE && posQualifier != null), (tgtTocInt == -1) ? YCollectionType.TypeOfCollection.COLLECTION : YCollectionType.TypeOfCollection.getTypeOfCollection(tgtTocInt), null, tgtMetaType, null, new ModelTagListener.ModelData(),
                                deploymentName, isLocalized, true, generate);
                YRelation rt = getHandler().loadRelation(relationTypeDTO);
                if(rt != null)
                {
                    registerType(code, pk);
                    registerAttribute(tgtType, srcRole, srcPK);
                    registerAttribute(srcType, tgtRole, tgtPK);
                }
            }
        }
        catch(SQLException e)
        {
            handle(e, query);
        }
        finally
        {
            tryToClose(stmt, rs);
        }
    }
}
