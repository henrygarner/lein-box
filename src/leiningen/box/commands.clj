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

(defn destroy
  "Destroy the virtual machine."
  [project]
  (let [datastore ".vagrant"
        uuid (get-uuid datastore)]
    (if uuid
      (let [a (destroy-vm-cmd uuid)
            b (:out (apply sh a))
            c (set-uuid datastore nil)]
        (println "Destroyed " uuid))
      (println "No machine to destroy"))))
