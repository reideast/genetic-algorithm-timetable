-- These need to be made (or maybe just updated) using the API, which will calculate the passwords correctly
-- curl -X POST \
  -- http://localhost:5000/api/users/10 \
  -- -H 'Content-Type: application/json' \
  -- -H 'Postman-Token: 106b5708-f4fc-43be-a668-8d9a07a67a10' \
  -- -H 'cache-control: no-cache' \
  -- -d '{"password":"genetic"}'

INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(12287, 'admin', '$2a$10$usIGjWQXGnIEeLQu6IF30Os4e5ICFvmviR6PetdjHPLq.LUX4IxxG', 'Administrator', 1, true, true, 'andrew@andreweast.net', 'ROLE_ADMIN,ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(3, 'it', '$2a$10$m06zMQq2PNfgtTInjuz41eaPpGyFu6iUQ4/dXvXckFMzJMTWCu8nK', 'IT Dept Admin', 1, false, false, 'andrew@andreweast.net', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');

INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user0', 'UNHASHED_PW', 'Scheduler 0', 1, false, false, 'user0@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user1', 'UNHASHED_PW', 'Scheduler 1', 1, false, false, 'user1@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user2', 'UNHASHED_PW', 'Scheduler 2', 1, false, false, 'user2@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user3', 'UNHASHED_PW', 'Scheduler 3', 1, false, false, 'user3@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user4', 'UNHASHED_PW', 'Scheduler 4', 1, false, false, 'user4@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user5', 'UNHASHED_PW', 'Scheduler 5', 1, false, false, 'user5@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user6', 'UNHASHED_PW', 'Scheduler 6', 1, false, false, 'user6@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user7', 'UNHASHED_PW', 'Scheduler 7', 1, false, false, 'user7@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user8', 'UNHASHED_PW', 'Scheduler 8', 1, false, false, 'user8@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user9', 'UNHASHED_PW', 'Scheduler 9', 1, false, false, 'user9@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user10', 'UNHASHED_PW', 'Scheduler 10', 1, false, false, 'user10@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user11', 'UNHASHED_PW', 'Scheduler 11', 1, false, false, 'user11@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user12', 'UNHASHED_PW', 'Scheduler 12', 1, false, false, 'user12@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user13', 'UNHASHED_PW', 'Scheduler 13', 1, false, false, 'user13@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user14', 'UNHASHED_PW', 'Scheduler 14', 1, false, false, 'user14@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user15', 'UNHASHED_PW', 'Scheduler 15', 1, false, false, 'user15@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user16', 'UNHASHED_PW', 'Scheduler 16', 1, false, false, 'user16@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user17', 'UNHASHED_PW', 'Scheduler 17', 1, false, false, 'user17@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user18', 'UNHASHED_PW', 'Scheduler 18', 1, false, false, 'user18@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES('user19', 'UNHASHED_PW', 'Scheduler 19', 1, false, false, 'user19@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');

INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(3, 'it', '$2a$10$m06zMQq2PNfgtTInjuz41eaPpGyFu6iUQ4/dXvXckFMzJMTWCu8nK', 'IT Dept Admin', 1, false, false, 'andrew@andreweast.net', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(10, 'user0', '$2a$10$mUA1TBqdhcqce0vurxD8LeGUv4QMB9zymZ.AaxTfoECIvszR7k.n.', 'Scheduler 0', 1, false, false, 'user0@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(11, 'user1', '$2a$10$4dNe0q.1QR5MVPZchWfFAuU7gLl2E3E2PVyBQ5xxD7nKXdEGCUVTW', 'Scheduler 1', 1, false, false, 'user1@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(12, 'user2', '$2a$10$ZVN3VorMqyzM1q67Sv2Q8uM.jOOOQkm2grmHuoV3QsfK17uQmmBZW', 'Scheduler 2', 1, false, false, 'user2@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(13, 'user3', '$2a$10$tRGu9jsUvAIiguQmwc7JVOIZI9Bg08Xjznzs4byiQ9i4W2feyMXLG', 'Scheduler 3', 1, false, false, 'user3@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(14, 'user4', '$2a$10$biKzP1uJSVf2bGmH7Y9cyeVBrl18fCnCUg/I3faXtBJSjI.SoSC0i', 'Scheduler 4', 1, false, false, 'user4@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(15, 'user5', '$2a$10$K0sBluANvb86Xh0YXFaaOONst.MH8MM.KzcKHHz0.7kOTQ9zGZJUe', 'Scheduler 5', 1, false, false, 'user5@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(16, 'user6', '$2a$10$uGDb59qmkinGG52pCBYeRunFMjN6.O7iyFsxNsQe5VqQDV2qAjH/6', 'Scheduler 6', 1, false, false, 'user6@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(17, 'user7', '$2a$10$.prkEiAdcq3gbNRG0TJkVeX8tXkof2fmnDSMctXyFSbToMQVma89i', 'Scheduler 7', 1, false, false, 'user7@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(18, 'user8', '$2a$10$3YLy/1.UG.d.pmpwhH5Ine6up8J3ff1T9u1Nh8y3ZOXfa3N/4uNk.', 'Scheduler 8', 1, false, false, 'user8@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(19, 'user9', '$2a$10$SfmldWUFSntPQtSFJifKhumeU4K4Ec/uXoLW7XZc7TSXXEbDRxVZK', 'Scheduler 9', 1, false, false, 'user9@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(20, 'user10', '$2a$10$Pfe82hkul1OXGGJG9t1DqeRJuQWxlQ3MpgC46IzDkFZ5eEDs2BO7.', 'Scheduler 10', 1, false, false, 'user10@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(21, 'user11', '$2a$10$5JQ8QxRrf.UwgySMRNBLiemQEvz95vFxdmi7FpNa/JePJ3h1p8cna', 'Scheduler 11', 1, false, false, 'user11@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(22, 'user12', '$2a$10$gcXbMnfAG9iMwFweNLDdMO.4ayFlUDvxr7mRzJGiWF9wpOcDX8n1q', 'Scheduler 12', 1, false, false, 'user12@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(23, 'user13', '$2a$10$XHEWxjc0gO/TZhodg7KWZ.23w3RwaPX3MzUbqmtOBl.EPYXc8Sk.m', 'Scheduler 13', 1, false, false, 'user13@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(24, 'user14', '$2a$10$4T60wHRAAowuEh5YvyIFq.eZIgS8M42T6L2vaPM5tIq06Xz.ql8qW', 'Scheduler 14', 1, false, false, 'user14@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(25, 'user15', '$2a$10$LAg3Os9dKwj2j91OIubVT./.eRoq7N8mOIlrGL6N.XQ2GZGvmMgja', 'Scheduler 15', 1, false, false, 'user15@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(26, 'user16', '$2a$10$Rp46LacpGucFHToUUI6sYeRhtXRZayPz.eS4nMe1YB5FEGDZ6I1T.', 'Scheduler 16', 1, false, false, 'user16@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(27, 'user17', '$2a$10$c7bdtc.hMfKMWCvqWDbHpO28ch2Mmkk6/z2.FIgWKjomMQeIWaQH2', 'Scheduler 17', 1, false, false, 'user17@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(28, 'user18', '$2a$10$L0CHsPmqb2EsdgOfMYDbDeD5OZjF6r11NRyhbn.ErbnAAeznP7bX2', 'Scheduler 18', 1, false, false, 'user18@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(29, 'user19', '$2a$10$j5GQRKwIdGX0ZC9YZNdS2OTDlT..VH6Ph9tTIud5gQXBLaSqkq5mG', 'Scheduler 19', 1, false, false, 'user19@nuigalway.ie', 'ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
INSERT INTO public.users (user_id, username, "password", display_name, department_id, is_facilities, is_admin, email, roles) VALUES(12287, 'admin', '$2a$10$usIGjWQXGnIEeLQu6IF30Os4e5ICFvmviR6PetdjHPLq.LUX4IxxG', 'Administrator', 1, true, true, 'andrew@andreweast.net', 'ROLE_ADMIN,ROLE_SCHEDULE_CREATE,ROLE_GENETICALGORITHM_RUN');
