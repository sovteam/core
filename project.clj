(defproject sov/core1 "1.0-SNAPSHOT"
  :description "The core Sov business logic."
  :url "https://github.com/sovteam/core"
  :license {:name "GNU General Public License v3.0"
            :url "http://www.gnu.org/licenses/gpl-3.0.html"}

  :java-source-paths ["src-java"]

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [simple/check "2.0.0"]]

  :profiles {:dev {:dependencies [[midje "1.8.3"]
                                  [io.aviso/pretty "0.1.34"]]}}
  :aot :all
  :jvm-opts ["-Dclojure.compiler.elide-meta=[:doc :file :line :added]"
             "-Dclojure.compiler.direct-linking=true"])
