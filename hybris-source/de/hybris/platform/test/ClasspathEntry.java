package de.hybris.platform.test;

class ClasspathEntry
{
    public boolean exported;
    public String kind;
    public String path;
    public boolean combineaccessrules;


    public ClasspathEntry()
    {
    }


    public ClasspathEntry(boolean exported, String kind, String path, boolean combineaccessrules)
    {
        this.exported = exported;
        this.kind = kind;
        this.path = path;
        this.combineaccessrules = combineaccessrules;
    }


    public String toString()
    {
        return this.path;
    }
}
