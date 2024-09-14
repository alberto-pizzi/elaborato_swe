-- Popola la tabella User
INSERT INTO "User" (email, username, city, province, zip, country, password) VALUES
('mario.rossi@example.com', 'mariorossi', 'Roma', 'RM', '00100', 'Italia', 'password123'),
('luca.bianchi@example.com', 'lucabianchi', 'Milano', 'MI', '20100', 'Italia', 'password123'),
('giulia.verdi@example.com', 'giuliaverdi', 'Napoli', 'NA', '80100', 'Italia', 'password123');

-- Popola la tabella Owner
INSERT INTO "Owner" (email, username, city, province, zip, country, password) VALUES
('owner1@example.com', 'ownerone', 'Torino', 'TO', '10100', 'Italia', 'password123'),
('owner2@example.com', 'ownertwo', 'Firenze', 'FI', '50100', 'Italia', 'password123');

-- Popola la tabella Facility
INSERT INTO "Facility" (name, address, city, province, zip, country, n_managers, n_fields, telephone, image, id_owner) VALUES
('Centro Sportivo Roma', 'Via del Corso, 1', 'Roma', 'RM', '00100', 'Italia', 2, 5, '0612345678', 'olympicField.jpg', 1),
('Stadio Milano', 'Via Montenapoleone, 10', 'Milano', 'MI', '20100', 'Italia', 3, 3, '0212345678', 'olympicField.jpg', 2);

-- Popola la tabella WH (Working Hours)
INSERT INTO "WH" (day_of_week, opening, closing, id_facility) VALUES
('Monday', '09:00', '22:00', 1),
('Tuesday', '09:00', '22:00', 1),
('Wednesday', '09:00', '22:00', 2),
('Thursday', '09:00', '22:00', 2);

-- Popola la tabella Sport
INSERT INTO "Sport" (name, players_required) VALUES
('Calcio', 22),
('Tennis', 2),
('Pallavolo', 12);

-- Popola la tabella Field
INSERT INTO "Field" (name, id_sport, description, price, image, id_facility) VALUES
('Campo di Calcio', 1, 'Campo di calcio a 11 in erba sintetica', 100.00, 'olympicField.jpg', 1),
('Campo di Tennis', 2, 'Campo da tennis in terra rossa', 50.00, 'olympicField.jpg', 2);

-- Popola la tabella Reservation
INSERT INTO "Reservation" (res_date, event_date, res_time, event_time_start, event_time_end, id_field, n_participants, is_confirmed, is_matched, id_user) VALUES
('2024-09-01', '2024-09-10', '12:00', '18:00', '20:00', 1, 22, TRUE, FALSE, 1),
('2024-09-01', '2024-09-15', '15:00', '16:00', '18:00', 2, 2, FALSE, FALSE, 2);

-- Popola la tabella Group
INSERT INTO "Group" (group_head, participants_required, id_reservation) VALUES
(1, 22, 1),
(2, 2, 2);

-- Popola la tabella Invite
INSERT INTO "Invite" (id_group, id_user) VALUES
(1, 2),
(2, 3);

-- Popola la tabella IsPart
INSERT INTO "IsPart" (id_group, id_user, guest_users) VALUES
(1, 2, 0),
(2, 3, 1);

-- Popola la tabella Manages
INSERT INTO "Manages" (id_facility, id_user) VALUES
(1, 1),
(2, 2);
