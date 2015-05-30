package com.kalessil.phpStorm.phpInspectionsEA.inspectors.regularExpressions.optimizeStrategy;

import com.intellij.codeInspection.ProblemHighlightType;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.openapi.util.text.StringUtil;
import com.jetbrains.php.lang.psi.elements.StringLiteralExpression;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NonGreedyTransformCheckStrategy {
    private static final String strProblemDescription = "'%p%' can be replaced with '[^%c%]%m%%c%'";

    static private Pattern regexNonGreedyPattern = null;
    static {
        regexNonGreedyPattern = Pattern.compile("\\.(\\*|\\+)\\?([^\\(\\)\\$\\\\]|\\\\.)");
    }

    static public void apply(final String pattern, @NotNull final StringLiteralExpression target, @NotNull final ProblemsHolder holder) {
        if (!StringUtil.isEmpty(pattern) && pattern.indexOf('?') >= 0) {
            Matcher regexMatcher = regexNonGreedyPattern.matcher(pattern);
            if (regexMatcher.find()) {
                String strError = strProblemDescription
                        .replace("%p%", regexMatcher.group(0))
                        .replace("%c%", regexMatcher.group(2))
                        .replace("%m%", regexMatcher.group(1));
                holder.registerProblem(target, strError, ProblemHighlightType.WEAK_WARNING);
            }
        }
    }
}