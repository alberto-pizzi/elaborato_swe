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