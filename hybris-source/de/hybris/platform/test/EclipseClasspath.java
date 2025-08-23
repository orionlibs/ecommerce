package de.hybris.platform.test;

import de.hybris.bootstrap.config.ExtensionInfo;
import java.util.ArrayList;
import java.util.List;

class EclipseClasspath
{
    private final ExtensionInfo extension;
    private final List<ClasspathEntry> sources = new ArrayList<>();
    private final List<ClasspathEntry> libs = new ArrayList<>();


    public EclipseClasspath(ExtensionInfo extension)
    {
        this.extension = extension;
    }


    public void addClasspathEntry(ClasspathEntry entry)
    {
        if("src".equals(entry.kind))
        {
            addSource(entry.path, entry.exported, entry.combineaccessrules);
        }
        if("lib".equals(entry.kind))
        {
            addLibrary(entry.path, entry.exported, entry.combineaccessrules);
        }
    }


    public void addSource(String path, boolean exported, boolean combinedaccessrules)
    {
        this.sources.add(new ClasspathEntry(exported, "src", path, combinedaccessrules));
    }


    public void addLibrary(String path, boolean exported, boolean combinedaccessrules)
    {
        this.libs.add(new ClasspathEntry(exported, "lib", path, combinedaccessrules));
    }


    public List<ClasspathEntry> getAllClasspathEntries()
    {
        List<ClasspathEntry> entries = new ArrayList<>();
        entries.addAll(this.sources);
        entries.addAll(this.libs);
        return entries;
    }


    public List<ClasspathEntry> getLibs()
    {
        return this.libs;
    }


    public List<ClasspathEntry> getSources()
    {
        return this.sources;
    }


    public String toString()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Extension : " + this.extension);
        buffer.append(", Sources   : " + this.sources);
        buffer.append(", Libraries : " + this.libs);
        return buffer.toString();
    }


    public ExtensionInfo getExtension()
    {
        return this.extension;
    }
}
