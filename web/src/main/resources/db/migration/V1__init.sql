CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    profile_image_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE controllers (
     id SERIAL PRIMARY KEY,
     controller_sn VARCHAR(100),
     ip_address VARCHAR(100),
     port INTEGER,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE doors (
    id SERIAL PRIMARY KEY,
    door_name VARCHAR(255),
    door_number INTEGER,
    controller_id INTEGER REFERENCES controllers(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cards (
    id SERIAL PRIMARY KEY,
    card_no VARCHAR(50),
    user_id INTEGER REFERENCES users(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE access_permissions (
    id SERIAL PRIMARY KEY,
    door_id INTEGER REFERENCES doors(id),
    card_id INTEGER REFERENCES cards(id),
    is_allowed BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE access_logs (
    id SERIAL PRIMARY KEY,
    card_id INTEGER REFERENCES cards(id),
    door_id INTEGER REFERENCES doors(id),
    access_result BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
