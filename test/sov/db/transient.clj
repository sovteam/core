(ns sov.db.transient
  (:import (clojure.lang IDeref)
           (sov.db Database)))

(defn transient-database [data]
  (let [data-atom (atom data)]
    (reify
      Database
      (createTable [this table-name column-specs]
        #_(assoc-in data-atom [:schema table-name] column-specs))

      (update [this table-name pk-column pk-value record]
        (swap! data-atom update-in [:data table-name pk-value] merge record))

      (select [this table-name column value]
        (let [table-name (keyword table-name)]
          (println ">>>>" table-name)
          (->> data-atom :data (some #(= (% column) value)))))

      IDeref
      (deref [this] @data-atom))))
