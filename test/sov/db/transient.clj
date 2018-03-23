(ns sov.db.transient
  (:import (clojure.lang IDeref)))

(defn transient-database [data]
  (let [data-atom (atom data)]
    (reify
      sov.Database
      (createTable [this table-name column-specs]
        #_(assoc-in data-atom [:schema table-name] column-specs))

      (upsert [this table-name values]
        (swap! data-atom update-in [:data table-name] conj values))

      (selectOne [this table-name column value]
        (let [table-name (keyword table-name)]
          (println ">>>>" table-name)
          (->> data-atom :data (some #(= (% column) value)))))

      IDeref
      (deref [this] @data-atom))))
