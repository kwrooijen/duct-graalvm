(ns simple.loader
  (:require
   [clojure.java.io :as io]
   [duct.core :as duct]
   [integrant.core :as ig]
   [weavejester.dependency :as dep]
   [clojure.set :as set]))

(defn- find-keys [config keys f]
  (let [graph  (ig/dependency-graph config {:include-refsets? false})
        keyset (set (mapcat #(map key (ig/find-derived config %)) keys))]
    (->> (f graph keyset)
         (set/union keyset)
         (sort (ig/key-comparator (ig/dependency-graph config))))))

(defn dependent-keys [config keys]
  (find-keys config keys dep/transitive-dependencies-set))

(defn- keyword->namespaces [kw]
  (when-let [ns (namespace kw)]
    [(symbol ns)
     (symbol (str ns "." (name kw)))]))

(defn- key->namespaces [k]
     (if (vector? k)
       (mapcat keyword->namespaces k)
       (keyword->namespaces k)))

(defn all-namespaces [config]
  (->> (dependent-keys config (keys config))
       (mapcat #(conj (ancestors %) %))
       (mapcat key->namespaces)
       (distinct)))

(defmacro try-require [r]
  `(try
     (require '[~r])
     '[~r]
     (catch java.io.FileNotFoundException e#)))

(defmacro require-from-config [path]
  `(do
     ~@(for [i# (concat (all-namespaces (duct/read-config (io/resource path)))
                        (all-namespaces (duct/build-config (duct/read-config (io/resource path)))))]
         `(try-require ~i#))))
