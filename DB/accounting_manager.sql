-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 09, 2024 at 01:41 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `accounting_manager`
--

-- --------------------------------------------------------

--
-- Table structure for table `accountant_client`
--

CREATE TABLE `accountant_client` (
  `accountant_id` bigint(20) NOT NULL,
  `client_id` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `document`
--

CREATE TABLE `document` (
  `id` bigint(20) NOT NULL,
  `discriminator` varchar(31) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `creation_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `source_img` mediumblob NOT NULL,
  `detection_img` mediumblob DEFAULT NULL,
  `status` bit(1) NOT NULL,
  `deleted` bit(1) NOT NULL,
  `invoice_number` varchar(255) DEFAULT NULL,
  `invoice_date` datetime(6) DEFAULT NULL,
  `siret_number` varchar(255) DEFAULT NULL,
  `tva_number` varchar(255) DEFAULT NULL,
  `supplier_number` varchar(255) DEFAULT NULL,
  `pages_number` int(11) DEFAULT NULL,
  `discount` double DEFAULT NULL,
  `ht` double DEFAULT NULL,
  `support_tva` double DEFAULT NULL,
  `ttc` double DEFAULT NULL,
  `tva` double DEFAULT NULL,
  `iban` varchar(255) DEFAULT NULL,
  `rib` varchar(255) DEFAULT NULL,
  `bic` varchar(11) DEFAULT NULL,
  `account_number` varchar(11) DEFAULT NULL,
  `bank_statement_date` datetime(6) DEFAULT NULL,
  `period_start_date` date DEFAULT NULL,
  `period_end_date` date DEFAULT NULL,
  `fk_folder` bigint(20) DEFAULT NULL,
  `fk_process_type` bigint(20) DEFAULT NULL,
  `fk_type` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `folder`
--

CREATE TABLE `folder` (
  `id_folder` bigint(20) NOT NULL,
  `creation_date` datetime NOT NULL DEFAULT current_timestamp(),
  `description` varchar(255) NOT NULL,
  `name` varchar(50) NOT NULL,
  `status` bit(1) NOT NULL DEFAULT b'1',
  `favorite` bit(1) NOT NULL DEFAULT b'0',
  `archived` bit(1) NOT NULL DEFAULT b'0',
  `deleted` bit(1) NOT NULL DEFAULT b'0',
  `fk_type` bigint(20) DEFAULT NULL,
  `fk_client` bigint(20) DEFAULT NULL,
  `fk_creator` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `third_party`
--

CREATE TABLE `third_party` (
  `discriminator` varchar(31) NOT NULL,
  `id_third_party` bigint(20) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) NOT NULL,
  `legal_name` varchar(255) DEFAULT NULL,
  `siret_number` varchar(20) DEFAULT NULL,
  `firstname` varchar(40) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `lastname` varchar(40) DEFAULT NULL,
  `nic` varchar(20) DEFAULT NULL,
  `fk_third_party_role` bigint(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `third_party`
--

INSERT INTO `third_party` (`discriminator`, `id_third_party`, `email`, `password`, `phone_number`, `legal_name`, `siret_number`, `firstname`, `gender`, `lastname`, `nic`, `fk_third_party_role`) VALUES
('physical', 1, 'admin@gmail.com', '$2a$10$Ec/9JvaIgnFjVFsnAi/2tuKR50wkJKjKctzd7PNc9PY04..//MqrW', '11111111', NULL, NULL, 'Admin', 'male', 'Account', '11111111', 5),
('physical', 2, 'user@gmail.com', '$2a$10$nP/FY7wrgurqkqvoOdpjX.REpCwf0eOKwGXWjuf2l7qyTScJY7TeO', '22222222', NULL, NULL, 'User', 'male', 'Account', '22222222', 6);

-- --------------------------------------------------------

--
-- Table structure for table `token`
--

CREATE TABLE `token` (
  `creation_date` datetime(6) DEFAULT NULL,
  `expiration_date` datetime(6) DEFAULT NULL,
  `fk_third_party` bigint(20) NOT NULL,
  `fk_token_type` bigint(20) DEFAULT NULL,
  `token_id` bigint(20) NOT NULL,
  `token` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `type`
--

CREATE TABLE `type` (
  `id_type` bigint(20) NOT NULL,
  `code_type` varchar(40) NOT NULL,
  `label` varchar(120) DEFAULT NULL,
  `fk_parent_type` bigint(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Dumping data for table `type`
--

INSERT INTO `type` (`id_type`, `code_type`, `label`, `fk_parent_type`) VALUES
(1, 'ROLE', 'Role parent type', NULL),
(2, 'TOKEN', 'Token parent type', NULL),
(3, 'DOCUMENT_PROCESS', 'Invoice process parent type', NULL),
(4, 'DEFAULT_OCR', 'Default OCR parent type', NULL),
(5, 'ADMIN', 'ADMIN ROLE: Has full access to our application.', 1),
(6, 'ACCOUNTANT', 'ACCOUNTANT ROLE: An accountant who bought our application (He/She can be a client for other accountants).', 1),
(7, 'CLIENT', 'CLIENT ROLE: A client who did not bought our application, he have some access to our app in order to view his invoices.', 1),
(8, 'REFRESH', 'REFRESH TOKEN: Refresh token for getting a new JWT token.', 2),
(9, 'RESET', 'RESET TOKEN: Used for resetting user password.', 2),
(10, 'CONFIRMATION', 'CONFIRMATION TOKEN: Used for confirming and enabling a newly created account.', 2),
(11, 'AUTO', 'AUTO PROCESSED INVOICE: \'All values were detected by our Accounting Engine Module without any modifications.', 3),
(12, 'AUTO_MANUAL', 'All/ some detected values by our Accounting Engine Module were changed by the Accountant.', 3),
(13, 'MANUAL', 'MANUAL PROCESSED INVOICE: No invoice values detected by our Accounting', 3),
(14, 'ABBYY', 'Default OCR', 4),
(15, 'FOLDER_TYPE', 'Type of folder, used to specify which type of documents the folder can store.', NULL),
(16, 'INVOICES', 'Folder used to store invoices.', 15),
(17, 'BANK_STATEMENTS', 'Folder used to store bank statements.', 15);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `accountant_client`
--
ALTER TABLE `accountant_client`
  ADD PRIMARY KEY (`accountant_id`,`client_id`),
  ADD KEY `FK5n5agv6tgv8byrxoxqrrfj6s9` (`client_id`);

--
-- Indexes for table `document`
--
ALTER TABLE `document`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_folder_of_document` (`fk_folder`),
  ADD KEY `fk_type_of_document_process` (`fk_process_type`),
  ADD KEY `fk_type_of_document` (`fk_type`);

--
-- Indexes for table `folder`
--
ALTER TABLE `folder`
  ADD PRIMARY KEY (`id_folder`),
  ADD KEY `fk_client_of_folder` (`fk_client`),
  ADD KEY `fk_creator_of_folder` (`fk_creator`),
  ADD KEY `fk_type_of_folder` (`fk_type`);

--
-- Indexes for table `third_party`
--
ALTER TABLE `third_party`
  ADD PRIMARY KEY (`id_third_party`),
  ADD UNIQUE KEY `UK_dvnxiv6e9esyu4ene2kd9tygf` (`email`),
  ADD UNIQUE KEY `UK_h3wc2pynr41xvaw7h49kr7cju` (`legal_name`),
  ADD UNIQUE KEY `UK_eugscbb0q8p8fkadj8pnx8wpf` (`siret_number`),
  ADD UNIQUE KEY `UK_8n6u0jmot4lt2b8so4f2fnc2i` (`nic`),
  ADD KEY `fk_role_of_third_party` (`fk_third_party_role`) USING BTREE;

--
-- Indexes for table `token`
--
ALTER TABLE `token`
  ADD PRIMARY KEY (`token_id`),
  ADD UNIQUE KEY `UK_nwmt33fup4qyge6xxdbil3100` (`fk_third_party`),
  ADD KEY `fk_type_of_token` (`fk_token_type`);

--
-- Indexes for table `type`
--
ALTER TABLE `type`
  ADD PRIMARY KEY (`id_type`),
  ADD UNIQUE KEY `UKremkjpjbevfxkfw7yp65s50b2` (`code_type`,`fk_parent_type`),
  ADD KEY `fk_parent_type` (`fk_parent_type`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `document`
--
ALTER TABLE `document`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `folder`
--
ALTER TABLE `folder`
  MODIFY `id_folder` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `third_party`
--
ALTER TABLE `third_party`
  MODIFY `id_third_party` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `token`
--
ALTER TABLE `token`
  MODIFY `token_id` bigint(20) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `type`
--
ALTER TABLE `type`
  MODIFY `id_type` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `accountant_client`
--
ALTER TABLE `accountant_client`
  ADD CONSTRAINT `FK5n5agv6tgv8byrxoxqrrfj6s9` FOREIGN KEY (`client_id`) REFERENCES `third_party` (`id_third_party`),
  ADD CONSTRAINT `FK7r7ttpiwdyd3c17kdmiojm9i3` FOREIGN KEY (`accountant_id`) REFERENCES `third_party` (`id_third_party`);

--
-- Constraints for table `document`
--
ALTER TABLE `document`
  ADD CONSTRAINT `fk_folder_of_document` FOREIGN KEY (`fk_folder`) REFERENCES `folder` (`id_folder`),
  ADD CONSTRAINT `fk_type_of_document` FOREIGN KEY (`fk_type`) REFERENCES `type` (`id_type`),
  ADD CONSTRAINT `fk_type_of_document_process` FOREIGN KEY (`fk_process_type`) REFERENCES `type` (`id_type`);

--
-- Constraints for table `folder`
--
ALTER TABLE `folder`
  ADD CONSTRAINT `fk_client_of_folder` FOREIGN KEY (`fk_client`) REFERENCES `third_party` (`id_third_party`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_creator_of_folder` FOREIGN KEY (`fk_creator`) REFERENCES `third_party` (`id_third_party`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_type_of_folder` FOREIGN KEY (`fk_type`) REFERENCES `type` (`id_type`) ON DELETE SET NULL ON UPDATE SET NULL;

--
-- Constraints for table `third_party`
--
ALTER TABLE `third_party`
  ADD CONSTRAINT `fk_role_of_third_party` FOREIGN KEY (`fk_third_party_role`) REFERENCES `type` (`id_type`),
  ADD CONSTRAINT `fk_type_of_client` FOREIGN KEY (`fk_third_party_role`) REFERENCES `type` (`id_type`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `token`
--
ALTER TABLE `token`
  ADD CONSTRAINT `fk_thirdparty_of_token` FOREIGN KEY (`fk_third_party`) REFERENCES `third_party` (`id_third_party`),
  ADD CONSTRAINT `fk_type_of_token` FOREIGN KEY (`fk_token_type`) REFERENCES `type` (`id_type`);

--
-- Constraints for table `type`
--
ALTER TABLE `type`
  ADD CONSTRAINT `fk_parent_type` FOREIGN KEY (`fk_parent_type`) REFERENCES `type` (`id_type`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
