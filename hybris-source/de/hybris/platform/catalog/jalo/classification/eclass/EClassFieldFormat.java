package de.hybris.platform.catalog.jalo.classification.eclass;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;

public class EClassFieldFormat
{
    private final String format;
    private String fieldType;


    public static String getClassificationNumberForExport(String classNumber)
    {
        StringBuilder exportNumber = new StringBuilder();
        for(int i = 0; i <= 6; i += 2)
        {
            exportNumber.append(classNumber.substring(i, i + 2));
            if(i < 6)
            {
                exportNumber.append("-");
            }
        }
        return exportNumber.toString();
    }


    public static EnumerationValue convertToAttributeType(String fieldFormat)
    {
        return (new EClassFieldFormat(fieldFormat)).getClassificationAttributeType();
    }


    private boolean variable = false;
    private boolean signed = false;
    private int length1 = 0;
    private int length2 = 0;
    public static final char EXP = 'E';
    public static final char SIGNED = 'S';
    public static final String NUMBER_DOUBLE = "NR3";
    public static final String NUMBER_DECIMAL = "NR2";
    public static final String NUMBER_INT = "NR1";
    public static final String BOOLEAN = "V";
    public static final String CHAR_ALPHANUMERICAL = "X";
    public static final String CHAR_NUMERICAL = "N";
    public static final String CHAR_MIXED = "M";
    public static final String CHAR_ALPHA = "A";


    public EClassFieldFormat(String pattern)
    {
        this.format = pattern.trim();
        parse();
    }


    protected void parse()
    {
        StringBuilder stringBuilder = new StringBuilder();
        String formatUpper = this.format.toUpperCase();
        if(formatUpper.startsWith("NR1"))
        {
            this.fieldType = "NR1";
        }
        else if(formatUpper.startsWith("NR3"))
        {
            this.fieldType = "NR3";
        }
        else if(formatUpper.startsWith("NR2"))
        {
            this.fieldType = "NR2";
        }
        else if(formatUpper.startsWith("V"))
        {
            this.fieldType = "V";
        }
        else if(formatUpper.startsWith("X"))
        {
            this.fieldType = "X";
        }
        else if(formatUpper.startsWith("M"))
        {
            this.fieldType = "M";
        }
        else if(formatUpper.startsWith("A"))
        {
            this.fieldType = "A";
        }
        else if(formatUpper.startsWith("N"))
        {
            this.fieldType = "N";
        }
        else
        {
            throw new IllegalArgumentException("invalid eclass format '" + this.format + "' - no known field type");
        }
        int pos = this.fieldType.length();
        int formatLenght = this.format.length();
        if(pos < formatLenght)
        {
            char character = this.format.charAt(pos);
            if(character == ' ')
            {
                this.variable = false;
                for(; pos < formatLenght && this.format.charAt(pos) == ' '; pos++)
                    ;
            }
            else if(character == '.')
            {
                this.variable = true;
                for(; pos < formatLenght && (this.format.charAt(pos) == '.' || this.format.charAt(pos) == ' '); pos++)
                    ;
            }
            else
            {
                this.variable = true;
            }
        }
        if("V".equals(this.fieldType) || pos >= formatLenght)
        {
            return;
        }
        if(isText())
        {
            while(pos < formatLenght)
            {
                char character = this.format.charAt(pos);
                if(Character.isDigit(character))
                {
                    stringBuilder.append(character);
                    pos++;
                }
            }
            this.length1 = Integer.parseInt(stringBuilder.toString());
            stringBuilder.setLength(0);
        }
        else
        {
            if(this.format.charAt(pos) == 'S')
            {
                this.signed = true;
                pos++;
                char character = this.format.charAt(pos);
                if(character == ' ')
                {
                    this.variable = false;
                    for(; pos < formatLenght && this.format.charAt(pos) == ' '; pos++)
                        ;
                }
                else if(character == '.')
                {
                    this.variable = true;
                    for(; pos < formatLenght && (this.format.charAt(pos) == '.' || this.format.charAt(pos) == ' '); pos++)
                        ;
                }
            }
            while(pos < formatLenght)
            {
                char character = this.format.charAt(pos);
                if(Character.isDigit(character))
                {
                    stringBuilder.append(character);
                    pos++;
                }
            }
            this.length1 = Integer.parseInt(stringBuilder.toString());
            stringBuilder.setLength(0);
            if(pos < formatLenght && this.format.charAt(pos) == '.')
            {
                pos++;
                while(pos < formatLenght)
                {
                    char character = this.format.charAt(pos);
                    if(Character.isDigit(character))
                    {
                        stringBuilder.append(character);
                        pos++;
                    }
                }
                this.length2 = Integer.parseInt(stringBuilder.toString());
                stringBuilder.setLength(0);
            }
        }
    }


    protected boolean isText()
    {
        return ("X".equals(this.fieldType) || "M".equals(this.fieldType) || "N"
                        .equals(this.fieldType) || "A".equals(this.fieldType));
    }


    public EnumerationValue getClassificationAttributeType()
    {
        if(isText())
        {
            if("X".equals(this.fieldType) && this.length1 == 1)
            {
                return EnumerationManager.getInstance().getEnumerationValue(GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM, GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.BOOLEAN);
            }
            return EnumerationManager.getInstance().getEnumerationValue(GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM, GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.STRING);
        }
        if("V".equals(this.fieldType))
        {
            return EnumerationManager.getInstance().getEnumerationValue(GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM, GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.BOOLEAN);
        }
        return EnumerationManager.getInstance().getEnumerationValue(GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTETYPEENUM, GeneratedCatalogConstants.Enumerations.ClassificationAttributeTypeEnum.NUMBER);
    }


    public String getFieldType()
    {
        return this.fieldType;
    }


    public boolean isFixedLength()
    {
        return !this.variable;
    }


    public boolean isSigned()
    {
        return this.signed;
    }


    public int getFieldLength()
    {
        return this.length1;
    }


    public int getFractionDigits()
    {
        return this.length2;
    }
}
