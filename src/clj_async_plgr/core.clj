(ns clj-async-plgr.core)

(require '[clojure.core.async :as async :refer :all])

(def throughput-c (chan))
(def timeout-future
  (future 
    (Thread/sleep 5000)))

(defn a-delay []
  (go
    (<! (timeout 1000))
    (>! throughput-c :ready)))

(defn get-reviews []
  (Thread/sleep 2500)
  {:reviews [
    {:book {:id 1}}
    {:book {:id 2}}
    {:book {:id 3}}]})

(defn w-thrpt-and-timeout [f]
  (if (realized? timeout-future)
    :timeout
    (do
      (<!! throughput-c)
      (a-delay)
      (f))))

(put! throughput-c :ready)
(w-thrpt-and-timeout get-reviews)
