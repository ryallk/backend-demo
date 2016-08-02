(ns backend.sys
  (:require
    [backend.handler :refer [ring-handler]]
    [backend.search :refer [new-contact-search]]

    [backend.schema.component :refer [transact-datomic-schema transact-datomic-data]]
    [backend.schema.contact :refer [contact-schema]]
    [backend.schema.data :refer [gen-contacts]]

    (system.components
      [datomic :refer [new-datomic-db]]
      [jetty :refer [new-web-server]])
    [system.repl :refer [start set-init!]]
    [datomic.api :as d]
    [datomock.core :as dm]
    [com.stuartsierra.component :as component]
    [environ.core :refer [env]]))

(defrecord RingHandler [datomic handler]
  component/Lifecycle
  (start [component]
    (assoc component :handler (ring-handler (get datomic :conn))))
  (stop [component]))

(defn new-ring-handler []
  (map->RingHandler {}))

(def seed-data (gen-contacts 10000))

(defn dev []
  (component/system-map
    :datomic (new-datomic-db (str "datomic:mem://" (d/squuid)))
    :schema (component/using
              (transact-datomic-schema contact-schema)
              [:datomic])
    :seed-data (component/using
                 (transact-datomic-data seed-data)
                 [:datomic :schema])
    :search (component/using
              (new-contact-search)
              [:datomic])
    :handler (component/using (new-ring-handler) [:datomic])
    :web (component/using
           (new-web-server 3000)
           [:handler])))
