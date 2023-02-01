insert into web_shop(id, currency, username, password, merchant_id, merchant_password, merchant_uuid, success_url, fail_url, error_url, bank_url) values (nextval('web_shop_id_seq'),'rsd','EmploymentAgency',
                                                              '$2a$10$UVn74F/yEiUzKWBSGVyzHe2UfpVJ95zY50Q8bz1RFyrAYVfwFAj4i', '12345678912345678912345678912345', 'pilence', '12345678-1234-1234-1234-123456789012', 'http://localhost:7000/order-success', 'http://localhost:7000/order-fail', 'http://localhost:7000/order-error', 'http://localhost:8001');
insert into web_shop(id, currency, username, password, merchant_id, merchant_password, merchant_uuid, success_url, fail_url, error_url, bank_url) values (nextval('web_shop_id_seq'),'rsd','EmploymentAgency1',
                                                               '$2a$10$UVn74F/yEiUzKWBSGVyzHe2UfpVJ95zY50Q8bz1RFyrAYVfwFAj4i', '12345678912345678912345678912346', 'pilence123', '12345678-1234-1234-1234-123456789013', 'http://localhost:7000/order-success', 'http://localhost:7000/order-fail', 'http://localhost:7000/order-error', 'http://localhost:8001');
insert into web_shop(id, currency, username, password, merchant_id, merchant_password, merchant_uuid, bank_url) values (nextval('web_shop_id_seq'),'rsd','EmploymentAgency2',
                                                                                                              '$2a$10$UVn74F/yEiUzKWBSGVyzHe2UfpVJ95zY50Q8bz1RFyrAYVfwFAj4i', '12345678912345678912345678912342', 'imbiamba123', '12345678-1234-1234-1234-123456789017', 'http://localhost:8002');
insert into web_shop(id, currency, username, password, bank_url) values (nextval('web_shop_id_seq'),'rsd','wasd',
                                                               '$2a$12$/XyoO1s5ZClombP9y4Qfi.R5k6.43Hp3XgSVQXRRdcERGvkaTPzeK', 'http://localhost:8002');

insert into payment_method(id, name) values (nextval('payment_method_id_seq'), 'bank-card');
insert into payment_method(id, name) values (nextval('payment_method_id_seq'), 'qr-code');
insert into payment_method(id, name) values (nextval('payment_method_id_seq'), 'paypal');
insert into payment_method(id, name) values (nextval('payment_method_id_seq'), 'bitcoin');


insert into web_shop_payment_method(web_shop_id, payment_method_id) values (1, 1);
insert into web_shop_payment_method(web_shop_id, payment_method_id) values (1, 3);
insert into web_shop_payment_method(web_shop_id, payment_method_id) values (1, 4);
insert into web_shop_payment_method(web_shop_id, payment_method_id) values (2, 1);