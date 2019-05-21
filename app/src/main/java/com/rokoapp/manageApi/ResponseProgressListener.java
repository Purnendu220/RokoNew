package com.rokoapp.manageApi;


public interface ResponseProgressListener {

    void onResponseInProgress();

    void onResponseCompleted(int i);

    /*String  onResponsePending(int i);*/

    void onResponseFailed(FailureCodes code);
}
