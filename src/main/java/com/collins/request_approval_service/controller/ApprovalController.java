package com.collins.request_approval_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    /*
    * Method	Endpoint
        POST	/api/v1/approvals
        GET	    /api/v1/approvals/request/{requestId}
        GET 	/api/v1/approvals/pending
    * */
}
