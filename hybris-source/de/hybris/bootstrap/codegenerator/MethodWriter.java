package de.hybris.bootstrap.codegenerator;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

public class MethodWriter extends AbstractImportProvider
{
    public static final int FINAL = 1;
    public static final int STATIC = 2;
    public static final int SYNCHRONIZED = 4;
    public static final int ABSTRACT = 8;
    private final String name;
    private String returnType;
    private Visibility visibility = Visibility.PACKAGE_PROTECTED;
    private Set<String> thrownExceptions;
    private Map<String, String> signature;
    private CodeWriter content;
    private Set<String> annotations;
    private int modifiers = 0;
    private boolean constructor;
    private String javadoc;
    private String comment = null;


    public MethodWriter(String name)
    {
        this(null, name);
    }


    public MethodWriter(String returnType, String name)
    {
        this(Visibility.PACKAGE_PROTECTED, returnType, name);
    }


    public MethodWriter(Visibility visibility, String returnType, String name)
    {
        this.visibility = visibility;
        this.name = name;
        this.returnType = addRequiredImport(returnType);
    }


    public void write(JavaFile file)
    {
        if(this.comment != null && !this.comment.isEmpty())
        {
            file.add("//" + this.comment);
        }
        if(getJavadoc() != null)
        {
            file.add("/**");
            for(StringTokenizer st = new StringTokenizer(getJavadoc(), "\n"); st.hasMoreTokens(); )
            {
                file.add(" * " + st.nextToken());
            }
            file.add(" */");
        }
        for(String anno : getAnnotations())
        {
            file.add("@" + anno);
        }
        if(isAbstract())
        {
            file.add("" + getVisibility() + getVisibility() + modifiersToString(getModifiers()) + " " + (isConstructor() ? "" : (" " + getReturnType())) + "(" +
                            getName() + ")" + assembleSignature() + ";");
        }
        else
        {
            file.add("" + getVisibility() + getVisibility() + modifiersToString(getModifiers()) + " " + (isConstructor() ? "" : (" " + getReturnType())) + "(" +
                            getName() + ")" + assembleSignature());
            file.startBlock();
            writeContent(file);
            file.endBlock();
        }
    }


    public static String modifiersToString(int modifiers)
    {
        if(modifiers > 0)
        {
            StringBuilder result = new StringBuilder();
            if((modifiers & 0x8) == 8)
            {
                result.append(" abstract");
            }
            if((modifiers & 0x2) == 2)
            {
                result.append(" static");
            }
            if((modifiers & 0x1) == 1)
            {
                result.append(" final");
            }
            if((modifiers & 0x4) == 4)
            {
                result.append(" synchronized");
            }
            return result.toString();
        }
        return "";
    }


    protected void writeContent(JavaFile file)
    {
        if(getContent() != null)
        {
            getContent().write(file);
        }
    }


    public void addParameter(String type, String name)
    {
        if(this.signature == null)
        {
            this.signature = new LinkedHashMap<>();
        }
        else if(this.signature.containsKey(name))
        {
            throw new IllegalArgumentException("method " + this + " already contains parameter '" + name + "'");
        }
        this.signature.put(name, addRequiredImport(type));
    }


    public Map<String, String> getSignature()
    {
        return (this.signature != null) ? this.signature : Collections.<String, String>emptyMap();
    }


    public String getParameterType(String name)
    {
        return (this.signature != null) ? this.signature.get(name) : null;
    }


    protected String assembleSignature()
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> e : getSignature().entrySet())
        {
            if(first)
            {
                first = false;
            }
            else
            {
                result.append(", ");
            }
            result.append("final ").append(stripPackageName(e.getValue())).append(" ").append(e.getKey());
        }
        return result.toString();
    }


    protected String assembleThrowsClause()
    {
        if(this.thrownExceptions == null || this.thrownExceptions.isEmpty())
        {
            return "";
        }
        StringBuilder result = new StringBuilder(" throws ");
        boolean first = true;
        for(String exc : this.thrownExceptions)
        {
            if(first)
            {
                first = false;
            }
            else
            {
                result.append(",");
            }
            result.append(exc);
        }
        return result.toString();
    }


    public Visibility getVisibility()
    {
        return this.visibility;
    }


    public void setVisibility(Visibility visibility)
    {
        this.visibility = visibility;
    }


    public String getName()
    {
        return this.name;
    }


    public CodeWriter getContent()
    {
        return this.content;
    }


    public void setContent(CodeWriter content)
    {
        this.content = content;
    }


    public void setContentPlain(String plainText)
    {
        setContent((CodeWriter)new Object(this, plainText));
    }


    public static void writeTextToFile(JavaFile file, String plainText)
    {
        for(StringTokenizer st = new StringTokenizer(plainText, "\n"); st.hasMoreTokens(); )
        {
            String txt = st.nextToken().trim();
            if("{".equals(txt))
            {
                file.startBlock();
                continue;
            }
            if("}".equals(txt))
            {
                file.endBlock();
                continue;
            }
            file.add(txt);
        }
    }


    protected String analyzeAndStripReturnType(String original)
    {
        if(original != null)
        {
            Pattern pattern = Pattern.compile("(([a-zA-Z]\\w*\\.)*)([A-Z]\\w*)");
            Matcher matcher = pattern.matcher(original);
            StringBuffer result = new StringBuffer();
            while(matcher.find())
            {
                String pkg = matcher.group(1);
                if(pkg != null && pkg.length() > 0)
                {
                    addRequiredImport(matcher.group(0));
                }
                matcher.appendReplacement(result, matcher.group(3));
            }
            matcher.appendTail(result);
            return result.toString();
        }
        return null;
    }


    public static Set<String> extractClassNames(String expression)
    {
        Set<String> ret = null;
        if(expression != null)
        {
            Pattern pattern = Pattern.compile("(([a-zA-Z]\\w*\\.)+)([A-Z]\\w*)");
            Matcher matcher = pattern.matcher(expression);
            while(matcher.find())
            {
                String pkg = matcher.group(1);
                if(pkg != null && pkg.length() > 0)
                {
                    if(ret == null)
                    {
                        ret = new HashSet<>();
                    }
                    ret.add(matcher.group(0));
                }
            }
        }
        return (ret != null) ? ret : Collections.<String>emptySet();
    }


    public static String stripPackageName(String className)
    {
        if(className == null)
        {
            return null;
        }
        Pattern pattern = Pattern.compile("(([a-zA-Z]\\w*\\.)*)([A-Z]\\w*)");
        Matcher matcher = pattern.matcher(className);
        StringBuilder result = new StringBuilder();
        for(; matcher.find(); matcher.appendReplacement(result, matcher.group(3)))
            ;
        matcher.appendTail(result);
        return result.toString();
    }


    public String getReturnType()
    {
        return (this.returnType != null) ? this.returnType : "void";
    }


    public Set<String> getRequiredImports()
    {
        Set<String> ret = new LinkedHashSet<>();
        ret.addAll(super.getRequiredImports());
        if(this.signature != null)
        {
            for(Map.Entry<String, String> entry : this.signature.entrySet())
            {
                if(((String)entry.getValue()).indexOf('.') >= 0)
                {
                    ret.add(entry.getValue());
                }
            }
        }
        return ret;
    }


    public void setReturnType(String returnType)
    {
        if(this.returnType != returnType && (this.returnType == null || !this.returnType.equals(returnType)))
        {
            this.returnType = addRequiredImport(returnType);
        }
    }


    public boolean isAbstract()
    {
        return ((this.modifiers & 0x8) == 8);
    }


    public void setAbstract(boolean isAbstract)
    {
        if(isAbstract)
        {
            this.modifiers |= 0x8;
        }
        else
        {
            this.modifiers &= 0xFFFFFFF7;
        }
    }


    public void addDeprecatedAnnotation(String deprecatedSince)
    {
        if(StringUtils.isNotBlank(deprecatedSince))
        {
            addAnnotation("Deprecated(since = \"" + deprecatedSince + "\", forRemoval = true)");
        }
        else
        {
            addAnnotation("Deprecated(since = \"ages\", forRemoval = true)");
        }
    }


    public void addAnnotation(String anno)
    {
        if(this.annotations == null)
        {
            this.annotations = new LinkedHashSet<>();
        }
        this.annotations.add(anno);
    }


    public Set<String> getAnnotations()
    {
        return (this.annotations != null) ? this.annotations : Collections.<String>emptySet();
    }


    public int getModifiers()
    {
        return this.modifiers;
    }


    public void setModifiers(int modifiers)
    {
        this.modifiers = modifiers;
    }


    public void addThrownException(String exceptionType)
    {
        if(this.thrownExceptions == null)
        {
            this.thrownExceptions = new LinkedHashSet<>();
        }
        this.thrownExceptions.add(addRequiredImport(exceptionType));
    }


    public Set<String> getThrownExceptions()
    {
        return (this.thrownExceptions != null) ? this.thrownExceptions : Collections.<String>emptySet();
    }


    public boolean isConstructor()
    {
        return this.constructor;
    }


    public void setConstructor(boolean isConstructor)
    {
        this.constructor = isConstructor;
    }


    public String getJavadoc()
    {
        return this.javadoc;
    }


    public void setJavadoc(String javadoc)
    {
        this.javadoc = javadoc;
    }


    public void addComment(String comment)
    {
        this.comment = comment;
    }
}
