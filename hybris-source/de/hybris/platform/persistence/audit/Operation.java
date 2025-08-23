package de.hybris.platform.persistence.audit;

import com.google.common.base.MoreObjects;
import de.hybris.platform.core.PK;
import java.util.Objects;

public final class Operation
{
    private final Type type;
    private final PK itemPk;
    private final PK itemTypePk;
    private final String typeCode;


    private Operation(Type type, PK itemPk, PK itemTypePk)
    {
        this(type, itemPk, Objects.<PK>requireNonNull(itemTypePk, "itemTypePk mustn't be null"), null);
    }


    private Operation(Type type, PK itemPk, String typeCode)
    {
        this(type, itemPk, null, Objects.<String>requireNonNull(typeCode, "typeCode mustn't be null"));
    }


    private Operation(Type type, PK itemPk, PK itemTypePk, String typeCode)
    {
        this.type = Objects.<Type>requireNonNull(type, "type mustn't be null");
        this.itemPk = Objects.<PK>requireNonNull(itemPk, "itemPk mustn't be null");
        this.itemTypePk = itemTypePk;
        this.typeCode = typeCode;
    }


    public static Operation create(PK itemPk, PK itemTypePk)
    {
        return new Operation(Type.CREATE, itemPk, itemTypePk);
    }


    public static Operation update(PK itemPk, PK itemTypePk)
    {
        return new Operation(Type.UPDATE, itemPk, itemTypePk);
    }


    public static Operation delete(PK itemPk, PK itemTypePk)
    {
        return new Operation(Type.DELETE, itemPk, itemTypePk);
    }


    public static Operation create(PK itemPk, String typeCode)
    {
        return new Operation(Type.CREATE, itemPk, typeCode);
    }


    public static Operation update(PK itemPk, String typeCode)
    {
        return new Operation(Type.UPDATE, itemPk, typeCode);
    }


    public static Operation delete(PK itemPk, String typeCode)
    {
        return new Operation(Type.DELETE, itemPk, typeCode);
    }


    public String toString()
    {
        return MoreObjects.toStringHelper(this).add("type", this.type).add("itemPk", this.itemPk).toString();
    }


    public boolean isCreation()
    {
        return Type.CREATE.equals(this.type);
    }


    public boolean isDeletion()
    {
        return Type.DELETE.equals(this.type);
    }


    public PK getItemPk()
    {
        return this.itemPk;
    }


    public PK getItemTypePk()
    {
        return this.itemTypePk;
    }


    public boolean isTypeIdentifiedByPk()
    {
        return (this.itemTypePk != null);
    }


    public String getItemTypeCode()
    {
        return this.typeCode;
    }
}
