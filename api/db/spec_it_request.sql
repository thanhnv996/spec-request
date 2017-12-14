-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th12 14, 2017 lúc 03:28 AM
-- Phiên bản máy phục vụ: 10.1.25-MariaDB
-- Phiên bản PHP: 5.6.31

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";
DROP DATABASE IF EXISTS spec_it_request;

CREATE DATABASE spec_it_request; 

USE `spec_it_request`;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `spec_it_request`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `employees`
--

CREATE TABLE `employees` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `url_image` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rolecode` varchar(50) NOT NULL,
  `partcode` varchar(50) NOT NULL,
  `team_id` int(10) UNSIGNED NOT NULL,
  `remember_token` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `part`
--

CREATE TABLE `part_it` (
  `partcode` varchar(50) NOT NULL,
  `partdesc` varchar(254) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `part`
--

INSERT INTO `part_it` (`partcode`, `partdesc`) VALUES
('DANANG', 'Đà nẵng'),
('HANOI', 'Hà nội');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `password_resets`
--

CREATE TABLE `password_resets` (
  `email` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `permission`
--

CREATE TABLE `permission` (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `permission` varchar(50) NOT NULL,
  `rolecode` varchar(50) NOT NULL,
  `partcode` varchar(50) NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `permission`
--

INSERT INTO `permission` (`permission`, `rolecode`, `partcode`) VALUES
('pms_get_request_me', 'member', 'HANOI'),
('pms_get_request_me', 'member', 'DANANG'),
('pms_get_request_team', 'sublead', 'HANOI'),
('pms_post_request', 'leader', 'HANOI'),
('pms_post_request', 'leader', 'HANOI'),
('pms_get_request_team', 'leader', 'HANOI'),
('pms_put_request_team', 'leader', 'HANOI'),
('pms_get_request_part', 'leader', 'HANOI'),
('pms_get_request_team', 'sublead', 'DANANG'),
('pms_post_request_team', 'sublead', 'DANANG'),
('pms_put_request_team', 'sublead', 'DANANG'),
('pms_get_request_part', 'leader', 'DANANG'),
('pms_put_request_part', 'leader', 'DANANG'),
('pms_post_request_part', 'leader', 'DANANG'),
('pms_close_request', 'leader', 'DANANG'),
('pms_get_request_team', 'leader', 'DANANG');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `role`
--

CREATE TABLE `role` (
  `rolecode` varchar(50) NOT NULL,
  `roledesc` varchar(254) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Đang đổ dữ liệu cho bảng `role`
--

INSERT INTO `role` (`rolecode`, `roledesc`) VALUES
('leader', 'Leader'),
('member', 'Member'),
('sublead', 'Sublead');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `teams`
--

CREATE TABLE `teams` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tickets`
--

CREATE TABLE `tickets` (
  `id` int(10) UNSIGNED NOT NULL,
  `subject` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_by` int(10) UNSIGNED NOT NULL,
  `status` tinyint(4) DEFAULT NULL,
  `priority` tinyint(4) NOT NULL,
  `deadline` datetime NOT NULL,
  `assigned_to` int(10) UNSIGNED NOT NULL,
  `partcode` varchar(50) NOT NULL,
  `rating` tinyint(4) DEFAULT NULL,
  `team_id` int(10) UNSIGNED NOT NULL,
  `resolved_at` datetime DEFAULT NULL,
  `closed_at` datetime DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ticket_attributes`
--

CREATE TABLE `ticket_attributes` (
  `id` int(10) UNSIGNED NOT NULL,
  `ticket_id` int(10) UNSIGNED NOT NULL,
  `status` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `priority` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `rating` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `reopened` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ticket_images`
--

CREATE TABLE `ticket_images` (
  `ticket_id` int(10) UNSIGNED NOT NULL,
  `url_image` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ticket_reads`
--

CREATE TABLE `ticket_reads` (
  `ticket_id` int(10) UNSIGNED NOT NULL,
  `reader_id` int(10) UNSIGNED NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ticket_relaters`
--

CREATE TABLE `ticket_relaters` (
  `ticket_id` int(10) UNSIGNED NOT NULL,
  `employee_id` int(10) UNSIGNED NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ticket_thread`
--

CREATE TABLE `ticket_thread` (
  `id` int(10) UNSIGNED NOT NULL,
  `ticket_id` int(10) UNSIGNED NOT NULL,
  `employee_id` int(10) UNSIGNED NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `note` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT NULL,
  `updated_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `employees`
--
ALTER TABLE `employees`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD UNIQUE KEY `employees_email_unique` (`email`) USING BTREE,
  ADD KEY `USER_ROLE` (`rolecode`),
  ADD KEY `USER_PART` (`partcode`);

--
-- Chỉ mục cho bảng `part`
--
ALTER TABLE `part_it`
  ADD PRIMARY KEY (`partcode`);

--
-- Chỉ mục cho bảng `password_resets`
--
ALTER TABLE `password_resets`
  ADD KEY `password_resets_email_index` (`email`) USING BTREE;

--
-- Chỉ mục cho bảng `permission`
--
ALTER TABLE `permission`
  ADD KEY `rolecode` (`rolecode`),
  ADD KEY `partcode` (`partcode`);

--
-- Chỉ mục cho bảng `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`rolecode`);

--
-- Chỉ mục cho bảng `teams`
--
ALTER TABLE `teams`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD UNIQUE KEY `teams_name_unique` (`name`) USING BTREE;

--
-- Chỉ mục cho bảng `tickets`
--
ALTER TABLE `tickets`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD KEY `tickets_created_by_foreign` (`created_by`) USING BTREE,
  ADD KEY `tickets_assigned_to_foreign` (`assigned_to`) USING BTREE,
  ADD KEY `tickets_team_id_foreign` (`team_id`) USING BTREE;

--
-- Chỉ mục cho bảng `ticket_attributes`
--
ALTER TABLE `ticket_attributes`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD KEY `ticket_attributes_ticket_id_foreign` (`ticket_id`) USING BTREE;

--
-- Chỉ mục cho bảng `ticket_reads`
--
ALTER TABLE `ticket_reads`
  ADD PRIMARY KEY (`ticket_id`,`reader_id`) USING BTREE,
  ADD KEY `ticket_reads_reader_id_foreign` (`reader_id`) USING BTREE;

--
-- Chỉ mục cho bảng `ticket_relaters`
--
ALTER TABLE `ticket_relaters`
  ADD PRIMARY KEY (`ticket_id`,`employee_id`) USING BTREE,
  ADD KEY `ticket_relaters_employee_id_foreign` (`employee_id`) USING BTREE;

--
-- Chỉ mục cho bảng `ticket_thread`
--
ALTER TABLE `ticket_thread`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD KEY `ticket_thread_ticket_id_foreign` (`ticket_id`) USING BTREE,
  ADD KEY `ticket_thread_employee_id_foreign` (`employee_id`) USING BTREE;

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `employees`
--
ALTER TABLE `employees`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT cho bảng `teams`
--
ALTER TABLE `teams`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT cho bảng `tickets`
--
ALTER TABLE `tickets`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT cho bảng `ticket_attributes`
--
ALTER TABLE `ticket_attributes`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT cho bảng `ticket_thread`
--
ALTER TABLE `ticket_thread`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `employees`
--
ALTER TABLE `employees`
  ADD CONSTRAINT `employee_part` FOREIGN KEY (`partcode`) REFERENCES `part_it` (`partcode`) ON DELETE CASCADE,
  ADD CONSTRAINT `employee_team` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `employee_role` FOREIGN KEY (`rolecode`) REFERENCES `role` (`rolecode`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `permission`
--
ALTER TABLE `permission`
  ADD CONSTRAINT `permission_part` FOREIGN KEY (`partcode`) REFERENCES `part_it` (`partcode`) ON DELETE CASCADE,
  ADD CONSTRAINT `permission_role` FOREIGN KEY (`rolecode`) REFERENCES `role` (`rolecode`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `tickets`
--
ALTER TABLE `tickets`
  ADD CONSTRAINT `tickets_assigned_to_foreign` FOREIGN KEY (`assigned_to`) REFERENCES `employees` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `tickets_created_by_foreign` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `tickets_part_foreign` FOREIGN KEY (`partcode`) REFERENCES `part_it` (`partcode`) ON DELETE CASCADE,
  ADD CONSTRAINT `tickets_team_id_foreign` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `ticket_attributes`
--
ALTER TABLE `ticket_attributes`
  ADD CONSTRAINT `ticket_attributes_ticket_id_foreign` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `ticket_reads`
--
ALTER TABLE `ticket_reads`
  ADD CONSTRAINT `ticket_reads_reader_id_foreign` FOREIGN KEY (`reader_id`) REFERENCES `employees` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `ticket_reads_ticket_id_foreign` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `ticket_relaters`
--
ALTER TABLE `ticket_relaters`
  ADD CONSTRAINT `ticket_relaters_employee_id_foreign` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `ticket_relaters_ticket_id_foreign` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`) ON DELETE CASCADE;

--
-- Các ràng buộc cho bảng `ticket_thread`
--
ALTER TABLE `ticket_thread`
  ADD CONSTRAINT `ticket_thread_employee_id_foreign` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `ticket_thread_ticket_id_foreign` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`) ON DELETE CASCADE;
COMMIT;

-- Insert data
INSERT INTO `teams` (`id`, `name`) 
VALUES (NULL, 'team 1'),
(NULL, 'team 2');
INSERT INTO `employees` (`id`, `name`, `email`, `password`, `url_image`, `rolecode`, `partcode`, `team_id`, `remember_token`, `created_at`, `updated_at`) 
VALUES (NULL, 'Thanh', 'thanh@gmail.com', '123456', NULL, 'leader', 'HANOI', '1', NULL, NULL, NULL),
 (NULL, 'Nguyen', 'nguyen@gmail.com', '123456', NULL, 'member', 'DANANG', '1', NULL, NULL, NULL);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
