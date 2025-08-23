package de.hybris.platform.persistence.hjmpgen;

import de.hybris.platform.core.PK;
import java.util.Collection;

class HJMPFindByPKListMethod extends HJMPFindMethod
{
    public HJMPFindByPKListMethod(HJMPEntityBean containingBean)
    {
        super(containingBean, null, null);
    }


    protected final String getHomeFindName()
    {
        return "findByPKList";
    }


    protected boolean isReturningCollection()
    {
        return true;
    }


    protected final int getParameterCount()
    {
        return 1;
    }


    protected Class getParameterType(int i)
    {
        return Collection.class;
    }


    protected boolean shouldBeCached()
    {
        return true;
    }


    public void writeToFileContent(JavaFile fileContent)
    {
        fileContent.add("static final class FindByPKListFinderResult extends FinderResult");
        fileContent.startBlock();
        fileContent.add("");
        fileContent.add("private java.util.Collection<PK> pks;");
        fileContent.add("static final Logger log = Logger.getLogger( FindByPKListFinderResult.class.getName() );");
        fileContent.add("");
        fileContent.add("FindByPKListFinderResult( PersistencePool pool, ItemDeployment depl, java.util.Collection pks )");
        fileContent.startBlock();
        fileContent.add("super( pool,depl, \"" + getContainingBean().getTypeCode() + "\", \"ejbFindByPKList\", new Object[]{pks} );");
        fileContent.add("this.pks = pks;");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("public boolean isCachingSupported()");
        fileContent.startBlock();
        fileContent.add("return " + Boolean.toString(shouldBeCached()) + ";");
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("public Object getFinderResult() throws SQLException, YFinderException");
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
        fileContent.add("final Collection ret = new ArrayList( pks.size() );");
        fileContent.add("Connection connection = null;");
        fileContent.add("Statement statement = null;");
        fileContent.add("ResultSet resultSet = null;");
        fileContent.add("try");
        fileContent.startBlock();
        fileContent.add("connection = pool.getDataSource().getConnection();");
        fileContent.add("int pageSize = pool.getDataSource().getMaxPreparedParameterCount();");
        fileContent.add("if( pageSize == -1 ) pageSize = pks.size();");
        fileContent.add("int offset = 0;");
        fileContent.add("final List<PK> pkList = new ArrayList<PK>( pks );");
        fileContent.add("while( offset < pkList.size() )");
        fileContent.startBlock();
        fileContent.add("int currentPageEnd = Math.min( pkList.size(), offset + pageSize );");
        fileContent.add("final StringBuilder sb = new StringBuilder();");
        fileContent.add("sb.append(\"SELECT * FROM \").append(getTable()).append(\" WHERE PK IN ( \");");
        fileContent.add("// insert ?'s");
        fileContent.add("for( int i = 0, s = currentPageEnd - offset; i < s ; i++ )");
        fileContent.startBlock();
        fileContent.add("sb.append( i > 0 ? \",\" : \"\" ).append(\"?\");");
        fileContent.endBlock();
        fileContent.add("sb.append(\")\");");
        fileContent.add("statement = connection.prepareStatement(sb.toString());");
        fileContent.add("// now bind parameters");
        fileContent.add("int paramPos = 1;");
        fileContent.add("for( int i = offset; i < currentPageEnd ; i++ )");
        fileContent.startBlock();
        fileContent.add("((ValueWriter)" + getContainingBean().getAccess(PK.class) + "[WRITER]).setValue( (PreparedStatement)statement,paramPos++, pkList.get( i ));");
        fileContent.endBlock();
        fileContent.add("resultSet = ((PreparedStatement)statement).executeQuery();");
        fileContent.add("ret.addAll(" + getContainingBean().getSimpleBaseName() + "_HJMPWrapper.handleResult(pool,getDeployment(),resultSet));");
        fileContent.add("// close stmt and rs to next turn");
        fileContent.add("HJMPUtils.tryToClose( statement, resultSet );");
        fileContent.add("statement = null;");
        fileContent.add("resultSet = null;");
        fileContent.add("// jump to next page for next turn");
        fileContent.add("offset += pageSize;");
        fileContent.endBlock();
        fileContent.add("if (LOG) /*conv-log*/ log.debug(\"    found \"+ret);");
        fileContent.add("return ret;");
        fileContent.endBlock();
        fileContent.add("finally");
        fileContent.add("{");
        fileContent.add("\tHJMPUtils.tryToClose( connection, statement, resultSet );");
        fileContent.add("}");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("");
        fileContent.add("public java.util.Collection findByPKList( java.util.Collection pks )" + getExceptionDeclaration());
        fileContent.startBlock();
        fileContent
                        .add("return (java.util.Collection)de.hybris.platform.util.EJBTools.convertEntityFinderResult( ejbFindByPKList( pks ), getEntityContext().getPersistencePool().getTenant().getSystemEJB());");
        fileContent.endBlock();
        fileContent.add("public java.util.Collection ejbFindByPKList( java.util.Collection pks )" + getExceptionDeclaration());
        fileContent.startBlock();
        fileContent.add("try");
        fileContent.startBlock();
        fileContent.add("if (LOG) /*conv-log*/ log.debug( \"" +
                        getContainingBean().getSimpleBaseName() + " ejbFindByPKList\" );");
        for(int i = 0; i < getParameterCount(); i++)
        {
            fileContent.add("if (LOG) /*conv-log*/ log.debug(\"  " + i + "->\"+pks);");
        }
        fileContent
                        .add("Object resultObject = new FindByPKListFinderResult( getEntityContext().getPersistencePool(), getEntityContext().getItemDeployment(), pks ).getFinderResult();");
        fileContent.add("if (LOG) /*conv-log*/ log.debug(\"  result=\"+resultObject);");
        fileContent.add("return (java.util.Collection)resultObject;");
        fileContent.endBlock();
        fileContent.add("catch (SQLException e)");
        fileContent.startBlock();
        fileContent.add("throw new HJMPFinderException( e );");
        fileContent.endBlock();
        fileContent.endBlock();
        fileContent.add("");
    }
}
