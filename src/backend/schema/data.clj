(ns backend.schema.data
  (:require
    [clojure.string :as s]
    [datomic.api :as d]
    [faker.company :as fc]
    [faker.name :as fn]
    [faker.address :as fa]))

(defn gen-contact []
  (hash-map
    :db/id (d/tempid :db.part/user)
    :contact/name (first (fn/names))
    :contact/location (fa/city)
    :contact/description (str (fc/catch-phrase) ", " (fc/bs))
    :contact/company (first (fc/names))))

(defn gen-contacts [n]
  (vec (repeatedly n gen-contact)))
