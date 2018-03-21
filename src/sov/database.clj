(ns sov.database)

; This interface defines the methods that the database injected into the core must
; have. They are called by reflection, though, so the injected database does not
; need to declare that it implements this interface.
; This (minimal) dependency is implicit, so that the core and any projects that
; depend on it can be compiled independently.
(gen-interface :name sov.Database
               :methods [[createTable [String java.util.List] void]
                         [upsert [String java.util.Map] void]
                         [selectOne [String String Object] Object]])

(defn transient-database []
  (let [db (atom {})]
    (reify sov.Database
      (createTable [this table-name column-specs]
        #_(assoc-in db [:schema table-name] column-specs))

      (upsert [this table-name values]
        (swap! db update-in [:data table-name] conj values))

      (selectOne [this table-name column value]
        (let [table-name (keyword table-name)]
          (println ">>>>" table-name)
          (->> db :data (some #(= (% column) value))))))))
