--this script initiates db for h2 db (used in test profile)
insert into user (account_status, email, first_name, last_name) values ('CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (account_status, email, first_name) values ('NEW', 'brian@domain.com', 'Brian')
insert into user (account_status, email, first_name, last_name) values ('CONFIRMED', 'confirmed@domain.com', 'confirmed', 'confirmed')
insert into user (account_status, email, first_name) values ('REMOVED', 'removed@domain.com', 'removed')
insert into blog_post (id, entry, user_id) values (1, 'entry1', 1)
insert into blog_post (id, entry, user_id) values (2, 'entry2', 2)
