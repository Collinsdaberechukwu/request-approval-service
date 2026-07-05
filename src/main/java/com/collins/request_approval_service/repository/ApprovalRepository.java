package com.collins.request_approval_service.repository;

import com.collins.request_approval_service.model.Approval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<Approval,Long> {
}
