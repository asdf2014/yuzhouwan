package com.yuzhouwan.site.spel;

import com.yuzhouwan.common.util.StrUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Copyright @ 2015 yuzhouwan.com
 * All right reserved.
 * Function：Spring EL Demo
 *
 * @author Benedict Jin
 * @since 2016/3/15
 */
public class SpringELTry {

    private static final Logger _log = LoggerFactory.getLogger(SpringELTry.class);

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

    public boolean matchExpression2() {
        ExpressionParser parser = new SpelExpressionParser();
        String s = "-DevIP=116.1.239.33; Line protocol on the interface GigabitEthernet1/0/3 is DOWN.";
        String regular = ".*is DOWN.";
        System.out.println("aim:\t\t" + s + " \r\nregular:\t" + regular);
        Expression expression = parser.parseExpression(String.format("'%s' matches '%s'", s, regular));
        return expression.getValue(Boolean.class);
    }

    @Test
    public void matchExpression3() {
        String s = "Mar 29 2016 14:35:51+08:00 CT_SC-CD-XX_SR-1.S5328 %%01NTP/4/PACKET_LENGTH_WRONG(l)[71]:The received NTP packet is longer than or shorter than a valid packet. (RcvLen=8)";
        SimpleDateFormat datePrettyFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dataFormatChengdu = new SimpleDateFormat("MMM dd yyyy HH:mm:ssX", Locale.ENGLISH);

        String REGULAR_SPLIT_INTO_FOUR_PARTS = "(?<time>(\\S+ +){4})(?<device>\\S+ )(?<operator>\\S+):(?<body>.*)";
        Pattern patternSplitIntoFourParts = Pattern.compile(REGULAR_SPLIT_INTO_FOUR_PARTS);
        Matcher parts = patternSplitIntoFourParts.matcher(s);
        if (!parts.find()) {
            _log.error("Cannot catch four parts in switch log!");
        }
        _log.info("Group's Count:\t" + parts.groupCount() + "\r\n");

        String timePart = parts.group("time").trim();
        if (StrUtils.isEmpty(timePart)) {
            _log.error("Cannot find date in this switch log.");
        } else {
            try {
                //Mar 29 2016 14:35:51+08:00
                Date time;
                if (timePart.contains("+")) {
                    _log.info(timePart);
                    time = dataFormatChengdu.parse(timePart);
                    _log.info(time.toString());
                    timePart = datePrettyFormat.format(time);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                _log.error("Parsing date\"{}\" had a error:{}", timePart, e.getMessage());
            }
        }
        _log.debug("Time:\t" + timePart);
    }

    @Test
    public void testZone() {
        String aim = "Mar 29 2016 14:35:51+08:00";
        SimpleDateFormat dataFormatChengdu = new SimpleDateFormat("MMM dd yyyy HH:mm:ssX", Locale.ENGLISH);
        try {
            dataFormatChengdu.parse(aim);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(dataFormatChengdu.format(new Date()));
    }
}
