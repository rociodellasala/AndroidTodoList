package com.example.pam_project.utils.schedulers;

import io.reactivex.Scheduler;

public interface SchedulerProvider {

    Scheduler io();

    Scheduler computation();

    Scheduler ui();
}
