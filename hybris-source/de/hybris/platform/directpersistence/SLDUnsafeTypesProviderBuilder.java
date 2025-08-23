package de.hybris.platform.directpersistence;

import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import de.hybris.bootstrap.typesystem.YTypeSystem;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.directpersistence.annotation.SLDSafe;
import de.hybris.platform.jalo.ExtensibleItem;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.c2l.LocalizableItem;
import de.hybris.platform.jalo.extension.ExtensionManager;
import de.hybris.platform.jalo.type.AttributeAccess;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.ReflectionAttributeAccess;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.typesystem.TypeSystemUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public class SLDUnsafeTypesProviderBuilder
{
    public SLDUnsafeTypesProvider build(Collection<String> extensions)
    {
        return (SLDUnsafeTypesProvider)new DefaultSLDUnsafeTypesProvider(findUnsafeTypes(extensions));
    }


    protected Set<UnsafeTypeInfo> findUnsafeTypes(Collection<String> extensions)
    {
        YTypeSystem yTypeSystem = TypeSystemUtils.loadViaClassLoader(Utilities.getExtensionNames(), true);
        Set<UnsafeTypeInfo> matches = new LinkedHashSet<>();
        inspectRecursively(null, TypeManager.getInstance().getComposedType("GenericItem"), extensions, yTypeSystem, matches, false);
        return matches;
    }


    protected void inspectRecursively(UnsafeTypeInfo parent, ComposedType type, Collection<String> extensions, YTypeSystem yTypeSystem, Set<UnsafeTypeInfo> matches, boolean superTypeIsUnsafe)
    {
        ExtensionInfo extInfo = Utilities.getExtensionInfo(type.getExtensionName());
        if(extInfo == null)
        {
            extInfo = Utilities.getExtensionInfo("core");
        }
        boolean isUnsafe = superTypeIsUnsafe;
        UnsafeTypeInfo newUnsafeOne = null;
        if(extensions == null || extensions.contains(extInfo.getName()) || superTypeIsUnsafe)
        {
            Collection<UnsafeMethodInfo> unsafeAttributes = findUnsafeAttributesWithMethods(type, extInfo, yTypeSystem, extensions);
            if(superTypeIsUnsafe || CollectionUtils.isNotEmpty(unsafeAttributes))
            {
                if(type.getJaloClass().isAnnotationPresent((Class)SLDSafe.class))
                {
                    isUnsafe = false;
                }
                else
                {
                    isUnsafe = true;
                    newUnsafeOne = new UnsafeTypeInfo(parent, type.getCode(), extInfo.getName(), unsafeAttributes);
                    matches.add(newUnsafeOne);
                }
            }
        }
        for(ComposedType subType : type.getSubTypes())
        {
            inspectRecursively((newUnsafeOne != null) ? newUnsafeOne : parent, subType, extensions, yTypeSystem, matches, isUnsafe);
        }
    }


    protected Collection<UnsafeMethodInfo> findUnsafeAttributesWithMethods(ComposedType type, ExtensionInfo extInfo, YTypeSystem yTypeSystem, Collection<String> extensions)
    {
        Collection<UnsafeMethodInfo> unsafeAttributes = new ArrayList<>();
        for(AttributeDescriptor ad : type.getAttributeDescriptorsIncludingPrivate())
        {
            YAttributeDescriptor yAd = getYAttributeDescriptor(type, ad, yTypeSystem);
            if((extensions == null || extensions.contains(ad.getExtensionName())) && isExposedInModel(yAd) && isNotDynamic(ad))
            {
                Class jaloClass = type.getJaloClass();
                ReflectionAttributeAccess accessor = getReflectionAccessorFor(ad, jaloClass);
                if(accessor != null && (!isSldSafe(accessor.getGetter()) || !isSldSafe(accessor.getSetter())))
                {
                    unsafeAttributes.addAll(
                                    getUnsafeMethodsFromNonGeneratedClasses(ad.getQualifier(), accessor, extInfo
                                                    .getAbstractClassPrefix()));
                }
            }
        }
        if(extensions == null || extensions.contains(type.getExtensionName()))
        {
            for(Method unsafeCreate : findUnsafeCreateMethods(type.getJaloClass(), extInfo.getAbstractClassPrefix()))
            {
                unsafeAttributes.add(new UnsafeMethodInfo("<create>", unsafeCreate, false,
                                isAnnotatedAsKnownProblem(unsafeCreate)));
            }
            for(Method unsafeRemove : findUnsafeRemoveMethods(type.getJaloClass(), extInfo.getAbstractClassPrefix()))
            {
                unsafeAttributes.add(new UnsafeMethodInfo("<remove>", unsafeRemove, false,
                                isAnnotatedAsKnownProblem(unsafeRemove)));
            }
        }
        return unsafeAttributes;
    }


    private boolean isSldSafe(Method method)
    {
        return (method != null && method.getDeclaringClass().isAnnotationPresent((Class)SLDSafe.class));
    }


    private boolean isNotDynamic(AttributeDescriptor ad)
    {
        return StringUtils.isBlank(ad.getAttributeHandler());
    }


    protected boolean isExposedInModel(YAttributeDescriptor yAd)
    {
        return (yAd == null || yAd.isGenerateInModel());
    }


    protected YAttributeDescriptor getYAttributeDescriptor(ComposedType type, AttributeDescriptor ad, YTypeSystem yTypeSystem)
    {
        YAttributeDescriptor yAd = yTypeSystem.getAttribute(type.getCode(), ad.getQualifier());
        if(yAd == null)
        {
            yAd = yTypeSystem.getAttribute(ad.getDeclaringEnclosingType().getCode(), ad.getQualifier());
        }
        return yAd;
    }


    protected Collection<Method> findUnsafeCreateMethods(Class itemClass, String generatedClassPrefix)
    {
        Map<String, Method> ret = new HashMap<>();
        for(Class cl = itemClass; cl != null && isNonCoreClass(cl); cl = cl.getSuperclass())
        {
            Method[] arrayOfMethod;
            int i;
            byte b;
            for(arrayOfMethod = cl.getDeclaredMethods(), i = arrayOfMethod.length, b = 0; ; b++)
                ;
            continue;
        }
        return ret.values();
    }


    boolean isNonCoreClass(Class cl)
    {
        return (!Item.class.equals(cl) && !ExtensibleItem.class.equals(cl) && !LocalizableItem.class.equals(cl) &&
                        !GenericItem.class.equals(cl));
    }


    protected Collection<Method> findUnsafeRemoveMethods(Class itemClass, String generatedClassPrefix)
    {
        Map<String, Method> ret = new HashMap<>();
        for(Class cl = itemClass; cl != null && isNonCoreClass(cl); cl = cl.getSuperclass())
        {
            Method[] arrayOfMethod;
            int i;
            byte b;
            for(arrayOfMethod = cl.getDeclaredMethods(), i = arrayOfMethod.length, b = 0; ; b++)
                ;
            continue;
        }
        return ret.values();
    }


    protected Collection<UnsafeMethodInfo> getUnsafeMethodsFromNonGeneratedClasses(String qualifier, ReflectionAttributeAccess accessor, String generatedClassPrefix)
    {
        Collection<UnsafeMethodInfo> ret = new ArrayList<>();
        if(isFromNonGeneratedClassExcludingSafeOnes(accessor.getGetter(), generatedClassPrefix))
        {
            ret.add(new UnsafeMethodInfo(qualifier, accessor.getGetter(), true, isAnnotatedAsKnownProblem(accessor.getGetter())));
        }
        if(isFromNonGeneratedClassExcludingSafeOnes(accessor.getSetter(), generatedClassPrefix))
        {
            ret.add(new UnsafeMethodInfo(qualifier, accessor.getSetter(), false,
                            isAnnotatedAsKnownProblem(accessor.getSetter())));
        }
        if(isFromNonGeneratedClassExcludingSafeOnes(accessor.getAllGetter(), generatedClassPrefix))
        {
            ret.add(new UnsafeMethodInfo(qualifier, accessor.getAllGetter(), false,
                            isAnnotatedAsKnownProblem(accessor.getAllGetter())));
        }
        if(isFromNonGeneratedClassExcludingSafeOnes(accessor.getAllSetter(), generatedClassPrefix))
        {
            ret.add(new UnsafeMethodInfo(qualifier, accessor.getAllSetter(), false,
                            isAnnotatedAsKnownProblem(accessor.getAllSetter())));
        }
        return ret;
    }


    protected boolean isFromNonGeneratedClassExcludingSafeOnes(Method m, String generatedClassPrefix)
    {
        return (m != null && isNoBridgeMethod(m) && isNotAnnotatedAsSafe(m) &&
                        isNoGeneratedClasse(m.getDeclaringClass(), generatedClassPrefix));
    }


    protected final ReflectionAttributeAccess getReflectionAccessorFor(AttributeDescriptor fieldDescriptor, Class myClass)
    {
        ExtensionManager.getInstance().getExtensions();
        Tenant tenant = Registry.getCurrentTenantNoFallback();
        AttributeAccess accessor = ReflectionAttributeAccess.createReflectionAccess(tenant, myClass, fieldDescriptor);
        if(accessor != null)
        {
            return (ReflectionAttributeAccess)accessor;
        }
        return null;
    }


    private boolean isNoGeneratedClasse(Class itemClass, String generatedClassPrefix)
    {
        return !itemClass.getSimpleName().startsWith(generatedClassPrefix);
    }


    private boolean isNoBridgeMethod(Method method)
    {
        return !method.isBridge();
    }


    private boolean isNotAnnotatedAsSafe(Method method)
    {
        return !method.isAnnotationPresent((Class)SLDSafe.class);
    }


    private boolean isAnnotatedAsKnownProblem(Method method)
    {
        return (isAnnotatedAsKnownProblem(method.getDeclaringClass()) || (
                        !method.isAnnotationPresent((Class)SLDSafe.class) && method.isAnnotationPresent((Class)ForceJALO.class)));
    }


    private boolean isAnnotatedAsKnownProblem(Class itemClass)
    {
        return itemClass.isAnnotationPresent((Class)ForceJALO.class);
    }
}
