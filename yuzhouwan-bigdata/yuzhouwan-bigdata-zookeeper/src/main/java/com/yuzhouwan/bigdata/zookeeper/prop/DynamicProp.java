package com.yuzhouwan.bigdata.zookeeper.prop;

import com.yuzhouwan.common.dir.DirUtils;
import com.yuzhouwan.common.dir.IDirUtils;
import com.yuzhouwan.common.dir.WatchRunnable;
import com.yuzhouwan.common.util.PropUtils;
import com.yuzhouwan.common.util.StrUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.WatchEvent;

/**
 * Copyright @ 2019 yuzhouwan.com
 * All right reserved.
 * Function: Dynamic Prop
 *
 * @author Benedict Jin
 * @since 2016/7/14
 */
public class DynamicProp implements IDirUtils {

    private static final Logger _log = LoggerFactory.getLogger(DynamicProp.class);

    //0: nothing; 1: upload
    private static final String DYNAMIC_PROP_UPLOAD = "1";

    private WatchRunnable runnable;
    private Thread thread;

    public DynamicProp(String path) {
        init(path, null);
    }

    public DynamicProp(String path, Long waitTime) {
        init(path, waitTime);
    }

    private void init(String path, Long waitTime) {
        try {
            runnable = DirUtils.buildWatchService(path, this, waitTime);
            thread = new Thread(runnable);
        } catch (Exception e) {
            _log.error("Dynamic Properties initialization failed!", e);
            throw new RuntimeException(e);
        }
    }

    public void startWatch() {
        thread.start();
    }

    public void stopWatch(long timeout) {
        runnable.setRunning(false);
        try {
            thread.join(timeout);
        } catch (InterruptedException e) {
            _log.error("Dynamic Properties safely stopping failed!", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 监控文件夹，读取变更(like, Create + Modify + Delete) 的配置文件，并保存 k-v信息为 JSON格式.
     *
     * @return <propFile's name>|<k-v JSON>
     */
    public String readProp2JSON() {
        return "";
    }

    /**
     * 上传 JSON到 指定的ZK 路径下.
     *
     * @return isSuccess
     */
    public boolean uploadProp2ZK() {
        return true;
    }

    @Override
    public void dealWithEvent(WatchEvent<?> event) {

        _log.debug(event.context() + ":\t " + event.kind() + " event.");

        String dynamicPolicy = PropUtils.getInstance().getProperty("dynamic.prop.changed");
        _log.debug("DynamicPolicy is {}", dynamicPolicy);
        if (!StrUtils.isEmpty(dynamicPolicy) && DYNAMIC_PROP_UPLOAD.equals(dynamicPolicy)) {
            uploadProp2ZK();
        }
    }
}
