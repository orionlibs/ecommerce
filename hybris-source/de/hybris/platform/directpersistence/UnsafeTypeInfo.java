package de.hybris.platform.directpersistence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;

public class UnsafeTypeInfo
{
    private final UnsafeTypeInfo parent;
    private final String typeCode;
    private final String extensionName;
    private final Map<String, Collection<UnsafeMethodInfo>> unsafeToRead;
    private final Map<String, Collection<UnsafeMethodInfo>> unsafeToWrite;
    private final boolean hasUnknownUnsafeReadMethods;
    private final boolean hasUnknownUnsafeWriteMethods;
    private final Collection<UnsafeMethodInfo> allMethods;


    public UnsafeTypeInfo(UnsafeTypeInfo parent, String typeCode, String extensionName, Collection<UnsafeMethodInfo> methods)
    {
        this.parent = parent;
        this.extensionName = extensionName;
        this.typeCode = typeCode;
        this.allMethods = Collections.unmodifiableCollection(new ArrayList<>(methods));
        Map<String, Collection<UnsafeMethodInfo>> read = new LinkedHashMap<>();
        Map<String, Collection<UnsafeMethodInfo>> write = new LinkedHashMap<>();
        boolean foundUnknownReads = false;
        boolean foundUnknownWrites = false;
        for(UnsafeMethodInfo m : methods)
        {
            if(m.isRead())
            {
                addMethod(m, read);
                foundUnknownReads |= m.isUnknownProblem();
                continue;
            }
            addMethod(m, write);
            foundUnknownWrites |= m.isUnknownProblem();
        }
        this.unsafeToRead = Collections.unmodifiableMap(read);
        this.hasUnknownUnsafeReadMethods = foundUnknownReads;
        this.unsafeToWrite = Collections.unmodifiableMap(write);
        this.hasUnknownUnsafeWriteMethods = foundUnknownWrites;
    }


    private void addMethod(UnsafeMethodInfo methodInfo, Map<String, Collection<UnsafeMethodInfo>> map)
    {
        Collection<UnsafeMethodInfo> coll = map.getOrDefault(methodInfo.getAttribute(), new ArrayList<>());
        coll.add(methodInfo);
        map.put(methodInfo.getAttribute(), coll);
    }


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public boolean isUnsafeIgnoringMarked()
    {
        return (isUnsafeToReadIgnoringMarked() || isUnsafeToWriteIgnoringMarked());
    }


    public boolean isUnsafeToRead()
    {
        return !this.unsafeToRead.isEmpty();
    }


    public boolean isUnsafeToReadIgnoringMarked()
    {
        return this.hasUnknownUnsafeReadMethods;
    }


    public boolean isUnsafeToWrite()
    {
        return !this.unsafeToWrite.isEmpty();
    }


    public boolean isUnsafeToWriteIgnoringMarked()
    {
        return this.hasUnknownUnsafeWriteMethods;
    }


    public boolean isUnsafeToRead(String attribute)
    {
        return this.unsafeToRead.containsKey(attribute);
    }


    public boolean isUnsafeToReadIgnoringMarked(String attribute)
    {
        Collection<UnsafeMethodInfo> methods = this.unsafeToRead.get(attribute);
        return (CollectionUtils.isNotEmpty(methods) && methods.stream().anyMatch(UnsafeMethodInfo::isUnknownProblem));
    }


    public boolean isUnsafeToWrite(String attribute)
    {
        return this.unsafeToWrite.containsKey(attribute);
    }


    public boolean isUnsafeToWriteIgnoringMarked(String attribute)
    {
        Collection<UnsafeMethodInfo> methods = this.unsafeToWrite.get(attribute);
        return (CollectionUtils.isNotEmpty(methods) && methods.stream().anyMatch(UnsafeMethodInfo::isUnknownProblem));
    }


    public Collection<String> getUnsafeAttributesToRead()
    {
        return this.unsafeToRead.keySet();
    }


    public Collection<String> getUnsafeAttributesToWrite()
    {
        return this.unsafeToWrite.keySet();
    }


    public Collection<UnsafeMethodInfo> getAllMethods()
    {
        return this.allMethods;
    }


    public Collection<UnsafeMethodInfo> getMethodsNotCoveredByParent()
    {
        return (Collection<UnsafeMethodInfo>)this.allMethods.stream().filter(this::isNotCoveredByParent).collect(Collectors.toList());
    }


    public boolean isNotCoveredByParent(UnsafeMethodInfo method)
    {
        for(UnsafeTypeInfo t = getParent(); t != null; t = t.getParent())
        {
            if(t.getAllMethods().contains(method))
            {
                return false;
            }
        }
        return true;
    }


    public Collection<UnsafeMethodInfo> getUnsafeToRead(String attribute)
    {
        return this.unsafeToRead.getOrDefault(attribute, Collections.emptyList());
    }


    public Collection<UnsafeMethodInfo> getUnsafeToWrite(String attribute)
    {
        return this.unsafeToWrite.getOrDefault(attribute, Collections.emptyList());
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("unsafe ").append(isUnsafeToRead() ? "R" : "").append(isUnsafeToWrite() ? "W" : "");
        sb.append(" type: ").append(this.typeCode);
        sb.append(" extension:").append(this.extensionName).append('[');
        if(isUnsafeToRead())
        {
            sb.append("read:").append(this.unsafeToRead);
        }
        if(isUnsafeToWrite())
        {
            sb.append(" write:").append(this.unsafeToWrite);
        }
        sb.append(']');
        return sb.toString();
    }


    public UnsafeTypeInfo getParent()
    {
        return this.parent;
    }
}
