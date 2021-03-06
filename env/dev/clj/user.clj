(ns user
  "Userspace functions you can run by default in your local REPL."
  (:require
    [clojure.pprint]
    [clojure.spec.alpha :as s]
    [clojure.tools.namespace.repl :as repl]
    [criterium.core :as c]                                  ;; benchmarking
    [expound.alpha :as expound]
    [integrant.core :as ig]
    [integrant.repl :refer [clear go halt prep init reset reset-all]]
    [integrant.repl.state :as state]
    [jpe.kit-bug-demo.web.routes.pages :as pages]
    [kit.api :as kit]
  #_[lambdaisland.classpath.watch-deps :as watch-deps] ;; hot loading for deps
    [jpe.kit-bug-demo.core :refer [start-app]]))

;; uncomment to enable hot loading for deps
#_(watch-deps/start! {:aliases [:dev :test]})

(alter-var-root #'s/*explain-out* (constantly expound/printer))

(add-tap (bound-fn* clojure.pprint/pprint))

(def query-fn (:db.sql/query-fn state/system))
(comment
  (pages/authenticate-user query-fn "a@b.com" "pass"))

(defn dev-prep!
  []
  (integrant.repl/set-prep! (fn []
                              (-> (jpe.kit-bug-demo.config/system-config {:profile :dev})
                                  (ig/prep)))))

(defn test-prep!
  []
  (integrant.repl/set-prep! (fn []
                              (-> (jpe.kit-bug-demo.config/system-config {:profile :test})
                                  (ig/prep)))))

;; Can change this to test-prep! if want to run tests as the test profile in your repl
;; You can run tests in the dev profile, too, but there are some differences between
;; the two profiles.
(dev-prep!)

(repl/set-refresh-dirs "src/clj")

(def refresh repl/refresh)

(comment
  (go)
  (reset))
