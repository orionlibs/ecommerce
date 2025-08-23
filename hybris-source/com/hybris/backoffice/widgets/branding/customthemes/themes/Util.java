/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.widgets.branding.customthemes.themes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.media.MediaService;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util
{
    private static final Logger LOG = LoggerFactory.getLogger(Util.class);
    private static final String HEX_COLOR_SHORT_REGEXP = "#([0-9a-fA-F])([0-9a-fA-F])([0-9a-fA-F])";
    private static final String HEX_COLOR_LONG_REPLACEMENT = "#$1$1$2$2$3$3";
    private static final Pattern BO_VARIABLE_PATTERN = Pattern.compile("\\-\\-bo\\-.*\\:.*\\;");
    private static final Pattern PRIMARY_VARIABLE_PATTERN = Pattern.compile("\\-\\-primary.*\\:.*\\;");
    private static final Pattern SAP_VARIABLE_PATTERN = Pattern.compile("\\-\\-sap.*\\:.*\\;");
    private static final Pattern VALID_HEX_COLOR_REGEXP_PATTERN = Pattern.compile("^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$");
    private static final Pattern SHORT_HEX_COLOR_REGEXP_PATTERN = Pattern.compile(HEX_COLOR_SHORT_REGEXP);
    private static final Pattern BO_VARIABLE_VALUE_REGEXP_PATTERN = Pattern.compile("var\\(\\-\\-sap.*\\)");
    private MediaService mediaService;


    protected InputStream mapToStyleInputStream(final Map<String, String> map)
    {
        final var sb = new StringBuilder();
        sb.append(":root {\n");
        map.entrySet().forEach(entry -> sb.append(String.format("%s: %s;", entry.getKey(), entry.getValue())).append("\n"));
        sb.append("}\n");
        return IOUtils.toInputStream(sb.toString(), StandardCharsets.UTF_8);
    }


    protected boolean isValidHexColor(final String color)
    {
        return VALID_HEX_COLOR_REGEXP_PATTERN.matcher(color).matches();
    }


    protected String toLongHexColor(final String color)
    {
        return color != null && SHORT_HEX_COLOR_REGEXP_PATTERN.matcher(color).matches()
                        ? color.replaceAll(HEX_COLOR_SHORT_REGEXP, HEX_COLOR_LONG_REPLACEMENT)
                        : color;
    }


    protected boolean isValidThemeStyle(final MediaModel style)
    {
        return !convertStyleMediaToVariableMap(style).isEmpty();
    }


    public Map<String, String> convertStyleMediaToVariableMap(final MediaModel style)
    {
        try
        {
            if(Objects.isNull(style) || !getMediaService().hasData(style))
            {
                return Collections.emptyMap();
            }
            final String data = IOUtils.toString(getMediaService().getStreamFromMedia(style), StandardCharsets.UTF_8);
            final Map<String, String> boCssVarMap = extractCssVariables(BO_VARIABLE_PATTERN, data);
            final Map<String, String> primaryCssVarMap = extractCssVariables(PRIMARY_VARIABLE_PATTERN, data);
            final Map<String, String> sapCssVarMap = extractCssVariables(SAP_VARIABLE_PATTERN, data);
            boCssVarMap.putAll(primaryCssVarMap);
            replaceCssVariableValues(boCssVarMap, sapCssVarMap);
            return boCssVarMap;
        }
        catch(final IOException e)
        {
            LOG.error("Read theme style media failed", e);
        }
        return Collections.emptyMap();
    }


    /**
     * Map bo css variable value to real values e.g. var(--sapTile_TextColor) to #xxxx
     */
    private void replaceCssVariableValues(final Map<String, String> boCssVarMap, final Map<String, String> sapCssVarMap)
    {
        boCssVarMap.entrySet().forEach(entry -> {
            final var value = entry.getValue();
            if(BO_VARIABLE_VALUE_REGEXP_PATTERN.matcher(value).matches())
            {
                //cut string "var(--sapTile_TextColor)" to "--sapTile_TextColor"
                final var key = value.substring(4, value.length() - 1);
                entry.setValue(toLongHexColor(sapCssVarMap.get(key)));
            }
        });
    }


    private Map<String, String> extractCssVariables(final Pattern pattern, final String data)
    {
        final Map<String, String> cssVarMap = new LinkedHashMap<>();
        final Matcher matcher = pattern.matcher(data);
        matcher.results().forEach(matchResult -> {
            final var matchString = matchResult.group();
            final var keyValue = matchString.substring(0, matchString.length() - 1).split(":");
            cssVarMap.put(keyValue[0].trim(), keyValue[1].trim());
        });
        return cssVarMap;
    }


    protected List<ThemeVariablesMapping> getThemeVariablesMapping()
    {
        try(final InputStream is = this.getClass().getResourceAsStream("ThemeVariablesMapping.json"))
        {
            final String data = IOUtils.toString(is, StandardCharsets.UTF_8);
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(data, new TypeReference<List<ThemeVariablesMapping>>()
            {
            });
        }
        catch(final IOException ex)
        {
            LOG.error("Load ThemeVariablesMapping failed", ex);
        }
        return Collections.emptyList();
    }


    protected byte[] getPreviewImageData(final String previewImgData)
    {
        return Base64.getDecoder().decode(previewImgData);
    }


    public MediaService getMediaService()
    {
        return mediaService;
    }


    public void setMediaService(final MediaService mediaService)
    {
        this.mediaService = mediaService;
    }
}
