(ns sov.core-test
  (:require
    [sov.core :refer [init own-name own-nick own-name-set own-nick-set]]
    [sov.db.test-script :refer [script]]))

; (do (require 'midje.repl) (midje.repl/autotest))

(def initial-state
  (script "New Sov installation" {} init))

(def new-user-ann
  (script "User Ann enters own name on first use."
    initial-state
    own-name                 nil
    own-nick                 nil
    own-name-set "Ann Smith"
    own-nick-set "Annie"
    own-name                 "Ann Smith"
    own-nick                 "Annie"))

