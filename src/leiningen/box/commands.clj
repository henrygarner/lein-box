(ns leiningen.box.commands
  (:require [leiningen.box.providers.virtualbox :refer (vm-info vm-start)]))

(defn start
  "Start the virtual machine."
  [project]
  (println "Starting virtual machine..."))

(defn stop
  "Stop the virtual machine."
  [project]
  (println "Stopping virtual machine..."))

(defn resume
  "Resume the virtual machine."
  [project]
  (println (vm-start)))

(defn status
  "Get the virtual machine's status."
  [project]
  (println (vm-info "VMState")))

(defn ssh
  "Shell into the virtual machine."
  [project]
  (println "Shelling into virtual machine..."))
