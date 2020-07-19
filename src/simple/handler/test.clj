(ns simple.handler.test
  (:require
   [integrant.core :as ig]))

(defmethod ig/init-key :simple.handler/test [_ opts]
  (fn [_request]
    (println "SIMPLE TEST")
    {:status 200}))
