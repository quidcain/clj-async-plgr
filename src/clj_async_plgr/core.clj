(ns clj-async-plgr.core
  (:require
    [clojure.core.async :as a]))

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

(defn timeout-future [ms]
  (future
    (Thread/sleep 1000)))

(defn a-delay [c]
  (go
    (<! (a/timeout 1000))
    (>! c :ready)))

(defn w-thrpt-and-timeout [ftr c]
  (put! c :ready)
  (fn [f & args]
    (if (realized? ftr)
      :timeout
      (do
        (<!! c)
        (a-delay c)
        (apply f args)))))

(let [throughput-c (a/chan)
      timeout-future (timeout-future 1000)
      w-thrpt-and-timeout (w-thrpt-and-timeout timeout-future throughput-c)
      get-reviews (partial w-thrpt-and-timeout get-reviews)
      get-book-info (partial w-thrpt-and-timeout get-book-info)
      reviews (get-reviews)
    ]
  (if-not (= reviews :timeout)
    (map
      #(-> % :book :id get-book-info)
      (:reviews reviews))
    :timeout))
