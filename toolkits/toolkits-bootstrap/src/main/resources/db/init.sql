-- Project table
CREATE TABLE IF NOT EXISTS `project` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(200) NOT NULL COMMENT '项目名称',
    `description` TEXT COMMENT '项目描述',
    `start_date` DATE COMMENT '开始日期',
    `end_date` DATE COMMENT '结束日期',
    `status` TINYINT NOT NULL DEFAULT 0 COMMENT '状态: 0-草稿, 1-进行中, 2-已完成',
    `created_at` BIGINT NOT NULL COMMENT '创建时间戳(ms)',
    `updated_at` BIGINT NOT NULL COMMENT '更新时间戳(ms)',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目表';

-- Feature breakdown (WBS) table
CREATE TABLE IF NOT EXISTS `feature_breakdown` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `project_id` BIGINT NOT NULL COMMENT '所属项目ID',
    `parent_id` BIGINT DEFAULT NULL COMMENT '父级功能ID, NULL表示顶级',
    `name` VARCHAR(200) NOT NULL COMMENT '功能名称',
    `description` TEXT COMMENT '功能描述',
    `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序序号',
    `created_at` BIGINT NOT NULL COMMENT '创建时间戳(ms)',
    `updated_at` BIGINT NOT NULL COMMENT '更新时间戳(ms)',
    PRIMARY KEY (`id`),
    KEY `idx_project_id` (`project_id`),
    KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='功能拆解表(WBS)';

-- Budget item table
CREATE TABLE IF NOT EXISTS `budget_item` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `feature_id` BIGINT NOT NULL COMMENT '所属功能ID',
    `name` VARCHAR(200) NOT NULL COMMENT '预算项名称',
    `unit` VARCHAR(50) COMMENT '单位(人天/个/项等)',
    `unit_cost` DECIMAL(12,2) NOT NULL DEFAULT 0.00 COMMENT '单价',
    `quantity` DECIMAL(10,2) NOT NULL DEFAULT 1.00 COMMENT '数量',
    `created_at` BIGINT NOT NULL COMMENT '创建时间戳(ms)',
    `updated_at` BIGINT NOT NULL COMMENT '更新时间戳(ms)',
    PRIMARY KEY (`id`),
    KEY `idx_feature_id` (`feature_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预算项表';

-- Development role table
CREATE TABLE IF NOT EXISTS `development_role` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL COMMENT '角色名称',
    `description` VARCHAR(500) COMMENT '角色描述',
    `created_at` BIGINT NOT NULL COMMENT '创建时间戳(ms)',
    `updated_at` BIGINT NOT NULL COMMENT '更新时间戳(ms)',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='开发角色表';

-- Personnel table
CREATE TABLE IF NOT EXISTS `personnel` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL COMMENT '姓名',
    `email` VARCHAR(200) COMMENT '邮箱',
    `phone` VARCHAR(20) COMMENT '电话',
    `hire_date` DATE COMMENT '入职日期',
    `role_id` BIGINT COMMENT '开发角色ID',
    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态: 0-离职, 1-在职',
    `created_at` BIGINT NOT NULL COMMENT '创建时间戳(ms)',
    `updated_at` BIGINT NOT NULL COMMENT '更新时间戳(ms)',
    PRIMARY KEY (`id`),
    KEY `idx_role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员表';

-- Salary record table
CREATE TABLE IF NOT EXISTS `salary_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `personnel_id` BIGINT NOT NULL COMMENT '人员ID',
    `amount` DECIMAL(12,2) NOT NULL COMMENT '月薪金额',
    `effective_date` DATE NOT NULL COMMENT '生效日期',
    `created_at` BIGINT NOT NULL COMMENT '创建时间戳(ms)',
    PRIMARY KEY (`id`),
    KEY `idx_personnel_id` (`personnel_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='薪资记录表';

-- Personnel project association table
CREATE TABLE IF NOT EXISTS `personnel_project` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `personnel_id` BIGINT NOT NULL COMMENT '人员ID',
    `project_id` BIGINT NOT NULL COMMENT '项目ID',
    `role_in_project` VARCHAR(100) COMMENT '在项目中的角色',
    `created_at` BIGINT NOT NULL COMMENT '创建时间戳(ms)',
    PRIMARY KEY (`id`),
    KEY `idx_personnel_id` (`personnel_id`),
    KEY `idx_project_id` (`project_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员项目关联表';

-- Seed development roles
INSERT INTO `development_role` (`name`, `description`, `created_at`, `updated_at`) VALUES
('前端开发工程师', '负责前端页面开发和交互实现', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
('后端开发工程师', '负责后端服务和API开发', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
('测试工程师', '负责软件测试和质量保障', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
('产品经理', '负责产品需求分析和规划', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000),
('项目经理', '负责项目进度和资源管理', UNIX_TIMESTAMP() * 1000, UNIX_TIMESTAMP() * 1000);
