insert into credit_card(id, pan, security_code, card_holder_name, valid_thru, amount_of_money) values (1, 'f535c2c61757f77ef3d949888f9e517cca66ee151ed6cac62b8959cf6e8fab6a', 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Imenko Prezimic', '2025-12-01', 1000);
insert into credit_card(id, pan, security_code, card_holder_name, valid_thru, amount_of_money) values (2, 'c6674b2111ad8a5ab2e0925611d3fd78d0719373fddd673af3af9d42a0737fdf', '8d23cf6c86e834a7aa6eded54c26ce2bb2e74903538c61bdd5d2197997ab2f72', 'Pera Peric', '2025-10-01', 2000);
--insert into credit_card(id, pan, security_code, card_holder_name, valid_thru, amount_of_money) values (3, '8cfc7e348cc6902b44c9fd129d1be268c35553e202d12b9254b3d6a2d8054c34', 'b3a8e0e1f9ab1bfe3a36f231f676f78bb30a519d2b21e6c530c0eee8ebb4a5d0', 'Maja Maric', '2026-10-01', 3000);

insert into account(id, name, surname, password, phone_number, merchant_id, credit_card_id, account_number) values (nextval('account_sequence'), 'Imenko', 'Prezimic', 'pilence', '065123123', '12345678912345678912345678912345', 1, '845000000040484987' );
insert into account(id, name, surname, password, phone_number, merchant_id, credit_card_id, account_number) values (nextval('account_sequence'), 'Pera', 'Peric', 'pilence123', '065123123', '12345678912345678912345678912346', 2, '109000000000000029');
--insert into account(id, name, surname, password, phone_number, merchant_id, credit_card_id, account_number) values (nextval('account_sequence'), 'Maja', 'Maric', 'imbiamba123', '065123123', '12345678912345678912345678912342', 3, '109000000000000029');