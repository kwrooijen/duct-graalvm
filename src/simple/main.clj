(ns simple.main
  (:require
   [simple.loader]
   [duct.core :as duct]
   [clojure.java.io :as io]
   [duct.module.web]
   [duct.module.logging])
  (:gen-class))

(duct/load-hierarchy)

(simple.loader/require-from-config "simple/config.edn")

(defn -main [& args]
  (let [keys     (or (duct/parse-keys args) [:duct/daemon])
        profiles [:duct.profile/prod]]
    (-> (io/resource "simple/config.edn")
        (duct/read-config)
        (duct/exec-config profiles keys))))
