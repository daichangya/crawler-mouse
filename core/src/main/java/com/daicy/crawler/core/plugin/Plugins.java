package com.daicy.crawler.core.plugin;

import com.daicy.crawler.core.Page;
import com.daicy.crawler.core.Request;
import com.daicy.crawler.core.Spider;
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
 * Class for invoking plugins. The methods in this class are invoked from the crawler Core.
 */
public class Plugins {

    private static final Logger LOGGER = LoggerFactory.getLogger(Plugins.class
            .getName());

    @SuppressWarnings("unchecked")
    static final Set<Class<? extends Plugin>> KNOWN_PLUGINS = ImmutableSet
            .of(AfterCrawlingPlugin.class,
                    BeforeCrawlingPlugin.class,
                    AfterDownloadPlugin.class, BeforeDownloadPlugin.class,
                    AfterProcessPlugin.class);

    private ListMultimap<Class<? extends Plugin>, Plugin> plugins = ArrayListMultimap.create();

    private Map<Class<? extends Plugin>, AtomicInteger> counters = Maps.newConcurrentMap();

    private Map<Class<? extends Plugin>, AtomicInteger> failCounts = Maps.newConcurrentMap();

    public Plugins() {
        for (Class<? extends Plugin> knownPlugin : KNOWN_PLUGINS) {
            counters.put(knownPlugin, new AtomicInteger());
            failCounts.put(knownPlugin, new AtomicInteger());
        }
    }

    public void addPlugins(
            List<? extends Plugin> plugins) {
        ArrayList<Plugin> unusedPlugins = Lists.newArrayList(plugins);
        for (Plugin plugin : plugins) {
            counters.put(plugin.getClass(), new AtomicInteger());
            failCounts.put(plugin.getClass(), new AtomicInteger());
            for (Class<?> clazz : plugin.getClass().getInterfaces()) {
                if (KNOWN_PLUGINS.contains(clazz)) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Plugin> pluginClass = (Class<? extends Plugin>) clazz;
                    this.plugins.put(pluginClass, plugin);
                    LOGGER.info("Loaded {} as a {}", plugin,
                            clazz.getSimpleName());
                    unusedPlugins.remove(plugin);
                }
            }
        }
        if (!unusedPlugins.isEmpty()) {
            LOGGER.warn(
                    "These plugins were added but are ignored because they are unknown to crawler, {}",
                    unusedPlugins);
        }
    }

    private void reportFailingPlugin(Plugin plugin, RuntimeException e) {
        LOGGER.error("Plugin {} caused an error while running. {}", plugin, e.getMessage(), e);
        incrementFailCounterFor(plugin);
    }

    private void incrementFailCounterFor(Plugin plugin) {
        failCounts.get(plugin.getClass()).incrementAndGet();
//        registry.counter(MetricsModule.PLUGINS_PREFIX + plugin.getClass().getSimpleName()
//                + ".fail_count").inc();
    }


    /**
     * @param spider The given {@link Spider}.
     */
    public void runAfterCrawlingPlugins(Spider spider) {
        LOGGER.debug("Running AfterCrawlingPlugin...");
        counters.get(AfterCrawlingPlugin.class).incrementAndGet();
        for (Plugin plugin : plugins.get(AfterCrawlingPlugin.class)) {
            if (plugin instanceof AfterCrawlingPlugin) {
                try {
                    LOGGER.debug("Calling plugin {}", plugin);
                    ((AfterCrawlingPlugin) plugin).handle(spider);
                } catch (RuntimeException e) {
                    reportFailingPlugin(plugin, e);
                }
            }
        }
    }


    /**
     * @param spider The given {@link Spider}.
     */
    public void runBeforeCrawlingPlugins(Spider spider) {
        LOGGER.debug("Running BeforeCrawlingPlugin...");
        counters.get(BeforeCrawlingPlugin.class).incrementAndGet();
        for (Plugin plugin : plugins.get(BeforeCrawlingPlugin.class)) {
            if (plugin instanceof BeforeCrawlingPlugin) {
                LOGGER.debug("Calling plugin {}", plugin);
                try {
                    ((BeforeCrawlingPlugin) plugin).handle(spider);
                } catch (RuntimeException e) {
                    reportFailingPlugin(plugin, e);
                }
            }
        }
    }

    /**
     * @param request The given {@link Request}.
     */
    public void runBeforeDownloadPlugins(Request request) {
        LOGGER.debug("Running BeforeDownloadPlugin...");
        counters.get(BeforeDownloadPlugin.class).incrementAndGet();
        for (Plugin plugin : plugins.get(BeforeDownloadPlugin.class)) {
            if (plugin instanceof BeforeDownloadPlugin) {
                LOGGER.debug("Calling plugin {}", plugin);
                try {
                    ((BeforeDownloadPlugin) plugin).handle(request);
                } catch (RuntimeException e) {
                    reportFailingPlugin(plugin, e);
                }
            }
        }
    }

    public void runAfterDownloadPlugins(Request request, Page page) {
        LOGGER.debug("Running AfterDownloadPlugin...");
        counters.get(AfterDownloadPlugin.class).incrementAndGet();
        for (Plugin plugin : plugins.get(AfterDownloadPlugin.class)) {
            if (plugin instanceof AfterDownloadPlugin) {
                LOGGER.debug("Calling plugin {}", plugin);
                try {
                    ((AfterDownloadPlugin) plugin).handle(request, page);
                } catch (RuntimeException e) {
                    reportFailingPlugin(plugin, e);
                }
            }
        }
    }

    public void runAfterProcessSuccess(Request request, Page page) {
        LOGGER.debug("Running AfterProcessPlugin...");
        counters.get(AfterProcessPlugin.class).incrementAndGet();
        for (Plugin plugin : plugins.get(AfterProcessPlugin.class)) {
            if (plugin instanceof AfterProcessPlugin) {
                LOGGER.debug("Calling plugin {}", plugin);
                try {
                    ((AfterProcessPlugin) plugin).handle(request, page);
                } catch (RuntimeException e) {
                    reportFailingPlugin(plugin, e);
                }
            }
        }
    }

    public void runAfterProcessError(Request request) {
        LOGGER.debug("Running AfterProcessPlugin Error...");
        counters.get(AfterProcessPlugin.class).incrementAndGet();
        for (Plugin plugin : plugins.get(AfterProcessPlugin.class)) {
            if (plugin instanceof AfterProcessPlugin) {
                LOGGER.debug("Calling plugin {}", plugin);
                try {
                    ((AfterProcessPlugin) plugin).handleException(request);
                } catch (RuntimeException e) {
                    reportFailingPlugin(plugin, e);
                }
            }
        }
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(plugins);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Plugins) {
            Plugins that = (Plugins) object;
            return Objects.equal(this.plugins, that.plugins);
        }
        return false;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("plugins", plugins).toString();
    }

    /**
     * @return A {@link ImmutableSet} of the {@link Plugin} names that are installed.
     */
    public ImmutableSet<String> pluginNames() {
        ImmutableSortedSet.Builder<String> names = ImmutableSortedSet
                .naturalOrder();
        for (Plugin plugin : plugins.values()) {
            names.add(plugin.toString());
        }
        return names.build();
    }

}
