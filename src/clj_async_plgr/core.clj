(ns clj-async-plgr.core)

(require '[clojure.core.async :as async :refer :all])

(def throughput-c (chan))
(def c (chan))
(def timeout-future
  (future 
    (Thread/sleep 5000)
    (>! c :timeout)
    (close! c)))

(defn get-data-1[]
  (if (realized? timeout-future)
    :timeout
    (do
      (fn [x]
        (>>! c x))
      (go (>! throughput-c :delay))
      (<!! c))))

(defn get-data-2[]
  (if (realized? timeout-future)
    :timeout
    (do
      (if (= :timout (first (alts!! [throughput-c c])))
        :timeout)
      (fn [x]
        (>>! c x))
      (go (>! throughput-c :delay))
      (<!! c))))

(let [c (promise-chan)]
  (go
    (>!! c "hello")
    (>!! c "man"))
  (println (<!! c))
  (println (<!! c))
  (close! c))
