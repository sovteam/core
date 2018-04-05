(ns sov.db.database
  (:import (sov.db Database)))

(def ^:dynamic ^Database *database*)

(defn insert! [table record]
  (.insert *database* (name table) record))

(defn update! [table pk-column pk-value record]
  (.update *database* table pk-column pk-value record))

(defn select-one [table-name column value]
  (->> (.select *database* table-name column value)
    (into {} (map (fn [[col value]]
                    [(-> col .toLowerCase keyword) value])))))
