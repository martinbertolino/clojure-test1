(ns test1.core)

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(foo "testing")

;; Extract simple words from a sentence
(re-seq #"\w+" "My Favorite Things")

(def a-string "this is a string")

(.length a-string)
(count a-string)

(def parse-re #"(?i)^(;)?([0-9A-Z_]+),(.*)$")

(def sample0 ";NA_UNUSED, do not use")
(def sample1 "NCP_TAG,  always 'NCP01' to identify record type and layout version")

(re-matches parse-re sample0)
(def zzz (re-matches parse-re sample1))
(type zzz)

(doseq [x (range 10)] {(symbol (str x)) [x (str x)]})

(def raw-file-layout ["NCP_TAG,  always 'NCP01' to identify record type and layout version"
                      "NC_ACCTNUM, customer acct number"
                      "NA_FIRST_NAME,"
                      "NA_LAST_NAME,"
                      ";NA_UNUSED, do not use"])

(doseq [l raw-file-layout] (println (re-matches parse-re l)))

(doseq [l raw-file-layout] (doseq [m  (re-matches parse-re l)] (println "m-> " m (count m))))

(defn parse-line-def [line]
  (let [parts (re-matches #"(?i)^(;)?([0-9A-Z_]+),(.*)$" line)]
    (if (= 4 (count parts))
      {:commented (not (nil? (parts 1))) :name (parts 2) :comment (parts 0)}
      {:commented true :name "UNKNOWN" :comment "N/A"})))

(parse-line-def sample0)

(doseq [l raw-file-layout] (println (parse-line-def l)))

(defn parse-def [lines]
  "parse the the definition lines for later processing"
  (defn parse-line-def [line]
    (let [parts (re-matches #"(?i)^(;)?([0-9A-Z_]+),(.*)$" line)]
      (if (= 4 (count parts))
        {:commented (not (nil? (parts 1))) :name (parts 2) :comment (parts 0)}
        {:commented true :name "UNKNOWN" :comment "N/A"})))
  (into [] (for [l lines] (parse-line-def l))))

(parse-def raw-file-layout)

(def fdef (parse-def raw-file-layout))

(require '[clojure.pprint :as ppr])

(ppr/pprint "12")
(ppr/pprint fdef)

(def sample-list ["l1f2|l1f2|l1f3|l1f4|l1f5"
                  "l2f2|l2f2|l2f3|l2f4|l2f5"
                  "l3f2|l3f2|l3f3|l3f4|l3f5"
                  "l4f2|l4f2|l4f3|l4f4|l4f5"])

(defn fun1 [n]
  "test1"
  (defn fun2 [n] (* n n n))
  (+ (fun2 n) (fun2 n)))

(fun1 3)

(defonce q 1)

(def qwerty (atom 10000))

(swap! qwerty inc)
(swap! qwerty + 42)

; end
