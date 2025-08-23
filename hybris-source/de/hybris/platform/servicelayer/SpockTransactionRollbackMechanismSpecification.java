package de.hybris.platform.servicelayer;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.directpersistence.DirectPersistenceUtils;
import de.hybris.platform.jalo.user.Title;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import groovy.lang.GroovyObject;
import groovy.lang.Reference;
import groovy.transform.Generated;
import java.text.MessageFormat;
import java.util.Date;
import javax.annotation.Resource;
import org.codehaus.groovy.runtime.BytecodeInterface8;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.junit.Test;
import org.spockframework.mock.runtime.MockController;
import org.spockframework.runtime.SpecificationContext;
import org.spockframework.runtime.model.BlockKind;
import org.spockframework.runtime.model.BlockMetadata;
import org.spockframework.runtime.model.FeatureMetadata;
import org.spockframework.runtime.model.FieldMetadata;
import org.spockframework.runtime.model.SpecMetadata;
import org.springframework.jdbc.core.JdbcTemplate;

@IntegrationTest
@SpecMetadata(filename = "SpockTransactionRollbackMechanismSpecification.groovy", line = 19)
public class SpockTransactionRollbackMechanismSpecification extends ServicelayerTransactionalSpockSpecification
{
    @Resource
    @FieldMetadata(line = 22, name = "jdbcTemplate", ordinal = 0, initializer = false)
    private JdbcTemplate jdbcTemplate;


    @Test
    @FeatureMetadata(line = 25, name = "check transaction is rolled back by inserting the same object two times -first", ordinal = 0, blocks = {@BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_5_0()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).setThrownException((Throwable)ScriptBytecodeAdapter.castToType(null, Throwable.class));
        null;
        try
        {
            if(!__$stMC && !BytecodeInterface8.disabledStandardMetaClass())
            {
                insertTitle();
                null;
            }
            else
            {
                arrayOfCallSite[6].callCurrent((GroovyObject)this);
            }
        }
        catch(Throwable $spock_ex)
        {
            ((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).setThrownException($spock_ex);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[7].callCurrent((GroovyObject)this);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    @Test
    @FeatureMetadata(line = 34, name = "check transaction is rolled back by inserting the same object two times -second", ordinal = 1, blocks = {@BlockMetadata(kind = BlockKind.WHEN, texts = {}), @BlockMetadata(kind = BlockKind.THEN, texts = {})}, parameterNames = {})
    public void $spock_feature_5_1()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        ((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).setThrownException((Throwable)ScriptBytecodeAdapter.castToType(null, Throwable.class));
        null;
        try
        {
            if(!__$stMC && !BytecodeInterface8.disabledStandardMetaClass())
            {
                insertTitle();
                null;
            }
            else
            {
                arrayOfCallSite[8].callCurrent((GroovyObject)this);
            }
        }
        catch(Throwable $spock_ex)
        {
            ((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).setThrownException($spock_ex);
            null;
        }
        finally
        {
        }
        arrayOfCallSite[9].callCurrent((GroovyObject)this);
        ((MockController)ScriptBytecodeAdapter.castToType(((SpecificationContext)ScriptBytecodeAdapter.castToType(getSpecificationContext(), SpecificationContext.class)).getMockController(), MockController.class)).leaveScope();
        null;
    }


    private void insertTitle()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        Reference dateWriter = new Reference(ScriptBytecodeAdapter.castToType(arrayOfCallSite[0].callStatic(JDBCValueMappings.class, Date.class), JDBCValueMappings.ValueWriter.class));
        Object titleTableName = arrayOfCallSite[1].call(arrayOfCallSite[2].call(DirectPersistenceUtils.class, arrayOfCallSite[3].call(Title.class)));
        Object sql = arrayOfCallSite[4].call(MessageFormat.class, "INSERT INTO {0} (PK,p_code, createdTS) VALUES (?,?,?)", titleTableName);
        arrayOfCallSite[5].call(this.jdbcTemplate, sql, new _insertTitle_closure1(this, this, dateWriter));
    }


    @Generated
    public JdbcTemplate getJdbcTemplate()
    {
        return this.jdbcTemplate;
    }


    @Generated
    public void setJdbcTemplate(JdbcTemplate paramJdbcTemplate)
    {
        this.jdbcTemplate = paramJdbcTemplate;
    }
}
