-- ===============================
-- 1. UŻYTKOWNICY
-- Hasło: "password" (hash bcrypt)
-- Rola: 'USER' lub 'ADMIN' (zgodnie z Enum Role w Java)
-- ===============================
INSERT INTO users (username, password, role)
VALUES (
           'user',
           '$2a$10$7Q6z0l9Z9eZr3Q5GzZz2Rex5FZ1lY5n6Zs4G6cK6G2b0l3Xc7JY5K',
           'USER'
       )
    ON CONFLICT (username) DO NOTHING;

INSERT INTO users (username, password, role)
VALUES (
           'admin',
           '$2a$10$7Q6z0l9Z9eZr3Q5GzZz2Rex5FZ1lY5n6Zs4G6cK6G2b0l3Xc7JY5K',
           'ADMIN'
       )
    ON CONFLICT (username) DO NOTHING;


-- ===============================
-- 2. SALE KINOWE
-- ===============================
INSERT INTO cinema_rooms (name)
VALUES ('Sala 1')
    ON CONFLICT (name) DO NOTHING;


-- ===============================
-- 3. MIEJSCA (SEATS)
-- Generuje rzędy 1-3, miejsca 1-5 dla 'Sala 1'
-- Klucz unikalny: (room_id, row_number, seat_number)
-- ===============================
INSERT INTO seats (row_number, seat_number, room_id)
SELECT r, s, cr.id
FROM cinema_rooms cr,
     generate_series(1, 3) r,
     generate_series(1, 5) s
WHERE cr.name = 'Sala 1'
    ON CONFLICT (room_id, row_number, seat_number) DO NOTHING;


-- ===============================
-- 4. FILMY
-- Używamy WHERE NOT EXISTS, bo w Javie tytuł nie ma 'unique=true',
-- więc ON CONFLICT (title) by nie zadziałało.
-- ===============================
INSERT INTO movies (title, duration_minutes, description, cover_path)
SELECT 'Inception', 148, 'Film science-fiction Christophera Nolana', NULL
    WHERE NOT EXISTS (
    SELECT 1 FROM movies WHERE title = 'Inception'
);


-- ===============================
-- 5. SEANSE
-- Dodajemy seans na "jutro"
-- ===============================
INSERT INTO screenings (movie_id, room_id, start_time)
SELECT m.id, cr.id, NOW() + INTERVAL '1 day'
FROM movies m, cinema_rooms cr
WHERE m.title = 'Inception'
  AND cr.name = 'Sala 1'
  AND NOT EXISTS (
    SELECT 1 FROM screenings s
    WHERE s.movie_id = m.id
  AND s.room_id = cr.id
  AND s.start_time > NOW() -- proste zabezpieczenie przed duplikatami
    );


-- ===============================
-- 6. MIEJSCA NA SEANS (ScreeningSeat)
-- UWAGA: To jest kluczowe! Bez tego seans w SQL nie będzie miał wolnych miejsc.
-- W kodzie Java robi to ScreeningService. W SQL musimy zrobić to sami.
-- ===============================
INSERT INTO screening_seat (screening_id, seat_id, status)
SELECT sc.id, st.id, 'FREE'
FROM screenings sc
         JOIN cinema_rooms cr ON sc.room_id = cr.id
         JOIN seats st ON st.room_id = cr.id
WHERE sc.movie_id = (SELECT id FROM movies WHERE title = 'Inception')
  AND NOT EXISTS (
    SELECT 1 FROM screening_seat ss
    WHERE ss.screening_id = sc.id
      AND ss.seat_id = st.id
);