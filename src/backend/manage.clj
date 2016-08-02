(ns backend.manage
  (:require [datomic.api :as d]))

(defn create [{:keys [datomic]} contact]
  (let [temp-id (d/tempid :db.part/user)]
    (as-> [(assoc contact :db/id temp-id)] $
          (deref (d/transact (:conn datomic) $))
          (d/resolve-tempid (:db-after $) (:tempids $) temp-id))))

(defn delete [{:keys [datomic]} eid]
  (dorun
    @(d/transact (:conn datomic) [[:db.fn/retractEntity eid]])))


