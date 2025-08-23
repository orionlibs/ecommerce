package de.hybris.platform.patches.actions.utils;

import de.hybris.platform.core.Registry;
import de.hybris.platform.patches.model.PatchExecutionModel;
import de.hybris.platform.patches.model.PatchExecutionUnitModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import groovy.lang.GroovyObject;
import groovy.lang.MetaClass;
import groovy.transform.Generated;
import groovy.transform.Internal;
import java.beans.Transient;
import java.util.Collections;
import java.util.List;
import org.codehaus.groovy.runtime.ScriptBytecodeAdapter;
import org.codehaus.groovy.runtime.callsite.CallSite;
import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

public final class PatchExecutionUtils implements GroovyObject
{
    @Generated
    public PatchExecutionUtils()
    {
        MetaClass metaClass = $getStaticMetaClass();
        this.metaClass = metaClass;
    }


    public static List<PatchExecutionUnitModel> getPatchExecutionUnits(PatchExecutionModel patchExecution)
    {
        CallSite[] arrayOfCallSite = $getCallSiteArray();
        FlexibleSearchQuery query = (FlexibleSearchQuery)ScriptBytecodeAdapter.castToType(arrayOfCallSite[0].callConstructor(FlexibleSearchQuery.class, arrayOfCallSite[1].call(arrayOfCallSite[2].call(
                        arrayOfCallSite[3].call(arrayOfCallSite[4].call(arrayOfCallSite[5].call(arrayOfCallSite[6].call("select {", arrayOfCallSite[7].callGetProperty(PatchExecutionUnitModel.class)), "} from {"), arrayOfCallSite[8].callGetProperty(PatchExecutionUnitModel.class)), "} where {"),
                        arrayOfCallSite[9].callGetProperty(PatchExecutionUnitModel.class)), "} = ?patch")), FlexibleSearchQuery.class);
        arrayOfCallSite[10].call(query, arrayOfCallSite[11].callGetProperty(PatchExecutionUnitModel.class), patchExecution);
        SearchResult result = (SearchResult)ScriptBytecodeAdapter.castToType(arrayOfCallSite[12].call(arrayOfCallSite[13].call(arrayOfCallSite[14].call(Registry.class), "flexibleSearchService"), query), SearchResult.class);
        List results = (List)ScriptBytecodeAdapter.castToType(arrayOfCallSite[15].call(result), List.class);
        return (List<PatchExecutionUnitModel>)ScriptBytecodeAdapter.castToType(DefaultTypeTransformation.booleanUnbox(arrayOfCallSite[16].call(results)) ? arrayOfCallSite[17].call(Collections.class) : results, List.class);
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
