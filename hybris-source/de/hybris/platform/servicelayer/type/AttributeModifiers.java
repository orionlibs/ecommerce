package de.hybris.platform.servicelayer.type;

public final class AttributeModifiers
{
    public static final int READ = 1;
    public static final int WRITE = 2;
    public static final int REMOVE = 4;
    public static final int OPTIONAL = 8;
    public static final int SEARCH = 16;
    public static final int PARTOF = 32;
    public static final int PRIVATE = 128;
    public static final int PROPERTY = 256;
    public static final int LOCALIZED = 512;
    public static final int INHERITED = 1024;
    public static final int INITIAL = 2048;
    public static final int TIM_IGNORE = 8192;
    public static final int ENCRYPTED = 16384;
    public static final int PRIMITIVE = 65536;
    public static final int ALL = 94143;
    public static final AttributeModifierCriteria ALL_CRITERIA = (AttributeModifierCriteria)new ImmutableAttributeModifierCriteria(0, 94143, 0);
    public static final AttributeModifierCriteria ALL_PUBLIC_CRITERIA = (AttributeModifierCriteria)new ImmutableAttributeModifierCriteria(0, 94143, 128);
    public static final AttributeModifierCriteria ALL_PRIVATE_CRITERIA = (AttributeModifierCriteria)new ImmutableAttributeModifierCriteria(128, 0, 0);


    private AttributeModifiers()
    {
        throw new AssertionError("This class cannot be instantiated");
    }
}
