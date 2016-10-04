-- phpMyAdmin SQL Dump
-- version 3.5.2.2
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Aug 21, 2016 at 07:02 PM
-- Server version: 5.5.27
-- PHP Version: 5.4.7

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `trafficapp`
--

-- --------------------------------------------------------

--
-- Table structure for table `location`
--

CREATE TABLE IF NOT EXISTS `location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=6 ;

--
-- Dumping data for table `location`
--

INSERT INTO `location` (`id`, `name`) VALUES
(1, 'Dhanmondi'),
(2, 'Shahbag'),
(3, 'Science Lab'),
(4, 'Nilkhet'),
(5, 'Azimpur');

-- --------------------------------------------------------

--
-- Table structure for table `location_user`
--

CREATE TABLE IF NOT EXISTS `location_user` (
  `userId` int(11) NOT NULL,
  `locationId` int(11) NOT NULL,
  UNIQUE KEY `unique_row` (`locationId`,`userId`),
  KEY `userId` (`userId`,`locationId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `location_user`
--

INSERT INTO `location_user` (`userId`, `locationId`) VALUES
(7, 1),
(10, 1),
(17, 1),
(18, 1),
(31, 2),
(34, 2),
(10, 4),
(7, 5),
(10, 5),
(31, 5),
(34, 5);

-- --------------------------------------------------------

--
-- Table structure for table `notification`
--

CREATE TABLE IF NOT EXISTS `notification` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `notifTo` int(11) NOT NULL,
  `notifFrom` int(11) NOT NULL,
  `notifType` enum('like','comment','commentResponse','request','requestResponse','follow','followResponse') DEFAULT NULL,
  `notifAbout` enum('update','post','announcement','request') NOT NULL,
  `notifAboutId` int(11) DEFAULT NULL,
  `timeOfNotification` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_2` (`id`),
  KEY `id` (`id`,`notifTo`,`notifFrom`),
  KEY `notifTo` (`notifTo`),
  KEY `notifFrom` (`notifFrom`),
  KEY `postId` (`notifAboutId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=378 ;

--
-- Dumping data for table `notification`
--

INSERT INTO `notification` (`id`, `notifTo`, `notifFrom`, `notifType`, `notifAbout`, `notifAboutId`, `timeOfNotification`) VALUES
(1, 18, 17, 'like', 'update', 33, '2015-11-23 18:27:17'),
(3, 7, 10, 'request', 'request', 33, '2015-11-25 08:59:23'),
(5, 10, 18, 'like', 'update', 33, '2015-11-25 09:29:58'),
(6, 7, 18, 'request', 'request', 33, '2015-11-25 09:38:43'),
(7, 10, 18, 'request', 'request', 33, '2015-11-25 09:38:43'),
(8, 7, 18, 'request', 'request', 33, '2015-11-25 09:47:09'),
(9, 10, 18, 'request', 'request', 33, '2015-11-25 09:47:09'),
(12, 7, 18, 'request', 'request', 33, '2015-11-26 06:57:02'),
(13, 10, 18, 'request', 'request', 33, '2015-11-26 06:57:02'),
(14, 17, 18, 'request', 'request', 33, '2015-11-26 06:57:02'),
(20, 18, 17, 'requestResponse', 'request', 33, '2015-11-28 04:58:50'),
(21, 18, 17, 'followResponse', 'request', 33, '2015-11-28 04:58:50'),
(22, 10, 17, 'followResponse', 'request', 33, '2015-11-28 04:58:50'),
(24, 7, 18, 'request', 'request', 33, '2015-11-28 12:31:39'),
(25, 17, 18, 'like', 'update', 33, '2015-11-28 12:50:31'),
(96, 7, 17, 'like', 'post', 33, '2015-11-28 16:56:04'),
(114, 17, 18, 'like', 'post', 33, '2015-11-28 17:36:41'),
(121, 10, 18, 'like', 'post', 33, '2015-11-28 18:36:12'),
(122, 10, 18, 'like', 'post', 33, '2015-11-28 18:36:15'),
(123, 10, 18, 'like', 'post', 33, '2015-11-28 18:36:20'),
(125, 10, 18, 'like', 'post', 33, '2015-11-28 18:37:12'),
(127, 10, 18, 'like', 'post', 33, '2015-11-28 18:37:34'),
(137, 18, 19, 'like', 'update', 33, '2015-11-29 05:52:57'),
(138, 18, 19, 'like', 'update', 33, '2015-11-29 05:53:50'),
(143, 7, 18, 'request', 'request', 33, '2015-11-29 06:31:43'),
(144, 18, 7, 'requestResponse', 'request', 33, '2015-11-29 06:33:56'),
(145, 7, 18, 'like', 'update', 33, '2015-12-01 09:34:57'),
(155, 10, 18, 'comment', 'update', 33, '2015-12-02 11:14:34'),
(157, 10, 17, 'comment', 'update', 33, '2015-12-02 11:20:44'),
(158, 18, 17, 'commentResponse', 'update', 33, '2015-12-02 11:20:44'),
(159, 17, 10, 'commentResponse', 'update', 33, '2015-12-02 11:21:43'),
(160, 18, 10, 'commentResponse', 'update', 33, '2015-12-02 11:21:43'),
(161, 10, 18, 'like', 'update', 33, '2015-12-02 18:25:42'),
(162, 10, 18, 'like', 'post', 33, '2015-12-09 06:53:35'),
(163, 10, 18, 'like', 'post', 33, '2015-12-09 06:53:37'),
(164, 17, 18, 'like', 'post', 33, '2015-12-09 11:39:53'),
(165, 10, 17, 'comment', 'post', 33, '2015-12-09 13:11:00'),
(166, 10, 18, 'comment', 'post', 33, '2015-12-09 13:27:21'),
(167, 10, 18, 'comment', 'post', 33, '2015-12-09 13:34:01'),
(168, 10, 18, 'comment', 'post', 33, '2015-12-09 13:34:15'),
(169, 17, 18, 'comment', 'post', 33, '2015-12-09 13:37:17'),
(170, 7, 10, 'request', 'request', 33, '2015-12-09 13:44:00'),
(171, 7, 18, 'comment', 'update', 33, '2015-12-09 17:04:38'),
(173, 18, 10, 'comment', 'post', 10, '2015-12-09 17:29:04'),
(174, 18, 7, 'comment', 'post', 10, '2015-12-09 17:37:55'),
(175, 10, 7, 'commentResponse', 'post', 10, '2015-12-09 17:37:55'),
(177, 18, 10, 'comment', 'post', 10, '2015-12-09 17:46:44'),
(178, 7, 10, 'commentResponse', 'post', 10, '2015-12-09 17:46:44'),
(179, 7, 10, 'comment', 'post', 6, '2015-12-09 17:52:37'),
(180, 7, 10, 'comment', 'post', 6, '2015-12-09 17:52:40'),
(181, 18, 10, 'comment', 'post', 11, '2015-12-09 17:55:27'),
(182, 17, 18, 'like', 'post', 8, '2015-12-09 17:58:37'),
(183, 17, 18, 'like', 'post', 8, '2015-12-09 17:58:45'),
(184, 17, 18, 'like', 'post', 8, '2015-12-09 17:59:26'),
(186, 18, 10, 'follow', 'request', 52, '2015-12-09 18:12:01'),
(187, 18, 27, 'like', 'update', 36, '2015-12-12 14:51:58'),
(188, 18, 27, 'like', 'update', 35, '2015-12-12 14:51:59'),
(189, 18, 27, 'like', 'update', 34, '2015-12-12 14:52:01'),
(190, 7, 27, 'like', 'update', 33, '2015-12-12 14:52:06'),
(191, 18, 27, 'like', 'update', 31, '2015-12-12 14:52:12'),
(192, 18, 27, 'like', 'update', 30, '2015-12-12 14:52:13'),
(193, 18, 27, 'like', 'update', 26, '2015-12-12 14:52:15'),
(194, 18, 27, 'like', 'update', 21, '2015-12-12 14:52:16'),
(195, 18, 27, 'like', 'update', 20, '2015-12-12 14:52:17'),
(196, 18, 27, 'like', 'update', 18, '2015-12-12 14:52:19'),
(197, 18, 27, 'like', 'update', 17, '2015-12-12 14:52:20'),
(198, 24, 27, 'like', 'update', 16, '2015-12-12 14:52:21'),
(199, 18, 27, 'like', 'update', 15, '2015-12-12 14:52:23'),
(200, 18, 27, 'like', 'update', 14, '2015-12-12 14:52:24'),
(201, 18, 27, 'like', 'update', 13, '2015-12-12 14:52:26'),
(202, 18, 27, 'like', 'update', 12, '2015-12-12 14:52:27'),
(203, 18, 27, 'like', 'update', 12, '2015-12-12 14:52:28'),
(204, 18, 27, 'like', 'update', 12, '2015-12-12 14:52:30'),
(205, 18, 27, 'like', 'update', 11, '2015-12-12 14:52:31'),
(206, 18, 27, 'like', 'update', 10, '2015-12-12 14:52:32'),
(207, 18, 27, 'like', 'update', 9, '2015-12-12 14:52:34'),
(208, 18, 27, 'like', 'update', 8, '2015-12-12 14:52:35'),
(209, 10, 27, 'like', 'update', 7, '2015-12-12 14:52:36'),
(210, 10, 27, 'like', 'update', 5, '2015-12-12 14:52:39'),
(211, 7, 27, 'like', 'update', 4, '2015-12-12 14:52:40'),
(212, 18, 27, 'like', 'update', 36, '2015-12-12 14:56:50'),
(213, 18, 27, 'like', 'update', 34, '2015-12-12 14:56:55'),
(214, 7, 27, 'like', 'update', 33, '2015-12-12 14:57:03'),
(215, 18, 27, 'like', 'update', 31, '2015-12-12 14:57:08'),
(216, 18, 27, 'like', 'update', 31, '2015-12-12 14:57:11'),
(217, 18, 27, 'like', 'update', 31, '2015-12-12 14:57:14'),
(218, 18, 27, 'like', 'update', 31, '2015-12-12 14:57:24'),
(219, 18, 27, 'like', 'update', 31, '2015-12-12 14:57:25'),
(220, 18, 27, 'like', 'update', 31, '2015-12-12 14:57:33'),
(221, 18, 27, 'like', 'update', 32, '2015-12-12 14:57:35'),
(222, 18, 27, 'like', 'update', 36, '2015-12-12 14:59:20'),
(223, 18, 27, 'like', 'update', 36, '2015-12-12 14:59:22'),
(224, 27, 18, 'like', 'update', 37, '2015-12-12 16:38:14'),
(225, 27, 18, 'like', 'update', 37, '2015-12-12 16:48:54'),
(226, 27, 18, 'like', 'update', 37, '2015-12-12 16:48:57'),
(227, 27, 18, 'like', 'update', 37, '2015-12-12 16:48:59'),
(228, 27, 18, 'like', 'update', 37, '2015-12-12 16:49:02'),
(229, 27, 18, 'like', 'update', 37, '2015-12-12 16:50:51'),
(230, 27, 18, 'like', 'update', 37, '2015-12-12 16:50:58'),
(231, 27, 18, 'like', 'update', 37, '2015-12-12 16:51:03'),
(232, 27, 18, 'like', 'update', 37, '2015-12-12 16:51:08'),
(233, 27, 18, 'like', 'update', 37, '2015-12-12 16:51:15'),
(234, 27, 18, 'like', 'update', 37, '2015-12-12 16:51:21'),
(235, 27, 18, 'like', 'update', 37, '2015-12-12 16:51:27'),
(236, 27, 18, 'like', 'update', 37, '2015-12-12 16:51:32'),
(237, 27, 18, 'like', 'update', 37, '2015-12-12 16:51:37'),
(238, 27, 18, 'like', 'update', 37, '2015-12-12 16:51:45'),
(239, 27, 18, 'like', 'update', 37, '2015-12-12 16:51:50'),
(240, 27, 18, 'like', 'update', 37, '2015-12-12 16:51:56'),
(241, 27, 18, 'like', 'update', 37, '2015-12-12 16:52:01'),
(242, 27, 18, 'like', 'update', 37, '2015-12-12 16:52:06'),
(243, 27, 18, 'like', 'update', 37, '2015-12-12 17:15:16'),
(244, 27, 18, 'like', 'update', 37, '2015-12-12 17:15:21'),
(245, 27, 18, 'like', 'update', 37, '2015-12-12 17:15:27'),
(246, 27, 18, 'like', 'update', 37, '2015-12-12 17:15:32'),
(247, 27, 18, 'like', 'update', 37, '2015-12-12 17:15:37'),
(248, 27, 18, 'like', 'update', 37, '2015-12-12 17:15:42'),
(249, 27, 18, 'like', 'update', 37, '2015-12-12 17:15:47'),
(250, 27, 18, 'like', 'update', 37, '2015-12-12 17:15:57'),
(251, 27, 18, 'like', 'update', 37, '2015-12-12 17:16:02'),
(252, 27, 18, 'like', 'update', 37, '2015-12-12 17:16:07'),
(253, 27, 18, 'like', 'update', 37, '2015-12-12 17:39:47'),
(254, 27, 18, 'like', 'update', 37, '2015-12-12 17:39:55'),
(255, 27, 18, 'like', 'update', 37, '2015-12-12 17:40:08'),
(256, 27, 18, 'like', 'update', 37, '2015-12-12 17:40:14'),
(257, 27, 18, 'like', 'update', 37, '2015-12-12 17:40:18'),
(258, 27, 18, 'like', 'update', 37, '2015-12-12 17:40:24'),
(259, 27, 18, 'like', 'update', 37, '2015-12-12 17:40:29'),
(260, 17, 18, 'like', 'post', 8, '2015-12-13 02:02:39'),
(261, 17, 18, 'like', 'post', 8, '2015-12-13 02:02:44'),
(262, 17, 18, 'like', 'post', 8, '2015-12-13 02:02:49'),
(263, 17, 18, 'like', 'post', 8, '2015-12-13 02:02:54'),
(264, 17, 18, 'like', 'post', 8, '2015-12-13 02:02:59'),
(265, 17, 18, 'like', 'post', 8, '2015-12-13 02:03:04'),
(266, 17, 18, 'like', 'post', 8, '2015-12-13 02:03:17'),
(267, 17, 18, 'like', 'post', 8, '2015-12-13 02:03:22'),
(268, 17, 18, 'like', 'post', 8, '2015-12-13 02:03:27'),
(269, 17, 18, 'like', 'post', 8, '2015-12-13 02:03:32'),
(270, 17, 18, 'like', 'post', 8, '2015-12-13 02:03:37'),
(271, 17, 18, 'like', 'post', 8, '2015-12-13 02:03:42'),
(272, 17, 18, 'like', 'post', 8, '2015-12-13 02:03:47'),
(273, 17, 18, 'like', 'post', 8, '2015-12-13 02:03:52'),
(274, 17, 18, 'like', 'post', 8, '2015-12-13 02:03:58'),
(275, 17, 18, 'like', 'post', 8, '2015-12-13 02:04:03'),
(276, 17, 18, 'like', 'post', 8, '2015-12-13 02:04:08'),
(277, 17, 18, 'like', 'post', 8, '2015-12-13 02:04:11'),
(278, 17, 18, 'like', 'post', 8, '2015-12-13 02:04:16'),
(279, 17, 18, 'like', 'post', 8, '2015-12-13 02:04:21'),
(280, 17, 18, 'like', 'post', 8, '2015-12-13 02:04:26'),
(281, 17, 18, 'like', 'post', 8, '2015-12-13 02:04:31'),
(282, 17, 18, 'like', 'post', 8, '2015-12-13 02:04:39'),
(283, 17, 18, 'like', 'post', 8, '2015-12-13 02:04:45'),
(284, 17, 18, 'like', 'post', 8, '2015-12-13 02:04:50'),
(285, 17, 18, 'like', 'post', 8, '2015-12-13 02:04:55'),
(286, 17, 18, 'like', 'post', 8, '2015-12-13 02:05:00'),
(287, 17, 18, 'like', 'post', 8, '2015-12-13 02:05:06'),
(288, 17, 18, 'like', 'post', 8, '2015-12-13 02:05:11'),
(289, 17, 18, 'like', 'post', 8, '2015-12-13 02:05:14'),
(290, 17, 18, 'like', 'post', 8, '2015-12-13 02:05:37'),
(291, 17, 18, 'like', 'post', 8, '2015-12-13 02:05:42'),
(292, 17, 18, 'like', 'post', 8, '2015-12-13 02:05:47'),
(293, 17, 18, 'like', 'post', 8, '2015-12-13 02:05:52'),
(294, 10, 18, 'follow', 'request', 54, '2015-12-13 02:15:45'),
(295, 17, 18, 'like', 'post', 8, '2015-12-13 02:18:07'),
(296, 17, 18, 'like', 'post', 8, '2015-12-13 02:18:12'),
(297, 17, 18, 'like', 'post', 8, '2015-12-13 02:18:16'),
(298, 10, 18, 'follow', 'request', 54, '2015-12-13 02:18:53'),
(299, 10, 18, 'follow', 'request', 54, '2015-12-13 02:18:56'),
(300, 10, 18, 'follow', 'request', 54, '2015-12-13 02:18:58'),
(301, 10, 18, 'follow', 'request', 54, '2015-12-13 02:19:01'),
(302, 27, 18, 'comment', 'update', 37, '2015-12-13 02:59:00'),
(303, 27, 18, 'like', 'update', 37, '2015-12-13 02:59:02'),
(304, 18, 31, 'like', 'update', 38, '2015-12-13 06:09:24'),
(305, 18, 31, 'like', 'update', 38, '2015-12-13 06:09:30'),
(306, 18, 31, 'comment', 'update', 38, '2015-12-13 06:09:43'),
(307, 18, 31, 'like', 'post', 15, '2015-12-13 06:12:02'),
(308, 18, 31, 'comment', 'post', 15, '2015-12-13 06:12:39'),
(309, 7, 31, 'follow', 'request', 53, '2015-12-13 06:13:32'),
(310, 18, 31, 'follow', 'request', 55, '2015-12-13 06:14:49'),
(311, 7, 18, 'request', 'request', 56, '2015-12-13 06:16:28'),
(312, 31, 18, 'request', 'request', 56, '2015-12-13 06:16:28'),
(313, 18, 31, 'comment', 'post', 14, '2015-12-13 06:28:43'),
(314, 7, 31, 'request', 'request', 57, '2015-12-13 06:52:18'),
(315, 10, 31, 'request', 'request', 57, '2015-12-13 06:52:18'),
(316, 17, 31, 'request', 'request', 57, '2015-12-13 06:52:18'),
(317, 18, 31, 'request', 'request', 57, '2015-12-13 06:52:18'),
(318, 31, 18, 'requestResponse', 'request', 57, '2015-12-13 06:53:40'),
(319, 31, 18, 'follow', 'request', 57, '2015-12-29 07:10:49'),
(320, 31, 18, 'follow', 'request', 57, '2015-12-29 07:10:53'),
(321, 7, 32, 'request', 'request', 58, '2015-12-29 12:19:30'),
(322, 10, 32, 'request', 'request', 58, '2015-12-29 12:19:30'),
(323, 17, 32, 'request', 'request', 58, '2015-12-29 12:19:30'),
(324, 18, 32, 'request', 'request', 58, '2015-12-29 12:19:30'),
(325, 32, 18, 'follow', 'request', 58, '2015-12-29 12:20:35'),
(326, 31, 18, 'follow', 'request', 57, '2015-12-30 17:17:20'),
(327, 31, 10, 'follow', 'request', 57, '2016-01-03 07:07:57'),
(328, 18, 10, 'like', 'update', 39, '2016-01-03 07:09:00'),
(329, 18, 10, 'like', 'post', 15, '2016-01-03 07:10:31'),
(330, 18, 34, 'like', 'update', 38, '2016-01-08 05:46:23'),
(331, 18, 34, 'comment', 'update', 38, '2016-01-08 05:47:33'),
(332, 7, 34, 'request', 'request', 59, '2016-01-08 05:48:18'),
(333, 10, 34, 'request', 'request', 59, '2016-01-08 05:48:18'),
(334, 17, 34, 'request', 'request', 59, '2016-01-08 05:48:18'),
(335, 18, 34, 'request', 'request', 59, '2016-01-08 05:48:18'),
(336, 31, 34, 'request', 'request', 59, '2016-01-08 05:48:18'),
(337, 34, 10, 'follow', 'request', 59, '2016-01-08 05:49:54'),
(338, 34, 10, 'requestResponse', 'request', 59, '2016-01-08 05:51:35'),
(339, 10, 10, 'followResponse', 'request', 59, '2016-01-08 05:51:36'),
(340, 10, 34, 'request', 'request', 60, '2016-01-08 05:54:04'),
(341, 7, 34, 'request', 'request', 60, '2016-01-08 05:54:04'),
(342, 31, 34, 'request', 'request', 60, '2016-01-08 05:54:04'),
(343, 34, 10, 'follow', 'request', 60, '2016-01-08 05:55:19'),
(344, 34, 10, 'requestResponse', 'request', 60, '2016-01-08 05:55:44'),
(345, 10, 34, 'like', 'update', 41, '2016-01-08 05:56:22'),
(346, 10, 34, 'comment', 'update', 40, '2016-01-08 05:58:18'),
(347, 7, 10, 'request', 'request', 61, '2016-01-08 06:13:44'),
(348, 17, 10, 'request', 'request', 61, '2016-01-08 06:13:44'),
(349, 18, 10, 'request', 'request', 61, '2016-01-08 06:13:44'),
(350, 31, 10, 'request', 'request', 61, '2016-01-08 06:13:44'),
(351, 34, 10, 'request', 'request', 61, '2016-01-08 06:13:44'),
(352, 10, 34, 'requestResponse', 'request', 61, '2016-01-08 06:15:35'),
(353, 34, 10, 'like', 'update', 42, '2016-01-08 06:16:07'),
(354, 7, 34, 'request', 'request', 62, '2016-01-08 06:26:17'),
(355, 10, 34, 'request', 'request', 62, '2016-01-08 06:26:17'),
(356, 17, 34, 'request', 'request', 62, '2016-01-08 06:26:17'),
(357, 18, 34, 'request', 'request', 62, '2016-01-08 06:26:17'),
(358, 31, 34, 'request', 'request', 62, '2016-01-08 06:26:17'),
(359, 34, 10, 'requestResponse', 'request', 62, '2016-01-08 06:27:48'),
(360, 10, 34, 'like', 'update', 43, '2016-01-08 06:28:23'),
(361, 7, 10, 'request', 'request', 63, '2016-01-08 06:51:08'),
(362, 17, 10, 'request', 'request', 63, '2016-01-08 06:51:08'),
(363, 18, 10, 'request', 'request', 63, '2016-01-08 06:51:08'),
(364, 31, 10, 'request', 'request', 63, '2016-01-08 06:51:08'),
(365, 34, 10, 'request', 'request', 63, '2016-01-08 06:51:08'),
(366, 10, 34, 'follow', 'request', 63, '2016-01-08 06:51:42'),
(367, 10, 34, 'requestResponse', 'request', 63, '2016-01-08 06:52:14'),
(368, 34, 10, 'like', 'update', 44, '2016-01-08 06:54:35'),
(369, 7, 10, 'request', 'request', 64, '2016-01-08 07:08:59'),
(370, 17, 10, 'request', 'request', 64, '2016-01-08 07:08:59'),
(371, 18, 10, 'request', 'request', 64, '2016-01-08 07:08:59'),
(372, 31, 10, 'request', 'request', 64, '2016-01-08 07:08:59'),
(373, 34, 10, 'request', 'request', 64, '2016-01-08 07:08:59'),
(374, 10, 34, 'follow', 'request', 64, '2016-01-08 07:09:35'),
(375, 10, 34, 'requestResponse', 'request', 64, '2016-01-08 07:10:01'),
(376, 34, 10, 'like', 'update', 45, '2016-01-08 07:10:35'),
(377, 34, 10, 'comment', 'update', 45, '2016-01-08 07:11:20');

-- --------------------------------------------------------

--
-- Table structure for table `post`
--

CREATE TABLE IF NOT EXISTS `post` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` enum('discussion','announcement') NOT NULL,
  `description` text NOT NULL,
  `locationId` int(11) DEFAULT NULL,
  `posterId` int(11) NOT NULL,
  `likeCount` int(11) DEFAULT '0',
  `dislikeCount` int(11) DEFAULT '0',
  `title` varchar(32) DEFAULT NULL,
  `source` varchar(60) DEFAULT NULL,
  `timeOfPost` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `id` (`id`,`locationId`,`posterId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=16 ;

--
-- Dumping data for table `post`
--

INSERT INTO `post` (`id`, `type`, `description`, `locationId`, `posterId`, `likeCount`, `dislikeCount`, `title`, `source`, `timeOfPost`) VALUES
(1, 'discussion', 'Saw a kid driving a car at Dhanmondi today. La hawla wa laa quwwata illa billah.', 3, 14, 0, 0, NULL, NULL, '2015-11-12 02:34:46'),
(3, 'discussion', 'Shahbag should be awarded for causing the most noise pollution in the world -_-', 2, 10, 0, 0, NULL, NULL, '2015-11-12 02:40:09'),
(4, 'discussion', 'Someone beat up a bus conductor at Science Lab today. Baaje obostha', 3, 7, 0, 0, NULL, NULL, '2015-11-12 02:42:17'),
(5, 'discussion', 'Not sure whether it''s a residential area or a commercial one :@', 1, 7, 0, 0, NULL, NULL, '2015-11-12 02:43:03'),
(6, 'announcement', 'Road closed off for PM movement from 5-9 pm tomorrow.', 1, 7, 0, 0, NULL, NULL, '2015-11-12 03:09:13'),
(7, 'announcement', 'The moncho is set, folks. 6am-9am blockade at Shahbag tomorrow.', 2, 10, 0, 1, 'Blockade at Shahbag', 'www.thedailystar.net', '2015-11-12 03:13:26'),
(8, 'discussion', 'Test post', 1, 17, 1, 1, NULL, NULL, '2015-11-12 04:21:16'),
(10, 'discussion', 'metro rail ki edik diye jabe naki?', 2, 18, 0, 0, NULL, NULL, '2015-11-23 17:32:09'),
(11, 'announcement', 'Kalke sakib sir class nibe. What a surprise.', 0, 18, 0, 0, NULL, NULL, '2015-12-09 17:54:18'),
(13, 'discussion', 'What joy have I found in thy traffic-less roads!', 0, 18, 0, 1, NULL, NULL, '2015-12-13 03:38:59'),
(14, 'discussion', 'beautiful day :D', 0, 18, 0, 2, NULL, NULL, '2015-12-13 03:43:44'),
(15, 'discussion', 'Where have the people of this city gone?!', 0, 18, 1, 1, NULL, NULL, '2015-12-13 03:51:28');

-- --------------------------------------------------------

--
-- Table structure for table `post_commenteruser`
--

CREATE TABLE IF NOT EXISTS `post_commenteruser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `postId` int(11) NOT NULL,
  `commenterId` int(11) NOT NULL,
  `commentText` text NOT NULL,
  `timeOfComment` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `postId` (`postId`,`commenterId`),
  KEY `commenterId` (`commenterId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=16 ;

--
-- Dumping data for table `post_commenteruser`
--

INSERT INTO `post_commenteruser` (`id`, `postId`, `commenterId`, `commentText`, `timeOfComment`) VALUES
(1, 3, 17, '0', '2015-12-09 13:11:00'),
(2, 10, 18, 'test', '2015-12-09 13:27:01'),
(3, 7, 18, 'test', '2015-12-09 13:27:21'),
(4, 7, 18, '', '2015-12-09 13:34:01'),
(5, 7, 18, 'hi guys', '2015-12-09 13:34:15'),
(6, 8, 18, 'hu', '2015-12-09 13:37:17'),
(8, 10, 10, 'na bhai, oidik diye jabe :P', '2015-12-09 17:29:04'),
(9, 10, 7, 'ki bhai shob, edik na oidik? ;)', '2015-12-09 17:37:54'),
(10, 10, 10, 'uff, eto faltu kotha je bole manush', '2015-12-09 17:46:44'),
(11, 6, 10, 'uff, eto faltu kotha je bole manush', '2015-12-09 17:52:37'),
(12, 6, 10, 'uff, eto faltu kotha je bole manush', '2015-12-09 17:52:40'),
(13, 11, 10, 'faizlami''r ekta kima ase. Shei kima khaite khub e moja.', '2015-12-09 17:55:27'),
(14, 15, 31, 'ki', '2015-12-13 06:12:39'),
(15, 14, 31, 'i agree!', '2015-12-13 06:28:43');

-- --------------------------------------------------------

--
-- Table structure for table `post_voteruser`
--

CREATE TABLE IF NOT EXISTS `post_voteruser` (
  `postId` int(11) DEFAULT NULL,
  `userId` int(11) DEFAULT NULL,
  `voteType` enum('like','dislike') DEFAULT NULL,
  `timeOfVote` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `postId` (`postId`,`userId`),
  KEY `userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `post_voteruser`
--

INSERT INTO `post_voteruser` (`postId`, `userId`, `voteType`, `timeOfVote`) VALUES
(7, 18, 'dislike', '2015-12-09 06:53:40'),
(8, 18, 'dislike', '2015-12-13 02:18:15'),
(8, 18, 'like', '2015-12-13 02:18:16'),
(15, 18, 'dislike', '2015-12-13 05:02:17'),
(15, 31, 'like', '2015-12-13 06:12:02'),
(13, 18, 'dislike', '2015-12-30 17:14:32'),
(14, 18, 'dislike', '2015-12-30 17:16:47'),
(14, 10, 'dislike', '2016-01-03 07:11:19');

-- --------------------------------------------------------

--
-- Table structure for table `request`
--

CREATE TABLE IF NOT EXISTS `request` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `locationIdFrom` int(11) NOT NULL,
  `locationIdTo` int(11) NOT NULL,
  `description` text NOT NULL,
  `requesterId` int(11) NOT NULL,
  `followerCount` int(11) NOT NULL DEFAULT '0',
  `timeOfRequest` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('active','answered','expired') DEFAULT 'active',
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `id_2` (`id`,`locationIdFrom`,`locationIdTo`,`requesterId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=65 ;

--
-- Dumping data for table `request`
--

INSERT INTO `request` (`id`, `locationIdFrom`, `locationIdTo`, `description`, `requesterId`, `followerCount`, `timeOfRequest`, `status`) VALUES
(1, 1, 3, 'first requester ever!', 17, 0, '2015-11-12 05:56:01', 'active'),
(2, 4, 2, 'Please let me know the current situation! Very urgent!', 7, 0, '2015-11-12 06:03:05', 'active'),
(3, 5, 2, '', 7, 0, '2015-11-12 06:18:58', 'active'),
(4, 2, 1, 'first request from app!', 18, 0, '2015-11-14 06:37:31', 'active'),
(5, 3, 4, 'Testing double line input. Also testing textCapSentences. Testing double line input. Also testing textCapSentences.', 18, 0, '2015-11-14 06:48:56', 'active'),
(6, 2, 5, 'Very urgent', 10, 0, '2015-11-14 07:16:43', 'active'),
(7, 2, 3, 'New request from class', 18, 0, '2015-11-15 05:50:22', 'active'),
(8, 1, 2, 'Jam kemon?', 18, 0, '2015-11-23 08:51:57', 'active'),
(10, 4, 5, 'Testing last state memory', 18, 0, '2015-11-23 09:16:10', 'active'),
(17, 3, 1, 'Test#4 for location followers', 18, 0, '2015-11-25 05:53:16', 'active'),
(40, 1, 3, 'Test #5 for notification of location followers', 18, 0, '2015-11-25 08:37:54', 'active'),
(47, 1, 3, 'Keu ektu update den please ', 18, 0, '2015-11-25 09:38:43', 'active'),
(48, 2, 1, 'Dhanmondi te ki obostha bhai?', 18, 0, '2015-11-25 09:47:09', 'active'),
(50, 1, 3, 'Test', 18, 2, '2015-11-26 06:57:02', 'active'),
(51, 2, 3, 'Can i follow?', 18, 0, '2015-11-28 12:31:39', 'active'),
(52, 2, 3, 'Request', 18, 1, '2015-11-29 06:31:43', 'active'),
(53, 5, 3, 'testing notifAboutId from Postman', 7, 1, '2015-12-09 13:40:30', 'active'),
(54, 5, 3, 'testing notifAboutId from Postman 2', 10, 0, '2015-12-09 13:44:00', 'active'),
(55, 3, 2, 'Request for update', 18, 1, '2015-12-13 06:14:35', 'active'),
(56, 5, 3, 'Situation aT AZI!PUR?', 18, 0, '2015-12-13 06:16:28', 'active'),
(57, 1, 3, 'ki', 31, 0, '2015-12-13 06:52:17', 'active'),
(58, 3, 1, 'Till Rapa Plaza', 32, 1, '2015-12-29 12:19:30', 'active'),
(59, 5, 1, '', 34, 1, '2016-01-08 05:48:18', 'active'),
(60, 3, 5, '', 34, 0, '2016-01-08 05:54:04', 'active'),
(61, 5, 1, '', 10, 0, '2016-01-08 06:13:44', 'active'),
(62, 5, 1, '', 34, 0, '2016-01-08 06:26:17', 'active'),
(63, 5, 1, '', 10, 0, '2016-01-08 06:51:08', 'active'),
(64, 5, 1, '', 10, 0, '2016-01-08 07:08:59', 'active');

-- --------------------------------------------------------

--
-- Table structure for table `request_followeruser`
--

CREATE TABLE IF NOT EXISTS `request_followeruser` (
  `requestId` int(11) NOT NULL,
  `userId` int(11) NOT NULL,
  `timeOfFollowing` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `requestId` (`requestId`),
  KEY `userId` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `request_followeruser`
--

INSERT INTO `request_followeruser` (`requestId`, `userId`, `timeOfFollowing`) VALUES
(50, 10, '2015-11-28 04:16:33'),
(50, 17, '2015-11-28 04:16:46'),
(52, 10, '2015-12-09 18:12:01'),
(53, 31, '2015-12-13 06:13:32'),
(55, 31, '2015-12-13 06:14:48'),
(58, 18, '2015-12-29 12:20:34'),
(59, 10, '2016-01-08 05:49:53');

-- --------------------------------------------------------

--
-- Table structure for table `update`
--

CREATE TABLE IF NOT EXISTS `update` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `locationIdFrom` int(11) NOT NULL,
  `locationIdTo` int(11) NOT NULL,
  `estTimeToCross` int(11) DEFAULT NULL,
  `situation` enum('Free','Mild','Moderate','Extreme','Gridlock') NOT NULL,
  `description` text,
  `timeOfSituation` timestamp NULL DEFAULT NULL,
  `updaterId` int(11) NOT NULL,
  `likeCount` int(11) NOT NULL DEFAULT '0',
  `dislikeCount` int(11) NOT NULL DEFAULT '0',
  `requestId` int(11) DEFAULT NULL,
  `timeOfUpdate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `id` (`id`),
  KEY `id_2` (`id`,`locationIdFrom`,`locationIdTo`,`updaterId`,`requestId`),
  KEY `id_3` (`id`,`locationIdFrom`,`locationIdTo`,`updaterId`,`requestId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=46 ;

--
-- Dumping data for table `update`
--

INSERT INTO `update` (`id`, `locationIdFrom`, `locationIdTo`, `estTimeToCross`, `situation`, `description`, `timeOfSituation`, `updaterId`, `likeCount`, `dislikeCount`, `requestId`, `timeOfUpdate`) VALUES
(4, 1, 2, 20, 'Mild', 'just a test', '2015-02-02 04:42:00', 7, 1, 0, NULL, '2015-11-11 16:49:18'),
(5, 2, 1, 60, 'Extreme', 'second test', '2015-11-11 04:51:00', 10, 1, 0, NULL, '2015-11-11 16:52:02'),
(7, 3, 1, 120, 'Gridlock', 'Allah bachao', '2015-11-11 17:07:00', 10, 1, 0, NULL, '2015-11-11 17:07:09'),
(8, 2, 3, 10, 'Mild', 'hello', '2015-11-13 09:19:27', 18, 1, 0, NULL, '2015-11-13 09:39:15'),
(9, 5, 4, 5, 'Free', 'wireless update :D', '2015-11-13 11:56:31', 18, 1, 0, NULL, '2015-11-13 11:56:18'),
(10, 3, 3, 0, 'Free', 'here', '2015-11-13 17:54:19', 18, 1, 0, NULL, '2015-11-13 17:54:06'),
(11, 3, 3, 0, 'Free', 'here', '2015-11-13 17:54:20', 18, 1, 0, NULL, '2015-11-13 17:54:08'),
(12, 5, 1, 45, 'Extreme', 'ki jam re baba', '2015-11-14 00:02:50', 18, 1, 0, NULL, '2015-11-14 00:22:38'),
(13, 3, 3, 10, 'Free', 'hi', '2015-11-14 18:28:27', 18, 1, 0, NULL, '2015-11-14 18:28:26'),
(14, 4, 3, 80, 'Gridlock', 'khubi jam', '2015-11-15 01:26:18', 18, 1, 0, NULL, '2015-11-15 01:36:16'),
(15, 3, 3, 30, 'Free', 'Ken ber hoilam -_-', '2015-11-15 05:43:20', 18, 1, 0, NULL, '2015-11-15 05:43:06'),
(16, 2, 1, 30, 'Mild', 'Fixed sign up error, alhamdulillah!', '2015-11-23 08:33:26', 24, 1, 0, NULL, '2015-11-23 08:41:22'),
(17, 4, 1, 10, 'Free', 'Mojai lagse! ðŸ˜', '2015-11-23 08:41:46', 18, 1, 0, NULL, '2015-11-23 08:49:41'),
(18, 5, 1, 60, 'Extreme', 'Testing notifications ðŸ˜Ž', '2015-11-24 05:42:39', 18, 1, 0, NULL, '2015-11-24 05:50:35'),
(20, 5, 4, 5, 'Moderate', 'valoi asey', '2015-11-26 04:23:14', 18, 1, 0, NULL, '2015-11-26 04:33:10'),
(21, 5, 2, 15, 'Mild', 'halka jaam', '2015-11-26 04:24:22', 18, 1, 0, NULL, '2015-11-26 04:34:17'),
(26, 1, 3, 20, 'Free', 'testing notif for req followers', '2015-11-28 04:44:31', 18, 1, 0, 50, '2015-11-28 04:42:25'),
(30, 2, 1, 15, 'Free', 'gulir begðŸ˜', '2015-11-29 00:52:26', 18, 1, 0, NULL, '2015-11-29 00:52:20'),
(31, 1, 3, 12, 'Mild', 'uttor dilam', '2015-11-29 00:45:07', 18, 0, 1, NULL, '2015-11-29 00:55:00'),
(32, 5, 3, 20, 'Free', 'extra', '2015-11-29 06:13:19', 18, 1, 0, NULL, '2015-11-29 06:31:12'),
(33, 2, 3, 20, 'Free', 'response', '2015-11-29 06:34:02', 7, 1, 0, 52, '2015-11-29 06:33:56'),
(34, 3, 3, 0, 'Free', 'test', '0000-00-00 00:00:00', 18, 0, 0, NULL, '2015-11-30 12:45:11'),
(35, 2, 3, 20, 'Free', 'testing notif', '2015-12-01 18:00:00', 18, 0, 0, 52, '2015-12-01 09:36:50'),
(36, 5, 1, 20, 'Mild', 'This shall be the longest update description ever posted in this app but this is just to check how the description box handles long descriptions.', '0000-00-00 00:00:00', 18, 1, 0, NULL, '2015-12-08 10:55:33'),
(37, 4, 1, 600, 'Extreme', 'dont go there ', '0000-00-00 00:00:00', 27, 1, 0, NULL, '2015-12-12 14:51:35'),
(38, 3, 5, 5, 'Mild', 'free as wind', '0000-00-00 00:00:00', 18, 2, 0, NULL, '2015-12-13 04:39:17'),
(39, 1, 3, 50, 'Gridlock', 'ki jam', '0000-00-00 00:00:00', 18, 0, 0, 57, '2015-12-13 06:53:40'),
(40, 5, 1, 30, 'Moderate', 'Lot better than yesterday ðŸ˜’', '0000-00-00 00:00:00', 10, 0, 0, 59, '2016-01-08 05:51:35'),
(41, 3, 5, 20, 'Moderate', '', '0000-00-00 00:00:00', 10, 1, 0, 60, '2016-01-08 05:55:44'),
(42, 5, 1, 50, 'Gridlock', 'edikey ashi na', '0000-00-00 00:00:00', 34, 0, 1, 61, '2016-01-08 06:15:35'),
(43, 5, 1, 10, 'Mild', 'rasya gala', '0000-00-00 00:00:00', 10, 1, 0, 62, '2016-01-08 06:27:48'),
(44, 5, 1, 20, 'Extreme', 'baaje obostha', '0000-00-00 00:00:00', 34, 0, 0, 63, '2016-01-08 06:52:14'),
(45, 5, 1, 20, 'Free', '', '0000-00-00 00:00:00', 34, 0, 1, 64, '2016-01-08 07:10:01');

-- --------------------------------------------------------

--
-- Table structure for table `update_commenteruser`
--

CREATE TABLE IF NOT EXISTS `update_commenteruser` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `updateId` int(11) NOT NULL,
  `commenterId` int(11) NOT NULL,
  `commentText` text NOT NULL,
  `timeOfComment` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `updateId` (`updateId`,`commenterId`),
  KEY `commenterId` (`commenterId`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=40 ;

--
-- Dumping data for table `update_commenteruser`
--

INSERT INTO `update_commenteruser` (`id`, `updateId`, `commenterId`, `commentText`, `timeOfComment`) VALUES
(4, 7, 18, 'testing new comment from postman!', '2015-12-02 11:14:34'),
(6, 7, 17, 'testing comment response from postman!', '2015-12-02 11:20:44'),
(7, 7, 10, 'testing own comment response from postman!', '2015-12-02 11:21:43'),
(8, 35, 18, 'first database add', '2015-12-02 16:23:03'),
(9, 35, 18, 'second', '2015-12-02 16:23:41'),
(10, 35, 18, 'third', '2015-12-02 17:28:35'),
(11, 35, 18, 'fourth', '2015-12-02 17:29:09'),
(12, 35, 18, 'fifth', '2015-12-02 17:29:37'),
(13, 35, 18, 'sixth', '2015-12-02 17:30:09'),
(14, 35, 18, 'seven', '2015-12-02 17:30:20'),
(15, 34, 18, '', '2015-12-02 17:30:36'),
(16, 34, 18, 'th', '2015-12-02 17:30:57'),
(17, 32, 18, 's', '2015-12-02 17:34:10'),
(18, 32, 18, 'two', '2015-12-02 17:38:29'),
(19, 32, 18, 'three', '2015-12-02 17:38:52'),
(20, 32, 18, 'four', '2015-12-02 17:38:58'),
(21, 32, 18, 'five', '2015-12-02 17:39:04'),
(22, 32, 18, 'six', '2015-12-02 17:39:10'),
(23, 14, 18, 'hi', '2015-12-02 18:22:07'),
(24, 14, 18, 'hello', '2015-12-02 18:22:16'),
(25, 14, 18, 'beshi jam', '2015-12-02 18:24:42'),
(26, 36, 18, 'hi', '2015-12-09 11:40:40'),
(27, 33, 18, 'testing notif', '2015-12-09 17:04:38'),
(28, 14, 18, 'testing scrollview', '2015-12-10 06:50:39'),
(29, 14, 18, 'testing comments in a way that has never been done before in the history of testing or commenting. ', '2015-12-10 08:51:18'),
(30, 14, 18, 'testing never ends!', '2015-12-10 08:59:05'),
(31, 14, 18, 'and here goes the final test for the day..', '2015-12-10 09:15:39'),
(32, 14, 18, 'this is the last, i promise!', '2015-12-10 09:25:28'),
(33, 37, 18, 'vdhkeje', '2015-12-13 02:59:00'),
(34, 38, 31, 'ok', '2015-12-13 06:09:43'),
(35, 39, 18, 'are bachi na', '2015-12-13 06:54:26'),
(36, 38, 34, 'not now. ', '2016-01-08 05:47:33'),
(37, 40, 34, 'yes', '2016-01-08 05:58:18'),
(38, 43, 10, 'thakl uoi', '2016-01-08 06:28:47'),
(39, 45, 10, 'college', '2016-01-08 07:11:20');

-- --------------------------------------------------------

--
-- Table structure for table `update_voteruser`
--

CREATE TABLE IF NOT EXISTS `update_voteruser` (
  `userId` int(11) NOT NULL,
  `updateId` int(11) NOT NULL,
  `voteType` enum('like','dislike') NOT NULL,
  `timeOfVote` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY `unique_row` (`userId`,`updateId`),
  KEY `updateId` (`updateId`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `update_voteruser`
--

INSERT INTO `update_voteruser` (`userId`, `updateId`, `voteType`, `timeOfVote`) VALUES
(10, 42, 'dislike', '2016-01-08 06:52:49'),
(10, 45, 'dislike', '2016-01-08 07:10:38'),
(18, 31, 'dislike', '2015-12-10 09:39:36'),
(18, 33, 'like', '2015-12-01 09:34:57'),
(18, 37, 'like', '2015-12-13 02:59:02'),
(27, 4, 'like', '2015-12-12 14:52:40'),
(27, 5, 'like', '2015-12-12 14:52:39'),
(27, 7, 'like', '2015-12-12 14:52:36'),
(27, 8, 'like', '2015-12-12 14:52:35'),
(27, 9, 'like', '2015-12-12 14:52:34'),
(27, 10, 'like', '2015-12-12 14:52:32'),
(27, 11, 'like', '2015-12-12 14:52:31'),
(27, 12, 'like', '2015-12-12 14:52:27'),
(27, 13, 'like', '2015-12-12 14:52:26'),
(27, 14, 'like', '2015-12-12 14:52:24'),
(27, 15, 'like', '2015-12-12 14:52:23'),
(27, 16, 'like', '2015-12-12 14:52:21'),
(27, 17, 'like', '2015-12-12 14:52:20'),
(27, 18, 'like', '2015-12-12 14:52:19'),
(27, 20, 'like', '2015-12-12 14:52:17'),
(27, 21, 'like', '2015-12-12 14:52:16'),
(27, 26, 'like', '2015-12-12 14:52:15'),
(27, 30, 'like', '2015-12-12 14:52:13'),
(27, 32, 'like', '2015-12-12 14:57:35'),
(27, 36, 'like', '2015-12-12 14:59:22'),
(31, 38, 'like', '2015-12-13 06:09:30'),
(34, 38, 'like', '2016-01-08 05:46:23'),
(34, 41, 'like', '2016-01-08 05:56:22'),
(34, 43, 'like', '2016-01-08 06:28:23');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(32) NOT NULL,
  `gender` enum('male','female') DEFAULT NULL,
  `email` varchar(32) NOT NULL,
  `password_hash` text NOT NULL,
  `api_key` varchar(32) NOT NULL,
  `status` int(1) NOT NULL DEFAULT '1',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `id` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=35 ;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `username`, `gender`, `email`, `password_hash`, `api_key`, `status`, `created_at`) VALUES
(7, 'a', 'male', 'e@gmail.com', '$2a$10$d33ce823b520daa1d0e79ulypDDusfkyKH7gjgC6LJ0svdtj6Amb2', 'a79f36b91bab317efe358fa184f5b112', 1, '2015-11-08 08:07:06'),
(10, 'Shahriar', 'male', 'sskhan11@live.com', '$2a$10$358598254f662ca494509u4XGRKTsDC2HkA2jkagNJp9HJg851zQG', 'f9de3a33787e1bd560f88a5f7005dd71', 1, '2015-11-08 09:33:20'),
(17, 's', 'male', 'sskhan11@lu.vi', '$2a$10$ecc64f3be1b8fd549dde4Oj7slSTLC5LSFi/Yz4SV8AhZdFsizSCi', '9e3c3c2283952d6a7f903f0074124824', 1, '2015-11-08 13:34:32'),
(18, 'Shabab', 'male', 'a@a.aa', '$2a$10$92c2635cf3879d986a34duSjkOILL7GFFf1Rz8q/X5Eg.29mRqnJm', '99dc41c8fa7e95290d1009400fa63192', 1, '2015-11-08 17:42:50'),
(19, 'wasif123', 'male', 'wasif@gmail.com', '$2a$10$df43c8a36778e90b712aeuUVi9W5rddasr9f/0eL4YnSC9.hKikBy', 'c3920f70eeea3d104dfb64eba0aebb06', 1, '2015-11-15 05:46:22'),
(20, 'chk', 'female', 'test@email.com', '$2a$10$c5d0aa8386acaac36325cO.w7/O87yUWaKVX1eIQ0jCQXqXcgChTK', 'fc7ffa83ee4f11b539dfc8090a291b22', 1, '2015-11-23 06:28:06'),
(21, 'chk2', 'male', 'test2@email.com', '$2a$10$8fdb5751ea27e92e0724aeaD2Ofxjlvr3xjWz/VORD42TGDaWsUqW', 'bb95b1480861923246b7c99b7954e308', 1, '2015-11-23 06:36:01'),
(22, 'chk3', 'female', 'test3@email.com', '$2a$10$f7d3a99dcc802ea41ca50eMtua7Xm9iu6fSDVekTKPpk.nX9S5NmS', '8ba2a678a39b62a6b29eb3a1a21ada83', 1, '2015-11-23 08:16:10'),
(23, 'chk4', 'male', 'test4@email.com', '$2a$10$fa14b924291902474bcc7O16m9OWSr2AQ/1EvjyAW1bXx0qE4WU0y', 'c40af200fd2ba1437eede5dcdca81fa0', 1, '2015-11-23 08:30:52'),
(24, 'chk5', 'female', 'test5@email.com', '$2a$10$5d8bb25b442e2ac2a62b1eWqKCOxUf7WvwQec2En7Jycp8YhS1cti', 'f36e5d3ec21ff3c517bd1e69d4111e43', 1, '2015-11-23 08:40:29'),
(25, 'fgg', 'male', 'dffg@yyu.fgg', '$2a$10$2abb2c4afeb07bd9ca5ffuuW7I.PQo315DVoDDiTdJnf9F61zCP.y', '59857db890370554ff5673ff8860d0fd', 1, '2015-11-25 08:38:44'),
(26, 'chk6', 'male', 'test6@email.com', '$2a$10$ce78202664dd01bc629dcuCqHiDbSe/JwHMBjdBpCjQvEltNGX/lK', 'b953b1467a643dd7372d15e7e3a0cbd8', 1, '2015-11-29 06:05:01'),
(27, 'tahsin24', 'male', 'tarequehasan2@gmail.com', '$2a$10$30f2271eec84f00a6729dOm6s2JuPjTD1deTidwsfglPObsFvjFVW', 'f1cdedff3e7ff7dcea0a431aecc59d19', 1, '2015-12-12 14:49:26'),
(28, 'wasif', 'male', 'wasifkhan@gmail.com', '$2a$10$24a9b02404205971d5995urq5FOjJPWZ9Zei23GovZOb4WQ.cmfxm', '53ac59560450cd5dd5ca26f3bcf362fb', 1, '2015-12-13 03:56:27'),
(29, 'neamul', 'male', 'neamul@gmail.com', '$2a$10$0cf37add7c7d3081316daumqMMb9IPLKWBhdyJr7ikAcic/6pxqo6', '139f14787c9b7dd848fec6645a405361', 1, '2015-12-13 04:02:38'),
(30, 'masud', 'male', 'masud@gmail.com', '$2a$10$07d51abea2a7785ae4f3cOr1idR.tQinZBWUMceSMuTn8KilvzkGC', 'b56affc4b20bec038d6557a4ba5bb6cf', 1, '2015-12-13 04:13:17'),
(31, 'madhusudan', 'male', 'm@m.mm', '$2a$10$76deb5dab72f8537e5ad1uxOzcCS8iGJ5c29rSiQiP9/J8Px9XcIe', '65e734f616db81d4e1d5813ed8e58322', 1, '2015-12-13 06:07:29'),
(32, 'azchow', 'male', 'Chowdhury.azhar@gmail.com', '$2a$10$74748dd436b7a9c71a16fOcO55nm79hmxKLg9PpAKx2Jk3yxsb.qW', '55e888b3f0fc03aafcf3548724f31fe0', 1, '2015-12-29 12:17:33'),
(33, 'rahee', 'male', 'rahee.h2@gmail.com', '$2a$10$5c965e4af53a326dd9d67u4WrVTqEz5oaBCvYJLOWZD0rMqanvHIC', '35b9ad6b62d22dea300035d24594e869', 1, '2015-12-29 12:26:02'),
(34, 'sadman', 'male', 'rahmanwasifur@gmail.com', '$2a$10$63c26cbd99e8a2acf3b68u4sJrRMzya/f9yGZHBt7Pbeoxp.uvvY6', 'f78268ebe2fe7fd15923b93d5389fb0c', 1, '2016-01-08 05:45:54');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `notification`
--
ALTER TABLE `notification`
  ADD CONSTRAINT `notification_ibfk_1` FOREIGN KEY (`notifTo`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `notification_ibfk_2` FOREIGN KEY (`notifFrom`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `post_commenteruser`
--
ALTER TABLE `post_commenteruser`
  ADD CONSTRAINT `post_commenteruser_ibfk_1` FOREIGN KEY (`postId`) REFERENCES `post` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `post_commenteruser_ibfk_2` FOREIGN KEY (`commenterId`) REFERENCES `user` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `post_voteruser`
--
ALTER TABLE `post_voteruser`
  ADD CONSTRAINT `post_voteruser_ibfk_1` FOREIGN KEY (`postId`) REFERENCES `post` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `post_voteruser_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `request_followeruser`
--
ALTER TABLE `request_followeruser`
  ADD CONSTRAINT `request_followeruser_ibfk_1` FOREIGN KEY (`requestId`) REFERENCES `request` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  ADD CONSTRAINT `request_followeruser_ibfk_2` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

--
-- Constraints for table `update_commenteruser`
--
ALTER TABLE `update_commenteruser`
  ADD CONSTRAINT `update_commenteruser_ibfk_1` FOREIGN KEY (`updateId`) REFERENCES `update` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `update_commenteruser_ibfk_2` FOREIGN KEY (`commenterId`) REFERENCES `user` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `update_voteruser`
--
ALTER TABLE `update_voteruser`
  ADD CONSTRAINT `update_voteruser_ibfk_1` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `update_voteruser_ibfk_2` FOREIGN KEY (`updateId`) REFERENCES `update` (`id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
