package org.shrigorevich.ml.domain.users;

public enum Job {
    WARRIOR(1), FARMER(2), WIZARD(3);

    private final int jobId;

    Job(int jobId) {
        this.jobId = jobId;
    }

    public int getJobId() {
        return jobId;
    }

    public static Job valueOf(int id) {
        for(Job job : Job.values()) {
            if (job.getJobId() == id) {
                return job;
            }
        }
        return null;
    }
}
