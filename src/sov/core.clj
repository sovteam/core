(ns sov.core
  (:require
    [simple.state1 :refer [*state*]])
  (:gen-class
    :methods [^:static [init [java.util.Map] java.util.Queue]])
  (:import (java.util Queue Map)))

(defn update-schema [database]
  (.createTable database "property"
                [["key"   "TEXT" "UNIQUE"]
                 ["value" "TEXT"]]))

(defn database []
  (:database *state*))

(defn upsert [table-name values]
  (println "db" (database))
  (set! *state* {:database
                 (.upsert (database) (name table-name)
                          (into {} (map (fn [[k v]]
                                          [(name k) v])
                                        values)))}))

(defn select-one [table-name column value]
  (->> (.selectOne (database) (name table-name) (name column) value)
    (into {} (map (fn [[col value]]
                    [(-> col .toLowerCase keyword) value])))))

(defn property [key]
  (-> (select-one :property :key key) :value))

(defn property-set! [property new-value]
  (upsert :property {:key property, :value new-value}))

(defn own-name []
  (property :own-name))

(defn own-nick []
  (property :own-nick))

(defn ^:command own-name-set [new-name]
  (property-set! :own-name new-name))

(defn ^:command own-nick-set [new-nick]
  (property-set! :own-nick new-nick))

(defn init []
  )

(defn -init [{:strs [database ^Queue state-out-queue]}]
  (alter-var-root *state* assoc :database database)

  #_(println
    (.echo database
          (str "CREATE TABLE IF NOT EXISTS PROPERTY ("
               "ID INTEGER PRIMARY KEY,"
               "NAME TEXT)")))
  (reify Queue
    (add [this [function & args]]
      (let [function (resolve (symbol function))]
        (.add state-out-queue (apply function args))))))

