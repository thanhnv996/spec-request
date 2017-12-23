-- phpMyAdmin SQL Dump
-- version 4.7.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Dec 23, 2017 at 01:00 AM
-- Server version: 10.1.25-MariaDB
-- PHP Version: 5.6.31

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
-- Database: `spec_it_request`
--

-- --------------------------------------------------------

--
-- Table structure for table `employees`
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
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `employees`
--

INSERT INTO `employees` (`id`, `name`, `email`, `password`, `url_image`, `rolecode`, `partcode`, `team_id`, `remember_token`, `created_at`, `updated_at`) VALUES
(1, 'thanh', 'thanh@gmail.com', '123456', NULL, 'leader', 'DANANG', 1, NULL, '2017-12-16 19:06:21', '2017-12-16 19:06:21'),
(2, 'van', 'van@gmail.com', '123456', NULL, 'sublead', 'DANANG', 1, NULL, '2017-12-16 19:06:21', '2017-12-16 19:06:21'),
(3, 'nguyen', 'nguyen@gmail.com', '123456', NULL, 'member', 'DANANG', 1, NULL, '2017-12-16 19:06:21', '2017-12-16 19:06:21'),
(4, 'ro', 'ro@gmail.com', '123456', NULL, 'leader', 'HANOI', 2, NULL, '2017-12-16 19:06:21', '2017-12-16 19:06:21'),
(5, 'nal', 'nal@gmail.com', '123456', NULL, 'sublead', 'HANOI', 2, NULL, '2017-12-16 19:06:21', '2017-12-16 19:06:21'),
(6, 'do', 'do@gmail.com', '123456', NULL, 'member', 'HANOI', 2, NULL, '2017-12-16 19:06:21', '2017-12-16 19:06:21');

-- --------------------------------------------------------

--
-- Table structure for table `part_it`
--

CREATE TABLE `part_it` (
  `partcode` varchar(50) NOT NULL,
  `partdesc` varchar(254) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `part_it`
--

INSERT INTO `part_it` (`partcode`, `partdesc`) VALUES
('DANANG', 'Đà nẵng'),
('HANOI', 'Hà nội');

-- --------------------------------------------------------

--
-- Table structure for table `password_resets`
--

CREATE TABLE `password_resets` (
  `email` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `token` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_at` timestamp NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `permission`
--

CREATE TABLE `permission` (
  `id` int(10) UNSIGNED NOT NULL,
  `permission` varchar(50) NOT NULL,
  `rolecode` varchar(50) NOT NULL,
  `partcode` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `permission`
--

INSERT INTO `permission` (`id`, `permission`, `rolecode`, `partcode`) VALUES
(1, 'pms_get_request_me', 'member', 'HANOI'),
(2, 'pms_get_request_me', 'member', 'DANANG'),
(3, 'pms_get_request_team', 'sublead', 'HANOI'),
(4, 'pms_post_request', 'leader', 'HANOI'),
(5, 'pms_post_request', 'leader', 'HANOI'),
(6, 'pms_get_request_team', 'leader', 'HANOI'),
(7, 'pms_put_request_team', 'leader', 'HANOI'),
(8, 'pms_get_request_part', 'leader', 'HANOI'),
(9, 'pms_get_request_team', 'sublead', 'DANANG'),
(10, 'pms_post_request_team', 'sublead', 'DANANG'),
(11, 'pms_put_request_team', 'sublead', 'DANANG'),
(12, 'pms_get_request_part', 'leader', 'DANANG'),
(13, 'pms_put_request_part', 'leader', 'DANANG'),
(14, 'pms_post_request_part', 'leader', 'DANANG'),
(15, 'pms_close_request', 'leader', 'DANANG'),
(16, 'pms_get_request_team', 'leader', 'DANANG'),
(17, 'pms_put_request_team', 'leader', 'DANANG'),
(18, 'pms_all', 'leader', 'DANANG');

-- --------------------------------------------------------

--
-- Table structure for table `reader`
--

CREATE TABLE `reader` (
  `id` int(10) UNSIGNED NOT NULL,
  `ticket_id` int(10) UNSIGNED NOT NULL,
  `employee_id` int(10) UNSIGNED NOT NULL,
  `status_read` int(10) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `reader`
--

INSERT INTO `reader` (`id`, `ticket_id`, `employee_id`, `status_read`) VALUES
(1, 12, 6, 0),
(2, 11, 4, 0),
(11, 12, 1, 1),
(12, 9, 1, 1),
(13, 14, 1, 1);

-- --------------------------------------------------------

--
-- Table structure for table `role`
--

CREATE TABLE `role` (
  `rolecode` varchar(50) NOT NULL,
  `roledesc` varchar(254) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `role`
--

INSERT INTO `role` (`rolecode`, `roledesc`) VALUES
('leader', 'Leader'),
('member', 'Member'),
('sublead', 'Sublead');

-- --------------------------------------------------------

--
-- Table structure for table `teams`
--

CREATE TABLE `teams` (
  `id` int(10) UNSIGNED NOT NULL,
  `name` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `teams`
--

INSERT INTO `teams` (`id`, `name`, `description`, `created_at`, `updated_at`) VALUES
(1, 'team 1', 'đội 1', '2017-12-16 19:06:21', '2017-12-16 19:06:21'),
(2, 'team 2', 'đội 2', '2017-12-16 19:06:21', '2017-12-16 19:06:21');

-- --------------------------------------------------------

--
-- Table structure for table `tickets`
--

CREATE TABLE `tickets` (
  `id` int(10) UNSIGNED NOT NULL,
  `subject` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `created_by` int(10) UNSIGNED NOT NULL,
  `status` varchar(25) DEFAULT NULL,
  `priority` tinyint(4) NOT NULL,
  `deadline` datetime NOT NULL,
  `assigned_to` int(10) UNSIGNED NOT NULL,
  `partcode` varchar(50) NOT NULL,
  `rating` tinyint(4) DEFAULT NULL,
  `team_id` int(10) UNSIGNED NOT NULL,
  `resolved_at` datetime DEFAULT NULL,
  `closed_at` datetime DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `tickets`
--

INSERT INTO `tickets` (`id`, `subject`, `content`, `created_by`, `status`, `priority`, `deadline`, `assigned_to`, `partcode`, `rating`, `team_id`, `resolved_at`, `closed_at`, `created_at`, `updated_at`, `deleted_at`) VALUES
(2, 'tên công việc 2', 'thay đổi nội dung', 1, 'cancelled', 1, '2017-12-21 14:24:00', 4, 'DANANG', 1, 1, NULL, NULL, '2017-12-16 19:10:19', '2017-12-21 08:00:30', NULL),
(3, 'tên công việc 3', 'thay đổi nội dung', 1, 'closed', 5, '2018-12-12 12:13:00', 4, 'DANANG', NULL, 1, NULL, NULL, '2017-12-16 19:11:33', '2017-12-18 14:56:21', NULL),
(4, 'tên công việc 4', 'thay đổi nội dung', 3, 'resolved', 3, '2018-12-12 12:13:00', 4, 'DANANG', 0, 1, NULL, NULL, '2017-12-17 04:18:18', '2017-12-18 14:56:25', NULL),
(5, 'tên công việc 5', 'not content', 4, 'new', 1, '1212-12-12 12:12:00', 2, 'HANOI', NULL, 2, NULL, NULL, '2017-12-17 08:21:54', '2017-12-18 14:56:31', NULL),
(6, 'aaaaaaaaaaaaaaaaaasd', 'not content', 1, 'inprogress', 1, '1212-12-12 12:12:00', 2, 'HANOI', 0, 1, NULL, NULL, '2017-12-19 12:45:41', '2017-12-21 08:01:23', NULL),
(7, 'zzzzzzz', 'zzzzz', 1, 'new', 4, '1111-11-11 11:11:00', 5, 'HANOI', 0, 1, NULL, NULL, '2017-12-19 13:39:09', '2017-12-20 18:01:51', NULL),
(8, 'aa', 'ádfasdfasdf', 1, 'new', 2, '2018-04-30 12:12:00', 5, 'HANOI', NULL, 1, NULL, NULL, '2017-12-19 13:42:21', '2017-12-20 17:55:50', NULL),
(9, 'zz', 'zzz', 1, 'new', 4, '1111-11-11 11:22:00', 6, 'HANOI', NULL, 1, NULL, NULL, '2017-12-19 13:43:07', '2017-12-21 15:22:01', NULL),
(10, 'zzz', 'zzzzz', 1, 'inprogress', 4, '1111-11-11 22:11:00', 6, 'HANOI', 1, 1, NULL, NULL, '2017-12-19 13:43:42', '2017-12-21 15:49:11', NULL),
(11, 'ôn thi đi', 'ôn thi nhanh mai thi rồi', 1, 'cancelled', 4, '2017-12-20 12:00:00', 1, 'HANOI', NULL, 1, NULL, NULL, '2017-12-19 13:46:12', '2017-12-21 08:10:21', NULL),
(12, 'on', '<p>Ôn thi ngay và luôn</p>', 1, 'new', 3, '2100-12-15 13:13:00', 3, 'HANOI', NULL, 1, NULL, NULL, '2017-12-19 14:30:02', '2017-12-20 18:16:13', NULL),
(13, 'qẻqwerqwe', '<p>zzzzzz</p>', 1, 'new', 3, '1111-01-01 01:01:00', 5, 'HANOI', 0, 1, NULL, NULL, '2017-12-19 14:30:53', '2017-12-21 06:05:42', NULL),
(14, 'chơi game', '<p>chơi game chứ làm gì</p>', 1, 'closed', 4, '2018-12-12 12:13:00', 6, 'DANANG', 0, 1, NULL, NULL, '2017-12-20 10:49:43', '2017-12-21 08:22:16', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `ticket_attributes`
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
-- Table structure for table `ticket_images`
--

CREATE TABLE `ticket_images` (
  `ticket_id` int(10) UNSIGNED NOT NULL,
  `url_image` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `ticket_relaters`
--

CREATE TABLE `ticket_relaters` (
  `id` int(10) UNSIGNED NOT NULL,
  `ticket_id` int(10) UNSIGNED NOT NULL,
  `employee_id` int(10) UNSIGNED NOT NULL,
  `status_read` int(10) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ticket_relaters`
--

INSERT INTO `ticket_relaters` (`id`, `ticket_id`, `employee_id`, `status_read`) VALUES
(29, 4, 6, 0),
(30, 4, 4, 0),
(31, 4, 1, 0),
(32, 2, 1, 0),
(38, 10, 6, 0),
(48, 9, 2, 0),
(49, 9, 1, 0);

-- --------------------------------------------------------

--
-- Table structure for table `ticket_thread`
--

CREATE TABLE `ticket_thread` (
  `id` int(10) UNSIGNED NOT NULL,
  `ticket_id` int(10) UNSIGNED NOT NULL,
  `employee_id` int(10) UNSIGNED NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` tinyint(4) DEFAULT NULL,
  `note` varchar(191) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Dumping data for table `ticket_thread`
--

INSERT INTO `ticket_thread` (`id`, `ticket_id`, `employee_id`, `content`, `type`, `note`, `created_at`, `updated_at`) VALUES
(17, 2, 1, '\nThay đổi mức độ ưu tiên : Cao>Cao\ncứ từ từ mà làm :)))', NULL, NULL, '2017-12-17 07:08:15', '2017-12-17 07:08:15'),
(18, 2, 1, '\nThay đổi deadline : từWed Dec 12 12:13:00 ICT 2018 -> Wed Dec 12 12:13:00 ICT 2018\nchú làm ăn như ****', NULL, NULL, '2017-12-17 07:08:15', '2017-12-17 07:08:15'),
(20, 2, 1, '\nThay đổi deadline : từWed Dec 12 12:13:00 ICT 2018 -> Wed Dec 12 12:13:00 ICT 2018\nchú làm ăn như ****', NULL, NULL, '2017-12-17 07:10:51', '2017-12-17 07:10:51'),
(21, 2, 1, '\nThay đổi mức độ ưu tiên : Cao>Cao\ncứ từ từ mà làm :)))', NULL, NULL, '2017-12-17 07:10:51', '2017-12-17 07:10:51'),
(23, 4, 1, '\nThay đổi deadline : từWed Dec 12 12:12:00 ICT 1212 -> Wed Dec 12 12:13:00 ICT 2018\nchú làm ăn như ****', NULL, NULL, '2017-12-17 07:11:46', '2017-12-17 07:11:46'),
(24, 4, 1, '\nThay đổi mức độ ưu tiên : Thấp>Cao\ncứ từ từ mà làm :)))', NULL, NULL, '2017-12-17 07:11:46', '2017-12-17 07:11:46'),
(26, 4, 1, '\nThay đổi deadline : từWed Dec 12 12:13:00 ICT 2018 -> Wed Dec 12 12:13:00 ICT 2018\nchú làm ăn như ****', NULL, NULL, '2017-12-17 07:14:27', '2017-12-17 07:14:27'),
(29, 4, 3, 'comment thôi ', NULL, NULL, '2017-12-17 07:19:24', '2017-12-17 07:19:24'),
(30, 4, 1, 'comment thôi ', NULL, NULL, '2017-12-17 07:29:58', '2017-12-17 07:29:58'),
(31, 4, 6, 'comment thôi ', NULL, NULL, '2017-12-17 07:30:51', '2017-12-17 07:30:51'),
(32, 4, 1, '\nThay đổi mức độ ưu tiên : Cao>Cao\ncứ từ từ mà làm :)))', NULL, NULL, '2017-12-17 07:34:41', '2017-12-17 07:34:41'),
(33, 4, 1, '\nThay đổi deadline : từ12/12/2018 12:13 -> Wed Dec 12 12:13:00 ICT 2018\nchú làm ăn như ****', NULL, NULL, '2017-12-17 07:34:41', '2017-12-17 07:34:41'),
(35, 4, 1, '\nThay đổi mức độ ưu tiên : Cao>Cao\ncứ từ từ mà làm :)))', NULL, NULL, '2017-12-17 07:35:14', '2017-12-17 07:35:14'),
(36, 4, 1, '\nThay đổi deadline : từ12/12/2018 12:13 -> 12/12/2018 12:13\nchú làm ăn như ****', NULL, NULL, '2017-12-17 07:35:14', '2017-12-17 07:35:14'),
(58, 2, 1, '<p>hihi</p>', NULL, NULL, '2017-12-20 10:24:15', '2017-12-20 10:24:15'),
(59, 2, 1, '\nĐánh giá : không hài lòng\nchú làm ăn như ****', NULL, NULL, '2017-12-20 13:43:58', '2017-12-20 13:43:58'),
(60, 2, 1, '\nĐánh giá : không hài lòng\nchú làm ăn như ****', NULL, NULL, '2017-12-20 13:56:55', '2017-12-20 13:56:55'),
(61, 2, 1, '\nĐánh giá : không hài lòng\nchú làm ăn như ****', NULL, NULL, '2017-12-20 13:57:00', '2017-12-20 13:57:00'),
(62, 2, 1, '\nĐánh giá : không hài lòng\n<p>zzzz</p>', NULL, NULL, '2017-12-20 14:15:05', '2017-12-20 14:15:05'),
(63, 2, 1, '\nĐánh giá : hài lòng\n<p>adfasdf</p>', NULL, NULL, '2017-12-20 14:23:51', '2017-12-20 14:23:51'),
(64, 2, 1, '\nĐánh giá : hài lòng\n<p>z</p>', NULL, NULL, '2017-12-20 14:56:44', '2017-12-20 14:56:44'),
(65, 2, 1, '\nĐánh giá : hài lòng\n<p>ádfasdfasd</p>', NULL, NULL, '2017-12-20 15:00:26', '2017-12-20 15:00:26'),
(66, 2, 1, '\nĐánh giá : hài lòng\n<p>ádfasdfasd</p>', NULL, NULL, '2017-12-20 15:00:50', '2017-12-20 15:00:50'),
(67, 2, 1, '\nĐánh giá : không hài lòng\n<p>qưerqwer</p>', NULL, NULL, '2017-12-20 16:45:36', '2017-12-20 16:45:36'),
(68, 2, 1, '\nĐánh giá : không hài lòng\n<p>fdfdf</p>', NULL, NULL, '2017-12-20 16:47:30', '2017-12-20 16:47:30'),
(69, 2, 1, '\nĐánh giá : không hài lòng\n<p>fdfdf</p>', NULL, NULL, '2017-12-20 16:47:43', '2017-12-20 16:47:43'),
(70, 14, 1, '\nThay đổi deadline : từ11/11/1191 11:11 -> 12/12/2018 12:13\nmày làm chậm quá t phải thay đổi :<<<', NULL, NULL, '2017-12-20 17:39:05', '2017-12-20 17:39:05'),
(71, 14, 1, '\nThay đổi deadline : từ12/12/2018 12:13 -> 12/12/2018 12:13\nmày làm chậm quá t phải thay đổi :<<<', NULL, NULL, '2017-12-20 17:40:22', '2017-12-20 17:40:22'),
(72, 2, 1, '\nThay đổi deadline : từ12/12/2018 12:13 -> 11/11/1111 11:11\n<p>21/12/2017</p>', NULL, NULL, '2017-12-20 17:41:40', '2017-12-20 17:41:40'),
(73, 2, 1, '<p>zzz</p>', NULL, NULL, '2017-12-20 17:42:20', '2017-12-20 17:42:20'),
(74, 8, 1, '\nThay đổi deadline : từ11/11/1111 22:22 -> 30/04/2018 12:12\n<p>lần cuối trong ngày</p>', NULL, NULL, '2017-12-20 17:43:56', '2017-12-20 17:43:56'),
(75, 8, 1, '<p>ádflajsdlkfjaslkdf</p>', NULL, NULL, '2017-12-20 17:52:21', '2017-12-20 17:52:21'),
(76, 8, 1, '<p>ádflajsdlkfjaslkdfqưeqre</p>', NULL, NULL, '2017-12-20 17:52:23', '2017-12-20 17:52:23'),
(77, 8, 1, '<p>như cc vậy\\</p><p><br></p>', NULL, NULL, '2017-12-20 17:52:40', '2017-12-20 17:52:40'),
(78, 8, 1, '\nThay đổi mức độ ưu tiên : Thấp>Cao\n<p>thích thì đổi thôi</p>', NULL, NULL, '2017-12-20 17:53:07', '2017-12-20 17:53:07'),
(79, 9, 1, '\nThay đổi mức độ ưu tiên : Bình thường>Khẩn cấp\n<p>không lý do</p>', NULL, NULL, '2017-12-20 17:54:55', '2017-12-20 17:54:55'),
(80, 8, 1, '\nThay đổi mức độ ưu tiên : Cao>Bình thường\n<p>xxx</p>', NULL, NULL, '2017-12-20 17:55:50', '2017-12-20 17:55:50'),
(81, 10, 1, '\nThay đổi mức độ ưu tiên : Khẩn cấp>Bình thường\n<p>ọlkjl</p>', NULL, NULL, '2017-12-20 17:57:02', '2017-12-20 17:57:02'),
(82, 7, 1, '\nThay đổi mức độ ưu tiên : Thấp>Khẩn cấp\n<p>ádf</p>', NULL, NULL, '2017-12-20 18:01:29', '2017-12-20 18:01:29'),
(83, 7, 1, '\nĐánh giá : không hài lòng\n<p>làm chậm quá em ơi</p>', NULL, NULL, '2017-12-20 18:01:51', '2017-12-20 18:01:51'),
(84, 7, 1, '\nThay đổi mức độ ưu tiên : Khẩn cấp>Khẩn cấp\n<p>ádf</p>', NULL, NULL, '2017-12-20 18:01:51', '2017-12-20 18:01:51'),
(85, 10, 1, '\nThay đổi mức độ ưu tiên : Bình thường>Cao\n<p>zxcv</p>', NULL, NULL, '2017-12-20 18:09:03', '2017-12-20 18:09:03'),
(86, 9, 1, '\nThay đổi mức độ ưu tiên : Khẩn cấp>Khẩn cấp\n<p>fdfsf</p>', NULL, NULL, '2017-12-20 18:11:38', '2017-12-20 18:11:38'),
(87, 12, 1, '\nThay đổi mức độ ưu tiên : Thấp>Bình thường\n<p>ưewe</p>', NULL, NULL, '2017-12-20 18:12:21', '2017-12-20 18:12:21'),
(88, 12, 1, '\nThay đổi mức độ ưu tiên : Bình thường>Cao\n<p>ưewe</p>', NULL, NULL, '2017-12-20 18:13:30', '2017-12-20 18:13:30'),
(89, 12, 1, '\nThay đổi mức độ ưu tiên : Cao>Bình thường\n<p>ưewew</p>', NULL, NULL, '2017-12-20 18:15:43', '2017-12-20 18:15:43'),
(90, 12, 1, '\nThay đổi mức độ ưu tiên : Bình thường>Khẩn cấp\n<p>ưewew</p>', NULL, NULL, '2017-12-20 18:16:09', '2017-12-20 18:16:09'),
(91, 12, 1, '\nThay đổi mức độ ưu tiên : Khẩn cấp>Cao\n<p>ưewew</p>', NULL, NULL, '2017-12-20 18:16:13', '2017-12-20 18:16:13'),
(92, 10, 1, '\nThay đổi mức độ ưu tiên : Cao>Thấp\n<p>fsdfsdf</p>', NULL, NULL, '2017-12-20 18:16:32', '2017-12-20 18:16:32'),
(93, 10, 1, '\nThay đổi mức độ ưu tiên : Thấp>Thấp\n<p>fsdfsdf</p>', NULL, NULL, '2017-12-20 18:16:33', '2017-12-20 18:16:33'),
(94, 10, 1, '\nThay đổi mức độ ưu tiên : Thấp>Thấp\n<p>fsdfsdf</p>', NULL, NULL, '2017-12-20 18:16:40', '2017-12-20 18:16:40'),
(95, 10, 1, '\nThay đổi mức độ ưu tiên : Thấp>Thấp\n<p>fsdfsdf</p>', NULL, NULL, '2017-12-20 18:17:05', '2017-12-20 18:17:05'),
(96, 10, 1, '\nThay đổi mức độ ưu tiên : Thấp>Cao\n<p>fsdfsdf</p>', NULL, NULL, '2017-12-20 18:17:17', '2017-12-20 18:17:17'),
(97, 10, 1, '\nThay đổi mức độ ưu tiên : Cao>Thấp\n<p>fsdfsdf</p>', NULL, NULL, '2017-12-20 18:17:23', '2017-12-20 18:17:23'),
(98, 10, 1, '\nThay đổi mức độ ưu tiên : Thấp>Khẩn cấp\n<p>fsdfsdf</p>', NULL, NULL, '2017-12-20 18:17:32', '2017-12-20 18:17:32'),
(99, 10, 1, '\nThay đổi mức độ ưu tiên : Khẩn cấp>Khẩn cấp\n<p>fsdfsdf</p>', NULL, NULL, '2017-12-20 18:17:42', '2017-12-20 18:17:42'),
(100, 10, 1, '\nĐánh giá : không hài lòng\n<p>sdsd</p>', NULL, NULL, '2017-12-20 18:17:42', '2017-12-20 18:17:42'),
(101, 2, 1, '\nĐánh giá : hài lòng\n<p>lklk</p>', NULL, NULL, '2017-12-21 05:59:56', '2017-12-21 05:59:59'),
(102, 14, 1, '\nĐánh giá : hài lòng\n<p>sfgsfg</p>', NULL, NULL, '2017-12-21 06:01:52', '2017-12-21 06:01:52'),
(103, 14, 1, '\nĐánh giá : không hài lòng\n<p>sfgsfg</p>', NULL, NULL, '2017-12-21 06:02:00', '2017-12-21 06:02:00'),
(104, 14, 1, '\nĐánh giá : không hài lòng\n<p>sfgsfg</p>', NULL, NULL, '2017-12-21 06:02:07', '2017-12-21 06:02:07'),
(105, 14, 1, '\nĐánh giá : hài lòng\n<p>lklkl</p>', NULL, NULL, '2017-12-21 06:04:01', '2017-12-21 06:04:01'),
(106, 14, 1, '\nĐánh giá : không hài lòng\n<p>lklkl</p>', NULL, NULL, '2017-12-21 06:04:07', '2017-12-21 06:04:07'),
(107, 14, 1, '\nĐánh giá : hài lòng\n<p>lklkl</p>', NULL, NULL, '2017-12-21 06:04:49', '2017-12-21 06:04:49'),
(108, 14, 1, '\nĐánh giá : không hài lòng\n<p>lklkl</p>', NULL, NULL, '2017-12-21 06:04:51', '2017-12-21 06:04:51'),
(109, 13, 1, '\nĐánh giá : hài lòng\n<p>qưerwe</p>', NULL, NULL, '2017-12-21 06:05:38', '2017-12-21 06:05:39'),
(110, 13, 1, '\nĐánh giá : không hài lòng\n<p>qưerwe</p>', NULL, NULL, '2017-12-21 06:05:42', '2017-12-21 06:05:42'),
(111, 6, 1, '\nĐánh giá : không hài lòng\n<p>ádfasdf</p>', NULL, NULL, '2017-12-21 06:06:02', '2017-12-21 06:06:02'),
(112, 2, 1, '\nĐánh giá : hài lòng\n<p>lklk</p>', NULL, NULL, '2017-12-21 07:23:30', '2017-12-21 07:23:30'),
(113, 2, 1, '\nĐánh giá : hài lòng\n<p>jkjkjk</p>', NULL, NULL, '2017-12-21 07:24:38', '2017-12-21 07:24:38'),
(114, 2, 1, '\nThay đổi deadline : từ11/11/1111 11:11 -> 21/12/2017 14:24\n<p>ấdf</p>', NULL, NULL, '2017-12-21 07:25:07', '2017-12-21 07:25:07'),
(115, 2, 1, '\nThay đổi mức độ ưu tiên : Cao>Thấp\n<p>lklkl</p>', NULL, NULL, '2017-12-21 07:30:35', '2017-12-21 07:30:35'),
(116, 10, 1, '\nĐánh giá : hài lòng\n<p>zxcvcxv</p>', NULL, NULL, '2017-12-21 15:49:11', '2017-12-21 15:49:11');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `employees`
--
ALTER TABLE `employees`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD UNIQUE KEY `employees_email_unique` (`email`) USING BTREE,
  ADD KEY `USER_ROLE` (`rolecode`),
  ADD KEY `USER_PART` (`partcode`),
  ADD KEY `employee_team` (`team_id`);

--
-- Indexes for table `part_it`
--
ALTER TABLE `part_it`
  ADD PRIMARY KEY (`partcode`);

--
-- Indexes for table `password_resets`
--
ALTER TABLE `password_resets`
  ADD KEY `password_resets_email_index` (`email`) USING BTREE;

--
-- Indexes for table `permission`
--
ALTER TABLE `permission`
  ADD PRIMARY KEY (`id`),
  ADD KEY `rolecode` (`rolecode`),
  ADD KEY `partcode` (`partcode`);

--
-- Indexes for table `reader`
--
ALTER TABLE `reader`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `role`
--
ALTER TABLE `role`
  ADD PRIMARY KEY (`rolecode`);

--
-- Indexes for table `teams`
--
ALTER TABLE `teams`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD UNIQUE KEY `teams_name_unique` (`name`) USING BTREE;

--
-- Indexes for table `tickets`
--
ALTER TABLE `tickets`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD KEY `tickets_created_by_foreign` (`created_by`) USING BTREE,
  ADD KEY `tickets_assigned_to_foreign` (`assigned_to`) USING BTREE,
  ADD KEY `tickets_team_id_foreign` (`team_id`) USING BTREE,
  ADD KEY `tickets_part_foreign` (`partcode`);

--
-- Indexes for table `ticket_attributes`
--
ALTER TABLE `ticket_attributes`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD KEY `ticket_attributes_ticket_id_foreign` (`ticket_id`) USING BTREE;

--
-- Indexes for table `ticket_relaters`
--
ALTER TABLE `ticket_relaters`
  ADD PRIMARY KEY (`id`),
  ADD KEY `ticket_relaters_ticket_id_foreign` (`ticket_id`) USING BTREE,
  ADD KEY `ticket_relaters_employee_id_foreign` (`employee_id`) USING BTREE;

--
-- Indexes for table `ticket_thread`
--
ALTER TABLE `ticket_thread`
  ADD PRIMARY KEY (`id`) USING BTREE,
  ADD KEY `ticket_thread_ticket_id_foreign` (`ticket_id`) USING BTREE,
  ADD KEY `ticket_thread_employee_id_foreign` (`employee_id`) USING BTREE;

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `employees`
--
ALTER TABLE `employees`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `permission`
--
ALTER TABLE `permission`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;
--
-- AUTO_INCREMENT for table `reader`
--
ALTER TABLE `reader`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
--
-- AUTO_INCREMENT for table `teams`
--
ALTER TABLE `teams`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `tickets`
--
ALTER TABLE `tickets`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
--
-- AUTO_INCREMENT for table `ticket_attributes`
--
ALTER TABLE `ticket_attributes`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `ticket_relaters`
--
ALTER TABLE `ticket_relaters`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=50;
--
-- AUTO_INCREMENT for table `ticket_thread`
--
ALTER TABLE `ticket_thread`
  MODIFY `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=117;
--
-- Constraints for dumped tables
--

--
-- Constraints for table `employees`
--
ALTER TABLE `employees`
  ADD CONSTRAINT `employee_part` FOREIGN KEY (`partcode`) REFERENCES `part_it` (`partcode`) ON DELETE CASCADE,
  ADD CONSTRAINT `employee_role` FOREIGN KEY (`rolecode`) REFERENCES `role` (`rolecode`) ON DELETE CASCADE,
  ADD CONSTRAINT `employee_team` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `permission`
--
ALTER TABLE `permission`
  ADD CONSTRAINT `permission_part` FOREIGN KEY (`partcode`) REFERENCES `part_it` (`partcode`) ON DELETE CASCADE,
  ADD CONSTRAINT `permission_role` FOREIGN KEY (`rolecode`) REFERENCES `role` (`rolecode`) ON DELETE CASCADE;

--
-- Constraints for table `tickets`
--
ALTER TABLE `tickets`
  ADD CONSTRAINT `tickets_assigned_to_foreign` FOREIGN KEY (`assigned_to`) REFERENCES `employees` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `tickets_created_by_foreign` FOREIGN KEY (`created_by`) REFERENCES `employees` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `tickets_part_foreign` FOREIGN KEY (`partcode`) REFERENCES `part_it` (`partcode`) ON DELETE CASCADE,
  ADD CONSTRAINT `tickets_team_id_foreign` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `ticket_attributes`
--
ALTER TABLE `ticket_attributes`
  ADD CONSTRAINT `ticket_attributes_ticket_id_foreign` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `ticket_relaters`
--
ALTER TABLE `ticket_relaters`
  ADD CONSTRAINT `ticket_relaters_employee_id_foreign` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `ticket_relaters_ticket_id_foreign` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `ticket_thread`
--
ALTER TABLE `ticket_thread`
  ADD CONSTRAINT `ticket_thread_employee_id_foreign` FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `ticket_thread_ticket_id_foreign` FOREIGN KEY (`ticket_id`) REFERENCES `tickets` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
