(ns clj-async-plgr.core)

(require '[clojure.core.async :as async :refer :all])

(let [c1 (timeout 500)
      c2 (timeout 300)]  
  (let [[v ch] (alts!! [c1 c2])]
    (println "Read" v "from" ch)))
