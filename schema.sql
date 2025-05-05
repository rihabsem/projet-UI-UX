CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       name VARCHAR(100),
                       email VARCHAR(100) UNIQUE
);

CREATE TABLE transactions (
                              id SERIAL PRIMARY KEY,
                              user_id UUID REFERENCES users(id),
                              date DATE NOT NULL,
                              amount NUMERIC(10, 2) NOT NULL,
                              note TEXT,
                              main_category VARCHAR(50),
                              sub_category VARCHAR(50),
                              created_at TIMESTAMP DEFAULT NOW(),
                              updated_at TIMESTAMP DEFAULT NOW()
);
