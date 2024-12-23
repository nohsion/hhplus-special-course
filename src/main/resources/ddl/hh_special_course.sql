CREATE TABLE `user`
(
    `id`         int PRIMARY KEY AUTO_INCREMENT,
    `name`       varchar(10)  NOT NULL COMMENT '사용자 이름',
    `email`      varchar(100) NOT NULL COMMENT '사용자 이메일',
    `created_at` datetime     NOT NULL DEFAULT (CURRENT_TIMESTAMP()),
    `updated_at` datetime     NOT NULL DEFAULT (CURRENT_TIMESTAMP())
);

CREATE TABLE `user_course_enrollment`
(
    `id`         int PRIMARY KEY AUTO_INCREMENT,
    `student_id` int COMMENT '특강 신청한 유저 Id',
    `course_id`  int COMMENT '특강 ID',
    `created_at` datetime NOT NULL DEFAULT (CURRENT_TIMESTAMP())
);

CREATE TABLE `course`
(
    `id`            int PRIMARY KEY AUTO_INCREMENT,
    `instructor_id` int COMMENT '강연자 ID',
    `title`         varchar(100) NOT NULL COMMENT '특강 타이틀',
    `description`   varchar(200) COMMENT '특강 설명',
    `max_students`  int          NOT NULL COMMENT '최대 수강인원',
    `course_date`   char(8)      NOT NULL COMMENT '특강 날짜 e.g. 20241225',
    `created_at`    datetime     NOT NULL DEFAULT (CURRENT_TIMESTAMP()),
    `updated_at`    datetime     NOT NULL DEFAULT (CURRENT_TIMESTAMP())
);

CREATE UNIQUE INDEX `user_course_enrollment_index_0` ON `user_course_enrollment` (`student_id`, `course_id`);

ALTER TABLE `user_course_enrollment`
    ADD FOREIGN KEY (`student_id`) REFERENCES `user` (`id`);

ALTER TABLE `user_course_enrollment`
    ADD FOREIGN KEY (`course_id`) REFERENCES `course` (`id`);

ALTER TABLE `course`
    ADD FOREIGN KEY (`instructor_id`) REFERENCES `user` (`id`);
