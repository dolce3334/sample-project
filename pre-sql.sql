#需要预执行的sql


CREATE TABLE `rc_sys_request_log` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `request_uuid` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `module` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `request_uri` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `method` varchar(11) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `request_parmas` text COLLATE utf8mb4_unicode_ci,
  `response` longtext COLLATE utf8mb4_unicode_ci,
  `stack_trace` text COLLATE utf8mb4_unicode_ci,
  `response_time` bigint(20) DEFAULT NULL,
  `customer_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `device_uuid` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '设备号',
  `error_code` int(11) DEFAULT NULL COMMENT '错误码',
  `request_ip` varchar(64) COLLATE utf8mb4_unicode_ci DEFAULT '' COMMENT '请求方法',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_lsrl_request_uuid` (`request_uuid`) USING BTREE,
  KEY `idx_rsrl_customer_id` (`customer_id`) USING BTREE,
  KEY `idx_rsrl_device_uuid` (`device_uuid`) USING BTREE,
  KEY `idx_rsrl_created_at` (`created_at`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=72789 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci ROW_FORMAT=DYNAMIC;