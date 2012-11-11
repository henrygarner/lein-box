(ns leiningen.test.box.providers.virtualbox
  (:use clojure.test
        leiningen.box.providers.virtualbox))

(defn- slurp-resource [name]
  (slurp (clojure.java.io/resource name)))

(deftest virtualbox-info
  (let [name (-> (slurp-resource "test/showvminfo")
                 info->map
                 (get "name"))]
    (is (= name "paduka_1347891541"))))

(deftest test-forwarded-ports
  (let [ports (-> (slurp-resource "test/showvminfo")
                  info->map
                  forwarded-ports)]
    (is (= ports [["ssh" "2222" "22"] ["2to-2to" "3000" "3000"]]))))

(deftest imported-uuid
  (let [uuid (-> (slurp-resource "test/list-vms")
                 (machine-uuid "lucid64"))]
    (is (= uuid "c334df61-aafe-4d66-8ab2-94942738ca65"))))

(deftest set-name
  (let [uuid "c334df61-aafe-4d66-8ab2-94942738ca65"
        name "project_134789154"
        cmd  (set-name-cmd uuid name)]
    (is (= cmd ["VBoxManage" "modifyvm" uuid "--name" name]))))

(deftest list-machines
  (let [cmd (list-vms-cmd)]
    (is (= cmd ["VBoxManage" "list" "vms"]))))

(deftest test-destroy-vm-cmd
  (let [uuid "c334df61-aafe-4d66-8ab2-94942738ca65"
        cmd (destroy-vm-cmd uuid)]
    (is (= cmd ["VBoxManage" "unregistervm" uuid "--delete"]))))
