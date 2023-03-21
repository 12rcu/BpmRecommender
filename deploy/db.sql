DROP TABLE bpm_user_recommendation;
DROP TABLE bpm_item_category_inheritance;
DROP TABLE bpm_item_categories;
DROP TABLE bpm_items;
DROP TABLE bpm_user;


CREATE TABLE bpm_item_categories
(
    `id`          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`        VARCHAR(255) NOT NULL,
    `description` TEXT         NOT NULL,
    `value_range` INT          NOT NULL DEFAULT 1
);

CREATE TABLE bpm_items
(
    `id`          INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `name`        VARCHAR(255) NOT NULL,
    `description` TEXT         NOT NULL
);

CREATE TABLE bpm_item_category_inheritance
(
    `id`          INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `item_id`     INT NOT NULL,
    `category_id` INT NOT NULL,
    `value`       INT NOT NULL DEFAULT 0,

    FOREIGN KEY (`item_id`) REFERENCES bpm_items (id),
    FOREIGN KEY (`category_id`) REFERENCES bpm_item_categories (id)
);

CREATE TABLE bpm_user
(
    `id`        INT                    NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `type`      ENUM ('API', 'GOOGLE') NOT NULL DEFAULT 'API',
    `type_info` TEXT                   NULL     DEFAULT NULL
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