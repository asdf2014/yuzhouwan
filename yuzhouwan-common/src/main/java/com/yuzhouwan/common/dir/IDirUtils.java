package com.yuzhouwan.common.dir;

import java.nio.file.WatchEvent;

/**
 * Copyright @ 2018 yuzhouwan.com
 * All right reserved.
 * Function: DirUtils' interface
 *
 * @author Benedict Jin
 * @since 2016/7/14
 */
public interface IDirUtils {

    /**
     * 监控到文件夹事件，进行处理的流程.
     *
     * @param event watchService 监控到的 event
     */
    void dealWithEvent(WatchEvent<?> event);
}
