package de.hybris.platform.util.migration;

import de.hybris.platform.core.PK;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.Logger;

public class MigrationUtilities
{
    private static final Logger LOG = Logger.getLogger(MigrationUtilities.class);
    private static volatile Collection<TypecodeMapper> _typecodeMappers;
    private static volatile Collection<PKMapper> _pkMappers;


    public static synchronized void registerTypecodeMapper(TypecodeMapper mapper)
    {
        if(_typecodeMappers == null)
        {
            _typecodeMappers = new CopyOnWriteArrayList<>();
        }
        if(!_typecodeMappers.contains(mapper))
        {
            _typecodeMappers.add(mapper);
        }
    }


    public static synchronized boolean unregisterTypecodeMapper(TypecodeMapper mapper)
    {
        if(_typecodeMappers == null)
        {
            return false;
        }
        return _typecodeMappers.remove(mapper);
    }


    public static synchronized boolean unregisterPKMapper(PKMapper mapper)
    {
        if(_pkMappers == null)
        {
            return false;
        }
        return _pkMappers.remove(mapper);
    }


    public static synchronized void registerPKMapper(PKMapper mapper)
    {
        if(_pkMappers == null)
        {
            _pkMappers = new CopyOnWriteArrayList<>();
        }
        if(!_pkMappers.contains(mapper))
        {
            _pkMappers.add(mapper);
        }
    }


    static
    {
        registerPKMapper((PKMapper)new Object());
    }

    public static PK convertOldPK(String oldPKString)
    {
        if(oldPKString == null)
        {
            return null;
        }
        OldPK oldPK = new OldPK(oldPKString);
        PK newPK = null;
        if(LOG.isDebugEnabled())
        {
            LOG.debug("OldPK=" + oldPK);
            LOG.debug(" -" + new Date(oldPK.getCreationTime()));
            LOG.debug(" -" + oldPK.getTypecode());
            LOG.debug(" -" + oldPK.getRandom());
            LOG.debug(" -" + oldPK.getInetAddress());
        }
        if(_pkMappers != null)
        {
            for(PKMapper mapper : _pkMappers)
            {
                PK mappedPK = mapper.mapPK(oldPK);
                if(mappedPK != null)
                {
                    LOG.debug("Mapped PK from " + oldPK + " to " + mappedPK);
                    newPK = mappedPK;
                    break;
                }
            }
        }
        if(newPK == null)
        {
            byte random = (byte)(int)(oldPK.getRandom() & 0xFL);
            byte random2 = (byte)(int)((oldPK.getRandom() & 0xF0L) >> 4L);
            int typecode = oldPK.getTypecode();
            if(_typecodeMappers != null)
            {
                for(TypecodeMapper mapper : _typecodeMappers)
                {
                    int mappedCode = mapper.mapTypecode(typecode);
                    if(mappedCode != typecode)
                    {
                        LOG.debug("Mapped typecode from " + typecode + " to " + mappedCode);
                        typecode = mappedCode;
                        break;
                    }
                }
            }
            newPK = PK.fromLong(calcNewPKLongValue(typecode, random2, random, oldPK.getCreationTime()));
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("NewPK=" + newPK);
            LOG.debug(" -" + new Date(newPK.getCreationTime()));
            LOG.debug(" -" + newPK.getTypeCode());
        }
        return newPK;
    }


    public static boolean isOldPK(String oldPKString)
    {
        if(oldPKString == null)
        {
            return false;
        }
        int delIdx = oldPKString.lastIndexOf("-");
        if(delIdx < 0)
        {
            return false;
        }
        if(delIdx == 0)
        {
            return false;
        }
        if(delIdx == oldPKString.length() - 1)
        {
            return false;
        }
        return true;
    }


    private static long calcNewPKLongValue(int typecode, byte clusterid, byte millicnt, long creationtime)
    {
        if(typecode < 0 || typecode > 32767)
        {
            throw new IllegalArgumentException("illegal typecode : " + typecode + ", allowed range: 0-32767");
        }
        long longValue = typecode << 48L & 0xFFFF000000000000L;
        longValue += clusterid << 44L & 0xF00000000000L;
        longValue += creationtime - 788914800000L << 4L & 0xFFFFFFFFFF0L;
        longValue += millicnt & 0xFL;
        return longValue;
    }
}
