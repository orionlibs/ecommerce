package de.hybris.platform.persistence.hjmpgen;

import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.hjmp.HJMPException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HJMPEntityBean
{
    private static final List COPYRIGHT_STATEMENT = Arrays.asList(new String[] {"/*", " * Copyright (c) " +
                    Year.now()
                                    .toString() + " SAP SE or an SAP affiliate company. All rights reserved.", " */"});
    private static final List STATIC_VARIABLES = Arrays.asList(
                    new String[] {"", "private static boolean LOG=false;", "private static boolean LOG_ATTRIBUTEACCESS=false;", "private static final boolean LOG_MODIFICATIONS = false;", "private static final boolean CHECK_VALUE_DIFFERENCES = Config.getBoolean(\"hjmp.write.changes.only\", true );",
                                    "//private static final ItemDeployment deployment;", "//private static final String database = Config.getDatabase();", "//private static final String table;", "//private static final String dumpTable;"});
    private static final List FIX_VARIABLES = Arrays.asList(new String[] {"private int ejbCreateNestedCounter = 0;", "private boolean beforeCreate = false;", "", "public static final int READER = 0;", "public static final int WRITER = 1;", ""});
    private static final List FIX_METHODS = Arrays.asList(new String[] {
                    "", "", "//private static String appserver = de.hybris.platform.util.Config.getParameter(\"appserver\");", "", "private void clearFields()", "{", "  localInvalidationAdded = false;", "\tsetEntityState(null);", "\tif (ejbCreateNestedCounter!=0)", "\t{",
                    "\t\tif (LOG) /*conv-log*/ log.info( \"ejbCreateNestedCounter is \"+ejbCreateNestedCounter+\" in clearFields()\" );", "\t\tejbCreateNestedCounter = 0;", "\t}", "}", "", "public void ejbLoad()", "{", "\t   clearFields();", "\t   loadData();", "\t   super.ejbLoad();",
                    "}", "", "", "public long getHJMPTS()", "{", "\treturn getEntityState().hjmpTS;", "}", "", ""});
    private static final Map jdbcAccessors;
    private static final Map jdbcNULLAccessors;
    private static final Set jdbcNullAbleTypes;
    private static final Map primiveTypes;
    private final ItemDeployment itemDeployment;

    static
    {
        Map<Object, Object> map2 = new HashMap<>();
        map2.put(String.class, "String");
        map2.put(long.class, "Long");
        map2.put(boolean.class, "Boolean");
        map2.put(int.class, "Int");
        map2.put(Integer.class, "Object");
        map2.put(double.class, "Double");
        map2.put(Date.class, "Timestamp");
        map2.put(Float.class, "Float");
        map2.put(Serializable.class, "BinaryStream");
        map2.put(Long.class, "Long");
        map2.put(Integer.class, "Integer");
        map2.put(PK.class, "String");
        jdbcAccessors = Collections.unmodifiableMap(map2);
        Map<Object, Object> map1 = new HashMap<>();
        map1.put(String.class, "VARCHAR");
        map1.put(long.class, "NUMERIC");
        map1.put(boolean.class, "NUMERIC");
        map1.put(int.class, "NUMERIC");
        map1.put(Integer.class, "NUMERIC");
        map1.put(double.class, "NUMERIC");
        map1.put(Date.class, "TIMESTAMP");
        map1.put(Serializable.class, "BLOB");
        jdbcNULLAccessors = Collections.unmodifiableMap(map1);
        Set<Class<String>> set = new HashSet();
        set.add(String.class);
        set.add(Integer.class);
        set.add(Date.class);
        set.add(Serializable.class);
        jdbcNullAbleTypes = Collections.unmodifiableSet(set);
        Map<Object, Object> map = new HashMap<>();
        map.put("boolean", boolean.class);
        map.put("char", char.class);
        map.put("byte", byte.class);
        map.put("short", short.class);
        map.put("int", int.class);
        map.put("long", long.class);
        map.put("float", float.class);
        map.put("double", double.class);
        primiveTypes = Collections.unmodifiableMap(map);
    }

    private List nonPKAttributes = null;
    private List allAttributes = null;
    private List createMethods = null;
    private Map class2AccessMap = null;
    private Map attribute2NumberMap = null;
    private final String EXTITEM_CLASSNAME = "de.hybris.platform.persistence.ExtensibleItemEJB";
    private Boolean isExtensible;
    private Class ejbClass;


    protected Class getHomeClass()
    {
        return this.itemDeployment.getHomeInterface();
    }


    public HJMPEntityBean(ItemDeployment newItemDeployment)
    {
        this.EXTITEM_CLASSNAME = "de.hybris.platform.persistence.ExtensibleItemEJB";
        this.isExtensible = null;
        this.ejbClass = null;
        this.itemDeployment = newItemDeployment;
    }


    private boolean isExtensibleItem()
    {
        if(this.isExtensible == null)
        {
            try
            {
                Class ec = getClassForName("de.hybris.platform.persistence.ExtensibleItemEJB");
                this.isExtensible = Boolean.valueOf(ec.isAssignableFrom(getEJBClass()));
            }
            catch(ClassNotFoundException e)
            {
                throw new HJMPGeneratorException("cannot find extensible item class de.hybris.platform.persistence.ExtensibleItemEJB");
            }
        }
        return this.isExtensible.booleanValue();
    }


    private boolean isMetaInformationItem()
    {
        return getEJBClass().getName().endsWith(".MetaInformationEJB");
    }


    protected Class getEJBClass()
    {
        if(this.ejbClass == null)
        {
            String ejbName = getFullyQualifiedSuperClassName();
            try
            {
                this.ejbClass = getClassForName(ejbName);
            }
            catch(ClassNotFoundException e)
            {
                throw new HJMPGeneratorException("cannot find ejb class " + ejbName);
            }
        }
        return this.ejbClass;
    }


    protected String getFullyQualifiedBaseName()
    {
        return this.itemDeployment.getName();
    }


    protected boolean isGeneric()
    {
        return this.itemDeployment.isGeneric();
    }


    protected int getTypeCode()
    {
        return this.itemDeployment.getTypeCode();
    }


    protected String getSimpleBaseName()
    {
        return getFullyQualifiedBaseName().substring(getFullyQualifiedBaseName().lastIndexOf('.') + 1);
    }


    protected String getFullyQualifiedClassName()
    {
        return getFullyQualifiedBaseName() + "_HJMPWrapper";
    }


    protected String getFullyQualifiedRemoteClassName()
    {
        return getFullyQualifiedBaseName() + "_Remote";
    }


    protected String getFullyQualifiedHomeClassName()
    {
        return getFullyQualifiedBaseName() + "_Home";
    }


    protected String getFullyQualifiedSuperClassName()
    {
        return this.itemDeployment.getImplClass().getName();
    }


    protected String getSimpleClassName()
    {
        return getFullyQualifiedClassName().substring(getFullyQualifiedClassName().lastIndexOf('.') + 1);
    }


    protected String getEntityStateName()
    {
        return getSimpleBaseName() + "EntityState";
    }


    protected String getCacheUnitName()
    {
        return getEntityStateName() + "CacheUnit";
    }


    protected String getPackageName()
    {
        return getFullyQualifiedBaseName().substring(0, getFullyQualifiedBaseName().lastIndexOf('.'));
    }


    protected List getAllAttributes()
    {
        if(this.allAttributes == null)
        {
            this.allAttributes = new ArrayList();
            this.allAttributes.addAll(this.itemDeployment.getAttributes());
        }
        return this.allAttributes;
    }


    protected List getNonPKAttributes()
    {
        if(this.nonPKAttributes == null)
        {
            this.nonPKAttributes = new ArrayList();
            Iterator<ItemDeployment.Attribute> iter = getAllAttributes().iterator();
            while(iter.hasNext())
            {
                ItemDeployment.Attribute next = iter.next();
                if(!next.getQualifier().equals(getPKFieldName()))
                {
                    this.nonPKAttributes.add(next);
                }
            }
        }
        return this.nonPKAttributes;
    }


    protected int getAttributeNumber(String qualifier)
    {
        if(this.attribute2NumberMap == null)
        {
            this.attribute2NumberMap = new HashMap<>();
            this.attribute2NumberMap.put(qualifier, Integer.valueOf(0));
            return 0;
        }
        Integer number = (Integer)this.attribute2NumberMap.get(qualifier);
        if(number == null)
        {
            this.attribute2NumberMap.put(qualifier, number = Integer.valueOf(this.attribute2NumberMap.size()));
        }
        return number.intValue();
    }


    protected String getAccess(Class<?> c)
    {
        if(this.class2AccessMap == null)
        {
            this.class2AccessMap = new HashMap<>();
        }
        Integer i = (Integer)this.class2AccessMap.get(c);
        if(i == null)
        {
            this.class2AccessMap.put(c, i = Integer.valueOf(this.class2AccessMap.size()));
        }
        return "access[" + i.intValue() + "]";
    }


    protected String getAccessDefinition(Class c, int pos)
    {
        return "access[" + pos + "] = new Object[]{ JDBCValueMappings.getInstance().getValueReader( " + c.getName() + ".class ),JDBCValueMappings.getInstance().getValueWriter( " + c
                        .getName() + ".class ) }";
    }


    protected void insertAccessDefinitionBlock(int insertPos, String indent, JavaFile fileContent)
    {
        if(this.class2AccessMap == null || this.class2AccessMap.isEmpty())
        {
            return;
        }
        fileContent.insert(insertPos++, indent, "private static final Object[][] access = new Object[" + this.class2AccessMap.size() + "][2];");
        fileContent.insert(insertPos++, indent, " static {");
        for(Iterator<Map.Entry> it = this.class2AccessMap.entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry e = it.next();
            Class type = (Class)e.getKey();
            Integer pos = (Integer)e.getValue();
            fileContent.insert(insertPos++, indent, getAccessDefinition(type, pos.intValue()) + ";");
        }
        fileContent.insert(insertPos++, indent, "}");
    }


    protected List getCreateMethods()
    {
        if(this.createMethods == null)
        {
            this.createMethods = new ArrayList();
            Class ejbClass = getEJBClass();
            Method[] methods = ejbClass.getMethods();
            for(int i = 0; i < methods.length; i++)
            {
                Method next = methods[i];
                if(next.getName().equals("ejbCreate") && Modifier.isPublic(next.getModifiers()))
                {
                    try
                    {
                        Method postMethod = ejbClass.getMethod("ejbPostCreate", next.getParameterTypes());
                        this.createMethods.add(new HJMPCreateMethod(next.getParameterTypes(), next.getExceptionTypes(), postMethod
                                        .getExceptionTypes()));
                    }
                    catch(NoSuchMethodException e)
                    {
                        throw new HJMPGeneratorException("no ejbPostCreate for " + next);
                    }
                }
            }
        }
        return this.createMethods;
    }


    protected static String getJDBCAccessorFor(Class c)
    {
        String result = (String)jdbcAccessors.get(c);
        if(result == null)
        {
            throw new HJMPException("no JDBC Accessor defined for " + c);
        }
        return result;
    }


    protected static String getJDBCNullTypeFor(Class c)
    {
        String result = (String)jdbcNULLAccessors.get(c);
        if(result == null)
        {
            throw new HJMPException("no JDBC NULL type defined for " + c);
        }
        return result;
    }


    protected static boolean isJDBCNullAbleType(Class c)
    {
        return jdbcNullAbleTypes.contains(c);
    }


    protected static String getJDBCValueFor(Class type, String varName)
    {
        return getJDBCValueFor(type, varName, false);
    }


    protected static String getJDBCValueFor(Class type, String varName, boolean forNullCheck)
    {
        if(type.equals(Date.class))
        {
            return forNullCheck ? varName : (varName + "==null ? null : new java.sql.Timestamp( " + varName + ".getTime() )");
        }
        if(type.equals(Serializable.class))
        {
            return forNullCheck ? (varName + "_binaryStream") : (varName + "_binaryStream, " + varName + "_length");
        }
        return varName;
    }


    protected String getFileName()
    {
        return getFullyQualifiedClassName().replace('.', File.separatorChar) + ".java";
    }


    protected String getRemoteFileName()
    {
        return getFullyQualifiedRemoteClassName().replace('.', File.separatorChar) + ".java";
    }


    protected String getHomeFileName()
    {
        return getFullyQualifiedHomeClassName().replace('.', File.separatorChar) + ".java";
    }


    public void writeToFile(File targetDirectory) throws IOException, ClassNotFoundException
    {
        File targetFile = new File(targetDirectory.getAbsolutePath() + targetDirectory.getAbsolutePath() + File.separator);
        targetFile.getParentFile().mkdirs();
        FileWriter fileWriter = new FileWriter(targetFile);
        BufferedWriter writer = new BufferedWriter(fileWriter);
        try
        {
            Iterator<String> iter = getFileContent().iterator();
            while(iter.hasNext())
            {
                writer.write((String)iter.next() + "\n");
            }
            writer.flush();
            writer.close();
        }
        catch(Throwable throwable)
        {
            try
            {
                writer.close();
            }
            catch(Throwable throwable1)
            {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
        if(isGeneric() && "de.hybris.platform.persistence.GenericItemHome".equalsIgnoreCase(getHomeClass().getName()))
        {
            File remoteFile = new File(targetDirectory.getAbsolutePath() + targetDirectory.getAbsolutePath() + File.separator);
            remoteFile.getParentFile().mkdirs();
            fileWriter = new FileWriter(remoteFile);
            BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriter);
            try
            {
                bufferedWriter1.write("package de.hybris.platform.persistence;\n");
                bufferedWriter1.write("public interface " + getSimpleBaseName() + "_Remote extends de.hybris.platform.persistence.GenericItemRemote\n");
                bufferedWriter1.write("{}");
                bufferedWriter1.flush();
                bufferedWriter1.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    bufferedWriter1.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
            File homeFile = new File(targetDirectory.getAbsolutePath() + targetDirectory.getAbsolutePath() + File.separator);
            homeFile.getParentFile().mkdirs();
            fileWriter = new FileWriter(homeFile);
            BufferedWriter bufferedWriter2 = new BufferedWriter(fileWriter);
            try
            {
                bufferedWriter2.write("package de.hybris.platform.persistence;\n");
                bufferedWriter2.write("public interface " +
                                getSimpleBaseName() + "_Home extends de.hybris.platform.persistence.ItemHome\n");
                bufferedWriter2.write("{");
                bufferedWriter2.write("public " +
                                getSimpleBaseName() + "_Remote create( String pkBase, de.hybris.platform.persistence.type.ComposedTypeRemote type, de.hybris.platform.persistence.property.EJBPropertyContainer props ) throws javax.ejb.CreateException;\n");
                bufferedWriter2.write("public " + getSimpleBaseName() + "_Remote findByPrimaryKey( PK pk ) throws YFinderException;\n");
                bufferedWriter2.write("}");
                bufferedWriter2.flush();
                bufferedWriter2.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    bufferedWriter2.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        else if(isGeneric() && "de.hybris.platform.persistence.link.LinkHome".equalsIgnoreCase(getHomeClass().getName()))
        {
            File remoteFile = new File(targetDirectory.getAbsolutePath() + targetDirectory.getAbsolutePath() + File.separator);
            remoteFile.getParentFile().mkdirs();
            fileWriter = new FileWriter(remoteFile);
            BufferedWriter bufferedWriter1 = new BufferedWriter(fileWriter);
            try
            {
                bufferedWriter1.write("package de.hybris.platform.persistence.link;\n");
                bufferedWriter1.write("public interface " + getSimpleBaseName() + "_Remote extends de.hybris.platform.persistence.link.LinkRemote\n");
                bufferedWriter1.write("{}");
                bufferedWriter1.flush();
                bufferedWriter1.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    bufferedWriter1.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
            File homeFile = new File(targetDirectory.getAbsolutePath() + targetDirectory.getAbsolutePath() + File.separator);
            homeFile.getParentFile().mkdirs();
            fileWriter = new FileWriter(homeFile);
            BufferedWriter bufferedWriter2 = new BufferedWriter(fileWriter);
            try
            {
                bufferedWriter2.write("package de.hybris.platform.persistence.link;\n");
                bufferedWriter2.write("public interface " +
                                getSimpleBaseName() + "_Home extends de.hybris.platform.persistence.link.LinkHome\n");
                bufferedWriter2.write("{");
                bufferedWriter2.write("public " +
                                getSimpleBaseName() + "_Remote create( String qual, String langPK, String sourcePK , String targetPK, int sequenceNumber ) throws javax.ejb.CreateException, de.hybris.platform.persistence.EJBInvalidParameterException;");
                bufferedWriter2.write("public " + getSimpleBaseName() + "_Remote findByPrimaryKey( PK pk ) throws YFinderException;\n");
                bufferedWriter2.write("}");
                bufferedWriter2.flush();
                bufferedWriter2.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    bufferedWriter2.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
    }


    public String getPKGetter()
    {
        if(getSimpleBaseName().equals("PKFactory"))
        {
            return "getId()";
        }
        return "getPkString()";
    }


    public String getPKColumnName()
    {
        if(getSimpleBaseName().equals("PKFactory"))
        {
            return "ID";
        }
        return "PK";
    }


    public String getPKFieldName()
    {
        if(getSimpleBaseName().equals("PKFactory"))
        {
            return "id";
        }
        return "pkString";
    }


    public boolean isBeanAnItem()
    {
        return !getSimpleBaseName().equals("PKFactory");
    }


    private static final Map primitiveTypeMappings = new HashMap<>();

    static
    {
        primitiveTypeMappings.put("float", float.class);
        primitiveTypeMappings.put("double", double.class);
        primitiveTypeMappings.put("byte", byte.class);
        primitiveTypeMappings.put("char", char.class);
        primitiveTypeMappings.put("short", short.class);
        primitiveTypeMappings.put("boolean", boolean.class);
        primitiveTypeMappings.put("long", long.class);
        primitiveTypeMappings.put("int", int.class);
    }

    protected Class tryToResolveClass(String attributeType)
    {
        String s = attributeType.trim();
        if(s.indexOf('.') < 0)
        {
            if(Character.isUpperCase(s.charAt(0)))
            {
                s = "java.lang." + s;
            }
            else
            {
                Class cl = (Class)primitiveTypeMappings.get(s);
                if(cl == null)
                {
                    throw new IllegalArgumentException("cannot find primitive class for type string '" + attributeType + "'");
                }
                return cl;
            }
        }
        try
        {
            return Class.forName(s, false, HJMPEntityBean.class.getClassLoader());
        }
        catch(ClassNotFoundException e)
        {
            System.err.println("cannot find class for type string '" + attributeType + "' (tried '" + s + "') : " + e
                            .getMessage());
            throw new IllegalArgumentException("cannot find class for type string '" + attributeType + "' (tried '" + s + "') : " + e
                            .getMessage());
        }
    }


    public List getFileContent() throws ClassNotFoundException
    {
        JavaFile fileContent = new JavaFile();
        List<ItemDeployment.Attribute> allAttributes = getAllAttributes();
        fileContent.addAll(COPYRIGHT_STATEMENT);
        fileContent.add("package " + getPackageName() + ";");
        fileContent.add("");
        fileContent.add("import static de.hybris.platform.core.threadregistry.OperationInfo.Category.SYSTEM;");
        fileContent.add("import static de.hybris.platform.core.threadregistry.OperationInfo.updateThread;");
        fileContent.add("");
        fileContent.add("import de.hybris.platform.persistence.SystemEJB;");
        fileContent.add("import de.hybris.platform.jdbcwrapper.DataSourceImpl; ");
        fileContent.add("import de.hybris.platform.persistence.property.JDBCValueMappings; ");
        fileContent.add("import de.hybris.platform.persistence.property.JDBCValueMappings.ValueReader; ");
        fileContent.add("import de.hybris.platform.persistence.property.JDBCValueMappings.ValueWriter; ");
        fileContent.add("import de.hybris.platform.persistence.hjmp.EntityState;");
        fileContent.add("import de.hybris.platform.persistence.hjmp.FinderResult;");
        fileContent.add("import de.hybris.platform.persistence.hjmp.HJMPFinderException;");
        fileContent.add("import de.hybris.platform.persistence.hjmp.HJMPException;");
        fileContent.add("import de.hybris.platform.persistence.hjmp.HJMPConstants;");
        fileContent.add("import de.hybris.platform.persistence.hjmp.HJMPUtils;");
        fileContent.add("import de.hybris.platform.persistence.hjmp.HJMPFindInvalidationListener;");
        fileContent.add("import de.hybris.platform.tx.*;");
        fileContent.add("import de.hybris.platform.cache.AbstractCacheUnit;");
        fileContent.add("import de.hybris.platform.cache.Cache;");
        fileContent.add("import de.hybris.platform.core.ItemDeployment;");
        fileContent.add("import de.hybris.platform.licence.*;");
        fileContent.add("import de.hybris.platform.persistence.framework.PersistencePool;");
        fileContent.add("import de.hybris.platform.core.Registry;");
        fileContent.add("import de.hybris.platform.cache.InvalidationManager;");
        fileContent.add("import de.hybris.platform.cache.InvalidationTopic;");
        fileContent.add("import de.hybris.platform.persistence.ItemCacheKey;");
        fileContent.add("import de.hybris.platform.persistence.AbstractEntityState;");
        fileContent.add("import de.hybris.platform.persistence.property.TypeInfoMap;");
        fileContent.add("import de.hybris.platform.persistence.property.PersistenceManager;");
        fileContent.add("import de.hybris.platform.persistence.property.EJBPropertyRowCache;");
        fileContent.add("import de.hybris.platform.persistence.property.PropertyJDBC;");
        fileContent.add("import de.hybris.platform.persistence.property.ItemPropertyCacheKey;");
        fileContent.add("import de.hybris.platform.core.PK;");
        fileContent.add("import de.hybris.platform.core.threadregistry.OperationInfo;");
        fileContent.add("import de.hybris.platform.core.threadregistry.RevertibleUpdate;");
        fileContent.add("import de.hybris.platform.util.Config;");
        fileContent.add("import java.util.BitSet;");
        fileContent.add("import java.util.Collection;");
        fileContent.add("import java.util.ArrayList;");
        fileContent.add("import java.util.Map;");
        fileContent.add("import java.util.HashMap;");
        fileContent.add("import java.util.List;");
        fileContent.add("import java.util.Iterator;");
        fileContent.add("import java.util.Arrays;");
        fileContent.add("import java.util.Date;");
        fileContent.add("");
        fileContent.add("import de.hybris.platform.util.jeeapi.*;");
        fileContent.add("import java.sql.*;");
        fileContent.add("import javax.sql.DataSource;");
        fileContent.add("import java.io.*;");
        fileContent.add("import org.apache.log4j.Logger;");
        fileContent.add("");
        fileContent.add("@SuppressWarnings(\"all\")");
        fileContent.add("public class " + getSimpleClassName() + " extends " + getFullyQualifiedSuperClassName());
        fileContent.startBlock();
        fileContent.addAll(STATIC_VARIABLES);
        for(Iterator<ItemDeployment.Attribute> it = getAllAttributes().iterator(); it.hasNext(); )
        {
            ItemDeployment.Attribute attr = it.next();
            fileContent.add("//private final String column_" + attr.getQualifier() + " = getColumn(\"" + attr.getQualifier() + "\");");
        }
        fileContent.add(" public static final String TYPECODE_STR = \"" + getTypeCode() + "\".intern();");
        fileContent.add("static");
        fileContent.startBlock();
        fileContent.add("LOG = \"true\".equals( System.getProperty( \"hjmp.log\" ) );");
        fileContent.add("LOG_ATTRIBUTEACCESS = \"true\".equals( System.getProperty( \"hjmp.log.attributeaccess\" ) );");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.addAll(FIX_VARIABLES);
        int accessInsertPos = fileContent.getLineNumber();
        String accessIndent = fileContent.getIndent();
        fileContent.add(getEntityStateName() + " entityState = null;");
        fileContent.add("boolean localInvalidationAdded = false;");
        fileContent.addAll(FIX_METHODS);
        fileContent.add("");
        fileContent.add("static final Logger log = Logger.getLogger( " + getSimpleClassName() + ".class.getName() );");
        fileContent.add("private final String getID()");
        fileContent.startBlock();
        fileContent.add("return Integer.toHexString( System.identityHashCode(this) );");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("private final " + getEntityStateName() + " getEntityState()");
        fileContent.add("{");
        fileContent.add("\tif (entityState==null)");
        fileContent.add("\t{");
        fileContent.add("\t\tthrow new HJMPException( \"no entity state available for \"+entityContext.getPK() );");
        fileContent.add("\t}");
        fileContent.add("\treturn entityState;");
        fileContent.add("}");
        fileContent.add("");
        fileContent.add("private final void invalidate( boolean success, boolean removed )");
        fileContent.startBlock();
        fileContent.add("final Transaction tx = Transaction.current();");
        fileContent.add("");
        fileContent.add("final PK thePK = getEntityState().getPK();");
        fileContent
                        .add("final int invType = removed ? AbstractCacheUnit.INVALIDATIONTYPE_REMOVED : AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED;");
        fileContent
                        .add("final Object[] key = new Object[]{ Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, thePK.getTypeCodeAsString(), thePK };");
        fileContent.add("");
        fileContent.add("if (tx.isRunning()) // tx running -> just invalidate");
        fileContent.add("   tx.invalidate(key, 3, invType);");
        fileContent.add("else if (success)");
        fileContent
                        .add("   tx.invalidateAndNotifyCommit(key, 3, invType); // tx not running -> invalidate and notify commit in one go");
        fileContent.add("else");
        fileContent
                        .add("   tx.invalidateAndNotifyRollback(key, 3, invType);// tx not running -> invalidate and notify rollback in one go");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("private final void setEntityState( final " + getEntityStateName() + " newEntityState )");
        fileContent.add("{");
        fileContent.add("   if( newEntityState!=null ) setNeedsStoring(true);");
        fileContent.add("   entityState = newEntityState;");
        fileContent.add("\tif( ejbCreateNestedCounter==0 ) addLocalRollbackInvalidation(false);");
        fileContent.add("}");
        fileContent.add("private final void addLocalRollbackInvalidation( boolean remove )");
        fileContent.add("{");
        fileContent
                        .add("\tif( !localInvalidationAdded && entityState != null && entityState.isTransactionBound && Transaction.current().isRunning() )");
        fileContent.add("\t{");
        fileContent.add("      localInvalidationAdded = true;");
        fileContent.add("      Object[] key = new Object[]");
        fileContent.add("      {");
        fileContent.add("      \tCache.CACHEKEY_HJMP,");
        fileContent.add("      \tCache.CACHEKEY_ENTITY,");
        fileContent.add("\t      TYPECODE_STR,");
        fileContent.add("\t      entityContext.getPK()");
        fileContent.add("      };");
        fileContent.add("      Transaction.current().addToDelayedRollbackLocalInvalidations(");
        fileContent.add("      \tkey,");
        fileContent.add("      \tremove?AbstractCacheUnit.INVALIDATIONTYPE_REMOVED : AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED,");
        fileContent.add("      \t3");
        fileContent.add("      );");
        fileContent.add("\t}");
        fileContent.add("}");
        fileContent.add("");
        fileContent.add("private final void loadData()");
        fileContent.startBlock();
        fileContent.add("PK pk = entityContext.getPK();");
        fileContent.add(getEntityStateName() + " state =");
        fileContent
                        .add("\t" +
                                        getCacheUnitName() + ".getInstance( getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), pk ).getEntityState();");
        fileContent.add("if (LOG) /*conv-log*/ log.debug(\"EJB loading \"+pk+\" to \"+getID()+\": \"+state.toDetailedString());");
        fileContent.add("if (state==null) throw new YNoSuchEntityException(\"entity \"+pk+\" not found\",pk);");
        fileContent.add("setEntityState( state );");
        fileContent.add("setNeedsStoring(false);");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("private OperationInfo getEJBOperationInfo()");
        fileContent.startBlock();
        fileContent.add("if (getEntityContext().getPersistencePool().isSystemCriticalType(" + getTypeCode() + "))");
        fileContent.startBlock();
        fileContent.add("return OperationInfo.builder().withCategory(SYSTEM).build();");
        fileContent.endBlock();
        fileContent.add("return OperationInfo.empty();");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("public void ejbStore()");
        fileContent.startBlock();
        fileContent.add("if (LOG || (LOG_MODIFICATIONS && needsStoring())) ");
        fileContent.add("\t/*conv-log*/ log.debug(\"EJB storing \"+" + getPKGetter() + "+\" from \"+getID()+\" (tx-bound=\"+needsStoring()+\")\" );");
        fileContent.add("if (needsStoring())");
        fileContent.startBlock();
        fileContent.add("final boolean fieldsChanged = getEntityState().hasChangedFields();");
        fileContent.add("final boolean cachesChanged = hasModifiedCaches();");
        fileContent.add("if( fieldsChanged || cachesChanged )");
        fileContent.startBlock();
        fileContent.add("boolean success = false;");
        fileContent.add("try (final RevertibleUpdate theUpdate = updateThread(getEJBOperationInfo()))");
        fileContent.startBlock();
        fileContent.add("super.ejbStore();");
        fileContent.add("// save timestamps");
        fileContent.add("setModifiedTimestamp( new Date() );");
        fileContent.add("// save caches");
        fileContent.add("if( cachesChanged ) storeCaches();");
        fileContent.add("super.ejbStore();");
        if(isExtensibleItem())
        {
            fileContent.add("if( cachesChanged ) writePropertyCaches();");
        }
        fileContent.add("writeACLEntries();");
        if(isExtensibleItem())
        {
            fileContent.add("final EJBPropertyRowCache prc = cachesChanged ? getModifiedUnlocalizedPropertyCache() : null;");
            fileContent.add("final TypeInfoMap infoMap = prc != null ? getTypeInfoMap() : null;");
            fileContent.add("setEntityState( getEntityState().storeChanges( prc , infoMap ) );");
        }
        else
        {
            fileContent.add("setEntityState( getEntityState().storeChanges() );");
        }
        fileContent.add("setNeedsStoring(false);");
        fileContent.add("success = true;");
        fileContent.endBlock();
        fileContent.add("finally");
        fileContent.startBlock();
        fileContent.add("invalidate(success, false);");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("else");
        fileContent.startBlock();
        fileContent.add("setNeedsStoring(false);");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("");
        for(Iterator<HJMPCreateMethod> iterator3 = getCreateMethods().iterator(); iterator3.hasNext(); )
        {
            HJMPCreateMethod next = iterator3.next();
            fileContent.add("public " + getFullyQualifiedBaseName() + "Remote  create( " + next.getParametersForDeclaration() + " )" + next
                            .getCreateExceptionsForDeclaration());
            fileContent.startBlock();
            fileContent.add("  throw new HJMPException(\"is never called!!\");");
            fileContent.endBlock();
            fileContent.add("public PK ejbCreate( " + next.getParametersForDeclaration() + " )" + next
                            .getCreateExceptionsForDeclaration());
            fileContent.startBlock();
            fileContent.add("try (final RevertibleUpdate theUpdate = updateThread(getEJBOperationInfo()))");
            fileContent.startBlock();
            fileContent.add("ejbCreateNestedCounter++;");
            fileContent.add("if (ejbCreateNestedCounter==1)");
            fileContent.add("{");
            fileContent.add("\tsetEntityState( new " + getEntityStateName() + "( getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment() ) );");
            fileContent.add("}");
            fileContent.add("else");
            fileContent.add("{");
            fileContent.add("\tif (entityState==null) throw new HJMPException( \"no entity state in nested ejbCreate\" );");
            fileContent.add("}");
            fileContent.add("beforeCreate = true;");
            fileContent.add("super.ejbCreate( " + next.getParametersForCall() + " );");
            fileContent.add("if (ejbCreateNestedCounter==1)");
            fileContent.startBlock();
            fileContent.add("boolean success = false;");
            fileContent.add("try");
            fileContent.startBlock();
            fileContent.add("if (LOG||LOG_MODIFICATIONS) /*conv-log*/ log.debug(\"EJB creating \"+" + getPKGetter() + "+\" in \"+getID());");
            if(isExtensibleItem())
            {
                fileContent.add("final EJBPropertyRowCache prc = getModifiedUnlocalizedPropertyCache();");
                fileContent.add("final TypeInfoMap infoMap = prc != null ? getTypeInfoMap() : null;");
                fileContent.add("getEntityState().createEntity( prc, infoMap );");
                fileContent.add("beforeCreate = false;");
                fileContent.add("writePropertyCaches();");
            }
            else
            {
                fileContent.add("getEntityState().createEntity( );");
                fileContent.add("beforeCreate = false;");
                if(isExtensibleItem())
                {
                    fileContent.add("writePropertyCaches();");
                }
            }
            fileContent.add("writeACLEntries();");
            fileContent.add("//setNeedsStoring(false);");
            fileContent.add("success = true;");
            fileContent.endBlock();
            fileContent.add("finally");
            fileContent.startBlock();
            fileContent.add("addLocalRollbackInvalidation(true);");
            fileContent.add("invalidate(success, false);");
            fileContent.add("beforeCreate = false;");
            fileContent.endBlock();
            fileContent.endBlock();
            fileContent.add("else");
            fileContent.add("{");
            fileContent.add("\tif (LOG) /*conv-log*/ log.debug(\"nested ejbCreate in \"+getID());");
            fileContent.add("}");
            fileContent.add("return " + getPKGetter() + ";");
            fileContent.endBlock();
            fileContent.add("finally");
            fileContent.add("{");
            fileContent.add("\tejbCreateNestedCounter--;");
            fileContent.add("}");
            fileContent.endBlock();
            fileContent.add("");
        }
        for(Iterator<HJMPCreateMethod> iterator2 = getCreateMethods().iterator(); iterator2.hasNext(); )
        {
            HJMPCreateMethod next = iterator2.next();
            fileContent.add("public void ejbPostCreate( " + next.getParametersForDeclaration() + " )" + next
                            .getPostCreateExceptionsForDeclaration());
            fileContent.startBlock();
            fileContent.add("super.ejbPostCreate( " + next.getParametersForCall() + " );");
            fileContent.add("if (needsStoring())");
            fileContent.startBlock();
            fileContent.add("try (final RevertibleUpdate theUpdate = updateThread(getEJBOperationInfo()))");
            fileContent.startBlock();
            if(isExtensibleItem())
            {
                fileContent.add("final EJBPropertyRowCache prc = getModifiedUnlocalizedPropertyCache();");
                fileContent.add("final TypeInfoMap infoMap = prc != null ? getTypeInfoMap() : null;");
                fileContent.add("setEntityState( getEntityState().storeChanges(prc, infoMap ) );");
            }
            else
            {
                fileContent.add("setEntityState( getEntityState().storeChanges( ) );");
            }
            fileContent.endBlock();
            fileContent.endBlock();
            fileContent.endBlock();
            fileContent.add("");
        }
        fileContent.add("public void ejbRemove()");
        fileContent.startBlock();
        fileContent.add("loadData();");
        fileContent.add("boolean success = false;");
        fileContent.add("try (final RevertibleUpdate theUpdate = updateThread(getEJBOperationInfo()))");
        fileContent.startBlock();
        fileContent.add("super.ejbRemove();");
        fileContent.add("if (LOG||LOG_MODIFICATIONS) /*conv-log*/ log.debug(\"EJB removing \"+" + getPKGetter() + "+\" in \"+getID());");
        if(isExtensibleItem())
        {
            fileContent.add("removePropertyData();");
        }
        fileContent.add("removeACLEntries();");
        fileContent.add("setEntityState( getEntityState().removeEntity( ) );");
        fileContent.add("success = true;");
        fileContent.endBlock();
        fileContent.add("finally");
        fileContent.startBlock();
        fileContent.add("invalidate(success, true);");
        fileContent.endBlock();
        fileContent.add("clearFields();");
        fileContent.endBlock();
        fileContent.add("public void ejbHomeLoadItemData( final ResultSet rs )");
        fileContent.startBlock();
        fileContent.add("//log.debug( \"got resultset \"+ rs + \" - trying to load cache now\" );");
        fileContent
                        .add("final PK pk = " +
                                        getSimpleBaseName() + "_HJMPWrapper.handleResultRow( getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), rs );");
        fileContent.add("log.debug( \"loaded item \"+pk );");
        fileContent.endBlock();
        fileContent.add("");
        fileContent
                        .add("private static final Collection handleResult( PersistencePool pool, ItemDeployment depl, final ResultSet rs )");
        fileContent.startBlock();
        fileContent.add("try");
        fileContent.startBlock();
        fileContent.add("if( rs.next() )");
        fileContent.startBlock();
        fileContent.add("final Collection result = new ArrayList();");
        fileContent.add("do");
        fileContent.startBlock();
        fileContent.add("result.add( handleResultRow( pool, depl, rs ) );");
        fileContent.endBlock();
        fileContent.add("while( rs.next() );");
        fileContent.add("return result;");
        fileContent.endBlock();
        fileContent.add("else return java.util.Collections.EMPTY_LIST;");
        fileContent.endBlock();
        fileContent.add("catch (SQLException e)");
        fileContent.startBlock();
        fileContent.add("throw new HJMPException( e );");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("");
        fileContent.add("private static final PK handleResultRow( PersistencePool pool, ItemDeployment depl, final ResultSet rs )");
        fileContent.startBlock();
        fileContent.add("try");
        fileContent.startBlock();
        fileContent.add("PK pk = pool.getJDBCValueMappings().PK_READER.getValue( rs, \"PK\" );");
        fileContent.add("new " + getEntityStateName() + "CacheUnit( pool, depl, pk ).hintEntityState(");
        fileContent.add("\tnew " + getEntityStateName() + "( pool, depl, rs )");
        fileContent.add(");");
        fileContent.add("return pk;");
        fileContent.endBlock();
        fileContent.add("catch (SQLException e)");
        fileContent.startBlock();
        fileContent.add("throw new HJMPException( e );");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("// ----- Attributes -----");
        fileContent.add("");
        for(Iterator<ItemDeployment.Attribute> iterator1 = allAttributes.iterator(); iterator1.hasNext(); )
        {
            ItemDeployment.Attribute next = iterator1.next();
            String name = next.getQualifier().substring(0, 1).toUpperCase() + next.getQualifier().substring(0, 1).toUpperCase();
            String type = next.getType();
            Class typeClass = tryToResolveClass(type);
            fileContent.add("public " + type + " get" + name + "()");
            fileContent.startBlock();
            fileContent.add(getEntityStateName() + " entityState = getEntityState();");
            fileContent.add(type + " result = getEntityState().get" + type + "();");
            fileContent.add("if (LOG_ATTRIBUTEACCESS) /*conv-log*/ log.debug(\"getting " + name + " for \"+entityContext.getPK()+\" in " +
                            getSimpleBaseName() + " eid=\"+getID()+\" sid=\"+entityState.getID()+\": \"+result);");
            fileContent.add("return result;");
            fileContent.endBlock();
            fileContent.add("");
            fileContent.add("public void set" + name + "(" + type + " newvalue)");
            fileContent.startBlock();
            fileContent.add(getEntityStateName() + " oldEntityState = getEntityState();");
            fileContent.add(getEntityStateName() + " newEntityState = null;");
            fileContent.add("if( CHECK_VALUE_DIFFERENCES )");
            fileContent.startBlock();
            fileContent.add("final " + type + " oldvalue = oldEntityState.get" + name + "();");
            fileContent.add("if( oldvalue != newvalue " + (
                            !typeClass.isPrimitive() ? "&& ( oldvalue == null || !oldvalue.equals( newvalue ) )" : "") + ")");
            fileContent.startBlock();
            fileContent.add("newEntityState = oldEntityState.set" + name + "(newvalue);");
            fileContent.add("setEntityState( newEntityState );");
            fileContent.endBlock();
            fileContent.endBlock();
            fileContent.add("else");
            fileContent.startBlock();
            fileContent.add("newEntityState = oldEntityState.set" + name + "(newvalue);");
            fileContent.add("setEntityState( newEntityState );");
            fileContent.endBlock();
            fileContent.add("if (LOG_ATTRIBUTEACCESS)");
            fileContent.startBlock();
            fileContent.add("String sidString;");
            fileContent.add("if (oldEntityState==newEntityState)");
            fileContent.startBlock();
            fileContent.add("sidString = \"sid=\"+oldEntityState.getID();");
            fileContent.endBlock();
            fileContent.add("else");
            fileContent.startBlock();
            fileContent.add("sidString = \"oldsid=\"+oldEntityState.getID()+\" newsid=\" + (newEntityState != null ? newEntityState.getID() : \"n/a\");");
            fileContent.endBlock();
            fileContent.add("/*conv-log*/ log.debug(\"setting " + name + " for \"+entityContext.getPK()+\" in " +
                            getSimpleBaseName() + " eid=\"+getID()+\" \"+sidString+\": \"+newvalue);");
            fileContent.endBlock();
            fileContent.endBlock();
            fileContent.add("");
        }
        fileContent.add("public " + getFullyQualifiedBaseName() + "Remote findByPrimaryKey(PK pkValue) throws YObjectNotFoundException, YFinderException");
        fileContent.startBlock();
        fileContent.add("throw new HJMPException(\"never called.\" );");
        fileContent.endBlock();
        fileContent.add("public PK ejbFindByPrimaryKey(PK pkValue) throws YObjectNotFoundException");
        fileContent.startBlock();
        fileContent.add("if (LOG) /*conv-log*/ log.debug( \"" + getSimpleBaseName() + " ejbFindByPrimaryKey \"+pkValue );");
        fileContent.add("try");
        fileContent.startBlock();
        fileContent
                        .add("return " +
                                        getCacheUnitName() + ".getInstance( getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), pkValue ).getEntityState().getPK();");
        fileContent.endBlock();
        fileContent.add("catch(YNoSuchEntityException e)");
        fileContent.startBlock();
        fileContent.add("throw new YObjectNotFoundException(e.getMessage());");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("");
        for(Iterator<ItemDeployment.FinderMethod> iter = this.itemDeployment.getFinderMethods().iterator(); iter.hasNext(); )
        {
            ItemDeployment.FinderMethod next = iter.next();
            List parameterList = getClassListForName(next.getParameterTypes());
            try
            {
                Class[] parameterTypes = (Class[])parameterList.toArray((Object[])new Class[parameterList.size()]);
                HJMPFindMethod findMethod = new HJMPFindMethod(this, next, getHomeClass().getMethod(next.getMethodName(), parameterTypes));
                findMethod.writeToFileContent(fileContent);
            }
            catch(NoSuchMethodException e)
            {
                throw new HJMPGeneratorException("no method " + next
                                .getMethodName() + " in " + getHomeClass().getName() + " found");
            }
        }
        HJMPFindByPKListMethod pkListFinder = new HJMPFindByPKListMethod(this);
        pkListFinder.writeToFileContent(fileContent);
        fileContent.add("");
        fileContent.add("protected String getItemTableNameImpl()");
        fileContent.startBlock();
        fileContent.add("return getEntityContext().getItemDeployment().getDatabaseTableName();");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("public String getOwnJNDIName()");
        fileContent.startBlock();
        fileContent.add("return \"" + getFullyQualifiedBaseName() + "\";");
        fileContent.endBlock();
        fileContent.add("");
        if(isGeneric())
        {
            fileContent.add("protected final int typeCode(){return " + getTypeCode() + ";}");
        }
        if(isExtensibleItem())
        {
            fileContent.add("public String getPropertyTableNameImpl()");
            fileContent.startBlock();
            fileContent.add("return getEntityContext().getItemDeployment().getDumpPropertyTableName();");
            fileContent.endBlock();
            fileContent.add("");
        }
        fileContent.add("@Override");
        fileContent.add("public boolean isBeforeCreate()");
        fileContent.startBlock();
        fileContent.add("return beforeCreate;");
        fileContent.endBlock();
        fileContent.add("protected Object getCachedValueForModification( final ItemCacheKey key )");
        fileContent.startBlock();
        fileContent.add("setEntityState( getEntityState().getModifiedVersion() );");
        fileContent.add("return super.getCachedValueForModification( key );");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("protected Map getCacheKeyMap()");
        fileContent.startBlock();
        fileContent.add("return getEntityState().getCacheKeyMap();");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("private static final class " + getCacheUnitName() + " extends AbstractCacheUnit");
        fileContent.startBlock();
        fileContent.add("static final " +
                        getCacheUnitName() + " getInstance( PersistencePool pool, ItemDeployment depl, PK pk )");
        fileContent.startBlock();
        fileContent.add("return new " + getCacheUnitName() + "( pool, depl, pk );");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("static final Logger log = Logger.getLogger( " + getCacheUnitName() + ".class.getName() );");
        fileContent.add("final PK thePK;");
        fileContent.add("final PersistencePool pool;");
        fileContent.add("final ItemDeployment depl;");
        fileContent.add("");
        fileContent.add(getCacheUnitName() + "( PersistencePool pool, ItemDeployment depl, PK pk )");
        fileContent.add("{");
        fileContent.add("   super( pool.getCache() );");
        fileContent.add("\tthePK = pk;");
        fileContent.add("\tthis.pool = pool;");
        fileContent.add("\tthis.depl = depl;");
        fileContent.add("}");
        fileContent.add("");
        fileContent.add("public Object[] createKey()");
        fileContent.startBlock();
        fileContent.add("return new Object[]");
        fileContent.add("{");
        fileContent.add("\tCache.CACHEKEY_HJMP,");
        fileContent.add("\tCache.CACHEKEY_ENTITY,");
        fileContent.add("\tTYPECODE_STR,");
        fileContent.add("\tthePK");
        fileContent.add("};");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("public int getInvalidationTopicDepth()");
        fileContent.startBlock();
        fileContent.add("return 3;");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("public " + getEntityStateName() + " getEntityState() throws HJMPException");
        fileContent.startBlock();
        fileContent.add("try");
        fileContent.startBlock();
        fileContent.add("return (" + getEntityStateName() + ")get();");
        fileContent.endBlock();
        fileContent.add("catch (final YNoSuchEntityException e)");
        fileContent.startBlock();
        fileContent.add("throw e;");
        fileContent.endBlock();
        fileContent.add("catch (HJMPException e)");
        fileContent.startBlock();
        fileContent.add("throw e;");
        fileContent.endBlock();
        fileContent.add("catch (Exception e)");
        fileContent.startBlock();
        fileContent.add("throw new HJMPException( \"illegal exception type: \"+e.getClass().getName(), e );");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("public void hintEntityState( " + getEntityStateName() + " newState )");
        fileContent.add("{");
        fileContent.add("\tsuper.hintValue( newState );");
        fileContent.add("}");
        fileContent.add("");
        fileContent.add("public Object compute()");
        fileContent.startBlock();
        fileContent.add("if (!pool.isSystemCriticalType(depl.getTypeCode()))");
        fileContent.startBlock();
        fileContent.add("return getFromDatabase();");
        fileContent.endBlock();
        fileContent.add("try (final RevertibleUpdate theUpdate = updateThread(OperationInfo.builder().withCategory(SYSTEM).build()))");
        fileContent.startBlock();
        fileContent.add("return getFromDatabase();");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("private Object getFromDatabase()");
        fileContent.startBlock();
        fileContent.add("if (LOG) /*conv-log*/ log.debug(\"    computing " + getEntityStateName() + "\");");
        fileContent.add("Connection connection = null;");
        fileContent.add("try");
        fileContent.add("{");
        fileContent.add("\tconnection = pool.getDataSource().getConnection();");
        fileContent.add("\treturn new " + getEntityStateName() + "( pool, depl, thePK, connection );");
        fileContent.add("}");
        fileContent.add("catch (YNoSuchEntityException e)");
        fileContent.add("{");
        fileContent.add("\tif (LOG) /*conv-log*/ log.debug(\"    no such entity\");");
        fileContent.add("\tthrow e;");
        fileContent.add("}");
        fileContent.add("catch (SQLException e)");
        fileContent.add("{");
        fileContent.add("\tif (LOG) /*conv-log*/ log.debug(\"    SQLException\");");
        fileContent.add("\tthrow new HJMPException( e );");
        fileContent.add("}");
        fileContent.add("finally");
        fileContent.add("{");
        fileContent.add("\ttry");
        fileContent.add("\t{");
        fileContent.add("\t\tif (connection!=null) connection.close();");
        fileContent.add("\t}");
        fileContent.add("\tcatch (SQLException e)");
        fileContent.add("\t{");
        fileContent.add("\t\tthrow new HJMPException( e );");
        fileContent.add("\t}");
        fileContent.add("}");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("private static final class " + getEntityStateName() + " extends AbstractEntityState");
        fileContent.startBlock();
        fileContent.add("static final Logger log = Logger.getLogger( " + getEntityStateName() + ".class.getName() );");
        fileContent.add("// hjmp timestamp");
        fileContent.add("private long hjmpTS = 0;");
        fileContent.add("private final BitSet changes;");
        fileContent.add("//");
        fileContent.add("private final boolean isTransactionBound;");
        for(int i = 0; i < allAttributes.size(); i++)
        {
            ItemDeployment.Attribute next = allAttributes.get(i);
            String name = next.getQualifier();
            Class type = getClassForName(next.getType());
            fileContent.add("private " + type.getName() + " " + name + ";");
        }
        fileContent.add("private Map cacheKeyMap;");
        fileContent.add("");
        fileContent.add("/**");
        fileContent.add(" * create a new instance, initially not backed by a database entity");
        fileContent.add(" */");
        fileContent.add(getEntityStateName() + "( PersistencePool pool, ItemDeployment depl )");
        fileContent.startBlock();
        fileContent.add("super( pool, depl );");
        fileContent.add("if (LOG) /*conv-log*/ log.debug(\"creating new tx-bound entity state for new " + getSimpleBaseName() + "\"); ");
        fileContent.add("isTransactionBound = true;");
        fileContent.add("changes = new BitSet(" + getNonPKAttributes().size() + ");");
        fileContent.endBlock();
        fileContent.add("/**");
        fileContent.add(" * create a new instance by reading the entity with the given pk from the database");
        fileContent.add(" */");
        fileContent.add(getEntityStateName() + "( PersistencePool pool, ItemDeployment depl, PK pk, Connection connection )");
        fileContent.startBlock();
        fileContent.add("super( pool, depl );");
        fileContent.add("PreparedStatement statement = null;");
        fileContent.add("ResultSet resultSet = null;");
        fileContent.add("try");
        fileContent.startBlock();
        fileContent.add("getPool().verifyTableExistenceIfNeeded(connection, depl);");
        fileContent.add("statement = connection.prepareStatement(\"SELECT * FROM \"+getTable()+\" WHERE " +
                        getPKColumnName() + "=?\");");
        fileContent.add("getPool().getJDBCValueMappings().PK_WRITER.setValue( statement, 1, pk );");
        fileContent.add("resultSet = statement.executeQuery(); ");
        fileContent.add("if ( ! resultSet.next() ) ");
        fileContent.startBlock();
        fileContent.add("resultSet = HJMPUtils.retryMissingPKLookup(resultSet, statement, getPool().getTenant().getConfig());");
        fileContent
                        .add("if (resultSet == null) throw new YNoSuchEntityException( \"Found 0 lines for PK \"+pk+\" in table \"+getTable()+\" of tenant \"+pool.getTenant().getTenantID(), pk );");
        fileContent.endBlock();
        fileContent.add("setStateFromResultSet( resultSet );");
        fileContent.add("//log.debug( \"loaded \"+pk+\" version \"+hjmpTS+\"...\" );");
        fileContent
                        .add("if ( resultSet.next() ) throw new HJMPException( \"Found multiple lines for PK \"+pk+\" in table \"+getTable()+\" of tenant \"+pool.getTenant().getTenantID() );");
        fileContent.add("if ( ! pk.equals(" + getPKFieldName() + ") ) throw new HJMPException( \"found PK \"+" + getPKFieldName() + "+\", expected \"+pk );");
        fileContent.add("isTransactionBound = false;");
        fileContent.add("changes = null;");
        fileContent.endBlock();
        fileContent.add("catch (SQLException e)");
        fileContent.startBlock();
        fileContent.add("\tthrow new HJMPException( e );");
        fileContent.endBlock();
        fileContent.add("finally");
        fileContent.startBlock();
        fileContent.add("HJMPUtils.tryToClose( statement, resultSet );");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("/**");
        fileContent.add(" * create a new instance with data from the current line of the given QueryResult");
        fileContent.add(" */");
        fileContent.add(getEntityStateName() + "( PersistencePool pool, ItemDeployment depl, ResultSet rs )");
        fileContent.startBlock();
        fileContent.add("super( pool, depl );");
        fileContent.add("isTransactionBound = false;");
        fileContent.add("setStateFromResultSet( rs );");
        fileContent.add("changes = null;");
        fileContent.add("//log.debug(\"loaded \"+" + getPKFieldName() + "+\" version \"+hjmpTS+\"...\" );");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add(getEntityStateName() + "( " + getEntityStateName() + " original )");
        fileContent.startBlock();
        fileContent.add("super( original.getPool(), original.getDeployment() );");
        fileContent.add("if (LOG) /*conv-log*/ log.debug(\"creating tx-bound entity state for \"+original.getPK() ); ");
        fileContent.add("changes = new BitSet(" + getNonPKAttributes().size() + ");");
        fileContent.add("hjmpTS = original.hjmpTS;");
        for(Iterator<ItemDeployment.Attribute> iterator5 = allAttributes.iterator(); iterator5.hasNext(); )
        {
            ItemDeployment.Attribute next = iterator5.next();
            String name = next.getQualifier();
            String getMethod = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
            fileContent.add(name + " = original." + name + "();");
        }
        fileContent.add("if (original.cacheKeyMap!=null)");
        fileContent.startBlock();
        fileContent.add("cacheKeyMap = new HashMap();");
        fileContent.add("Iterator iter = original.cacheKeyMap.entrySet().iterator();");
        fileContent.add("while (iter.hasNext())");
        fileContent.startBlock();
        fileContent.add("Map.Entry next = (Map.Entry)iter.next();");
        fileContent.add("Object cacheQualifier = next.getKey();");
        fileContent.add("ItemCacheKey itemCacheKey = ((ItemCacheKey)next.getValue()).getCopy();");
        fileContent.add("cacheKeyMap.put( cacheQualifier, itemCacheKey );");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("isTransactionBound = true;");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("final public String getFullBeanName()");
        fileContent.startBlock();
        fileContent.add("return \"" + getFullyQualifiedBaseName() + "\";");
        fileContent.endBlock();
        fileContent.add("");
        writeSetStateFromResultSet(fileContent);
        fileContent.add("");
        fileContent.add("private final void wroteChanges()");
        fileContent.startBlock();
        fileContent.add("changes.clear();");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("private final boolean hasChangedFields()");
        fileContent.startBlock();
        fileContent.add("return changes!=null && !changes.isEmpty();");
        fileContent.endBlock();
        fileContent.add("private final void markChanged( int pos )");
        fileContent.startBlock();
        fileContent.add("changes.set( pos );");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("private final boolean wasChanged( int pos )");
        fileContent.startBlock();
        fileContent.add("return changes.get( pos );");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("// This is highly questionable since there should actually be no reason for having intermediate");
        fileContent.add("// invalidations right before (!) firing a JDBC update that is followed by a 'official' one a couple");
        fileContent.add("// of lines later!!!");
        fileContent.add("private final void invalidateInternal(final int invalidationType)");
        fileContent.startBlock();
        fileContent.add("// removed obsolete inner invalidation");
        fileContent.add("//final Transaction tx = Transaction.current();");
        fileContent.add("//");
        fileContent.add("//final PK thePK = getPK();");
        fileContent
                        .add("//final Object[] key = new Object[]{ Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, thePK.getTypeCodeAsString(), thePK };");
        fileContent.add("//");
        fileContent.add("//tx.invalidate(key, 3, invalidationType);");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("public PK getPK()");
        fileContent.add("{");
        fileContent.add("\treturn " + getPKGetter() + ";");
        fileContent.add("}");
        fileContent.add("");
        fileContent.add("public " + getEntityStateName() + " getModifiedVersion()");
        fileContent.add("{");
        fileContent.add("\tif (isTransactionBound==true)");
        fileContent.add("\t{");
        fileContent.add("\t\treturn this;");
        fileContent.add("\t}");
        fileContent.add("\telse");
        fileContent.add("\t{");
        fileContent.add("\t\t" + getEntityStateName() + " newState = new " + getEntityStateName() + "( this );");
        fileContent.add("\t\tif (LOG_MODIFICATIONS) /*conv-log*/ log.debug(\"created modified version of \"+toString());");
        fileContent.add("\t\treturn newState;");
        fileContent.add("\t}");
        fileContent.add("}");
        fileContent.add("");
        for(Iterator<ItemDeployment.Attribute> iterator4 = allAttributes.iterator(); iterator4.hasNext(); )
        {
            ItemDeployment.Attribute next = iterator4.next();
            String type = next.getType();
            String name = next.getQualifier();
            String ucName = next.getQualifier().substring(0, 1).toUpperCase() + next.getQualifier().substring(0, 1).toUpperCase();
            fileContent.add("public " + type + " get" + ucName + "()");
            fileContent.startBlock();
            fileContent.add("return " + name + ";");
            fileContent.endBlock();
            fileContent.add("public " + getEntityStateName() + " set" + ucName + "(" + type + " newValue)");
            fileContent.startBlock();
            fileContent.add(getEntityStateName() + " newState = getModifiedVersion();");
            fileContent.add("newState." + name + " = newValue;");
            fileContent.add("newState.markChanged(" + getAttributeNumber(name) + ");");
            fileContent.add("return newState;");
            fileContent.endBlock();
            fileContent.add("");
        }
        if(isExtensibleItem())
        {
            fileContent.add(
                            getEntityStateName() + " storeChanges(  final EJBPropertyRowCache prc, final TypeInfoMap typeInfoMap )");
        }
        else
        {
            fileContent.add(getEntityStateName() + " storeChanges()");
        }
        fileContent.startBlock();
        fileContent.add("Connection connection = null;");
        fileContent.add("PreparedStatement statement = null;");
        fileContent.add("PreparedStatement statement2 = null;");
        fileContent.add("ResultSet rs = null;");
        fileContent.add("try");
        fileContent.startBlock();
        fileContent.add("// make sure we're using master data source before storing");
        fileContent.add("getPool().getTenant().forceMasterDataSource();");
        fileContent.add("// now we should be getting the master data source");
        fileContent.add("if (getPool().getTenant().isSlaveDataSource())");
        fileContent.add("{ throw new IllegalArgumentException(\"got slave data source!\"); }");
        fileContent.add("connection = getPool().getDataSource().getConnection();");
        fileContent.add("if ( ! isTransactionBound ) return this;");
        fileContent.add("");
        fileContent.add("// CHECK for changes first");
        if(isExtensibleItem())
        {
            fileContent.add("final List[] data = prc != null ? PropertyJDBC.getChangeData( connection, typeInfoMap , prc, false ) : null;");
            fileContent.add("if( hasChangedFields() || ( data != null && data.length > 0 ) )");
        }
        else
        {
            fileContent.add("if( hasChangedFields() )");
        }
        fileContent.startBlock();
        fileContent.add("");
        fileContent
                        .add("final boolean optimisticLockingEnabled = getPool().getTenant().getConfig().getBoolean(\"hjmp.throw.concurrent.modification.exceptions\", true);");
        fileContent.add("// increment hjmp timestamp");
        fileContent.add("final long oldTS = hjmpTS++;");
        fileContent.add("//log.debug(\"updating \"+" + getPKFieldName() + "+\" version \"+oldTS+\" to \"+hjmpTS+\" ( state = \"+System.identityHashCode(this) );");
        fileContent.add("// ");
        fileContent.add("final StringBuilder sb = new StringBuilder(");
        fileContent.add("\t\"UPDATE \"+getTable()+\" SET hjmpTS = ? \"); ");
        List<ItemDeployment.Attribute> list = new ArrayList(getNonPKAttributes());
        for(Iterator<ItemDeployment.Attribute> iterator10 = list.iterator(); iterator10.hasNext(); )
        {
            ItemDeployment.Attribute next = iterator10.next();
            String name = next.getQualifier();
            int idx = getAttributeNumber(name);
            fileContent.add("if( wasChanged(" + idx + ") ) sb.append(\",\").append(getColumn(\"" + name + "\")).append(\"=?\" );");
        }
        if(isExtensibleItem())
        {
            fileContent.add("if( data != null )");
            fileContent.startBlock();
            fileContent.add("for( int i = 0, s = data[0].size(); i < s ; i++ )");
            fileContent.startBlock();
            fileContent.add("sb.append(\",\").append((String)data[0].get(i)).append(\"=?\");");
            fileContent.endBlock();
            fileContent.endBlock();
        }
        fileContent.add("sb.append( \" WHERE " + getPKColumnName() + " = ? \" );");
        fileContent.add("if (optimisticLockingEnabled) sb.append(\"AND hjmpTS = ? \");");
        fileContent.add("");
        fileContent.add("statement = connection.prepareStatement( sb.toString() );");
        fileContent.add(getValueWriterCode("statement", 1, long.class, "hjmpTS"));
        fileContent.add("int writeCount = 2; // start at pos 2 since hjmpTS is at 1");
        for(Iterator<ItemDeployment.Attribute> iterator9 = list.iterator(); iterator9.hasNext(); )
        {
            ItemDeployment.Attribute next = iterator9.next();
            String name = next.getQualifier();
            Class type = getClassForName(next.getType());
            int idx = getAttributeNumber(name);
            fileContent.add("if( wasChanged(" + idx + ")) " + getValueWriterCode("statement", "writeCount++", type, name) + ";");
        }
        if(isExtensibleItem())
        {
            fileContent.add("if( data != null )");
            fileContent.startBlock();
            fileContent.add("for( int i = 0, s = data[0].size(); i < s ; i++ )");
            fileContent.startBlock();
            fileContent.add("//log.debug( \"setting unloc property \"+data[0].get(i)+\" to \"+data[1].get(i) );");
            fileContent.add("((ValueWriter)data[2].get(i)).setValue( statement, writeCount++, data[1].get(i) );");
            fileContent.endBlock();
            fileContent.endBlock();
        }
        fileContent
                        .add("getPool().getJDBCValueMappings().PK_WRITER.setValue( statement, writeCount++, " + getPKFieldName() + " );");
        fileContent.add("if( optimisticLockingEnabled ) statement.setLong( writeCount++, oldTS );");
        fileContent.add("");
        fileContent.add("// TODO");
        fileContent.add("invalidateInternal(AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED);");
        fileContent.add("");
        fileContent.add("int modifiedLines = statement.executeUpdate();");
        fileContent.add("//log.debug(\"updating \"+" + getPKFieldName() + "+\" version \"+oldTS+\" to \"+hjmpTS+\" entity state = \" + this+\" modified lines = \" + modifiedLines );");
        fileContent.add("if (modifiedLines!=1 && optimisticLockingEnabled)");
        fileContent.startBlock();
        fileContent.add("statement2 = connection.prepareStatement( \"SELECT hjmpTS FROM \"+getTable()+\" WHERE " +
                        getPKColumnName() + " = ?\" );");
        fileContent.add("getPool().getJDBCValueMappings().PK_WRITER.setValue( statement2, 1, " + getPKFieldName() + " );");
        fileContent.add("//statement2.setString( 1, " + getPKFieldName() + ".toString() );");
        fileContent.add("rs = statement2.executeQuery();");
        fileContent.add("// concurrent modification ?");
        fileContent.add("if( rs.next() )");
        fileContent.startBlock();
        fileContent.add("final long dbTS = rs.getLong(1);");
        fileContent.add("// multiple rows ???");
        fileContent.add("if( rs.next() )");
        fileContent.startBlock();
        fileContent.add("throw new HJMPException( \"item pk \"+" +
                        getPKFieldName() + "+\" exist multiple times in database\" );");
        fileContent.endBlock();
        fileContent.add("else");
        fileContent.startBlock();
        fileContent.add("throw new HJMPException( \"item pk \"+" + getPKFieldName() + "+\" was modified concurrently - expected version \"+oldTS+\" but got \"+dbTS+\", entity state = \" + this );");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("// item doesnt exist !!!");
        fileContent.add("else");
        fileContent.startBlock();
        fileContent.add("throw new HJMPException( \"item \"+" + getPKFieldName() + "+\" doesnt exist in database\" );");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("//Thread.dumpStack();");
        if(isExtensibleItem())
        {
            fileContent.add("if( prc != null ) prc.wroteChanges(true);");
        }
        fileContent.add("wroteChanges();");
        fileContent.add("return this;");
        fileContent.endBlock();
        fileContent.add("catch (SQLException e)");
        fileContent.startBlock();
        fileContent.add("\tthrow new HJMPException(getPool().getDataSource().translateToDataAccessException(e), getDeployment());");
        fileContent.endBlock();
        fileContent.add("finally");
        fileContent.startBlock();
        fileContent.add("HJMPUtils.tryToClose( connection, statement, statement2, rs );");
        fileContent.endBlock();
        fileContent.endBlock();
        if(isExtensibleItem())
        {
            fileContent.add("void createEntity(  final EJBPropertyRowCache prc, final TypeInfoMap typeInfoMap )");
        }
        else
        {
            fileContent.add("private final void createEntity()");
        }
        fileContent.startBlock();
        fileContent.add("Connection connection = null;");
        fileContent.add("PreparedStatement statement = null; ");
        fileContent.add("try");
        fileContent.startBlock();
        fileContent.add("if ( ! isTransactionBound )");
        fileContent.startBlock();
        fileContent.add("throw new HJMPException( \"createEntity in EntityState that is not transaction bound\" );");
        fileContent.endBlock();
        fileContent.add("// make sure we're using master data source before storing");
        fileContent.add("getPool().getTenant().forceMasterDataSource();");
        fileContent.add("// now we should be getting the master data source");
        fileContent.add("if (getPool().getTenant().isSlaveDataSource())");
        fileContent.add("{ throw new IllegalArgumentException(\"got slave data source!\"); }");
        fileContent.add("connection = getPool().getDataSource().getConnection();");
        fileContent.add("//log.debug(\"inserting \"+" + getPKFieldName() + "+\" version \"+hjmpTS+\" ( state = \"+System.identityHashCode(this) );");
        fileContent.add("final StringBuilder sb = new StringBuilder( ");
        fileContent.add("\t\"INSERT INTO \"+getTable()+\" ( \"+");
        fileContent.add("\t\"hjmpTS\");");
        fileContent.add("int columnCount = 1;");
        list = new ArrayList<>(allAttributes);
        for(Iterator<ItemDeployment.Attribute> iterator8 = list.iterator(); iterator8.hasNext(); )
        {
            ItemDeployment.Attribute next = iterator8.next();
            String name = next.getQualifier();
            int idx = getAttributeNumber(name);
            fileContent.add("if( wasChanged(" + idx + "))");
            fileContent.startBlock();
            fileContent.add("sb.append(\",\").append(getColumn(\"" + name + "\"));");
            fileContent.add("columnCount++;");
            fileContent.endBlock();
        }
        if(isExtensibleItem())
        {
            fileContent.add("List[] data = null;");
            fileContent.add("if( prc != null )");
            fileContent.startBlock();
            fileContent.add("data = PropertyJDBC.getChangeData( connection, typeInfoMap, prc, false );");
            fileContent.add("for( int i = 0, s = data[0].size(); i < s ; i++ )");
            fileContent.startBlock();
            fileContent.add("sb.append(\",\").append((String)data[0].get(i));");
            fileContent.add("columnCount++;");
            fileContent.endBlock();
            fileContent.endBlock();
        }
        fileContent.add("sb.append( \" ) VALUES (\" );");
        fileContent.add("for( int i = 0; i < columnCount ; i++ )");
        fileContent.startBlock();
        fileContent.add("sb.append(i==0?\"?\":\",?\");");
        fileContent.endBlock();
        fileContent.add("sb.append(\")\" );");
        fileContent.add("statement = connection.prepareStatement( sb.toString() );");
        writeValueWriterCode(fileContent, "statement", 1, long.class, "hjmpTS");
        fileContent.add("int writeCount = 2; // start with 2 since hjmpTS is at 1");
        for(Iterator<ItemDeployment.Attribute> iterator7 = list.iterator(); iterator7.hasNext(); )
        {
            ItemDeployment.Attribute next = iterator7.next();
            String name = next.getQualifier();
            Class type = getClassForName(next.getType());
            int idx = getAttributeNumber(name);
            fileContent.add("if( wasChanged(" + idx + ")) ");
            fileContent.startBlock();
            fileContent.add(getValueWriterCode("statement", "writeCount++", type, name) + ";");
            fileContent.endBlock();
        }
        if(isExtensibleItem())
        {
            fileContent.add("if( data != null )");
            fileContent.startBlock();
            fileContent.add("for( int i = 0, s = data[0].size(); i < s ; i++ )");
            fileContent.startBlock();
            fileContent.add("((ValueWriter)data[2].get(i)).setValue( statement, writeCount++, data[1].get(i) );");
            fileContent.endBlock();
            fileContent.endBlock();
        }
        fileContent.add("");
        fileContent.add("// TODO");
        fileContent.add("invalidateInternal(AbstractCacheUnit.INVALIDATIONTYPE_MODIFIED);");
        fileContent.add("");
        fileContent.add("int modifiedLines = statement.executeUpdate();");
        fileContent.add("if (modifiedLines!=1 ) throw new HJMPException( \"unexpected insert count: \"+modifiedLines );");
        fileContent.add("//log.debug(\"inserted \"+" + getPKFieldName() + "+\" version \"+hjmpTS );");
        if(isExtensibleItem())
        {
            fileContent.add("if( prc != null ) prc.wroteChanges(true);");
        }
        fileContent.add("wroteChanges();");
        fileContent.add("getPool().notifyEntityCreation( this.pkString );");
        fileContent.endBlock();
        fileContent.add("catch (SQLException e)");
        fileContent.startBlock();
        fileContent.add("\tthrow new HJMPException(getPool().getDataSource().translateToDataAccessException(e), getDeployment());");
        fileContent.endBlock();
        fileContent.add("finally");
        fileContent.startBlock();
        fileContent.add("HJMPUtils.tryToClose( connection, statement );");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add(getEntityStateName() + " removeEntity()");
        fileContent.startBlock();
        fileContent.add("Connection connection = null;");
        fileContent.add("PreparedStatement statement = null;");
        fileContent.add("try");
        fileContent.startBlock();
        fileContent.add("// make sure we're using master data source before storing");
        fileContent.add("getPool().getTenant().forceMasterDataSource();");
        fileContent.add("// now we should be getting the master data source");
        fileContent.add("if (getPool().getTenant().isSlaveDataSource())");
        fileContent.add("{ throw new IllegalArgumentException(\"got slave data source!\"); }");
        fileContent.add("connection = getPool().getDataSource().getConnection();");
        fileContent.add("statement = connection.prepareStatement(");
        fileContent.add("\t\"DELETE FROM \"+getTable()+\" WHERE " + getPKColumnName() + " = ?\"");
        fileContent.add(");");
        fileContent.add("getPool().getJDBCValueMappings().PK_WRITER.setValue( statement, 1, " + getPKGetter() + " );");
        fileContent.add("//statement.setString( 1, " + getPKGetter() + ".toString() );");
        fileContent.add(getEntityStateName() + " newState = getModifiedVersion();");
        fileContent.add("");
        fileContent.add("// TODO");
        fileContent.add("invalidateInternal(AbstractCacheUnit.INVALIDATIONTYPE_REMOVED);");
        fileContent.add("");
        fileContent.add("int modifiedLines = statement.executeUpdate();");
        fileContent
                        .add("if (modifiedLines!=1 && Config.getBoolean( \"hjmp.throw.concurrent.modification.exceptions\" ,true)) throw new HJMPException( \"unexpected modification count for deletion of \"+" +
                                        getPKGetter() + "+\": \"+modifiedLines );");
        fileContent.add("return newState;");
        fileContent.endBlock();
        fileContent.add("catch (SQLException e)");
        fileContent.startBlock();
        fileContent.add("\tthrow new HJMPException( e );");
        fileContent.endBlock();
        fileContent.add("finally");
        fileContent.startBlock();
        fileContent.add("HJMPUtils.tryToClose( connection, statement );");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("Map getCacheKeyMap()");
        fileContent.startBlock();
        fileContent.add("if (LOG)");
        fileContent.startBlock();
        fileContent.add("/*conv-log*/ log.debug(\"" + getEntityStateName() + " getCacheKeyMap -> \"+Integer.toHexString(System.identityHashCode(cacheKeyMap))+\" \"+cacheKeyMap );");
        fileContent.add("if (cacheKeyMap!=null)");
        fileContent.add("{");
        fileContent.add("\tIterator iter = cacheKeyMap.entrySet().iterator();");
        fileContent.add("\twhile (iter.hasNext())");
        fileContent.add("\t{");
        fileContent.add("\t\tMap.Entry next = (Map.Entry)iter.next();");
        fileContent.add("\t\t/*conv-log*/ log.debug(\"\t\"+next.getKey()+\"->\"+next.getValue() );");
        fileContent.add("\t}");
        fileContent.add("}");
        fileContent.endBlock();
        fileContent.add("if (cacheKeyMap==null)");
        fileContent.startBlock();
        fileContent.add("if (LOG) /*conv-log*/ log.debug(\"\tcreating new map\");");
        fileContent.add("cacheKeyMap = new java.util.concurrent.ConcurrentHashMap();");
        fileContent.endBlock();
        fileContent.add("return cacheKeyMap;");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("private String getID()");
        fileContent.startBlock();
        fileContent.add("return Integer.toHexString( System.identityHashCode(this) );");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("public String toString()");
        fileContent.startBlock();
        fileContent.add("return \"" + getSimpleBaseName() + "EntityState(PK=\"+getPK()+\",txbound=\"+isTransactionBound+\",sid=\"+getID()+\",hjmpTS\"+hjmpTS+\")\";");
        fileContent.endBlock();
        fileContent.add("public String toDetailedString()");
        fileContent.startBlock();
        fileContent.add("return \"" + getCacheUnitName() + "(\"");
        fileContent.add("\t+\"isTransactionBound=\"+isTransactionBound+\" \"");
        fileContent.add("\t+\"sid=\"+getID()+\" \"");
        for(Iterator<ItemDeployment.Attribute> iterator6 = allAttributes.iterator(); iterator6.hasNext(); )
        {
            ItemDeployment.Attribute nextAttribute = iterator6.next();
            String qualifier = nextAttribute.getQualifier();
            fileContent.add("\t+\"" + qualifier + "=\"+" + qualifier + "+\" \"");
        }
        fileContent.add("\t+\")\";");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.assertBlocksClosed();
        insertAccessDefinitionBlock(accessInsertPos, accessIndent, fileContent);
        return fileContent.getLines();
    }


    private void writeSetStateFromResultSet(JavaFile fileContent) throws ClassNotFoundException
    {
        List<ItemDeployment.Attribute> attributes = getAllAttributes();
        fileContent.add("private final void setStateFromResultSet( final ResultSet rs )");
        fileContent.startBlock();
        fileContent.add("try");
        fileContent.startBlock();
        fileContent.add("this.hjmpTS = rs.getLong( \"hjmpTS\" );");
        for(int i = 0; i < attributes.size(); i++)
        {
            ItemDeployment.Attribute next = attributes.get(i);
            String name = next.getQualifier();
            Class type = getClassForName(next.getType());
            writeValueReaderCode(fileContent, "rs", type, name);
        }
        if(isExtensibleItem() && !isMetaInformationItem())
        {
            fileContent.add("//PK pk = PK.parse( pkString );");
            fileContent.add("//PK typePK = PK.parse( typePkString );");
            fileContent
                            .add("final TypeInfoMap tim = (typePkString!=null&&(!pkString.equals(typePkString)||de.hybris.platform.core.Registry.getPersistenceManager().cachesInfoFor(typePkString))) ? de.hybris.platform.core.Registry.getPersistenceManager().getPersistenceInfo(typePkString):null;");
            fileContent.add("if( tim != null && tim.tablesInitialized() && tim.hasInfos( false ) )");
            fileContent.startBlock();
            fileContent
                            .add("final EJBPropertyRowCache prc = PropertyJDBC.readPropertyRow( rs, pkString, typePkString, null, propertyTimestampInternal, tim );");
            fileContent.add("getCacheKeyMap().put( ItemPropertyCacheKey.QUALI , new ItemPropertyCacheKey( prc, pkString ) );");
            fileContent.endBlock();
        }
        fileContent.endBlock();
        fileContent.add("catch (SQLException e)");
        fileContent.startBlock();
        fileContent.add("throw new HJMPException( e );");
        fileContent.endBlock();
        fileContent.endBlock();
    }


    private void writeValueReaderCode(JavaFile fileContent, String resultsetName, Class type, String name)
    {
        String base = null;
        if(type.equals(boolean.class))
        {
            base = "((ValueReader)" + getAccess(type) + "[READER]).getBoolean(" + resultsetName + ",getColumn(\"" + name + "\"))";
            fileContent.add(name + " = " + name + ";");
        }
        else if(type.equals(long.class))
        {
            base = "((ValueReader)" + getAccess(type) + "[READER]).getLong(" + resultsetName + ",getColumn(\"" + name + "\"))";
            fileContent.add(name + " = " + name + ";");
        }
        else if(type.equals(double.class))
        {
            base = "((ValueReader)" + getAccess(type) + "[READER]).getDouble(" + resultsetName + ",getColumn(\"" + name + "\"))";
            fileContent.add(name + " = " + name + ";");
        }
        else if(type.equals(int.class))
        {
            base = "((ValueReader)" + getAccess(type) + "[READER]).getInt(" + resultsetName + ",getColumn(\"" + name + "\"))";
            fileContent.add(name + " = " + name + ";");
        }
        else if(type.equals(float.class))
        {
            base = "((ValueReader)" + getAccess(type) + "[READER]).getFloat(" + resultsetName + ",getColumn(\"" + name + "\"))";
            fileContent.add(name + " = " + name + ";");
        }
        else if(type.equals(byte.class))
        {
            base = "((ValueReader)" + getAccess(type) + "[READER]).getByte(" + resultsetName + ",getColumn(\"" + name + "\"))";
            fileContent.add(name + " = " + name + ";");
        }
        else if(type.equals(char.class))
        {
            base = "((ValueReader)" + getAccess(type) + "[READER]).getChar(" + resultsetName + ",getColumn(\"" + name + "\"))";
            fileContent.add(name + " = " + name + ";");
        }
        else if(type.equals(short.class))
        {
            base = "((ValueReader)" + getAccess(type) + "[READER]).getShort(" + resultsetName + ",getColumn(\"" + name + "\"))";
            fileContent.add(name + " = " + name + ";");
        }
        else
        {
            fileContent.add(name + " = (" + name + ")((ValueReader)" + type.getName() + "[READER]).getValue(" + getAccess(type) + ",getColumn(\"" + resultsetName + "\"));");
        }
    }


    protected final String getValueWriterCode(String statementName, int statementPos, Class type, String name)
    {
        String setter = type.isPrimitive() ? "setPrimitive" : "setValue";
        return "((ValueWriter)" + getAccess(type) + "[WRITER])." + setter + "(" + statementName + "," + statementPos + ", this." + name + ");";
    }


    protected final String getValueWriterCode(String statementName, String statementPosExpr, Class type, String name)
    {
        String setter = type.isPrimitive() ? "setPrimitive" : "setValue";
        return "((ValueWriter)" + getAccess(type) + "[WRITER])." + setter + "(" + statementName + "," + statementPosExpr + ", this." + name + ")";
    }


    private void writeValueWriterCode(JavaFile fileContent, String statementName, int statementPos, Class type, String name)
    {
        fileContent.add(getValueWriterCode(statementName, statementPos, type, name));
    }


    private static List getClassListForName(List classNames) throws ClassNotFoundException
    {
        List<Class<?>> classList = new ArrayList();
        for(Iterator<String> it = classNames.iterator(); it.hasNext(); )
        {
            classList.add(getClassForName(it.next()));
        }
        return classList;
    }


    private static Class getClassForName(String className) throws ClassNotFoundException
    {
        if(primiveTypes.containsKey(className))
        {
            return (Class)primiveTypes.get(className);
        }
        return Class.forName(className, false, HJMPEntityBean.class.getClassLoader());
    }
}
