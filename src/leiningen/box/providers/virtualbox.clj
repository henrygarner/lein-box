(ns leiningen.box.providers.virtualbox
  (:require [leiningen.box.datastore :as store]
            [clj-shell.shell :refer (sh)]
            [clojure.string :as s]))

(defn- line->pair [line]
  (let [[key value] (s/split line #"=" 2)]
    [key (s/replace value #"^\"|\"$" "")]))

(defn info->map [input]
  (->> (s/split-lines input)
       (map line->pair)
       (into {})))

(defn vm-info
  ([] (:out (sh "VBoxManage" "showvminfo" (store/active-default) "--machinereadable")))
  ([key] (-> (vm-info) info->map (get key))))
