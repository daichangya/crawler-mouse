package com.daicy.samples.samples.scheduler;

import com.daicy.crawler.core.Request;
import com.daicy.crawler.core.Task;
import com.daicy.crawler.core.scheduler.PriorityScheduler;

/**
 * @author daichangya@163.com
 */
public class LevelLimitScheduler extends PriorityScheduler {

    private int levelLimit = 3;

    public LevelLimitScheduler(int levelLimit) {
        this.levelLimit = levelLimit;
    }

    @Override
    public synchronized void push(Request request, Task task) {
        if (((Integer) request.getExtra("_level")) <= levelLimit) {
            super.push(request, task);
        }
    }
}
