package de.hybris.platform.catalog.jalo.classification.util;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.ProductFeature;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationAttribute;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.jalo.classification.ClassificationSystemVersion;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.CSVUtils;
import de.hybris.platform.util.FlexibleSearchUtils;
import de.hybris.platform.util.SingletonCreator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class FeatureContainer implements Cloneable, Serializable
{
    private static final Logger log = Logger.getLogger(FeatureContainer.class.getName());
    public static final String WITHIN_FEATURE_CONTAINER_TRANSACTION = "within.feature.container.ta";
    private final Product product;
    private final Set<ClassificationSystemVersion> clSystems;
    private final Set<ClassificationClass> classes;
    private final Set<ClassAttributeAssignment> assignments;
    private final Map<String, Feature> featureMap;


    public static final boolean isInFeatureContainerTA(SessionContext ctx)
    {
        return (ctx != null && Boolean.TRUE.equals(ctx.getAttribute("within.feature.container.ta")));
    }


    public String toString()
    {
        return "Features(" + getProduct() + ")[" + this.featureMap.size() + "]";
    }


    protected Set<ClassAttributeAssignment> getAssignmentsInternal()
    {
        return (this.assignments != null) ? this.assignments : Collections.EMPTY_SET;
    }


    public static FeatureContainer create(Product product)
    {
        return new FeatureContainer(product, extractAssignments(resolveClasses(product)));
    }


    public static FeatureContainer createTyped(Product product, Set<ClassificationClass> classes) throws JaloInvalidParameterException
    {
        if(classes == null || classes.isEmpty())
        {
            throw new JaloInvalidParameterException("cannot create typed feature container for " + product + " without classes", 0);
        }
        return new FeatureContainer(product, extractAssignments(classes));
    }


    public static FeatureContainer createTyped(Product product, List<ClassAttributeAssignment> assignments) throws JaloInvalidParameterException
    {
        if(assignments == null || assignments.isEmpty())
        {
            throw new JaloInvalidParameterException("cannot create typed feature container for " + product + " without assignments", 0);
        }
        return new FeatureContainer(product, assignments);
    }


    public static FeatureContainer createUntyped(Product product)
    {
        return new FeatureContainer(product, null);
    }


    public static FeatureContainer load(Product product)
    {
        FeatureContainer ret = create(product);
        ret.fillFromDatabase();
        return ret;
    }


    public static FeatureContainer loadTyped(Product product) throws JaloInvalidParameterException
    {
        return loadTyped(product, new LinkedHashSet<>(CatalogManager.getInstance().getClassificationClasses(product)));
    }


    public static void removeAllTypedValues(Product product) throws JaloInvalidParameterException
    {
        removeAllTypedValues(product, new LinkedHashSet<>(CatalogManager.getInstance().getClassificationClasses(product)));
    }


    public static FeatureContainer loadTyped(Product product, Set<ClassificationClass> classes) throws JaloInvalidParameterException
    {
        FeatureContainer ret = createTyped(product, classes);
        ret.fillFromDatabase();
        return ret;
    }


    public static FeatureContainer loadTyped(Product product, ClassAttributeAssignment... assignments) throws JaloInvalidParameterException
    {
        return loadTyped(product, Arrays.asList(assignments));
    }


    public static FeatureContainer loadTyped(Product product, List<ClassAttributeAssignment> assignments) throws JaloInvalidParameterException
    {
        FeatureContainer ret = createTyped(product, assignments);
        ret.fillFromDatabase();
        return ret;
    }


    public static void removeAllTypedValues(Product product, Set<ClassificationClass> classes) throws JaloInvalidParameterException
    {
        try
        {
            createTyped(product, classes).store();
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public static FeatureContainer loadTyped(Product product, ClassificationClass... classes) throws JaloInvalidParameterException
    {
        return loadTyped(product, new LinkedHashSet<>(Arrays.asList(classes)));
    }


    public static void removeAllTypedValues(Product product, ClassificationClass... classes) throws JaloInvalidParameterException
    {
        removeAllTypedValues(product, new LinkedHashSet<>(Arrays.asList(classes)));
    }


    public static void removeAllTypedValues(Product product, List<ClassAttributeAssignment> assignments) throws JaloInvalidParameterException
    {
        try
        {
            createTyped(product, assignments).store();
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public static FeatureContainer loadUntyped(Product product)
    {
        FeatureContainer ret = createUntyped(product);
        ret.fillFromDatabase();
        return ret;
    }


    public static void removeAllUntypedValues(Product product)
    {
        try
        {
            createUntyped(product).store();
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e);
        }
    }


    public void store() throws ConsistencyCheckException
    {
        writeToDatabase();
    }


    public void reload()
    {
        this.featureMap.clear();
        if(!isUntyped())
        {
            createTypedFeatures();
        }
        fillFromDatabase();
    }


    protected FeatureContainer(Product product, List<ClassAttributeAssignment> classAssignments)
    {
        this.product = product;
        this
                        .assignments = (classAssignments != null) ? Collections.<ClassAttributeAssignment>unmodifiableSet(new LinkedHashSet<>(classAssignments)) : null;
        this.classes = (classAssignments != null) ? Collections.<ClassificationClass>unmodifiableSet(extractClasses(classAssignments)) : null;
        this.clSystems = (classAssignments != null) ? Collections.<ClassificationSystemVersion>unmodifiableSet(extractVersions(this.classes)) : null;
        this.featureMap = (Map<String, Feature>)new CaseInsensitiveLinkedHashMap();
        if(!isUntyped())
        {
            createTypedFeatures();
        }
    }


    protected FeatureContainer(FeatureContainer src) throws CloneNotSupportedException
    {
        this.product = src.product;
        this.clSystems = src.clSystems;
        this.classes = src.classes;
        this.assignments = src.assignments;
        this.featureMap = (Map<String, Feature>)new CaseInsensitiveLinkedHashMap();
        for(Map.Entry<String, Feature> entry : src.featureMap.entrySet())
        {
            this.featureMap.put(entry.getKey(), ((Feature)entry.getValue()).clone());
        }
    }


    protected static Set<ClassificationClass> resolveClasses(Product product)
    {
        return new LinkedHashSet<>(CatalogManager.getInstance().getClassificationClasses(product));
    }


    public FeatureContainer clone() throws CloneNotSupportedException
    {
        return new FeatureContainer(this);
    }


    public Set<ClassificationSystemVersion> getSystemVersions()
    {
        return (this.clSystems != null) ? this.clSystems : Collections.EMPTY_SET;
    }


    protected ClassificationSystemVersion matchSystemVersion(String catalogID, String versionID, boolean failIfAmbiguous)
    {
        ClassificationSystemVersion match = null;
        for(ClassificationSystemVersion ver : getSystemVersions())
        {
            if(versionID.equalsIgnoreCase(ver.getVersion()))
            {
                if(catalogID != null && catalogID.equalsIgnoreCase(ver.getCatalog().getId()))
                {
                    return ver;
                }
                if(catalogID == null)
                {
                    if(match != null && !match.equals(ver))
                    {
                        if(failIfAmbiguous)
                        {
                            throw new JaloInvalidParameterException("duplicate verison '" + versionID + "' within " +
                                            getSystemVersions() + " - cannot match", 0);
                        }
                        return null;
                    }
                    match = ver;
                }
            }
        }
        return match;
    }


    public Set<ClassificationClass> getClasses()
    {
        return (this.classes != null) ? this.classes : Collections.EMPTY_SET;
    }


    protected Set<ClassificationClass> getSupportedSuperClasses(ClassificationClass classificationClass)
    {
        Set<ClassificationClass> myClasses = new LinkedHashSet<>(getClasses());
        if(myClasses.contains(classificationClass))
        {
            myClasses.retainAll(classificationClass.getAllSuperClasses());
            return myClasses;
        }
        return Collections.EMPTY_SET;
    }


    public boolean hasClass(ClassificationClass cclass)
    {
        return getClasses().contains(cclass);
    }


    public Set<ClassificationClass> getFeatureProvidingClasses()
    {
        if(isUntyped())
        {
            return Collections.EMPTY_SET;
        }
        Set<ClassificationClass> ret = new LinkedHashSet<>();
        for(ClassAttributeAssignment asgnmt : getAssignmentsInternal())
        {
            ret.add(asgnmt.getClassificationClass());
        }
        return ret;
    }


    public ClassificationClass getClassificationClass(String code) throws JaloInvalidParameterException
    {
        if(isUntyped())
        {
            throw new JaloInvalidParameterException("container is untyped", 0);
        }
        ClassificationClass ret = null;
        if(!getClasses().isEmpty())
        {
            List<String> splitAndUnescape = CSVUtils.splitAndUnescape(code, new char[] {'/'}, false);
            String[] tokens = splitAndUnescape.<String>toArray(new String[splitAndUnescape.size()]);
            String catalogID = null;
            String versionID = null;
            String classID = null;
            switch(tokens.length)
            {
                case 3:
                    catalogID = tokens[tokens.length - 3];
                case 2:
                    versionID = tokens[tokens.length - 2];
                case 1:
                    classID = tokens[tokens.length - 1];
                    break;
            }
            ClassificationSystemVersion clSys = (versionID != null) ? matchSystemVersion(catalogID, versionID, true) : null;
            if(versionID != null && clSys == null)
            {
                throw new JaloInvalidParameterException("wrong classification system identifier '" + catalogID + "/" + versionID + "' - no matching system version among " +
                                getSystemVersions(), 0);
            }
            ret = matchClass(clSys, classID);
            if(ret == null)
            {
                throw new JaloInvalidParameterException("no class '" + classID + "' found within " + getClasses(), 0);
            }
            return ret;
        }
        throw new JaloInvalidParameterException("no class '" + code + "' found within " + getClasses(), 0);
    }


    protected ClassificationClass matchClass(ClassificationSystemVersion clSys, String classIdentifier)
    {
        ClassificationClass ret = null;
        for(ClassificationClass cl : getClasses())
        {
            if(classIdentifier.equalsIgnoreCase(cl.getCode()) && (clSys == null || clSys.equals(cl.getSystemVersion())))
            {
                if(ret == null)
                {
                    ret = cl;
                    continue;
                }
                throw new JaloInvalidParameterException("ambigous class identifier '" + classIdentifier + "' - found at least two maching classes " + ret + " and " + cl, 0);
            }
        }
        return ret;
    }


    public ClassAttributeAssignment getClassificationAttributeAssignment(String code) throws JaloInvalidParameterException
    {
        return getClassificationAttributeAssignment(code, true);
    }


    protected ClassAttributeAssignment getClassificationAttributeAssignment(String code, boolean failIfNotExists) throws JaloInvalidParameterException
    {
        if(isUntyped())
        {
            throw new JaloInvalidParameterException("container is untyped", 0);
        }
        ClassAttributeAssignment retAsgnmt = null;
        if(!getAssignmentsInternal().isEmpty())
        {
            String catalogID = null;
            String versionID = null;
            String classID = null;
            String attributeID = null;
            List<String> splitAndUnescape = CSVUtils.splitAndUnescape(code, new char[] {'/'}, false);
            String[] tokens = splitAndUnescape.<String>toArray(new String[splitAndUnescape.size()]);
            int pos = tokens.length - 1;
            if(pos >= 0)
            {
                attributeID = tokens[pos];
                if(pos < 3 && attributeID.contains("."))
                {
                    int cut = attributeID.indexOf('.');
                    classID = attributeID.substring(0, cut).trim();
                    attributeID = attributeID.substring(cut + 1).trim();
                }
            }
            pos--;
            if(pos >= 0)
            {
                if(classID != null)
                {
                    versionID = tokens[pos];
                }
                else
                {
                    classID = tokens[pos];
                }
            }
            pos--;
            if(versionID == null && pos >= 0)
            {
                versionID = tokens[pos];
            }
            pos--;
            if(pos >= 0)
            {
                catalogID = tokens[pos];
            }
            ClassificationSystemVersion clSys = (versionID != null) ? matchSystemVersion(catalogID, versionID, failIfNotExists) : null;
            if(versionID != null && clSys == null)
            {
                if(failIfNotExists)
                {
                    throw new JaloInvalidParameterException("wrong classification system identifier '" + catalogID + "/" + versionID + "' - no matching system version among " +
                                    getSystemVersions(), 0);
                }
                return null;
            }
            ClassificationClass classificationClass = (classID != null) ? matchClass(clSys, classID) : null;
            if(classID != null && classificationClass == null)
            {
                if(failIfNotExists)
                {
                    throw new JaloInvalidParameterException("wrong classification class identifier '" + catalogID + "/" + versionID + "/" + classID + "' - no matching class among " +
                                    getClasses(), 0);
                }
                return null;
            }
            Set<ClassificationClass> permittedClasses = (classificationClass != null) ? new LinkedHashSet<>() : null;
            if(classificationClass != null)
            {
                permittedClasses.add(classificationClass);
                permittedClasses.addAll(getSupportedSuperClasses(classificationClass));
            }
            retAsgnmt = matchAttribute(clSys, permittedClasses, attributeID);
        }
        if(retAsgnmt == null && failIfNotExists)
        {
            throw new JaloInvalidParameterException("no attribute '" + code + "' found within " + getAssignmentsInternal(), 0);
        }
        return retAsgnmt;
    }


    public ClassificationAttribute getClassificationAttribute(String code) throws JaloInvalidParameterException
    {
        return getClassificationAttributeAssignment(code).getClassificationAttribute();
    }


    protected ClassAttributeAssignment matchAttribute(ClassificationSystemVersion clSys, Set<ClassificationClass> permittedClasses, String attributeIdentifier)
    {
        ClassAttributeAssignment retAsgnmt = null;
        for(ClassAttributeAssignment asgnmt : getAssignmentsInternal())
        {
            ClassificationClass myCl = asgnmt.getClassificationClass();
            if(clSys != null && !clSys.equals(myCl.getSystemVersion()))
            {
                continue;
            }
            if(permittedClasses != null && !permittedClasses.contains(myCl))
            {
                continue;
            }
            if(attributeIdentifier.equalsIgnoreCase(asgnmt.getClassificationAttribute().getCode()))
            {
                if(retAsgnmt == null)
                {
                    retAsgnmt = asgnmt;
                    continue;
                }
                if(!retAsgnmt.equals(asgnmt))
                {
                    throw new JaloInvalidParameterException("ambigous attribute identifier '" + attributeIdentifier + "' - found at least two maching attributes " + retAsgnmt + " and " + asgnmt, 0);
                }
            }
        }
        return retAsgnmt;
    }


    public boolean isUntyped()
    {
        return getClasses().isEmpty();
    }


    public boolean isEmpty()
    {
        if(this.featureMap.isEmpty())
        {
            return true;
        }
        for(Map.Entry<String, Feature> entry : this.featureMap.entrySet())
        {
            if(!((Feature)entry.getValue()).isEmptyIgnoringLanguage())
            {
                return false;
            }
        }
        return true;
    }


    public Set<ClassAttributeAssignment> getSupportedAttributeAssignments() throws IllegalStateException
    {
        if(isUntyped())
        {
            throw new IllegalStateException("container is untyped");
        }
        return Collections.unmodifiableSet(getAssignmentsInternal());
    }


    public List<ClassAttributeAssignment> getSupportedAttributeAssignments(ClassificationClass cclass) throws IllegalStateException
    {
        if(isUntyped())
        {
            throw new IllegalStateException("container is untyped");
        }
        if(getAssignmentsInternal().isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<ClassAttributeAssignment> ret = new ArrayList<>(cclass.getClassificationAttributeAssignments());
        ret.retainAll(getAssignmentsInternal());
        return ret;
    }


    public Set<ClassificationAttribute> getSupportedAttributes() throws IllegalStateException
    {
        if(isUntyped())
        {
            throw new IllegalStateException("container is untyped");
        }
        if(getAssignmentsInternal().isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<ClassificationAttribute> ret = new LinkedHashSet<>();
        for(ClassAttributeAssignment asgnmt : getAssignmentsInternal())
        {
            ret.add(asgnmt.getClassificationAttribute());
        }
        return ret;
    }


    public Product getProduct()
    {
        return this.product;
    }


    public <T> UntypedFeature<T> createFeature(String qualifer) throws IllegalStateException, JaloInvalidParameterException
    {
        return createFeature(qualifer, false);
    }


    public <T> UntypedFeature<T> createFeature(String qualifer, boolean localized) throws IllegalStateException, JaloInvalidParameterException
    {
        if(!isUntyped())
        {
            throw new IllegalStateException("feature container " + this + " cannot handle typed features");
        }
        if(getFeature(qualifer) != null)
        {
            throw new JaloInvalidParameterException("feature '" + qualifer + "' already exists within " + this, 0);
        }
        UntypedFeature<T> ret = new UntypedFeature(this, qualifer, localized);
        addFeature((Feature)ret);
        return ret;
    }


    public List<? extends Feature> getFeatures()
    {
        if(this.featureMap == null || this.featureMap.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<Feature> ret = new ArrayList<>(this.featureMap.size());
        for(Map.Entry<String, Feature> mapped : this.featureMap.entrySet())
        {
            ret.add(mapped.getValue());
        }
        return Collections.unmodifiableList(ret);
    }


    public List<? extends Feature> getFeatures(ClassificationClass cclass)
    {
        if(isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<Feature> ret = new ArrayList<>();
        for(ClassAttributeAssignment supported : getSupportedAttributeAssignments(cclass))
        {
            ret.add((Feature)getFeature(supported));
        }
        return Collections.unmodifiableList(ret);
    }


    protected String unescape(String qualifier)
    {
        return CSVUtils.unescapeString(qualifier, new char[] {'/', '.'}, false);
    }


    public boolean hasFeature(String qualifier)
    {
        boolean ret = false;
        if(this.featureMap.containsKey(unescape(qualifier)))
        {
            ret = true;
        }
        else if(!isUntyped())
        {
            ClassAttributeAssignment assignment = getClassificationAttributeAssignment(qualifier, false);
            ret = (assignment != null && this.featureMap.containsKey(createUniqueKey(assignment)));
        }
        return ret;
    }


    public <T> Feature<T> getFeature(String qualifier) throws JaloInvalidParameterException
    {
        if(!isUntyped() && !this.featureMap.containsKey(unescape(qualifier)))
        {
            ClassAttributeAssignment assignment = getClassificationAttributeAssignment(qualifier, false);
            if(assignment != null)
            {
                String uniqueKey = createUniqueKey(assignment);
                if(this.featureMap.containsKey(uniqueKey))
                {
                    return this.featureMap.get(uniqueKey);
                }
                throw new JaloInvalidParameterException("got no typed feature with qualifier '" + uniqueKey + "'", 0);
            }
            throw new JaloInvalidParameterException("got no typed feature with qualifier '" + qualifier + "'", 0);
        }
        return this.featureMap.get(qualifier);
    }


    public <T> Feature<T> getOrCreateFeature(String qualifier)
    {
        UntypedFeature<?> untypedFeature;
        Feature<T> ret = this.featureMap.get(qualifier);
        if(ret == null)
        {
            untypedFeature = createFeature(qualifier);
        }
        return (Feature)untypedFeature;
    }


    public <T> TypedFeature<T> getFeature(ClassAttributeAssignment assignment) throws JaloInvalidParameterException
    {
        TypedFeature<T> typedFeature = (TypedFeature<T>)this.featureMap.get(createUniqueKey(assignment));
        if(typedFeature == null)
        {
            throw new JaloInvalidParameterException("feature " + assignment + " is not manageable by this container, valid for this class = " +
                            getSupportedAttributeAssignments(assignment
                                            .getClassificationClass()), 0);
        }
        return typedFeature;
    }


    public <T> TypedFeature<T> getFeature(ClassificationClass cclass, ClassificationAttribute attr) throws JaloInvalidParameterException
    {
        try
        {
            return getFeature(cclass.getAttributeAssignment(attr));
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloInvalidParameterException("class " + cclass + " has got no assignmed attribute " + attr, 0);
        }
    }


    public boolean hasFeature(ClassAttributeAssignment assignment)
    {
        return (!isUntyped() && getAssignmentsInternal().contains(assignment));
    }


    public boolean hasFeature(ClassificationAttribute attr) throws JaloInvalidParameterException
    {
        try
        {
            return hasFeature(getAssignment(attr));
        }
        catch(JaloInvalidParameterException e)
        {
            return false;
        }
    }


    public TypedFeature getFeature(ClassificationAttribute attr) throws JaloInvalidParameterException
    {
        return getFeature(getAssignment(attr));
    }


    public void clearValues()
    {
        for(Map.Entry<String, Feature> entry : this.featureMap.entrySet())
        {
            ((Feature)entry.getValue()).clearAll();
        }
    }


    public void clearFeatures() throws IllegalStateException
    {
        if(isUntyped())
        {
            this.featureMap.clear();
        }
        else
        {
            throw new IllegalStateException("typed features are not removable");
        }
    }


    public void deleteFeature(String qualifier) throws IllegalStateException
    {
        if(!isUntyped())
        {
            throw new IllegalStateException("typed features are not removable");
        }
        this.featureMap.remove(qualifier);
    }


    protected Feature addFeature(Feature feat)
    {
        return this.featureMap.put(feat.getUniqueKey(), feat);
    }


    public static String createUniqueKey(ClassAttributeAssignment assignment)
    {
        Map<PK, String> cache = getUniqueKeyCache();
        PK pk = assignment.getPK();
        String ret = cache.get(pk);
        if(ret == null)
        {
            cache.put(pk, ret = createUniqueKeyInternal(assignment));
        }
        return ret;
    }


    private static String createUniqueKeyInternal(ClassAttributeAssignment assignment)
    {
        return assignment.getSystemVersion().getFullVersionName() + "/" + assignment.getSystemVersion().getFullVersionName() + "." + assignment.getClassificationClass().getCode();
    }


    private static final SingletonCreator.Creator<Map<PK, String>> UNIQUE_KEY_CACHE_CREATOR = (SingletonCreator.Creator<Map<PK, String>>)new Object();

    static
    {
        new Object();
    }

    private static Map<PK, String> getUniqueKeyCache()
    {
        return (Map<PK, String>)Registry.getCurrentTenant().getCache().getStaticCacheContent(UNIQUE_KEY_CACHE_CREATOR);
    }


    protected static Set<ClassificationSystemVersion> extractVersions(Collection<ClassificationClass> classes)
    {
        Set<ClassificationSystemVersion> ret = new LinkedHashSet<>();
        for(ClassificationClass cl : classes)
        {
            ret.add(cl.getSystemVersion());
        }
        return ret;
    }


    protected static Set<ClassificationClass> extractClasses(Collection<ClassAttributeAssignment> assignments)
    {
        if(assignments != null && !assignments.isEmpty())
        {
            Set<ClassificationClass> ret = new LinkedHashSet<>();
            for(ClassAttributeAssignment asgnmt : assignments)
            {
                ret.add(asgnmt.getClassificationClass());
            }
            return ret;
        }
        return Collections.EMPTY_SET;
    }


    protected static List<ClassAttributeAssignment> extractAssignments(Set<ClassificationClass> classes)
    {
        if(classes != null && !classes.isEmpty())
        {
            List<ClassAttributeAssignment> set = new ArrayList<>();
            for(ClassificationClass ccl : classes)
            {
                set.addAll(ccl.getClassificationAttributeAssignments());
            }
            return set;
        }
        return Collections.EMPTY_LIST;
    }


    protected void writeToDatabase_TX()
    {
        try
        {
            Transaction.current().execute((TransactionBody)new Object(this));
        }
        catch(Exception e)
        {
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            throw new RuntimeException(e);
        }
    }


    protected void writeToDatabase()
    {
        try
        {
            JaloSession.getCurrentSession().getSessionContext().setAttribute("within.feature.container.ta", Boolean.TRUE);
            writeToDatabase_Impl();
            getProduct().setModificationTime(new Date());
        }
        finally
        {
            JaloSession.getCurrentSession().getSessionContext().removeAttribute("within.feature.container.ta");
        }
    }


    protected void writeToDatabase_Impl()
    {
        Collection<PK> validPKs = new ArrayList<>();
        int featurePosition = 0;
        for(Map.Entry<String, Feature> entry : this.featureMap.entrySet())
        {
            Feature feature = entry.getValue();
            if(!feature.isEmptyIgnoringLanguage())
            {
                validPKs.addAll(((Feature)entry.getValue()).writeToDatabase(
                                (feature instanceof TypedFeature) ? (
                                                (TypedFeature)feature).getClassAttributeAssignment().getPositionAsPrimitive() : featurePosition++));
            }
        }
        try
        {
            Map<ClassAttributeAssignment, List<ProductFeature>> classAttributeAssignmentListMap = fetchItems(getProduct(),
                            isUntyped() ? Collections.EMPTY_SET : getSupportedAttributeAssignments(), validPKs);
            for(Map.Entry<ClassAttributeAssignment, List<ProductFeature>> entry : classAttributeAssignmentListMap.entrySet())
            {
                for(ProductFeature toRemove : entry.getValue())
                {
                    toRemove.remove();
                }
            }
        }
        catch(ConsistencyCheckException e)
        {
            throw new JaloSystemException(e);
        }
    }


    protected Map<ClassAttributeAssignment, List<ProductFeature>> fetchItems(Product product, Set<ClassAttributeAssignment> assignments, Collection<PK> excludePKs)
    {
        boolean untyped = isUntyped();
        if(!untyped && assignments.isEmpty())
        {
            return Collections.EMPTY_MAP;
        }
        Map<Object, Object> values = new HashMap<>();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT {").append(Item.PK).append("},{classificationAttributeAssignment} ");
        stringBuilder.append("FROM {").append(GeneratedCatalogConstants.TC.PRODUCTFEATURE).append("} ");
        stringBuilder.append("WHERE {").append("product").append("}=?product AND ");
        values.put("product", product.getPK());
        if(untyped)
        {
            stringBuilder.append("{").append("classificationAttributeAssignment").append("} IS NULL ");
        }
        else if(assignments.size() == 1)
        {
            stringBuilder.append("{").append("classificationAttributeAssignment").append("} = ?assignment ");
            values.put("assignment", ((ClassAttributeAssignment)assignments.iterator().next()).getPK());
        }
        else
        {
            List<PK> lst = new ArrayList<>();
            for(ClassAttributeAssignment attr : assignments)
            {
                lst.add(attr.getPK());
            }
            values.put("assignments", lst);
            stringBuilder.append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{classificationAttributeAssignment} IN (?assignments) ", "assignments", "OR", lst, values));
        }
        if(excludePKs != null && !excludePKs.isEmpty())
        {
            stringBuilder.append(" AND ");
            if(excludePKs.size() == 1)
            {
                stringBuilder.append("{" + Item.PK + "} <> ?excludePK ");
                values.put("excludePK", excludePKs.iterator().next());
            }
            else
            {
                ArrayList<PK> lst = new ArrayList();
                for(PK pk : excludePKs)
                {
                    lst.add(pk);
                }
                values.put("excludePKs", lst);
                stringBuilder.append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{" + Item.PK + "} NOT IN (?excludePKs) ", "excludePKs", "AND", lst, values));
            }
        }
        stringBuilder.append(" ORDER BY {").append("featurePosition").append("} ASC, ");
        stringBuilder.append("{").append("classificationAttributeAssignment").append("} ASC, ");
        stringBuilder.append("{").append("language").append("} ASC, ");
        stringBuilder.append("{").append("valuePosition").append("} ASC ");
        List<List> rows = FlexibleSearch.getInstance().search(stringBuilder.toString(), values, Arrays.asList((Class<?>[][])new Class[] {ProductFeature.class, ClassAttributeAssignment.class}, ), true, true, 0, -1).getResult();
        Map<ClassAttributeAssignment, List<ProductFeature>> ret = new HashMap<>();
        for(List<ProductFeature> row : rows)
        {
            ProductFeature productFeature = row.get(0);
            ClassAttributeAssignment asgnmt = (ClassAttributeAssignment)row.get(1);
            List<ProductFeature> features = ret.get(asgnmt);
            if(features == null)
            {
                ret.put(asgnmt, features = new ArrayList<>());
            }
            features.add(productFeature);
        }
        return ret;
    }


    protected void createTypedFeatures()
    {
        if(isUntyped())
        {
            throw new IllegalStateException("container is not typed");
        }
        for(ClassAttributeAssignment assignment : getSupportedAttributeAssignments())
        {
            addFeature((Feature)new TypedFeature(this, assignment));
        }
    }


    protected void fillFromDatabase()
    {
        SessionContext ctx = JaloSession.getCurrentSession().createSessionContext();
        Set<ClassAttributeAssignment> assignments = isUntyped() ? Collections.EMPTY_SET : getSupportedAttributeAssignments();
        Map<ClassAttributeAssignment, List<ProductFeature>> featureItemMappings = fetchItems(getProduct(), assignments, Collections.EMPTY_LIST);
        if(isUntyped())
        {
            List<ProductFeature> features = featureItemMappings.get(null);
            if(features != null)
            {
                for(ProductFeature fItem : features)
                {
                    Language lang = fItem.getLanguage();
                    ctx.setLanguage(lang);
                    UntypedFeature feature = (UntypedFeature)getOrCreateFeature(fItem.getQualifier());
                    feature.setLocalized((feature.isLocalized() || lang != null));
                    feature.loadValue(ctx, fItem);
                }
            }
        }
        else
        {
            for(ClassAttributeAssignment asgnmt : assignments)
            {
                TypedFeature<?> feature = getFeature(asgnmt);
                List<ProductFeature> fItems = featureItemMappings.get(asgnmt);
                if(fItems != null)
                {
                    for(ProductFeature fItem : fItems)
                    {
                        Language lang = fItem.getLanguage();
                        ctx.setLanguage(lang);
                        if(feature.isLocalized() && lang == null)
                        {
                            log.error("found inconsistent product feature " + fItem + " : missing language for localized feature");
                            continue;
                        }
                        if(!feature.isLocalized() && lang != null)
                        {
                            log.error("found inconsistent product feature " + fItem + " : obsolete language for unlocalized feature");
                            continue;
                        }
                        feature.loadValue(ctx, fItem);
                    }
                }
            }
        }
    }


    protected ClassAttributeAssignment getAssignment(ClassificationAttribute attr) throws JaloInvalidParameterException
    {
        ClassAttributeAssignment ret = null;
        if(!getAssignmentsInternal().isEmpty())
        {
            for(ClassAttributeAssignment asgnmt : getAssignmentsInternal())
            {
                if(attr.equals(asgnmt.getClassificationAttribute()))
                {
                    if(ret != null && !ret.equals(asgnmt))
                    {
                        throw new JaloInvalidParameterException("multiple assignments " + ret + " and " + asgnmt + " found for attribute " + attr
                                        .getCode() + " within classes " + getClasses(), 0);
                    }
                    ret = asgnmt;
                }
            }
        }
        if(ret == null)
        {
            throw new JaloInvalidParameterException("no assignment found for attribute " + attr.getCode() + " within classes " +
                            getClasses(), 0);
        }
        return ret;
    }


    protected String getEmptyValueString()
    {
        return "-";
    }


    protected String getBooleanString(boolean booleanToString)
    {
        return Boolean.toString(booleanToString);
    }


    public static Map<ClassificationClass, List<ClassAttributeAssignment>> groupAssignments(Collection<ClassAttributeAssignment> assignments)
    {
        if(assignments == null)
        {
            return null;
        }
        if(assignments.isEmpty())
        {
            return Collections.EMPTY_MAP;
        }
        Map<ClassificationClass, List<ClassAttributeAssignment>> ret = new HashMap<>();
        for(ClassAttributeAssignment asgnmt : assignments)
        {
            ClassificationClass classificationClass = asgnmt.getClassificationClass();
            List<ClassAttributeAssignment> clAssignments = ret.get(classificationClass);
            if(clAssignments == null)
            {
                ret.put(classificationClass, clAssignments = new ArrayList<>());
            }
            clAssignments.add(asgnmt);
        }
        return ret;
    }
}
