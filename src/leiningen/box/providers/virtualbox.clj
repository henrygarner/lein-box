(ns leiningen.box.providers.virtualbox
  (:require [leiningen.box.datastore :as store]
            [clj-shell.shell :refer (sh)]
            [clojure.string :as s]
            [cheshire.core :refer :all]
            [clojure.algo.monads :refer :all]))

(defn- line->pair [line]
  (let [[key value] (s/split line #"=" 2)]
    [key (s/replace value #"^\"|\"$" "")]))

(defn info->map [input]
  (->> (s/split-lines input)
       (map line->pair)
       (into {})))

(defn vm-show-info []
  ["VBoxManage" "showvminfo" "602c73f1-002e-48b6-a73a-7e5fc0d3d7b1" "--machinereadable"])

(defn vm-info
  ([] (info->map (:out (sh "VBoxManage" "showvminfo" (store/active-default) "--machinereadable"))))
  ([key] (-> (vm-info) (get key))))

(defn vm-start [project]
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

(defn vm-import-cmd [name]
  ["VBoxManage" "import" (str (vm-directory) name "/box.ovf")])

(defn vm-import-resp [input]
  (comment "Check last line reads: Successfully imported the appliance."))

(defn machine-uuid [input name]
  (let [re (re-pattern (str "\"" name "\" \\{(.+?)\\}"))]
    (second (re-find re input))))

(defn vm-forwarded-ports []
  (let [forwarding (filter #(.startsWith (first %) "Forwarding") (vm-info))]
    (map #(map (s/split (last %) #",") [0 3 5]) forwarding)))

(defn vm-clear-forwarded-ports []
  (let [port-names (map first (vm-forwarded-ports))
        ; TODO: figure out how to reliably determine nat
        args (mapcat #(["--natpf1" "delete" %]) port-names)]
    (apply sh "VBoxManage" "modifyvm" (store/active-default) args)))

(defn set-name-cmd [uuid value]
  ["VBoxManage" "modifyvm" uuid  "--name" value])

(defn set-mac-cmd [uuid value]
  ["VBoxManage" "modifyvm" uuid  "--macaddress1" value])

(defn default-name []
  (str (System/getProperty "user.dir") "-" (rand-int 1000000)))

(defn list-vms-cmd []
  ["VBoxManage" "list" "vms"])

(defn persist-uuid-cmd [uuid]
  (let [json (generate-string {"active" {"default" uuid}})
        escape (s/replace json "\"" "\\\"")]
    ["bash" "-c" (str "echo " escape " > .vagrant")]))

(defn set-uuid [datastore uuid]
  (spit datastore (generate-string {"active" {"default" uuid}})))

(defn get-uuid [datastore]
  (try (get-in (parse-string (slurp datastore)) ["active" "default"])
       (catch java.io.FileNotFoundException e nil)))

(defn forward-ports [ports]
  (comment "VBoxManage modifyvm uuid --natpf1", "ssh,tcp,,2222,,22"))

(defn check-accessible []
  (not= (:state (vm-info))) "inaccessible")

(defn clean-machine-folder []
  (comment "VirtualBox bug means .xml-prev files may be left over"))

(defn clear-forwarded-ports [])
