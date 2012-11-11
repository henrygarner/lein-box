(ns leiningen.box.commands
  (:require [leiningen.box.providers.virtualbox :refer :all]
            [clj-shell.shell :refer (sh)]))

(defn start
  "Start the virtual machine."
  [project]
  (let [box (-> project :box :name)
        name (project :name)
        datastore ".vagrant"
        uuid (get-uuid datastore)
        ]
    (if uuid (println "Found " uuid)
        (let [
              a (vm-import-cmd box)
              b (:out (apply sh a))
              c (list-vms-cmd)
              d (:out (apply sh c))
              e (machine-uuid d box)
              f (set-uuid datastore e)
              g (set-name-cmd e name)
              h (:out (apply sh g))
              ]
          (println "Created " e))
        )))

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
