package de.hybris.platform.persistence.hjmpgen;

import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

class HJMPFindMethod
{
    static final Map OBJECT_WRAPPERS;
    final HJMPEntityBean theContainingBean;
    final ItemDeployment.FinderMethod deploymentMethod;
    final Method homeMethod;

    static
    {
        Map<Object, Object> map = new HashMap<>();
        map.put(boolean.class, Boolean.class);
        map.put(char.class, Character.class);
        map.put(byte.class, Byte.class);
        map.put(short.class, Short.class);
        map.put(int.class, Integer.class);
        map.put(long.class, Long.class);
        map.put(float.class, Float.class);
        map.put(double.class, Double.class);
        OBJECT_WRAPPERS = Collections.unmodifiableMap(map);
    }

    final List bindings = new ArrayList();
    final List valueWriterReader = new ArrayList();


    public HJMPFindMethod(HJMPEntityBean containingBean, ItemDeployment.FinderMethod newDeploymentMethod, Method newHomeMethod)
    {
        this.theContainingBean = containingBean;
        this.deploymentMethod = newDeploymentMethod;
        this.homeMethod = newHomeMethod;
        if(this.deploymentMethod != null && this.homeMethod != null && !this.deploymentMethod.getMethodName().equals(this.homeMethod.getName()))
        {
            throw new RuntimeException("name mismatch: " + this.deploymentMethod.getMethodName() + ", " + this.homeMethod.getName());
        }
    }


    public HJMPEntityBean getContainingBean()
    {
        return this.theContainingBean;
    }


    private Class getReturnType()
    {
        if(isReturningCollection())
        {
            return Collection.class;
        }
        return PK.class;
    }


    private Class getRemoteReturnType()
    {
        if(isReturningCollection())
        {
            return Collection.class;
        }
        try
        {
            return Class.forName(getContainingBean().getFullyQualifiedBaseName() + "Remote", false, HJMPFindMethod.class
                            .getClassLoader());
        }
        catch(ClassNotFoundException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    private String getEJBFindName()
    {
        String homeFindName = getHomeFindName();
        return "ejb" + homeFindName.substring(0, 1).toUpperCase() + homeFindName.substring(1);
    }


    protected String getFinderClassName()
    {
        String homeFindName = getHomeFindName();
        return homeFindName.substring(0, 1).toUpperCase() + homeFindName.substring(0, 1).toUpperCase() + homeFindName.substring(1) + "FinderResult";
    }


    protected String getHomeFindName()
    {
        return this.homeMethod.getName();
    }


    protected int getParameterCount()
    {
        return (this.homeMethod.getParameterTypes()).length;
    }


    protected Class getParameterType(int i)
    {
        return this.homeMethod.getParameterTypes()[i];
    }


    private String getParameterName(int i)
    {
        return "param" + i;
    }


    private String getParameterDeclaration()
    {
        StringBuilder parameters = new StringBuilder();
        for(int i = 0; i < getParameterCount(); i++)
        {
            if(i != 0)
            {
                parameters.append(", ");
            }
            parameters.append(getParameterType(i).getName() + " " + getParameterType(i).getName());
        }
        return parameters.toString();
    }


    private String getParametersForCall()
    {
        StringBuilder parameters = new StringBuilder();
        for(int i = 0; i < getParameterCount(); i++)
        {
            if(i != 0)
            {
                parameters.append(", ");
            }
            parameters.append(getParameterName(i));
        }
        return parameters.toString();
    }


    private String getWrappedParametersForCall()
    {
        StringBuilder parameters = new StringBuilder();
        for(int i = 0; i < getParameterCount(); i++)
        {
            if(i != 0)
            {
                parameters.append(", ");
            }
            Class objectWrapper = (Class)OBJECT_WRAPPERS.get(getParameterType(i));
            if(objectWrapper == null)
            {
                parameters.append(getParameterName(i));
            }
            else
            {
                parameters.append("new " + objectWrapper.getName() + "( " + getParameterName(i) + " )");
            }
        }
        return parameters.toString();
    }


    protected String getExceptionDeclaration()
    {
        return " throws YFinderException" + (isReturningCollection() ? "" : ", YObjectNotFoundException");
    }


    protected boolean isReturningCollection()
    {
        return this.homeMethod.getReturnType().equals(Collection.class);
    }


    protected boolean shouldBeCached()
    {
        return this.deploymentMethod.shouldBeCached();
    }


    private static final Class[] LITERAL_NUMBER_PARAM_TYPES = new Class[] {Number.class, int.class, double.class, long.class, byte.class, float.class};


    private static final boolean isLiteralNumberType(Class<?> type)
    {
        for(int i = 0; i < LITERAL_NUMBER_PARAM_TYPES.length; i++)
        {
            if(LITERAL_NUMBER_PARAM_TYPES[i].isAssignableFrom(type))
            {
                return true;
            }
        }
        return false;
    }


    private String translateMethodMapping(String mapping, boolean useLiteralsWherePossible)
    {
        String withColumnNames = replaceAttributes(mapping);
        String withTableNames = replaceTableName(withColumnNames);
        StringBuilder buffer = new StringBuilder(withTableNames);
        if(StringUtils.isNotBlank(mapping))
        {
            buffer.insert(0, " WHERE ");
        }
        int variableCount = 0;
        int searchIndex = 0;
        int index;
        while(-1 != (index = buffer.toString().indexOf('$', searchIndex)))
        {
            if(buffer.length() > index && Character.isDigit(buffer.charAt(index + 1)))
            {
                variableCount++;
                int endIndex = index + 1;
                while(endIndex < buffer.length() && Character.isDigit(buffer.charAt(endIndex)))
                {
                    endIndex++;
                }
                int parameterIndex = Integer.parseInt(buffer.substring(index + 1, endIndex)) - 1;
                if(parameterIndex >= getParameterCount())
                {
                    throw new HJMPGeneratorException("illegal parameter index " + parameterIndex + 1 + " in " + mapping);
                }
                Class<?> parameterType = getParameterType(parameterIndex);
                String parameterName = getParameterName(parameterIndex);
                if(useLiteralsWherePossible)
                {
                    if(String.class.isAssignableFrom(parameterType))
                    {
                        buffer.replace(index, endIndex, "'\"+quoteSQLStringLiteralForQuery(" + parameterName + ")+\"'");
                    }
                    else if(boolean.class.isAssignableFrom(parameterType))
                    {
                        buffer.replace(index, endIndex, "\"+(" + parameterName + "?1:0)+\"");
                    }
                    else if(Boolean.class.isAssignableFrom(parameterType))
                    {
                        buffer.replace(index, endIndex, "\"+(java.lang.Boolean.TRUE.equals(" + parameterName + ")?1:0)+\"");
                    }
                    else if(isLiteralNumberType(parameterType))
                    {
                        buffer.replace(index, endIndex, "\"+" + parameterName + "+\"");
                    }
                    else
                    {
                        buffer.replace(index, endIndex, "?");
                        this.bindings.add("set" + HJMPEntityBean.getJDBCAccessorFor(parameterType) + "( " + variableCount + ", " +
                                        HJMPEntityBean.getJDBCValueFor(parameterType, parameterName) + " )");
                        this.valueWriterReader.add(
                                        getContainingBean().getValueWriterCode("(PreparedStatement)statement", variableCount, parameterType, parameterName));
                    }
                }
                else
                {
                    buffer.replace(index, endIndex, "?");
                    this.bindings.add("set" + HJMPEntityBean.getJDBCAccessorFor(parameterType) + "( " + variableCount + ", " +
                                    HJMPEntityBean.getJDBCValueFor(parameterType, parameterName) + " )");
                    this.valueWriterReader.add(getContainingBean().getValueWriterCode("(PreparedStatement)statement", variableCount, parameterType, parameterName));
                }
            }
            searchIndex = index + 1;
        }
        return buffer.toString();
    }


    private void buildSQL(JavaFile fileContent, String statementName)
    {
        if(!this.bindings.isEmpty())
        {
            throw new HJMPGeneratorException("bindings not empty:" + this.bindings + "; possibly second call to buildSQLString?");
        }
        fileContent.add("String sql = \"" + translateMethodMapping(this.deploymentMethod.getDefaultMethodMapping(), false) + "\";");
        boolean first = true;
        for(Iterator<Map.Entry> it = this.deploymentMethod.getAdditionalMethodMappings().entrySet().iterator(); it.hasNext(); )
        {
            if(first)
            {
                fileContent.add("final String db = de.hybris.platform.util.Config.getDatabase();");
            }
            Map.Entry e = it.next();
            fileContent.add((!first ? "else " : "") + "if( db.equalsIgnoreCase(\"" + (!first ? "else " : "") + "\")) sql = \"" + (String)e.getKey() + "\";");
            first = false;
        }
        if(this.valueWriterReader.isEmpty())
        {
            fileContent.add(statementName + " = connection.createStatement();");
            fileContent.add("sql = \"SELECT * FROM \"+getTable()+\" \"+sql;");
        }
        else
        {
            fileContent.add(statementName + " = connection.prepareStatement(");
            fileContent.add("\t\"SELECT * FROM \"+getTable()+\" \"+sql");
            fileContent.add(");");
        }
    }


    private String replaceAttributes(String s)
    {
        StringBuilder buffer = new StringBuilder(s);
        List<?> attributes = new ArrayList(getContainingBean().getAllAttributes());
        Collections.sort(attributes, (Comparator<?>)new Object(this));
        Iterator<?> iter = attributes.iterator();
        while(iter.hasNext())
        {
            ItemDeployment.Attribute next = (ItemDeployment.Attribute)iter.next();
            String qualifier = "$" + next.getQualifier();
            replaceInStringBuilder(buffer, qualifier, "\"+getColumn(\"" + next.getQualifier() + "\")+\"");
        }
        return buffer.toString();
    }


    private void replaceInStringBuilder(StringBuilder buffer, String original, String replacement)
    {
        int index;
        while(-1 != (index = buffer.toString().indexOf(original)))
        {
            buffer.replace(index, index + original.length(), replacement);
        }
    }


    private String replaceTableName(String s)
    {
        StringBuilder buffer = new StringBuilder(s);
        replaceInStringBuilder(buffer, "$$", "\"+getTable()+\"");
        return buffer.toString();
    }


    public void writeToFileContent(JavaFile fileContent)
    {
        fileContent.add("public static final class " + getFinderClassName() + " extends FinderResult");
        fileContent.startBlock();
        fileContent.add("");
        for(int i = 0; i < getParameterCount(); i++)
        {
            fileContent.add("private " + getParameterType(i).getName() + " " + getParameterName(i) + ";");
        }
        fileContent.add("static final Logger log = Logger.getLogger( " + getFinderClassName() + ".class.getName() );");
        fileContent.add("");
        String paramDecl = getParameterDeclaration();
        if(StringUtils.isNotBlank(paramDecl))
        {
            paramDecl = "," + paramDecl;
        }
        fileContent.add(getFinderClassName() + "( PersistencePool pool, ItemDeployment depl " + getFinderClassName() + " )");
        fileContent.startBlock();
        fileContent.add("super( pool,depl, \"" + getContainingBean().getTypeCode() + "\", \"" + getEJBFindName() + "\", new Object[]{" +
                        getWrappedParametersForCall() + "} );");
        int j;
        for(j = 0; j < getParameterCount(); j++)
        {
            fileContent.add("this." + getParameterName(j) + " = " + getParameterName(j) + ";");
        }
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add(getFinderClassName() + "( PersistencePool pool, ItemDeployment depl " + getFinderClassName() + ", Object cacheValue)");
        fileContent.startBlock();
        fileContent.add("super( pool,depl, \"" + getContainingBean().getTypeCode() + "\", \"" + getEJBFindName() + "\", new Object[]{" +
                        getWrappedParametersForCall() + "} );");
        for(j = 0; j < getParameterCount(); j++)
        {
            fileContent.add("this." + getParameterName(j) + " = " + getParameterName(j) + ";");
        }
        fileContent.add("hintValue(cacheValue);");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("public final boolean isCachingSupported()");
        fileContent.startBlock();
        fileContent.add("return " + Boolean.toString(shouldBeCached()) + ";");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("public final Object getFinderResult() throws SQLException, YFinderException");
        fileContent.startBlock();
        fileContent.add("try");
        fileContent.add("{");
        fileContent.add("\treturn super.get();");
        fileContent.add("}");
        fileContent.add("catch (SQLException e)");
        fileContent.add("{");
        fileContent.add("\tthrow e;");
        fileContent.add("}");
        fileContent.add("catch (YFinderException e)");
        fileContent.add("{");
        fileContent.add("\tthrow e;");
        fileContent.add("}");
        fileContent.add("catch (RuntimeException e)");
        fileContent.add("{");
        fileContent.add("\tthrow e;");
        fileContent.add("}");
        fileContent.add("catch (Exception e)");
        fileContent.add("{");
        fileContent.add("\tthrow new RuntimeException( e );");
        fileContent.add("}");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("public Object compute() throws SQLException, YFinderException");
        fileContent.startBlock();
        fileContent.add("if (!pool.isSystemCriticalType(" + getContainingBean().getTypeCode() + "))");
        fileContent.startBlock();
        fileContent.add("return getFromDatabase();");
        fileContent.endBlock();
        fileContent.add("try (final RevertibleUpdate theUpdate = updateThread(OperationInfo.builder().withCategory(SYSTEM).build()))");
        fileContent.startBlock();
        fileContent.add("return getFromDatabase();");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("private Object getFromDatabase() throws SQLException, YFinderException");
        fileContent.startBlock();
        fileContent.add("if (LOG) /*conv-log*/ log.debug(\"    computing " + getFinderClassName() + "\");");
        fileContent.add("Connection connection = null;");
        fileContent.add("Statement statement = null;");
        fileContent.add("ResultSet resultSet = null;");
        fileContent.add("try");
        fileContent.startBlock();
        fileContent.add("connection = pool.getDataSource().getConnection();");
        buildSQL(fileContent, "statement");
        boolean dontPrepare = this.valueWriterReader.isEmpty();
        Iterator<String> iter = this.bindings.iterator();
        fileContent.add("/*");
        while(iter.hasNext())
        {
            String next = iter.next();
            fileContent.add("statement." + next + ";");
        }
        fileContent.add("*/");
        Iterator<String> iter2 = this.valueWriterReader.iterator();
        while(iter2.hasNext())
        {
            String next = iter2.next();
            fileContent.add(next);
        }
        if(dontPrepare)
        {
            fileContent.add("resultSet = statement.executeQuery( sql );");
        }
        else
        {
            fileContent.add("resultSet = ((PreparedStatement)statement).executeQuery();");
        }
        fileContent.add("Collection result = " + getContainingBean().getSimpleBaseName() + "_HJMPWrapper.handleResult(pool,getDeployment(),resultSet);");
        fileContent.add("if (LOG) /*conv-log*/ log.debug(\"    found \"+result);");
        if(isReturningCollection())
        {
            fileContent.add("return result;");
        }
        else
        {
            fileContent.add("switch (result.size())");
            fileContent.startBlock();
            fileContent.add("case 0:");
            fileContent.add("\treturn null;");
            fileContent.add("case 1:");
            fileContent.add("\treturn result.iterator().next();");
            fileContent.add("default:");
            fileContent.add("\tthrow new HJMPFinderException( \"many results for " + getHomeFindName() + ": \"+result );");
            fileContent.endBlock();
        }
        fileContent.endBlock();
        fileContent.add("finally");
        fileContent.add("{");
        fileContent.add("\tHJMPUtils.tryToClose( connection, statement, resultSet );");
        fileContent.add("}");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("public " + getRemoteReturnType().getName() + " " + getHomeFindName() + "( " + getParameterDeclaration() + " )" +
                        getExceptionDeclaration());
        fileContent.startBlock();
        fileContent.add("   return (" + getRemoteReturnType().getName() + ")de.hybris.platform.util.EJBTools.convertEntityFinderResult(" +
                        getEJBFindName() + "( " + getParametersForCall() + "), getEntityContext().getPersistencePool().getTenant().getSystemEJB() );");
        fileContent.endBlock();
        fileContent.add("public " + getReturnType().getName() + " " + getEJBFindName() + "( " + getParameterDeclaration() + " )" +
                        getExceptionDeclaration());
        fileContent.startBlock();
        fileContent.add("try");
        fileContent.startBlock();
        fileContent.add("if (LOG) /*conv-log*/ log.debug( \"" + getContainingBean().getSimpleBaseName() + " " + getEJBFindName() + "\" );");
        for(int k = 0; k < getParameterCount(); k++)
        {
            fileContent.add("if (LOG) /*conv-log*/ log.debug(\"  " + k + "->\"+" + getParameterName(k) + ");");
        }
        fileContent.add("Object resultObject = new " + getFinderClassName() + "( getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment()" + (
                        (getParametersForCall().trim().length() == 0) ? "" : ("," + getParametersForCall())) + " ).getFinderResult();");
        fileContent.add("if (LOG) /*conv-log*/ log.debug(\"  result=\"+resultObject);");
        if(!isReturningCollection())
        {
            fileContent.add("if (resultObject==null) throw new YObjectNotFoundException();");
        }
        fileContent.add("return (" + getReturnType().getName() + ")resultObject;");
        fileContent.endBlock();
        fileContent.add("catch (SQLException e)");
        fileContent.startBlock();
        fileContent.add("throw new HJMPFinderException( e );");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("");
    }
}
