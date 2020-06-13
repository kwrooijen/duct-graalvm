(ns simple.main
  (:gen-class)
  (:require [duct.core :as duct]
            [clojure.java.io :as io]))

(duct/load-hierarchy)

(defn spy [v x]
  (println "SPYING:"x " " v)
  v)

(defn -main [& args]
  (let [keys     (or (duct/parse-keys args) [:duct/daemon])
        profiles [:duct.profile/prod]]
    (-> (io/resource "config.edn")
        (spy "RESOURCE")
        (duct/read-config)
        (spy "READ")
        (duct/exec-config profiles keys))))
