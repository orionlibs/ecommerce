package de.hybris.bootstrap.beangenerator;

import com.google.common.base.Preconditions;
import de.hybris.bootstrap.beangenerator.definitions.model.BeanPrototype;
import de.hybris.bootstrap.beangenerator.definitions.model.ClassNameAware;
import de.hybris.bootstrap.beangenerator.definitions.model.ClassNamePrototype;
import de.hybris.bootstrap.beangenerator.definitions.model.EnumPrototype;
import de.hybris.bootstrap.beangenerator.definitions.model.EnumValuePrototype;
import de.hybris.bootstrap.beangenerator.definitions.model.PropertyPrototype;
import de.hybris.bootstrap.beangenerator.definitions.view.EnumValue;
import de.hybris.bootstrap.beangenerator.definitions.view.MemberVariable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.time.Year;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.ResourceNotFoundException;

public class BeanSourceFileProcessor
{
    protected static final Logger LOG = Logger.getLogger(BeanSourceFileProcessor.class);
    private final VelocityEngine velocityEngine;
    private final File gensrcDirectory;


    public BeanSourceFileProcessor(String generateTargetDirName)
    {
        Preconditions.checkArgument((new File(generateTargetDirName)).exists(), "Given target generation dir [" + generateTargetDirName + "] does not exist");
        this.velocityEngine = new VelocityEngine();
        this.velocityEngine.setProperty("runtime.strict_mode.enable", Boolean.TRUE);
        this.velocityEngine.setProperty("resource.default_encoding", "UTF-8");
        this.velocityEngine.setProperty("output.encoding", "UTF-8");
        this.velocityEngine.init();
        this.gensrcDirectory = new File(generateTargetDirName);
    }


    public void generateBeanSourceFile(ClassNameAware bean, File velocityTemplateFile)
    {
        VelocityContext ctx = createVelocityContext(bean);
        File srcFile = getFileFromClassName(bean.getClassName().getBaseClass());
        (new File(srcFile.getParent())).mkdirs();
        try
        {
            Writer fileWriter = new OutputStreamWriter(new FileOutputStream(srcFile), StandardCharsets.UTF_8);
            try
            {
                Reader templateReader = new InputStreamReader(new FileInputStream(velocityTemplateFile), StandardCharsets.UTF_8);
                try
                {
                    this.velocityEngine.evaluate((Context)ctx, fileWriter, "beangenerator", templateReader);
                    templateReader.close();
                }
                catch(Throwable throwable)
                {
                    try
                    {
                        templateReader.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                    throw throwable;
                }
                fileWriter.close();
            }
            catch(Throwable throwable)
            {
                try
                {
                    fileWriter.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
                throw throwable;
            }
        }
        catch(IOException e)
        {
            throw new ResourceNotFoundException(e.getMessage(), e);
        }
    }


    protected File getFileFromClassName(String className)
    {
        return new File(this.gensrcDirectory, className.replace('.', '/') + ".java");
    }


    protected VelocityContext createVelocityContext(ClassNameAware beanPrototype)
    {
        VelocityContext ctx = new VelocityContext();
        ctx.put("currentDateTime", DateFormat.getDateTimeInstance().format(new Date()));
        ctx.put("currentYear", Integer.valueOf(Year.now().getValue()));
        ctx.put("packageName", ClassNameUtil.getPackageName(beanPrototype.getClassName().getBaseClass()));
        ctx.put("shortClassName", ClassNameUtil.getShortClassName(beanPrototype.getClassName()));
        ctx.put("StringUtils", StringUtils.class);
        if(beanPrototype instanceof BeanPrototype)
        {
            return decorateVelocityContext(ctx, (BeanPrototype)beanPrototype);
        }
        if(beanPrototype instanceof EnumPrototype)
        {
            return decorateVelocityContext(ctx, (EnumPrototype)beanPrototype);
        }
        throw new IllegalArgumentException("Not supported type " + beanPrototype);
    }


    protected VelocityContext decorateVelocityContext(VelocityContext ctx, EnumPrototype bean)
    {
        ctx.put("enumValue", createEnumValues(bean));
        ctx.put("hasDescription", Boolean.valueOf(StringUtils.isNotBlank(bean.getDescription())));
        ctx.put("description", bean.getDescription());
        ctx.put("hasDeprecated", Boolean.valueOf(StringUtils.isNotBlank(bean.getDeprecated())));
        ctx.put("deprecated", bean.getDeprecated());
        ctx.put("hasDeprecatedSince", Boolean.valueOf(StringUtils.isNotBlank(bean.getDeprecatedSince())));
        ctx.put("deprecatedSince", bean.getDeprecatedSince());
        return ctx;
    }


    protected VelocityContext decorateVelocityContext(VelocityContext ctx, BeanPrototype bean)
    {
        if(bean.getSuperclassName() != null)
        {
            if(shortClassNameSameAsShortSuperclassName(bean))
            {
                ctx.put("superclassName", bean.getSuperclassName());
            }
            else
            {
                ctx.put("superclassName", ClassNameUtil.getShortClassName(bean.getSuperclassName()));
            }
        }
        ctx.put("constructorName", ClassNameUtil.getShortClassName(bean.getClassName().getBaseClass()));
        ctx.put("memberVariables", createMemberVariables(bean, bean.getProperties()));
        ctx.put("imports", createImportList(bean));
        ctx.put("hints", bean.getHints());
        ctx.put("hasDescription", Boolean.valueOf(StringUtils.isNotBlank(bean.getDescription())));
        ctx.put("description", bean.getDescription());
        ctx.put("hasDeprecated", Boolean.valueOf(StringUtils.isNotBlank(bean.getDeprecated())));
        ctx.put("deprecated", bean.getDeprecated());
        ctx.put("hasDeprecatedSince", Boolean.valueOf(StringUtils.isNotBlank(bean.getDeprecatedSince())));
        ctx.put("deprecatedSince", bean.getDeprecatedSince());
        ctx.put("hasAnnotations", Boolean.valueOf(StringUtils.isNotBlank(bean.getAnnotations())));
        ctx.put("annotations", bean.getAnnotations());
        ctx.put("isAbstract", Boolean.valueOf(bean.isAbstract()));
        Collection<PropertyPrototype> equalsProps = bean.getEqualsProperties();
        ctx.put("hasEqualsProperties", Boolean.valueOf(!equalsProps.isEmpty()));
        if(!equalsProps.isEmpty())
        {
            ctx.put("equalsProperties", createMemberVariables(bean, equalsProps));
            ctx.put("superEquals", Boolean.valueOf(bean.isSuperEquals()));
        }
        return ctx;
    }


    protected List<MemberVariable> createMemberVariables(BeanPrototype bean, Collection<PropertyPrototype> properties)
    {
        List<MemberVariable> memberVariables = new ArrayList<>();
        for(PropertyPrototype property : properties)
        {
            memberVariables.add(new MemberVariable(bean, property));
        }
        return memberVariables;
    }


    protected List<EnumValue> createEnumValues(EnumPrototype bean)
    {
        List<EnumValue> enumValues = new ArrayList<>();
        for(EnumValuePrototype property : bean.getValues())
        {
            enumValues.add(new EnumValue(bean, property));
        }
        return enumValues;
    }


    private void createImportList(Set<String> imports, ClassNamePrototype classType)
    {
        if(ClassNameUtil.needsImporting(classType))
        {
            imports.add(classType.getBaseClass());
        }
        for(ClassNamePrototype singleSubType : classType.getPrototypes())
        {
            createImportList(imports, singleSubType);
        }
    }


    protected List<String> createImportList(BeanPrototype bean)
    {
        Set<String> imports = new HashSet<>(bean.getDeclaredImports());
        for(PropertyPrototype attribute : bean.getProperties())
        {
            createImportList(imports, attribute.getType());
        }
        if(bean.getSuperclassName() != null && !shortClassNameSameAsShortSuperclassName(bean))
        {
            createImportList(imports, bean.getSuperclassName());
        }
        List<String> importList = new ArrayList<>(imports);
        Collections.sort(importList);
        return importList;
    }


    private boolean shortClassNameSameAsShortSuperclassName(BeanPrototype bean)
    {
        String className = ClassNameUtil.getShortClassName(bean.getClassName());
        String superclassName = ClassNameUtil.getShortClassName(bean.getSuperclassName());
        return className.equals(superclassName);
    }
}
