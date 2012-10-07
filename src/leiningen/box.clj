(ns leiningen.box
  (:use [leiningen.help :only (help-for)]
        [leiningen.box.start :only (start)]
        [leiningen.box.stop :only (stop)]
        [leiningen.box.status :only (status)]
        [leiningen.box.ssh :only (ssh)]))

(defn box
  "Manage the project's virtual machine."
  {:help-arglists '([start stop status ssh])
   :subtasks [#'start #'stop #'status #'ssh]}
  ([project]
     (println (help-for project "box")))
  ([project subtask & args]
     (case subtask
       "start"  (apply start project args)
       "stop"   (apply stop project args)
       "status" (apply status project args)
       "ssh"    (apply ssh project args))))
