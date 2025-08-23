package de.hybris.platform.cockpit.model.meta;

public interface PropertyDescriptor
{
    public static final String QUALIFIER_DELIM = ".";
    public static final String QUALIFIER_DELIM_EXP = "\\.";
    public static final String TEXT = "TEXT";
    public static final String DECIMAL = "DECIMAL";
    public static final String FLOAT = "FLOAT";
    public static final String INTEGER = "INTEGER";
    public static final String LONG = "LONG";
    public static final String BOOLEAN = "BOOLEAN";
    public static final String DATE = "DATE";
    public static final String ENUM = "ENUM";
    public static final String PK = "PK";
    public static final String REFERENCE = "REFERENCE";
    public static final String FEATURE = "FEATURE";


    String getQualifier();


    boolean isLocalized();


    Occurrence getOccurence();


    String getEditorType();


    Multiplicity getMultiplicity();


    String getName();


    String getName(String paramString);


    String getDescription();


    boolean isReadable();


    boolean isWritable();


    String getSelectionOf();
}
