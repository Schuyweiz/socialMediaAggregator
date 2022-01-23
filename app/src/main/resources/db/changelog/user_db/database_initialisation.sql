--kpike: added app_user table
CREATE TABLE IF NOT EXISTS app_user(
                           id BIGINT NOT NULL,
                           first_name VARCHAR(255) NOT NULL,
                           second_name VARCHAR(255) NOT NULL,
                           role VARCHAR(255) NOT NULL,
                           mail VARCHAR(255) NOT NULL,
                           pswdhash VARCHAR(255) NOT NULL,
                           username VARCHAR(255) NOT NULL
);
ALTER TABLE
    "app_user" ADD PRIMARY KEY(id);