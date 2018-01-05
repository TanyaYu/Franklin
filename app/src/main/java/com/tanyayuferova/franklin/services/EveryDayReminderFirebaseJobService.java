package com.tanyayuferova.franklin.services;

import android.os.AsyncTask;

import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.firebase.jobdispatcher.RetryStrategy;
import com.tanyayuferova.franklin.utils.EveryDayReminderUtils;

/**
 * Created by Tanya Yuferova on 11/3/2017.
 */

public class EveryDayReminderFirebaseJobService extends JobService {
    private static AsyncTask asyncTask;

    @Override
    public boolean onStartJob(final JobParameters job) {
        asyncTask = new AsyncTask() {

            @Override
            protected Object doInBackground(Object[] params) {
                EveryDayReminderUtils.executeEveryDayReminder(EveryDayReminderFirebaseJobService.this);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                /* Inform that the job is finished */
                jobFinished(job, false);
            }
        };

        asyncTask.execute();
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
        if (asyncTask != null) asyncTask.cancel(true);
        return true;
    }

}
