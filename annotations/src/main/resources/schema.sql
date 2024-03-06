CREATE
    TABLE
        IF NOT EXISTS friends(
            id uuid NOT NULL,
            first_name VARCHAR NOT NULL,
            last_name VARCHAR NOT NULL,
            email VARCHAR,
            phone_number VARCHAR,
            birthday DATE,
            PRIMARY KEY(id)
        );
