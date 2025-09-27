-- drop triggers
DROP TRIGGER IF EXISTS trg_check_delete_reservation_user ON "NotificationUser";
DROP TRIGGER IF EXISTS trg_check_delete_reservation_owner ON "NotificationOwner";
DROP TRIGGER IF EXISTS trigger_delete_message_user ON "NotificationUser";
DROP TRIGGER IF EXISTS trigger_delete_message_owner ON "NotificationOwner";
DROP TRIGGER IF EXISTS trg_cleanup_old_reservations ON "Reservation";

-- trigger function
CREATE OR REPLACE FUNCTION delete_unused_messages()
RETURNS TRIGGER AS $$
BEGIN
    -- checks if message has some references
    IF OLD.id_message IS NOT NULL THEN
        IF NOT EXISTS (
            SELECT 1
            FROM "NotificationUser"
            WHERE id_message = OLD.id_message
        ) AND NOT EXISTS (
            SELECT 1
            FROM "NotificationOwner"
            WHERE id_message = OLD.id_message
        ) THEN
            -- delete message if there aren't references
DELETE FROM "Message"
WHERE id = OLD.id_message;
END IF;
END IF;

RETURN NULL;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION fn_check_and_delete_reservation()
RETURNS trigger AS $$
DECLARE
reservation_id INTEGER;
    reservation_status BOOLEAN;
BEGIN
    -- takes reservation id by deleted record
    reservation_id := OLD.id_reservation;

    IF reservation_id IS NOT NULL THEN
        -- check reservation state (is_deleted)
SELECT is_deleted INTO reservation_status
FROM "Reservation"
WHERE id = reservation_id;

-- if reservation is marked as deleted...
IF reservation_status = true THEN
            -- ... and there aren't any references...
            IF NOT EXISTS (
                SELECT 1 FROM "NotificationUser" WHERE id_reservation = reservation_id
            ) AND NOT EXISTS (
                SELECT 1 FROM "NotificationOwner" WHERE id_reservation = reservation_id
            ) THEN
DELETE FROM "Reservation" WHERE id = reservation_id;
END IF;
END IF;
END IF;

RETURN OLD;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION cleanup_old_reservations()
RETURNS TRIGGER AS $$
BEGIN
DELETE FROM "Reservation"
WHERE is_deleted = true
  AND event_date < CURRENT_DATE - INTERVAL '31 days';

RETURN NEW;
END;
$$ LANGUAGE plpgsql;



-- Trigger on NotificationUser
CREATE TRIGGER trigger_delete_message_user
    AFTER DELETE
    ON "NotificationUser"
    FOR EACH ROW
    EXECUTE FUNCTION delete_unused_messages();

-- Trigger on NotificationOwner
CREATE TRIGGER trigger_delete_message_owner
    AFTER DELETE
    ON "NotificationOwner"
    FOR EACH ROW
    EXECUTE FUNCTION delete_unused_messages();

-- Trigger on NotificationUser
CREATE TRIGGER trg_check_delete_reservation_user
    AFTER DELETE
    ON "NotificationUser"
    FOR EACH ROW
    EXECUTE FUNCTION fn_check_and_delete_reservation();

-- Trigger on NotificationOwner
CREATE TRIGGER trg_check_delete_reservation_owner
    AFTER DELETE
    ON "NotificationOwner"
    FOR EACH ROW
    EXECUTE FUNCTION fn_check_and_delete_reservation();

-- Trigger on Reservation
CREATE TRIGGER trg_cleanup_old_reservations
    AFTER INSERT OR UPDATE
    ON "Reservation"
    FOR EACH STATEMENT
    EXECUTE FUNCTION cleanup_old_reservations();
