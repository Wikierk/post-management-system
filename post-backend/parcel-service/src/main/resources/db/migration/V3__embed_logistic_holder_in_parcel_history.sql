ALTER TABLE parcel_histories ADD COLUMN logistic_holder_type TEXT;

UPDATE parcel_histories ph
SET logistic_holder_type = lh.logistic_holder_type
FROM logistic_holders lh
WHERE ph.logistic_holder_id = lh.logistic_holder_id;

ALTER TABLE parcel_histories
ALTER COLUMN logistic_holder_type SET NOT NULL;

ALTER TABLE parcel_histories DROP CONSTRAINT parcel_histories_logistic_holder_id_fkey;

DROP TABLE logistic_holders;
