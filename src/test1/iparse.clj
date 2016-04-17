(ns test1.iparse)

(require '[clojure.pprint :as ppr])
(require '[instaparse.core :as insta])

(def as-and-bs
  (insta/parser
   "S = AB*
     AB = A B
     A = 'a'+
     B = 'b'+"))

(as-and-bs "aaaaabbbaaaabb")
(type (as-and-bs "aaaaabbbaaaabb"))

(as-and-bs "aaaaabbbaaa")
(type (as-and-bs "aaaaabbbaaa"))
(ppr/pprint (as-and-bs "aaaaabbbaaa"))

(def cdap 
  (insta/parser 
   "S = R B+
        R = 'r'"))
