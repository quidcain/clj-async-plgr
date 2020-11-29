(ns clj-async-plgr.core)

(require '[clojure.core.async :as async :refer :all])

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))
