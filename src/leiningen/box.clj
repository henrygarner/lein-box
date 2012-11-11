(ns leiningen.box
  (:use [leiningen.help :only (help-for)]
        [leiningen.box.commands :refer :all]))

(defn box
  "Manage the project's virtual machine."
  {:help-arglists '([start])
   :subtasks [#'start]}
  ([project]
     (println (help-for project "box")))
  ([project subtask & args]
     (case subtask
       "start"   (apply start project args)
       "destroy" (apply destroy project args))))
