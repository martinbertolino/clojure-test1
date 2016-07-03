(ns test1.mytwn)

(require '[schema.core :as s])
(require '[clj-time.core :as t])

(s/defrecord employee-information
             [date-first-reported :- org.joda.time.DateTime
              date-last-reported :- org.joda.time.DateTime
              id :- s/Str
              first-name :- s/Str
              last-name :- s/Str])

(s/defrecord misc-information
             [date-first-reported :- org.joda.time.DateTime
              date-last-reported :- org.joda.time.DateTime
              as-of-date :- org.joda.time.DateTime
              months-of-service :- s/Int])

(s/defrecord income-information
             [date-first-reported :- org.joda.time.DateTime
              date-last-reported :- org.joda.time.DateTime
              income-year :- s/Int
              gross-income :- s/Num
              net-income :- s/Num])

(s/defrecord verification-of-employment
             [as-of-date :- java.util.Date])

; end of file
