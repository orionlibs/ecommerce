package de.hybris.platform.catalog.jalo.classification;

import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class ClassificationSystemVersion extends GeneratedClassificationSystemVersion
{
    private static final Logger LOG = Logger.getLogger(ClassificationSystemVersion.class.getName());


    public String toString()
    {
        if(getImplementation() == null)
        {
            return super.toString();
        }
        return getFullVersionName() + " (" + getFullVersionName() + ")";
    }


    public ClassificationSystem getClassificationSystem()
    {
        try
        {
            return (ClassificationSystem)getCatalog();
        }
        catch(ClassCastException e)
        {
            return null;
        }
    }


    @ForceJALO(reason = "something else")
    protected void setCatalog(SessionContext ctx, Catalog param)
    {
        if(!(param instanceof ClassificationSystem))
        {
            throw new JaloInvalidParameterException("ClassificationSystemVersion.catalog only allows ClassificationSystem : got " + param
                            .getClass(), 0);
        }
        super.setCatalog(ctx, param);
    }


    public String getFullVersionName()
    {
        return getCatalog().getId() + "/" + getCatalog().getId();
    }


    public ClassificationClass createClass(String code) throws ConsistencyCheckException
    {
        return createClass(null, code);
    }


    public ClassificationClass createClass(ClassificationClass superClass, String code) throws ConsistencyCheckException
    {
        if(code == null)
        {
            throw new NullPointerException("code was null");
        }
        try
        {
            getClassificationClass(code);
            throw new ConsistencyCheckException(" class '" + code + "' alerady exists with system " + getFullVersionName(), 0);
        }
        catch(JaloItemNotFoundException e)
        {
            Map<Object, Object> attribs = new HashMap<>();
            attribs.put(CatalogConstants.Attributes.Category.CATALOGVERSION, this);
            attribs.put("code", code);
            if(superClass != null)
            {
                attribs.put("supercategories", Collections.singletonList(superClass));
            }
            return CatalogManager.getInstance().createClassificationClass(attribs);
        }
    }


    public ClassificationAttribute createClassificationAttribute(String code) throws ConsistencyCheckException
    {
        if(code == null)
        {
            throw new NullPointerException("code was null");
        }
        try
        {
            getClassificationAttribute(code);
            throw new ConsistencyCheckException(" attribute '" + code + "' alerady exists with system " + getFullVersionName(), 0);
        }
        catch(JaloItemNotFoundException jaloItemNotFoundException)
        {
            Map<Object, Object> attribs = new HashMap<>();
            attribs.put("systemVersion", this);
            attribs.put("code", code);
            return CatalogManager.getInstance().createClassificationAttribute(attribs);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ClassificationAttribute getClassificationAttribute(String code) throws JaloItemNotFoundException
    {
        if(code == null)
        {
            throw new NullPointerException("code was null");
        }
        Map<Object, Object> values = new HashMap<>();
        values.put("code", code);
        values.put("me", getPK());
        List<ClassificationAttribute> res = FlexibleSearch.getInstance()
                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTE + "}  WHERE {code}=?code AND {systemVersion}= ?me ", values, Collections.singletonList(ClassificationAttribute.class), true, true, 0, -1).getResult();
        switch(res.size())
        {
            case 0:
                throw new JaloItemNotFoundException("no ClassificationAttribute '" + code + "' found within " +
                                getFullVersionName(), 0);
            case 1:
                return res.get(0);
        }
        throw new JaloSystemException(null, "multiple ClassificationAttributes '" + code + "' found within " +
                        getFullVersionName(), 0);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ClassificationClass getClassificationClass(String code) throws JaloItemNotFoundException
    {
        if(code == null)
        {
            throw new NullPointerException("code was null");
        }
        Map<Object, Object> values = new HashMap<>();
        values.put("code", code);
        values.put("me", getPK());
        List<ClassificationClass> res = FlexibleSearch.getInstance()
                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CLASSIFICATIONCLASS + "}  WHERE {code}=?code AND {" + CatalogConstants.Attributes.Category.CATALOGVERSION + "}=?me ", values, Collections.singletonList(ClassificationClass.class), true, true, 0, -1)
                        .getResult();
        switch(res.size())
        {
            case 0:
                throw new JaloItemNotFoundException("no ClassificationClass '" + code + "' found within " + getFullVersionName(), 0);
            case 1:
                return res.get(0);
        }
        throw new JaloSystemException(null, "multiple ClassificationClasses'" + code + "' found within " +
                        getFullVersionName(), 0);
    }


    public ClassificationClass getClassificationClass(Product product) throws JaloInvalidParameterException
    {
        Collection<ClassificationClass> classes = getClassificationClasses(product);
        switch(classes.size())
        {
            case 0:
                return null;
            case 1:
                return classes.iterator().next();
        }
        throw new JaloInvalidParameterException("product " + product + " is assigned to multiple classes " + classes + " inside " + this, 0);
    }


    public Collection<ClassificationClass> getClassificationClasses(Product product)
    {
        return CatalogManager.getInstance().getClassificationClasses(this, product);
    }


    public void setClassificationClass(Product product, ClassificationClass classificationClass)
    {
        if(classificationClass == null)
        {
            throw new NullPointerException("classificationclass in argument was null");
        }
        if(!equals(classificationClass.getSystemVersion()))
        {
            throw new JaloInvalidParameterException("class " + classificationClass + " doesnt belong to this system version " + this, 0);
        }
        for(Iterator<ClassificationClass> iter = getClassificationClasses(product).iterator(); iter.hasNext(); )
        {
            ClassificationClass ccl = iter.next();
            if(!ccl.equals(classificationClass))
            {
                ccl.removeProduct(product);
            }
        }
        classificationClass.addProduct(product);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ClassificationClass> getRootClasses()
    {
        Collection<ClassificationClass> ret = new LinkedList(getRootCategories());
        for(Iterator<Category> iter = ret.iterator(); iter.hasNext(); )
        {
            Category cat = iter.next();
            if(!(cat instanceof ClassificationClass))
            {
                iter.remove();
            }
        }
        return ret;
    }


    public ClassificationAttributeValue createClassificationAttributeValue(String code) throws ConsistencyCheckException
    {
        try
        {
            getClassificationAttributeValue(code);
            throw new ConsistencyCheckException("a value with code '" + code + "' already exists", 0);
        }
        catch(JaloItemNotFoundException e)
        {
            Map<Object, Object> attr = new HashMap<>();
            attr.put("code", code);
            attr.put("systemVersion", this);
            return CatalogManager.getInstance().createClassificationAttributeValue(attr);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ClassificationAttributeValue getClassificationAttributeValue(String code) throws JaloItemNotFoundException
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("code", code);
        values.put("me", getPK());
        List<ClassificationAttributeValue> ret = FlexibleSearch.getInstance()
                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTEVALUE + "} WHERE {code}=?code AND {systemVersion}=?me ", values, Collections.singletonList(ClassificationAttributeValue.class), true, true, 0, -1).getResult();
        switch(ret.size())
        {
            case 0:
                throw new JaloItemNotFoundException("no ClassificationAttributeValue found for code " + code, 0);
            case 1:
                return ret.get(0);
        }
        throw new JaloSystemException(null, "multiple ClassificationAttributeValue objects found for code " + code, 0);
    }


    public ClassificationAttributeUnit createAttributeUnit(String code, String symbol) throws ConsistencyCheckException
    {
        return createAttributeUnit(code, symbol, null, 1.0D);
    }


    public ClassificationAttributeUnit createAttributeUnit(String code, String symbol, String unitType, double conversion) throws ConsistencyCheckException, JaloInvalidParameterException
    {
        try
        {
            getAttributeUnit(code);
            throw new ConsistencyCheckException("attribute unit '" + code + "' already exists", 0);
        }
        catch(JaloItemNotFoundException e)
        {
            if(conversion <= 0.0D)
            {
                throw new JaloInvalidParameterException("conversion factor " + conversion + " <= 0.0", 0);
            }
            Map<Object, Object> attr = new HashMap<>();
            attr.put("systemVersion", this);
            attr.put("code", code);
            if(symbol != null)
            {
                attr.put("symbol", symbol);
            }
            if(unitType != null)
            {
                attr.put("unitType", unitType);
            }
            attr.put("conversionFactor", new Double(conversion));
            return CatalogManager.getInstance().createClassificationAttributeUnit(attr);
        }
    }


    public ClassificationAttributeUnit getAttributeUnitBySymbol(String unitSymbol) throws JaloItemNotFoundException
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("unitSymbol", unitSymbol);
        values.put("me", getPK());
        List<ClassificationAttributeUnit> units = FlexibleSearch.getInstance()
                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTEUNIT + "} WHERE {systemVersion}=?me AND {symbol}=?unitSymbol", values, Collections.singletonList(ClassificationAttributeUnit.class), true, true, 0, -1).getResult();
        switch(units.size())
        {
            case 0:
                throw new JaloItemNotFoundException("no attribute unit with symbol '" + unitSymbol + "'", 0);
            case 1:
                return units.iterator().next();
        }
        throw new JaloSystemException("more than one attribute unit found with symbol '" + unitSymbol + "'", 0);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public ClassificationAttributeUnit getAttributeUnit(String code) throws JaloItemNotFoundException
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("code", code);
        values.put("me", getPK());
        List<ClassificationAttributeUnit> units = FlexibleSearch.getInstance()
                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTEUNIT + "} WHERE {systemVersion}=?me AND {code}=?code", values, Collections.singletonList(ClassificationAttributeUnit.class), true, true, 0, -1).getResult();
        switch(units.size())
        {
            case 0:
                throw new JaloItemNotFoundException("no attribute unit with code '" + code + "'", 0);
            case 1:
                return units.iterator().next();
        }
        throw new JaloSystemException("more than one attribute unit found with code '" + code + "'", 0);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<ClassificationAttributeUnit> getAttributeUnits()
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("me", getPK());
        return
                        FlexibleSearch.getInstance()
                                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.CLASSIFICATIONATTRIBUTEUNIT + "} WHERE {systemVersion}= ?me ", values,
                                                        Collections.singletonList(ClassificationAttributeUnit.class), true, true, 0, -1)
                                        .getResult();
    }
}
