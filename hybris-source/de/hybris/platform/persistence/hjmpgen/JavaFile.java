package de.hybris.platform.persistence.hjmpgen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class JavaFile
{
    protected static final String INDENT_STEP = "\t";
    private static final String INITIAL_INDENT = "";
    final List lines = new ArrayList();
    String indent = "";


    public String getIndent()
    {
        return this.indent;
    }


    public int getLineNumber()
    {
        return this.lines.size();
    }


    public void add(String newLine)
    {
        this.lines.add(this.indent + this.indent);
    }


    public void insert(int pos, String indent, String newLine)
    {
        this.lines.add(pos, indent + indent);
    }


    public void addAll(List newLines)
    {
        Iterator<String> iter = newLines.iterator();
        while(iter.hasNext())
        {
            String next = iter.next();
            add(next);
        }
    }


    public void startBlock()
    {
        add("{");
        this.indent += "\t";
    }


    public void endBlock()
    {
        this.indent = this.indent.substring("\t".length());
        add("}");
    }


    public List getLines()
    {
        return Collections.unmodifiableList(this.lines);
    }


    public void assertBlocksClosed()
    {
        if(!this.indent.equals(""))
        {
            throw new HJMPGeneratorException("not all blocks closed!");
        }
    }
}
