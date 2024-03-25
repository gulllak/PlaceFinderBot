--liquibase formatted sql
--changeset evgenii:202403242233.add_location

ALTER TABLE filters ADD COLUMN current_lat float8;
ALTER TABLE filters ADD COLUMN current_lon float8;