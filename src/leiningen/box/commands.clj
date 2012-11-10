(ns leiningen.box.commands
  (:require [leiningen.box.providers.virtualbox :refer :all]
            [clj-shell.shell :refer (sh)]))

(defn start
  "Start the virtual machine."
  [project]
  (let [box (-> project :box :name)
        name (project :name)
        a (vm-import-cmd box)
        b (:out (apply sh a))
        c true ; TODO determine successful import
        d (list-vms-cmd)
        e (:out (apply sh d))
        f (machine-uuid e box)
        g true ; TODO determine successful uuid
        h (set-name-cmd f name)
        i (:out (apply sh h))
        j true ; TODO determine successful set-name
        ]
    (println "Done")))

(defn stop
  "Stop the virtual machine."
  [project]
  (println (vm-stop)))

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
