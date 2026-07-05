ALTER TABLE approvals
ADD CONSTRAINT uk_request_approval_level
UNIQUE (request_id, approval_level);