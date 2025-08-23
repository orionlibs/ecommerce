package de.hybris.platform.patches.actions.data;

import de.hybris.platform.patches.actions.PatchAction;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.transform.Generated;
import groovy.transform.Internal;
import java.beans.Transient;
import org.codehaus.groovy.runtime.callsite.CallSite;

public class SamplePatchActionForTests implements PatchAction, GroovyObject
{
    @Generated
    public SamplePatchActionForTests()
    {
        MetaClass metaClass = $getStaticMetaClass();
        this.metaClass = metaClass;
    }


    public void perform(PatchActionData data)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        arrayOfCallSite[0].call(data, arrayOfCallSite[1].callGetProperty(PatchActionDataOption.Impex.class));
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
