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
