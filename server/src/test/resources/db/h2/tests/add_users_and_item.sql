insert into users(id,name,email)
values (1,'test_1','a@a1.test'),
       (2,'test_2','a@a2.test');

insert into items(id,owner_id,name,description,available)
values (1, 1,'item 1','test item', true),
       (2, 2,'item 2','-', true),
       (3, 2,'item 3','-', false);