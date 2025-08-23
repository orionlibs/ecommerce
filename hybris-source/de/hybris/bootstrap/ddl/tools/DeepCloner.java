package de.hybris.bootstrap.ddl.tools;

import com.rits.cloning.Cloner;
import org.apache.commons.lang.StringUtils;

public class DeepCloner<T>
{
    private static final String ENABLE_DUMP_SYSTEM_PROPERTY = "initialization.dump.cloned.classes";
    private final Cloner cloner;


    public DeepCloner()
    {
        this.cloner = new Cloner();
        this.cloner.setDumpClonedClasses(isDumpingClonedClassesEnabled());
    }


    private boolean isDumpingClonedClassesEnabled()
    {
        String dumpingEnabled = System.getProperty("initialization.dump.cloned.classes");
        return !StringUtils.isEmpty(dumpingEnabled);
    }


    public T cloneDeeply(T toClone)
    {
        return (T)this.cloner.deepClone(toClone);
    }
}
