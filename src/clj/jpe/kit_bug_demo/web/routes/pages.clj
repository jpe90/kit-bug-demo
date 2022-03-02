(ns jpe.kit-bug-demo.web.routes.pages
  (:require
   [jpe.kit-bug-demo.web.middleware.exception :as exception]
   [jpe.kit-bug-demo.web.pages.layout :as layout]
   [jpe.kit-bug-demo.web.routes.utils :as utils]   
   [integrant.core :as ig]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]
   [ring.middleware.anti-forgery :refer [wrap-anti-forgery]]
   [ring.util.http-response :as http-response]
   [clojure.tools.logging :as log]))

(defn wrap-page-defaults []
  (let [error-page (layout/error-page
                    {:status 403
                     :title "Invalid anti-forgery token"})]
    #(wrap-anti-forgery % {:error-response error-page})))

(defn home [request]
  (layout/render request "home.html"))

(defn authenticate-user [query-fn email password]
  (let [{user-password :password :as user} (query-fn :get-user-for-auth* {:email email})]
    (when (= password user-password)
      (dissoc user :password))))

(defn login [{{:strs [email password]}         :form-params
              session                          :session
              :as request}]
  (let [{:keys [query-fn]} (utils/route-data request)]
    (log/debug "current session: " session)
    (if-some [user (authenticate-user query-fn email password)]
      (do
        (log/debug "login found: " user)
        (->
         (http-response/found "/")
         ;; using this line instead will remove the exception
         ;; (assoc :session (assoc session :identity (dissoc user :timestamp)))
         (assoc :session (assoc session :identity user))
         ))
      (do
        (log/debug "login not found")
        (tap> session)
        (->
         (http-response/found "/"))))))

;; Routes
(defn page-routes [_opts]
  [["/" {:get home
         :post login}]])

(defn route-data [opts]
  (merge
   opts
   {:middleware 
    [;; Default middleware for pages
     (wrap-page-defaults)
     ;; query-params & form-params
     parameters/parameters-middleware
     ;; encoding response body
     muuntaja/format-response-middleware
     ;; exception handling
     exception/wrap-exception]}))

(derive :reitit.routes/pages :reitit/routes)

(defmethod ig/init-key :reitit.routes/pages
  [_ {:keys [base-path]
      :or   {base-path ""}
      :as   opts}]
  (layout/init-selmer!)
  [base-path (route-data opts) (page-routes opts)])

