package com.example.TestCaseManagementSystem.apiresponses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StandardApiResponse<T> {
    private int totalCount;
    private int respCode;
    private String respMsg;
    private T respBody;

    public StandardApiResponse(int totalCount, int respCode, String respMsg, T respBody) {
        this.totalCount = totalCount;
        this.respCode = respCode;
        this.respMsg = respMsg;
        this.respBody = respBody;
    }
}
