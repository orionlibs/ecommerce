package de.hybris.platform.ruleengine.util;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengine.constants.RuleEngineConstants;
import de.hybris.platform.ruleengine.model.DroolsKIEModuleModel;
import de.hybris.platform.ruleengine.model.DroolsRuleModel;
import java.io.File;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RuleEngineUtils
{
    private static final String RULE_SPLITTING_PATTERN = "(query\\s+.*)(rule\\s+.*)";
    private static Pattern ruleSplittingRegexp = Pattern.compile("(query\\s+.*)(rule\\s+.*)", 32);


    public static String getCleanedContent(String seedRuleContent, String ruleUuid)
    {
        if(Objects.isNull(seedRuleContent))
        {
            return null;
        }
        Matcher matcher = ruleSplittingRegexp.matcher(seedRuleContent);
        StringBuilder cleanedContentSB = new StringBuilder();
        if(matcher.find())
        {
            cleanedContentSB.append(matcher.group(1).trim()).append(matcher.group(2).trim());
        }
        else
        {
            cleanedContentSB.append(seedRuleContent.trim());
        }
        String uuidConcat = ruleUuid.replace("-", "");
        return cleanedContentSB.toString().replace(ruleUuid, "RULE_UUID").replace(uuidConcat, "RULEUUID");
    }


    public static String getNormalizedRulePath(String rulePath)
    {
        if(Objects.isNull(rulePath))
        {
            return null;
        }
        return rulePath.replace(File.separatorChar, '/');
    }


    public static String getRulePath(DroolsRuleModel rule)
    {
        String rulePackagePath = "";
        if(rule.getRulePackage() != null)
        {
            rulePackagePath = rule.getRulePackage().replace('.', File.separatorChar);
        }
        return getNormalizedRulePath(RuleEngineConstants.DROOLS_BASE_PATH + RuleEngineConstants.DROOLS_BASE_PATH + rulePackagePath + "RuleMedia.drl");
    }


    public static String stripDroolsMainResources(String normalizedPath)
    {
        String normalizedDroolsBasePath = getNormalizedRulePath(RuleEngineConstants.DROOLS_BASE_PATH);
        if(normalizedDroolsBasePath != null && normalizedPath.startsWith(normalizedDroolsBasePath))
        {
            return normalizedPath.substring(normalizedDroolsBasePath.length());
        }
        return normalizedPath;
    }


    public static String getDeployedRulesModuleVersion(DroolsKIEModuleModel rulesModule)
    {
        Preconditions.checkArgument(Objects.nonNull(rulesModule), "Rules module shouldn't be null here");
        return rulesModule.getMvnVersion() + "." + rulesModule.getMvnVersion();
    }


    public static boolean isDroolsKieModuleDeployed(DroolsKIEModuleModel rulesModule)
    {
        return getDeployedRulesModuleVersion(rulesModule).equals(rulesModule.getDeployedMvnVersion());
    }
}
