DROP TABLE bpm_user_recommendation;
DROP TABLE bpm_items;
DROP TABLE bpm_user;

CREATE TABLE bpm_items
(
    `id`          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`        VARCHAR(255) NOT NULL,
    `description` TEXT         NOT NULL
);

CREATE TABLE bpm_user
(
    `id`   INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(255) NOT NULL,
    `info` TEXT NULL DEFAULT NULL
);

CREATE TABLE bpm_user_recommendation
(
    `id`      INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `userid`  INT NOT NULL,
    `item_id` INT NOT NULL,
    `rating`  INT NOT NULL DEFAULT 0,

    FOREIGN KEY (`userid`) REFERENCES bpm_user (id),
    FOREIGN KEY (`item_id`) REFERENCES bpm_items (id)
)