package de.hybris.platform.impex.jalo.exp.generator;

import de.hybris.platform.impex.jalo.exp.ExportUtils;
import de.hybris.platform.impex.jalo.exp.ScriptGenerator;
import de.hybris.platform.impex.jalo.media.MediaDataTranslator;
import de.hybris.platform.impex.jalo.translators.TaxValuesTranslator;
import de.hybris.platform.impex.jalo.translators.UserPasswordTranslator;
import de.hybris.platform.jalo.type.ComposedType;
import java.util.Set;
import org.apache.log4j.Logger;

public class MigrationScriptModifier implements ScriptModifier
{
    private static final Logger log = Logger.getLogger(MigrationScriptModifier.class.getName());


    public void init(ScriptGenerator generator)
    {
        generator.addIgnoreColumn("Item", "allDocuments");
        generator.addIgnoreColumn("Website", "rootNavigationElements");
        generator.addIgnoreColumn("Catalog", "rootCategories");
        generator.addIgnoreColumn("CatalogVersion", "rootCategories");
        generator.addIgnoreColumn("ProductFeature", "booleanValue");
        generator.addIgnoreColumn("ProductFeature", "numberValue");
        generator.addIgnoreColumn("ProductFeature", "stringValue");
        generator.addIgnoreColumn("ProductFeature", "rawValue");
        generator.addIgnoreColumn("LuceneIndex", "indexConfigurations");
        generator.addIgnoreColumn("LuceneIndex", "languageConfigurations");
        generator.addIgnoreColumn("IndexConfiguration", "attributeConfigurations");
        generator.addIgnoreColumn("IndexConfiguration", "pendingUpdates");
        generator.addIgnoreColumn("StyleContainer", "owner");
        generator.addIgnoreColumn("Product", "features");
        generator.addIgnoreColumn("Product", "untypedFeatures");
        generator.addIgnoreColumn("ClassificationSystem", "catalogVersions");
        generator.addIgnoreColumn("ClassificationClass", "declaredClassificationAttributeAssignments");
        generator.addIgnoreColumn("PDTRow", "dateRange");
        generator.addIgnoreColumn("Template", "content");
        generator.addSpecialColumn("User", "password[translator=" + UserPasswordTranslator.class
                        .getName() + "]");
        generator.addSpecialColumn("Media", "media[translator=" + MediaDataTranslator.class
                        .getName() + "]");
        generator.addAdditionalModifier("ProductFeature", "value", "translator", "de.hybris.platform.catalog.jalo.classification.impex.ProductFeatureValueTranslator");
        generator.addAdditionalModifier("OrderEntry", "taxValues", "translator", TaxValuesTranslator.class
                        .getName());
        generator.addAdditionalModifier("CartEntry", "taxValues", "translator", TaxValuesTranslator.class
                        .getName());
        generator.addAdditionalModifier("Media", "mediaFormat", "forceWrite", null);
        generator.addAdditionalModifier("Item", "owner", "forceWrite", null);
        generator.addAdditionalModifier("PDTRow", "pg", "forceWrite", null);
        generator.addAdditionalModifier("PDTRow", "product", "forceWrite", null);
        generator.addAdditionalModifier("PDTRow", "ug", "forceWrite", null);
        generator.addAdditionalModifier("PDTRow", "user", "forceWrite", null);
        generator.addAdditionalModifier("AttributeValueAssignment", "systemVersion", "forceWrite", null);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<ComposedType> collectSubtypesWithOwnDeployment(ComposedType rootType)
    {
        return ExportUtils.collectSubtypesWithOwnDeployment(rootType);
    }


    public boolean filterTypeCompletely(ComposedType type)
    {
        return ExportUtils.filterTypeCompletely(type);
    }


    public Set<ComposedType> getExportableRootTypes(ScriptGenerator callback)
    {
        return ExportUtils.getExportableRootTypes(callback);
    }
}
