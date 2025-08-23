package de.hybris.platform.jalo.type;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.security.JaloSecurityException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class ReflectionAttributeAccess implements AttributeAccess
{
    private static final Logger LOG = Logger.getLogger(ReflectionAttributeAccess.class.getName());
    private static final int MATCHVALUE_USING_CTX = 1;
    private static final int MATCHVALUE_NO_BOXING = 2;
    private static final int MATCHVALUE_BEST = 3;
    private static final Map<Class<?>, Class<?>> PRIMITIVE2TYPED;
    private static final Map<Class<?>, Object> DEFAULTS;
    private final String me;
    private final boolean localized;
    private final boolean writable;
    private final boolean initialWritable;
    private final AttributeMethod getter;
    private final AttributeMethod allGetter;
    private final AttributeMethod setter;
    private final AttributeMethod allSetter;

    static
    {
        Map<Class<?>, Class<?>> m = new HashMap<>();
        m.put(int.class, Integer.class);
        m.put(byte.class, Byte.class);
        m.put(char.class, Character.class);
        m.put(double.class, Double.class);
        m.put(long.class, Long.class);
        m.put(float.class, Float.class);
        m.put(boolean.class, Boolean.class);
        PRIMITIVE2TYPED = Collections.unmodifiableMap(m);
        Map<Class<?>, Object> mm = new HashMap<>();
        mm.put(Integer.class, Integer.valueOf(0));
        mm.put(Byte.class, Byte.valueOf((byte)0));
        mm.put(Character.class, Character.valueOf(false));
        mm.put(Double.class, Double.valueOf(0.0D));
        mm.put(Long.class, Long.valueOf(0L));
        mm.put(Float.class, Float.valueOf(0.0F));
        mm.put(Boolean.class, Boolean.FALSE);
        DEFAULTS = Collections.unmodifiableMap(mm);
    }

    private static List<Method> getAllMethods(Class cl)
    {
        List<Method> ret = new ArrayList<>();
        for(Class tmp = cl; tmp != null; tmp = tmp.getSuperclass())
        {
            ret.addAll(Arrays.asList(tmp.getDeclaredMethods()));
        }
        return ret;
    }


    private static AttributeMethod matchMethod(Class itemClass, Class managerClass, String methodName, Class returnType, Class argumentType)
    {
        boolean getterMode = (argumentType == null);
        int currentMatch = -1;
        Method best = null;
        Class<?> realArgumentType = null;
        Iterator<Method> it;
        for(it = getAllMethods(itemClass).iterator(); it.hasNext(); )
        {
            int match = -1;
            Method m = it.next();
            boolean methodIsUsingCtx = false;
            boolean methodRequiresBoxingResult = false;
            boolean methodRequiresUnboxingArgument = false;
            Class<?> methodArgumentType = null;
            if(!methodName.equalsIgnoreCase(m.getName()))
            {
                continue;
            }
            Class<?> methodReturnType = m.getReturnType();
            if(!returnType.isAssignableFrom(methodReturnType))
            {
                if(!returnType.equals(void.class) && methodReturnType.isPrimitive() && PRIMITIVE2TYPED.containsKey(methodReturnType) && returnType
                                .isAssignableFrom(PRIMITIVE2TYPED.get(methodReturnType)))
                {
                    methodRequiresBoxingResult = true;
                }
                else
                {
                    if(Item.accessorLog.isDebugEnabled())
                    {
                        Item.accessorLog.debug("skipped method " + m + " due to return type mismatch : " + returnType + " <> " + methodReturnType);
                    }
                    continue;
                }
            }
            List<Class<?>> paramTypes = Arrays.asList(m.getParameterTypes());
            if(getterMode)
            {
                if(paramTypes.size() == 1 && SessionContext.class.isAssignableFrom(paramTypes.get(0)))
                {
                    methodIsUsingCtx = true;
                }
                else if(!paramTypes.isEmpty())
                {
                    if(Item.accessorLog.isDebugEnabled())
                    {
                        Item.accessorLog.debug("skipped method " + m + " due to wrong signature : expected [] or [ctx] but got " + paramTypes);
                    }
                    continue;
                }
                match = (!methodRequiresBoxingResult ? 2 : 0) + (methodIsUsingCtx ? 1 : 0);
            }
            else
            {
                if(paramTypes.size() == 2 && SessionContext.class.isAssignableFrom(paramTypes.get(0)))
                {
                    methodArgumentType = paramTypes.get(1);
                    if(!argumentType.isAssignableFrom(methodArgumentType))
                    {
                        if(methodArgumentType.isPrimitive() && PRIMITIVE2TYPED.containsKey(methodArgumentType) && argumentType
                                        .isAssignableFrom(PRIMITIVE2TYPED.get(methodArgumentType)))
                        {
                            methodRequiresUnboxingArgument = true;
                        }
                        else
                        {
                            if(Item.accessorLog.isDebugEnabled())
                            {
                                Item.accessorLog.debug("skipped method " + m + " due to argument type mismatch : " + argumentType + " <> " + methodArgumentType);
                            }
                            continue;
                        }
                    }
                    methodIsUsingCtx = true;
                }
                else if(paramTypes.size() == 1)
                {
                    methodArgumentType = paramTypes.get(0);
                    if(!argumentType.isAssignableFrom(methodArgumentType))
                    {
                        if(methodArgumentType.isPrimitive() && PRIMITIVE2TYPED.containsKey(methodArgumentType) && argumentType
                                        .isAssignableFrom(PRIMITIVE2TYPED.get(methodArgumentType)))
                        {
                            methodRequiresUnboxingArgument = true;
                        }
                        else
                        {
                            if(Item.accessorLog.isDebugEnabled())
                            {
                                Item.accessorLog.debug("skipped method " + m + " due to argument type mismatch : " + argumentType + " <> " + methodArgumentType);
                            }
                            continue;
                        }
                    }
                }
                else
                {
                    if(Item.accessorLog.isDebugEnabled())
                    {
                        Item.accessorLog.debug("skipped method " + m + " due to wrong signature : expected [" + argumentType + "] or [ctx," + argumentType + "] but got " + paramTypes);
                    }
                    continue;
                }
                match = (!methodRequiresUnboxingArgument ? 2 : 0) + (methodIsUsingCtx ? 1 : 0);
            }
            if(currentMatch < match)
            {
                if(best != null && Item.accessorLog.isDebugEnabled())
                {
                    Item.accessorLog.debug("prefering " + m + " to " + best + " due to match value " + currentMatch + "<" + match);
                }
                else if(Item.accessorLog.isDebugEnabled())
                {
                    Item.accessorLog.debug("choosing " + m + " with match value " + match);
                }
                currentMatch = match;
                best = m;
                realArgumentType = methodArgumentType;
                if(currentMatch == 3)
                {
                    break;
                }
            }
        }
        if(best != null)
        {
            AttributeMethod ret = new AttributeMethod(best, realArgumentType, ((currentMatch & 0x1) == 1));
            ret.needUnboxingArgument = (!getterMode && (currentMatch & 0x2) == 0);
            return ret;
        }
        if(managerClass == null)
        {
            return null;
        }
        for(it = getAllMethods(managerClass).iterator(); it.hasNext(); )
        {
            int match = -1;
            Method m = it.next();
            boolean methodIsUsingCtx = false;
            boolean methodRequiresBoxingResult = false;
            boolean methodRequiresUnboxingArgument = false;
            Class<?> methodArgumentType = null;
            if(!methodName.equalsIgnoreCase(m.getName()))
            {
                continue;
            }
            Class<?> methodReturnType = m.getReturnType();
            if(!returnType.isAssignableFrom(methodReturnType))
            {
                if(!returnType.equals(void.class) && methodReturnType.isPrimitive() && PRIMITIVE2TYPED.containsKey(methodReturnType) && returnType
                                .isAssignableFrom(PRIMITIVE2TYPED.get(methodReturnType)))
                {
                    methodRequiresBoxingResult = true;
                }
                else
                {
                    if(Item.accessorLog.isDebugEnabled())
                    {
                        Item.accessorLog.debug("skipped method " + m + " due to return type mismatch : " + returnType + " <> " + methodReturnType);
                    }
                    continue;
                }
            }
            List<Class<?>> paramTypes = Arrays.asList(m.getParameterTypes());
            if(getterMode)
            {
                if(paramTypes.size() == 2 && SessionContext.class.isAssignableFrom(paramTypes.get(0)) && itemClass
                                .isAssignableFrom(paramTypes.get(1)))
                {
                    methodIsUsingCtx = true;
                }
                else if(paramTypes.size() == 1 && itemClass.isAssignableFrom(paramTypes.get(0)))
                {
                    methodIsUsingCtx = false;
                }
                else
                {
                    if(Item.accessorLog.isDebugEnabled())
                    {
                        Item.accessorLog.debug("skipped method " + m + " due to wrong signature : expected (" + itemClass + ") or (ctx," + itemClass + ") but got " + paramTypes);
                    }
                    continue;
                }
                match = (!methodRequiresBoxingResult ? 2 : 0) + (methodIsUsingCtx ? 1 : 0);
            }
            else
            {
                if(paramTypes.size() == 3 && SessionContext.class.isAssignableFrom(paramTypes.get(0)) && itemClass
                                .isAssignableFrom(paramTypes.get(1)))
                {
                    methodArgumentType = paramTypes.get(2);
                    if(!argumentType.isAssignableFrom(methodArgumentType))
                    {
                        if(methodArgumentType.isPrimitive() && PRIMITIVE2TYPED.containsKey(methodArgumentType) && argumentType
                                        .isAssignableFrom(PRIMITIVE2TYPED.get(methodArgumentType)))
                        {
                            methodRequiresUnboxingArgument = true;
                        }
                        else
                        {
                            if(Item.accessorLog.isDebugEnabled())
                            {
                                Item.accessorLog.debug("skipped method " + m + " due to argument type mismatch : " + argumentType + " <> " + methodArgumentType);
                            }
                            continue;
                        }
                    }
                    methodIsUsingCtx = true;
                }
                else if(paramTypes.size() == 2 && itemClass.isAssignableFrom(paramTypes.get(0)))
                {
                    methodArgumentType = paramTypes.get(1);
                    if(!argumentType.isAssignableFrom(methodArgumentType))
                    {
                        if(methodArgumentType.isPrimitive() && PRIMITIVE2TYPED.containsKey(methodArgumentType) && argumentType
                                        .isAssignableFrom(PRIMITIVE2TYPED.get(methodArgumentType)))
                        {
                            methodRequiresUnboxingArgument = true;
                        }
                        else
                        {
                            if(Item.accessorLog.isDebugEnabled())
                            {
                                Item.accessorLog.debug("skipped method " + m + " due to argument type mismatch : " + argumentType + " <> " + methodArgumentType);
                            }
                            continue;
                        }
                    }
                    methodIsUsingCtx = false;
                }
                else
                {
                    if(Item.accessorLog.isDebugEnabled())
                    {
                        Item.accessorLog.debug("skipped method " + m + " due to wrong signature : expected (" + itemClass + "," + argumentType + ") or (ctx," + itemClass + "," + argumentType + ") but got " + paramTypes);
                    }
                    continue;
                }
                match = (!methodRequiresUnboxingArgument ? 2 : 0) + (methodIsUsingCtx ? 1 : 0);
            }
            if(currentMatch < match)
            {
                if(best != null && Item.accessorLog.isDebugEnabled())
                {
                    Item.accessorLog.debug("prefering " + m + " to " + best + " due to match value " + currentMatch + "<" + match);
                }
                else if(Item.accessorLog.isDebugEnabled())
                {
                    Item.accessorLog.debug("choosing " + m + " with match value " + match);
                }
                currentMatch = match;
                best = m;
                realArgumentType = methodArgumentType;
                if(currentMatch == 3)
                {
                    break;
                }
            }
        }
        if(best != null)
        {
            AttributeMethod ret = new AttributeMethod(best, realArgumentType, ((currentMatch & 0x1) == 1));
            ret.needUnboxingArgument = (!getterMode && (currentMatch & 0x2) == 0);
            ret.setManagerMethod(managerClass);
            return ret;
        }
        return null;
    }


    private static AttributeMethod findGetter(Class itemClass, Class declaringItemClass, Class managerClass, Class returnType, Class declaredReturnType, String qualifier, boolean isBoolean, boolean allGetter)
    {
        AttributeMethod ret = null;
        ret = matchMethod(itemClass, managerClass, (allGetter ? "getAll" : "get") + (allGetter ? "getAll" : "get") + qualifier.substring(0, 1).toUpperCase(LocaleHelper.getPersistenceLocale()),
                        allGetter ? Map.class : returnType, null);
        if(ret == null && (!itemClass.equals(declaringItemClass) || !returnType.equals(declaredReturnType)))
        {
            ret = matchMethod(declaringItemClass, managerClass, (allGetter ? "getAll" : "get") + (allGetter ? "getAll" : "get") + qualifier
                                            .substring(0, 1).toUpperCase(LocaleHelper.getPersistenceLocale()),
                            allGetter ? Map.class : declaredReturnType, null);
        }
        if(ret == null)
        {
            ret = matchMethod(itemClass, managerClass, (allGetter ? "getAll" : "get") + (allGetter ? "getAll" : "get") + qualifier.substring(0, 1).toUpperCase(LocaleHelper.getPersistenceLocale()) + "s",
                            allGetter ? Map.class : returnType, null);
            if(ret == null && !itemClass.equals(declaringItemClass))
            {
                ret = matchMethod(declaringItemClass, managerClass, (allGetter ? "getAll" : "get") + (allGetter ? "getAll" : "get") + qualifier
                                .substring(0, 1).toUpperCase(LocaleHelper.getPersistenceLocale()) + "s", allGetter ? Map.class :
                                declaredReturnType, null);
            }
        }
        if(ret == null && isBoolean && !allGetter)
        {
            ret = matchMethod(itemClass, managerClass, ((
                            qualifier.startsWith("is") || qualifier.startsWith("Is")) && qualifier.length() > 2 && Character.isUpperCase(qualifier.charAt(2))) ? qualifier : ("is" +
                            qualifier.substring(0, 1).toUpperCase(LocaleHelper.getPersistenceLocale()) + qualifier.substring(1)), returnType, null);
            if(ret == null && (!itemClass.equals(declaringItemClass) || !returnType.equals(declaredReturnType)))
            {
                ret = matchMethod(declaringItemClass, managerClass, ((
                                qualifier.startsWith("is") || qualifier.startsWith("Is")) && qualifier.length() > 2 && Character.isUpperCase(qualifier.charAt(2))) ? qualifier : ("is" +
                                qualifier.substring(0, 1).toUpperCase(LocaleHelper.getPersistenceLocale()) + qualifier.substring(1)), declaredReturnType, null);
            }
        }
        return ret;
    }


    private static AttributeMethod findSetter(Class itemClass, Class declaredItemClass, Class managerClass, Class argumentType, Class declaredArgumentType, String qualifier, boolean allSetter)
    {
        AttributeMethod ret = matchMethod(itemClass, managerClass, (allSetter ? "setAll" : "set") + (allSetter ? "setAll" : "set") + qualifier
                                        .substring(0, 1).toUpperCase(LocaleHelper.getPersistenceLocale()), void.class,
                        allSetter ? Map.class : argumentType);
        if(ret == null && (!itemClass.equals(declaredItemClass) || !argumentType.equals(declaredArgumentType)))
        {
            ret = matchMethod(declaredItemClass, managerClass, (allSetter ? "setAll" : "set") + (allSetter ? "setAll" : "set") + qualifier
                            .substring(0, 1).toUpperCase(LocaleHelper.getPersistenceLocale()), void.class, allSetter ? Map.class :
                            declaredArgumentType);
        }
        if(ret == null)
        {
            ret = matchMethod(itemClass, managerClass, (allSetter ? "setAll" : "set") + (allSetter ? "setAll" : "set") + qualifier.substring(0, 1).toUpperCase(LocaleHelper.getPersistenceLocale()) + "s",
                            void.class, allSetter ? Map.class : argumentType);
            if(ret == null && !itemClass.equals(declaredItemClass))
            {
                ret = matchMethod(declaredItemClass, managerClass, (allSetter ? "setAll" : "set") + (allSetter ? "setAll" : "set") + qualifier
                                                .substring(0, 1).toUpperCase(LocaleHelper.getPersistenceLocale()) + "s", void.class,
                                allSetter ? Map.class :
                                                declaredArgumentType);
            }
        }
        return ret;
    }


    private static Class getValueType(AttributeDescriptor ad, boolean localized)
    {
        Class ret;
        Type attributeType = localized ? ((MapType)ad.getRealAttributeType()).getReturnType() : ad.getRealAttributeType();
        if(attributeType instanceof AtomicType)
        {
            ret = ((AtomicType)attributeType).getJavaClass();
        }
        else if(attributeType instanceof ComposedType)
        {
            ret = ((ComposedType)attributeType).getJaloClass();
        }
        else if(attributeType instanceof CollectionType)
        {
            Class<Collection> clazz = Collection.class;
        }
        else if(attributeType instanceof MapType)
        {
            ret = Map.class;
        }
        else
        {
            Item.accessorLog.error("invalid attribute type " + attributeType + " - dont have return type");
            return null;
        }
        if(ret.isPrimitive() && ad.isOptional() && PRIMITIVE2TYPED.containsKey(ret))
        {
            ret = PRIMITIVE2TYPED.get(ret);
        }
        return ret;
    }


    public static final AttributeAccess createReflectionAccess(Tenant t, Class itemClass, AttributeDescriptor ad)
    {
        int modifiers = ad.getModifiers();
        boolean localized = ((modifiers & 0x200) != 0);
        boolean writable = ((modifiers & 0x2) != 0);
        boolean readable = ((modifiers & 0x1) != 0);
        boolean inherited = ((modifiers & 0x400) != 0);
        boolean initialWritable = (!writable && (modifiers & 0x800) != 0);
        String quali = ad.getQualifier();
        Class<?> valueType = getValueType(ad, localized);
        ComposedType declaringEnclosingType = ad.getDeclaringEnclosingType();
        AttributeDescriptor declaredAd = inherited ? declaringEnclosingType.getAttributeDescriptorIncludingPrivate(quali) : ad;
        Class<?> declaredValueType = inherited ? getValueType(declaredAd, declaredAd.isLocalized()) : valueType;
        boolean isBoolean = Boolean.class.isAssignableFrom(valueType);
        String extensionName = declaredAd.getExtensionName();
        boolean available = t.getTenantSpecificExtensionNames().contains(extensionName);
        Class<?> managerClass = null;
        if(extensionName != null)
        {
            if(!available)
            {
                LOG.warn("extension " + extensionName + " not installed, cannot call attribute accessor for " + ad);
            }
            else
            {
                managerClass = t.getJaloConnection().getExtensionManager().getExtension(extensionName).getClass();
            }
        }
        Class declaredItemClass = declaringEnclosingType.getJaloClass();
        AttributeMethod getter = findGetter(itemClass, declaredItemClass, managerClass, valueType, declaredValueType, quali, isBoolean, false);
        if(getter == null && Map.class.isAssignableFrom(declaredValueType) && !localized)
        {
            getter = findGetter(itemClass, declaredItemClass, managerClass, valueType, declaredValueType, "all" + quali
                            .substring(0, 1).toUpperCase(LocaleHelper.getPersistenceLocale()) + quali.substring(1), isBoolean, false);
        }
        if(getter == null && Item.accessorLog.isDebugEnabled())
        {
            Item.accessorLog.debug("cannot find getter method for attribute " + ad + " in class " + itemClass + " or " + managerClass);
        }
        AttributeMethod allGetter = localized ? findGetter(itemClass, declaredItemClass, managerClass, valueType, declaredValueType, quali, isBoolean, true) : null;
        if(localized && allGetter == null && Item.accessorLog.isDebugEnabled())
        {
            Item.accessorLog.debug("cannot find all-getter method for attribute " + ad + " in class " + itemClass + " or " + managerClass);
        }
        AttributeMethod setter = findSetter(itemClass, declaredItemClass, managerClass, valueType, declaredValueType, quali, false);
        if(setter == null && Map.class.isAssignableFrom(declaredValueType) && !localized)
        {
            setter = findSetter(itemClass, declaredItemClass, managerClass, valueType, declaredValueType, "all" + quali
                            .substring(0, 1).toUpperCase(LocaleHelper.getPersistenceLocale()) + quali.substring(1), false);
        }
        if(setter == null && writable && !initialWritable && Item.accessorLog.isDebugEnabled())
        {
            Item.accessorLog.debug("cannot find setter method for attribute " + ad + " in class " + itemClass + " or " + managerClass);
        }
        AttributeMethod allSetter = localized ? findSetter(itemClass, declaredItemClass, managerClass, valueType, declaredValueType, quali, true) : null;
        if(localized && allSetter == null && writable && !initialWritable && Item.accessorLog.isDebugEnabled())
        {
            Item.accessorLog.debug("cannot find all-setter method for attribute " + ad + " in class " + itemClass + " or " + managerClass);
        }
        if((getter == null && readable) || (setter == null && writable) || (localized && ((allGetter == null && readable) || (allSetter == null && writable))))
        {
            return null;
        }
        ReflectionAttributeAccess ret = new ReflectionAttributeAccess(itemClass.getName() + "." + itemClass.getName(), getter, allGetter, setter, allSetter, localized, writable, initialWritable);
        ret.adjustPermissions();
        return ret;
    }


    public Method getGetter()
    {
        return (this.getter == null) ? null : this.getter.method;
    }


    public Method getSetter()
    {
        return (this.setter == null) ? null : this.setter.method;
    }


    public Method getAllGetter()
    {
        return (this.allGetter == null) ? null : this.allGetter.method;
    }


    public Method getAllSetter()
    {
        return (this.allSetter == null) ? null : this.allSetter.method;
    }


    public String toString()
    {
        return this.me + "[" + this.me + (this.localized ? "l" : "") + (this.writable ? "w" : "") + (this.initialWritable ? "i" : "") + (
                        (this.getter != null) ? "G" : "") + ((this.allGetter != null) ? "aG" : "") + ((this.setter != null) ? "S" : "") + "]";
    }


    public String getInfo()
    {
        return this.me + "[" + this.me + (this.localized ? "l," : "") + (this.writable ? "w," : "") + (this.initialWritable ? "i," : "") + (
                        (this.getter != null) ? ("G=" + this.getter.method + ",") : "") + (
                        (this.allGetter != null) ? ("aG=" + this.allGetter.method + ",") : "") + (
                        (this.setter != null) ? ("S=" + this.setter.method + ",") : "") + "]";
    }


    public ReflectionAttributeAccess(String label, AttributeMethod getter, AttributeMethod allGetter, AttributeMethod setter, AttributeMethod allSetter, boolean localized, boolean writable, boolean initialWritable)
    {
        this.me = label;
        this.localized = localized;
        this.writable = writable;
        this.initialWritable = initialWritable;
        this.getter = getter;
        this.allGetter = allGetter;
        this.setter = setter;
        this.allSetter = allSetter;
    }


    protected void adjustPermissions()
    {
        if(this.getter != null && this.getter.method != null)
        {
            this.getter.method.setAccessible(true);
        }
        if(this.allGetter != null && this.allGetter.method != null)
        {
            this.allGetter.method.setAccessible(true);
        }
        if(this.setter != null && this.setter.method != null)
        {
            this.setter.method.setAccessible(true);
        }
        if(this.allSetter != null && this.allSetter.method != null)
        {
            this.allSetter.method.setAccessible(true);
        }
    }


    public Object getValue(SessionContext ctx, Item item) throws JaloTypeException, JaloInvalidParameterException, JaloSecurityException
    {
        if(this.localized && (ctx == null || ctx.getLanguage() == null))
        {
            return this.allGetter.invokeGetter(ctx, item, this.me);
        }
        return this.getter.invokeGetter(ctx, item, this.me);
    }


    public void setValue(SessionContext ctx, Item item, Object value) throws JaloTypeException, JaloInvalidParameterException, JaloSecurityException, JaloBusinessException
    {
        if(this.setter == null)
        {
            throw new JaloSecurityException("attribute " + this.me + " is not changeable", 0);
        }
        if(this.initialWritable && !Item.isInCreate(ctx))
        {
            throw new JaloSecurityException("attribute " + this.me + " cannot be changed except during item creation", 0);
        }
        if(this.localized && (ctx == null || ctx.getLanguage() == null))
        {
            if(this.allSetter == null)
            {
                Item.accessorLog.warn("missing all-setter for " + this.me + " - simulating with single setter instead");
                Map<Language, ?> values = (Map<Language, ?>)value;
                SessionContext locCtx = new SessionContext(ctx);
                for(Language l : C2LManager.getInstance().getAllLanguages())
                {
                    locCtx.setLanguage(l);
                    this.setter.invokeSetter(ctx, item, (values != null) ? values.get(l) : null, this.me);
                }
            }
            else
            {
                this.allSetter.invokeSetter(ctx, item, value, this.me);
            }
        }
        else
        {
            this.setter.invokeSetter(ctx, item, value, this.me);
        }
    }
}
