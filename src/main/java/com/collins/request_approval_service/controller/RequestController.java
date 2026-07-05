package com.collins.request_approval_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/requests")
@RequiredArgsConstructor
public class RequestController {

    /*
    * Method	Endpoint	Purpose
        POST	/api/v1/requests	Create request
        GET	    /api/v1/requests	Get all requests
        GET	    /api/v1/requests/{id}	Get request details
        PUT	    /api/v1/requests/{id}	Update request (before approval)
        DELETE	/api/v1/requests/{id}	Delete request (before approval)
        GET	    /api/v1/requests/status/{status}	Filter by status
    * */
}
