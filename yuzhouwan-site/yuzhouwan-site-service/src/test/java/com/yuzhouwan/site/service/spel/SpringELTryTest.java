package com.yuzhouwan.site.service.spel;

import com.yuzhouwan.common.util.StrUtils;
import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yuzhouwan.common.util.StrUtils.NEXT_LINE;
import static org.junit.Assert.assertEquals;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function：MultiIPAddresses Tester
 *
 * @author Benedict Jin
 * @since 2016/3/15
 */
public class SpringELTryTest {

    private static final Logger _log = LoggerFactory.getLogger(SpringELTryTest.class);

    private SpringELTry springELTry;

    @Before
    public void init() {
        springELTry = new SpringELTry();
    }

    @Test
    public void testHello() throws Exception {
        assertEquals("Hello, World!", springELTry.hello());
    }

    @Test
    public void testRegular() throws Exception {
        assertEquals(true, springELTry.matchExpression());
    }

    @Test
    public void testRegular2() throws Exception {
        assertEquals(true, springELTry.matchExpression2());
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
        _log.info("Group's Count:\t" + parts.groupCount() + NEXT_LINE);

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
                _log.error("Parsing date\"{}\" had a error:{}", timePart, e.getMessage());
                throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
        System.out.println(dataFormatChengdu.format(new Date()));
    }
}
