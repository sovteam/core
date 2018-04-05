(ns sov.test-script
  (:require
    [sov.db.transient :refer [transient-database]]
    [sov.db.database :refer [*database*]]
    [simple.state1 :refer [*state*]]
    [simple.check2 :refer [check]]
    [io.aviso.exception :refer [write-exception]]
    [io.aviso.ansi :refer [yellow]]))

(def this-namespace (-> *ns* ns-name str))

(defn- relevant-stack-frame? [{:keys [package name]}]
  (cond
    (nil? package) :hide
    (-> package (.startsWith "java.")) :hide
    (-> package (.startsWith "sun.")) :hide
    (-> package (.startsWith "clojure.")) :hide
    (-> name    (.startsWith "clojure.")) :hide
    (-> name    (.startsWith "midje.")) :hide
    (-> name    (.startsWith this-namespace)) :hide
    :default :show))

(defn- exception-message? [result expected]
  (and
    (instance? Exception result)
    (.getMessage result)
    (instance? String expected)
    (-> result .getMessage (.contains expected))))

(defn- demunge [function]
  (-> function str clojure.repl/demunge (clojure.string/split #"@") first))

(defn- inc-pass-counter-if-using-midje! []
  (when-let [inc-pass-counter (resolve 'midje.emission.state/output-counters:inc:midje-passes!)]
    (inc-pass-counter)))

(defn- print-failure [description step# step-str expected result]
  (println "Failed: " (yellow description))
  (println "Step" (str step# ": ") (yellow step-str #_(demunge command)))
  #_(when (not-empty args)
    (println "Args:" args))
  (when (not= expected ::no-expectation)
    (println "Expected:" expected))
  (if (instance? Throwable result)
    (do
      (println "Thrown:" (or (.getMessage result) (-> result .getClass .getSimpleName)))
      (write-exception *out* result {:filter relevant-stack-frame?})
      (throw result))
    (do
      (println "  Actual:" result)
      (throw (RuntimeException. "Test failed")))))


(defn- safe-apply [f args]
  (try
    (apply f args)
    (catch Exception result
      result)))

(defn- arity [function]
  (-> function meta :arglists first count))

(defn- ->var [function]
  (-> function demunge symbol resolve))

(defn apply-command [description step# user operation script]
  (let [command (->var operation)
        arg-count (arity command)
        args (take arg-count script)
        result (safe-apply command args)]
    (if (instance? Throwable result)
      (print-failure description step# (str (demunge operation) (vec args)) ::no-expectation result)
      (inc-pass-counter-if-using-midje!))
    (drop arg-count script)))

(defn apply-query [description step# user query-path [expected & script-rest]]
  (println "IMPLEMENT query->step")
  script-rest)

(defn- consume-step [description step# user operation script]
  (if (vector? operation)
    (apply-query   description step# user operation script)
    (apply-command description step# user operation script)))

(defn- apply-script [description single-user script]
  (loop [step# 1
         script script]
    (let [user (or single-user (first script))
          script (cond-> script (not single-user) rest)
          [operation & script] script]
      (when operation
        (recur (inc step#)
               (consume-step description step# user operation script))))))

(defn script-user
  "Same as script, but takes an initial user argument, allowing the user to be
  omitted from the steps."
  [single-user description data & script]
  (let [db (transient-database data)]
    (binding [*database* db
              *state* (atom {})]
      (apply-script description single-user script))
    @db))

(defn script
  "Takes a test script description, its initial state and a sequence of steps.

  Each step, takes up 3 or 4 arguments:
   1) User
   2) function to be called on the state
   3) params map (ommited if function does not take one)
   4) Expected result

  Executes the function in each step, checking for the expected result.
  If an exception is thrown, the expected result is compared to the exception message.
  The state returned by the function is passed on to the next step.

  Returns the state after the last step. This is useful for starting new scripts using
  the state created in previous ones.

  Example:
  (def initial-state {})
  (def with-users (script \"Users become active when they log in.\"
    inital-state
    :ann login nil
    :bob login nil
    :ann active-users [:ann :bob]))
  (script \"Users become inactive when they log out.\"
    with-users
    :bob logout nil
    :ann active-users [:ann])"
  [description data & script]
  (apply script-user nil description data script))
