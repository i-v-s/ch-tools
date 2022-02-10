(ns house.common
  (:require [clojure.string :as str]))

(defn str-some [& args] (apply str (filter some? args)))

(defn comma-join
  "Join items with comma"
  [items]
  (clojure.string/join ", " items))

(defn group-by-contains
  [possible values]
  (let [{not-exists false exists true}
        (group-by (partial contains? possible) values)]
    [exists not-exists]))

(defn vec-to-map-of-vec
  "Convert vector of records to map of vectors"
  [key value coll]
  (reduce
   (fn [coll item]
     (let [kr (key item)]
       (assoc coll kr (conj (get coll kr []) (value item)))))
   {} coll))
