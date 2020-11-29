(ns clj-async-plgr.core)

(require '[clojure.core.async :as async :refer :all])

(def c (chan))
(def timeout-future
  (future 
    (Thread/sleep 5000)
    (>! c :timeout)
    (close! c)))

(defn get-data[]
  (when (not (realized? timeout-future))
    (fn [x]
      (>!! c x))
    (<!! c))
