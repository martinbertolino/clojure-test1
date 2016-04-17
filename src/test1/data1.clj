(ns test1.data1)

(require '[clojure.string :as cstr])
(require '[clj-fuzzy.metrics :as cljfm])
(require '[clj-fuzzy.stemmers :as cljfs])
(require '[clj-fuzzy.phonetics :as cljfp])

(def input-record {:first "first"
                   :last "last"
                   :middle "middle"
                   :address1 "123 main st."
                   :address2 ""
                   :city "anytown"
                   :state "MO"
                   :zip "63011"})

(defn normalize [record]
  "normalize the information in the record"
  (into {} (map (fn [[k v]] [k (cstr/upper-case v)]) record)))

(normalize input-record)

(pr-str input-record)

(cljfp/double-metaphone "bertolino")
(cljfp/double-metaphone "BERTOLINO")
(cljfp/double-metaphone "BERTOLINi")
(cljfp/double-metaphone "BERTOLoNi")

(cljfp/metaphone "bertolino")
(cljfp/metaphone "BERTOLINO")
(cljfp/metaphone "BERTOLINi")
(cljfp/metaphone "BERTOLoNi")

(cljfp/metaphone "TO")

(cljfm/jaro-winkler "martin" "martini")
(cljfm/jaro-winkler "robert" "roberto")
(cljfm/jaro-winkler "robert" "robert")
(cljfm/jaro-winkler "roberta" "robert")

; end
