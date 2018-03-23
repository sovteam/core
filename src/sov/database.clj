(ns sov.database)

(gen-interface :name sov.Database
               :methods [[createTable [String java.util.List] void]
                         [upsert [String java.util.Map] void]
                         [selectOne [String String Object] Object]])

(def ^:dynamic *database* nil)
