(ns clj-async-plgr.core)

(require '[clojure.core.async :as async :refer :all])

(let [c1 (chan)
      c2 (chan)]
  (go
    (let [[v ch] (alts! [c1 c2])]
    (println "Read" v "from" ch)))
  (>!! c1 "hi")
  (>!! c2 "there"))
