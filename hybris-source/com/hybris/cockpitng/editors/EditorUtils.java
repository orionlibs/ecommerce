/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.editors;

import com.hybris.cockpitng.dataaccess.facades.type.CollectionDataType;
import com.hybris.cockpitng.dataaccess.facades.type.DataAttribute;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.dataaccess.facades.type.MapDataType;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;

public final class EditorUtils
{
    private static final String REFERENCE_EDITOR_PATTERN = "Reference($3)";
    private static final Pattern REGEX_LOCALIZED_EDITOR_PATTERN = Pattern.compile("^Localized\\(([\\w.]+)\\)$");
    private static final Pattern REGEX_REFERENCE_EDITOR_PATTERN = Pattern.compile("^Reference\\(([\\w.]+)\\)$");
    private static final Pattern REGEX_MULTI_REF_EDITOR_PATTERN = Pattern
                    .compile("^(Enum)?MultiReference-(COLLECTION|LIST|SET)\\((\\s*([\\w.]+)\\s*)\\)$");
    private static final Pattern REGEX_LIST_EDITOR_PATTERN = Pattern.compile("^List\\(\\s*(.+)\\s*\\)$");
    private static final Pattern REGEX_MAP_EDITOR_PATTERN = Pattern.compile("^Map\\(\\s*(.+)\\s*,\\s*(.+)\\s*\\)$");


    private EditorUtils()
    {
        throw new AssertionError("Utility class should not be instantiated");
    }


    /**
     * Returns precompiled regular expression pattern identifying localized Editor with capture group containing nested type
     */
    public static Pattern getLocalizedEditorPattern()
    {
        return REGEX_LOCALIZED_EDITOR_PATTERN;
    }


    /**
     * Returns precompiled regular expression pattern identifying Reference Editor with capture group containing nested type
     */
    public static Pattern getReferenceEditorPattern()
    {
        return REGEX_REFERENCE_EDITOR_PATTERN;
    }


    /**
     * Returns precompiled regular expression pattern identifying MultiReference Editor with capture group containing nested
     * type
     */
    public static Pattern getMultiReferenceEditorPattern()
    {
        return REGEX_MULTI_REF_EDITOR_PATTERN;
    }


    /**
     * Returns precompiled regular expression pattern identifying List Editor with capture group containing nested type
     */
    public static Pattern getListEditorPattern()
    {
        return REGEX_LIST_EDITOR_PATTERN;
    }


    /**
     * Returns precompiled regular expression pattern identifying Map Editor with capture group 1 containing map's key type
     * and capture group 2 containing map's value type
     */
    public static Pattern getMapEditorPattern()
    {
        return REGEX_MAP_EDITOR_PATTERN;
    }


    /**
     * Gets a type mapping that would change each multi reference into single reference
     *
     * @return mapping to make all references single
     * @see #getEditorType(DataAttribute, boolean, Map)
     * @see #getEditorType(DataType, Boolean, Map)
     */
    public static Map.Entry<Pattern, String> getReferenceSinglingMapping()
    {
        return ImmutablePair.of(REGEX_MULTI_REF_EDITOR_PATTERN, REFERENCE_EDITOR_PATTERN);
    }


    /**
     * Adds a type mapping that would change each multi reference into single reference
     *
     * @param mappings
     *           current types mapping
     * @see #getEditorType(DataAttribute, boolean, Map)
     * @see #getEditorType(DataType, Boolean, Map)
     */
    public void addReferenceSinglingMapping(final Map<Pattern, String> mappings)
    {
        final Map.Entry<Pattern, String> singlingMapping = getReferenceSinglingMapping();
        mappings.put(singlingMapping.getKey(), singlingMapping.getValue());
    }


    private static String postProcessEditorType(final String editorType, final Boolean simplifiedLocalized,
                    final Map<Pattern, String> customMappings)
    {
        String result = editorType;
        if(MapUtils.isNotEmpty(customMappings))
        {
            final Set<Pattern> editorPatterns = customMappings.keySet();
            for(final Pattern pattern : editorPatterns)
            {
                final Matcher matcher = pattern.matcher(editorType);
                if(matcher.matches())
                {
                    final String replacement = customMappings.get(pattern);
                    result = matcher.replaceAll(replacement);
                    break;
                }
            }
        }
        if(simplifiedLocalized != null)
        {
            return simplifiedLocalized.booleanValue() ? String.format("LocalizedSimple(%s)", result)
                            : String.format("Localized(%s)", result);
        }
        return result;
    }


    /**
     * @param attribute
     *           an attribute for which the editor type should be determined
     * @return the type of editor
     * @see com.hybris.cockpitng.editors.EditorUtils#getEditorType(com.hybris.cockpitng.dataaccess.facades.type.DataAttribute,
     *      boolean)
     */
    public static String getEditorType(final DataAttribute attribute)
    {
        return getEditorType(attribute, false);
    }


    public static String getEditorType(final DataAttribute attribute, final boolean simplifiedLocalized,
                    final Map<Pattern, String> customMappings)
    {
        if(attribute == null)
        {
            return StringUtils.EMPTY;
        }
        final String editorType = getEditorTypeInternal(attribute.getDefinedType());
        return postProcessEditorType(editorType, attribute.isLocalized() ? Boolean.valueOf(simplifiedLocalized) : null,
                        customMappings);
    }


    public static String getEditorType(final DataAttribute attribute, final boolean simplifiedLocalized)
    {
        return getEditorType(attribute, simplifiedLocalized, Collections.emptyMap());
    }


    public static String getEditorType(final DataType type)
    {
        return getEditorTypeInternal(type);
    }


    /**
     * @param type
     *           type of data for which editor is to be found
     * @param simplifiedLocalized
     *           <code>null</code> if not localized, <code>true</code> if editor should be simplified (allows to choose only
     *           one localization
     * @return identity of editor to be used
     */
    public static String getEditorType(final DataType type, final Boolean simplifiedLocalized)
    {
        return getEditorType(type, simplifiedLocalized, Collections.emptyMap());
    }


    /**
     * @param type
     *           type of data for which editor is to be found
     * @param simplifiedLocalized
     *           <code>null</code> if not localized, <code>true</code> if editor should be simplified (allows to choose only
     *           one localization
     * @param customMappings
     *           maps data type patterns to editor types (i.e. multiple references into single reference)
     * @return identity of editor to be used
     */
    public static String getEditorType(final DataType type, final Boolean simplifiedLocalized,
                    final Map<Pattern, String> customMappings)
    {
        final String editorType = getEditorTypeInternal(type);
        return postProcessEditorType(editorType, simplifiedLocalized, customMappings);
    }


    public static String getMapEditorType(final String keyType, final String valueType)
    {
        return String.format("Map(%s, %s)", keyType, valueType);
    }


    public static String getEnumEditorType(final String enumCode)
    {
        return String.format("java.lang.Enum(%s)", enumCode);
    }


    public static String getReferenceEditorType(final String typeCode)
    {
        return String.format("Reference(%s)", typeCode);
    }


    public static String getCollectionEditorType(final DataType valueType, final DataType.Type collectionType)
    {
        if(valueType.isAtomic())
        {
            return String.format("List(%s)", valueType.getCode());
        }
        else
        {
            return String.format("%sMultiReference-%s(%s)", valueType.isEnum() ? "Enum" : StringUtils.EMPTY, collectionType.name(),
                            valueType.getCode());
        }
    }


    public static String getRangeEditorType(final String valueType)
    {
        return String.format("Range(%s)", valueType);
    }


    public static String getLocalizedEditorType(final String valueType)
    {
        return String.format("Localized(%s)", valueType);
    }


    public static String getFeatureEditorType()
    {
        return "Feature";
    }


    public static String getFeatureValueEditorType(final String valueType)
    {
        return String.format("FeatureValue(%s)", valueType);
    }


    private static String getEditorTypeInternal(final DataType type)
    {
        final String result;
        final DataType valueType;
        switch(type.getType())
        {
            case MAP:
                final MapDataType map = (MapDataType)type;
                final DataType keyType = map.getKeyType();
                valueType = map.getValueType();
                final String key = getEditorTypeInternal(keyType);
                final String value = getEditorTypeInternal(valueType);
                result = getMapEditorType(key, value);
                break;
            case ATOMIC:
                result = type.getCode();
                break;
            case ENUM:
                result = getEnumEditorType(type.getCode());
                break;
            case COMPOUND:
                result = getReferenceEditorType(type.getCode());
                break;
            case SET:
            case LIST:
            case COLLECTION:
                final CollectionDataType collection = (CollectionDataType)type;
                valueType = collection.getValueType();
                result = getCollectionEditorType(valueType, type.getType());
                break;
            default:
                result = StringUtils.EMPTY;
        }
        return result;
    }
}
