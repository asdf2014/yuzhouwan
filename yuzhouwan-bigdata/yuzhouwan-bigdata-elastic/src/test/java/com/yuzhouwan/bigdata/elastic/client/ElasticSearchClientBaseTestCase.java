package com.yuzhouwan.bigdata.elastic.client;

import com.carrotsearch.randomizedtesting.JUnit3MethodProvider;
import com.carrotsearch.randomizedtesting.MixWithSuiteName;
import com.carrotsearch.randomizedtesting.RandomizedTest;
import com.carrotsearch.randomizedtesting.annotations.*;

/**
 * Copyright @ 2020 yuzhouwan.com
 * All right reserved.
 * Function：ElasticSearch Client Base Test Case
 *
 * @author Benedict Jin
 * @since 2017/12/08
 */
@TestMethodProviders({JUnit3MethodProvider.class})
@SeedDecorators({MixWithSuiteName.class}) // See https://issues.apache.org/jira/browse/LUCENE-3995
@ThreadLeakScope(ThreadLeakScope.Scope.SUITE)
@ThreadLeakGroup(ThreadLeakGroup.Group.MAIN)
@ThreadLeakAction({ThreadLeakAction.Action.WARN, ThreadLeakAction.Action.INTERRUPT})
@ThreadLeakZombies(ThreadLeakZombies.Consequence.IGNORE_REMAINING_TESTS)
@ThreadLeakLingering(linger = 5000) // Time in 5000 millis to "linger" for any left-behind threads
@TimeoutSuite(millis = 2 * 60 * 60 * 1000)
public abstract class ElasticSearchClientBaseTestCase extends RandomizedTest {
}
