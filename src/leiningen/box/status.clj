(ns leiningen.box.status
  (:require [leiningen.box.providers.virtualbox :refer (vm-info)]))

(defn status
  "Get the virtual machine's status."
  [project]
  (println (vm-info "VMState")))
