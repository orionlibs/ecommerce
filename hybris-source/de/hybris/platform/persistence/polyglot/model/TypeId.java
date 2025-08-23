package de.hybris.platform.persistence.polyglot.model;

import de.hybris.platform.persistence.polyglot.PolyglotPersistence;

public class TypeId
{
    final int typecode;
    final Identity identity;


    private TypeId(Identity typeId, int typecode)
    {
        this.identity = typeId;
        this.typecode = typecode;
    }


    public int getTypeCode()
    {
        return this.typecode;
    }


    public boolean isTypeIdentityKnown()
    {
        return this.identity.isKnown();
    }


    public Identity getIdentity()
    {
        return this.identity;
    }


    public static TypeId fromTypeCode(int typeCode)
    {
        return new TypeId(PolyglotPersistence.unknownIdentity(), typeCode);
    }


    public static TypeId fromTypeId(Identity typeId, int typeCode)
    {
        return new TypeId(typeId, typeCode);
    }
}
