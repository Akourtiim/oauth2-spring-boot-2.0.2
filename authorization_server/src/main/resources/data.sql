INSERT INTO authority  VALUES(1,'ROLE_OAUTH_ADMIN');
INSERT INTO authority VALUES(2,'ROLE_ADMIN');
INSERT INTO authority VALUES(3,'ROLE_USER');
INSERT INTO credentials VALUES(1,b'1','oauth_admin','admin','0');
INSERT INTO credentials VALUES(2,b'1','resource_admin','admin','0');
INSERT INTO credentials  VALUES(3,b'1','user','$2a$10$BurTWIy5NTF9GJJH4magz.9Bd4bBurWYG8tmXxeQh1vs7r/wnCFG2','0');
INSERT INTO oauth_client_details VALUES('test','test', null, 'read,write', 'implicit', 'http://127.0.0.1', 'ROLE_USER', 7200, 0, NULL, 'true');
INSERT INTO `oauth_client_details`VALUES('read-only-client', 'todo-services', null, 'read', 'implicit', 'http://localhost', NULL, 7200, 0, NULL, 'false');
