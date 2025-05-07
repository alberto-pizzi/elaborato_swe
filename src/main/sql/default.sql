-- fill User table
INSERT INTO "User" (email, username, city, province, zip, country, password) VALUES
('mario.rossi@example.com', 'mariorossi', 'Roma', 'RM', '00100', 'Italia', '75K3eLr+dx6JJFuJ7LwIpEpOFmwGZZkRiB84PURz6U8='), --this password is: password123
('luca.bianchi@example.com', 'lucabianchi', 'Milano', 'MI', '20100', 'Italia', '75K3eLr+dx6JJFuJ7LwIpEpOFmwGZZkRiB84PURz6U8='),
('giulia.verdi@example.com', 'giuliaverdi', 'Napoli', 'NA', '80100', 'Italia', '75K3eLr+dx6JJFuJ7LwIpEpOFmwGZZkRiB84PURz6U8=');

-- fill Owner table
INSERT INTO "Owner" (email, username, city, province, zip, country, password) VALUES
('owner1@example.com', 'ownerone', 'Torino', 'TO', '10100', 'Italia', '75K3eLr+dx6JJFuJ7LwIpEpOFmwGZZkRiB84PURz6U8='),
('owner2@example.com', 'ownertwo', 'Firenze', 'FI', '50100', 'Italia', '75K3eLr+dx6JJFuJ7LwIpEpOFmwGZZkRiB84PURz6U8=');

-- fill Facility table
INSERT INTO "Facility" (name, address, city, province, zip, country, telephone, image, id_owner) VALUES
('Centro Sportivo Roma', 'Via del Corso, 1', 'Roma', 'RM', '00100', 'Italia', '0612345678', 'olympicField.jpg', 1),
('Stadio Milano', 'Via Montenapoleone, 10', 'Milano', 'MI', '20100', 'Italia', '0212345678', 'olympicField.jpg', 2);

-- fill WH (Working Hours) table
INSERT INTO "WH" (day_of_week, opening, closing, id_facility) VALUES
('MONDAY', '09:00', '22:00', 1),
('TUESDAY', '09:00', '22:00', 1),
('WEDNESDAY', '09:00', '22:00', 2),
('THURSDAY', '09:00', '22:00', 2);

-- fill Sport table
INSERT INTO "Sport" (name, players_required) VALUES
('Calcio', 22),
('Tennis', 2),
('Pallavolo', 12);

-- fill Field table
INSERT INTO "Field" (name, id_sport, description, price, image, id_facility) VALUES
('Campo di Calcio', 1, 'Campo di calcio a 11 in erba sintetica', 100.00, 'olympicField.jpg', 1),
('Campo di Tennis', 2, 'Campo da tennis in terra rossa', 50.00, 'olympicField.jpg', 2);

-- fill Reservation table
INSERT INTO "Reservation" (res_date, event_date, res_time, event_time_start, event_time_end, id_field, is_confirmed, is_matched,is_deleted,is_notified) VALUES
('2024-09-01', '2024-09-10', '12:00', '18:00', '20:00', 1,  TRUE, FALSE,FALSE,FALSE),
('2024-09-01', '2024-09-15', '15:00', '16:00', '18:00', 2,  FALSE, TRUE,FALSE,FALSE);

-- fill Group table
INSERT INTO "Group" (group_head, participants_required, id_reservation) VALUES
(1, 22, 2),
(2, 2, 1);

-- fill Invite table
INSERT INTO "Invite" (id_group, id_user) VALUES
(1, 2);

-- fill IsPart table
INSERT INTO "IsPart" (id_group, id_user, guest_users) VALUES
(1, 1, 0),
(2, 2, 1);

-- fill Manages table
INSERT INTO "Manages" (id_facility, id_user) VALUES
(1, 1),
(2, 2);

-- fill NotificationOwner table
INSERT INTO "NotificationOwner" (id_owner,notification_type,id_message, id_reservation) VALUES
(1, 'CONFIRMATION',NULL,1);

-- fill NotificationUser table
INSERT INTO "NotificationUser" (id_user,notification_type,id_message, id_reservation) VALUES
    (2, 'CONFIRMATION',NULL,1);




