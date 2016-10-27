package com.ibm.microservices.wfd.appetizer;

import com.codahale.metrics.graphite.Graphite; 
import com.codahale.metrics.graphite.GraphiteReporter; 
import com.codahale.metrics.MetricRegistry; 
import com.codahale.metrics.MetricFilter;
import java.net.InetSocketAddress; 
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.context.annotation.Bean;

import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.codahale.metrics.MetricSet;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Configuration
@ConfigurationProperties(prefix = "wfd.monitor")
public class MonitoringConfig {

    @Autowired
    private MetricRegistry registry;

	@Value("${wfd.monitor.graphiteServer}")
	private String graphiteServer;

	@Bean
    public GraphiteReporter graphiteReporter() {
		
		
		MetricSet metricset = new MetricSet() {
			@Override
			public Map<String, com.codahale.metrics.Metric> getMetrics() {
				Map<String, com.codahale.metrics.Metric> metrics = new HashMap<String, com.codahale.metrics.Metric>();
				metrics.put("memory", new MemoryUsageGaugeSet());
				metrics.put("garbage", new GarbageCollectorMetricSet());
				metrics.put("threads", new ThreadStatesGaugeSet());
				
				return metrics;
			}
		};
		
		registry.registerAll(metricset);
				
        Graphite graphite = new Graphite(new InetSocketAddress(graphiteServer, 2003));
		final GraphiteReporter reporter = GraphiteReporter.forRegistry(registry).prefixedWith("wfd-appetizer")
											.convertRatesTo(TimeUnit.SECONDS).convertDurationsTo(TimeUnit.MILLISECONDS).filter(MetricFilter.ALL).build(graphite);
        reporter.start(5, TimeUnit.SECONDS);

        return reporter;
    }
}

