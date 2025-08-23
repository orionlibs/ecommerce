package de.hybris.platform.directpersistence;

import org.apache.log4j.Logger;

public class LegacyFlagsUtils
{
    private static final Logger LOG = Logger.getLogger(LegacyFlagsUtils.class);
    private static final String PERSISTENCE_LEGACY_FLAG = "persistence.legacy.mode";
    private static final String IMPEX_LEGACY_FLAG = "impex.legacy.mode";
    private static final String SYNCHRONIZATION_LEGACY_FLAG = "synchronization.legacy.mode";
    private static final String SYSTEM_SETUP_SORT_LEGACY_FLAG = "system.setup.sort.legacy.mode";
    private static final ThreadLocal<LegacyFlagContext> PERSISTENCE_LEGACY_FLAG_MAP = (ThreadLocal<LegacyFlagContext>)new Object();
    private static final ThreadLocal<LegacyFlagContext> IMPEX_LEGACY_FLAG_MAP = (ThreadLocal<LegacyFlagContext>)new Object();
    private static final ThreadLocal<LegacyFlagContext> SYNCHRONIZATION_LEGACY_FLAG_MAP = (ThreadLocal<LegacyFlagContext>)new Object();
    private static final ThreadLocal<LegacyFlagContext> SYSTEM_SETUP_SORT_LEGACY_FLAG_MAP = (ThreadLocal<LegacyFlagContext>)new Object();


    public static boolean isLegacyFlagEnabled(LegacyFlag flag, Boolean forced)
    {
        switch(null.$SwitchMap$de$hybris$platform$directpersistence$LegacyFlagsUtils$LegacyFlag[flag.ordinal()])
        {
            case 1:
                return checkLegacyFlagForPersist(null);
            case 2:
                return checkLegacyFlagForImpex(forced);
            case 3:
                return checkLegacyFlagForSync(null);
            case 4:
                return checkLegacyFlagForSystemSetupSort(null);
        }
        return false;
    }


    public static boolean isLegacyFlagEnabled(LegacyFlag flag)
    {
        switch(null.$SwitchMap$de$hybris$platform$directpersistence$LegacyFlagsUtils$LegacyFlag[flag.ordinal()])
        {
            case 1:
                return checkLegacyFlagForPersist(null);
            case 2:
                return checkLegacyFlagForImpex(null);
            case 3:
                return checkLegacyFlagForSync(null);
            case 4:
                return checkLegacyFlagForSystemSetupSort(null);
        }
        return false;
    }


    public static void enableLegacyFlag(LegacyFlag flag)
    {
        switch(null.$SwitchMap$de$hybris$platform$directpersistence$LegacyFlagsUtils$LegacyFlag[flag.ordinal()])
        {
            case 1:
                enableLegacyFlagForPersist();
                break;
            case 2:
                enableLegacyFlagForImpex();
                break;
            case 3:
                enableLegacyFlagForSync();
                break;
            case 4:
                enableLegacyFlagForSystemSetupSort();
                break;
        }
    }


    public static void disableLegacyFlag(LegacyFlag flag)
    {
        switch(null.$SwitchMap$de$hybris$platform$directpersistence$LegacyFlagsUtils$LegacyFlag[flag.ordinal()])
        {
            case 1:
                disableLegacyFlagForPersist();
                break;
            case 2:
                disableLegacyFlagForImpex();
                break;
            case 3:
                disableLegacyFlagForSync();
                break;
            case 4:
                disableLegacyFlagForSystemSetupSort();
                break;
        }
    }


    public static void clearLegacySetting(LegacyFlag flag)
    {
        switch(null.$SwitchMap$de$hybris$platform$directpersistence$LegacyFlagsUtils$LegacyFlag[flag.ordinal()])
        {
            case 1:
                clearLegacyFlagForPersist();
                break;
            case 2:
                clearLegacyFlagForImpex();
                break;
            case 3:
                clearLegacyFlagForSync();
                break;
            case 4:
                clearLegacyFlagForSystemSetupSort();
                break;
        }
    }


    private static boolean checkLegacyFlagForPersist(Boolean forced)
    {
        boolean enabled = ((LegacyFlagContext)PERSISTENCE_LEGACY_FLAG_MAP.get()).isLegacyFlagEnabled(forced);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Legacy flag (persistence) for thread " + Thread.currentThread() + " is " + enabled);
        }
        return enabled;
    }


    private static boolean checkLegacyFlagForImpex(Boolean forced)
    {
        boolean enabled = ((LegacyFlagContext)IMPEX_LEGACY_FLAG_MAP.get()).isLegacyFlagEnabled(forced);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Legacy flag (impex) for thread " + Thread.currentThread() + " is " + enabled);
        }
        return enabled;
    }


    private static boolean checkLegacyFlagForSync(Boolean forced)
    {
        boolean enabled = ((LegacyFlagContext)SYNCHRONIZATION_LEGACY_FLAG_MAP.get()).isLegacyFlagEnabled(forced);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Legacy flag (synchronization) for thread " + Thread.currentThread() + " is " + enabled);
        }
        return enabled;
    }


    private static boolean checkLegacyFlagForSystemSetupSort(Boolean forced)
    {
        boolean enabled = ((LegacyFlagContext)SYSTEM_SETUP_SORT_LEGACY_FLAG_MAP.get()).isLegacyFlagEnabled(forced);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Legacy flag (system.setup.sort) for thread " + Thread.currentThread() + " is " + enabled);
        }
        return enabled;
    }


    private static void enableLegacyFlagForPersist()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Enabling legacy flag (persistence) for thread " + Thread.currentThread());
        }
        ((LegacyFlagContext)PERSISTENCE_LEGACY_FLAG_MAP.get()).enableLegacyFlag();
    }


    private static void enableLegacyFlagForImpex()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Enabling legacy flag (impex) for thread " + Thread.currentThread());
        }
        ((LegacyFlagContext)IMPEX_LEGACY_FLAG_MAP.get()).enableLegacyFlag();
    }


    private static void enableLegacyFlagForSync()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Enabling legacy flag (synchronization) for thread " + Thread.currentThread());
        }
        ((LegacyFlagContext)SYNCHRONIZATION_LEGACY_FLAG_MAP.get()).enableLegacyFlag();
    }


    private static void enableLegacyFlagForSystemSetupSort()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Enabling legacy flag (system.setup.sort) for thread " + Thread.currentThread());
        }
        ((LegacyFlagContext)SYSTEM_SETUP_SORT_LEGACY_FLAG_MAP.get()).enableLegacyFlag();
    }


    private static void disableLegacyFlagForImpex()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Disabling legacy flag (impex) for thread " + Thread.currentThread());
        }
        ((LegacyFlagContext)IMPEX_LEGACY_FLAG_MAP.get()).disableLegacyFlag();
    }


    private static void disableLegacyFlagForPersist()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Disabling legacy flag (persistence) for thread " + Thread.currentThread());
        }
        ((LegacyFlagContext)PERSISTENCE_LEGACY_FLAG_MAP.get()).disableLegacyFlag();
    }


    private static void disableLegacyFlagForSync()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Disabling legacy flag (synchronization) for thread " + Thread.currentThread());
        }
        ((LegacyFlagContext)SYNCHRONIZATION_LEGACY_FLAG_MAP.get()).disableLegacyFlag();
    }


    private static void disableLegacyFlagForSystemSetupSort()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Disabling legacy flag (system.setup.sort) for thread " + Thread.currentThread());
        }
        ((LegacyFlagContext)SYSTEM_SETUP_SORT_LEGACY_FLAG_MAP.get()).disableLegacyFlag();
    }


    private static void clearLegacyFlagForPersist()
    {
        ((LegacyFlagContext)PERSISTENCE_LEGACY_FLAG_MAP.get()).clearLegacyFlag();
    }


    private static void clearLegacyFlagForSync()
    {
        ((LegacyFlagContext)SYNCHRONIZATION_LEGACY_FLAG_MAP.get()).clearLegacyFlag();
    }


    private static void clearLegacyFlagForImpex()
    {
        ((LegacyFlagContext)IMPEX_LEGACY_FLAG_MAP.get()).clearLegacyFlag();
    }


    private static void clearLegacyFlagForSystemSetupSort()
    {
        ((LegacyFlagContext)SYSTEM_SETUP_SORT_LEGACY_FLAG_MAP.get()).clearLegacyFlag();
    }
}
