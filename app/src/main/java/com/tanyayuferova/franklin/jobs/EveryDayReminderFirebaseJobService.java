package com.tanyayuferova.franklin.jobs;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;

/**
 * Created by Tanya Yuferova on 11/3/2017.
 */

public class EveryDayReminderFirebaseJobService extends JobService {
    private static AsyncTask mBackgroundTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        mBackgroundTask = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                EveryDayReminderTasks.executeTask(EveryDayReminderFirebaseJobService.this, EveryDayReminderTasks.ACTION_EVERY_DAY_REMINDER);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                /* Inform that the job is finished */
                jobFinished(job, false);
            }
        };

        mBackgroundTask.execute();
        return true;
    }

    /**
     * Called when the scheduling engine has decided to interrupt the execution of a running job,
     * most likely because the runtime constraints associated with the job are no longer satisfied.
     *
     * @return whether the job should be retried
     * @see Job.Builder#setRetryStrategy(RetryStrategy)
     * @see RetryStrategy
     */
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        if (mBackgroundTask != null) mBackgroundTask.cancel(true);
        return true;
    }

}
