(ns backend.search
  (:require
    [datomic.api :as d]))

(defprotocol IContactSearch
  (text-search [self text]))

(defrecord ContactSearch [datomic _]
  IContactSearch

  (text-search [_self text]
    (d/q '[:find [(pull ?e [*]) ...]
           :in $ ?input
           :where
           (or [(fulltext $ :contact/name ?input) [[?e]]]
               [(fulltext $ :contact/description ?input) [[?e]]])]
         (d/db (:conn datomic))
         text)))

(defn new-contact-search []
  (map->ContactSearch {}))

(defn search [{:keys [search]} text]
  (text-search search text))

