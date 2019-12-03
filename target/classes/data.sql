INSERT INTO melusine.accounts
(id, email, password, is_barman, created_at, updated_at, user_id)
VALUES ('8fa3aca1-c0bf-4586-b519-a7878d6577c0', 'emeric.hoerner@gmail.com',
'$shiro1$SHA-256$500000$Ml7/U9aYWT8YwZvSuDiBcA==$29cKm4zPNGkGMU7U//N9mrIoW/5tNJBIHi8JVXaCdGE=', 1,
'2019-11-26 15:03:40', '2019-11-26 15:03:40', '6509e418-a12a-4a8a-b7af-8df1f7bcce85'),
('8fa3aca1-c0bf-4586-b519-a7878d6577c1', 'jason.mangin@gmail.com',
'$shiro1$SHA-256$500000$Ml7/U9aYWT8YwZvSuDiBcA==$29cKm4zPNGkGMU7U//N9mrIoW/5tNJBIHi8JVXaCdGE=', 1,
'2019-11-26 15:03:40', '2019-11-26 15:03:40', '6509e418-a12a-4a8a-b7af-8df1f7bcce86'),
('8fa3aca1-c0bf-4586-b519-a7878d6577c2', 'stephane.mazzei@gmail.com',
'$shiro1$SHA-256$500000$Ml7/U9aYWT8YwZvSuDiBcA==$29cKm4zPNGkGMU7U//N9mrIoW/5tNJBIHi8JVXaCdGE=', 1,
'2019-11-26 15:03:40', '2019-11-26 15:03:40', '6509e418-a12a-4a8a-b7af-8df1f7bcce86');

INSERT INTO melusine.users
(id, first_name, last_name, nick_name, section, credit, is_membership, created_at, updated_at)
VALUES ('6509e418-a12a-4a8a-b7af-8df1f7bcce85', 'Emeric', 'Hoerner', 'Mick', 'EXTERNAL', 100, 1, '2019-12-03 12:07:11',
'2019-12-03 12:07:11'),
('6509e418-a12a-4a8a-b7af-8df1f7bcce86', 'Jason', 'Mangin', 'Jazzy', 'EXTERNAL', 100, 1, '2019-12-03 12:07:11',
'2019-12-03 12:07:11'),
('6509e418-a12a-4a8a-b7af-8df1f7bcce87', 'Stephane', 'Mazzei', 'Maze', 'EXTERNAL', 100, 1, '2019-12-03 12:07:11',
'2019-12-03 12:07:11');

INSERT INTO melusine.ingredients
(id, name, price, stock, image, created_at, updated_at)
VALUES ('6509e418-a12a-4a8a-b7af-8df1f7bcce88', 'Tomate', 50, 100, '/var/lib/tomate', '2019-12-03 12:07:11',
'2019-12-03 12:07:11'),
('6509e418-a12a-4a8a-b7af-8df1f7bcce89', 'Oignon', 50, 100, '/var/lib/oignon', '2019-12-03 12:07:11',
'2019-12-03 12:07:11'),
('6509e418-a12a-4a8a-b7af-8df1f7bcce90', 'Salade', 50, 100, '/var/lib/salade', '2019-12-03 12:07:11',
'2019-12-03 12:07:11');

INSERT INTO melusine.products
(id, name, price, image, category, is_original, created_at, updated_at)
VALUES ('6509e418-a12a-4a8a-b7af-8df1f7bcce91', 'Mitch', 120, '/var/lib/tomate', 'COLD', 1, '2019-12-03 12:07:11',
'2019-12-03 12:07:11');