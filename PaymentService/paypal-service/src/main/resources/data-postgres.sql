insert into merchant(merchant_id, merchant_uuid, merchant_paypal_id, email) values(nextval('merchant_id_seq'), 'aaaaa', 'SQK683LFHNNTN', 'sb-msi0x23501552@business.example.com');

insert into subscription_plan(id, product_id, merchant_id, plan_paypal_id) values(nextval('plan_id_seq'), 1, 1, 'P-82T63888Y87914132MOPPK7Y');