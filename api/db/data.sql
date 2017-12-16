INSERT INTO `teams` (`id`, `name`, `description`, `created_at`, `updated_at`) 
VALUES (1, 'team 1', 'đội 1', NULL, NULL),
(2, 'team 2', 'đội 2', NULL, NULL);


INSERT INTO `employees` (`id`, `name`, `email`, `password`, `url_image`, `rolecode`, `partcode`, `team_id`, `remember_token`, `created_at`, `updated_at`) VALUES 
(1, 'thanh', 'thanh@gmail.com', '123456', NULL, 'leader', 'DANANG', '1', NULL, NULL, NULL),
(2, 'van', 'van@gmail.com', '123456', NULL, 'sublead', 'DANANG', '1', NULL, NULL, NULL),
(3, 'nguyen', 'nguyen@gmail.com', '123456', NULL, 'member', 'DANANG', '1', NULL, NULL, NULL),
(4, 'ro', 'ro@gmail.com', '123456', NULL, 'leader', 'HANOI', '2', NULL, NULL, NULL),
(5, 'nal', 'nal@gmail.com', '123456', NULL, 'sublead', 'HANOI', '2', NULL, NULL, NULL),
(6, 'do', 'do@gmail.com', '123456', NULL, 'member', 'HANOI', '2', NULL, NULL, NULL);