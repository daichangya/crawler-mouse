package com.daicy.crawler.core.filter;

import com.daicy.crawler.core.Request;
import com.daicy.crawler.core.SpiderContext;

import static com.daicy.crawler.core.Request.DEL_KEY_WORD;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.core.filterRequest
 * @date:9/7/20
 */
public class FilterKeyWordRequest implements BeforeDownloadFilter {
    @Override
    public boolean filterRequest(Request request, SpiderContext spiderContext) {
        if (null != request.getExtra(DEL_KEY_WORD) &&
                spiderContext.getDelRequestKeyWords()
                        .contains(request.getExtra(DEL_KEY_WORD))) {
            return true;
        }
        return false;
    }
}
