(ns test1.destruct)

(require '[clojure.string :as cstr])

(require '[me.raynes.fs :as fs]) 

(require '[clj-fuzzy.phonetics :as cljfp])

(let [{flower1 :flower1 :as all-flowers} {:flower1 "red"}]
  [flower1 all-flowers])

(defn lazy-open [file]
  (defn helper [rdr]
    (lazy-seq
     (if-let [line (.readLine rdr)]
       (cons line (helper rdr))
       (do (.close rdr) (println "closed") nil))))
  (lazy-seq
   (do (println "opening")
       (helper (clojure.java.io/reader file)))))

(->>
 (lazy-open "/etc/passwd")
 count)

(->>
 (lazy-open (fs/expand-home "~/test-data/last-names.txt"))
 count)

(def first-names
  (vec
   (doall (->>
           (lazy-open (fs/expand-home "~/test-data/first-names.txt"))
           (map cstr/upper-case)))))

(class first-names)
(count first-names)
(rand-nth first-names)

; for yields a sequence

(for [animal [:mouse :duck :lory]]
  (str (name animal)))

(for [animal [:mouse :duck :lory]
      color  [:red :blue]]
  (str (name color) (name animal)))

; dorun forces the traversal of a lazy sequence
; important if the functions involved have side effects?
; dorun does NOT hold on to the head of the sequence

(dorun 5 (repeatedly #(println "hi")))
(take 5 (repeatedly #(println "hi")))

(dorun 5 (cycle [:a :b :c]))
(dorun 7 (map #(println "cycle" %) (cycle [:a :b :c])))
(take 7 (cycle [:a :b :c]))

(dorun (map #(println "hi" %) ["mum" "dad" "sister"]))

; doall RETAINS the sequence head and then returns it (watch for memory usage...)

; returns the sequence (nil, nil, nil)
(doall (map println [1 2 3]))
(doall (map println (range 10)))
(doall (map #(do (println %) (+ 1000 % %)) (range 10)))

; return nil
(dorun (map println [1 2 3]))
(dorun (map println (range 10)))
(dorun 3 (map println (range 10)))

; doseq

(doseq [x (range 3) y ["a" "b" "c"]] (println x y))
; vs.


(slurp (fs/expand-home "~/test-data/sample.txt"))
(cstr/split-lines (slurp (fs/expand-home "~/test-data/sample.txt")))

(take 15 (shuffle (cstr/split-lines (slurp (fs/expand-home "~/test-data/first-names.txt")))))
(take 15 (shuffle (cstr/split-lines (slurp (fs/expand-home "~/test-data/last-names.txt")))))

(map cstr/upper-case (take 15 (shuffle (cstr/split-lines (slurp (fs/expand-home "~/test-data/last-names.txt"))))))

(->>
 (fs/expand-home "~/test-data/last-names.txt")
 (slurp)
 (cstr/split-lines)
 (shuffle)
 (take 15)
 (map cstr/upper-case))

(defn random-item-from-file-generator 
  "note that this function is limited in how many entries it can process"
  ([filename] (random-item-from-file-generator filename Integer/MAX_VALUE))
  ([filename sample-size]
   (let [items (->>
                filename
                (slurp)
                (cstr/split-lines)
                (shuffle)
                (take sample-size)
                (map cstr/upper-case))] 
     (fn [] (rand-nth items)))))

(def rand-fn (random-item-from-file-generator (fs/expand-home "~/test-data/last-names.txt") 100))

(rand-fn)

(def curr-id (atom 1000000))

(defn next-id []
  (swap! curr-id inc))

(Integer/toString (next-id) 36)

(defn next-id-str [] (Integer/toString (next-id) 36))

(next-id-str)

(def rand-first-name (random-item-from-file-generator (fs/expand-home "~/test-data/first-names.txt")))
(def rand-middle-name (random-item-from-file-generator (fs/expand-home "~/test-data/first-names.txt")))
(def rand-last-name (random-item-from-file-generator (fs/expand-home "~/test-data/first-names.txt")))

(defn rand-person []
  [(next-id-str) (rand-first-name) (rand-middle-name) (rand-last-name)])

(rand-person)

(->>
 (rand-person)
 (interpose "|")
 (apply str))

(defn write-random-people [filename how-many]
  (with-open [wrtr (clojure.java.io/writer filename)]
    (dorun how-many
           (repeatedly #(do
                          (.write wrtr (->>
                                        (rand-person)
                                        (interpose "|")
                                        (apply str)))
                          (.newLine wrtr))))))

(comment (write-random-people (fs/expand-home "~/test-data/random-people.txt") 10000))

; see http://www.markhneedham.com/blog/2013/09/23/clojure-anonymous-functions-using-short-notation-and-the-arityexception-wrong-number-of-args-0-passed-to-persistentvector/ why we need this
(#({:clear (str %)}) "test1")
(#(-> {:clear (str %)}) "test1")

(defn mkd [v] {:clear v})
(mkd "test1")

{:clear "test1"}

;end
