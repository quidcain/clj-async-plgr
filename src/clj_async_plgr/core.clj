(ns clj-async-plgr.core)

(require '[clojure.core.async :as async :refer :all])

(let [c1 (chan)]
  (>!! c1 "hi"))

(let [c1 (chan)]
  (>!! c1 "hi")
  (close! c1))
