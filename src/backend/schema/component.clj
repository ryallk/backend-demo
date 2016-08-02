(ns backend.schema.component
  (:require
    [datomic.api :as d]
    [com.stuartsierra.component :as component]))

(defrecord DatomicSchema [schema]
  component/Lifecycle
  (start [component]
    (let [conn (get-in component [:datomic :conn])]
      @(d/transact-async conn schema)))
  (stop [component]))

(defn transact-datomic-schema [schema]
  (map->DatomicSchema {:schema schema}))

(defrecord DatomicData [data]
  component/Lifecycle
  (start [component]
    (let [conn (get-in component [:datomic :conn])]
      @(d/transact-async conn data)))
  (stop [component]))

(defn transact-datomic-data [data]
  (map->DatomicData {:data data}))
