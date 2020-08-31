package com.daicy.crawler.core.plugin;

import java.io.File;
import java.util.Map;

public interface HostInterface {

	File getOutputDirectory();

	Map<String, String> getParameters();
}
