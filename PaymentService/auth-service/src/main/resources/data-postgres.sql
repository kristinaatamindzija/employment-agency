insert into web_shop(id, currency, username, password) values (nextval('web_shop_id_seq'),'rsd','EmploymentAgency',
                                                              '$2a$10$UVn74F/yEiUzKWBSGVyzHe2UfpVJ95zY50Q8bz1RFyrAYVfwFAj4i');
insert into web_shop(id, currency, username, password) values (nextval('web_shop_id_seq'),'rsd','wasd',
                                                               '$2a$12$1gl47PZdh4FkOdHq8dPwauildxIqmMo71XHooLwv9tAZKDvHx/1s6');

insert into payment_method(id, name) values (nextval('payment_method_id_seq'), 'bank-card');
insert into payment_method(id, name) values (nextval('payment_method_id_seq'), 'qr-code');
insert into payment_method(id, name) values (nextval('payment_method_id_seq'), 'paypal');
insert into payment_method(id, name) values (nextval('payment_method_id_seq'), 'bitcoin');

insert into merchant_payment_method(id, payment_method_id, merchant_id, merchant_password)
values (nextval('merchant_payment_method_id_seq'), 1, 'tinaa', '$2a$10$46vcjpM2KOvc76hjcNb9NOgXsNKpWXR1b.tEXetZcWV0l4FQc8i5.');
insert into merchant_payment_method(id, payment_method_id, merchant_id, merchant_password)
values (nextval('merchant_payment_method_id_seq'), 1, 'tinaa', '$2a$10$46vcjpM2KOvc76hjcNb9NOgXsNKpWXR1b.tEXetZcWV0l4FQc8i5.');

insert into web_shop_payment_method(web_shop_id, merchant_payment_id) values (1, 1);