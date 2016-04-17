(ns test1.cdaparse)

(require '[clojure.pprint :as ppr])
(require '[instaparse.core :as insta])

(def cda0 "r|CX|root record")

(def cda1 "r|PX|root record for persons")

(def cda2 
  "r|ZX|root record Z \r
    b|ZX|block ZZ")

(def cda3 
  "r|ZX|root record Z \r
    b|ZX|block ZZ \r
    b|YX|block YX \r")

(def cda4 
  "r|ZX|root record Z \r
    b|ZX|block ZZ \r
    v|Field1 | this is field 1 \r
    v|Field2 | this is field 2 \r
    b|YX|block YX")

(def cda-parse0
  (insta/parser
   "CDA = record (block)*
        record = <ws> <'r'> <ws> <'|'> <ws> name <ws> <'|'> <ws> remark <eol>*
        block = <ws> <'b'> <ws> <'|'> <ws> name <ws> <'|'> <ws> remark <eol>* (value)*
        value = <ws> <'v'> <ws> <'|'> <ws> name <ws> <'|'> <ws> remark <eol>*
        name = #'[a-zA-Z\\+-]([0-9a-zA-Z\\+-]*)'
        remark = #'[^|^\\r^\\n.]*'
        ws = #'[ \\t\\x0B\\f]*'
        eol = #'[\\r\\n]+'
        xxws = #'\\s*'"))

(cda-parse0 cda0)
(cda-parse0 cda1)
(cda-parse0 cda2)
(ppr/pprint (cda-parse0 cda3))
(ppr/pprint (cda-parse0 cda4))
