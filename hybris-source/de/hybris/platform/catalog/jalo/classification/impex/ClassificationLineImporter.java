package de.hybris.platform.catalog.jalo.classification.impex;

import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.classification.ClassAttributeAssignment;
import de.hybris.platform.catalog.jalo.classification.ClassificationClass;
import de.hybris.platform.catalog.jalo.classification.util.FeatureContainer;
import de.hybris.platform.catalog.jalo.classification.util.FeatureValue;
import de.hybris.platform.catalog.jalo.classification.util.TypedFeature;
import de.hybris.platform.core.PK;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.impex.jalo.translators.SpecialValueTranslator;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.MapUtils;

class ClassificationLineImporter
{
    static void importFeatures(ValueLine line, HeaderDescriptor header, Product product) throws ImpExException
    {
        Collection<ClassificationAttributeTranslator> translators = null;
        Iterator<SpecialColumnDescriptor> it = header.getSpecificColumns(SpecialColumnDescriptor.class).iterator();
        while(it.hasNext())
        {
            SpecialValueTranslator valueTranslator = ((SpecialColumnDescriptor)it.next()).getValueTranslator();
            if(valueTranslator instanceof ClassificationAttributeTranslator)
            {
                if(translators == null)
                {
                    translators = new ArrayList<>();
                }
                translators.add((ClassificationAttributeTranslator)valueTranslator);
            }
        }
        if(translators != null && !translators.isEmpty())
        {
            importFeatures(line, translators, product);
        }
    }


    static void importFeatures(ValueLine line, Collection<ClassificationAttributeTranslator> valueTranslators, Product product) throws ImpExException
    {
        Map<ClassificationAttributeTranslator, ClassAttributeAssignment> assignmentsFromTranslators = collectAssignmentsFromTranslators(valueTranslators, product);
        try
        {
            if(MapUtils.isNotEmpty(assignmentsFromTranslators))
            {
                extractAndStoreValues(line, product, assignmentsFromTranslators);
            }
        }
        finally
        {
            markUnusedTranslatorsAsDone(product, valueTranslators, assignmentsFromTranslators);
        }
    }


    static void extractAndStoreValues(ValueLine line, Product product, Map<ClassificationAttributeTranslator, ClassAttributeAssignment> assignmentsFromTranslators) throws ImpExException
    {
        List<ClassAttributeAssignment> assignments = new ArrayList<>(assignmentsFromTranslators.values());
        FeatureContainer cont = FeatureContainer.loadTyped(product, assignments);
        for(Map.Entry<ClassificationAttributeTranslator, ClassAttributeAssignment> e : assignmentsFromTranslators.entrySet())
        {
            extractAndStoreValue(line, product, cont, e.getKey(), e.getValue());
        }
        try
        {
            cont.store();
        }
        catch(ConsistencyCheckException e1)
        {
            throw new ImpExException("error writing classification features " + cont + " : " + e1.getMessage());
        }
        finally
        {
            markTranslatorsAsDone(product, assignmentsFromTranslators);
        }
    }


    static void markUnusedTranslatorsAsDone(Product product, Collection<ClassificationAttributeTranslator> valueTranslators, Map<ClassificationAttributeTranslator, ClassAttributeAssignment> assignmentsFromTranslators)
    {
        Map<ClassificationAttributeTranslator, ClassAttributeAssignment> unusedAssignmentsFromTranslators = new LinkedHashMap<>();
        for(ClassificationAttributeTranslator translator : valueTranslators)
        {
            if(!assignmentsFromTranslators.containsKey(translator))
            {
                unusedAssignmentsFromTranslators.put(translator, null);
            }
        }
        if(MapUtils.isNotEmpty(unusedAssignmentsFromTranslators))
        {
            markTranslatorsAsDone(product, unusedAssignmentsFromTranslators);
        }
    }


    static void markTranslatorsAsDone(Product product, Map<ClassificationAttributeTranslator, ClassAttributeAssignment> assignmentsFromTranslators)
    {
        PK myPK = product.getPK();
        for(Map.Entry<ClassificationAttributeTranslator, ClassAttributeAssignment> e : assignmentsFromTranslators.entrySet())
        {
            ClassificationAttributeTranslator translator = e.getKey();
            translator.allDoneFor = myPK;
            translator.currentCellValue = null;
        }
    }


    static void extractAndStoreValue(ValueLine line, Product product, FeatureContainer cont, ClassificationAttributeTranslator clTrans, ClassAttributeAssignment assignment) throws HeaderValidationException
    {
        Collection<UnitAwareValue> valuesToImport = clTrans.translateCurrentUnitAwareValues(line, assignment, product);
        boolean localized = assignment.isLocalizedAsPrimitive();
        SessionContext ctx = clTrans.getValueCtx(localized);
        TypedFeature feature = cont.getFeature(assignment);
        if(valuesToImport == null)
        {
            feature.clear(ctx);
        }
        else if(!valuesToImport.isEmpty())
        {
            List<FeatureValue> actualValues = feature.getLanguageValues(ctx, feature.extractLanguage(ctx), false);
            if(removeActualValues(valuesToImport, actualValues))
            {
                feature.clear(ctx);
                actualValues = Collections.EMPTY_LIST;
            }
            int i = 0;
            for(UnitAwareValue value : valuesToImport)
            {
                if(actualValues.size() > 0)
                {
                    FeatureValue featureValue1 = actualValues.get(i++);
                    featureValue1.setValue(value.getValue());
                    if(value.hasUnit())
                    {
                        featureValue1.setUnit(value.getUnit());
                    }
                    continue;
                }
                FeatureValue featureValue = feature.createValue(ctx, -1, value.getValue(), false);
                if(value.hasUnit())
                {
                    featureValue.setUnit(value.getUnit());
                }
            }
        }
    }


    private static boolean removeActualValues(Collection<UnitAwareValue> valuesToImport, List actualValues)
    {
        return (actualValues.size() > 0 && actualValues.size() != valuesToImport.size());
    }


    static Map<ClassificationAttributeTranslator, ClassAttributeAssignment> collectAssignmentsFromTranslators(Collection<ClassificationAttributeTranslator> valueTranslators, Product product)
    {
        PK ppk = product.getPK();
        Map<ClassificationAttributeTranslator, ClassAttributeAssignment> ret = null;
        List<ClassificationClass> productClasses = null;
        for(SpecialValueTranslator trans : valueTranslators)
        {
            if(trans instanceof ClassificationAttributeTranslator)
            {
                ClassificationAttributeTranslator cTrans = (ClassificationAttributeTranslator)trans;
                if(ppk.equals(cTrans.allDoneFor))
                {
                    break;
                }
                if(cTrans.currentCellValue != null && cTrans.currentCellValue.startsWith("<ignore>"))
                {
                    continue;
                }
                ClassAttributeAssignment assignment = cTrans.getAssignment();
                if(assignment == null)
                {
                    if(productClasses == null)
                    {
                        productClasses = CatalogManager.getInstance().getClassificationClasses(product);
                    }
                    assignment = cTrans.matchAssignment(productClasses);
                }
                if(assignment != null)
                {
                    if(ret == null)
                    {
                        ret = new LinkedHashMap<>(valueTranslators.size() * 2);
                    }
                    ret.put(cTrans, assignment);
                }
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_MAP;
    }
}
