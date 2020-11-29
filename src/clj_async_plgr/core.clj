(ns clj-async-plgr.core)

(require '[clojure.core.async :as async :refer :all])

(def throughput-c (chan))
(def c (chan))
(def timeout-future
  (future 
    (Thread/sleep 5000)
    (>! c :timeout)
    (close! c)))

(defn a-delay
  (go
    (<! (timeout 1000))
    (>! throughput-c :ready)))

(defn get-reviews []
  (Thread/sleep 2500)
  {:reviews [
    {:book {:id 1}}
    {:book {:id 2}}
    {:book {:id 3}}]})

(defn w-throughput [f]
  (if (realized? timeout-future)
    :timeout
    (do
      (if (= :timeout (first (alts!! [throughput-c c])))
        :timeout
        (do
          (>>! c (f))
          (a-delay)
          (<!! c))))))
