(ns sov.core-test
  (:require
    [sov.core :refer [init-db own-name-set own-nick-set]]
    [sov.test-script :refer [script-user]]))

; (do (require 'midje.repl) (midje.repl/autotest))

(def initial-state
  (script-user :ann "New Sov installation" {} init-db))

(def new-user-ann
  (script-user :ann "Ann enters own name on first use."
    initial-state
    [:profile :own-name] nil
    [:profile :own-nick] nil
    own-name-set "Ann Smith"
    own-nick-set "Annie"
    [:profile :own-name] "Ann Smith"
    [:profile :own-nick] "Annie"))

