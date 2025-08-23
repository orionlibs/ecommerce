package de.hybris.bootstrap.beangenerator;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import de.hybris.bootstrap.beangenerator.definitions.model.ClassNamePrototype;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

public final class ClassNameUtil
{
    private static final char DOT_CHAR = '.';
    private static final String COMMA_STRING = ",";
    private static final String GT_STRING = ">";
    private static final String LT_STRING = "<";
    private static final String EMPTY_STRING = "";
    private static final String[] JAVA_KEYWORDS = new String[] {
                    "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char", "class", "const",
                    "continue", "default", "do", "double", "else", "enum", "extends", "false", "final", "finally",
                    "float", "for", "goto", "if", "implements", "import", "instanceof", "int", "interface", "long",
                    "native", "new", "null", "package", "private", "protected", "public", "return", "short", "static",
                    "strictfp", "super", "switch", "synchronized", "this", "throw", "throws", "transient", "true", "try",
                    "void", "volatile", "while"};
    private static final Pattern JAVA_PATTERN = Pattern.compile("(java\\.)(\\w+\\.){0,}(\\w+)");
    private static final Pattern JAVA_LANG_PATTERN = Pattern.compile("java\\.lang\\.[^\\.]*");
    private static final Pattern MODEL_OR_ENUM_PATTERN = Pattern.compile("(de\\.hybris\\.platform\\.)(\\w+\\.){0,}((model\\.)|(enums\\.))(\\w+\\.){0,}(\\w+)");
    private static final String[] JAVA_PRIMITIVE_TYPES = new String[] {"byte", "short", "int", "long", "float", "double", "boolean", "char"};
    static final Collator englishCollator = Collator.getInstance(Locale.ENGLISH);


    public static boolean needsImporting(ClassNamePrototype typeProto)
    {
        return needsImporting(typeProto.getBaseClass());
    }


    public static boolean needsImporting(String type)
    {
        if(type.indexOf('.') == -1)
        {
            return false;
        }
        if(ArrayUtils.contains((Object[])JAVA_PRIMITIVE_TYPES, type))
        {
            return false;
        }
        return !JAVA_LANG_PATTERN.matcher(type).matches();
    }


    public static boolean isJavaKeyword(String literal)
    {
        return (Arrays.binarySearch(JAVA_KEYWORDS, literal, englishCollator) >= 0);
    }


    public static boolean isPlatformModelOrEnum(String literal)
    {
        return MODEL_OR_ENUM_PATTERN.matcher(literal).matches();
    }


    public static boolean isJavaClass(String literal)
    {
        return JAVA_PATTERN.matcher(literal).matches();
    }


    public static String getPackageName(String className)
    {
        if(StringUtils.isBlank(className))
        {
            return null;
        }
        int i = className.lastIndexOf('.');
        return className.substring(0, i);
    }


    public static String getShortClassName(String className)
    {
        if(StringUtils.isBlank(className))
        {
            return null;
        }
        int extendsIndex = className.lastIndexOf("extends");
        if(extendsIndex > -1)
        {
            return className;
        }
        int i = className.lastIndexOf('.');
        return className.substring(i + 1);
    }


    public static String getShortClassName(ClassNamePrototype className)
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append(getShortClassName(className.getBaseClass()));
        if(!className.getPrototypes().isEmpty())
        {
            buffer.append("<");
            for(Iterator<ClassNamePrototype> subClassesIter = className.getPrototypes().iterator(); subClassesIter.hasNext(); )
            {
                ClassNamePrototype subClass = subClassesIter.next();
                buffer.append(getShortClassName(subClass));
                if(subClassesIter.hasNext())
                {
                    buffer.append(",");
                }
            }
            buffer.append(">");
        }
        return buffer.toString();
    }


    public static String shortenType(ClassNamePrototype className)
    {
        StringBuilder buffer = new StringBuilder();
        buffer.append(shortenType(className.getBaseClass()));
        if(!className.getPrototypes().isEmpty())
        {
            buffer.append("<");
            for(Iterator<ClassNamePrototype> subClassesIter = className.getPrototypes().iterator(); subClassesIter.hasNext(); )
            {
                ClassNamePrototype subClass = subClassesIter.next();
                buffer.append(shortenType(subClass));
                if(subClassesIter.hasNext())
                {
                    buffer.append(",");
                }
            }
            buffer.append(">");
        }
        return buffer.toString();
    }


    public static String erasureType(String className)
    {
        int i = className.indexOf("<");
        if(i == -1)
        {
            return className;
        }
        return className.substring(0, i);
    }


    public static String shortenType(String type)
    {
        int i = type.lastIndexOf('.');
        if(i == -1)
        {
            return type;
        }
        return type.substring(i + 1);
    }


    private static int countSubstring(String subStr, String str)
    {
        return (str.length() - str.replace(subStr, "").length()) / subStr.length();
    }


    public static ClassNamePrototype toPrototype(String classNameLiteral)
    {
        Preconditions.checkArgument(StringUtils.isNotBlank(classNameLiteral));
        int starts = countSubstring("<", classNameLiteral);
        int ends = countSubstring(">", classNameLiteral);
        if(starts != ends)
        {
            throw new IllegalArgumentException("Invalid class name identifier " + classNameLiteral);
        }
        if(starts == 0)
        {
            return new ClassNamePrototype(classNameLiteral, new ClassNamePrototype[0]);
        }
        int newEnd = StringUtils.indexOf(classNameLiteral, "<");
        List<ClassNamePrototype> subProtos = new ArrayList<>();
        String subType = processOneSubType(classNameLiteral);
        StringBuilder singleExpression = new StringBuilder();
        for(String single : Splitter.on(",").split(subType))
        {
            try
            {
                singleExpression.append(single);
                subProtos.add(toPrototype(singleExpression.toString()));
                singleExpression = new StringBuilder();
            }
            catch(IllegalArgumentException ile)
            {
                singleExpression.append(",");
            }
        }
        return new ClassNamePrototype(classNameLiteral.substring(0, newEnd), subProtos);
    }


    private static String processOneSubType(String classNameLiteral)
    {
        int newStart = StringUtils.indexOf(classNameLiteral, "<");
        int newEnd = StringUtils.lastIndexOf(classNameLiteral, ">");
        return classNameLiteral.substring(newStart + "<".length(), newEnd);
    }
}
