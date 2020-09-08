package com.daicy.crawler.core.filter;

import com.daicy.crawler.core.Request;
import com.daicy.crawler.core.SpiderContext;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: create by daichangya
 * @version: v1.0
 * @description: com.daicy.crawler.core.filterRequest
 * @date:9/7/20
 */
public class Filters {
    private static final Logger LOGGER = LoggerFactory.getLogger(Filters.class
            .getName());

    @SuppressWarnings("unchecked")
    static final Set<Class<? extends Filter>> KNOWN_FILTERS = ImmutableSet
            .of(BeforeDownloadFilter.class);

    private ListMultimap<Class<? extends Filter>, Filter> filters = ArrayListMultimap.create();

    private Map<Class<? extends Filter>, AtomicInteger> counters = Maps.newConcurrentMap();

    private Map<Class<? extends Filter>, AtomicInteger> failCounts = Maps.newConcurrentMap();

    public Filters() {
        for (Class<? extends Filter> knownFilter : KNOWN_FILTERS) {
            counters.put(knownFilter, new AtomicInteger());
            failCounts.put(knownFilter, new AtomicInteger());
        }
    }

    public void addFilters(
            List<? extends Filter> filters) {
        ArrayList<Filter> unusedFilters = Lists.newArrayList(filters);
        for (Filter filter : filters) {
            counters.put(filter.getClass(), new AtomicInteger());
            failCounts.put(filter.getClass(), new AtomicInteger());
            for (Class<?> clazz : filter.getClass().getInterfaces()) {
                if (KNOWN_FILTERS.contains(clazz)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Filter> filterClass = (Class<? extends Filter>) clazz;
                    this.filters.put(filterClass, filter);
                    LOGGER.info("Loaded {} as a {}", filter,
                            clazz.getSimpleName());
                    unusedFilters.remove(filter);
                }
            }
        }
        if (!unusedFilters.isEmpty()) {
            LOGGER.warn(
                    "These filters were added but are ignored because they are unknown to crawler, {}",
                    unusedFilters);
        }
    }

    private void reportFailingFilter(Filter filter, RuntimeException e) {
        LOGGER.error("Filter {} caused an error while running. {}", filter, e.getMessage(), e);
        incrementFailCounterFor(filter);
    }

    private void incrementFailCounterFor(Filter filter) {
        failCounts.get(filter.getClass()).incrementAndGet();
//        registry.counter(MetricsModule.PLUGINS_PREFIX + filterRequest.getClass().getSimpleName()
//                + ".fail_count").inc();
    }


    public boolean runBeforeDownloadFilters(Request request, SpiderContext spiderContext) {
        LOGGER.debug("Running BeforeDownloadFilter...");
        counters.get(BeforeDownloadFilter.class).incrementAndGet();
        for (Filter filter : filters.get(BeforeDownloadFilter.class)) {
            if (filter instanceof BeforeDownloadFilter) {
                LOGGER.debug("Calling filterRequest {}", filter);
                try {
                    boolean isFilter = ((BeforeDownloadFilter) filter).filterRequest(request, spiderContext);
                    if (isFilter) {
                        LOGGER.warn("runBeforeDownloadFilters filter request:{}", request.getUrl());
                        return isFilter;
                    }
                } catch (RuntimeException e) {
                    reportFailingFilter(filter, e);
                }
            }
        }
        return false;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(filters);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Filters) {
            Filters that = (Filters) object;
            return Objects.equal(this.filters, that.filters);
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("filters", filters).toString();
    }

    /**
     * @return A {@link ImmutableSet} of the {@link Filter} names that are installed.
     */
    public ImmutableSet<String> filterNames() {
        ImmutableSortedSet.Builder<String> names = ImmutableSortedSet
                .naturalOrder();
        for (Filter filter : filters.values()) {
            names.add(filter.toString());
        }
        return names.build();
    }

}
