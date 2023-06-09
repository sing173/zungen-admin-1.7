CREATE TABLE IF NOT EXISTS "bpm_user_group" (
    "id" bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    "name" varchar(63) NOT NULL,
    "description" varchar(255) NOT NULL,
    "status" tinyint NOT NULL,
    "member_user_ids" varchar(255) NOT NULL,
    "creator" varchar(64) DEFAULT '',
    "create_time" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updater" varchar(64) DEFAULT '',
    "update_time" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "deleted" bit NOT NULL DEFAULT FALSE,
    PRIMARY KEY ("id")
) COMMENT '用户组';

CREATE TABLE IF NOT EXISTS "bpm_form" (
    "id" bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    "name" varchar(63) NOT NULL,
    "status" tinyint NOT NULL,
    "fields" varchar(255) NOT NULL,
    "conf" varchar(255) NOT NULL,
    "remark" varchar(255),
    "creator" varchar(64) DEFAULT '',
    "create_time" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "updater" varchar(64) DEFAULT '',
    "update_time" timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    "deleted" bit NOT NULL DEFAULT FALSE,
    PRIMARY KEY ("id")
) COMMENT '动态表单';

CREATE TABLE IF NOT EXISTS "bpm_loan_user" (
                                               "id" bigint NOT NULL GENERATED BY DEFAULT AS IDENTITY,
                                               "user_id" bigint,
                                               "name" varchar NOT NULL,
                                               "mobile" varchar NOT NULL,
                                               "address" varchar,
                                               "work" varchar,
                                               "work_phone" varchar,
                                               "work_address" varchar,
                                               "email" varchar,
                                               "status" varchar NOT NULL,
                                               "identity_id" bigint,
                                               "contact_id" bigint,
                                               "creator" varchar DEFAULT '',
                                               "create_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                               "updater" varchar DEFAULT '',
                                               "update_time" datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                               "deleted" bit NOT NULL DEFAULT FALSE,
                                               "male" varchar,
                                               "age" int,
                                               PRIMARY KEY ("id")
    ) COMMENT '贷款人信息表';
