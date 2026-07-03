CREATE TABLE IF NOT EXISTS logbook_entries (
    id UUID PRIMARY KEY,
    title VARCHAR(120) NOT NULL,
    description VARCHAR(4000),
    start_time TIMESTAMPTZ NOT NULL,
    end_time TIMESTAMPTZ NOT NULL,

    start_city VARCHAR(255) NOT NULL,
    start_station_id VARCHAR(255),

    destination_city VARCHAR(255) NOT NULL,
    destination_station_id VARCHAR(255),

    transport_mode VARCHAR(20) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_logbook_entries_start_time ON logbook_entries (start_time);
CREATE INDEX IF NOT EXISTS idx_logbook_entries_end_time ON logbook_entries (end_time);
CREATE INDEX IF NOT EXISTS idx_logbook_entries_transport_mode ON logbook_entries (transport_mode);
