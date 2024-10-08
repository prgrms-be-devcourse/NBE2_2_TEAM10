-- INSERT INTO 멤버값 먼저 할당

INSERT IGNORE INTO member (id, address, nickname, password,  username, role)
VALUES (1, '서울시 마포구', 'user1', 'password123',  'username1', 'User');

INSERT IGNORE INTO member (id, address, nickname, password,  username, role)
VALUES (2, '서울시 서초구', 'user2', 'password456', 'username2', 'User');

INSERT IGNORE INTO member (id, address, nickname, password, username, role)
VALUES (3, '서울시 강서구', 'user3', 'password789', 'username3', 'User');

INSERT IGNORE INTO member (id, address, nickname, password, username, role)
VALUES (4, '서울시 중구', 'user4', 'password012', 'username4', 'User');

INSERT IGNORE INTO member (id, address, nickname, password, username, role)
VALUES (5, '서울시 용산구', 'user5', 'password345', 'username5', 'User');

-- 잡포스트 값 할당

INSERT IGNORE INTO job_post (id, member_id, title, description, location, pay_status, created_at, updated_at, shoot_method, audition_date, start_date, end_date)
VALUES (1, 1, 'aaMovie', 'aa', 'SEOUL', 'PAID', NOW(), NOW(), 'FILM', '2024-10-15', '2024-10-20', '2024-10-25');

INSERT IGNORE INTO job_post (id, member_id, title, description, location, pay_status, created_at, updated_at, shoot_method, audition_date, start_date, end_date)
VALUES (2, 2, 'bbPhoto', 'bb', 'BUSAN', 'FREE', NOW(), NOW(), 'PHOTO', '2024-11-05', '2024-11-10', '2024-11-15');

INSERT IGNORE INTO job_post (id, member_id, title, description, location, pay_status, created_at, updated_at, shoot_method, audition_date, start_date, end_date)
VALUES (3, 1, 'ccMovie', 'cc', 'SEOUL', 'PAID', NOW(), NOW(), 'FILM', '2024-12-01', '2024-12-05', '2024-12-10');


-- 어플리케이션 설정

INSERT IGNORE INTO application (id, job_post_id, member_id, applied_at)
VALUES (1, 1, 1, NOW());

INSERT IGNORE INTO application (id, job_post_id, member_id, applied_at)
VALUES (2, 1, 2, NOW());

INSERT IGNORE INTO application (id, job_post_id, member_id, applied_at)
VALUES (3, 2, 1, NOW());

--