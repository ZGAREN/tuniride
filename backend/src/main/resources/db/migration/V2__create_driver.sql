CREATE TABLE driver (
    id UUID PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    phone VARCHAR(12) NOT NULL UNIQUE
        CHECK (phone ~ '^\+216[0-9]{8}$'),
    taxi_license_number VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL DEFAULT 'OFFLINE'
        CHECK (status IN ('OFFLINE', 'AVAILABLE', 'ON_RIDE')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    version INTEGER NOT NULL DEFAULT 0
);
-- The API filters drivers by status ("who is AVAILABLE?"), so index it.
CREATE INDEX idx_driver_status ON driver (status);
