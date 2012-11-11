(defproject lein-box "0.1.0-SNAPSHOT"
  :description "A plugin for managing a project-specific virtual machine."
  :url "https://github.com/henrygarner/lein-box"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [org.clojure/algo.monads "0.1.0"]
                 [clj-shell "0.2.0"]
                 [cheshire "4.0.4"]]
  :eval-in-leiningen true)