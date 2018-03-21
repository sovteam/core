(ns sov.core-test
  (:require
    [sov.core :refer [update-schema own-name own-nick own-name-set own-nick-set]]
    [simple.test-script2 :refer [script script-user]]))

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
