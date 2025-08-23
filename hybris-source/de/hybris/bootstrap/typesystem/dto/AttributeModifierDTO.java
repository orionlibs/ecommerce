package de.hybris.bootstrap.typesystem.dto;

import org.apache.log4j.Logger;

public class AttributeModifierDTO
{
    private static final Logger LOG = Logger.getLogger(AttributeModifierDTO.class);
    private final int modifiers;


    public AttributeModifierDTO(int modifiers)
    {
        this.modifiers = modifiers;
    }


    public boolean isDontOptimize()
    {
        return ((this.modifiers & 0x2000) == 8192);
    }


    public boolean isOptional()
    {
        return ((this.modifiers & 0x8) == 8);
    }


    public boolean isPartOf()
    {
        return ((this.modifiers & 0x20) == 32);
    }


    public boolean isInitial()
    {
        return ((this.modifiers & 0x800) == 2048);
    }


    public boolean isProperty()
    {
        return ((this.modifiers & 0x100) == 256);
    }


    public int asInt()
    {
        return this.modifiers;
    }


    public String toString()
    {
        return "AttributeModifierDTO{modifiers=" + this.modifiers + "} {" +
                        Integer.toBinaryString(this.modifiers) + "}";
    }


    public AttributeModifierDTO withOptimize(boolean flag)
    {
        return new AttributeModifierDTO(flag ? (this.modifiers & 0xFFFFDFFF) : (this.modifiers | 0x2000));
    }
}
