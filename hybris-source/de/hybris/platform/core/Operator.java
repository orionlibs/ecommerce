package de.hybris.platform.core;

import com.google.common.collect.ImmutableSet;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public final class Operator extends FlexibleSearchTranslatable
{
    public static final Operator EQUAL = new Operator(1, "=", "equal");
    public static final Operator UNEQUAL = new Operator(2, "<>", "<>");
    public static final Operator LIKE = new Operator(3, "LIKE", "like");
    public static final Operator STARTS_WITH = new Operator(4, "LIKE", "starts with");
    public static final Operator ENDS_WITH = new Operator(5, "LIKE", "ends with");
    public static final Operator CONTAINS = new Operator(6, "LIKE", "contains");
    public static final Operator GREATER = new Operator(7, ">", ">");
    public static final Operator LESS = new Operator(8, "<", "<");
    public static final Operator GREATER_OR_EQUAL = new Operator(9, ">=", ">=");
    public static final Operator LESS_OR_EQUAL = new Operator(10, "<=", "<=");
    public static final Operator IS_NOT_NULL = new Operator(11, "IS NOT NULL", "is not null");
    public static final Operator IS_NULL = new Operator(12, "IS NULL", "is null");
    public static final Operator IN = new Operator(13, "IN", "in");
    public static final Operator NOT_IN = new Operator(14, "NOT IN", "not in");
    public static final Operator EXISTS = new Operator(15, "EXISTS", "exists");
    public static final Operator NOT_EXISTS = new Operator(16, "NOT EXISTS", "not exists");
    public static final Operator AND = new Operator(17, "AND", "and");
    public static final Operator OR = new Operator(18, "OR", "or ");
    public static final Operator NOT_LIKE = new Operator(19, "NOT LIKE", "not like");
    public static final Operator DEFAULT = EQUAL;
    private final int code;
    private final String code_str;
    private final String sql_str;
    private static final Set<Integer> TRANSLATABLE_OPERATORS = (Set<Integer>)ImmutableSet.copyOf(Arrays.asList(new Integer[] {Integer.valueOf(EQUAL.code), Integer.valueOf(UNEQUAL.code),
                    Integer.valueOf(GREATER.code), Integer.valueOf(LESS.code),
                    Integer.valueOf(GREATER_OR_EQUAL.code),
                    Integer.valueOf(LESS_OR_EQUAL.code), Integer.valueOf(AND.code),
                    Integer.valueOf(OR.code), Integer.valueOf(IS_NOT_NULL.code),
                    Integer.valueOf(IS_NULL.code)}));


    protected Operator(int code, String sql_str, String code_str)
    {
        this.code = code;
        this.code_str = code_str;
        this.sql_str = sql_str;
    }


    public int getCode()
    {
        return this.code;
    }


    public String getStringCode()
    {
        return this.code_str;
    }


    public String getSQLString()
    {
        return this.sql_str;
    }


    public void toFlexibleSearch(StringBuilder queryBuffer, Map<String, String> typeIndexMap, Map<String, Object> valueMap)
    {
        queryBuffer.append(" ").append(getSQLString()).append(" ");
    }


    public static Operator getOperatorByStringCode(String str_code)
    {
        if(EQUAL.getStringCode().equals(str_code))
        {
            return EQUAL;
        }
        if(UNEQUAL.getStringCode().equals(str_code))
        {
            return UNEQUAL;
        }
        if(LIKE.getStringCode().equals(str_code))
        {
            return LIKE;
        }
        if(STARTS_WITH.getStringCode().equals(str_code))
        {
            return STARTS_WITH;
        }
        if(ENDS_WITH.getStringCode().equals(str_code))
        {
            return ENDS_WITH;
        }
        if(CONTAINS.getStringCode().equals(str_code))
        {
            return CONTAINS;
        }
        if(GREATER.getStringCode().equals(str_code))
        {
            return GREATER;
        }
        if(LESS.getStringCode().equals(str_code))
        {
            return LESS;
        }
        if(GREATER_OR_EQUAL.getStringCode().equals(str_code))
        {
            return GREATER_OR_EQUAL;
        }
        if(LESS_OR_EQUAL.getStringCode().equals(str_code))
        {
            return LESS_OR_EQUAL;
        }
        if(IS_NULL.getStringCode().equals(str_code))
        {
            return IS_NULL;
        }
        if(IS_NOT_NULL.getStringCode().equals(str_code))
        {
            return IS_NOT_NULL;
        }
        if(IN.getStringCode().equals(str_code))
        {
            return IN;
        }
        if(NOT_IN.getStringCode().equals(str_code))
        {
            return NOT_IN;
        }
        if(EXISTS.getStringCode().equals(str_code))
        {
            return EXISTS;
        }
        if(NOT_EXISTS.getStringCode().equals(str_code))
        {
            return NOT_EXISTS;
        }
        if(AND.getStringCode().equals(str_code))
        {
            return AND;
        }
        if(OR.getStringCode().equals(str_code))
        {
            return OR;
        }
        if(NOT_LIKE.getStringCode().equals(str_code))
        {
            return NOT_LIKE;
        }
        throw new IllegalArgumentException("Illegal operator code " + str_code + ".");
    }


    public static Operator getOperatorByCode(int code)
    {
        switch(code)
        {
            case 1:
                return EQUAL;
            case 2:
                return UNEQUAL;
            case 3:
                return LIKE;
            case 4:
                return STARTS_WITH;
            case 5:
                return ENDS_WITH;
            case 6:
                return CONTAINS;
            case 7:
                return GREATER;
            case 8:
                return LESS;
            case 9:
                return GREATER_OR_EQUAL;
            case 10:
                return LESS_OR_EQUAL;
            case 11:
                return IS_NOT_NULL;
            case 12:
                return IS_NULL;
            case 13:
                return IN;
            case 14:
                return NOT_IN;
            case 15:
                return EXISTS;
            case 16:
                return NOT_EXISTS;
            case 17:
                return AND;
            case 18:
                return OR;
            case 19:
                return NOT_LIKE;
        }
        return DEFAULT;
    }


    public String toString()
    {
        return getStringCode() + "(" + getStringCode() + ")";
    }


    public boolean equals(Object arg0)
    {
        if(this == arg0)
        {
            return true;
        }
        if(!(arg0 instanceof Operator))
        {
            return false;
        }
        return (((Operator)arg0).getCode() == getCode());
    }


    public int hashCode()
    {
        return getCode();
    }


    public boolean allowsCollection()
    {
        return (IN.equals(this) || NOT_IN.equals(this));
    }


    public boolean isUnary()
    {
        return (IS_NOT_NULL.equals(this) || IS_NULL.equals(this) || EXISTS.equals(this) || NOT_EXISTS.equals(this));
    }


    public boolean isTranslatableToPolyglotDialect()
    {
        int myCode = getCode();
        return TRANSLATABLE_OPERATORS.contains(Integer.valueOf(myCode));
    }
}
