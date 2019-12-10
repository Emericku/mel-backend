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
(id, category, name, image, price, is_original, quantity, created_at, updated_at)
VALUES ('6509e418-a12a-4a8a-b7af-8df1f7bcce91', 'CUSTOM', 'Sandwich froid', '/assets/thumbnails/sandwich-froid.png', 380
, 1, 23, '2019-12-03 12:07:11', '2019-12-03 12:07:11'),
('6510e418-a12a-4a8a-b7af-8df1f7bcce91', 'CUSTOM', 'Sandwich chaud', '/assets/thumbnails/sandwich-chaud.png', 180, 1, 30
,
'2019-12-03 12:07:11',
'2019-12-03 12:07:11'),
('6511e418-a12a-4a8a-b7af-8df1f7bcce91', 'CUSTOM', 'Salade Gégé', '/assets/thumbnails/salade-gege.png', 400, 1, 30,
'2019-12-03 12:07:11',
'2019-12-03 12:07:11'),
('6512e418-a12a-4a8a-b7af-8df1f7bcce91', 'CHAUD', 'Pizza', '/assets/thumbnails/pizza.png', 300, 1, 35,
'2019-12-03 12:07:11'
,
'2019-12-03 12:07:11'),
('6513e418-a12a-4a8a-b7af-8df1f7bcce91', 'CHAUD', 'Quiche', '/assets/thumbnails/quiche.png', 250, 1, 38,
'2019-12-03 12:07:11'
,
'2019-12-03 12:07:11'),
('6514e418-a12a-4a8a-b7af-8df1f7bcce91', 'CHAUD', 'Pâté Lorrain', '/assets/thumbnails/pate-lorrain.png', 250, 1, 150,
'2019-12-03 12:07:11',
'2019-12-03 12:07:11'),
('6515e418-a12a-4a8a-b7af-8df1f7bcce91', 'CHAUD', 'Croque monsieur', '/assets/thumbnails/croque-monsieur.png', 100, 1,
80,
'2019-12-03 12:07:11',
'2019-12-03 12:07:11'),
('6516e418-a12a-4a8a-b7af-8df1f7bcce91', 'FROID', 'Mitch', '/assets/thumbnails/mitch.png', 100, 1, 80,
'2019-12-03 12:07:11'
,
'2019-12-03 12:07:11'),
('6517e418-a12a-4a8a-b7af-8df1f7bcce91', 'FROID', 'Jambon beurre', '/assets/thumbnails/jambon-beurre.png', 100, 1, 80,
'2019-12-03 12:07:11',
'2019-12-03 12:07:11'),
('6518e418-a12a-4a8a-b7af-8df1f7bcce91', 'FROID', 'Jambon tartare', '/assets/thumbnails/jambon-tartare.png', 1.20, 1, 80
,
'2019-12-03 12:07:11',
'2019-12-03 12:07:11'),
('6519e418-a12a-4a8a-b7af-8df1f7bcce91', 'FROID', 'Rosette', '/assets/thumbnails/rosette.png', 120, 1, 80,
'2019-12-03 12:07:11',
'2019-12-03 12:07:11'),
('6520e418-a12a-4a8a-b7af-8df1f7bcce91', 'FROID', 'Paté', '/assets/thumbnails/pate.png', 120, 1, 80,
'2019-12-03 12:07:11',
'2019-12-03 12:07:11'),
('6521e418-a12a-4a8a-b7af-8df1f7bcce91', 'BOISSON', 'Soda', '/assets/thumbnails/soda.png', 100, 1, 80,
'2019-12-03 12:07:11'
,
'2019-12-03 12:07:11'),
('6522e418-a12a-4a8a-b7af-8df1f7bcce91', 'BOISSON', 'Eau', '/assets/thumbnails/eau.png', 100, 1, 100,
'2019-12-03 12:07:11',
'2019-12-03 12:07:11'),
('6523e418-a12a-4a8a-b7af-8df1f7bcce91', 'BOISSON', 'Sirop', '/assets/thumbnails/sirop.png', 50, 1, 100,
'2019-12-03 12:07:11',
'2019-12-03 12:07:11'),
('6524e418-a12a-4a8a-b7af-8df1f7bcce91', 'DESSERT', 'Croissant', '/assets/thumbnails/croissant.png', 50, 1, 100,
'2019-12-03 12:07:11',
'2019-12-03 12:07:11'),
('6525e418-a12a-4a8a-b7af-8df1f7bcce91', 'DESSERT', 'Pain choco', '/assets/thumbnails/pain-choco.png', 70, 1, 50,
'2019-12-03 12:07:11',
'2019-12-03 12:07:11'),
('6526e418-a12a-4a8a-b7af-8df1f7bcce91', 'DESSERT', 'Barre choco', '/assets/thumbnails/barre-choco.png', 100, 1, 50,
'2019-12-03 12:07:11',
'2019-12-03 12:07:11'),
('6527e418-a12a-4a8a-b7af-8df1f7bcce91', 'DESSERT', 'Kinder Bueno', '/assets/thumbnails/kinder-bueno.png', 120, 1, 50,
'2019-12-03 12:07:11',
'2019-12-03 12:07:11');