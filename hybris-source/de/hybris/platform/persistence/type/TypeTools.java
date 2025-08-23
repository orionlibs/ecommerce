package de.hybris.platform.persistence.type;

import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.persistence.EJBInvalidParameterException;
import de.hybris.platform.persistence.ItemRemote;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class TypeTools
{
    public static void assureAdmissibleRedeclaration(String qualifier, TypeRemote superType, TypeRemote type, int superModifiers, int modifiers) throws EJBInvalidParameterException
    {
        if((modifiers & 0x1) != 0 && type != null && !superType.isAssignableFrom(type))
        {
            throw new EJBInvalidParameterException(null, "cannot change type " + asString(superType) + " to " + asString(type) + ". feature-descriptor " + qualifier + " is redeclared as readable.", 4711);
        }
    }


    public static void setPartOf(ItemRemote item, Collection<?> elements, Binding binding) throws EJBInvalidParameterException
    {
        if(item == null || elements == null || elements.contains(null))
        {
            throw new JaloSystemException(null, "item " + item + " and elements " + elements + " must not be or containing null", 4711);
        }
        Collection<?> oldElements = binding.getElements(item);
        Set toRemove = new HashSet(oldElements);
        toRemove.removeAll(elements);
        Set toAdd = new HashSet(elements);
        toAdd.removeAll(oldElements);
        binding.unbind(toRemove);
        binding.bind(item, toAdd);
    }


    private static final CharacterConstraint DEFAULT_CODE_OR_QUALIFIER_CONSTRAINT = (CharacterConstraint)new Object();
    private static final CharacterConstraint DESCRIPTOR_QUALIFIER_CONSTRAINT = (CharacterConstraint)new Object();


    public static boolean isValidCode(String s)
    {
        return isValidString(s, DEFAULT_CODE_OR_QUALIFIER_CONSTRAINT);
    }


    public static boolean isValidQualifier(String s)
    {
        return isValidString(s, DESCRIPTOR_QUALIFIER_CONSTRAINT);
    }


    public static boolean isValidString(String s, CharacterConstraint cc)
    {
        CharacterIterator iter = new StringCharacterIterator(s);
        int idx = 0;
        for(char c = iter.first(); c != Character.MAX_VALUE; c = iter.next(), idx++)
        {
            if(!cc.isSatisfied(s, c, idx))
            {
                return false;
            }
        }
        return true;
    }


    public static String asString(Collection c)
    {
        StringBuilder sb = new StringBuilder();
        for(Iterator<TypeRemote> iter = c.iterator(); iter.hasNext(); )
        {
            TypeRemote type = iter.next();
            sb.append(asString(type));
            if(iter.hasNext())
            {
                sb.append(',');
            }
        }
        return sb.toString();
    }


    public static String asString(AttributeDescriptorRemote fd)
    {
        return (fd != null) ? (asString((TypeRemote)fd.getEnclosingType()) + "." + asString((TypeRemote)fd.getEnclosingType()) + ":" + fd.getQualifier()) :
                        String.valueOf(fd);
    }


    public static String asString(TypeRemote type)
    {
        if(type == null)
        {
            return String.valueOf(type);
        }
        String code = type.getCode();
        if(type instanceof AtomicTypeRemote)
        {
            String className = ((AtomicTypeRemote)type).getJavaClass().getName();
            return code + code;
        }
        if(type instanceof CollectionTypeRemote)
        {
            CollectionTypeRemote collectionType = (CollectionTypeRemote)type;
            int ct = collectionType.getTypeOfCollection();
            return "'" + code + "'" + (
                            (ct == 0) ? "Coll" : ((ct == 1) ? "Set" : (
                                            (ct == 2) ? "List" : ((ct == 3) ? "SortedSet" : "???")))) + "(" +
                            asString(collectionType.getElementType()) + ")";
        }
        if(type instanceof MapTypeRemote)
        {
            MapTypeRemote mt = (MapTypeRemote)type;
            return asString(mt.getArgumentType()) + "-->" + asString(mt.getArgumentType());
        }
        return code;
    }
}
