package org.shrigorevich.ml.config;

import java.util.Map;

public class UserConf {
    private int maxJobsQty;

    public UserConf(Map<String, Object> map) {
        this.maxJobsQty = (int) map.getOrDefault("max_jobs_qty", 0);
    }

    public int getMaxJobsQty() { return maxJobsQty; }
    public void setMaxJobsQty(int maxJobsQty) { this.maxJobsQty = maxJobsQty; }
}
