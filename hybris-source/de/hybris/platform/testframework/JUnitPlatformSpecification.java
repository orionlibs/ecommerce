package de.hybris.platform.testframework;

import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.transform.Generated;
import groovy.transform.Internal;
import java.beans.Transient;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.spockframework.runtime.model.SpecMetadata;
import spock.lang.Specification;

@Ignore
@RunWith(HybrisSpockRunner.class)
@SpecMetadata(filename = "JUnitPlatformSpecification.groovy", line = 11)
public abstract class JUnitPlatformSpecification extends Specification implements GroovyObject
{
    @Generated
    public JUnitPlatformSpecification()
    {
        MetaClass metaClass = $getStaticMetaClass();
        this.metaClass = metaClass;
    }


    @Test
    public void platformDummyTest()
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
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
