CREATE INDEX idx_user_email
ON users(email);

CREATE INDEX idx_request_requested_by
ON requests(requested_by);

CREATE INDEX idx_request_status
ON requests(request_status);

CREATE INDEX idx_request_current_level
ON requests(current_approval_level);

CREATE INDEX idx_approval_request
ON approvals(request_id);

CREATE INDEX idx_approval_user
ON approvals(approved_by);

CREATE INDEX idx_approval_level
ON approvals(approval_level);

CREATE INDEX idx_approval_decision
ON approvals(decision);