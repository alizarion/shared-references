INSERT INTO `security_credential` (`id`, `credential_creation_date`, `logon`, `password`, `state`, `user_name`) VALUES (1, '2014-10-16 14:24:34', 'P', '0b9c2625dc21ef05f6ad4ddf47c5f203837aa32c', 'A', 'username');
INSERT INTO `security_roles` (`id`, `role_creation_date`, `description`, `unique_key`, `role_name`) VALUES (1, '2014-10-16 14:24:34', 'allow user to produce openid indentification', 'openid', 'my app openid role'), (2, '2014-10-16 14:24:34', 'allow user to access to profile data', 'profile', 'my app profile role'), (3, '2014-10-16 14:24:34', 'allow user to access email data', 'email', 'my app email role'),(4, '2014-10-16 14:24:34', 'allow user to produce oauth authorizations', 'oauth', 'my app oauth role');
INSERT INTO `security_oauth_client_application` (`id`, `client_id`, `client_secret`, `home_page_url`, `name`, `redirect_uri`, `trusted_client`) VALUES (1, 'clientId', 'clientSecret', 'http://myproject.com', 'myproject', 'http://localhost:8180/redirect', 00000000);
commit;
INSERT INTO `security_oauth_scope_server` (`id`, `role_creation_date`, `description`, `unique_key`, `role_name`, `oauth_role_id`) VALUES (1, '2014-10-16 14:24:34', 'authorized to get id_token', 'openid', 'my app openid scope', 3), (2, '2014-10-16 14:24:34', 'authorized to get users profile data', 'profile', 'my app profile scope', 4);
INSERT INTO `security_oauth_client_application_server_scope` (`client_application_id`, `server_scope_id`) VALUES (1, 1), (1, 2);
INSERT INTO `security_credential_roles` (`id`, `credential_id`, `role_id`) VALUES (1, 1, 3), (2, 1, 4);
INSERT INTO `sequence` (`SEQ_NAME`, `SEQ_COUNT`) VALUES ('security_roles', 1), ('security_credential', 1), ('security_credential_roles', 1), ('OAuthApplication', 1), ('OAuthScope', 1);
commit;
