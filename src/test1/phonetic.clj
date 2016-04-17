(ns test1.phonetic)

(require '[clojure.string :as cstr])
(require '[me.raynes.fs :as fs]) 
(require '[clj-fuzzy.phonetics :as cljfp])
(require '[clj-fuzzy.metrics :as cljfm])

; load the data and pre-compute the phonetic encodings
(def index-data
  (->>
   (fs/expand-home "~/test-data/all-names.txt")
   (slurp)
   (cstr/split-lines)
   (pmap cstr/upper-case)
   (pmap #(-> {:clear % 
               :metaphone (cljfp/metaphone %) 
               :double-metaphone (cljfp/double-metaphone %)
               :soundex (cljfp/soundex %)
               :soundex3 (subs (cljfp/soundex %) 0 3)
               :nysiis (cljfp/nysiis %)
               :caverphone (cljfp/caverphone %)}))
   (vec)))

; inspect
(count index-data)
(dorun (map #(println %) (take 10 index-data)))
(comment (dorun (map #(println %) (take 10 (shuffle index-data)))))

(defn get-encoding [value encoding]
  (let [uvalue (cstr/upper-case value)] 
    (condp = encoding
      :clear uvalue
      :metaphone (cljfp/metaphone uvalue) 
      :double-metaphone (cljfp/double-metaphone uvalue)
      :soundex (cljfp/soundex uvalue)
      :soundex3 (subs (cljfp/soundex uvalue) 0 3)
      :nysiis (cljfp/nysiis uvalue)
      :caverphone (cljfp/caverphone uvalue)
      :else (throw (Exception. (str "invalid encoding " encoding))))))

(get-encoding "chris" :clear)
(get-encoding "chris" :metaphone)
(get-encoding "chris" :double-metaphone)
(get-encoding "chris" :qqqq)

(def encodings [:clear :metaphone :double-metaphone :soundex :soundex3 :nysiis :caverphone])

(let [value "chris"]
  (dorun (map #(println % "->" (get-encoding value %)) encodings)))

; re-implementing this function to use a different store is all that would be needed to use a different store
(defn get-candidates [value index encoding]
  (let [match-key (get-encoding value encoding)]
    (filter #(= (% encoding) match-key) index)))

(get-candidates "chris" index-data :soundex)
(get-candidates "chris" index-data :metaphone)
(get-candidates "chris" index-data :double-metaphone)

(let [value "smith"]
  (dorun (map #(println % "->" (count (get-candidates value index-data %))) encodings)))

(let [value "smith"
      encoding :metaphone]
  (dorun (map #(println %) (get-candidates value index-data encoding))))

(let [value "bertoloni"
      encoding :caverphone]
  (dorun (map #(println % (cljfm/jaro-winkler (cstr/upper-case value) (% :clear))) (get-candidates value index-data encoding))))

(let [value "smith"
      encoding :soundex3]
  (dorun (map #(println (% :clear) encoding "->" (% encoding) "JW ->" (cljfm/jaro-winkler (cstr/upper-case value) (% :clear))) (get-candidates value index-data encoding))))

; TODO
; - compute string distances for the candidates
; - find candidates using string distances instead?
; - show performance differences using map/pmap
; - use all of the distinct names/last names in the credit file
; - switch from in-memory to Cassandra? show how most of the code should remain the same
; - at some point we need a baseline to compare to, human needs to review the results and tweak -> D&A


; end
