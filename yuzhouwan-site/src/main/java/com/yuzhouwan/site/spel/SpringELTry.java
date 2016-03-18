package com.yuzhouwan.site.spel;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：Spring EL Demo
 *
 * @author Benedict Jin
 * @since 2016/3/15
 */
public class SpringELTry {

    public String hello() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression =
                parser.parseExpression("('Hello' + ', World').concat(#end)");
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("end", "!");
        return expression.getValue(context).toString();
    }

    public boolean matchExpression() {
        ExpressionParser parser = new SpelExpressionParser();
        /**
         * java.lang.IllegalStateException: Cannot handle (59) ';'
         */
        String s = "Feb 23 11:09:17 2016 GX-NN-SR-1.D.S5820 %%10SSHS/6/SSHLOG: -DevIP=116.1.239.33; User lianghb logged out from 219.143.200.182 port 65164.";
        String regular = "^\\w+ \\d+ \\d{2}:\\d{2}:\\d{2} \\d{4} \\b(?>(\\w+-){3}\\d\\.\\w\\.).{5}\\b %%10\\w+/\\d/\\w+:.+$";
        Expression expression = parser.parseExpression(String.format("'%s' matches '%s'", s, regular));
        return expression.getValue(Boolean.class);
    }

}
