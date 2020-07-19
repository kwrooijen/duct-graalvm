(ns simple.main
  (:require
   [duct.core :as duct]
   [clojure.java.io :as io]
   [duct.module.web]
   [duct.module.sql]
   [duct.module.logging]
   [duct.handler.static]
   [duct.server.http.http-kit]
   [duct.handler.root]
   [duct.middleware.web]
   [duct.logger.timbre]
   [duct.router.ataraxy]
   [simple.handler.test]
   [duct.database.sql.hikaricp]
   [duct.router.cascading])
  (:gen-class))

(duct/load-hierarchy)

(defn -main [& args]
  (let [keys     (or (duct/parse-keys args) [:duct/daemon])
        profiles [:duct.profile/prod]]
    (-> (io/resource "simple/config.edn")
        (duct/read-config)
        (duct/exec-config profiles keys))))
