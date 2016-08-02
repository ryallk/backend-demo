(ns backend.handler
  (:require
    [backend.api]

    [environ.core :refer [env]]
    [ring.util.response :as resp]
    [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
    [ring.middleware.cors :refer [wrap-cors]]
    [castra.middleware :as castra :refer [wrap-castra clj->json json->clj]]

    [debux.core :as dx]))

(def frontend-url "http://localhost:8000")

(defn ring-handler [_conn]
  (->
    (resp/not-found "not found")
    (wrap-defaults api-defaults)
    (wrap-castra 'backend.api)
    (wrap-cors (re-pattern (str "^" frontend-url "/?$")))))
