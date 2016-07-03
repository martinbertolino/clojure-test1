(ns test1.explore)

(require '[clojure.repl :as repl])
(require '[clojure.pprint :as ppr])

(repl/doc map)

(println *ns*)

(def testv [[:name "ab"] [:remark "remark ab"] [:value [:name "f1"]] [:value [:name "f2"]]])

(ppr/pprint testv) 

(group-by #(nth % 0) testv)
(ppr/pprint (group-by #(nth % 0) testv))

(map
 (fn [[key val]]
   (condp = key
     :name (val 0)
     :remark (val 0)
     :value [:values val 0]))
 (group-by #(nth % 0) testv))

(ppr/pprint 
 (map
  (fn [[key val]]
    (condp = key
      :name (val 0)
      :remark (val 0)
      :value [:values val]))
  (group-by #(nth % 0) testv)))

(defn block-transform [block-children]
  (map
   (fn [[key val]]
     (condp = key
       :name (val 0)
       :remark (val 0)
       :value [:values val]))
   (group-by #(nth % 0) block-children)))

(block-transform testv)

; exploration of namespaces

(repl/dir clojure.pprint)
(repl/dir clojure.core)

(ns-publics 'clojure.pprint)
(keys (ns-publics 'clojure.pprint))

(ns-map 'clojure.pprint)
(ns-interns 'clojure.pprint)
(ns-publics 'clojure.pprint)
(ns-imports 'clojure.pprint)

(ns-imports *ns*)

(ns-aliases *ns*)

(require '[clj-time.core :as t])

(t/date-time 1986 10 14 4 3 27 456)
(ppr/pprint (t/date-time 1986 10 14 4 3 27 456))
