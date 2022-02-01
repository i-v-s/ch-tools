(ns ch-tools.ch-test
  (:require [clojure.test :refer :all]
            [ch-tools.ch :as ch]))

(def ^:dynamic *conn* nil)

(defn db-fixture [f]
  (let [url "jdbc:clickhouse://127.0.0.1:9000/test"]
    (ch/create-db! url)
    (binding [*conn* (ch/connect url)]
      (f)
      (ch/exec! *conn* "DROP DATABASE test"))))

(def rec1 [(array-map
            :id "Int32 CODEC(Delta(4), LZ4)"
            :time "DateTime"
            :buy "UInt8"
            :price "Float64 CODEC(Gorilla)"
            :coin "Float32"
            :base "Float32")
           :engine "ReplacingMergeTree()"
           :partition-by "toYYYYMM(time)"
           :order-by ["id"]])

(def rec2 [(array-map
            :time "DateTime"
            :price "Float64 CODEC(Gorilla)"
            :base "Float32")
           :engine "ReplacingMergeTree()"
           :partition-by "toYYYYMM(time)"
           :order-by ["time" "price"]])

(defn create-table
  [size]
  (ch/ensure-tables! *conn* {"tab1" rec1})
  (ch/insert-many!
   *conn*
   (ch/insert-query "tab1" (first rec1))
   (for [i (range size)]
     [i (java.sql.Timestamp. (System/currentTimeMillis)) 0 (rand) (rand) (rand)])))

(use-fixtures :each db-fixture)

(deftest basic-test
  (testing "Fetch tables."
    (is (empty? (ch/fetch-tables *conn*)))
    (ch/ensure-tables! *conn* {"tab1" rec1 "tab2" rec2})
    (is (= 2 (count (ch/fetch-tables *conn*))))))

(deftest freeze-test
  (create-table 1000))
