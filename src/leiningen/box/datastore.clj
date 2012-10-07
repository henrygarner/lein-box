(ns leiningen.box.datastore
  (:require [cheshire.core :refer (parse-string)]))

(def file-path ".vagrant")

(defn load-dotfile []
  (parse-string (slurp file-path) :as-keywords))

(defn active-default []
  (-> (load-dotfile) :active :default))
