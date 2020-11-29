(ns clj-async-plgr.core)

(require '[clojure.core.async :as async :refer :all])

(def throughput-c (chan))
(def timeout-c (chan))
(def timeout-future
  (future
    (Thread/sleep 5000)
    (>! timeout-c :timeout)
    (close! timeout-c)))

(defn get-data[]
  (when (not (realized? timeout-future))
    (fn [x]
      (>>! throughput-c x))
    (alts!! [c1 c2]))
