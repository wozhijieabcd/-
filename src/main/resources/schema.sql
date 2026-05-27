-- =============================================
-- 个人博客系统 - 数据库初始化脚本
-- =============================================

CREATE DATABASE IF NOT EXISTS blog DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE blog;

-- 用户表
CREATE TABLE IF NOT EXISTS `user` (
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username`     VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password`     VARCHAR(255) NOT NULL COMMENT '密码(BCrypt)',
    `email`        VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `nickname`     VARCHAR(50)  DEFAULT NULL COMMENT '昵称',
    `avatar`       VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `role`         VARCHAR(20)  NOT NULL DEFAULT 'USER' COMMENT '角色: ADMIN/USER',
    `status`       TINYINT      NOT NULL DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用',
    `created_at`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    UNIQUE KEY `uk_email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 分类表
CREATE TABLE IF NOT EXISTS `category` (
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '分类ID',
    `name`         VARCHAR(50)  NOT NULL COMMENT '分类名称',
    `description`  VARCHAR(200) DEFAULT NULL COMMENT '分类描述',
    `sort_order`   INT          NOT NULL DEFAULT 0 COMMENT '排序',
    `created_at`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='分类表';

-- 文章表
CREATE TABLE IF NOT EXISTS `article` (
    `id`           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '文章ID',
    `title`        VARCHAR(200) NOT NULL COMMENT '标题',
    `content`      LONGTEXT     NOT NULL COMMENT '正文',
    `summary`      VARCHAR(500) DEFAULT NULL COMMENT '摘要',
    `category_id`  BIGINT       DEFAULT NULL COMMENT '分类ID',
    `user_id`      BIGINT       NOT NULL COMMENT '作者ID',
    `status`       TINYINT      NOT NULL DEFAULT 0 COMMENT '状态: 0-草稿, 1-已发布',
    `view_count`   INT          NOT NULL DEFAULT 0 COMMENT '阅读量',
    `is_top`       TINYINT      NOT NULL DEFAULT 0 COMMENT '是否置顶: 0-否, 1-是',
    `created_at`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `idx_category_id` (`category_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status_created` (`status`, `created_at`),
    CONSTRAINT `fk_article_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE SET NULL,
    CONSTRAINT `fk_article_user`     FOREIGN KEY (`user_id`)     REFERENCES `user` (`id`)     ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- 评论表
CREATE TABLE IF NOT EXISTS `comment` (
    `id`           BIGINT    NOT NULL AUTO_INCREMENT COMMENT '评论ID',
    `article_id`   BIGINT    NOT NULL COMMENT '文章ID',
    `user_id`      BIGINT    NOT NULL COMMENT '评论者ID',
    `parent_id`    BIGINT    DEFAULT NULL COMMENT '父评论ID(回复)',
    `content`      TEXT      NOT NULL COMMENT '评论内容',
    `status`       TINYINT   NOT NULL DEFAULT 1 COMMENT '状态: 0-隐藏, 1-显示',
    `created_at`   DATETIME  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    KEY `idx_article_id` (`article_id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_parent_id` (`parent_id`),
    CONSTRAINT `fk_comment_article` FOREIGN KEY (`article_id`) REFERENCES `article` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_comment_user`    FOREIGN KEY (`user_id`)    REFERENCES `user` (`id`)    ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- 初始化一个管理员账号 (密码: admin123, BCrypt加密)
INSERT INTO `user` (`username`, `password`, `nickname`, `role`) VALUES
('admin', '$2a$10$mad09AGT7jZd8mBFtR86quZNVCZ.4fMeFjDhxFhmxizb4RVIYS.cq', '博主', 'ADMIN');
