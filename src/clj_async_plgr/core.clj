(ns clj-async-plgr.core)

(require '[clojure.core.async :as async :refer :all])

(def c (chan))
(go 
  (Thread/sleep 5000)
  (>! c :timeout)
  (close! c))
(<!! c)

(def timeout-future
  (future (Thread/sleep 5000)))
(realized? timeout-future)
