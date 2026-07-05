CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,

    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,

    role VARCHAR(30) NOT NULL,
    department VARCHAR(30) NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    version BIGINT NOT NULL DEFAULT 0
);

CREATE TABLE requests (
    id BIGSERIAL PRIMARY KEY,

    title VARCHAR(150) NOT NULL,
    description TEXT NOT NULL,

    request_status VARCHAR(30) NOT NULL,
    current_approval_level VARCHAR(30) NOT NULL,

    requested_by BIGINT NOT NULL,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_request_user
        FOREIGN KEY (requested_by)
        REFERENCES users(id)
);

CREATE TABLE approvals (
    id BIGSERIAL PRIMARY KEY,

    request_id BIGINT NOT NULL,
    approved_by BIGINT NOT NULL,

    approval_level VARCHAR(30) NOT NULL,
    decision VARCHAR(30) NOT NULL,

    comment TEXT,

    approved_at TIMESTAMP NOT NULL,

    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,

    version BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_approval_request
        FOREIGN KEY (request_id)
        REFERENCES requests(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_approval_user
        FOREIGN KEY (approved_by)
        REFERENCES users(id)
);