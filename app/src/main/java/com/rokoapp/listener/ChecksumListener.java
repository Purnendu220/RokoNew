package com.rokoapp.listener;

import com.rokoapp.model.response.ChecksumResponse;

public interface ChecksumListener {
    void getChecksumHash(String checksumHash);
    void getTxnAmount(String txnAmount);
    void getCheckSumObject(ChecksumResponse response);
}
