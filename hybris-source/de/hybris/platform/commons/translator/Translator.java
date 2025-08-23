package de.hybris.platform.commons.translator;

import de.hybris.platform.commons.corrector.HTMLCorrector;
import de.hybris.platform.commons.translator.nodes.AbstractNode;
import de.hybris.platform.commons.translator.nodes.SimpleNode;
import de.hybris.platform.commons.translator.parsers.AbstractParser;
import de.hybris.platform.commons.translator.prerenderers.Prerenderer;
import de.hybris.platform.commons.translator.renderers.AbstractRenderer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Translator
{
    TranslatorConfiguration config;
    private static final String MAIN_NODE = "mainNode";
    private static final String TEXT_NODE = "textNode";
    private HashMap<String, Object> contextProperties = new HashMap<>();


    public Translator(TranslatorConfiguration config)
    {
        this.config = config;
    }


    public AbstractNode createNodesTree(String text)
    {
        SimpleNode simpleNode = new SimpleNode("mainNode", text);
        simpleNode.addChildNodes(parseText(simpleNode.getNodeText()));
        return (AbstractNode)simpleNode;
    }


    public List<AbstractNode> parseText(String text)
    {
        List<AbstractNode> childNodes = new ArrayList<>();
        if(text == null)
        {
            return null;
        }
        int cursor = 0;
        int lastCursorPosition = 0;
        boolean notStop = true;
        while(notStop)
        {
            notStop = false;
            AbstractParser abstractParser = null;
            String startTag = null;
            Set<String> expressions = this.config.getParsers();
            int where = text.length() + 1;
            Matcher matcher = null;
            for(String startValue : expressions)
            {
                Pattern pattern = Pattern.compile(startValue, 42);
                Matcher patternMatcher = pattern.matcher(text);
                if(patternMatcher.find(cursor) && where > patternMatcher.start())
                {
                    where = patternMatcher.start();
                    matcher = patternMatcher;
                    notStop = true;
                    abstractParser = this.config.getParser(startValue);
                }
            }
            if(notStop)
            {
                cursor = where;
                if(cursor != lastCursorPosition)
                {
                    SimpleNode simpleNode = new SimpleNode("textNode", text.substring(lastCursorPosition, cursor));
                    childNodes.add(simpleNode);
                    lastCursorPosition = cursor;
                }
                startTag = matcher.group(0);
                String startEndValue = abstractParser.getStartEndExpression();
                boolean startEndTag = false;
                Pattern pattern = Pattern.compile(startEndValue, 42);
                Matcher patternMatcher = pattern.matcher(text.substring(cursor));
                if(patternMatcher.find() && patternMatcher.start() == 0)
                {
                    startEndTag = true;
                }
                if(startEndTag)
                {
                    childNodes.add(abstractParser.createNode(startTag, null, null, this));
                    cursor += startTag.length();
                    lastCursorPosition = cursor;
                    continue;
                }
                String[] tag = searchForClosingTag(abstractParser, startTag, text.substring(cursor));
                childNodes.add(abstractParser.createNode(tag[0], tag[1], tag[2], this));
                cursor += tag[0].length() + tag[1].length() + tag[2].length();
                lastCursorPosition = cursor;
            }
        }
        if(lastCursorPosition != 0 && lastCursorPosition != text.length())
        {
            childNodes.add(new SimpleNode("textNode", text.substring(lastCursorPosition, text.length())));
        }
        return childNodes;
    }


    public String renderTextFromNode(AbstractNode node)
    {
        if(node == null)
        {
            return "";
        }
        AbstractRenderer renderer = this.config.getRenderer(node.getNodeName());
        if(renderer == null)
        {
            renderer = this.config.getDefaultRenderer();
        }
        return renderer.renderTextFromNode(node, this);
    }


    public String translate(String htmlInput)
    {
        return renderTextFromNode(prepareNode(createNodesTree(HTMLCorrector.correct(htmlInput, "replace.strategy", "/commons/corrector/brreplacestrategy.properties"))));
    }


    public String[] searchForClosingTag(AbstractParser abstractParser, String startTag, String textToSearch)
    {
        String endExpression = abstractParser.getEndExpression(startTag);
        Pattern pattern = Pattern.compile(endExpression, 42);
        Matcher patternMatcher = pattern.matcher(textToSearch);
        String endTag = null;
        String tagText = null;
        boolean searching = true;
        label27:
        while(searching)
        {
            Set<Integer> ignorePositions = new HashSet<>();
            searching = false;
            patternMatcher.find();
            endTag = textToSearch.substring(patternMatcher.start(), patternMatcher.end());
            tagText = textToSearch.substring(startTag.length(), patternMatcher.start());
            for(String expression2 : this.config.getParsers())
            {
                Pattern pattern2 = Pattern.compile(expression2, 42);
                Matcher patternMatcher2 = pattern2.matcher(tagText);
                while(patternMatcher2.find())
                {
                    if(!ignorePositions.contains(Integer.valueOf(patternMatcher2.start())))
                    {
                        String startEndExpression2 = this.config.getParser(expression2).getStartEndExpression();
                        Pattern pattern3 = Pattern.compile(startEndExpression2, 42);
                        Matcher patternMatcher3 = pattern3.matcher(tagText.substring(patternMatcher2.start()));
                        if(!patternMatcher3.find() || patternMatcher3.start() > 0)
                        {
                            searching = true;
                            String endExpression2 = this.config.getParser(expression2).getEndExpression(expression2);
                            Pattern pattern4 = Pattern.compile(endExpression2, 42);
                            Matcher patternMatcher4 = pattern4.matcher(tagText);
                            while(patternMatcher4.find())
                            {
                                if(!ignorePositions.contains(Integer.valueOf(patternMatcher4.start())))
                                {
                                    searching = false;
                                    ignorePositions.add(Integer.valueOf(patternMatcher4.start()));
                                    break;
                                }
                            }
                            if(searching)
                            {
                                continue label27;
                            }
                        }
                        ignorePositions.add(Integer.valueOf(patternMatcher2.start()));
                    }
                }
            }
        }
        String[] returnValue = {startTag, endTag, tagText};
        return returnValue;
    }


    public String renderContent(AbstractNode node)
    {
        String content = "";
        if(node == null)
        {
            return content;
        }
        if(node.getChildNodes() != null && node.getChildNodes().size() > 0)
        {
            for(AbstractNode child : node.getChildNodes())
            {
                content = content + content;
            }
        }
        if(node.getNodeText() != null && (node.getChildNodes() == null || node.getChildNodes().size() == 0))
        {
            content = content + content;
        }
        return content;
    }


    public HashMap<String, Object> getContextProperties()
    {
        return this.contextProperties;
    }


    public Object getContextProperty(String propertyName)
    {
        return this.contextProperties.get(propertyName);
    }


    public void setContextProperties(HashMap<String, Object> contextProperties)
    {
        this.contextProperties = contextProperties;
    }


    public void addContextProperty(String propertyName, Object propertyValue)
    {
        this.contextProperties.put(propertyName, propertyValue);
    }


    public void addAllContextProperties(HashMap<String, Object> additionalContextProperties)
    {
        this.contextProperties.putAll(additionalContextProperties);
    }


    public AbstractNode prepareNode(AbstractNode node)
    {
        if(this.config != null)
        {
            for(Prerenderer prerenderer : this.config.getPrerendersList())
            {
                node = prerenderer.prepareNode(node);
            }
        }
        return node;
    }
}
