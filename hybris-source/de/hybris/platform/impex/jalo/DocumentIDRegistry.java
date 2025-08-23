package de.hybris.platform.impex.jalo;

import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.util.CSVReader;
import de.hybris.platform.util.CSVWriter;
import de.hybris.platform.util.Config;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.log4j.Logger;

public class DocumentIDRegistry
{
    private static final String DISALLOW_IGNORED_VALUES = "documentidregistry.disallow.ignored.values";
    private Map<String, DualHashBidiMap<String, Long>> resolvedQualifiers;
    private Map<String, DualHashBidiMap<String, Long>> unresolvedQualifiers;
    private final CSVWriter writer;
    private final CSVReader reader;
    private static final Logger LOGGER = Logger.getLogger(DocumentIDRegistry.class.getName());
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = this.lock.readLock();
    private final Lock writeLock = this.lock.writeLock();


    public DocumentIDRegistry()
    {
        this(null, null);
    }


    public DocumentIDRegistry(CSVWriter documentIDWriter)
    {
        this(null, documentIDWriter);
    }


    public DocumentIDRegistry(CSVReader documentIDReader)
    {
        this(documentIDReader, null);
    }


    public DocumentIDRegistry(CSVReader documentIDReader, CSVWriter documentIDWriter)
    {
        this.writer = documentIDWriter;
        this.reader = documentIDReader;
        importIDs(documentIDReader);
    }


    public void importIDs(CSVReader documentIDReader)
    {
        if(documentIDReader != null)
        {
            while(documentIDReader.readNextLine())
            {
                Map<Integer, String> line = documentIDReader.getLine();
                if(line.size() == 3)
                {
                    try
                    {
                        addID(line.get(Integer.valueOf(0)), line.get(Integer.valueOf(1)),
                                        Long.parseLong(line.get(Integer.valueOf(2))), MODE.RESOLVED);
                    }
                    catch(NumberFormatException e)
                    {
                        LOGGER.warn("Can not parse pk of line, will skip it:" + line, e);
                    }
                    catch(ImpExException e)
                    {
                        LOGGER.warn("Can not create entry for line, will skip it:" + e.getMessage(), (Throwable)e);
                    }
                    continue;
                }
                LOGGER.warn("Can not interprete line, will skip it:" + line);
            }
        }
    }


    protected void exportID(String qualifier, String documentID, long pk)
    {
        if(this.writer != null)
        {
            try
            {
                Map<Integer, String> toWrite = new HashMap<>();
                toWrite.put(Integer.valueOf(0), qualifier);
                toWrite.put(Integer.valueOf(1), documentID);
                toWrite.put(Integer.valueOf(2), Long.toString(pk));
                this.writer.write(toWrite);
            }
            catch(IOException e)
            {
                LOGGER.warn("error while writing document id entry: " + qualifier + ":" + documentID + ":" + pk + ":" + e
                                .getMessage(), e);
            }
        }
    }


    public String registerID(String qualifier, String documentID, long pk) throws ImpExException
    {
        addID(qualifier, documentID, pk, MODE.RESOLVED);
        return documentID;
    }


    public String registerPK(String qualifier, long pk)
    {
        String id = getID(qualifier, pk, MODE.RESOLVED);
        if(id == null)
        {
            id = getID(qualifier, pk, MODE.UNRESOLVED);
            if(id != null)
            {
                resolveID(qualifier, id, pk);
            }
        }
        if(id == null)
        {
            id = calculateNextID(qualifier);
            try
            {
                addID(qualifier, id, pk, MODE.RESOLVED);
            }
            catch(ImpExException e)
            {
                throw new JaloSystemException(e, "Error while adding document id where I have already checked containing", 0);
            }
        }
        return id;
    }


    public String lookupPK(String qualifier, long pk)
    {
        String id = getID(qualifier, pk, MODE.RESOLVED);
        if(id == null)
        {
            id = getID(qualifier, pk, MODE.UNRESOLVED);
        }
        if(id == null)
        {
            id = calculateNextID(qualifier);
            try
            {
                addID(qualifier, id, pk, MODE.UNRESOLVED);
            }
            catch(ImpExException e)
            {
                throw new JaloSystemException(e, "Error while adding document id where I have already checked containing", 0);
            }
        }
        return id;
    }


    public long lookupID(String qualifier, String documentID)
    {
        return getPK(qualifier, documentID, MODE.RESOLVED);
    }


    public boolean containsID(String qualifier, String documentID)
    {
        return (getPK(qualifier, documentID, MODE.RESOLVED) == -1L && getPK(qualifier, documentID, MODE.UNRESOLVED) == -1L);
    }


    public boolean containsPK(String qualifier, long pk)
    {
        return (getID(qualifier, pk, MODE.RESOLVED) == null && getID(qualifier, pk, MODE.UNRESOLVED) == null);
    }


    public boolean hasUnresolvedIDs()
    {
        return !getQualifiersMap(MODE.UNRESOLVED).isEmpty();
    }


    public boolean isUnresolved(String qualifier, String documentID)
    {
        return (getPK(qualifier, documentID, MODE.UNRESOLVED) != -1L);
    }


    public boolean isResolved(String qualifier, String documentID)
    {
        return (getPK(qualifier, documentID, MODE.RESOLVED) != -1L);
    }


    public List<String> printUnresolvedIDs(String separator)
    {
        this.readLock.lock();
        try
        {
            List<String> ret;
            if(hasUnresolvedIDs())
            {
                ret = new ArrayList<>();
                for(Map.Entry<String, DualHashBidiMap<String, Long>> qualifierEntry : getQualifiersMap(MODE.UNRESOLVED)
                                .entrySet())
                {
                    for(String id : ((DualHashBidiMap)qualifierEntry.getValue()).keySet())
                    {
                        ret.add("Scope:" + (String)qualifierEntry.getKey() + separator + "ID:" + id + separator + "PK:" + ((DualHashBidiMap)qualifierEntry
                                        .getValue()).get(id));
                    }
                }
            }
            else
            {
                ret = Collections.EMPTY_LIST;
            }
            return ret;
        }
        finally
        {
            this.readLock.unlock();
        }
    }


    public void closeStreams()
    {
        if(this.writer != null)
        {
            try
            {
                this.writer.close();
            }
            catch(IOException e)
            {
                LOGGER.warn("Error while closing csv writer: " + e.getMessage());
            }
        }
        if(this.reader != null)
        {
            try
            {
                this.reader.close();
            }
            catch(IOException e)
            {
                LOGGER.warn("Error while closing csv reader: " + e.getMessage());
            }
        }
    }


    protected String getID(String qualifier, long pk, MODE mode)
    {
        this.readLock.lock();
        try
        {
            DualHashBidiMap<String, Long> qualifierMap = getQualifiersMap(mode).get(qualifier);
            if(qualifierMap == null)
            {
                return null;
            }
            return (String)qualifierMap.inverseBidiMap().get(Long.valueOf(pk));
        }
        finally
        {
            this.readLock.unlock();
        }
    }


    protected long getPK(String qualifier, String documentID, MODE mode)
    {
        this.readLock.lock();
        try
        {
            Map<String, Long> qualifierMap = (Map<String, Long>)getQualifiersMap(mode).get(qualifier);
            if(qualifierMap == null)
            {
                return -1L;
            }
            Long ret = qualifierMap.get(documentID);
            return (ret == null) ? -1L : ret.longValue();
        }
        finally
        {
            this.readLock.unlock();
        }
    }


    protected String calculateNextID(String qualifier)
    {
        this.readLock.lock();
        try
        {
            int number = 0;
            Map map = (Map)getQualifiersMap(MODE.RESOLVED).get(qualifier);
            if(map != null)
            {
                number += map.size();
            }
            map = (Map)getQualifiersMap(MODE.UNRESOLVED).get(qualifier);
            if(map != null)
            {
                number += map.size();
            }
            String id = qualifier + qualifier;
            while(getQualifiersMap(MODE.RESOLVED).containsKey(id) || getQualifiersMap(MODE.UNRESOLVED).containsKey(id))
            {
                number++;
                id = qualifier + qualifier;
            }
            return id;
        }
        finally
        {
            this.readLock.unlock();
        }
    }


    protected void addID(String qualifier, String documentID, long pk, MODE mode) throws ImpExException
    {
        if(shouldNotAddID(documentID))
        {
            return;
        }
        this.writeLock.lock();
        try
        {
            DualHashBidiMap<String, Long> qualifierMap = getQualifiersMap(mode).get(qualifier);
            if(qualifierMap == null)
            {
                qualifierMap = new DualHashBidiMap();
                getQualifiersMap(mode).put(qualifier, qualifierMap);
            }
            Long old = (Long)qualifierMap.put(documentID, Long.valueOf(pk));
            if(old != null && old.longValue() != pk)
            {
                qualifierMap.put(documentID, old);
                throw new ImpExException("id " + documentID + " for qualifier " + qualifier + " already used for item with pk=" + old);
            }
            if(mode == MODE.RESOLVED)
            {
                exportID(qualifier, documentID, pk);
            }
        }
        finally
        {
            this.writeLock.unlock();
        }
    }


    private boolean shouldNotAddID(String documentID)
    {
        return (Config.getBoolean("documentidregistry.disallow.ignored.values", true) && documentID != null && documentID
                        .startsWith("<ignore>"));
    }


    protected void resolveID(String qualifier, String documentID, long pk)
    {
        this.writeLock.lock();
        try
        {
            Map<String, Long> qualifierMap = (Map<String, Long>)getQualifiersMap(MODE.UNRESOLVED).get(qualifier);
            if(qualifierMap != null)
            {
                Long storedPK = qualifierMap.remove(documentID);
                if(storedPK.longValue() != pk)
                {
                    throw new JaloSystemException("Document ID <" + qualifier + "," + documentID + "," + pk + "> will be resolved, but existing unresolved entry is ,mapped to PK=" + storedPK
                                    .longValue());
                }
                if(qualifierMap.isEmpty())
                {
                    getQualifiersMap(MODE.UNRESOLVED).remove(qualifier);
                }
            }
            try
            {
                addID(qualifier, documentID, pk, MODE.RESOLVED);
            }
            catch(ImpExException e)
            {
                throw new JaloSystemException(e, "Error while adding document id where I have already checked containing", 0);
            }
        }
        finally
        {
            this.writeLock.unlock();
        }
    }


    protected Map<String, DualHashBidiMap<String, Long>> getQualifiersMap(MODE mode)
    {
        if(mode == MODE.RESOLVED)
        {
            if(this.resolvedQualifiers == null)
            {
                this.resolvedQualifiers = new HashMap<>();
            }
            return this.resolvedQualifiers;
        }
        if(this.unresolvedQualifiers == null)
        {
            this.unresolvedQualifiers = new HashMap<>();
        }
        return this.unresolvedQualifiers;
    }
}
