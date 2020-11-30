(ns clj-async-plgr.core
  (:require
    [clojure.core.async :as a]))

(def throughput-c (a/chan))
(def timeout-future
  (future 
    (Thread/sleep 1000)))

(defn a-delay []
  (go
    (<! (a/timeout 1000))
    (>! throughput-c :ready)))

(defn w-thrpt-and-timeout [f & args]
  (if (realized? timeout-future)
    :timeout
    (do
      (<!! throughput-c)
      (a-delay)
      (apply f args))))

(defn get-reviews []
  (Thread/sleep 2500)
  {:reviews [
    {:book {:id 1}}
    {:book {:id 2}}
    {:book {:id 3}}]})

(defn get-book-info [id]
  (Thread/sleep 2500)
  {:id :id
   :author "Eric"
   :rating 4.3})

(put! throughput-c :ready)
#_(let [get-reviews (partial w-thrpt-and-timeout get-reviews)
      reviews (get-reviews)]
  (if-not (= reviews :timeout)
    reviews
    :timeout))

((partial w-thrpt-and-timeout get-reviews))
((partial w-thrpt-and-timeout (fn [] 4)))
