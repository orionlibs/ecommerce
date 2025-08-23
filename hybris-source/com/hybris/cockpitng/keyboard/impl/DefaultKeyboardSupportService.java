/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.keyboard.impl;

import com.hybris.cockpitng.keyboard.KeyboardSupportService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.KeyEvent;

/**
 * Default implementation of keyboard support service. {@link KeyboardSupportService}
 */
public class DefaultKeyboardSupportService implements KeyboardSupportService
{
    protected static final String PREFIX_SIMPLE_KEYS = "prefixSimpleKeys";
    protected static final String PREFIX_FUNCTION_KEYS = "prefixFunctionKeys";
    protected static final String FUNCTION_KEYS = "functionKeys";
    protected static final String SIMPLE_KEYS = "simpleKeys";
    /**
     * Patter should find for given @0@b@#ins^1^c^#del$2$d$#bak#f10 following groups
     * "@0", "@b", "@#ins", "^1", "^c", "^#del", "$2", "$d", "$#bak", "#f10".
     */
    protected static final Pattern PATTERN = Pattern.compile("" + "(?<" + PREFIX_SIMPLE_KEYS + ">([@\\^\\$]{1,3}))(?<"
                    + SIMPLE_KEYS + ">([\\w]))" + "|" + "(?<" + PREFIX_FUNCTION_KEYS + ">([@\\^\\$]{0,3}))" + "(?<" + FUNCTION_KEYS
                    + ">(#(home|del|up|pgdn|end|left|down|bak|ins|right|pgup|([f,F]((1[0-2])|[1-9])))))");
    /**
     * Patter should check if prefix has only !^@ without duplicates
     */
    protected static final Pattern PREFIX_PATTERN = Pattern.compile("^(?=[@\\^\\$]{1,3}$)(?!.*(.).*\\1).*$");
    private final Map<Integer, String> keyEventMapping;


    /**
     * Initialize binding between key codes and it's string representations
     */
    public DefaultKeyboardSupportService()
    {
        keyEventMapping = new HashMap<>();
        keyEventMapping.put(KeyEvent.F1, F1);
        keyEventMapping.put(KeyEvent.F2, F2);
        keyEventMapping.put(KeyEvent.F3, F3);
        keyEventMapping.put(KeyEvent.F4, F4);
        keyEventMapping.put(KeyEvent.F5, F5);
        keyEventMapping.put(KeyEvent.F6, F6);
        keyEventMapping.put(KeyEvent.F7, F7);
        keyEventMapping.put(KeyEvent.F8, F8);
        keyEventMapping.put(KeyEvent.F9, F9);
        keyEventMapping.put(KeyEvent.F10, F10);
        keyEventMapping.put(KeyEvent.F11, F11);
        keyEventMapping.put(KeyEvent.F12, F12);
        keyEventMapping.put(KeyEvent.PAGE_UP, PAGE_UP);
        keyEventMapping.put(KeyEvent.PAGE_DOWN, PAGE_DOWN);
        keyEventMapping.put(KeyEvent.END, END);
        keyEventMapping.put(KeyEvent.HOME, HOME);
        keyEventMapping.put(KeyEvent.LEFT, LEFT);
        keyEventMapping.put(KeyEvent.UP, UP);
        keyEventMapping.put(KeyEvent.RIGHT, RIGHT);
        keyEventMapping.put(KeyEvent.DOWN, DOWN);
        keyEventMapping.put(8, BACKSPACE);
        keyEventMapping.put(KeyEvent.INSERT, INSERT);
        keyEventMapping.put(KeyEvent.DELETE, DELETE);
    }


    /**
     * Splits given triggerOnKeys in key codes
     *
     * @param ctrlKeys triggerOnKeys e.g. "@0^c$#del#f10"
     * @return set with following values "@0", "^c","$#del", "#f10"
     */
    protected Set<String> splitCtrlKeys(final String ctrlKeys)
    {
        final Matcher matcher = PATTERN.matcher(ctrlKeys);
        final Set<String> keys = new HashSet<>();
        while(matcher.find())
        {
            keys.add(matcher.group());
        }
        return keys;
    }


    @Override
    public boolean containsKeyEvent(final String ctrlKeys, final KeyEvent keyEvent)
    {
        if(StringUtils.isNotBlank(ctrlKeys))
        {
            final String keyEventCode = convertToKeyEventCode(keyEvent);
            final Matcher matcher = PATTERN.matcher(ctrlKeys);
            while(matcher.find())
            {
                if(keyCodesMatch(keyEventCode, matcher.group()))
                {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * Tells if two keyCodes are equal where modifiers can be in random order. E.g. @$^#del is equal to $@^#del
     *
     * @param keyCodeOne $^#del
     * @param keyCodeTwo $@^#del
     * @return true
     */
    protected boolean keyCodesMatch(final String keyCodeOne, final String keyCodeTwo)
    {
        if(keyCodeOne.length() != keyCodeTwo.length())
        {
            return false;
        }
        if(keyCodeOne.equalsIgnoreCase(keyCodeTwo))
        {
            return true;
        }
        final char[] charsOne = keyCodeOne.toCharArray();
        final char[] charsTwo = keyCodeTwo.toCharArray();
        Arrays.sort(charsOne);
        Arrays.sort(charsTwo);
        return Arrays.equals(charsOne, charsTwo);
    }


    @Override
    public String mergeCtrlKeys(final String... ctrlKeysArg)
    {
        final Map<String, String> joinedMap = new HashMap<>();
        for(final String ctrlKeys : ctrlKeysArg)
        {
            splitCtrlKeys(ctrlKeys).forEach(ctrlKey -> {
                final String lowerCaseCtrlKeys = ctrlKey.toLowerCase();
                final char[] chars = lowerCaseCtrlKeys.toCharArray();
                Arrays.sort(chars);
                joinedMap.put(String.valueOf(chars), lowerCaseCtrlKeys);
            });
        }
        return StringUtils.join(joinedMap.values(), null);
    }


    @Override
    public boolean validateCtrlKeys(final String ctrlKeys)
    {
        int correctKeysSize = 0;
        if(StringUtils.isNotBlank(ctrlKeys))
        {
            final Matcher matcher = PATTERN.matcher(ctrlKeys);
            while(matcher.find())
            {
                final boolean prefixMatches;
                final String simpleKeys = matcher.group(SIMPLE_KEYS);
                if(StringUtils.isNotBlank(simpleKeys))
                {
                    final String prefix = matcher.group(PREFIX_SIMPLE_KEYS);
                    prefixMatches = StringUtils.isNotBlank(prefix)
                                    && (!StringUtils.containsOnly(prefix, SHIFT_PREFIX) && PREFIX_PATTERN.matcher(prefix).matches());
                }
                else
                {
                    final String prefix = matcher.group(PREFIX_FUNCTION_KEYS);
                    prefixMatches = StringUtils.isBlank(prefix) || PREFIX_PATTERN.matcher(prefix).matches();
                }
                if(prefixMatches)
                {
                    final String group = matcher.group();
                    if(group != null)
                    {
                        correctKeysSize += group.length();
                    }
                }
            }
        }
        return ctrlKeys != null && ctrlKeys.length() == correctKeysSize;
    }


    /**
     * Converts given key code to it's string representation.
     *
     * @param key {@link KeyEvent#DELETE} which is represented by 46
     * @return string representation of key e.g. #del
     */
    protected String convertToKeyEventCode(final int key)
    {
        return keyEventMapping.containsKey(key) ? keyEventMapping.get(key) : Character.toString((char)key).toLowerCase();
    }


    @Override
    public String convertToKeyEventCode(final KeyEvent keyEvent)
    {
        final StringBuilder prefix = new StringBuilder();
        if(keyEvent.isShiftKey())
        {
            prefix.append(SHIFT_PREFIX);
        }
        if(keyEvent.isAltKey())
        {
            prefix.append(ALT_PREFIX);
        }
        if(keyEvent.isCtrlKey())
        {
            prefix.append(CTRL_PREFIX);
        }
        prefix.append(convertToKeyEventCode(keyEvent.getKeyCode()));
        return prefix.toString();
    }


    @Override
    public String getToolTip(final String ctrlKeys)
    {
        final Set<String> keys = splitCtrlKeys(ctrlKeys);
        final List<String> toolTips = new ArrayList<>();
        keys.forEach(ctrlKey -> {
            final Matcher matcher = PATTERN.matcher(ctrlKey);
            if(matcher.find())
            {
                final StringBuilder sb = new StringBuilder();
                final String simpleKeys = matcher.group(SIMPLE_KEYS);
                if(StringUtils.isNotBlank(simpleKeys))
                {
                    sb.append(convertPrefix(matcher.group(PREFIX_SIMPLE_KEYS)));
                    sb.append(simpleKeys);
                }
                else
                {
                    final String fnKeys = matcher.group(FUNCTION_KEYS);
                    if(StringUtils.isNotBlank(fnKeys))
                    {
                        sb.append(convertPrefix(matcher.group(PREFIX_FUNCTION_KEYS)));
                        sb.append(getLabel("keyboard." + fnKeys.substring(1).toLowerCase()));
                    }
                }
                if(sb.length() > 0)
                {
                    toolTips.add(sb.toString());
                }
            }
        });
        return String.format("[%s]", StringUtils.join(toolTips, ", "));
    }


    protected String convertPrefix(final String prefix)
    {
        final StringBuilder sb = new StringBuilder();
        if(StringUtils.isNotBlank(prefix))
        {
            if(StringUtils.contains(prefix, CTRL_PREFIX))
            {
                sb.append(getLabel("keyboard.ctrl"));
                sb.append("+");
            }
            if(StringUtils.contains(prefix, ALT_PREFIX))
            {
                sb.append(getLabel("keyboard.alt"));
                sb.append("+");
            }
            if(StringUtils.contains(prefix, SHIFT_PREFIX))
            {
                sb.append(getLabel("keyboard.shift"));
                sb.append("+");
            }
        }
        return sb.toString();
    }


    protected String getLabel(final String key)
    {
        return Labels.getLabel(key);
    }
}
