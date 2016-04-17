(ns test1.multmet)

(defmulti cda-format class)

(defrecord cda-field [^String field-name field-type])

(def a-field (->cda-field "field1" :date))

(println a-field)

(defmethod cda-format cda-field [v] (println "cda-field" "name:" (:field-name v)))

(cda-format a-field)
