package de.hybris.platform.servicelayer.type.daos.impl;

import de.hybris.platform.core.model.type.AtomicTypeModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.core.model.type.TypeModel;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.daos.TypeDao;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Set;

public class DefaultTypeDao implements TypeDao
{
    private TypeManager typeManager;
    private ModelService modelService;


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    private TypeManager getTypeManager()
    {
        if(this.typeManager == null)
        {
            this.typeManager = TypeManager.getInstance();
        }
        return this.typeManager;
    }


    public ComposedTypeModel findComposedTypeByCode(String code)
    {
        try
        {
            return (ComposedTypeModel)getModelService().get(TypeManager.getInstance().getComposedType(code));
        }
        catch(JaloItemNotFoundException inf)
        {
            throw new UnknownIdentifierException("No composed type " + code + " exists");
        }
    }


    public TypeModel findTypeByCode(String code)
    {
        try
        {
            return (TypeModel)getModelService().get(getTypeManager().getType(code));
        }
        catch(JaloItemNotFoundException inf)
        {
            throw new UnknownIdentifierException("No type " + code + " exists");
        }
    }


    public AtomicTypeModel findAtomicTypeByCode(String code)
    {
        TypeModel type = findTypeByCode(code);
        if(type instanceof AtomicTypeModel)
        {
            return (AtomicTypeModel)type;
        }
        throw new UnknownIdentifierException("No atomic type " + code + " exists");
    }


    public AtomicTypeModel findAtomicTypeByJavaClass(Class javaClass)
    {
        Set atomicTypes = getTypeManager().getAtomicTypesForJavaClass(javaClass);
        ServicesUtil.validateIfSingleResult(atomicTypes, "No type for class " + javaClass + " exists", "More than one atomic type for class " + javaClass + " exists");
        return (AtomicTypeModel)getModelService().get(atomicTypes.iterator().next());
    }
}
