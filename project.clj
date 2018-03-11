(defproject core "0.1.0-SNAPSHOT"
  :description "The core Sov business logic."
  :url "https://github.com/sovteam/core"
  :license {:name "GNU Geberal Public License v3.0"
            :url "http://www.gnu.org/licenses/gpl-3.0.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]]

  :aot :all
  :jvm-opts ["-Dclojure.compiler.elide-meta=[:doc :file :line :added]"
             "-Dclojure.compiler.direct-linking=true"])
