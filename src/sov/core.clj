(ns sov.core
  (:gen-class
    :methods [^:static [init [java.util.Queue] java.util.Queue]])
  (:import (java.util Queue)))

(defn -init [state-out-queue]
  (reify Queue
    (add [this event]
      (.add state-out-queue {"echo" (str "echo: " event)}))))