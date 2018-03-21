(ns simple.state1)

(def ^:dynamic *state*)

(defn apply-to [state function args]
  (binding [*state* state]
    (apply function args)))