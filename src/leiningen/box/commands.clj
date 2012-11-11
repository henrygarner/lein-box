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
        c (list-vms-cmd)
        d (:out (apply sh c))
        e (machine-uuid d box)
        f (persist-uuid-cmd e)
        g (:out (apply sh f))
        h (set-name-cmd e name)
        i (:out (apply sh h))
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
