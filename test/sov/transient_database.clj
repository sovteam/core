(ns sov.transient-database)

; This interface defines has the same methods as
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

(defn init-database []
  {:database (doto (transient-database) update-schema)})

(try
  (def own-name-ann
    (script "User enters own name on first use."
            (init-database)
            :ann own-name                 nil
            :ann own-nick                 nil
            :ann own-name-set "Ann Smith" nil
            :ann own-nick-set "Wakanda"   nil))
  (catch Exception e (.printStackTrace e)))

; (do (require 'midje.repl) (midje.repl/autotest))
