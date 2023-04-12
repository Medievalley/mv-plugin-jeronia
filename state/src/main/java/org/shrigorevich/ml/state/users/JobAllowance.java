package org.shrigorevich.ml.state.users;

import org.shrigorevich.ml.domain.users.Job;
import org.shrigorevich.ml.domain.users.User;
import org.shrigorevich.ml.domain.users.UserJob;

public class JobAllowance implements Allowance{
    private Job jobRequired;
    private int levelRequired;

    public JobAllowance(Job jobRequired, int levelRequired)
    {
        this.jobRequired = jobRequired;
        this.levelRequired = levelRequired;
    }
    public JobAllowance(){}
    @Override
    public boolean isMathed(User user) {
        for (UserJob job: user.getJobsInfo()) {
            if(job.getJob() == jobRequired
                && job.getLevel() == levelRequired)
                return true;
        }
        return false;
    }

    public void setJobId(int jobId){
        this.jobRequired = Job.valueOf(jobId);
    }

    public void setLevel(int level){
        this.levelRequired = level;
    }

    public Job getJob(){
        return jobRequired;
    }

    public int getLevel() {
        return levelRequired;
    }
}
