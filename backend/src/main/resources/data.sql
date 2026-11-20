INSERT INTO t_user (username, password, role, create_time) VALUES
                                                               ('admin', '123456', 'ADMIN', NOW()),
                                                               ('user1', '123456', 'USER', NOW())
    ON DUPLICATE KEY UPDATE username=username;