insert into items(id,owner_id,name,description,available)
values (1, 1,'item 1',' -', true),
       (2, 2,'item 2','-', true);

insert into bookings(id,item_id,booker_id,status,start_time,end_time)
values (1, 1, 2, 'WAITING', '2025-12-05 10:37:22', '2025-12-06 10:37:22'),
       (2, 2, 1, 'WAITING', '2025-07-06 10:37:22', '2025-07-07 10:37:22');