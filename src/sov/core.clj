(ns sov.core
  (:require
    [simple.state1 :refer [*state*]]
    [sov.db.database :as db])
  (:gen-class
    :methods [^:static [init [java.util.Map] java.util.Queue]])
  (:import (java.util Queue Map)))

#_(defn update-schema [database]
  (.createTable database "property"
                [["key"   "TEXT" "UNIQUE"]
                 ["value" "TEXT"]]))

(defn properties [] (:properties @*state*))

(defn property-set! [key new-value]
  (if (contains? (properties) key)
    (db/insert! "properties" {"key" (name key)  "value" new-value})
    (db/update! "properties" "key" (name key) {"value" new-value}))
  (swap! *state* assoc-in [:properties key] new-value))

(defn own-name-set [new-name]
  (property-set! :own-name new-name))

(defn own-nick-set [new-nick]
  (property-set! :own-nick new-nick))

(defn init-db []
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

