(defproject sov/core1 "1.0-SNAPSHOT"
  :description "The core Sov business logic."
  :url "https://github.com/sovteam/core"
  :license {:name "GNU General Public License v3.0"
            :url "http://www.gnu.org/licenses/gpl-3.0.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]]

  :profiles {:dev {:dependencies [[midje "1.8.3"]

                                  ;[simple/test-script1 "0.1.1"]
                                    [cheshire "5.8.0"] ; JSON for serializing test arguments.
                                    [io.aviso/pretty "0.1.34"]

                                  ]}}
  :aot :all
  :jvm-opts ["-Dclojure.compiler.elide-meta=[:doc :file :line :added]"
             "-Dclojure.compiler.direct-linking=true"])
