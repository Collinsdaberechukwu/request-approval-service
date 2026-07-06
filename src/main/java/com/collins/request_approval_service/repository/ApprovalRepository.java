package com.collins.request_approval_service.repository;

import com.collins.request_approval_service.enums.ApprovalLevel;
import com.collins.request_approval_service.model.Approval;
import com.collins.request_approval_service.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval,Long> {
    boolean existsByRequestAndApprovalLevel(Request request, ApprovalLevel currentApprovalLevel);
}
