package de.hybris.bootstrap.codegenerator;

import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.typesystem.YComposedType;
import de.hybris.bootstrap.typesystem.YExtension;
import java.text.DateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;

public class ClassWriter extends AbstractImportProvider
{
    private static final String DEFAULT_COPYRIGHT = " \nCopyright (c) " + Year.now()
                    .toString() + " SAP SE or an SAP affiliate company. All rights reserved.\n";
    public static final String GENERATED_NOTICE;
    private final CodeGenerator gen;
    private final YExtension ext;
    private final String className;
    private String extendsClass;
    private List<String> interfaces;
    private String packageName;
    private String copyright;
    private String javadoc;
    private List<String> annotations;

    static
    {
        StringBuilder dateString = new StringBuilder();
        dateString.append(DateFormat.getDateTimeInstance().format(new Date()));
        for(int i = dateString.length(); i < 44; i++)
        {
            dateString.append(' ');
        }
        GENERATED_NOTICE = "----------------------------------------------------------------\n--- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---\n--- Generated at " + dateString.toString() + "---\n----------------------------------------------------------------\n";
    }

    private int modifiers = 0;
    private List<MethodWriter> constructors;
    private List<MethodWriter> methods;
    private List<CodeWriter> declarations;
    private List<CodeWriter> constantDeclarations;
    private Visibility visibility = Visibility.PACKAGE_PROTECTED;


    public static String assembleAbstractClassName(CodeGenerator gen, YExtension ext, String name)
    {
        return gen.getInfo(ext).getAbstractClassPrefix() + gen.getInfo(ext).getAbstractClassPrefix() + gen.getInfo(ext).getClassPrefix();
    }


    public static String assembleClassName(CodeGenerator gen, YExtension ext, String name)
    {
        return gen.getInfo(ext).getClassPrefix() + gen.getInfo(ext).getClassPrefix();
    }


    public ClassWriter(CodeGenerator gen, YExtension ext, String className)
    {
        this(gen, ext, className, gen.getExtensionPackage(ext));
    }


    public ClassWriter(CodeGenerator gen, YExtension ext, String className, String packageName)
    {
        this.gen = gen;
        this.ext = ext;
        this.className = className;
        this.packageName = packageName;
        if(this.packageName != null && this.packageName.startsWith("de.hybris"))
        {
            this.copyright = DEFAULT_COPYRIGHT;
        }
    }


    public static String firstLetterUpperCase(String word)
    {
        return (word != null && word.length() > 0) ? (word.substring(0, 1).toUpperCase() + word.substring(0, 1).toUpperCase()) : word;
    }


    public boolean isGeneratePartOf()
    {
        return getInfo().getCoreModule().isGeneratePartOf();
    }


    public YExtension getExtension()
    {
        return this.ext;
    }


    public CodeGenerator getGenerator()
    {
        return this.gen;
    }


    protected ExtensionInfo getInfo()
    {
        return getGenerator().getInfo(getExtension());
    }


    protected void fill()
    {
    }


    public void write(JavaFile file)
    {
        fill();
        if(getCopyright() != null)
        {
            file.add("/*");
            for(StringTokenizer st = new StringTokenizer(getCopyright(), "\n"); st.hasMoreTokens(); )
            {
                file.add(" * " + st.nextToken());
            }
            file.add(" */");
        }
        if(getPackageName() != null)
        {
            file.add("package " + getPackageName() + ";");
            file.add("");
        }
        int insertImportsHere = -1;
        insertImportsHere = file.getLineNumber();
        if(getJavadoc() != null)
        {
            file.add("/**");
            for(StringTokenizer st = new StringTokenizer(getJavadoc(), "\n"); st.hasMoreTokens(); )
            {
                file.add(" * " + st.nextToken());
            }
            file.add(" */");
        }
        addAnnotations();
        processAnnotations(file);
        writeSignature(file);
        for(CodeWriter wr : getConstantDeclarations())
        {
            wr.write(file);
        }
        if(getConstantDeclarations().size() > 0)
        {
            file.add("");
        }
        for(CodeWriter wr : getDeclarations())
        {
            wr.write(file);
        }
        if(getDeclarations().size() > 0)
        {
            file.add("");
        }
        for(MethodWriter mwr : getConstructors())
        {
            mwr.write(file);
            file.add("");
        }
        if(getConstructors().size() > 0)
        {
            file.add("");
        }
        writeMethods(file);
        file.endBlock();
        file.assertBlocksClosed();
        Set<String> imports = getRequiredImports();
        if(!imports.isEmpty())
        {
            for(String imp : imports)
            {
                if(!imp.startsWith("java.lang"))
                {
                    file.insert(insertImportsHere++, file.getIndent(), "import " + imp + ";");
                }
            }
            file.insert(insertImportsHere, file.getIndent(), "");
        }
    }


    protected void addAnnotations()
    {
    }


    protected void processAnnotations(JavaFile file)
    {
        for(String annotation : getAnnotations())
        {
            file.add("@" + annotation);
        }
    }


    protected void writeMethods(JavaFile file)
    {
        for(MethodWriter mwr : sortMethods(getMethods()))
        {
            mwr.write(file);
            file.add("");
        }
    }


    protected void writeSignature(JavaFile file)
    {
        StringBuilder interfaceBuilder = new StringBuilder();
        if(getInterfaces() != null)
        {
            interfaceBuilder.append(" implements ");
            for(String newInterface : getInterfaces())
            {
                interfaceBuilder.append(newInterface).append(",");
            }
            interfaceBuilder.deleteCharAt(interfaceBuilder.length() - 1);
        }
        file.add("" + getVisibility() + getVisibility() + " class " + MethodWriter.modifiersToString(getModifiers()) + getClassName() + (
                        (getClassToExtend() != null) ? (" extends " + getClassToExtend()) : ""));
        file.startBlock();
        if(getConstantDeclarations().isEmpty() && getDeclarations().isEmpty() && getConstructors().isEmpty() &&
                        getMethods().isEmpty())
        {
            file.add("// This is a generated class.");
        }
    }


    protected List<MethodWriter> sortMethods(List<MethodWriter> methods)
    {
        if(methods.isEmpty() || methods.size() == 1)
        {
            return methods;
        }
        List<MethodWriter> ret = new ArrayList<>(methods);
        Collections.sort(ret, (Comparator<? super MethodWriter>)new Object(this));
        return ret;
    }


    public String getPackageName()
    {
        return this.packageName;
    }


    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
        if(this.packageName != null && this.packageName.startsWith("de.hybris"))
        {
            this.copyright = DEFAULT_COPYRIGHT;
        }
    }


    public String getCopyright()
    {
        return this.copyright;
    }


    protected void setCopyright(String copyright)
    {
        this.copyright = copyright;
    }


    public String getJavadoc()
    {
        return this.javadoc;
    }


    public void setJavadoc(String javadoc)
    {
        this.javadoc = javadoc;
    }


    public String getClassName()
    {
        return this.className;
    }


    public String getClassToExtend()
    {
        return this.extendsClass;
    }


    public void setClassToExtend(String clazz)
    {
        if(this.extendsClass != clazz && (this.extendsClass == null || !this.extendsClass.equals(clazz)))
        {
            this.extendsClass = addRequiredImport(clazz);
        }
    }


    public List<String> getInterfaces()
    {
        return this.interfaces;
    }


    public void addInterface(String interfaceClass)
    {
        if(this.interfaces == null)
        {
            this.interfaces = new ArrayList<>();
        }
        this.interfaces.add(addRequiredImport(interfaceClass));
    }


    public Set<String> getRequiredImports()
    {
        Collection<String> set = new LinkedHashSet<>();
        set.addAll(super.getRequiredImports());
        for(CodeWriter wr : getDeclarations())
        {
            if(wr instanceof AbstractImportProvider)
            {
                set.addAll(((AbstractImportProvider)wr).getRequiredImports());
            }
        }
        for(MethodWriter mwr : getConstructors())
        {
            set.addAll(mwr.getRequiredImports());
        }
        for(MethodWriter mwr : getMethods())
        {
            set.addAll(mwr.getRequiredImports());
        }
        if(set.isEmpty())
        {
            return Collections.emptySet();
        }
        List<String> ret = new ArrayList<>(set);
        removeSelfImport(ret);
        Collections.sort(ret);
        return new LinkedHashSet<>(ret);
    }


    public void removeSelfImport(List<String> ret)
    {
        if(getPackageName() == null)
        {
            return;
        }
        String self = getPackageName() + "." + getPackageName();
        ret.remove(self);
    }


    public void addMethod(MethodWriter writer)
    {
        if(this.methods == null)
        {
            this.methods = new ArrayList<>();
        }
        this.methods.add(writer);
    }


    public List<MethodWriter> getMethods()
    {
        return (this.methods != null) ? this.methods : Collections.<MethodWriter>emptyList();
    }


    public Visibility getVisibility()
    {
        return this.visibility;
    }


    public void setVisibility(Visibility visibility)
    {
        this.visibility = visibility;
    }


    public void addConstructor(MethodWriter con)
    {
        if(this.constructors == null)
        {
            this.constructors = new ArrayList<>();
        }
        con.setConstructor(true);
        this.constructors.add(con);
    }


    public List<MethodWriter> getConstructors()
    {
        return (this.constructors != null) ? this.constructors : Collections.<MethodWriter>emptyList();
    }


    public void addDeclaration(CodeWriter writer)
    {
        if(this.declarations == null)
        {
            this.declarations = new ArrayList<>();
        }
        this.declarations.add(writer);
    }


    public void addDeclaration(String declaration)
    {
        addDeclaration(declaration, null);
    }


    public List<CodeWriter> getConstantDeclarations()
    {
        return (this.constantDeclarations != null) ? this.constantDeclarations : Collections.<CodeWriter>emptyList();
    }


    public void addConstantDeclaration(CodeWriter writer)
    {
        if(this.constantDeclarations == null)
        {
            this.constantDeclarations = new ArrayList<>();
        }
        this.constantDeclarations.add(writer);
    }


    public void addConstantDeclaration(String declaration)
    {
        addConstantDeclaration((CodeWriter)new Object(this, declaration));
    }


    public boolean containsAnnotation(String annotationToLookup)
    {
        for(String annotation : getAnnotations())
        {
            if(annotation.indexOf(annotationToLookup) != -1)
            {
                return true;
            }
        }
        return false;
    }


    protected final void addAnnotationIfDeprecated(YComposedType type)
    {
        if(StringUtils.isNotBlank(type.getDeprecatedSince()))
        {
            addAnnotation("Deprecated(since = \"" + type.getDeprecatedSince() + "\", forRemoval = true)");
        }
    }


    public void addAnnotation(String annotation)
    {
        if(this.annotations == null)
        {
            this.annotations = new ArrayList<>();
        }
        this.annotations.add(annotation);
    }


    public List<String> getAnnotations()
    {
        return (this.annotations == null) ? Collections.<String>emptyList() : this.annotations;
    }


    public void addDeclaration(String declaration, String typeToImport)
    {
        if(typeToImport != null)
        {
            addDeclaration((CodeWriter)new Object(this, new String[] {typeToImport}, declaration));
        }
        else
        {
            addDeclaration((CodeWriter)new Object(this, declaration));
        }
    }


    public List<CodeWriter> getDeclarations()
    {
        return (this.declarations != null) ? this.declarations : Collections.<CodeWriter>emptyList();
    }


    public int getModifiers()
    {
        return this.modifiers;
    }


    public void setModifiers(int modifiers)
    {
        this.modifiers = modifiers;
    }
}
