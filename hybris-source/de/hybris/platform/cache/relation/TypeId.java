package de.hybris.platform.cache.relation;

import de.hybris.platform.core.PK;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

public final class TypeId
{
    private final PK typePk;
    private final String typeName;
    private final Integer typeCode;


    TypeId(PK typePk, String typeName, Integer typeCode)
    {
        if(typePk == null && typeName == null && typeCode == null)
        {
            throw new IllegalArgumentException("At least one non null attribute is required.");
        }
        this.typePk = typePk;
        this.typeName = typeName;
        this.typeCode = typeCode;
    }


    public static TypeId fromTypePk(PK typePk)
    {
        Objects.requireNonNull(typePk);
        return new TypeId(typePk, null, null);
    }


    public static TypeId fromTypeName(String qualifier)
    {
        Objects.requireNonNull(qualifier);
        return new TypeId(null, qualifier, null);
    }


    public static TypeId fromTypeCode(int typeCode)
    {
        if(typeCode <= 0)
        {
            throw new IllegalArgumentException("Only positive type codes are allowed");
        }
        return new TypeId(null, null, Integer.valueOf(typeCode));
    }


    Optional<PK> getTypePk()
    {
        return Optional.ofNullable(this.typePk);
    }


    Optional<String> getTypeName()
    {
        return Optional.ofNullable(this.typeName);
    }


    Optional<Integer> getTypeCode()
    {
        return Optional.ofNullable(this.typeCode);
    }


    public String toString()
    {
        if(this.typePk != null)
        {
            return String.format(Locale.ROOT, "TypeId{typePk=%s}", new Object[] {Long.valueOf(this.typePk.getLongValue())});
        }
        if(this.typeName != null)
        {
            return String.format(Locale.ROOT, "TypeId{typeName=%s}", new Object[] {this.typeName});
        }
        return String.format(Locale.ROOT, "TypeId{typeCode=%s}", new Object[] {this.typeCode});
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        TypeId typeId = (TypeId)o;
        return (Objects.equals(this.typePk, typeId.typePk) && Objects.equals(this.typeName, typeId.typeName) &&
                        Objects.equals(this.typeCode, typeId.typeCode));
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.typePk, this.typeName, this.typeCode});
    }
}
