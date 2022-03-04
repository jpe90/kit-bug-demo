(ns jpe.kit-bug-demo.web.middleware.core
  (:require
   [ring.middleware.session.cookie :as cookie]
   [ring.middleware.session :as session]
   [ring.middleware.cookies :as cookies]
   [tick.core :as tick]))

(defn wrap-base
  [{:keys [cookie-session-config]}]
  (let [{:keys [cookie-secret cookie-name cookie-default-max-age]} cookie-session-config
        cookie-store (cookie/cookie-store {:key     (.getBytes ^String cookie-secret)
                                           :readers {'inst                 (fn [x]
                                                                             (tick/inst x))
                                                     'time/zoned-date-time #'tick/zoned-date-time}})]
    (fn [handler]
           (cond-> handler
             true (session/wrap-session {:store        cookie-store
                                         :cookie-name  cookie-name
                                         :cookie-attrs {:max-age cookie-default-max-age}})
             true (cookies/wrap-cookies)))))
