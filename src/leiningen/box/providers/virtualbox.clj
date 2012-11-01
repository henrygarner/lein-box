(ns leiningen.box.providers.virtualbox
  (:require [leiningen.box.datastore :as store]
            [clj-shell.shell :refer (sh)]
            [clojure.string :as s]))

(defn- line->pair [line]
  (let [[key value] (s/split line #"=" 2)]
    [key (s/replace value #"^\"|\"$" "")]))

(defn- info->map [input]
  (->> (s/split-lines input)
       (map line->pair)
       (into {})))

(defn vm-info
  ([] (info->map (:out (sh "VBoxManage" "showvminfo" (store/active-default) "--machinereadable"))))
  ([key] (-> (vm-info) (get key))))

(defn vm-start []
  (let [result (sh "VBoxManage" "startvm" (store/active-default) "--type" "headless")]
    (or (= (:exit result) 0)
        (re-find #"VM \".+?\" has been successfully started" (:out result)))))

(defn vm-stop []
  (-> (sh "VBoxManage" "controlvm" (store/active-default) "savestate")
      :exit
      (= 0)))

(defn vm-destroy []
  (sh "VBoxManage" "unregistervm" (store/active-default) "--delete"))

(defn vm-directory []
  ; TODO: dynamically determine this directory
  "/Users/henry/.vagrant.d/boxes/")

(defn vm-import [name]
  ; TODO: return the machine id so that can be named
  ; Suggested VM name (.+?)
  ; name {(.+?)}
  (sh "VBoxManage" "import" (str (vm-directory) name "/box.ovf")))

(defn vm-forwarded-ports []
  (let [forwarding (filter #(.startsWith (first %) "Forwarding") (vm-info))]
    (map #(map (s/split (last %) #",") [0 3 5]) forwarding)))

(defn vm-clear-forwarded-ports []
  (let [port-names (map first (vm-forwarded-ports))
        ; TODO: figure out how to reliably determine nat
        args (mapcat #(["--natpf1" "delete" %]) port-names)]
    (apply sh "VBoxManage" "modifyvm" (store/active-default) args)))

(defn vm-set-name [name]
  (sh "VBoxManager" "modifyvm" (store/active-default) "--name" name))

(defn forward-ports [ports]
  (comment "VBoxManage modifyvm uuid --natpf1", "ssh,tcp,,2222,,22"))

(defn check-accessible []
  (not= (:state (vm-info))) "inaccessible")

(defn clean-machine-folder []
  (comment "VirtualBox bug means .xml-prev files may be left over"))

(defn clear-forwarded-ports [])
