-- insert into credit_card(id, pan, security_code, card_holder_name, valid_thru, amount_of_money) values (1, '7774567891234567', '123', 'Imenko Prezimic', '2025-12-01', 1000);
-- insert into credit_card(id, pan, security_code, card_holder_name, valid_thru, amount_of_money) values (2, '7771567891234567', '321', 'Pera Peric', '2025-10-01', 2000);
insert into credit_card(id, pan, security_code, card_holder_name, valid_thru, amount_of_money) values (3, '8881567891234567', '456', 'Maja Maric', '2026-10-01', 3000);

-- insert into account(id, name, surname, password, phone_number, merchant_id, credit_card_id) values (nextval('account_sequence'), 'Imenko', 'Prezimic', 'pilence', '065123123', '12345678912345678912345678912345', 1);
-- insert into account(id, name, surname, password, phone_number, merchant_id, credit_card_id) values (nextval('account_sequence'), 'Pera', 'Peric', 'pilence123', '065123123', '12345678912345678912345678912346', 2);
insert into account(id, name, surname, password, phone_number, merchant_id, credit_card_id) values (nextval('account_sequence'), 'Maja', 'Maric', 'imbiamba123', '065123123', '12345678912345678912345678912342', 3);