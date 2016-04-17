(ns test1.rdescpar)

(defn load-lines [filename]
  (seq (clojure.string/split-lines (slurp filename))))

(def sample (load-lines "sample.txt"))

(defn parse-cda [cda-lines]
  (defn parse-r [l]
    (let [prec (re-matches #"^r\|(\w+)" l)]
      {:record (prec 1)}))
  (defn internal-parse-cda [cda-lines cda]
    (if (seq cda-lines)
      (let [cl (first cda-lines) rl (rest cda-lines)]
        (do
          (println "cda" cda)
          (cond
            (re-find #"^##" cl) (do (println "comment" cl) (internal-parse-cda rl cda))
            (re-find #"^r" cl) (do (println "record" cl) (internal-parse-cda rl {:root (parse-r cl)})) 
            (re-find #"^b" cl) (do (println "block" cl) (internal-parse-cda rl cda))
            (re-find #"^v" cl) (do (println "field" cl) (internal-parse-cda rl cda))
            (re-find #"^I" cl) (do (println "iterator" cl) (internal-parse-cda rl cda))
            :else (do (println "OTHER" cl) (internal-parse-cda rl cda)))))
      cda))
  (internal-parse-cda cda-lines {}))

(parse-cda sample)

(parse-cda '("## record"))
