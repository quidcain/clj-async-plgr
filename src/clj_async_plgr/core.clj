(ns clj-async-plgr.core)

(require '[clojure.core.async :as async :refer :all])

(let [c (chan)]
  (thread (>!! c "hello"))
  (assert (= "hello" (<!! c)))
  (close! c))

(let [c (chan)]
  (thread (Thread/sleep 3000) (>!! c "hello"))
  (println (<!! c))
  (close! c))

(let [c1 (chan)
      c2 (chan)]
    (thread (while true
            (let [[v ch] (alts!! [c1 c2])]
              (println "Read" v "from" ch))))
    (>!! c1 "hi")
    (>!! c2 "there"))
