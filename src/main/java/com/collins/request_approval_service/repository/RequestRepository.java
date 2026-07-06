package com.collins.request_approval_service.repository;

import com.collins.request_approval_service.enums.RequestStatus;
import com.collins.request_approval_service.model.Request;
import com.collins.request_approval_service.model.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RequestRepository extends JpaRepository<Request,Long> {

    boolean existsByTitleIgnoreCaseAndRequestedByAndRequestStatus(String trim, User requester, RequestStatus requestStatus);


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        SELECT r
        FROM Request r
        WHERE r.id = :requestId
      """)
    Optional<Request> findByIdForUpdate(@Param("requestId") Long requestId);


    Page<Request> findByRequestStatus(RequestStatus requestStatus, Pageable pageable);
}
