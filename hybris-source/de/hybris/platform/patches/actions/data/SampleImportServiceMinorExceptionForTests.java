package de.hybris.platform.patches.actions.data;

import de.hybris.platform.servicelayer.impex.ImportConfig;
import de.hybris.platform.servicelayer.impex.ImportResult;
import de.hybris.platform.servicelayer.impex.impl.DefaultImportService;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.transform.Generated;
import groovy.transform.Internal;
import java.beans.Transient;
import org.codehaus.groovy.runtime.callsite.CallSite;

public class SampleImportServiceMinorExceptionForTests extends DefaultImportService implements GroovyObject
{
    @Generated
    public SampleImportServiceMinorExceptionForTests()
    {
        MetaClass metaClass = $getStaticMetaClass();
        this.metaClass = metaClass;
    }


    public ImportResult importData(ImportConfig config)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        throw (Throwable)arrayOfCallSite[0].callConstructor(IllegalArgumentException.class, "minor exception");
    }


    @Generated
    @Internal
    @Transient
    public MetaClass getMetaClass()
    {
        if(this.metaClass != null)
        {
            return this.metaClass;
        }
        this.metaClass = $getStaticMetaClass();
        return this.metaClass;
    }


    @Generated
    @Internal
    public void setMetaClass(MetaClass paramMetaClass)
    {
        this.metaClass = paramMetaClass;
    }
}
