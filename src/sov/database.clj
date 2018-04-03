(ns sov.database
  (:refer-clojure :exclude [update]))

(gen-interface :name sov.Database
               :methods [[createTable [#_table-name   String
                                       #_column-specs java.util.List] void]

                         [insert [#_table-name String
                                  #_record     java.util.Map] void] ; Might return id?

                         [update [#_table-name String
                                  #_pk-column  String
                                  #_pk-value   String
                                  #_record     java.util.Map] void]

                         [select [#_table-name String
                                  #_where      String
                                  #_where-args java.util.List]
                          #_records "[[Ljava.lang.Object;"] ; Object[][] - Column names in the first element, records follow.
                         ])

(def ^:dynamic ^sov.Database *database*)

(defn insert [table record]
  (.insert *database* (name table) record))

(defn update [table pk-column pk-value record]
  (.update *database* table pk-column pk-value record))

(defn select-one [table-name column value]
  (->> (.select *database* table-name column value)
    (into {} (map (fn [[col value]]
                    [(-> col .toLowerCase keyword) value])))))
